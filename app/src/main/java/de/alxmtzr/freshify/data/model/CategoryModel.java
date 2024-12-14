package de.alxmtzr.freshify.data.model;

public class CategoryModel {
    private final int id;
    private final String name;

    public CategoryModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
