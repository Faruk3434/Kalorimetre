package com.example.kalorimetre;

public class Meals {
    private String id, products, calorie, ListID;

    public Meals() {
    }

    public Meals(String ListID, String id, String products, String calorie) {
        this.ListID = ListID;
        this.id = id;
        this.products = products;
        this.calorie = calorie;
    }

    public String getId() {
        return id;
    }

    public String getListID() {
        return ListID;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setListID(String id) {
        this.ListID = ListID;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }
}
