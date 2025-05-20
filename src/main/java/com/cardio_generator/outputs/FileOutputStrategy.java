package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@code FileOutputStrategy} class implements the {@link OutputStrategy} interface and writes
 * patient health data to text files on disk.
 */
// Renamed class name to use UpperCamelCase
public class FileOutputStrategy implements OutputStrategy {

  // Renamed variable name to use lowerCamelCase.
  private String baseDirectory;

  // Renamed constant name to use UPPER_SNAKE_CASE
  public final ConcurrentHashMap<String, String> FILE_MAP = new ConcurrentHashMap<>();

  /**
   * Constructs a new {@code FileOutputStrategy} with the specified base directory.
   *
   * @param baseDirectory The directory where output files will be written.
   */
  public FileOutputStrategy(String baseDirectory) {
    this.baseDirectory = baseDirectory;
  }

  /**
   * Outputs the data by writing it to a text file named after the data label. The output line
   * includes the patient ID, timestamp, label, and actual data.
   *
   * <p>If the directory or file does not exist, they are created.
   *
   * @param patientId The ID of the patient associated with the data.
   * @param timestamp The timestamp of the generated data in milliseconds since epoch.
   * @param label A label identifying the type of data.
   * @param data The data content to be written to the file.
   */
  @Override
  public void output(int patientId, long timestamp, String label, String data) {
    try {
      // Create the directory
      Files.createDirectories(Paths.get(baseDirectory));
    } catch (IOException e) {
      System.err.println("Error creating base directory: " + e.getMessage());
      return;
    }
    // Set the FilePath variable
    String FilePath =
        FILE_MAP.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());

    // Write the data to the file
    try (PrintWriter out =
        new PrintWriter(
            Files.newBufferedWriter(
                Paths.get(FilePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
      out.printf(
          "Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n",
          patientId, timestamp, label, data);
    } catch (Exception e) {
      System.err.println("Error writing to file " + FilePath + ": " + e.getMessage());
    }
  }
}
