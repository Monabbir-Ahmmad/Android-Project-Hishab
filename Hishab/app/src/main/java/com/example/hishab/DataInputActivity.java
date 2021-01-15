package com.example.hishab;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DataInputActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button btn_saveData;
    private TextView tv_category;
    private TextInputEditText et_amount, et_date, et_time, et_note;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_input);

        //This is the toolbar
        toolbar = findViewById(R.id.toolbar_dataInput);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Toolbar back arrow
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tv_category = findViewById(R.id.textView_category);
        et_amount = findViewById(R.id.editText_amount);
        et_note = findViewById(R.id.editText_note);

        et_date = findViewById(R.id.editText_date);
        et_date.setOnClickListener(this::onClick);

        et_time = findViewById(R.id.editText_time);
        et_time.setOnClickListener(this::onClick);

        btn_saveData = findViewById(R.id.button_saveData);
        btn_saveData.setOnClickListener(this::onClick);

        setDefaultOnLoad();
    }


    //This sets expense category and current date-time on view create
    private void setDefaultOnLoad() {
        tv_category.setText(getIntent().getStringExtra("category"));
        et_date.setText(new SimpleDateFormat("dd MMM yyyy",
                Locale.getDefault()).format(new Date()));
        et_time.setText(new SimpleDateFormat("hh:mm a",
                Locale.getDefault()).format(new Date()));
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_saveData) {
            saveData();
        } else if (v.getId() == R.id.editText_time) {
            selectTime();
        } else if (v.getId() == R.id.editText_date) {
            selectDate();
        }
    }


    //This is time picker dialog
    private void selectTime() {
        calendar = Calendar.getInstance();
        int mMinute = calendar.get(Calendar.MINUTE);
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        Boolean withAMPM = true;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);

                simpleDateFormat = new SimpleDateFormat(withAMPM ? "hh:mm a" : "HH:mm", Locale.getDefault());
                et_time.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, mHour, mMinute, !withAMPM);

        timePickerDialog.show();
    }


    //This is date picker dialog
    private void selectDate() {
        calendar = Calendar.getInstance();
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        int mMonth = calendar.get(Calendar.MONTH);
        int mYear = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                simpleDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                et_date.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }


    //This saves data on button click
    private void saveData() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        if (!et_amount.getText().toString().isEmpty() && Float.parseFloat(et_amount.getText().toString()) > 0) {
            float money = Float.parseFloat(et_amount.getText().toString());
            String category = tv_category.getText().toString();
            String date = et_date.getText().toString();
            String time = et_time.getText().toString();
            String note = et_note.getText().toString();
            Long datetime_id = Long.parseLong(generateDatetimeID(date, time));

            if (note.trim().isEmpty())
                note = null;

            databaseHelper.insertData(category, money, date, time, note, datetime_id);

            startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        } else {
            Toast.makeText(getApplicationContext(), "Please fill the required fields", Toast.LENGTH_SHORT).show();
        }
    }


    //This generates DatetimeID form Date
    private String generateDatetimeID(String date, String time) {

        String datetime = date + " " + time;
        String inputPattern = "dd MMM yyyy hh:mm a";
        String outputPattern = "yyyyMMddHHmm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date datetimeInputFormat = null;
        String datetimeOutputFormat = null;

        try {
            datetimeInputFormat = inputFormat.parse(datetime);
            datetimeOutputFormat = outputFormat.format(datetimeInputFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datetimeOutputFormat;
    }


}