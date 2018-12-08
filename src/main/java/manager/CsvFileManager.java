/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package manager;

import com.csvreader.CsvReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * csv �ļ�����
 * @author ruiying.hry
 * @version $Id: CsvFileManager.java, v 0.1 2018��12��07�� 7:14 PM ruiying.hry Exp $
 */
public class CsvFileManager {

    /**
     * ��ȡcsv�ļ�
     * @param file
     * @param lines

     */
    public void readeCsv(File file, List<String[]> lines) throws FileNotFoundException,
                                                                  IOException {
        CsvReader reader = new CsvReader(file.getPath(), ',', Charset.defaultCharset());

        //��ȡ��ͷ
        reader.getHeaders();

        while (reader.readRecord()) {
            //��������
            lines.add(reader.getValues());
        }

        reader.close();

    }

}