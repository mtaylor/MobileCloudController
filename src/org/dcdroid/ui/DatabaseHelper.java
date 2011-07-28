package org.dcdroid.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper     
{
    private static final String DATABASE_NAME = "dcdroid.db";
    private static final int DATABASE_VERSION = 4;

    DatabaseHelper(Context context) 
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		// CREATE PROPERTIES TABLE
		String createSettingsTable = "CREATE TABLE " + DCDbAdapter.SETTINGS_TABLE + " ( " + 
			DCDbAdapter.SETTING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			DCDbAdapter.SETTING_KEY + " TEXT, " +
			DCDbAdapter.SETTING_VALUE + " TEXT );";
		db.execSQL(createSettingsTable);
		
		// CREATE PROVIDERS TABLE
		String createProvidersTable = "CREATE TABLE " + DCDbAdapter.PROVIDERS_TABLE + " ( " + 
			DCDbAdapter.PROVIDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			DCDbAdapter.PROVIDER_NAME + " TEXT, " +
			DCDbAdapter.PROVIDER_URL + " TEXT );";
		db.execSQL(createProvidersTable);

		// CREATE PROVIDER ACCOUNTS TABLE
		String createProviderAccountsTable = "CREATE TABLE " + DCDbAdapter.PROVIDER_ACCOUNTS_TABLE + " ( " + 
		DCDbAdapter.PROVIDER_ACCOUNTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		DCDbAdapter.PROVIDER_ACCOUNTS_NAME + " TEXT, " +
		DCDbAdapter.PROVIDER_ACCOUNTS_USERNAME + " TEXT, " +
		DCDbAdapter.PROVIDER_ACCOUNTS_PASSWORD + " TEXT, " +
		DCDbAdapter.PROVIDER_ACCOUNTS_PROVIDER_ID + " INTEGER NOT NULL, " +
					"FOREIGN KEY (" + DCDbAdapter.PROVIDER_ACCOUNTS_PROVIDER_ID + ") REFERENCES " + DCDbAdapter.PROVIDERS_TABLE + "(" + DCDbAdapter.PROVIDER_ID + "));";
		db.execSQL(createProviderAccountsTable);
		
		//CREATE IMAGES TABLE
		String createImagesTable = "CREATE TABLE " + DCDbAdapter.IMAGES_TABLE + " ( " + 
		DCDbAdapter.IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		DCDbAdapter.IMAGE_NAME + " TEXT, " +
		DCDbAdapter.IMAGE_OWNER_ID + " TEXT, " +
		DCDbAdapter.IMAGE_DESCRIPTION + " TEXT, " +
		DCDbAdapter.IMAGE_HREF + " TEXT, " +
		DCDbAdapter.IMAGE_EXTERNAL_ID + " TEXT, " +
		DCDbAdapter.IMAGE_ARCHITECTURE + " TEXT, " +
		DCDbAdapter.IMAGE_PROVIDER_ACCOUNT_ID + " INTEGER NOT NULL, " +
				"FOREIGN KEY (" + DCDbAdapter.IMAGE_PROVIDER_ACCOUNT_ID + ") REFERENCES " +DCDbAdapter.PROVIDER_ACCOUNTS_TABLE + "(" + DCDbAdapter.PROVIDER_ACCOUNTS_ID + "));";
		db.execSQL(createImagesTable);
		
		//CREATE HARDWARE_PROFILE TABLE
		String createHardwareProfilesTable = "CREATE TABLE " + DCDbAdapter.HARDWARE_PROFILES_TABLE + " ( " + 
		DCDbAdapter.HARDWARE_PROFILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		DCDbAdapter.HARDWARE_PROFILE_NAME + " TEXT, " +
		DCDbAdapter.HARDWARE_PROFILE_HREF + " TEXT, " +
		DCDbAdapter.HARDWARE_PROFILE_EXTERNAL_ID + " TEXT, " +
		DCDbAdapter.HARDWARE_PROFILE_PROVIDER_ACCOUNT_ID + " INTEGER NOT NULL, " +
				"FOREIGN KEY (" + DCDbAdapter.HARDWARE_PROFILE_PROVIDER_ACCOUNT_ID + ") REFERENCES " + DCDbAdapter.PROVIDER_ACCOUNTS_TABLE + "(" + DCDbAdapter.PROVIDER_ACCOUNTS_ID + "));";
		db.execSQL(createHardwareProfilesTable);   
		
		//CREATE HARDWARE PROFILE PROPERTIES TABLE
		String createHardwareProfilePropertiesTable = "CREATE TABLE " + DCDbAdapter.HARDWARE_PROFILE_PROPERTIES_TABLE + " ( " + 
		DCDbAdapter.HARDWARE_PROFILE_PROPERTIES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		DCDbAdapter.HARDWARE_PROFILE_PROPERTIES_NAME + " TEXT, " +
		DCDbAdapter.HARDWARE_PROFILE_PROPERTIES_UNIT + " TEXT, " +
		DCDbAdapter.HARDWARE_PROFILE_PROPERTIES_KIND + " TEXT, " +
		DCDbAdapter.HARDWARE_PROFILE_PROPERTIES_VALUE + " TEXT, " +
		DCDbAdapter.HARDWARE_PROFILE_PROPERTIES_RANGE_FROM + " TEXT, " +
		DCDbAdapter.HARDWARE_PROFILE_PROPERTIES_RANGE_TO + " TEXT, " +
		DCDbAdapter.HARDWARE_PROFILE_PROPERTIES_HARDWARE_PROFILE_ID + " INTEGER NOT NULL, " +
				"FOREIGN KEY (" + DCDbAdapter.HARDWARE_PROFILE_PROPERTIES_HARDWARE_PROFILE_ID + ") REFERENCES " + DCDbAdapter.HARDWARE_PROFILES_TABLE + "(" + DCDbAdapter.HARDWARE_PROFILE_ID + "));";
		db.execSQL(createHardwareProfilePropertiesTable);
		
		//CREATE IMAGES TABLE
		String createHardwareProfilePropertiesEnumsTable = "CREATE TABLE " + DCDbAdapter.HARDWARE_PROFILE_PROPERTIES_ENUMS_TABLE + " ( " + 
		DCDbAdapter.HARDWARE_PROFILE_PROPERTIES_ENUM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		DCDbAdapter.HARDWARE_PROFILE_PROPERTIES_ENUM_VALUE + " TEXT, " +
		DCDbAdapter.HARDWARE_PROFILE_PROPERTIES_ENUM_PROPERTY_ID + " INTEGER NOT NULL, " +
				"FOREIGN KEY (" + DCDbAdapter.HARDWARE_PROFILE_PROPERTIES_ENUM_PROPERTY_ID + ") REFERENCES " + DCDbAdapter.HARDWARE_PROFILE_PROPERTIES_TABLE + "(" + DCDbAdapter.HARDWARE_PROFILE_PROPERTIES_ID + "));";
		db.execSQL(createHardwareProfilePropertiesEnumsTable);

		//CREATE REALMS TABLE
		String createRealmsTable = "CREATE TABLE " + DCDbAdapter.REALMS_TABLE + " ( " + 
		DCDbAdapter.REALM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		DCDbAdapter.REALM_NAME + " TEXT, " +
		DCDbAdapter.REALM_HREF + " TEXT, " +
		DCDbAdapter.REALM_LIMIT + " TEXT, " +
		DCDbAdapter.REALM_EXTERNAL_ID + " TEXT, " +
		DCDbAdapter.REALM_STATE + " TEXT, " +
		DCDbAdapter.REALM_PROVIDER_ACCOUNT_ID + " INTEGER NOT NULL, " +
				"FOREIGN KEY (" + DCDbAdapter.REALM_PROVIDER_ACCOUNT_ID + ") REFERENCES " + DCDbAdapter.PROVIDER_ACCOUNTS_TABLE + "(" + DCDbAdapter.PROVIDER_ACCOUNTS_ID + "));";
		db.execSQL(createRealmsTable);    		

		//CREATE ADDRESSES TABLE
		String createAddressesTable = "CREATE TABLE " + DCDbAdapter.ADDRESSES_TABLE + " ( " + 
		DCDbAdapter.ADDRESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		DCDbAdapter.ADDRESS_VALUE + " TEXT, " +
		DCDbAdapter.ADDRESS_PRIVACY + " TEXT, " +
		DCDbAdapter.ADDRESS_INSTANCE_ID + " INTEGER NOT NULL, " +
				"FOREIGN KEY (" + DCDbAdapter.ADDRESS_INSTANCE_ID + ") REFERENCES " + DCDbAdapter.PROVIDER_ACCOUNTS_TABLE + "(" + DCDbAdapter.PROVIDER_ACCOUNTS_ID + "));";
		db.execSQL(createAddressesTable);
		
		//CREATE INSTANCES TABLE
		String createInstancesTable = "CREATE TABLE " + DCDbAdapter.INSTANCES_TABLE + " ( " + 
		DCDbAdapter.INSTANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		DCDbAdapter.INSTANCE_NAME + " TEXT, " +
		DCDbAdapter.INSTANCE_OWNER_ID + " TEXT, " +
		DCDbAdapter.INSTANCE_IMAGE_ID + " TEXT, " +
		DCDbAdapter.INSTANCE_HARDWARE_PROFILE_ID + " TEXT, " +
		DCDbAdapter.INSTANCE_STATE + " TEXT, " +
		DCDbAdapter.INSTANCE_REALM_ID + " TEXT, " +
		DCDbAdapter.INSTANCE_HREF + " TEXT, " +
		DCDbAdapter.INSTANCE_EXTERNAL_ID + " TEXT, " +
		DCDbAdapter.INSTANCE_PROVIDER_ACCOUNT_ID + " INTEGER NOT NULL, " +
				"FOREIGN KEY (" + DCDbAdapter.INSTANCE_PROVIDER_ACCOUNT_ID + ") REFERENCES " + DCDbAdapter.PROVIDER_ACCOUNTS_TABLE + "(" + DCDbAdapter.PROVIDER_ACCOUNTS_ID + "));";
		db.execSQL(createInstancesTable);    		

		//CREATE ACTIONS TABLE
		String createActionsTable = "CREATE TABLE " + DCDbAdapter.ACTIONS_TABLE + " ( " + 
		DCDbAdapter.ACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		DCDbAdapter.ACTION_HREF + " TEXT, " +
		DCDbAdapter.ACTION_METHOD + " TEXT, " +
		DCDbAdapter.ACTION_REL + " TEXT, " +
		DCDbAdapter.ACTION_INSTANCE_ID  + " INTEGER NOT NULL, " +
				"FOREIGN KEY (" + DCDbAdapter.ACTION_INSTANCE_ID + ") REFERENCES " + DCDbAdapter.INSTANCES_TABLE + "(" + DCDbAdapter.INSTANCE_ID + "));";
		db.execSQL(createActionsTable);
		
	}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
    {
    }
}
