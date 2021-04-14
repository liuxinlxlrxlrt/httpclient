package com.httpclient.test;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HttpClientTimeOut {
    /**
     * 连接超时和读取超时
     */
   @Test
    public void testTimeOut(){
       CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

       String url = "https://maven.apache.org/";

       HttpGet httpGet = new HttpGet(url);

       RequestConfig build = RequestConfig.custom()
               //设置连接超时，ms，完成tcp三次握手上限
               .setConnectTimeout(5000)
               //读取超时，ms，表示从请求网址出获取响应数据的时间间隔
               .setSocketTimeout(3000)
               //从连接池获取connection超时时间
                .setConnectionRequestTimeout(5000)
               .build();

       CloseableHttpResponse response = null;

       try {
           response=closeableHttpClient.execute(httpGet);

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
