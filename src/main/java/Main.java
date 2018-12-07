import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;

/**
 * 入口
 * @author ruiying.hry
 * @version $Id: Main.java, v 0.1 2017-11-15 下午8:33 ruiying.hry Exp $$
 */
public class Main {

    /** 日志管理 */
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    class A {
        String a;

        public A(String a) {
            this.a = a;
        }
    }

    public static void main(String args[]) throws ParseException {

    }

    ///**
    // * 程序的主入口就在这
    // * @param args
    // */
    //public static void main(String args[]) throws ParseException {
    //
    //    //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //    //
    //    //CronExpression cronExpression = new CronExpression("0 30 14-21/2 * * ?");
    //    //
    //    //
    //    //System.out.println(cronExpression.isSatisfiedBy(simpleDateFormat.parse("2018-04-27 16:30:00")));
    //    //
    //    //System.out.println(cronExpression.isSatisfiedBy(simpleDateFormat.parse("2018-10-27 10:30:00")));
    //    //
    //    //System.out.println(cronExpression.isSatisfiedBy(simpleDateFormat.parse("2018-10-27 14:30:00")));
    //    //System.out.println(cronExpression.isSatisfiedBy(simpleDateFormat.parse("2018-10-27 14:32:00")));
    //    //System.out.println(cronExpression.isSatisfiedBy(simpleDateFormat.parse("2018-10-27 21:30:00")));
    //    //System.out.println(cronExpression.isSatisfiedBy(simpleDateFormat.parse("2018-10-27 22:32:00")));
    //
    //    //        try {
    //    //            //1. 获取所有基金列表
    //    //            //            ProxyHandlerFactory.newInstance(ProxyTypeEnum.FUND_ALL).execute();
    //    //
    //    //            //2. 获取基金单个页面详情
    //    //            FundInfoProxyHandler fundInfoProxyHandler = (FundInfoProxyHandler) ProxyHandlerFactory
    //    //                .newInstance(ProxyTypeEnum.FUND_INFO);
    //    //            fundInfoProxyHandler.initial(LoggerFactory.getLogger("fund_info_error_code"),
    //    //                LoggerFactory.getLogger("fund_info_success_code"),
    //    //                FileNameContants.FUND_INFO_HANDLED_CODES_FILE, PageTypeEnum.FUND_INFO);
    //    //            fundInfoProxyHandler.execute();
    //    //
    //    //            //3. 获取基金净值页面
    //    //            FundInfoProxyHandler fundInfoProxyHandlerNav = (FundInfoProxyHandler) ProxyHandlerFactory
    //    //                .newInstance(ProxyTypeEnum.FUND_INFO);
    //    //            fundInfoProxyHandlerNav.initial(LoggerFactory.getLogger("fund_nav_error_code"),
    //    //                LoggerFactory.getLogger("fund_nav_success_code"),
    //    //                FileNameContants.FUND_NAV_HANDLED_CODES_FILE, PageTypeEnum.FUND_NAV);
    //    //            fundInfoProxyHandlerNav.execute();
    //    //
    //    //            //4. 获取基金经理页面
    //    //            FundInfoProxyHandler fundInfoProxyHandlerManager = (FundInfoProxyHandler) ProxyHandlerFactory
    //    //                    .newInstance(ProxyTypeEnum.FUND_INFO);
    //    //            fundInfoProxyHandlerNav.initial(LoggerFactory.getLogger("funder_error_code"),
    //    //                    LoggerFactory.getLogger("funder_success_code"),
    //    //                    FileNameContants.FUNDER_HANDLED_CODES_FILE, PageTypeEnum.FUND_MANAGER);
    //    //            fundInfoProxyHandlerNav.execute();
    //    //
    //    //        } catch (Exception e) {
    //    //            LogUtil.error(logger, e);
    //    //        } finally {
    //    //            //子线程还在运行,主线程就跑到这儿了,不想等待线程了,别关闭
    //    //            //            DBCPUtil.close();
    //    //        }
    //
    //    Map<String, Object> conditionMap = new HashMap<String, Object>();
    //
    //    //conditionMap.put("FACTOR123", "22");
    //    //conditionMap.put("node3Output1", "辣");
    //    //conditionMap.put("FACTOR2", "");
    //    //conditionMap.put("FC", null);
    //    //
    //    //
    //    //
    //    //String sentence = "FC!=NULL";
    //    //
    //    ////        String sentence = "(FACTOR123>='222'&&node3Output1=='辣')||(!string.contains(FACTOR2,'3'))||()";
    //    //
    //    //Expression expression = AviatorEvaluator.compile(sentence);
    //    //System.out.println(expression.execute(conditionMap));
    //
    //    //conditionMap.put("a", "1990-11-11");
    //    //conditionMap.put("b", "lalalala");
    //    //conditionMap.put("c", 2222);
    //
    //    //conditionMap.put("m65id3", "2018-04-09");
    //
    //    //String ex1 = "a=='1990-11-11'";
    //
    //    //String ex2="(m65id1=='111')||(m65id2>22222222)||(m65id3=='2018-04-08')||(m65id5=='2018-08-30 16:13:58')";
    //
    //    //conditionMap.put("IVR_SPEECH_STANDARD_QUESTION_ID", "4123");
    //    //conditionMap.put("IVR_IE_END_RULE", "STD");
    //    //conditionMap.put("FACTOR_1466759638204_9558", true);
    //    //conditionMap.put("FACTOR_1531122116544_18557", "间接");
    //    //conditionMap.put("dayOfHour", 12);
    //    //conditionMap.put("cn6jniinput", "5");
    //    //
    //    ////String ex3 = "(IVR_SPEECH_STANDARD_QUESTION_ID=='4123')&&(IVR_IE_END_RULE=='STD')&&(FACTOR_1466759638204_9558==true)&&(FACTOR_1531122116544_18557!='false')&&(dayOfHour>=8)&&(dayOfHour<=23)";
    //    //String ex4 = "(cn6jniinput!=str('#'))";
    //    //Expression compileExp = AviatorEvaluator.compile(ex4);
    //    //boolean result = (Boolean) compileExp.execute(conditionMap);
    //    //System.out.print(result);
    //
    //    //String ex1 = "string.contains(aaa,'a')";
    //    //String ex2 = "NODE123<34";
    //    //System.out.print(AviatorEvaluator.execute("("+ex1+"||"+ex2+")&&(d==NULL)",conditionMap));
    //    //System.out.print(AviatorEvaluator.execute("(d=='')",conditionMap));
    //    //System.out.print(AviatorEvaluator.execute(ex2,null));
    //
    //    //String dateStr = "2018-06-05T10:01:33.222";
    //    //String formatter = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    //    //if (dateStr.length() == 19) {
    //    //    formatter = "yyyy-MM-dd'T'HH:mm:ss";
    //    //
    //    //} else if (dateStr.length() == 16) {
    //    //    formatter = "yyyy-MM-dd'T'HH:mm";
    //    //} else if (dateStr.length() == 13) {
    //    //    formatter = "yyyy-MM-dd'T'HH";
    //    //}
    //    //String formatter = "yyyy年MM月dd日HH时mm分ss秒";
    //    //Date date = new Date();
    //    //DateFormat dateFormat = new SimpleDateFormat(formatter);
    //
    //    try {
    //        //String beiginDiffDayStr = "-1";
    //        //Integer.parseInt(beiginDiffDayStr);
    //        //System.out.println(Integer.parseInt(beiginDiffDayStr));
    //        //
    //        //String abc = "";
    //        //String[] array = abc.split(":");
    //        //
    //        //System.out.println(JSON.toJSONString(array));
    //        //
    //        //StringBuilder sb = new StringBuilder();
    //        //sb.append("");
    //        ////System.out.print( dateFormat.format(date));
    //        //if(sb.length()>=1) {
    //        //    System.out.println(sb.delete(sb.length() - 1, sb.length()));
    //        //}
    //        Map<String,Object>map = new HashMap<String, Object>();
    //        map.put("a",true);
    //
    //        Boolean a = (Boolean) map.get("a");
    //
    //        //String config =
    //        //
    //        //System.out.print(a);
    //
    //
    //
    //    } catch (Exception ex) {
    //
    //        int[] a = new int[]{1,2};
    //
    //    }
    //
    //}




}