package de.alxmtzr.freshify;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import de.alxmtzr.freshify.adapter.ItemsAdapter;
import de.alxmtzr.freshify.data.local.CategoryDatabase;
import de.alxmtzr.freshify.data.local.FreshifyRepository;
import de.alxmtzr.freshify.data.local.impl.CategoryDatabaseImpl;
import de.alxmtzr.freshify.data.local.impl.FreshifyDBHelper;
import de.alxmtzr.freshify.data.local.impl.FreshifyRepositoryImpl;
import de.alxmtzr.freshify.data.model.ItemEntity;

public class FridgeFragment extends Fragment {

    private FreshifyRepository repository;
    private ItemsAdapter itemsAdapter;

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
        RecyclerView itemsRecyclerView;
        super.onViewCreated(view, savedInstanceState);

        FreshifyDBHelper dbHelper = new FreshifyDBHelper(getContext());
        repository = new FreshifyRepositoryImpl(dbHelper);

        itemsRecyclerView = view.findViewById(R.id.itemsRecyclerView);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Adapter
        itemsAdapter = new ItemsAdapter(new ArrayList<>());
        itemsRecyclerView.setAdapter(itemsAdapter);

        initCategoryChips(view);

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
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // filter items by category
                if (isChecked) {
                    filterItemsByCategory(categoryDatabase, category);
                } else {
                    resetFilter();
                }
            });

            catgegoryChipGroup.addView(chip);
        }
    }

    private void filterItemsByCategory(CategoryDatabase categoryDatabase, String categoryName) {
        int categoryId = categoryDatabase.getIdByCategory(categoryName);

        // get items by category from database
        List<ItemEntity> filteredItems = repository.getItemsByCategories(List.of(categoryId));

        // Adapter aktualisieren
        itemsAdapter.updateItems(filteredItems);

        Toast.makeText(requireContext(), "Filtered by: " + categoryName, Toast.LENGTH_SHORT).show();
    }

    private void resetFilter() {
        // show all items
        List<ItemEntity> allItems = repository.getAllItems();

        // refresh adapter
        itemsAdapter.updateItems(allItems);

        Toast.makeText(requireContext(), "Filter reset", Toast.LENGTH_SHORT).show();
    }
}