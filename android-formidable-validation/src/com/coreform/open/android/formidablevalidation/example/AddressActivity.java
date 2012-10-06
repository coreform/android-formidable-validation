package com.coreform.open.android.formidablevalidation.example;

import java.util.HashMap;
import java.util.Map.Entry;

import com.coreform.open.android.formidablevalidation.CheckBoxCheckedDependencyValidator;
import com.coreform.open.android.formidablevalidation.FinalValidationResult;
import com.coreform.open.android.formidablevalidation.R;
import com.coreform.open.android.formidablevalidation.RegExpressionValueValidator;
import com.coreform.open.android.formidablevalidation.SetErrorAbleSpinner;
import com.coreform.open.android.formidablevalidation.SpinnerRequiredValueValidator;
import com.coreform.open.android.formidablevalidation.ValidationManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

public class AddressActivity extends Activity {
	private static final boolean DEBUG = true;
	private static final String TAG = "AddressActivity";
	
	private static final int REQUEST_COLOURPICKER = 101;
	
	private ValidationManager mValidationManager;
	
	private ScrollView mContentScrollView;
	private EditText mAddressLine1EditText;
	private EditText mAddressLine2EditText;
	private EditText mCityEditText;
	private EditText mProvinceEditText;
	private SetErrorAbleSpinner mCountrySpinner;
	private EditText mPhoneEditText;
	private EditText mEmailEditText;
	private CheckBox mSignupNewsletterCheckBox;
	private Button mFavouriteColourButton;
	private Button mClearFormButton;
	private Button mValidateFormButton;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_form_address);
        
        mContentScrollView = (ScrollView) findViewById(R.id.contentScrollView);
        mAddressLine1EditText = (EditText) findViewById(R.id.field01EditText);
    	mAddressLine2EditText = (EditText) findViewById(R.id.field02EditText);
    	mCityEditText = (EditText) findViewById(R.id.field03EditText);
    	mProvinceEditText = (EditText) findViewById(R.id.field04EditText);
    	mCountrySpinner = (SetErrorAbleSpinner) findViewById(R.id.field05Spinner);
    	mPhoneEditText = (EditText) findViewById(R.id.field06EditText);
    	mEmailEditText = (EditText) findViewById(R.id.field07EditText);
    	mSignupNewsletterCheckBox = (CheckBox) findViewById(R.id.field08CheckBox);
    	mFavouriteColourButton = (Button) findViewById(R.id.field09Button);
    	mClearFormButton = (Button) findViewById(R.id.clearFormButton);
    	mValidateFormButton = (Button) findViewById(R.id.validateFormButton);
    	
    	mFavouriteColourButton.setOnClickListener(new FavouriteColourButtonOnClickListener());
    	mValidateFormButton.setOnClickListener(new ValidateFormButtonOnClickListener());
    	
    	mCountrySpinner.setErrorPopupPadding(12, 12, 12, 12);
    	
    	//setup validation
    	mValidationManager = new ValidationManager();
    	
    	mValidationManager.add("addressLine1");
    	mValidationManager.add("addressLine1", new RegExpressionValueValidator(mAddressLine1EditText, "^[a-zA-Z]{3}$", "please enter your address."));
    	mValidationManager.add("signupNewsletter");
    	mValidationManager.add("countrySpinner");
    	mValidationManager.add("countrySpinner", new SpinnerRequiredValueValidator(mCountrySpinner, "please select your country (haha you can't)."));
    	mValidationManager.add("emailAddress");
    	mValidationManager.add("emailAddress", new CheckBoxCheckedDependencyValidator(mEmailEditText, "signupNewsletter", mSignupNewsletterCheckBox, true, false, "Please enter your email address to signup to the newsletter list."));
    	mValidationManager.add("emailAddress", new RegExpressionValueValidator(mEmailEditText, "^([0-9a-zA-Z]([-\\.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})$", "Email address must be valid."));
    	mValidationManager.add("favouriteColour");
    	mValidationManager.add("favouriteColour", new ColourPickerButtonValueValidator(mFavouriteColourButton, true));
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
		if(requestCode == REQUEST_COLOURPICKER) {
			if(resultCode == RESULT_OK) {
				int colourResource = data.getIntExtra("colour", 0);
				if(colourResource > 0) {
					mFavouriteColourButton.setText(null);
					mFavouriteColourButton.setBackgroundResource(colourResource);
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
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(AddressActivity.this, ColourPickerActivity.class);
			startActivityForResult(intent, REQUEST_COLOURPICKER);
		}
	}
	
	private class ValidateFormButtonOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			/*
			Spannable error1Text = new SpannableStringBuilder("Please enter your address.");
			mAddressLine1EditText.setError(error1Text);
			
			Spannable error2Text = new SpannableStringBuilder("Please select your country.");
			mCountrySpinner.setError(error2Text);
			*/
			mValidationManager.validateAllAndSetError(mContentScrollView);
		}
	}
}
