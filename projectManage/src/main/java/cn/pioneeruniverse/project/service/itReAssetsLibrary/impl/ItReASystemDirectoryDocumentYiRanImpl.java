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
     * ????????????????????????
     * @param id  ????????????id
     * @return SystemDirectoryDocumentVO
     */
    @Override
    public SystemDirectoryDocumentVO selectSystemDirectoryDocumentById(Long id, HttpServletRequest request) {
        SystemDirectoryDocumentVO systemDirectoryDocumentVO = reASystemDirectoryDocumentYiRanDao.selectSystemDirectoryDocumentById(Long.valueOf(12345));
        try {
            if (systemDirectoryDocumentVO != null){
                //??????????????????????????????
                systemDirectoryDocumentVO.setCreateByName(reASystemDirectoryDocumentYiRanDao.getUserNameById(systemDirectoryDocumentVO.getCreateBy()));
                systemDirectoryDocumentVO.setLastUpdateByName(reASystemDirectoryDocumentYiRanDao.getUserNameById(systemDirectoryDocumentVO.getLastUpdateBy()));
                //??????S3????????????
                if (StringUtils.isNotBlank(systemDirectoryDocumentVO.getDocumentS3Bucket()) && StringUtils.isNotBlank(systemDirectoryDocumentVO.getDocumentS3Key())){
                    systemDirectoryDocumentVO.setDirectoryDocumentContent(s3Util.getStringByS3(systemDirectoryDocumentVO.getDocumentS3Bucket(), systemDirectoryDocumentVO.getDocumentS3Key()));
                }
                //??????????????????

            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }

        return systemDirectoryDocumentVO;
    }

    /**
     * ??????????????????????????????
     * @param id  ??????id
     * @param request
     * @return Map<String,Object>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map systemDirectoryDocumentSignOffById(Long id,String userAccount, HttpServletRequest request) {
        Map<String,Object> mapValues = new HashMap<>();
        //????????????????????????
        SystemDirectoryDocumentVO systemDirectory = reASystemDirectoryDocumentYiRanDao.systemDirectoryDocumentSignOffById(id);
        //????????????????????????id
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();  
        encryptor.setPassword("ccic"); 
      //?????????????????????????????????????????????url????????????????????????????????????+????????????????????????????????? ?????????????????????????????????
        userAccount = userAccount.replaceAll(" ","+");
        Long userId  = reASystemDirectoryDocumentYiRanDao.selectUserId(encryptor.decrypt(userAccount));
        String content = null;
        Integer code = 0;
        //?????????????????????S3_MONGO_KEY,???????????????S3????????????
        try{
           if (StringUtils.isNotBlank(systemDirectory.getChaptersS3Bucket()) && StringUtils.isNotBlank(systemDirectory.getChaptersS3Key())){
                content = s3Util.getStringByS3(systemDirectory.getChaptersS3Bucket(), systemDirectory.getChaptersS3Key());
                systemDirectory.setDirectoryDocumentContent(content);
            }
            //?????????????????????1:??????2:??????
            code = reASystemDirectoryDocumentYiRanDao.updateDirectoryDocumentSignOffStatusById(id, userId);
            //????????????
            mapValues.put("systemDirectory",systemDirectory);
            //0???????????????????????????????????????
            mapValues.put("code",code);
            //markdown??????
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
//     * ????????????????????????
//     * @param id
//     * @param request
//     * @return
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public Map systemDirectoryDocumentSignOffById(Long id, HttpServletRequest request) {
//        Map<String,Object> mapValues = new HashMap<>();
//        //????????????????????????
//        SystemDirectoryDocumentVO systemDirectory = aSystemDirectoryDocumentYiRanDao.systemDirectoryDocumentSignOffById(id);
//        //????????????????????????id
//        Long userId  = CommonUtil.getCurrentUserId(request);
//        String content = null;
//        //?????????????????????S3_MONGO_KEY,???????????????S3????????????
//           try{
//               if (systemDirectory != null && userId.equals(systemDirectory.getCheckoutUserId()) && StringUtils.isNotBlank(systemDirectory.getDocumentS3Bucket()) && StringUtils.isNotBlank(systemDirectory.getChaptersTempS3Key())){
//                   //???MONGO??????KEY(??????????????????)
//                   content = s3Util.getStringByS3(systemDirectory.getDocumentS3Bucket(), systemDirectory.getChaptersTempS3Key());
//                   systemDirectory.setDirectoryDocumentContent(content);
//                   //?????????????????????1:??????2:??????
//                   aSystemDirectoryDocumentYiRanDao.updateDirectoryDocumentSignOffStatusById(id, userId);
//                   mapValues.put("systemDirectory",systemDirectory);
//                   mapValues.put("code",1);
//                   mapValues.put("content",content);
//                   return mapValues;
//               }else if (systemDirectory != null && !userId.equals(systemDirectory.getCheckoutUserId()) && StringUtils.isNotBlank(systemDirectory.getDocumentS3Bucket()) && StringUtils.isNotBlank(systemDirectory.getChaptersS3Key())){
//                   content = s3Util.getStringByS3(systemDirectory.getDocumentS3Bucket(), systemDirectory.getChaptersS3Key());
//                   systemDirectory.setDirectoryDocumentContent(content);
//                   //?????????????????????1:??????2:??????
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
     * ??????
     * @param request
     * @param systemDirectoryDocumentVO ????????????
     * @return ResponseMessageModel
     * @throws IOException
     */
    @Override
   public ResponseMessageModel addTemporaryStorageById(HttpServletRequest request,SystemDirectoryDocumentVO systemDirectoryDocumentVO,String userAccount) throws IOException{
        ResponseMessageModel messageModel = new ResponseMessageModel();
        Integer code = 0;
        String msg = this.writeTxtFile(systemDirectoryDocumentVO);  //s3????????????
        String htmlMsg = this.writHtmlFile(systemDirectoryDocumentVO); //s3html????????????
        if(!msg.equals("error") && !htmlMsg.equals("error")){
//            String content = s3Util.getStringByS3(systemDirectoryDocumentVO.getDocumentS3Bucket(), systemDirectoryDocumentVO.getDocumentTempMongoKey());
            systemDirectoryDocumentVO.getLastUpdateDate();
            //????????????????????????id
            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();  
            encryptor.setPassword("ccic"); 
            //?????????????????????????????????????????????url????????????????????????????????????+????????????????????????????????? ?????????????????????????????????
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
     * ??????
     * @param request
     * @param systemDirectoryDocumentVO ????????????
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
      	  String msg = this.writeTxtFile(systemDirectoryDocumentVO);  //s3????????????
      	  String htmlMsg = this.writHtmlFile(systemDirectoryDocumentVO); //s3html????????????
      	  if(!msg.equals("error") && !htmlMsg.equals("error")){
      		  systemDirectoryDocumentVO.setLastUpdateDate(lastUpdateDate.toString());
      		  StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();  
      		  encryptor.setPassword("ccic"); 
      		  userAccount = userAccount.replaceAll(" ","+");
      		  Long userId=reASystemDirectoryDocumentYiRanDao.selectUserId(encryptor.decrypt(userAccount));
      		  systemDirectoryDocumentVO.setLastUpdateBy(userId);
      		  //?????????????????????????????????   ???????????? =  ????????????+1  ???????????? = ???????????? +1
      		  //??????????????????
      		  TblSystemDirectoryDocument tblSystemDirectoryDocument = reASystemDirectoryDocumentYiRanDao.selectDocumentVersionById(systemDirectoryDocumentVO.getSystemDirectoryDocumentId());
      		  //??????????????????code
      		  String typeCode=reSystemDirectoryDocumentDao.selectTypeCode(tblSystemDirectoryDocument.getDocumentType());
                //????????????+1
      		  reASystemDirectoryDocumentYiRanDao.updateDocumentVersionById(systemDirectoryDocumentVO.getSystemDirectoryDocumentId());
      		  systemDirectoryDocumentVO.setDocumentVersion(tblSystemDirectoryDocument.getDocumentVersion());

      		  //????????????????????????????????????????????????
      		  TblSystemDirectoryDocumentChapters directoryDocumentChaptersById = reASystemDirectoryDocumentYiRanDao.getDirectoryDocumentChaptersById(systemDirectoryDocumentVO.getId());
      		
      		  directoryDocumentChaptersById.setLastUpdateDate(lastUpdateDate);
      		  directoryDocumentChaptersById.setChaptersS3Key(systemDirectoryDocumentVO.getChaptersS3Key());
      		  directoryDocumentChaptersById.setChaptersS3Key2(systemDirectoryDocumentVO.getChaptersS3Key2());
      		  directoryDocumentChaptersById.setChaptersS3Bucket(systemDirectoryDocumentVO.getChaptersS3Bucket());
      		  reASystemDirectoryDocumentYiRanDao.insertDirectoryDocumentHistory(directoryDocumentChaptersById);
      		  //??????
      		  reASystemDirectoryDocumentYiRanDao.directoryDocumentSubmitById(systemDirectoryDocumentVO);
              //??????????????????????????????
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
      			  chaptersRequirementFeatureMapper.insertChaptersRequirementFeature(record);//?????????????????????
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
        		  reTblSystemDirectoryDocumentChaptersRequirementMapper.insertChaptersRequirement(record);//???????????????
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
      		  //??????????????????
	     	  mapAll.put("requestBody",mapBody);
	     	  logger.info("????????????--------->>>>>>"+ JSON.toJSONString(mapAll,true));
	     	  //????????????
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
     *  ??????
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
     * ???????????????????????????????????????DocumentS3Key
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
            String fileNameOld = systemDirectoryDocument.getId().toString(); //?????????
            fileNameOld = fileNameOld.substring(fileNameOld.lastIndexOf("\\") + 1);
            file = new File(file+"\\"+fileNameOld);// ???????????????????????????????????????????????????output???txt??????
            file.createNewFile(); // ???????????????

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
            logger.info("????????????mongoKey:"+mongoKey);
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
     * html????????????
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
            String fileNameOld = systemDirectoryDocument.getId()+""; //?????????
            fileNameOld = fileNameOld.substring(fileNameOld.lastIndexOf("\\") + 1);
            file = new File(file+"\\"+fileNameOld);// ???????????????????????????????????????????????????output???txt??????
            file.createNewFile(); // ???????????????

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
            logger.info("????????????htmlMongoKey:"+htmlMongoKey);
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
