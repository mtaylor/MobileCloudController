package org.dcdroid.ui.provideraccounts;

import org.dcdroid.R;
import org.dcdroid.ui.DCDbAdapter;
import org.dcdroid.ui.Home;
import org.dcdroid.ui.provideraccounts.ProviderAccountEdit;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.TextView;

public class ProviderAccounts extends ListActivity 
{
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int VIEW_ID = Menu.FIRST + 1;
    private static final int EDIT_ID = Menu.FIRST + 2;
    private static final int DELETE_ID = Menu.FIRST + 3;

    private Long providerId;

    private DCDbAdapter dbHelper;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

    	if((getIntent().getExtras() != null) && (getIntent().getExtras().containsKey(DCDbAdapter.PROVIDER_ID)))
        {
    		providerId = getIntent().getExtras().getLong(DCDbAdapter.PROVIDER_ID);
        }
        fillData();
        registerForContextMenu(getListView());
    }

    private void fillData() 
    {
    	setTitle("Accounts");
        dbHelper = new DCDbAdapter(this);
        dbHelper.open();
        Cursor cursor = dbHelper.fetchAllProvidersAccounts(providerId);
        startManagingCursor(cursor);

        String[] from = new String[]{DCDbAdapter.PROVIDER_ACCOUNTS_NAME};

        int[] to = new int[]{R.id.list_row_text};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_row, cursor, from, to);
        setListAdapter(adapter);
        dbHelper.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.menu_insert);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) 
    {
        switch(item.getItemId()) 
        {
            case INSERT_ID:
                createProviderAccount();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, VIEW_ID, 0, "View");
        menu.add(0, EDIT_ID, 0, "Edit");
        menu.add(0, DELETE_ID, 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) 
    {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) 
        {
            case VIEW_ID:
                Intent si = new Intent(this, ProviderAccountShow.class);
                si.putExtra("providerAccountId", info.id);
                startActivityForResult(si, 0);
                return true;
            case EDIT_ID:
                Intent ei = new Intent(this, ProviderAccountEdit.class);
                ei.putExtra("providerAccountId", info.id);
                startActivityForResult(ei, 0);
                return true;
            case DELETE_ID:
                dbHelper = new DCDbAdapter(this);
                dbHelper.open();
                dbHelper.deleteProviderAccount(info.id);
                fillData();
                dbHelper.close();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createProviderAccount() 
    {
        Intent i = new Intent(this, ProviderAccountEdit.class);
        i.putExtra("providerId", providerId);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) 
    {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, Home.class);
        i.putExtra(DCDbAdapter.PROVIDER_ACCOUNTS_ID, id);
        startActivityForResult(i, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) 
    {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }
}
