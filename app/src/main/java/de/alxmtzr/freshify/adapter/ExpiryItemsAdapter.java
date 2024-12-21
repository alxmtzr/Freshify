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

public class ExpiryItemsAdapter extends BaseAdapter {

    private final List<ItemEntity> items;
    private final Context context;

    public ExpiryItemsAdapter(Context context, List<ItemEntity> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ExpiryItemsAdapter.ViewHolder holder;

        if (convertView == null) {
            // Inflate the row layout
            convertView = LayoutInflater.from(context).inflate(R.layout.card_item_list_row, parent, false);
            holder = new ExpiryItemsAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ExpiryItemsAdapter.ViewHolder) convertView.getTag();
        }

        // Get the current item
        ItemEntity item = items.get(position);

        // Set the item's data to the view
        holder.itemName.setText(item.getName());

        // calculate days between now and expiry date
        long daysUntilExpiry = item.getExpiryDate().toEpochDay() - java.time.LocalDate.now().toEpochDay();

        // set the expiry date text based on the days until expiry
        if (daysUntilExpiry == 1) {
            holder.expiryItemText.setText(
                    holder.itemName.getContext().getString(R.string.expires_in_one_day, daysUntilExpiry)
            );
        } else if (daysUntilExpiry > 1) {
            holder.expiryItemText.setText(
                    holder.itemName.getContext().getString(R.string.expires_in_multiple_days, daysUntilExpiry)
            );
        } else if (daysUntilExpiry == -1) {
            holder.expiryItemText.setText(
                    holder.itemName.getContext().getString(R.string.expired_since_one_day, -daysUntilExpiry)
            );
        } else {
            holder.expiryItemText.setText(
                    holder.itemName.getContext().getString(R.string.expired_since_days, -daysUntilExpiry)
            );
        }



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
        TextView expiryItemText;
        ImageView navigateToDetailsButton;

        ViewHolder(View view) {
            // Initialize the views
            itemName = view.findViewById(R.id.expiryItemName);
            expiryItemText = view.findViewById(R.id.expiryItemExpiryDate);
            navigateToDetailsButton = view.findViewById(R.id.navigateToDetailsButton);
        }
    }

    public List<ItemEntity> getItems() {
        return items;
    }
}
