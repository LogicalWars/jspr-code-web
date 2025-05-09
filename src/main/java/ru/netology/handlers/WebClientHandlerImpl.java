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
import java.util.stream.Collectors;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URLEncodedUtils;
import java.util.List;
import java.nio.charset.StandardCharsets;

public class WebClientHandlerImpl implements ClientHandler {

    @Override
    public void handle(Socket clientSocket, Server server) {
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             final BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream())) {

            final String requestLine = in.readLine();
            final String[] partsOfRequestLine = requestLine.split(" "); // GET /index.html?param=value HTTP/1.1
            if (partsOfRequestLine.length != 3) {
                clientSocket.close();
                return;
            }

            final String method = partsOfRequestLine[0];
            final String fullPath = partsOfRequestLine[1];
            final String httpVersion = partsOfRequestLine[2];

            String path;
            Map<String, List<String>> queryParams = new HashMap<>();

            int queryStart = fullPath.indexOf('?');
            path = queryStart == -1 ? fullPath : fullPath.substring(0, queryStart);

            if (queryStart != -1) {
                String queryString = fullPath.substring(queryStart + 1);
                List<NameValuePair> params = URLEncodedUtils.parse("?" + queryString, StandardCharsets.UTF_8);

                queryParams = params.stream()
                        .collect(Collectors.groupingBy(
                                NameValuePair::getName,
                                Collectors.mapping(NameValuePair::getValue, Collectors.toList())
                        ));
            }

            Map<String, String> headers = new HashMap<>();
            String header;
            while (!(header = in.readLine()).isEmpty()) {
                String[] partsHeader = header.split(":", 2);
                if (partsHeader.length == 2) {
                    headers.put(partsHeader[0].trim(), partsHeader[1].trim());
                }
            }

            String body = null;
            if (headers.containsKey("Content-Length")) {
                int contentLength = Integer.parseInt(headers.get("Content-Length"));
                char[] bodyChars = new char[contentLength];
                in.read(bodyChars, 0, contentLength);
                body = new String(bodyChars);
            }

            if (!server.getHandlers().containsKey(method) ||
                    !server.getHandlers().get(method).containsKey(path)) {

                out.write((
                        "HTTP/1.1 404 Not Found\r\n" +
                                "Content-Length: 0\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                out.flush();
                clientSocket.close();
                return;
            }

            Request request = new Request(method, path, httpVersion, body, queryParams);

            server.getHandlers().get(method).get(path).handle(request, out);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}