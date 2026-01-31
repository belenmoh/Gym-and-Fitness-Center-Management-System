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

    public Optional<Member> findMemberById(int id) {
        return memberDAO.findById(id);
    }

    public List<Member> getAllMembers() {
        return memberDAO.findAll();
    }

    public List<Member> getActiveMembers() {
        return memberDAO.findActiveMembers();
    }

    public List<Member> getMembersByMembershipType(String membershipType) {
        return memberDAO.findByMembershipType(membershipType);
    }

    public boolean isMembershipActive(int memberId) {
        Optional<Member> memberOpt = memberDAO.findById(memberId);
        return memberOpt.map(Member::isMembershipActive).orElse(false);
    }

    public Membership createMembership(String type, double price) {
        return switch (type.toLowerCase()) {
            case "monthly" -> new MonthlyMembership(0, "Monthly Membership", price);
            case "annual" -> new AnnualMembership(0, "Annual Membership", price);
            case "vip" -> new VIPMembership(0, "VIP Membership", price);
            default -> throw new IllegalArgumentException("Invalid membership type: " + type);
        };
    }

    public double calculateMembershipPrice(Membership membership) {
        return membership.getPrice() - membership.calculateDiscount();
    }

    public String getMembershipBenefits(Membership membership) {
        return membership.getBenefits();
    }
}
