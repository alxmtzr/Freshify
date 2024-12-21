package de.alxmtzr.freshify.ui;

import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import de.alxmtzr.freshify.R;
import de.alxmtzr.freshify.data.local.FreshifyRepository;
import de.alxmtzr.freshify.data.local.impl.FreshifyDBHelper;
import de.alxmtzr.freshify.data.local.impl.FreshifyRepositoryImpl;
import de.alxmtzr.freshify.data.model.ItemEntity;
import de.alxmtzr.freshify.ui.fragments.FridgeFragment;
import de.alxmtzr.freshify.ui.fragments.HomeFragment;
import de.alxmtzr.freshify.ui.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initBottomNavBar(savedInstanceState);

//        insertTestData();
    }

    // only for test purposes
    private void insertTestData() {
        FreshifyRepository repository = new FreshifyRepositoryImpl(new FreshifyDBHelper(this));

        // Check if data already exists
        if (!repository.getAllItems().isEmpty()) {
            return; // Exit if data already exists
        }

        List<ItemEntity> exampleItems = new ArrayList<>();
        // Fruits & Vegetables (Category ID: 1)
        exampleItems.add(new ItemEntity(0, "Apple", 5, 1, "Fruits & Vegetables", LocalDate.of(2024, 12, 31), "Fresh and juicy apples"));
        exampleItems.add(new ItemEntity(0, "Carrots", 3, 1, "Fruits & Vegetables", LocalDate.of(2024, 12, 20), "Organic carrots"));
        exampleItems.add(new ItemEntity(0, "Bananas", 6, 1, "Fruits & Vegetables", LocalDate.of(2024, 12, 25), "Ripe and sweet bananas"));
        exampleItems.add(new ItemEntity(0, "Spinach", 2, 1, "Fruits & Vegetables", LocalDate.of(2024, 12, 15), "Washed and ready to cook"));

        // Dairy Alternatives (Category ID: 2)
        exampleItems.add(new ItemEntity(0, "Milk", 2, 2, "Dairy Alternatives", LocalDate.of(2024, 12, 25), "Low-fat organic milk"));
        exampleItems.add(new ItemEntity(0, "Butter", 1, 2, "Dairy Alternatives", LocalDate.of(2025, 1, 10), "Unsalted butter"));
        exampleItems.add(new ItemEntity(0, "Cheddar Cheese", 1, 2, "Dairy Alternatives", LocalDate.of(2025, 1, 15), "Mature cheddar block"));

        // Meat & Fish Alternatives (Category ID: 3)
        exampleItems.add(new ItemEntity(0, "Chicken Breast", 3, 3, "Meat & Fish Alternatives", LocalDate.of(2024, 12, 20), "Fresh chicken, ready to cook"));
        exampleItems.add(new ItemEntity(0, "Salmon Fillet", 2, 3, "Meat & Fish Alternatives", LocalDate.of(2024, 12, 22), "Wild-caught salmon"));
        exampleItems.add(new ItemEntity(0, "Ground Beef", 1, 3, "Meat & Fish Alternatives", LocalDate.of(2024, 12, 19), "For burgers and pasta"));

        // Drinks (Category ID: 4)
        exampleItems.add(new ItemEntity(0, "Orange Juice", 1, 4, "Drinks", LocalDate.of(2024, 12, 15), "No added sugar"));
        exampleItems.add(new ItemEntity(0, "Coke", 6, 4, "Drinks", LocalDate.of(2025, 6, 10), "Cans of classic coke"));
        exampleItems.add(new ItemEntity(0, "Sparkling Water", 3, 4, "Drinks", LocalDate.of(2025, 1, 5), "Lemon flavored"));

        // Prepared Meals (Category ID: 5)
        exampleItems.add(new ItemEntity(0, "Lasagna", 1, 5, "Prepared Meals", LocalDate.of(2024, 12, 18), "Frozen lasagna for easy dinner"));
        exampleItems.add(new ItemEntity(0, "Pizza Margherita", 2, 5, "Prepared Meals", LocalDate.of(2024, 12, 22), "Stone-baked pizza"));
        exampleItems.add(new ItemEntity(0, "Vegetable Stir Fry", 1, 5, "Prepared Meals", LocalDate.of(2024, 12, 19), "Quick microwave meal"));

        // Sauces, Dips & Spreads (Category ID: 6)
        exampleItems.add(new ItemEntity(0, "Ketchup", 1, 6, "Sauces, Dips & Spreads", LocalDate.of(2025, 1, 10), "Classic tomato ketchup"));
        exampleItems.add(new ItemEntity(0, "Mayonnaise", 1, 6, "Sauces, Dips & Spreads", LocalDate.of(2025, 2, 15), "Creamy and tangy"));
        exampleItems.add(new ItemEntity(0, "Hummus", 2, 6, "Sauces, Dips & Spreads", LocalDate.of(2024, 12, 30), "Garlic-flavored hummus"));

        // Bakery & Dough (Category ID: 7)
        exampleItems.add(new ItemEntity(0, "Bread Dough", 2, 7, "Bakery & Dough", LocalDate.of(2024, 12, 22), "Ready-to-bake dough"));
        exampleItems.add(new ItemEntity(0, "Croissants", 4, 7, "Bakery & Dough", LocalDate.of(2024, 12, 18), "Frozen croissants"));
        exampleItems.add(new ItemEntity(0, "Pizza Dough", 3, 7, "Bakery & Dough", LocalDate.of(2024, 12, 20), "Fresh pizza dough"));

        // Snacks & Sweets (Category ID: 8)
        exampleItems.add(new ItemEntity(0, "Chocolate Bar", 10, 8, "Snacks & Sweets", LocalDate.of(2025, 2, 15), "Dark chocolate, 70% cocoa"));
        exampleItems.add(new ItemEntity(0, "Granola Bars", 5, 8, "Snacks & Sweets", LocalDate.of(2025, 1, 25), "Peanut butter flavor"));
        exampleItems.add(new ItemEntity(0, "Ice Cream", 2, 8, "Snacks & Sweets", LocalDate.of(2025, 5, 15), "Vanilla ice cream tub"));

        // Frozen Items (Category ID: 9)
        exampleItems.add(new ItemEntity(0, "Frozen Peas", 3, 9, "Frozen Items", LocalDate.of(2025, 6, 1), "Great for quick meals"));
        exampleItems.add(new ItemEntity(0, "Frozen French Fries", 2, 9, "Frozen Items", LocalDate.of(2025, 5, 20), "Crispy and easy to bake"));

        // Canned & Non-Perishable (Category ID: 10)
        exampleItems.add(new ItemEntity(0, "Canned Beans", 4, 10, "Canned & Non-Perishable", LocalDate.of(2025, 12, 31), "High in protein"));
        exampleItems.add(new ItemEntity(0, "Tomato Paste", 2, 10, "Canned & Non-Perishable", LocalDate.of(2025, 6, 30), "Rich and thick"));

        // Spices, Herbs & Ingredients (Category ID: 11)
        exampleItems.add(new ItemEntity(0, "Paprika Powder", 1, 11, "Spices, Herbs & Ingredients", LocalDate.of(2026, 5, 10), "Perfect for stews"));
        exampleItems.add(new ItemEntity(0, "Oregano", 1, 11, "Spices, Herbs & Ingredients", LocalDate.of(2026, 12, 1), "Dried oregano leaves"));

        // Miscellaneous (Category ID: 12)
        exampleItems.add(new ItemEntity(0, "Aluminum Foil", 1, 12, "Miscellaneous", LocalDate.of(2028, 12, 31), "Useful for food storage"));
        exampleItems.add(new ItemEntity(0, "Sandwich Bags", 3, 12, "Miscellaneous", LocalDate.of(2027, 8, 15), "Plastic bags for sandwiches"));

        for (ItemEntity item : exampleItems) {
            repository.insertItem(item);
        }
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarItemDetails);
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