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

import java.util.ArrayList;
import java.util.List;

/**
 * Generic abstract validation class for the 
 * validation of Android input fields
 * 
 * based off: http://dekwant.eu/2010/10/04/android-form-field-validation/
 * 
 * customised and enhanced by:
 * @author Linden Darling (contact@coreform.com.au)
 *
 */
public abstract class AbstractValueValidator implements ValueValidatorInterface {
	private static final boolean DEBUG = true;
	private static final String TAG = "AbstractValueValidator";
	
	private Object mSource;
	private int mSourceResID;
	private boolean mRequired = false;
	private boolean mEnabled = true;
	
	protected String mFaultMessage = "Validation failure";
	protected String mRequiredMessage = "The field is required";
	
	/** CONSTRUCTORS */
	
	public AbstractValueValidator() {}
	
	public AbstractValueValidator(boolean required) {
		mRequired = required;
	}

	public AbstractValueValidator(boolean required, boolean enabled) {
		mRequired = required;
		mEnabled = enabled;
	}

	public ValueValidationResult validateValue() {
		ValueValidationResult _vr = null;
		if (mEnabled) {
			if (mRequired && getSource() == null) {
				_vr = new ValueValidationResult(mSource, false, mRequiredMessage);
			} else {
				_vr = new ValueValidationResult(mSource, true, "");
			}
		} else {
			_vr = new ValueValidationResult(mSource, true, "");
		}
		return _vr;
	}
	
	/**
	 * THis is a convenience method that enables you
	 * to quickly validate all validators.
	 * 
	 * @param validators an array of validators
	 * @return an array of validation failure results.
	 */
	public static List<ValueValidationResult> validateAll(List<ValueValidatorInterface> validators) {
		List<ValueValidationResult> _result = new ArrayList<ValueValidationResult>();
		ValueValidationResult _vr = null;
		for (ValueValidatorInterface v : validators) {
			_vr = v.validateValue();
			if (!_vr.isValid()) {
				_result.add(_vr);
			}
		}
		return _result;
	}
	
	public abstract void setSource(Object source);
	
	public abstract Object getSource();
	
	public void setSourceResID(int sourceResID) {
		mSourceResID = sourceResID;
	}
	public int getSourceResID() {
		return mSourceResID;
	}
	
	public void setFaultMessage(String message) {
		mFaultMessage = message;
	}
	
	public void setRequiredMessage(String message) {
		mRequiredMessage = message;
	}

	public void setEnabled(boolean enabled) {
		mEnabled = enabled;
	}

	public void setRequired(boolean required) {
		mRequired = required;
	}
	
	public boolean isEnabled() {
		return mEnabled;
	}

	public boolean isRequired() {
		return mRequired;
	}

}
