package com.example.hishab;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

public class DataHolder {

    private int id;
    private String category;
    private int money;
    private String date;
    private String time;
    private String note;
    private Long datetimeId;
    private Drawable icon;
    private Context context;

    //Constructor
    public DataHolder(Context context) {
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

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
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

    public Drawable getIcon() {
        return icon;
    }

    private void setIcon(String category) {

        //if (category == "Food")
        this.icon = ContextCompat.getDrawable(context, R.drawable.ic_amount);
    }


}
