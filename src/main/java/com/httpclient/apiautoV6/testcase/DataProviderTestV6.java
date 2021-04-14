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
 * 存在问题：
 * 1、大量存在行号和列号
 * 2、修改用例后，代码需要修改，维护成本高
 * 3、重复读取
 *
 */
public class DataProviderTestV6 {
    String caasePath = "src/main/resources/case_v3.xls";
    String sheetName1 = "Sheet1";
    String sheetName2 = "Sheet2";

    /**
     * 获取参数数据
     *
     * @return
     */
    @DataProvider(name = "test")
    public Object[][] datas1() {
//        int[] rows = {2, 3, 4, 5, 6, 7};
        int[] rows = {8, 9, 10, 11, 12, 13};
        int[] cells = {3, 4};
        Object[][] dates = ExcelUtils.getDataByArray(caasePath, sheetName2, rows, cells);
        return dates;
    }


    @Test(dataProvider = "test")
    public void test01(String apiidFromCase, String params) throws UnsupportedEncodingException {
        int[] rows = {2, 3};
        int[] cells = {1, 3, 4};
        String url = "";
        String type = "";
        Object[][] datas = ExcelUtils.getDataByArray(caasePath, sheetName1, rows, cells);
        //遍历数组
        for (Object[] objects : datas) {
            //获取每一组的第一个数据
            String apiidFromRest = objects[0].toString();
            //判断用例中读取到的接口编号是否和接口地址表的中接口编号是否相等
            //相等：获取接口类型和接口地址
            if (apiidFromRest.equals(apiidFromCase)) {
                type = objects[1].toString();
                url = objects[2].toString();
                break;
            }
        }
        System.out.println("url=" + url);


//        String url="http://www.kuaidi100.com/query";
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/x-www-form-urlencoded");
        Map<String, String> parameters = (Map<String, String>) JSONObject.parse(params);
        String result = HttpClientUtil.doService(url, type, parameters, header);
        System.out.println("result=" + result);
    }

    @Test
    public void test03() throws UnsupportedEncodingException {
        String url = "http://apis.juhe.cn/idioms/query";
        String key = "0baac739352ce3fda2089d3f1882ec82";
        String wd = "一心一意";
//        String encode = URLEncoder.encode(wd, "utf-8");
//        System.out.println(encode);

        Map<String, String> params = new HashMap<>();
        params.put("key",key);
        params.put("wd",wd);
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/x-www-form-urlencoded");
        String result = HttpClientUtil.doGet(url, params, header);
        System.out.println(result);
    }

}
