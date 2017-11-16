package base.template.impl;

import base.Result;
import base.template.ServiceCallBack;
import base.template.ServiceTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.LogUtil;


/**
 * 模板类的实现
 * 1. 在before()方法中进行某些类的初始化
 * 2. 在end()中释放初始化的东西
 * 3. executeService()中写业务逻辑
 *
 * @author ruiying.hry
 * @version $Id: ServiceTemplateImpl.java, v 0.1 2017-11-15 下午9:58 ruiying.hry Exp $$
 */
public class ServiceTemplateImpl implements ServiceTemplate {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ServiceTemplateImpl.class);


    @Override
    public void executeWithoutTransaction(Result result, ServiceCallBack action) {

        try {
            action.before();
            action.executeService();
        } catch (Exception e) {
            result.setSuccess(false);
            LogUtil.error(logger, e, "HANDLE ERROR");

        } finally {
            action.end();
            LogUtil.infoCritical(logger, "RESULT=", result);
        }
    }


}