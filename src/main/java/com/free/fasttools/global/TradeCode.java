package com.free.fasttools.global;

/**
 * @Auther: zhengwei
 * @Date: 2018/11/29 11:27
 * @Description: 错误信息枚举类
 */
public enum TradeCode {
    TRADE_SUCCESS("0000","操作成功!"),

    TRADE_SYS_ERROR("9999","系统异常!"),

    TRADE_FORMAT_ERROR("9001","格式化出错：数据格式错误！");





    private String code;
    private String message;

    private TradeCode(String code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }

    /**
     * 获取接口描述
     * @param code
     * @return
     */
    public static String getMessage(String code) {
        for (TradeCode c : TradeCode.values()) {
            if (c.getCode().equals(code)) {
                return c.message;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
