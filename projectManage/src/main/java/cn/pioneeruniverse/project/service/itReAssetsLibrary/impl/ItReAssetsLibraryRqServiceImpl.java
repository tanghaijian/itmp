package cn.pioneeruniverse.project.service.itReAssetsLibrary.impl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import cn.pioneeruniverse.common.databus.DataBusRequestHead;
import cn.pioneeruniverse.common.utils.BrowserUtil;
import cn.pioneeruniverse.common.utils.S3Util;
import cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary.ReASystemDirectoryDocumentYiRanDao;
import cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary.ReSystemDirectoryDocumentDao;
import cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary.ReSystemDirectoryDocumentHistoryDao;
import cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary.ReTblSystemDirectoryDocumentAttachmentMapper;
import cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary.ReTblSystemDirectoryDocumentChaptersMapper;
import cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary.ReTblSystemDirectoryDocumentChaptersRequirementFeatureMapper;
import cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary.ReTblSystemDirectoryDocumentChaptersRequirementMapper;
import cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary.ReTblSystemDirectoryDocumentRequirementFeaturMapper;
import cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary.ReTblSystemDirectoryDocumentRequirementMapper;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocument;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentAttachment;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChapters;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentRequirement;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentRequirementFeature;
import cn.pioneeruniverse.project.service.itReAssetsLibrary.ItReAssetsLibraryRqService;

@Transactional(readOnly = true)
@Service("itReAssetsLibraryRqService")
public class ItReAssetsLibraryRqServiceImpl implements ItReAssetsLibraryRqService {
	private static Logger log = LoggerFactory.getLogger(ItReAssetsLibraryRqServiceImpl.class);
	@Autowired
	private ReSystemDirectoryDocumentDao reSystemDirectoryDocumentDao;
	@Autowired
	private ReTblSystemDirectoryDocumentRequirementMapper reTblSystemDirectoryDocumentRequirementMapper;
	@Autowired
	private ReTblSystemDirectoryDocumentChaptersMapper systemDirectoryDocumentChaptersMapper;
	@Autowired
    private ReSystemDirectoryDocumentHistoryDao systemDirectoryDocumentHistoryDao;
	@Autowired
	private ReTblSystemDirectoryDocumentAttachmentMapper reDirectoryDocumentAttachmentMapper;
	
	@Autowired
	private ReTblSystemDirectoryDocumentChaptersRequirementMapper reChaptersRequirementMapper;
	@Autowired
	private ReTblSystemDirectoryDocumentChaptersRequirementFeatureMapper reChaptersRequirementFeatureMapper;
	@Autowired
    private ReASystemDirectoryDocumentYiRanDao reASystemDirectoryDocumentYiRanDao;
	@Autowired
    private ReTblSystemDirectoryDocumentRequirementFeaturMapper tblSystemDirectoryDocumentRequirementFeaturMapper;
	@Autowired
    private S3Util s3Util;
	@Value("${s3.documentBucket}")
    private String documentBucket;
	@Autowired
	private RestTemplate restTemplate;
	@Value("${esb.itcd.callback.demandfilesaveservicerest}")
    private String syncallback;
	
	/**
	 * 
	* @Title: getDocumentByRequirement
	* @Description: ??????????????????????????????????????????????????????
	* @author author
	* @param requirementCode ????????????
	* @param featureCode ??????????????????
	* @return List<TblSystemDirectoryDocument> ??????????????????
	 */
	@Override
	public List<TblSystemDirectoryDocument> getDocumentByRequirement(String requirementCode,String featureCode) {
		if(featureCode!=null&&!featureCode.equals("")) {
			Long featureId= reSystemDirectoryDocumentDao.selectReFId(featureCode);
			return reSystemDirectoryDocumentDao.selectDocumentByFeature(featureId);
		}else {
			return reSystemDirectoryDocumentDao.selectDocumentByRe(requirementCode);
		}
	}


