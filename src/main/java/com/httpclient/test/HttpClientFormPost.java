package com.httpclient.test;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HttpClientFormPost {
    /**
     * 发送"application/x-www-form-urlencoded"表单形式POST请求
     * enctype: 表单数据提交时使用的编码类型，
     * 默认使用"application/x-www-form-urlencoded"，
     * 如果是使用POST请求，则请求头中的content-type指定值就是该值。
     * 如果表单中有上传文件，编码类型需要使用"multipart/form-data"，类型，才能完成传递文件数据。
     */
    @Test
    public void testFormPost(){
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

        String url = "https://github.com/";

        //创建POST对象
        HttpPost httpPost = new HttpPost(url);
        //设置请求头
        httpPost.setHeader("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        //给POST对象设置参数
        //NameValuePair
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("userName","liuxin"));
        list.add(new BasicNameValuePair("password","123456"));
        //把参数集合设置到formEntity
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(list, Consts.UTF_8);
        httpPost.setEntity(formEntity);

        CloseableHttpResponse response = null;

        try {
            response=closeableHttpClient.execute(httpPost);

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
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
