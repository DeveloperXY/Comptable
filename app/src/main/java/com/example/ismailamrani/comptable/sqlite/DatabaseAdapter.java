package com.example.ismailamrani.comptable.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Mohammed Aouf ZOUAG on 29/04/2016.
 */
public class DatabaseAdapter {

    /**
     * The singleton instance.
     */
    private static DatabaseAdapter instance;

    public static final String KEY_ID = "id";
    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_TYPE = "type";
    public static final String KEY_CREATION_DATE = "creationDate";
    public static final String KEY_EXPIRATION_DATE = "expirationDate";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_COMPANY_ID = "companyID";

    private static final String TAG = DatabaseAdapter.class.getSimpleName();
    private static final String DATABASE_NAME = "contactsDatabase";
    private static final String DATABASE_TABLE = "contacts";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLES_CREATE =
            "CREATE TABLE " + DATABASE_TABLE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_FIRSTNAME + " TEXT NOT NULL, " +
                    KEY_LASTNAME + " TEXT NOT NULL, " +
                    KEY_TYPE + " TEXT NOT NULL, " +
                    KEY_CREATION_DATE + " INTEGER, " +
                    KEY_EXPIRATION_DATE + " INTEGER, " +
                    KEY_USERNAME + " TEXT NOT NULL, " +
                    KEY_PASSWORD + " TEXT NOT NULL, " +
                    KEY_COMPANY_ID + " INTEGER)";
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Context mContext;

    /**
     * A static factory method.
     *
     * @param context
     * @return the singleton instance of the DatabaseAdapter.
     */
    public static DatabaseAdapter getInstance(Context context) {
        if (instance == null) {
            synchronized (DatabaseAdapter.class) {
                if (instance == null)
                    instance = new DatabaseAdapter(context).open();
            }
        }

        return instance;
    }

    private DatabaseAdapter(Context context) {
        mContext = context;
        dbHelper = new DatabaseHelper(context);
    }

    private DatabaseAdapter open() {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLES_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion +
                    " to " + newVersion + ", which will destroy all old data.");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }
}