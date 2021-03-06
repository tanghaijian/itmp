package cn.pioneeruniverse.project.service.wikiLibrary.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.SystemDirectoryDocumentDao;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.TblSystemDirectoryDocumentAttachmentMapper;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.TblSystemDirectoryDocumentChaptersMapper;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.TblSystemDirectoryDocumentMapper;
import cn.pioneeruniverse.project.dao.mybatis.wikiLibrary.WikiSystemDirectoryMapper;
import cn.pioneeruniverse.project.entity.TblSystemDirectory;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocument;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentAttachment;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChapters;
import cn.pioneeruniverse.project.service.wikiLibrary.WikiService;
import cn.pioneeruniverse.project.vo.SystemDirectoryDocumentVO;

@Service("wikiService")
@Transactional(readOnly=true)
public class WikiServiceImpl implements WikiService{
	//??????s3??????????????????????????????
	private static  final  String MARKDOWNPATH = "../projectManage/src/main/resources/";
	private static final Logger logger = LoggerFactory.getLogger(WikiServiceImpl.class);
	@Autowired
	private WikiSystemDirectoryMapper wikiSystemDirectoryMapper;
	@Autowired
	private SystemDirectoryDocumentDao systemDirectoryDocumentDao;
	@Autowired
	private TblSystemDirectoryDocumentMapper tblSystemDirectoryDocumentMapper;
	@Autowired
	private TblSystemDirectoryDocumentChaptersMapper systemDirectoryDocumentChaptersMapper;
	@Autowired
	private ASystemDirectoryDocumentYiRanDao aSystemDirectoryDocumentYiRanDao;
	@Autowired
    private S3Util s3Util;
	@Autowired
    private TblSystemDirectoryDocumentAttachmentMapper directoryDocumentAttachmentMapper;
	@Value("${s3.documentBucket}")
    private String documentBucket;
	
	/**
	 * 
	* @Title: getSystemDirectory
	* @Description: ??????????????????ID
	* @author author
	* @param projectId ??????ID
	* @param request
	* @return Long
	 */
	@Override
	@Transactional(readOnly=false)
	public Long getSystemDirectory(Long projectId,HttpServletRequest request) {
		List<TblSystemDirectory> list=wikiSystemDirectoryMapper.getDirectoryByProjectId(projectId);
		Long systemDiretoryId=0l;
		List<Map<String, Object>> systemList=wikiSystemDirectoryMapper.systemSystemName(projectId);
		//????????????????????????????????????ID
		if(list.size()>0) {
			systemDiretoryId=list.get(0).getId();
		}else {
			 //???????????????????????????????????????????????????
			if(systemList.size()>0) {
				TblSystemDirectory tblSystemDirectory=new TblSystemDirectory();
				tblSystemDirectory.setProjectId(projectId);
				tblSystemDirectory.setProjectType(2);
				tblSystemDirectory.setDirName("wiki??????");
				tblSystemDirectory.setOrderNumber(1);
				CommonUtil.setBaseValue(tblSystemDirectory, request);
				tblSystemDirectory.setTierNumber(1);
				tblSystemDirectory.setParentId(0l);
				tblSystemDirectory.setParentIds("0");
				tblSystemDirectory.setCreateType(1);
				tblSystemDirectory.setDirectoryType(2);
				wikiSystemDirectoryMapper.addSystemDirectory(tblSystemDirectory);
				systemDiretoryId=tblSystemDirectory.getId();
				
			}
		}
		
		
		for(int i=0;i<systemList.size();i++) {
			Long systemId=Long.parseLong(systemList.get(i).get("id").toString());
			List<TblSystemDirectoryDocument> sysDirectoryList=systemDirectoryDocumentDao.selectBySystemId(systemId,projectId);
			//?????????????????????????????????????????????????????????????????????????????????
			if(sysDirectoryList.size()==0) {
				TblSystemDirectoryDocument tblSystemDirectoryDocument=new TblSystemDirectoryDocument();
				tblSystemDirectoryDocument.preInsertOrUpdate(request);
				tblSystemDirectoryDocument.setDocumentVersion(0);
		        tblSystemDirectoryDocument.setSaveType(3);
		        tblSystemDirectoryDocument.setDocumentName("wiki????????????");
		        tblSystemDirectoryDocument.setSystemDirectoryId(systemDiretoryId);
		        tblSystemDirectoryDocument.setSystemId(Long.parseLong(systemList.get(i).get("id").toString()));
		        systemDirectoryDocumentDao.insertDirectoryDocument(tblSystemDirectoryDocument);
		        TblSystemDirectoryDocumentChapters record=new TblSystemDirectoryDocumentChapters();
		        record.setSystemDirectoryDocumentId(tblSystemDirectoryDocument.getId());
		        record.setDocumentVersion(0);
		        record.setChaptersName("????????????");
		        record.setChaptersLevel(1);
		        record.setChaptersOrder(1);
		        record.setChaptersVersion(0);
		        record.setCheckoutStatus(2);
		        //record.setCheckoutUserId(CommonUtil.getCurrentUserId(request));
		        record.setStatus(1);
		        Date date = new Date();       
		        Timestamp nousedate = new Timestamp(date.getTime());
		        record.setCreateDate(nousedate);
		        record.setCreateBy(CommonUtil.getCurrentUserId(request));
		        systemDirectoryDocumentChaptersMapper.insert(record);
			}
			
		}
		return systemDiretoryId;
	}

	

