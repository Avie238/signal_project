package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

// Renamed class name to use UpperCamelCase
public class FileOutputStrategy implements OutputStrategy {

  // Renamed variable name to use lowerCamelCase.
  private String baseDirectory;

  // Renamed constant name to use UPPER_SNAKE_CASE
  public final ConcurrentHashMap<String, String> FILE_MAP = new ConcurrentHashMap<>();

  public FileOutputStrategy(String baseDirectory) {
    this.baseDirectory = baseDirectory;
  }

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
