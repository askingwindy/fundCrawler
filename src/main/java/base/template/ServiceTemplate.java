package base.template;

import base.Result;

/**
 * 模板执行处理.在最上层的业务处理类中,应该采用模板写入逻辑
 * 好处:
 * 1. 模板中做了try{}catch{}处理
 * 2. 便于错误日志的输出
 *
 * @author ruiying.hry
 * @version $Id: ServiceTemplate.java, v 0.1 2017-11-15 下午9:53 ruiying.hry Exp $$
 */
public interface ServiceTemplate {

    /**
     *  无事务模板执行业务处理
     * 1.异常捕获，及分类处理
     * 2.业务日志记录
     * @param baseResult 返回对象
     * @param action 业务操作回调的接口
     */
    void executeWithoutTransaction(Result baseResult, ServiceCallBack action);
}