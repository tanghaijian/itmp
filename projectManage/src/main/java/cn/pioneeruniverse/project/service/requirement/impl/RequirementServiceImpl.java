package cn.pioneeruniverse.project.service.requirement.impl;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;

import cn.pioneeruniverse.common.annotion.DataSource;
import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.common.utils.S3Util;
import cn.pioneeruniverse.common.utils.SpringContextHolder;
import cn.pioneeruniverse.project.common.BrowserUtil;
import cn.pioneeruniverse.project.common.SynRequirementUtil;
import cn.pioneeruniverse.project.dao.mybatis.RequirementAttachementMapper;
import cn.pioneeruniverse.project.dao.mybatis.RequirementMapper;
import cn.pioneeruniverse.project.dao.mybatis.RequirmentSystemMapper;
import cn.pioneeruniverse.project.dao.mybatis.TblRequirementAttentionMapper;
import cn.pioneeruniverse.project.entity.ExtendedField;
import cn.pioneeruniverse.project.entity.TblRequirementAttachement;
import cn.pioneeruniverse.project.entity.TblRequirementAttention;
import cn.pioneeruniverse.project.entity.TblRequirementInfo;
import cn.pioneeruniverse.project.entity.TblRequirementSystem;
import cn.pioneeruniverse.project.feignInterface.DevTaskInterface;
import cn.pioneeruniverse.project.feignInterface.ProjectToSystemInterface;
import cn.pioneeruniverse.project.feignInterface.TestTaskInterface;
import cn.pioneeruniverse.project.service.requirement.RequirementService;
import cn.pioneeruniverse.project.service.requirementsystem.RequirementSystemService;
import cn.pioneeruniverse.project.vo.SynRequirement;

@Transactional(readOnly = true)
@Service("requirementService")
public class RequirementServiceImpl implements RequirementService {

	@Autowired
	private S3Util s3Util;
	@Autowired
	private RedisUtils redisUtils;
	@Autowired
	private DevTaskInterface taskInterface;
	@Autowired
	private TestTaskInterface testInterface;
	@Autowired
	private RequirementMapper requirementMapper;
	@Autowired
	private RequirementAttachementMapper attaMapper;
	@Autowired
	private RequirementMapper tblRequirementInfoMapper;
	@Autowired
	private RequirmentSystemMapper tblRequirementSystemMapper;
	@Autowired
	private ProjectToSystemInterface projectToSystemInterface;
	@Autowired
	private RequirementSystemService requirementSystemService;
	@Autowired
	private TblRequirementAttentionMapper tblRequirementAttentionMapper;

	private static final Logger logger = LoggerFactory.getLogger(RequirementServiceImpl.class);

	/**
	 * @deprecated ??????????????????????????????
	 * @param requirement ????????????
	 * @param roleCodes ??????????????????
	 * @return int
	 */
	@Override
	public int getCountRequirement(TblRequirementInfo requirement,List<String> roleCodes) {
		Map<String, Object> map = getRequirementMap(requirement);
		map.put("req", requirement);
		if (roleCodes!=null && roleCodes.contains("XITONGGUANLIYUAN")) {//?????????????????????????????????????????????
			return tblRequirementInfoMapper.getCountRequirement1(map);
		}else {
			return tblRequirementInfoMapper.getCountRequirement(map);
		}

	}

	/**
	 * @deprecated ????????????????????????
	 * @param requirement ????????????
	 * @param pageIndex ??????
	 * @param pageSize ????????????
	 * @param roleCodes ??????????????????
	 * @return List<TblRequirementInfo>
	 */
	@Override
	public List<TblRequirementInfo> getAllRequirement(TblRequirementInfo requirement, Integer pageIndex,Integer pageSize,List<String> roleCodes) {
		Map<String, Object> map = getRequirementMap(requirement);
		List<TblRequirementInfo> reqList = new ArrayList<>();
		if (pageIndex != null && pageSize != null) {			
			//PageHelper.startPage(pageIndex, pageSize);						
			map.put("pageIndex", (pageIndex - 1) * pageSize);
			map.put("pageSize", pageSize);	
			map.put("req", requirement);
			if (roleCodes!=null && roleCodes.contains("XITONGGUANLIYUAN")) {//?????????????????????????????????????????????
				reqList = tblRequirementInfoMapper.getAllRequirement1(map);
			}else {
				reqList = tblRequirementInfoMapper.getAllRequirement(map);
			}
		} else {
			map.put("req", requirement);
			if (roleCodes!=null && roleCodes.contains("XITONGGUANLIYUAN")) {//?????????????????????????????????????????????
				reqList = tblRequirementInfoMapper.excelRequirement1(map);
			}else {
				reqList = tblRequirementInfoMapper.excelRequirement(map);
			}
		}		
		return reqList;
	}
		

