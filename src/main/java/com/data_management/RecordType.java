package com.data_management;

public enum RecordType {
  CHOLESTEROL("Cholesterol"),
  WHITE_BLOOD_CELLS("WhiteBloodCells"),
  RED_BLOOD_CELLS("RedBloodCells"),
  SYSTOLIC_PRESSURE("SystolicPressure"),
  DIASTOLIC_PRESSURE("DiastolicPressure"),
  SATURATION("Saturation"),
  ECG("ECG");

  private String label;

  RecordType(String label) {
    this.label = label;
  }

  public String getName() {
    return this.label;
  }

  public static RecordType fromLabel(String label) {
    for (RecordType value : RecordType.values()) {
      if (value.label.equalsIgnoreCase(label)) {
        return value;
      }
    }
    return null;
  }
}
