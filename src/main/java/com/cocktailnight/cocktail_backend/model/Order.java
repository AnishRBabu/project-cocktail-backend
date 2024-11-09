package com.cocktailnight.cocktail_backend.model;

import java.util.UUID;

public class Order {
    private String id;
    private String customerName;
    private String drinkName;
    private boolean completed = false;

    public Order() {
        this.id = UUID.randomUUID().toString();
    }

    public Order(String customerName, String drinkName) {
        this.id = UUID.randomUUID().toString();
        this.customerName = customerName;
        this.drinkName = drinkName;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}