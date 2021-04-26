package cn.pioneeruniverse.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.Arrays;
import java.util.UUID;

public class Base64Util {

    private static final Logger logger = LoggerFactory.getLogger(Base64Util.class);



    public static byte[] base64ToByte(String image) throws IOException {
        // 通过base64来转化图片
        image = image.replaceAll("data:image/jpg;base64,", "");
        // Base64解码
        BASE64Decoder d = new BASE64Decoder();
        byte[] data = d.decodeBuffer(image);
        return data;
    }


    /**
     * 本地图片转换成base64字符串
     * @param imgFile	图片本地路径
     * @return
     *
     * @author ZHANGJL
     * @dateTime 2018-02-23 14:40:46
     */
    public static String ImageToBase64ByLocal(String imgFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();

        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }



    /**
     * base64字符串转换成图片
     * @param imgStr		base64字符串
     * @param imgFilePath	图片存放路径
     * @return
     *
     * @author ZHANGJL
     * @dateTime 2018-02-23 14:42:17
     */
    public static boolean Base64ToImage(String imgStr,String imgFilePath) throws IOException { // 对字节数组字符串进行Base64解码并生成图片

        if (imgStr == null) // 图像数据为空
            return false;
        File folder = new File(imgFilePath+".jpg");
        if(!folder.getParentFile().exists()){
            folder.getParentFile().mkdirs();//创建父级文件路径
            folder.createNewFile();//创建文件
        }


        BASE64Decoder decoder = new BASE64Decoder();
        OutputStream out = null;
        try {
            out = new FileOutputStream(folder);
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            System.out.println("Base64解码:"+ Arrays.toString(b));
            out.write(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
            return true;
        }

    }
    /**
     * <p>将文件转成base64 字符串</p>
     * @param FileName 文件名称
     * @return
     * @throws Exception
     */
    public static String encodeBase64File(String FileName) throws Exception {
        File file = new File(FileName);
        if(!file.exists()){
            file.mkdirs();
        }
        file.createNewFile();
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return new BASE64Encoder().encode(buffer);
    }




}
