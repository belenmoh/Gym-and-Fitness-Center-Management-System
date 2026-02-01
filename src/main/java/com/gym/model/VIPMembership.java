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
    @Override
    public double calculateDiscount() {
        return getPrice() * DISCOUNT_RATE;
    }
    @Override
    public String getBenefits() {
        return "All gym access, VIP locker room, premium equipment, unlimited personal training, spa access, nutrition consultation";
    }
    @Override
    public String toString() {
        return "VIPMembership{} " + super.toString();
    }
}
