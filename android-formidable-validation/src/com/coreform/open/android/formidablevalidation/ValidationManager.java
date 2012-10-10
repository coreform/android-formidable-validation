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
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
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
					if(!aValueValidationResult.isValid()) fieldValueValidationResultsList.add(aValueValidationResult);
					if(DEBUG && !aValueValidationResult.isValid()) Log.d(TAG, ".........a ValueValidationResult is INVALID...");
					if(DEBUG && aValueValidationResult.isValid()) Log.d(TAG, ".........a ValueValidationResult is VALID...");
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
					if(DEBUG && !aDependencyValidationResult.isValid()) Log.d(TAG, ".........a DependencyValidationResult is INVALID...");
					if(DEBUG && aDependencyValidationResult.isValid()) Log.d(TAG, ".........a DependencyValidationResult is VALID...");
					if(DEBUG && dependencyValidatorInterface.getSource() == null) Log.d(TAG, ".........a DependencyValidator's source is NULL...");
				}
				//add ResultsList to ResultsMap for this field
				dependencyValidationResultsMap.put(entry.getKey(), fieldCruxValidationResultsList);
			}
		}
		return dependencyValidationResultsMap;
	}
	
	public HashMap<String, FinalValidationResult> validateAll() {
		if(DEBUG) Log.d(TAG, ".validateAll()...");
		HashMap<String, FinalValidationResult> finalValidationResultMap = new HashMap<String, FinalValidationResult>();
		HashMap<String, List<ValueValidationResult>> valueValidationResultsMap = validateValues();
		HashMap<String, List<DependencyValidationResult>> dependencyValidationResultsMap = validateDependencies(valueValidationResultsMap);
		Object fieldSource = null;
		//get a wholistic set of fields for which validations are applicable (since ValueValidators and DependencyValidators don't have to be added 1-for-1)
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
	
	public boolean validateAllAndSetError(ScrollView containerScrollView) {
		//validateAll
		HashMap<String, FinalValidationResult> finalValidationResultMap = validateAll();
		if(finalValidationResultMap.isEmpty()) {
			//all valid
			return true;
		} else {
			//something invalid
			if(DEBUG) Log.d(TAG, "...at least one field is invalid, perhaps more.");
			if(DEBUG) Log.d(TAG, "...number of invalid fields: "+Integer.toString(finalValidationResultMap.size()));
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
						//autoscroll to field
						if(!invalidFieldHasAlreadyBeenFocused) {
							//((EditText) aFinalValidationResult.getSource()).requestFocus();	//problematic for onFocusChangeListener (yields 2x focused EditTexts = BAD STUFF)
							invalidFieldHasAlreadyBeenFocused = true;
						}
						//setError
						SpannableStringBuilder errorText = new SpannableStringBuilder(aFinalValidationResult.getDependencyInvalidMessage());
						((EditText) aFinalValidationResult.getSource()).setError(errorText);
						//need to delay accessibility announcement so its the last View to steal focus
						AnnounceForAccessibilityRunnable announceForAccessibilityRunnable = new AnnounceForAccessibilityRunnable(errorText);
						mHandler.postDelayed(announceForAccessibilityRunnable, ACCESSIBILITY_ANNOUNCE_DELAY);
						//break validationLoop;
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
						} else if(aFinalValidationResult.getSource() instanceof TextView) {
							//autoscroll to field
							if(true || !invalidFieldHasAlreadyBeenFocused) {
								((EditText) aFinalValidationResult.getSource()).requestFocus();
								invalidFieldHasAlreadyBeenFocused = true;
							}
							//setError
							SpannableStringBuilder errorText = new SpannableStringBuilder(aFinalValidationResult.getValueInvalidMessage());
							((EditText) aFinalValidationResult.getSource()).setError(errorText);
							//need to delay accessibility announcement so its the last View to steal focus
							AnnounceForAccessibilityRunnable announceForAccessibilityRunnable = new AnnounceForAccessibilityRunnable(errorText);
							mHandler.postDelayed(announceForAccessibilityRunnable, ACCESSIBILITY_ANNOUNCE_DELAY);
							//return false;	//break now to block a subsequent EditText or SetErrorAble-View from showing another ErrorPopup
						} else if(aFinalValidationResult.getSource() instanceof SetErrorAble) {
							//note: this is seriously custom code, specific for this use-case, not very portable to other use-cases in its current form
							if(DEBUG) Log.d(TAG, "...attempting to display custom error balloon...");
							//autoscroll to field
							if(true || !invalidFieldHasAlreadyBeenFocused) {
								//((Button) aFinalValidationResult.getSource()).requestFocus();	//Button.requestFocus does nada
								invalidFieldHasAlreadyBeenFocused = true;
								if(containerScrollView != null) {
									//forcibly scroll the scrollView as most non-EditText form Views won't receive focus
									containerScrollView.scrollTo(0, 0);	//hardcoded scroll to top of form! TODO: make this scroll to dynamic position of invalid Button
								}
							}
							//setError (for a button, this won't requestFocus() and won't show the message in a popup...it only sets the exclamation inner drawable)
							SpannableStringBuilder errorText = new SpannableStringBuilder(aFinalValidationResult.getValueInvalidMessage());
							((SetErrorAble) aFinalValidationResult.getSource()).setError(errorText);
							//need to delay accessibility announcement so its the last View to steal focus
							AnnounceForAccessibilityRunnable announceForAccessibilityRunnable = new AnnounceForAccessibilityRunnable(errorText);
							mHandler.postDelayed(announceForAccessibilityRunnable, ACCESSIBILITY_ANNOUNCE_DELAY);
							//return false;	//break now to block a subsequent ErrorText stealing focus (and thereby showing more than 1x ErrorPopup)
							//manually set the rest of error
							/*
							if(mCustomErrorBalloon == null) {
								LayoutInflater lInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
								mCustomErrorBalloon = (LinearLayout) lInflater.inflate(R.layout.proforma_custom_error_balloon, null);
								TextView customErrorTextView = (TextView) mCustomErrorBalloon.findViewById(R.id.balloonTextView);
								customErrorTextView.setText(errorText);
								LinearLayout baseLinearLayout = (LinearLayout) findViewById(R.id.baseLinearLayout);
								LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
								params.gravity = Gravity.RIGHT;
								int buttonIndex = baseLinearLayout.indexOfChild((View) aFinalValidationResult.getSource());
								if(DEBUG) Log.d(TAG, "...adding custom error balloon at position: "+buttonIndex);
								baseLinearLayout.addView(mCustomErrorBalloon, buttonIndex+1, params);
								//mCustomErrorBalloon.bringToFront();	//interestingly, this will cause the View to be shafted to bottom of LinearLayout
							}
							*/
						} else {
							if(DEBUG) Log.d(TAG, "...field does not support .setError()!");
						}
					}
				}
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
