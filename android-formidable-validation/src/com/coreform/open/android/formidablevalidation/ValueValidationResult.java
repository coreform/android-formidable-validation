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
