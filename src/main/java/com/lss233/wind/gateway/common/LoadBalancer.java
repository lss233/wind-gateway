package com.lss233.wind.gateway.common;

import java.util.Collections;
import java.util.List;

public abstract class LoadBalancer {
    protected final List<Upstream.Destination> destinations;
    public LoadBalancer(List<Upstream.Destination> destinations) {
        this.destinations = Collections.unmodifiableList(destinations);
    }

    public abstract Upstream.Destination getLoadBalance();

    public List<Upstream.Destination> getDestinations() {
        return destinations;
    }
}
