package base.template;

/**
 *
 * @author ruiying.hry
 * @version $Id: ServiceCallBack.java, v 0.1 2017-11-15 下午9:54 ruiying.hry Exp $$
 */
public interface ServiceCallBack {

    /**
     * 前置处理
     */
    void before();

    /**
     * 执行业务逻辑
     */
    void executeService();

    /**
     * 后置处理
     */
    void end();
}