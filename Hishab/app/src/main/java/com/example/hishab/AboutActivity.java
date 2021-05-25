package com.example.hishab;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //Find views
        Toolbar toolbar = findViewById(R.id.toolbar_about);
        TextView tvAppVersion = findViewById(R.id.textView_appVersion);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(v -> finish());
        tvAppVersion.setText(String.format("Version %s", BuildConfig.VERSION_NAME));
    }
}