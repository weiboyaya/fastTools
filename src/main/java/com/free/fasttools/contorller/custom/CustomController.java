package com.free.fasttools.contorller.custom;

import com.alibaba.fastjson.JSONObject;
import com.free.fasttools.contorller.BaseContorller;
import com.free.fasttools.po.custom.SuggestPO;
import com.free.fasttools.service.custom.service.CustomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: zhengwei
 * @Date: 2018/12/6 15:57
 * @Description: 自定义功能 控制器
 */
@Controller
@RequestMapping("/CustomService")
public class CustomController extends BaseContorller {

    private static final Logger logger= LoggerFactory.getLogger(CustomController.class);

    @Autowired
    private CustomService customService;

    @RequestMapping("/suggest")
    public void post(ModelMap modelMap, HttpServletRequest request,
                     HttpServletResponse response, SuggestPO dto){

        JSONObject json=null;
        json=customService.addSuggest(dto);
        this.writeToPage(response,json);
    }

}
