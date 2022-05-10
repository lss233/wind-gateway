package com.lss233.wind.gateway.service.http;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lss233.wind.gateway.common.Route;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HttpRoute extends Route {

    protected String path;
    protected String host;
    protected String uri;

    public HttpRoute() {
        uri = host + path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUri() {
        return uri;
    }
}
