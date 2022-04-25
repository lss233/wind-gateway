package com.lss233.wind.gateway.service.http;

import com.lss233.wind.gateway.Route;

import java.net.URI;

public class HttpRoute extends Route {
    protected URI uri;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }
}
