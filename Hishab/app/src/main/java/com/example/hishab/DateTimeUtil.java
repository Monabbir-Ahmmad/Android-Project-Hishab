package com.example.hishab;

import android.content.Context;
import android.widget.EditText;

import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtil {

    public final String START_OF_DAY = "12:00 am";
    public final String END_OF_DAY = "11:59 pm";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    private final SimpleDateFormat timeFormat24H = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.getDefault());
    private final Context context;

    //Constructor
    public DateTimeUtil(Context context) {
        this.context = context;
    }


    //Date picker dialog
    public void showDatePicker(FragmentManager fragmentManager, EditText editText) {
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        datePicker.show(fragmentManager, "Date picker");

        datePicker.addOnPositiveButtonClickListener(
                (MaterialPickerOnPositiveButtonClickListener<Long>) selection ->
                {
                    editText.setText(dateFormat.format(selection));
                });
    }


    //Time picker dialog
    public void showTimePicker(FragmentManager fragmentManager, EditText editText) {
        Calendar calendar = Calendar.getInstance();

        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                .setMinute(calendar.get(Calendar.MINUTE))
                .setTitleText("Select time")
                .build();
        timePicker.show(fragmentManager, "Time picker");

        timePicker.addOnPositiveButtonClickListener(v -> {
            String time = null;
            try {
                time = timeFormat.format(timeFormat24H.parse(String.format("%s:%s",
                        timePicker.getHour(), timePicker.getMinute())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            editText.setText(time);
        });
    }

    //Date range picker dialog
    public void showDateRangePicker(FragmentManager fragmentManager, EditText editText) {
        MaterialDatePicker<Pair<Long, Long>> dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select date")
                .build();

        dateRangePicker.show(fragmentManager, "Date picker");

        dateRangePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                editText.setText(dateFormat.format(selection.first) + " - "
                        + dateFormat.format(selection.first));
            }
        });


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


    //Get time ago from timestamp
    public String getTimeAgo(long timestamp) {
        long day = 86400L;
        long today = getTimestamp(dateFormat.format(new Date()), START_OF_DAY);

        if (timestamp >= today && timestamp < today + day)
            return "Today " + timeFormat.format(timestamp * 1000L);

        else if (timestamp >= today + day && timestamp < today + (day * 2))
            return "Tomorrow " + timeFormat.format(timestamp * 1000L);

        else if (timestamp >= today - day && timestamp < today)
            return "Yesterday " + timeFormat.format(timestamp * 1000L);

        else
            return dateFormat.format(timestamp * 1000L);
    }


}
