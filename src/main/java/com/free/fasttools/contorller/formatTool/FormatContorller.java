package com.free.fasttools.contorller.formatTool;

import com.alibaba.fastjson.JSONObject;
import com.free.fasttools.contorller.BaseContorller;
import com.free.fasttools.dto.formatTool.FormatToolDTO;
import com.free.fasttools.service.formatTool.service.FormatService;
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
 * @Date: 2018/12/3 16:52
 * @Description: 转换工具控制器
 */
@Controller
@RequestMapping("/FormatService")
public class FormatContorller extends BaseContorller {

    private static final Logger logger= LoggerFactory.getLogger(FormatContorller.class);

    @Autowired
    private FormatService formatService;

    @RequestMapping("/json")
    public void jsonFormat(ModelMap modelMap, HttpServletRequest request,
                           HttpServletResponse response, FormatToolDTO dto){
        JSONObject json=null;
        json=formatService.getFormatResult(dto);
        this.writeToPage(response,json);
    }

}
