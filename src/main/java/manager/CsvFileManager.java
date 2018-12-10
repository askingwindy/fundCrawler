/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package manager;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * csv 文件读入
 * @author ruiying.hry
 * @version $Id: CsvFileManager.java, v 0.1 2018年12月07日 7:14 PM ruiying.hry Exp $
 */
public class CsvFileManager {

    /**
     * 读取csv文件
     * @param file
     * @param lines

     */
    public void readeCsv(File file, List<String[]> lines) throws FileNotFoundException, IOException {
        CsvReader reader = new CsvReader(file.getPath(), ',', Charset.defaultCharset());

        //读取表头
        try {
            reader.getHeaders();

            while (reader.readRecord()) {
                //逐行数据
                lines.add(reader.getValues());
            }

            reader.close();
        } catch (Exception ex) {

        } finally {
            reader.close();
        }

    }

    public void writeCsv(String path, List<String[]> lines) {
        CsvWriter csvWriter = new CsvWriter(path, ',', Charset.defaultCharset());

        try {
            for (String[] line : lines) {
                csvWriter.writeRecord(line);
            }
        } catch (Exception ex) {

        } finally {
            csvWriter.close();
        }
    }

}