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
 * �����߼�
 * @author ruiying.hry
 * @version $Id: ProjectOneComponent.java, v 0.1 2018��12��07�� 7:17 PM ruiying.hry Exp $
 */
public class ProjectOneComponent {
    /** ��־���� */
    private static Logger                           logger      = LoggerFactory
                                                                    .getLogger("project_handle_log");

    private final static String                     TITLE       = "��������,�ͻ��˻�,�ͻ�����,��������,������, ֧����� ,���,�Է��˻�,�Է�����,��ע,��������,�ͻ��˻�,�ͻ�����,��������,������,֧�����,���,�Է��˻�,�Է�����,��ע,��������,�ͻ��˻�,�ͻ�����,��������,������,֧�����,���,�Է��˻�,�Է�����,��ע,��������,�ͻ��˻�,�ͻ�����,��������,������,֧�����,���,�Է��˻�,�Է�����,��ע";

    /** ��1��Դͷ������ÿһ�е�����*/
    private List<FileDTO>                           table1DataList;

    /** ��2���ݣ��Թ�˾��Ϊkey���洢��5����˾�����ݣ��ڶ���keyΪ�ͻ��˻�*/
    private Map<String, Map<String, List<FileDTO>>> table2UidDataMap;

    /** ��3���ݣ��Ը�������Ϊkey�����ڶ���keyΪ�ͻ��˻�*/
    private Map<String, Map<String, List<FileDTO>>> table3UidDataMap;

    private FileManager                             fileManager = new FileManager();

    private List<String[]>                          result      = new ArrayList<String[]>();

    private Set<FileDTO>                            fileSet     = new HashSet<FileDTO>();

    public void handle() {
        //�ļ���2���ļ���3���кϲ�
        Map<String, Map<String, List<FileDTO>>> findingMap = new HashMap<String, Map<String, List<FileDTO>>>();
        findingMap.putAll(table2UidDataMap);
        findingMap.putAll(table3UidDataMap);

        ////todo:����
        //FileDTO test = new FileDTO();
        //test.setOwnedBank("��������");
        //test.setCustomerAccount("2307484119125979086");
        //test.setCustomerName("�ڽ��괴���ز��������޹�˾");
        //test.setTradeDate(DateUtil.parseDate("2015-04-17", "yyyy-MM-dd"));
        //test.setInMoney(new BigDecimal(0));
        //test.setOutMoney(new BigDecimal("4000000.00"));
        //test.setBalanceMoney(new BigDecimal("0"));
        //test.setTradeToAccount("22910101040031812");
        //test.setTradeToName("�Ĵ�ʢԴʵҵ��չ���޹�˾");
        //test.setMemo("\t����");
        //
        //table1DataList.clear();
        //table1DataList.add(test);

        //�������
        recursion(table1DataList, findingMap, new ArrayList<FileDTO>());

        CsvFileManager csvFileManager = new CsvFileManager();

        result.add(0, TITLE.split(","));
        csvFileManager
            .writeCsv(
                "/Users/ruiyingHe/Library/Mobile Documents/com~apple~CloudDocs/work/2018/���/hongchuang_2019.csv",
                result);
    }

