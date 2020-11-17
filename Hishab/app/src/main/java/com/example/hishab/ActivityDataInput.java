package com.example.hishab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ActivityDataInput extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button button_save_data;
    private TextView textView_transaction_type, textView_category;
    private TextInputEditText editText_amount, editText_date, editText_time, editText_note;
    private TimePicker timePicker;
    private DatePicker datePicker;

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

    private void setTextViewText() {
        textView_transaction_type = findViewById(R.id.textView_transactionType);
        textView_category = findViewById(R.id.textView_category);

        Intent intent = getIntent();
        ArrayList<String> s = intent.getStringArrayListExtra("key");
        textView_transaction_type.setText(s.get(0));
        textView_category.setText(s.get(1));

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.button_save_data) {

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
                Toast.makeText(getApplicationContext(), "Please fill the required fields", Toast.LENGTH_LONG).show();
            }
        }

        if (v.getId() == R.id.editText_time) {
            timePicker = new TimePicker(editText_time, true);
        }
        if (v.getId() == R.id.editText_date) {
            datePicker = new DatePicker(editText_date, this);
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