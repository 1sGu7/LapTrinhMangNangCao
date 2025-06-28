package server;

import crypto.CryptoUtils;
import model.Request;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class ServerSocketHandler {
    private final int port;

    public ServerSocketHandler(int port) {
        this.port = port;
    }

    public void start() {
        System.out.println("[Server] Starting on port " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[Server] Ready for connections");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("\n[Server] New connection from: " + clientSocket.getInetAddress());

                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("[Server] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

            System.out.println("[Server] Reading request...");
            Request request = (Request) in.readObject();
            System.out.println("[Server] Received request for message: " +
                    request.getMessage().substring(0, Math.min(20, request.getMessage().length())) + "...");

            System.out.println("[Server] Processing request...");
            String response = processRequest(request);

            System.out.println("[Server] Sending response...");
            out.writeObject(response);
            out.flush();

            System.out.println("[Server] Response sent");
        } catch (Exception e) {
            System.err.println("[Server] Client handling error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                System.out.println("[Server] Connection closed");
            } catch (IOException e) {
                System.err.println("[Server] Error closing socket: " + e.getMessage());
            }
        }
    }

    private String processRequest(Request request) {
        try {
            System.out.println("[Server] Reconstructing AES key and IV");
            SecretKey aesKey = new SecretKeySpec(request.getAesKeyBytes(), "AES");
            IvParameterSpec iv = new IvParameterSpec(request.getIv());

            System.out.println("[Server] Decrypting public key");
            byte[] decryptedPublicKey = CryptoUtils.decryptAES(
                    request.getEncryptedPublicKey(),
                    aesKey,
                    iv
            );

            System.out.println("[Server] Creating public key from bytes");
            PublicKey publicKey = KeyFactory.getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(decryptedPublicKey));

            System.out.println("[Server] Verifying signature");
            boolean isValid = CryptoUtils.verify(
                    request.getMessage(),
                    request.getSignature(),
                    publicKey
            );

            if (!isValid) {
                System.out.println("[Server] Verification FAILED");
                return "VERIFICATION_FAILED";
            }

            System.out.println("[Server] Verification SUCCESS");
            System.out.println("[Server] Saving key to database");
            Database.saveKeyInfo(
                    publicKey.getEncoded(),
                    request.getAesKeyBytes(),
                    request.getIv()
            );

            System.out.println("[Server] Scanning subdomains...");
            return SubdomainScanner.scan("huflit.edu.vn");

        } catch (Exception e) {
            System.err.println("[Server] Processing error: " + e.getMessage());
            e.printStackTrace();
            return "SERVER_ERROR: " + e.getMessage();
        }
    }
}