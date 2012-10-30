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
import java.util.regex.Pattern;

import android.text.Editable;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;

public class CheckBoxCheckedDependencyValidator extends AbstractDependencyValidator {
	private static final boolean DEBUG = true;
	private static final String TAG = "CheckBoxCheckedDependencyValidator";
	
	private Object mSource;
	private int mSourceResID;
	private String mCruxFieldKey;	//the key in ValidationManager's ValueValidatorsMap that corresponds with the Dependent Field
	private CheckBox mCruxFieldSource;
	private int mCruxFieldResID;
	private boolean mCruxFieldRequiredToExist;
	private boolean mCruxFieldRequiredToBeValid;
	private List<ValueValidationResult> mValueValidationResultList;
	private String mCruxFieldNonExistentMessage = "Crux field does not exist";
	private String mCruxFieldInvalidMessage = "Crux field value is invalid";
	private String mEmptyMessage = "A value is necessary here due to a dependency being satisfied.";
	private String mMessage = "Dependency not satisfied";
	
	public CheckBoxCheckedDependencyValidator(Object source, String cruxFieldKey, Object cruxSource, boolean dependentFieldRequiredToExist, boolean dependentFieldRequiredToBeValid, String message) {
		if(DEBUG) Log.d(TAG, "source is null!");	//TODO fix this issue
		mSource = source;
		mCruxFieldKey = cruxFieldKey;
		mCruxFieldSource = (CheckBox) cruxSource;
		mCruxFieldRequiredToExist = dependentFieldRequiredToExist;
		mCruxFieldRequiredToBeValid = dependentFieldRequiredToBeValid;
		mMessage = message;
	}
	
	public void setSource(Object source) {
		mSource = source;
	}

	public void setSourceResID(int sourceResID) {
		mSourceResID = sourceResID;
	}
	
	public void setCruxFieldKey(String dependentFieldKey) {
		mCruxFieldKey = dependentFieldKey;
	}

	public int getSourceResID() {
		return mSourceResID;
	}

	public void setCruxFieldResID(int dependentFieldResID) {
		mCruxFieldResID = dependentFieldResID;
	}

	public int getCruxFieldResID() {
		return mCruxFieldResID;
	}
	
	public String getCruxFieldKey() {
		return mCruxFieldKey;
	}
	
	public void setValueValidationResults(List<ValueValidationResult> valueValidationResultList) {
		mValueValidationResultList = valueValidationResultList;
	}

	public DependencyValidationResult validateDependency(String thisFieldInvalidMessage, List<ValueValidationResult> cruxValueValidationResultList) {
		if(DEBUG) Log.d(TAG, ".validateDependency()...");
		boolean thisFieldIsValid = (thisFieldInvalidMessage == null)? true : false;
		boolean ok = true;	//override default if reason found
		String message = mMessage;
		Object sourceField = null;
		if(mCruxFieldRequiredToBeValid && !mCruxFieldSource.isChecked()) {
			if(DEBUG) Log.d(TAG, "...crux CheckBox required to be valid AND crux CheckBox is NOT checked...");
			ok = false;
			message = mCruxFieldInvalidMessage;
		}
		if(!mCruxFieldRequiredToBeValid && !mCruxFieldSource.isChecked() && ((Editable) ((EditText) mSource).getText()).length() > 0 && !thisFieldIsValid) {
			if(DEBUG) Log.d(TAG, "...crux CheckBox NOT required to be valid AND crux CheckBox is NOT checked AND this field is NOT empty AND this field is INVALID...");
			ok = false;
			message = thisFieldInvalidMessage;
		}
		if(mCruxFieldRequiredToExist && mCruxFieldSource == null) {
			if(DEBUG) Log.d(TAG, "...crux CheckBox required to exist AND crux source is NULL...");
			ok = false;
			message = mCruxFieldNonExistentMessage;
		}
		if(mCruxFieldSource.isChecked() && ((Editable) ((EditText) mSource).getText()).length() == 0) {
			if(DEBUG) Log.d(TAG, "...crux CheckBox is checked AND this field is EMPTY...");
			ok = false;
			message = mMessage;
		} else if(mCruxFieldSource.isChecked() && !thisFieldIsValid) {
			if(DEBUG) Log.d(TAG, "...crux CheckBox is checked AND this field is NOT valid...");
			ok = false;
			message = thisFieldInvalidMessage;
		}
		return new DependencyValidationResult(sourceField, ok, message);
	}

	@Override
	public Object getSource() {
		return mSource;
	}

	@Override
	public Object getCruxFieldSource() {
		return mCruxFieldSource;
	}

}
