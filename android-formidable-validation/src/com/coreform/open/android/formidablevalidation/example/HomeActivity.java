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
        
        example2Button.setVisibility(View.GONE);	//hide until the example is working
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