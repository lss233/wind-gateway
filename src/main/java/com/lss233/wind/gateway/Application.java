package com.lss233.wind.gateway;

import com.lss233.wind.gateway.service.http.HttpBootstrap;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
    private final static Logger LOG = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args) throws Exception {
        LoggerFactory.getLogger("GWBanner").info("Start blowing...\n" +
                "\n" +
                "m     m   \"               #           mmm m     m\n" +
                "#  #  # mmm    m mm    mmm#         m\"   \"#  #  #\n" +
                "\" #\"# #   #    #\"  #  #\" \"#         #   mm\" #\"# #\n" +
                " ## ##\"   #    #   #  #   #         #    # ## ##\"\n" +
                " #   #  mm#mm  #   #  \"#m##          \"mmm\" #   #\n");
        LOG.info("Loading services...");
        HttpBootstrap bootstrap  = new HttpBootstrap();
        bootstrap.start();
    }
}
