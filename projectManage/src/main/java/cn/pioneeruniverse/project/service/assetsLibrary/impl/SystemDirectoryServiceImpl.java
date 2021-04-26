package cn.pioneeruniverse.project.service.assetsLibrary.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.pioneeruniverse.common.dto.TblAttachementInfoDTO;
import cn.pioneeruniverse.common.entity.AjaxModel;
import cn.pioneeruniverse.common.utils.CollectionUtil;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.S3Util;
import cn.pioneeruniverse.common.utils.UploadFileUtil;
import cn.pioneeruniverse.project.dao.mybatis.ProjectInfoMapper;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.SystemDirectoryDao;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.SystemDirectoryDocumentAttachmentDao;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.SystemDirectoryDocumentDao;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.SystemDirectoryDocumentHistoryDao;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.SystemDirectoryUserAuthorityDao;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.SystemDocumentType;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.TblSystemDirectoryDocumentAttachmentMapper;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.TblSystemDirectoryDocumentChaptersHistoryMapper;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.TblSystemDirectoryDocumentChaptersMapper;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.TblSystemDirectoryDocumentChaptersRequirementFeatureMapper;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.TblSystemDirectoryDocumentChaptersRequirementMapper;
import cn.pioneeruniverse.project.entity.TblProjectInfo;
import cn.pioneeruniverse.project.entity.TblSystemDirectory;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocument;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentAttachment;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChapters;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersHistory;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentHistory;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryUserAuthority;
import cn.pioneeruniverse.project.entity.TblSystemDocumentType;
import cn.pioneeruniverse.project.feignInterface.DevTaskInterface;
import cn.pioneeruniverse.project.service.assetsLibrary.SystemDirectoryService;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 11:57 2019/11/12
 * @Modified By:
 */
@Transactional(readOnly = true)
@Service("systemDirectoryService")
public class SystemDirectoryServiceImpl extends ServiceImpl<SystemDirectoryDao, TblSystemDirectory> implements SystemDirectoryService {



    @Autowired
    private SystemDirectoryDao systemDirectoryDao;

    @Autowired
    private SystemDirectoryDocumentDao systemDirectoryDocumentDao;
    
    @Autowired
    private SystemDirectoryDocumentHistoryDao systemDirectoryDocumentHistoryDao;

    @Autowired
    private SystemDirectoryDocumentAttachmentDao systemDirectoryDocumentAttachmentDao;
    @Autowired
    private TblSystemDirectoryDocumentChaptersHistoryMapper chaptersHistoryMapper;

    /**
     * 系统目录文档附件
     **/
    @Autowired
    private TblSystemDirectoryDocumentAttachmentMapper directoryDocumentAttachmentMapper;

    /**
     * 系统目录文档章节需求关系表
     **/
    @Autowired
    private TblSystemDirectoryDocumentChaptersRequirementMapper chaptersRequirementMapper;

    /**
     * 系统目录文档章节开发任务
     **/
    @Autowired
    private TblSystemDirectoryDocumentChaptersRequirementFeatureMapper chaptersRequirementFeatureMapper;
    @Autowired
    private TblSystemDirectoryDocumentChaptersMapper systemDirectoryDocumentChaptersMapper;
    @Autowired
    private SystemDocumentType systemDocumentType;
    @Autowired
    private DevTaskInterface devTaskInterface;
    @Autowired
    private ProjectInfoMapper projectInfoMapper;
    @Autowired
    private SystemDirectoryUserAuthorityDao systemDirectoryUserAuthorityDao;
    @Autowired
    private S3Util s3Util;

    //s3中的bucket，在properties文件中配置
    @Value("${s3.documentBucket}")
    private String documentBucket;
    
    /**
     * 
    * @Title: getDirectoryTreeForDocumentLibrary
    * @Description: 获取文档目录结构
    * @author author
    * @param projectId
    * @return List<TblSystemDirectory>
     */
    @Override
    @Transactional(readOnly = true)
    public List<TblSystemDirectory> getDirectoryTreeForDocumentLibrary(Long projectId) {
        return systemDirectoryDao.getDirectoryTreeForDocumentLibrary(projectId);
    }

