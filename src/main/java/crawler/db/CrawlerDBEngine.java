package crawler.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * 数据库处理引擎
 * @author ruiying.hry
 * @version $Id: CrawlerDBEngine.java, v 0.1 2017-11-15 下午6:16 ruiying.hry Exp $$
 */
public class CrawlerDBEngine {

    /** 日志管理 */
    private static Logger         logger = LoggerFactory.getLogger(CrawlerDBEngine.class);

    /**
     * 数据库信息处理队列===插入队列
     * key:插入的表名
     * value:插入队列
     */
    private BlockingQueue<Object> waitingInsertObj;

    private String                tableName;

    public CrawlerDBEngine(String tableName, BlockingQueue<Object> waitingInsertObj) {
        this.tableName = tableName;
        this.waitingInsertObj = waitingInsertObj;
    }

    /**
     * 存储数据主接口
     * @param insertObj  插入的内容
     */
    public void store(Map<String, Object> insertObj) {
        try {

            this.waitingInsertObj.put(insertObj);

        } catch (InterruptedException e) {
            logger.error("store interrupt error. ", e);
        } catch (Exception e) {
            logger.error("store error. ", e);
        }

    }

}