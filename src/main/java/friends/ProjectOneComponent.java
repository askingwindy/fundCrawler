/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package friends;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import friends.dto.FileDTO;
import manager.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.LogUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * �����߼�
 * @author ruiying.hry
 * @version $Id: ProjectOneComponent.java, v 0.1 2018��12��07�� 7:17 PM ruiying.hry Exp $
 */
public class ProjectOneComponent {
    /** ��־���� */
    private static Logger                           logger      = LoggerFactory
                                                                    .getLogger("project_handle_log");

    /** ��1��Դͷ������ÿһ�е�����*/
    private List<FileDTO>                           table1DataList;

    /** ��2���ݣ��Թ�˾��Ϊkey���洢��5����˾�����ݣ��ڶ���keyΪ�ͻ��˻�*/
    private Map<String, Map<String, List<FileDTO>>> table2UidDataMap;

    /** ��3���ݣ��Ը�������Ϊkey�����ڶ���keyΪ�ͻ��˻�*/
    private Map<String, Map<String, List<FileDTO>>> table3UidDataMap;

    private FileManager                             fileManager = new FileManager();

    private String                                  fileName;

    public void handle() {

        //�������
        recursion(table1DataList, new ArrayList<FileDTO>());
    }

    /**
     * �ݹ��ҵ�����Ҫ�����������
     * @param sourceDTOLineList ��Ҫ�Աȵ�ԭ�����ݵ�������
     * @param traceList �켣
     */
    public void recursion(List<FileDTO> sourceDTOLineList, List<FileDTO> traceList) {
        for (FileDTO sourceDTO : sourceDTOLineList) {
            //1. ������sourceLineList��"�Է�����"����Ӧtable2DataMap��key
            String tradeToName = sourceDTO.getTradeToName();
            String tradeToAccount = sourceDTO.getTradeToAccount();

            if (!table2UidDataMap.containsKey(tradeToName)) {
                //�ļ���2�����б�����Ʋ����ϣ��������ڵ������ļ����в���

                if (!table3UidDataMap.containsKey(tradeToName)) {
                    //�ļ���3�����б�����Ʋ����ϣ�����
                    LogUtil.info(logger,
                        "�ļ���3��û�з��ϱ�׼��Ŀ����� ��ѯ�Ķ��� sourceDTO =" + JSON.toJSONString(sourceDTO)
                                + ",destTableName=" + tradeToName);
                    return;
                }
                Map<String, List<FileDTO>> table3UidDTOLineMap = table3UidDataMap.get(tradeToName);
                if (!table3UidDTOLineMap.containsKey(tradeToAccount)) {
                    //��3�������˻����Ʋ����ϣ�����
                    LogUtil.info(logger,
                        "��3�н����˻��޷���ʵ�� ��ѯ�Ķ��� sourceDTO =" + JSON.toJSONString(sourceDTO)
                                + ",tradeToAccount=" + tradeToAccount + ",destTableName="
                                + tradeToName);
                    return;
                }
                List<FileDTO> table3DataList = table3UidDTOLineMap.get(tradeToAccount);

                int outExistIdx = getIdxForOutMoneyExists(sourceDTO, table3DataList, "table3");

                if (outExistIdx != -1) {
                    //��3�ҵ��˺Ϸ����ݣ����м�¼
                    traceList.add(table1DataList.get(outExistIdx));
                    fileManager.writeIntoFile("newData.data", JSONObject.toJSONString(traceList),
                        true);
                }

                return;
            }

            //2. ���ļ���2��ά����2��map���У��õ�"�Է�����"��Ӧ����һ�ű������
            Map<String, List<FileDTO>> destUidDTOLineMap = table2UidDataMap.get(tradeToName);
            if (!destUidDTOLineMap.containsKey(tradeToAccount)) {
                //�ļ�2�������˻����Ʋ����ϣ�����
                LogUtil.info(logger, "�����˻��޷���ʵ�� ��ѯ�Ķ��� sourceDTO =" + JSON.toJSONString(sourceDTO)
                                     + ",tradeToAccount=" + tradeToAccount + ",destTableName="
                                     + tradeToName);
                return;
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
            int inMoneyLineIdx = outMoneyLineIdx;
            inMoneyLineIdx = inMoneyLineIdx + 1;
            for (; inMoneyLineIdx < destDTOLineList.size(); inMoneyLineIdx++) {
                FileDTO tabl2NextLine = destDTOLineList.get(inMoneyLineIdx);
                BigDecimal table2NextLineInMoney = tabl2NextLine.getInMoney();
                if (table2NextLineInMoney.compareTo(new BigDecimal(0)) != 0) {
                    //���벻Ϊ�գ�����
                    break;
                }

            }

            if (inMoneyLineIdx >= destDTOLineList.size()) {
                LogUtil.info(logger,
                    "��֧����Ϊ�պ��޷���λ�����벻Ϊ�յ�һ�С�,֧����Ϊ�յ�һ��ΪoutMoneyLineIdx=" + outMoneyLineIdx
                            + ",  ��ѯ�Ķ��� sourceDTO =" + JSON.toJSONString(sourceDTO)
                            + ",destTableName=" + tradeToName);
                continue;
            }

            //3. �ҵ�֧����Ϊ�յĵ�һ�У������벻Ϊ�յĵ�һ�е���һ�У�֮�������·���ٴν��в�ѯ
            List<FileDTO> innerList = destDTOLineList.subList(outMoneyLineIdx, inMoneyLineIdx);

            //3.1 �����sourceDTO����켣������
            traceList.add(sourceDTO);
            //3.2 ���й켣��ѯ
            this.recursion(innerList, traceList);
            traceList.remove(traceList.size() - 1);

        }
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
            //2.2 �ȽϷ��Ϲ������һ�е����룬����һ�е��������һ�е�����*0.1 >= ��һ�е��£��������Ϊ����������
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

        //3. ��ʼ�ҵ���һ�У�ֱ��֧����Ϊ�յ���һ�е����벻Ϊ�յĵ�һ��
        int firstSearchIdx = destIdx;
        //���ҵ�֧����Ϊ�յĵ�һ��
        destIdx = destIdx + 1;
        for (; destIdx < searchLineList.size(); destIdx++) {
            FileDTO outExistDTO = searchLineList.get(destIdx);
            if (outExistDTO.getOutMoney().compareTo(new BigDecimal(0)) != 0) {
                //֧����Ϊ�գ�����
                break;
            }

        }

        if (destIdx >= searchLineList.size()) {
            LogUtil.info(
                logger,
                "Ŀ�ı��ѯ���,����β֧����Ϊ�ա� sourceDTO=" + JSON.toJSONString(sourceDTO) + ",destTableName="
                        + tableName + "������Ҫ���������һ��Ϊ serarch first destDto="
                        + JSONObject.toJSONString(destDTO) + ",firstSearchIdx=" + firstSearchIdx);
            return -1;
        }

        return destIdx;
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
     * Setter method for property   fileName .
     *
     * @param fileName  value to be assigned to property fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
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