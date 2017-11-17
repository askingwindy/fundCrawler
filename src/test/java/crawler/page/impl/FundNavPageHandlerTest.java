package crawler.page.impl;

import manager.FileManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

/**
 * Created by ruiyingHe on 2017/11/17.
 */
public class FundNavPageHandlerTest {

    /**
     * 测试非货币基金的写入
     * @throws Exception
     */
    @Test
    public void testHandle() throws Exception {
        FundNavPageHandler fundNavPageHandler = new FundNavPageHandler();
        fundNavPageHandler.setFundCode("000001");

        fundNavPageHandler.handle();
    }

    /**
     * 测试货币基金的写入
     * @throws Exception
     */
    @Test
    public void testCurrencyHandle() throws Exception {
        FundNavPageHandler fundNavPageHandler = new FundNavPageHandler();
        fundNavPageHandler.setFundCode("002758");

        fundNavPageHandler.handle();
    }


    @Test
    public void testResolveNavPage() {
        FileManager manager = new FileManager();

        manager.setDefaultFileDic("src/test/resources/");
        String htmlStr = manager.readFile("fundInfo.html");


        Document docNav = Jsoup.parse(htmlStr);
        //1. 获得所有行的数据
        Elements fundNavTableValues = docNav.select("table").select("tbody");
        Elements fundLines = fundNavTableValues.select("tr");
    }
}