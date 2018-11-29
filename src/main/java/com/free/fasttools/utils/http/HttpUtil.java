package com.free.fasttools.utils.http;

import com.free.fasttools.global.Global;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import  org.apache.http.client.methods.RequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.http.HttpEntity;

import java.io.IOException;


/**
 * @Auther: zhengwei
 * @Date: 2018/11/29 09:19
 * @Description:http工具类
 */
public class HttpUtil {
    private final static Logger logger= LoggerFactory.getLogger(HttpUtil.class);

    /**
     *
     * @param url 請求地址
     * @param method 方法类型
     * @param entity 请求参数
     * @return
     * @throws Exception
     */
    public static String httpsSend(String url,String method, HttpEntity entity)throws IOException,Exception {
        logger.info("请求开始，请求报文：{}，url：{}，报文头：{}", EntityUtils.toString(entity), url, entity.toString());

        /* 创建HttpClient */
        CloseableHttpClient client = HttpClients.custom()
                .setConnectionManager(MyPoolingHttpClientConnectionManager.INSTANCE)
                .setSSLSocketFactory(TrustAllCASSLConnectionSocketFactory.INSTANCE)
                .build();

        //创建HttpRequest的配置项（主要是一些网络通讯方面的设置：如超时时间，代理等）
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(10000)//连接超时时间，单位毫秒
                .setSocketTimeout(60000)//请求获取数据的超时时间，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用
                .setConnectionRequestTimeout(5000)//设置从connect Manager获取Connection 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
                .build();

        //创建Http请求
        HttpUriRequest request = getMethod(method).setUri(url).setEntity(entity).setConfig(config).build();

        String resStr = client.execute(request, StringResponseHandle.INSTANCE);
        logger.info("请求结束，返回报文：{}", resStr);

        return resStr;

    }

    /**
     * 获取RequestBuilder
     * @param method
     * @return
     */
    private static RequestBuilder getMethod(String method){
        if(Global.POST.equalsIgnoreCase(method)){
            return RequestBuilder.post();
        }else if(Global.GET.equalsIgnoreCase(method)){
            return RequestBuilder.get();
        }else if(Global.PUT.equalsIgnoreCase(method)){
            return RequestBuilder.put();
        }else if (Global.PATCH.equalsIgnoreCase(method)){
            return RequestBuilder.patch();
        }else if(Global.DELETE.equalsIgnoreCase(method)){
            return RequestBuilder.delete();
        }
        return null;
    }

}
