package de.alxmtzr.freshify.data.local.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import de.alxmtzr.freshify.data.local.FreshifyRepository;
import de.alxmtzr.freshify.data.model.ItemEntity;

public class FreshifyRepositoryImpl implements FreshifyRepository {

    private final FreshifyDBHelper dbHelper;

    public FreshifyRepositoryImpl(FreshifyDBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public long deleteItem(long itemId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long deletedRows = db.delete(
                FreshifyDBHelper.FRESHIFY_TABLE,
                BaseColumns._ID + " = ?",
                new String[]{String.valueOf(itemId)}
        );

        db.close();
        return deletedRows;
    }

    @Override
    public long insertItem(ItemEntity item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FreshifyDBHelper.FRESHIFY_COL_ITEM_NAME, item.getName());
        values.put(FreshifyDBHelper.FRESHIFY_COL_ITEM_QUANTITY, item.getQuantity());
        values.put(FreshifyDBHelper.FRESHIFY_COL_ITEM_CATEGORY_ID, item.getCategoryId());
        values.put(FreshifyDBHelper.FRESHIFY_COL_ITEM_CATEGORY_NAME, item.getCategoryName());
        values.put(FreshifyDBHelper.FRESHIFY_COL_ITEM_EXPIRY_DATE, item.getExpiryDate().toString()); // convert LocalDate to String
        values.put(FreshifyDBHelper.FRESHIFY_COL_COMMENT, item.getComment());

        long newRowId = db.insert(FreshifyDBHelper.FRESHIFY_TABLE, null, values);

        db.close();
        return newRowId; // -1 if an error occurred
    }

    @Override
    public List<ItemEntity> getAllItems() {
        List<ItemEntity> items = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                FreshifyDBHelper.FRESHIFY_TABLE,
                null,
                null,
                null,
                null,
                null,
                FreshifyDBHelper.FRESHIFY_COL_ITEM_NAME + " ASC"  // sort by name
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                items.add(mapCursorToItemEntity(cursor));
            }
            cursor.close();
        }
        return items;
    }

    @Override
    public List<ItemEntity> getItemsByCategories(List<Integer> categoryIds) {
        List<ItemEntity> items = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // dynamic WHERE clause based on category IDs
        StringBuilder whereClause = new StringBuilder();
        List<String> args = new ArrayList<>();
        for (int i = 0; i < categoryIds.size(); i++) {
            if (i > 0) whereClause.append(" OR ");
            whereClause.append(FreshifyDBHelper.FRESHIFY_COL_ITEM_CATEGORY_ID).append(" = ?");
            args.add(String.valueOf(categoryIds.get(i)));
        }

        Cursor cursor = db.query(
                FreshifyDBHelper.FRESHIFY_TABLE,
                null,
                whereClause.toString(),
                args.toArray(new String[0]),
                null,
                null,
                null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                items.add(mapCursorToItemEntity(cursor));
            }
            cursor.close();
        }
        return items;
    }

    @Override
    public List<ItemEntity> getExpiredItems() {
        List<ItemEntity> items = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                FreshifyDBHelper.FRESHIFY_TABLE,
                null,
                FreshifyDBHelper.FRESHIFY_COL_ITEM_EXPIRY_DATE + " < date('now')",
                null,
                null,
                null,
                null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                items.add(mapCursorToItemEntity(cursor));
            }
            cursor.close();
        }
        return items;
    }

    @Override
    public List<ItemEntity> getItemsExpiringSoon(int daysUntilExpiry) {
        List<ItemEntity> items = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                FreshifyDBHelper.FRESHIFY_TABLE,
                null,
                FreshifyDBHelper.FRESHIFY_COL_ITEM_EXPIRY_DATE + " BETWEEN date('now') AND date('now', ?)",
                new String[]{"+" + daysUntilExpiry + " days"},
                null,
                null,
                null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                items.add(mapCursorToItemEntity(cursor));
            }
            cursor.close();
        }
        return items;
    }

    @Override
    public List<ItemEntity> getItemsByName(String itemName) {
        List<ItemEntity> items = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                FreshifyDBHelper.FRESHIFY_TABLE,
                null,
                FreshifyDBHelper.FRESHIFY_COL_ITEM_NAME + " LIKE ?",
                new String[]{"%" + itemName + "%"},
                null,
                null,
                null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                items.add(mapCursorToItemEntity(cursor));
            }
            cursor.close();
        }
        return items;
    }

    @Override
    public ItemEntity getItemById(long itemId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ItemEntity item = null;

        Cursor cursor = db.query(
                FreshifyDBHelper.FRESHIFY_TABLE,
                null,
                BaseColumns._ID + " = ?",
                new String[]{String.valueOf(itemId)},
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                item = mapCursorToItemEntity(cursor);
            }
            cursor.close();
        }

        db.close();
        return item;
    }


    private ItemEntity mapCursorToItemEntity(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(FreshifyDBHelper.FRESHIFY_COL_ITEM_NAME));
        int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(FreshifyDBHelper.FRESHIFY_COL_ITEM_QUANTITY));
        int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(FreshifyDBHelper.FRESHIFY_COL_ITEM_CATEGORY_ID));
        String categoryName = cursor.getString(cursor.getColumnIndexOrThrow(FreshifyDBHelper.FRESHIFY_COL_ITEM_CATEGORY_NAME));
        String expiryDateString = cursor.getString(cursor.getColumnIndexOrThrow(FreshifyDBHelper.FRESHIFY_COL_ITEM_EXPIRY_DATE));
        LocalDate expiryDate = LocalDate.parse(expiryDateString);  // parse String to LocalDate
        String comment = cursor.getString(cursor.getColumnIndexOrThrow(FreshifyDBHelper.FRESHIFY_COL_COMMENT));

        return new ItemEntity(id, name, quantity, categoryId, categoryName, expiryDate, comment);
    }
}
