package com.httpclient.apiautoV8.testcase;

import com.httpclient.apiautoV8.util.CaseUtil;
import org.testng.annotations.DataProvider;

/**
 * 存在问题：
 * 1、大量存在行号和列号
 * 2、修改用例后，代码需要修改，维护成本高
 * 3、重复读取
 */
public class DataProviderTestV7{
//    String casePath = "src/main/resources/case_v8.xls";
//    String sheetName1 = "Sheet1";
//    String sheetName2 = "Sheet2";
//    String actualResponseData = "ActualResponseData";

    /**
     * 获取接口1的所有测试数据
     *
     * @return
     */
//    @DataProvider(name = "test1")
//    public Object[][] datas1() {
//        Object[][] datas = CaseUtil.getCaseDatasByApiId("1", cellNames);
//        return datas;
//    }


    /**
     * 获取接口2的所有测试数据
     *
     * @return
     */
    @DataProvider(name = "test02")
    public Object[][] datas2() {
        String[] cellNames = {"CaseId", "ApiId", "Params", "ExpectedResponseData", "PreVerifyDataSql", "AfterVerifyDataSql"};
        Object[][] datas = CaseUtil.getCaseDatasByApiId("2", cellNames);
        return datas;
    }

    /**
     * 获取接口2的所有测试数据
     *
     * @return
     */
    @DataProvider(name = "test03")
    public Object[][] datas3() {
        String[] cellNames = {"CaseId", "ApiId", "Params", "ExpectedResponseData", "PreVerifyDataSql", "AfterVerifyDataSql"};
        Object[][] datas = CaseUtil.getCaseDatasByApiId("3", cellNames);
        return datas;
    }



//    //验证根据apiId获取测试数据
//    public static void main(String[] args) {
//        //遍历获取apiId、Params的数据
//        String[] cellNames = {"CaseId", "ApiId", "Params", "ExpectedResponseData", "PreVerifyDataSql", "AfterVerifyDataSql"};
//        Object[][] datas = CaseUtil.getCaseDatasByApiId("3", cellNames);
//        for (Object[] objects : datas) {
//            for (Object object : objects) {
//                System.out.print("【" + object + "】");
//            }
//            System.out.println();
//        }
//    }
}
