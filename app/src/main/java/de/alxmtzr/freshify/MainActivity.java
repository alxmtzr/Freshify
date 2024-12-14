package de.alxmtzr.freshify;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import de.alxmtzr.freshify.data.local.FreshifyRepository;
import de.alxmtzr.freshify.data.local.impl.FreshifyDBHelper;
import de.alxmtzr.freshify.data.local.impl.FreshifyRepositoryImpl;
import de.alxmtzr.freshify.data.model.ItemEntity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initBottomNavBar(savedInstanceState);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set status bar color
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.md_theme_primaryContainer));
    }

    private void initBottomNavBar(Bundle savedInstanceState) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // set home fragment as default
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // determine which fragment to show
            if (item.getItemId() == R.id.home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.fridge) {
                selectedFragment = new FridgeFragment();
            } else if (item.getItemId() == R.id.settings) {
                selectedFragment = new SettingsFragment();
            }

            // show the selected fragment
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });
    }
}