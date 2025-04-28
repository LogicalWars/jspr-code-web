package ru.netology;

import ru.netology.entity.Request;
import ru.netology.handlers.Handler;

import java.io.BufferedOutputStream;

public class Main {

    private static final int SERVER_PORT = 8080;
    private static final int COUNT_THREADS = 64;

    public static void main(String[] args) {
        Server server = new Server();

        server.addHandler("GET", "/index.html", new Handler() {
            public void handle(Request request, BufferedOutputStream responseStream) {
                // TODO: handlers code
            }
        });
        server.addHandler("POST", "/spring.svg", new Handler() {
            public void handle(Request request, BufferedOutputStream responseStream) {
                // TODO: handlers code
            }
        });

        server.listen(SERVER_PORT, COUNT_THREADS);

    }
}


