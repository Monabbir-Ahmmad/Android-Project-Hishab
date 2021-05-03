package com.example.hishab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.hishab.expense.ExpenseFragment;
import com.example.hishab.overview.OverviewFragment;
import com.example.hishab.statistics.StatisticsFragment;
import com.google.android.material.navigation.NavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ChipNavigationBar chipNavigationBar;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor sharedPrefsEdit;
    private boolean isDarkModeOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find views
        toolbar = findViewById(R.id.toolbar_main);
        chipNavigationBar = findViewById(R.id.bottomNav);
        drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        //This sets the default fragment and bottom nav button on startup
        chipNavigationBar.setItemSelected(R.id.bottomNav_overview, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new OverviewFragment()).commit();

        //This calls the method that controls the bottom navigation
        bottomNavigationBar();
        //This calls the method that saves app instance
        darkModeInstance();

        //Side drawer switch to change theme
        SwitchCompat switchCompat = navigationView.getMenu().findItem(R.id.nav_dark_mode)
                .getActionView().findViewById(R.id.drawer_switch);
        switchCompat.setChecked(isDarkModeOn);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    sharedPrefsEdit.putBoolean("DarkMode", true);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    sharedPrefsEdit.putBoolean("DarkMode", false);
                }
                sharedPrefsEdit.apply();
                chipNavigationBar.setItemSelected(R.id.bottomNav_overview, true);
            }
        });

    }


    //Close side drawer on back press
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    //This is the side drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_dark_mode) {


        } else if (item.getItemId() == R.id.nav_trash) {


        } else if (item.getItemId() == R.id.nav_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);

        } else if (item.getItemId() == R.id.nav_help) {


        } else if (item.getItemId() == R.id.nav_feedback) {
            sendFeedback();

        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
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

            if (id == R.id.bottomNav_overview) {
                selectedFragment = new OverviewFragment();
            } else if (id == R.id.bottomNav_expense) {
                selectedFragment = new ExpenseFragment();
            } else if (id == R.id.bottomNav_statistics) {
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
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback: Hishab App ");
            startActivity(Intent.createChooser(intent, "Send email using"));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email client installed on your device.", Toast.LENGTH_SHORT).show();
        }
    }


}