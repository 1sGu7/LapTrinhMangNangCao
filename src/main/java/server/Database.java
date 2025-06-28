package server;

import java.sql.*;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:publicKeys.db";

    public static void initialize() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS public_keys (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "key_data BLOB NOT NULL," +
                    "aes_key BLOB NOT NULL," +
                    "iv BLOB NOT NULL," +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP);";
            stmt.execute(sql);

            System.out.println("[Database] Initialized successfully");
        } catch (SQLException e) {
            System.err.println("[Database] Initialization error: " + e.getMessage());
        }
    }

    public static void saveKeyInfo(byte[] keyData, byte[] aesKey, byte[] iv) {
        String sql = "INSERT INTO public_keys(key_data, aes_key, iv) VALUES(?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBytes(1, keyData);
            pstmt.setBytes(2, aesKey);
            pstmt.setBytes(3, iv);
            pstmt.executeUpdate();

            System.out.println("[Database] Key info saved");
        } catch (SQLException e) {
            System.err.println("[Database] Error saving key info: " + e.getMessage());
        }
    }
}