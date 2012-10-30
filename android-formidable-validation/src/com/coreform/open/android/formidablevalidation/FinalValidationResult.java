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

import android.util.Log;

/**
 * A wrapper class for ValueValidationResult + DependencyValidationResult.
 * Typically values based off the aggregation -or- first instance of ValidationResults
 */
public class FinalValidationResult {
	private static final boolean DEBUG = true;
	private static final String TAG = "FinalValidationResult";
	
	private Object mSource;
	
	private boolean mValueValid = false;
	private boolean mDependencyValid = false;
	//private String mMessage = "";	//let consumer of this object choose between messages as necessary per use
	private String mValueInvalidMessage = "";
	private String mDependencyInvalidMessage = "";
	
	/*
	public FinalValidationResult(boolean valueOk, boolean dependencyOk, String valueInvalidMessage, String dependencyInvalidMessage) {
		mValueValid = valueOk;
		mDependencyValid = dependencyOk;
		mValueInvalidMessage = valueInvalidMessage;
		mDependencyInvalidMessage = dependencyInvalidMessage;
	}
	*/
	
	public FinalValidationResult(Object source, boolean valueOk, boolean dependencyOk, String valueInvalidMessage, String dependencyInvalidMessage) {
		if(source == null) {
			if(DEBUG) Log.d(TAG, "...source is null when instantiating FinalValidationResult!");
		}
		mSource = source;
		mValueValid = valueOk;
		mDependencyValid = dependencyOk;
		mValueInvalidMessage = valueInvalidMessage;
		mDependencyInvalidMessage = dependencyInvalidMessage;
	}
	
	public void setSource(Object source) {
		mSource = source;
	}
	
	public Object getSource() {
		return mSource;
	}
	
	public boolean isValid() {
		return (mValueValid && mDependencyValid);
	}
	
	public boolean isValueValid() {
		return mValueValid;
	}
	
	public boolean isDependencyValid() {
		return mDependencyValid;
	}
	
	public String getValueInvalidMessage() {
		return mValueInvalidMessage;
	}
	
	public String getDependencyInvalidMessage() {
		return mDependencyInvalidMessage;
	}
}
