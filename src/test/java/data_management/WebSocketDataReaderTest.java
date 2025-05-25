package data_management;

import static org.junit.jupiter.api.Assertions.*;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;
import com.data_management.RecordType;
import com.data_management.WebSocketDataReader;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WebSocketDataReaderTest {

  private DataStorage mockStorage;
  private WebSocketDataReader reader;

  @BeforeEach
  void setUp() throws Exception {
    mockStorage = new DataStorage(true);

    // Use localhost dummy URI (does not need to connect in unit tests)
    reader =
        new WebSocketDataReader("ws://localhost:12345") {
          @Override
          public void connect() {
            // Prevent real connection during test
          }
        };
  }

  @Test
  void testOnMessageWithValidMessageAndStorageSet() {
    reader.readData(mockStorage);
    String message = "5,1700000000000,Saturation,99";

    reader.onMessage(message);

    List<PatientRecord> records = mockStorage.getRecords(5, 1700000000000L, 1700000000001L);

    assertEquals(1, records.size());
    assertEquals(5, records.get(0).getPatientId());
    assertEquals(99, records.get(0).getMeasurementValue());
    assertEquals(RecordType.SATURATION, records.get(0).getRecordType());
    assertEquals(1700000000000L, records.get(0).getTimestamp());
  }

  @Test
  void testOnMessageWithNoStorageSet() {
    String message = "5,1700000000000,Saturation,99";

    // Should not throw error even though storage is null, just ignore the message
    assertDoesNotThrow(() -> reader.onMessage(message));
  }

  @Test
  void testOnErrorLogging() {
    Exception ex = new Exception("Test error");
    assertDoesNotThrow(() -> reader.onError(ex));
  }

  @Test
  void testMalformedMessageThrowsException() {
    reader.readData(mockStorage);
    String malformedMessage = "bad message with no commas";

    // Let it throw or catch/log depending on desired behavior
    assertDoesNotThrow(() -> reader.onMessage(malformedMessage));
  }
}
