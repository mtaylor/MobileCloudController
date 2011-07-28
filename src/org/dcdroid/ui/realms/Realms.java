package org.dcdroid.ui.realms;

import org.dcdroid.R;
import org.dcdroid.ui.DCDbAdapter;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Realms extends ListActivity 
{
	private Long providerAccountId;
	
	private boolean createInstance;
	
	private Long imageId;
	
	private Long hardwareProfileId;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        if((getIntent().getExtras() != null) && (getIntent().getExtras().containsKey(DCDbAdapter.PROVIDER_ACCOUNTS_ID)))
        {
        	providerAccountId = getIntent().getExtras().getLong(DCDbAdapter.PROVIDER_ACCOUNTS_ID);
        	fillData();
        	registerForContextMenu(getListView());

            createInstance = getIntent().getExtras().containsKey("CreateInstance");
            if(createInstance)
            {
            	setTitle("Create Instance: Select Realm");
            	imageId = getIntent().getExtras().getLong("ImageId");
            	hardwareProfileId = getIntent().getExtras().getLong("HardwareProfileId");
            }
            else
            {
            	setTitle("Realms");
            }
        }
    }

    private void fillData()
    {
    	DCDbAdapter dbHelper = new DCDbAdapter(this);
        dbHelper.open();
        
        Cursor notesCursor = dbHelper.fetchAllRealms(providerAccountId);
        startManagingCursor(notesCursor);

        String[] from = new String[]{DCDbAdapter.REALM_NAME};
        int[] to = new int[]{R.id.list_row_text};

        SimpleCursorAdapter accounts = new SimpleCursorAdapter(this, R.layout.list_row, notesCursor, from, to);
        setListAdapter(accounts);
        
        dbHelper.close();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) 
    {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, RealmShow.class);
        i.putExtra("RealmId", id);
        if(createInstance)
        {
        	i.putExtra("CreateInstance", true);
        	i.putExtra(DCDbAdapter.PROVIDER_ACCOUNTS_ID, providerAccountId);
        	i.putExtra("ImageId", imageId);
        	i.putExtra("HardwareProfileId", hardwareProfileId);
        }
        i.putExtra(DCDbAdapter.PROVIDER_ACCOUNTS_ID, providerAccountId);
        startActivityForResult(i, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) 
    {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }
}