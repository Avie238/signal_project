package com.data_management;

import java.net.URI;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class WebSocketDataReader extends WebSocketClient implements DataReader {

  DataStorage storage;

  public WebSocketDataReader(URI serverURI) {
    super(serverURI);
    this.connect();
  }

  public void readData(DataStorage storage) {
    this.storage = storage;
  }

  @Override
  public void onOpen(ServerHandshake handshakedata) {
    System.out.println("new connection opened");
  }

  @Override
  public void onClose(int code, String reason, boolean remote) {

    System.out.println("closed with exit code " + code + " additional info: " + reason);
  }

  @Override
  public void onMessage(String message) {
    System.out.println("received message: " + message);

    // If no storage defined just ignore the message
    if (storage != null) {
      // Expected format: int,long,String,double
      String[] parts = message.split(",");
      int patientId = Integer.parseInt(parts[0]);
      long timestamp = Long.parseLong(parts[1]);
      String label = parts[2];
      double data = Double.parseDouble(parts[3]);

      storage.addPatientData(patientId, data, label, timestamp);
    }
  }

  public void onError(Exception ex) {
    System.err.println("an error occurred:" + ex);
  }
}
