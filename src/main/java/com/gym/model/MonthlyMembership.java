package com.gym.model;

public class MonthlyMembership extends Membership{
    private static final double DISCOUNT_RATE = 0.0;

    public MonthlyMembership() {
        super();
        setDurationMonths(1);
    }

    public MonthlyMembership(int id, String name, double price) {
        super(id, name, price, 1);
    }
}
