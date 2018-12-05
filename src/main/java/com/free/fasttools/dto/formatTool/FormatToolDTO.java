package com.free.fasttools.dto.formatTool;

import com.free.fasttools.dto.BaseDTO;

/**
 * @Auther: zhengwei
 * @Date: 2018/12/3 21:16
 * @Description: 格式转换DTO
 */
public class FormatToolDTO extends BaseDTO {
    /**
     * 请求字符串
     */
    private String inputVal;
    /**
     * 转换类型
     */
    private String type;
    /**
     * 响应字符串
     */
    private String resultVal;

    public String getInputVal() {
        return inputVal;
    }

    public void setInputVal(String inputVal) {
        this.inputVal = inputVal;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResultVal() {
        return resultVal;
    }

    public void setResultVal(String resultVal) {
        this.resultVal = resultVal;
    }

    @Override
    public String toString() {
        return "[inputVal="+this.inputVal+",type="+this.type+",resultVal="+this.resultVal+"]";
    }
}
