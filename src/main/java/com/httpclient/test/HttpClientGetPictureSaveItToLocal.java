package com.httpclient.test;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import java.io.FileOutputStream;
import java.io.IOException;

public class HttpClientGetPictureSaveItToLocal {
    /**
     * 获取网络图片并且保存到本地
     */
    @Test
    public void testPictureSaveItToLocal(){
        //1、可关闭的HttpClients客户端，相当于你打开一个浏览器
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        //http://www.roadjava.com/
        String url ="http://www.roadjava.com/uploads/imgs/2021/01/23581493830814400_httpclient.png";

        // 2 创建httpGet对象，相当于设置url请求地址
        HttpGet httpGet  = new HttpGet(url);


        CloseableHttpResponse httpResponse = null;

        try {
            //3、使用HttpClient执行httpGet
            httpResponse = closeableHttpClient.execute(httpGet);

            //获取请求成功、失败的状态
            StatusLine statusLine = httpResponse.getStatusLine();
            if (HttpStatus.SC_OK == statusLine.getStatusCode()) {
                System.out.println("响应成功");

                //3.1 HttpEntity获取响应结果
                HttpEntity entity = httpResponse.getEntity();

                //判断图片的格式:imgs/jpg\jpeg\png,imgs/图片的后缀
                String contentType = entity.getContentType().getValue();
                System.out.println("contentType是："+contentType);

                String suffix = ".jpg";
                if (contentType.contains("jpg")||contentType.contains("jpeg")){
                    suffix=".jpg";
                }else if(contentType.contains("bmp")||contentType.contains("bitmap")){
                    suffix=".bmp";
                }else if(contentType.contains("png")){
                   suffix=".png";
                }else if(contentType.contains("gif")){
                    suffix=".gif";
                }
                //获取文件的字节流
                byte[] bytes = EntityUtils.toByteArray(entity);

                //定义本地保存图片的绝对路径
                String localPath="D:\\test\\123"+suffix;
                FileOutputStream fos = new FileOutputStream(localPath);
                fos.write(bytes);
                fos.close();

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
