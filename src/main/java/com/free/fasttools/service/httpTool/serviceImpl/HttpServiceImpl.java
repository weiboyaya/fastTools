package com.free.fasttools.service.httpTool.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.free.fasttools.dto.httpTool.HttpToolDTO;
import com.free.fasttools.global.Global;
import com.free.fasttools.global.TradeCode;
import com.free.fasttools.service.httpTool.service.HttpService;
import com.free.fasttools.utils.http.HttpUtil;
import com.free.utils.expection.FastException;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Auther: zhengwei
 * @Date: 2018/12/3 20:55
 * @Description: HttpService实现方法
 */
@Service
public class HttpServiceImpl implements HttpService {

    private static final Logger logger= LoggerFactory.getLogger(HttpServiceImpl.class);

    private static final  String BODY="body";

    private static final  String HEAD="head";


    @Override
    public JSONObject getHttpResult(HttpToolDTO dto,String method) {
        HttpEntity retEntity=null;
        JSONObject json=new JSONObject();
        try{
            StringEntity entity=new StringEntity(JSON.toJSONString(dto), getContentType(dto.getContentType()));
            retEntity= HttpUtil.httpsSend(dto.getUrl(), Global.POST,entity);
            json.put(Global.SUCCESS,true);
            json.put(Global.ERRMSG, TradeCode.TRADE_SUCCESS.getMessage());
            json.put(BODY, EntityUtils.toString(retEntity));
            json.put(HEAD,retEntity.toString());

        }catch(IOException ioe){
            logger.error(ioe.getMessage());
            json.put(Global.SUCCESS,false);
            json.put(Global.ERRMSG,ioe.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            json.put(Global.SUCCESS,false);
            json.put(Global.ERRMSG,TradeCode.TRADE_SYS_ERROR.getMessage());
        }
        return json;
    }

    /**
     * 获取ContentType
     * @param contentType
     * @return
     */
    private static ContentType getContentType(String contentType){
        if(Global.APPLICATION_JSON.equalsIgnoreCase(contentType)){
            return ContentType.APPLICATION_JSON;
        }else if (Global.APPLICATION_XML.equalsIgnoreCase(contentType)){
            return ContentType.APPLICATION_XML;
        }else if(Global.MULTIPART_FORM_DATA.equalsIgnoreCase(contentType)){
            return ContentType.MULTIPART_FORM_DATA;
        }else if(Global.APPLICATION_X_WWW_FORM_URLENCODED.equalsIgnoreCase(contentType)){
            return ContentType.APPLICATION_FORM_URLENCODED;
        }
        return null;
    }
}
