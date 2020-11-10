package com.example.hishab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class ActivityDataInput extends AppCompatActivity {

    private Toolbar toolbar;
    private Button button_save_data;
    private TextView textView_transaction_type, textView_category;

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


        button_save_data = findViewById(R.id.button_save_data);
        button_save_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        setTextViewText();
    }

    private void setTextViewText() {
        textView_transaction_type = findViewById(R.id.textView_transactionType);
        textView_category = findViewById(R.id.textView_category);

        Intent i = getIntent();
        ArrayList<String> s = i.getStringArrayListExtra("key");
        textView_transaction_type.setText(s.get(0));
        textView_category.setText(s.get(1));

    }
}