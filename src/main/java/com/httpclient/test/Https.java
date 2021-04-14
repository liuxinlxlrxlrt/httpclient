package com.httpclient.test;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class Https {
    /**
     * HttpClient如何绕过https的安全认证？
     * https是收费的，如果不是官方认证的证书，或者https过期，就会提示不安全的链接
     * 会报错：ssl.SSHHandshakeException
     * <p>
     * 解决办法：
     * 1、通过认证需要的秘钥配置httpClient
     * 2、配置httpClient绕过https安全认证
     * <p>
     * 如果绕过认证呢？
     * 定制CloseableHttpClient对象
     */
    @Test
    public void testNoParam() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", trusHttpsCertificates())
                .build();
        //创建ConnectionManager对象
        PoolingHttpClientConnectionManager poolConnectionManager = new PoolingHttpClientConnectionManager(registry);
        //定制CloseableHttpClient对象
        HttpClientBuilder httpClientBuilder = HttpClients.custom().setConnectionManager(poolConnectionManager);

        //配置好httpClient之后，通过build方法创建HttpClient对象
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();

        String url = "http://www.baidu.com/";

        // 2 创建httpGet对象，相当于设置url请求地址
        HttpGet httpGet = new HttpGet(url);
        //2.1添加请求头:解决HttpClient被认为不是真人行为
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36");
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
        } finally {
            if (closeableHttpClient != null) {
                try {
                    closeableHttpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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

