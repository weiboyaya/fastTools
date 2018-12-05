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
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.StringWriter;

/**
 * @Auther: zhengwei
 * @Date: 2018/12/3 21:28
 * @Description: 格式转化service
 */
@Service
public class FormatServiceImpl implements FormatService {

    private static final Logger logger= LoggerFactory.getLogger(FormatServiceImpl.class);

    @Override
    public JSONObject getFormatResult(FormatToolDTO dto) {
        String formatJson= Global.EMPTY;
        JSONObject json=null;
        try{
            if(Global.TYPE_JSON.equalsIgnoreCase(dto.getType())){
                formatJson=toPrettyFormat(dto.getInputVal());
            }else if(Global.TYPE_XML.equalsIgnoreCase(dto.getType())){
                formatJson=xmlFormat(dto.getInputVal());
            }

            dto.setErrMsg(TradeCode.TRADE_SUCCESS.getMessage());
            dto.setResultVal(formatJson);
            json=(JSONObject)JSON.toJSON(dto);
            json.put(Global.SUCCESS,true);
        }catch (Exception e){
            logger.error("格式化出错：{}",e.getMessage());
            dto.setResultVal(e.getMessage());
            dto.setErrMsg(TradeCode.TRADE_FORMAT_ERROR.getMessage());
            json=(JSONObject)JSON.toJSON(dto);
            json.put(Global.SUCCESS,false);
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
        json=json.replaceAll("\\\\\"","").replaceAll("\r|\n", "");
        if(json.startsWith("\"")&&json.endsWith("\"")){
            json=json.substring(1,json.length()-1);
        }
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonObject);
    }

    private String xmlFormat(String json)throws Exception{
        Document document = null;
        document = DocumentHelper.parseText(json);
        /**
         * 格式化输出格式
          */
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(Global.UTF8);
        StringWriter writer = new StringWriter();
        /**
         * 格式化输出流
         */
        XMLWriter xmlWriter = new XMLWriter(writer, format);
        /**
         * 将document写入到输出流
         */
        xmlWriter.write(document);
        xmlWriter.close();

        return writer.toString();
    }

}
