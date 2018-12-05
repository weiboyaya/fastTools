package com.free.fasttools.service.formatTool.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.free.fasttools.dto.formatTool.FormatToolDTO;
import com.free.fasttools.global.Global;
import com.free.fasttools.global.TradeCode;
import com.free.fasttools.service.formatTool.service.FormatService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
        String formatJson= Global.EMPTY;
        JSONObject json=null;
        try{
            formatJson=toPrettyFormat(dto.getInputVal());
            dto.setSuccess(true);
            dto.setErrMsg(TradeCode.TRADE_SUCCESS.getMessage());
            dto.setResultVal(formatJson);
            json=(JSONObject)JSON.toJSON(dto);
        }catch (Exception e){
            logger.error("JSON格式化出错：{}",e.getMessage());
            dto.setSuccess(false);
            dto.setErrMsg(TradeCode.TRADE_FORMAT_ERROR.getMessage());
            json=(JSONObject)JSON.toJSON(dto);
        }

        return json;
    }

    /**
     *  使用gson进行json格式化
     * @param json
     * @return
     */
    private String toPrettyFormat(String json)throws Exception{
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonObject);
    }

}
