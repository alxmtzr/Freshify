package de.alxmtzr.freshify.data.concurrency;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
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
    private final CardView loadingOverlay;

    public GetAllItemsRunnable(FreshifyRepository repository,
                               RecyclerView recyclerView,
                               ItemsAdapter adapter,
                               List<ItemEntity> data,
                               CardView loadingOverlay) {
        this.repository = repository;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.data = data;
        this.loadingOverlay = loadingOverlay;
    }

    @Override
    public void run() {
        List<ItemEntity> allItems = repository.getAllItems();
        if (allItems != null) {
            data.clear();
            data.addAll(allItems);

            // post UI update
            recyclerView.post(() -> {
                adapter.notifyDataSetChanged();
                loadingOverlay.setVisibility(View.GONE);
            });
            Log.i("GetAllItemsRunnable", "Loaded " + allItems.size() + " items,");
        } else {
            // post error message
            recyclerView.post(() -> {
                Toast.makeText(recyclerView.getContext(), "Error loading items.", Toast.LENGTH_SHORT).show();
                loadingOverlay.setVisibility(View.GONE);
            });
            Log.i("GetAllItemsRunnable", "Error loading items");
        }
    }
}

