/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package friends;

import com.alibaba.fastjson.JSON;
import friends.dto.FileDTO;
import manager.CsvFileManager;
import manager.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DateUtil;
import util.LogUtil;
import util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理所有的文件，转换到标准的dto
 * @author ruiying.hry
 * @version $Id: FileHandler.java, v 0.1 2018年12月07日 7:24 PM ruiying.hry Exp $
 */
public class FileHandler {

    private FileManager   fileManager;

    /** 日志管理 */
    private static Logger logger = LoggerFactory.getLogger("data_record_code");

    public List<FileDTO> exactFile1(String path) {
        File file = new File(path);

        CsvFileManager csvFileManager = new CsvFileManager();
        List<String[]> lines = new ArrayList<String[]>();

        try {
            csvFileManager.readeCsv(file, lines);
        } catch (IOException e) {
            LogUtil.error(logger, e, "read failed.");

        }

        if (lines.size() == 0) {
            LogUtil.error(logger, "异常，检查文件路径");
            return null;
        }
        List<FileDTO> result = new ArrayList<FileDTO>();
        for (int idx = 1; idx < lines.size(); idx++) {
            String[] line = lines.get(idx);

            FileDTO fileOneDTO = new FileDTO();
            fileOneDTO.setOwnedBank(line[0]);
            fileOneDTO.setCustomerAccount(line[1]);
            fileOneDTO.setCustomerName(line[2]);

            String dateStr = line[3];
            Date date = null;
            if (dateStr.contains("-")) {
                date = DateUtil.parseDate(dateStr, "yyyy-MM-dd");

            } else {
                date = DateUtil.parseDate(dateStr, "yyyy/M/d");

            }

            if (date == null) {
                LogUtil.error(logger, "DATE ERROR");
                return null;
            }
            fileOneDTO.setTradeDate(date);
            try {
                fileOneDTO.setInMoney(this.formateMoney(line[4]));

                fileOneDTO.setOutMoney(this.formateMoney(line[5]));

                fileOneDTO.setBalanceMoney(this.formateMoney(line[6]));
            } catch (Exception ex) {
                LogUtil.error(logger, ex, "ERROR,idx=" + idx + ",line=" + JSON.toJSONString(line));
                return null;
            }

            fileOneDTO.setTradeToAccount(line[7]);
            fileOneDTO.setTradeToName(line[8]);
            fileOneDTO.setMemo(line[9]);

            result.add(fileOneDTO);
        }

        return result;

    }

    public Map<String, List<FileDTO>> readDic(String path) {
        Map<String, List<FileDTO>> rstMap = new HashMap<String, List<FileDTO>>();
        File[] fileList = new File(path).listFiles();

        for (File file : fileList) {
            if (file.isFile()) {
                List<FileDTO> rst = this.exactFile1(file.getPath());
                rstMap.put(file.getName().split("\\.")[0], rst);
            }
        }
        return rstMap;
    }

    private BigDecimal formateMoney(String money) {
        if (StringUtils.isEmpty(money) || StringUtils.equals(money, "-")) {
            return new BigDecimal("0");
        } else {
            money = money.replace(",", "");
            money = money.trim();
            BigDecimal moneyBalance = new BigDecimal(money);
            return moneyBalance;
        }
    }

    public static void main(String args[]) {
        FileHandler fileHandler = new FileHandler();
        List<FileDTO> rst = new FileHandler()
            .exactFile1("/Users/ruiyingHe/Downloads/501/file2/table2-四川宏创实业有限公司.csv");
        //List<FileDTO> rst = fileHandler
        //    .exactFile1("/Users/ruiyingHe/Downloads/501/file2/四川盛源实业发展有限公司.csv");
        LogUtil.info(logger, JSON.toJSON(rst));

        //Map<String, List<FileDTO>> rstMap = new FileHandler()
        //    .readDic("/Users/ruiyingHe/Downloads/501/file3/");
        //
        //for (String key : rstMap.keySet()) {
        //    new FileManager().writeIntoFile("file3/table3_" + key + ".log",
        //        JSON.toJSONString(rstMap.get(key)), false);
        //}

    }

}