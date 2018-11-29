package com.free.fasttools.contorller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.free.fasttools.global.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Auther: zhengwei
 * @Date: 2018/11/28 22:00
 * @Description:控制器基类
 */
public class BaseContorller {

    private final static Logger logger= LoggerFactory.getLogger(BaseContorller.class);

    public void writeToPage(HttpServletResponse response, JSONObject json){
        try {
            response.setCharacterEncoding(Global.UTF8);
            response.getWriter().print(json);
            response.getWriter().flush();
            response.getWriter().close();
        }catch (IOException e){
            logger.error("IO操作异常:{}",e.getMessage());
        }
    }
}