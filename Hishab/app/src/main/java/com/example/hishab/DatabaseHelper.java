package com.example.hishab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String DATABASE_NAME = "ExpenseManager.db";
    private static final int VERSION_NUMBER = 1;

    private static final String TABLE_NAME = "Transactions";
    private static final String ID = "ID";
    private static final String TRANSACTION_TYPE = "TransactionType";
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
                + TRANSACTION_TYPE + " TEXT, "
                + CATEGORY + " TEXT, "
                + MONEY + " INTEGER, "
                + DATE + " TEXT, "
                + TIME + " TEXT, "
                + NOTE + " TEXT, "
                + DATETIME_ID + " INTEGER);";

        try {
            Toast.makeText(context, "Table created", Toast.LENGTH_SHORT).show();
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
    public void insertData(String transaction_type, String category, int money, String date, String time, String note, Long datetime_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TRANSACTION_TYPE, transaction_type);
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
        db.execSQL("DELETE FROM " + TABLE_NAME);
        Toast.makeText(context, "All data cleared", Toast.LENGTH_SHORT).show();
        db.close();

    }

    //This queries all data from table
    public ArrayList<DataHolder> getAllData() {

        ArrayList<DataHolder> allData = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                DataHolder dataHolder = new DataHolder(context);

                dataHolder.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                dataHolder.setTransaction_type(cursor.getString(cursor.getColumnIndex(TRANSACTION_TYPE)));
                dataHolder.setCategory(cursor.getString(cursor.getColumnIndex(CATEGORY)));
                dataHolder.setMoney(cursor.getInt(cursor.getColumnIndex(MONEY)));
                dataHolder.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                dataHolder.setTime(cursor.getString(cursor.getColumnIndex(TIME)));
                dataHolder.setNote(cursor.getString(cursor.getColumnIndex(NOTE)));
                dataHolder.setDatetimeId(cursor.getLong(cursor.getColumnIndex(DATETIME_ID)));

                allData.add(dataHolder);

            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();

        return allData;
    }
}
