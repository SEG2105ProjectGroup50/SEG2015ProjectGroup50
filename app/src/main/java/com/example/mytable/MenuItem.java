package com.example.mytable;

public class MenuItem {
    private String name, cuisineType, mealType,description, price, id, cookId;
    private boolean isOffered;


    public String getCookId() {
        return cookId;
    }

    public void setCookId(String cookId) {
        this.cookId = cookId;
    }

    public MenuItem() {
        name = "";
        description = "";
        price = "0.00";
        isOffered = false;
        mealType = "default";
        cuisineType = "default";
    }

    public MenuItem(String name, String description, String price, String cuisineType, String mealType, boolean isOffered) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.cuisineType = cuisineType;
        this.mealType = mealType;
        this.isOffered = isOffered;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean getIsOffered() {
        return isOffered;
    }

    public void setIsOffered(boolean offered) {
        isOffered = offered;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cusisineType) {
        this.cuisineType = cusisineType;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }
}
