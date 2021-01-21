package com.example.hishab;

import android.content.Context;

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
    private int[] iconArray = {R.drawable.ic_automobile, R.drawable.ic_bills, R.drawable.ic_clothes,
            R.drawable.ic_education, R.drawable.ic_entertainment, R.drawable.ic_finance, R.drawable.ic_food,
            R.drawable.ic_health, R.drawable.ic_housing, R.drawable.ic_misc, R.drawable.ic_parenting,
            R.drawable.ic_personal, R.drawable.ic_pet, R.drawable.ic_service, R.drawable.ic_shopping,
            R.drawable.ic_tax, R.drawable.ic_transport};

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
        String[] categoryArray = context.getResources().getStringArray(R.array.Category);
        int index = Arrays.asList(categoryArray).indexOf(category);
        this.icon = iconArray[index];
    }


}
