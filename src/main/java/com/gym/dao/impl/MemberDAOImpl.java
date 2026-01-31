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
}
