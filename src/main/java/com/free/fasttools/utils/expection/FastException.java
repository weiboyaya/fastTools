package com.free.fasttools.utils.expection;

/**
 * @Auther: zhengwei
 * @Date: 2018-11-28 17:04
 * @Description:自定义异常类
 */
public class FastException extends  Exception{

    private String errMsg="";

    public FastException(String errMsg){
        super(errMsg);
        this.errMsg=errMsg;
    }

    public String getErrMsg() {
        return errMsg;
    }
}