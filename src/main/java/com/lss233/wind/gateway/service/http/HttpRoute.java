package com.lss233.wind.gateway.service.http;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lss233.wind.gateway.common.Route;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HttpRoute extends Route implements Serializable {

    public static final long serialVersionUID = 1L;

    private String host;
    private String path;
    private List<MatchRule> matchRuleList;

    public HttpRoute() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<MatchRule> getMatchRuleList() {
        return matchRuleList;
    }

    public void setMatchRuleList(List<MatchRule> matchRuleList) {
        this.matchRuleList = matchRuleList;
    }
}
