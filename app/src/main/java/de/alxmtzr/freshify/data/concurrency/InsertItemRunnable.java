package de.alxmtzr.freshify.data.concurrency;

import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.alxmtzr.freshify.adapter.ItemsAdapter;
import de.alxmtzr.freshify.data.local.FreshifyRepository;
import de.alxmtzr.freshify.data.model.ItemEntity;

public class InsertItemRunnable implements Runnable {
    private final FreshifyRepository repository;
    private final RecyclerView recyclerView;
    private final ItemsAdapter adapter;
    private final List<ItemEntity> data;
    private final ItemEntity newItem;

    public InsertItemRunnable(FreshifyRepository repository,
                              RecyclerView recyclerView,
                              ItemsAdapter adapter,
                              List<ItemEntity> data,
                              ItemEntity newItem) {
        this.repository = repository;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.data = data;
        this.newItem = newItem;
    }

    @Override
    public void run() {
        long id = repository.insertItem(newItem);
        if (id != -1) {
            newItem.setId(id);
            data.add(newItem);

            // post UI update
            recyclerView.post(() -> adapter.notifyItemInserted(data.size() - 1));
        } else {
            // post error message
            recyclerView.post(() -> {
                Toast.makeText(recyclerView.getContext(), "Error inserting new item.", Toast.LENGTH_SHORT).show();
            });
        }
    }

}


