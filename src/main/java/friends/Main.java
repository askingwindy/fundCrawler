/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package friends;

import friends.dto.FileDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ruiying.hry
 * @version $Id: Main.java, v 0.1 2018��12��07�� 8:30 PM ruiying.hry Exp $
 */
public class Main {

    public static void main(String args[]) {
        FileHandler fileHandler = new FileHandler();
        //1. ���ر�1����
        List<FileDTO> file1Shengyuan = fileHandler
            .exactFile1("/Users/ruiyingHe/Downloads/501/file1/table1-�Ĵ�ʢԴʵҵ��չ���޹�˾.csv");
        List<FileDTO> file1Hongchuang = fileHandler
            .exactFile1("/Users/ruiyingHe/Downloads/501/file1/table1-�Ĵ��괴ʵҵ���޹�˾.csv");

        //2. ���ر�2����
        List<FileDTO> �ڽ��괴���ز��������޹�˾ = fileHandler
            .exactFile1("/Users/ruiyingHe/Downloads/501/file2/�ڽ��괴���ز��������޹�˾.csv");

        List<FileDTO> �Ĵ�����Ͷ�ʹ������޹�˾ = fileHandler
            .exactFile1("/Users/ruiyingHe/Downloads/501/file2/�Ĵ�����Ͷ�ʹ������޹�˾.csv");

        List<FileDTO> �Ĵ��괴ʵҵ���޹�˾ = fileHandler
            .exactFile1("/Users/ruiyingHe/Downloads/501/file2/�Ĵ��괴ʵҵ���޹�˾.csv");

        List<FileDTO> �Ĵ����ʵҵ = fileHandler
            .exactFile1("/Users/ruiyingHe/Downloads/501/file2/�Ĵ����ʵҵ�����ţ����޹�˾.csv");

        List<FileDTO> �Ĵ�ʢԴʵҵ��չ���޹�˾ = fileHandler
            .exactFile1("/Users/ruiyingHe/Downloads/501/file2/�Ĵ�ʢԴʵҵ��չ���޹�˾.csv");

        Map<String, List<FileDTO>> table2Map = new HashMap<String, List<FileDTO>>();
        table2Map.put("�ڽ��괴���ز��������޹�˾", �ڽ��괴���ز��������޹�˾);
        table2Map.put("�Ĵ�����Ͷ�ʹ������޹�˾", �Ĵ�����Ͷ�ʹ������޹�˾);
        table2Map.put("�Ĵ��괴ʵҵ���޹�˾", �Ĵ��괴ʵҵ���޹�˾);
        table2Map.put("�Ĵ����ʵҵ�����ţ����޹�˾", �Ĵ����ʵҵ);
        table2Map.put("�Ĵ�ʢԴʵҵ��չ���޹�˾", �Ĵ�ʢԴʵҵ��չ���޹�˾);

        //3. ���ر�3����
        Map<String, List<FileDTO>> table3Map = fileHandler
            .readDic("/Users/ruiyingHe/Downloads/501/file3/");

        //4. ��������
        ProjectOneComponent projectOneComponent = new ProjectOneComponent();
        projectOneComponent.setTable1DataList(file1Shengyuan);

        Map<String, Map<String, List<FileDTO>>> uid2Map = getUidMap(table2Map);
        projectOneComponent.setTable2UidDataMap(uid2Map);

        Map<String, Map<String, List<FileDTO>>> uid3Map = getUidMap(table3Map);
        projectOneComponent.setTable3UidDataMap(uid3Map);
        projectOneComponent.handle();
    }

    private static Map<String, Map<String, List<FileDTO>>> getUidMap(Map<String, List<FileDTO>> dataMap) {
        Map<String, Map<String, List<FileDTO>>> uidDataMap = new HashMap<String, Map<String, List<FileDTO>>>();
        for (String tableName : dataMap.keySet()) {
            List<FileDTO> tableInfoLine = dataMap.get(tableName);

            Map<String, List<FileDTO>> uidMap = new HashMap<String, List<FileDTO>>();

            for (FileDTO line : tableInfoLine) {
                String customerAccount = line.getCustomerAccount();
                if (uidMap.containsKey(customerAccount)) {
                    uidMap.get(customerAccount).add(line);
                } else {
                    List<FileDTO> fileDTOS = new ArrayList<FileDTO>();
                    fileDTOS.add(line);
                    uidMap.put(customerAccount, fileDTOS);
                }
            }

        }

        return uidDataMap;
    }
}