	@Override
	public List<Map<String, Object>> selectSystemByProjectId(Long projectId, String systemName) {
		
		return wikiSystemDirectoryMapper.systemSystemName2(projectId);
	}

	/**
	 * 
	* @Title: richTextSubmit
	* @Description: ??????????????????????????????html??????????????????
	* @author author
	* @param request
	* @param systemDirectoryDocumentVO ????????????
	* @return ResponseMessageModel
	 */
	@Override
	@Transactional(readOnly=false)
	public ResponseMessageModel richTextSubmit(HttpServletRequest request,
			SystemDirectoryDocumentVO systemDirectoryDocumentVO) {
		
		 ResponseMessageModel messageModel = new ResponseMessageModel();
	        Integer code = 0;
	        try{
	            Timestamp lastUpdateDate = new Timestamp(System.currentTimeMillis());
	            Long lastUpdateId = CommonUtil.getCurrentUserId(request);
	            String msg = this.writeTxtFile(systemDirectoryDocumentVO);  //s3????????????
	            String htmlMsg = this.writHtmlFile(systemDirectoryDocumentVO); //s3html????????????
	            if(!msg.equals("error") && !htmlMsg.equals("error")){
	                systemDirectoryDocumentVO.setLastUpdateDate(lastUpdateDate.toString());
	                systemDirectoryDocumentVO.setLastUpdateBy(lastUpdateId);
	                //?????????????????????????????????   ???????????? =  ????????????+1  ???????????? = ???????????? +1
	                //??????????????????
	                TblSystemDirectoryDocument tblSystemDirectoryDocument = aSystemDirectoryDocumentYiRanDao.selectDocumentVersionById(systemDirectoryDocumentVO.getSystemDirectoryDocumentId());

	            //??????????????????????????????
	                TblSystemDirectoryDocumentChapters directoryDocumentChaptersById = aSystemDirectoryDocumentYiRanDao.getDirectoryDocumentChaptersById(systemDirectoryDocumentVO.getId());
	                Map<String,Object> result = new HashMap<>();
                    result.put("chaptersVersion", directoryDocumentChaptersById.getChaptersVersion()+1);
                    messageModel.setResult(result);
	                if (systemDirectoryDocumentVO.getChaptersVersion() > 0){
	                    aSystemDirectoryDocumentYiRanDao.insertSystemDirectoryDocumentHistory(tblSystemDirectoryDocument);
	                    //????????????????????????????????????????????????
	                    aSystemDirectoryDocumentYiRanDao.insertDirectoryDocumentHistory(directoryDocumentChaptersById);
	                }
	                  //??????
	                aSystemDirectoryDocumentYiRanDao.directoryDocumentSubmitById(systemDirectoryDocumentVO);

	                //????????????????????????????????????+1
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
	 * 
	* @Title: writeTxtFile
	* @Description: ?????????????????????????????????S3
	* @author author
	* @param systemDirectoryDocument ????????????
	* @return String S3?????????key
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
	        	//??????????????????
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
	 * @Description: html?????????????????????S3
	 * @author author
	 * @param systemDirectoryDocument ????????????
	 * @return s3?????????Key
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

	 
	 /**
	  * 
	 * @Title: systemDirectoryDocumentSignOffById
	 * @Description: ????????????
	 * @author author
	 * @param id ??????ID
	 * @param request
	 * @return Map<String,Object>
	  */
	@Override
	@Transactional(readOnly=false)
	public Map systemDirectoryDocumentSignOffById(Long id, HttpServletRequest request) {
		 Map<String,Object> mapValues = new HashMap<>();
	        //????????????????????????
	        SystemDirectoryDocumentVO systemDirectory = aSystemDirectoryDocumentYiRanDao.systemDirectoryDocumentSignOffById(id);
	        //????????????????????????id
	        Long userId  = CommonUtil.getCurrentUserId(request);
	        String content = null;
	        Integer code = 0;
	        //?????????????????????S3_MONGO_KEY,???????????????S3????????????
	        try{
	           if (StringUtils.isNotBlank(systemDirectory.getChaptersS3Bucket()) && StringUtils.isNotBlank(systemDirectory.getChaptersS3Key())){
	                content = s3Util.getStringByS3(systemDirectory.getChaptersS3Bucket(), systemDirectory.getChaptersS3Key());
	                systemDirectory.setDirectoryDocumentContent(content);
	                //?????????????????????1:??????2:??????
	            }
	            code = aSystemDirectoryDocumentYiRanDao.updateDirectoryDocumentSignOffStatusById(id, userId);
	           //????????????
	            mapValues.put("systemDirectory",systemDirectory);
	            //??????????????????0?????????????????????
	            mapValues.put("code",code);
	            //??????S3????????????????????????
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
	 * 
	* @Title: addTemporaryStorageById
	* @Description: ??????
	* @author author
	* @param request
	* @param systemDirectoryDocumentVO
	* @return ResponseMessageModel
	* @throws IOException
	 */
	@Override
	@Transactional(readOnly=false)
	public ResponseMessageModel addTemporaryStorageById(HttpServletRequest request,
			SystemDirectoryDocumentVO systemDirectoryDocumentVO) throws IOException {
		ResponseMessageModel messageModel = new ResponseMessageModel();
		Integer code = 0;
        String msg = this.writeTxtFile(systemDirectoryDocumentVO);  //s3????????????
        String htmlMsg = this.writHtmlFile(systemDirectoryDocumentVO); //s3html????????????
        if(!msg.equals("error") && !htmlMsg.equals("error")){
            systemDirectoryDocumentVO.getLastUpdateDate();
            Long userId = CommonUtil.getCurrentUserId(request);
            systemDirectoryDocumentVO.setLastUpdateBy(userId);
           //??????
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
	 * 
	* @Title: selectChaptersFiles
	* @Description: ??????????????????
	* @author author
	* @param systemDirectoryDocumentChapters
	* @param request
	* @return List<TblSystemDirectoryDocumentAttachment>
	 */
	@Override
	public List<TblSystemDirectoryDocumentAttachment> selectChaptersFiles(
			TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters, HttpServletRequest request) {
		return directoryDocumentAttachmentMapper.selectAttachmentByChaptersId(systemDirectoryDocumentChapters.getId(),null);
	}

	

	@Override
	public List<TblSystemDirectoryDocumentChapters> selectWikiTree(Long projectId, Long systemId) {
		return systemDirectoryDocumentChaptersMapper.selectWikiTree(systemId, projectId);
	}

	/**
	 * 
	* @Title: selectChaptersMarkDown
	* @Description: ??????????????????Markdown??????
	* @author author
	* @param systemDirectoryDocumentChapters ???????????????????????????ID???
	* @param request
	* @return Map<String, Object>
	* @throws Exception
	 */
	@Override
	public Map<String, Object> selectChaptersMarkDown(
			TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters, HttpServletRequest request)
			throws Exception {
		 Map<String, Object> result = new HashMap<>();
	        TblSystemDirectoryDocumentChapters chapters = systemDirectoryDocumentChaptersMapper.selectChaptersWithRelatedData(systemDirectoryDocumentChapters.getId());
	        Long userId = CommonUtil.getCurrentUserId(request);
	        String markdown = null;
	        //??????????????????????????????????????????????????????????????????????????????
	        Integer buttonState = 2;
	        //???????????????????????????????????????????????????
	        if(userId.equals(chapters.getCheckoutUserId()) && chapters.getCheckoutStatus() == 1) {
	            //???MONGO??????KEY(??????????????????)
	            markdown = s3Util.getStringByS3(chapters.getChaptersS3Bucket(),chapters.getChaptersTempS3Key());
	           //markdown??????
	            result.put("markdown",markdown);
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
	
	        result.put("markdown",markdown);
	        result.put("chapters",chapters);
	        result.put("buttonState",buttonState);
	        return  result;
	}

	/**
	 * 
	* @Title: getAllDocChapters
	* @Description: ?????????????????????????????????????????????
	* @author author
	* @param systemDirectoryDocumentIds ???,?????????????????????ID
	* @return List<TblSystemDirectoryDocument>
	 */
	@Override
	public List<TblSystemDirectoryDocument> getAllDocChapters(String systemDirectoryDocumentIds) {
		
		String id[]=systemDirectoryDocumentIds.split(",");
		List<TblSystemDirectoryDocument> list= new ArrayList<>();
		for(int i=0;i<id.length;i++) {
			TblSystemDirectoryDocument tblSystemDirectoryDocument=tblSystemDirectoryDocumentMapper.getAllDocChapters(Long.parseLong(id[i]));
			if(tblSystemDirectoryDocument!=null) {
				list.add(tblSystemDirectoryDocument);
			}
		}
		
		return list;
	}
	
	
}
