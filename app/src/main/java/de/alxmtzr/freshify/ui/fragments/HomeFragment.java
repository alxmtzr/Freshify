package de.alxmtzr.freshify.ui.fragments;

import static de.alxmtzr.freshify.ui.fragments.SettingsFragment.PREF_DAYS_UNTIL_EXPIRY;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import de.alxmtzr.freshify.R;
import de.alxmtzr.freshify.adapter.ExpiryItemsAdapter;
import de.alxmtzr.freshify.data.concurrency.GetExpiredItemsRunnable;
import de.alxmtzr.freshify.data.concurrency.GetExpiringItemsRunnable;
import de.alxmtzr.freshify.data.local.CategoryDatabase;
import de.alxmtzr.freshify.data.local.FreshifyRepository;
import de.alxmtzr.freshify.data.local.impl.CategoryDatabaseImpl;
import de.alxmtzr.freshify.data.local.impl.FreshifyDBHelper;
import de.alxmtzr.freshify.data.local.impl.FreshifyRepositoryImpl;
import de.alxmtzr.freshify.data.model.ItemEntity;

public class HomeFragment extends Fragment {

    private static final String GOOGLE_SEARCH_URL = "https://www.google.com/search?q=";

    private int daysUntilExpiry;

    private ListView expiringItemsListView;
    private ExpiryItemsAdapter expiringItemsAdapter;
    private ListView expiredItemsListView;
    private ExpiryItemsAdapter expiredItemsAdapter;
    private TextView noExpiringFoodText;
    private TextView noExpiredFoodText;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initCategoryDropDown(view);
        initExpiryDateField(view);
        initWebSearchButton(view);
        initSaveItemButton(view);
        initExpiringCardView(view);
        initExpiredCardView(view);
        getDaysUntilExpiry(view);
    }

    private void getDaysUntilExpiry(View view) {
        SharedPreferences preferences = view.getContext().getSharedPreferences("prefs_freshify", 0);
        daysUntilExpiry = preferences.getInt(PREF_DAYS_UNTIL_EXPIRY, 3); // Default to 3 days
    }

    private void initExpiredCardView(@NonNull View view) {
        noExpiredFoodText = view.findViewById(R.id.noExpiredFoodText);
        expiredItemsListView = view.findViewById(R.id.expiredFoodListView);
        expiredItemsAdapter = new ExpiryItemsAdapter(
                requireContext(),
                new ArrayList<>(),
                ExpiryItemsAdapter.TYPE_EXPIRED);
        expiredItemsListView.setAdapter(expiredItemsAdapter);
    }

    private void initExpiringCardView(@NonNull View view) {
        noExpiringFoodText = view.findViewById(R.id.noExpiringFoodText);
        expiringItemsListView = view.findViewById(R.id.expiringFoodListView);
        expiringItemsAdapter = new ExpiryItemsAdapter(
                requireContext(),
                new ArrayList<>(),
                ExpiryItemsAdapter.TYPE_EXPIRING);
        expiringItemsListView.setAdapter(expiringItemsAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();

        saveFieldValues();
    }

    private void saveFieldValues() {
        SharedPreferences prefs = getActivity().getSharedPreferences("prefs_freshify", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        TextInputEditText itemNameEditText = requireView().findViewById(R.id.itemNameEditText);
        String itemName = itemNameEditText.getText().toString();
        editor.putString("itemName", itemName);

        TextInputEditText itemQuantityEditText = requireView().findViewById(R.id.itemQuantityEditText);
        String itemQuantity = itemQuantityEditText.getText().toString();
        editor.putString("itemQuantity", itemQuantity);

        AutoCompleteTextView categoryDropdownMenu = requireView().findViewById(R.id.categoryDropdownMenu);
        String category = categoryDropdownMenu.getText().toString();
        editor.putString("category", category);

        TextInputEditText expiryDateEditText = requireView().findViewById(R.id.expiryDateEditText);
        String expiryDate = expiryDateEditText.getText().toString();
        editor.putString("expiryDate", expiryDate);

        TextInputEditText commentEditText = requireView().findViewById(R.id.commentEditText);
        String comment = commentEditText.getText().toString();
        editor.putString("comment", comment);

        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();

        // fill in the fields with the saved data
        fillInFields();

        loadExpiringItems();
        loadExpiredItems();
        getDaysUntilExpiry(requireView());
    }

    private void fillInFields() {
        SharedPreferences prefs = getActivity().getSharedPreferences("prefs_freshify", Context.MODE_PRIVATE);

        TextInputEditText itemNameEditText = requireView().findViewById(R.id.itemNameEditText);
        itemNameEditText.setText(prefs.getString("itemName", ""));

        TextInputEditText itemQuantityEditText = requireView().findViewById(R.id.itemQuantityEditText);
        itemQuantityEditText.setText(prefs.getString("itemQuantity", ""));

        AutoCompleteTextView categoryDropdownMenu = requireView().findViewById(R.id.categoryDropdownMenu);
        categoryDropdownMenu.setText(prefs.getString("category", ""));

        TextInputEditText expiryDateEditText = requireView().findViewById(R.id.expiryDateEditText);
        expiryDateEditText.setText(prefs.getString("expiryDate", ""));

        TextInputEditText commentEditText = requireView().findViewById(R.id.commentEditText);
        commentEditText.setText(prefs.getString("comment", ""));
    }

    private void loadExpiringItems() {
        FreshifyDBHelper dbHelper = new FreshifyDBHelper(requireContext());
        FreshifyRepository repository = new FreshifyRepositoryImpl(dbHelper);
        List<ItemEntity> expiringItems = expiringItemsAdapter.getItems();

        // get expiring items
        GetExpiringItemsRunnable runnable = new GetExpiringItemsRunnable(
                repository,
                expiringItemsListView,
                expiringItemsAdapter,
                expiringItems,
                daysUntilExpiry,
                noExpiringFoodText
                );
        new Thread(runnable).start();
    }

    private void loadExpiredItems() {
        FreshifyDBHelper dbHelper = new FreshifyDBHelper(requireContext());
        FreshifyRepository repository = new FreshifyRepositoryImpl(dbHelper);
        List<ItemEntity> expiredItems = expiredItemsAdapter.getItems();

        // get expiring items
        GetExpiredItemsRunnable runnable = new GetExpiredItemsRunnable(
                repository,
                expiredItemsListView,
                expiredItemsAdapter,
                expiredItems,
                noExpiredFoodText);
        new Thread(runnable).start();
    }

    private void initSaveItemButton(View view) {
        MaterialButton saveItemButton = view.findViewById(R.id.saveItemButton);

        saveItemButton.setOnClickListener(v -> {
            // get input fields
            TextInputEditText itemNameEditText = view.findViewById(R.id.itemNameEditText);
            TextInputEditText itemQuantityEditText = view.findViewById(R.id.itemQuantityEditText);
            AutoCompleteTextView categoryDropdownMenu = view.findViewById(R.id.categoryDropdownMenu);
            TextInputEditText expiryDateEditText = view.findViewById(R.id.expiryDateEditText);
            TextInputEditText commentEditText = view.findViewById(R.id.commentEditText);

            // validate inputs
            String itemName = Objects.requireNonNull(itemNameEditText.getText()).toString().trim();
            String itemQuantityStr = Objects.requireNonNull(itemQuantityEditText.getText()).toString().trim();
            String expiryDateStr = Objects.requireNonNull(expiryDateEditText.getText()).toString().trim();
            String category = Objects.requireNonNull(categoryDropdownMenu.getText()).toString().trim();
            String comment = commentEditText.getText() != null ? commentEditText.getText().toString().trim() : ""; // optional

            // show toast if required fields are empty
            if (itemName.isEmpty() || itemQuantityStr.isEmpty() || expiryDateStr.isEmpty() || category.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.please_fill_in_all_required_fields), Toast.LENGTH_SHORT).show();
                return;
            }

            // parse quantity
            int itemQuantity;
            try {
                itemQuantity = Integer.parseInt(itemQuantityStr);
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), getString(R.string.invalid_quantity), Toast.LENGTH_SHORT).show();
                return;
            }

            // parse expiry date
            LocalDate expiryDate;
            try {
                String[] dateParts = expiryDateStr.split("/");
                int day = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);
                expiryDate = LocalDate.of(year, month, day);
            } catch (Exception e) {
                Toast.makeText(requireContext(), getString(R.string.invalid_expiry_date_format), Toast.LENGTH_SHORT).show();
                return;
            }

            // create new item and store it in the database
            CategoryDatabase categoryDatabase = new CategoryDatabaseImpl(requireContext());
            ItemEntity newItem = new ItemEntity(
                    0, // will be generated by the database
                    itemName,
                    itemQuantity,
                    categoryDatabase.getIdByCategory(category), // category id based on category name
                    category,
                    expiryDate,
                    comment
            );

            FreshifyDBHelper dbHelper = new FreshifyDBHelper(requireContext());
            FreshifyRepository repository = new FreshifyRepositoryImpl(dbHelper);

            long newRowId = repository.insertItem(newItem);
            if (newRowId != -1) {
                Toast.makeText(requireContext(), getString(R.string.item_saved_successfully), Toast.LENGTH_SHORT).show();
                clearInputFields(view); // clear input fields
            } else {
                Toast.makeText(requireContext(), getString(R.string.failed_to_save_item), Toast.LENGTH_SHORT).show();
            }

            loadExpiringItems();
            loadExpiredItems();
        });
    }

    private void initCategoryDropDown(@NonNull View view) {
        AutoCompleteTextView dropdownMenu = view.findViewById(R.id.categoryDropdownMenu);

        // data source for categories
        CategoryDatabase categoryDatabase = new CategoryDatabaseImpl(requireContext());
        String[] items = categoryDatabase.getCategories().toArray(new String[0]);

        // create adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.category_dropdown_item,
                R.id.categoryDropdownTextView,
                items
        );

        dropdownMenu.setAdapter(adapter);
    }

    private void initExpiryDateField(View view) {
        TextInputEditText expiryDateEditText = view.findViewById(R.id.expiryDateEditText);

        // click listener to show calendar
        expiryDateEditText.setOnClickListener(v -> {
            // current date
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // create date picker dialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        // format date
                        String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        expiryDateEditText.setText(formattedDate);
                    },
                    year, month, day
            );

            // show date picker
            datePickerDialog.show();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void clearInputFields(View view) {
        ((TextInputEditText) view.findViewById(R.id.itemNameEditText)).setText("");
        ((TextInputEditText) view.findViewById(R.id.itemQuantityEditText)).setText("");
        ((TextInputEditText) view.findViewById(R.id.expiryDateEditText)).setText("");
        ((AutoCompleteTextView) view.findViewById(R.id.categoryDropdownMenu)).setText("");
        ((TextInputEditText) view.findViewById(R.id.commentEditText)).setText("");
    }

    /** OnClick Listener for web search button */
    public void initWebSearchButton(View view) {
        ImageButton webSearchButton = view.findViewById(R.id.webSearchIcon);
        webSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText itemNameEditText = view.findViewById(R.id.itemNameEditText);
                String itemName = itemNameEditText.getText().toString().trim();

                if (itemName.isEmpty()) {
                    Toast.makeText(requireContext(), getString(R.string.please_enter_an_item_name), Toast.LENGTH_SHORT).show();
                } else {
                    startWebSearch(requireContext(), itemName);
                }
            }
        });
    }

    /** start web search for item */
    private void startWebSearch(Context context, String itemName) {
        String query = getString(R.string.how_long_does) + itemName + getString(R.string.last);
        String url = GOOGLE_SEARCH_URL + Uri.encode(query);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }
}