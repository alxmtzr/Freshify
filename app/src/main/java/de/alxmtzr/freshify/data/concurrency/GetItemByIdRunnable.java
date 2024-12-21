package de.alxmtzr.freshify.data.concurrency;

import android.widget.TextView;
import de.alxmtzr.freshify.data.local.FreshifyRepository;
import de.alxmtzr.freshify.data.model.ItemEntity;

public class GetItemByIdRunnable implements Runnable{
    private final FreshifyRepository repository;
    private final long id;
    private final TextView itemName;
    private final TextView itemQuantity;
    private final TextView itemCategory;
    private final TextView itemExpirationDate;
    private final TextView itemComment;

    public GetItemByIdRunnable(
            FreshifyRepository repository,
            long id,
            TextView itemName,
            TextView itemQuantity,
            TextView itemCategory,
            TextView itemExpirationDate,
            TextView itemComment) {
        this.repository = repository;
        this.id = id;
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemCategory = itemCategory;
        this.itemExpirationDate = itemExpirationDate;
        this.itemComment = itemComment;
    }

    @Override
    public void run() {
        ItemEntity item = repository.getItemById(id);
        updateUI(item);
    }

    private void updateUI(ItemEntity item) {
        itemName.post(() -> {
            itemName.setText(item.getName());
            itemQuantity.setText(String.valueOf(item.getQuantity()));
            itemCategory.setText(item.getCategoryName());
            itemExpirationDate.setText(item.getExpiryDate().toString());
            itemComment.setText(item.getComment());
        });
    }
}
