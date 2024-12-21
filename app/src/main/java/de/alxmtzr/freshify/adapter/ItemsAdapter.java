package de.alxmtzr.freshify.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.alxmtzr.freshify.R;
import de.alxmtzr.freshify.data.model.ItemEntity;
import de.alxmtzr.freshify.ui.ItemDetailsActivity;

public class ItemsAdapter extends BaseAdapter {

    private final List<ItemEntity> items;
    private final Context context;

    public ItemsAdapter(Context context, List<ItemEntity> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            // Inflate the row layout
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_row, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the current item
        ItemEntity item = items.get(position);

        // Set the item's data to the view
        holder.itemName.setText(item.getName());
        holder.itemQuantity.setText(String.valueOf(item.getQuantity()));
        holder.itemCategory.setText(item.getCategoryName());
        holder.itemExpiryDate.setText(item.getExpiryDate().toString());

        // Set the delete button's click listener
        holder.navigateToDetailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(parent.getContext(), ItemDetailsActivity.class);
            intent.putExtra("itemId", item.getId());
            parent.getContext().startActivity(intent);
        });

        return convertView;
    }

    public void updateItems(List<ItemEntity> newItems) {
        this.items.clear();
        this.items.addAll(newItems);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView itemName;
        TextView itemQuantity;
        TextView itemCategory;
        TextView itemExpiryDate;
        ImageView navigateToDetailsButton;

        ViewHolder(View view) {
            // Initialize the views
            itemName = view.findViewById(R.id.itemName);
            itemQuantity = view.findViewById(R.id.itemQuantity);
            itemCategory = view.findViewById(R.id.itemCategory);
            itemExpiryDate = view.findViewById(R.id.itemExpiryDate);
            navigateToDetailsButton = view.findViewById(R.id.navigateToDetailsButton);
        }
    }

    public List<ItemEntity> getItems() {
        return items;
    }
}
