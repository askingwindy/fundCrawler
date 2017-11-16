package manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.LogUtil;

import java.io.*;

/**
 * �ļ���д������
 * 
 * @author ruiying.hry
 * @version $Id: FileManager.java, v 0.1 2017-10-30 ����9:12 ruiying.hry Exp $$
 */
public class FileManager {

    /** ��־���� */
    private static Logger logger         = LoggerFactory.getLogger(FileManager.class);

    /**
     * �����ʽ
     */
    private String        encoding       = "UTF-8";

    /** �ļ��е�ַ*/
    private String        defaultFileDic = "src/main/resources/";

    public FileManager() {
    }

    public FileManager(String encoding) {
        this.encoding = encoding;
    }

    /**
     * ��data����д�����ļ���
     *
     * @param outputFileName ����ļ���
     * @param data ����
     * @param append �Ƿ�׷������;false����������������
     *
     * @return �ɹ�/ʧ��
     */
    public boolean writeIntoFile(String outputFileName, String data, boolean append) {
        boolean rst = true;
        File file = new File(defaultFileDic + outputFileName);

        try {
            //�����µ��ļ�
            if (!file.exists()) {
                LogUtil.info(logger, "NO SUCH FILE, CREATE A NEW ONE, FILENAME= " + outputFileName);

                file.createNewFile();
            }

            //false��ʾ�����ļ�
            LogUtil.infoCritical(logger, "START WRITING FILE,FILENAME= ", outputFileName);

            FileOutputStream ws = new FileOutputStream(file, append);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ws, this.encoding));

            writer.write(data);
            writer.flush();

            logger.debug("writing done");

            writer.close();
            ws.close();

        } catch (Exception ex) {
            LogUtil.error(logger, ex, "writing file failed, filename=", outputFileName);
            rst = false;
        }

        LogUtil.infoCritical(logger, "SUCCESS WRITING FILE, FILENAME=", outputFileName);

        return rst;

    }

    /**
     * ��file���ݶ��뵽�ڴ���
     *
     * @param fileName �ļ���
     *
     * @return ����.ʧ��Ϊnull
     */
    public String readFile(String fileName) {
        StringBuilder dataBuilder = new StringBuilder();
        File file = new File(defaultFileDic + fileName);

        LogUtil.infoCritical(logger, "START READING FILE,FILENAME= ", fileName);

        try {

            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), this.encoding);

            BufferedReader reader = new BufferedReader(isr);

            String tempString = null;
            // һ�ζ���һ�У�ֱ������nullΪ�ļ�����
            while ((tempString = reader.readLine()) != null) {
                dataBuilder.append(tempString);
            }

            logger.debug("reading done");

            reader.close();
            isr.close();

        } catch (Exception ex) {
            LogUtil.error(logger, ex, "reading file failed, filename=", fileName);

            return null;
        }

        LogUtil.infoCritical(logger, "SUCCESS READING FILE,FILENAME= ", fileName);

        return dataBuilder.toString();

    }

    /**
     * Setter method for property   defaultFileDic .
     *
     * @param defaultFileDic value to be assigned to property defaultFileDic
     */
    public void setDefaultFileDic(String defaultFileDic) {
        this.defaultFileDic = defaultFileDic;
    }
}