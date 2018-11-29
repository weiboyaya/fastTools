package com.free.fasttools.utils.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @Auther: zhengwei
 * @Date: 2018/11/29 10:21
 * @Description: 返回报文处理器 将返回报文自动转为String类型
 */
public class StringResponseHandle implements ResponseHandler<String> {

    private final static Logger logger = LoggerFactory.getLogger(StringResponseHandle.class);

    public final static StringResponseHandle INSTANCE = new StringResponseHandle();

    private StringResponseHandle() {
    }

    @Override
    public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        int status = response.getStatusLine().getStatusCode();
        if (status >= 200 && status < 300) {
            HttpEntity entity = response.getEntity();
            ContentType contentType = ContentType.getOrDefault(entity);
            Charset charset = contentType.getCharset();
            return entity != null ? EntityUtils.toString(entity, charset) : null;
        } else {
            logger.error("Unexpected response status: " + status);
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
    }
}
