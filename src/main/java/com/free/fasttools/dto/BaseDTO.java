package com.free.fasttools.dto;

/**
 * @Auther: zhengwei
 * @Date: 2018/11/28 22:34
 * @Description: javaBean 基类
 */
public class BaseDTO {

    private String success;

    private String code;

    private String errMsg;

    private Long count;

    private Long pageSize;

    private Long pages;

    public String getSuccess() {
        return success;
    }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPages() {
        return pages;
    }

    public void setPages(Long pages) {
        this.pages = pages;
    }
}
