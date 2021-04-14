package com.httpclient.test;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HttpClinetSetProxyIp {
/**
 * 设置代理
 */
    @Test
    public void testSetProxyIp(){
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

        String url = "www.baidu.com";

        HttpGet httpGet = new HttpGet(url);
        String ip = "192.168.1.102";
        int port =9999;


        HttpHost proxy = new HttpHost(ip,port);

        RequestConfig build = RequestConfig.custom().setProxy(proxy).build();
        httpGet.setConfig(build);

        CloseableHttpResponse response=null;

        try {
            response = closeableHttpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            String toString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            System.out.println(toString);
            EntityUtils.consume(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (closeableHttpClient!=null){
                try {
                    closeableHttpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (response!=null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}

