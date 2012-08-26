package com.coreform.open.android.formidablevalidation;

/**
 * Replacement for List<Validator> that handles validation dependencies.
 * E.g. 'Email Address' only required if 'Send me an email' is checked.
 * E.g. 'Last name' only required if 'First name' valid.
 * 
 * based off: http://dekwant.eu/2010/10/04/android-form-field-validation/
 * 
 * debugged, customised, and enhanced by:
 * @author Linden Darling (contact@coreform.com.au)
 *
 */
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.CheckBox;
import android.widget.EditText;

public class ValidatorList {
	private static final boolean DEBUG = true;
	private static final String TAG = "ValidatorList";
	
	private ArrayList<DependantValidator> validators;
	
	public ValidatorList() {
		validators = new ArrayList<DependantValidator>();
	}
	
	/*
	 * METHODS
	 */
	
	public void add(ValueValidatorInterface validator, int dependantSourceRes, ValidateOnSourceState validateOnSourceState) {
		DependantValidator dependantValidator = new DependantValidator();
		dependantValidator.validator = validator;
		dependantValidator.dependantSourceRes = dependantSourceRes;
		dependantValidator.validateOnSourceState = validateOnSourceState;
		validators.add(dependantValidator);
	}
	
	public DependantValidationResult dependantValidate(int index) {
		DependantValidator dependantValidator = (DependantValidator) validators.get(index);
		if(DEBUG) Log.d(TAG, "...dependant validation for source: "+((View) dependantValidator.validator.getSource()).getTag());
		ValueValidationResult validationResult = dependantValidator.validator.validateValue();
		if(DEBUG) Log.d(TAG, "...dependant validator's validation result isValid: "+Boolean.toString(validationResult.isValid()));
		if(DEBUG) Log.d(TAG, "...dependant validator's validation result message: "+validationResult.getMessage());
		boolean dependencySatisfied = false;
		String dependencyMessage = "Dependency not satisfied.";
		//validate dependancy
			//search for dependant Source in validators, ignoring if Source is found in index entry (avoids infinite recursion!)
			boolean sourceMatchFound = false;
			String sourceMatchTag = "?";
			for(int i=0; i<validators.size(); i++) {
				if(i != index) {
					DependantValidator aDependantValidator = validators.get(i);
					//match dependantSource ids
					if(aDependantValidator.dependantSourceRes == ((View) dependantValidator.validator.getSource()).getId()) {
						if(DEBUG) Log.d(TAG, "...dependant validator's source found...");
						sourceMatchFound = true;
						View parentView = (View) ((View) dependantValidator.validator.getSource()).getParent();
						sourceMatchTag = (String) parentView.findViewById(aDependantValidator.dependantSourceRes).getTag();
						if(DEBUG) Log.d(TAG, "...dependant source tag: "+sourceMatchTag);
						if(dependantValidator.validateOnSourceState.sourceValid) {
							ValueValidationResult aValidationResult = aDependantValidator.validator.validateValue();
							dependencySatisfied = aValidationResult.isValid();
						} else if(dependantValidator.validateOnSourceState.checkBox_checked) {
							CheckBox aCheckBox = (CheckBox) aDependantValidator.validator.getSource();
							dependencySatisfied = aCheckBox.isChecked();
						} else if(dependantValidator.validateOnSourceState.checkBox_unchecked) {
							CheckBox aCheckBox = (CheckBox) aDependantValidator.validator.getSource();
							dependencySatisfied = !aCheckBox.isChecked();
						} else {
							//at this stage & in this situation, assume success (allowing sourceExists check to override if necessary)
							dependencySatisfied = true;
						}
					}
				}
			}
			
			if(!sourceMatchFound) {
				if(DEBUG) Log.d(TAG, "...dependant validator's source NOT found...");
			}
			
			//if dependancy requires dependencySource to exist and it doesn't, override any success via validation to failure
			if(dependantValidator.validateOnSourceState.sourceExists) {
				if(!sourceMatchFound) {
					dependencySatisfied = false;
				}
			}
		
		return new DependantValidationResult(validationResult, dependencySatisfied, "This field depends on the "+((View) dependantValidator.validator.getSource()).getTag()+" field");
	}
	
	/**
	 * Validate all dependantValidators (consequently validates each validator).
	 * 
	 * @param validators an array of validators
	 * @return an array of validation failure results.
	 */
	public List<DependantValidationResult> validateAll() {
		List<DependantValidationResult> _result = new ArrayList<DependantValidationResult>();
		DependantValidationResult _dvr = null;
		/*
		for (DependantValidator dv : validators) {
			_dvr = dv.validate();
			if (!_vr.isValid()) {
				_result.add(_vr);
			}
		}
		*/
		for(int i=0; i<validators.size(); i++) {
			_dvr = dependantValidate(i);
			DependantValidator aDependentValidator = validators.get(i);
			if(!_dvr.isDependancySatisfied()) {
				_result.add(_dvr);
				//only display dependencyMessage if the dependent field is valid AND this field is an EditText
				if(_dvr.getValidationResult().isValid() && "EditText".equals(aDependentValidator.validator.getSource().getClass().getSimpleName())) {
					((EditText) aDependentValidator.validator.getSource()).setError(_dvr.dependencyMessage);
				}
			} else if(!_dvr.getValidationResult().isValid()) {
				_result.add(_dvr);
				//only display validationMessage if this field is an EditText
				if("EditText".equals(aDependentValidator.validator.getSource().getClass().getSimpleName())) {
					((EditText) aDependentValidator.validator.getSource()).setError(_dvr.validationResult.getMessage());
				}
			}
		}
		return _result;
	}
	
	/*
	 * INNER CLASSES
	 */
	
	private class DependantValidator {
		ValueValidatorInterface validator;
		int dependantSourceRes;	//Resource ID of a View that has a .getText() method that returns an Object that has a .toString() method
		ValidateOnSourceState validateOnSourceState;
	}
	
	public class ValidateOnSourceState {
		public boolean sourceExists = true;	//note: if source doesn't exist and sourceExists is true, will override outcome from any other dependency checks
		public boolean sourceValid = false;	//note: use as alternative to checkBox_checked
		public boolean checkBox_checked = false;	//note: only makes sense (works) if the source is a CheckBox. Use as alternative to sourceValid and checkBox_unchecked
		public boolean checkBox_unchecked = false;	//note: only makes sense (works) if the source is a CheckBox. Use as alternative to sourceValid and checkBox_checked
	}
	
	//a wrapper for ValidationResult that is based on ValidationResult
	public class DependantValidationResult {
		ValueValidationResult validationResult;
		private boolean dependencySatisfied = false;
		private String dependencyMessage = "Dependancy not satisfied.";
		
		public DependantValidationResult(ValueValidationResult validationResult, boolean dependancySatisfied, String dependancyMessage) {
			this.validationResult = validationResult;
			this.dependencySatisfied = dependancySatisfied;
			this.dependencyMessage = dependancyMessage;
		}
		
		public ValueValidationResult getValidationResult() {
			return validationResult;
		}
		
		public boolean isDependancySatisfied() {
			return dependencySatisfied;
		}
		
		public String getDependencyMessage() {
			return dependencyMessage;
		}
	}
}
