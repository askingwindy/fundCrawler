package manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 文件读写管理器
 * 
 * @author ruiying.hry
 * @version $Id: FileManager.java, v 0.1 2017-10-30 上午9:12 ruiying.hry Exp $$
 */
public class FileManager {

    /** 日志管理 */
    private static Logger logger  = LoggerFactory.getLogger(FileManager.class);

    /**
     * 编码格式
     */
    private String              encoding         = "UTF-8";

    /**
     * 读入数据的文件名.默认为input.txt
     */
    private String              inputFileName    = "input.txt";

    /**
     * 写出数据的文件名.默认为output.txt
     */
    private String              outputFileName   = "output.txt";

    /** 文件夹地址*/
    private static final String DEFAULT_FILE_DIC = "src/main/resources/";

    public FileManager() {
    }

    public FileManager(String encoding) {
        this.encoding = encoding;
    }

    /**
     * 将data内容写出到文件中
     * @param data 内容
     * @return 成功/失败
     */
    public boolean writeIntoFile(String data) {
        boolean rst = true;
        File file = new File(DEFAULT_FILE_DIC + outputFileName);

        try {
            //创建新的文件
            if (!file.exists()) {
                logger.info("NO SUCH FILE, CREATE A NEW ONE, FILE name = "+ outputFileName);

                file.createNewFile();
            }

            //false表示覆盖文件
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
     * 将file内容读入到内存中
     * @param fileName 文件名
     * @return 内容.失败为null
     */
    public String readFile(String fileName) {
        StringBuilder dataBuilder = new StringBuilder();
        File file = new File(DEFAULT_FILE_DIC + fileName);

        try {

            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), this.encoding);

            BufferedReader reader = new BufferedReader(isr);

            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
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