package de.alxmtzr.freshify.data.concurrency;

import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.alxmtzr.freshify.adapter.ItemsAdapter;
import de.alxmtzr.freshify.data.local.FreshifyRepository;
import de.alxmtzr.freshify.data.model.ItemEntity;

public class GetExpiredItemsRunnable implements Runnable {
    private final FreshifyRepository repository;
    private final RecyclerView recyclerView;
    private final ItemsAdapter adapter;
    private final List<ItemEntity> data;

    public GetExpiredItemsRunnable(FreshifyRepository repository,
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
        List<ItemEntity> expiredItems = repository.getExpiredItems();
        if (expiredItems != null) {
            data.clear();
            data.addAll(expiredItems);

            recyclerView.post(adapter::notifyDataSetChanged);
        } else {
            recyclerView.post(() -> Toast.makeText(recyclerView.getContext(), "Error fetching expired items.", Toast.LENGTH_SHORT).show());
        }
    }
}

