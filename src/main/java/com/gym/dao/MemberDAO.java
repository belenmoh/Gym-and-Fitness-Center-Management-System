package com.gym.dao;

import com.gym.model.Member;
import java.util.List;
import java.util.Optional;
public interface MemberDAO {
    Member save(Member member);
    Optional<Member> findById(int id);
    Optional<Member> findByUserId(int userId);
    List<Member> findAll();
    List<Member> findByMembershipType(String membershipType);
    List<Member> findActiveMembers();
    Member update(Member member);
    boolean delete(int id);
}
