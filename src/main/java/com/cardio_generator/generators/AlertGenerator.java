package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;
import java.util.Random;

/**
 * The {@code AlertGenerator} class simulates the triggering of alert states for patients in *
 *
 * <p>Each patient has a binary alert state: {@code pressed} or {@code resolved}. The generator
 * periodically decides whether an alert is triggered or resolved.
 */
public class AlertGenerator implements PatientDataGenerator {

  public static final Random randomGenerator = new Random();
  // Renamed variable name to use lowerCammelCase
  private boolean[] alertStates; // false = resolved, true = pressed

  /**
   * Constructs a new {@code AlertGenerator} for the specified number of patients. All patients
   * start with resolved alert states.
   *
   * @param patientCount The number of patients to simulate alerts for.
   */
  public AlertGenerator(int patientCount) {
    alertStates = new boolean[patientCount + 1];
  }

  /**
   * Generates a simulated alert state for the specified patient.
   *
   * <p>If an alert is already active, there's a 90% chance it will be resolved. If no alert is
   * active, a new alert may be triggered
   *
   * <p>Outputs are sent with the label {@code "Alert"} and data values of either {@code
   * "triggered"} or {@code "resolved"}.
   *
   * @param patientId The ID of the patient.
   * @param outputStrategy The strategy used to send or store the generated alert data.
   */
  @Override
  public void generate(int patientId, OutputStrategy outputStrategy) {
    try {
      if (alertStates[patientId]) {
        if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve
          alertStates[patientId] = false;
          // Output the alert
          outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
        }
      } else {
        // Renamed variable name to use lowerCammelCase
        double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
        double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
        boolean alertTriggered = randomGenerator.nextDouble() < p;

        if (alertTriggered) {
          alertStates[patientId] = true;
          // Output the alert
          outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
        }
      }
    } catch (Exception e) {
      System.err.println("An error occurred while generating alert data for patient " + patientId);
      e.printStackTrace();
    }
  }
}
