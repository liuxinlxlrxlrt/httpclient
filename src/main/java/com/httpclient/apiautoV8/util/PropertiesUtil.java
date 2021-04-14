package com.httpclient.apiautoV8.util;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    /**
     * 读取jdbc.properties文件
     */
    private static Properties jdbcProp;

    public static Properties getJdbcProp(){
        try {
            if(jdbcProp == null){
                jdbcProp =new Properties();
                jdbcProp.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("Properties/jdbc.properties"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jdbcProp;
    }
}
