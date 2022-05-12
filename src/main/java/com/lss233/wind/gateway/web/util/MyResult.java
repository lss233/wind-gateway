package com.lss233.wind.gateway.web.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * @author zzl
 * @date 2022/5/9 19:20
 */
public class MyResult<T> implements Serializable {

    private static final long serialVersionUID = 4580737268023862568L;

    private Integer code;

    private String msg;

    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public MyResult() {
    }

    //成功
    public static <T> MyResult<T> success(){
        return success(ResultEnum.SUCCESS.getMsg(), null);
    }
    public static <T> MyResult<T> success(T data){
        return success(ResultEnum.SUCCESS.getMsg(), data);
    }
    public static <T> MyResult<T> success(String msg, T data){
        MyResult<T> result = new MyResult<>();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg(msg);
        result.setData(data);
        return result;
    }



    //失败
    public static <T> MyResult<T> fail(ResultEnum resultEnum){
        return fail(resultEnum.getCode(), resultEnum.getMsg(), null);
    }
    public static <T> MyResult<T> fail(ResultEnum resultEnum,T data){
        return fail(resultEnum.getCode(),resultEnum.getMsg() ,data);
    }
    public static <T> MyResult<T> fail(Integer code , String msg, T data){
        MyResult<T> result = new MyResult<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }


}