    /**
     * �ݹ��ҵ�����Ҫ�����������
     * @param sourceDTOLineList ��Ҫ�Աȵ�ԭ�����ݵ�������
     * @param traceList �켣
     */
    public void recursion(List<FileDTO> sourceDTOLineList,
                          Map<String, Map<String, List<FileDTO>>> destUidDataMap,
                          List<FileDTO> traceList) {
        for (int i = 0; i < sourceDTOLineList.size(); i++) {

            FileDTO sourceDTO = sourceDTOLineList.get(i);
            //1. ������sourceLineList��"�Է�����"����Ӧtable2DataMap��key
            String tradeToName = sourceDTO.getTradeToName();
            String tradeToAccount = sourceDTO.getTradeToAccount();

            if (!destUidDataMap.containsKey(tradeToName)) {
                //�ļ��������б�����Ʋ����ϣ����Խ��������

                String custorName = sourceDTO.getCustomerName();
                if (!table3UidDataMap.containsKey(custorName)) {
                    //�ļ���3�²��������"�ͻ�����"������Ҫ����������
                    continue;
                }

                //> �ҵ�������֧����
                int lastOutIdx = this.getLastOutIdx(i + 1, sourceDTOLineList, tradeToName,
                    sourceDTO);

                List<FileDTO> innerList = sourceDTOLineList.subList(i, lastOutIdx + 1);

                for (FileDTO output : innerList) {
                    //���ظ���¼һ������Ϣ
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

                    //fileManager.writeIntoFile("������-�Ĵ�ʢԴʵҵ��չ���޹�˾.csv", sb.toString(), true);
                    //fileManager.writeIntoFile("newData.data", "\r\n", true);
                    traceList.remove(traceList.size() - 1);
                }

                continue;
            }

            //2. �ӣ�ά�����map���У��õ�"�Է�����"��Ӧ����һ�ű������
            Map<String, List<FileDTO>> destUidDTOLineMap = destUidDataMap.get(tradeToName);

            if (!destUidDTOLineMap.containsKey(tradeToAccount)) {
                //�ļ�2�������˻����Ʋ����ϣ�����
                LogUtil.info(logger, "�����˻��޷���ʵ�� ��ѯ�Ķ��� sourceDTO =" + JSON.toJSONString(sourceDTO)
                                     + ",tradeToAccount=" + tradeToAccount + ",destTableName="
                                     + tradeToName);
                continue;
            }

            List<FileDTO> destDTOLineList = destUidDTOLineMap.get(tradeToAccount);

            //2.1 �ҵ�֧����Ϊ�յĵ�һ��
            int outMoneyLineIdx = this.getIdxForOutMoneyExists(sourceDTO, destDTOLineList,
                tradeToName);

            if (outMoneyLineIdx == -1) {
                LogUtil.info(logger,
                    "û���ҵ�֧����Ϊ�յĵ�һ�С� ��ѯ�Ķ��� sourceDTO =" + JSON.toJSONString(sourceDTO)
                            + ",destTableName=" + tradeToName);
                continue;
            }

            //2.2 ���ҵ����벻Ϊ�յĵ�һ��
            int lastOutIdx = this.getLastOutIdx(outMoneyLineIdx + 1, destDTOLineList, tradeToName,
                sourceDTO);

            //3. �ҵ�[֧����Ϊ�յĵ�һ�У������벻Ϊ�յĵ���һ��]֮�������·���ٴν��в�ѯ
            List<FileDTO> innerList = destDTOLineList.subList(outMoneyLineIdx, lastOutIdx + 1);

            //3.1 �����sourceDTO����켣������
            traceList.add(sourceDTO);
            //3.2 ���й켣��ѯ
            this.recursion(innerList, destUidDataMap, traceList);
            //3.3 ɾ�����trace
            traceList.remove(traceList.size() - 1);

        }
    }

    /**
     * �ҵ�֧����Ϊ�յ����һ�У���һ���ǵ�һ�����룩
     * @param inMonIdx
     * @param destDTOLineList
     * @param tradeToName
     * @param sourceDTO
     * @return
     */
    private int getLastOutIdx(int inMonIdx, List<FileDTO> destDTOLineList, String tradeToName,
                              FileDTO sourceDTO) {
        //����ģ����ҵ����벻Ϊ�յĵ�һ��
        for (; inMonIdx < destDTOLineList.size(); inMonIdx++) {
            FileDTO tabl2NextLine = destDTOLineList.get(inMonIdx);
            BigDecimal table2NextLineInMoney = tabl2NextLine.getInMoney();
            if (table2NextLineInMoney.compareTo(new BigDecimal(0)) > 0) {
                //���벻Ϊ�գ�����
                break;
            }

        }
        int lastOutIdx = inMonIdx - 1;

        if (lastOutIdx >= destDTOLineList.size()) {
            LogUtil.info(logger,
                "��֧����Ϊ�պ��޷���λ�����벻Ϊ�յ�һ�С� ��ѯ�Ķ��� sourceDTO =" + JSON.toJSONString(sourceDTO)
                        + ",destTableName=" + tradeToName);
            return -1;
        }
        return lastOutIdx;
    }

