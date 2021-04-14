package com.httpclient.apiautoV6.pojo;

import lombok.Data;

@Data
public class Case {
    private String caseId;
    private String apiId;
    private String desc;
    private String params;
    private String expectedResponseData;
    private String actualResponseData;

    @Override
    public String toString(){
        return "caseId="+caseId +",apiId="+apiId +",desc="+desc +",params="+params+"" + ",expectedResponseData"+expectedResponseData +",actualResponseData"+actualResponseData;
    }
}
