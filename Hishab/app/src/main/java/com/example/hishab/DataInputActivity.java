package com.example.hishab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.hishab.database.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataInputActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button btnSaveData;
    private TextView tvCategory;
    private TextInputEditText etAmount, etDate, etTime, etNote;
    private ImageView ivIcon;
    private DateTimeUtil dateTimeUtil;
    private boolean isUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_input);

        //Check if the call is for adding new date or update existing data
        isUpdate = getIntent().getBooleanExtra("update", false);

        //Find views
        toolbar = findViewById(R.id.toolbar_dataInput);
        tvCategory = findViewById(R.id.textView_category);
        ivIcon = findViewById(R.id.imageView_icon);
        etAmount = findViewById(R.id.editText_amount);
        etNote = findViewById(R.id.editText_note);
        etDate = findViewById(R.id.editText_date);
        etTime = findViewById(R.id.editText_time);
        btnSaveData = findViewById(R.id.button_saveData);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(v -> finish());

        dateTimeUtil = new DateTimeUtil();

        etDate.setOnClickListener(this);
        etTime.setOnClickListener(this);
        btnSaveData.setOnClickListener(this);

        //Set toolbar title
        if (!isUpdate) {
            setTitle("Add new");
            setViewsNew();
        } else {
            setTitle("Edit");
            setViewsUpdate();
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_saveData) {
            saveData();

        } else if (v.getId() == R.id.editText_time) {
            dateTimeUtil.showTimePicker(getSupportFragmentManager(), etTime);

        } else if (v.getId() == R.id.editText_date) {
            dateTimeUtil.showDatePicker(getSupportFragmentManager(), etDate);

        }
    }


    //Set expense category, date, time, note on create
    private void setViewsNew() {
        tvCategory.setText(getIntent().getStringExtra("category"));
        ivIcon.setImageResource(getIntent().getIntExtra("icon", -1));
        etDate.setText(new SimpleDateFormat("dd MMM yyyy",
                Locale.getDefault()).format(new Date()));
        etTime.setText(new SimpleDateFormat("hh:mm a",
                Locale.getDefault()).format(new Date()));
    }


    //Set expense category, amount, date, time, note on create to update data
    private void setViewsUpdate() {
        tvCategory.setText(getIntent().getStringExtra("category"));
        ivIcon.setImageResource(getIntent().getIntExtra("icon", -1));
        etAmount.setText(getIntent().getStringExtra("amount"));
        etDate.setText(getIntent().getStringExtra("date"));
        etTime.setText(getIntent().getStringExtra("time"));
        etNote.setText(getIntent().getStringExtra("note"));
    }


    //This saves data on button click
    private void saveData() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        if (!etAmount.getText().toString().isEmpty() && Float.parseFloat(etAmount.getText().toString()) > 0) {
            float amount = Float.parseFloat(etAmount.getText().toString());
            String category = tvCategory.getText().toString();
            String note = etNote.getText().toString();
            Long timestamp = dateTimeUtil.getTimestamp(etDate.getText().toString(), etTime.getText().toString());

            if (note.trim().isEmpty())
                note = null;

            //If not update data, insert new data
            if (!isUpdate) {
                databaseHelper.insertData(category, amount, note, timestamp);
            }
            //If update, update existing data
            else {
                int id = getIntent().getIntExtra("id", -1);
                databaseHelper.updateData(id, category, amount, note, timestamp);
            }
            startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        } else {
            Toast.makeText(getApplicationContext(), "Please fill the required fields", Toast.LENGTH_SHORT).show();
        }
    }


}