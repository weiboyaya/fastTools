package com.free.fasttools.service.custom.service;

import com.alibaba.fastjson.JSONObject;
import com.free.fasttools.po.custom.SuggestPO;
import com.free.fasttools.utils.expection.FastException;

/**
 * @Auther: zhengwei
 * @Date: 2018/12/6 16:04
 * @Description:
 */
public interface CustomService {
    /**
     * 添加建议信息
     * @param dto
     * @return
     */
    public JSONObject addSuggest(SuggestPO dto);
}
