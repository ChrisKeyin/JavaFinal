package com.chrisking.service;

import com.chrisking.dao.WorkoutClassDAO;
import com.chrisking.dao.jdbc.WorkoutClassJdbcDAO;
import com.chrisking.model.WorkoutClass;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.logging.Logger;
import com.chrisking.util.AppLog;

public class WorkoutClassService {
    private final WorkoutClassDAO dao = new WorkoutClassJdbcDAO();
    private static final Logger log = AppLog.get(WorkoutClassService.class);

    public WorkoutClass create(int trainerId, String type, String desc, OffsetDateTime when) {
        if (type == null || type.isBlank()) throw new IllegalArgumentException("Class type required");
        WorkoutClass wc = new WorkoutClass();
        wc.setTrainerId(trainerId);
        wc.setClassType(type);
        wc.setClassDescription(desc);
        wc.setScheduledAt(when);
        WorkoutClass saved = dao.create(wc);
        log.info("Class created: id=" + saved.getWorkoutClassId() + ", trainerId=" + trainerId + ", type=" + type);
        return saved;
    }

    public boolean update(int classId, int trainerId, String type, String desc, OffsetDateTime when) {
        if (type == null || type.isBlank()) throw new IllegalArgumentException("Class type required");
        boolean ok = dao.update(classId, trainerId, type, desc, when);
        log.info("Class update attempt: id=" + classId + ", trainerId=" + trainerId + ", success=" + ok);
        return ok;
    }

    public boolean delete(int classId, int trainerId) {
        boolean ok = dao.delete(classId, trainerId);
        log.info("Class delete attempt: id=" + classId + ", trainerId=" + trainerId + ", success=" + ok);
        return ok;
    }

    public List<WorkoutClass> byTrainer(int trainerId) {
        return dao.findByTrainer(trainerId);
    }

    public List<WorkoutClass> upcoming() {
        return dao.findAllUpcoming();
    }
}
