package com.example.hishab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.hishab.expense.ExpenseFragment;
import com.example.hishab.overview.OverviewFragment;
import com.example.hishab.statistics.StatisticsFragment;
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

        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        chipNavigationBar = findViewById(R.id.chip_nav_menu);
        //This sets the default fragment and bottom nav button on startup
        chipNavigationBar.setItemSelected(R.id.overview, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new OverviewFragment()).commit();

        //This calls the method that controls the bottom navigation
        bottomNavigationBar();
        //This calls the method that saves app instance
        darkModeInstance();

    }


    //This is the toolbar option menu inflater
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        menu.findItem(R.id.darkMode).setChecked(isDarkModeOn);
        return true;
    }


    //This is the toolbar menus selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.darkMode) {

            if (item.isChecked()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                sharedPrefsEdit.putBoolean("DarkMode", false);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                sharedPrefsEdit.putBoolean("DarkMode", true);
            }
            item.setChecked(isDarkModeOn);
            sharedPrefsEdit.apply();
            chipNavigationBar.setItemSelected(R.id.overview, true);

        } else if (item.getItemId() == R.id.feedback) {
            sendFeedback();

        } else if (item.getItemId() == R.id.about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);

        } else if (item.getItemId() == R.id.clearData) {
            DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
            databaseHelper.deleteTable();
        }

        return super.onOptionsItemSelected(item);
    }


    //This method saves app instance
    private void darkModeInstance() {
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
        chipNavigationBar.setOnItemSelectedListener(id -> {
            Fragment selectedFragment = null;

            if (id == R.id.overview) {
                selectedFragment = new OverviewFragment();
            } else if (id == R.id.expense) {
                selectedFragment = new ExpenseFragment();
            } else if (id == R.id.statistics) {
                selectedFragment = new StatisticsFragment();
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        });
    }


    //This will let users send feedback to the developers
    private void sendFeedback() {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"monabbir.ahmmad@yahoo.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback: Hishab App " + BuildConfig.VERSION_NAME);
            startActivity(Intent.createChooser(intent, "Send email using"));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email client installed on your device.", Toast.LENGTH_SHORT).show();
        }
    }


}