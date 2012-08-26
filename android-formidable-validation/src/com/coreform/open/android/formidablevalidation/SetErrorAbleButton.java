package com.coreform.open.android.formidablevalidation;


import android.content.Context;
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

}
