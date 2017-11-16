package crawler.page.impl;

import manager.FileManager;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by ruiyingHe on 2017/11/16.
 */
public class FundInfoPageHandlerTest {

    private String htmlStr;

    private FundInfoPageHandler fundInfoPageHandler;

    @Before
    public void readHtml(){
        FileManager manager = new FileManager();
        manager.setDefaultFileDic("src/test/resources/");
        htmlStr = manager.readFile("fundInfo.html");
    }
    @Test
    public void testHandle() throws Exception {
        fundInfoPageHandler = new FundInfoPageHandler("169201");

        fundInfoPageHandler.handle(this.htmlStr);
    }
}