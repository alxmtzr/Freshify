package de.alxmtzr.freshify.data.concurrency;

import static android.widget.ListView.*;

import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import de.alxmtzr.freshify.adapter.ExpiryItemsAdapter;
import de.alxmtzr.freshify.data.local.FreshifyRepository;
import de.alxmtzr.freshify.data.model.ItemEntity;

public class GetExpiringItemsRunnable implements Runnable {
    private final FreshifyRepository repository;
    private final ListView listView;
    private final ExpiryItemsAdapter adapter;
    private final List<ItemEntity> data;
    private final int daysUntilExpiry;
    private final TextView noExpiringItemsTextView;

    public GetExpiringItemsRunnable(FreshifyRepository repository,
                                        ListView listView,
                                        ExpiryItemsAdapter adapter,
                                        List<ItemEntity> data,
                                        int daysUntilExpiry,
                                        TextView noExpiringItemsTextView) {
        this.repository = repository;
        this.listView = listView;
        this.adapter = adapter;
        this.data = data;
        this.daysUntilExpiry = daysUntilExpiry;
        this.noExpiringItemsTextView = noExpiringItemsTextView;
    }

    @Override
    public void run() {
        List<ItemEntity> expiringItems;
        synchronized (repository) {
            expiringItems = repository.getItemsExpiringSoon(daysUntilExpiry);
        }

        if (expiringItems != null) {
            synchronized (data) {
                data.clear();
                data.addAll(expiringItems);
            }

            for (ItemEntity item : expiringItems) {
                Log.i("GetExpiringItemsRunnable", "Expiring item: " + item.getName());
            }

            listView.post(() -> {
                adapter.notifyDataSetChanged();
                if (expiringItems.isEmpty()) {
                    noExpiringItemsTextView.setVisibility(VISIBLE);
                    listView.setVisibility(GONE);
                } else {
                    noExpiringItemsTextView.setVisibility(GONE);
                    listView.setVisibility(VISIBLE);
                }
            });
        } else {
            listView.post(() -> Toast.makeText(listView.getContext(), "Error fetching expiring items.", Toast.LENGTH_SHORT).show());
        }
    }

}

