package org.dcdroid.ui.images;

import org.dcdroid.R;
import org.dcdroid.ui.DCDbAdapter;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Images extends ListActivity 
{
	private Long providerAccountId;
	
	private boolean createInstance;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        
        if((getIntent().getExtras() != null) && (getIntent().getExtras().containsKey("CreateInstance")))
        {
        	setTitle("Create Instance: Select Image");
        	createInstance = true;
        }
        else
        {
        	setTitle("Images");
        	createInstance = false;
        }
        
        if((getIntent().getExtras() != null) && (getIntent().getExtras().containsKey(DCDbAdapter.PROVIDER_ACCOUNTS_ID)))
        {
        	providerAccountId = getIntent().getExtras().getLong(DCDbAdapter.PROVIDER_ACCOUNTS_ID);
        	fillData();
        	registerForContextMenu(getListView());
        }
    }

    private void fillData()
    {
    	DCDbAdapter dbHelper = new DCDbAdapter(this);
        dbHelper.open();
        
        Cursor notesCursor = dbHelper.fetchAllImages(providerAccountId);
        startManagingCursor(notesCursor);

        String[] from = new String[]{DCDbAdapter.IMAGE_NAME};
        int[] to = new int[]{R.id.list_row_text};

        SimpleCursorAdapter accounts = new SimpleCursorAdapter(this, R.layout.list_row, notesCursor, from, to);
        setListAdapter(accounts);
        
        dbHelper.close();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) 
    {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, ImageShow.class);
        i.putExtra("ImageId", id);
        i.putExtra(DCDbAdapter.PROVIDER_ACCOUNTS_ID, providerAccountId);
        if(createInstance)
        {
        	i.putExtra("CreateInstance", true);
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