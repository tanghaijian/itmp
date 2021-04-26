package cn.pioneeruniverse.project.service.assetsLibrary.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.ResponseMessageModel;
import cn.pioneeruniverse.common.utils.S3Util;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.ASystemDirectoryDocumentYiRanDao;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocument;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChapters;
import cn.pioneeruniverse.project.service.assetsLibrary.ASystemDirectoryDocumentYiRanService;
import cn.pioneeruniverse.project.vo.SystemDirectoryDocumentVO;

@Service
public class ASystemDirectoryDocumentYiRanImpl implements ASystemDirectoryDocumentYiRanService {

    private static final Logger logger = LoggerFactory.getLogger(ASystemDirectoryDocumentYiRanImpl.class);

    @Autowired
    private ASystemDirectoryDocumentYiRanDao aSystemDirectoryDocumentYiRanDao;

    @Autowired
    private S3Util s3Util;

    @Value("${s3.documentBucket}")
    private String documentBucket;

    //上传S3时，生成文件临时存放的目录
    private static  final  String MARKDOWNPATH = "../projectManage/src/main/resources/";


    /**
     * 系统目录文档查看
     * @param id  目录文档id
     * @return
     */
    @Override
    public SystemDirectoryDocumentVO selectSystemDirectoryDocumentById(Long id, HttpServletRequest request) {
        SystemDirectoryDocumentVO systemDirectoryDocumentVO = aSystemDirectoryDocumentYiRanDao.selectSystemDirectoryDocumentById(id);
        try {
            if (systemDirectoryDocumentVO != null){
                //目录文档人员信息处理
                systemDirectoryDocumentVO.setCreateByName(aSystemDirectoryDocumentYiRanDao.getUserNameById(systemDirectoryDocumentVO.getCreateBy()));
                systemDirectoryDocumentVO.setLastUpdateByName(aSystemDirectoryDocumentYiRanDao.getUserNameById(systemDirectoryDocumentVO.getLastUpdateBy()));
                //获取S3文档内容
                if (StringUtils.isNotBlank(systemDirectoryDocumentVO.getDocumentS3Bucket()) && StringUtils.isNotBlank(systemDirectoryDocumentVO.getDocumentS3Key())){
                    systemDirectoryDocumentVO.setDirectoryDocumentContent(s3Util.getStringByS3(systemDirectoryDocumentVO.getDocumentS3Bucket(), systemDirectoryDocumentVO.getDocumentS3Key()));
                }
                //获取附件信息

            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }

        return systemDirectoryDocumentVO;
    }

    /**
     * 系统目录文档签出
     * @param id
     * @param request
     * @return map key systemDirectory:系统目录
     *                 code 0失败，1成功
     *                 content 文档内容
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map systemDirectoryDocumentSignOffById(Long id, HttpServletRequest request) {
        Map<String,Object> mapValues = new HashMap<>();
        //当前签出文档信息
        SystemDirectoryDocumentVO systemDirectory = aSystemDirectoryDocumentYiRanDao.systemDirectoryDocumentSignOffById(id);
        //获取当前登录用户id
        Long userId  = CommonUtil.getCurrentUserId(request);
        String content = null;
        Integer code = 0;
        //判断是否用临时S3_MONGO_KEY,并获取当前S3文档内容
        try{
           if (StringUtils.isNotBlank(systemDirectory.getChaptersS3Bucket()) && StringUtils.isNotBlank(systemDirectory.getChaptersS3Key())){
                content = s3Util.getStringByS3(systemDirectory.getChaptersS3Bucket(), systemDirectory.getChaptersS3Key());
                systemDirectory.setDirectoryDocumentContent(content);
                //改变签出状态（1:是，2:否）
            }
            code = aSystemDirectoryDocumentYiRanDao.updateDirectoryDocumentSignOffStatusById(id, userId);
            mapValues.put("systemDirectory",systemDirectory);
            mapValues.put("code",code);
            mapValues.put("content",content);
            return mapValues;
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error(e.getMessage(),e);
            mapValues.put("systemDirectory",systemDirectory);
            mapValues.put("code",0);
            mapValues.put("content",content);
            return mapValues;
        }
    }

    /**
     * 暂存
     * @param request
     * @param systemDirectoryDocumentVO：需要保存的内容
     * @return
     * @throws IOException
     */
    @Override
   public ResponseMessageModel addTemporaryStorageById(HttpServletRequest request,SystemDirectoryDocumentVO systemDirectoryDocumentVO) throws IOException{
        ResponseMessageModel messageModel = new ResponseMessageModel();
        Integer code = 0;
        String msg = this.writeTxtFile(systemDirectoryDocumentVO);  //s3内容上传
        String htmlMsg = this.writHtmlFile(systemDirectoryDocumentVO); //s3html内容上传
        if(!msg.equals("error") && !htmlMsg.equals("error")){
            systemDirectoryDocumentVO.getLastUpdateDate();
            Long userId = CommonUtil.getCurrentUserId(request);
            systemDirectoryDocumentVO.setLastUpdateBy(userId);
           //暂存
            code = aSystemDirectoryDocumentYiRanDao.addTemporaryStorageById(systemDirectoryDocumentVO);
            messageModel.setCode(code);
            messageModel.setMsg(code);
            Map mapValues = new HashMap();
            mapValues.put("currentUserId",userId);
            messageModel.setResult(mapValues);
            return messageModel;
        }
        messageModel.setCode(code);
        messageModel.setMsg(code);
        return messageModel;
    }


    /**
     * 提交
     * @param request
     * @param systemDirectoryDocumentVO 存放的文档ID
     * @return
     * @throws IOException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseMessageModel directoryDocumentSubmitById(HttpServletRequest request,SystemDirectoryDocumentVO systemDirectoryDocumentVO){
        ResponseMessageModel messageModel = new ResponseMessageModel();
        Integer code = 0;
        try{
            Timestamp lastUpdateDate = new Timestamp(System.currentTimeMillis());
            Long lastUpdateId = CommonUtil.getCurrentUserId(request);
            String msg = this.writeTxtFile(systemDirectoryDocumentVO);  //s3内容上传
            String htmlMsg = this.writHtmlFile(systemDirectoryDocumentVO); //s3html内容上传
            if(!msg.equals("error") && !htmlMsg.equals("error")){
                systemDirectoryDocumentVO.setLastUpdateDate(lastUpdateDate.toString());
                systemDirectoryDocumentVO.setLastUpdateBy(lastUpdateId);
                //系统目录文档章节表增加   文档版本 =  文档版本+1  章节版本 = 章节版本 +1
                //获取文档版本
                TblSystemDirectoryDocument tblSystemDirectoryDocument = aSystemDirectoryDocumentYiRanDao.selectDocumentVersionById(systemDirectoryDocumentVO.getSystemDirectoryDocumentId());
//                systemDirectoryDocumentVO.setDocumentVersion(tblSystemDirectoryDocument.getDocumentVersion());

            //系统目录文档历史增加
//                tblSystemDirectoryDocument.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
//                tblSystemDirectoryDocument.setLastUpdateDate(lastUpdateDate);
                if (tblSystemDirectoryDocument.getDocumentVersion() > 0){
                    aSystemDirectoryDocumentYiRanDao.insertSystemDirectoryDocumentHistory(tblSystemDirectoryDocument);
                    //系统目录文档章节信息历史数据增加
                    TblSystemDirectoryDocumentChapters directoryDocumentChaptersById = aSystemDirectoryDocumentYiRanDao.getDirectoryDocumentChaptersById(systemDirectoryDocumentVO.getId());
//                    directoryDocumentChaptersById.setLastUpdateDate(lastUpdateDate);
//                    directoryDocumentChaptersById.setChaptersS3Key(systemDirectoryDocumentVO.getChaptersS3Key());
//                    directoryDocumentChaptersById.setChaptersS3Key2(systemDirectoryDocumentVO.getChaptersS3Key2());
//                    directoryDocumentChaptersById.setChaptersS3Bucket(systemDirectoryDocumentVO.getChaptersS3Bucket());
                    aSystemDirectoryDocumentYiRanDao.insertDirectoryDocumentHistory(directoryDocumentChaptersById);

                }
                  //提交
                aSystemDirectoryDocumentYiRanDao.directoryDocumentSubmitById(systemDirectoryDocumentVO);

                //文档版本+1
            aSystemDirectoryDocumentYiRanDao.updateDocumentVersionById(systemDirectoryDocumentVO.getSystemDirectoryDocumentId(), lastUpdateDate, lastUpdateId);
                code = 1;
                }
           }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error(e.getMessage(),e);
            messageModel.setCode(0);
            messageModel.setMsg(0);
            return messageModel;
        }
        messageModel.setCode(code);
        messageModel.setMsg(code);
        return messageModel;
    }

    /**
     *  取消签出
     * @param id 章节ID
     * @return
     */
    @Override
    public ResponseMessageModel cancel(Long id) {
        ResponseMessageModel messageModel = new ResponseMessageModel();
        Integer code = 0;
        try{
            code = aSystemDirectoryDocumentYiRanDao.cancelById(id);
            messageModel.setCode(code);
            messageModel.setMsg(code);
            return messageModel;
        }catch (Exception e){
            messageModel.setCode(0);
            messageModel.setMsg(0);
            return messageModel;
        }
    }


    /**
     * 内容写入文档并上传文件返回DocumentS3Key
     * @param systemDirectoryDocument
     * @return String :新的文件名
     * @throws IOException
     */
    private String writeTxtFile(SystemDirectoryDocumentVO systemDirectoryDocument) throws IOException {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos = null;
        PrintWriter pw = null;
        String mongoKey = null;

        try{
            File file= new File(MARKDOWNPATH);
            if(!file.exists()){
                file.mkdirs();
            }
            String fileNameOld = systemDirectoryDocument.getId().toString(); //文件名
            fileNameOld = fileNameOld.substring(fileNameOld.lastIndexOf("\\") + 1);
            file = new File(file+"\\"+fileNameOld);// 相对路径，如果没有则要建立一个新的output。txt文件
            file.createNewFile(); // 创建新文件

            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buf = new StringBuffer();

            buf.append(systemDirectoryDocument.getContent());

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buf.toString().toCharArray());
            pw.flush();

            mongoKey = s3Util.putObject(documentBucket, fileNameOld, fis,".txt");
            systemDirectoryDocument.setChaptersS3Key(mongoKey);
            systemDirectoryDocument.setChaptersTempS3Key(mongoKey);
            systemDirectoryDocument.setChaptersS3Bucket(documentBucket);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return mongoKey;
    }

    /**
     * 
    * @Title: writHtmlFile
    * @Description: 上传保存html
    * @author author
    * @param systemDirectoryDocument
    * @return String 新的文件名
    * @throws IOException
    * @throws
     */
    private String writHtmlFile(SystemDirectoryDocumentVO systemDirectoryDocument) throws IOException {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos = null;
        PrintWriter pw = null;
        String htmlMongoKey = null;

        try{
            File file= new File(MARKDOWNPATH);
            if(!file.exists()){
                file.mkdirs();
            }
            String fileNameOld = systemDirectoryDocument.getId()+""; //文件名
            fileNameOld = fileNameOld.substring(fileNameOld.lastIndexOf("\\") + 1);
            file = new File(file+"\\"+fileNameOld);// 相对路径，如果没有则要建立一个新的output。txt文件
            file.createNewFile(); // 创建新文件

            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buf = new StringBuffer();

            buf.append(systemDirectoryDocument.getContentHtml());

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buf.toString().toCharArray());
            pw.flush();

            htmlMongoKey = s3Util.putObject(documentBucket, fileNameOld, fis,".html");
            systemDirectoryDocument.setChaptersS3Key2(htmlMongoKey);
            systemDirectoryDocument.setChaptersTempS3Key2(htmlMongoKey);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return "error";
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return htmlMongoKey;
    }



}
