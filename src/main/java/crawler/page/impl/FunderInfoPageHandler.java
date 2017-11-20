package crawler.page.impl;

import base.contants.FundTableNameContants;
import base.enums.FunderExperienceInfoTableMappingEnum;
import base.enums.FunderInfoTableMappingEnum;
import com.alibaba.fastjson.JSONObject;
import crawler.page.PageHandler;
import manager.DatabaseManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DateUtil;
import util.HttpUtils;
import util.LogUtil;
import util.StringUtils;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基金经理页面解析
 * @author sukki.qin
 * @version $Id: FunderInfoPageHandler.java, v 0.1 2017-11-18 下午6:01 shuqi.qin Exp $$
 */
public class FunderInfoPageHandler implements PageHandler {
    /** 日志管理 */
    private static Logger logger = LoggerFactory.getLogger(FunderInfoPageHandler.class);

    private String funderCode;

    private Date current;

    private Map<String, Object> baseInfo = new HashMap<String, Object>();

    private String url;

    public FunderInfoPageHandler() {
        current = new Date();

    }

    private void initBaseInfo(){
        baseInfo.put(FunderExperienceInfoTableMappingEnum.FUNDER_CODE.getDbCode(), funderCode);
        baseInfo.put(FunderExperienceInfoTableMappingEnum.CREATE_TIME.getDbCode(), current);
        baseInfo.put(FunderExperienceInfoTableMappingEnum.UPDATE_TIME.getDbCode(), current);
    }


