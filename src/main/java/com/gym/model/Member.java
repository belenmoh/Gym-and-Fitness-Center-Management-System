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
}
