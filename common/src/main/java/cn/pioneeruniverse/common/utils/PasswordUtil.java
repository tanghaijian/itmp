package cn.pioneeruniverse.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;


/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 14:34 2019/4/29
 * @Modified By:
 */
public class PasswordUtil {

    private static final Logger logger = LoggerFactory.getLogger(PasswordUtil.class);

    private static final String SHA_PWD_PREFIX = "{SHA}";

    private static final String AES_KEY = "abcdef0123456789";//aes加密用的key,必须为16位

    private static final String AES_IV_PARAMETER = "1234567890abcdef";//必须为16位

    private static final String AES_KEY_ALGORITHM = "AES";

    private static final String AES_DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";//AES-128-CBC加密模式

    /**
     * @param str
     * @return java.lang.String
     * @Description SHA1加密
     * @MethodName encryptForSHA1
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/5/8 10:02
     */
    public static String encryptForSHA1(String str) {
        try {
            if (StringUtils.isNotEmpty(str)) {
                str = new StringBuilder(SHA_PWD_PREFIX).
                        append(new BASE64Encoder().encode(MessageDigest.getInstance("SHA1").digest(str.getBytes()))).toString();
            }
            return str;
        } catch (Exception e) {
            logger.error("生成svn用户http协议下密码错误，错误原因：" + e.getMessage());
            return null;
        }
    }

    /**
     * @param str
     * @return java.lang.String
     * @Description AES加密
     * @MethodName encryptForAES
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/5/8 10:13
     */
    public static String encryptForAES(String str) {
        try {
            if (StringUtils.isNotEmpty(str)) {
                Cipher cipher = Cipher.getInstance(AES_DEFAULT_CIPHER_ALGORITHM);//创建密码器
                byte[] strBytes = str.getBytes("UTF-8");
                IvParameterSpec iv = new IvParameterSpec(AES_IV_PARAMETER.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
                cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(AES_KEY.getBytes("ASCII"),AES_KEY_ALGORITHM), iv);//初始化为加密模式密码器
                byte[] result = cipher.doFinal(strBytes);
                str = new BASE64Encoder().encode(result);
            }
            return str;
        } catch (NoSuchPaddingException e) {
            logger.error("AES加密失败:" + e.getMessage());
            return null;
        } catch (NoSuchAlgorithmException e) {
            logger.error("AES加密失败:" + e.getMessage());
            return null;
        } catch (InvalidKeyException e) {
            logger.error("AES加密失败:" + e.getMessage());
            return null;
        } catch (BadPaddingException e) {
            logger.error("AES加密失败:" + e.getMessage());
            return null;
        } catch (IllegalBlockSizeException e) {
            logger.error("AES加密失败:" + e.getMessage());
            return null;
        } catch (UnsupportedEncodingException e) {
            logger.error("AES加密失败:" + e.getMessage());
            return null;
        } catch (InvalidAlgorithmParameterException e) {
            logger.error("AES加密失败:" + e.getMessage());
            return null;
        }
    }

    /**
     * @param str
     * @return java.lang.String
     * @Description AES解密
     * @MethodName decryptForAES
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/5/8 10:21
     */
    public static String decryptForAES(String str) {
        try {
            if (StringUtils.isNotEmpty(str)) {
                Cipher cipher = Cipher.getInstance(AES_DEFAULT_CIPHER_ALGORITHM);
                IvParameterSpec iv = new IvParameterSpec(AES_IV_PARAMETER.getBytes());
                cipher.init(Cipher.DECRYPT_MODE,  new SecretKeySpec(AES_KEY.getBytes("ASCII"),AES_KEY_ALGORITHM), iv);//初始化为加密模式密码器
                byte[] result = cipher.doFinal(new BASE64Decoder().decodeBuffer(str));
                str = new String(result, "UTF-8");
            }
            return str;
        } catch (NoSuchPaddingException e) {
            logger.error("AES解密失败:" + e.getMessage());
            return null;
        } catch (NoSuchAlgorithmException e) {
            logger.error("AES解密失败:" + e.getMessage());
            return null;
        } catch (InvalidKeyException e) {
            logger.error("AES解密失败:" + e.getMessage());
            return null;
        } catch (BadPaddingException e) {
            logger.error("AES解密失败:" + e.getMessage());
            return null;
        } catch (IllegalBlockSizeException e) {
            logger.error("AES解密失败:" + e.getMessage());
            return null;
        } catch (UnsupportedEncodingException e) {
            logger.error("AES解密失败:" + e.getMessage());
            return null;
        } catch (IOException e) {
            logger.error("AES解密失败:" + e.getMessage());
            return null;
        } catch (InvalidAlgorithmParameterException e) {
            logger.error("AES解密失败:" + e.getMessage());
            return null;
        }
    }


    /**
     * @param str
     * @param encrptyStr
     * @return java.lang.Boolean
     * @Description AES密码校验
     * @MethodName validateForAES
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/5/8 10:53
     */
    public static Boolean validateForAES(String str, String encrptyStr) {
        return StringUtils.equals(str, decryptForAES(encrptyStr));
    }
    
//    public static void main(String []args) {
//    	String a= decryptForAES("HyG0HX+XBwPgUSo8XOcRCA==");
//    	String b= decryptForAES("HyG0HX+XBwPgUSo8XOcRCA==");
//    	String c= decryptForAES("WRWRxbgDJVtnkv3NfPK7Gw==");
//    	String d="";
//    }

}
