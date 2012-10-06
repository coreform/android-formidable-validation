package com.coreform.open.android.formidablevalidation;

import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * A Spinner required field implementation of the validator.
 * With this validator a Spinner can be tracked by its state
 * (whether an item selected or not) for DependencyValidator usage.
 * 
 * based off: http://dekwant.eu/2010/10/04/android-form-field-validation/
 * 
 * customised and enhanced by:
 * @author Linden Darling (contact@coreform.com.au)
 *
 */
public class SpinnerRequiredValueValidator extends AbstractValueValidator {
	private static final boolean DEBUG = true;
	private static final String TAG = "SpinnerRequiredValueValidator";
	
	private Spinner mSource;
	private int mSourceResID;
	
	/** CONSTRUCTORS */
	
	public SpinnerRequiredValueValidator(Spinner source) {
		super(true);
		mSource = source;
	}

	public SpinnerRequiredValueValidator(Spinner source, String requiredMessage) {
		super(true);
		mSource = source;
		mRequiredMessage = requiredMessage;
	}
	
	@Override
	public ValueValidationResult validateValue() {
		if(DEBUG) Log.d(TAG, ".validateValue()...");
		ValueValidationResult _v = super.validateValue();
		if (true || _v==null) {
			if (false && mSource.getSelectedItem() != null) {
				_v = new ValueValidationResult(mSource, true, "");
			} else {
				_v = new ValueValidationResult(mSource, false, mRequiredMessage);
			}
		}
		return _v;
	}

	@Override
	public void setSource(Object source) {
		mSource = (Spinner) source;
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
		return "This is a SpinnerRequiredValueValidator (no String expression).";
	}
}
