package org.dcdroid.ui.providers;

import org.dcdroid.R;
import org.dcdroid.ui.DCDbAdapter;
import org.dcdroid.ui.provideraccounts.ProviderAccount;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ProviderShow extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.show_provider);

    	if((getIntent().getExtras() != null) && (getIntent().getExtras().containsKey(DCDbAdapter.PROVIDER_ID)))
        {
        	Long providerId = getIntent().getExtras().getLong(DCDbAdapter.PROVIDER_ID);
        	populateFields(providerId);
        }
    }
    
    private void populateFields(Long providerId) 
    {
    	TextView titleText = (TextView) findViewById(R.id.title);
    	
    	DCDbAdapter dbHelper = new DCDbAdapter(this);
    	dbHelper.open();
    	Provider provider = dbHelper.getProvider(providerId);
        dbHelper.close();

        titleText.setText(provider.getName());
        fillProperties(provider);
    }
    
    private void fillProperties(Provider p)
    {
		((TextView) findViewById(R.id.url)).setText(p.getUrl());
    }
}
