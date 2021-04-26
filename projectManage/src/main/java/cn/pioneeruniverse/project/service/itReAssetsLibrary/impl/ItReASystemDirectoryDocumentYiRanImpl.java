package cn.pioneeruniverse.project.service.itReAssetsLibrary.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSON;

import cn.pioneeruniverse.common.databus.DataBusRequestHead;
import cn.pioneeruniverse.common.utils.ResponseMessageModel;
import cn.pioneeruniverse.common.utils.S3Util;
import cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary.ReASystemDirectoryDocumentYiRanDao;
import cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary.ReSystemDirectoryDocumentDao;
import cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary.ReTblSystemDirectoryDocumentChaptersRequirementFeatureMapper;
import cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary.ReTblSystemDirectoryDocumentChaptersRequirementMapper;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocument;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChapters;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersRequirement;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersRequirementFeature;
import cn.pioneeruniverse.project.service.itReAssetsLibrary.ItReASystemDirectoryDocumentYiRanService;
import cn.pioneeruniverse.project.vo.SystemDirectoryDocumentVO;

@Service
public class ItReASystemDirectoryDocumentYiRanImpl implements ItReASystemDirectoryDocumentYiRanService {

    private static final Logger logger = LoggerFactory.getLogger(ItReASystemDirectoryDocumentYiRanImpl.class);

    @Autowired
    private ReASystemDirectoryDocumentYiRanDao reASystemDirectoryDocumentYiRanDao;
    @Autowired
    private ReTblSystemDirectoryDocumentChaptersRequirementMapper reTblSystemDirectoryDocumentChaptersRequirementMapper;
    @Autowired
    private ReTblSystemDirectoryDocumentChaptersRequirementFeatureMapper chaptersRequirementFeatureMapper;
    @Autowired
	private ReSystemDirectoryDocumentDao reSystemDirectoryDocumentDao;
    @Autowired
    private S3Util s3Util;
    @Autowired 
    private ItReAssetsLibraryRqServiceImpl itReAssetsLibraryRqServiceImpl;
    @Value("${s3.documentBucket}")
    private String documentBucket;

    private static  final  String MARKDOWNPATH = "../projectManage/src/main/resources/";


