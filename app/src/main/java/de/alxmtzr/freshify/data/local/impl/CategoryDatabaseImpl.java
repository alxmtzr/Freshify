package de.alxmtzr.freshify.data.local.impl;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import de.alxmtzr.freshify.R;
import de.alxmtzr.freshify.data.local.CategoryDatabase;
import de.alxmtzr.freshify.data.model.CategoryEntity;

/**
 * Implementation of the {@link CategoryDatabase} interface.
 * Provides access to predefined categories and their associated IDs.
 */
public class CategoryDatabaseImpl implements CategoryDatabase {

    private final Context context;

    public CategoryDatabaseImpl(Context context) {
        this.context = context;
    }

    /**
     * Returns a list of category names as strings.
     *
     * @return a list of category names.
     */
    @Override
    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        for (CategoryEntity category : getCategoryModels()) {
            categories.add(category.getName());
        }
        return categories;
    }

    /**
     * Returns a list of {@link CategoryEntity} objects representing the categories.
     *
     * @return a list of category models, each containing an ID and a name.
     */
    @Override
    public List<CategoryEntity> getCategoryModels() {
        List<CategoryEntity> categories = new ArrayList<>();
        categories.add(new CategoryEntity(1, context.getString(R.string.category_fruits_vegetables)));
        categories.add(new CategoryEntity(2, context.getString(R.string.category_dairy_alternatives)));
        categories.add(new CategoryEntity(3, context.getString(R.string.category_meat_fish_alternatives)));
        categories.add(new CategoryEntity(4, context.getString(R.string.category_drinks)));
        categories.add(new CategoryEntity(5, context.getString(R.string.category_prepared_meals)));
        categories.add(new CategoryEntity(6, context.getString(R.string.category_sauces_dips_spreads)));
        categories.add(new CategoryEntity(7, context.getString(R.string.category_bakery_dough)));
        categories.add(new CategoryEntity(8, context.getString(R.string.category_snacks_sweets)));
        categories.add(new CategoryEntity(9, context.getString(R.string.category_frozen_items)));
        categories.add(new CategoryEntity(10, context.getString(R.string.category_canned_non_perishable)));
        categories.add(new CategoryEntity(11, context.getString(R.string.category_spices_herbs_ingredients)));
        categories.add(new CategoryEntity(12, context.getString(R.string.category_miscellaneous)));
        return categories;
    }

    /**
     * Returns the name of the category corresponding to the given ID.
     *
     * @param id the ID of the category.
     * @return the name of the category, or null if no category matches the ID.
     */
    @Override
    public String getCategoryById(int id) {
        for (CategoryEntity category : getCategoryModels()) {
            if (category.getId() == id) {
                return category.getName();
            }
        }
        return null;
    }

    /**
     * Returns the ID of the category corresponding to the given name.
     *
     * @param categoryName the name of the category.
     * @return the ID of the category, or null if no category matches the name.
     */
    @Override
    public Integer getIdByCategory(String categoryName) {
        for (CategoryEntity category : getCategoryModels()) {
            if (category.getName().equalsIgnoreCase(categoryName)) {
                return category.getId();
            }
        }
        return null;
    }
}
