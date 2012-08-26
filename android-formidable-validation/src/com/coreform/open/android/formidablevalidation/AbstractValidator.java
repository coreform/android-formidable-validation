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
public abstract class AbstractValidator implements Validator {
	private static final boolean DEBUG = true;
	private static final String TAG = "AbstractValidator";
		
	private boolean mRequired = false;
	private boolean mEnabled = true;
	
	protected String mFaultMessage = "Validation failure";
	protected String mRequiredMessage = "The field is required";
	
	/** CONSTRUCTORS */
	
	public AbstractValidator() {}
	
	public AbstractValidator(boolean required) {
		mRequired = required;
	}

	public AbstractValidator(boolean required, boolean enabled) {
		mRequired = required;
		mEnabled = enabled;
	}

	public ValidationResult validate() {
		ValidationResult _vr = null;
		if (mEnabled) {
			if (mRequired && getSource() == null) {
				_vr = new ValidationResult(false, mRequiredMessage);
			}
		} else {
			_vr = new ValidationResult(true, "");
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
	public static List<ValidationResult> validateAll(List<Validator> validators) {
		List<ValidationResult> _result = new ArrayList<ValidationResult>();
		ValidationResult _vr = null;
		for (Validator v : validators) {
			_vr = v.validate();
			if (!_vr.isValid()) {
				_result.add(_vr);
			}
		}
		return _result;
	}
	
	public abstract Object getSource();
		
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
