package com.coreform.open.android.formidablevalidation;

import java.util.List;

/**
 * Generic interface for validation of 
 * Android input fields
 * 
 * based off: http://dekwant.eu/2010/10/04/android-form-field-validation/
 * 
 * customised and enhanced by:
 * @author Linden Darling (contact@coreform.com.au)
 *
 */
public interface DependencyValidatorInterface {
	
	/**
	 * @return true if dependent field is required to exist
	 */
	boolean isCruxFieldRequiredToExist();
	
	/**
	 * @return true if dependent field is required to be valid
	 */
	boolean isCruxFieldRequiredToBeValid();
	
	/**
	 * @return true if dependency is satisfied (highest level)
	 */
	boolean isDependencySatisfied();
	
	/**
	 * @return true if dependent field exists
	 */
	boolean isCruxFieldExistent();
	
	/**
	 * @return true if all of dependent field's ValueValidations are met
	 */
	boolean isCruxFieldValid();
	
	/**
	 * Set whether dependent field is required to exist (typically default to true)
	 * @param requirement
	 */
	void setCruxFieldRequiredToExist(boolean required);
	
	/**
	 * Set whether dependent field is required to be valid (typically default to true)
	 * @param requirement
	 */
	void setCruxFieldRequiredToBeValid(boolean required);
	
	/**
	 * Message to be displayed when dependency unsatisfied (high level).
	 * @param message the error message
	 */
	void setUnsatisfiedMessage(String message);
	
	/**
	 * Message to be displayed when dependent field non-existent.
	 * @param message the error message
	 */
	void setCruxFieldNonExistentMessage(String message);
	
	/**
	 * Message to be displayed when dependent field invalid.
	 * @param message the error message
	 */
	void setCruxFieldInvalidMessage(String message);
	
	/**
	 * Source object that is under validation.
	 * @param the source object
	 */
	void setSource(Object source);
	
	/**
	 * Resource ID of source object that is under validation.
	 * @param the source object's Resource ID
	 */
	void setSourceResID(int sourceResID);
	
	/**
	 * Resource ID of dependent field.
	 * @param message the error message
	 */
	void setCruxFieldResID(int dependentFieldResID);
	
	/**
	 * Human-readable name of dependent field.
	 * May be inserted within feedback message(s).
	 * @param message the error message
	 */
	void setCruxFieldDisplayName(String dependentFieldDisplayName);
	
	/**
	 * This method returns the source object
	 * that is under validation. The actual
	 * implementation of the DependencyValidator is 
	 * responsible for the correctness of the
	 * Object.
	 * 
	 * @return the source object 
	 */
	Object getSource();
	
	/**
	 * Returns the resource ID of source object that is under validation.
	 * @return the source object's Resource ID
	 */
	int getSourceResID();
	
	/**
	 * This method returns the dependent field's source object
	 * that is ALSO under validation. The actual
	 * implementation of the DependencyValidator is 
	 * responsible for the correctness of the
	 * Object.
	 * 
	 * @return the source object 
	 */
	Object getCruxFieldSource();
	
	/**
	 * This method returns the dependent field's key,
	 * used in the ValueValidatorMap.
	 * 
	 * @return the dependent field's key
	 */
	Object getCruxFieldKey();
	
	/**
	 * Means to pass a List<ValueValidationResult> object to this class.
	 * Typically the passed param is extracted from a HashMap<String fieldName,List<ValueValidationResult>> object.
	 * Used by validateDependency() method to determine ValueValidationResult of the dependentField.
	 * 
	 * @param a List<ValueValidationResult> object
	 */
	void setValueValidationResults(List<ValueValidationResult> valueValidationResults);
	
	/**
	 * This method is called when this field (i.e. source) is to be validated
	 * using the DependencyValidator.
	 * 
	 * Typically the passed valueValidationResults param is extracted from a
	 * HashMap<String fieldName,List<ValueValidationResult>> object. I.e. it should be the
	 * List<ValueValidationResult> for the field to which this DependencyValidatorInterface
	 * is associated.
	 * 
	 * @param a List<ValueValidationResult> object
	 * @return an DependencyValidationResult object
	 * 
	 */
	DependencyValidationResult validateDependency(String thisFieldInvalidMessage, List<ValueValidationResult> cruxValueValidationResults);
}
