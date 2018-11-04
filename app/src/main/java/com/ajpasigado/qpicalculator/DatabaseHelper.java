package com.ajpasigado.qpicalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public final String TABLE_NAME = "preferences";
    public final String col1 = "first_session";
    public final String col2 = "username";
    public final String col3 = "password";
    public final String col4 = "grades";
    public final String col5 = "totalQPI";
    public final String col6 = "totalUnits";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "qpi_databse", null, 1);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE preferences (ID INTEGER PRIMARY KEY AUTOINCREMENT, first_session INTEGER DEFAULT 1, username TEXT, password TEXT, grades TEXT, totalQPI REAL, totalUnits INTEGER)");
        rawData(sqLiteDatabase);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public void rawData(SQLiteDatabase sqLiteDatabase) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("first_session", Integer.valueOf(1));
        contentValues.put("username", "none");
        contentValues.put("password", "none");
        contentValues.put("grades", "none");
        contentValues.put("totalQPI", Integer.valueOf(0));
        contentValues.put("totalUnits", Integer.valueOf(0));
        sqLiteDatabase.insert("preferences", null, contentValues);
    }

    public void updateData(String username, String password, String grades, Double totalQPI, Integer totalUnits) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("first_session", Integer.valueOf(0));
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("grades", grades);
        contentValues.put("totalQPI", totalQPI);
        contentValues.put("totalUnits", totalUnits);
        getWritableDatabase().update("preferences", contentValues, null, null);
    }

    public Cursor getData() {
        return getWritableDatabase().rawQuery("SELECT * FROM preferences", null);
    }
}
