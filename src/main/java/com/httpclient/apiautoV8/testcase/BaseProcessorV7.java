package com.httpclient.apiautoV8.testcase;

import com.alibaba.fastjson.JSONObject;
import com.httpclient.apiautoV8.pojo.WriteBackData;
import com.httpclient.apiautoV8.util.*;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 接口测试统一处理类
 */
public class BaseProcessorV7 {
    String casePath = "D:\\javaCode\\21_ProjectStorageFolder\\automatic\\java\\httpclient\\src\\main\\resources\\case_v7.xls";
    String sheetName1 = "Sheet1";
    String sheetName2 = "Sheet2";
    String actualResponseDataName = "ActualResponseData";

    public String[] cellNames = {"CaseId", "ApiId", "Params","ExpectedResponseData","PreVerifyDataSql","AfterVerifyDataSql"};

    @Test(dataProvider = "test1")
    public void test01(String caseId, String apiId, String params,String expectedResponseData,
                       String preVerifyDataSql,String afterVerifyDataSql ) throws UnsupportedEncodingException {

        if (preVerifyDataSql!=null&& preVerifyDataSql.trim().length()>0){
            //在接口调用前查询我们想要验证的字段
            String preVerifyDataResult= DBCheckUtil.doQuery(preVerifyDataSql);
            System.out.println("preVerifyDataResult"+preVerifyDataResult);
            ExcelUtils.writeBackDatas.add(new WriteBackData(sheetName2,caseId,"PreVerifyDataResult",preVerifyDataResult));

        }

        //根据接口编号拿接口地址
        String url = RestUtil.getUrlByApiId(apiId);
        String type = RestUtil.getTypeByApiId(apiId);
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/x-www-form-urlencoded");
        Map<String, String> parameters = (Map<String, String>) JSONObject.parse(params);

        //执行请求
        String actualResponseData = HttpClientUtil.doService(url, type, parameters, header);
        System.out.println("result=" + actualResponseData);

        //对比实际结果、期望结果
        actualResponseData= AssertUtil.assertEquals(actualResponseData,expectedResponseData);
        //保存回写对象
        ExcelUtils.writeBackDatas.add(new WriteBackData(sheetName2, caseId, actualResponseDataName, actualResponseData));


        if (afterVerifyDataSql!=null&& afterVerifyDataSql.trim().length()>0){
            //在接口调用后查询我们想要验证的字段
            String afterVerifyDataResult =DBCheckUtil.doQuery(afterVerifyDataSql);
            System.out.println("afterVerifyDataResult"+afterVerifyDataResult);
            ExcelUtils.writeBackDatas.add(new WriteBackData(sheetName2,caseId,"AfterVerifyDataResult",afterVerifyDataResult));
        }
    }

    @Test(dataProvider = "test2")
    public void test02(String caseId, String apiId, String params,String expectedResponseData,
                       String preVerifyDataSql,String afterVerifyDataSql ) throws UnsupportedEncodingException {
        if (preVerifyDataSql!=null&& preVerifyDataSql.trim().length()>0){
            //在接口调用前查询我们想要验证的字段
            String preVerifyDataResult= DBCheckUtil.doQuery(preVerifyDataSql);
            ExcelUtils.writeBackDatas.add(new WriteBackData(sheetName2,caseId,"PreVerifyDataResult",preVerifyDataResult));

        }

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

        if (afterVerifyDataSql!=null&& afterVerifyDataSql.trim().length()>0){
            //在接口调用后查询我们想要验证的字段
            String afterVerifyDataResult =DBCheckUtil.doQuery(afterVerifyDataSql);
            ExcelUtils.writeBackDatas.add(new WriteBackData(sheetName2,caseId,"AfterVerifyDataResult",afterVerifyDataResult));
        }
    }

    @AfterSuite
    public void batchWriteBackDate(){
        ExcelUtils.batchWriteBackDate(casePath);
    }
}

