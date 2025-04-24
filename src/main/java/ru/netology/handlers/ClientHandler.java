package ru.netology.handlers;

import ru.netology.Server;

import java.net.Socket;

public interface ClientHandler {
    void handle(Socket socket, Server server);
}
