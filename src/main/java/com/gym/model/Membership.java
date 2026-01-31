package com.gym.model;

public abstract class Membership {
    private int id;
    private String name;
    private double price;
    private int durationMonths;

    public Membership() {}

    public Membership(int id, String name, double price, int durationMonths) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.durationMonths = durationMonths;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(int durationMonths) {
        this.durationMonths = durationMonths;
    }

    public abstract double calculateDiscount();

    public abstract String getBenefits();
    @Override
    public String toString() {
        return "Membership{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", durationMonths=" + durationMonths +
                '}';
    }
}
