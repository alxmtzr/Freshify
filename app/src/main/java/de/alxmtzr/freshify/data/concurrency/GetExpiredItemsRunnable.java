package de.alxmtzr.freshify.data.concurrency;

import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import de.alxmtzr.freshify.adapter.ExpiryItemsAdapter;
import de.alxmtzr.freshify.data.local.FreshifyRepository;
import de.alxmtzr.freshify.data.model.ItemEntity;

public class GetExpiredItemsRunnable implements Runnable {
    private final FreshifyRepository repository;
    private final ListView listView;
    private final ExpiryItemsAdapter adapter;
    private final List<ItemEntity> data;

    public GetExpiredItemsRunnable(FreshifyRepository repository,
                                   ListView listView,
                                   ExpiryItemsAdapter adapter,
                                   List<ItemEntity> data) {
        this.repository = repository;
        this.listView = listView;
        this.adapter = adapter;
        this.data = data;
    }

    @Override
    public void run() {
        List<ItemEntity> expiredItems = repository.getExpiredItems();
        if (expiredItems != null) {
            data.clear();
            data.addAll(expiredItems);

            // log expiring items
            for (ItemEntity item : expiredItems) {
                Log.i("GetExpiredItemsRunnable", "Expired item: " + item.getName());
            }

            listView.post(adapter::notifyDataSetChanged);
        } else {
            listView.post(() -> Toast.makeText(listView.getContext(), "Error fetching expired items.", Toast.LENGTH_SHORT).show());
        }
    }
}

