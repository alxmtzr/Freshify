package de.alxmtzr.freshify.data.concurrency;

import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.alxmtzr.freshify.adapter.ItemsAdapter;
import de.alxmtzr.freshify.data.local.FreshifyRepository;
import de.alxmtzr.freshify.data.model.ItemEntity;

public class DeleteItemRunnable implements Runnable {
    private final FreshifyRepository repository;
    private final RecyclerView recyclerView;
    private final ItemsAdapter adapter;
    private final List<ItemEntity> data;
    private final int position;

    public DeleteItemRunnable(FreshifyRepository repository,
                              RecyclerView recyclerView,
                              ItemsAdapter adapter,
                              List<ItemEntity> data,
                              int position) {
        this.repository = repository;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.data = data;
        this.position = position;
    }

    @Override
    public void run() {
        ItemEntity itemToDelete = data.get(position);
        long deletedRows = repository.deleteItem(itemToDelete.getId());
        if (deletedRows > 0) {
            data.remove(position);

            // post UI update
            recyclerView.post(() -> adapter.notifyItemRemoved(position));
        } else {
            // post error message
            recyclerView.post(() -> {
                Toast.makeText(recyclerView.getContext(), "Error deleting item.", Toast.LENGTH_SHORT).show();
            });
        }
    }
}

