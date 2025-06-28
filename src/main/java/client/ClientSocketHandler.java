package client;

import model.Request;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientSocketHandler {
    private final String host;
    private final int port;
    private static final int TIMEOUT = 10000; // 10 seconds timeout

    public ClientSocketHandler(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String sendRequest(Request request) {
        try (Socket socket = new Socket()) {
            // Set connection timeout
            socket.connect(new java.net.InetSocketAddress(host, port), TIMEOUT);
            socket.setSoTimeout(TIMEOUT);

            try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                out.writeObject(request);
                out.flush();

                return (String) in.readObject();
            }
        } catch (SocketTimeoutException e) {
            return "Error: Connection timed out";
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        } catch (ClassNotFoundException e) {
            return "Error: Invalid response from server";
        }
    }
}