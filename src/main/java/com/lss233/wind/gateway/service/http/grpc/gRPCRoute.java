package com.lss233.wind.gateway.service.http.grpc;

import com.lss233.wind.gateway.common.Route;

import java.net.URI;

public class gRPCRoute extends Route {
    private URI uri;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }
}
