package de.alxmtzr.freshify.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;

import de.alxmtzr.freshify.R;
import de.alxmtzr.freshify.data.model.ItemEntity;
import de.alxmtzr.freshify.ui.ItemDetailsActivity;

public class ExpiryItemsAdapter extends BaseAdapter {

    public static final int TYPE_EXPIRING = 0;
    public static final int TYPE_EXPIRED = 1;

    private final List<ItemEntity> items;
    private final Context context;
    private final int adapterType;

    public ExpiryItemsAdapter(Context context, List<ItemEntity> items, int adapterType) {
        this.context = context;
        this.items = items;
        this.adapterType = adapterType;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.card_item_list_row, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the current item
        ItemEntity item = items.get(position);

        // Set the item's data to the view
        holder.itemName.setText(item.getName());

        // Calculate days between now and expiry date
        long daysUntilExpiry = item.getExpiryDate().toEpochDay() - java.time.LocalDate.now().toEpochDay();

        // Set the expiry date text based on the days until expiry
        if (daysUntilExpiry == 0) {
            holder.expiryItemText.setText(context.getString(R.string.expires_today));
        } else
        if (daysUntilExpiry == 1) {
            holder.expiryItemText.setText(
                    context.getString(R.string.expires_in_one_day, daysUntilExpiry)
            );
        } else if (daysUntilExpiry > 1) {
            holder.expiryItemText.setText(
                    context.getString(R.string.expires_in_multiple_days, daysUntilExpiry)
            );
        } else if (daysUntilExpiry == -1) {
            holder.expiryItemText.setText(
                    context.getString(R.string.expired_since_one_day, -daysUntilExpiry)
            );
        } else {
            holder.expiryItemText.setText(
                    context.getString(R.string.expired_since_days, -daysUntilExpiry)
            );
        }

        // Change colors based on the adapter type
        if (adapterType == TYPE_EXPIRING) {
            holder.itemName.setTextColor(ContextCompat.getColor(context, R.color.md_theme_onTertiaryContainer));
            holder.expiryItemText.setTextColor(ContextCompat.getColor(context, R.color.md_theme_onTertiaryContainer));
        } else if (adapterType == TYPE_EXPIRED) {
            holder.itemName.setTextColor(ContextCompat.getColor(context, R.color.md_theme_onErrorContainer));
            holder.expiryItemText.setTextColor(ContextCompat.getColor(context, R.color.md_theme_onErrorContainer));
        }

        // Set the details button's click listener
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