	/**
	 * @deprecated ??????????????????
	 * @param rId ??????id
	 * @param parentId ?????????id
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 */
	@Override
	public Map<String, Object> findRequirementById(Long rId,Long parentId) {
		Map<String, Object> result = new HashMap<>();
		StringBuilder triSons= new StringBuilder();
		
		TblRequirementInfo tri = tblRequirementInfoMapper.findRequirementById(rId);
		String requirement_ids = tri.getRequirementIds();
		if(requirement_ids != null) {
			List<String> requirementNames = tblRequirementInfoMapper.getRelationrequirementNames(requirement_ids);
			tri.setRequirementNames(requirementNames);
		}
		if(tri.getParentId() != null) {
			String code = tblRequirementInfoMapper.getCodeById(tri.getParentId());
			tri.setParentCode(code);
		}
		//????????????????????????
		List<TblRequirementSystem> reqSysList = tblRequirementSystemMapper.getReqSystemByReqId(rId);
		if(reqSysList != null && reqSysList.size() > 0) {
			for (TblRequirementSystem requirementSystem : reqSysList) {
				String assetSystemName = tblRequirementSystemMapper.getAssetSystemNameByAssetSystemId(requirementSystem.getAssetSystemTreeId());
				String systemName = tblRequirementSystemMapper.getSystemNameBySystemId(requirementSystem.getSystemId());
				requirementSystem.setAssetSystemName(assetSystemName);
				requirementSystem.setSystemName(systemName);
			}
		}
		tri.setList(reqSysList);
//		List<Long> sysIds = tblRequirementSystemMapper.getSystemByrequirementId(rId);
//		if(sysIds.size()>0) {
//			List<String> sysNames = tblRequirementSystemMapper.getSystemNameByIds(sysIds);
//			tri.setSystemIds(StringUtils.join(sysIds, ","));
//			tri.setSystemNames(StringUtils.join(sysNames, ","));
//		}
//		Long assetSystemId = tblRequirementSystemMapper.getAssetSystemIdByRequirementId(rId);
//		String assetSystemName = tblRequirementSystemMapper.getAssetSystemNameByAssetSystemId(assetSystemId);
//		tri.setModuleId(assetSystemId);
//		tri.setModuleName(assetSystemName);
		
		
		TblRequirementInfo triFather = tblRequirementInfoMapper.findFatherReq(parentId);
		List<TblRequirementInfo> triSon = tblRequirementInfoMapper.findSonReq(rId);			
		List<TblRequirementAttachement> attachements = attaMapper.getRequirementAttachement(rId);
		for(int i=0;i< triSon.size();i++) {
			if(i<triSon.size()-1) {
				triSons.append(triSon.get(i).getRequirementCode()+" | "+triSon.get(i).getRequirementName()+", ");
			}else {
				triSons.append(triSon.get(i).getRequirementCode()+" | "+triSon.get(i).getRequirementName());
			}
		}
		result.put("data", tri);
		result.put("triFather", triFather);			
		result.put("triSon", triSons);
		result.put("attachements", attachements);
		return result;
	}

