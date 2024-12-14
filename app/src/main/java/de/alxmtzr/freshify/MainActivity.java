package de.alxmtzr.freshify;

import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;

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

//        insertTestData();
    }

    // only for testing purposes
    private void insertTestData() {
        FreshifyDBHelper dbHelper = new FreshifyDBHelper(this);
        FreshifyRepository repository = new FreshifyRepositoryImpl(dbHelper);

        // test data
        repository.insertItem(new ItemEntity(
                0,
                "Apple",
                5,
                1,
                "Fruits & Vegetables",
                LocalDate.of(2024, 12, 31),
                "Fresh and juicy apples"
        ));

        repository.insertItem(new ItemEntity(
                0,
                "Milk",
                2,
                2,
                "Dairy Alternatives",
                LocalDate.of(2024, 12, 25),
                "Low-fat organic milk"
        ));

        repository.insertItem(new ItemEntity(
                0,
                "Chicken Breast",
                3,
                3,
                "Meat & Fish Alternatives",
                LocalDate.of(2024, 12, 20),
                "Fresh chicken, ready to cook"
        ));

        repository.insertItem(new ItemEntity(
                0,
                "Orange Juice",
                1,
                4,
                "Drinks",
                LocalDate.of(2024, 12, 15),
                "No added sugar"
        ));

        repository.insertItem(new ItemEntity(
                0,
                "Lasagna",
                1,
                5,
                "Prepared Meals",
                LocalDate.of(2024, 12, 18),
                "Frozen lasagna for easy dinner"
        ));

        repository.insertItem(new ItemEntity(
                0,
                "Ketchup",
                1,
                6,
                "Sauces, Dips & Spreads",
                LocalDate.of(2025, 1, 10),
                "Classic tomato ketchup"
        ));

        repository.insertItem(new ItemEntity(
                0,
                "Bread Dough",
                2,
                7,
                "Bakery & Dough",
                LocalDate.of(2024, 12, 22),
                "Ready-to-bake dough"
        ));

        repository.insertItem(new ItemEntity(
                0,
                "Chocolate Bar",
                10,
                8,
                "Snacks & Sweets",
                LocalDate.of(2025, 2, 15),
                "Dark chocolate, 70% cocoa"
        ));

        repository.insertItem(new ItemEntity(
                0,
                "Frozen Peas",
                3,
                9,
                "Frozen Items",
                LocalDate.of(2025, 6, 1),
                "Great for quick meals"
        ));

        repository.insertItem(new ItemEntity(
                0,
                "Canned Beans",
                4,
                10,
                "Canned & Non-Perishable",
                LocalDate.of(2025, 12, 31),
                "High in protein"
        ));

        repository.insertItem(new ItemEntity(
                0,
                "Paprika Powder",
                1,
                11,
                "Spices, Herbs & Ingredients",
                LocalDate.of(2026, 5, 10),
                "Perfect for stews"
        ));

        repository.insertItem(new ItemEntity(
                0,
                "Aluminum Foil",
                1,
                12,
                "Miscellaneous",
                LocalDate.of(2028, 12, 31),
                "Useful for food storage"
        ));

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