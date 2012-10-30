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

package com.coreform.open.android.formidablevalidation.example;

import java.util.ArrayList;
import java.util.List;

import com.coreform.open.android.formidablevalidation.CheckBoxCheckedDependencyValidator;
import com.coreform.open.android.formidablevalidation.CheckBoxRequiredValueValidator;
import com.coreform.open.android.formidablevalidation.R;
import com.coreform.open.android.formidablevalidation.RegExpressionValueValidator;
import com.coreform.open.android.formidablevalidation.SetErrorAbleButton;
import com.coreform.open.android.formidablevalidation.SetErrorAbleCheckBox;
import com.coreform.open.android.formidablevalidation.SetErrorAbleEditText;
import com.coreform.open.android.formidablevalidation.SetErrorAbleSpinner;
import com.coreform.open.android.formidablevalidation.SpinnerRequiredValueValidator;
import com.coreform.open.android.formidablevalidation.ValidationManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class AddressActivity extends Activity {
	private static final boolean DEBUG = true;
	private static final String TAG = "AddressActivity";
	
	private static final int REQUEST_COLOURPICKER1 = 101;
	private static final int REQUEST_COLOURPICKER2 = 102;
	
	private ValidationManager mValidationManager;
	
	private ScrollView mContentScrollView;
	private CheckBox mUnderstoodCheckBox;
	private EditText mAddressLine1EditText;
	private EditText mAddressLine2EditText;
	private EditText mCityEditText;
	private EditText mProvinceEditText;
	private SetErrorAbleSpinner mCountrySpinner;
	private EditText mPhoneEditText;
	private EditText mEmailEditText;
	private CheckBox mSignupNewsletterCheckBox;
	private Button mFavouriteColourButton;
	private SetErrorAbleButton mExampleSetErrorAbleButton;
	private Button mClearFormButton;
	private Button mValidateFormButton;
	private SetErrorAbleEditText mExampleSetErrorAbleEditText;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if(Build.VERSION.SDK_INT > 10) {
        	setTheme(R.style.Theme_Holo);
        } else {
        	setTheme(R.style.Theme);
        }
        
        setContentView(R.layout.screen_form_address);
        
        mContentScrollView = (ScrollView) findViewById(R.id.contentScrollView);
        mUnderstoodCheckBox = (SetErrorAbleCheckBox) findViewById(R.id.field00CheckBox);
        mAddressLine1EditText = (EditText) findViewById(R.id.field01EditText);
    	mAddressLine2EditText = (EditText) findViewById(R.id.field02EditText);
    	mCityEditText = (EditText) findViewById(R.id.field03EditText);
    	mProvinceEditText = (EditText) findViewById(R.id.field04EditText);
    	mCountrySpinner = (SetErrorAbleSpinner) findViewById(R.id.field05Spinner);
    	mPhoneEditText = (EditText) findViewById(R.id.field06EditText);
    	mEmailEditText = (EditText) findViewById(R.id.field07EditText);
    	mSignupNewsletterCheckBox = (CheckBox) findViewById(R.id.field08CheckBox);
    	mFavouriteColourButton = (Button) findViewById(R.id.field09Button);
    	mExampleSetErrorAbleButton = (SetErrorAbleButton) findViewById(R.id.field09bButton);
    	mClearFormButton = (Button) findViewById(R.id.clearFormButton);
    	mValidateFormButton = (Button) findViewById(R.id.validateFormButton);
    	mExampleSetErrorAbleEditText = (SetErrorAbleEditText) findViewById(R.id.field10EditText);
    	
    	mFavouriteColourButton.setOnClickListener(new FavouriteColourButtonOnClickListener(REQUEST_COLOURPICKER1));
    	mExampleSetErrorAbleButton.setOnClickListener(new FavouriteColourButtonOnClickListener(REQUEST_COLOURPICKER2));
    	mValidateFormButton.setOnClickListener(new ValidateFormButtonOnClickListener());
    	
    	String[] itemTextsArray = getResources().getStringArray(R.array.countries_array);
    	List<String> itemTextsList = new ArrayList<String>();
    	for(String itemText : itemTextsArray) {
    		itemTextsList.add(itemText);
    	}
    	mCountrySpinner.setupUnselectedSpinnerAdapter(R.array.countries_array, android.R.layout.simple_spinner_item, itemTextsList);
    	
    	//setup validation
    	mValidationManager = new ValidationManager(this);
    	
    	mValidationManager.add("understood", new CheckBoxRequiredValueValidator(mUnderstoodCheckBox, "You must acknowledge that this form does not submit data anywhere and that it is simply for demonstration purposes."));
    	mValidationManager.add("addressLine1", new RegExpressionValueValidator(mAddressLine1EditText, "^[a-zA-Z0-9\\-'\\s]{3,}$", "please enter your address."));
    	mValidationManager.add("signupNewsletter");
    	mValidationManager.add("countrySpinner", new SpinnerRequiredValueValidator(mCountrySpinner, "please select a country."));
    	mValidationManager.add("emailAddress", new CheckBoxCheckedDependencyValidator(mEmailEditText, "signupNewsletter", mSignupNewsletterCheckBox, true, false, "Please enter your email address to signup to the newsletter list."));
    	mValidationManager.add("emailAddress", new RegExpressionValueValidator(mEmailEditText, "^([0-9a-zA-Z]([-\\.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})$", "Email address must be valid."));
    	mValidationManager.add("favouriteColour", new ColourPickerButtonValueValidator(mFavouriteColourButton, true));
    	mValidationManager.add("exampleSetErrorAbleButton", new ColourPickerButtonValueValidator(mExampleSetErrorAbleButton, true));
    	mValidationManager.add("exampleSetErrorAbleEditText", new RegExpressionValueValidator(mExampleSetErrorAbleEditText, "^[a-zA-Z0-9\\-'\\s]{3,}$", "please enter your address."));
    	
    }
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_COLOURPICKER1) {
			if(resultCode == RESULT_OK) {
				int colourResource = data.getIntExtra("colour", 0);
				if(colourResource > 0) {
					mFavouriteColourButton.setText(null);
					mFavouriteColourButton.setError(null, null);
					mFavouriteColourButton.setBackgroundResource(colourResource);
				}
			}
		} else if(requestCode == REQUEST_COLOURPICKER2) {
			if(resultCode == RESULT_OK) {
				int colourResource = data.getIntExtra("colour", 0);
				if(colourResource > 0) {
					mExampleSetErrorAbleButton.setText(null);
					mExampleSetErrorAbleButton.setError(null, null);
					mExampleSetErrorAbleButton.setBackgroundResource(colourResource);
				}
			}
		}
	}
	
	/*
	 * METHODS
	 */
	

	
	/*
	 * INNER CLASSES
	 */
	
	private class FavouriteColourButtonOnClickListener implements OnClickListener {
		int mRequestCode;
		public FavouriteColourButtonOnClickListener(int requestCode) {
			mRequestCode = requestCode;
		}
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(AddressActivity.this, ColourPickerActivity.class);
			startActivityForResult(intent, mRequestCode);
		}
	}
	
	private class ValidateFormButtonOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			mValidationManager.validateAllAndSetError();
		}
	}
	
	
}
