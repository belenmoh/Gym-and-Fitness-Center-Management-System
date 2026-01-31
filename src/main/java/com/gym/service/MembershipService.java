package com.gym.service;

import com.gym.dao.MemberDAO;
import com.gym.dao.UserDAO;
import com.gym.model.Member;
import com.gym.model.Membership;
import com.gym.model.MonthlyMembership;
import com.gym.model.AnnualMembership;
import com.gym.model.VIPMembership;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MembershipService {
    private final MemberDAO memberDAO;
    private final UserDAO userDAO;

    public MembershipService(MemberDAO memberDAO, UserDAO userDAO) {
        this.memberDAO = memberDAO;
        this.userDAO = userDAO;
    }

    public Member registerMember(Member member) {
        if (userDAO.existsByUsername(member.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        userDAO.save(member);
        return memberDAO.save(member);
    }

    public Member renewMembership(int memberId, Membership newMembership) {
        Optional<Member> memberOpt = memberDAO.findById(memberId);
        if (memberOpt.isEmpty()) {
            throw new IllegalArgumentException("Member not found");
        }

        Member member = memberOpt.get();
        LocalDate endDate = LocalDate.now().plusMonths(newMembership.getDurationMonths());

        member.setMembership(newMembership);
        member.setStartDate(LocalDate.now());
        member.setEndDate(endDate);

        return memberDAO.update(member);
    }
}
