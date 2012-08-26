package com.coreform.open.android.formidablevalidation;

import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * A Checkbox required field
 * implementation of the validator.
 * With this validator a checkbox can be tracked by value, useful for DependencyValidators.
 * The CheckBox isn't required to be checked.
 * 
 * based off: http://dekwant.eu/2010/10/04/android-form-field-validation/
 * 
 * extrapolated by:
 * @author Linden Darling (contact@coreform.com.au)
 *
 */
public class CheckBoxValueValidator extends AbstractValueValidator {
	private static final boolean DEBUG = true;
	private static final String TAG = "CheckBoxValueValidator";
	
	private CheckBox mSource;
	private int mSourceResID;
	
	/** CONSTRUCTORS */
	
	public CheckBoxValueValidator(CheckBox source) {
		super(false);
		mSource = source;
	}

	public CheckBoxValueValidator(CheckBox source, String requiredMessage) {
		super(false);
		mSource = source;
		mRequiredMessage = requiredMessage;
	}
	
	@Override
	public ValueValidationResult validateValue() {
		if(DEBUG) Log.d(TAG, ".validateValue()...");
		ValueValidationResult _v = super.validateValue();
		if (_v==null) {
			if (mSource.isChecked()) {
				_v = new ValueValidationResult(mSource, true, "");
			} else {
				_v = new ValueValidationResult(mSource, false, mRequiredMessage);
			}
		}
		return _v;
	}

	@Override
	public void setSource(Object source) {
		mSource = (CheckBox) source;
	}
	
	@Override
	public Object getSource() {
		return mSource;
	}

	public void setSourceResID(int sourceResID) {
		mSourceResID = sourceResID;
	}
	public int getSourceResID() {
		return mSourceResID;
	}

	public String getExpression() {
		return "This is a CheckBoxValueValidator (no String expression).";
	}
}
