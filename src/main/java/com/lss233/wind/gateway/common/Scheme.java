package com.lss233.wind.gateway.common;


import java.util.*;

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
