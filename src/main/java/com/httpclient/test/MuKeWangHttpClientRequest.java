package com.httpclient.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.client.methods.HttpUriRequest;

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

public class MuKeWangHttpClientRequest {
    private String url;

    private HttpUriRequest httpMethod;

    private Map<String,String> headers = new HashMap<>();

    private String body;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpUriRequest getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpUriRequest httpMethod) {
        this.httpMethod = httpMethod;
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
