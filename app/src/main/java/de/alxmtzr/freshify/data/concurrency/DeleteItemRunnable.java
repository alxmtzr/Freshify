package de.alxmtzr.freshify.data.concurrency;

import android.view.View;
import android.widget.Toast;

import de.alxmtzr.freshify.data.local.FreshifyRepository;

public class DeleteItemRunnable implements Runnable {
    private final FreshifyRepository repository;
    private final long itemId;
    private final View view; // To access the context for UI updates
    private final Runnable onDeleteSuccess;

    public DeleteItemRunnable(
            FreshifyRepository repository,
            long itemId, View view,
            Runnable onDeleteSuccess) {
        this.repository = repository;
        this.itemId = itemId;
        this.view = view;
        this.onDeleteSuccess = onDeleteSuccess;
    }

    @Override
    public void run() {
        long deletedRows = repository.deleteItem(itemId);
        view.post(() -> {
            if (deletedRows > 0) {
                Toast.makeText(view.getContext(), "Item deleted successfully!", Toast.LENGTH_SHORT).show();
                onDeleteSuccess.run(); // Execute success callback (e.g., finish activity)
            } else {
                Toast.makeText(view.getContext(), "Failed to delete item.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
