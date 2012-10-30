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
import android.util.AttributeSet;
import android.widget.Button;

public class SetErrorAbleButton extends Button implements SetErrorAble {
	private Context mContext;
	private SetErrorHandler mSetErrorHandler;
	
	public SetErrorAbleButton(Context context) {
		super(context);
		mContext = context;
		mSetErrorHandler = new SetErrorHandler(context, this);
	}

	//necessary for XML inflation
	public SetErrorAbleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mSetErrorHandler = new SetErrorHandler(context, this);
	}
	
	public SetErrorAbleButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		mSetErrorHandler = new SetErrorHandler(context, this);
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
	//note: you must set the View's isFocusable and isFocusableInTouchMode to true for this to work.
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
