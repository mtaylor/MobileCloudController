package org.dcdroid.ui.realms;

import org.dcdroid.R;
import org.dcdroid.api.Image;
import org.dcdroid.api.Realm;
import org.dcdroid.ui.DCDbAdapter;
import org.dcdroid.ui.hardwareprofiles.HardwareProfiles;
import org.dcdroid.ui.instances.NewInstance;
import org.dcdroid.ui.provideraccounts.ProviderAccount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RealmShow extends Activity implements OnClickListener
{
    private boolean createInstance;
    
    private Long providerAccountId;
    
    private Long imageId;
    
    private Long hardwareProfileId;

    private Long realmId;


    protected void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.show_realm);

    	if((getIntent().getExtras() != null) && (getIntent().getExtras().containsKey(DCDbAdapter.PROVIDER_ACCOUNTS_ID)))
        {
        	createInstance = getIntent().getExtras().containsKey("CreateInstance");
        	if(createInstance)
        	{
            	imageId = getIntent().getExtras().getLong("ImageId");
            	hardwareProfileId = getIntent().getExtras().getLong("HardwareProfileId");
        	}

        	providerAccountId = getIntent().getExtras().getLong(DCDbAdapter.PROVIDER_ACCOUNTS_ID);
        	if(getIntent().getExtras().containsKey(DCDbAdapter.REALM_ID))
            {
            	realmId = getIntent().getExtras().getLong("RealmId");
            	populateFields(realmId, providerAccountId);
            }
        }
    }
    
    private void populateFields(Long realmId, Long providerAccountId) 
    {    	
    	DCDbAdapter dbHelper = new DCDbAdapter(this);
    	dbHelper.open();
    	Realm realm = dbHelper.getRealm(realmId);
    	ProviderAccount account = dbHelper.getProviderAcccount(providerAccountId);
        dbHelper.close();

        fillProperties(realm);
    }
    
    private void fillProperties(Realm r)
    {
    	((TextView) findViewById(R.id.title)).setText(r.getName());
    	String limit = r.getLimit().equals("") ? "N\\A" :  r.getLimit();
		((TextView) findViewById(R.id.limit)).setText(limit);
		if(!createInstance)
		{
			((Button) findViewById(R.id.confirmButton)).setVisibility(View.INVISIBLE);
			setTitle("Show Realm");
		}
		else
		{
			((Button) findViewById(R.id.confirmButton)).setOnClickListener(this);
			setTitle("Create Instance: Select Realm");
		}
    }


	public void onClick(View v) 
	{
		Intent i;
		switch (v.getId()) 
		{
			case R.id.confirmButton:
				i = new Intent(this, NewInstance.class);
				break;
			default:
				return;
		}
		i.putExtra(DCDbAdapter.PROVIDER_ACCOUNTS_ID, providerAccountId);
		i.putExtra("ImageId", imageId);
		i.putExtra("HardwareProfileId", hardwareProfileId);
		i.putExtra("RealmId", realmId);
		i.putExtra("CreateInstance", true);
        startActivityForResult(i, 0);
	}
}
