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
 * @version $Id: Main.java, v 0.1 2018年12月07日 8:30 PM ruiying.hry Exp $
 */
public class Main {

    public static void main(String args[]) {
        FileHandler fileHandler = new FileHandler();
        //1. 加载表1数据
        List<FileDTO> file1Shengyuan = fileHandler
            .exactFile1("/Users/ruiyingHe/Downloads/501/file1/table1-四川盛源实业发展有限公司.csv");
        List<FileDTO> file1Hongchuang = fileHandler
            .exactFile1("/Users/ruiyingHe/Downloads/501/file1/table1-四川宏创实业有限公司.csv");

        //2. 加载表2数据
        List<FileDTO> 内江宏创房地产开发有限公司 = fileHandler
            .exactFile1("/Users/ruiyingHe/Downloads/501/file2/内江宏创房地产开发有限公司.csv");

        List<FileDTO> 四川富华投资管理有限公司 = fileHandler
            .exactFile1("/Users/ruiyingHe/Downloads/501/file2/四川富华投资管理有限公司.csv");

        List<FileDTO> 四川宏创实业有限公司 = fileHandler
            .exactFile1("/Users/ruiyingHe/Downloads/501/file2/四川宏创实业有限公司.csv");

        List<FileDTO> 四川奇峰实业 = fileHandler
            .exactFile1("/Users/ruiyingHe/Downloads/501/file2/四川奇峰实业（集团）有限公司.csv");

        List<FileDTO> 四川盛源实业发展有限公司 = fileHandler
            .exactFile1("/Users/ruiyingHe/Downloads/501/file2/四川盛源实业发展有限公司.csv");

        Map<String, List<FileDTO>> table2Map = new HashMap<String, List<FileDTO>>();
        table2Map.put("内江宏创房地产开发有限公司", 内江宏创房地产开发有限公司);
        table2Map.put("四川富华投资管理有限公司", 四川富华投资管理有限公司);
        table2Map.put("四川宏创实业有限公司", 四川宏创实业有限公司);
        table2Map.put("四川奇峰实业（集团）有限公司", 四川奇峰实业);
        table2Map.put("四川盛源实业发展有限公司", 四川盛源实业发展有限公司);

        //3. 加载表3数据
        Map<String, List<FileDTO>> table3Map = fileHandler
            .readDic("/Users/ruiyingHe/Downloads/501/file3/");

        //4. 进行运行
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