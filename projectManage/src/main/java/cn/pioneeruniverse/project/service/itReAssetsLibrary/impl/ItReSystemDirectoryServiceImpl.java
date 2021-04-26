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
    * @Description: 获取文档历史
    * @author author
    * @param documentId 文档ID
    * @param requirementCode 需求编码
    * @param featureCode 开发任务编码
    * @return List<TblSystemDirectoryDocumentHistory>文档历史列表
     */
	@Override
	@Transactional(readOnly = true)
	public List<TblSystemDirectoryDocumentHistory> getSystemDirectoryDocumentHistory(Long documentId,String requirementCode,String featureCode) {
		//如果有开发任务编码，则查找与开发任务关联的文档
		if(featureCode!=null&&!featureCode.equals("")) {
			return resystemDirectoryDocumentHistoryDao.getFeHistoryByDocumentId(documentId);
			
		}else {//普通查找
			return resystemDirectoryDocumentHistoryDao.getReHistoryByDocumentId(documentId);
		}
		
	}

	/**
	 * 
	* @Title: getAllDocChapters
	* @Description: 获取一个文档下的所有章节
	* @author author
	* @param systemDirectoryDocumentId 文档ID
	* @return List<TblSystemDirectoryDocumentChapters> 章节列表
	 */
	@Override
	public List<TblSystemDirectoryDocumentChapters> getAllDocChapters(Long systemDirectoryDocumentId) {
		return systemDirectoryDocumentChaptersMapper.getAllBySystemDirectoryDocumentId(systemDirectoryDocumentId);

	}

	/**
	 * 
	* @Title: addSystemDirectory
	* @Description: 新增文档目录结构（树形结构）
	* @author author
	* @param request
	* @param tblSystemDirectory 文档目录信息
	* @return TblSystemDirectory
	 */
	@Override
	@Transactional(readOnly = false)
	public TblSystemDirectory addSystemDirectory(HttpServletRequest request,
			TblSystemDirectory tblSystemDirectory) {
		
		//获取项目类型新建类、运维类
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
	* @Description: 删除文档目录
	* @author author
	* @param directoryId 目录ID
	* @throws Exception
	 */
	@Override
	@Transactional(readOnly = false)
	public void delSystemDirectory(Long directoryId) throws Exception {
		TblSystemDirectory tblSystemDirectory = reSystemDirectoryDao.selectById(directoryId);
		if (ObjectUtils.equals(tblSystemDirectory.getCreateType(), 1)) {
			throw new Exception("该目录为自动创建目录，不得删除！");
		}
		//获取当前目录及其子目录ID集合
		List<Long> directoryIds = reSystemDirectoryDao.getAllSonDirectoryIds(tblSystemDirectory);
		//判断上述集合中是否存在关联文档
		if (CollectionUtil.isNotEmpty(directoryIds)) {
			Boolean existRelatedDocuments = reSystemDirectoryDocumentDao.existRelatedDocuments(directoryIds);
			if (existRelatedDocuments) {
				throw new Exception("该目录下存在文档，不得删除！");
			} else {
				reSystemDirectoryDao.delSystemDirectoriesByIds(directoryIds);
			}
		}
	}

	/**
	 * 
	* @Title: updateSystemDirectoryName
	* @Description: 修改目录名称
	* @author author
	* @param request
	* @param directoryId 目录ID
	* @param directoryName 目录名称
	* @throws Exception
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateSystemDirectoryName(HttpServletRequest request, Long directoryId, String directoryName) throws Exception {
		/* TblSystemDirectory tblSystemDirectory = systemDirectoryDao.selectByDirectory(directoryId);
	        if (ObjectUtils.equals(tblSystemDirectory.getCreateType(), 1)) {
	            throw new Exception("该目录为自动创建目录，不得修改！");
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
	* @Description: 获取某个项目下的目录结构
	* @author author
	* @param projectId 项目ID
	* @return List<TblSystemDirectory> 目录列表
	 */
	@Override
	public List<TblSystemDirectory> getDirectoryTreeForDocumentLibrary(Long projectId) {
		return reSystemDirectoryDao.getDirectoryTreeForDocumentLibrary(projectId);
	}
	
	
	/**
	 * 
	* @Title: moveDocChapters
	* @Description: 移动章节位置
	* @author author
	* @param systemDirectoryDocumentChapters 章节信息
	 */
	@Override
	@Transactional(readOnly = false)
	public void moveDocChapters(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters) {
		Long id = systemDirectoryDocumentChapters.getId();
		//移动的目录需要修改 parentId parentIDs chaptersLevel chaptersOrder
		 systemDirectoryDocumentChaptersMapper.updateParent(systemDirectoryDocumentChapters);
		 //移动的位置之后节点的order都要+1
		 if(systemDirectoryDocumentChapters.getChaptersOrder()!=null) {
			 //if(systemDirectoryDocumentChapters.getParentId()!=null) {//移动到非一级章节 根据移动位置的父id查询所有order>=移动位置的order的节点然后order+1
				systemDirectoryDocumentChaptersMapper.updateChaptersOrder(systemDirectoryDocumentChapters);
//			 }else {//移动到一级章节 没有父id 根据文档id查询这个文档下所有一级章节  移动位置后的order+1
//				 
//			 }
		}
		 //移动的目录的children 只需要修改parentIds 找到移动的父级id 替换掉在这个父级之前的所有父级 保留后面
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
		//修改层级 子级在父级的层级数上+1
		updateLevel(systemDirectoryDocumentChapters);
			
	}
	
	/**
	 * 
	* @Title: updateLevel
	* @Description: 递归更新章节层级，后续可以用PARENT_IDS字段进行优化更新
	* @author author
	* @param systemDirectoryDocumentChapters 章节信息
	 */
	public void updateLevel(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters) {
		List<TblSystemDirectoryDocumentChapters> child = systemDirectoryDocumentChaptersMapper.getChildChapters(systemDirectoryDocumentChapters.getId());
		if(!child.isEmpty() && child.size()>0) {
			for (TblSystemDirectoryDocumentChapters tblSystemDirectoryDocumentChapters : child) {
				systemDirectoryDocumentChaptersMapper.updateChaptersLevel(tblSystemDirectoryDocumentChapters.getId(),systemDirectoryDocumentChapters.getChaptersLevel());
				updateLevel(tblSystemDirectoryDocumentChapters);//循环递归 直到没有子集
			}
		}
	}

	/**
	 * 
	* @Title: deleteDocChapters
	* @Description: 删除章节
	* @author author
	* @param systemDirectoryDocumentChapters 存储章节ID
	* @param currentUserAccount 当前用户账号信息（加密，后续要解密）
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
		//逻辑删除
		systemDirectoryDocumentChaptersMapper.updateStatusById(systemDirectoryDocumentChapters);
			
			
	}

	/**
	 * 
	* @Title: removeChaptersFile
	* @Description: 删除章节附件
	* @author author
	* @param directoryDocumentAttachment 章节附件信息
	* @param currentUserAccount 操作人账号
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
		//逻辑删除
		reTblSystemDirectoryDocumentAttachmentMapper.removeChaptersFile(directoryDocumentAttachment);
			
	}

	/**
	 * 
	* @Title: selectChaptersMarkDownContrast
	* @Description: 设置md形式的文档章节对比
	* @author author
	* @param type 1是添加条目数量查看操作，2 是修改条目数量查看操作 3 是删除条目数量查看操作
	* @param systemDirectoryDocumentChapters 章节信息
	* @param request
	* @return Map<String, Object>
	* @throws Exception
	 */
	@Override
	public Map<String, Object> selectChaptersMarkDownContrast(Integer type, TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters, HttpServletRequest request) throws Exception{
        Map<String, Object> result = new HashMap<>();
        // 最新版本
        result.put("latestVersion",selectChaptersMarkDown(systemDirectoryDocumentChapters,request));
        if(type.intValue() == 2){
            // 上次版本
            TblSystemDirectoryDocumentChaptersHistory history = chaptersHistoryMapper.getMaxVersionChaptersHistory(systemDirectoryDocumentChapters.getId());
            if(history != null) {
                String markDowm = "";
                if(StringUtils.isNotBlank(history.getChaptersS3Bucket()) && StringUtils.isNotBlank(history.getChaptersTempS3Key())) {
                    //取MONGO存储KEY(存放临时数据)
                    markDowm = s3Util.getStringByS3(history.getChaptersS3Bucket(),history.getChaptersTempS3Key());
                }else {
                    markDowm = s3Util.getStringByS3(history.getChaptersS3Bucket(),history.getChaptersS3Key());
                }
                //上一版本
                result.put("lastVersion",markDowm);
            }
        }
        return result;
    }

	/**
	 * 
	* @Title: selectChaptersMarkDown
	* @Description: 获取md形式的章节信息
	* @author author
	* @param systemDirectoryDocumentChapters 章节信息
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
	            //取MONGO存储KEY(存放临时数据)
	            markdown = s3Util.getStringByS3(chapters.getChaptersS3Bucket(),chapters.getChaptersTempS3Key());
	            //markdown信息
	            result.put("markdown",markdown);
	            //前端页面控制按钮
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
	* @Description: 上传文档附件
	* @author author
	* @param files 附件
	* @param directoryDocumentAttachment  文档附件信息
	* @param requirementCode 需求编码
	* @param featureCode 开发任务编码
	* @param currentUserAccount 操作人
	* @param fileName 文件名
	* @param request
	* @return Long 上传的文件数
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
	                	//上传S3
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
	* @Description: 上传到s3
	* @author author
	* @param file 文件
	* @param fileNameOld 原文件名
	* @param s3Util
	* @param bucketName s3桶
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
	                    .substring(file.getOriginalFilename().lastIndexOf(".") + 1);// 后缀名

	            if (BrowserUtil.isMSBrowser(request)) {
	                fileNameOld = fileNameOld.substring(fileNameOld.lastIndexOf("\\")+1);
	            }
	            keyName = s3Util.putFileUpload(bucketName, UUID.randomUUID().toString(), inputStream, file.getContentType());

	            attachementInfoDTO.setFileS3Bucket(bucketName);
	            attachementInfoDTO.setFileS3Key(keyName.equals("error") ? "" : keyName);
	            attachementInfoDTO.setFilePath(attachementInfoDTO.getFilePath() + bucketName + File.separatorChar +new Date().getTime()+"-"+ fileNameOld);
	            attachementInfoDTO.setFileNameOld(fileNameOld);
	            attachementInfoDTO.setFileType(fileType);
	            log.info("上传文件成功：" + attachementInfoDTO.getFilePath());
	        }catch (Exception e){
	        	log.error("上传文件失败：" + attachementInfoDTO.getFilePath());
	            throw new Exception(e.getMessage());
	        }
	        return attachementInfoDTO;
	    }

}
