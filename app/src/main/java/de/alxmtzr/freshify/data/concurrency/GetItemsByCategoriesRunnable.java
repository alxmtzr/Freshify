package de.alxmtzr.freshify.data.concurrency;

import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import de.alxmtzr.freshify.adapter.ItemsAdapter;
import de.alxmtzr.freshify.data.local.FreshifyRepository;
import de.alxmtzr.freshify.data.model.ItemEntity;

public class GetItemsByCategoriesRunnable implements Runnable {
    private final FreshifyRepository repository;
    private final ListView listView;
    private final ItemsAdapter adapter;
    private final List<ItemEntity> data;
    private final List<Integer> categoryIds;

    public GetItemsByCategoriesRunnable(FreshifyRepository repository,
                                        ListView listView,
                                        ItemsAdapter adapter,
                                        List<ItemEntity> data,
                                        List<Integer> categoryIds) {
        this.repository = repository;
        this.listView = listView;
        this.adapter = adapter;
        this.data = data;
        this.categoryIds = categoryIds;
    }

    @Override
    public void run() {
        List<ItemEntity> filteredItems;
        synchronized (repository) {
            filteredItems = repository.getItemsByCategories(categoryIds);
        }

        if (filteredItems != null) {
            synchronized (data) {
                data.clear();
                data.addAll(filteredItems);
            }

            listView.post(adapter::notifyDataSetChanged);
            Log.i("GetItemsByCategoriesRunnable", "Filtered by " + categoryIds.size() + " categories");
        } else {
            listView.post(() -> Toast.makeText(listView.getContext(), "Error filtering items.", Toast.LENGTH_SHORT).show());
            Log.i("GetItemsByCategoriesRunnable", "Error filtering items");
        }
    }

}

