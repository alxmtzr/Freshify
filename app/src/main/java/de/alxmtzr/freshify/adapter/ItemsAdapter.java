package de.alxmtzr.freshify.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import de.alxmtzr.freshify.R;
import de.alxmtzr.freshify.data.local.FreshifyRepository;
import de.alxmtzr.freshify.data.local.impl.FreshifyDBHelper;
import de.alxmtzr.freshify.data.local.impl.FreshifyRepositoryImpl;
import de.alxmtzr.freshify.data.model.ItemEntity;

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
        holder.deleteItemIcon.setOnClickListener(v -> {
            FreshifyDBHelper dbHelper = new FreshifyDBHelper(context);
            FreshifyRepository repository = new FreshifyRepositoryImpl(dbHelper);

            long deletedRows = repository.deleteItem(item.getId());
            if (deletedRows > 0) {
                items.remove(position); // remove item from the list
                notifyDataSetChanged(); // notify the adapter
                Toast.makeText(context, R.string.item_deleted, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, R.string.failed_to_delete_item, Toast.LENGTH_SHORT).show();
            }
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
        ImageView deleteItemIcon;

        ViewHolder(View view) {
            // Initialize the views
            itemName = view.findViewById(R.id.itemName);
            itemQuantity = view.findViewById(R.id.itemQuantity);
            itemCategory = view.findViewById(R.id.itemCategory);
            itemExpiryDate = view.findViewById(R.id.itemExpiryDate);
            deleteItemIcon = view.findViewById(R.id.deleteItemIcon);
        }
    }

    public List<ItemEntity> getItems() {
        return items;
    }
}
