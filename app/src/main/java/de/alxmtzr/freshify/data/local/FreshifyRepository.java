package de.alxmtzr.freshify.data.local;

import java.util.List;

import de.alxmtzr.freshify.data.model.ItemEntity;

public interface FreshifyRepository {

    /**
     * Retrieves an item by its ID.
     *
     * @param itemId the ID of the item to retrieve.
     * @return the item with the specified ID, or null if no such item exists.
     */
    long deleteItem(long itemId);

    /**
     * Inserts a new item into the database.
     *
     * @param item the item to insert.
     * @return the ID of the newly inserted item.
     */
    long insertItem(ItemEntity item);

    /**
     * Retrieves all items from the database.
     *
     * @return a list of all items.
     */
    List<ItemEntity> getAllItems();

    /**
     * Retrieves all items that belong to the specified categories.
     *
     * @param categoryIds a list of category IDs to filter by.
     * @return a list of items that match the specified categories.
     */
    List<ItemEntity> getItemsByCategories(List<Integer> categoryIds);

    /**
     * Retrieves all items that have expired.
     *
     * @return a list of items that are expired.
     */
    List<ItemEntity> getExpiredItems();

    /**
     * Retrieves all items that will expire soon.
     *
     * @param daysUntilExpiry the number of days to consider as "soon".
     * @return a list of items that will expire within the specified number of days.
     */
    List<ItemEntity> getItemsExpiringSoon(int daysUntilExpiry);

    /**
     * Retrieves all items that match the specified item name.
     *
     * @param itemName the name of the item to search for.
     * @return a list of items that match the specified name.
     */
    List<ItemEntity> getItemsByName(String itemName);
}

