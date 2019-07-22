/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package friends;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import friends.dto.FileDTO;
import manager.CsvFileManager;
import manager.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.LogUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 处理逻辑
 * @author ruiying.hry
 * @version $Id: ProjectOneComponent.java, v 0.1 2018年12月07日 7:17 PM ruiying.hry Exp $
 */
public class ProjectOneComponent {
    /** 日志管理 */
    private static Logger                           logger      = LoggerFactory
                                                                    .getLogger("project_handle_log");

    private final static String                     TITLE       = "所属银行,客户账户,客户名称,交易日期,收入金额, 支出金额 ,余额,对方账户,对方名称,备注,所属银行,客户账户,客户名称,交易日期,收入金额,支出金额,余额,对方账户,对方名称,备注,所属银行,客户账户,客户名称,交易日期,收入金额,支出金额,余额,对方账户,对方名称,备注,所属银行,客户账户,客户名称,交易日期,收入金额,支出金额,余额,对方账户,对方名称,备注";

    /** 表1是源头表，分析每一行的数据*/
    private List<FileDTO>                           table1DataList;

    /** 表2数据，以公司名为key，存储了5个公司的数据，第二个key为客户账户*/
    private Map<String, Map<String, List<FileDTO>>> table2UidDataMap;

    /** 表3数据，以个人名名为key，，第二个key为客户账户*/
    private Map<String, Map<String, List<FileDTO>>> table3UidDataMap;

    private FileManager                             fileManager = new FileManager();

    private List<String[]>                          result      = new ArrayList<String[]>();

    private Set<FileDTO>                            fileSet     = new HashSet<FileDTO>();

    public void handle() {
        //文件夹2和文件夹3进行合并
        Map<String, Map<String, List<FileDTO>>> findingMap = new HashMap<String, Map<String, List<FileDTO>>>();
        findingMap.putAll(table2UidDataMap);
        findingMap.putAll(table3UidDataMap);

        ////todo:测试
        //FileDTO test = new FileDTO();
        //test.setOwnedBank("工商银行");
        //test.setCustomerAccount("2307484119125979086");
        //test.setCustomerName("内江宏创房地产开发有限公司");
        //test.setTradeDate(DateUtil.parseDate("2015-04-17", "yyyy-MM-dd"));
        //test.setInMoney(new BigDecimal(0));
        //test.setOutMoney(new BigDecimal("4000000.00"));
        //test.setBalanceMoney(new BigDecimal("0"));
        //test.setTradeToAccount("22910101040031812");
        //test.setTradeToName("四川盛源实业发展有限公司");
        //test.setMemo("\t货款");
        //
        //table1DataList.clear();
        //table1DataList.add(test);

        //运行入口
        recursion(table1DataList, findingMap, new ArrayList<FileDTO>());

        CsvFileManager csvFileManager = new CsvFileManager();

        result.add(0, TITLE.split(","));
        csvFileManager
            .writeCsv(
                "/Users/ruiyingHe/Library/Mobile Documents/com~apple~CloudDocs/work/2018/结果/hongchuang_2019.csv",
                result);
    }

