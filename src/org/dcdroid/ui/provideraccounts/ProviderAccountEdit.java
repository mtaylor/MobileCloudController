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

package org.dcdroid.ui.provideraccounts;

import java.util.Map;

import org.dcdroid.R;
import org.dcdroid.ui.DCDbAdapter;
import org.dcdroid.ui.Settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class ProviderAccountEdit extends Activity 
{
	private TextView titleText;
    private EditText nameText;
    private EditText usernameText;
    private TextView passwordText;
    private CheckBox defaultCheck;

    private Long providerAccountId;
    private Long providerId;
    private DCDbAdapter dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.edit_provider_account);

    	titleText = (TextView) findViewById(R.id.title);
    	titleText.setText("Provider Account");

    	nameText = (EditText) findViewById(R.id.name);
    	usernameText = (EditText) findViewById(R.id.username);
    	passwordText = (EditText) findViewById(R.id.password);
    	defaultCheck = (CheckBox) findViewById(R.id.default_account);

    	dbHelper = new DCDbAdapter(this);

    	Button saveButton = (Button) findViewById(R.id.save);

    	if((getIntent().getExtras() != null) && (getIntent().getExtras().containsKey("providerId")))
        {
        	providerId = getIntent().getExtras().getLong("providerId");
        }
    	else if((getIntent().getExtras() != null) && (getIntent().getExtras().containsKey("providerAccountId")))
        {
    		providerAccountId = getIntent().getExtras().getLong("providerAccountId");
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
    	if(providerAccountId != null)
    	{
        	setTitle("Edit Provider Account");
	    	dbHelper.open();
	        ProviderAccount pa = dbHelper.getProviderAcccount(providerAccountId);
	        Map<String, String> settings = dbHelper.getSettings();
	        dbHelper.close();

	        titleText.setText(pa.getName());
	        nameText.setText(pa.getName());
	        usernameText.setText(pa.getUsername());
	        passwordText.setText(pa.getPassword());
	        
	        if(settings.containsKey(Settings.DEFAULT_ACCOUNT))
	        {
	        	Long defaultAccount = Long.parseLong(settings.get(Settings.DEFAULT_ACCOUNT));
	        	if(defaultAccount.equals(providerAccountId))
	        	{
	        		defaultCheck.setChecked(true);
	        	}
	        }
    	}
    	else
    	{
        	setTitle("New Provider Account");
    		defaultCheck.setChecked(true);
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
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        boolean defaultAccount = defaultCheck.isChecked();

    	dbHelper.open();
        if (providerAccountId == null) 
        {
            long id = dbHelper.createProviderAccount(name, username, password, providerId);
            if (id > 0) 
            {
                providerAccountId = id;
            }
        } 
        else 
        {
            dbHelper.updateProviderAccount(providerAccountId, name, username, password);
        }
        
        if(defaultAccount)
        {
        	dbHelper.createSetting(Settings.DEFAULT_ACCOUNT, providerAccountId.toString());
        }
        dbHelper.close();
    }
}
