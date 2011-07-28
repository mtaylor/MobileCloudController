/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dcdroid.ui.providers;

import org.dcdroid.R;
import org.dcdroid.R.id;
import org.dcdroid.R.layout;
import org.dcdroid.R.string;
import org.dcdroid.ui.DCDbAdapter;
import org.dcdroid.ui.Settings;
import org.dcdroid.ui.provideraccounts.ProviderAccounts;
import org.deltacloud.api.client.DeltaCloudClientException;

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

public class Providers extends ListActivity 
{
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int VIEW_ID = Menu.FIRST + 1;
    private static final int EDIT_ID = Menu.FIRST + 2;
    private static final int DELETE_ID = Menu.FIRST + 3;

    private DCDbAdapter dbHelper;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        setTitle("Providers");
        dbHelper = new DCDbAdapter(this);
        dbHelper.open();

//        dbHelper.deleteSetting(Settings.DEFAULT_ACCOUNT);
//        dbHelper.deleteProviders();
//        dbHelper.deleteProviderAccounts();
//        long id = dbHelper.createProviderAccount("EC2 Account", "AKIAI5HJ6ZDO6KLRG7YA", "Hz3uYk+xf9z2KGX7iOufi5XAZ9OU7CvQZEc4bm0K", dbHelper.createProvider("Amazon EC2 US East", "http://192.168.0.3:3009/api"));
//        try
//        {
//        	dbHelper.refreshProviderAccount(id);
//        }
//        catch(Exception e)
//        {
//        	e.printStackTrace();
//        }

        dbHelper.close();
        fillData();
        registerForContextMenu(getListView());
    }

    private void fillData() 
    {
        dbHelper = new DCDbAdapter(this);
        dbHelper.open();
        Cursor cursor = dbHelper.fetchAllProviders();
        startManagingCursor(cursor);

        String[] from = new String[]{DCDbAdapter.PROVIDER_NAME};

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
                createProvider();
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
                Intent si = new Intent(this, ProviderShow.class);
                si.putExtra(DCDbAdapter.PROVIDER_ID, info.id);
                startActivityForResult(si, 0);
                return true;
            case EDIT_ID:
                Intent ei = new Intent(this, ProviderEdit.class);
                ei.putExtra(DCDbAdapter.PROVIDER_ID, info.id);
                startActivityForResult(ei, 0);
                return true;
            case DELETE_ID:
                dbHelper = new DCDbAdapter(this);
                dbHelper.open();
                dbHelper.deleteProvider(info.id);
                fillData();
                dbHelper.close();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createProvider() 
    {
        Intent i = new Intent(this, ProviderEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) 
    {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, ProviderAccounts.class);
        i.putExtra(DCDbAdapter.PROVIDER_ID, id);
        startActivityForResult(i, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) 
    {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }
}
