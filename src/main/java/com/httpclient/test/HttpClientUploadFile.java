package com.httpclient.test;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HttpClientUploadFile {
    /**
     * 发送multpart/from-data类型上传文件的请求
     * 文件上传+普通表单字段的传递功能
     */
    @Test
    public void testUploadFile(){
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

        String url = "https://github.com/";

        //创建POST对象
        HttpPost httpPost = new HttpPost(url);

        //构造一个ContentBody的实现类对象
        FileBody fileBody = new FileBody(new File("D:\\test\\a.txt"));

        //构造上传文件使用的entity
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setCharset(Consts.UTF_8);//设置编码
        multipartEntityBuilder.setContentType(ContentType.create("multpart/from-data",Consts.UTF_8));

        //对于普通的表单字段如果含有中文的话，不能通过addTextBody，否则乱码
        //text指的是输入的值
        StringBody stringBody = new StringBody("小明",ContentType.create(
                "text/plain",Consts.UTF_8));

        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器模式
        HttpEntity build = multipartEntityBuilder.addPart("fileName", fileBody)
                //通过file。byte[]，inputstreams上传文件
                .addBinaryBody("fileName", new File("D:\\test\\123.png"))
//   //            .addTextBody("userName", "小明")不用这个
                .addPart("userName",stringBody)
                .addTextBody("password", "123456")
                .build();

        httpPost.setEntity(build);

        CloseableHttpResponse response = null;

        try {
            response = closeableHttpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();

            String toString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            System.out.println(toString);

            EntityUtils.consume(entity);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (closeableHttpClient != null) {
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

