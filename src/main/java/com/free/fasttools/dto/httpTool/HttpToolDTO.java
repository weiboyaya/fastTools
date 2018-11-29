package com.free.fasttools.dto.httpTool;

import com.free.fasttools.dto.BaseDTO;

/**
 * @Auther: zhengwei
 * @Date: 2018/11/28 22:32
 * @Description:http工具请求对象
 */
public class HttpToolDTO extends BaseDTO {
    /**
     * 请求url地址
     */
    private String url;
    /**
     * 请求参数
     */
    private String body;
    /**
     * 请求内容类型
     */
    private String contentType;
    /**
     * 内容头
     */
    private String head;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    @Override
    public String toString() {
        return "[url:"+this.url+",body:"+this.body+",contentType:"+this.contentType+",head:"+head+"]";
    }
}