    /**
     * 系统目录文档查看
     * @param id  目录文档id
     * @return SystemDirectoryDocumentVO
     */
    @Override
    public SystemDirectoryDocumentVO selectSystemDirectoryDocumentById(Long id, HttpServletRequest request) {
        SystemDirectoryDocumentVO systemDirectoryDocumentVO = reASystemDirectoryDocumentYiRanDao.selectSystemDirectoryDocumentById(Long.valueOf(12345));
        try {
            if (systemDirectoryDocumentVO != null){
                //目录文档人员信息处理
                systemDirectoryDocumentVO.setCreateByName(reASystemDirectoryDocumentYiRanDao.getUserNameById(systemDirectoryDocumentVO.getCreateBy()));
                systemDirectoryDocumentVO.setLastUpdateByName(reASystemDirectoryDocumentYiRanDao.getUserNameById(systemDirectoryDocumentVO.getLastUpdateBy()));
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
     * 系统目录文档章节签出
     * @param id  章节id
     * @param request
     * @return Map<String,Object>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map systemDirectoryDocumentSignOffById(Long id,String userAccount, HttpServletRequest request) {
        Map<String,Object> mapValues = new HashMap<>();
        //当前签出文档信息
        SystemDirectoryDocumentVO systemDirectory = reASystemDirectoryDocumentYiRanDao.systemDirectoryDocumentSignOffById(id);
        //获取当前登录用户id
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();  
        encryptor.setPassword("ccic"); 
      //由于在开放给全流程使用的时候，url中有进行加密，而加密后的+号在传输过程中会变成“ ”，所以此处要转换过来
        userAccount = userAccount.replaceAll(" ","+");
        Long userId  = reASystemDirectoryDocumentYiRanDao.selectUserId(encryptor.decrypt(userAccount));
        String content = null;
        Integer code = 0;
        //判断是否用临时S3_MONGO_KEY,并获取当前S3文档内容
        try{
           if (StringUtils.isNotBlank(systemDirectory.getChaptersS3Bucket()) && StringUtils.isNotBlank(systemDirectory.getChaptersS3Key())){
                content = s3Util.getStringByS3(systemDirectory.getChaptersS3Bucket(), systemDirectory.getChaptersS3Key());
                systemDirectory.setDirectoryDocumentContent(content);
            }
            //改变签出状态（1:是，2:否）
            code = reASystemDirectoryDocumentYiRanDao.updateDirectoryDocumentSignOffStatusById(id, userId);
            //章节信息
            mapValues.put("systemDirectory",systemDirectory);
            //0失败返回，其他表示更新成功
            mapValues.put("code",code);
            //markdown信息
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


//    /**
//     * 系统目录文档签出
//     * @param id
//     * @param request
//     * @return
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public Map systemDirectoryDocumentSignOffById(Long id, HttpServletRequest request) {
//        Map<String,Object> mapValues = new HashMap<>();
//        //当前签出文档信息
//        SystemDirectoryDocumentVO systemDirectory = aSystemDirectoryDocumentYiRanDao.systemDirectoryDocumentSignOffById(id);
//        //获取当前登录用户id
//        Long userId  = CommonUtil.getCurrentUserId(request);
//        String content = null;
//        //判断是否用临时S3_MONGO_KEY,并获取当前S3文档内容
//           try{
//               if (systemDirectory != null && userId.equals(systemDirectory.getCheckoutUserId()) && StringUtils.isNotBlank(systemDirectory.getDocumentS3Bucket()) && StringUtils.isNotBlank(systemDirectory.getChaptersTempS3Key())){
//                   //取MONGO存储KEY(存放临时数据)
//                   content = s3Util.getStringByS3(systemDirectory.getDocumentS3Bucket(), systemDirectory.getChaptersTempS3Key());
//                   systemDirectory.setDirectoryDocumentContent(content);
//                   //改变签出状态（1:是，2:否）
//                   aSystemDirectoryDocumentYiRanDao.updateDirectoryDocumentSignOffStatusById(id, userId);
//                   mapValues.put("systemDirectory",systemDirectory);
//                   mapValues.put("code",1);
//                   mapValues.put("content",content);
//                   return mapValues;
//               }else if (systemDirectory != null && !userId.equals(systemDirectory.getCheckoutUserId()) && StringUtils.isNotBlank(systemDirectory.getDocumentS3Bucket()) && StringUtils.isNotBlank(systemDirectory.getChaptersS3Key())){
//                   content = s3Util.getStringByS3(systemDirectory.getDocumentS3Bucket(), systemDirectory.getChaptersS3Key());
//                   systemDirectory.setDirectoryDocumentContent(content);
//                   //改变签出状态（1:是，2:否）
//                   aSystemDirectoryDocumentYiRanDao.updateDirectoryDocumentSignOffStatusById(id, userId);
//                   mapValues.put("systemDirectory",systemDirectory);
//                   mapValues.put("code",2);
//                   mapValues.put("content",content);
//                   return mapValues;
//               }
//               mapValues.put("systemDirectory",systemDirectory);
//               mapValues.put("code",3);
//               mapValues.put("content",content);
//               return mapValues;
//            }catch (Exception e){
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                logger.error(e.getMessage(),e);
//                mapValues.put("systemDirectory",systemDirectory);
//                mapValues.put("code",0);
//                mapValues.put("content",content);
//               return mapValues;
//            }
//    }

    /**
     * 暂存
     * @param request
     * @param systemDirectoryDocumentVO 章节信息
     * @return ResponseMessageModel
     * @throws IOException
     */
    @Override
   public ResponseMessageModel addTemporaryStorageById(HttpServletRequest request,SystemDirectoryDocumentVO systemDirectoryDocumentVO,String userAccount) throws IOException{
        ResponseMessageModel messageModel = new ResponseMessageModel();
        Integer code = 0;
        String msg = this.writeTxtFile(systemDirectoryDocumentVO);  //s3内容上传
        String htmlMsg = this.writHtmlFile(systemDirectoryDocumentVO); //s3html内容上传
        if(!msg.equals("error") && !htmlMsg.equals("error")){
//            String content = s3Util.getStringByS3(systemDirectoryDocumentVO.getDocumentS3Bucket(), systemDirectoryDocumentVO.getDocumentTempMongoKey());
            systemDirectoryDocumentVO.getLastUpdateDate();
            //获取当前登录用户id
            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();  
            encryptor.setPassword("ccic"); 
            //由于在开放给全流程使用的时候，url中有进行加密，而加密后的+号在传输过程中会变成“ ”，所以此处要转换过来
            userAccount = userAccount.replaceAll(" ","+");
            Long userId  = reASystemDirectoryDocumentYiRanDao.selectUserId(encryptor.decrypt(userAccount));
            systemDirectoryDocumentVO.setLastUpdateBy(userId);
            code = reASystemDirectoryDocumentYiRanDao.addTemporaryStorageById(systemDirectoryDocumentVO);
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
     * @param systemDirectoryDocumentVO 章节信息
     * @return ResponseMessageModel
     * @throws IOException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseMessageModel directoryDocumentSubmitById(HttpServletRequest request,SystemDirectoryDocumentVO systemDirectoryDocumentVO,String userAccount){
  	  ResponseMessageModel messageModel = new ResponseMessageModel();
        Integer code = 0;
        
        try{
      	  Timestamp lastUpdateDate = new Timestamp(System.currentTimeMillis());
      	  Map<String, Object> map= new LinkedHashMap<>();
      	  Map<String, Object> mapAll = new LinkedHashMap<>();
      	  mapAll.put("requestHead",DataBusRequestHead.getRequestHead());
      	  Map<String, Object> mapBody = new HashMap<>();//requestBody
      	  Long id= systemDirectoryDocumentVO.getSystemDirectoryDocumentId();
      	  String msg = this.writeTxtFile(systemDirectoryDocumentVO);  //s3内容上传
      	  String htmlMsg = this.writHtmlFile(systemDirectoryDocumentVO); //s3html内容上传
      	  if(!msg.equals("error") && !htmlMsg.equals("error")){
      		  systemDirectoryDocumentVO.setLastUpdateDate(lastUpdateDate.toString());
      		  StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();  
      		  encryptor.setPassword("ccic"); 
      		  userAccount = userAccount.replaceAll(" ","+");
      		  Long userId=reASystemDirectoryDocumentYiRanDao.selectUserId(encryptor.decrypt(userAccount));
      		  systemDirectoryDocumentVO.setLastUpdateBy(userId);
      		  //系统目录文档章节表增加   文档版本 =  文档版本+1  章节版本 = 章节版本 +1
      		  //获取文档版本
      		  TblSystemDirectoryDocument tblSystemDirectoryDocument = reASystemDirectoryDocumentYiRanDao.selectDocumentVersionById(systemDirectoryDocumentVO.getSystemDirectoryDocumentId());
      		  //获取文档类型code
      		  String typeCode=reSystemDirectoryDocumentDao.selectTypeCode(tblSystemDirectoryDocument.getDocumentType());
                //文档版本+1
      		  reASystemDirectoryDocumentYiRanDao.updateDocumentVersionById(systemDirectoryDocumentVO.getSystemDirectoryDocumentId());
      		  systemDirectoryDocumentVO.setDocumentVersion(tblSystemDirectoryDocument.getDocumentVersion());

      		  //系统目录文档章节信息历史数据增加
      		  TblSystemDirectoryDocumentChapters directoryDocumentChaptersById = reASystemDirectoryDocumentYiRanDao.getDirectoryDocumentChaptersById(systemDirectoryDocumentVO.getId());
      		
      		  directoryDocumentChaptersById.setLastUpdateDate(lastUpdateDate);
      		  directoryDocumentChaptersById.setChaptersS3Key(systemDirectoryDocumentVO.getChaptersS3Key());
      		  directoryDocumentChaptersById.setChaptersS3Key2(systemDirectoryDocumentVO.getChaptersS3Key2());
      		  directoryDocumentChaptersById.setChaptersS3Bucket(systemDirectoryDocumentVO.getChaptersS3Bucket());
      		  reASystemDirectoryDocumentYiRanDao.insertDirectoryDocumentHistory(directoryDocumentChaptersById);
      		  //提交
      		  reASystemDirectoryDocumentYiRanDao.directoryDocumentSubmitById(systemDirectoryDocumentVO);
              //系统目录文档历史增加
      		  tblSystemDirectoryDocument.setDocumentVersion(tblSystemDirectoryDocument.getDocumentVersion());
      		  tblSystemDirectoryDocument.setLastUpdateBy(userId);
      		  tblSystemDirectoryDocument.setLastUpdateDate(lastUpdateDate);
      		  code = reASystemDirectoryDocumentYiRanDao.insertSystemDirectoryDocumentHistory(tblSystemDirectoryDocument);
      		  if(!systemDirectoryDocumentVO.getFeatureCode().equals("")) {
      			  TblSystemDirectoryDocumentChaptersRequirementFeature record=new TblSystemDirectoryDocumentChaptersRequirementFeature();
      			  record.setChaptersVersion(systemDirectoryDocumentVO.getChaptersVersion());
      			  Long  featureId= reSystemDirectoryDocumentDao.selectReFId(systemDirectoryDocumentVO.getFeatureCode());
      			  record.setRequirementFeatureId(featureId);
      			  record.setSystemDirectoryDocumentChaptersId(systemDirectoryDocumentVO.getId());
      			  chaptersRequirementFeatureMapper.insertChaptersRequirementFeature(record);//章节开发任务表
      			  mapBody.put("reqCode",systemDirectoryDocumentVO.getRequirementCode());
               
          	
      			  Map<String, Object> mapReqDoc = new HashMap<>();//ReqDoc
      			  mapReqDoc.put("docKey", id);
      			  mapReqDoc.put("docName", tblSystemDirectoryDocument.getDocumentName());
      			  mapReqDoc.put("docStatus", 1);
      			  mapReqDoc.put("docType", typeCode);
      			  mapReqDoc.put("userAccount", userAccount);
      			  mapBody.put("taskDoc", mapReqDoc);
      			  mapBody.put("reqDoc", "");
        	  }else {
        		  TblSystemDirectoryDocumentChaptersRequirement record=new TblSystemDirectoryDocumentChaptersRequirement();
        		  record.setChaptersVersion(systemDirectoryDocumentVO.getChaptersVersion());
        		  record.setRequirementCode(systemDirectoryDocumentVO.getRequirementCode());
        		  record.setSystemDirectoryDocumentChaptersId(systemDirectoryDocumentVO.getId());
        		  reTblSystemDirectoryDocumentChaptersRequirementMapper.insertChaptersRequirement(record);//章节需求表
        		  mapBody.put("reqCode", systemDirectoryDocumentVO.getRequirementCode());
        		  //mapBody.put("reqCode", reTblSystemDirectoryDocumentRequirementFeaturMapper.getRequirementByFeatureCode(systemDirectoryDocumentVO.getFeatureCode()));
        		  Map<String, Object> mapReqDoc = new HashMap<>();//ReqDoc
        		  mapReqDoc.put("docKey", id);
        		  mapReqDoc.put("docName", tblSystemDirectoryDocument.getDocumentName());
        		  mapReqDoc.put("docType", typeCode);
        		  mapReqDoc.put("docStatus", 1);
        		  mapReqDoc.put("userAccount", userAccount);
        		  mapBody.put("reqDoc", mapReqDoc);
        		  mapBody.put("taskDoc", "");
        	  }
      		  HttpHeaders headers = new HttpHeaders();
      		  headers.setContentType(MediaType.APPLICATION_JSON);
      		  headers.set("Content-Type", "application/json;charset=UTF-8");
      		  headers.set("Accept", "application/json");
      		  //查看请求报文
	     	  mapAll.put("requestBody",mapBody);
	     	  logger.info("请求报文--------->>>>>>"+ JSON.toJSONString(mapAll,true));
	     	  //发送请求
	     	  HttpEntity<Map<String, Object>> sendParam = new HttpEntity<>(mapAll, headers);
	     	  Map [] mapResult = itReAssetsLibraryRqServiceImpl.send(sendParam);
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
     *  取消
     * @param id
     * @return
     */
    @Override
    public ResponseMessageModel cancel(Long id) {
        ResponseMessageModel messageModel = new ResponseMessageModel();
        Integer code = 0;
        try{
            code = reASystemDirectoryDocumentYiRanDao.cancelById(id);
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
     * @return
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
            logger.info("上传后的mongoKey:"+mongoKey);
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
     * html内容上传
     * @param systemDirectoryDocument
     * @return
     * @throws IOException
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
            logger.info("上传后的htmlMongoKey:"+htmlMongoKey);
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