	/**
	 * 
	* @Title: getRequirementId
	* @Description: ??????????????????????????????id
	* @author author
	* @param requirementCode ????????????
	* @return Long ??????id
	 */
	@Override
	public Long getRequirementId(String requirementCode) {
		return reSystemDirectoryDocumentDao.getRequirementId(requirementCode);
	}


	/**
	 * 
	* @Title: getTypeAndSystem
	* @Description: ??????????????????
	* @author author
	* @param featureCode ??????????????????
	* @return Map<String, Object>
	 */
	@Override
	public Map<String, Object> getTypeAndSystem(String featureCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer classIfy=2;//?????????
		if(featureCode.equals(null)||featureCode.equals("")) {
			classIfy=1;//?????????
		}
		/*map.put("system", reSystemDirectoryDocumentDao.getSystemByRequirement(requirementCode));*/
		//????????????
		map.put("documentType", reSystemDirectoryDocumentDao.getDocumentType(classIfy));
		return map;
	}


	/**
	 * 
	* @Title: upItLoadNewDocument
	* @Description: IT???????????????????????????
	* @author author
	* @param files ??????
	* @param tblSystemDirectoryDocument ????????????
	* @param currentUserAccount ?????????
	* @param fileName ?????????
	* @param request
	 */
	@Override
	@Transactional(readOnly = false)
	public void upItLoadNewDocument(MultipartFile[] files, TblSystemDirectoryDocument tblSystemDirectoryDocument,
			String currentUserAccount ,String[] fileName,HttpServletRequest request) {
		if (files != null && files.length > 0) {
			List<String> fileNameList= Arrays.asList(fileName);
			String typeCode=reSystemDirectoryDocumentDao.selectTypeCode(tblSystemDirectoryDocument.getDocumentType());
			Map<String, Object> map= new LinkedHashMap<>();
			Map<String, Object> mapAll = new LinkedHashMap<>();
			mapAll.put("requestHead",DataBusRequestHead.getRequestHead());
			Map<String, Object> mapBody = new HashMap<>();//requestBody
			List<Map<String, Object>> listMapReqDoc = new ArrayList<>();//ReqDoc
			List<Map<String, Object>> listMapTaskDoc = new ArrayList<>();//taskqDoc
			Long featureId=0L;
			if(tblSystemDirectoryDocument.getFeatureCode()!=null&&!tblSystemDirectoryDocument.getFeatureCode().equals("")) {
				featureId= reSystemDirectoryDocumentDao.selectReFId(tblSystemDirectoryDocument.getFeatureCode());
				mapBody.put("reqCode", tblSystemDirectoryDocument.getRequirementCode());
			}else {
				mapBody.put("reqCode", tblSystemDirectoryDocument.getRequirementCode());
			}
			int num=0; 
			for (MultipartFile file : files) {
				
				try {
                    //?????????S3
					this.upLoadDocument(file, tblSystemDirectoryDocument,fileNameList.get(num), request);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//?????????S3????????????????????????
				if(!tblSystemDirectoryDocument.getDocumentS3Key().equals("error")) {
					StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();  
					encryptor.setPassword("ccic");  
					currentUserAccount = currentUserAccount.replaceAll(" ","+");
					String userAccount=encryptor.decrypt(currentUserAccount);
					Long userId  = reASystemDirectoryDocumentYiRanDao.selectUserId(userAccount);
					tblSystemDirectoryDocument.setCreateBy(userId);
					tblSystemDirectoryDocument.setCreateDate(new Timestamp(new Date().getTime()));
					tblSystemDirectoryDocument.setLastUpdateBy(userId);
					tblSystemDirectoryDocument.setLastUpdateDate(new Timestamp(new Date().getTime()));
					tblSystemDirectoryDocument.setStatus(1);
					/*tblSystemDirectoryDocument.preInsertOrUpdate(request);*/
					/*tblSystemDirectoryDocument.setDocumentVersion(1);*/
					//????????????
					reSystemDirectoryDocumentDao.insertDirectoryDocument(tblSystemDirectoryDocument);
					Long id=tblSystemDirectoryDocument.getId();
					// systemDirectoryDocumentHistoryDao.insertDirectoryDocumentHistory(tblSystemDirectoryDocument);
					Date date = new Date();  
					Timestamp nousedate = new Timestamp(date.getTime());
					String fileNameOld =fileNameList.get(num);
					if (BrowserUtil.isMSBrowser(request)) {
						fileNameOld = fileNameOld.substring(fileNameOld.lastIndexOf("\\")+1);
					}
					//??????????????????
					if(tblSystemDirectoryDocument.getFeatureCode()!=null&&!tblSystemDirectoryDocument.getFeatureCode().equals("")) {
						TblSystemDirectoryDocumentRequirementFeature doReFeature=new TblSystemDirectoryDocumentRequirementFeature();
						doReFeature.setSystemDirectoryDocumentId(id);
						
						doReFeature.setRequiremenFeaturetId(featureId);
						doReFeature.setSystemDirectoryDocumentId(id);
						doReFeature.setDocumentVersion(1);
						doReFeature.setUpdateUserId(userId);
						doReFeature.setUpdateTime(nousedate);
						tblSystemDirectoryDocumentRequirementFeaturMapper.insert(doReFeature);
                        	
						Map<String, Object> mapReqDoc = new HashMap<>();//ReqDoc
						mapReqDoc.put("docKey", id);
						mapReqDoc.put("docType", typeCode);
						mapReqDoc.put("docName", fileNameOld);
						mapReqDoc.put("docStatus", 1);
						mapReqDoc.put("userAccount", currentUserAccount);
						listMapTaskDoc.add(mapReqDoc);
						
					}else {
						//????????????
						TblSystemDirectoryDocumentRequirement documentRequirement=new TblSystemDirectoryDocumentRequirement();
						documentRequirement.setSystemDirectoryDocumentId(id);
						documentRequirement.setRequirementCode(tblSystemDirectoryDocument.getRequirementCode());
						documentRequirement.setDocumentVersion(1);
						documentRequirement.setUpdateUserId(userId);
						documentRequirement.setUpdateTime(nousedate);
						reTblSystemDirectoryDocumentRequirementMapper.insert(documentRequirement);
                        	
						Map<String, Object> mapReqDoc = new HashMap<>();//ReqDoc
						mapReqDoc.put("docKey", id);
						mapReqDoc.put("docType", typeCode);
						mapReqDoc.put("docName", fileNameOld);
						mapReqDoc.put("docStatus", 1);
						mapReqDoc.put("userAccount", currentUserAccount);
						listMapReqDoc.add(mapReqDoc);
    	                    
    	                   
					}
				}
				num=num+1;  
                    
			}
			//???????????????
			mapBody.put("reqDoc", listMapReqDoc);
            mapBody.put("taskDoc", listMapTaskDoc);
            HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.set("Content-Type", "application/json;charset=UTF-8");
	        headers.set("Accept", "application/json");
	        //??????????????????
	        mapAll.put("requestBody",mapBody);
	        log.info("????????????--------->>>>>>"+ JSON.toJSONString(mapAll,true));
	        //?????????IT????????????demandfilesaveservicerest/Restful
	        HttpEntity<Map<String, Object>> sendParam = new HttpEntity<>(mapAll, headers);
	        Map [] mapResult = send(sendParam);
        }
	}

	/**
	 * 
	* @Title: upLoadDocument
	* @Description: ?????????????????????s3
	* @author author
	* @param file ??????
	* @param tblSystemDirectoryDocument ????????????
	* @param fileName ?????????
	* @param request
	* @return
	* @throws IOException
	 */
	 @Override
	 @Transactional(readOnly = false)
	 public String upLoadDocument(MultipartFile file, TblSystemDirectoryDocument tblSystemDirectoryDocument,String fileName,HttpServletRequest request) throws IOException {
		 InputStream inputStream = file.getInputStream();
		 //String fileNameOld = file.getOriginalFilename();
		 /*if (BrowserUtil.isMSBrowser(request)) {*/
		 if (BrowserUtil.isMSBrowser(request)) {
			 fileName = fileName.substring(fileName.lastIndexOf("\\")+1);
		 }
		 tblSystemDirectoryDocument.setDocumentName(fileName);
		 Random random = new Random();
		 String i = String.valueOf(random.nextInt());
		 /* }*/
		 tblSystemDirectoryDocument.setDocumentS3Bucket(documentBucket);
		 String S3Key = s3Util.uploadFile(documentBucket, fileName, inputStream);
		 tblSystemDirectoryDocument.setDocumentS3Key(S3Key.equals("error") ? null : S3Key);
		 return S3Key;
	 }


	 /**
	  * 
	 * @Title: upItNewMarkDocument
	 * @Description: IT???????????????markdown
	 * @author author
	 * @param tblSystemDirectoryDocument ????????????
	 * @param currentUserAccount ?????????
	 * @param request
	 * @return
	  */
	@Override
	@Transactional(readOnly = false)
	public Long upItNewMarkDocument(TblSystemDirectoryDocument tblSystemDirectoryDocument,String currentUserAccount, HttpServletRequest request) {
		
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();  
		encryptor.setPassword("ccic");  
		currentUserAccount = currentUserAccount.replaceAll(" ","+");
		String userAccount=encryptor.decrypt(currentUserAccount);
		//????????????ID
		Long userId  = reASystemDirectoryDocumentYiRanDao.selectUserId(userAccount);
		tblSystemDirectoryDocument.setCreateBy(userId);
		tblSystemDirectoryDocument.setCreateDate(new Timestamp(new Date().getTime()));
		tblSystemDirectoryDocument.setStatus(1);
		tblSystemDirectoryDocument.setLastUpdateBy(userId);
		tblSystemDirectoryDocument.setLastUpdateDate(new Timestamp(new Date().getTime()));
		//tblSystemDirectoryDocument.preInsertOrUpdate(request);
        //????????????
		
        reSystemDirectoryDocumentDao.insertDirectoryDocument(tblSystemDirectoryDocument);
        Long id=tblSystemDirectoryDocument.getId();
        //systemDirectoryDocumentHistoryDao.insertDirectoryDocumentHistory(tblSystemDirectoryDocument);
        
        //?????????????????????????????????????????????????????????
        TblSystemDirectoryDocumentChapters record=new TblSystemDirectoryDocumentChapters();
        record.setSystemDirectoryDocumentId(id);
        record.setCheckoutUserId(userId);
        record.setDocumentVersion(1);
        record.setChaptersName("????????????");
        record.setChaptersLevel(1);
        record.setChaptersOrder(1);
        record.setChaptersVersion(1);
        record.setCheckoutStatus(1);
        record.setStatus(1);
        Date date = new Date();       
        Timestamp nousedate = new Timestamp(date.getTime());
        record.setCreateDate(nousedate);
        record.setCreateBy(userId);
        //?????????????????????????????????
        if(tblSystemDirectoryDocument.getFeatureCode().equals("")) {
        	TblSystemDirectoryDocumentRequirement documentRequirement=new TblSystemDirectoryDocumentRequirement();
        	documentRequirement.setSystemDirectoryDocumentId(id);
            documentRequirement.setRequirementCode(tblSystemDirectoryDocument.getRequirementCode());
            documentRequirement.setDocumentVersion(1);
            documentRequirement.setUpdateUserId(userId);
            documentRequirement.setUpdateTime(nousedate);
            reTblSystemDirectoryDocumentRequirementMapper.insert(documentRequirement);
        }else {
        	//???????????????????????????
        	TblSystemDirectoryDocumentRequirementFeature doReFeature=new TblSystemDirectoryDocumentRequirementFeature();
        	doReFeature.setSystemDirectoryDocumentId(id);
        	Long featureId= reSystemDirectoryDocumentDao.selectReFId(tblSystemDirectoryDocument.getFeatureCode());
        	doReFeature.setRequiremenFeaturetId(featureId);
        	doReFeature.setSystemDirectoryDocumentId(id);
        	doReFeature.setDocumentVersion(1);
        	doReFeature.setUpdateUserId(userId);
        	doReFeature.setUpdateTime(nousedate);
        	tblSystemDirectoryDocumentRequirementFeaturMapper.insert(doReFeature);
        }
        
        systemDirectoryDocumentChaptersMapper.insert(record);
	
   
	return id;
	}


	/**
	 * 
	* @Title: coverUploadOldDocument
	* @Description: ????????????
	* @author author
	* @param files ??????
	* @param tblSystemDirectoryDocument ????????????
	* @param currentUserAccount ?????????
	* @param fileName ?????????
	* @param request
	* @throws IOException
	 */
	@Override
	@Transactional(readOnly = false)
	public void coverUploadOldDocument(MultipartFile[] files, TblSystemDirectoryDocument tblSystemDirectoryDocument,String currentUserAccount,String[] fileName,
			HttpServletRequest request) throws IOException {
		 if (files != null && files.length > 0) {
			 List<String> fileNameList= Arrays.asList(fileName);
			 int number=0;
	            for (MultipartFile file : files) {
	                if (!file.isEmpty()) {
	                	Long id=tblSystemDirectoryDocument.getId();
	                	StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();  
	                    encryptor.setPassword("ccic");
	                    currentUserAccount = currentUserAccount.replaceAll(" ","+");
	                    String userAccount=encryptor.decrypt(currentUserAccount);
	                    Long userId  = reASystemDirectoryDocumentYiRanDao.selectUserId(userAccount);
	                    //??????S3??????
	                	if(!this.upLoadDocument(file, tblSystemDirectoryDocument,fileNameList.get(number), request).equals("error")){
	                		tblSystemDirectoryDocument.setLastUpdateBy(userId);
		                	tblSystemDirectoryDocument.setLastUpdateDate(new Timestamp(new Date().getTime()));
		                    //tblSystemDirectoryDocument.preInsertOrUpdate(request);
		                    TblSystemDirectoryDocument tblSystemDirectoryDocumentOld=reSystemDirectoryDocumentDao.getDirectoryDocumentsById(tblSystemDirectoryDocument.getId());
		                    //tblSystemDirectoryDocumentOld.setDocumentS3Key(tblSystemDirectoryDocument.getDocumentS3Key());
		                    //?????????????????????
		                    systemDirectoryDocumentHistoryDao.insertDirectoryDocumentHistory(tblSystemDirectoryDocumentOld);
		                    tblSystemDirectoryDocument.setLastUpdateBy(userId);
		                    tblSystemDirectoryDocument.setDocumentVersion(tblSystemDirectoryDocumentOld.getDocumentVersion()+1);
		                    //??????????????????
		                    reSystemDirectoryDocumentDao.updateVersionForCoverUploadDocument(tblSystemDirectoryDocument);
		                    //??????????????????
		                    if(tblSystemDirectoryDocument.getFeatureCode().equals("")) {
		                    	TblSystemDirectoryDocumentRequirement systemDirectoryDocumentRequirement=new TblSystemDirectoryDocumentRequirement();
		   	                    systemDirectoryDocumentRequirement.setSystemDirectoryDocumentId(id);
		   	                    systemDirectoryDocumentRequirement.setRequirementCode(tblSystemDirectoryDocument.getRequirementCode());
		   	                    systemDirectoryDocumentRequirement.setDocumentVersion(tblSystemDirectoryDocumentOld.getDocumentVersion()+1);
		   	                    systemDirectoryDocumentRequirement.setUpdateUserId(userId);
		   	                    reTblSystemDirectoryDocumentRequirementMapper.insertDocumentRe(systemDirectoryDocumentRequirement);//??????????????????????????????????????????
		                    }else {
		                    	//????????????????????????
		                    	TblSystemDirectoryDocumentRequirementFeature documentRequirementFeature=new TblSystemDirectoryDocumentRequirementFeature();
		                    	documentRequirementFeature.setSystemDirectoryDocumentId(id);
		                    	Long featureId= reSystemDirectoryDocumentDao.selectReFId(tblSystemDirectoryDocument.getFeatureCode());
		                    	documentRequirementFeature.setRequiremenFeaturetId(featureId);
		                    	documentRequirementFeature.setDocumentVersion(tblSystemDirectoryDocumentOld.getDocumentVersion()+1);
		                    	documentRequirementFeature.setUpdateUserId(userId);
		   	                    tblSystemDirectoryDocumentRequirementFeaturMapper.insertDocumentReFeature(documentRequirementFeature);
		                    }
	                	}
	                	
	                  }
	                number=number+1;
	            }
	        }
		
	}


	/**
	 * 
	* @Title: selectChaptersMarkDown
	* @Description: ???????????????markdown??????
	* @author author
	* @param systemDirectoryDocumentChapters ????????????
	* @param currentUserAccount ????????????
	* @param request
	* @return Map<String, Object>
	* @throws Exception
	 */
	@Override
	public Map<String, Object> selectChaptersMarkDown(
			TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters,String currentUserAccount, HttpServletRequest request)
			throws Exception {
		Map<String, Object> result = new HashMap<>();
        TblSystemDirectoryDocumentChapters chapters = systemDirectoryDocumentChaptersMapper.selectChaptersWithRelatedData(systemDirectoryDocumentChapters.getId());
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();  
        encryptor.setPassword("ccic");
       
        currentUserAccount = currentUserAccount.replaceAll(" ","+");
        String userAccount=encryptor.decrypt(currentUserAccount);
        Long userId  = reASystemDirectoryDocumentYiRanDao.selectUserId(userAccount);
        //Long userId = CommonUtil.getCurrentUserId(request);
        String markdown = null;
        Integer buttonState = 2;
        if(userId.equals(chapters.getCheckoutUserId()) && chapters.getCheckoutStatus() == 1) {
            //???MONGO??????KEY(??????????????????)
            markdown = s3Util.getStringByS3(chapters.getChaptersS3Bucket(),chapters.getChaptersTempS3Key());
           //markdown??????
            result.put("markdown",markdown);
            //?????????????????????
            result.put("buttonState",1);
            //????????????
            result.put("chapters",chapters);
            return  result;
        }else if (!userId.equals(chapters.getCheckoutUserId()) && StringUtils.isNotBlank(chapters.getChaptersS3Bucket()) && StringUtils.isNotBlank(chapters.getChaptersS3Key())) {
            markdown = s3Util.getStringByS3(chapters.getChaptersS3Bucket(), chapters.getChaptersS3Key());
            result.put("markdown",markdown);
            result.put("buttonState",2);
            result.put("chapters",chapters);
            return  result;
        }else if (userId.equals(chapters.getCheckoutUserId()) && chapters.getCheckoutStatus() == 2 && StringUtils.isNotBlank(chapters.getChaptersS3Bucket()) && StringUtils.isNotBlank(chapters.getChaptersTempS3Key())){
            markdown = s3Util.getStringByS3(chapters.getChaptersS3Bucket(), chapters.getChaptersS3Key());
            result.put("markdown",markdown);
            result.put("buttonState",2);
            result.put("chapters",chapters);
            return  result;
        }
// else if (chapters.getCheckoutUserId() ==  null && StringUtils.isBlank(chapters.getChaptersS3Bucket())){
////            markdown = s3Util.getStringByS3(chapters.getChaptersS3Bucket(), chapters.getChaptersS3Key());
//            result.put("markdown",markdown);
//            result.put("buttonState",3);
//            result.put("chapters",chapters);
//            return  result;
//        }
        result.put("markdown",markdown);
        result.put("chapters",chapters);
        result.put("buttonState",buttonState);
        return  result;
	}


	/**
	 * 
	* @Title: selectChaptersFiles
	* @Description: ??????????????????
	* @author author
	* @param systemDirectoryDocumentChapters ????????????
	* @param request
	* @return List<TblSystemDirectoryDocumentAttachment> ????????????
	 */
	@Override
	public List<TblSystemDirectoryDocumentAttachment> selectChaptersFiles(
			TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters, HttpServletRequest request) {
		  return reDirectoryDocumentAttachmentMapper.selectAttachmentByChaptersId(systemDirectoryDocumentChapters.getId(),null);
		   
	}


	/**
	 * 
	* @Title: selectChaptersRelatedData
	* @Description: ??????????????????
	* @author author
	* @param systemDirectoryDocumentChapters ????????????
	* @param request
	* @return Map<String, Object>
	 */
	@Override
	public Map<String, Object> selectChaptersRelatedData(
			TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		//????????????
        result.put("requirement",reChaptersRequirementMapper.selectRequirementByChaptersId(systemDirectoryDocumentChapters.getId(),null));
        //??????????????????
        result.put("Feature",reChaptersRequirementFeatureMapper.selectRequirementFeatureByChaptersId(systemDirectoryDocumentChapters.getId(),null));
        return result;
	}


	/**
	 * 
	* @Title: addOrUpdateDocChapters
	* @Description: ??????????????????
	* @author author
	* @param systemDirectoryDocumentChapters ??????????????????
	* @param currentUserAccount ??????
	* @param request
	* @return TblSystemDirectoryDocumentChapters ????????????????????????
	 */
	@Override
	@Transactional(readOnly = false)
	public TblSystemDirectoryDocumentChapters addOrUpdateDocChapters(
			TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters,String currentUserAccount, HttpServletRequest request) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();  
		encryptor.setPassword("ccic");  
		currentUserAccount = currentUserAccount.replaceAll(" ","+");
		String userAccount=encryptor.decrypt(currentUserAccount);
		Long userId  = reASystemDirectoryDocumentYiRanDao.selectUserId(userAccount);
		if(systemDirectoryDocumentChapters.getId()!=null) {//??????
			systemDirectoryDocumentChapters.setLastUpdateBy(userId);
			systemDirectoryDocumentChapters.setLastUpdateDate(new Timestamp(new Date().getTime()));
			systemDirectoryDocumentChaptersMapper.updateByPrimaryKeySelective(systemDirectoryDocumentChapters);
			TblSystemDirectoryDocumentChapters sysDocChapters = systemDirectoryDocumentChaptersMapper.selectByPrimaryKey(systemDirectoryDocumentChapters.getId());
			return sysDocChapters;
		}else {
			//??????chaptersOrder ????????????????????????????????????????????????
			//???????????????????????????order?????????1
			if(systemDirectoryDocumentChapters.getParentId()!=null) {
				Integer maxOrder = systemDirectoryDocumentChaptersMapper.getChildrenMaxOrder(systemDirectoryDocumentChapters.getParentId());//
				systemDirectoryDocumentChapters.setChaptersOrder(maxOrder==null? 1 : maxOrder+1);
			}else {//???????????? chaptersOrder?????????
				
			}
			systemDirectoryDocumentChapters.setCheckoutStatus(2);
			systemDirectoryDocumentChapters.setCreateBy(userId);
			systemDirectoryDocumentChapters.setCreateDate(new Timestamp(new Date().getTime()));
			systemDirectoryDocumentChapters.setLastUpdateBy(userId);
			systemDirectoryDocumentChapters.setLastUpdateDate(new Timestamp(new Date().getTime()));
			systemDirectoryDocumentChapters.setStatus(1);
			//CommonUtil.setBaseValue(systemDirectoryDocumentChapters, request);
			systemDirectoryDocumentChaptersMapper.insert(systemDirectoryDocumentChapters);
			TblSystemDirectoryDocumentChapters sysDocChapters =  systemDirectoryDocumentChaptersMapper.selectByPrimaryKey(systemDirectoryDocumentChapters.getId());
			return sysDocChapters;
		}
	}


