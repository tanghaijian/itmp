package cn.pioneeruniverse.project.service.itReAssetsLibrary.impl;

import java.io.File;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.pioneeruniverse.common.dto.TblAttachementInfoDTO;
import cn.pioneeruniverse.common.utils.BrowserUtil;
import cn.pioneeruniverse.common.utils.CollectionUtil;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.S3Util;
import cn.pioneeruniverse.project.dao.mybatis.ProjectInfoMapper;
import cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary.ReASystemDirectoryDocumentYiRanDao;
import cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary.ReSystemDirectoryDao;
import cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary.ReSystemDirectoryDocumentDao;
import cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary.ReSystemDirectoryDocumentHistoryDao;
import cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary.ReTblSystemDirectoryDocumentAttachmentMapper;
import cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary.ReTblSystemDirectoryDocumentChaptersHistoryMapper;
import cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary.ReTblSystemDirectoryDocumentChaptersMapper;
import cn.pioneeruniverse.project.entity.TblProjectInfo;
import cn.pioneeruniverse.project.entity.TblSystemDirectory;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentAttachment;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChapters;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersHistory;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentHistory;
import cn.pioneeruniverse.project.service.itReAssetsLibrary.ItReSystemDirectoryService;
import cn.pioneeruniverse.project.service.requirement.impl.RequirementServiceImpl;

@Transactional(readOnly = true)
@Service("itResystemDirectoryService")
public class ItReSystemDirectoryServiceImpl extends ServiceImpl<ReSystemDirectoryDao, TblSystemDirectory> implements ItReSystemDirectoryService {
	private static Logger log = LoggerFactory.getLogger(RequirementServiceImpl.class);

	@Autowired
    private ReSystemDirectoryDocumentHistoryDao resystemDirectoryDocumentHistoryDao;
	@Autowired
	private ReTblSystemDirectoryDocumentChaptersMapper systemDirectoryDocumentChaptersMapper;
	@Autowired
	private ReSystemDirectoryDocumentDao reSystemDirectoryDocumentDao;
	@Autowired
	private ReSystemDirectoryDao reSystemDirectoryDao;
	@Autowired
	private ProjectInfoMapper projectInfoMapper;
	@Autowired
	private ReTblSystemDirectoryDocumentAttachmentMapper reTblSystemDirectoryDocumentAttachmentMapper;
	@Autowired
    private ReTblSystemDirectoryDocumentChaptersHistoryMapper chaptersHistoryMapper;
	@Autowired
    private ReASystemDirectoryDocumentYiRanDao reASystemDirectoryDocumentYiRanDao;
	@Autowired
    private S3Util s3Util;
    @Value("${s3.documentBucket}")
    private String documentBucket;
   
    /**
     * 
    * @Title: getSystemDirectoryDocumentHistory
    * @Description: ??????????????????
    * @author author
    * @param documentId ??????ID
    * @param requirementCode ????????????
    * @param featureCode ??????????????????
    * @return List<TblSystemDirectoryDocumentHistory>??????????????????
     */
	@Override
	@Transactional(readOnly = true)
	public List<TblSystemDirectoryDocumentHistory> getSystemDirectoryDocumentHistory(Long documentId,String requirementCode,String featureCode) {
		//?????????????????????????????????????????????????????????????????????
		if(featureCode!=null&&!featureCode.equals("")) {
			return resystemDirectoryDocumentHistoryDao.getFeHistoryByDocumentId(documentId);
			
		}else {//????????????
			return resystemDirectoryDocumentHistoryDao.getReHistoryByDocumentId(documentId);
		}
		
	}

	/**
	 * 
	* @Title: getAllDocChapters
	* @Description: ????????????????????????????????????
	* @author author
	* @param systemDirectoryDocumentId ??????ID
	* @return List<TblSystemDirectoryDocumentChapters> ????????????
	 */
	@Override
	public List<TblSystemDirectoryDocumentChapters> getAllDocChapters(Long systemDirectoryDocumentId) {
		return systemDirectoryDocumentChaptersMapper.getAllBySystemDirectoryDocumentId(systemDirectoryDocumentId);

	}

