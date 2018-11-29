package com.free.fasttools.utils.http;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * @Auther: zhengwei
 * @Date: 2018/11/29 10:12
 * @Description:自定义httpclient连接池
 */
public class MyPoolingHttpClientConnectionManager {
    public static final PoolingHttpClientConnectionManager INSTANCE = MyPoolingHttpClientConnectionManager.build();

    private MyPoolingHttpClientConnectionManager() {
    }

    /**
     * 创建PoolingHttpClientConnectionManager对象
     *
     * @return
     */
    private static PoolingHttpClientConnectionManager build() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        //将http连接池可用数量设置为200
        cm.setMaxTotal(300);
        //设置单个主机的最大连接数
        cm.setDefaultMaxPerRoute(30);
        return cm;
    }
}
