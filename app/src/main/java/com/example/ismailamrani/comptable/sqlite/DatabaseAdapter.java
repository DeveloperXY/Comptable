package com.example.ismailamrani.comptable.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ismailamrani.comptable.models.Activation;
import com.example.ismailamrani.comptable.models.User;

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

    public static final String KEY_CODE = "code";
    public static final String KEY_IS_ACTIVATED = "isActivated";

    private static final String TAG = DatabaseAdapter.class.getSimpleName();
    private static final String DATABASE_NAME = "comptableDatabase";
    private static final String USER_TABLE = "User";
    private static final String ACTIVATION_TABLE = "Activation";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_USER_TABLE =
            "CREATE TABLE " + USER_TABLE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_FIRSTNAME + " TEXT NOT NULL, " +
                    KEY_LASTNAME + " TEXT NOT NULL, " +
                    KEY_TYPE + " TEXT NOT NULL, " +
                    KEY_CREATION_DATE + " INTEGER, " +
                    KEY_EXPIRATION_DATE + " INTEGER, " +
                    KEY_USERNAME + " TEXT NOT NULL, " +
                    KEY_PASSWORD + " TEXT NOT NULL, " +
                    KEY_COMPANY_ID + " INTEGER)";
    private static final String CREATE_ACTIVATION_TABLE =
            "CREATE TABLE " + ACTIVATION_TABLE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_CODE + " TEXT NOT NULL, " +
                    KEY_COMPANY_ID + " INTEGER, " +
                    KEY_IS_ACTIVATED + " INTEGER)";
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

    /**
     * @return a User object representing the currently logged-in user,
     * if there was any.
     */
    public User getLoggedUser() {
        Cursor cursor = db.query(
                true,
                USER_TABLE,
                new String[]{KEY_ID, KEY_FIRSTNAME, KEY_LASTNAME,
                        KEY_TYPE, KEY_USERNAME, KEY_PASSWORD, KEY_COMPANY_ID},
                null, null, null, null, null, null);
        return extractUserFromCursor(cursor);
    }

    private User extractUserFromCursor(Cursor cursor) {
        return cursor.moveToFirst() ?
                new User.Builder()
                        .id(cursor.getInt(0))
                        .firstname(cursor.getString(1))
                        .lastname(cursor.getString(2))
                        .type(cursor.getString(3))
                        .username(cursor.getString(4))
                        .password(cursor.getString(5))
                        .companyID(cursor.getInt(6))
                        .createUser() :
                null;
    }

    /**
     * Saves the passed-in user into the local database.
     *
     * @param user to be saved.
     */
    public void saveLoggedInUser(User user) {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, user.getId());
        values.put(KEY_FIRSTNAME, user.getFirstname());
        values.put(KEY_LASTNAME, user.getLastname());
        values.put(KEY_TYPE, user.getType());
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_COMPANY_ID, user.getCompanyID());

        insertUser(values);
    }

    private long insertUser(ContentValues values) {
        return db.insert(USER_TABLE, null, values);
    }

    public Activation getCurrentActivation() {
        Cursor cursor = db.query(
                true,
                ACTIVATION_TABLE,
                new String[]{KEY_ID, KEY_CODE, KEY_COMPANY_ID, KEY_IS_ACTIVATED},
                null, null, null, null, null, null);
        return extractActivationFromCursor(cursor);
    }

    private Activation extractActivationFromCursor(Cursor cursor) {
        return cursor == null ? null :
                cursor.moveToFirst() ? new Activation(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2) == 1,
                        cursor.getInt(3)) : null;
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
            db.execSQL(CREATE_USER_TABLE);
            db.execSQL(CREATE_ACTIVATION_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion +
                    " to " + newVersion + ", which will destroy all old data.");
            db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + ACTIVATION_TABLE);
            onCreate(db);
        }
    }
}