    /**
     * 递归找到符合要求的所有数据
     * @param sourceDTOLineList 需要对比的原表数据的所有行
     * @param traceList 轨迹
     */
    public void recursion(List<FileDTO> sourceDTOLineList,
                          Map<String, Map<String, List<FileDTO>>> destUidDataMap,
                          List<FileDTO> traceList) {
        for (int i = 0; i < sourceDTOLineList.size(); i++) {

            FileDTO sourceDTO = sourceDTOLineList.get(i);
            //1. 遍历的sourceLineList的"对方名称"，对应table2DataMap的key
            String tradeToName = sourceDTO.getTradeToName();
            String tradeToAccount = sourceDTO.getTradeToAccount();

            if (!destUidDataMap.containsKey(tradeToName)) {
                //文件夹下所有表格名称不符合，可以进行输出了

                String custorName = sourceDTO.getCustomerName();
                if (!table3UidDataMap.containsKey(custorName)) {
                    //文件夹3下不存在这个"客户名称"，不需要输出这个数据
                    continue;
                }

                //> 找到连续的支出项
                int lastOutIdx = this.getLastOutIdx(i + 1, sourceDTOLineList, tradeToName,
                    sourceDTO);

                List<FileDTO> innerList = sourceDTOLineList.subList(i, lastOutIdx + 1);

                for (FileDTO output : innerList) {
                    //别重复记录一样的信息
                    if (fileSet.contains(output)) {
                        continue;
                    } else {
                        fileSet.add(output);

                    }
                    traceList.add(output);
                    List<String> lineList = new ArrayList<String>();
                    for (FileDTO traceDTO : traceList) {
                        lineList.add(traceDTO.getOwnedBank());
                        lineList.add("A" + traceDTO.getCustomerAccount());
                        lineList.add(traceDTO.getCustomerName());
                        lineList.add(traceDTO.getTradeDateStr());
                        lineList.add(traceDTO.getInMoney().toString());
                        lineList.add(traceDTO.getOutMoney().toString());
                        lineList.add(traceDTO.getBalanceMoney().toString());
                        lineList.add("A" + traceDTO.getTradeToAccount());
                        lineList.add(traceDTO.getTradeToName());
                        lineList.add(traceDTO.getMemo());
                    }

                    String[] array = new String[lineList.size()];
                    String[] s = lineList.toArray(array);
                    result.add(s);

                    //fileManager.writeIntoFile("结果输出-四川盛源实业发展有限公司.csv", sb.toString(), true);
                    //fileManager.writeIntoFile("newData.data", "\r\n", true);
                    traceList.remove(traceList.size() - 1);
                }

                continue;
            }

            //2. 从（维护表的map）中，拿到"对方名称"对应的那一张表的数据
            Map<String, List<FileDTO>> destUidDTOLineMap = destUidDataMap.get(tradeToName);

            if (!destUidDTOLineMap.containsKey(tradeToAccount)) {
                //文件2下所有账户名称不符合，跳出
                LogUtil.info(logger, "交易账户无法核实。 查询的对象 sourceDTO =" + JSON.toJSONString(sourceDTO)
                                     + ",tradeToAccount=" + tradeToAccount + ",destTableName="
                                     + tradeToName);
                continue;
            }

            List<FileDTO> destDTOLineList = destUidDTOLineMap.get(tradeToAccount);

            //2.1 找到支出不为空的第一行
            int outMoneyLineIdx = this.getIdxForOutMoneyExists(sourceDTO, destDTOLineList,
                tradeToName);

            if (outMoneyLineIdx == -1) {
                LogUtil.info(logger,
                    "没有找到支出不为空的第一行。 查询的对象 sourceDTO =" + JSON.toJSONString(sourceDTO)
                            + ",destTableName=" + tradeToName);
                continue;
            }

            //2.2 再找到收入不为空的第一行
            int lastOutIdx = this.getLastOutIdx(outMoneyLineIdx + 1, destDTOLineList, tradeToName,
                sourceDTO);

            //3. 找到[支出不为空的第一行，到收入不为空的的上一行]之间的子链路，再次进行查询
            List<FileDTO> innerList = destDTOLineList.subList(outMoneyLineIdx, lastOutIdx + 1);

            //3.1 将这个sourceDTO计入轨迹链表中
            traceList.add(sourceDTO);
            //3.2 进行轨迹查询
            this.recursion(innerList, destUidDataMap, traceList);
            //3.3 删除这个trace
            traceList.remove(traceList.size() - 1);

        }
    }

    /**
     * 找到支出不为空的最后一行（下一行是第一笔收入）
     * @param inMonIdx
     * @param destDTOLineList
     * @param tradeToName
     * @param sourceDTO
     * @return
     */
    private int getLastOutIdx(int inMonIdx, List<FileDTO> destDTOLineList, String tradeToName,
                              FileDTO sourceDTO) {
        //变相的，先找到收入不为空的第一行
        for (; inMonIdx < destDTOLineList.size(); inMonIdx++) {
            FileDTO tabl2NextLine = destDTOLineList.get(inMonIdx);
            BigDecimal table2NextLineInMoney = tabl2NextLine.getInMoney();
            if (table2NextLineInMoney.compareTo(new BigDecimal(0)) > 0) {
                //收入不为空，跳出
                break;
            }

        }
        int lastOutIdx = inMonIdx - 1;

        if (lastOutIdx >= destDTOLineList.size()) {
            LogUtil.info(logger,
                "在支出不为空后，无法定位到收入不为空的一行。 查询的对象 sourceDTO =" + JSON.toJSONString(sourceDTO)
                        + ",destTableName=" + tradeToName);
            return -1;
        }
        return lastOutIdx;
    }

