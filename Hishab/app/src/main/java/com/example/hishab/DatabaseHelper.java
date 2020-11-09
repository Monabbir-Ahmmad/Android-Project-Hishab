package com.example.hishab;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String DATABASE_NAME = "ExpenseManager.db";
    private static final int VERSION_NUMBER = 1;

    private static final String TABLE_NAME = "Transactions";
    private static final String ID = "_id";
    private static final String CATEGORY = "Category";
    private static final String MONEY = "Money";
    private static final String NOTE = "Note";
    private static final String DATETIME = "Datetime";
    private static final String DATETIME_ID = "DatetimeID";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
        this.context = context;
    }


    //This creates the database for the 1st time
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String createTable = "CREATE TABLE " + TABLE_NAME + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CATEGORY + " TEXT, "
                + MONEY + " INTEGER, "
                + NOTE + " TEXT, "
                + DATETIME + " TEXT, "
                + DATETIME_ID + " INTEGER);";

        try {
            Toast.makeText(context, "Table created", Toast.LENGTH_LONG).show();
            db.execSQL(createTable);

        } catch (Exception e) {
            Toast.makeText(context, "Exception: " + e, Toast.LENGTH_LONG).show();
        }
    }

    //This checks if a table exists
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {
            Toast.makeText(context, "Table Upgraded", Toast.LENGTH_LONG).show();

            db.execSQL("DROP TABLE IF EXISTs " + TABLE_NAME);
            onCreate(db);

        } catch (Exception e) {
            Toast.makeText(context, "Exception: " + e, Toast.LENGTH_LONG).show();
        }


    }

    //This inserts data into the data table
    public void insertData(String category, int money, String note, String datetime, int datetime_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CATEGORY, category);
        contentValues.put(MONEY, money);
        contentValues.put(NOTE, note);
        contentValues.put(DATETIME, datetime);
        contentValues.put(DATETIME_ID, datetime_id);

        long rowID = db.insert(TABLE_NAME, null, contentValues);

        if (rowID == -1) {
            Toast.makeText(context, "Failed to insert data", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Successfully inserted data", Toast.LENGTH_LONG).show();
        }
    }
}
