package de.alxmtzr.freshify.data.local.impl;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import de.alxmtzr.freshify.R;
import de.alxmtzr.freshify.data.local.CategoryDatabase;

public class CategoryDatabaseImpl implements CategoryDatabase {

    private final Context context;

    public CategoryDatabaseImpl(Context context) {
        this.context = context;
    }

    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        categories.add(context.getString(R.string.category_fruits_vegetables));
        categories.add(context.getString(R.string.category_dairy_alternatives));
        categories.add(context.getString(R.string.category_meat_fish_alternatives));
        categories.add(context.getString(R.string.category_drinks));
        categories.add(context.getString(R.string.category_prepared_meals));
        categories.add(context.getString(R.string.category_sauces_dips_spreads));
        categories.add(context.getString(R.string.category_bakery_dough));
        categories.add(context.getString(R.string.category_snacks_sweets));
        categories.add(context.getString(R.string.category_frozen_items));
        categories.add(context.getString(R.string.category_canned_non_perishable));
        categories.add(context.getString(R.string.category_spices_herbs_ingredients));
        categories.add(context.getString(R.string.category_miscellaneous));
        return categories;
    }
}

