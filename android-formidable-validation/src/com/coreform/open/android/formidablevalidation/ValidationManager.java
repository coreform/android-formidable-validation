/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coreform.open.android.formidablevalidation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class ValidationManager {
	private static final boolean DEBUG = true;
	private static final String TAG = "ValidationManager";
	
	private static final long ACCESSIBILITY_ANNOUNCE_DELAY = 1 * 1000;
	
	private static Context mContext;
	private Handler mHandler;
	
	/**
     * The accessibility manager for this context. This is used to check the
     * accessibility enabled state, as well as to send raw accessibility events.
     */
    private static AccessibilityManager mAccessibilityManager;
	
	private LinkedHashMap<String, List<ValueValidatorInterface>> valueValidatorMap;
	private LinkedHashMap<String, List<DependencyValidatorInterface>> dependencyValidatorMap;
	
	public ValidationManager(Context context) {
		mContext = context;
		valueValidatorMap = new LinkedHashMap<String, List<ValueValidatorInterface>>();
		dependencyValidatorMap = new LinkedHashMap<String, List<DependencyValidatorInterface>>();
		
		mHandler = new Handler();
		
		// Keep a handle to the accessibility manager.
    	mAccessibilityManager = (AccessibilityManager) mContext.getSystemService(Context.ACCESSIBILITY_SERVICE);
	}
	
	/*
	 * METHODS
	 */
	
	/**
	 * note: add the key to both maps (if not already created)
	 * 
	 * @param key
	 */
	public ValidationManager add(String key) {
		if(valueValidatorMap.containsKey(key)) {
			//field has already been added, thus no need to re-add it!
			//do nada
		} else {
			//field has NOT already been added, so create it's List<ValueValidatorInterface>...
			//...and add the key and the empty List<ValueValidatorInterface> to the valueValidatorMap
			List<ValueValidatorInterface> fieldValueValidatorList = new ArrayList<ValueValidatorInterface>();
			valueValidatorMap.put(key, fieldValueValidatorList);
		}
		if(dependencyValidatorMap.containsKey(key)) {
			//field has already been added, thus no need to re-add it!
			//do nada
		} else {
			//field has NOT already been added, so create it's List<DependencyValidatorInterface>...
			//...and add the key and the empty List<DependencyValidatorInterface> to the dependencyValidatorMap
			List<DependencyValidatorInterface> fieldDependencyValidatorList = new ArrayList<DependencyValidatorInterface>();
			dependencyValidatorMap.put(key, fieldDependencyValidatorList);
		}
		return this;
	}
	
	public ValidationManager add(String key, ValueValidatorInterface valueValidator) {
		if(valueValidatorMap.containsKey(key)) {
			//field has already been added, thus it's List<ValueValidatorInterface> has already been created, so just add to that
			List<ValueValidatorInterface> fieldValueValidatorList = valueValidatorMap.get(key);
			fieldValueValidatorList.add(valueValidator);
		} else {
			//field has NOT already been added, so create it's List<ValueValidatorInterface> and add the first valueValidator to that...
			//...then add the key and the List<ValueValidatorInterface> to the valueValidatorMap
			List<ValueValidatorInterface> fieldValueValidatorList = new ArrayList<ValueValidatorInterface>();
			fieldValueValidatorList.add(valueValidator);
			valueValidatorMap.put(key, fieldValueValidatorList);
		}
		return this;
	}
	
	public ValidationManager add(String key, DependencyValidatorInterface dependencyValidator) {
		if(dependencyValidatorMap.containsKey(key)) {
			//field has already been added, thus it's List<DependencyValidatorInterface> has already been created, so just add to that
			List<DependencyValidatorInterface> fieldDependencyValidatorList = dependencyValidatorMap.get(key);
			fieldDependencyValidatorList.add(dependencyValidator);
		} else {
			//field has NOT already been added, so create it's List<DependencyValidatorInterface> and add the first dependencyValidator to that...
			//...then add the key and the List<DependencyValidatorInterface> to the dependencyValidatorMap
			List<DependencyValidatorInterface> fieldDependencyValidatorList = new ArrayList<DependencyValidatorInterface>();
			fieldDependencyValidatorList.add(dependencyValidator);
			dependencyValidatorMap.put(key, fieldDependencyValidatorList);
		}
		return this;
	}
	
	public HashMap<String, List<ValueValidationResult>> validateValues() {
		if(DEBUG) Log.d(TAG, ".validateValues()...");
		HashMap<String, List<ValueValidationResult>> valueValidationResultsMap = new HashMap<String, List<ValueValidationResult>>();
		for(Entry<String, List<ValueValidatorInterface>> entry : valueValidatorMap.entrySet()) {
			List<ValueValidatorInterface> entryValidatorList = entry.getValue();
			if(DEBUG) Log.d(TAG, "...validating value for field: "+entry.getKey()+" (field has "+entryValidatorList.size()+" valueValidators)");
			if(entryValidatorList.size() > 0) {
				//only bother creating a result entry for this field if it actually has at least one ValueValidator
				List<ValueValidationResult> fieldValueValidationResultsList = new ArrayList<ValueValidationResult>();
				//now run validation for each ValueValidator and put its Result into the ResultsList
				for(ValueValidatorInterface valueValidatorInterface : entryValidatorList) {
					if(DEBUG) Log.d(TAG, "......value validation expression: "+valueValidatorInterface.getExpression());
					ValueValidationResult aValueValidationResult = valueValidatorInterface.validateValue();
					//only add a FinalValidationResult if ValueValidationResult is invalid WHETHER OR NOT we care if its invalid
					if(!aValueValidationResult.isValid()) { fieldValueValidationResultsList.add(aValueValidationResult); }
					if(DEBUG && !aValueValidationResult.isValid()) { Log.d(TAG, ".........a ValueValidationResult is INVALID..."); }
					if(DEBUG && aValueValidationResult.isValid()) { Log.d(TAG, ".........a ValueValidationResult is VALID..."); }
				}
				//add ResultsList to ResultsMap for this field
				valueValidationResultsMap.put(entry.getKey(), fieldValueValidationResultsList);
			}
		}
		return valueValidationResultsMap;
	}
	
	public HashMap<String, List<DependencyValidationResult>> validateDependencies(HashMap<String, List<ValueValidationResult>> valueValidationResultsMap) {
		if(DEBUG) Log.d(TAG, ".validateDependencies()...");
		HashMap<String, List<DependencyValidationResult>> dependencyValidationResultsMap = new HashMap<String, List<DependencyValidationResult>>();
		for(Entry<String, List<DependencyValidatorInterface>> entry : dependencyValidatorMap.entrySet()) {
			List<DependencyValidatorInterface> entryValidatorList = entry.getValue();
			//get List<ValueValidationResult> for this field (note: might be null if no ValueValidators were set for this field)
			//List<ValueValidationResult> fieldValueValidationResultsList = valueValidationResultsMap.get(entry.getKey());	//actually, we want the dependent field's ValueValidators, derrr
			//if(DEBUG) Log.d(TAG, "...validating dependency for field:"+entry.getKey()+" (field has "+entryValidatorList.size()+" dependencyValidators and "+fieldValueValidationResultsList.size()+" valueValidationResults)");
			List<ValueValidationResult> thisFieldValueValidationResultsList = valueValidationResultsMap.get(entry.getKey());
			int thisFieldValueValidationAmount = (thisFieldValueValidationResultsList != null)? thisFieldValueValidationResultsList.size() : 0;
			if(DEBUG) Log.d(TAG, "...validating dependency for field:"+entry.getKey()+" (field has "+entryValidatorList.size()+" dependencyValidators and "+thisFieldValueValidationAmount+" valueValidators)");
			//only bother creating a result entry for this field if it actually has at least one DependencyValidator
			if(entryValidatorList.size() > 0) {
				//determine whether this field's value is itself valid according to its ValueValidationResults
				String thisFieldInvalidMessage = null;	//override if any invalid results found (using first result found)
				if(!thisFieldValueValidationResultsList.isEmpty()) {
					thisFieldInvalidMessage = thisFieldValueValidationResultsList.get(0).getMessage();
				}
				//get dependent field's ValidationResults
				List<DependencyValidationResult> fieldCruxValidationResultsList = new ArrayList<DependencyValidationResult>();
				//now run validation for each DependencyValidator and put its Result into the ResultsList
				for(DependencyValidatorInterface dependencyValidatorInterface : entryValidatorList) {
					List<ValueValidationResult> cruxFieldValueValidationResultsList = valueValidationResultsMap.get(dependencyValidatorInterface.getCruxFieldKey());
					int cruxValueValidationResultAmount = (cruxFieldValueValidationResultsList != null)? cruxFieldValueValidationResultsList.size() : 0;
					if(DEBUG) Log.d(TAG, "......crux field's number of ValueValidationResults: "+cruxValueValidationResultAmount);
					DependencyValidationResult aDependencyValidationResult = dependencyValidatorInterface.validateDependency(thisFieldInvalidMessage, cruxFieldValueValidationResultsList);
					//null check is a bit of a quick hack to alleviate a bigger problem...which it doesn't fix...TODO fix the actual problem!
					//NullPointerException when accessing FinalValidationResult.getSource() in submit button's onClickListener WHEN nameFirst is invalid (so its due to the dependency check logic)
					if(!aDependencyValidationResult.isValid()) fieldCruxValidationResultsList.add(aDependencyValidationResult);
					if(DEBUG && !aDependencyValidationResult.isValid()) { Log.d(TAG, ".........a DependencyValidationResult is INVALID..."); }
					if(DEBUG && aDependencyValidationResult.isValid()) { Log.d(TAG, ".........a DependencyValidationResult is VALID..."); }
					if(DEBUG && dependencyValidatorInterface.getSource() == null) { Log.d(TAG, ".........a DependencyValidator's source is NULL..."); }
				}
				//add ResultsList to ResultsMap for this field
				dependencyValidationResultsMap.put(entry.getKey(), fieldCruxValidationResultsList);
			}
		}
		return dependencyValidationResultsMap;
	}
	
	private void removePreviouslySetErrors() {
		for(Entry<String, List<ValueValidatorInterface>> entry : valueValidatorMap.entrySet()) {
			List<ValueValidatorInterface> entryValidatorList = entry.getValue();
			for(ValueValidatorInterface valueValidatorInterface : entryValidatorList) {
				if(valueValidatorInterface.getSource() instanceof SetErrorAble) {
					((SetErrorAble)valueValidatorInterface.getSource()).betterSetError(null);
				} else if(valueValidatorInterface.getSource() instanceof TextView) {
					((TextView)valueValidatorInterface.getSource()).setError(null);
				}
			}
		}
		for(Entry<String, List<DependencyValidatorInterface>> entry : dependencyValidatorMap.entrySet()) {
			List<DependencyValidatorInterface> entryValidatorList = entry.getValue();
			for(DependencyValidatorInterface dependencyValidatorInterface : entryValidatorList) {
				if(dependencyValidatorInterface.getSource() instanceof SetErrorAble) {
					((SetErrorAble)dependencyValidatorInterface.getSource()).betterSetError(null);
				} else if(dependencyValidatorInterface.getSource() instanceof TextView) {
					((TextView)dependencyValidatorInterface.getSource()).setError(null);
				}
			}
		}
	}
	
	public HashMap<String, FinalValidationResult> validateAll() {
		return validateAll(false);
	}
	
	public HashMap<String, FinalValidationResult> validateAll(boolean removePreviouslySetErrors) {
		if(DEBUG) Log.d(TAG, ".validateAll()...");
		if(removePreviouslySetErrors) {
			removePreviouslySetErrors();
		}
		HashMap<String, FinalValidationResult> finalValidationResultMap = new HashMap<String, FinalValidationResult>();
		HashMap<String, List<ValueValidationResult>> valueValidationResultsMap = validateValues();
		HashMap<String, List<DependencyValidationResult>> dependencyValidationResultsMap = validateDependencies(valueValidationResultsMap);
		Object fieldSource = null;
		//get a full set of fields for which validations are applicable (since ValueValidators and DependencyValidators don't have to be added 1-for-1)
		//...note: if a field was added to either ValueValidator list or DependencyValidator list or both but never had a validator associated with it, it won't be in this set.
		Set<String> valueValidatorFieldsSet = valueValidationResultsMap.keySet();
		Set<String> dependencyValidatorFieldsSet = dependencyValidationResultsMap.keySet();
		Set<String> validatedFieldsSet = new LinkedHashSet<String>();
		validatedFieldsSet.addAll(dependencyValidatorFieldsSet);
		validatedFieldsSet.addAll(valueValidatorFieldsSet);
		//now aggregate the ValueValidationResults with DependencyValidationResults into a FinalValidationResult for each field
		for(String fieldRefName : validatedFieldsSet) {
			if(DEBUG) Log.d(TAG, "...validateAll for field: "+fieldRefName+"...");
			boolean fieldValueOk = true;	//will be overridden to false if a validation found to have failed
			boolean fieldDependencyOk = true;	//will be overridden to false if a validation found to have failed
			String fieldValueValidationResultMessage = "Default field value validation result message";
			String fieldDependencyValidationResultMessage = "Default field dependency validation result message";
			
			//get List<DependencyValidationResult> for fieldRefName
			List<DependencyValidationResult> aFieldDependencyValidationResultsList = dependencyValidationResultsMap.get(fieldRefName);
			if(aFieldDependencyValidationResultsList != null) {
				if(DEBUG) Log.d(TAG, "...field has unsatisfied dependencies...");
				//find the first (if any) DependencyValidation failure for this field
				for(DependencyValidationResult aDependencyValidationResult : aFieldDependencyValidationResultsList) {
					if(!aDependencyValidationResult.isValid()) {
						fieldDependencyOk = false;
						fieldDependencyValidationResultMessage = aDependencyValidationResult.getMessage();
					}
				}
			} else {
				if(DEBUG) Log.d(TAG, "...field has satisfied dependencies...");
				//field has no DependencyValidators so definitely check its ValueValidators
				//get List<ValueValidationResult> for fieldRefName
				List<ValueValidationResult> aFieldValueValidationResultsList = valueValidationResultsMap.get(fieldRefName);
				if(aFieldValueValidationResultsList != null) {
					if(DEBUG) Log.d(TAG, "......field has invalid value...");
					//find the first (if any) ValueValidation failure for this field
					if(!aFieldValueValidationResultsList.isEmpty()) {
						for(ValueValidationResult aValueValidationResult : aFieldValueValidationResultsList) {
							fieldSource = aValueValidationResult.getSource();
							if(!aValueValidationResult.isValid()) {
								fieldValueOk = false;
								fieldValueValidationResultMessage = aValueValidationResult.getMessage();
							}
						}
					} else {
						if(DEBUG) Log.d(TAG, "...field's value is invalid, however no ValueValidationResults exists. You may need to implement the validate() method in your AbstractValueValidator implementation.");
					}
				}
			}
			//populate field source via DependencyValidationResults OR ValueValidationResults
			if(fieldSource == null) {
				List<ValueValidationResult> aFieldValueValidationResultsList = valueValidationResultsMap.get(fieldRefName);
				if(!aFieldValueValidationResultsList.isEmpty()) {
					fieldSource = aFieldValueValidationResultsList.get(0).getSource();
				} else {
					if(DEBUG) Log.d(TAG, "...field has no source! (no DependencyValidators and no ValueValidators)...");
				}
			}
			//create and add the FinalValidationResult for this field to the finalValidationResultMap ONLY if a value/dependency fail occurred
			if(!fieldValueOk || !fieldDependencyOk) {
				FinalValidationResult aFinalValidationResult = new FinalValidationResult(fieldSource, fieldValueOk, fieldDependencyOk, fieldValueValidationResultMessage, fieldDependencyValidationResultMessage);
				finalValidationResultMap.put(fieldRefName, aFinalValidationResult);
			}
		}
		return finalValidationResultMap;
	}
	
	public boolean validateAllAndSetError() {
		//validateAll
		HashMap<String, FinalValidationResult> finalValidationResultMap = validateAll(true);
		if(finalValidationResultMap.isEmpty()) {
			//all valid
			return true;
		} else {
			//something invalid
			if(DEBUG) Log.d(TAG, "...at least one field is invalid, perhaps more.");
			if(DEBUG) Log.d(TAG, "...number of invalid fields: "+Integer.toString(finalValidationResultMap.size()));
			boolean setErrorAlreadyShown = false;
			View firstInvalidView = null;
			SpannableStringBuilder firstInvalidErrorText = null;
			validationLoop: for(Entry<String, FinalValidationResult> entry : finalValidationResultMap.entrySet()) {
				if(DEBUG) Log.d(TAG, "...validation result failed for field: "+entry.getKey());
				FinalValidationResult aFinalValidationResult = entry.getValue();
				boolean invalidDependencyTakesPrecedence = false;
				boolean invalidFieldHasAlreadyBeenFocused = false;	//used to force focus to the first invalid field in List instead of last
				if(!aFinalValidationResult.isDependencyValid()) {
					if(DEBUG) Log.d(TAG, "...dependency validation failed, with message: "+aFinalValidationResult.getDependencyInvalidMessage());
					invalidDependencyTakesPrecedence = true;
					//if(DEBUG) Log.d(TAG, "aFinalValidationResult source's class: "+aFinalValidationResult.getSource().getClass().getSimpleName());
					if("EditText".equals(aFinalValidationResult.getSource().getClass().getSimpleName())) {
						if(!invalidFieldHasAlreadyBeenFocused) {
							invalidFieldHasAlreadyBeenFocused = true;
						}
						//setError
						SpannableStringBuilder errorText = new SpannableStringBuilder(aFinalValidationResult.getDependencyInvalidMessage());
						((EditText) aFinalValidationResult.getSource()).setError(errorText);
						setErrorAlreadyShown = true;
						if(firstInvalidView == null) {
							firstInvalidView = (View) aFinalValidationResult.getSource();
							firstInvalidErrorText = errorText;
						}
					}
				}
				if(!aFinalValidationResult.isValueValid()) {
					//note: ValueValidationResults for which careInvalid = false should NOT have caused a submission of FinalValidationResult to this List
					if(DEBUG) Log.d(TAG, "...value validation failed, with message: "+aFinalValidationResult.getValueInvalidMessage());
					//only display value invalid message if dependency(ies) satisfied for this field
					if(!invalidDependencyTakesPrecedence) {
						if(aFinalValidationResult.getSource() == null) {
							if(DEBUG) Log.d(TAG, "...cannot display error balloon because source is null!");
							//do nothing
						} else if(aFinalValidationResult.getSource() instanceof SetErrorAble) {
							//only set (don't show) error for a SetErrorAble View if no other Views have had their error set.
							//...to avoid having multiple ErrorPopups displayed at same time (at least until User moves focus)
							SpannableStringBuilder errorText = new SpannableStringBuilder(aFinalValidationResult.getValueInvalidMessage());
							if(DEBUG) Log.d(TAG, "...set and show custom error balloon...");
							if(true || !invalidFieldHasAlreadyBeenFocused) {
								invalidFieldHasAlreadyBeenFocused = true;
							}
							//setError (for a button, this won't requestFocus() and won't show the message in a popup...it only sets the exclamation inner drawable)
							((SetErrorAble) aFinalValidationResult.getSource()).betterSetError(errorText, false);
							setErrorAlreadyShown = true;
							if(firstInvalidView == null) {
								firstInvalidView = (View) aFinalValidationResult.getSource();
								firstInvalidErrorText = errorText;
							}
						} else if(aFinalValidationResult.getSource() instanceof TextView) {
							if(DEBUG) Log.d(TAG, "...set and show native error balloon...");
							if(!invalidFieldHasAlreadyBeenFocused) {
								invalidFieldHasAlreadyBeenFocused = true;
							}
							//setError
							SpannableStringBuilder errorText = new SpannableStringBuilder(aFinalValidationResult.getValueInvalidMessage());
							((TextView) aFinalValidationResult.getSource()).setError(errorText);
							setErrorAlreadyShown = true;
							if(firstInvalidView == null) {
								firstInvalidView = (View) aFinalValidationResult.getSource();
								firstInvalidErrorText = errorText;
							}
						} else {
							if(DEBUG) Log.d(TAG, "...field does not support .setError()!");
						}
					}
				}
			}
			//need to delay accessibility announcement so its the last View to steal focus
			if(DEBUG) Log.d(TAG, "firstInvalidErrorText: "+firstInvalidErrorText);
			AnnounceForAccessibilityRunnable announceForAccessibilityRunnable = new AnnounceForAccessibilityRunnable(firstInvalidErrorText);
			mHandler.postDelayed(announceForAccessibilityRunnable, ACCESSIBILITY_ANNOUNCE_DELAY);
			if(((View) firstInvalidView).isFocusable() && ((View) firstInvalidView).isFocusableInTouchMode()) {
				((View) firstInvalidView).requestFocus();
			} else if(firstInvalidView instanceof SetErrorAble) {
				((SetErrorAble) firstInvalidView).betterSetError(firstInvalidErrorText, true);
				((View) firstInvalidView).requestFocusFromTouch();	//probably won't do much
				
			}
			return false;
		}
	}
	
	/**
     * Generates and dispatches an SDK-specific spoken announcement.
     * <p>
     * For backwards compatibility, we're constructing an event from scratch
     * using the appropriate event type. If your application only targets SDK
     * 16+, you can just call View.announceForAccessibility(CharSequence).
     * </p>
     * 
     * Adapted from https://http://eyes-free.googlecode.com/files/accessibility_codelab_demos_v2_src.zip
     *
     * @param text The text to announce.
     */
    public static void announceForAccessibilityCompat(CharSequence text) {
        if (!mAccessibilityManager.isEnabled()) {
            return;
        }

        // Prior to SDK 16, announcements could only be made through FOCUSED
        // events. Jelly Bean (SDK 16) added support for speaking text verbatim
        // using the ANNOUNCEMENT event type.
        final int eventType;
        if (Build.VERSION.SDK_INT < 16) {
            eventType = AccessibilityEvent.TYPE_VIEW_FOCUSED;
        } else {
            eventType = AccessibilityEventCompat.TYPE_ANNOUNCEMENT;
        }

        // Construct an accessibility event with the minimum recommended
        // attributes. An event without a class name or package may be dropped.
        final AccessibilityEvent event = AccessibilityEvent.obtain(eventType);
        event.getText().add(text);
        event.setClassName(SetErrorHandler.class.getName());
        event.setPackageName(mContext.getPackageName());

        // Sends the event directly through the accessibility manager. If your
        // application only targets SDK 14+, you should just call
        // getParent().requestSendAccessibilityEvent(this, event);
        mAccessibilityManager.sendAccessibilityEvent(event);
    }
    
    /*
     * INNER CLASSES
     */
    
    private static class AnnounceForAccessibilityRunnable implements Runnable {
    	private final CharSequence mText;
    	
    	public AnnounceForAccessibilityRunnable(CharSequence text) {
    		mText = text;
    	}
		@Override
		public void run() {
			announceForAccessibilityCompat(mText);
		}
    	
    }
}