	/**
	 * @deprecated ??????????????????????????????
	 * @param requirementInfo ??????id
	 * @return List<TblRequirementInfo>
	 */
	@Override
	public List<TblRequirementInfo> findRequirementByName(TblRequirementInfo requirementInfo){		
		return tblRequirementInfoMapper.findRequirementByName(requirementInfo);		
	}
	/**
	 * @deprecated ????????????
	 * @param rId ??????id
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 */
	@Override
	public Map<String, Object> toEditRequirementById(Long rId) {
		Map<String, Object> result = new HashMap<>();
		TblRequirementInfo tri = tblRequirementInfoMapper.findRequirementById(rId);
		String requirement_ids = tri.getRequirementIds();
		if(requirement_ids != null) {
			List<String> requirementNames = tblRequirementInfoMapper.getRelationrequirementNames(requirement_ids);
			tri.setRequirementNames(requirementNames);
		}
		if(tri.getParentId() != null) {
			String code = tblRequirementInfoMapper.getCodeById(tri.getParentId());
			tri.setParentCode(code);
		}
		//????????????????????????
		List<TblRequirementSystem> reqSysList = tblRequirementSystemMapper.getReqSystemByReqId(rId);
		if(reqSysList != null && reqSysList.size() > 0) {
			for (TblRequirementSystem requirementSystem : reqSysList) {
				String assetSystemName = tblRequirementSystemMapper.getAssetSystemNameByAssetSystemId(requirementSystem.getAssetSystemTreeId());
				String systemName = tblRequirementSystemMapper.getSystemNameBySystemId(requirementSystem.getSystemId());
				String systemCode = tblRequirementSystemMapper.getSystemCodeBySystemId(requirementSystem.getSystemId());
				requirementSystem.setAssetSystemName(assetSystemName);
				requirementSystem.setSystemName(systemName);
				requirementSystem.setSystemCode(systemCode);
			}
		}
		tri.setList(reqSysList);
		result.put("data", tri);		
		return result;
	}

	/**
	 * @deprecated ?????????????????????itmp_db
	 * @param files ????????????
	 * @param reqId ??????id
	 * @param request
	 * @return
	 */
    @Override
	@DataSource(name = "itmpDataSource")
	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void uploadFileItmp(MultipartFile[] files, Long reqId, HttpServletRequest request) throws Exception{
    	List<TblRequirementAttachement> listAtta= new ArrayList<>();
        if (files.length > 0 && files !=null) {
            for (MultipartFile file : files) {
                if (Objects.isNull(file) || file.isEmpty()) {
                	return;
                }
                TblRequirementAttachement atta = putFile(file,request);
                atta = (TblRequirementAttachement) CommonUtil.setBaseValue(atta,request);
                atta.setRequirementId(reqId);
                
                attaMapper.insertSelective(atta);
                listAtta.add(atta);           
            }
        }
		SpringContextHolder.getBean(RequirementService.class).uploadFileTmp(listAtta);
    }
    

	/**
	 * @deprecated ?????????????????????tmp_db
	 * @param listAtta ?????????????????????
	 * @return
	 */
    @Override
	@DataSource(name = "tmpDataSource")
	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void uploadFileTmp(List<TblRequirementAttachement> listAtta) throws Exception{
        for (TblRequirementAttachement atta : listAtta) {
            attaMapper.insert(atta);
        }      
    }
    
    /**
     * ??????????????????
     * @param file
     * @return
     * @throws Exception
     */
	@Transactional(readOnly = false)
	public TblRequirementAttachement putFile(MultipartFile file, HttpServletRequest request) throws Exception {
		TblRequirementAttachement atta = new TblRequirementAttachement();
		if (!file.isEmpty()) {
			InputStream inputStream = file.getInputStream();
			String extension = file.getOriginalFilename()
					.substring(file.getOriginalFilename().lastIndexOf(".") + 1);// ?????????
			String fileNameOld = file.getOriginalFilename();
			if (BrowserUtil.isMSBrowser(request)) {
				fileNameOld = fileNameOld.substring(fileNameOld.lastIndexOf("\\") + 1);
			}

			String keyname = s3Util.putObject(s3Util.getRequirementBucket(), fileNameOld, inputStream);
			atta.setFileS3Bucket(s3Util.getRequirementBucket());
			atta.setFileS3Key(keyname);
			atta.setFileNameOld(fileNameOld);
			atta.setFileType(extension);
		}		
		return atta;
	}
    

	/**
	 * @deprecated ??????itmp_db????????????
	 * @param atta ?????????????????????
	 * @param request
	 * @return
	 */
	@Override
	@DataSource(name = "itmpDataSource")
	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void removeAttItmp(TblRequirementAttachement atta, HttpServletRequest request) {
		atta.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		atta.setLastUpdateDate(new Timestamp(new Date().getTime()));
		attaMapper.removeAttachement(atta);
		SpringContextHolder.getBean(RequirementService.class).removeAttTmp(atta);
	}

