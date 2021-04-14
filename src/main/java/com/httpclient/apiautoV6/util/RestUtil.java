package com.httpclient.apiautoV6.util;

import com.httpclient.apiautoV6.pojo.Rest;

import java.util.ArrayList;
import java.util.List;

public class RestUtil {
    public static List<Rest>  rests =new ArrayList<>();

    static {
        String casePath = "src/main/resources/case_v4.xls";
        String sheetName1 = "Sheet1";
        //将所有数据解析封装到cases集合对象中
        ExcelUtils.load(casePath, sheetName1,Rest.class);
    }

    /**
     * 根据接口编号获取接口地址
     * @param apiId
     * @return
     */
    public static String getUrlByApiId(String apiId){
        for (Rest rest : rests) {
            if (rest.getApiId().equals(apiId)){
                return rest.getUrl();
            }
            
        }
        return "";
    }

    /**
     * 根据接口编号获取接口类型
     * @param apiId
     * @return
     */
    public static String getTypeByApiId(String apiId){
        for (Rest rest : rests) {
            if (rest.getApiId().equals(apiId)){
                return rest.getType();
            }

        }
        return "";
    }

//    public static void main(String[] args) {
//        for (Rest rest : rests) {
//            System.out.println(rest);
//        }
//    }
}
