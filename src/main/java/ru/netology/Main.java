package ru.netology;

import ru.netology.handlers.ClientHandlerImpl;
import java.util.List;

public class Main {

    private static final int SERVER_PORT = 8080;
    private static final int COUNT_THREADS = 64;

    public static void main(String[] args) {
        final List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");
        new Server(SERVER_PORT).start().startListening(COUNT_THREADS, new ClientHandlerImpl(validPaths));

    }
}