	/**
	 * 
	* @Title: addSystemDirectory
	* @Description: ??????????????????????????????????????????
	* @author author
	* @param request
	* @param tblSystemDirectory ??????????????????
	* @return TblSystemDirectory
	 */
	@Override
	@Transactional(readOnly = false)
	public TblSystemDirectory addSystemDirectory(HttpServletRequest request,
			TblSystemDirectory tblSystemDirectory) {
		
		//???????????????????????????????????????
		if(tblSystemDirectory.getParentId()==null) {
			TblProjectInfo tblProjectInfo=projectInfoMapper.selectProjectById(tblSystemDirectory.getProjectId());
			tblSystemDirectory.setProjectType(tblProjectInfo.getProjectType());
		}
		tblSystemDirectory.setCreateType(2);
		tblSystemDirectory.preInsertOrUpdate(request);
		reSystemDirectoryDao.addSystemDirectory(tblSystemDirectory);
		//TblSystemDirectory data= systemDirectoryDao.selectByDirectory(tblSystemDirectory.getId());
		return tblSystemDirectory;
	}

	/**
	 * 
	* @Title: delSystemDirectory
	* @Description: ??????????????????
	* @author author
	* @param directoryId ??????ID
	* @throws Exception
	 */
	@Override
	@Transactional(readOnly = false)
	public void delSystemDirectory(Long directoryId) throws Exception {
		TblSystemDirectory tblSystemDirectory = reSystemDirectoryDao.selectById(directoryId);
		if (ObjectUtils.equals(tblSystemDirectory.getCreateType(), 1)) {
			throw new Exception("????????????????????????????????????????????????");
		}
		//?????????????????????????????????ID??????
		List<Long> directoryIds = reSystemDirectoryDao.getAllSonDirectoryIds(tblSystemDirectory);
		//?????????????????????????????????????????????
		if (CollectionUtil.isNotEmpty(directoryIds)) {
			Boolean existRelatedDocuments = reSystemDirectoryDocumentDao.existRelatedDocuments(directoryIds);
			if (existRelatedDocuments) {
				throw new Exception("??????????????????????????????????????????");
			} else {
				reSystemDirectoryDao.delSystemDirectoriesByIds(directoryIds);
			}
		}
	}

