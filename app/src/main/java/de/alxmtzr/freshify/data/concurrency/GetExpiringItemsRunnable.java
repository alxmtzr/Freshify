package de.alxmtzr.freshify.data.concurrency;

import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.alxmtzr.freshify.adapter.ItemsAdapter;
import de.alxmtzr.freshify.data.local.FreshifyRepository;
import de.alxmtzr.freshify.data.model.ItemEntity;

public class GetExpiringItemsRunnable implements Runnable {
    private final FreshifyRepository repository;
    private final RecyclerView recyclerView;
    private final ItemsAdapter adapter;
    private final List<ItemEntity> data;
    private final int daysUntilExpiry;

    public GetExpiringItemsRunnable(FreshifyRepository repository,
                                        RecyclerView recyclerView,
                                        ItemsAdapter adapter,
                                        List<ItemEntity> data,
                                        int daysUntilExpiry) {
        this.repository = repository;
        this.recyclerView = recyclerView;
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

            recyclerView.post(adapter::notifyDataSetChanged);
        } else {
            recyclerView.post(() -> Toast.makeText(recyclerView.getContext(), "Error fetching expiring items.", Toast.LENGTH_SHORT).show());
        }
    }
}

