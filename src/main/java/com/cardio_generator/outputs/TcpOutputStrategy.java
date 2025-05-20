package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * The {@code TcpOutputStrategy} class implements the {@link OutputStrategy} interface to send
 * patient health data to a TCP client over a socket connection.
 *
 * <p>When instantiated, it starts a TCP server on the specified port and listens for a single
 * client connection. Once connected, data is sent to the client in real-time.
 *
 * <p>Each data point is sent as a comma-separated line in the format:
 *
 * <pre>{@code patientId,timestamp,label,data}</pre>
 */
public class TcpOutputStrategy implements OutputStrategy {

  private ServerSocket serverSocket;
  private Socket clientSocket;
  private PrintWriter out;

  /**
   * Constructs a new {@code TcpOutputStrategy} and starts a TCP server on the given port. The
   * server accepts a single client connection in a background thread.
   *
   * @param port The port number on which the TCP server listens for incoming connections.
   */
  public TcpOutputStrategy(int port) {
    try {
      serverSocket = new ServerSocket(port);
      System.out.println("TCP Server started on port " + port);

      // Accept clients in a new thread to not block the main thread
      Executors.newSingleThreadExecutor()
          .submit(
              () -> {
                try {
                  clientSocket = serverSocket.accept();
                  out = new PrintWriter(clientSocket.getOutputStream(), true);
                  System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                  e.printStackTrace();
                }
              });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Sends a formatted data message to the connected TCP client. If no client is connected, the
   * message is silently ignored.
   *
   * @param patientId The ID of the patient associated with the data.
   * @param timestamp The timestamp of the generated data in milliseconds since epoch.
   * @param label A label identifying the type of data.
   * @param data The actual data to be transmitted.
   */
  @Override
  public void output(int patientId, long timestamp, String label, String data) {
    if (out != null) {
      String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
      out.println(message);
    }
  }
}
