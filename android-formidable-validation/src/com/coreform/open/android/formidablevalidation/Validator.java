package com.coreform.open.android.formidablevalidation;

/**
 * Generic interface for validation of 
 * Android input fields
 * 
 * based off: http://dekwant.eu/2010/10/04/android-form-field-validation/
 * @author http://nl.linkedin.com/in/marcdekwant
 *
 */
public interface Validator {
	
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
	 * This method is called when an input field is to be validated
	 * using the validator
	 * 
	 * @return an validation result object
	 */
	ValidationResult validate();
	
	/**
	 * Set a message that will be displayed when
	 * the validation requirements are NOT met.
	 * 
	 * @param message the fault message to be displayed
	 */
	void setFaultMessage(String message);

}
