package com.lss233.wind.gateway.service.http.grpc;

import com.lss233.wind.gateway.common.Route;
import com.lss233.wind.gateway.service.http.DomainMatch;
import com.lss233.wind.gateway.service.http.PathMatch;

import java.io.Serializable;
import java.net.URI;
import java.util.List;

public class gRPCRoute extends Route implements Serializable {
    public static final long serialVersionUID = 1L;

    private List<String> host;
    private List<String> path;

    List<PathMatch> pathMatch;
    List<DomainMatch> DomainMath;

    public gRPCRoute() {
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

    public List<DomainMatch> getDomainMath() {
        return DomainMath;
    }

    public void setDomainMath(List<DomainMatch> domainMath) {
        DomainMath = domainMath;
    }
}
