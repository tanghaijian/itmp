package cn.pioneeruniverse.project.common;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

public class CsvUtils {
    private static final Charset DEFAULT_CHARSET = Charset.forName("GBK");

    /**
     * 读取Json String
     * @param is
     * @param fields
     * @return
     * @throws IOException
     */
    public static String read(InputStream is, String[] fields) throws IOException {
        return read(is, fields, DEFAULT_CHARSET);
    }

    /**
     * 读取Json String
     * @param is
     * @param fields Json key（按csv表头格式传入对应的列字段名）
     * @param charset
     * @return
     * @throws IOException
     */
    public static String read(InputStream is, String[] fields, Charset charset) throws IOException {
        // 创建CSV读对象
        CsvReader csvReader = new CsvReader(is, ',', charset);
        // 读表头
        csvReader.readHeaders();
        // 获取表头
        String[] headers = csvReader.getHeaders();

        if(fields == null || fields.length <= 0) {
            fields = csvReader.getHeaders();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        while (csvReader.readRecord()) {
            sb.append("{");
            for (int i = 0; i < fields.length; i++) {
                sb.append("\"").append(fields[i]).append("\":\"").append(csvReader.get(headers[i])).append("\"");
                if (i < fields.length - 1) {
                    sb.append(",");
                }
            }
            sb.append("},");
        }
        if (sb.lastIndexOf(",") > 0) {
            sb.deleteCharAt(sb.lastIndexOf(",")).append("]");
        }
        return sb.toString();
    }

    /**
     * 写入csv
     * @param filePath
     * @param headers
     * @param content
     * @throws IOException
     */
    public static void write(String filePath, String[] headers, List<String[]> content) throws IOException {
        write(filePath, headers, content, DEFAULT_CHARSET);
    }

    /**
     * 写入csv文件
     * @param filePath 写入路径
     * @param headers  表头
     * @param content  内容
     * @param charset  编码
     * @throws IOException
     */
    public static void write(String filePath, String[] headers, List<String[]> content, Charset charset) throws IOException {
        // 创建CSV写对象
        CsvWriter csvWriter = new CsvWriter(filePath, ',', charset);
        // 写表头
        csvWriter.writeRecord(headers);
        // 写行数据
        int i = 0;
        for (String[] rowTxt : content) {
            i++;
            csvWriter.writeRecord(rowTxt);
            if (i == 10000) {
                csvWriter.flush();
                i = 0;
            }
        }
        csvWriter.flush();
    }
}