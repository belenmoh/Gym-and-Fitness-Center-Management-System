package com.gym.model;

public class VIPMembership extends Membership{
    private static final double DISCOUNT_RATE = 0.25;

    public VIPMembership() {
        super();
        setDurationMonths(12);
    }

    public VIPMembership(int id, String name, double price) {
        super(id, name, price, 12);
    }
}
