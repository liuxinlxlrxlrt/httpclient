package com.httpclient.test;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;

import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HttpClientJsonPost {
    /**
     * 发送"application/json"类型POST请求
     */
    @Test
    public void testJsonPost(){
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

        String url = "https://github.com/";

        //创建POST对象
        HttpPost httpPost = new HttpPost(url);

        //确保请求头是json类型
        httpPost.addHeader("Content-Type","application/json;charset=utf-8");
        //string 是一个json
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nuserName","liuxin");
        jsonObject.put("password","123456");

        //设置请求体
        StringEntity jsonEntity = new StringEntity(jsonObject.toString(),Consts.UTF_8);
        //也需要给Entity设置内容类型
        jsonEntity.setContentType(new BasicHeader("Content-type","application/json;charset=utf-8"));
//        jsonEntity.setContentType("application/json;charset=utf-8"));
       //设置entity编码
        jsonEntity.setContentEncoding(Consts.UTF_8.name());

        httpPost.setEntity(jsonEntity);

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
