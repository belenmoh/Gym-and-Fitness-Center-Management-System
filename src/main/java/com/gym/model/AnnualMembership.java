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
    @Override
    public double calculateDiscount() {
        return getPrice() * DISCOUNT_RATE;
    }

    @Override
    public String getBenefits() {
        return "Full gym access, locker room, all equipment, 1 free personal training session per month";
    }

    @Override
    public String toString() {
        return "AnnualMembership{} " + super.toString();
    }
}
