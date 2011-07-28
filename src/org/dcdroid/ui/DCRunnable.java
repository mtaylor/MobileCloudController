package org.dcdroid.ui;

import org.deltacloud.api.client.DeltaCloudClient;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.widget.Toast;

public class DCRunnable implements Runnable 
{
	public static int REFRESH_ACCOUNT = 0;

	public static int REFRESH_INSTANCES = 1;
	
	public static int REFRESH_IMAGES = 2;

	public static int REFRESH_PROFILES = 3;

	public static int REFRESH_REALMS = 4;

	public static int SEND_REQUEST = 5;

	public static int CREATE_INSTANCE = 6;

	private Context context;

	private int command;

	private String request;

	private String requestType;

	private Dialog diaglog;

	private long providerAccountId;

	private Toast toast;

	private Intent intent;

	private String imageId;

	private String hwpId;
	
	private String realmId;

	public DCRunnable(Context context, int command, long providerAccountId, Dialog dialog, Toast toast, Intent intent)
	{
		this.command = command;
		this.context = context;
		this.providerAccountId = providerAccountId;
		this.diaglog = dialog;
		this.toast = toast;
		this.intent = intent;
	}

	public DCRunnable(Context context, int command, String request, String requestType, Dialog dialog, long providerAccountId, Toast toast, Intent intent)
	{
		this.command = command;
		this.context = context;
		this.request = request;
		this.requestType = requestType;
		this.diaglog = dialog;
		this.providerAccountId = providerAccountId;
		this.toast = toast;
		this.intent = intent;
	}

	public DCRunnable(Context context, int command, String imageId, String hwpId, String realmId, Dialog dialog, long providerAccountId, Toast toast, Intent intent)
	{
		this.command = command;
		this.context = context;;
		this.diaglog = dialog;
		this.providerAccountId = providerAccountId;
		this.toast = toast;
		this.imageId = imageId;
		this.hwpId = hwpId;
		this.realmId = realmId;
		this.intent = intent;
	}
	@Override
	public void run() 
	{
		try
		{	
			DCDbAdapter db = new DCDbAdapter(context);
			db.open();
			String completeMessage = " Successfully Update";
			DeltaCloudClient client;
			switch(command)
			{
				case 0: 	db.refreshProviderAccount(providerAccountId);
							completeMessage = "Account" + completeMessage;
							break;
				case 1: 	db.refreshInstances(providerAccountId);
							completeMessage = "Instances" + completeMessage;
							break;
				case 2: 	db.refreshImages(providerAccountId);
							completeMessage = "Images" + completeMessage;
							break;
				case 3: 	db.refreshHardwareProfiles(providerAccountId);
							completeMessage = "Profiles" + completeMessage;
							break;
				case 4: 	db.refreshRealms(providerAccountId);
							completeMessage = "Realms" + completeMessage;
							break;
				case 5: 	client = db.createDCClient(providerAccountId);
							client.performAction(request, requestType);
							db.refreshInstances(providerAccountId);
							break;
				case 6: 	client = db.createDCClient(providerAccountId);
							client.createInstance(imageId, hwpId, realmId, "");
							db.refreshInstances(providerAccountId);
							break;
				default:	break;
			}
			db.close();
			diaglog.dismiss();
			toast.show();
			if(!(intent == null))
			{
				context.startActivity(intent);
			}
		}
		catch(Exception e)
		{
			diaglog.dismiss();
			//showAlertBox(e.getMessage());
		}
	}

    private void showAlertBox(String message)
    {	
	    AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
	    alertbox.setMessage(message);
	    alertbox.setNeutralButton("OK", null);
	    alertbox.show();
	}
}
