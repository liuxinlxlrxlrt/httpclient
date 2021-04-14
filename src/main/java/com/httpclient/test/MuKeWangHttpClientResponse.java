package com.httpclient.test;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
/**
 * request param /HttpClientRequest
 * 1、url String
 * 2、httpMethod(HttpGet/HttpPost)HttpUriRequst
 * 3、httpHeaders Map<String,String>
 * 4、body String
 *
 * response param / HttpClientResponse
 * 1、statusCode String
 * 2、httpHeaders Map<String,String>
 * 3、body String
 */

public class MuKeWangHttpClientResponse {
    private String statusCode;
    private Map<String,String> headers = new HashMap<>();
    private String body;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
