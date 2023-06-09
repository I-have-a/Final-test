package com.niuma.questionnaire.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 一个通用的返回结果类，服务端响应的数据最终都会封装成此对象
 *
 * @param <T>
 */
@Data
public class R<T> implements Serializable {
    private static final long serialVersionUID = 5073771109732335840L;
    private Integer code; //编码

    private String msg; //提示信息

    private T data; //数据

    public static <T> R<T> success(T object, Integer code, String msg) {
        R<T> r = new R<>();
        r.data = object;
        r.code = code;
        r.msg = msg;
        return r;
    }

    public static <T> R<T> success(Integer code, String msg) {
        R<T> r = new R<>();
        r.code = code;
        r.msg = msg;
        return r;
    }

    public static <T> R<T> error(Integer code, String msg) {
        R<T> r = new R<>();
        r.msg = msg;
        r.code = code;
        return r;
    }
}
