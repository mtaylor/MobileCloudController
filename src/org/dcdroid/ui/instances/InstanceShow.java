package org.dcdroid.ui.instances;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dcdroid.R;
import org.dcdroid.api.Instance;
import org.dcdroid.api.Link;
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
import android.text.Layout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InstanceShow extends Activity implements OnClickListener
{
	private Map <Integer, String> links;

	private Long providerAccountId;

	private Long instanceId;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.show_instance);

    	if((getIntent().getExtras() != null) && (getIntent().getExtras().containsKey(DCDbAdapter.PROVIDER_ACCOUNTS_ID)))
        {
        	providerAccountId = getIntent().getExtras().getLong(DCDbAdapter.PROVIDER_ACCOUNTS_ID);
        	if((getIntent().getExtras() != null) && (getIntent().getExtras().containsKey("InstanceId")))
            {
            	instanceId = getIntent().getExtras().getLong("InstanceId");
            	populateFields(instanceId, providerAccountId);
            }
        }
    }
    
    private void populateFields(Long instanceId, Long providerAccountId) 
    {
    	setTitle("Show Instance");
    	TextView titleText = (TextView) findViewById(R.id.title);

    	DCDbAdapter dbHelper = new DCDbAdapter(this);
    	dbHelper.open();
    	Instance instance = dbHelper.getInstance(instanceId);
    	ProviderAccount account = dbHelper.getProviderAcccount(providerAccountId);
        dbHelper.close();

        titleText.setText(instance.getId());
        fillProperties(instance);
    }
    
    private void fillProperties(Instance i)
    {
    	links = new HashMap<Integer, String>();
    	((TextView) findViewById(R.id.owner)).setText(i.getOwnerId());
    	((TextView) findViewById(R.id.state)).setText(i.getState());
    	((TextView) findViewById(R.id.realm)).setText(i.getRealm().getName());
    	((TextView) findViewById(R.id.hardware_profile)).setText(i.getHardwareProfile().getName());
    	((TextView) findViewById(R.id.image)).setText(i.getImage().getName());

    	RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
    	

    	int id = 9999;
    	for(Link link : i.getActions().getLinks())
    	{
    		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
    		if(id == 9999)
    		{
    			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    		}
    		else
    		{
    			params.addRule(RelativeLayout.ABOVE, id);
    		}
    		id --;
    		Button button = new Button(this);
    		button.setText(link.getRel());
    		button.setLayoutParams(params);
    		button.setId(id);
    		button.setOnClickListener(this);
    		links.put(button.getId(), link.getHref());
    		layout.addView(button);
    	}
    }


	public void onClick(View v) 
	{
		sendRequest(links.get(v.getId()));
	}

	private void sendRequest(String request)
	{
		Intent intent = new Intent(this, Instances.class);
		intent.putExtra(DCDbAdapter.PROVIDER_ACCOUNTS_ID, providerAccountId);
    	ProgressDialog progressBar = ProgressDialog.show(this, null, "Sending Request...");
    	Toast toast = Toast.makeText(this, "Request Sent", Toast.LENGTH_SHORT);

    	DCRunnable dcr = new DCRunnable(this, DCRunnable.SEND_REQUEST, request, "put", progressBar,  providerAccountId, toast, intent);
    	Thread bgThread = new Thread(dcr);
    	bgThread.start();
	}
}
