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
	* @Description: 根据开发任务或者需求编码获取文档信息
	* @author author
	* @param requirementCode 需求编码
	* @param featureCode 开发任务编码
	* @return List<TblSystemDirectoryDocument> 文档目录列表
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
	* @Description: 根据需求编码获取需求id
	* @author author
	* @param requirementCode 需求编码
	* @return Long 需求id
	 */
	@Override
	public Long getRequirementId(String requirementCode) {
		return reSystemDirectoryDocumentDao.getRequirementId(requirementCode);
	}


	/**
	 * 
	* @Title: getTypeAndSystem
	* @Description: 获取文档类型
	* @author author
	* @param featureCode 开发任务编号
	* @return Map<String, Object>
	 */
	@Override
	public Map<String, Object> getTypeAndSystem(String featureCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer classIfy=2;//系统类
		if(featureCode.equals(null)||featureCode.equals("")) {
			classIfy=1;//需求类
		}
		/*map.put("system", reSystemDirectoryDocumentDao.getSystemByRequirement(requirementCode));*/
		//文档类型
		map.put("documentType", reSystemDirectoryDocumentDao.getDocumentType(classIfy));
		return map;
	}


	/**
	 * 
	* @Title: upItLoadNewDocument
	* @Description: IT全流程上传文档附件
	* @author author
	* @param files 附件
	* @param tblSystemDirectoryDocument 文档信息
	* @param currentUserAccount 操作人
	* @param fileName 文件名
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
                    //上传到S3
					this.upLoadDocument(file, tblSystemDirectoryDocument,fileNameList.get(num), request);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//上传到S3成功后更新数据库
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
					//新写添加
					reSystemDirectoryDocumentDao.insertDirectoryDocument(tblSystemDirectoryDocument);
					Long id=tblSystemDirectoryDocument.getId();
					// systemDirectoryDocumentHistoryDao.insertDirectoryDocumentHistory(tblSystemDirectoryDocument);
					Date date = new Date();  
					Timestamp nousedate = new Timestamp(date.getTime());
					String fileNameOld =fileNameList.get(num);
					if (BrowserUtil.isMSBrowser(request)) {
						fileNameOld = fileNameOld.substring(fileNameOld.lastIndexOf("\\")+1);
					}
					//关联开发任务
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
						//关联需求
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
			//组装报文体
			mapBody.put("reqDoc", listMapReqDoc);
            mapBody.put("taskDoc", listMapTaskDoc);
            HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.set("Content-Type", "application/json;charset=UTF-8");
	        headers.set("Accept", "application/json");
	        //查看请求报文
	        mapAll.put("requestBody",mapBody);
	        log.info("请求报文--------->>>>>>"+ JSON.toJSONString(mapAll,true));
	        //发送给IT全流程：demandfilesaveservicerest/Restful
	        HttpEntity<Map<String, Object>> sendParam = new HttpEntity<>(mapAll, headers);
	        Map [] mapResult = send(sendParam);
        }
	}

	/**
	 * 
	* @Title: upLoadDocument
	* @Description: 上传文档附件到s3
	* @author author
	* @param file 文件
	* @param tblSystemDirectoryDocument 文档信息
	* @param fileName 文件名
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
	 * @Description: IT全流程新建markdown
	 * @author author
	 * @param tblSystemDirectoryDocument 文档信息
	 * @param currentUserAccount 操作人
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
		//获取用户ID
		Long userId  = reASystemDirectoryDocumentYiRanDao.selectUserId(userAccount);
		tblSystemDirectoryDocument.setCreateBy(userId);
		tblSystemDirectoryDocument.setCreateDate(new Timestamp(new Date().getTime()));
		tblSystemDirectoryDocument.setStatus(1);
		tblSystemDirectoryDocument.setLastUpdateBy(userId);
		tblSystemDirectoryDocument.setLastUpdateDate(new Timestamp(new Date().getTime()));
		//tblSystemDirectoryDocument.preInsertOrUpdate(request);
        //新写添加
		
        reSystemDirectoryDocumentDao.insertDirectoryDocument(tblSystemDirectoryDocument);
        Long id=tblSystemDirectoryDocument.getId();
        //systemDirectoryDocumentHistoryDao.insertDirectoryDocumentHistory(tblSystemDirectoryDocument);
        
        //新建文档后，默认新建一个章节：新建章节
        TblSystemDirectoryDocumentChapters record=new TblSystemDirectoryDocumentChapters();
        record.setSystemDirectoryDocumentId(id);
        record.setCheckoutUserId(userId);
        record.setDocumentVersion(1);
        record.setChaptersName("新建章节");
        record.setChaptersLevel(1);
        record.setChaptersOrder(1);
        record.setChaptersVersion(1);
        record.setCheckoutStatus(1);
        record.setStatus(1);
        Date date = new Date();       
        Timestamp nousedate = new Timestamp(date.getTime());
        record.setCreateDate(nousedate);
        record.setCreateBy(userId);
        //文档和开发任务关联关系
        if(tblSystemDirectoryDocument.getFeatureCode().equals("")) {
        	TblSystemDirectoryDocumentRequirement documentRequirement=new TblSystemDirectoryDocumentRequirement();
        	documentRequirement.setSystemDirectoryDocumentId(id);
            documentRequirement.setRequirementCode(tblSystemDirectoryDocument.getRequirementCode());
            documentRequirement.setDocumentVersion(1);
            documentRequirement.setUpdateUserId(userId);
            documentRequirement.setUpdateTime(nousedate);
            reTblSystemDirectoryDocumentRequirementMapper.insert(documentRequirement);
        }else {
        	//文档和需求关联关系
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
	* @Description: 覆盖上传
	* @author author
	* @param files 文件
	* @param tblSystemDirectoryDocument 文档信息
	* @param currentUserAccount 操作人
	* @param fileName 文件名
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
	                    //上传S3成功
	                	if(!this.upLoadDocument(file, tblSystemDirectoryDocument,fileNameList.get(number), request).equals("error")){
	                		tblSystemDirectoryDocument.setLastUpdateBy(userId);
		                	tblSystemDirectoryDocument.setLastUpdateDate(new Timestamp(new Date().getTime()));
		                    //tblSystemDirectoryDocument.preInsertOrUpdate(request);
		                    TblSystemDirectoryDocument tblSystemDirectoryDocumentOld=reSystemDirectoryDocumentDao.getDirectoryDocumentsById(tblSystemDirectoryDocument.getId());
		                    //tblSystemDirectoryDocumentOld.setDocumentS3Key(tblSystemDirectoryDocument.getDocumentS3Key());
		                    //文档保存进历史
		                    systemDirectoryDocumentHistoryDao.insertDirectoryDocumentHistory(tblSystemDirectoryDocumentOld);
		                    tblSystemDirectoryDocument.setLastUpdateBy(userId);
		                    tblSystemDirectoryDocument.setDocumentVersion(tblSystemDirectoryDocumentOld.getDocumentVersion()+1);
		                    //更新版本信息
		                    reSystemDirectoryDocumentDao.updateVersionForCoverUploadDocument(tblSystemDirectoryDocument);
		                    //文档关联需求
		                    if(tblSystemDirectoryDocument.getFeatureCode().equals("")) {
		                    	TblSystemDirectoryDocumentRequirement systemDirectoryDocumentRequirement=new TblSystemDirectoryDocumentRequirement();
		   	                    systemDirectoryDocumentRequirement.setSystemDirectoryDocumentId(id);
		   	                    systemDirectoryDocumentRequirement.setRequirementCode(tblSystemDirectoryDocument.getRequirementCode());
		   	                    systemDirectoryDocumentRequirement.setDocumentVersion(tblSystemDirectoryDocumentOld.getDocumentVersion()+1);
		   	                    systemDirectoryDocumentRequirement.setUpdateUserId(userId);
		   	                    reTblSystemDirectoryDocumentRequirementMapper.insertDocumentRe(systemDirectoryDocumentRequirement);//添加至系统目录文档需求关系表
		                    }else {
		                    	//文档关联开发任务
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
	* @Description: 获取章节的markdown信息
	* @author author
	* @param systemDirectoryDocumentChapters 章节信息
	* @param currentUserAccount 当前用户
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
            //取MONGO存储KEY(存放临时数据)
            markdown = s3Util.getStringByS3(chapters.getChaptersS3Bucket(),chapters.getChaptersTempS3Key());
           //markdown信息
            result.put("markdown",markdown);
            //页面按钮控制用
            result.put("buttonState",1);
            //章节信息
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
	* @Description: 获取章节附件
	* @author author
	* @param systemDirectoryDocumentChapters 章节信息
	* @param request
	* @return List<TblSystemDirectoryDocumentAttachment> 附件列表
	 */
	@Override
	public List<TblSystemDirectoryDocumentAttachment> selectChaptersFiles(
			TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters, HttpServletRequest request) {
		  return reDirectoryDocumentAttachmentMapper.selectAttachmentByChaptersId(systemDirectoryDocumentChapters.getId(),null);
		   
	}


	/**
	 * 
	* @Title: selectChaptersRelatedData
	* @Description: 章节关联信息
	* @author author
	* @param systemDirectoryDocumentChapters 章节信息
	* @param request
	* @return Map<String, Object>
	 */
	@Override
	public Map<String, Object> selectChaptersRelatedData(
			TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		//关联需求
        result.put("requirement",reChaptersRequirementMapper.selectRequirementByChaptersId(systemDirectoryDocumentChapters.getId(),null));
        //关联开发任务
        result.put("Feature",reChaptersRequirementFeatureMapper.selectRequirementFeatureByChaptersId(systemDirectoryDocumentChapters.getId(),null));
        return result;
	}


	/**
	 * 
	* @Title: addOrUpdateDocChapters
	* @Description: 更新章节信息
	* @author author
	* @param systemDirectoryDocumentChapters 文档章节信息
	* @param currentUserAccount 账号
	* @param request
	* @return TblSystemDirectoryDocumentChapters 更新后的章节信息
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
		if(systemDirectoryDocumentChapters.getId()!=null) {//编辑
			systemDirectoryDocumentChapters.setLastUpdateBy(userId);
			systemDirectoryDocumentChapters.setLastUpdateDate(new Timestamp(new Date().getTime()));
			systemDirectoryDocumentChaptersMapper.updateByPrimaryKeySelective(systemDirectoryDocumentChapters);
			TblSystemDirectoryDocumentChapters sysDocChapters = systemDirectoryDocumentChaptersMapper.selectByPrimaryKey(systemDirectoryDocumentChapters.getId());
			return sysDocChapters;
		}else {
			//获取chaptersOrder 新增目前只会新增到同级的最后一个
			//获取该父级下最大的order然后加1
			if(systemDirectoryDocumentChapters.getParentId()!=null) {
				Integer maxOrder = systemDirectoryDocumentChaptersMapper.getChildrenMaxOrder(systemDirectoryDocumentChapters.getParentId());//
				systemDirectoryDocumentChapters.setChaptersOrder(maxOrder==null? 1 : maxOrder+1);
			}else {//一级目录 chaptersOrder前台传
				
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
	* @Description: 删除文档附件
	* @author author
	* @param directoryDocumentId 文档ID
	* @param documentType 文档类型
	* @param currentUserAccount 操作人
	* @param requirementCode 需求编号
	* @param featureCode 开发任务编号
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
		//组装报文
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
        //查看请求报文
        mapAll.put("requestBody",mapBody);
        log.info("请求报文--------->>>>>>"+ JSON.toJSONString(mapAll,true));
        //发送请求demandfilesaveservicerest/Restful
        HttpEntity<Map<String, Object>> sendParam = new HttpEntity<>(mapAll, headers);
        Map [] mapResult = send(sendParam);
		
	}
   
	//发送esb报文
	public Map [] send(HttpEntity<Map<String, Object>> he) {
        Map [] maps = null;
        try{
            //发送请求
            ResponseEntity<String> result = restTemplate.exchange
                    (syncallback, HttpMethod.POST, he,String.class);
            if(result.getStatusCode().is2xxSuccessful()){
                log.info("响应报文--------->>>>>>"+result.getBody());
                String str = JSON.parseObject(result.getBody()).get("responseBody").toString();
                maps = JSONArray.parseObject(str,Map [].class);
            }else{
                log.info("调用失败，状态码："+result.getStatusCodeValue()+"。");
            }
        }catch (Exception ex){
            ex.getMessage();
        }
        return maps;
    }
	
	public String getCharset(InputStream is) throws IOException {
        BufferedInputStream bin = new BufferedInputStream(is);
        int p = (bin.read() << 8) + bin.read();//读取文件头前16位  
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
