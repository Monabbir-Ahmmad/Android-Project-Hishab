package com.example.hishab;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AboutActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tv_appVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //This is the toolbar
        toolbar = findViewById(R.id.toolbar_about);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Toolbar back arrow
        toolbar.setNavigationOnClickListener(v -> finish());

        tv_appVersion = findViewById(R.id.textView_appVersion);
        tv_appVersion.setText(String.format("Version %s", BuildConfig.VERSION_NAME));
    }
}