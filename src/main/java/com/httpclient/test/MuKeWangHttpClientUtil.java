package com.httpclient.test;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import java.io.IOException;

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
public class MuKeWangHttpClientUtil {

    private static Object MuKeWangHttpClientUtil;
    private final static Logger logger = Logger.getLogger((Class) MuKeWangHttpClientUtil);

    public void testMethodGet() {
        CloseableHttpClient request = HttpClientBuilder.create().build();
        String url = "http://localhost:8888/v1/getUserInfo";
        HttpGet httpGet = new HttpGet(url);
        //增加requst header
        httpGet.setHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        CloseableHttpResponse response = null;
        try {
            response = request.execute(httpGet);
            //打印响应码
            logger.info(response.getStatusLine().toString().split(" ")[1]);
            //获取respose的header
            Header[] headers = response.getAllHeaders();
            for (Header header : headers) {
                logger.info(header.getName() + ":" + header.getValue());
            }

            //获取实体
            HttpEntity entity = response.getEntity();
            String body = IOUtils.toString(entity.getContent());
            logger.info(body);
            request.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testMethodPost(){
        CloseableHttpClient request = HttpClientBuilder.create().build();
        String url = "http://localhost:8888/v1/getUserInfo";
        HttpPost httpPost = new HttpPost(url);
        //增加requst header
        httpPost.setHeader("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        //设置请求体
        StringEntity stringEntity = new StringEntity("123", Consts.UTF_8);
        //正价request body
        httpPost.setEntity(stringEntity);
        CloseableHttpResponse response = null;
        try {
            response=request.execute(httpPost);
            //打印响应码
            logger.info(response.getStatusLine().toString().split(" ")[1]);
            //获取respose的header
            Header[] headers = response.getAllHeaders();
            for(Header header:headers){
                logger.info(header.getName()+":"+header.getValue());
            }

            //获取实体
            HttpEntity entity = response.getEntity();
            String body = IOUtils.toString(entity.getContent());
            logger.info(body);
            request.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
