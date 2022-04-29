package com.lss233.wind.gateway.service.consul.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Author: icebigpig
 * Data: 2022/4/29 19:22
 * Version 1.0
 **/
@Getter
@Setter
public class KeyValue {

    private String Key;

    private String Value;
    //Yaml文件字符串，需要进行反序列化解析

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
