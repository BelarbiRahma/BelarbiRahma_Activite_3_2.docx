package dartp3act2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class CalculatorServer {
    private int port;
    private AtomicInteger totalOperations = new AtomicInteger(0);

    public CalculatorServer(int port) {
        this.port = port;
    }

    public void start() {
        System.out.println("CalculatorServer starting on port " + port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Waiting for clients...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket.getRemoteSocketAddress());
                ClientHandler handler = new ClientHandler(clientSocket, totalOperations);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 5000;
        if (args.length >= 1) {
            try { port = Integer.parseInt(args[0]); } catch (NumberFormatException ignored) {}
        }
        new CalculatorServer(port).start();
    }
}

