package com.example.commons;

import java.io.Serializable;

import lombok.Data;
import reactor.core.publisher.Mono;

@Data
@SuppressWarnings("rawtypes")
public class ResponseInfo<T> implements Serializable {
	
	private static final Integer SUCCESS = 100;
    private static final Integer FAILED = 101;
    private static final Integer ERROR = 102;
    private static final Integer UNLOG = 103;
    private static final Integer NOAUTH = 104;
    private static final String MSG_OK = "Action OK";
    
    private String message;
    private Integer code;
    private Object result;
    private String url;
    
    private ResponseInfo() {
        this.code = SUCCESS;
        this.message = MSG_OK;
    }
    private ResponseInfo(T result) {
        this.code = SUCCESS;
        this.message = MSG_OK;
        this.result = result;
    }
    
    private ResponseInfo(String url, Boolean useUrl) {
        this.code = SUCCESS;
        this.message = MSG_OK;
        this.url = url;
    }

    private ResponseInfo(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private ResponseInfo(Integer code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }
    
    public static <T> ResponseInfo ok() {
        return new ResponseInfo<T>();
    }
    
    public static <T> ResponseInfo<T> ok(T result) {
        return new ResponseInfo<T>(result);
    }
    
    public static <T> ResponseInfo<T> url(String result) {
        return new ResponseInfo<T>(result, true);
    }

    public static <T> ResponseInfo ok(String message, T result) {
        return new ResponseInfo<T>(SUCCESS, message, result);
    }

    public static <T> ResponseInfo not(String message) {
        return new ResponseInfo<T>(FAILED, message);
    }

	public static <T> ResponseInfo not(String message, T result) {
        return new ResponseInfo<T>(FAILED, message, result);
    }
	
    
}