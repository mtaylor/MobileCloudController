package org.dcdroid.ui.provideraccounts;

import java.util.Map;

import org.dcdroid.R;
import org.dcdroid.ui.DCDbAdapter;
import org.dcdroid.ui.Settings;
import org.dcdroid.ui.provideraccounts.ProviderAccount;
import org.dcdroid.ui.providers.Provider;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

public class ProviderAccountShow extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.show_provider_account);

    	if((getIntent().getExtras() != null) && (getIntent().getExtras().containsKey("providerAccountId")))
        {
    		Long providerAccountId = getIntent().getExtras().getLong("providerAccountId");
    		populateFields(providerAccountId);
        }
    }
    
    private void populateFields(Long providerAccountId) 
    {
    	DCDbAdapter dbHelper = new DCDbAdapter(this);
    	dbHelper.open();
    	ProviderAccount providerAccount = dbHelper.getProviderAcccount(providerAccountId);
    	Map<String, String> settings = dbHelper.getSettings();
    	dbHelper.close();
    	
    	if(settings.containsKey(Settings.DEFAULT_ACCOUNT))
    	{
    		Long default_account = Long.parseLong(settings.get(Settings.DEFAULT_ACCOUNT));
    		if(default_account.equals(providerAccountId))
    		{
    			((CheckBox) findViewById(R.id.default_account)).setChecked(true);
    		}
    	}
    	((TextView) findViewById(R.id.title)).setText(providerAccount.getName());
    	((CheckBox) findViewById(R.id.default_account)).setClickable(false);
		((TextView) findViewById(R.id.username)).setText(providerAccount.getUsername());
    }
}
