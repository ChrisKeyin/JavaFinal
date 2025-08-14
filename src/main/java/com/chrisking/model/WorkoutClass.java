package com.chrisking.model;

import java.time.OffsetDateTime;

public class WorkoutClass {
    private Integer workoutClassId;
    private String classType;
    private String classDescription;
    private Integer trainerId;
    private OffsetDateTime scheduledAt;

    public Integer getWorkoutClassId() { return workoutClassId; }
    public void setWorkoutClassId(Integer workoutClassId) { this.workoutClassId = workoutClassId; }

    public String getClassType() { return classType; }
    public void setClassType(String classType) { this.classType = classType; }

    public String getClassDescription() { return classDescription; }
    public void setClassDescription(String classDescription) { this.classDescription = classDescription; }

    public Integer getTrainerId() { return trainerId; }
    public void setTrainerId(Integer trainerId) { this.trainerId = trainerId; }

    public OffsetDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(OffsetDateTime scheduledAt) { this.scheduledAt = scheduledAt; }

    @Override
    public String toString() {
        return "#" + workoutClassId + " | " + classType + " | trainerId=" + trainerId +
                " | when=" + (scheduledAt == null ? "(unscheduled)" : scheduledAt) +
                " | " + (classDescription == null ? "" : classDescription);
    }
}
