package com.lss233.wind.gateway.common;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.*;

@JsonDeserialize(converter = SchemeDeserializer.class)
public interface Scheme {
    /**
     * 协议名称
     * @return 名称
     */
    String getName();

    /**
     * 协议别名
     * @return 别名
     */
    List<String> getAliases();
}
