package com.example.hishab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String DATABASE_NAME = "ExpenseManager.db";
    private static final int VERSION_NUMBER = 1;

    private static final String TABLE_NAME = "Transactions";
    private static final String ID = "ID";
    private static final String CATEGORY = "Category";
    private static final String MONEY = "Money";
    private static final String DATE = "Date";
    private static final String TIME = "Time";
    private static final String NOTE = "Note";
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
                + DATE + " TEXT, "
                + TIME + " TEXT, "
                + NOTE + " TEXT, "
                + DATETIME_ID + " INTEGER);";

        try {
            Toast.makeText(context, "New table created", Toast.LENGTH_SHORT).show();
            db.execSQL(createTable);

        } catch (Exception e) {
            Toast.makeText(context, "Exception: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    //This checks if a table exists
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {
            Toast.makeText(context, "Table Upgraded", Toast.LENGTH_SHORT).show();

            db.execSQL("DROP TABLE IF EXISTs " + TABLE_NAME);
            onCreate(db);

        } catch (Exception e) {
            Toast.makeText(context, "Exception: " + e, Toast.LENGTH_SHORT).show();
        }


    }

    //This inserts data into table
    public void insertData(String category, float money, String date, String time, String note, Long datetime_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CATEGORY, category);
        contentValues.put(MONEY, money);
        contentValues.put(DATE, date);
        contentValues.put(TIME, time);
        contentValues.put(NOTE, note);
        contentValues.put(DATETIME_ID, datetime_id);

        long rowID = db.insert(TABLE_NAME, null, contentValues);

        if (rowID == -1) {
            Toast.makeText(context, "Failed to insert data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully inserted data", Toast.LENGTH_SHORT).show();
        }

    }

    //This removes all rows form table
    public void removeAll() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTs " + TABLE_NAME);

        Toast.makeText(context, "All data cleared", Toast.LENGTH_SHORT).show();
        onCreate(db);

    }

    //This removes specific row form table
    public void deleteData(int id) {
        String[] whereArgs = new String[]{String.valueOf(id)};

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + "=?", whereArgs);

    }

    //This queries all data from table
    public ArrayList<DataItem> getAllData() {

        ArrayList<DataItem> allData = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                DataItem dataItem = new DataItem();

                dataItem.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                dataItem.setCategory(cursor.getString(cursor.getColumnIndex(CATEGORY)));
                dataItem.setMoney(cursor.getFloat(cursor.getColumnIndex(MONEY)));
                dataItem.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                dataItem.setTime(cursor.getString(cursor.getColumnIndex(TIME)));
                dataItem.setNote(cursor.getString(cursor.getColumnIndex(NOTE)));
                dataItem.setDatetimeId(cursor.getLong(cursor.getColumnIndex(DATETIME_ID)));

                allData.add(dataItem);

            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();

        return allData;
    }
}
