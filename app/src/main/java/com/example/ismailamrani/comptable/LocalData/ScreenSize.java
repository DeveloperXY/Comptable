package com.example.ismailamrani.comptable.localdata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ismailamrani.comptable.models.ScreenSizeModel;


/**
 * Created by ismai on 15/10/2015.
 */
public class ScreenSize extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SCREENSIZE";
    private static final String TABLE = "SIZE";
    private static final String KEY_ID = "id";
    private static final String KEY_WIDTH = "WIDTH";
    private static final String KEY_HEIGHT = "HEIGHT";

    public ScreenSize(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_WIDTH + " INTEGER,"
                + KEY_HEIGHT + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public void AddSize(int WIDTH, int HEIGHT) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_WIDTH, WIDTH);
        values.put(KEY_HEIGHT, HEIGHT);

        db.insert(TABLE, null, values);

        db.close();
    }

    public ScreenSizeModel GetSize(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE, new String[]{KEY_ID,
                        KEY_WIDTH, KEY_HEIGHT}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ScreenSizeModel size = new ScreenSizeModel();
        size.setWHIDTH(cursor.getInt(1));
        size.setHEIGHT(cursor.getInt(2));

        return size;
    }

    public int getCount() {
        int count;
        String countQuery = "SELECT  * FROM " + TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        return count;
    }
}
