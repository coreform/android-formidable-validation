package com.coreform.open.android.formidablevalidation;

public abstract class AbstractDependencyValidator implements DependencyValidatorInterface {
	private static final boolean DEBUG = true;
	private static final String TAG = "AbstractDependencyValidator";
	
	private int mCruxFieldResID;
	private String mCruxFieldDisplayName;
	private String mCruxFieldKey;	//the key in ValidationManager's ValueValidatorsMap that corresponds with the Crux Field
	
	private boolean mCruxRequiredToExist = true;
	private boolean mCruxFieldRequiredToBeValid = true;
	
	private boolean mDependencySatisfied = false;
	private boolean mCruxFieldExistent = false;
	private boolean mCruxFieldValid = false;
	
	protected String mUnsatisfiedMessage = "DependencyValidation failure";
	protected String mCruxFieldNonExistentMessage = "The dependent field does not exist";
	protected String mCruxFieldInvalidMessage = "The dependent field is invalid";
	
	public boolean isCruxFieldRequiredToExist() {
		return mCruxRequiredToExist;
	}
	
	public boolean isCruxFieldRequiredToBeValid() {
		return mCruxFieldRequiredToBeValid;
	}
	
	public boolean isDependencySatisfied() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isCruxFieldExistent() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isCruxFieldValid() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setCruxFieldRequiredToExist(boolean required) {
		mCruxRequiredToExist = required;
	}
	
	public void setCruxFieldRequiredToBeValid(boolean required) {
		mCruxFieldRequiredToBeValid = required;
	}
	
	public void setUnsatisfiedMessage(String message) {
		mUnsatisfiedMessage = message;
	}

	public void setCruxFieldNonExistentMessage(String message) {
		mCruxFieldNonExistentMessage = message;
	}

	public void setCruxFieldInvalidMessage(String message) {
		mCruxFieldInvalidMessage = message;
	}

	public void setCruxFieldResID(int dependentFieldResID) {
		mCruxFieldResID = dependentFieldResID;
	}

	public void setCruxFieldDisplayName(String dependentFieldDisplayName) {
		mCruxFieldDisplayName = dependentFieldDisplayName;
	}
	
	public String getCruxFieldKey() {
		return mCruxFieldKey;
	}

	public abstract Object getSource();
	
	public abstract Object getCruxFieldSource();
}
