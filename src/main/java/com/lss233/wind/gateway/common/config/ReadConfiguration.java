package com.lss233.wind.gateway.common.config;

/**
 * 读取Yaml配置文件.
 * Author: icebigpig
 * Data: 2022/5/13 16:05
 * Version 1.0
 **/

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ReadConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ReadConfiguration.class);

    public void readYamlConfiguration() {
        try {
            File f = new File("./config.yml");
            InputStream test = Files.newInputStream(f.toPath());

            Yaml yaml = new Yaml(new Constructor(ConfigEntity.class));
            Iterable<Object> its = yaml.loadAll(test);
            List<ConfigEntity> configEntityList = new ArrayList<>();
            for(Object it : its) {
                configEntityList.add((ConfigEntity) it);
            }
            logger.info(">>>>>>Config:{}", configEntityList);
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}