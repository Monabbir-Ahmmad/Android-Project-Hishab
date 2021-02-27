package com.example.hishab;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomDateTime {

    public final String START_OF_DAY = "12:00 am";
    public final String END_OF_DAY = "11:59 pm";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.getDefault());
    private final Context context;

    //Constructor
    public CustomDateTime(Context context) {
        this.context = context;
    }


    //Date picker dialog
    public void pickDate(EditText editText) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            editText.setText(dateFormat.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(1L);

        datePickerDialog.show();
    }


    //Time picker dialog
    public void pickTime(EditText editText) {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, (view, hourOfDay, minute) -> {
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);

            editText.setText(timeFormat.format(calendar.getTime()));
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);

        timePickerDialog.show();
    }

    //Generates Timestamp in sec form Date and time
    public long getTimestamp(String date, String time) {
        if (time == null || time.isEmpty())
            time = START_OF_DAY;

        long timestamp = 0L;
        try {
            Date datetime = dateTimeFormat.parse(date + " " + time);
            timestamp = datetime.getTime() / 1000L;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;
    }

    //Get get date from timestamp
    public String getDate(long timestamp) {
        return dateFormat.format(timestamp * 1000L);
    }

    //Get get time from timestamp
    public String getTime(long timestamp) {
        return timeFormat.format(timestamp * 1000L);
    }


}
