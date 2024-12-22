package de.alxmtzr.freshify.data.concurrency;

import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import de.alxmtzr.freshify.adapter.ItemsAdapter;
import de.alxmtzr.freshify.data.local.FreshifyRepository;
import de.alxmtzr.freshify.data.model.ItemEntity;

public class InsertItemRunnable implements Runnable {
    private final FreshifyRepository repository;
    private final ListView listView;
    private final ItemsAdapter adapter;
    private final List<ItemEntity> data;
    private final ItemEntity newItem;

    public InsertItemRunnable(FreshifyRepository repository,
                              ListView listView,
                              ItemsAdapter adapter,
                              List<ItemEntity> data,
                              ItemEntity newItem) {
        this.repository = repository;
        this.listView = listView;
        this.adapter = adapter;
        this.data = data;
        this.newItem = newItem;
    }

    @Override
    public void run() {
        long id;
        synchronized (repository) {
            id = repository.insertItem(newItem);
        }

        if (id != -1) {
            newItem.setId(id);
            synchronized (data) {
                data.add(newItem);
            }

            listView.post(adapter::notifyDataSetChanged);
        } else {
            listView.post(() -> Toast.makeText(listView.getContext(), "Error inserting new item.", Toast.LENGTH_SHORT).show());
        }
    }
}