	/**
	 * 
	* @Title: updateSystemDirectoryName
	* @Description: ??????????????????
	* @author author
	* @param request
	* @param directoryId ??????ID
	* @param directoryName ????????????
	* @throws Exception
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateSystemDirectoryName(HttpServletRequest request, Long directoryId, String directoryName) throws Exception {
		/* TblSystemDirectory tblSystemDirectory = systemDirectoryDao.selectByDirectory(directoryId);
	        if (ObjectUtils.equals(tblSystemDirectory.getCreateType(), 1)) {
	            throw new Exception("????????????????????????????????????????????????");
	        }*/
		TblSystemDirectory newDirectory = new TblSystemDirectory();
		newDirectory.setDirName(directoryName);
		newDirectory.setId(directoryId);
		newDirectory.preInsertOrUpdate(request);
		reSystemDirectoryDao.updateSystemDirectoryName(newDirectory);
	}

	/**
	 * 
	* @Title: getDirectoryTreeForDocumentLibrary
	* @Description: ????????????????????????????????????
	* @author author
	* @param projectId ??????ID
	* @return List<TblSystemDirectory> ????????????
	 */
	@Override
	public List<TblSystemDirectory> getDirectoryTreeForDocumentLibrary(Long projectId) {
		return reSystemDirectoryDao.getDirectoryTreeForDocumentLibrary(projectId);
	}
	
	
	/**
	 * 
	* @Title: moveDocChapters
	* @Description: ??????????????????
	* @author author
	* @param systemDirectoryDocumentChapters ????????????
	 */
	@Override
	@Transactional(readOnly = false)
	public void moveDocChapters(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters) {
		Long id = systemDirectoryDocumentChapters.getId();
		//??????????????????????????? parentId parentIDs chaptersLevel chaptersOrder
		 systemDirectoryDocumentChaptersMapper.updateParent(systemDirectoryDocumentChapters);
		 //??????????????????????????????order??????+1
		 if(systemDirectoryDocumentChapters.getChaptersOrder()!=null) {
			 //if(systemDirectoryDocumentChapters.getParentId()!=null) {//???????????????????????? ????????????????????????id????????????order>=???????????????order???????????????order+1
				systemDirectoryDocumentChaptersMapper.updateChaptersOrder(systemDirectoryDocumentChapters);
//			 }else {//????????????????????? ?????????id ????????????id???????????????????????????????????????  ??????????????????order+1
//				 
//			 }
		}
		 //??????????????????children ???????????????parentIds ?????????????????????id ????????????????????????????????????????????? ????????????
		List<TblSystemDirectoryDocumentChapters> children = systemDirectoryDocumentChaptersMapper.getChildrenChapters(systemDirectoryDocumentChapters.getId());
		if(!children.isEmpty() && children.size()>0) {
			for (TblSystemDirectoryDocumentChapters tblSystemDirectoryDocumentChapters : children) {
				int index = tblSystemDirectoryDocumentChapters.getParentIds().indexOf(id.toString());
				String parentIds = tblSystemDirectoryDocumentChapters.getParentIds().substring(index, tblSystemDirectoryDocumentChapters.getParentIds().length());
				parentIds =  systemDirectoryDocumentChapters.getParentIds()+ parentIds;
				tblSystemDirectoryDocumentChapters.setParentIds(parentIds);
				systemDirectoryDocumentChaptersMapper.updateParentIdsById(tblSystemDirectoryDocumentChapters);
				 /*if(tblSystemDirectoryDocumentChapters.getChaptersOrder()!=null && tblSystemDirectoryDocumentChapters.getChaptersOrder()>=systemDirectoryDocumentChapters.getChaptersOrder()) {
					systemDirectoryDocumentChaptersMapper.updateChaptersOrder(tblSystemDirectoryDocumentChapters.getId());
				}*/
			}
		}
		//???????????? ??????????????????????????????+1
		updateLevel(systemDirectoryDocumentChapters);
			
	}
	
	/**
	 * 
	* @Title: updateLevel
	* @Description: ??????????????????????????????????????????PARENT_IDS????????????????????????
	* @author author
	* @param systemDirectoryDocumentChapters ????????????
	 */
	public void updateLevel(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters) {
		List<TblSystemDirectoryDocumentChapters> child = systemDirectoryDocumentChaptersMapper.getChildChapters(systemDirectoryDocumentChapters.getId());
		if(!child.isEmpty() && child.size()>0) {
			for (TblSystemDirectoryDocumentChapters tblSystemDirectoryDocumentChapters : child) {
				systemDirectoryDocumentChaptersMapper.updateChaptersLevel(tblSystemDirectoryDocumentChapters.getId(),systemDirectoryDocumentChapters.getChaptersLevel());
				updateLevel(tblSystemDirectoryDocumentChapters);//???????????? ??????????????????
			}
		}
	}

	/**
	 * 
	* @Title: deleteDocChapters
	* @Description: ????????????
	* @author author
	* @param systemDirectoryDocumentChapters ????????????ID
	* @param currentUserAccount ??????????????????????????????????????????????????????
	* @param request
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteDocChapters(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters,
		String	currentUserAccount,HttpServletRequest request) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();  
		encryptor.setPassword("ccic");  
		currentUserAccount = currentUserAccount.replaceAll(" ","+");
		String userAccount=encryptor.decrypt(currentUserAccount);
		Long userId  = reASystemDirectoryDocumentYiRanDao.selectUserId(userAccount);
		systemDirectoryDocumentChapters.setLastUpdateBy(userId);
		systemDirectoryDocumentChapters.setLastUpdateDate(new Timestamp(new Date().getTime()));
		//????????????
		systemDirectoryDocumentChaptersMapper.updateStatusById(systemDirectoryDocumentChapters);
			
			
	}

	/**
	 * 
	* @Title: removeChaptersFile
	* @Description: ??????????????????
	* @author author
	* @param directoryDocumentAttachment ??????????????????
	* @param currentUserAccount ???????????????
	* @param request
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void removeChaptersFile(TblSystemDirectoryDocumentAttachment directoryDocumentAttachment,
			String currentUserAccount,HttpServletRequest request) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();  
		encryptor.setPassword("ccic");  
		currentUserAccount = currentUserAccount.replaceAll(" ","+");
		String userAccount=encryptor.decrypt(currentUserAccount);
		directoryDocumentAttachment.setStatus(2);
		directoryDocumentAttachment.setLastUpdateBy(reASystemDirectoryDocumentYiRanDao.selectUserId(userAccount));
		directoryDocumentAttachment.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
		//????????????
		reTblSystemDirectoryDocumentAttachmentMapper.removeChaptersFile(directoryDocumentAttachment);
			
	}

	/**
	 * 
	* @Title: selectChaptersMarkDownContrast
	* @Description: ??????md???????????????????????????
	* @author author
	* @param type 1????????????????????????????????????2 ????????????????????????????????? 3 ?????????????????????????????????
	* @param systemDirectoryDocumentChapters ????????????
	* @param request
	* @return Map<String, Object>
	* @throws Exception
	 */
	@Override
	public Map<String, Object> selectChaptersMarkDownContrast(Integer type, TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters, HttpServletRequest request) throws Exception{
        Map<String, Object> result = new HashMap<>();
        // ????????????
        result.put("latestVersion",selectChaptersMarkDown(systemDirectoryDocumentChapters,request));
        if(type.intValue() == 2){
            // ????????????
            TblSystemDirectoryDocumentChaptersHistory history = chaptersHistoryMapper.getMaxVersionChaptersHistory(systemDirectoryDocumentChapters.getId());
            if(history != null) {
                String markDowm = "";
                if(StringUtils.isNotBlank(history.getChaptersS3Bucket()) && StringUtils.isNotBlank(history.getChaptersTempS3Key())) {
                    //???MONGO??????KEY(??????????????????)
                    markDowm = s3Util.getStringByS3(history.getChaptersS3Bucket(),history.getChaptersTempS3Key());
                }else {
                    markDowm = s3Util.getStringByS3(history.getChaptersS3Bucket(),history.getChaptersS3Key());
                }
                //????????????
                result.put("lastVersion",markDowm);
            }
        }
        return result;
    }

	/**
	 * 
	* @Title: selectChaptersMarkDown
	* @Description: ??????md?????????????????????
	* @author author
	* @param systemDirectoryDocumentChapters ????????????
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
	        Integer buttonState = 2;
	        if(userId.equals(chapters.getCheckoutUserId()) && chapters.getCheckoutStatus() == 1 && StringUtils.isNotBlank(chapters.getChaptersS3Bucket()) && StringUtils.isNotBlank(chapters.getChaptersTempS3Key())) {
	            //???MONGO??????KEY(??????????????????)
	            markdown = s3Util.getStringByS3(chapters.getChaptersS3Bucket(),chapters.getChaptersTempS3Key());
	            //markdown??????
	            result.put("markdown",markdown);
	            //????????????????????????
	            result.put("buttonState",1);
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
	* @Title: uploadChaptersFiles
	* @Description: ??????????????????
	* @author author
	* @param files ??????
	* @param directoryDocumentAttachment  ??????????????????
	* @param requirementCode ????????????
	* @param featureCode ??????????????????
	* @param currentUserAccount ?????????
	* @param fileName ?????????
	* @param request
	* @return Long ??????????????????
	* @throws Exception
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public  Integer uploadChaptersFiles(MultipartFile[] files,
			TblSystemDirectoryDocumentAttachment directoryDocumentAttachment, String requirementCode,String featureCode,String currentUserAccount,String[] fileName,HttpServletRequest request)
			throws Exception {
		 Integer number = 0;
		 
		 StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();  
		 encryptor.setPassword("ccic");  
		 currentUserAccount = currentUserAccount.replaceAll(" ","+");
		 String userAccount=encryptor.decrypt(currentUserAccount);
		 Long userId  = reASystemDirectoryDocumentYiRanDao.selectUserId(userAccount);
	        if (files.length > 0 && files != null) {
	        	List<String> fileNameList= Arrays.asList(fileName);
	        	int i=0;
	            for (MultipartFile file : files) {
	                if (!file.isEmpty()) {
	                	//??????S3
	                    TblAttachementInfoDTO attachementInfoDTO = this.updateMultipartFile(file,fileNameList.get(i), s3Util, documentBucket, request);
	                    directoryDocumentAttachment.setAttachmentNameOld(attachementInfoDTO.getFileNameOld());
	                    directoryDocumentAttachment.setAttachmentS3Bucket(attachementInfoDTO.getFileS3Bucket());
	                    directoryDocumentAttachment.setAttachmentS3Key(attachementInfoDTO.getFileS3Key());
	                    directoryDocumentAttachment.setAttachmentUrl(attachementInfoDTO.getFilePath());
	                    directoryDocumentAttachment.setCreateBy(userId);
	                    directoryDocumentAttachment.setCreateDate(new Timestamp(new Date().getTime()));
	                    if (!attachementInfoDTO.getFileS3Key().equals("error")) {
	                        number = reTblSystemDirectoryDocumentAttachmentMapper.insertSelective(directoryDocumentAttachment);
	                    }
	                }
	                i=i+1;
	            }
	        }	
	        return number;
	}
		
	/**
	 * 
	* @Title: updateMultipartFile
	* @Description: ?????????s3
	* @author author
	* @param file ??????
	* @param fileNameOld ????????????
	* @param s3Util
	* @param bucketName s3???
	* @param request
	* @return TblAttachementInfoDTO
	* @throws Exception TblAttachementInfoDTO
	 */
	  public static TblAttachementInfoDTO updateMultipartFile(MultipartFile file,String fileNameOld, S3Util s3Util, String bucketName, HttpServletRequest request) throws Exception {
	        TblAttachementInfoDTO attachementInfoDTO = new TblAttachementInfoDTO();
	        try {
	            Map map = new HashMap();
	            String keyName = null;
	            InputStream inputStream = file.getInputStream();
	            map = new HashMap<String, Object>();
	            String fileType = file.getOriginalFilename()
	                    .substring(file.getOriginalFilename().lastIndexOf(".") + 1);// ?????????

	            if (BrowserUtil.isMSBrowser(request)) {
	                fileNameOld = fileNameOld.substring(fileNameOld.lastIndexOf("\\")+1);
	            }
	            keyName = s3Util.putFileUpload(bucketName, UUID.randomUUID().toString(), inputStream, file.getContentType());

	            attachementInfoDTO.setFileS3Bucket(bucketName);
	            attachementInfoDTO.setFileS3Key(keyName.equals("error") ? "" : keyName);
	            attachementInfoDTO.setFilePath(attachementInfoDTO.getFilePath() + bucketName + File.separatorChar +new Date().getTime()+"-"+ fileNameOld);
	            attachementInfoDTO.setFileNameOld(fileNameOld);
	            attachementInfoDTO.setFileType(fileType);
	            log.info("?????????????????????" + attachementInfoDTO.getFilePath());
	        }catch (Exception e){
	        	log.error("?????????????????????" + attachementInfoDTO.getFilePath());
	            throw new Exception(e.getMessage());
	        }
	        return attachementInfoDTO;
	    }

}
