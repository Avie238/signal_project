package data_management;

import static org.junit.jupiter.api.Assertions.*;

import com.data_management.DataStorage;
import com.data_management.FileDataReader;
import com.data_management.PatientRecord;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileDataReaderTest {

  @TempDir Path tempDir;

  private DataStorage storage;
  private FileDataReader reader;

  @BeforeEach
  void setup() {
    storage = new DataStorage();
    reader = new FileDataReader(tempDir.toString());
  }

  @Test
  void testReadDataSingleFile() throws IOException {
    String fileName = "HeartRate.txt";
    Path filePath = tempDir.resolve(fileName);

    String content =
        String.join(
            System.lineSeparator(),
            "Patient ID: 1, Timestamp: 1700000000000, Label: HeartRate, Data: 75.5",
            "Patient ID: 1, Timestamp: 1700000001000, Label: HeartRate, Data: 80.0");

    Files.write(filePath, content.getBytes());

    reader.readData(storage);

    List<PatientRecord> records = storage.getRecords(1, 1700000000000L, 1700000002000L);

    assertEquals(2, records.size());

    PatientRecord first = records.get(0);
    assertEquals(1, first.getPatientId());
    assertEquals("HeartRate", first.getRecordType());
    assertEquals(75.5, first.getMeasurementValue());
    assertEquals(1700000000000L, first.getTimestamp());

    PatientRecord second = records.get(1);
    assertEquals(1, second.getPatientId());
    assertEquals("HeartRate", second.getRecordType());
    assertEquals(80.0, second.getMeasurementValue());
    assertEquals(1700000001000L, second.getTimestamp());
  }

  @Test
  void testReadDataWithMalformedLine() throws IOException {
    Path filePath = tempDir.resolve("BloodPressure.txt");

    String content =
        String.join(
            System.lineSeparator(),
            "Patient ID: 2, Timestamp: 1700000000000, Label: BloodPressure, Data: 120.0",
            "This is a malformed line",
            "Patient ID: 2, Timestamp: 1700000001000, Label: BloodPressure, Data: 125.5");

    Files.write(filePath, content.getBytes());

    reader.readData(storage);

    List<PatientRecord> records = storage.getRecords(2, 1700000000000L, 1700000001000L);

    // The reader should have rejected the middle mallformed line
    assertEquals(2, records.size());
  }

  @Test
  void testEmptyDirectory() {
    reader.readData(storage);
    assertTrue(storage.getAllPatients().isEmpty());
  }

  @Test
  void testNonexistentDirectory() {
    FileDataReader badReader = new FileDataReader("nonexistent_directory");
    badReader.readData(storage);
    assertTrue(storage.getAllPatients().isEmpty());
  }
}
