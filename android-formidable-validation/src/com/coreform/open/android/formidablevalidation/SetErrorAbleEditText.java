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


import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class SetErrorAbleEditText extends EditText implements SetErrorAble {
	private Context mContext;
	private SetErrorHandler mSetErrorHandler;
	
	public SetErrorAbleEditText(Context context) {
		super(context);
		mContext = context;
		mSetErrorHandler = new SetErrorHandler(context, this);
		setupListeners();
	}

	//necessary for XML inflation
	public SetErrorAbleEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mSetErrorHandler = new SetErrorHandler(context, this);
		setupListeners();
	}
	
	public SetErrorAbleEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		mSetErrorHandler = new SetErrorHandler(context, this);
		setupListeners();
	}
	
	private void setupListeners() {
		this.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				if (mSetErrorHandler.mError != null) {
					mSetErrorHandler.setError(null, null);
					//mSetErrorHandler.hideError();
		        }
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});
		this.setOnKeyListener(new OnKeyListener() {
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (mSetErrorHandler.mError != null) {
					mSetErrorHandler.setError(null, null);
					//mSetErrorHandler.hideError();
		        }
		        return false;
		    }
		});
	}
	
	@Override
	public void setErrorPopupPadding(int left, int top, int right, int bottom) {
		// TODO Auto-generated method stub
		
	}
	
	//for Views that didnt already have a setError method, simply proxy these methods to setError,
	//otherwise proxy to SetErrorHandler.setError.
	@Override
	public void betterSetError(CharSequence error) {
		Drawable icon = mContext.getResources().getDrawable(R.drawable.indicator_input_error_green);
		mSetErrorHandler.setError(error, icon);
	}
	@Override
	public void betterSetError(CharSequence error, Drawable icon) {
		mSetErrorHandler.setError(error, icon);
	}
	@Override
	public void betterSetError(CharSequence error, boolean showError) {
		Drawable icon = mContext.getResources().getDrawable(R.drawable.indicator_input_error_green);
		mSetErrorHandler.setError(error, icon, showError);
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

}
