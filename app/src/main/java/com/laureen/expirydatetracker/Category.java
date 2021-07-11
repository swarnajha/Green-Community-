package com.laureen.expirydatetracker;

public class Category {
    private int id;
    private String name;
    private int days;
    private int imageNo;

    public Category(int id, String name, int days, int imageNo) {
        this.id = id;
        this.name = name;
        this.days = days;
        this.imageNo = imageNo;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id: " + id +
                ", name: " + name +
                ", days: " + days +
                ", imageName: " + imageNo +
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

    public int getImageNo() {
        return imageNo;
    }

    public void setImageNo(int imageNo) {
        this.imageNo = imageNo;
    }
}
