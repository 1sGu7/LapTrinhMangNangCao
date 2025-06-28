package crypto;

import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class KeyGeneratorApp {

    public static void main(String[] args) {
        try {
            // Tạo cặp khóa RSA 2048-bit
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair keyPair = keyGen.generateKeyPair();

            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            // Tạo file private.pem
            String privateKeyPEM = "-----BEGIN PRIVATE KEY-----\n" +
                    Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(privateKey.getEncoded()) +
                    "\n-----END PRIVATE KEY-----";

            try (FileWriter writer = new FileWriter("private.pem")) {
                writer.write(privateKeyPEM);
            }

            // Tạo file public.pem
            String publicKeyPEM = "-----BEGIN PUBLIC KEY-----\n" +
                    Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(publicKey.getEncoded()) +
                    "\n-----END PUBLIC KEY-----";

            try (FileWriter writer = new FileWriter("public.pem")) {
                writer.write(publicKeyPEM);
            }

            System.out.println("Keys generated successfully!");
            System.out.println("1. private.pem");
            System.out.println("2. public.pem");
            System.out.println("\nFiles saved in project root directory");

        } catch (NoSuchAlgorithmException | IOException e) {
            System.err.println("Key generation error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}