package com.laureen.expirydatetracker;

public class Category {
    private int id;
    private String name;
    private int days;
    private String imageName;

    public Category(int id, String name, int days, String imageName) {
        this.id = id;
        this.name = name;
        this.days = days;
        this.imageName = imageName;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id: " + id +
                ", name: " + name +
                ", days: " + days +
                ", imageName: " + imageName +
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

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