	/**
	 * 
	* @Title: delectDocumentFile
	* @Description: ??????????????????
	* @author author
	* @param directoryDocumentId ??????ID
	* @param documentType ????????????
	* @param currentUserAccount ?????????
	* @param requirementCode ????????????
	* @param featureCode ??????????????????
	 */
	@Override
	@Transactional(readOnly = false)
	public void delectDocumentFile(Long directoryDocumentId,Long documentType,String currentUserAccount,String requirementCode,String featureCode) {
		Map<String, Object> map= new LinkedHashMap<>();
		Map<String, Object> mapAll = new LinkedHashMap<>();
		String typeCode=reSystemDirectoryDocumentDao.selectTypeCode(documentType);
		mapAll.put("requestHead",DataBusRequestHead.getRequestHead());
		Map<String, Object> mapBody = new HashMap<>();//requestBody
		List<Map<String, Object>> listMapReqDoc = new ArrayList<>();//ReqDoc
		List<Map<String, Object>> listMapTaskDoc = new ArrayList<>();//taskqDoc
		mapBody.put("reqCode", requirementCode);
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();  
		encryptor.setPassword("ccic");  
		currentUserAccount = currentUserAccount.replaceAll(" ","+");
		String userAccount=encryptor.decrypt(currentUserAccount);
		Long userId  = reASystemDirectoryDocumentYiRanDao.selectUserId(userAccount);
		TblSystemDirectoryDocument systemDirectoryDocument=reSystemDirectoryDocumentDao.getDocumentAndTypeCoodeById(directoryDocumentId);
		reSystemDirectoryDocumentDao.removeDocumentFile(directoryDocumentId,userId);
		//????????????
		if(!featureCode.equals("")) {
			Map<String, Object> mapReqDoc = new HashMap<>();//ReqDoc
			mapReqDoc.put("docKey", directoryDocumentId);
			mapReqDoc.put("docType", systemDirectoryDocument.getValueCode());
			mapReqDoc.put("docName", systemDirectoryDocument.getDocumentName());
			mapReqDoc.put("docStatus", 2);
			mapReqDoc.put("userAccount", currentUserAccount);
			listMapTaskDoc.add(mapReqDoc);
		}else {
			Map<String, Object> mapReqDoc = new HashMap<>();//ReqDoc
			mapReqDoc.put("docKey", directoryDocumentId);
			mapReqDoc.put("docType", systemDirectoryDocument.getValueCode());
			mapReqDoc.put("docName", systemDirectoryDocument.getDocumentName());
			mapReqDoc.put("docStatus", 2);
			mapReqDoc.put("userAccount", currentUserAccount);
			listMapReqDoc.add(mapReqDoc);
		}
		
		
		mapBody.put("reqDoc", listMapReqDoc);
        mapBody.put("taskDoc", listMapTaskDoc);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Content-Type", "application/json;charset=UTF-8");
        headers.set("Accept", "application/json");
        //??????????????????
        mapAll.put("requestBody",mapBody);
        log.info("????????????--------->>>>>>"+ JSON.toJSONString(mapAll,true));
        //????????????demandfilesaveservicerest/Restful
        HttpEntity<Map<String, Object>> sendParam = new HttpEntity<>(mapAll, headers);
        Map [] mapResult = send(sendParam);
		
	}
   
	//??????esb??????
	public Map [] send(HttpEntity<Map<String, Object>> he) {
        Map [] maps = null;
        try{
            //????????????
            ResponseEntity<String> result = restTemplate.exchange
                    (syncallback, HttpMethod.POST, he,String.class);
            if(result.getStatusCode().is2xxSuccessful()){
                log.info("????????????--------->>>>>>"+result.getBody());
                String str = JSON.parseObject(result.getBody()).get("responseBody").toString();
                maps = JSONArray.parseObject(str,Map [].class);
            }else{
                log.info("???????????????????????????"+result.getStatusCodeValue()+"???");
            }
        }catch (Exception ex){
            ex.getMessage();
        }
        return maps;
    }
	
	public String getCharset(InputStream is) throws IOException {
        BufferedInputStream bin = new BufferedInputStream(is);
        int p = (bin.read() << 8) + bin.read();//??????????????????16???  
        String code = null;
        switch (p) {
            case 0xefbb:
                code = "UTF-8";
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16";
                break;
            default:
                code = "GB2312";
        }
        return code;
    }


}
