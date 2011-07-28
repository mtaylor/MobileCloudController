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

package org.dcdroid.ui;

import java.util.Map;

import org.dcdroid.R;
import org.dcdroid.ui.hardwareprofiles.HardwareProfiles;
import org.dcdroid.ui.images.Images;
import org.dcdroid.ui.instances.Instances;
import org.dcdroid.ui.provideraccounts.ProviderAccount;
import org.dcdroid.ui.providers.Provider;
import org.dcdroid.ui.providers.ProviderShow;
import org.dcdroid.ui.providers.Providers;
import org.dcdroid.ui.realms.Realms;
import org.deltacloud.api.client.DeltaCloudClientException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.TextView;

public class Home extends Activity implements OnClickListener
{
	public static final int DIALOG_ERROR = 0;
	
	public static final int DIALOG_PROGRESS = 1;
	
	public static final int PROVIDERS_ID = 0;

	public static final int REFRESH_ID = 1;
	
	private Long providerAccountId;

	private DCDbAdapter dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        
        TextView message = (TextView) findViewById(R.id.message);
        Button instancesButton = (Button) findViewById(R.id.buttonInstances);
        Button imagesButton = (Button) findViewById(R.id.buttonImages);
        Button realmsButton = (Button) findViewById(R.id.buttonRealms);
        Button profilesButton = (Button) findViewById(R.id.buttonProfiles);
        
        dbHelper = new DCDbAdapter(this);
        dbHelper.open();
        Map<String, String> settings = dbHelper.getSettings();
        
        if((getIntent().getExtras() != null) && (getIntent().getExtras().containsKey(DCDbAdapter.PROVIDER_ACCOUNTS_ID)))
        {
        	providerAccountId = getIntent().getExtras().getLong(DCDbAdapter.PROVIDER_ACCOUNTS_ID);
        }
        else if (settings.containsKey(Settings.DEFAULT_ACCOUNT))
        {
        	providerAccountId = Long.parseLong(settings.get(Settings.DEFAULT_ACCOUNT));
        }
    		
    	if(providerAccountId != null)
    	{
    		dbHelper.open();
    		ProviderAccount providerAccount = dbHelper.getProviderAcccount(providerAccountId);
    		Provider provider = dbHelper.getProvider(providerAccount.getProviderId());
    		dbHelper.close();
    		
    		message.setText(provider.getName() + " : " + providerAccount.getName());
    		
    		instancesButton.setOnClickListener(this);
    		imagesButton.setOnClickListener(this);
        	realmsButton.setOnClickListener(this);
        	profilesButton.setOnClickListener(this);
    	}
        else
        {
        	instancesButton.setEnabled(false);
        	imagesButton.setEnabled(false);
        	realmsButton.setEnabled(false);
        	profilesButton.setEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        super.onCreateOptionsMenu(menu);
        menu.add(0, PROVIDERS_ID, 0, R.string.providers);
        menu.add(0, REFRESH_ID, 1, R.string.refresh);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) 
    {
        switch(item.getItemId()) 
        {
            case PROVIDERS_ID:
            	Intent i = new Intent(this, Providers.class);
            	startActivityForResult(i, 0);
                return true;
            case REFRESH_ID:
            	refreshProviderAccount();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    public void refreshProviderAccount()
    {
    	Intent intent = new Intent(this, Instances.class);
    	intent.putExtra(DCDbAdapter.PROVIDER_ACCOUNTS_ID, providerAccountId);

    	ProgressDialog dialog = ProgressDialog.show(this, null, "Refreshing Account...");
    	Toast toast = Toast.makeText(this, "Account Successfully Updated", Toast.LENGTH_LONG);

    	DCRunnable dcr = new DCRunnable(this, DCRunnable.REFRESH_ACCOUNT, providerAccountId, dialog, toast, null);
    	Thread bgThread = new Thread(dcr);
    	bgThread.start();
    }

	@Override
	public void onClick(View v) 
	{
		Intent i;
		switch (v.getId()) 
		{
			case R.id.buttonInstances:
				i = new Intent(this, Instances.class);
				break;
			case R.id.buttonImages:
				i = new Intent(this, Images.class);
				break;
			case R.id.buttonRealms:
				i = new Intent(this, Realms.class);
				break;
			case R.id.buttonProfiles:
				i = new Intent(this, HardwareProfiles.class);
				break;
			default:
				return;
		}
		i.putExtra(DCDbAdapter.PROVIDER_ACCOUNTS_ID, providerAccountId);
        startActivityForResult(i, 0);
	}
}
