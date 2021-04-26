package cn.pioneeruniverse.common.utils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * 文件服务器上传、下载工具
 * 
 * @author TC1118
 * 
 */
public class FtpUtil {

    private static Logger log = LoggerFactory.getLogger(FtpUtil.class);

//    private static String proPath = "/config.properties";

    private static String ftpServerUrl;

    private static String ftpUserName;

    private static String ftpPassword;

    private static int ftpPort;

    /**
     * utf-8
     */
    private static String defaultEncoding = "GBK";

//    static {
//        InputStream is = FtpUtil.class.getResourceAsStream(proPath);
//        Properties pro = new Properties();
//        try {
//            pro.load(is);
//            serverUrl = pro.getProperty("serverUrl");
//            userName = pro.getProperty("userName");
//            password = pro.getProperty("password");
//            port = Integer.parseInt(pro.getProperty("port"));
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//    }
    
    public static String getServerUrl() {
        return ftpServerUrl;
    }

    public static void setServerUrl(String serverUrl) {
        FtpUtil.ftpServerUrl = serverUrl;
    }   

    public static String getFtpUserName() {
		return ftpUserName;
	}

	public static void setFtpUserName(String ftpUserName) {
		FtpUtil.ftpUserName = ftpUserName;
	}

	public static String getFtpPassword() {
		return ftpPassword;
	}

	public static void setFtpPassword(String ftpPassword) {
		FtpUtil.ftpPassword = ftpPassword;
	}

	public static int getFtpPort() {
		return ftpPort;
	}

	public static void setFtpPort(int ftpPort) {
		FtpUtil.ftpPort = ftpPort;
	}

	/**
     * 以流的方式下载文件
     * 
     * @param sourceFile
     *            源文件全路径名称
     * @return
     * @throws Exception
     */
    @Deprecated
    public static InputStream downloadForStream(String sourceFile) throws Exception {
        return null;
    }

    /**
     * 以流的方式下载文件
     * 
     * @param sourceFile
     *            源文件全路径名称
     * @return
     * @throws Exception
     */
    public static byte[] downloadForBytes(String sourceFile) throws Exception {
        if (sourceFile == null) {
            return null;
        } else {
            sourceFile = sourceFile.replace("\\", "/");
        }
        FTPClient client = new FTPClient();
        InputStream is = null;
        try {
            login(client);
            String sourcePath = sourceFile.substring(0, sourceFile.lastIndexOf("/"));
            client.changeWorkingDirectory(new String(sourcePath.getBytes("GBK"), "iso-8859-1"));
            String fileName = sourceFile.substring(sourcePath.length() + 1);
            is = client.retrieveFileStream(new String(fileName.getBytes("GBK"), "iso-8859-1"));

            byte[] bytes = null;
            byte[] temp2 = new byte[10240];
            byte[] temp1 = null;
            int size = 0;
            while ((size = is.read(temp2)) > 0) {
                if (bytes == null) {
                    bytes = new byte[size];
                    System.arraycopy(temp2, 0, bytes, 0, size);
                } else {
                    temp1 = bytes;
                    bytes = new byte[bytes.length + size];
                    System.arraycopy(temp1, 0, bytes, 0, temp1.length);
                    System.arraycopy(temp2, 0, bytes, temp1.length, size);
                }
            }
            return bytes;
        } finally {
            if (is != null)
                is.close();
            
            
          /*  if (!client.completePendingCommand()) {
                return null;
            }*/
            
            logout(client);
        }
    }

    /**
     * 以流的方式下载文件
     * 
     * @param sourceFile
     *            源文件全路径名称
     * @return
     * @throws Exception
     */
    public static void downloadForStream(OutputStream os, String sourceFile) throws Exception {
        if (sourceFile == null) {
            return;
        } else {
            sourceFile = sourceFile.replace("\\", "/");
        }
        FTPClient client = new FTPClient();
        InputStream is = null;
        try {
            login(client);
            String sourcePath = sourceFile.substring(0, sourceFile.lastIndexOf("/"));
            client.changeWorkingDirectory(new String(sourcePath.getBytes("GBK"), "iso-8859-1"));
            String fileName = sourceFile.substring(sourcePath.length() + 1);
            is = client.retrieveFileStream(new String(fileName.getBytes("GBK"), "iso-8859-1"));

            byte[] temp = new byte[10240];
            int size = 0;
            while ((size = is.read(temp)) > 0) {
                os.write(temp, 0, size);
            }
        } finally {
            if (is != null)
                is.close();
            
           /* if (!client.completePendingCommand()) {
                return;
            }*/
            
            logout(client);
        }
    }

    
    
