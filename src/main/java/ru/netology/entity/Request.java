package ru.netology.entity;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Request {
    private final String method;
    private final String path;
    private final String httpVersion;
    private final String body;
    private final Map<String, List<String>> queryParams;

    public Request(String method, String path, String httpVersion, String body, Map<String, List<String>> queryParams) {
        this.method = method;
        this.path = path;
        this.httpVersion = httpVersion;
        this.body = body;
        this.queryParams = queryParams;
    }

    public Optional<String> getQueryParam(String name) {
        return Optional.ofNullable(queryParams.get(name))
                .filter(list -> !list.isEmpty())
                .map(List::getFirst);
    }

    public List<String> getQueryParams(String name) {
        return queryParams.getOrDefault(name, Collections.emptyList());
    }
}
