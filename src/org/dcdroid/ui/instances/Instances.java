package org.dcdroid.ui.instances;

import org.dcdroid.R;
import org.dcdroid.ui.DCRunnable;
import org.dcdroid.ui.DCDbAdapter;
import org.dcdroid.ui.images.Images;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import android.widget.Toast;

public class Instances extends ListActivity 
{
	private static final int INSERT_ID = 0;

	private static final int REFRESH_ID = 1;

	private Long providerAccountId;
	
	private ProgressDialog progressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        setTitle("Instances");
        
        if(providerAccountId != null || ((getIntent().getExtras() != null) && (getIntent().getExtras().containsKey(DCDbAdapter.PROVIDER_ACCOUNTS_ID))))
        {
        	providerAccountId = getIntent().getExtras().getLong(DCDbAdapter.PROVIDER_ACCOUNTS_ID);
        	fillData();
        	registerForContextMenu(getListView());
        }
    }

    @Override
    public void onResume()
    {
    	super.onResume();
    	fillData();
    }

    private void fillData()
    {
    	DCDbAdapter dbHelper = new DCDbAdapter(this);
        dbHelper.open();
        
        Cursor notesCursor = dbHelper.fetchAllInstances(providerAccountId);
        startManagingCursor(notesCursor);

        String[] from = new String[]{DCDbAdapter.INSTANCE_NAME};
        int[] to = new int[]{R.id.list_row_text};

        SimpleCursorAdapter accounts = new SimpleCursorAdapter(this, R.layout.list_row, notesCursor, from, to);
        setListAdapter(accounts);
        
        dbHelper.close();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) 
    {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, InstanceShow.class);
        i.putExtra("InstanceId", id);
        i.putExtra(DCDbAdapter.PROVIDER_ACCOUNTS_ID, providerAccountId);
        startActivityForResult(i, 0);
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.menu_insert_instance);
        menu.add(0, REFRESH_ID, 1, R.string.refresh);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) 
    {
        switch(item.getItemId()) 
        {
            case INSERT_ID:
            	Intent i = new Intent(this, Images.class);
            	i.putExtra("CreateInstance", true);
            	i.putExtra(DCDbAdapter.PROVIDER_ACCOUNTS_ID, providerAccountId);
            	startActivityForResult(i, 0);
                return true;
            case REFRESH_ID: return refreshInstances();
        }
        return super.onMenuItemSelected(featureId, item);
    }

    private boolean refreshInstances()
    {
    	Intent intent = new Intent(this, Instances.class);
    	intent.putExtra(DCDbAdapter.PROVIDER_ACCOUNTS_ID, providerAccountId);

    	progressBar = ProgressDialog.show(this, null, "Refreshing Instances...");
    	Toast toast = Toast.makeText(this, "Instances Successfully Updated", Toast.LENGTH_LONG);

    	DCRunnable dcr = new DCRunnable(this, DCRunnable.REFRESH_INSTANCES, providerAccountId, progressBar, toast, intent);
    	Thread bgThread = new Thread(dcr);
    	bgThread.start();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) 
    {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }
}