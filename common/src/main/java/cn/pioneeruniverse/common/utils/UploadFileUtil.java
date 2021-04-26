package cn.pioneeruniverse.common.utils;

import cn.pioneeruniverse.common.dto.TblAttachementInfoDTO;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.*;

/**
 * Description: 上传文件
 * Author:liushan
 * Date: 2019/2/25 下午 2:39
 */
public class UploadFileUtil {

    public final static Logger logger = LoggerFactory.getLogger(UploadFileUtil.class);

    /**
     * 上传单个文件
     *
     * @param file 本地文件
     * @param bucketName 上传文件的桶名
     * @return TblAttachementInfoDTO 上传文件存储数据
     * @throws Exception
     */
    public static TblAttachementInfoDTO updateFile(File file,S3Util s3Util,String bucketName) throws Exception {
        TblAttachementInfoDTO attachementInfoDTO = new TblAttachementInfoDTO();
        try {
            InputStream inputStream = new FileInputStream(file);
            String fileName = file.getName();
            String oldFileName = fileName.substring(0, fileName.lastIndexOf("."));
            fileName = fileName.substring(file.getName().lastIndexOf(File.separator) + 1);
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);//后缀名
            String keyname = s3Util.putObject(bucketName, oldFileName, inputStream);
            attachementInfoDTO.setFileS3Key(keyname);
            attachementInfoDTO.setFileS3Bucket(bucketName);
            attachementInfoDTO.setFilePath(attachementInfoDTO.getFilePath() + bucketName +File.separator+fileName);
            attachementInfoDTO.setFileNameOld(fileName);
            attachementInfoDTO.setFileType(fileType);
            attachementInfoDTO.setCreateBy(1L);
            attachementInfoDTO.setCreateDate(new Timestamp(new Date().getTime()));
            attachementInfoDTO.setLastUpdateBy(1L);
            attachementInfoDTO.setLastUpdateDate(new Timestamp(new Date().getTime()));
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return attachementInfoDTO;
    }



    /**
     * 上传单个文件
     * @author liushan
     * @param file 文件 MultipartFile文件
     * @param bucketName 桶名
     * @return  上传文件存储数据
     * @throws Exception
     */
    public static TblAttachementInfoDTO updateMultipartFile(MultipartFile file, S3Util s3Util, String bucketName, HttpServletRequest request) throws Exception {
        TblAttachementInfoDTO attachementInfoDTO = new TblAttachementInfoDTO();
        Long userId = CommonUtil.getCurrentUserId(request);
        try {
            Map map = new HashMap();
            String keyName = null;
            InputStream inputStream = file.getInputStream();
            map = new HashMap<String, Object>();
            String fileType = file.getOriginalFilename()
                    .substring(file.getOriginalFilename().lastIndexOf(".") + 1);// 后缀名

            String fileNameOld = file.getOriginalFilename();
            if (BrowserUtil.isMSBrowser(request)) {
                fileNameOld = fileNameOld.substring(fileNameOld.lastIndexOf("\\")+1);
            }
//            Random random = new Random();
//            String i = String.valueOf(random.nextInt());
            keyName = s3Util.putFileUpload(bucketName, UUID.randomUUID().toString(), inputStream, file.getContentType());
//            keyName = s3Util.putFileUpload(s3Util.getDevTaskBucket(), i, inputStream, fileType);

//            String fileName = file.getOriginalFilename();
//            fileName = fileName.substring(fileName.lastIndexOf(File.separatorChar) + 1);
//            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);//后缀名
//            String oldFileName = fileName.substring(0, fileName.lastIndexOf("."));
////            keyName = s3Util.putObject(bucketName, oldFileName, file.getInputStream());

            attachementInfoDTO.setFileS3Bucket(bucketName);
            attachementInfoDTO.setFileS3Key(keyName.equals("error") ? "" : keyName);
            attachementInfoDTO.setFilePath(attachementInfoDTO.getFilePath() + bucketName + File.separatorChar +new Date().getTime()+"-"+ fileNameOld);
            attachementInfoDTO.setFileNameOld(fileNameOld);
            attachementInfoDTO.setFileType(fileType);
            attachementInfoDTO.setCreateBy(userId);
            attachementInfoDTO.setCreateDate(new Timestamp(System.currentTimeMillis()));
            attachementInfoDTO.setLastUpdateBy(userId);
            attachementInfoDTO.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
            logger.info("上传文件成功：" + attachementInfoDTO.getFilePath());
        }catch (Exception e){
            logger.error("上传文件失败：" + attachementInfoDTO.getFilePath());
            throw new Exception(e.getMessage());
        }
        return attachementInfoDTO;
    }


    /**
     * 说明：根据指定URL将文件下载到指定目标位置
     * @param urlPath 下载路径
     * @return 返回下载文件
     */
    public static HttpURLConnection downloadFile(String urlPath) {
        HttpURLConnection httpURLConnection = null;
        JiraUtil jiraUtil = SpringContextHolder.getBean(JiraUtil.class);
        try {
            //统一资源
            URL url = new URL(urlPath);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            // http的连接类
            httpURLConnection = (HttpURLConnection) urlConnection;
            //设置超时
            httpURLConnection.setConnectTimeout(1000*5);
            httpURLConnection.setDoInput(true);

            //设置请求方式，默认是GET
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
            httpURLConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            httpURLConnection.setRequestProperty("User-Agent", " Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36");

            String user = jiraUtil.getJiraUsername();
            String pwd = jiraUtil.getJiraPassword();
            String auth = user+":"+pwd;
            //对其进行加密
            byte[] rel = Base64.encodeBase64(auth.getBytes());
            String res = new String(rel);

            //设置认证属性
            httpURLConnection.setRequestProperty("Authorization","Basic " + res);
            httpURLConnection.connect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("文件下载失败！");
        }
        return httpURLConnection;
    }
}
