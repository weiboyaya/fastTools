package com.free.fasttools.contorller.httpTool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.free.fasttools.contorller.BaseContorller;
import com.free.fasttools.dto.httpTool.HttpToolDTO;
import com.free.fasttools.global.Global;
import com.free.fasttools.service.httpTool.service.HttpService;
import com.free.fasttools.utils.http.HttpUtil;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * http工具服务层
     */
    @Autowired
    private HttpService httpService;

    @RequestMapping("/post")
    public void post(ModelMap modelMap, HttpServletRequest request,
                     HttpServletResponse response, HttpToolDTO dto){

        JSONObject json=null;
        json=httpService.getHttpResult(dto,Global.POST);
        this.writeToPage(response,json);
    }

    @RequestMapping("/get")
    public void get(ModelMap modelMap, HttpServletRequest request,
                     HttpServletResponse response, HttpToolDTO dto){
        JSONObject json=null;
        json=httpService.getHttpResult(dto,Global.GET);
        this.writeToPage(response,json);
    }

    @RequestMapping("/put")
    public void put(ModelMap modelMap, HttpServletRequest request,
                    HttpServletResponse response, HttpToolDTO dto){
        JSONObject json=null;
        json=httpService.getHttpResult(dto,Global.PUT);
        this.writeToPage(response,json);
    }

    @RequestMapping("patch")
    public void patch(ModelMap modelMap, HttpServletRequest request,
                     HttpServletResponse response, HttpToolDTO dto){
        JSONObject json=null;
        json=httpService.getHttpResult(dto,Global.PATCH);
        this.writeToPage(response,json);
    }

    @RequestMapping("delete")
    public void delete(ModelMap modelMap, HttpServletRequest request,
                     HttpServletResponse response, HttpToolDTO dto){
        JSONObject json=null;
        json=httpService.getHttpResult(dto,Global.DELETE);
        this.writeToPage(response,json);
    }

}