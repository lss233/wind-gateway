package com.lss233.wind.gateway.service.consul.Cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: icebigpig
 * Data: 2022/5/14 21:35
 * Version 1.0
 **/

public class ScheduledTasks implements Runnable{

    private final static Logger LOG = LoggerFactory.getLogger(ScheduledTasks.class);

    public void run() {

        /**
         * 定时刷新读取缓存
         */
        while (true) {
            try {

                HttpRouteCache.updateCache();
                UpstreamCache.updateCache();
                DomainMatchCache.updateCache();
                PathMatchCache.updateCache();

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            LOG.info("Update Cache");
            try {
                Thread.sleep(100 * 50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}