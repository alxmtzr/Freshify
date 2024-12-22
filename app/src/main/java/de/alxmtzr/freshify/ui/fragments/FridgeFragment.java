package de.alxmtzr.freshify.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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
import de.alxmtzr.freshify.ui.ItemDetailsActivity;

public class FridgeFragment extends Fragment {

    private FreshifyRepository repository;
    private final List<Integer> selectedCategoryIds = new ArrayList<>();

    // ui components
    private ItemsAdapter itemsAdapter;
    private ListView itemsListView;
    private CardView loadingOverlay;

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

        // initialize ui components
        loadingOverlay = view.findViewById(R.id.loadingOverlayFridgeFragment);
        // initialize RecyclerView
        itemsListView = view.findViewById(R.id.itemsListView);
        // Adapter
        itemsAdapter = new ItemsAdapter(requireContext(), new ArrayList<>());
        itemsListView.setAdapter(itemsAdapter);
        Log.i("FridgeFragment", "Adapter set. Current items count: " + itemsAdapter.getCount());
        setItemClickListener();

        initSearchView(view);
        initCategoryChips(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        resetFilter();
    }

    private void setItemClickListener() {
        itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // get the clicked item
                ItemEntity clickedItem = (ItemEntity) adapterView.getItemAtPosition(position);

                // open details activity
                Intent intent = new Intent(requireContext(), ItemDetailsActivity.class);
                intent.putExtra("itemId", clickedItem.getId());
                startActivity(intent);
            }
        });
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
                itemsListView,
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
                    itemsListView,
                    itemsAdapter,
                    filteredItems,
                    selectedCategoryIds
            );
            new Thread(runnable).start();
        }
    }

    private void resetFilter() {
        showLoadingOverlay();

        // get all items
        List<ItemEntity> allItems = itemsAdapter.getItems();
        GetAllItemsRunnable runnable = new GetAllItemsRunnable(
                repository,
                itemsListView,
                itemsAdapter,
                allItems,
                loadingOverlay
        );

        new Thread(runnable).start();

        selectedCategoryIds.clear();
        Log.i("FridgeFragment", "Filter reset");
    }

    private void showLoadingOverlay() {
        if (loadingOverlay != null) {
            loadingOverlay.setVisibility(View.VISIBLE);
        }
    }
}