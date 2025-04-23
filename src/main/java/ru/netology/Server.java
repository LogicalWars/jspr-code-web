package ru.netology;

import ru.netology.handlers.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final int PORT;
    private ServerSocket serverSocket;

    public Server(int port) {
        PORT = port;
    }

    public Server start() {
        try {
            serverSocket = new ServerSocket(PORT);
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startListening(int threads, ClientHandler clientHandler) {
        try (ExecutorService executorService = Executors.newFixedThreadPool(threads)) {
            try (final Socket clientSocket = serverSocket.accept()) {
                while (true) {
                    executorService.submit(() -> clientHandler.handle(clientSocket));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                executorService.shutdown();
            }
        }
    }
}
