package com.free.fasttools.service.formatTool.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.free.fasttools.dto.formatTool.FormatToolDTO;
import com.free.fasttools.service.formatTool.service.FormatService;
import org.apache.logging.slf4j.SLF4JLoggerContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Auther: zhengwei
 * @Date: 2018/12/3 21:28
 * @Description:
 */
@Service
public class FormatServiceImpl implements FormatService {

    private static final Logger logger= LoggerFactory.getLogger(FormatServiceImpl.class);

    @Override
    public JSONObject getFormatResult(FormatToolDTO dto) {
        JSONObject json=new JSONObject();

        return json;
    }
}
