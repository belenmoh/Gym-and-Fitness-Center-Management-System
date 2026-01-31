package com.gym.service;

import com.gym.dao.PaymentDAO;
import com.gym.model.Member;
import com.gym.model.Membership;
import com.gym.model.Payment;
import com.gym.model.PaymentType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class BillingService {
    private final PaymentDAO paymentDAO;
    private final com.gym.service.MembershipService membershipService;

    public BillingService(PaymentDAO paymentDAO, com.gym.service.MembershipService membershipService) {
        this.paymentDAO = paymentDAO;
        this.membershipService = membershipService;
    }

    public Payment recordMembershipPayment(int memberId, double amount) {
        Optional<Member> memberOpt = membershipService.findMemberById(memberId);
        if (memberOpt.isEmpty()) {
            throw new IllegalArgumentException("Member not found");
        }

        Payment payment = new Payment();
        payment.setMemberId(memberId);
        payment.setAmount(amount);
        payment.setDate(LocalDate.now());
        payment.setType(PaymentType.MEMBERSHIP);

        return paymentDAO.save(payment);
    }

    public Payment recordClassPayment(int memberId, double amount) {
        Optional<Member> memberOpt = membershipService.findMemberById(memberId);
        if (memberOpt.isEmpty()) {
            throw new IllegalArgumentException("Member not found");
        }

        Payment payment = new Payment();
        payment.setMemberId(memberId);
        payment.setAmount(amount);
        payment.setDate(LocalDate.now());
        payment.setType(PaymentType.CLASS);

        return paymentDAO.save(payment);
    }

    public Payment recordOtherPayment(int memberId, double amount) {
        Optional<Member> memberOpt = membershipService.findMemberById(memberId);
        if (memberOpt.isEmpty()) {
            throw new IllegalArgumentException("Member not found");
        }

        Payment payment = new Payment();
        payment.setMemberId(memberId);
        payment.setAmount(amount);
        payment.setDate(LocalDate.now());
        payment.setType(PaymentType.OTHER);

        return paymentDAO.save(payment);
    }

    public Optional<Payment> findPaymentById(int id) {
        return paymentDAO.findById(id);
    }

    public List<Payment> getPaymentsByMember(int memberId) {
        return paymentDAO.findByMemberId(memberId);
    }

    public List<Payment> getPaymentsByType(PaymentType type) {
        return paymentDAO.findByType(type);
    }

    public List<Payment> getPaymentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return paymentDAO.findByDateRange(startDate, endDate);
    }

    public List<Payment> getPaymentsByMonth(int month, int year) {
        return paymentDAO.findByMonth(month, year);
    }

    public double getTotalPaymentsByMember(int memberId) {
        return paymentDAO.findByMemberId(memberId).stream()
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    public double getTotalPaymentsByMemberAndType(int memberId, PaymentType type) {
        return paymentDAO.findByMemberId(memberId).stream()
                .filter(payment -> payment.getType() == type)
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    public double calculateMembershipFee(Membership membership) {
        return membershipService.calculateMembershipPrice(membership);
    }

    public boolean processMembershipRenewal(int memberId, Membership newMembership) {
        try {
            double fee = calculateMembershipFee(newMembership);
            recordMembershipPayment(memberId, fee);
            membershipService.renewMembership(memberId, newMembership);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
