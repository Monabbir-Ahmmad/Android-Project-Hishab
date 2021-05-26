package com.example.hishab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.hishab.ui.expense.ExpenseFragment;
import com.example.hishab.ui.overview.OverviewFragment;
import com.example.hishab.ui.statistics.StatisticsFragment;
import com.google.android.material.navigation.NavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, ChipNavigationBar.OnItemSelectedListener {

    private ChipNavigationBar bottomNavBar;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find views
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        bottomNavBar = findViewById(R.id.bottomNav);
        drawer = findViewById(R.id.drawer);
        NavigationView sideNavView = findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        sideNavView.setNavigationItemSelectedListener(this);
        bottomNavBar.setOnItemSelectedListener(this);

        if (savedInstanceState == null) {
            //This sets the default fragment and bottom nav button on startup
            bottomNavBar.setItemSelected(R.id.bottomNav_overview, true);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new OverviewFragment()).commit();
        }

        useDarkMode();

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

    //This controls fragment transitions and bottom navigation
    @Override
    public void onItemSelected(int id) {
        Fragment selectedFragment = null;

        if (id == R.id.bottomNav_overview) {
            selectedFragment = new OverviewFragment();
        } else if (id == R.id.bottomNav_expense) {
            selectedFragment = new ExpenseFragment();
        } else if (id == R.id.bottomNav_statistics) {
            selectedFragment = new StatisticsFragment();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                selectedFragment).commit();
    }

    //This is the side drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

        } else if (item.getItemId() == R.id.nav_help) {


        } else if (item.getItemId() == R.id.nav_feedback) {
            sendFeedback();

        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    //This method uses dark mode based on settings
    private void useDarkMode() {
        SharedPreferences themePref = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = themePref.getString("theme", "System default");
        if (theme.equals("System default")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        } else if (theme.equals("Light")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (theme.equals("Dark")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
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
            Toast.makeText(this, "There are no email client installed on your device.",
                    Toast.LENGTH_SHORT).show();
        }
    }


}