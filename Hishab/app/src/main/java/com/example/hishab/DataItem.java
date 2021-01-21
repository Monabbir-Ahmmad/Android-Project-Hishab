package com.example.hishab;

import android.content.Context;
import android.content.res.TypedArray;

import java.util.Arrays;

public class DataItem {

    private Context context;
    private int id;
    private String category;
    private float money;
    private String date;
    private String time;
    private String note;
    private Long datetimeId;
    private int icon;

    //Constructor
    public DataItem(Context context) {
        this.context = context;
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

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getDatetimeId() {
        return datetimeId;
    }

    public void setDatetimeId(Long datetimeId) {
        this.datetimeId = datetimeId;
    }

    public int getIcon() {
        return icon;
    }

    private void setIcon(String category) {
        String[] categoryArray = context.getResources().getStringArray(R.array.categoryArray);
        TypedArray iconArray = context.getResources().obtainTypedArray(R.array.iconArray);
        int index = Arrays.asList(categoryArray).indexOf(category);
        this.icon = iconArray.getResourceId(index, -1);
    }


}
