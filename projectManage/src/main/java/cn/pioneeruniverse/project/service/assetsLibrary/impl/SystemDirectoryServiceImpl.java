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
     * ????????????????????????
     **/
    @Autowired
    private TblSystemDirectoryDocumentAttachmentMapper directoryDocumentAttachmentMapper;

    /**
     * ???????????????????????????????????????
     **/
    @Autowired
    private TblSystemDirectoryDocumentChaptersRequirementMapper chaptersRequirementMapper;

    /**
     * ????????????????????????????????????
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

    //s3??????bucket??????properties???????????????
    @Value("${s3.documentBucket}")
    private String documentBucket;
    
    /**
     * 
    * @Title: getDirectoryTreeForDocumentLibrary
    * @Description: ????????????????????????
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
    * @Description: ?????????????????????ID????????????
    * @author author
    * @param systemIds ??????id
    * @param request
    * @return List<Map<String, Object>>
     */
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getDirectoryTree(String systemIds, HttpServletRequest request) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (StringUtils.isEmpty(systemIds)) {
            String token = CommonUtil.getToken(request);
            List<Map<String, Object>> systems = devTaskInterface.getMyProjectSystems(token);//????????????????????????????????????????????????
            if (CollectionUtil.isNotEmpty(systems)) {
                systemIds = StringUtils.join(CollectionUtil.collect(systems, o -> ((Map<String, Object>) o).get("id")), ",");
            }
        }
        //?????????????????????????????????????????????????????????????????????????????????
        List<TblSystemDirectoryDocument> documentTypes = systemDirectoryDocumentDao.getDocumentTypesBySystemIds(systemIds);
        if (CollectionUtil.isNotEmpty(documentTypes)) {
            Map<Long, List<TblSystemDirectoryDocument>> documentTypesGroups = documentTypes.stream().collect(Collectors.groupingBy(TblSystemDirectoryDocument::getSystemId));
            for (Map.Entry<Long, List<TblSystemDirectoryDocument>> entry : documentTypesGroups.entrySet()) {
                result.add(new LinkedHashMap<String, Object>() {{
                    put("id", "SYSTEM-" + entry.getKey());//??????
                    put("pId", null);
                    put("name", devTaskInterface.getSystemNameById(entry.getKey()));//?????????
                }});
                for (TblSystemDirectoryDocument tblSystemDirectoryDocument : entry.getValue()) {
                    result.add(new LinkedHashMap<String, Object>() {{
                        put("id", "DOCUMENTTYPE-" + tblSystemDirectoryDocument.getDocumentType());//????????????
                        put("pId", "SYSTEM-" + entry.getKey());//tree?????????nodeId
                        put("name", tblSystemDirectoryDocument.getDocumentTypeName());//???????????????
                        put("systemId", entry.getKey());//??????ID
                        put("documentTypeId", tblSystemDirectoryDocument.getDocumentType());//????????????ID
                    }});
                }
            }
        }
        return result;
    }

    /**
     * 
    * @Title: getFilesUnderDirectory
    * @Description: ????????????????????????????????????????????????
    * @author author
    * @param systemId ??????ID
    * @param documentType ????????????
    * @return Map<String, Object>
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getFilesUnderDirectory(Long systemId, Long documentType) {
        Map<String, Object> result = new HashMap<>();
        List<TblSystemDirectoryDocument> documents = systemDirectoryDocumentDao.getDocumentsUnderDocumentTypesDirectory(systemId, documentType);
        if (CollectionUtil.isNotEmpty(documents)) {
            //???????????????markdown????????????????????????markdown??????
            Map<Boolean, List<TblSystemDirectoryDocument>> predicateMap = documents.stream().collect(Collectors.groupingBy(x -> ObjectUtils.equals(x.getSaveType(), 2)));
            List<TblSystemDirectoryDocument> markDownList = predicateMap.get(true) == null ? new ArrayList<>() : predicateMap.get(true);
            if (CollectionUtil.isNotEmpty(markDownList) && markDownList.size() == 1) {
            	//markdown??????
                result.put("type", "markDown");
                //markdown??????ID
                result.put("markDownDocumentId", markDownList.get(0).getId());
            } else {
            	//????????????
                result.put("type", "documents");
                //??????
                result.put("documents", documents);
            }
        }
        return result;
    }

    /**
     * 
    * @Title: getAllAttachments
    * @Description: ??????????????????
    * @author author
    * @param documentId ??????ID
    * @return List<TblSystemDirectoryDocumentAttachment> ??????????????????
     */
    @Override
    @Transactional(readOnly = true)
    public List<TblSystemDirectoryDocumentAttachment> getAllAttachments(Long documentId) {
        return systemDirectoryDocumentAttachmentDao.getNumOfAttachmentsByDocumentId(documentId, null);
    }

    /**
     * 
    * @Title: addSystemDirectory
    * @Description: ??????????????????
    * @author author
    * @param request
    * @param tblSystemDirectory ????????????
    * @return TblSystemDirectory ???????????????????????????
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
    * @Description: ??????????????????
    * @author author
    * @param directoryId ????????????ID
    * @throws Exception
     */
    @Override
    @Transactional(readOnly = false)
    public void delSystemDirectory(Long directoryId) throws Exception {
        TblSystemDirectory tblSystemDirectory = systemDirectoryDao.selectById(directoryId);
        if (ObjectUtils.equals(tblSystemDirectory.getCreateType(), 1)) {
            throw new Exception("????????????????????????????????????????????????");
        }
        //?????????????????????????????????ID??????
        List<Long> directoryIds = systemDirectoryDao.getAllSonDirectoryIds(tblSystemDirectory);
        //?????????????????????????????????????????????
        if (CollectionUtil.isNotEmpty(directoryIds)) {
            Boolean existRelatedDocuments = systemDirectoryDocumentDao.existRelatedDocuments(directoryIds);
            if (existRelatedDocuments) {
                throw new Exception("??????????????????????????????????????????");
            } else {
                systemDirectoryDao.delSystemDirectoriesByIds(directoryIds);
            }
        }
    }

    /**
     * 
    * @Title: updateSystemDirectoryName
    * @Description: ????????????????????????
    * @author author
    * @param request
    * @param directoryId ????????????ID
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
        systemDirectoryDao.updateSystemDirectoryName(newDirectory);
    }

    /**
     * 
    * @Title: getDocumentById
    * @Description: ??????ID??????????????????
    * @author author
    * @param id ??????ID
    * @return TblSystemDirectoryDocument ????????????
     */
    @Override
    @Transactional(readOnly = true)
    public TblSystemDirectoryDocument getDocumentById(Long id) {
        return systemDirectoryDocumentDao.selectById(id);
    }

    /**
     * 
    * @Title: getSystemDirectoryDocumentHistory
    * @Description: ?????????????????????????????????
    * @author author
    * @param documentId ??????id
    * @return List<TblSystemDirectoryDocumentHistory>????????????????????????
     */
    @Override
    @Transactional(readOnly = true)
    public List<TblSystemDirectoryDocumentHistory> getSystemDirectoryDocumentHistory(Long documentId) {
        return systemDirectoryDocumentHistoryDao.getSystemDirectoryHistoryByDocumentId(documentId);
    }

    /**
     * 
    * @Title: upLoadNewDocument
    * @Description: ???????????????
    * @author author
    * @param files ?????????????????????
    * @param tblSystemDirectoryDocument ????????????
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
                            //????????????????????????
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
    * @Description: ??????markdown??????
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
    	     //?????????????????????????????????????????????????????????????????????????????????????????????
	         tblSystemDirectoryDocument.preInsertOrUpdate(request);
             //????????????
             tblSystemDirectoryDocument.setDocumentVersion(0);
             //????????????
             systemDirectoryDocumentDao.insertDirectoryDocument(tblSystemDirectoryDocument);
             //????????????
             TblSystemDirectoryDocumentChapters record=new TblSystemDirectoryDocumentChapters();
             record.setSystemDirectoryDocumentId(tblSystemDirectoryDocument.getId());
             record.setDocumentVersion(0);
             record.setChaptersName("????????????");
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
    * @Description: ??????????????????
    * @author author
    * @param files ???????????????
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
                        //????????????
                        systemDirectoryDocumentHistoryDao.insertDirectoryDocumentHistory(tblSystemDirectoryDocumentOld);
                        tblSystemDirectoryDocument.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
                        tblSystemDirectoryDocument.setDocumentVersion(tblSystemDirectoryDocumentOld.getDocumentVersion()+1);
                        //????????????
                        systemDirectoryDocumentDao.updateVersionForCoverUploadDocument(tblSystemDirectoryDocument);
                    }
                }
            }
        }
    }

    /**
     * 
    * @Title: downLoadDocument
    * @Description: ???S3???????????????
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
    * @Description: ???????????????S3?????????key:????????????????????????
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
    * @Description: ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
    * @author author
    * @param directoryId
    * @return map key  valueCode:????????????ID
    *                  valueName:????????????
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
		 if (object != null &&!"".equals( object)) {//redis????????????redis??????
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
	 * ??????
	 */
	@Override
	public void signedOutDocumentFile(Long directoryDocumentId) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 
	* @Title: getDocumentFile
	* @Description: ????????????????????????
	* @author author
	* @param directoryId ??????Id
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
	* @Description: ??????????????????
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
	* @Title: delectDocumentFile
	* @Description: ????????????
	* @author author
	* @param directoryDocumentId ??????ID 
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
	* @Description: ??????????????????
	* @author author
	* @param directoryId ??????ID
	* @return List<Map<String, Object>>  key:??????ID???value:?????????
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
	* @Description: ????????????ID??????????????????
	* @author author
	* @param systemDirectoryDocumentId
	* @return List<TblSystemDirectoryDocumentChapters>??????????????????
	 */
	@Override
	public List<TblSystemDirectoryDocumentChapters> getAllDocChapters(Long systemDirectoryDocumentId) {
		
		return systemDirectoryDocumentChaptersMapper.getAllBySystemDirectoryDocumentId(systemDirectoryDocumentId);
	}

	/**
	 * 
	* @Title: getMarkById
	* @Description: ??????ID????????????????????????
	* @author author
	* @param id ??????ID
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
	* @Description: ???????????????????????????
	* @author author
	* @param systemDirectoryDocumentChapters
	* @param request
	* @return TblSystemDirectoryDocumentChapters
	 */
	@Override
	@Transactional(readOnly = false)
	public TblSystemDirectoryDocumentChapters addOrUpdateDocChapters(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters,HttpServletRequest request) {
		if(systemDirectoryDocumentChapters.getId()!=null) {//??????
			systemDirectoryDocumentChapters.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
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
			CommonUtil.setBaseValue(systemDirectoryDocumentChapters, request);
			systemDirectoryDocumentChaptersMapper.insert(systemDirectoryDocumentChapters);
			TblSystemDirectoryDocumentChapters sysDocChapters =  systemDirectoryDocumentChaptersMapper.selectByPrimaryKey(systemDirectoryDocumentChapters.getId());
			return sysDocChapters;
		}
		
	}

	/**
	 * 
	* @Title: deleteDocChapters
	* @Description: ??????????????????
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
     *@Description ??????????????????
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
                    //????????????
                    result.put("attachmentNameOld", attachementInfoDTO.getFileNameOld());	
                    //s3???
                    result.put("attachmentS3Bucket", attachementInfoDTO.getFileS3Bucket());
                    //s3key
                    result.put("attachmentS3Key", attachementInfoDTO.getFileS3Key());
                    //?????????S3?????????????????????????????????
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
//	*@Description ??????????????????
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
    *@Description ??????????????????
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
	*@Description ???????????????mark????????????
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
            //???MONGO??????KEY(??????????????????)
            markdown = s3Util.getStringByS3(chapters.getChaptersS3Bucket(),chapters.getChaptersTempS3Key());
            //markdown??????
            result.put("markdown",markdown);
            //??????????????????
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


//        if (systemDirectory != null && userId.equals(systemDirectory.getCheckoutUserId()) && StringUtils.isNotBlank(systemDirectory.getDocumentS3Bucket()) && StringUtils.isNotBlank(systemDirectory.getChaptersTempS3Key())){
//            //???MONGO??????KEY(??????????????????)
//            content = s3Util.getStringByS3(systemDirectory.getDocumentS3Bucket(), systemDirectory.getChaptersTempS3Key());
//            systemDirectory.setDirectoryDocumentContent(content);
//            //?????????????????????1:??????2:??????
//            aSystemDirectoryDocumentYiRanDao.updateDirectoryDocumentSignOffStatusById(id, userId);
//            mapValues.put("systemDirectory",systemDirectory);
//            mapValues.put("code",1);
//            mapValues.put("content",content);
//            return mapValues;
//        }else if (systemDirectory != null && !userId.equals(systemDirectory.getCheckoutUserId()) && StringUtils.isNotBlank(systemDirectory.getDocumentS3Bucket()) && StringUtils.isNotBlank(systemDirectory.getChaptersS3Key())){
//            content = s3Util.getStringByS3(systemDirectory.getDocumentS3Bucket(), systemDirectory.getChaptersS3Key());
//            systemDirectory.setDirectoryDocumentContent(content);
//            //?????????????????????1:??????2:??????
//            aSystemDirectoryDocumentYiRanDao.updateDirectoryDocumentSignOffStatusById(id, userId);
//            mapValues.put("systemDirectory",systemDirectory);
//            mapValues.put("code",2);
//            mapValues.put("content",content);
//            return mapValues;
//        }



//    /**
//     *@author liushan
//     *@Description ???????????????mark????????????
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
    *@Description ????????????????????????
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
    *@Description ????????????????????????????????????????????????
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
     *@Description ????????????markDown?????????????????????????????????
     * type: 1????????????????????????????????????2 ????????????????????????????????? 3 ?????????????????????????????????
     *@Date 2020/2/19
     *@Param
     *@return
     **/
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
                result.put("lastVersion",markDowm);
            }
        }
        return result;
    }
    
    //??????????????????????????????????????????????????????(??????????????????)
	@Override
	public Map<String, Object> getCurrentAuthority(Long id, Long projectId,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		//???????????????????????????????????????
		Long uid = CommonUtil.getCurrentUserId(request);
		List<TblSystemDirectoryUserAuthority> userAuthList = systemDirectoryUserAuthorityDao.selectBySystemDirectoryIdAndUid(id,uid);
		if (!userAuthList.isEmpty()) {
			if(userAuthList.size() == 1) {
				map.put("readAuth", userAuthList.get(0).getReadAuth());
				map.put("writeAuth", userAuthList.get(0).getWriteAuth());
			}else {
				map.put("message", "????????????!");
			}
		}else {//??????????????????  ????????? 
			map.put("readAuth", 2);//1 ???  2 ???
			map.put("writeAuth", 2);
		}
				
	    //???????????????????????????????????????
		
		//?????????????????????????????????????????? ???????????????
		
		return map;
	}
}
