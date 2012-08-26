package com.coreform.open.android.formidablevalidation.example;

import android.location.Address;
import android.widget.Button;

import com.coreform.open.android.formidablevalidation.AbstractValueValidator;

public class ColourPickerButtonValueValidator extends AbstractValueValidator {

	private Button mSource;
	private int mSourceResID;
	private String mFaultNoStringSourceMessage = "The field is not a valid String object";
	
	public ColourPickerButtonValueValidator(Button source, boolean required) {
		super(required);
		mSource = source;
		mFaultMessage = "Please select a colour.";	//default
	}
	
	public ColourPickerButtonValueValidator(Button source, boolean required, String faultMessage) {
		super(required);
		mSource = source;
		mFaultMessage = faultMessage;
	}

	@Override
	public String getExpression() {
		//our ColourButton doesn't require a String expression, so just return null
		return null;
	}

	@Override
	public void setSource(Object source) {
		mSource = (Button) source;
	}

	@Override
	public Object getSource() {
		return mSource;
	}
}
