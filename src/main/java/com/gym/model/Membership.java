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
}
