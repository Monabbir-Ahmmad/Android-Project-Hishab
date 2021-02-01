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
import java.util.Date;
import java.util.Locale;

public class DataInputActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button btn_saveData;
    private TextView tv_category;
    private TextInputEditText et_amount, et_date, et_time, et_note;
    private CustomDateTime customDateTime;
    private boolean isUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_input);

        //Check if the call is for adding new date or update existing data
        isUpdate = getIntent().getBooleanExtra("update", false);

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

        customDateTime = new CustomDateTime(this);

        tv_category = findViewById(R.id.textView_category);
        et_amount = findViewById(R.id.editText_amount);
        et_note = findViewById(R.id.editText_note);

        et_date = findViewById(R.id.editText_date);
        et_date.setOnClickListener(this::onClick);

        et_time = findViewById(R.id.editText_time);
        et_time.setOnClickListener(this::onClick);

        btn_saveData = findViewById(R.id.button_saveData);
        btn_saveData.setOnClickListener(this::onClick);

        if (!isUpdate) {
            setTitle("Add new");
            setViewsForNew();
        } else {
            setTitle("Edit");
            setViewsForUpdate();
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_saveData) {
            saveData();
        } else if (v.getId() == R.id.editText_time) {
            customDateTime.pickTime(et_time);
        } else if (v.getId() == R.id.editText_date) {
            customDateTime.pickDate(et_date);
        }
    }


    //Set expense category, date, time, note on create
    private void setViewsForNew() {
        tv_category.setText(getIntent().getStringExtra("category"));
        et_date.setText(new SimpleDateFormat("dd MMM yyyy",
                Locale.getDefault()).format(new Date()));
        et_time.setText(new SimpleDateFormat("hh:mm a",
                Locale.getDefault()).format(new Date()));
    }


    //Set expense category, amount, date, time, note on create to update data
    private void setViewsForUpdate() {
        tv_category.setText(getIntent().getStringExtra("category"));
        et_amount.setText(getIntent().getStringExtra("money"));
        et_date.setText(getIntent().getStringExtra("date"));
        et_time.setText(getIntent().getStringExtra("time"));
        et_note.setText(getIntent().getStringExtra("note"));
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
            Long timestamp = customDateTime.getTimestamp(date, time);

            if (note.trim().isEmpty())
                note = null;

            //If not update data, insert new data
            if (!isUpdate) {
                databaseHelper.insertData(category, money, date, time, note, timestamp);
            }
            //If update, update existing data
            else {
                int id = getIntent().getIntExtra("id", -1);
                databaseHelper.updateData(id, category, money, date, time, note, timestamp);
            }
            startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        } else {
            Toast.makeText(getApplicationContext(), "Please fill the required fields", Toast.LENGTH_SHORT).show();
        }
    }


}