package com.free.fasttools.service.httpTool.service;


import com.alibaba.fastjson.JSONObject;
import com.free.fasttools.dto.httpTool.HttpToolDTO;
import com.free.utils.expection.FastException;

/**
 * @Auther: zhengwei
 * @Date: 2018/12/3 16:58
 * @Description: http服务接口
 */
public interface HttpService {
    /**
     * 获取http请求结果
     * @param dto
     * @param method
     * @return
     * @throws FastException
     */
    public JSONObject getHttpResult(HttpToolDTO dto,String method);
}
