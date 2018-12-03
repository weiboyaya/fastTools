package com.free.fasttools.service.formatTool.service;

import com.alibaba.fastjson.JSONObject;
import com.free.fasttools.dto.formatTool.FormatToolDTO;

/**
 * @Auther: zhengwei
 * @Date: 2018/12/3 21:23
 * @Description: 转换服务接口
 */
public interface FormatService {

    /**
     * 格式转换
     * @param dto
     * @return
     */
    public JSONObject getFormatResult(FormatToolDTO dto);
}
