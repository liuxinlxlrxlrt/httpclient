package com.httpclient.apiautoV8.util;

import com.httpclient.apiautoV8.pojo.Case;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CaseUtil {
    /**
     * 保存所有用例对象（共享数据）
     */
    public static List<Case> cases = new ArrayList<>();

    static {
        String casePath = "src/main/resources/case_v7.xls";
        String sheetName2 = "Sheet2";
        //将所有数据解析封装到cases集合对象中
        ExcelUtils.load(casePath, sheetName2, Case.class);

    }

    /**
     * 根据接口编号获取对应接口的测试数据
     *
     * @param apiId     指定接口编号
     * @param cellNames 要获取的数据对应的别名
     * @return
     */
    public static Object[][] getCaseDatasByApiId(String apiId, String[] cellNames) {
        Class<Case> clazz = Case.class;
        //保存指定接口编号的case对象到csList集合中
        ArrayList<Case> csList = new ArrayList<>();
        //通过循环找出指定接口编号对应的这些用例数据
        for (Case cs : cases) {
            //循环处理
            if (cs.getApiId().equals(apiId)) {
                csList.add(cs);
            }
        }
        //定义一个Object[][]二维数组
        Object[][] datas = new Object[csList.size()][cellNames.length];
        //指定接口编号的case对象到csList集合,将参数保存到 Object[][]二维数组中
        for (int i = 0; i < csList.size(); i++) {
            Case cs = csList.get(i);
            for (int j = 0; j < cellNames.length; j++) {
                //要反射的方法
                String methodName = "get" + cellNames[j];
                Method method = null;
                try {
                    method = clazz.getMethod(methodName);
                    String value = (String) method.invoke(cs);
                    datas[i][j] = value;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return datas;
    }
}
