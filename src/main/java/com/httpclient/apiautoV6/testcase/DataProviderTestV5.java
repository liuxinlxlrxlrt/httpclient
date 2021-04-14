package com.httpclient.apiautoV6.testcase;

import com.alibaba.fastjson.JSONObject;
import com.httpclient.apiautoV6.util.ExcelUtils;
import com.httpclient.apiautoV6.util.HttpClientUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 实现读取接口用例的的数据
 */
public class DataProviderTestV5 {
    String path = "src/main/resources/login.xls";
    String caasePath = "src/main/resources/case_v3.xls";
    String sheetName = "Sheet4";

    @DataProvider(name = "test01")
    public Object[][] datas1() {
        int[] rows = {2, 3, 4, 5, 6, 7};
        int[] cells = {7, 8};
        Object[][] dates = ExcelUtils.getDataByArray(path, sheetName, rows, cells);
        return dates;
//        return  new  Object[][] {
//                {"shunfeng", " "},
//                {" ", "362847643154"},
//                {"shun", "362847643154"},
//                {"shunfeng", "12ddfdsfdf"},
//                {"shunfeng", "362847643154"},
//                {"shunfeng", "362847643154"}
//        };
    }

    @DataProvider(name = "test02")
    public Object[][] datas2() {
        int[] rows = {2, 3, 4, 5, 6, 7};
        int[] cells = {6};
        Object[][] dates = ExcelUtils.getDataByArray(path, sheetName, rows, cells);
        return dates;
    }

    @Test(dataProvider = "test01")
    public void test01(String type,String postid) throws UnsupportedEncodingException {
//        String url="http://119.23.241.154:8080/futureloan/mvc/api/member/register";
//"http://www.kuaidi100.com/query?type=shunfeng&postid=362847643154";
        String url="http://www.kuaidi100.com/query";
        Map<String, String> params = new HashMap<>();
        params.put("type",type);
        params.put("postid",postid);
        String reult = HttpClientUtil.doFormPost(url, params,null);
        System.out.println("结果1为："+reult);
    }

    @Test(dataProvider = "test02")
    public void test02(String params){
        String url="http://www.kuaidi100.com/query";
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type","application/x-www-form-urlencoded");
        Map<String, String> parameters = (Map<String, String>)JSONObject.parse(params);
        String reult =HttpClientUtil.doFormPost(url,parameters,header);
        System.out.println("结果2为："+reult);
    }
}
