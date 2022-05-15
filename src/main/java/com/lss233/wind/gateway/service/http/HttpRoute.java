package com.lss233.wind.gateway.service.http;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lss233.wind.gateway.common.Route;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HttpRoute extends Route implements Serializable {

    public static final long serialVersionUID = 1L;

    private List<String> host;
    private List<String> path;

    List<PathMatch> pathMatch;
    List<DomainMatch> domainMatch;

    public HttpRoute() {
    }

    public List<String> getHost() {
        return host;
    }

    public void setHost(List<String> host) {
        this.host = host;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    public List<PathMatch> getPathMatch() {
        return pathMatch;
    }

    public void setPathMatch(List<PathMatch> pathMatch) {
        this.pathMatch = pathMatch;
    }

    public List<DomainMatch> getDomainMatch() {
        return domainMatch;
    }

    public void setDomainMatch(List<DomainMatch> domainMatch) {
        this.domainMatch = domainMatch;
    }
}