    public static Boolean downloadForFileByCharSet(String sourceFile, String targetFile,String charSetName) throws Exception {
    	Boolean boo =false;
        if (targetFile == null) {
        	boo = false;
        } else {
            targetFile = targetFile.replace("\\", "/");
        }
        if (sourceFile == null) {
        	boo = false;
        } else {
            sourceFile = sourceFile.replace("\\", "/");
        }
        FTPClient client = new FTPClient();
        BufferedOutputStream os = null;
        try {
            login(client);
            String sourcePath = sourceFile.substring(0, sourceFile.lastIndexOf("/"));
            client.changeWorkingDirectory(new String(sourcePath.getBytes(charSetName), "iso-8859-1"));
            os = new BufferedOutputStream(new FileOutputStream(targetFile));
            String fileName = sourceFile.substring(sourcePath.length() + 1);
            FTPFile[] ftpFiles = client.listFiles();
            for(FTPFile ftpFile:ftpFiles){
            	if((fileName).equals(ftpFile.getName())){
            		client.retrieveFile(new String(fileName.getBytes(charSetName), "iso-8859-1"), os);
            		boo = true;
            		break;
            	}
            	else
            		boo = false;
            }
            }catch(Exception e){
            	e.printStackTrace();
            } finally {
            if (os != null)
                os.close();
            logout(client);
        }
            return boo;
    }
    
    
    
    /**
     * 下载文件
     * 
     * @param sourceFile
     *            ftp服务器全路径文件名称
     * @param targetFile
     *            全路径文件名称
     */
    public static Boolean downloadForFile(String sourceFile, String targetFile) throws Exception {
    	Boolean boo =false;
        if (targetFile == null) {
        	boo = false;
        } else {
            targetFile = targetFile.replace("\\", "/");
        }
        if (sourceFile == null) {
        	boo = false;
        } else {
            sourceFile = sourceFile.replace("\\", "/");
        }
        FTPClient client = new FTPClient();
        BufferedOutputStream os = null;
        try {
            login(client);
            String sourcePath = sourceFile.substring(0, sourceFile.lastIndexOf("/"));
            client.changeWorkingDirectory(new String(sourcePath.getBytes("GBK"), "iso-8859-1"));
            os = new BufferedOutputStream(new FileOutputStream(targetFile));
            String fileName = sourceFile.substring(sourcePath.length() + 1);
            if (client.listFiles(fileName).length != 0) {
            	client.retrieveFile(new String(fileName.getBytes("GBK"), "iso-8859-1"), os);
            } else {
            	boo = false;
            }
            boo = true;
            }catch(Exception e){
            	e.printStackTrace();
            } finally {
            if (os != null)
                os.close();
            logout(client);
        }
            return boo;
    }

    /**
     * 上传文件
     * 
     * @param bytes
     *            以文件流的方式上传文件
     * @param targetFile
     *            全路径文件名称
     */
    public static void uploadToFtpServer(FileInputStream fis, String targetFile) throws Exception {
        if (targetFile == null) {
            return;
        } else {
            targetFile = targetFile.replace("\\", "/");
        }
        FTPClient client = new FTPClient();
        try {
            login(client);
            String targetPath = targetFile.substring(0, targetFile.lastIndexOf("/"));
            StringTokenizer st = new StringTokenizer(targetPath, "/");
            while (st.hasMoreElements()) {
                String path = new String(st.nextToken().getBytes("GBK"), "iso-8859-1");
                boolean b = client.changeWorkingDirectory(path);
                if (!b) {
                    client.makeDirectory(path);
                    client.changeWorkingDirectory(path);
                }
            }
            String fileName = targetFile.substring(targetPath.length() + 1);
            client.storeFile(new String(fileName.getBytes("GBK"), "iso-8859-1"), fis);
        } finally {
            logout(client);
        }
    }
    
