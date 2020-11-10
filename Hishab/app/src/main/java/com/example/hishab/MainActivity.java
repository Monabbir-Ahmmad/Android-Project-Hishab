package com.example.hishab;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {

    private ChipNavigationBar chipNavigationBar;
    private Toolbar toolbar;
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor sharedPrefsEdit;
    private boolean isDarkModeOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        chipNavigationBar = findViewById(R.id.chip_nav_menu);
        //This sets the default fragment and bottom nav button on startup
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new OverviewFragment()).commit();
        chipNavigationBar.setItemSelected(R.id.overview, true);

        //This calls the method that controls the bottom navigation
        bottomNavigationBar();
        //This calls the method that saves app instance
        appSaveInstance();

    }


    //This method saves app instance
    private void appSaveInstance() {

        sharedPrefs = getSharedPreferences("AppPrefs", 0);
        sharedPrefsEdit = sharedPrefs.edit();
        isDarkModeOn = sharedPrefs.getBoolean("DarkMode", false);

        //This will check app theme and set it on startup based on the theme selected
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }
    }


    //This controls fragment transitions and bottom navigation
    private void bottomNavigationBar() {

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


    //This is the toolbar option menu inflater
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }


    //This is the toolbar menus selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.theme_dark:
                if (!isDarkModeOn) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    sharedPrefsEdit.putBoolean("DarkMode", true);
                    sharedPrefsEdit.apply();
                }
                return true;

            case R.id.theme_light:
                if (isDarkModeOn) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    sharedPrefsEdit.putBoolean("DarkMode", false);
                    sharedPrefsEdit.apply();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}