package com.free.fasttools.service.custom.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.free.fasttools.dao.custom.SuggestDAO;
import com.free.fasttools.global.Global;
import com.free.fasttools.global.TradeCode;
import com.free.fasttools.po.custom.SuggestPO;
import com.free.fasttools.service.custom.service.CustomService;
import com.free.fasttools.utils.base.DateUtils;
import com.free.fasttools.utils.expection.FastException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;

import java.util.Date;
import java.util.List;

/**
 * @Auther: zhengwei
 * @Date: 2018/12/6 16:06
 * @Description: 自定义功能
 */
@Service
public class CustomServiceImpl implements CustomService {

    private static final Logger logger= LoggerFactory.getLogger(CustomServiceImpl.class);

    @Autowired
    private SuggestDAO suggestDAO;


    @Override
    public JSONObject addSuggest(SuggestPO dto){
        JSONObject json=null;
        logger.info("建议信息：{}",JSON.toJSONString(dto));
        try{
            json=this.saveSuggest(dto);
        }catch(FastException fe){
            logger.error("保存建议信息出错：{}",fe.getMessage());
            dto.setErrMsg(fe.getErrMsg());
            json=(JSONObject) JSON.toJSON(dto);
            json.put(Global.SUCCESS,false);
        }catch (Exception e){
            logger.error("保存建议信息出错：{}",e.getMessage());
            dto.setErrMsg(TradeCode.TRADE_INSERT_DATA_ERROR.getMessage());
            json=(JSONObject) JSON.toJSON(dto);
            json.put(Global.SUCCESS,false);
        }
        return json;
    }

    @Transactional(rollbackFor =FastException.class )
    public JSONObject saveSuggest(SuggestPO dto)throws FastException{
        JSONObject json=null;
        List<SuggestPO> list=suggestDAO.querySuggest(dto.getEmail(),dto.getRemark());
        if(null!=list&&list.size()>0){
            throw new FastException("请勿重复提交！");
        }

        int result=suggestDAO.save(dto.getEmail(),dto.getRemark(), DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        if(result<0){
            throw new FastException("登记失败，请稍后再试！");
        }
        dto.setErrMsg(TradeCode.TRADE_SUCCESS.getMessage());
        json=(JSONObject) JSON.toJSON(dto);
        json.put(Global.SUCCESS,true);
        return json;
    }
}
