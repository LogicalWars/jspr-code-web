package ru.netology.handlers;

import ru.netology.entity.Request;

import java.io.BufferedOutputStream;

public interface Handler {
    void handle(Request request, BufferedOutputStream responseStream);
}