package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;
import java.util.Random;

/**
 * The {@code BloodSaturationDataGenerator} class simulates blood oxygen saturation values for a
 * given number of patients.
 *
 * <p>It generates values that fluctuate slightly over time to mimic realistic patient behavior. The
 * values stay within a healthy range (90–100%).
 *
 * <p>Each patient is initialized with a random baseline value between 95% and 100%, and small
 * variations are applied on each generation cycle.
 *
 * <p>The output is formatted as a percangege and labeled as "Saturation".
 */
public class BloodSaturationDataGenerator implements PatientDataGenerator {
  private static final Random random = new Random();
  private int[] lastSaturationValues;

  /**
   * Constructs a new {@code BloodSaturationDataGenerator} and initializes each patient with a
   * random baseline blood saturation level between 95% and 100%.
   *
   * @param patientCount The number of patients for which to simulate data.
   */
  public BloodSaturationDataGenerator(int patientCount) {
    lastSaturationValues = new int[patientCount + 1];

    // Initialize with baseline saturation values for each patient
    for (int i = 1; i <= patientCount; i++) {
      lastSaturationValues[i] =
          95 + random.nextInt(6); // Initializes with a value between 95 and 100
    }
  }

  /**
   * Generates a simulated blood saturation value for the given patient, with slight variation from
   * the last known value, and sends it to the specified output strategy.
   *
   * <p>Values are bounded between 90% and 100% to ensure realism.
   *
   * @param patientId The ID of the patient for whom to generate data.
   * @param outputStrategy The strategy used to deliver the generated data.
   */
  @Override
  public void generate(int patientId, OutputStrategy outputStrategy) {
    try {
      // Simulate blood saturation values
      int variation = random.nextInt(3) - 1; // -1, 0, or 1 to simulate small fluctuations
      int newSaturationValue = lastSaturationValues[patientId] + variation;

      // Ensure the saturation stays within a realistic and healthy range
      newSaturationValue = Math.min(Math.max(newSaturationValue, 90), 100);
      lastSaturationValues[patientId] = newSaturationValue;
      // NOTE:  Deleted the percangege sign in the end all data for the data storage is stored as a
      // number
      // so that sign will be lost either way, there is no need to add it.
      outputStrategy.output(
          patientId, System.currentTimeMillis(), "Saturation", Double.toString(newSaturationValue));
    } catch (Exception e) {
      System.err.println(
          "An error occurred while generating blood saturation data for patient " + patientId);
      e.printStackTrace(); // This will print the stack trace to help identify where the error
      // occurred.
    }
  }
}
