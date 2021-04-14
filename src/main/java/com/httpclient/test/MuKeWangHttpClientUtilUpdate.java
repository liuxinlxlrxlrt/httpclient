package com.httpclient.test;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
public class MuKeWangHttpClientUtilUpdate {

    private static Logger logger = Logger.getLogger(MuKeWangHttpClientUtilUpdate.class);

    public void testMethodGet() {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String url = "http://localhost:8888/v1/getUserInfo";
        HttpGet httpGet = new HttpGet(url);
        //增加requst header
        httpGet.setHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            //打印响应码
            logger.info(httpResponse.getStatusLine().toString().split(" ")[1]);
            //获取respose的header
            Header[] headers = httpResponse.getAllHeaders();
            for (Header header : headers) {
                logger.info(header.getName() + ":" + header.getValue());
            }

            //获取实体
            HttpEntity entity = httpResponse.getEntity();
            String body = IOUtils.toString(entity.getContent());
            logger.info(body);
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public MuKeWangHttpClientResponse doPost(MuKeWangHttpClientRequest request) throws UnsupportedEncodingException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String url = request.getUrl();
        HttpUriRequest httpPost = new HttpPost(url);
        //增加requst header
        Map<String,String> requstHeaders = request.getHeaders();
        for(String key:requstHeaders.keySet()){
            httpPost.setHeader(key,requstHeaders.get(key));
        }

        //设置请求体
        ((HttpPost) httpPost).setEntity(new StringEntity(request.getBody()));
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse=httpClient.execute(httpPost);
            //打印响应码
            logger.info(httpResponse.getStatusLine().toString().split(" ")[1]);
            httpResponse.getStatusLine().toString();
            //获取respose的header
            Header[] headers = httpResponse.getAllHeaders();
            for(Header header:headers){
                logger.info(header.getName()+":"+header.getValue());
            }

            //获取实体
            HttpEntity entity = httpResponse.getEntity();
            String body = IOUtils.toString(entity.getContent());
            logger.info(body);
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (MuKeWangHttpClientResponse) httpResponse;
    }


}
