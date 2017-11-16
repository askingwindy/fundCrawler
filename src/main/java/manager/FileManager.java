package manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.LogUtil;

import java.io.*;

/**
 * 文件读写管理器
 * 
 * @author ruiying.hry
 * @version $Id: FileManager.java, v 0.1 2017-10-30 上午9:12 ruiying.hry Exp $$
 */
public class FileManager {

    /** 日志管理 */
    private static Logger logger         = LoggerFactory.getLogger(FileManager.class);

    /**
     * 编码格式
     */
    private String        encoding       = "UTF-8";

    /** 文件夹地址*/
    private String        defaultFileDic = "src/main/resources/";

    public FileManager() {
    }

    public FileManager(String encoding) {
        this.encoding = encoding;
    }

    /**
     * 将data内容写出到文件中
     *
     * @param outputFileName 输出文件名
     * @param data 内容
     * @param append 是否追加内容;false表明覆盖所有内容
     *
     * @return 成功/失败
     */
    public boolean writeIntoFile(String outputFileName, String data, boolean append) {
        boolean rst = true;
        File file = new File(defaultFileDic + outputFileName);

        try {
            //创建新的文件
            if (!file.exists()) {
                LogUtil.info(logger, "NO SUCH FILE, CREATE A NEW ONE, FILENAME= " + outputFileName);

                file.createNewFile();
            }

            //false表示覆盖文件
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
     * 将file内容读入到内存中
     *
     * @param fileName 文件名
     *
     * @return 内容.失败为null
     */
    public String readFile(String fileName) {
        StringBuilder dataBuilder = new StringBuilder();
        File file = new File(defaultFileDic + fileName);

        LogUtil.infoCritical(logger, "START READING FILE,FILENAME= ", fileName);

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