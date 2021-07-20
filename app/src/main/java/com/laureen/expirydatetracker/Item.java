package com.laureen.expirydatetracker;

import java.util.Comparator;

public class Item {
    private int id;
    private String name;
    private String date;
    private boolean expired;
    private int categoryID;

    public Item(int id, String name, String date, int categoryID) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.categoryID = categoryID;
        this.expired = false;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id: " + id +
                ", name: " + name +
                ", date: " + date +
                ", categoryID: " + categoryID +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpiry(boolean expired) {
        this.expired = expired;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }
}

