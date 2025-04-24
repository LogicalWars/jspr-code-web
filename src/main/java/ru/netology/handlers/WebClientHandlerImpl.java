package ru.netology.handlers;

import ru.netology.entity.Request;
import ru.netology.Server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class WebClientHandlerImpl implements ClientHandler {
    @Override
    public void handle(Socket clientSocket, Server server) {
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             final BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream())) {

            final String requestLine = in.readLine();
            final String[] partsOfRequestLine = requestLine.split(" "); // GET /index.html HTTP/1.1
            final String method = partsOfRequestLine[0];
            final String endpoints = partsOfRequestLine[1];
            final String httpVersion = partsOfRequestLine[2];

            if (partsOfRequestLine.length != 3) {
                clientSocket.close();
            }

            Map<String, String> headers = new HashMap<>();
            String header;
            while ((header = in.readLine()) != null && header != null) {
                String[] partsHeader = requestLine.split(":");
                headers.put(partsHeader[0], partsHeader[1]);
            }

            String body = null;
            if (headers.containsKey("Content-Length")) {
                int contentLength = Integer.parseInt(headers.get("Content-Length"));
                char[] bodyChars = new char[contentLength];
                in.read(bodyChars, 0, contentLength);
                body = new String(bodyChars);
            }

            if (!server.getHandlers().containsKey(method) || !server.getHandlers().get(method).containsKey(endpoints)) {
                out.write((
                        "HTTP/1.1 404 Not Found\r\n" +
                                "Content-Length: 0\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                out.flush();
                clientSocket.close();
            }

            Request request = new Request(method, endpoints, httpVersion, body);

            server.getHandlers().get(method).get(endpoints).handle(request, out);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
