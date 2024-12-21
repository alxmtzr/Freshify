package de.alxmtzr.freshify.data.concurrency;

import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.alxmtzr.freshify.adapter.ItemsAdapter;
import de.alxmtzr.freshify.data.local.FreshifyRepository;
import de.alxmtzr.freshify.data.model.ItemEntity;

public class GetAllItemsRunnable implements Runnable {
    private final FreshifyRepository repository;
    private final RecyclerView recyclerView;
    private final ItemsAdapter adapter;
    private final List<ItemEntity> data;

    public GetAllItemsRunnable(FreshifyRepository repository,
                               RecyclerView recyclerView,
                               ItemsAdapter adapter,
                               List<ItemEntity> data) {
        this.repository = repository;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.data = data;
    }

    @Override
    public void run() {
        List<ItemEntity> allItems = repository.getAllItems();
        if (allItems != null) {
            data.clear();
            data.addAll(allItems);

            // post UI update
            recyclerView.post(adapter::notifyDataSetChanged);
            Log.i("GetAllItemsRunnable", "Loaded " + allItems.size() + " items,");
        } else {
            // post error message
            recyclerView.post(() -> Toast.makeText(recyclerView.getContext(), "Error loading items.", Toast.LENGTH_SHORT).show());
            Log.i("GetAllItemsRunnable", "Error loading items");
        }
    }
}

