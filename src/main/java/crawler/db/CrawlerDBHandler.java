/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package crawler.db;

import manager.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * db各类请求的消费者
 * @author ruiying.hry
 * @version $Id: CrawlerDBHandler.java, v 0.1 2017-11-15 下午6:34 ruiying.hry Exp $$
 */
public class CrawlerDBHandler implements Runnable {

    /** 日志管理 */
    private static Logger         logger = LoggerFactory.getLogger(CrawlerDBHandler.class);

    /** 数据库连接管理*/
    private DatabaseManager databaseManager;

    /** 数据库信息处理队列===插入队 */
    private BlockingQueue<Object> waitingInsertObj;

    /** 操作数据库的表名*/
    private String                tableName;

    public CrawlerDBHandler(DatabaseManager databaseManager, String tableName,
                            BlockingQueue<Object> waitingInsertObj) {
        this.databaseManager = databaseManager;
        this.tableName = tableName;
        this.waitingInsertObj = waitingInsertObj;
    }

    /**
     * 消费队列接口, 处理insert语句
     * */
    @Override
    public void run() {

        try {
            Map<String, Object> sqlMap = (Map<String, Object>) waitingInsertObj.take();

            databaseManager.insert(tableName, sqlMap);
        } catch (InterruptedException e) {
            logger.error("consume interrupt error. ", e);
        } catch (Exception e) {
            logger.error("consume error. ", e);
        }

    }
}