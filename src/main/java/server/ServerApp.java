package server;

public class ServerApp {
    public static void main(String[] args) {
        // Initialize database
        System.out.println("Initializing server...");
        Database.initialize();

        // Start server socket handler
        ServerSocketHandler server = new ServerSocketHandler(12345);
        server.start();
    }
}