    /**
     * �õ�Ŀ�ı��з��Ϲ������һ�е��±꣬����
     * 1. Ŀ�ı������==Դ���֧�����ҷ���������ͬһ�졣��ΪdestDTO
     * 2. destDTO�����룬��destDTO��һ�е������бȽϡ��������*0.1>=��һ�е����������е�����
     * 3. ��destDTO����Ѱ�ң��ҵ�֧����Ϊ�յĵ�һ�У�������һ�е�idx
     * 
     * @param sourceDTO ��Ҫƥ���Դ������
     * @param searchLineList ��Ҫ������Ŀ�ı���
     * @param tableName ��ѯ�ı������
     * @return �±�ֵ�����Ϊ-1����������
     */
    public int getIdxForOutMoneyExists(FileDTO sourceDTO, List<FileDTO> searchLineList,
                                       String tableName) {
        //1. ԭ���֧�����ڶ�ӦĿ�ĵ�����
        BigDecimal outMoney = sourceDTO.getOutMoney();
        Date tradeDate = sourceDTO.getTradeDate();

        //2.1 Դ���֧��==Ŀ�����е����룬�ҽ���������ͬ
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
            LogUtil.error(logger, "�޷���Ŀ�ı��ҵ�Դ���Ӧ��֧����,  sourceDTO=" + JSON.toJSONString(sourceDTO)
                                  + ",destTableName=" + tableName);
            return -1;
        }
        FileDTO destDTO = searchLineList.get(destIdx);

        //����ǵ�һ�У���Ϊ�����������������Ҫ�������Ƚ�
        if (destIdx != 0) {
            //2.2 �ȽϷ��Ϲ������һ�е����룬����һ�е��������һ�е�����*0.1 >= ��һ�е����������Ϊ����������
            FileDTO destLastLineDTO = searchLineList.get(destIdx - 1);
            BigDecimal fac1 = destDTO.getInMoney().multiply(new BigDecimal("0.1"));
            BigDecimal fac2 = destLastLineDTO.getBalanceMoney();
            if (fac1.compareTo(fac2) == -1) {
                //���λ�õ����ݣ�����*0.1 < ��һ�ε�����������������
                JSONObject curData = new JSONObject();
                curData.put("curData", destDTO);
                curData.put("lastestData", destLastLineDTO);
                curData.put("table1Data", sourceDTO);
                LogUtil.info(logger, "Ŀ�ı� NotSatisfiedData.data", curData.toJSONString());
                return -1;
            }
        }

        //3. �ҵ�֧����Ϊ�յĵ�һ��
        int searchIdx = destIdx + 1;
        for (; searchIdx < searchLineList.size(); searchIdx++) {
            FileDTO outExistDTO = searchLineList.get(searchIdx);
            if (outExistDTO.getOutMoney().compareTo(new BigDecimal(0)) > 0) {
                //֧����Ϊ�գ�����
                break;
            }

        }

        if (searchIdx >= searchLineList.size()) {
            LogUtil.info(
                logger,
                "Ŀ�ı��ѯ���,����β֧����Ϊ�ա� sourceDTO=" + JSON.toJSONString(sourceDTO) + ",destTableName="
                        + tableName + "������Ҫ���������һ��Ϊ serarch first destDto="
                        + JSONObject.toJSONString(destDTO) + ",sourceSearchIdex=" + searchIdx);
            return -1;
        }

        LogUtil.info(
            logger,
            "Ŀ�ı��ѯ���,�ҵ�Ŀ���С� sourceDTO=" + JSON.toJSONString(sourceDTO) + ",destTableName="
                    + tableName + "������Ҫ��������� idx=" + destIdx + ",������ obj="
                    + JSON.toJSONString(searchLineList.get(destIdx)) + ",֧����Ϊ�� idx=" + searchIdx
                    + ",֧����Ϊ�� obj=" + JSON.toJSONString(searchLineList.get(searchIdx)));

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