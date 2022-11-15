package com.example.mytable;

public class MenuItem {
    private String name, description, price;
    private boolean isOffered;
    private String id;

    public MenuItem() {
        name = "";
        description = "";
        price = "0.00";
        isOffered = false;
    }

    public MenuItem(String name, String description, String price, boolean isOffered) {
        this.name = name;
        this.description = description;
        this.price = price;
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
}
