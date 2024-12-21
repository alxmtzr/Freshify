package de.alxmtzr.freshify.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import de.alxmtzr.freshify.R;
import de.alxmtzr.freshify.adapter.ItemsAdapter;
import de.alxmtzr.freshify.data.concurrency.GetAllItemsRunnable;
import de.alxmtzr.freshify.data.concurrency.GetItemsByCategoriesRunnable;
import de.alxmtzr.freshify.data.concurrency.GetItemsByNameRunnable;
import de.alxmtzr.freshify.data.local.CategoryDatabase;
import de.alxmtzr.freshify.data.local.FreshifyRepository;
import de.alxmtzr.freshify.data.local.impl.CategoryDatabaseImpl;
import de.alxmtzr.freshify.data.local.impl.FreshifyDBHelper;
import de.alxmtzr.freshify.data.local.impl.FreshifyRepositoryImpl;
import de.alxmtzr.freshify.data.model.ItemEntity;

public class FridgeFragment extends Fragment {

    private FreshifyRepository repository;
    private ItemsAdapter itemsAdapter;
    private RecyclerView itemsRecyclerView;
    private final List<Integer> selectedCategoryIds = new ArrayList<>();

    public FridgeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fridge, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialize database
        FreshifyDBHelper dbHelper = new FreshifyDBHelper(getContext());
        repository = new FreshifyRepositoryImpl(dbHelper);

        // initialize RecyclerView
        itemsRecyclerView = view.findViewById(R.id.itemsRecyclerView);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        // Adapter
        itemsAdapter = new ItemsAdapter(new ArrayList<>());
        itemsRecyclerView.setAdapter(itemsAdapter);

        initSearchView(view);
        initCategoryChips(view);

        // load all items at start
        resetFilter();
    }

    private void initSearchView(View view) {
        SearchView searchView = view.findViewById(R.id.searchItemView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // filter items by search text
                if (newText.isEmpty()) {
                    resetFilter(); // show all items
                } else {
                    searchItems(newText); // search based on item name
                }
                return true;
            }
        });
    }

    private void searchItems(String query) {
        // search items by name
        List<ItemEntity> searchedItems = itemsAdapter.getItems();
        GetItemsByNameRunnable runnable = new GetItemsByNameRunnable(
                repository,
                itemsRecyclerView,
                itemsAdapter,
                searchedItems,
                query
        );
        new Thread(runnable).start();
    }


    private void initCategoryChips(View view) {
        ChipGroup catgegoryChipGroup = view.findViewById(R.id.categoryChipGroup);

        // get categories from database
        CategoryDatabase categoryDatabase = new CategoryDatabaseImpl(requireContext());
        List<String> categories = categoryDatabase.getCategories();

        for (String category : categories) {
            Chip chip = new Chip(requireContext());
            chip.setText(category);
            chip.setCheckable(true);

            // event for check status of chip
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int categoryId = categoryDatabase.getIdByCategory(category);
                // add or remove category id from selectedCategoryIds
                if (isChecked) {
                    selectedCategoryIds.add(categoryId);
                } else {
                    if (selectedCategoryIds.contains(categoryId)) {
                        selectedCategoryIds.remove(Integer.valueOf(categoryId));
                    }
                }

                // filter items by selected categories
                filterItemsByCategory();
            });

            catgegoryChipGroup.addView(chip);
        }
    }

    private void filterItemsByCategory() {
        if (selectedCategoryIds.isEmpty()) {
            // no filter -> show all items
            resetFilter();
        } else {
            // show items with selected categories
            List<ItemEntity> filteredItems = itemsAdapter.getItems();
            GetItemsByCategoriesRunnable runnable = new GetItemsByCategoriesRunnable(
                    repository,
                    itemsRecyclerView,
                    itemsAdapter,
                    filteredItems,
                    selectedCategoryIds
            );
            new Thread(runnable).start();
        }
    }

    private void resetFilter() {
        // show all items
        List<ItemEntity> allItems = itemsAdapter.getItems();

        GetAllItemsRunnable runnable = new GetAllItemsRunnable(
                repository,
                itemsRecyclerView,
                itemsAdapter,
                allItems
        );
        new Thread(runnable).start();

        // clear selected categories (filter)
        selectedCategoryIds.clear();

        Log.i("FridgeFragment", "Filter reset");
    }
}