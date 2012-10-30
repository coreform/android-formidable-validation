/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coreform.open.android.formidablevalidation;

import java.util.List;

import com.coreform.open.android.formidablevalidation.example.AddressActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SetErrorAbleSpinner extends Spinner implements SetErrorAble {
	private Context mContext;
	private SetErrorHandler mSetErrorHandler;

	public SetErrorAbleSpinner(Context context) {
		super(context);
		mContext = context;
		mSetErrorHandler = new SetErrorHandler(context, this);
	}

	//necessary for XML inflation
	public SetErrorAbleSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mSetErrorHandler = new SetErrorHandler(context, this);
	}
	
	public SetErrorAbleSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		mSetErrorHandler = new SetErrorHandler(context, this);
	}
	
	public void setError(CharSequence error) {
		mSetErrorHandler.setError(error);
	}
	
	public void setError(CharSequence error, Drawable icon) {
		mSetErrorHandler.setError(error, icon);
	}
	
	public void setErrorPopupPadding(int left, int top, int right, int bottom) {
		mSetErrorHandler.setErrorPopupPadding(left, top, right, bottom);
	}
	
	//for Views that didnt already have a setError method, simply proxy these methods to setError,
	//otherwise proxy to SetErrorHandler.setError.
	@Override
	public void betterSetError(CharSequence error) {
		setError(error);
	}
	@Override
	public void betterSetError(CharSequence error, Drawable icon) {
		setError(error, icon);
	}
	@Override
	public void betterSetError(CharSequence error, boolean showError) {
		mSetErrorHandler.setError(error, showError);
	}
	@Override
	public void betterSetError(CharSequence error, Drawable icon, boolean showError) {
		mSetErrorHandler.setError(error, icon, showError);
	}
	@Override
	public void betterSetError(CharSequence error, Drawable icon, boolean showError, boolean showCompoundDrawableOnRight) {
		mSetErrorHandler.setError(error, icon, showError, showCompoundDrawableOnRight);
	}
	@Override
	public void betterSetError(CharSequence error, boolean showError, boolean showCompoundDrawableOnRight) {
		Drawable icon = mContext.getResources().getDrawable(R.drawable.indicator_input_error_green);
		mSetErrorHandler.setError(error, icon, showError, showCompoundDrawableOnRight);
	}
	
	//mimic the behaviour of native TextView in showing and hiding its ErrorPopup onFocusChanged.
	@Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
		if(focused) {
			if(mSetErrorHandler.mError != null) {
				mSetErrorHandler.showError();
			}
		} else {
			if(mSetErrorHandler.mError != null) {
				mSetErrorHandler.hideError();
			}
		}
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}
	
	@Override
	protected void onDetachedFromWindow() {
		if (mSetErrorHandler.mError != null) {
			mSetErrorHandler.hideError();
        }
		super.onDetachedFromWindow();
	}
	
	//convenience method for setting up an UnselectedSpinnerArrayAdapter
	public void setupUnselectedSpinnerAdapter(int resource, int textViewResourceId, List<String> objects) {
		UnselectedSpinnerAdapter adapter = new UnselectedSpinnerAdapter(mContext, resource, textViewResourceId, objects);
		this.setAdapter(adapter);
	}
	
	public class UnselectedSpinnerAdapter extends ArrayAdapter<String> {
		Context mContext;
		int mTextViewResourceId;
		List<String> mItemTextsList;
		public UnselectedSpinnerAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
			super(context, resource, textViewResourceId, objects);
			mContext = context;
			mTextViewResourceId = textViewResourceId;
			mItemTextsList = objects;
		}
		
		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			if(mSetErrorHandler.mError != null) {
				mSetErrorHandler.hideError();
			}
			return getCustomView(position, convertView, parent, true);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getCustomView(position, convertView, parent, false);
		}
		public View getCustomView(int position, View convertView, ViewGroup parent, boolean hideFirstItem) {
			//return an empty View if position is the first in the list (i.e. the default-prompt-faux-item)
			TextView row = (TextView) convertView;
			if(row == null) {
				row = (TextView) ((Activity) mContext).getLayoutInflater().inflate(mTextViewResourceId, parent, false);
			}
			if(hideFirstItem && position == 0) {
				row.setVisibility(View.GONE);
			} else {
				row.setText(mItemTextsList.get(position));
				row.setVisibility(View.VISIBLE);
			}
			return row;
		}
	}
}
