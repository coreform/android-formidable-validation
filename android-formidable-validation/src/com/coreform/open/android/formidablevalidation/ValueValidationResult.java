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

/**
 * ValueValidationResult class
 * value holder.
 * this class contains true/false (validation status)
 * and a message.
 * 
 * based off: http://dekwant.eu/2010/10/04/android-form-field-validation/
 * 
 * customised and enhanced by:
 * @author Linden Darling (contact@coreform.com.au)
 *
 */
public class ValueValidationResult {
	private static final boolean DEBUG = true;
	private static final String TAG = "ValueValidationResult";
	
	private Object mSource; 	//clunky way of passing the source from Validator through to end-handler to be used for, say, .setError()
	private boolean mActive = false;	//true indicates this field is activated (checked, populated, etc), usually this is true when mValueValid is true, indicating dependent fields need to also be valid
	private boolean mValid = false;	//high level validity, 
	private String mMessage = "";
	
	public ValueValidationResult(boolean ok, String message) {
		mValid = ok;
		mMessage = message;
	}
	
	public ValueValidationResult(Object source, boolean ok, String message) {
		mSource = source;
		mValid = ok;
		mMessage = message;
	}
	
	public ValueValidationResult(Object source, boolean ok) {
		mSource = source;
		mValid = ok;
	}
	
	public boolean isValid() {
		return mValid;
	}
	
	public String getMessage() {
		return mMessage;
	}
	
	public Object getSource() {
		return mSource;
	}
}
