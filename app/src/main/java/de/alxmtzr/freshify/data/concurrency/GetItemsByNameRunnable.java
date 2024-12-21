package de.alxmtzr.freshify.data.concurrency;

import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.alxmtzr.freshify.adapter.ItemsAdapter;
import de.alxmtzr.freshify.data.local.FreshifyRepository;
import de.alxmtzr.freshify.data.model.ItemEntity;

public class GetItemsByNameRunnable implements Runnable {
    private final FreshifyRepository repository;
    private final RecyclerView recyclerView;
    private final ItemsAdapter adapter;
    private final List<ItemEntity> data;
    private final String itemName;

    public GetItemsByNameRunnable(FreshifyRepository repository,
                                  RecyclerView recyclerView,
                                  ItemsAdapter adapter,
                                  List<ItemEntity> data,
                                  String itemName) {
        this.repository = repository;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.data = data;
        this.itemName = itemName;
    }

    @Override
    public void run() {
        List<ItemEntity> items = repository.getItemsByName(itemName);
        if (items != null) {
            data.clear();
            data.addAll(items);

            recyclerView.post(adapter::notifyDataSetChanged);
            Log.i("GetItemsByNameRunnable", "Search result for: " + itemName + " - " + data.size() + " items found.");
        } else {
            recyclerView.post(() -> Toast.makeText(recyclerView.getContext(), "Error fetching items by name.", Toast.LENGTH_SHORT).show());
            Log.i("GetItemsByNameRunnable", "Error fetching items by name for: " + itemName);
        }
    }
}

