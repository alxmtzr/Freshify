package de.alxmtzr.freshify.data.concurrency;

import static android.view.View.*;

import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
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
    private final TextView noExpiredFoodText;

    public GetExpiredItemsRunnable(FreshifyRepository repository,
                                   ListView listView,
                                   ExpiryItemsAdapter adapter,
                                   List<ItemEntity> data,
                                   TextView noExpiredFoodText) {
        this.repository = repository;
        this.listView = listView;
        this.adapter = adapter;
        this.data = data;
        this.noExpiredFoodText = noExpiredFoodText;
    }

    @Override
    public void run() {
        List<ItemEntity> expiredItems;
        synchronized (repository) {
            expiredItems = repository.getExpiredItems();
        }

        if (expiredItems != null) {
            synchronized (data) {
                data.clear();
                data.addAll(expiredItems);
            }
            // Log items
            for (ItemEntity item : expiredItems) {
                Log.i("GetExpiredItemsRunnable", "Expired item: " + item.getName());
            }

            listView.post(() -> {
                adapter.notifyDataSetChanged();
                if (expiredItems.isEmpty()) {
                    noExpiredFoodText.setVisibility(VISIBLE);
                    listView.setVisibility(GONE);
                } else {
                    noExpiredFoodText.setVisibility(GONE);
                    listView.setVisibility(VISIBLE);
                }
            });
        } else {
            listView.post(() -> Toast.makeText(listView.getContext(), "Error fetching expired items.", Toast.LENGTH_SHORT).show());
        }
    }

}

