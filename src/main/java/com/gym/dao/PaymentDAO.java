package com.gym.dao;

import com.gym.model.Payment;
import com.gym.model.PaymentType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
public interface PaymentDAO {
    Payment save(Payment payment);
    Optional<Payment> findById(int id);
    List<Payment> findAll();
    List<Payment> findByMemberId(int memberId);
    List<Payment> findByDateRange(LocalDate startDate, LocalDate endDate);
    List<Payment> findByType(PaymentType type);
    List<Payment> findByMonth(int month, int year);
    double getTotalIncomeByMonth(int month, int year);
    Payment update(Payment payment);
    boolean delete(int id);
}
