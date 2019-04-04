package com.studienprojekt.smarthome.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.studienprojekt.smarthome.Device;
import com.studienprojekt.smarthome.activities.MainActivity;

public class DBHelper extends SQLiteOpenHelper {

    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 1;
    private final static String TAG= MainActivity.class.getName().toString();

    // Database Name
    private static final String DATABASE_NAME = "roomsearch.db";

    public DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here

        String CREATE_TABLE_ROOM = "CREATE TABLE " + Device.TABLE  + "("
                + Device.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Device.KEY_name + " TEXT)";
        db.execSQL(CREATE_TABLE_ROOM);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!
        Log.d(TAG, "onUpgrade is called!");
        db.execSQL(" DROP TABLE IF EXISTS " + Device.TABLE);
        Log.d(TAG, "Older Tables are deleted!");

        // Create tables again
        onCreate(db);
    }
}