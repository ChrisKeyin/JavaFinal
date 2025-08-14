package com.chrisking.dao.jdbc;

import com.chrisking.dao.WorkoutClassDAO;
import com.chrisking.model.WorkoutClass;
import com.chrisking.util.Database;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class WorkoutClassJdbcDAO implements WorkoutClassDAO {

    @Override
    public WorkoutClass create(WorkoutClass wc) {
        String sql = """
            INSERT INTO workout_classes (class_type, class_description, trainer_id, scheduled_at)
            VALUES (?,?,?,?)
            RETURNING workout_class_id, scheduled_at
            """;
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, wc.getClassType());
            ps.setString(2, wc.getClassDescription());
            ps.setInt(3, wc.getTrainerId());
            if (wc.getScheduledAt() == null) {
                ps.setNull(4, Types.TIMESTAMP_WITH_TIMEZONE);
            } else {
                ps.setObject(4, Timestamp.from(wc.getScheduledAt().toInstant()));
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    wc.setWorkoutClassId(rs.getInt("workout_class_id"));
                    Timestamp ts = rs.getTimestamp("scheduled_at");
                    if (ts != null) wc.setScheduledAt(ts.toInstant().atOffset(java.time.ZoneOffset.UTC));
                }
            }
            return wc;
        } catch (SQLException e) {
            throw new RuntimeException("Create workout class failed: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(int classId, int trainerId, String type, String desc, OffsetDateTime when) {
        String sql = """
            UPDATE workout_classes
            SET class_type = ?, class_description = ?, scheduled_at = ?
            WHERE workout_class_id = ? AND trainer_id = ?
            """;
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, type);
            ps.setString(2, desc);
            if (when == null) {
                ps.setNull(3, Types.TIMESTAMP_WITH_TIMEZONE);
            } else {
                ps.setObject(3, Timestamp.from(when.toInstant()));
            }
            ps.setInt(4, classId);
            ps.setInt(5, trainerId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Update workout class failed: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int classId, int trainerId) {
        String sql = "DELETE FROM workout_classes WHERE workout_class_id = ? AND trainer_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ps.setInt(2, trainerId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Delete workout class failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<WorkoutClass> findByTrainer(int trainerId) {
        String sql = """
            SELECT workout_class_id, class_type, class_description, trainer_id, scheduled_at
            FROM workout_classes
            WHERE trainer_id = ?
            ORDER BY scheduled_at NULLS LAST, workout_class_id
            """;
        List<WorkoutClass> list = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, trainerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("findByTrainer failed: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<WorkoutClass> findAllUpcoming() {
        String sql = """
            SELECT workout_class_id, class_type, class_description, trainer_id, scheduled_at
            FROM workout_classes
            WHERE scheduled_at IS NULL OR scheduled_at >= now()
            ORDER BY scheduled_at NULLS LAST, workout_class_id
            """;
        List<WorkoutClass> list = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("findAllUpcoming failed: " + e.getMessage(), e);
        }
        return list;
    }

    private WorkoutClass map(ResultSet rs) throws SQLException {
        WorkoutClass wc = new WorkoutClass();
        wc.setWorkoutClassId(rs.getInt("workout_class_id"));
        wc.setClassType(rs.getString("class_type"));
        wc.setClassDescription(rs.getString("class_description"));
        wc.setTrainerId(rs.getInt("trainer_id"));
        Timestamp ts = rs.getTimestamp("scheduled_at");
        if (ts != null) wc.setScheduledAt(ts.toInstant().atOffset(java.time.ZoneOffset.UTC));
        return wc;
    }
}
