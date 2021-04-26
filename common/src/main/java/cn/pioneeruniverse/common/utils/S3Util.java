package cn.pioneeruniverse.common.utils;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Component("S3Util")
@Lazy
public class S3Util {

    private static Logger log = LoggerFactory.getLogger(S3Util.class);
    /**
     * @param args
     */

    @Value("${s3.accessKeyID}")
    private String accessKey;
    @Value("${s3.secretAccessKey}")
    private String secretKey;
    @Value("${s3.endpoint}")
    private String endpoint;

    @Value("${s3.projectBucket}")
    private String projectBucket;
    @Value("${s3.requirementBucket}")
    private String requirementBucket;
    @Value("${s3.devTaskBucket}")
    private String devTaskBucket;
    @Value("${s3.testTaskBucket}")
    private String testTaskBucket;
    @Value("${s3.defectBucket}")
    private String defectBucket;


    public String getProjectBucket() {
        return projectBucket;
    }

    public String getRequirementBucket() {
        return requirementBucket;
    }

    public String getDevTaskBucket() {
        return devTaskBucket;
    }

    public String getTestTaskBucket() {
        return testTaskBucket;
    }

    public String getDefectBucket() {
        return defectBucket;
    }
    
    /**
     * 上传字符串到string
     *
     * @param bucketName 存储桶
     * @param jobKeyName   文件名
     * @return keyName 附件上传后的key值
     */
    public String putObjectLogs(String bucketName, String jobKeyName, String content) {
        System.setProperty(SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "true");

        AWSCredentials credentials = null;
        try {
            credentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "")).build();

            s3.putObject(bucketName,jobKeyName,content);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "error";
        }
        return jobKeyName;
    }



    /**
     * 上传附件到bucket
     *
     * @param bucketName 存储桶
     * @param fileName   文件名
     * @return keyName 附件上传后的key值
     */
    public String putObject(String bucketName, String fileName, InputStream inputstream) {
        System.setProperty(SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "true");
        String keyName = "";
        AWSCredentials credentials = null;
        try {
            keyName = fileName + "_" + new Date().getTime()+".txt";
            credentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "")).build();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(((FileInputStream) inputstream).getChannel().size());

            s3.putObject(bucketName, keyName, inputstream, metadata);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "error";
        }
        return keyName;
    }

    public String putObject1(String bucketName, String fileName, InputStream inputstream,Long contentLength) {
        System.setProperty(SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "true");
        String keyName = "";
        AWSCredentials credentials = null;
        try {
            keyName = fileName + "_" + new Date().getTime();
            credentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "")).build();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(contentLength);
            s3.putObject(bucketName, keyName, inputstream, metadata);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return keyName;
    }
    /**
     * 从bucket下载附件
     *
     * @param bucketName 存储桶
     * @param keyName    附件存储key
     * @param fileName   文件名
     */
    public void downObject(String bucketName, String keyName, String fileName, HttpServletResponse response) {
        System.setProperty(SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "true");
        AWSCredentials credentials = null;
        try {
            credentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "")).build();

            S3Object object = s3.getObject(new GetObjectRequest(bucketName, keyName));
            S3ObjectInputStream s3is = object.getObjectContent();
            String fileName1 = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName1);
            //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型    
            response.setContentType("multipart/form-data");
            OutputStream os = response.getOutputStream();
            byte[] read_buf = new byte[1024];
            int read_len = 0;
            while ((read_len = s3is.read(read_buf)) != -1) {
                os.write(read_buf, 0, read_len);
                os.flush();
            }
            s3is.close();
            os.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    /**
     * 从bucket下载附件
     *
     * @param bucketName 存储桶
     * @param keyName    附件存储key
     */
    public String getStringByS3(String bucketName, String keyName) {
        String markdown="";
        System.setProperty(SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "true");
        AWSCredentials credentials = null;
        try {
            credentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "")).build();
            S3Object object = s3.getObject(new GetObjectRequest(bucketName, keyName));
            S3ObjectInputStream s3is = object.getObjectContent();
            InputStreamReader reader = new InputStreamReader(s3is);
            StringWriter writer = new StringWriter();
            try {
                //将输入流写入输出流
                char[] buffer = new char[1024];
                int n = 0;
                while (-1 != (n = reader.read(buffer))) {
                    writer.write(buffer, 0, n);
                }
            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (reader != null)
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
             markdown=writer.toString();
             s3is.close();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return markdown;
    }

    /**



    /**
     * 删除对象
     *
     * @param bucketName
     * @param keyName  对象的名称
     */
    public void deleteObject(String bucketName, String keyName) {
        System.setProperty(SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "true");
        AWSCredentials credentials = null;
        try {
            credentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "")).build();

            s3.deleteObject(bucketName, keyName);
        } catch (AmazonServiceException e) {
            log.error(e.getMessage(), e);
            System.exit(1);
        }
    }



    /**
     * 上传附件到bucket
     *
     * @param bucketName 存储桶
     * @param fileName   文件名
     *  @param type   类型(txt/html)
     * @return keyName 附件上传后的key值
     */
    public String putObject(String bucketName, String fileName, InputStream inputstream,String type) {
        System.setProperty(SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "true");
        String keyName = "";
        AWSCredentials credentials = null;
        try {
            keyName = fileName + "_" + new Date().getTime()+type;
            credentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "")).build();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(((FileInputStream) inputstream).getChannel().size());
            s3.putObject(bucketName, keyName, inputstream, metadata);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "error";
        }
        return keyName;
    }

    /**
     * 上传图片到bucket
     *
     * @param bucketName 存储桶
     * @param fileName   图片名
     * @return keyName 附件上传后的key值
     */
    public String putImage(String bucketName, String fileName, InputStream inputstream) {
        System.setProperty(SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "true");
        String keyName = "";
        AWSCredentials credentials = null;
        try {
            keyName = fileName + "_img";
            credentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "")).build();

            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setContentType("image/"+"jpg");
            metadata.setContentLength(((FileInputStream) inputstream).getChannel().size());
            s3.putObject(bucketName, fileName, inputstream, metadata);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "error";
        }
        return keyName;
    }


    /**
     * s3图片获取
     *
     * @param bucketName 存储桶
     * @param fileName   图片名
     * @return keyName 附件上传后的key值
     */
    public String getImage(String bucketName, String fileName) {
        System.setProperty(SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "true");
        URL fileUrl = null;
        AWSCredentials credentials = null;
        try {
            credentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "")).build();
            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
                    bucketName, fileName).withMethod(HttpMethod.GET);
            fileUrl = s3.generatePresignedUrl(generatePresignedUrlRequest);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "error";
        }
        return fileUrl.toString();
    }




    /**
     * 上传附件到bucket
     * @return keyName 附件上传后的key值
     */
    public String putFileUpload(String bucketName, String fileName, InputStream inputstream, String type) {
        System.setProperty(SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "true");
        String keyName = "";
        AWSCredentials credentials = null;
        try {
            keyName = fileName;
            credentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "")).build();

            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setContentType("text/tab-separated-values;charset=UTF-8");