    /**
     * 
    * @Title: getDirectoryTree
    * @Description: 获取选定的系统ID的目录树
    * @author author
    * @param systemIds 系统id
    * @param request
    * @return List<Map<String, Object>>
     */
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getDirectoryTree(String systemIds, HttpServletRequest request) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (StringUtils.isEmpty(systemIds)) {
            String token = CommonUtil.getToken(request);
            List<Map<String, Object>> systems = devTaskInterface.getMyProjectSystems(token);//获取当前用户所属项目组对应的系统
            if (CollectionUtil.isNotEmpty(systems)) {
                systemIds = StringUtils.join(CollectionUtil.collect(systems, o -> ((Map<String, Object>) o).get("id")), ",");
            }
        }
        //获取文档类型，即需求说明书、项目规划方案、概要设计书等
        List<TblSystemDirectoryDocument> documentTypes = systemDirectoryDocumentDao.getDocumentTypesBySystemIds(systemIds);
        if (CollectionUtil.isNotEmpty(documentTypes)) {
            Map<Long, List<TblSystemDirectoryDocument>> documentTypesGroups = documentTypes.stream().collect(Collectors.groupingBy(TblSystemDirectoryDocument::getSystemId));
            for (Map.Entry<Long, List<TblSystemDirectoryDocument>> entry : documentTypesGroups.entrySet()) {
                result.add(new LinkedHashMap<String, Object>() {{
                    put("id", "SYSTEM-" + entry.getKey());//系统
                    put("pId", null);
                    put("name", devTaskInterface.getSystemNameById(entry.getKey()));//系统名
                }});
                for (TblSystemDirectoryDocument tblSystemDirectoryDocument : entry.getValue()) {
                    result.add(new LinkedHashMap<String, Object>() {{
                        put("id", "DOCUMENTTYPE-" + tblSystemDirectoryDocument.getDocumentType());//文档类型
                        put("pId", "SYSTEM-" + entry.getKey());//tree的节点nodeId
                        put("name", tblSystemDirectoryDocument.getDocumentTypeName());//文档类型名
                        put("systemId", entry.getKey());//系统ID
                        put("documentTypeId", tblSystemDirectoryDocument.getDocumentType());//文档类型ID
                    }});
                }
            }
        }
        return result;
    }

    /**
     * 
    * @Title: getFilesUnderDirectory
    * @Description: 获取某系统下的某种文档类型的文档
    * @author author
    * @param systemId 系统ID
    * @param documentType 系统类型
    * @return Map<String, Object>
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getFilesUnderDirectory(Long systemId, Long documentType) {
        Map<String, Object> result = new HashMap<>();
        List<TblSystemDirectoryDocument> documents = systemDirectoryDocumentDao.getDocumentsUnderDocumentTypesDirectory(systemId, documentType);
        if (CollectionUtil.isNotEmpty(documents)) {
            //当前集合中markdown有且仅有一个返回markdown数据
            Map<Boolean, List<TblSystemDirectoryDocument>> predicateMap = documents.stream().collect(Collectors.groupingBy(x -> ObjectUtils.equals(x.getSaveType(), 2)));
            List<TblSystemDirectoryDocument> markDownList = predicateMap.get(true) == null ? new ArrayList<>() : predicateMap.get(true);
            if (CollectionUtil.isNotEmpty(markDownList) && markDownList.size() == 1) {
            	//markdown类型
                result.put("type", "markDown");
                //markdown文档ID
                result.put("markDownDocumentId", markDownList.get(0).getId());
            } else {
            	//附件类型
                result.put("type", "documents");
                //文档
                result.put("documents", documents);
            }
        }
        return result;
    }

    /**
     * 
    * @Title: getAllAttachments
    * @Description: 获取所有附件
    * @author author
    * @param documentId 文档ID
    * @return List<TblSystemDirectoryDocumentAttachment> 文档附件信息
     */
    @Override
    @Transactional(readOnly = true)
    public List<TblSystemDirectoryDocumentAttachment> getAllAttachments(Long documentId) {
        return systemDirectoryDocumentAttachmentDao.getNumOfAttachmentsByDocumentId(documentId, null);
    }

    /**
     * 
    * @Title: addSystemDirectory
    * @Description: 添加系统目录
    * @author author
    * @param request
    * @param tblSystemDirectory 系统目录
    * @return TblSystemDirectory 添加完成的系统目录
     */
    @Override
    @Transactional(readOnly = false)
    public TblSystemDirectory addSystemDirectory(HttpServletRequest request, TblSystemDirectory tblSystemDirectory) {
    	if(tblSystemDirectory.getParentId()==null) {
    		TblProjectInfo tblProjectInfo=projectInfoMapper.selectProjectById(tblSystemDirectory.getProjectId());
    		tblSystemDirectory.setProjectType(tblProjectInfo.getProjectType());
    	}
    	tblSystemDirectory.setCreateType(2);
        tblSystemDirectory.preInsertOrUpdate(request);
        systemDirectoryDao.addSystemDirectory(tblSystemDirectory);
        //TblSystemDirectory data= systemDirectoryDao.selectByDirectory(tblSystemDirectory.getId());
        return tblSystemDirectory;
    }

    /**
     * 
    * @Title: delSystemDirectory
    * @Description: 删除系统目录
    * @author author
    * @param directoryId 系统目录ID
    * @throws Exception
     */
    @Override
    @Transactional(readOnly = false)
    public void delSystemDirectory(Long directoryId) throws Exception {
        TblSystemDirectory tblSystemDirectory = systemDirectoryDao.selectById(directoryId);
        if (ObjectUtils.equals(tblSystemDirectory.getCreateType(), 1)) {
            throw new Exception("该目录为自动创建目录，不得删除！");
        }
        //获取当前目录及其子目录ID集合
        List<Long> directoryIds = systemDirectoryDao.getAllSonDirectoryIds(tblSystemDirectory);
        //判断上述集合中是否存在关联文档
        if (CollectionUtil.isNotEmpty(directoryIds)) {
            Boolean existRelatedDocuments = systemDirectoryDocumentDao.existRelatedDocuments(directoryIds);
            if (existRelatedDocuments) {
                throw new Exception("该目录下存在文档，不得删除！");
            } else {
                systemDirectoryDao.delSystemDirectoriesByIds(directoryIds);
            }
        }
    }

    /**
     * 
    * @Title: updateSystemDirectoryName
    * @Description: 更新系统目录名称
    * @author author
    * @param request
    * @param directoryId 系统目录ID
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
        systemDirectoryDao.updateSystemDirectoryName(newDirectory);
    }

    /**
     * 
    * @Title: getDocumentById
    * @Description: 通过ID获取目录文档
    * @author author
    * @param id 文档ID
    * @return TblSystemDirectoryDocument 文档信息
     */
    @Override
    @Transactional(readOnly = true)
    public TblSystemDirectoryDocument getDocumentById(Long id) {
        return systemDirectoryDocumentDao.selectById(id);
    }

    /**
     * 
    * @Title: getSystemDirectoryDocumentHistory
    * @Description: 获取系统目录的文档历史
    * @author author
    * @param documentId 文档id
    * @return List<TblSystemDirectoryDocumentHistory>文档历史信息列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<TblSystemDirectoryDocumentHistory> getSystemDirectoryDocumentHistory(Long documentId) {
        return systemDirectoryDocumentHistoryDao.getSystemDirectoryHistoryByDocumentId(documentId);
    }

    /**
     * 
    * @Title: upLoadNewDocument
    * @Description: 上传新文档
    * @author author
    * @param files 需要上传的文件
    * @param tblSystemDirectoryDocument 系统目录
    * @param request
    * @return AjaxModel
     */
    @Override
    @Transactional(readOnly = false)
    public AjaxModel upLoadNewDocument(MultipartFile[] files, TblSystemDirectoryDocument tblSystemDirectoryDocument, HttpServletRequest request)  {
        AjaxModel model = new AjaxModel();
        if (files != null && files.length > 0) {
            model.setFlag(false);
            for (MultipartFile file : files) {
                    try {
                        String status = this.upLoadDocument(file, tblSystemDirectoryDocument, request);
                        if (!status.equals("error")){
                            tblSystemDirectoryDocument.preInsertOrUpdate(request);
                            //新增文档数据添加
                            systemDirectoryDocumentDao.insertDirectoryDocument(tblSystemDirectoryDocument);
                            model.setFlag(true);
                        }
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
                        model.setFlag(false);
					}
            }
        }
        return  model;
    }
    
    /**
     * 
    * @Title: upNewMarkDocument
    * @Description: 上传markdown文档
    * @author author
    * @param tblSystemDirectoryDocument
    * @param request
    * @return
    * @throws
     */
    @Override
    @Transactional(readOnly = false)
	public Long upNewMarkDocument(TblSystemDirectoryDocument tblSystemDirectoryDocument, HttpServletRequest request) {
    			
    		/*File file=new File("../projectManage/src/main/resources/markDown.txt");
			InputStream inputStream =  new FileInputStream(file);
			 String fileNameOld =tblSystemDirectoryDocument.getDocumentName()+".txt";
	         fileNameOld = fileNameOld.substring(fileNameOld.lastIndexOf("\\") + 1);
	         tblSystemDirectoryDocument.setDocumentName(fileNameOld);
	         
	         tblSystemDirectoryDocument.setDocumentS3Bucket(documentBucket);
	         tblSystemDirectoryDocument.setDocumentS3Key(s3Util.putObject(documentBucket, fileNameOld, inputStream));*/
    	     //前置操作，比如设置更新人，更新时间，创建人，创建时间等公共字段
	         tblSystemDirectoryDocument.preInsertOrUpdate(request);
             //新写添加
             tblSystemDirectoryDocument.setDocumentVersion(0);
             //新增文档
             systemDirectoryDocumentDao.insertDirectoryDocument(tblSystemDirectoryDocument);
             //新增章节
             TblSystemDirectoryDocumentChapters record=new TblSystemDirectoryDocumentChapters();
             record.setSystemDirectoryDocumentId(tblSystemDirectoryDocument.getId());
             record.setDocumentVersion(0);
             record.setChaptersName("新建章节");
             record.setChaptersLevel(1);
             record.setChaptersOrder(1);
             record.setChaptersVersion(0);
             record.setCheckoutStatus(1);
             record.setCheckoutUserId(CommonUtil.getCurrentUserId(request));
             record.setStatus(1);
             Date date = new Date();       
             Timestamp nousedate = new Timestamp(date.getTime());
             record.setCreateDate(nousedate);
             record.setCreateBy(CommonUtil.getCurrentUserId(request));
             systemDirectoryDocumentChaptersMapper.insert(record);
		
        
		return tblSystemDirectoryDocument.getId();
	}
    
    /**
     * 
    * @Title: coverUploadOldDocument
    * @Description: 覆盖上传文档
    * @author author
    * @param files 上传的附件
    * @param tblSystemDirectoryDocument
    * @param request
    * @throws IOException
     */
    @Override
    @Transactional(readOnly = false)
    public void coverUploadOldDocument(MultipartFile[] files, TblSystemDirectoryDocument
            tblSystemDirectoryDocument, HttpServletRequest request) throws IOException {
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String status = this.upLoadDocument(file, tblSystemDirectoryDocument, request);
                    if (!status.equals("error")){
                        tblSystemDirectoryDocument.preInsertOrUpdate(request);
                        TblSystemDirectoryDocument tblSystemDirectoryDocumentOld=systemDirectoryDocumentDao.getDirectoryDocumentsById(tblSystemDirectoryDocument.getId());
                        //插入历史
                        systemDirectoryDocumentHistoryDao.insertDirectoryDocumentHistory(tblSystemDirectoryDocumentOld);
                        tblSystemDirectoryDocument.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
                        tblSystemDirectoryDocument.setDocumentVersion(tblSystemDirectoryDocumentOld.getDocumentVersion()+1);
                        //更新版本
                        systemDirectoryDocumentDao.updateVersionForCoverUploadDocument(tblSystemDirectoryDocument);
                    }
                }
            }
        }
    }

    /**
     * 
    * @Title: downLoadDocument
    * @Description: 从S3中下载文档
    * @author author
    * @param tblSystemDirectoryDocument
    * @param response
    * @throws
     */
    @Override
    public void downLoadDocument(TblSystemDirectoryDocument tblSystemDirectoryDocument, HttpServletResponse
            response) {
        s3Util.downObject(tblSystemDirectoryDocument.getDocumentS3Bucket(),
                tblSystemDirectoryDocument.getDocumentS3Key(),
                tblSystemDirectoryDocument.getDocumentName(), response);
    }

    /**
     * 
    * @Title: upLoadDocument
    * @Description: 上传文档到S3并返回key:重新生成的文件名
    * @author author
    * @param file
    * @param tblSystemDirectoryDocument
    * @param request
    * @return
    * @throws IOException
     */
    @Override
    @Transactional(readOnly = false)
    public String upLoadDocument(MultipartFile file, TblSystemDirectoryDocument tblSystemDirectoryDocument, HttpServletRequest request) throws IOException {
        InputStream inputStream = file.getInputStream();
        String fileNameOld = file.getOriginalFilename();
        fileNameOld = fileNameOld.substring(fileNameOld.lastIndexOf("\\") + 1);
        tblSystemDirectoryDocument.setDocumentName(fileNameOld);
        tblSystemDirectoryDocument.setDocumentS3Bucket(documentBucket);
        String S3Key = s3Util.uploadFile(documentBucket, fileNameOld, inputStream);
        tblSystemDirectoryDocument.setDocumentS3Key(S3Key.equals("error") ? null : S3Key);
        return S3Key;
    }
    
    /**
     * 
    * @Title: getDocumentTypes
    * @Description: 获取该目录下的文档类型，如果该层没有定义文档类型，则一层层向上找父节点的文档类型
    * @author author
    * @param directoryId
    * @return map key  valueCode:文档类型ID
    *                  valueName:文档名称
    * @throws
     */
	@Override
	public List<Map<String, Object>>  getDocumentTypes(Long directoryId) {
		List<Map<String, Object>> list=new ArrayList<>();
		TblSystemDirectory tblSystemDirectory=systemDirectoryDao.getDocumentType(directoryId);
		
		if(tblSystemDirectory!=null) {
			
		
		if(("").equals(tblSystemDirectory.getDocumentTypes())||tblSystemDirectory.getDocumentTypes()==null) {
			List<Map<String, Object>> list2=getDocumentTypes(tblSystemDirectory.getParentId());
			if(list2.size()>0) {return list2;}
		}
		/* Object object = redisUtils.get("DOCUMENT_TYPE1");
		 Map<String, Object> mapsource=new HashMap<String, Object>();
		 if (object != null &&!"".equals( object)) {//redis有直接从redis中取
			 mapsource = JSON.parseObject(object.toString());
		 }*/
		 String documentType=tblSystemDirectory.getDocumentTypes();
		 String[] documentTypeArr = null;
		 if(documentType!=""&&documentType!=null) {
			 documentTypeArr = documentType.split(",");
		 }
		 if(documentTypeArr!=null) {
			 for(int i=0;i<documentTypeArr.length;i++) {
				 Map<String, Object> map=new HashMap<String, Object>();
				 TblSystemDocumentType tblSystemDocumentType=systemDocumentType.selectTypeById(Long.parseLong(documentTypeArr[i]));
				 map.put("valueCode", tblSystemDocumentType.getId());
				 map.put("valueName", tblSystemDocumentType.getDocumentType());
				 list.add(map);
				 
			 }
		 }
		 
		
		}
		return list;
	}
	private boolean IsEmpty(TblSystemDirectory tblSystemDirectory) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 签出
	 */
	@Override
	public void signedOutDocumentFile(Long directoryDocumentId) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 
	* @Title: getDocumentFile
	* @Description: 获取系统目录文档
	* @author author
	* @param directoryId 目录Id
	* @return List<TblSystemDirectoryDocument>
	* @throws
	 */
	@Override
	public List<TblSystemDirectoryDocument> getDocumentFile(Long directoryId) {
		 List<TblSystemDirectoryDocument> list=systemDirectoryDocumentDao.getDirectoryDocumentsByDirectoryId(directoryId);
		return list;
	}
	
	/**
	 * 
	* @Title: moveDocChapters
	* @Description: 移动文件章节
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
	* @Description: 调节章节次序
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
	* @Title: delectDocumentFile
	* @Description: 删除目录
	* @author author
	* @param directoryDocumentId 文档ID 
	* @throws
	 */
	@Override
	@Transactional(readOnly = false)
	public void delectDocumentFile(Long directoryDocumentId) {
		systemDirectoryDocumentDao.removeDocumentFile(directoryDocumentId);
	}

	/**
	 * 
	* @Title: getSystemName
	* @Description: 获取系统名称
	* @author author
	* @param directoryId 目录ID
	* @return List<Map<String, Object>>  key:系统ID，value:系统名
	 */
	@Override
	public   List<Map<String, Object>> getSystemName(Long directoryId) {
		TblSystemDirectory tblSystemDirectory=systemDirectoryDao.getDocumentType(directoryId);
		List<Map<String, Object>>  systemName=systemDirectoryDao.selectSystemByProject(tblSystemDirectory.getProjectId());
		return systemName;
	}
	
	/**
	 * 
	* @Title: getAllDocChapters
	* @Description: 查询目录ID下的所有章节
	* @author author
	* @param systemDirectoryDocumentId
	* @return List<TblSystemDirectoryDocumentChapters>章节信息列表
	 */
	@Override
	public List<TblSystemDirectoryDocumentChapters> getAllDocChapters(Long systemDirectoryDocumentId) {
		
		return systemDirectoryDocumentChaptersMapper.getAllBySystemDirectoryDocumentId(systemDirectoryDocumentId);
	}

	/**
	 * 
	* @Title: getMarkById
	* @Description: 通过ID获取系统目录文档
	* @author author
	* @param id 文档ID
	* @return TblSystemDirectoryDocument
	 */
	@Override
	public TblSystemDirectoryDocument getMarkById(Long id) {
		TblSystemDirectoryDocument tblSystemDirectoryDocument=systemDirectoryDocumentDao.selectById(id);
		return tblSystemDirectoryDocument;
	}
	
	/**
	 * 
	* @Title: addOrUpdateDocChapters
	* @Description: 新增或编辑文档章节
	* @author author
	* @param systemDirectoryDocumentChapters
	* @param request
	* @return TblSystemDirectoryDocumentChapters
	 */
	@Override
	@Transactional(readOnly = false)
	public TblSystemDirectoryDocumentChapters addOrUpdateDocChapters(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters,HttpServletRequest request) {
		if(systemDirectoryDocumentChapters.getId()!=null) {//编辑
			systemDirectoryDocumentChapters.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
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
			CommonUtil.setBaseValue(systemDirectoryDocumentChapters, request);
			systemDirectoryDocumentChaptersMapper.insert(systemDirectoryDocumentChapters);
			TblSystemDirectoryDocumentChapters sysDocChapters =  systemDirectoryDocumentChaptersMapper.selectByPrimaryKey(systemDirectoryDocumentChapters.getId());
			return sysDocChapters;
		}
		
	}

	/**
	 * 
	* @Title: deleteDocChapters
	* @Description: 删除文档章节
	* @author author
	* @param systemDirectoryDocumentChapters
	* @param request
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteDocChapters(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters, HttpServletRequest request) {
		systemDirectoryDocumentChapters.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		systemDirectoryDocumentChapters.setLastUpdateDate(new Timestamp(new Date().getTime()));
		systemDirectoryDocumentChaptersMapper.updateStatusById(systemDirectoryDocumentChapters);
		
		
	}


    /**
     *@author liushan
     *@Description 章节上传附件
     *@Date 2020/1/15
     *@Param [files, directoryDocumentAttachment, request]
     *@return Map<String, Object>
     **/
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Map<String, Object> uploadChaptersFiles(MultipartFile[] files, TblSystemDirectoryDocumentAttachment directoryDocumentAttachment, HttpServletRequest request) throws Exception{
        Integer number = 0;
        Map<String, Object> result = new HashMap<>();
        if (files.length > 0 && files != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    TblAttachementInfoDTO attachementInfoDTO = UploadFileUtil.updateMultipartFile(file, s3Util, documentBucket, request);
                    directoryDocumentAttachment.setAttachmentNameOld(attachementInfoDTO.getFileNameOld());
                    directoryDocumentAttachment.setAttachmentS3Bucket(attachementInfoDTO.getFileS3Bucket());
                    directoryDocumentAttachment.setAttachmentS3Key(attachementInfoDTO.getFileS3Key());
                    directoryDocumentAttachment.setAttachmentUrl(attachementInfoDTO.getFilePath());
                    directoryDocumentAttachment.setCreateBy(CommonUtil.getCurrentUserId(request));
                    directoryDocumentAttachment.setCreateDate(new Timestamp(new Date().getTime()));
                    //附件原名
                    result.put("attachmentNameOld", attachementInfoDTO.getFileNameOld());	
                    //s3桶
                    result.put("attachmentS3Bucket", attachementInfoDTO.getFileS3Bucket());
                    //s3key
                    result.put("attachmentS3Key", attachementInfoDTO.getFileS3Key());
                    //上传到S3成功后，再插入数据库库
                    if (!attachementInfoDTO.getFileS3Key().equals("error")) {
                        number = directoryDocumentAttachmentMapper.insertSelective(directoryDocumentAttachment);
                    }
                }
            }
        }
            return result;
    }


