package org.dcdroid.ui.hardwareprofiles;

import org.dcdroid.R;
import org.dcdroid.api.HardwareProfile;
import org.dcdroid.api.Property;
import org.dcdroid.ui.DCDbAdapter;
import org.dcdroid.ui.provideraccounts.ProviderAccount;
import org.dcdroid.ui.providers.Provider;
import org.dcdroid.ui.realms.Realms;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

public class HardwareProfileShow extends Activity implements OnClickListener
{
	private boolean createInstance;

	private Long providerAccountId;

	private Long imageId;	

	private Long hardwareProfileId;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.show_hardware_profile);

    	if((getIntent().getExtras() != null) && (getIntent().getExtras().containsKey(DCDbAdapter.PROVIDER_ACCOUNTS_ID)))
        {
        	providerAccountId = getIntent().getExtras().getLong(DCDbAdapter.PROVIDER_ACCOUNTS_ID);
            hardwareProfileId = getIntent().getExtras().getLong("HardwareProfileId");

        	if(getIntent().getExtras().containsKey("ImageId"))
        	{
        		imageId = getIntent().getExtras().getLong("ImageId");
        	}

            createInstance = getIntent().getExtras().containsKey("CreateInstance");
            populateFields(hardwareProfileId, providerAccountId, createInstance);
        }
    }
    
    private void populateFields(Long hardwareProfileId, Long providerAccountId, boolean createInstance) 
    {
    	if(!createInstance)
    	{
    		setTitle("Show Profile");
    	}
    	else
    	{
    		setTitle("Create Instance: Select Profile");
    	}
    	
    	DCDbAdapter dbHelper = new DCDbAdapter(this);
    	dbHelper.open();
    	HardwareProfile hwp = dbHelper.getHardwareProfile(hardwareProfileId);
    	ProviderAccount account = dbHelper.getProviderAcccount(providerAccountId);
    	//Provider provider = dbHelper.getProvider(account.getProviderId());
        dbHelper.close();

        fillProperties(hwp);
    }
    
    private void fillProperties(HardwareProfile hwp)
    {
    	((TextView) findViewById(R.id.title)).setText(hwp.getName().toUpperCase());
        for(Property p : hwp.getProperties())
        {
	    	if(p.getName().toLowerCase().equals("memory"))
	    	{
	    		((TextView) findViewById(R.id.memory_label)).setText(p.getName().toUpperCase());
	    		((TextView) findViewById(R.id.memory)).setText(p.getRangeEnumValueAsString());
	    	}
	    	else  if(p.getName().toLowerCase().equals("cpu"))
	    	{
	    		((TextView) findViewById(R.id.cpu_label)).setText(p.getName().toUpperCase());
	    		((TextView) findViewById(R.id.cpu)).setText(p.getRangeEnumValueAsString());
	    	}
	    	else  if(p.getName().toLowerCase().equals("storage"))
	    	{
	    		((TextView) findViewById(R.id.storage_label)).setText(p.getName().toUpperCase());
	    		((TextView) findViewById(R.id.storage)).setText(p.getRangeEnumValueAsString());
	    	}
	    	else  if(p.getName().toLowerCase().equals("architecture"))
	    	{
	    		((TextView) findViewById(R.id.architecture_label)).setText(p.getName().toUpperCase());
	    		((TextView) findViewById(R.id.architecture)).setText(p.getRangeEnumValueAsString());
	    	}
			if(!createInstance)
			{
				((Button) findViewById(R.id.confirmButton)).setVisibility(View.INVISIBLE);
			}
			else
			{
				((Button) findViewById(R.id.confirmButton)).setOnClickListener(this);
			}
        }
    }

	@Override
	public void onClick(View v) 
	{
		Intent i;
		switch (v.getId()) 
		{
			case R.id.confirmButton:
				i = new Intent(this, Realms.class);
				break;
			default:
				return;
		}
		i.putExtra(DCDbAdapter.PROVIDER_ACCOUNTS_ID, providerAccountId);
		i.putExtra("ImageId", imageId);
		i.putExtra("HardwareProfileId", hardwareProfileId);
		i.putExtra("CreateInstance", true);
        startActivityForResult(i, 0);		
	}
}
