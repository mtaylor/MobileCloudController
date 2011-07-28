/*
 * Copyright (C) 2008 Google Inc.
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

package org.dcdroid.ui.providers;

import org.dcdroid.R;
import org.dcdroid.R.id;
import org.dcdroid.R.layout;
import org.dcdroid.ui.DCDbAdapter;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ProviderEdit extends Activity {

    private EditText nameText;
    private EditText urlText;
    private TextView titleText;
    private Long providerId;
    
    private DCDbAdapter dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.edit_provider);

    	titleText = (TextView) findViewById(R.id.title);
    	titleText.setText("Edit Provider");
    	nameText = (EditText) findViewById(R.id.name);
    	urlText = (EditText) findViewById(R.id.url);

    	Button saveButton = (Button) findViewById(R.id.save);
    	if((getIntent().getExtras() != null) && (getIntent().getExtras().containsKey(DCDbAdapter.PROVIDER_ID)))
        {
        	providerId = getIntent().getExtras().getLong(DCDbAdapter.PROVIDER_ID);
        	populateFields();
        }
    	saveButton.setOnClickListener(new View.OnClickListener() 
    	{
    	    public void onClick(View view)
    	    {
    	        setResult(RESULT_OK);
    	        finish();
    	    }
    	});
    }
    
    private void populateFields() 
    {
    	if(providerId != null)
    	{
	    	dbHelper = new DCDbAdapter(this);
	    	dbHelper.open();
	        Provider provider = dbHelper.getProvider(providerId);
	        titleText.setText(provider.getName());
	        nameText.setText(provider.getName());
	        urlText.setText(provider.getUrl());
	        dbHelper.close();
    	}
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) 
    {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(DCDbAdapter.PROVIDER_ID, providerId);
    }
    
    @Override
    protected void onPause() 
    {
        super.onPause();
        saveState();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }     
    
    private void saveState() 
    {
        String name = nameText.getText().toString();
        String url = urlText.getText().toString();

    	dbHelper = new DCDbAdapter(this);
    	dbHelper.open();
        if (providerId == null) 
        {
            long id = dbHelper.createProvider(name, url);
            if (id > 0) 
            {
                providerId = id;
            }
        } 
        else 
        {
            dbHelper.updateProvider(providerId, name, url);
        }
        dbHelper.close();
    }
}
