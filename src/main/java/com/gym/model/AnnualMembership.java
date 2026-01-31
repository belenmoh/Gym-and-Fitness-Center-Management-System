package com.gym.model;

public class AnnualMembership extends Membership{
    private static final double DISCOUNT_RATE = 0.15;

    public AnnualMembership() {
        super();
        setDurationMonths(12);
    }

    public AnnualMembership(int id, String name, double price) {
        super(id, name, price, 12);
    }
}
