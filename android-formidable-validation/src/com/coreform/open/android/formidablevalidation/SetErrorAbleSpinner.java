package com.coreform.open.android.formidablevalidation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Spinner;

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
}
