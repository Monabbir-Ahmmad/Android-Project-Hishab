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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DataInputActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button button_save_data;
    private TextView textView_transaction_type, textView_category;
    private TextInputEditText editText_amount, editText_date, editText_time, editText_note;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_input);

        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editText_amount = findViewById(R.id.editText_amount);

        editText_date = findViewById(R.id.editText_date);
        editText_date.setText(new SimpleDateFormat("dd MMM yyyy",
                Locale.getDefault()).format(new Date()));
        editText_date.setOnClickListener(this);

        editText_time = findViewById(R.id.editText_time);
        editText_time.setText(new SimpleDateFormat("hh:mm a",
                Locale.getDefault()).format(new Date()));
        editText_time.setOnClickListener(this);

        editText_note = findViewById(R.id.editText_note);

        button_save_data = findViewById(R.id.button_save_data);
        button_save_data.setOnClickListener(this);

        setTextViewText();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_save_data) {
            saveData();
        } else if (v.getId() == R.id.editText_time) {
            selectTime();
        } else if (v.getId() == R.id.editText_date) {
            selectDate();
        }
    }

    //This sets the transaction type and category
    private void setTextViewText() {
        textView_transaction_type = findViewById(R.id.textView_transactionType);
        textView_category = findViewById(R.id.textView_category);

        Intent intent = getIntent();
        ArrayList<String> s = intent.getStringArrayListExtra("key");
        textView_transaction_type.setText(s.get(0));
        textView_category.setText(s.get(1));
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
                editText_time.setText(simpleDateFormat.format(calendar.getTime()));
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
                editText_date.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    //This saves data on button click
    private void saveData() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        if (!editText_amount.getText().toString().isEmpty()) {
            int money = Integer.parseInt(editText_amount.getText().toString());
            String transaction_type = textView_transaction_type.getText().toString();
            String category = textView_category.getText().toString();
            String date = editText_date.getText().toString();
            String time = editText_time.getText().toString();
            String note = editText_note.getText().toString();
            Long datetime_id = Long.parseLong(generateDatetimeID(date, time));

            databaseHelper.insertData(transaction_type, category, money, date, time, note, datetime_id);

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