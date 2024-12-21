package de.alxmtzr.freshify.data.concurrency;

import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.alxmtzr.freshify.adapter.ItemsAdapter;
import de.alxmtzr.freshify.data.local.FreshifyRepository;
import de.alxmtzr.freshify.data.model.ItemEntity;

public class GetItemsByCategoryRunnable implements Runnable {
    private final FreshifyRepository repository;
    private final RecyclerView recyclerView;
    private final ItemsAdapter adapter;
    private final List<ItemEntity> data;
    private final List<Integer> categoryIds;

    public GetItemsByCategoryRunnable(FreshifyRepository repository,
                                      RecyclerView recyclerView,
                                      ItemsAdapter adapter,
                                      List<ItemEntity> data,
                                      List<Integer> categoryIds) {
        this.repository = repository;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.data = data;
        this.categoryIds = categoryIds;
    }

    @Override
    public void run() {
        List<ItemEntity> filteredItems = repository.getItemsByCategories(categoryIds);
        if (filteredItems != null) {
            data.clear();
            data.addAll(filteredItems);

            // post UI update
            recyclerView.post(adapter::notifyDataSetChanged);
        } else {
            // post error message
            recyclerView.post(() -> {
                Toast.makeText(recyclerView.getContext(), "Error filtering items.", Toast.LENGTH_SHORT).show();
            });
        }
    }
}