	/**
	 * @deprecated ??????tmp_db????????????
	 * @param atta ?????????????????????
	 * @return
	 */
	@Override
	@DataSource(name = "tmpDataSource")
	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public void removeAttTmp(TblRequirementAttachement atta) {
		attaMapper.removeAttachement(atta);		
	}

	/**
	 * @deprecated ???????????????itmp_db
	 * @param requirementInfo ????????????
	 * @param request
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 */
	@Override
	@DataSource(name = "itmpDataSource")
	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Map<String, Object> insertRequirementItmp(TblRequirementInfo requirementInfo,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		requirementInfo.setRequirementCode(getRequirementCode1());
		requirementInfo = (TblRequirementInfo) CommonUtil.setBaseValue(requirementInfo,request);
		tblRequirementInfoMapper.insertRequirement(requirementInfo);		

        map.put("reqId",requirementInfo.getId());
        map.put("requirementInfo", JSONObject.toJSONString(requirementInfo));
        //????????????????????????
        List<TblRequirementSystem> reqsysList = requirementInfo.getList();
        if(reqsysList !=null && reqsysList.size()>0) {
        	for (TblRequirementSystem requirementSystem : reqsysList) {
        		requirementSystem.setCreateBy(CommonUtil.getCurrentUserId(request));
            	requirementSystem.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
            	requirementSystem.setCreateDate(new Timestamp(new Date().getTime()));
            	requirementSystem.setLastUpdateDate(new Timestamp(new Date().getTime()));
            	requirementSystem.setStatus(1);
            	requirementSystem.setRequirementId(requirementInfo.getId());
            	tblRequirementSystemMapper.insertReqSystem(requirementSystem);
			}
        }

    	//??????????????????????????????????????? --ztt
  		Map<String,Object> emWeMap = new HashMap<String, Object>();
  		emWeMap.put("messageTitle", "???IT???????????????????????????- ??????????????????????????????");
  		emWeMap.put("messageContent","?????????????????????????????????"+ requirementInfo.getRequirementCode()+" | "+requirementInfo.getRequirementName()+"????????????????????????");
  		String userIds = requirementMapper.getProManageUserIds(requirementInfo.getId());//????????????????????????????????????????????????????????? 
  		emWeMap.put("messageReceiver",userIds);//????????? ???????????????
  		emWeMap.put("sendMethod", 3);//???????????? 3 ???????????????
  		projectToSystemInterface.sendMessage(JSON.toJSONString(emWeMap));

		SpringContextHolder.getBean(RequirementService.class).insertRequirementTmp(requirementInfo);
        return map;
	}

	/**
	 * @deprecated ???????????????tmp_db
	 * @param requirementInfo ????????????
	 * @return
	 */
	@Override
	@DataSource(name = "tmpDataSource")
	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public void insertRequirementTmp(TblRequirementInfo requirementInfo) {
		TblRequirementInfo requirement = tblRequirementInfoMapper.findRequirementById1(requirementInfo.getId());
		if(requirement==null) {
			tblRequirementInfoMapper.insertRequirement(requirementInfo);
		}
	}
	
	//????????????
	@Override
	public void sendAddMessage(HttpServletRequest request, TblRequirementInfo requirementInfo) {
		// TODO Auto-generated method stub
		String userIds = "";
		if(requirementInfo.getApplyUserId()!=null) {
			userIds += requirementInfo.getApplyUserId() + ",";
		}
		if(requirementInfo.getDevelopmentManageUserId()!=null) {
			userIds += requirementInfo.getDevelopmentManageUserId() + ",";
		}
		if(requirementInfo.getRequirementManageUserId()!=null) {
			userIds += requirementInfo.getRequirementManageUserId() + ",";
		}
		if(requirementInfo.getRequirementAcceptanceUserId()!=null) {
			userIds += requirementInfo.getRequirementAcceptanceUserId() + ",";
		}
		List<TblRequirementSystem> list = requirementInfo.getList();
		String systemIds = "";
		if(list != null && list.size()>0) {
			for (TblRequirementSystem tblRequirementSystem : list) {
				systemIds += tblRequirementSystem.getSystemId() + ",";
			}
		}
		if(!systemIds.equals("")) {
			List<Long> ids = tblRequirementInfoMapper.getUserIdsBySystemIds(systemIds);
			for (Long id : ids) {
				userIds += id+",";
			}
		}
		Map<String,Object> map=new HashMap<>();
		map.put("messageTitle","?????????????????????");
		map.put("messageContent",requirementInfo.getRequirementCode()+"|"+requirementInfo.getRequirementName());
		map.put("messageReceiverScope",2);
		map.put("messageReceiver",userIds);
		projectToSystemInterface.insertMessage(JSON.toJSONString(map));
	}

