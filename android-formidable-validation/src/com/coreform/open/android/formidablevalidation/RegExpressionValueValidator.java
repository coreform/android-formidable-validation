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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.util.Log;
import android.widget.EditText;

/**
 * A regular expression validator
 * implementation of the validator.
 * With this validator EditText can be validated
 * against a regular expression.
 * 
 * based off: http://dekwant.eu/2010/10/04/android-form-field-validation/
 * 
 * debugged, customised, and enhanced by:
 * @author Linden Darling (contact@coreform.com.au)
 *
 */
public class RegExpressionValueValidator extends AbstractValueValidator {
	private static final boolean DEBUG = true;
	private static final String TAG = "RegExpressionValueValidator";
	
	private EditText mSource;
	private int mSourceResID;
	private String mExpression = null;
	private Pattern mPattern = null;
	private String mFaultExpressionMessage = "No valid expression found";
	private String mFaultPatternMessage = "Regex pattern problem";
	private String mFaultNoStringSourceMessage = "The field is not a valid String object";
	
	/** CONSTRUCTORS */

	public RegExpressionValueValidator() {}

	public RegExpressionValueValidator(EditText source, String expression) {
		mSource = source;
		setExpression(expression);
	}
	
	public RegExpressionValueValidator(EditText source, String expression, String faultMessage) {
		mSource = source;
		setExpression(expression);
		mFaultMessage = faultMessage;
	}
	
	public RegExpressionValueValidator(EditText source, String expression, String faultMessage, String faultExpressionMessage) {
		mSource = source;
		setExpression(expression);
		mFaultMessage = faultMessage;
		mFaultExpressionMessage = faultExpressionMessage;
	}

	@Override
	public ValueValidationResult validateValue() {
		if(DEBUG) Log.d(TAG, ".validateValue()...");
		ValueValidationResult _v = super.validateValue();
		if (true || _v == null) {
			if(DEBUG) Log.d(TAG, "...super.validateValue() returned null...");
			// here starts the custom Regexpression validator implementation
			
			if (mSource!=null) {
				if (!(mSource instanceof EditText)) {
					if(DEBUG) Log.d(TAG, "...source is NOT an EditText!");
					_v = new ValueValidationResult(mSource, false, mFaultNoStringSourceMessage);
				} else if (mExpression == null || mExpression.length() == 0 || mPattern == null) {
					if(DEBUG) Log.d(TAG, "...something NOT right!");
					if (mPattern == null) {
						_v = new ValueValidationResult(mSource, false, mFaultPatternMessage);
					} else {
						_v = new ValueValidationResult(mSource, false, mFaultExpressionMessage);
					}
				} else {
					if(DEBUG) Log.d(TAG, "...pattern matching...");
					Matcher _matcher = mPattern.matcher(mSource.getText().toString());
					if (!_matcher.find()) {
						_v = new ValueValidationResult(mSource, false, mFaultMessage);
					} else {
						_v = new ValueValidationResult(mSource, true, "");
					}
				}
			} else if (mSource==null) {
				if(DEBUG) Log.d(TAG, "...source is NULL!");
				// No validation is required...
				_v = new ValueValidationResult(mSource, true, "");
			}
		}
		//_v.setCareValueValidity(mCareValueValidity);
		//_v.setCareInvalidWhenDependentValid(mCareInvalidWhenDependentValid);
		return _v;
	}
	
	/** CUSTOM SETTERS */
	
	public void setExpression(String expression) {
		mExpression = expression;
		try {
		    mPattern = Pattern.compile(mExpression);
		} catch (PatternSyntaxException e) {
			mFaultPatternMessage = e.getMessage();
			mPattern = null;
		}
	}
	
	public void setFaultExpressionMessage(String message) {
		mFaultExpressionMessage = message;
	}
	
	@Override
	public void setSource(Object source) {
		mSource = (EditText) source;
	}
	
	@Override
	public Object getSource() {
		return mSource;
	}
	
	public void setSourceResID(int sourceResID) {
		mSourceResID = sourceResID;
	}
	public int getSourceResID() {
		return mSourceResID;
	}
	
	public String getExpression() {
		return mExpression;
	}

}
