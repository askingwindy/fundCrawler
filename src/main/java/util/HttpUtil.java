/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package util;

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
 * @version $Id: HttpUtil.java, v 0.1 2017-11-15 下午7:21 ruiying.hry Exp $$
 */
public class HttpUtil {

    /** 日志管理 */
    private static Logger   logger              = LoggerFactory.getLogger(HttpUtil.class);

    private static String[] HEAD_CONNECTION     = { "Keep-Alive", "close" };
    private static String[] HEAD_ACCEPT         = { "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*" };
    private static String[] HEAD_ACCEPT_LANUAGE = { "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7",
            "en-US,en;q=0.8,zh-Hans-CN;q=0.5,zh-Hans;q=0.3" };
    private static String[] HEAD_USER_AGENT     = {
            "Opera/8.0 (Macintosh; PPC Mac OS X; U; en)",
            "Opera/9.27 (Windows NT 5.2; U; zh-cn)",
            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Win64; x64; Trident/4.0)",
            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0)",
            "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.2; .NET4.0C; .NET4.0E)",
            "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.2; .NET4.0C; .NET4.0E; QQBrowser/7.3.9825.400)",
            "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0; BIDUBrowser 2.x)",
            "Mozilla/5.0 (Windows; U; Windows NT 5.1) Gecko/20070309 Firefox/2.0.0.3",
            "Mozilla/5.0 (Windows; U; Windows NT 5.1) Gecko/20070803 Firefox/1.5.0.12",
            "Mozilla/5.0 (Windows; U; Windows NT 5.2) Gecko/2008070208 Firefox/3.0.1",
            "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.12) Gecko/20080219 Firefox/2.0.0.12 Navigator/9.0.0.6",
            "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; rv:11.0) like Gecko)",
            "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0 ",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Maxthon/4.0.6.2000 Chrome/26.0.1410.43 Safari/537.1 ",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.92 Safari/537.1 LBBROWSER",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.75 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.11 TaoBrowser/3.0 Safari/536.11",
            "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko",
            "Mozilla/5.0 (Macintosh; PPC Mac OS X; U; en) Opera 8.0" };

    /**
     * 通过url获取页面数据
     * @param url
     * @return
     */
    public static String getHtmlByUrl(String url) {

        //0. 随机创建http 的header
        Map<String, String> httpHeaderMap = new HashMap<String, String>();
        httpHeaderMap.put("Connection", HEAD_CONNECTION[0]);
        httpHeaderMap.put("Accept", HEAD_ACCEPT[0]);
        httpHeaderMap.put("Accept-Language", HEAD_ACCEPT_LANUAGE[0]);
        httpHeaderMap
            .put("User-Agent", HEAD_USER_AGENT[new Random(System.currentTimeMillis())
                .nextInt(HEAD_USER_AGENT.length)]);

        //1. 通过httpClient请求页面
        String html = null;

        //创建httpClient对象
        HttpClient httpClient = new DefaultHttpClient();

        //以get方式请求该URL
        HttpGet httpget = new HttpGet(url);
        try {
            for (String headerKey : httpHeaderMap.keySet()) {
                httpget.addHeader(headerKey, httpHeaderMap.get(headerKey));
            }

            HttpResponse response = httpClient.execute(httpget);
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
            logger.error("url get error!!!" + url, e);
            e.printStackTrace();

        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return html;
    }
}