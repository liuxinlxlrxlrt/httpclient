package com.httpclient.apiautoV6.testcase;

import com.alibaba.fastjson.JSONObject;
import com.httpclient.apiautoV6.pojo.WriteBackData;
import com.httpclient.apiautoV6.util.AssertUtil;
import com.httpclient.apiautoV6.util.ExcelUtils;
import com.httpclient.apiautoV6.util.HttpClientUtil;
import com.httpclient.apiautoV6.util.RestUtil;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 接口测试统一处理类
 */
public class BaseProcessor {
    String casePath = "src/main/resources/case_v4.xls";
    String sheetName1 = "Sheet1";
    String sheetName2 = "Sheet2";
    String actualResponseDataName = "ActualResponseData";

    @Test(dataProvider = "test1")
    public void test01(String caseId, String apiId, String params,String expectedResponseData) throws UnsupportedEncodingException {
        //根据接口编号拿接口地址
        String url = RestUtil.getUrlByApiId(apiId);
        String type = RestUtil.getTypeByApiId(apiId);

        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/x-www-form-urlencoded");
        Map<String, String> parameters = (Map<String, String>) JSONObject.parse(params);
        String actualResponseData = HttpClientUtil.doService(url, type, parameters, header);System.out.println("result=" + actualResponseData);
        //对比实际结果、期望结果
        actualResponseData= AssertUtil.assertEquals(actualResponseData,expectedResponseData);
        //保存回写对象
        ExcelUtils.writeBackDatas.add(new WriteBackData(sheetName2, caseId, actualResponseDataName, actualResponseData));

    }

    @Test(dataProvider = "test2")
    public void test02(String caseId, String apiId, String params,String expectedResponseData) throws UnsupportedEncodingException {
        //根据接口编号拿接口地址
        String url = RestUtil.getUrlByApiId(apiId);
        String type = RestUtil.getTypeByApiId(apiId);

        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/x-www-form-urlencoded");
        Map<String, String> parameters = (Map<String, String>) JSONObject.parse(params);
        String actualResponseData = HttpClientUtil.doService(url, type, parameters, header);
        System.out.println("result=" + actualResponseData);
        //对比实际结果、期望结果
        actualResponseData= AssertUtil.assertEquals(actualResponseData,expectedResponseData);
        //保存回写对象

        /**
         * 文件重复读写，频繁操作IO流，影响性能开销，执行代码比慢
         * 解决办法：先把每条数据保存起来，把result疯封装成对象，最后只写一次
         * result+sheetName+caseId+cellName
         */
        //保存回写对象,修改后
        ExcelUtils.writeBackDatas.add(new WriteBackData(sheetName2,caseId,actualResponseDataName,actualResponseData));


    }

    @AfterSuite
    public void batchWriteBackDate(){
        ExcelUtils.batchWriteBackDate(casePath);
    }
}

