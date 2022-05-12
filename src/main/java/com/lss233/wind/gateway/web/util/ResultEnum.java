package com.lss233.wind.gateway.web.util;

/**
 * @author zzl
 * @date 2022/5/9 19:28
 */
public enum ResultEnum{

    //成功，未登录，失败使用的状态码。
    SUCCESS(200,"操作成功"),
    NOT_LOGIN(401,"未登录"),
    NOT_FOUND(404, "未找到该资源"),
    ERROR(500,"系统异常");

    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
