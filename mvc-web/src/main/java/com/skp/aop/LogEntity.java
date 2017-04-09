package com.skp.aop;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class LogEntity {

    private String ip;
    private Long   starttime;
    private String requestUrl;
    private String classMethod;
    private String requestBody;
    private String responseBody;
    private int    runtime;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getStarttime() {
        return starttime;
    }

    public void setStarttime(Long starttime) {
        this.starttime = starttime;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getClassMethod() {
        return classMethod;
    }

    public void setClassMethod(String classMethod) {
        this.classMethod = classMethod;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public LogEntity() {
        super();
    }

    public LogEntity(String ip, Long starttime, String requestUrl, String classMethod, String requestBody) {
        super();
        this.ip = ip;
        this.starttime = starttime;
        this.requestUrl = requestUrl;
        this.classMethod = classMethod;
        this.requestBody = requestBody;
    }

    public LogEntity(String ip, Long starttime, String requestUrl, String classMethod, String requestBody, String responseBody, int runtime) {
        super();
        this.ip = ip;
        this.starttime = starttime;
        this.requestUrl = requestUrl;
        this.classMethod = classMethod;
        this.requestBody = requestBody;
        this.responseBody = responseBody;
        this.runtime = runtime;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
