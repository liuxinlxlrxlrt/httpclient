package com.httpclient.test;

import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class HttpClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static final HttpClientBuilder httpClientBuilder = HttpClients.custom();

    {
        //1、绕过不安全的https请求的证书检验
        Registry<ConnectionSocketFactory> registry = null;
        try {
            registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", trusHttpsCertificates())
                    .build();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        //2、创建链接池
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);

        cm.setMaxTotal(50);//连接池最大有50个链接,<=20

        //ip+端口号=一个路由
        cm.setDefaultMaxPerRoute(50);//每个路由默认有多少个链接，<=2

/*
         //连接池的最大连接数
        System.out.println(cm.getMaxTotal());
         //每一个路由的最大连接数
        System.out.println(cm.getDefaultMaxPerRoute());
         //连接池的最大连接数
        PoolStats totalstats = cm.getTotalStats();
        System.out.println(totalstats.getMax());
         //连接池里面有有多少个链接被占用了
        System.out.println(totalstats.getLeased());
         //链接池里面有多少个链接可用
        System.out.println(totalstats.getAvailable());
*/

        httpClientBuilder.setConnectionManager(cm);

        //3、设置默认请求配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(3000)
                .setConnectionRequestTimeout(5000)
                .build();
        httpClientBuilder.setDefaultRequestConfig(requestConfig);

        //4、设置默认的一些header
        List<Header> headers = new ArrayList<>();
        BasicHeader userAgentHeader = new BasicHeader("User-Agent", "xxx");
        headers.add(userAgentHeader);
        httpClientBuilder.setDefaultHeaders(headers);

    }

    /**
     * 发送get请求
     */
    public static String executeGet(String url, Map<String, String> params, Map<String, String> header) {
        if (params != null) {
            //添加参数
            Set<Map.Entry<String, String>> parameters = params.entrySet();
            int mark = 1;

            for (Map.Entry<String, String> param : parameters) {
                if (mark == 1) {
                    url += "?" + param.getKey() + "=" + param.getValue();
                } else {
                    url += "&" + param.getKey() + "=" + param.getValue();
                }
            }
        }

        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();

        HttpGet httpGet = new HttpGet(url);
        //自定义请求头
        if (header != null) {
            Set<Map.Entry<String, String>> entries = header.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                httpGet.addHeader(new BasicHeader(entry.getKey(), entry.getValue()));
            }
        }

        CloseableHttpResponse response = null;

        try {
            response = closeableHttpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            if (HttpStatus.SC_OK == statusLine.getStatusCode()) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, StandardCharsets.UTF_8);
            } else {
                logger.error("响应失败，响应码：" + statusLine.getStatusCode());
            }
        } catch (IOException e) {
            logger.error("executeGet error,url:{}", url, e);
        } finally {
            HttpClientUtils.closeQuietly(response);

        }
        return null;
    }

    /**
     * 发送post表单形式的请求
     *
     * @param url
     * @param list
     * @param header
     * @return
     */
    public static String postForm(String url, List<NameValuePair> list, Map<String, String> header) {
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        //创建POST对象
        HttpPost httpPost = new HttpPost(url);
        //自定义请求头
        if (header != null) {
            Set<Map.Entry<String, String>> entries = header.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                httpPost.addHeader(new BasicHeader(entry.getKey(), entry.getValue()));
            }
        }


        //设置请求头
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //给POST对象设置参数
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(list, Consts.UTF_8);
        httpPost.setEntity(formEntity);


        CloseableHttpResponse response = null;

        try {
            response = closeableHttpClient.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            if (HttpStatus.SC_OK == statusLine.getStatusCode()) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, StandardCharsets.UTF_8);
            } else {
                logger.error("响应失败，响应码：" + statusLine.getStatusCode());
            }
        } catch (IOException e) {
            logger.error("executePost error,url:{}", url, e);
        } finally {
            HttpClientUtils.closeQuietly(response);

        }
        return null;
    }


    /**
     * 发送json的post请求
     */
    public static String postJson(String url, String body, Map<String, String> header) {
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        HttpPost httpPost = new HttpPost(url);
        //自定义请求头
        if (header != null) {
            Set<Map.Entry<String, String>> entries = header.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                httpPost.addHeader(new BasicHeader(entry.getKey(), entry.getValue()));
            }
        }

        //确保请求头是json类型
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
        //设置请求体
        StringEntity jsonEntity = new StringEntity(body, Consts.UTF_8);
        //也需要给Entity设置内容类型
        jsonEntity.setContentType("application/json;charset=utf-8");
        //设置entity编码
        jsonEntity.setContentEncoding(Consts.UTF_8.name());

        httpPost.setEntity(jsonEntity);


        CloseableHttpResponse response = null;

        try {
            response = closeableHttpClient.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            if (HttpStatus.SC_OK == statusLine.getStatusCode()) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, StandardCharsets.UTF_8);
            } else {
                logger.error("响应失败，响应码：" + statusLine.getStatusCode());
            }
        } catch (IOException e) {
            logger.error("executePost error,url:{}", url, e);
        } finally {
            HttpClientUtils.closeQuietly(response);

        }
        return null;
    }


    //创建支持安全协议的链接工厂
    private ConnectionSocketFactory trusHttpsCertificates() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
        //加载信任的证书
        sslContextBuilder.loadTrustMaterial(null, new TrustStrategy() {
            //判断是否信任url
            @Override
            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                return true;
            }
        });
        SSLContext sslContext = sslContextBuilder.build();
        SSLConnectionSocketFactory sslConnectionSocketFactory = new
                SSLConnectionSocketFactory(sslContext, new String[]{
                "SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"}
                , null, NoopHostnameVerifier.INSTANCE);
        return sslConnectionSocketFactory;
    }
}