    public static void uploadVisioToFtpServer(InputStream is, String targetFile) throws Exception {
        if (targetFile == null) {
            return;
        } else {
            targetFile = targetFile.replace("\\", "/");
        }
        FTPClient client = new FTPClient();
        try {
            login(client);
            String targetPath = targetFile.substring(0, targetFile.lastIndexOf("/"));
            StringTokenizer st = new StringTokenizer(targetPath, "/");
            while (st.hasMoreElements()) {
                String path = new String(st.nextToken().getBytes("GBK"), "iso-8859-1");
                boolean b = client.changeWorkingDirectory(path);
                if (!b) {
                    client.makeDirectory(path);
                    client.changeWorkingDirectory(path);
                }
            }
            String fileName = targetFile.substring(targetPath.length() + 1);
            client.storeFile(new String(fileName.getBytes("GBK"), "iso-8859-1"), is);
        } finally {
            logout(client);
        }
    }

    /**
     * 上传文件
     * 
     * @param bytes
     *            以字节流的方式上传文件
     * @param targetFile
     *            全路径文件名称
     */
    public static void uploadToFtpServer(byte[] bytes, String targetFile) throws Exception {
        if (targetFile == null) {
            return;
        } else {
            targetFile = targetFile.replace("\\", "/");
        }
        FTPClient client = new FTPClient();
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(bytes);
            login(client);
            String targetPath = targetFile.substring(0, targetFile.lastIndexOf("/"));
            StringTokenizer st = new StringTokenizer(targetPath, "/");
            while (st.hasMoreElements()) {
                String path = new String(st.nextToken().getBytes("GBK"), "iso-8859-1");
                boolean b = client.changeWorkingDirectory(path);
                if (!b) {
                    client.makeDirectory(path);
                    client.changeWorkingDirectory(path);
                }
            }
            String fileName = targetFile.substring(targetPath.length() + 1);
            client.storeFile(new String(fileName.getBytes("GBK"), "iso-8859-1"), is);
        } finally {
            if (is != null)
                is.close();
            logout(client);
        }
    }

    /**
     * 上传文件
     * 
     * @param sourceFile
     *            本地全路径文件名称
     * @param targetFile
     *            全路径文件名称
     */
    public static void uploadToFtpServer(String sourceFile, String targetFile) throws Exception {
        if (sourceFile == null) {
            return;
        } else {
            sourceFile = sourceFile.replace("\\", "/");
        }
        if (targetFile == null) {
            return;
        } else {
            targetFile = targetFile.replace("\\", "/");
        }
        FTPClient client = new FTPClient();
        InputStream is = null;
        FileInputStream fileInputStream = null;
        try {
            login(client);
            fileInputStream =  new FileInputStream(sourceFile);
            is = new BufferedInputStream(fileInputStream);
            String targetPath = targetFile.substring(0, targetFile.lastIndexOf("/"));
            StringTokenizer st = new StringTokenizer(targetPath, "/");
            while (st.hasMoreElements()) {
                String path = new String(st.nextToken().getBytes("GBK"), "iso-8859-1");
                boolean b = client.changeWorkingDirectory(path);
                if (!b) {
                    client.makeDirectory(path);
                    client.changeWorkingDirectory(path);
                }
            }
            String fileName = targetFile.substring(targetPath.length() + 1);
            client.storeFile(new String(fileName.getBytes("GBK"), "iso-8859-1"), is);
        } finally {
            if (is != null) {
            	  is.close();
            }
             if(fileInputStream!=null) {
            	 fileInputStream.close();
             }
            logout(client);
        }
    }

    /**
     * 删除文件
     * 
     * @param fullFileName
     * @throws Exception
     */
    public static void deleteFile(String fullFileName) throws Exception {
        if (fullFileName == null) {
            return;
        } else {
            fullFileName = fullFileName.replace("\\", "/");
        }
        FTPClient client = new FTPClient();
        try {
            login(client);
            client.deleteFile(new String(fullFileName.getBytes("GBK"), "iso-8859-1"));
        } finally {
            logout(client);
        }
    }

    /**
     * 移动文件
     * 
     * @param sourceFile
     * @param targetFile
     * @throws Exception
     */
    public static void moveFile(String sourceFile, String targetFile) throws Exception {
        if (sourceFile == null) {
            return;
        } else {
            sourceFile = sourceFile.replace("\\", "/");
        }
        if (targetFile == null) {
            return;
        } else {
            targetFile = targetFile.replace("\\", "/");
        }
        FTPClient client = new FTPClient();
        try {
            login(client);
            String targetPath = targetFile.substring(0, targetFile.lastIndexOf("/"));
            StringTokenizer st = new StringTokenizer(targetPath, "/");
            while (st.hasMoreElements()) {
                String path = new String(st.nextToken().getBytes("GBK"), "iso-8859-1");
                boolean b = client.changeWorkingDirectory(path);
                if (!b) {
                    client.makeDirectory(path);
                    client.changeWorkingDirectory(path);
                }
            }
            String sourcePath = sourceFile.substring(0, sourceFile.lastIndexOf("/"));
            boolean b = client.changeWorkingDirectory(new String(sourcePath.getBytes("GBK"), "iso-8859-1"));
            System.out.println("cwd: " + b);
            String fileName = sourceFile.substring(sourcePath.length() + 1);
            client.rename(new String(fileName.getBytes("GBK"), "iso-8859-1"), new String(
                    targetFile.getBytes("GBK"), "iso-8859-1"));
        } finally {
            logout(client);
        }
    }

    /**
     * 取出目录下所有文件的列表
     * 
     * @param client
     * @param dir
     * @return
     * @throws IOException
     */
    public static List<String> getFileNameList(String dir) throws Exception {
        List<String> fileNameList = new ArrayList<String>();
        FTPClient client = new FTPClient();
        try {
            login(client);
            client.changeWorkingDirectory(new String(dir.getBytes("GBK"), "iso-8859-1"));
            FTPFile[] files = client.listFiles();
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    if (FTPFile.FILE_TYPE == files[i].getType()) {
                        fileNameList.add(files[i].getName());
                    }
                }
            }
            return fileNameList;
        } catch (IOException e) {
            return fileNameList;
        } finally {
            logout(client);
        }
    }
    
    
    /**
     * 获取目录下所有文件，包括文件夹
     * author：zhongbing.test
     * @param dir
     * @return
     * @throws Exception
     * date：2016-6-2
     *
     */
    public static List<Map<String,Object>> getAllFileList(String dir) throws Exception{
        List<Map<String, Object>> fileList = new ArrayList<Map<String,Object>>();
        FTPClient client = new FTPClient();
        try {
            login(client);
            client.changeWorkingDirectory(new String(dir.getBytes("GBK"), "iso-8859-1"));
            FTPFile[] files = client.listFiles();
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                	    if(files[i].isDirectory() && files[i].getName().startsWith("."))
                	    	continue;
                	    Map<String,Object> map = new HashMap<String,Object>();
                	    map.put("fileName", files[i].getName());
                	    map.put("fileType", files[i].getType());
                	    fileList.add(map);
                }
            }
            return fileList;
        } catch (IOException e) {
            return fileList;
        } finally {
            logout(client);
        }
    }

    private static void login(FTPClient ftp) throws Exception {
        ftp.connect(ftpServerUrl, ftpPort);
        int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            log.error("ftp连接失败！");
            throw new Exception("ftp连接失败。");
        }
        ftp.setControlEncoding(defaultEncoding);
        FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
        conf.setServerLanguageCode("zh");
        ftp.login(ftpUserName, ftpPassword);
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            log.error("ftp登录失败！");
            throw new Exception("ftp登录失败。");
        }
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        ftp.enterLocalPassiveMode();
    }

    public static void logout(FTPClient client) throws Exception {
        try {
            client.logout();
        } finally {
            if (client.isConnected()) {
                client.disconnect();
            }
        }
    }

//    public static void main(String[] args) throws Exception {
//        int len = 500;
//        final CountDownLatch doneSignal = new CountDownLatch(len);
//        for (int i = 0; i < len; i++) {
////            ThreadPool.getThreadPool().execute(new MyThread(doneSignal, i));
//        }
//
//        try {
//            doneSignal.await();
//            System.out.println("-----------------------");
//        } catch (InterruptedException e) {
//            log.error(e.getMessage(), e);
//        }
//
//    }
    
}

