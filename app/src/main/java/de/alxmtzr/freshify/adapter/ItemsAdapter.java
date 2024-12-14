package de.alxmtzr.freshify.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.alxmtzr.freshify.R;
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

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
            itemCategory = itemView.findViewById(R.id.itemCategory);
            itemExpiryDate = itemView.findViewById(R.id.itemExpiryDate);
        }
    }
}

