package cn.pioneeruniverse.common.utils;

import java.security.MessageDigest;

/**
* @author author
* @Description  Md5工具类
* @Date 2020/9/14
* @return
**/
public class Md5Utils {
	private static final String encryModel="MD5";

	public  static String md5(String str) {
        return encrypt(encryModel, str);
    }
    public static String encrypt(String algorithm, String str) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(str.getBytes());
            StringBuffer sb = new StringBuffer();
            byte[] bytes = md.digest();
            for (int i = 0; i < bytes.length; i++) {
                int b = bytes[i] & 0xFF;
                if (b < 0x10) {
                    sb.append('0');
                }
                sb.append(Integer.toHexString(b));
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }
    
    
}