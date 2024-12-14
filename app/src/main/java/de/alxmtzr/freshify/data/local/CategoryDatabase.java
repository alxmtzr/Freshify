package de.alxmtzr.freshify.data.local;

import java.util.List;

import de.alxmtzr.freshify.data.model.CategoryEntity;

public interface CategoryDatabase {
    List<String> getCategories();
    List<CategoryEntity> getCategoryModels();
    String getCategoryById(int id);
    Integer getIdByCategory(String categoryName);
}
