package com.coreform.open.android.formidablevalidation;

/**
 * DependencyValidationResult class
 * value holder.
 * this class contains true/false (validation status)
 * and a message.
 * 
 * based off: http://dekwant.eu/2010/10/04/android-form-field-validation/
 * 
 * @author Linden Darling (contact@coreform.com.au)
 *
 */
public class DependencyValidationResult {
	private static final boolean DEBUG = true;
	private static final String TAG = "DependencyValidationResult";

	private Object mSource;
	private boolean mValid = false;
	private String mMessage = "";
	
	public DependencyValidationResult(boolean ok, String message) {
		mValid = ok;
		mMessage = message;
	}
	
	public DependencyValidationResult(Object source, boolean ok, String message) {
		mSource = source;
		mValid = ok;
		mMessage = message;
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
