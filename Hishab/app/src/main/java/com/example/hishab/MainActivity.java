package com.example.hishab;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new OverviewFragment()).commit();

        chipNavigationBar = findViewById(R.id.chip_nav_menu);
        chipNavigationBar.setItemSelected(R.id.overview, true);

        bottomNav();
    }

    //This is the toolbar option menu inflater
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_button, menu);
        return true;
    }

    //This controls fragment transitions and bottom navigation
    private void bottomNav() {

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {

                Fragment selectedFragment = null;

                switch (id) {
                    case R.id.overview:
                        selectedFragment = new OverviewFragment();

                        break;
                    case R.id.report:
                        selectedFragment = new ReportFragment();

                        break;
                    case R.id.transaction:
                        selectedFragment = new TransactionFragment();

                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();

            }
        });
    }
}