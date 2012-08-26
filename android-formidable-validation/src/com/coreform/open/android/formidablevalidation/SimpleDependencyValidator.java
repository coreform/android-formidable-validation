package com.coreform.open.android.formidablevalidation;

import java.util.List;
import java.util.regex.Pattern;

import android.util.Log;
import android.widget.EditText;

public class SimpleDependencyValidator extends AbstractDependencyValidator {
	private static final boolean DEBUG = true;
	private static final String TAG = "SimpleDependencyValidator";
	
	private Object mSource;
	private int mSourceResID;
	private String mCruxFieldKey;	//the key in ValidationManager's ValueValidatorsMap that corresponds with the Dependent Field
	private Object mCruxFieldSource;
	private int mCruxFieldResID;
	private boolean mCruxFieldRequiredToExist;
	private boolean mCruxFieldRequiredToBeValid;
	private List<ValueValidationResult> mValueValidationResultList;
	private String mCruxFieldNonExistentMessage = "Crux field does not exist";
	private String mCruxFieldInvalidMessage = "Crux field value is invalid";
	private String mMessage = "Dependency not satisfied";
	
	public SimpleDependencyValidator(Object source, String cruxFieldKey, Object cruxSource, boolean dependentFieldRequiredToExist, boolean dependentFieldRequiredToBeValid) {
		if(DEBUG) Log.d(TAG, "ZOMG source is null!");
		mSource = source;
		mCruxFieldKey = cruxFieldKey;
		mCruxFieldSource = cruxSource;
		mCruxFieldRequiredToExist = dependentFieldRequiredToExist;
		mCruxFieldRequiredToBeValid = dependentFieldRequiredToBeValid;
	}
	
	public void setSource(Object source) {
		mSource = source;
	}

	public void setSourceResID(int sourceResID) {
		mSourceResID = sourceResID;
	}
	
	public void setCruxFieldKey(String dependentFieldKey) {
		mCruxFieldKey = dependentFieldKey;
	}

	public int getSourceResID() {
		return mSourceResID;
	}

	public void setCruxFieldResID(int dependentFieldResID) {
		mCruxFieldResID = dependentFieldResID;
	}

	public int getCruxFieldResID() {
		return mCruxFieldResID;
	}
	
	public String getCruxFieldKey() {
		return mCruxFieldKey;
	}
	
	public void setValueValidationResults(List<ValueValidationResult> valueValidationResultList) {
		mValueValidationResultList = valueValidationResultList;
	}

	public DependencyValidationResult validateDependency(String thisFieldInvalidMessage, List<ValueValidationResult> cruxValueValidationResultList) {
		if(DEBUG) Log.d(TAG, ".validateDependency()...");
		boolean thisFieldIsValid = (thisFieldInvalidMessage == null)? true : false;
		boolean ok = true;	//below we'll override default if reason found
		String message = mMessage;
		Object sourceField = null;
		if(mCruxFieldRequiredToBeValid && !cruxValueValidationResultList.isEmpty()) {
			if(DEBUG) Log.d(TAG, "...crux required to be valid AND crux is INVALID...");
			//check validity of any ValueValidations for crux field with careInvalidWhenDependentValid=true
			for(ValueValidationResult aValueValidationResult : cruxValueValidationResultList) {
				if(!aValueValidationResult.isValid()) {
					if(DEBUG) Log.d(TAG, "...a crux ValueValidationResult is INVALID...");
					ok = false;
					message = mCruxFieldInvalidMessage;
				}
			}
		}
		if(mCruxFieldRequiredToExist && mCruxFieldSource == null) {
			if(DEBUG) Log.d(TAG, "...crux required to exist AND crux source is NULL...");
			ok = false;
			message = mCruxFieldNonExistentMessage;
		}
		return new DependencyValidationResult(sourceField, ok, message);
	}

	@Override
	public Object getSource() {
		return mSource;
	}

	@Override
	public Object getCruxFieldSource() {
		return mCruxFieldSource;
	}

}
