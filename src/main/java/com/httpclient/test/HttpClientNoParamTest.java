package com.httpclient.test;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import java.io.IOException;

import java.nio.charset.StandardCharsets;

public class HttpClientNoParamTest {

    /**
     * 使用HttpClient
     * 1、无参get请求
     * 2、加请求头：
     * 3、什么时候使用请求头？
     * 使用HttpClient一直请求某一个网站，网站安全性做得好，提示系统不是真人行为，拒绝访问
     */
    @Test
    public void testNoParam(){
        //1、可关闭的HttpClients客户端，相当于你打开一个浏览器
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

        String url = "http://www.baidu.com/";

        // 2 创建httpGet对象，相当于设置url请求地址
        HttpGet httpGet  = new HttpGet(url);
        //2.1添加请求头:解决HttpClient被认为不是真人行为
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36");
//        //2.2，防盗链:value要是发生防盗链的网址url
//        httpGet.setHeader("Referer","http://www.baidu.com/");

        CloseableHttpResponse httpResponse = null;

        try {
            //3、使用HttpClient执行httpGet
            httpResponse = closeableHttpClient.execute(httpGet);

            //获取请求成功、失败的状态
            StatusLine statusLine = httpResponse.getStatusLine();
            if (HttpStatus.SC_OK == statusLine.getStatusCode()) {
                System.out.println("响应成功");

                //获取响应头
                Header[] allHeaders = httpResponse.getAllHeaders();
                for (Header header : allHeaders) {
                    System.out.println("响应头：" + header.getName() + "的值：" + header.getValue());
                }

                //3.1 HttpEntity获取响应结果
                HttpEntity entity = httpResponse.getEntity();

                //响应的content-Type
                System.out.println("ContentType的值是：" + entity.getContentType());

                //3.2 对HttpEntity操作工具类
                String reesultToString = EntityUtils.toString(entity, StandardCharsets.UTF_8);

                System.out.println("返回值为：" + reesultToString);

                //3.3 确保关闭流
                EntityUtils.consume(entity);
            } else {
                System.out.println("响应失败，响应码是：" + statusLine.getStatusCode());
            }
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
            if (httpResponse!=null){
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
