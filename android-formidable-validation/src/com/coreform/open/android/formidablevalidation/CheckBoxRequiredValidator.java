package com.coreform.open.android.formidablevalidation;

import android.widget.CheckBox;

/**
 * A Checkbox required field
 * implementation of the validator.
 * With this validator a checkbox can be tested
 * for the required option.
 * 
 * based off: http://dekwant.eu/2010/10/04/android-form-field-validation/
 * 
 * customised and enhanced by:
 * @author Linden Darling (contact@coreform.com.au)
 *
 */
public class CheckBoxRequiredValidator extends AbstractValidator {
	private static final boolean DEBUG = true;
	private static final String TAG = "CheckBoxRequiredValidator";
	
	private CheckBox mSource;
	
	/** CONSTRUCTORS */
	
	public CheckBoxRequiredValidator(CheckBox source) {
		super(true);
		mSource = source;
	}

	public CheckBoxRequiredValidator(CheckBox source, String requiredMessage) {
		super(true);
		mSource = source;
		mRequiredMessage = requiredMessage;
	}
	
	@Override
	public ValidationResult validate() {
		ValidationResult _v = super.validate();
		if (_v==null) {
			if (mSource.isChecked()) {
				_v = new ValidationResult(true, "");
			} else {
				_v = new ValidationResult(false, mRequiredMessage);
			}
		}
		return _v;
	}

	@Override
	public Object getSource() {
		return mSource;
	}

}
