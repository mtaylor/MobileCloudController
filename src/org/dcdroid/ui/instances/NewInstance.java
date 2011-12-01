package org.dcdroid.ui.instances;

import org.dcdroid.R;
import org.dcdroid.api.HardwareProfile;
import org.dcdroid.api.Image;
import org.dcdroid.api.Realm;
import org.dcdroid.ui.DCDbAdapter;
import org.dcdroid.ui.DCRunnable;
import org.dcdroid.ui.hardwareprofiles.HardwareProfiles;
import org.dcdroid.ui.provideraccounts.ProviderAccount;
import org.deltacloud.api.client.DeltaCloudClient;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NewInstance extends Activity implements OnClickListener
{
    private boolean createInstance;
    
    private Long providerAccountId;
    
    private Long imageId;
    
    private Long hardwareProfileId;

    private Long realmId;

    private String imageExternalId;
    
    private String hardwareProfileExternalId;
    
    private String realmExternalId;


    protected void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.new_instance);

    	if((getIntent().getExtras() != null) && (getIntent().getExtras().containsKey(DCDbAdapter.PROVIDER_ACCOUNTS_ID)))
        {
        	createInstance = getIntent().getExtras().containsKey("CreateInstance");
        	if(createInstance)
        	{
        		providerAccountId = getIntent().getExtras().getLong(DCDbAdapter.PROVIDER_ACCOUNTS_ID);
            	imageId = getIntent().getExtras().getLong("ImageId");
            	hardwareProfileId = getIntent().getExtras().getLong("HardwareProfileId");
            	realmId = getIntent().getExtras().getLong("RealmId");
            	populateFields();
        	}
        }
    }
    
    private void populateFields() 
    {    	
    	setTitle("Create Instance");
    	DCDbAdapter dbHelper = new DCDbAdapter(this);
    	dbHelper.open();
    	Realm realm = dbHelper.getRealm(realmId);
    	HardwareProfile hardwareProfile = dbHelper.getHardwareProfile(hardwareProfileId);
    	Image image = dbHelper.getImage(imageId);

    	imageExternalId = image.getId();
    	hardwareProfileExternalId = hardwareProfile.getId();
    	realmExternalId = realm.getId();

    	((TextView) findViewById(R.id.image)).setText(image.getName());
    	((TextView) findViewById(R.id.hardware_profile)).setText(hardwareProfile.getName());
    	((TextView) findViewById(R.id.realm)).setText(realm.getName());
    	((Button) findViewById(R.id.confirmButton)).setOnClickListener(this);
    	((Button) findViewById(R.id.cancelButton)).setOnClickListener(this);
        dbHelper.close();
    }

    private void createInstance()
    {
    	Intent intent = new Intent(this, Instances.class);
    	intent.putExtra(DCDbAdapter.PROVIDER_ACCOUNTS_ID, providerAccountId);

    	ProgressDialog progressBar = ProgressDialog.show(this, null, "Creating Instance...");
    	Toast toast = Toast.makeText(this, "Instance Create Request Sent", Toast.LENGTH_LONG);

    	DCRunnable dcr = new DCRunnable(this, DCRunnable.CREATE_INSTANCE, imageExternalId, hardwareProfileExternalId,
    			realmExternalId, progressBar, providerAccountId, toast, intent);
    	Thread bgThread = new Thread(dcr);
    	bgThread.start();
    }


	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
			case R.id.confirmButton:
				try
				{
					createInstance();
					break;
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			case R.id.cancelButton: Toast toast = Toast.makeText(this, "Create Instance Cancelled", Toast.LENGTH_SHORT);
									toast.show();
		}
		Intent i = new Intent(this, Instances.class);
		i.putExtra(DCDbAdapter.PROVIDER_ACCOUNTS_ID, providerAccountId);
        startActivityForResult(i, 0);
	}
}
