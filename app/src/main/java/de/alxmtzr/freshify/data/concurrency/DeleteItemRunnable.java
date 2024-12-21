package de.alxmtzr.freshify.data.concurrency;

import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import de.alxmtzr.freshify.adapter.ItemsAdapter;
import de.alxmtzr.freshify.data.local.FreshifyRepository;
import de.alxmtzr.freshify.data.model.ItemEntity;

public class DeleteItemRunnable implements Runnable {
    private final FreshifyRepository repository;
    private final ListView listView;
    private final ItemsAdapter adapter;
    private final List<ItemEntity> data;
    private final int position;

    public DeleteItemRunnable(FreshifyRepository repository,
                              ListView listView,
                              ItemsAdapter adapter,
                              List<ItemEntity> data,
                              int position) {
        this.repository = repository;
        this.listView = listView;
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
            listView.post(adapter::notifyDataSetChanged);
        } else {
            // post error message
            listView.post(() -> Toast.makeText(listView.getContext(), "Error deleting item.", Toast.LENGTH_SHORT).show());
        }
    }
}

