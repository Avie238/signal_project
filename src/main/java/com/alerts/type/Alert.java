package com.alerts.type;

import com.data_management.RecordType;

// Represents an alert
public class Alert {
  private String patientId;
  private String condition;
  private long timestamp;

  private RecordType type;

  public Alert(String patientId, String condition, long timestamp) {
    this.patientId = patientId;
    this.condition = condition;
    this.timestamp = timestamp;
  }

  public String getPatientId() {
    return patientId;
  }

  public String getCondition() {
    return condition;
  }

  public long getTimestamp() {
    return timestamp;
  }
}