//            metadata.setContentType(type);
            metadata.setContentLength(((FileInputStream) inputstream).getChannel().size());
            s3.putObject(bucketName, keyName, inputstream, metadata);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "error";
        }
        return keyName;
    }


    /**
     * 下载图片
     * @param bucketName
     * @param keyName
     * @param response
     */
    public void downImgByStream(String bucketName, String keyName,HttpServletResponse response) {
        System.setProperty(SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "true");
        AWSCredentials credentials = null;
        OutputStream os = null;
        S3ObjectInputStream s3is = null;
        try {
            credentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "")).build();

            S3Object object = s3.getObject(new GetObjectRequest(bucketName, keyName));
            s3is = object.getObjectContent();
            response.setContentType("image/jpeg");
            os = response.getOutputStream();
            byte[] read_buf = new byte[1024];
            int read_len = 0;
            while ((read_len = s3is.read(read_buf)) != -1) {
                os.write(read_buf, 0, read_len);
                os.flush();
            }
            s3is.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }finally {
            if(s3is != null)
                try {
                    s3is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if(os != null)
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }



    /**
     * 上传图片到bucket（自定义类型）
     * @param bucketName 存储桶
     * @param fileName   图片名
     * @return keyName 附件上传后的key值
     */
    public String putImage(String bucketName, String fileName, InputStream inputstream,String type) {
        System.setProperty(SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "true");
        AWSCredentials credentials = null;
        try {
            credentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "")).build();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(((FileInputStream) inputstream).getChannel().size());
//            metadata.setContentType("image/"+type);
            s3.putObject(bucketName, fileName, inputstream, metadata);
            return fileName;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "error";
        }

    }



    /**
     * 上传附件到bucket
     *
     * @param bucketName 存储桶
     * @param fileName   文件名
     * @return keyName 附件上传后的key值
     */
    public String uploadFile(String bucketName, String fileName, InputStream inputstream) {
        System.setProperty(SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "true");
        String keyName = "";
        AWSCredentials credentials = null;
        try {
            keyName =  new Date().getTime() + "_"+fileName;
            credentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "")).build();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(((FileInputStream) inputstream).getChannel().size());

            s3.putObject(bucketName, keyName, inputstream, metadata);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "error";
        }
        return keyName;
    }


    public void downFile(String bucketName, String keyName, String fileName, HttpServletResponse response) {
        System.setProperty(SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "true");
        AWSCredentials credentials = null;
        try {
            credentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "")).build();

            S3Object object = s3.getObject(new GetObjectRequest(bucketName, keyName));
            S3ObjectInputStream s3is = object.getObjectContent();
            String fileName1 = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName1);
            //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType("multipart/form-data");
//            response.setCharacterEncoding("utf-8");
            OutputStream os = response.getOutputStream();
            byte[] read_buf = new byte[1024];
            int read_len = 0;
            while ((read_len = s3is.read(read_buf)) != -1) {
                os.write(read_buf, 0, read_len);
                os.flush();
            }
            s3is.close();
            os.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}