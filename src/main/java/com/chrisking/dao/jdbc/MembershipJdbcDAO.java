package com.chrisking.dao.jdbc;

import com.chrisking.dao.MembershipDAO;
import com.chrisking.model.Membership;
import com.chrisking.util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembershipJdbcDAO implements MembershipDAO {

    @Override
    public Membership create(Membership m) {
        String sql = """
            INSERT INTO memberships (membership_type, membership_description, membership_cost, member_id)
            VALUES (?,?,?,?)
            RETURNING membership_id, purchased_at
            """;
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getMembershipType());
            ps.setString(2, m.getMembershipDescription());
            ps.setBigDecimal(3, java.math.BigDecimal.valueOf(m.getMembershipCost()));
            ps.setInt(4, m.getMemberId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    m.setMembershipId(rs.getInt("membership_id"));
                    Timestamp ts = rs.getTimestamp("purchased_at");
                    if (ts != null) m.setPurchasedAt(ts.toInstant().atOffset(java.time.ZoneOffset.UTC));
                }
            }
            return m;
        } catch (SQLException e) {
            throw new RuntimeException("Create membership failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Membership> findByMemberId(int memberId) {
        String sql = """
            SELECT membership_id, membership_type, membership_description, membership_cost, member_id, purchased_at
            FROM memberships WHERE member_id = ?
            ORDER BY purchased_at DESC
            """;
        List<Membership> list = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("findByMemberId failed: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public double totalRevenue() {
        String sql = "SELECT COALESCE(SUM(membership_cost), 0) FROM memberships";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getBigDecimal(1).doubleValue();
        } catch (SQLException e) {
            throw new RuntimeException("totalRevenue failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Membership> findAll() {
        String sql = """
            SELECT membership_id, membership_type, membership_description, membership_cost, member_id, purchased_at
            FROM memberships ORDER BY purchased_at DESC
            """;
        List<Membership> list = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("findAll failed: " + e.getMessage(), e);
        }
        return list;
    }

    private Membership map(ResultSet rs) throws SQLException {
        Membership m = new Membership();
        m.setMembershipId(rs.getInt("membership_id"));
        m.setMembershipType(rs.getString("membership_type"));
        m.setMembershipDescription(rs.getString("membership_description"));
        m.setMembershipCost(rs.getBigDecimal("membership_cost").doubleValue());
        m.setMemberId(rs.getInt("member_id"));
        Timestamp ts = rs.getTimestamp("purchased_at");
        if (ts != null) m.setPurchasedAt(ts.toInstant().atOffset(java.time.ZoneOffset.UTC));
        return m;
    }
}
