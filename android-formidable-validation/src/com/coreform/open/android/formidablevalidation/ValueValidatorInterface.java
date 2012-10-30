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
 * Generic interface for validation of 
 * Android input fields
 * 
 * based off: http://dekwant.eu/2010/10/04/android-form-field-validation/
 * @author http://nl.linkedin.com/in/marcdekwant
 *
 */
public interface ValueValidatorInterface {
	
	/**
	 * @return true if enabled
	 */
	boolean isEnabled();
	
	/**
	 * @return true if required
	 */
	boolean isRequired();
	
	/**
	 * Message to be displayed when the required
	 * validation is not met.
	 * @param message the error message
	 */
	void setRequiredMessage(String message);
	
	void setEnabled(boolean enabled);
	
	void setRequired(boolean required);
	
	/**
	 * Set the the source object
	 * that is under validation. The actual
	 * implementation of the validator is 
	 * responsible for the correctness of the
	 * Object.
	 * 
	 * @return the source object 
	 */
	void setSource(Object source);
	
	/**
	 * Resource ID of source object that is under validation.
	 * @param the source object's Resource ID
	 */
	void setSourceResID(int sourceResID);
	
	/**
	 * This method return the source object
	 * that is under validation. The actual
	 * implementation of the validator is 
	 * responsible for the correctness of the
	 * Object.
	 * 
	 * @return the source object 
	 */
	Object getSource();
	
	/**
	 * Returns the resource ID of source object that is under validation.
	 * @return the source object's Resource ID
	 */
	int getSourceResID();
	
	/**
	 * Returns the expression used to validate the source's value.
	 * Return some default message if the implementing class does not have a String expression.
	 * @return the validation expression
	 */
	String getExpression();
	
	/**
	 * This method is called when an input field is to be validated
	 * using the validator
	 * 
	 * @return an validation result object
	 */
	ValueValidationResult validateValue();
	
	/**
	 * Set a message that will be displayed when
	 * the validation requirements are NOT met.
	 * 
	 * @param message the fault message to be displayed
	 */
	void setFaultMessage(String message);

}