//	/**
//	*@author liushan
//	*@Description 章节上传附件
//	*@Date 2020/1/15
//	*@Param [files, directoryDocumentAttachment, request]
//	*@return void
//	**/
//    @Override
//    @Transactional(readOnly = false, rollbackFor = Exception.class)
//    public void uploadChaptersFiles(MultipartFile[] files, TblSystemDirectoryDocumentAttachment directoryDocumentAttachment, HttpServletRequest request) throws Exception{
//        if (files != null && files.length > 0) {
//            for (MultipartFile file : files) {
//                if (!Objects.isNull(file) || !file.isEmpty()) {
//                    TblAttachementInfoDTO attachementInfoDTO = UploadFileUtil.updateFile(file,s3Util,documentBucket);
//                    directoryDocumentAttachment.setAttachmentNameOld(attachementInfoDTO.getFileNameOld()+attachementInfoDTO.getFileType());
//                    directoryDocumentAttachment.setAttachmentS3Bucket(attachementInfoDTO.getFileS3Bucket());
//                    directoryDocumentAttachment.setAttachmentS3Key(attachementInfoDTO.getFileS3Key());
//                    directoryDocumentAttachment.setAttachmentUrl(attachementInfoDTO.getFilePath());
//                    directoryDocumentAttachment.setId(null);
//                    directoryDocumentAttachmentMapper.insertSelective(directoryDocumentAttachment);
//                }
//            }
//        }
//    }

    /**
    *@author liushan
    *@Description 章节移除附件
    *@Date 2020/1/15
    *@Param [directoryDocumentAttachment, request]
    *@return void
    **/
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void removeChaptersFile(TblSystemDirectoryDocumentAttachment directoryDocumentAttachment, HttpServletRequest request) {
        directoryDocumentAttachment.setStatus(2);
        directoryDocumentAttachment.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
        directoryDocumentAttachment.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
        directoryDocumentAttachmentMapper.removeChaptersFile(directoryDocumentAttachment);
    }

    /**
	*@author liushan
	*@Description 查看本章节mark基本信息
	*@Date 2020/1/14
	*@Param [systemDirectoryDocumentChapters, request]
	*@return Map<String, Object>
	**/
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Map<String, Object> selectChaptersMarkDown(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters, HttpServletRequest request) throws Exception{
        Map<String, Object> result = new HashMap<>();
        TblSystemDirectoryDocumentChapters chapters = systemDirectoryDocumentChaptersMapper.selectChaptersWithRelatedData(systemDirectoryDocumentChapters.getId());
        Long userId = CommonUtil.getCurrentUserId(request);
        String markdown = null;
        Integer buttonState = 2;
        if(userId.equals(chapters.getCheckoutUserId()) && chapters.getCheckoutStatus() == 1) {
            //取MONGO存储KEY(存放临时数据)
            markdown = s3Util.getStringByS3(chapters.getChaptersS3Bucket(),chapters.getChaptersTempS3Key());
            //markdown信息
            result.put("markdown",markdown);
            //检出按钮状态
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


//        if (systemDirectory != null && userId.equals(systemDirectory.getCheckoutUserId()) && StringUtils.isNotBlank(systemDirectory.getDocumentS3Bucket()) && StringUtils.isNotBlank(systemDirectory.getChaptersTempS3Key())){
//            //取MONGO存储KEY(存放临时数据)
//            content = s3Util.getStringByS3(systemDirectory.getDocumentS3Bucket(), systemDirectory.getChaptersTempS3Key());
//            systemDirectory.setDirectoryDocumentContent(content);
//            //改变签出状态（1:是，2:否）
//            aSystemDirectoryDocumentYiRanDao.updateDirectoryDocumentSignOffStatusById(id, userId);
//            mapValues.put("systemDirectory",systemDirectory);
//            mapValues.put("code",1);
//            mapValues.put("content",content);
//            return mapValues;
//        }else if (systemDirectory != null && !userId.equals(systemDirectory.getCheckoutUserId()) && StringUtils.isNotBlank(systemDirectory.getDocumentS3Bucket()) && StringUtils.isNotBlank(systemDirectory.getChaptersS3Key())){
//            content = s3Util.getStringByS3(systemDirectory.getDocumentS3Bucket(), systemDirectory.getChaptersS3Key());
//            systemDirectory.setDirectoryDocumentContent(content);
//            //改变签出状态（1:是，2:否）
//            aSystemDirectoryDocumentYiRanDao.updateDirectoryDocumentSignOffStatusById(id, userId);
//            mapValues.put("systemDirectory",systemDirectory);
//            mapValues.put("code",2);
//            mapValues.put("content",content);
//            return mapValues;
//        }



//    /**
//     *@author liushan
//     *@Description 查看本章节mark基本信息
//     *@Date 2020/1/14
//     *@Param [systemDirectoryDocumentChapters, request]
//     *@return cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChapters
//     **/
//    @Override
//    public Map<String, Object> selectChaptersMarkDown(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters, HttpServletRequest request) throws Exception{
//        Map<String, Object> result = new HashMap<>();
//        TblSystemDirectoryDocumentChapters chapters = systemDirectoryDocumentChaptersMapper.selectChaptersWithRelatedData(systemDirectoryDocumentChapters.getId());
//        String markdown = null;
//        result.put("chapters",chapters);
//        if( StringUtils.isNotBlank(chapters.getChaptersS3Bucket()) && StringUtils.isNotBlank(chapters.getChaptersS3Key())) {
//           markdown = s3Util.getStringByS3(chapters.getChaptersS3Bucket(),chapters.getChaptersMongoKey());
//        }
//        result.put("markdown",markdown);
//        return  result;
//    }



    /**
    *@author liushan
    *@Description 查询章节所有附件
    *@Date 2020/1/15
    *@Param [systemDirectoryDocumentChapters, request]
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @Override
    public List<TblSystemDirectoryDocumentAttachment> selectChaptersFiles(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters, HttpServletRequest request) {
        return directoryDocumentAttachmentMapper.selectAttachmentByChaptersId(systemDirectoryDocumentChapters.getId(),null);
    }

    /**
    *@author liushan
    *@Description 查询章节的关联所有需求和开发任务
    *@Date 2020/1/15
    *@Param [systemDirectoryDocumentChapters, request]
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @Override
    public Map<String, Object> selectChaptersRelatedData(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        result.put("requirement",chaptersRequirementMapper.selectRequirementByChaptersId(systemDirectoryDocumentChapters.getId(),null));
        result.put("Feature",chaptersRequirementFeatureMapper.selectRequirementFeatureByChaptersId(systemDirectoryDocumentChapters.getId(),null));
        return result;
    }

	@Override
	public List<TblSystemDocumentType> getSomeDocumentType() {
		
		return systemDocumentType.getSomeDocumentType();
	}

    /**
     *@author liushan
     *@Description 查看章节markDown最新版本和上次版本对比
     * type: 1是添加条目数量查看操作，2 是修改条目数量查看操作 3 是删除条目数量查看操作
     *@Date 2020/2/19
     *@Param
     *@return
     **/
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
                result.put("lastVersion",markDowm);
            }
        }
        return result;
    }
    
    //查询当前登录用户对该目录下文档的权限(暂不考虑角色)
	@Override
	public Map<String, Object> getCurrentAuthority(Long id, Long projectId,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		//查询当前登录用户有没有权限
		Long uid = CommonUtil.getCurrentUserId(request);
		List<TblSystemDirectoryUserAuthority> userAuthList = systemDirectoryUserAuthorityDao.selectBySystemDirectoryIdAndUid(id,uid);
		if (!userAuthList.isEmpty()) {
			if(userAuthList.size() == 1) {
				map.put("readAuth", userAuthList.get(0).getReadAuth());
				map.put("writeAuth", userAuthList.get(0).getWriteAuth());
			}else {
				map.put("message", "数据错误!");
			}
		}else {//权限表没数据  无权限 
			map.put("readAuth", 2);//1 是  2 否
			map.put("writeAuth", 2);
		}
				
	    //当前登录用户在项目中的角色
		
		//查询前登录用户在项目中的角色 有没有权限
		
		return map;
	}
}
