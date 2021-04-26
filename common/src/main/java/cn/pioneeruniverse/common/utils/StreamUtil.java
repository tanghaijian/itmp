package cn.pioneeruniverse.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: IO流工具类
 * @Date: Created in 11:37 2018/11/5
 * @Modified By:
 */
public class StreamUtil {

    private static Logger logger = LoggerFactory.getLogger(StreamUtil.class);
    private static final int BUFFER_SIZE = 4096;

    /**
     * @param in
     * @return java.lang.String
     * @Description 输入流转String
     * @MethodName InputStreamTOString
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/11/5 11:41
     */
    public static String InputStreamTOString(InputStream in) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        String string = null;
        int count = 0;
        try {
            while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
                outStream.write(data, 0, count);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        data = null;
        try {
            string = new String(outStream.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }
        return string;
    }

    /**
     * @param in
     * @param encoding
     * @return java.lang.String
     * @Description 将输入流转为某种字符编码的String
     * @MethodName InputStreamTOString
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/11/5 11:43
     */
    public static String InputStreamTOString(InputStream in, String encoding) {
        String string = null;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        try {
            while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
                outStream.write(data, 0, count);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        data = null;
        try {
            string = new String(outStream.toByteArray(), encoding);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }
        return string;
    }

    /**
     * @param in
     * @return java.io.InputStream
     * @Description 将字符串转为输入流
     * @MethodName StringTOInputStream
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/11/5 11:44
     */
    public static InputStream StringTOInputStream(String in) throws Exception {
        ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("UTF-8"));
        return is;
    }

    /**
     * @param in
     * @return byte[]
     * @Description 将String转为byte数组
     * @MethodName StringTObyte
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/11/5 11:50
     */
    public static byte[] StringTObyte(String in) {
        byte[] bytes = null;
        try {
            bytes = InputStreamTOByte(StringTOInputStream(in));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return bytes;
    }

    /**
     * @param in
     * @return byte[]
     * @Description 将输入流转为byte数组
     * @MethodName InputStreamTOByte
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/11/5 11:51
     */
    public static byte[] InputStreamTOByte(InputStream in) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return outStream.toByteArray();
    }

    /**
     * @param in
     * @return java.io.InputStream
     * @Description 将byte数组转为输入流
     * @MethodName byteTOInputStream
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/11/5 11:53
     */
    public static InputStream byteTOInputStream(byte[] in) throws Exception {
        ByteArrayInputStream is = new ByteArrayInputStream(in);
        return is;
    }

    /**
     * @param in
     * @return java.lang.String
     * @Description 将byte数组转为String
     * @MethodName byteTOString
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/11/5 11:54
     */
    public static String byteTOString(byte[] in) {
        String result = null;
        InputStream is = null;
        try {
            is = byteTOInputStream(in);
            result = InputStreamTOString(is, "UTF-8");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * @param filepath
     * @return java.io.FileInputStream
     * @Description 根据文件路径获取输入流
     * @MethodName getFileInputStream
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/11/5 11:56
     */
    public static FileInputStream getFileInputStream(String filepath) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filepath);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        return fileInputStream;
    }

    /**
     * @param file
     * @return java.io.FileInputStream
     * @Description 根据文件对象获取文件输入流
     * @MethodName getFileInputStream
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/11/5 11:57
     */
    public static FileInputStream getFileInputStream(File file) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        return fileInputStream;
    }

    /**
     * @param file
     * @param append true:文件以追加方式打开,false:则覆盖原文件的内容
     * @return java.io.FileOutputStream
     * @Description 根据文件对象创建文件输出流
     * @MethodName getFileOutputStream
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/11/5 11:59
     */
    public static FileOutputStream getFileOutputStream(File file, boolean append) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file, append);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        return fileOutputStream;
    }

    /**
     * @Description 根据文件路径创建文件输出流
     * @MethodName getFileOutputStream
     * @param filepath
     * @param append true:文件以追加方式打开,false:则覆盖原文件的内容
     * @return java.io.FileOutputStream
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/11/5 12:04
     */
    public static FileOutputStream getFileOutputStream(String filepath,boolean append) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filepath,append);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        return fileOutputStream;
    }


}
