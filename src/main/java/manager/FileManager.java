package manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * �ļ���д������
 * 
 * @author ruiying.hry
 * @version $Id: FileManager.java, v 0.1 2017-10-30 ����9:12 ruiying.hry Exp $$
 */
public class FileManager {

    /** ��־���� */
    private static Logger logger  = LoggerFactory.getLogger(FileManager.class);

    /**
     * �����ʽ
     */
    private String              encoding         = "UTF-8";

    /**
     * �������ݵ��ļ���.Ĭ��Ϊinput.txt
     */
    private String              inputFileName    = "input.txt";

    /**
     * д�����ݵ��ļ���.Ĭ��Ϊoutput.txt
     */
    private String              outputFileName   = "output.txt";

    /** �ļ��е�ַ*/
    private static final String DEFAULT_FILE_DIC = "src/main/resources/";

    public FileManager() {
    }

    public FileManager(String encoding) {
        this.encoding = encoding;
    }

    /**
     * ��data����д�����ļ���
     * @param data ����
     * @return �ɹ�/ʧ��
     */
    public boolean writeIntoFile(String data) {
        boolean rst = true;
        File file = new File(DEFAULT_FILE_DIC + outputFileName);

        try {
            //�����µ��ļ�
            if (!file.exists()) {
                logger.info("NO SUCH FILE, CREATE A NEW ONE, FILE name = "+ outputFileName);

                file.createNewFile();
            }

            //false��ʾ�����ļ�
            FileOutputStream ws = new FileOutputStream(file, false);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ws, this.encoding));

            writer.write(data);
            writer.flush();

            logger.debug("writing done");

            writer.close();
            ws.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            rst = false;
        }

        return rst;

    }

    /**
     * ��file���ݶ��뵽�ڴ���
     * @param fileName �ļ���
     * @return ����.ʧ��Ϊnull
     */
    public String readFile(String fileName) {
        StringBuilder dataBuilder = new StringBuilder();
        File file = new File(DEFAULT_FILE_DIC + fileName);

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
            ex.printStackTrace();

            return null;
        }

        return dataBuilder.toString();

    }

    /**
     * Getter method for property   inputFileName.
     *
     * @return property value of inputFileName
     */
    public String getInputFileName() {
        return inputFileName;
    }

    /**
     * Setter method for property   inputFileName .
     *
     * @param inputFileName value to be assigned to property inputFileName
     */
    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    /**
     * Getter method for property   outputFileName.
     *
     * @return property value of outputFileName
     */
    public String getOutputFileName() {
        return outputFileName;
    }

    /**
     * Setter method for property   outputFileName .
     *
     * @param outputFileName value to be assigned to property outputFileName
     */
    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }
}