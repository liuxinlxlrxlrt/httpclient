package com.httpclient.apiautoV6.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 回写数据对象
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WriteBackData {
    private String sheetName;
    private String caseId;
    private String cellName;
    private String result;

    @Override
    public String toString(){
        return "sheetName="+sheetName+",caseId="+caseId+",cellName="+cellName+",result="+result;
    }
}
