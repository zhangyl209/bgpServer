package com.example;

public class MyHttpResponse {
    private int statusCode;
    private String body;
    private String error;

    // 构造方法（成功）
    public MyHttpResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    // 构造方法（失败）
    public MyHttpResponse(int statusCode, String error, boolean isError) {
        this.statusCode = statusCode;
        this.error = error;
    }

    // Getter 方法
    public int getStatusCode() { return statusCode; }
    public String getBody() { return body; }
    public String getError() { return error; }

    // 判断是否成功
    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 300;
    }
}