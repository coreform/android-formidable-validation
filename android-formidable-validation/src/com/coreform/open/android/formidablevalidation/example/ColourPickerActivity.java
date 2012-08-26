package com.coreform.open.android.formidablevalidation.example;

import com.coreform.open.android.formidablevalidation.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ColourPickerActivity extends Activity {
	
	public static final int COLOUR_RED = 101;
	public static final int COLOUR_GREEN = 111;
	public static final int COLOUR_BLUE = 121;
	public static final int COLOUR_YELLOW = 131;
	public static final int COLOUR_PURPLE = 141;
	public static final int COLOUR_ORANGE = 151;
	
	private Button mRedButton;
	private Button mGreenButton;
	private Button mBlueButton;
	private Button mYellowButton;
	private Button mPurpleButton;
	private Button mOrangeButton;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_colourpicker);
        
        mRedButton = (Button) findViewById(R.id.redButton);
    	mGreenButton = (Button) findViewById(R.id.greenButton);
    	mBlueButton = (Button) findViewById(R.id.blueButton);
    	mYellowButton = (Button) findViewById(R.id.yellowButton);
    	mPurpleButton = (Button) findViewById(R.id.purpleButton);
    	mOrangeButton = (Button) findViewById(R.id.orangeButton);
    	
    	mRedButton.setOnClickListener(new ColourButtonOnClickListener(COLOUR_RED));
    	mGreenButton.setOnClickListener(new ColourButtonOnClickListener(COLOUR_GREEN));
    	mBlueButton.setOnClickListener(new ColourButtonOnClickListener(COLOUR_BLUE));
    	mYellowButton.setOnClickListener(new ColourButtonOnClickListener(COLOUR_YELLOW));
    	mPurpleButton.setOnClickListener(new ColourButtonOnClickListener(COLOUR_PURPLE));
    	mOrangeButton.setOnClickListener(new ColourButtonOnClickListener(COLOUR_ORANGE));
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	/*
	 * METHODS
	 */
	
	private int getChosenColour(int colourCode) {
		int colourResource = R.color.red;	//default
		switch(colourCode) {
			case COLOUR_RED:
				break;
			case COLOUR_GREEN:
				colourResource = R.color.green;
				break;
			case COLOUR_BLUE:
				colourResource = R.color.blue;
				break;
			case COLOUR_YELLOW:
				colourResource = R.color.yellow;
				break;
			case COLOUR_PURPLE:
				colourResource = R.color.purple;
				break;
			case COLOUR_ORANGE:
				colourResource = R.color.orange;
				break;
		}
		return colourResource;
	}
	
	/*
	 * INNER CLASSES
	 */
	
	private class ColourButtonOnClickListener implements OnClickListener {
		private int mColourCode;
		public ColourButtonOnClickListener(int colourCode) {
			mColourCode = colourCode;
		}
		@Override
		public void onClick(View v) {
			Intent resultIntent = new Intent();
			resultIntent.putExtra("colour", getChosenColour(mColourCode));
			ColourPickerActivity.this.setResult(RESULT_OK, resultIntent);
			ColourPickerActivity.this.finish();
		}
		
	}
}
