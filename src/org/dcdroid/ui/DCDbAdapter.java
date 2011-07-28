/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.dcdroid.ui;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dcdroid.api.Actions;
import org.dcdroid.api.Address;
import org.dcdroid.api.Entry;
import org.dcdroid.api.HardwareProfile;
import org.dcdroid.api.Image;
import org.dcdroid.api.Instance;
import org.dcdroid.api.Link;
import org.dcdroid.api.Property;
import org.dcdroid.api.Range;
import org.dcdroid.api.Realm;
import org.dcdroid.api.Enum;
import org.dcdroid.ui.provideraccounts.ProviderAccount;
import org.dcdroid.ui.providers.Provider;
import org.deltacloud.api.client.DeltaCloudClient;
import org.deltacloud.api.client.DeltaCloudClientException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DCDbAdapter 
{
    public static final String DATABASE_TYPE_PRIMARY_KEY = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final String DATABASE_TYPE_TEXT = "TEXT";
    public static final String DATABASE_TYPE_INTEGER = "INTEGER";
    
	public static final String SETTINGS_TABLE = "settings";
	public static final String SETTING_ID = "_id";
	public static final String SETTING_KEY = "key";
	public static final String SETTING_VALUE = "value";
	
	public static final String PROVIDERS_TABLE = "providers";
	public static final String PROVIDER_ID = "_id";
	public static final String PROVIDER_NAME = "name";
	public static final String PROVIDER_URL = "url";
	
	public static final String PROVIDER_ACCOUNTS_TABLE = "provider_accounts";
	public static final String PROVIDER_ACCOUNTS_ID = "_id";
	public static final String PROVIDER_ACCOUNTS_NAME = "name";
	public static final String PROVIDER_ACCOUNTS_PROVIDER_ID = "provider_id";
	public static final String PROVIDER_ACCOUNTS_USERNAME = "username";
	public static final String PROVIDER_ACCOUNTS_PASSWORD = "password";
	public static final String PROVIDER_ACCOUNTS_DEFAULT = "default";

	public static final String IMAGES_TABLE = "images";
	public static final String IMAGE_ID  = "_id"; 
	public static final String IMAGE_NAME  = "name";
	public static final String IMAGE_OWNER_ID  = "owner_id";
	public static final String IMAGE_DESCRIPTION  = "description";
	public static final String IMAGE_HREF  = "href";
	public static final String IMAGE_EXTERNAL_ID  = "external_id";
	public static final String IMAGE_ARCHITECTURE  = "architecture";
	public static final String IMAGE_PROVIDER_ACCOUNT_ID  = "provider_account_id";

	public static final String HARDWARE_PROFILES_TABLE = "hardware_profiles";
	public static final String HARDWARE_PROFILE_ID  = "_id"; 
	public static final String HARDWARE_PROFILE_NAME  = "name";
	public static final String HARDWARE_PROFILE_HREF  = "href";
	public static final String HARDWARE_PROFILE_EXTERNAL_ID  = "external_id";
	public static final String HARDWARE_PROFILE_PROVIDER_ACCOUNT_ID  = "provider_account_id";
    
	public static final String HARDWARE_PROFILE_PROPERTIES_TABLE = "hardware_profiles_properties";
	public static final String HARDWARE_PROFILE_PROPERTIES_ID  = "_id"; 
	public static final String HARDWARE_PROFILE_PROPERTIES_NAME  = "name";
	public static final String HARDWARE_PROFILE_PROPERTIES_UNIT  = "unit"; 
	public static final String HARDWARE_PROFILE_PROPERTIES_KIND  = "kind";
	public static final String HARDWARE_PROFILE_PROPERTIES_VALUE  = "value";
	public static final String HARDWARE_PROFILE_PROPERTIES_RANGE_FROM  = "range_from";
	public static final String HARDWARE_PROFILE_PROPERTIES_RANGE_TO  = "range_to";
	public static final String HARDWARE_PROFILE_PROPERTIES_HARDWARE_PROFILE_ID  = "hardware_profile_id"; 
	
	public static final String HARDWARE_PROFILE_PROPERTIES_ENUMS_TABLE = "hardware_profile_properties_enums";
	public static final String HARDWARE_PROFILE_PROPERTIES_ENUM_ID  = "_id";
	public static final String HARDWARE_PROFILE_PROPERTIES_ENUM_VALUE  = "value";
	public static final String HARDWARE_PROFILE_PROPERTIES_ENUM_PROPERTY_ID  = "hardware_profile_property_id";

	public static final String REALMS_TABLE = "realms";
	public static final String REALM_ID  = "_id";
	public static final String REALM_HREF  = "href";
	public static final String REALM_NAME  = "name";
	public static final String REALM_LIMIT  = "lim";
	public static final String REALM_STATE  = "state";
	public static final String REALM_EXTERNAL_ID  = "external_id";
	public static final String REALM_PROVIDER_ACCOUNT_ID  = "provider_account_id";
	
	public static final String INSTANCES_TABLE = "instances";
	public static final String INSTANCE_ID  = "_id";
	public static final String INSTANCE_NAME = "name";
	public static final String INSTANCE_OWNER_ID = "owner_id";
	public static final String INSTANCE_IMAGE_ID = "image_id";
	public static final String INSTANCE_HARDWARE_PROFILE_ID = "hardware_profile_id";
	public static final String INSTANCE_REALM_ID = "realm_id";
	public static final String INSTANCE_STATE = "state";
	public static final String INSTANCE_HREF = "href";
	public static final String INSTANCE_EXTERNAL_ID = "external_id";
	public static final String INSTANCE_PROVIDER_ACCOUNT_ID = "provider_account_id";

	public static final String ACTIONS_TABLE = "actions";
	public static final String ACTION_ID = "_id";
	public static final String ACTION_REL = "rel";
	public static final String ACTION_HREF = "href";
	public static final String ACTION_METHOD = "method";
	public static final String ACTION_INSTANCE_ID = "instance_id";
	
	public static final String ADDRESSES_TABLE = "addresses";
	public static final String ADDRESS_ID = "_id";
	public static final String ADDRESS_PRIVACY = "privacy";
	public static final String ADDRESS_VALUE = "address";
	public static final String ADDRESS_INSTANCE_ID = "instance_id";
	
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context mCtx;

    public DCDbAdapter(Context ctx) 
    {
        this.mCtx = ctx;
    }

    public DCDbAdapter open() throws SQLException 
    {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() 
    {
        mDbHelper.close();
    }

    public long createSetting(String key, String value) 
    {
    	deleteSetting(key);
		ContentValues cv = new ContentValues();
		putCV(cv, SETTING_KEY, key);
		putCV(cv, SETTING_VALUE, value);
        return mDb.insert(SETTINGS_TABLE, null, cv);
    }

    public long createProvider(String name, String url) 
    {
		ContentValues cv = new ContentValues();
		putCV(cv, PROVIDER_NAME, name);
		putCV(cv, PROVIDER_URL, url);
        return mDb.insert(PROVIDERS_TABLE, null, cv);
    }

    public long createProviderAccount(String name, String username, String password, long providerId) 
    {
		ContentValues cv = new ContentValues();
		putCV(cv, PROVIDER_ACCOUNTS_NAME, name);
		putCV(cv, PROVIDER_ACCOUNTS_USERNAME, username);
		putCV(cv, PROVIDER_ACCOUNTS_PASSWORD, password);
		putCV(cv, PROVIDER_ACCOUNTS_PROVIDER_ID, providerId);
        return mDb.insert(PROVIDER_ACCOUNTS_TABLE, null, cv);
    }
    
    public long createImage(String href, String name, String architecture, String description, String owner_id, String external_id, long provider_account_id) 
    {
		ContentValues cv = new ContentValues();
		putCV(cv, IMAGE_NAME, name);
		putCV(cv, IMAGE_PROVIDER_ACCOUNT_ID, provider_account_id);
		putCV(cv, IMAGE_ARCHITECTURE, architecture);
		putCV(cv, IMAGE_DESCRIPTION, description);
		putCV(cv, IMAGE_EXTERNAL_ID, external_id);
		putCV(cv, IMAGE_HREF, href);
		putCV(cv, IMAGE_OWNER_ID, owner_id);
        return mDb.insert(IMAGES_TABLE, null, cv);
    }
    
    public long createHardwareProfileProperty(String name, String kind, String range_from, String range_to, String unit, String value, long hwp_id) 
    {
		ContentValues cv = new ContentValues();
		putCV(cv, HARDWARE_PROFILE_PROPERTIES_NAME, name);
		putCV(cv, HARDWARE_PROFILE_PROPERTIES_KIND, kind);
		putCV(cv, HARDWARE_PROFILE_PROPERTIES_RANGE_FROM, range_from);
		putCV(cv, HARDWARE_PROFILE_PROPERTIES_RANGE_TO, range_to);
		putCV(cv, HARDWARE_PROFILE_PROPERTIES_UNIT, unit);
		putCV(cv, HARDWARE_PROFILE_PROPERTIES_VALUE, value);
		putCV(cv, HARDWARE_PROFILE_PROPERTIES_HARDWARE_PROFILE_ID, hwp_id);
        return mDb.insert(HARDWARE_PROFILE_PROPERTIES_TABLE, null, cv);
    }

    public long createHardwareProfile(String href, String name, String external_id, long provider_account_id) 
    {
		ContentValues cv = new ContentValues();
		putCV(cv, HARDWARE_PROFILE_NAME, name);
		putCV(cv, HARDWARE_PROFILE_HREF, href);
		putCV(cv, HARDWARE_PROFILE_EXTERNAL_ID, external_id);
		putCV(cv, HARDWARE_PROFILE_PROVIDER_ACCOUNT_ID, provider_account_id);
        return mDb.insert(HARDWARE_PROFILES_TABLE, null, cv);
    }

    public long createHardwareProfilePropertyEnum(String value, long propertyId) 
    {
		ContentValues cv = new ContentValues();
		putCV(cv, HARDWARE_PROFILE_PROPERTIES_ENUM_VALUE, value);
		putCV(cv, HARDWARE_PROFILE_PROPERTIES_ENUM_PROPERTY_ID, propertyId);
        return mDb.insert(HARDWARE_PROFILE_PROPERTIES_ENUMS_TABLE, null, cv);
    }

    public long createRealms(String href, String name, String limit, String state, String external_id, long provider_account_id) 
    {
		ContentValues cv = new ContentValues();
		putCV(cv, REALM_HREF, href);
		putCV(cv, REALM_LIMIT, limit);
		putCV(cv, REALM_NAME, name);
		putCV(cv, REALM_STATE, state);
		putCV(cv, REALM_EXTERNAL_ID, external_id);
		putCV(cv, REALM_PROVIDER_ACCOUNT_ID, provider_account_id);
        return mDb.insert(REALMS_TABLE, null, cv);
    }
    
    public long createAddress(String value, String privacy, long instanceId) 
    {
		ContentValues cv = new ContentValues();
		putCV(cv, ADDRESS_VALUE, value);
		putCV(cv, ADDRESS_PRIVACY, privacy);
		putCV(cv, ADDRESS_INSTANCE_ID, instanceId);
        return mDb.insert(ADDRESSES_TABLE, null, cv);
    }
    
    public long createInstance(String href, String name, String imageId, String ownerId, String realmId, String hardwareProfileId, String state, String externalId, long providerAccountId) 
    {
		ContentValues cv = new ContentValues();
		putCV(cv, INSTANCE_HREF, href);
		putCV(cv, INSTANCE_NAME, name);
		putCV(cv, INSTANCE_IMAGE_ID, imageId);
		putCV(cv, INSTANCE_OWNER_ID, ownerId);
		putCV(cv, INSTANCE_REALM_ID,realmId);
		putCV(cv, INSTANCE_STATE, state);
		putCV(cv, INSTANCE_HARDWARE_PROFILE_ID, hardwareProfileId);
		putCV(cv, INSTANCE_EXTERNAL_ID, externalId);
		putCV(cv, INSTANCE_PROVIDER_ACCOUNT_ID, providerAccountId);
        return mDb.insert(INSTANCES_TABLE, null, cv);
    }
    
    public long createAction(String href, String method, String rel, long instanceId)
    {
		ContentValues cv = new ContentValues();
		putCV(cv, ACTION_HREF, href);
		putCV(cv, ACTION_METHOD, method);
		putCV(cv, ACTION_REL, rel);
		putCV(cv, ACTION_INSTANCE_ID, instanceId);
        return mDb.insert(ACTIONS_TABLE, null, cv);
    }

    public boolean deleteProvider(long rowId) 
    {
        return mDb.delete(PROVIDERS_TABLE, PROVIDER_ID + "=" + rowId, null) > 0;
    }

    public long deleteProviders()
    {
    	return mDb.delete(PROVIDERS_TABLE, null, null);
    }

    public boolean deleteProviderAccount(long rowId) 
    {
    	if(getSettings().get(Settings.DEFAULT_ACCOUNT) == "" + rowId)
    	{
    		deleteSetting(Settings.DEFAULT_ACCOUNT);
    	}
        return mDb.delete(PROVIDER_ACCOUNTS_TABLE, PROVIDER_ACCOUNTS_ID + "=" + rowId, null) > 0;
    }

    public boolean deleteSetting(String key) 
    {
        return mDb.delete(SETTINGS_TABLE, SETTING_KEY + "='" + key + "'", null) > 0;
    }

    public long deleteProviderAccounts()
    {
    	return mDb.delete(PROVIDER_ACCOUNTS_TABLE, null, null);
    }

    public void deleteCloudAccountObjects(long providerAccountId) 
    {
    	deleteInstances(providerAccountId);
    	deleteImages(providerAccountId);
    	deleteHardwareProfiles(providerAccountId);
    	deleteRealms(providerAccountId);
    }

    public void deleteInstances(long providerAccountId)
    {
    	mDb.delete(INSTANCES_TABLE, INSTANCE_PROVIDER_ACCOUNT_ID + "=" + providerAccountId, null);
    }

    public void deleteImages(long providerAccountId)
    {
    	mDb.delete(IMAGES_TABLE, IMAGE_PROVIDER_ACCOUNT_ID + "=" + providerAccountId, null);
    }

    public void deleteHardwareProfiles(long providerAccountId)
    {
    	mDb.delete(HARDWARE_PROFILES_TABLE, HARDWARE_PROFILE_PROVIDER_ACCOUNT_ID + "=" + providerAccountId, null);
    }

    public void deleteRealms(long providerAccountId)
    {
    	mDb.delete(REALMS_TABLE, REALM_PROVIDER_ACCOUNT_ID + "=" + providerAccountId, null);
    }

    public Cursor fetchProvider(long rowId) throws SQLException 
    {
        Cursor mCursor = mDb.query(true, PROVIDERS_TABLE, new String[] {PROVIDER_ID, PROVIDER_NAME, PROVIDER_URL}, PROVIDER_ID + "=" + rowId, 
        		null, null, null, null, null);
        if (mCursor != null) 
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchProviderAccount(long rowId) throws SQLException
    {
        Cursor mCursor = mDb.query(true, PROVIDER_ACCOUNTS_TABLE, new String[] {PROVIDER_ACCOUNTS_ID, PROVIDER_ACCOUNTS_NAME, PROVIDER_ACCOUNTS_USERNAME, PROVIDER_ACCOUNTS_PASSWORD, PROVIDER_ACCOUNTS_PROVIDER_ID},
             PROVIDER_ACCOUNTS_ID + " = " + rowId, null, null, null, null, null);
        if (mCursor != null) 
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchImage(long imageId) 
    {
    	return fetchImage(IMAGE_ID + " = " + imageId);
    }

    public Cursor fetchImage(String match) 
    {
    	Cursor mCursor = mDb.query(IMAGES_TABLE, new String[] {IMAGE_ID, IMAGE_NAME, IMAGE_ARCHITECTURE, IMAGE_HREF, IMAGE_OWNER_ID, IMAGE_DESCRIPTION, IMAGE_EXTERNAL_ID, IMAGE_PROVIDER_ACCOUNT_ID},
        		  match, null, null, null, null);
        if (mCursor != null) 
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchInstance(long instanceId) 
    {
    	Cursor mCursor = mDb.query(INSTANCES_TABLE, new String[] {INSTANCE_ID, INSTANCE_NAME, INSTANCE_HREF, INSTANCE_OWNER_ID, INSTANCE_REALM_ID, INSTANCE_HARDWARE_PROFILE_ID, INSTANCE_STATE, INSTANCE_IMAGE_ID, INSTANCE_PROVIDER_ACCOUNT_ID, INSTANCE_EXTERNAL_ID},
        		  INSTANCE_ID + " = " + instanceId, null, null, null, null);
        if (mCursor != null) 
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchRealm(long realmId) 
    {
    	return fetchRealm(REALM_ID + "=" + realmId);
    }

    public Cursor fetchRealm(String match) 
    {
    	Cursor mCursor = mDb.query(REALMS_TABLE, new String[] {REALM_ID, REALM_EXTERNAL_ID, REALM_NAME, REALM_STATE, REALM_LIMIT, REALM_EXTERNAL_ID, REALM_HREF, REALM_PROVIDER_ACCOUNT_ID},
        		 match, null, null, null, null);
        if (mCursor != null) 
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchHardwareProfile(long hardwareProfileId) throws SQLException 
    {
    	return fetchHardwareProfile(HARDWARE_PROFILE_ID + " = " + hardwareProfileId);
    }

    public Cursor fetchHardwareProfile(String match) throws SQLException 
    {
        Cursor mCursor = mDb.query(true, HARDWARE_PROFILES_TABLE, new String[] {HARDWARE_PROFILE_ID, HARDWARE_PROFILE_HREF, HARDWARE_PROFILE_NAME, HARDWARE_PROFILE_EXTERNAL_ID},
             match, null, null, null, null, null);
        if (mCursor != null) 
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchHardwareProfileProperties(long rowId) throws SQLException
    {
        Cursor mCursor = mDb.query(true, HARDWARE_PROFILE_PROPERTIES_TABLE, new String[] {HARDWARE_PROFILE_PROPERTIES_ID, HARDWARE_PROFILE_PROPERTIES_KIND, HARDWARE_PROFILE_PROPERTIES_NAME, 
        		HARDWARE_PROFILE_PROPERTIES_RANGE_FROM, HARDWARE_PROFILE_PROPERTIES_RANGE_TO, HARDWARE_PROFILE_PROPERTIES_UNIT, HARDWARE_PROFILE_PROPERTIES_VALUE},
                HARDWARE_PROFILE_PROPERTIES_HARDWARE_PROFILE_ID + " = " + rowId, null, null, null, null, null);
           if (mCursor != null) 
           {
               mCursor.moveToFirst();
           }
           return mCursor;
    }

    public Cursor fetchHardwareProfilePropertyEnums(long rowId) throws SQLException
    {
        Cursor mCursor = mDb.query(true, HARDWARE_PROFILE_PROPERTIES_ENUMS_TABLE, new String[] {HARDWARE_PROFILE_PROPERTIES_ENUM_ID, HARDWARE_PROFILE_PROPERTIES_ENUM_VALUE, HARDWARE_PROFILE_PROPERTIES_ENUM_PROPERTY_ID},
                HARDWARE_PROFILE_PROPERTIES_ENUM_PROPERTY_ID + " = " + rowId, null, null, null, null, null);
           if (mCursor != null) 
           {
               mCursor.moveToFirst();
           }
           return mCursor;
    }

    public Cursor fetchActions(long instanceId) throws SQLException
    {
        Cursor mCursor = mDb.query(true, ACTIONS_TABLE, new String[] {ACTION_ID, ACTION_HREF, ACTION_INSTANCE_ID, ACTION_REL, ACTION_METHOD},
                ACTION_INSTANCE_ID + " = " + instanceId, null, null, null, null, null);
           if (mCursor != null) 
           {
               mCursor.moveToFirst();
           }
           return mCursor;
    }

    public Cursor fetchAllSettings() 
    {
    	Cursor mCursor = mDb.query(SETTINGS_TABLE, new String[] {SETTING_ID, SETTING_KEY, SETTING_VALUE},
        		  null, null, null, null, null);
        if (mCursor != null) 
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllProviders() 
    {
    	Cursor mCursor = mDb.query(PROVIDERS_TABLE, new String[] {PROVIDER_ID, PROVIDER_NAME, PROVIDER_URL},
        		  null, null, null, null, null);
        if (mCursor != null) 
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllProvidersAccounts(long providerId) 
    {
    	Cursor mCursor = mDb.query(PROVIDER_ACCOUNTS_TABLE, new String[] {PROVIDER_ACCOUNTS_ID, PROVIDER_ACCOUNTS_NAME, PROVIDER_ACCOUNTS_USERNAME, PROVIDER_ACCOUNTS_PASSWORD},
        		  PROVIDER_ACCOUNTS_PROVIDER_ID + "=" + providerId, null, null, null, null);
        if (mCursor != null) 
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllImages(long providerAccountId) 
    {
    	Cursor mCursor = mDb.query(IMAGES_TABLE, new String[] {IMAGE_ID, IMAGE_NAME, IMAGE_ARCHITECTURE, IMAGE_HREF, IMAGE_OWNER_ID, IMAGE_DESCRIPTION, IMAGE_EXTERNAL_ID, IMAGE_PROVIDER_ACCOUNT_ID},
        		  IMAGE_PROVIDER_ACCOUNT_ID + "=" + providerAccountId, null, null, null, null);
        if (mCursor != null) 
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllHardwareProfiles(long providerAccountId) 
    {
    	Cursor mCursor = mDb.query(HARDWARE_PROFILES_TABLE, new String[] {HARDWARE_PROFILE_ID, HARDWARE_PROFILE_NAME, HARDWARE_PROFILE_HREF, HARDWARE_PROFILE_EXTERNAL_ID, HARDWARE_PROFILE_PROVIDER_ACCOUNT_ID},
        		  HARDWARE_PROFILE_PROVIDER_ACCOUNT_ID + "=" + providerAccountId, null, null, null, null);
        if (mCursor != null) 
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor fetchAllInstances(long providerAccountId) 
    {
    	Cursor mCursor = mDb.query(INSTANCES_TABLE, new String[] {INSTANCE_ID, INSTANCE_NAME, INSTANCE_HREF, INSTANCE_OWNER_ID, INSTANCE_REALM_ID, INSTANCE_HARDWARE_PROFILE_ID, INSTANCE_STATE, INSTANCE_PROVIDER_ACCOUNT_ID},
        		  INSTANCE_PROVIDER_ACCOUNT_ID + "=" + providerAccountId, null, null, null, null);
        if (mCursor != null) 
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllRealms(long providerAccountId) 
    {
    	Cursor mCursor = mDb.query(REALMS_TABLE, new String[] {REALM_ID, REALM_NAME, REALM_STATE, REALM_LIMIT, REALM_EXTERNAL_ID, REALM_HREF, REALM_PROVIDER_ACCOUNT_ID},
        		  REALM_PROVIDER_ACCOUNT_ID + "=" + providerAccountId, null, null, null, null);
        if (mCursor != null) 
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateProvider(long rowId, String name, String url) 
    {
        ContentValues args = new ContentValues();
        args.put(PROVIDER_ACCOUNTS_NAME, name);
        args.put(PROVIDER_URL, url);

        return mDb.update(PROVIDERS_TABLE, args, PROVIDER_ID + "=" + rowId, null) > 0;
    }
    
    public boolean updateProviderAccount(long rowId, String name, String username, String password) 
    {
    	name = name == null ? "" : name;
    	username = username == null ? "" : username;
    	password = password == null ? "" : password;

        ContentValues args = new ContentValues();
        args.put(PROVIDER_ACCOUNTS_NAME, name);
        args.put(PROVIDER_ACCOUNTS_USERNAME, username);
        args.put(PROVIDER_ACCOUNTS_PASSWORD, password);
        return mDb.update(PROVIDER_ACCOUNTS_TABLE, args, PROVIDER_ACCOUNTS_ID + "=" + rowId, null) > 0;
    }

    public Map<String, String> getSettings()
    {
    	HashMap<String, String> settings = new HashMap<String,String>();
    	Cursor cursor = fetchAllSettings();
    	for (int i = 0; i < cursor.getCount(); i++)
    	{
    		String key = cursor.getString(cursor.getColumnIndex(SETTING_KEY));
    		String value = cursor.getString(cursor.getColumnIndex(SETTING_VALUE));
    		settings.put(key, value);
    	}
    	cursor.close();
    	return settings;
    }

    public ProviderAccount getProviderAcccount(long providerAccountId)
    {
    	ProviderAccount account = new ProviderAccount();
    	
    	Cursor cursor = fetchProviderAccount(providerAccountId);
    	account.setId(cursor.getLong(cursor.getColumnIndex(PROVIDER_ACCOUNTS_ID)));
    	account.setName(cursor.getString(cursor.getColumnIndex(PROVIDER_ACCOUNTS_NAME)));
    	account.setUsername(cursor.getString(cursor.getColumnIndex(PROVIDER_ACCOUNTS_USERNAME)));
    	account.setPassword(cursor.getString(cursor.getColumnIndex(PROVIDER_ACCOUNTS_PASSWORD)));
    	account.setProviderId(cursor.getLong(cursor.getColumnIndex(PROVIDER_ACCOUNTS_PROVIDER_ID)));
    	cursor.close();
    	
    	return account;
    }

    public Provider getProvider(long providerId)
    {
    	Provider provider = new Provider();
    	
    	Cursor cursor = fetchProvider(providerId);
    	provider.setId(cursor.getLong(cursor.getColumnIndex(PROVIDER_ID)));
    	provider.setName(cursor.getString(cursor.getColumnIndex(PROVIDER_NAME)));
    	provider.setUrl(cursor.getString(cursor.getColumnIndex(PROVIDER_URL)));
    	cursor.close();
    	return provider;
    }

    public Image getImage(long imageId)
    {
    	return buildImage(fetchImage(imageId));
    }

    public Image getImageByExternalId(String externalId)
    {
    	return buildImage(fetchImage(IMAGE_EXTERNAL_ID + "=" + "'" + externalId + "'"));
    }

    public Image buildImage(Cursor cursor)
    {
    	Image image = new Image();
    	image.setId(cursor.getString(cursor.getColumnIndex(IMAGE_EXTERNAL_ID)));
    	image.setName(cursor.getString(cursor.getColumnIndex(IMAGE_NAME)));
    	image.setHref(cursor.getString(cursor.getColumnIndex(IMAGE_HREF)));
    	image.setArchitecture(cursor.getString(cursor.getColumnIndex(IMAGE_ARCHITECTURE)));
    	image.setDescription(cursor.getString(cursor.getColumnIndex(IMAGE_DESCRIPTION)));
    	image.setOwnerId(cursor.getString(cursor.getColumnIndex(IMAGE_OWNER_ID)));
    	return image;
    }

    public Realm getRealm(long realmId)
    {
    	return buildRealm(fetchRealm(realmId));
    }

    public Realm getRealmByExternalId(String externalId)
    {
    	return buildRealm(fetchRealm(IMAGE_EXTERNAL_ID + " = " + "'" + externalId + "'"));
    }

    public Realm buildRealm(Cursor cursor)
    {
    	Realm realm = new Realm();
    	realm.setId(cursor.getString(cursor.getColumnIndex(REALM_EXTERNAL_ID)));
    	realm.setName(cursor.getString(cursor.getColumnIndex(REALM_NAME)));
    	realm.setHref(cursor.getString(cursor.getColumnIndex(REALM_HREF)));
    	realm.setLimit(cursor.getString(cursor.getColumnIndex(REALM_LIMIT)));
    	realm.setState(cursor.getString(cursor.getColumnIndex(REALM_STATE)));
    	return realm;
    }

    public Instance getInstance(long instanceId)
    {
    	Instance instance = new Instance();
    	
    	Cursor cursor = fetchInstance(instanceId);
    	instance.setId(cursor.getString(cursor.getColumnIndex(INSTANCE_EXTERNAL_ID)));
    	instance.setName(cursor.getString(cursor.getColumnIndex(INSTANCE_NAME)));
    	instance.setHref(cursor.getString(cursor.getColumnIndex(INSTANCE_HREF)));
    	instance.setState(cursor.getString(cursor.getColumnIndex(INSTANCE_STATE)));
    	instance.setOwnerId(cursor.getString(cursor.getColumnIndex(INSTANCE_OWNER_ID)));
    	
    	String imageId = cursor.getString(cursor.getColumnIndex(INSTANCE_IMAGE_ID));
    	String realmId = cursor.getString(cursor.getColumnIndex(INSTANCE_REALM_ID));
    	String hardwareProfileId = cursor.getString(cursor.getColumnIndex(INSTANCE_HARDWARE_PROFILE_ID));
    	cursor.close();
    	
    	instance.setImage(getImageByExternalId(imageId));
    	instance.setHardwareProfile(getHardwareProfileByExternalId(hardwareProfileId));
    	instance.setRealm(getRealmByExternalId(realmId));
    	instance.setActions(getActions(instanceId));
    	return instance;
    }

    public Actions getActions(long instanceId)
    {
    	Actions actions = new Actions();
    	
    	Cursor cursor = fetchActions(instanceId);
    	cursor.moveToFirst();
    	for (int i = 0; i < cursor.getCount(); i++)
    	{
    		Link link = new Link();
    		link.setHref(cursor.getString(cursor.getColumnIndex(ACTION_HREF)));
    		link.setMethod(cursor.getString(cursor.getColumnIndex(ACTION_METHOD)));	
    		link.setRel(cursor.getString(cursor.getColumnIndex(ACTION_REL)));
    		actions.addLink(link);
    		cursor.moveToNext();
    	}
    	cursor.close();
    	return actions;
    }

    public HardwareProfile getHardwareProfile(long hardwareProfileId)
    {
    	return buildHardwareProfile(fetchHardwareProfile(hardwareProfileId));
    }

    public HardwareProfile getHardwareProfileByExternalId(String externalId)
    {
    	return buildHardwareProfile(fetchHardwareProfile(IMAGE_EXTERNAL_ID + " = " + "'" + externalId + "'"));
    }
    
    public HardwareProfile buildHardwareProfile(Cursor cursor)
    {
    	HardwareProfile hardwareProfile = new HardwareProfile();
    	hardwareProfile.setId(cursor.getString(cursor.getColumnIndex(HARDWARE_PROFILE_EXTERNAL_ID)));
    	hardwareProfile.setName(cursor.getString(cursor.getColumnIndex(HARDWARE_PROFILE_NAME)));
    	hardwareProfile.setHref(cursor.getString(cursor.getColumnIndex(HARDWARE_PROFILE_HREF)));
    	
    	hardwareProfile.setProperties(getHardwareProfileProperties(cursor.getLong(cursor.getColumnIndex(HARDWARE_PROFILE_ID))));
    	return hardwareProfile;
    }
    
    public List<Property> getHardwareProfileProperties(long hardwareProfileId)
    {
    	ArrayList<Property> properties = new ArrayList<Property>();
    	Cursor cursor = fetchHardwareProfileProperties(hardwareProfileId);

    	for (int i = 0; i < cursor.getCount(); i++)
    	{
    		Property property = new Property();
    		property.setName(cursor.getString(cursor.getColumnIndex(HARDWARE_PROFILE_PROPERTIES_NAME)));
    		property.setKind(cursor.getString(cursor.getColumnIndex(HARDWARE_PROFILE_PROPERTIES_KIND)));
    		property.setUnit(cursor.getString(cursor.getColumnIndex(HARDWARE_PROFILE_PROPERTIES_UNIT)));
    		property.setValue(cursor.getString(cursor.getColumnIndex(HARDWARE_PROFILE_PROPERTIES_VALUE)));
    		
    		if(property.getKind() == "range")
    		{
    			Range range = new Range();
    			range.setFirst(cursor.getString(cursor.getColumnIndex(HARDWARE_PROFILE_PROPERTIES_RANGE_FROM)));
    			range.setFirst(cursor.getString(cursor.getColumnIndex(HARDWARE_PROFILE_PROPERTIES_RANGE_TO)));
    			property.setRange(range);
    		}
    		else if(property.getKind() == "enum")
    		{
    			property.setEnum(getHardwareProfilePropertyEnums(cursor.getLong(cursor.getColumnIndex(HARDWARE_PROFILE_PROPERTIES_ID))));
    		}
    		properties.add(property);
    		cursor.moveToNext();
    	}
    	return properties;
    }
    
    public Enum getHardwareProfilePropertyEnums(long hardwareProfilePropertyId)
    {
    	Cursor cursor = fetchHardwareProfilePropertyEnums(hardwareProfilePropertyId);
    	
    	Enum e = new Enum();
    	
    	cursor.moveToFirst();
    	for (int i = 0; i < cursor.getCount(); i++)
    	{
    		Entry entry = new Entry();
    		entry.setValue(cursor.getString(cursor.getColumnIndex(HARDWARE_PROFILE_PROPERTIES_ENUM_VALUE)));    		
    	}
    	return e;
    }
    
    public void refreshInstances(long providerAccountId) throws DeltaCloudClientException
    {
    	deleteInstances(providerAccountId);

    	DeltaCloudClient client = createDCClient(providerAccountId);
    	for(Instance instance : client.listInstances())
    	{
    		long id = createInstance(instance.getHref(), instance.getName(), instance.getImage().getId(), instance.getOwnerId(), instance.getRealm().getId(),
    				instance.getHardwareProfile().getId(), instance.getState(), instance.getId(), providerAccountId);

    		for(Address address : instance.getPublicAddresses().getAddresses())
    		{

    		}
    		
    		for(Address address : instance.getPrivateAddresses().getAddresses())
    		{
    			createAddress(address.getAddress(), "private", id);
    		}
    		
    		for(Link link : instance.getActions().getLinks())
    		{
    			createAction(link.getHref(), link.getMethod(), link.getRel(), id);
    		}
    	}
    }

    public void refreshImages(long providerAccountId) throws DeltaCloudClientException
    {
    	deleteImages(providerAccountId);
    	DeltaCloudClient client = createDCClient(providerAccountId);
    	
    	HashMap<String, String> properties = new HashMap<String, String>();
    	properties.put("owner_id", "self");
    	for(Image image : client.listImages(properties))
    	{
    		createImage(image.getHref(), image.getName(), image.getArchitecture(), image.getDescription(), image.getOwnerId(), image.getId(), providerAccountId);
    	}
    }

    public void refreshHardwareProfiles(long providerAccountId) throws DeltaCloudClientException
    {
    	deleteHardwareProfiles(providerAccountId);
    	DeltaCloudClient client = createDCClient(providerAccountId);
    	for(HardwareProfile hwp : client.listHardwareProfiles())
    	{
    		long hwpId = createHardwareProfile(hwp.getHref(), hwp.getName(), hwp.getId(), providerAccountId);
    		for(Property p : hwp.getProperties())
    		{
    			long propertyId;
    			if(p.getRange() == null)
    			{
    				propertyId = createHardwareProfileProperty(p.getName(), p.getKind(), 
    						"", "", p.getUnit(), p.getValue(), hwpId);
    			}
    			else
    			{
    				propertyId = createHardwareProfileProperty(p.getName(), p.getKind(), 
    						p.getRange().getFirst(), p.getRange().getLast(), p.getUnit(), p.getValue(), hwpId);
    			}

    			if(p.getEnum() != null)
    			{
    				for(Entry e : p.getEnum().getEntries())
    				{
    					createHardwareProfilePropertyEnum(e.getValue(), propertyId);
    				}
    			}
    		}
    	}
    }

    public void refreshRealms(long providerAccountId) throws DeltaCloudClientException
    {
    	deleteRealms(providerAccountId);
    	DeltaCloudClient client = createDCClient(providerAccountId);
    	
    	List <Realm> realms = client.listRealms();
    	for(Realm r : client.listRealms())
    	{
    		createRealms(r.getHref(), r.getName(), r.getLimit(), r.getState(), r.getId(), providerAccountId);
    	}
    }
    
    public void refreshProviderAccount(long providerAccountId) throws DeltaCloudClientException
    {
    	refreshRealms(providerAccountId);
    	refreshHardwareProfiles(providerAccountId);
    	refreshImages(providerAccountId);
    	refreshInstances(providerAccountId);
    }

    public DeltaCloudClient createDCClient(long providerAccountId) throws DeltaCloudClientException
    {
    	try
    	{
    		ProviderAccount account = getProviderAcccount(providerAccountId);
    		Provider provider = getProvider(account.getProviderId());
    		return new DeltaCloudClient(new URL(provider.getUrl()), account.getUsername(), account.getPassword());
    	}
    	catch(MalformedURLException e)
    	{
    		throw new DeltaCloudClientException("Malformed URL", e);
    	}
    }

    private void putCV(ContentValues cv, String key, String value)
    {
    	if(value != null)
    	{
    		cv.put(key, value);
    	}
    	else
    	{
    		cv.put(key, "");
    	}
    }

    private void putCV(ContentValues cv, String key, long value)
    {
    	cv.put(key, value);
    }
}
