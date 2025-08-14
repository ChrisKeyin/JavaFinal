package com.chrisking.dao.jdbc;

import com.chrisking.dao.GymMerchDAO;
import com.chrisking.model.GymMerch;
import com.chrisking.util.Database;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GymMerchJdbcDAO implements GymMerchDAO {

    @Override
    public GymMerch create(GymMerch m) {
        String sql = """
            INSERT INTO gym_merch (merch_name, merch_type, merch_price, quantity_in_stock)
            VALUES (?,?,?,?)
            RETURNING merch_id
            """;
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getMerchName());
            ps.setString(2, m.getMerchType());
            ps.setBigDecimal(3, m.getMerchPrice());
            ps.setInt(4, m.getQuantityInStock());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) m.setMerchId(rs.getInt(1));
            }
            return m;
        } catch (SQLException e) {
            throw new RuntimeException("Create merch failed: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updatePrice(int merchId, BigDecimal newPrice) {
        String sql = "UPDATE gym_merch SET merch_price = ? WHERE merch_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, newPrice);
            ps.setInt(2, merchId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Update price failed: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateQuantity(int merchId, int newQty) {
        String sql = "UPDATE gym_merch SET quantity_in_stock = ? WHERE merch_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newQty);
            ps.setInt(2, merchId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Update quantity failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<GymMerch> findAll() {
        String sql = "SELECT merch_id, merch_name, merch_type, merch_price, quantity_in_stock FROM gym_merch ORDER BY merch_id";
        List<GymMerch> list = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                GymMerch m = new GymMerch();
                m.setMerchId(rs.getInt("merch_id"));
                m.setMerchName(rs.getString("merch_name"));
                m.setMerchType(rs.getString("merch_type"));
                m.setMerchPrice(rs.getBigDecimal("merch_price"));
                m.setQuantityInStock(rs.getInt("quantity_in_stock"));
                list.add(m);
            }
        } catch (SQLException e) {
            throw new RuntimeException("findAll merch failed: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public BigDecimal totalStockValue() {
        String sql = "SELECT COALESCE(SUM(merch_price * quantity_in_stock), 0) FROM gym_merch";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getBigDecimal(1);
        } catch (SQLException e) {
            throw new RuntimeException("totalStockValue failed: " + e.getMessage(), e);
        }
    }
}
