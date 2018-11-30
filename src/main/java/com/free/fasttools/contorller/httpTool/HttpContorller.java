package com.free.fasttools.contorller.httpTool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.free.fasttools.contorller.BaseContorller;
import com.free.fasttools.dto.httpTool.HttpToolDTO;
import com.free.fasttools.global.Global;
import com.free.fasttools.utils.http.HttpUtil;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.free.fasttools.global.TradeCode;

import java.io.IOException;

/**
 * @Auther: zhengwei
 * @Date: 2018/11/28 21:44
 * @Description:http工具控制器
 */
@Controller
@RequestMapping("/HttpService")
public class HttpContorller extends BaseContorller {

    private static final Logger logger= LoggerFactory.getLogger(HttpContorller.class);

    private static final  String BODY="body";

    private static final  String HEAD="head";

    @RequestMapping("/post")
    public void post(ModelMap modelMap, HttpServletRequest request,
                     HttpServletResponse response, HttpToolDTO dto){
        HttpEntity retEntity=null;
        JSONObject json=new JSONObject();
        try{
            StringEntity entity=new StringEntity(JSON.toJSONString(dto), getContentType(dto.getContentType()));
            retEntity=HttpUtil.httpsSend(dto.getUrl(), Global.POST,entity);
            json.put(Global.SUCCESS,true);
            json.put(Global.ERRMSG,TradeCode.TRADE_SUCCESS.getMessage());
            json.put(BODY, EntityUtils.toString(retEntity));
            json.put(HEAD,retEntity.toString());
            this.writeToPage(response,json);
        }catch(IOException ioe){
            logger.error(ioe.getMessage());
            json.put(Global.SUCCESS,false);
            json.put(Global.ERRMSG,ioe.getMessage());
            this.writeToPage(response,json);
        }catch (Exception e){
            logger.error(e.getMessage());
            json.put(Global.SUCCESS,false);
            json.put(Global.ERRMSG,TradeCode.TRADE_SYS_ERROR.getMessage());
            this.writeToPage(response,json);
        }
    }

    @RequestMapping("/get")
    public void get(ModelMap modelMap, HttpServletRequest request,
                     HttpServletResponse response, HttpToolDTO dto){
        HttpEntity retEntity=null;
        JSONObject json=new JSONObject();
        try{
            StringEntity entity=new StringEntity(JSON.toJSONString(dto), getContentType(dto.getContentType()));
            retEntity=HttpUtil.httpsSend(dto.getUrl(), Global.GET,entity);
            json.put(Global.SUCCESS,true);
            json.put(Global.ERRMSG,TradeCode.TRADE_SUCCESS.getMessage());
            json.put(BODY, EntityUtils.toString(retEntity));
            json.put(HEAD,retEntity.toString());
            this.writeToPage(response,json);
        }catch(IOException ioe){
            logger.error(ioe.getMessage());
            json.put(Global.SUCCESS,false);
            json.put(Global.ERRMSG,ioe.getMessage());
            this.writeToPage(response,json);
        }catch (Exception e){
            logger.error(e.getMessage());
            json.put(Global.SUCCESS,false);
            json.put(Global.ERRMSG,TradeCode.TRADE_SYS_ERROR.getMessage());
            this.writeToPage(response,json);
        }
    }

    @RequestMapping("/put")
    public void put(ModelMap modelMap, HttpServletRequest request,
                    HttpServletResponse response, HttpToolDTO dto){
        HttpEntity retEntity=null;
        JSONObject json=new JSONObject();
        try{
            StringEntity entity=new StringEntity(JSON.toJSONString(dto), getContentType(dto.getContentType()));
            retEntity=HttpUtil.httpsSend(dto.getUrl(), Global.PUT,entity);
            json.put(Global.SUCCESS,true);
            json.put(Global.ERRMSG,TradeCode.TRADE_SUCCESS.getMessage());
            json.put(BODY, EntityUtils.toString(retEntity));
            json.put(HEAD,retEntity.toString());
            this.writeToPage(response,json);
        }catch(IOException ioe){
            logger.error(ioe.getMessage());
            json.put(Global.SUCCESS,false);
            json.put(Global.ERRMSG,ioe.getMessage());
            this.writeToPage(response,json);
        }catch (Exception e){
            logger.error(e.getMessage());
            json.put(Global.SUCCESS,false);
            json.put(Global.ERRMSG,TradeCode.TRADE_SYS_ERROR.getMessage());
            this.writeToPage(response,json);
        }
    }

    @RequestMapping("patch")
    public void patch(ModelMap modelMap, HttpServletRequest request,
                     HttpServletResponse response, HttpToolDTO dto){
        HttpEntity retEntity=null;
        JSONObject json=new JSONObject();
        try{
            StringEntity entity=new StringEntity(JSON.toJSONString(dto), getContentType(dto.getContentType()));
            retEntity=HttpUtil.httpsSend(dto.getUrl(), Global.PATCH,entity);
            json.put(Global.SUCCESS,true);
            json.put(Global.ERRMSG,TradeCode.TRADE_SUCCESS.getMessage());
            json.put(BODY, EntityUtils.toString(retEntity));
            json.put(HEAD,retEntity.toString());
            this.writeToPage(response,json);
        }catch(IOException ioe){
            logger.error(ioe.getMessage());
            json.put(Global.SUCCESS,false);
            json.put(Global.ERRMSG,ioe.getMessage());
            this.writeToPage(response,json);
        }catch (Exception e){
            logger.error(e.getMessage());
            json.put(Global.SUCCESS,false);
            json.put(Global.ERRMSG,TradeCode.TRADE_SYS_ERROR.getMessage());
            this.writeToPage(response,json);
        }
    }

    @RequestMapping("delete")
    public void delete(ModelMap modelMap, HttpServletRequest request,
                     HttpServletResponse response, HttpToolDTO dto){
        HttpEntity retEntity=null;
        JSONObject json=new JSONObject();
        try{
            StringEntity entity=new StringEntity(JSON.toJSONString(dto), getContentType(dto.getContentType()));
            retEntity=HttpUtil.httpsSend(dto.getUrl(), Global.DELETE,entity);
            json.put(Global.SUCCESS,true);
            json.put(Global.ERRMSG,TradeCode.TRADE_SUCCESS.getMessage());
            json.put(BODY, EntityUtils.toString(retEntity));
            json.put(HEAD,retEntity.toString());
            this.writeToPage(response,json);
        }catch(IOException ioe){
            logger.error(ioe.getMessage());
            json.put(Global.SUCCESS,false);
            json.put(Global.ERRMSG,ioe.getMessage());
            this.writeToPage(response,json);
        }catch (Exception e){
            logger.error(e.getMessage());
            json.put(Global.SUCCESS,false);
            json.put(Global.ERRMSG,TradeCode.TRADE_SYS_ERROR.getMessage());
            this.writeToPage(response,json);
        }
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