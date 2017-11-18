package crawler.page.impl;

import manager.FileManager;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by shuqiQin on 2017/11/16.
 */
public class FunderInfoPageHandlerTest {

    private String htmlStr;

    private FunderInfoPageHandler funderInfoPageHandler;

    @Before
    public void readHtml(){
        FileManager manager = new FileManager();
        manager.setDefaultFileDic("src/test/resources/");
        htmlStr = manager.readFile("funderInfo.html");
    }
    @Test
    public void testHandle() throws Exception {
        funderInfoPageHandler = new FunderInfoPageHandler();
        funderInfoPageHandler.setUrl("http://fund.eastmoney.com/manager/30198442.html");

        funderInfoPageHandler.handle();
    }
}