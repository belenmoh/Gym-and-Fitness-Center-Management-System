package com.gym.dao.impl;

import com.gym.dao.MemberDAO;
import com.gym.model.Member;
import com.gym.model.Membership;
import com.gym.model.MonthlyMembership;
import com.gym.model.AnnualMembership;
import com.gym.model.VIPMembership;
import com.gym.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemberDAOImpl implements MemberDAO{
    private static final String INSERT_SQL =
            "INSERT INTO members (user_id, membership_type, start_date, end_date) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL =
            "SELECT m.*, u.name, u.username, u.password FROM members m " +
                    "JOIN users u ON m.user_id = u.id WHERE m.id = ?";
    private static final String SELECT_BY_USER_ID_SQL =
            "SELECT m.*, u.name, u.username, u.password FROM members m " +
                    "JOIN users u ON m.user_id = u.id WHERE m.user_id = ?";
    private static final String SELECT_ALL_SQL =
            "SELECT m.*, u.name, u.username, u.password FROM members m " +
                    "JOIN users u ON m.user_id = u.id";
    private static final String SELECT_BY_MEMBERSHIP_TYPE_SQL =
            "SELECT m.*, u.name, u.username, u.password FROM members m " +
                    "JOIN users u ON m.user_id = u.id WHERE m.membership_type = ?";
    private static final String SELECT_ACTIVE_MEMBERS_SQL =
            "SELECT m.*, u.name, u.username, u.password FROM members m " +
                    "JOIN users u ON m.user_id = u.id WHERE m.end_date > ?";
    private static final String UPDATE_SQL =
            "UPDATE members SET membership_type = ?, start_date = ?, end_date = ? WHERE id = ?";
    private static final String DELETE_SQL =
            "DELETE FROM members WHERE id = ?";

    @Override
    public Member save(Member member) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, member.getId());
            stmt.setString(2, member.getMembership().getClass().getSimpleName());
            stmt.setDate(3, Date.valueOf(member.getStartDate()));
            stmt.setDate(4, Date.valueOf(member.getEndDate()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating member failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    member.setMemberId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating member failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving member", e);
        }
        return member;
    }

    @Override
    public Optional<Member> findById(int id) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToMember(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding member by id", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Member> findByUserId(int userId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_USER_ID_SQL)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToMember(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding member by user id", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Member> findAll() {
        List<Member> members = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                members.add(mapResultSetToMember(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all members", e);
        }
        return members;
    }

    @Override
    public List<Member> findByMembershipType(String membershipType) {
        List<Member> members = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_MEMBERSHIP_TYPE_SQL)) {

            stmt.setString(1, membershipType);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                members.add(mapResultSetToMember(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding members by membership type", e);
        }
        return members;
    }

    @Override
    public List<Member> findActiveMembers() {
        List<Member> members = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ACTIVE_MEMBERS_SQL)) {

            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                members.add(mapResultSetToMember(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding active members", e);
        }
        return members;
    }

}
