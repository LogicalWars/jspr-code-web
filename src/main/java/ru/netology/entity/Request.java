package ru.netology.entity;

public class Request {
    private final String method;
    private final String endpoint;
    private final String versionHttp;
    private final String body;

    public Request(String method, String endpoint, String versionHttp, String body) {
        this.method = method;
        this.endpoint = endpoint;
        this.versionHttp = versionHttp;
        this.body = body;
    }
}
