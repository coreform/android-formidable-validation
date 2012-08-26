package com.coreform.open.android.formidablevalidation;

/**
 * Validation result class
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
public class ValidationResult {
	private static final boolean DEBUG = true;
	private static final String TAG = "ValidationResult";
	
	private boolean mValid = false;
	private String mMessage = "";
	
	public ValidationResult(boolean ok, String message) {
		mValid = ok;
		mMessage = message;
	}
	
	public boolean isValid() {
		return mValid;
	}
	
	public String getMessage() {
		return mMessage;
	}

}
