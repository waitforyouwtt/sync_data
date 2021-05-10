package com.yh.utils;

import lombok.Data;

@Data
public class Result<T> {

    private Integer code;
    private String message;
    private T data;

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result() {}

    public static <T> Result<T> success(T data) {
        Result<T> response = new Result();
        response.setCode(200000);
        response.setMessage("success");
        response.setData(data);
        return response;
    }
    public static <T> Result<T> success() {
        Result<T> response = new Result();
        response.setCode(200000);
        response.setMessage("success");
        response.setData(null);
        return response;
    }
    public static <T> Result<T> error(T data) {
        Result<T> response = new Result();
        response.setCode(500000);
        response.setMessage("error");
        response.setData(data);
        return response;
    }

    public static <T> Result<T> error() {
        Result<T> response = new Result();
        response.setCode(500000);
        response.setMessage("error");
        response.setData(null);
        return response;
    }

    public static <T> Result<T> error(Integer code,String message) {
        Result<T> response = new Result();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}
