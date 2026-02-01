package com.gym.model;

import java.time.LocalDate;

public class Member extends User{
    private int memberId;
    private Membership membership;
    private LocalDate startDate;
    private LocalDate endDate;

    public Member() {
        super();
        setRole(UserRole.MEMBER);
    }
    public Member(int id, String name, String username, String password,
                  int memberId, Membership membership, LocalDate startDate, LocalDate endDate) {
        super(id, name, username, password, UserRole.MEMBER);
        this.memberId = memberId;
        this.membership = membership;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public int getMemberId() {
        return memberId;
    }
    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }
    public Membership getMembership() {
        return membership;
    }
    public void setMembership(Membership membership) {
        this.membership = membership;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public boolean isMembershipActive() {
        return endDate != null && endDate.isAfter(LocalDate.now());
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberId=" + memberId +
                ", membership=" + membership +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                "} " + super.toString();
    }
}
