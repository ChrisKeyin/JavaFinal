package com.chrisking.dao;

import com.chrisking.model.WorkoutClass;
import java.time.OffsetDateTime;
import java.util.List;

public interface WorkoutClassDAO {
    WorkoutClass create(WorkoutClass wc);
    boolean update(int classId, int trainerId, String type, String desc, OffsetDateTime when);
    boolean delete(int classId, int trainerId);
    List<WorkoutClass> findByTrainer(int trainerId);
    List<WorkoutClass> findAllUpcoming();
}
