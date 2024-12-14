package de.alxmtzr.freshify.data.local;

import java.util.List;

import de.alxmtzr.freshify.data.model.CategoryModel;

public interface CategoryDatabase {
    List<String> getCategories();
    List<CategoryModel> getCategoryModels();
    String getCategoryById(int id);
    Integer getIdByCategory(String categoryName);
}
