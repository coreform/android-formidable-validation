package com.coreform.open.android.formidablevalidation.example;

import com.coreform.open.android.formidablevalidation.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeActivity extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_home);
        
        //find views
        Button example1Button = (Button) findViewById(R.id.example1Button);
        Button example2Button = (Button) findViewById(R.id.example2Button);
        
        //set OnClickListeners
        example1Button.setOnClickListener(new ExampleButtonOnClickListener(1));
        example2Button.setOnClickListener(new ExampleButtonOnClickListener(2));
    }
    
    /*
     * METHODS
     */
    
    private void moveActivity(Intent intent) {
    	startActivity(intent);
    }
    
    /*
     * INNER CLASSES
     */
    
    private class ExampleButtonOnClickListener implements OnClickListener {
    	private int mExampleNumber;
    	public ExampleButtonOnClickListener(int exampleNumber) {
    		mExampleNumber = exampleNumber;
    	}
		@Override
		public void onClick(View v) {
			switch(mExampleNumber) {
				case 1:
					moveActivity(new Intent(HomeActivity.this, AddressActivity.class));
					break;
				case 2:
					moveActivity(new Intent(HomeActivity.this, ColourPickerActivity.class));
					break;
			}
		}
    }
}