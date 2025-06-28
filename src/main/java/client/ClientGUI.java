package client;

import crypto.CryptoUtils;
import crypto.KeyManager;
import model.Request;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.swing.*;
import java.awt.*;
import java.security.PrivateKey;
import java.security.PublicKey;

public class ClientGUI extends JFrame {
    private JTextArea messageArea;
    private JTextArea responseArea;

    public ClientGUI() {
        setTitle("Client Application");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Message panel
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(BorderFactory.createTitledBorder("Message"));
        messageArea = new JTextArea(5, 20);
        messagePanel.add(new JScrollPane(messageArea), BorderLayout.CENTER);

        // Button
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendMessage());
        messagePanel.add(sendButton, BorderLayout.SOUTH);

        // Response panel
        JPanel responsePanel = new JPanel(new BorderLayout());
        responsePanel.setBorder(BorderFactory.createTitledBorder("Response"));
        responseArea = new JTextArea(15, 20);
        responseArea.setEditable(false);
        responsePanel.add(new JScrollPane(responseArea), BorderLayout.CENTER);

        // Add panels to frame
        add(messagePanel, BorderLayout.NORTH);
        add(responsePanel, BorderLayout.CENTER);
    }

    private void sendMessage() {
        String message = messageArea.getText().trim();
        if (message.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a message");
            return;
        }

        try {
            // Load keys
            PrivateKey privateKey = KeyManager.loadPrivateKey("private.pem");
            PublicKey publicKey = KeyManager.loadPublicKey("public.pem");

            // Sign message
            String signature = CryptoUtils.sign(message, privateKey);

            // Generate AES key and IV
            SecretKey aesKey = CryptoUtils.generateAESKey();
            byte[] ivBytes = new byte[16];
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            // Encrypt public key with AES
            byte[] encryptedPublicKey = CryptoUtils.encryptAES(publicKey.getEncoded(), aesKey, iv);

            // Create request
            Request request = new Request(
                    message,
                    signature,
                    encryptedPublicKey,
                    aesKey.getEncoded(),
                    iv.getIV()
            );

            // Send request to server
            ClientSocketHandler clientSocket = new ClientSocketHandler("localhost", 12345);

            // Show progress
            responseArea.setText("Sending request to server...");
            responseArea.paintImmediately(responseArea.getBounds());

            String response = clientSocket.sendRequest(request);

            // Display response
            responseArea.setText(response);

        } catch (Exception ex) {
            responseArea.setText("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientGUI clientGUI = new ClientGUI();
            clientGUI.setVisible(true);
        });
    }
}