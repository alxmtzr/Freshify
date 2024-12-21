package de.alxmtzr.freshify.data.concurrency;

import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import de.alxmtzr.freshify.adapter.ExpiringItemsAdapter;
import de.alxmtzr.freshify.data.local.FreshifyRepository;
import de.alxmtzr.freshify.data.model.ItemEntity;

public class GetExpiringItemsRunnable implements Runnable {
    private final FreshifyRepository repository;
    private final ListView listView;
    private final ExpiringItemsAdapter adapter;
    private final List<ItemEntity> data;
    private final int daysUntilExpiry;

    public GetExpiringItemsRunnable(FreshifyRepository repository,
                                        ListView listView,
                                        ExpiringItemsAdapter adapter,
                                        List<ItemEntity> data,
                                        int daysUntilExpiry) {
        this.repository = repository;
        this.listView = listView;
        this.adapter = adapter;
        this.data = data;
        this.daysUntilExpiry = daysUntilExpiry;
    }

    @Override
    public void run() {
        List<ItemEntity> expiringItems = repository.getItemsExpiringSoon(daysUntilExpiry);
        if (expiringItems != null) {
            data.clear();
            data.addAll(expiringItems);

            // log expiring items
            for (ItemEntity item : expiringItems) {
                Log.i("GetExpiringItemsRunnable", "Expiring item: " + item.getName());
            }

            listView.post(adapter::notifyDataSetChanged);
        } else {
            listView.post(() -> Toast.makeText(listView.getContext(), "Error fetching expiring items.", Toast.LENGTH_SHORT).show());
        }
    }
}

