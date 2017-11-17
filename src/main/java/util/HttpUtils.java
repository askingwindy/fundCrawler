package util;

import base.contants.HttpParamsContants;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * http工具类,获取页面数据
 * @author ruiying.hry
 * @version $Id: HttpUtils.java, v 0.1 2017-11-15 下午7:21 ruiying.hry Exp $$
 */
public class HttpUtils {

    /** 日志管理 */
    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 通过url获取页面数据
     * @param url
     * @return
     */
    public static String getHtmlByUrl(String url) {

        HttpClient httpClient = httpClient = new DefaultHttpClient();

        //0. 随机创建http 的header
        Map<String, String> httpHeaderMap = new HashMap<String, String>();
        httpHeaderMap.put("Connection", HttpParamsContants.HEAD_CONNECTION[0]);
        httpHeaderMap.put("Accept", HttpParamsContants.HEAD_ACCEPT[0]);
        httpHeaderMap.put("Accept-Language", HttpParamsContants.HEAD_ACCEPT_LANUAGE[0]);
        httpHeaderMap.put("User-Agent",
            HttpParamsContants.HEAD_USER_AGENT[new Random(System.currentTimeMillis())
                .nextInt(HttpParamsContants.HEAD_USER_AGENT.length)]);

        //1. 通过httpClient请求页面
        String html = null;

        //以get方式请求该URL
        HttpGet httpGet = new HttpGet(url);
        try {
            for (String headerKey : httpHeaderMap.keySet()) {
                httpGet.addHeader(headerKey, httpHeaderMap.get(headerKey));
            }

            HttpResponse response = httpClient.execute(httpGet);
            int resStatu = response.getStatusLine().getStatusCode();

            if (resStatu == HttpStatus.SC_OK) {
                //200正常  其他就不对

                //获得相应实体
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    //获得html源代码
                    html = EntityUtils.toString(entity, Charset.defaultCharset());
                }
            }
        } catch (Exception e) {
            LogUtil.error(logger, e, "url get error!!!" + url);
        } finally {
            httpClient.getConnectionManager().shutdown();

        }
        return html;
    }
}