	/**
	 * @deprecated ???????????????itmp_db
	 * @param requirementInfo ????????????
	 * @param request
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 */
	@Override
	@DataSource(name = "itmpDataSource")
	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Map<String, Object> updateRequirementItmp(TblRequirementInfo requirementInfo,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		requirementInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		requirementInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));
		
		tblRequirementInfoMapper.updateRequirement(requirementInfo);		
        map.put("reqId",requirementInfo.getId());
        map.put("requirementInfo", JSONObject.toJSONString(requirementInfo));
        //??????????????????
        tblRequirementSystemMapper.updateReqAssSystemTree(requirementInfo);
        List<TblRequirementSystem> list = requirementInfo.getList();
        if(list != null && list.size() > 0) {
        	for (TblRequirementSystem requirementSystem : list) {
        		requirementSystem.setCreateBy(CommonUtil.getCurrentUserId(request));
            	requirementSystem.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
            	requirementSystem.setCreateDate(new Timestamp(new Date().getTime()));
            	requirementSystem.setLastUpdateDate(new Timestamp(new Date().getTime()));
            	requirementSystem.setStatus(1);
            	requirementSystem.setRequirementId(requirementInfo.getId());
        		tblRequirementSystemMapper.insertReqSystem(requirementSystem);
			}
        }
		SpringContextHolder.getBean(RequirementService.class).updateRequirementTmp(requirementInfo);
		return map;
	}

	/**
	 * @deprecated ???????????????tmp_db
	 * @param requirementInfo ????????????
	 * @return
	 */
	@Override
	@DataSource(name = "tmpDataSource")
	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public void updateRequirementTmp(TblRequirementInfo requirementInfo) {
		tblRequirementInfoMapper.updateRequirement(requirementInfo);
	}
	
	//??????????????????
	@Override
	public void sendEditMessage(HttpServletRequest request, TblRequirementInfo requirementInfo, List<TblRequirementAttention> attentionList) {
		String userIds = "";
		for (TblRequirementAttention attention : attentionList) {
			userIds += attention.getUserId() + ",";
		}
		userIds = userIds.substring(0, userIds.length() - 1);
		String code = tblRequirementInfoMapper.getCodeById(requirementInfo.getId());
		Map<String,Object> map=new HashMap<>();
		map.put("messageTitle","???????????????????????????");
		map.put("messageContent",code+"|"+requirementInfo.getRequirementName());
		map.put("messageReceiverScope",2);
		map.put("messageReceiver",userIds);
		projectToSystemInterface.insertMessage(JSON.toJSONString(map));
	}
	
	@Override
	public List<TblRequirementAttention> getAttentionList(TblRequirementAttention attention) {
		List<TblRequirementAttention> attentionList = tblRequirementAttentionMapper.selectList(new EntityWrapper<TblRequirementAttention>(attention));
		return attentionList;
	}
	
	//????????????????????????
	@Override
	@Transactional(readOnly = false)
	public void changeAttention(Long id, Integer attentionStatus, HttpServletRequest request) {
		Long userId = CommonUtil.getCurrentUserId(request);
		Timestamp stamp = new Timestamp(new Date().getTime());
		TblRequirementAttention attention = new TblRequirementAttention();
		attention.setRequirementId(id);
		attention.setUserId(userId);
		TblRequirementAttention attentionOld = tblRequirementAttentionMapper.selectOne(attention);
		if (attentionStatus == 1) { //??????
			if (attentionOld == null) {
				CommonUtil.setBaseValue(attention, request);
				tblRequirementAttentionMapper.insert(attention);
			} else if (attentionOld.getStatus() == 2) {
				attentionOld.setStatus(1);
				attentionOld.setLastUpdateBy(userId);
				attentionOld.setLastUpdateDate(stamp);
				tblRequirementAttentionMapper.updateByPrimaryKey(attentionOld);
			}
		} else {
			if (attentionOld != null) {
				attentionOld.setStatus(2);
				attentionOld.setLastUpdateBy(userId);
				attentionOld.setLastUpdateDate(stamp);
				tblRequirementAttentionMapper.updateByPrimaryKey(attentionOld);
			}
		}
	}

	/**
	 * @deprecated ???????????????itmp_db
	 * @param requirementStr ????????????
	 * @param reqSystemList ???????????????
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 */
	@Override
	@DataSource(name = "itmpDataSource")
	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Map<String,Object> updateRequirementDataItmp(String requirementStr,String reqSystemList) {
		Map<String, Object> map = new HashMap<>();
		//???????????????????????????
		List<SynRequirement> synRequirement = JSONObject.parseArray(requirementStr, SynRequirement.class);
		//?????????SynRequirement????????????TblRequirementInfo
		TblRequirementInfo tri = SynRequirementUtil.SynTblRequirementInfo(synRequirement.get(0));
		TblRequirementInfo requirement = tblRequirementInfoMapper.getRequirementInByCode(tri.getRequirementCode());
		if(!tri.getRequirementStatus().toUpperCase(Locale.ENGLISH).equals("REQ_CANCELED")) {
			if (requirement == null) {
				//???????????????????????????true??????????????????false
				if (redisUtils.set("name_" + tri.getRequirementCode(), tri.getRequirementCode())) {
					try {
						tblRequirementInfoMapper.insertRequirement(tri);
						map.put("reqId", tri.getId());
						map.put("synStatus", "insert");
					} catch (Exception e) {
						//????????????????????????
						redisUtils.remove("name_" + tri.getRequirementCode());
						throw e;
					}
					logger.info(Thread.currentThread().getName() + "success");
					//???????????????3???
					redisUtils.expire("name_" + tri.getRequirementCode(), Long.valueOf(3));
				}
			} else {
				tri.setId(requirement.getId());
				tblRequirementInfoMapper.updateRequirementData(tri);
				map.put("synStatus", "update");
				map.put("reqId", requirement.getId());
			}
			map.put("requirementInfo",JSON.toJSONString(tri));
			requirementSystemService.updateReqSystemData(reqSystemList,map.get("reqId").toString());
		}else{
			if(requirement!=null){
				taskInterface.changeCancelStatus(requirement.getId());
				testInterface.changeCancelStatus(requirement.getId());
			}
		}
		SpringContextHolder.getBean(RequirementService.class).updateRequirementDataTmp(tri);
		return map; 
	}

	/**
	 * @deprecated ???????????????tmp_db
	 * @param tri ????????????
	 * @return
	 */
	@Override
	@DataSource(name = "tmpDataSource")
	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public void updateRequirementDataTmp(TblRequirementInfo tri) {
		if(!tri.getRequirementStatus().toUpperCase(Locale.ENGLISH).equals("REQ_CANCELED")) {
			TblRequirementInfo requirement = tblRequirementInfoMapper.findRequirementById1(tri.getId());
			if(requirement==null) {
				tblRequirementInfoMapper.insert(tri);
			}else {
				tblRequirementInfoMapper.updateRequirementData(tri);
			}
		}
	}

	/**
	 * @deprecated ????????????
	 * @param id ??????id
	 * @return
	 */
	@Override
	@Transactional(readOnly = false)
	public int deleteRequirement(Long id) {	
		return tblRequirementInfoMapper.deleteRequirement(id);
	}

	/**
	 * @deprecated ??????????????????
	 * @param id ??????id
	 * @return List<TblRequirementAttachement>
	 */
	@Override
	public List<TblRequirementAttachement> getRequirementAttachement(Long id) {		
		return attaMapper.getRequirementAttachement(id);
	}

	@Override
	public List<Map<String, Object>> getAllRequirement2(TblRequirementInfo requirement, int pageNumber, int pageSize) {
		Map<String, Object> map = new HashMap<>();
		int start = (pageNumber - 1) * pageSize;
		map.put("start", start);
		map.put("pageSize", pageSize);
		map.put("requirement", requirement);
		return tblRequirementInfoMapper.getAllReq(map);		
	}
	
	//???????????????????????????
	@Override
	public String selectMaxRqeuirementCode() {
		return tblRequirementInfoMapper.selectMaxRqeuirementCode();
	}

	@Override
	public int getAllRequirementCount(TblRequirementInfo requirement) {
		return tblRequirementInfoMapper.getAllRequirementCount(requirement);
	}

	private Map<String,Object> getRequirementMap(TblRequirementInfo requirement){
		Map<String, Object> map = new HashMap<>();
		if(requirement.getRequirementStatus()!=null&&!requirement.getRequirementStatus().equals("")) {
			String [] struts=requirement.getRequirementStatus().split(",");
			if(struts.length>0){
				map.put("struts", struts);
			}
		}
		if(requirement.getSystemId()!=null&&!requirement.getSystemId().equals("")) {
			String [] systemIds=requirement.getSystemId().split(",");
			if(systemIds.length>0){
				map.put("systemIds", systemIds);
			}
		}
		if(requirement.getFeatureId()!=null&&!requirement.getFeatureId().equals("")) {
			String [] featureIds=requirement.getFeatureId().split(",");
			if(featureIds.length>0){
				map.put("featureIds", featureIds);
			}
		}
		if(requirement.getDevManageIds()!=null&&!requirement.getDevManageIds().equals("")) {
			String [] devManageIds=requirement.getDevManageIds().split(",");
			if(devManageIds.length>0){
				map.put("devManageIds", devManageIds);
			}
		}
		if(requirement.getReqManageIds()!=null&&!requirement.getReqManageIds().equals("")) {
			String [] reqManageIds=requirement.getReqManageIds().split(",");
			if(reqManageIds.length>0){
				map.put("reqManageIds", reqManageIds);
			}
		}
		return map;
	}

	@Override
	public List<ExtendedField> findRequirementField(Long id) {
		// TODO Auto-generated method stub
		 Map<String,Object> map=projectToSystemInterface.findFieldByTableName("tbl_requirement_info");
         String listTxt = JSONArray.toJSONString(map.get("field"));
         List<ExtendedField> extendedFields = JSONArray.parseArray(listTxt, ExtendedField.class);
         if(id!=null) {
             if (extendedFields != null) {
                 Iterator<ExtendedField> it = extendedFields.iterator();
                 while (it.hasNext()) {
                     ExtendedField extendedField = it.next();
                     if (extendedField.getStatus().equals("2")) {
                         it.remove();
                     } else {
                         String fieldName=extendedField.getFieldName();
                         String valueName = tblRequirementInfoMapper.getDevTaskFieldTemplateById(id, fieldName);
                         extendedField.setValueName(valueName == null ? "" : valueName);
                     }
                 }
             }
         }else {
        	 if (extendedFields != null) {
                 Iterator<ExtendedField> it = extendedFields.iterator();
                 while (it.hasNext()) {
                     ExtendedField extendedField = it.next();
                     if (extendedField.getStatus().equals("2")) {
                         it.remove();
                     }
                 }
        	 }
         }
         return extendedFields;
	}

	@Override
	public List<String> getRequirementsByIds(String reqIds) {
		// TODO Auto-generated method stub
		List<String> list = tblRequirementInfoMapper.getRelationrequirementNames(reqIds);
		return list;
	}

	@Override
	public List<String> getsystems(Long id) {
		// TODO Auto-generated method stub
		List<String> list = tblRequirementSystemMapper.getsystems(id);
		return list;
	}

	// ??????????????????
	private String getRequirementCode1() {
		String featureCode = "";
		Integer codeInt = 0;
		Object object = redisUtils.get(Constants.TMP_TEST_REQ_CODE);
		if (object != null && !"".equals(object)) { // redis????????????redis??????
			String code = object.toString();
			if (!StringUtils.isBlank(code)) {
				codeInt = Integer.parseInt(code) + 1;
			}
		} else {									// redis????????????????????????????????????????????????
			String maxDefectNo = this.selectMaxRqeuirementCode();
			codeInt = 1;
			if (StringUtils.isNotBlank(maxDefectNo)) {
				codeInt = Integer.valueOf(maxDefectNo.substring(Constants.TMP_TEST_REQ_CODE.length())) + 1;
			}
		}
		featureCode = Constants.TMP_TEST_REQ_CODE + String.format("%08d", codeInt);
		redisUtils.set(Constants.TMP_TEST_REQ_CODE, String.format("%08d", codeInt));
		return featureCode;
	}
}
