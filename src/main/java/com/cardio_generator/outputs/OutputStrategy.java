package com.cardio_generator.outputs;

/**
 * The {@code OutputStrategy} interface defines a strategy for outputting generated health data for
 * patients.
 *
 * <p>Implementing classes are responsible for determining how and where the data is delivered, such
 * as to the console, a file, a WebSocket server, or a TCP socket.
 *
 * <p>This abstraction allows the {@code HealthDataSimulator} and data generators to remain
 * output-agnostic, supporting easy extensibility for new output mechanisms.
 */
public interface OutputStrategy {
  /**
   * Outputs the generated data for a specific patient using the chosen output mechanism.
   *
   * @param patientId The ID of the patient associated with the data.
   * @param timestamp The timestamp of the generated data.
   * @param label A label or category for the type of data.
   * @param data The actual data content to output.
   */
  void output(int patientId, long timestamp, String label, String data);
}
