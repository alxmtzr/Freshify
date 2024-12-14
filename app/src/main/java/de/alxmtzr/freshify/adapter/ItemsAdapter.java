package de.alxmtzr.freshify.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.alxmtzr.freshify.R;
import de.alxmtzr.freshify.data.local.FreshifyRepository;
import de.alxmtzr.freshify.data.local.impl.FreshifyDBHelper;
import de.alxmtzr.freshify.data.local.impl.FreshifyRepositoryImpl;
import de.alxmtzr.freshify.data.model.ItemEntity;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<ItemEntity> items;

    public ItemsAdapter(List<ItemEntity> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_row, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemEntity item = items.get(position);

        // Set the item's data to the view
        holder.itemName.setText(item.getName());
        holder.itemQuantity.setText(String.valueOf(item.getQuantity()));
        holder.itemCategory.setText(item.getCategoryName());
        holder.itemExpiryDate.setText(item.getExpiryDate().toString());

        // Set the delete button's click listener
        holder.deleteItemIcon.setOnClickListener(v -> {
            FreshifyDBHelper dbHelper = new FreshifyDBHelper(v.getContext());
            FreshifyRepository repository = new FreshifyRepositoryImpl(dbHelper);

            long deletedRows = repository.deleteItem(item.getId());
            if (deletedRows > 0) {
                items.remove(position); // remove item from the list
                notifyItemRemoved(position); // notify the adapter
                notifyItemRangeChanged(position, items.size()); // refresh list
                Toast.makeText(holder.itemView.getContext(), R.string.item_deleted, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(holder.itemView.getContext(), R.string.failed_to_delete_item, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(List<ItemEntity> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemQuantity, itemCategory, itemExpiryDate;
        ImageView deleteItemIcon;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            // text views for item information
            itemName = itemView.findViewById(R.id.itemName);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
            itemCategory = itemView.findViewById(R.id.itemCategory);
            itemExpiryDate = itemView.findViewById(R.id.itemExpiryDate);

            // delete button
            deleteItemIcon = itemView.findViewById(R.id.deleteItemIcon);
        }
    }

}

