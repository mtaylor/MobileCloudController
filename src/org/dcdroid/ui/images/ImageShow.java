package org.dcdroid.ui.images;

import org.dcdroid.R;
import org.dcdroid.api.Image;
import org.dcdroid.ui.DCDbAdapter;
import org.dcdroid.ui.hardwareprofiles.HardwareProfiles;
import org.dcdroid.ui.instances.Instances;
import org.dcdroid.ui.provideraccounts.ProviderAccount;
import org.dcdroid.ui.realms.Realms;

import android.app.Activity;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ImageShow extends Activity implements OnClickListener
{
	private boolean createInstance;

	private Long providerAccountId;

	private Long imageId;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.show_image);

    	if((getIntent().getExtras() != null) && (getIntent().getExtras().containsKey(DCDbAdapter.PROVIDER_ACCOUNTS_ID)))
        {
        	providerAccountId = getIntent().getExtras().getLong(DCDbAdapter.PROVIDER_ACCOUNTS_ID);
        	if((getIntent().getExtras() != null) && (getIntent().getExtras().containsKey(DCDbAdapter.IMAGE_ID)))
            {
            	imageId = getIntent().getExtras().getLong("ImageId");
                createInstance = getIntent().getExtras().containsKey("CreateInstance");
                populateFields(imageId, providerAccountId, createInstance);
            }
        }
    }
    
    private void populateFields(Long imageId, Long providerAccountId, boolean createInstance) 
    {    	
    	DCDbAdapter dbHelper = new DCDbAdapter(this);
    	dbHelper.open();
    	Image image = dbHelper.getImage(imageId);
    	ProviderAccount account = dbHelper.getProviderAcccount(providerAccountId);
        dbHelper.close();

        if(createInstance)
        {
            setTitle("Create Instance: Select Image");
            fillProperties(image, true);
        }
        else
        {
            setTitle("Show Image");
            fillProperties(image, false);
        }

    }
    
    private void fillProperties(Image i, boolean createInstance)
    {
    	((TextView) findViewById(R.id.title)).setText(i.getName());
		((TextView) findViewById(R.id.description)).setText(i.getDescription());
		((TextView) findViewById(R.id.architecture)).setText(i.getArchitecture());
		((TextView) findViewById(R.id.owner)).setText(i.getOwnerId());
		if(!createInstance)
		{
			((Button) findViewById(R.id.confirmButton)).setVisibility(View.INVISIBLE);
		}
		else
		{
			((Button) findViewById(R.id.confirmButton)).setOnClickListener(this);
		}
    }

	@Override
	public void onClick(View v) 
	{
		Intent i;
		switch (v.getId()) 
		{
			case R.id.confirmButton:
				i = new Intent(this, HardwareProfiles.class);
				break;
			default:
				return;
		}
		i.putExtra(DCDbAdapter.PROVIDER_ACCOUNTS_ID, providerAccountId);
		i.putExtra("ImageId", imageId);
		i.putExtra("CreateInstance", true);
        startActivityForResult(i, 0);
	}
}
