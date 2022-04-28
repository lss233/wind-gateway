package com.lss233.wind.gateway.common.lb;

import com.lss233.wind.gateway.common.LoadBalancer;
import com.lss233.wind.gateway.common.Upstream;

import java.util.List;
import java.util.Random;

public class RandomLoadBalancer extends LoadBalancer {
    private final Random random = new Random();
    public RandomLoadBalancer(List<Upstream.Destination> destinations) {
        super(destinations);
    }

    @Override
    public Upstream.Destination getLoadBalance() {
        int sum = 0;
        for(Upstream.Destination destination : getDestinations()) {
            if(destination.isOnline()) {
                sum += destination.getWeight();
            }
        }
        if (sum == 0) {
            return null;
        }
        int randval = random.nextInt(sum);
        sum = 0;
        for(Upstream.Destination destination : getDestinations()) {
            if(destination.isOnline()) {
                sum += destination.getWeight();
                if(sum >= randval) {
                    return destination;
                }
            }
        }
        return null;
    }
}
