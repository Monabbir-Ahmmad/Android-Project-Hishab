package com.example.hishab;

import android.content.Context;
import android.content.res.TypedArray;

import java.util.Arrays;

public class DataItem {

    private final Context context;
    private int id;
    private String category;
    private float amount;
    private String note;
    private final String[] categoryArray;
    private int icon;
    private final TypedArray iconArray;
    private long timestamp;

    //Constructor
    public DataItem(Context context) {
        this.context = context;
        categoryArray = context.getResources().getStringArray(R.array.categoryArray);
        iconArray = context.getResources().obtainTypedArray(R.array.iconArray);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
        setIcon(this.category);
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getIcon() {
        return icon;
    }

    private void setIcon(String category) {
        int index = Arrays.asList(categoryArray).indexOf(category);
        this.icon = iconArray.getResourceId(index, -1);
    }


}
