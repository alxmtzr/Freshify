package de.alxmtzr.freshify.data.model;

import java.time.LocalDate;

public class ItemEntity {
    private long id;
    private final String name;
    private final int quantity;
    private final int categoryId;
    private final String categoryName;
    private final LocalDate expiryDate;
    private final String comment;

    public ItemEntity(long id, String name, int quantity, int categoryId, String categoryName, LocalDate expiryDate, String comment) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.expiryDate = expiryDate;
        this.comment = comment;
    }

    // setter for id after db insert
    public void setId(long id) {
        this.id = id;
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public String getComment() {
        return comment;
    }
}
