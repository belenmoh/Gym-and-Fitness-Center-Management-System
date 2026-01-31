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
}