    @Override
    public boolean handle() {


        if (this.url==null||StringUtils.isEmpty(url)) {
            throw new RuntimeException("基金经理页面地址不存在,无法执行逻辑");
        }

        try {
            String[] infos = url.split("/");
            funderCode = infos[infos.length-1].split("\\.")[0];
        }catch (Exception e){
            throw new RuntimeException("基金经理代码不存在,无法执行逻辑");
        }

        initBaseInfo();

        final String htmltext = getHtml();

        if (htmltext==null||funderCode==null){
            throw new RuntimeException("获取基金经理页面失败");
        }

                //1. 从html的选择器中拿到table显示的数据
        final Document doc = Jsoup.parse(htmltext);

        //return handleProfilo(htmltext) && handleExperience(doc);

        FutureTask<Boolean> futureProfilo = new FutureTask<Boolean>(
                new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return handleProfilo(htmltext);
                    }
                }
        );
        FutureTask<Boolean> futureExperience = new FutureTask<Boolean>(
                new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return handleExperience(doc);
                    }
                }
        );
        new Thread(futureExperience).start();
        new Thread(futureProfilo).start();


        try {
            return futureExperience.get() && futureProfilo.get();
        } catch (InterruptedException e) {
            LogUtil.info(logger, e,"page handle funder exception,code=" + funderCode);
            return false;
        } catch (ExecutionException e) {
            LogUtil.info(logger, e,"page handle funder excution error,code=" + funderCode);
            return false;
        }


    }



    private boolean handleProfilo(String html){
        boolean rst = true;

        html = html.replaceAll("\r","").replaceAll("\n","");

        Map<FunderInfoTableMappingEnum,String> patterns = new HashMap<FunderInfoTableMappingEnum, String>(){
            {
                put(FunderInfoTableMappingEnum.FUNDER_NAME,".*?jlname=\"(.*?)\">");
                put(FunderInfoTableMappingEnum.DESCRIPTION,".*?基金经理简介.*?</span>(.*?)<");
                put(FunderInfoTableMappingEnum.INAUGURATION_DATE,".*?任职起始日期.*?</span>(.*?)<");
                put(FunderInfoTableMappingEnum.INCUMBENT_COMPANY,".*?现任基金公司.*?html\">(.*?)<");
                put(FunderInfoTableMappingEnum.ASSETS_SCALE,".*?现任基金资产.*?'redText'>(.*?)<");
                put(FunderInfoTableMappingEnum.BEST_RETURN,".*?任职期间最佳..*?'redText'>(.*?)<");

            }
        };

        //0. 准备map,这个用于写入数据库中
        Map<String, Object> funderInfo = new HashMap<String, Object>();

        Pattern pattern;
        Matcher matcher;
        for (FunderInfoTableMappingEnum k : patterns.keySet()){
            pattern = Pattern.compile(patterns.get(k));
            matcher = pattern.matcher(html);
            String value = "";
            if (matcher.find()) {
                value = matcher.group(1);
            }
            handleFunderVaule(funderInfo,k,value);

        }
        funderInfo.putAll(baseInfo);
        rst = rst && toDb(funderInfo,FundTableNameContants.FUNDER_INFO_TALBE_NAME,FundTableNameContants.getUpdateCols(FundTableNameContants.FUNDER_INFO_TALBE_NAME));


        return rst;
    }

    private boolean handleExperience(Document document){

        boolean rst = true;
        Map<String, Object> experienceInfo = new HashMap<String, Object>();




        Elements fundTableElement = document.select("table.ftrs");

        //2. 通过映射,找到对应的枚举
        //获取历史管理基金表格数据
        for (Element element:fundTableElement){
            System.out.println(element.select("caption").text());
            if (!element.select("caption").text().contains("管理过")){
                continue;
            }
            Elements ths = element.select("th");
            Elements tds = element.select("td");
            if (ths==null||ths.isEmpty()||tds==null||tds.isEmpty()||tds.size()%ths.size()!=0){
                logger.error("基金经理页面格式错误");
                return false;
            }
            for (int i = 0;i<tds.size();i++) {


                FunderExperienceInfoTableMappingEnum mappingEnum = FunderExperienceInfoTableMappingEnum.getEnumByChName(
                        ths.get(i%ths.size()).text());

                if (mappingEnum == null) {
                    continue;
                }

                this.handleFunderExprienceVaule(experienceInfo, mappingEnum, tds.get(i).text());

                if (experienceInfo.size()>0&&(i+1)%ths.size()==0){
                    experienceInfo.putAll(baseInfo);
                    rst = rst && toDb(experienceInfo,FundTableNameContants.FUNDER_EXPERIENCE_INFO_TALBE_NAME,FundTableNameContants.getUpdateCols(FundTableNameContants.FUNDER_EXPERIENCE_INFO_TALBE_NAME));
                    experienceInfo.clear();

                }

            }
        }

        LogUtil.debug(logger, experienceInfo.toString());



        LogUtil.info(logger, "handle fund success,code=" + funderCode);

        return rst;
    }


    //数据库处理
    public boolean toDb(Map<String,Object> funderInfo,String tableName,List<String> updateCols){
        try {
            LogUtil.debug(logger, funderInfo.toString());
            DatabaseManager databaseManager = new DatabaseManager();

            return databaseManager.update(tableName, funderInfo,updateCols);
        } catch (Exception e) {
            LogUtil.error(logger, e, String.format("handle funder experience info failed, code=%s,content=%s", funderCode,JSONObject.toJSONString(funderInfo)));
            return false;
        }
    }


    /**
     * 处理value,并放入到map中
     * @param funderExperienceInfo 需要写入到数据库的Map
     * @param mappingEnum 映射枚举
     * @param value 值
     * @return
     */
    private void handleFunderExprienceVaule(Map<String, Object> funderExperienceInfo, FunderExperienceInfoTableMappingEnum mappingEnum,
                                            String value) {

        if (mappingEnum==null||mappingEnum.getDbCode()==null)return;

        switch (mappingEnum) {
            case FUNDER_CODE:
                funderExperienceInfo.put(mappingEnum.getDbCode(),value.trim());
                break;
            case FUNDER_NAME:
                funderExperienceInfo.put(mappingEnum.getDbCode(),value.trim());
                break;
            case INCUMBENT_PERIOD:
                //这个类型,需要解析出关于任期的三个字段
                try {
                    String start = value.split("~")[0].trim();
                    funderExperienceInfo.put(FunderExperienceInfoTableMappingEnum.APPOINTMENT_DATE.getDbCode(),DateUtil.parseDate(start));
                    String end = value.split("~")[1].trim();
                    if (end!=null&&!end.contains("至今")){
                        funderExperienceInfo.put(FunderExperienceInfoTableMappingEnum.DIMISSION_DATE.getDbCode(),DateUtil.parseDate(end));
                        funderExperienceInfo.put(FunderExperienceInfoTableMappingEnum.ONGOING.getDbCode(),0);
                    }else if (end!=null){
                        funderExperienceInfo.put(FunderExperienceInfoTableMappingEnum.DIMISSION_DATE.getDbCode(),current);
                        funderExperienceInfo.put(FunderExperienceInfoTableMappingEnum.ONGOING.getDbCode(),1);
                    }else {
                        logger.error("解析基金经理任期结束时间出错");
                    }
                }catch (Exception e){
                    logger.error("解析基金经理任期出错");
                }

                break;
            case FUND_CODE:
                funderExperienceInfo.put(mappingEnum.getDbCode(),value.trim());
                break;
            case TOTAL_RETURN:
                if (value.trim().endsWith("%")){
                    double ratio = Float.parseFloat(value.substring(0,value.trim().length()-1))/100.0;
                    funderExperienceInfo.put(mappingEnum.getDbCode(),ratio);
                }
                break;
            default:
                break;
        }

    }

    /**
     * 处理value,并放入到map中
     * @param funderInfo 需要写入到数据库的Map
     * @param mappingEnum 映射枚举
     * @param value 值
     * @return
     */
    private void handleFunderVaule(Map<String, Object> funderInfo, FunderInfoTableMappingEnum mappingEnum,
                                            String value) {

        if (mappingEnum==null||mappingEnum.getDbCode()==null)return;

        switch (mappingEnum) {
            case FUNDER_CODE:
                funderInfo.put(mappingEnum.getDbCode(),value.trim());
                break;
            case FUNDER_NAME:
                funderInfo.put(mappingEnum.getDbCode(),value.trim());
                break;
            case DESCRIPTION:
                funderInfo.put(mappingEnum.getDbCode(),value.trim());
                break;
            case INAUGURATION_DATE:
                try {
                    funderInfo.put(mappingEnum.getDbCode(),DateUtil.parseDate(value.trim()));

                }catch (Exception e){
                    logger.error("解析基金经理任职时间出错");
                }

                break;
            case INCUMBENT_COMPANY:
                funderInfo.put(mappingEnum.getDbCode(),value.trim());
                break;
            case ASSETS_SCALE:
                funderInfo.put(mappingEnum.getDbCode(),Double.parseDouble(value.trim()));
                break;
            case BEST_RETURN:
                if (value.trim().endsWith("%")){
                    double ratio = Float.parseFloat(value.substring(0,value.trim().length()-1))/100.0;
                    funderInfo.put(mappingEnum.getDbCode(),ratio);
                }
                break;
            default:
                break;
        }

    }


    /**
     * @param url 基金经理页面url
     */

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 爬取网页数据
     * @return 网页内容
     */
    private String getHtml() {
        //http请求

        String fundInfoHtml = null;
        try {
            fundInfoHtml = HttpUtils.getHtmlByUrl(url);
        } catch (Exception e) {
            throw new RuntimeException("http请求获取失败", e);
        } finally {
        }
        return fundInfoHtml;
    }
}