package com.example.hishab;

import android.content.Context;

public class DataHolder {

    private int id;
    private String category;
    private float money;
    private String date;
    private String time;
    private String note;
    private Long datetimeId;
    private int icon;

    //Constructor
    public DataHolder() {

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

        //if (category == "Food")
        this.icon = R.drawable.ic_amount;
    }


}
