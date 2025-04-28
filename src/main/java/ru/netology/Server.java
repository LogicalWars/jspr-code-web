package ru.netology;

import ru.netology.handlers.Handler;
import ru.netology.handlers.WebClientHandlerImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket;
    private static ConcurrentHashMap<String, ConcurrentHashMap<String, Handler>> handlers = new ConcurrentHashMap<>();

    public void listen(int port, int threads) {
        start(port);
        try (ExecutorService executorService = Executors.newFixedThreadPool(threads)) {
            try (final Socket clientSocket = serverSocket.accept()) {
                while (true) {
                    executorService.submit(() -> new WebClientHandlerImpl().handle(clientSocket, this));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                executorService.shutdown();
            }
        }
    }

    private void start(int port) {
        try {
            serverSocket = new ServerSocket();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addHandler(String method, String path, Handler handler) {
        if (!handlers.containsKey(method)) {
            handlers.put(method, new ConcurrentHashMap<String, Handler>());
        } else {
            handlers.get(method).put(path, handler);
        }
    }

    public ConcurrentHashMap<String, ConcurrentHashMap<String, Handler>> getHandlers() {
        return handlers;
    }
}
