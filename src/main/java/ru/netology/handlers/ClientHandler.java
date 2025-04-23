package ru.netology.handlers;

import java.net.Socket;

public interface ClientHandler {
    void handle(Socket socket);
}
