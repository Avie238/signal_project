package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * The {@code PatientDataGenerator} interface defines how to generate patient-specific health data.
 *
 * <p>Implementing classes must define how data is generated for a given patient ID and how it is
 * delivered using a specified {@link OutputStrategy}.
 */
public interface PatientDataGenerator {
  /**
   * Generates health-related data for a specific patient and sends the output using the provided
   * {@link OutputStrategy}.
   *
   * @param patientId The ID of the patient to generate data for.
   * @param outputStrategy The strategy used to output the generated data.
   */
  void generate(int patientId, OutputStrategy outputStrategy);
}
