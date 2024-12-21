package de.alxmtzr.freshify.data.concurrency;

import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import de.alxmtzr.freshify.adapter.ItemsAdapter;
import de.alxmtzr.freshify.data.local.FreshifyRepository;
import de.alxmtzr.freshify.data.model.ItemEntity;

public class GetItemsByNameRunnable implements Runnable {
    private final FreshifyRepository repository;
    private final ListView listView;
    private final ItemsAdapter adapter;
    private final List<ItemEntity> data;
    private final String itemName;

    public GetItemsByNameRunnable(FreshifyRepository repository,
                                  ListView listView,
                                  ItemsAdapter adapter,
                                  List<ItemEntity> data,
                                  String itemName) {
        this.repository = repository;
        this.listView = listView;
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

            listView.post(adapter::notifyDataSetChanged);
            Log.i("GetItemsByNameRunnable", "Search result for: " + itemName + " - " + data.size() + " items found.");
        } else {
            listView.post(() -> Toast.makeText(listView.getContext(), "Error fetching items by name.", Toast.LENGTH_SHORT).show());
            Log.i("GetItemsByNameRunnable", "Error fetching items by name for: " + itemName);
        }
    }
}