    /**
     * 得到目的表中符合规则的那一行的下标，即：
     * 1. 目的表的收入==源表的支出，且发生日期在同一天。记为destDTO
     * 2. destDTO的收入，与destDTO上一行的余额进行比较。如果收入*0.1>=上一行的余额，继续进行第三部
     * 3. 从destDTO往下寻找，找到支出不为空的第一行，返回这一行的idx
     * 
     * @param sourceDTO 需要匹配的源表数据
     * @param searchLineList 需要搜索的目的表行
     * @param tableName 查询的表的名字
     * @return 下标值，如果为-1表明不存在
     */
    public int getIdxForOutMoneyExists(FileDTO sourceDTO, List<FileDTO> searchLineList,
                                       String tableName) {
        //1. 原表的支出，在对应目的的收入
        BigDecimal outMoney = sourceDTO.getOutMoney();
        Date tradeDate = sourceDTO.getTradeDate();

        //2.1 源表的支出==目的行中的收入，且交易日期相同
        int destIdx = 0;
        for (; destIdx < searchLineList.size(); destIdx++) {
            FileDTO table2Line = searchLineList.get(destIdx);
            Date table2Date = table2Line.getTradeDate();
            if (table2Date.compareTo(tradeDate) == 0) {
                BigDecimal inMoney = table2Line.getInMoney();
                if (inMoney.compareTo(outMoney) == 0) {
                    break;
                }
            }
        }
        if (destIdx >= searchLineList.size()) {
            LogUtil.error(logger, "无法在目的表找到源表对应的支出项,  sourceDTO=" + JSON.toJSONString(sourceDTO)
                                  + ",destTableName=" + tableName);
            return -1;
        }
        FileDTO destDTO = searchLineList.get(destIdx);

        //如果是第一行，认为这个收入恒成立，不需要进行余额比较
        if (destIdx != 0) {
            //2.2 比较符合规则的这一行的收入，与上一行的余额。如果这一行的收入*0.1 >= 上一行的余额，这个数据为搜索的数据
            FileDTO destLastLineDTO = searchLineList.get(destIdx - 1);
            BigDecimal fac1 = destDTO.getInMoney().multiply(new BigDecimal("0.1"));
            BigDecimal fac2 = destLastLineDTO.getBalanceMoney();
            if (fac1.compareTo(fac2) == -1) {
                //本次获得的数据，收入*0.1 < 上一次的余额，跳过，不做处理
                JSONObject curData = new JSONObject();
                curData.put("curData", destDTO);
                curData.put("lastestData", destLastLineDTO);
                curData.put("table1Data", sourceDTO);
                LogUtil.info(logger, "目的表 NotSatisfiedData.data", curData.toJSONString());
                return -1;
            }
        }

        //3. 找到支出不为空的第一行
        int searchIdx = destIdx + 1;
        for (; searchIdx < searchLineList.size(); searchIdx++) {
            FileDTO outExistDTO = searchLineList.get(searchIdx);
            if (outExistDTO.getOutMoney().compareTo(new BigDecimal(0)) > 0) {
                //支出不为空，跳出
                break;
            }

        }

        if (searchIdx >= searchLineList.size()) {
            LogUtil.info(
                logger,
                "目的表查询完毕,到表尾支出都为空。 sourceDTO=" + JSON.toJSONString(sourceDTO) + ",destTableName="
                        + tableName + "，符合要求的搜索第一行为 serarch first destDto="
                        + JSONObject.toJSONString(destDTO) + ",sourceSearchIdex=" + searchIdx);
            return -1;
        }

        LogUtil.info(
            logger,
            "目的表查询完毕,找到目标行。 sourceDTO=" + JSON.toJSONString(sourceDTO) + ",destTableName="
                    + tableName + "，符合要求的搜索行 idx=" + destIdx + ",搜索行 obj="
                    + JSON.toJSONString(searchLineList.get(destIdx)) + ",支出不为空 idx=" + searchIdx
                    + ",支出不为空 obj=" + JSON.toJSONString(searchLineList.get(searchIdx)));

        return searchIdx;
    }

    /**
     * Getter method for property   table3UidDataMap.
     *
     * @return property value of table3UidDataMap
     */
    public Map<String, Map<String, List<FileDTO>>> getTable3UidDataMap() {
        return table3UidDataMap;
    }

    /**
     * Setter method for property   table3UidDataMap .
     *
     * @param table3UidDataMap  value to be assigned to property table3UidDataMap
     */
    public void setTable3UidDataMap(Map<String, Map<String, List<FileDTO>>> table3UidDataMap) {
        this.table3UidDataMap = table3UidDataMap;
    }

    /**
     * Getter method for property   table2UidDataMap.
     *
     * @return property value of table2UidDataMap
     */
    public Map<String, Map<String, List<FileDTO>>> getTable2UidDataMap() {
        return table2UidDataMap;
    }

    /**
     * Setter method for property   table2UidDataMap .
     *
     * @param table2UidDataMap  value to be assigned to property table2UidDataMap
     */
    public void setTable2UidDataMap(Map<String, Map<String, List<FileDTO>>> table2UidDataMap) {
        this.table2UidDataMap = table2UidDataMap;
    }

    /**
     * Getter method for property   table1DataList.
     *
     * @return property value of table1DataList
     */
    public List<FileDTO> getTable1DataList() {
        return table1DataList;
    }

    /**
     * Setter method for property   table1DataList .
     *
     * @param table1DataList  value to be assigned to property table1DataList
     */
    public void setTable1DataList(List<FileDTO> table1DataList) {
        this.table1DataList = table1DataList;
    }
}