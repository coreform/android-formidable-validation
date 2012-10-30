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

package com.coreform.open.android.formidablevalidation.example;

import android.location.Address;
import android.util.Log;
import android.widget.Button;

import com.coreform.open.android.formidablevalidation.AbstractValueValidator;
import com.coreform.open.android.formidablevalidation.ValueValidationResult;

public class ColourPickerButtonValueValidator extends AbstractValueValidator {

	private Button mSource;
	private int mSourceResID;
	private String mFaultNoStringSourceMessage = "The field is not a valid String object";
	
	public ColourPickerButtonValueValidator(Button source, boolean required) {
		super(required);
		mSource = source;
		mRequiredMessage = "Please select a colour.";	//default
	}
	
	public ColourPickerButtonValueValidator(Button source, boolean required, String faultMessage) {
		super(required);
		mSource = source;
		mRequiredMessage = faultMessage;
	}

	@Override
	public ValueValidationResult validateValue() {
		ValueValidationResult _v = super.validateValue();
		if (true || _v==null) {
			if ("".equals(mSource.getText())) {
				_v = new ValueValidationResult(mSource, true, "");
			} else {
				_v = new ValueValidationResult(mSource, false, mRequiredMessage);
			}
		}
		return _v;
	}
	
	@Override
	public String getExpression() {
		return "This is a ColourPickerButtonValueValidator (no String expression).";
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
