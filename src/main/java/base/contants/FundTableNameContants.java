package base.contants;

import base.enums.FunderExperienceInfoTableMappingEnum;
import base.enums.FunderInfoTableMappingEnum;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ruiying.hry
 * @version $Id: FundTableNameContants.java, v 0.1 2017-11-16 下午5:09 ruiying.hry Exp $$
 */
public class FundTableNameContants {
    /** 存储基金详情*/
    public static final String FUND_INFO_TALBE_NAME = "fund_info";


    /** 基金净值存储表(日)-除货币基金*/
    public static final String FUND_NAV_TALBE_NAME = "fund_nav";

    /** 货币基金净值存储表(日)*/
    public static final String FUND_CURRENCY_NAV_TALBE_NAME = "fund_nav_currency";

    /** 存储基金经理详情*/
    public static final String FUNDER_INFO_TALBE_NAME = "funder_info";

    /** 存储基金经理从业经历*/
    public static final String FUNDER_EXPERIENCE_INFO_TALBE_NAME = "funder_experience_info";



    public static List<String> getUpdateCols(String table){
        //需要更新的字段
        List<String> updateCols = new ArrayList<String>();

        if (FUNDER_EXPERIENCE_INFO_TALBE_NAME.equals(table)){
            updateCols.add(FunderExperienceInfoTableMappingEnum.DIMISSION_DATE.getDbCode());
            updateCols.add(FunderExperienceInfoTableMappingEnum.ONGOING.getDbCode());
            updateCols.add(FunderExperienceInfoTableMappingEnum.TOTAL_RETURN.getDbCode());
            updateCols.add(FunderExperienceInfoTableMappingEnum.UPDATE_TIME.getDbCode());
        }else if (FUNDER_INFO_TALBE_NAME.equals(table)){

            updateCols.add(FunderInfoTableMappingEnum.FUNDER_NAME.getDbCode());
            updateCols.add(FunderInfoTableMappingEnum.ASSETS_SCALE.getDbCode());
            updateCols.add(FunderInfoTableMappingEnum.INCUMBENT_COMPANY.getDbCode());
            updateCols.add(FunderInfoTableMappingEnum.BEST_RETURN.getDbCode());
            updateCols.add(FunderInfoTableMappingEnum.DESCRIPTION.getDbCode());
            updateCols.add(FunderInfoTableMappingEnum.UPDATE_TIME.getDbCode());
        }

        return updateCols;
    }

}