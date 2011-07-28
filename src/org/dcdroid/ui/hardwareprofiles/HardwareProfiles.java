package org.dcdroid.ui.hardwareprofiles;

import org.dcdroid.R;
import org.dcdroid.ui.DCDbAdapter;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class HardwareProfiles extends ListActivity 
{
	private Long providerAccountId;

	private boolean createInstance;

	private Long imageId;
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        
        if(getIntent().getExtras() != null)
        {
        	if(getIntent().getExtras().containsKey("CreateInstance"))
            {
        		setTitle("Create Instance: Select Hardware Profile");
        		createInstance = true;
            }
        	else
        	{
        		setTitle("Hardware Profiles");
        		createInstance = false;
        	}

        	if(getIntent().getExtras().containsKey(DCDbAdapter.PROVIDER_ACCOUNTS_ID))
        	{
        		providerAccountId = getIntent().getExtras().getLong(DCDbAdapter.PROVIDER_ACCOUNTS_ID);
        		fillData();
        		registerForContextMenu(getListView());
        	}

        	if(getIntent().getExtras().containsKey("ImageId"))
        	{
        		imageId = getIntent().getExtras().getLong("ImageId");
        	}
        }
    }

    private void fillData()
    {
    	DCDbAdapter dbHelper = new DCDbAdapter(this);
        dbHelper.open();
        
        Cursor notesCursor = dbHelper.fetchAllHardwareProfiles(providerAccountId);
        startManagingCursor(notesCursor);

        String[] from = new String[]{DCDbAdapter.HARDWARE_PROFILE_NAME};
        int[] to = new int[]{R.id.list_row_text};

        SimpleCursorAdapter accounts = new SimpleCursorAdapter(this, R.layout.list_row, notesCursor, from, to);
        setListAdapter(accounts);
        
        dbHelper.close();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) 
    {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, HardwareProfileShow.class);
        i.putExtra("HardwareProfileId", id);
        i.putExtra(DCDbAdapter.PROVIDER_ACCOUNTS_ID, providerAccountId);
        if(createInstance)
        {
        	i.putExtra("CreateInstance", true);
        	i.putExtra("ImageId", imageId);
        }
        startActivityForResult(i, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) 
    {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }
}
