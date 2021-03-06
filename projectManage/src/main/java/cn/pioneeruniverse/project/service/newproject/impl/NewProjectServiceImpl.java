package cn.pioneeruniverse.project.service.newproject.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.pioneeruniverse.common.annotion.DataSource;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.common.utils.SpringContextHolder;
import cn.pioneeruniverse.project.dao.mybatis.DeptInfoMapper;
import cn.pioneeruniverse.project.dao.mybatis.NewProjectMapper;
import cn.pioneeruniverse.project.dao.mybatis.TblProgramInfoMapper;
import cn.pioneeruniverse.project.dao.mybatis.TblProjectSystemMapper;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.TblSystemDirectoryMapper;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.TblSystemDirectoryTemplateMapper;
import cn.pioneeruniverse.project.entity.TblProgramInfo;
import cn.pioneeruniverse.project.entity.TblProgramProject;
import cn.pioneeruniverse.project.entity.TblProjectInfo;
import cn.pioneeruniverse.project.entity.TblProjectSystem;
import cn.pioneeruniverse.project.entity.TblSystemDirectory;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryTemplate;
import cn.pioneeruniverse.project.service.newproject.NewProjectService;

@Service
public class NewProjectServiceImpl implements NewProjectService {
	
	@Autowired
	private NewProjectMapper newProjectMapper;
	
	@Autowired
	private RedisUtils redisUtils;
	
	@Autowired
	private DeptInfoMapper deptInfoMapper;
	
	@Autowired
	private TblProjectSystemMapper tblProjectSystemMapper;
	
	@Autowired
	private TblProgramInfoMapper programInfoMapper;
	
	@Autowired
	private TblSystemDirectoryMapper tblSystemDirectoryMapper;
	
	@Autowired
	private TblSystemDirectoryTemplateMapper tblSystemDirectoryTemplateMapper;

	/**
	 * 
	* @Title: getAllNewProject
	* @Description: ??????????????????????????????????????????
	* @author author
	* @param projectInfo
	* @param uid ????????????ID
	* @param roleCodes ??????
	* @param page ?????????
	* @param rows ????????????
	* @return List<TblProjectInfo>
	 */
	@Override
	@Transactional(readOnly=true)
	public List<TblProjectInfo> getAllNewProject(TblProjectInfo projectInfo, Long uid, List<String> roleCodes,
			Integer page, Integer rows) {
		Integer start = (page-1)*rows;
		Map<String,Object> map = new HashMap<>();
		map.put("projectInfo", projectInfo);
		map.put("uid", uid);
		map.put("start", start);
		map.put("rows", rows);
		List<TblProjectInfo> list = new ArrayList<>();
		if (roleCodes!=null && roleCodes.contains("XITONGGUANLIYUAN")) { //????????????????????????????????????????????????
			list = newProjectMapper.getAllNewProject(map);
		}else {
			list = newProjectMapper.getNewProjectByUid(map);
		}
		for (TblProjectInfo tblProjectInfo : list) {
			Integer projectStatus = tblProjectInfo.getProjectStatus();
			String redisStr = redisUtils.get("TBL_PROJECT_INFO_PROJECT_STATUS").toString();
			JSONObject jsonObj = JSON.parseObject(redisStr);
			String statusName = jsonObj.get(projectStatus+"").toString();
			tblProjectInfo.setProjectStatusName(statusName);
			
			Integer projectClass = tblProjectInfo.getProjectClass();
			String redisStr2 = redisUtils.get("TBL_PROJECT_INFO_PROJECT_CLASS").toString();
			JSONObject jsonObj2 = JSON.parseObject(redisStr2);
			String className = jsonObj2.get(projectClass+"").toString();
			tblProjectInfo.setProjectClassName(className);
		}
		return list;
	}

	
	/**
	 * 
	* @Title: getNewProjectById
	* @Description: ??????ID??????????????????????????????????????????
	* @author author
	* @param id
	* @return TblProjectInfo
	 */
	@Override
	@Transactional(readOnly=true)
	public TblProjectInfo getNewProjectById(Long id) {
		TblProjectInfo projectInfo = newProjectMapper.getNewProjectById(id);
		Integer projectStatus = projectInfo.getProjectStatus();
		//????????????
		String redisStr = redisUtils.get("TBL_PROJECT_INFO_PROJECT_STATUS").toString();
		JSONObject jsonObj = JSON.parseObject(redisStr);
		String statusName = jsonObj.get(projectStatus).toString();
		projectInfo.setProjectStatusName(statusName);
		
		Integer projectClass = projectInfo.getProjectClass();
		String redisStr2 = redisUtils.get("TBL_PROJECT_INFO_PROJECT_CLASS").toString();
		JSONObject jsonObj2 = JSON.parseObject(redisStr2);
		String className = jsonObj2.get(projectClass).toString();
		projectInfo.setProjectClassName(className);
		//???????????????????????????
		List<Long> developSystemIds = tblProjectSystemMapper.getDevelopSystemIds(id); 
		//???????????????????????????
		List<Long> peripheralSystemIds = tblProjectSystemMapper.getPeripheralSystemIds(id);  
		projectInfo.setDevelopSystemIds(StringUtils.join(developSystemIds,","));
		projectInfo.setPeripheralSystemIds(StringUtils.join(peripheralSystemIds,","));
		
		return projectInfo;
	}


	/**
	 * 
	* @Title: insertNewProject
	* @Description: ?????????????????????
	* @author author
	* @param projectInfo ??????bean 
	* @param request
	* @return Long ??????ID
	* @throws Exception
	 */
	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class,propagation = Propagation.REQUIRED)
	public Long insertNewProject(TblProjectInfo projectInfo, HttpServletRequest request) throws Exception {
		String projectCode = getProjectCode(projectInfo);
		projectInfo.setProjectCode(projectCode);
		//?????????????????????????????????????????????id
		Long deptId = deptInfoMapper.getDeptIdByNumber(projectInfo.getDeptNumber());
		projectInfo.setDeptId(deptId);
		Long businessDeptId = deptInfoMapper.getDeptIdByNumber(projectInfo.getBusinessDeptNumber());
		projectInfo.setBusinessDeptId(businessDeptId);
		projectInfo.setStatus(1);
		projectInfo.setProjectType(2);
		projectInfo.setProjectStatus(1);
		projectInfo.setCreateBy(CommonUtil.getCurrentUserId(request));
		projectInfo.setCreateDate(new Timestamp(new Date().getTime()));
		projectInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		projectInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));
		newProjectMapper.insertNewProject(projectInfo);
		Long projectId = projectInfo.getId();
		try {
			
			//?????????????????????????????????????????????????????????
			String sIds = projectInfo.getDevelopSystemIds();
			if(!("").equals(sIds)) {
				String[] developSystemIds = sIds.split(",");
				for (String developSystemId : developSystemIds) {
					TblProjectSystem projectSystem = new TblProjectSystem();
					projectSystem.setProjectId(projectId);
					projectSystem.setSystemId(Long.valueOf(developSystemId));
					//????????????
					projectSystem.setRelationType(1);
					projectSystem.setStatus(1);
					projectSystem.setCreateBy(CommonUtil.getCurrentUserId(request));
					projectSystem.setCreateDate(new Timestamp(new Date().getTime()));
					projectSystem.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
					projectSystem.setLastUpdateDate(new Timestamp(new Date().getTime()));
					tblProjectSystemMapper.insertProjectSystem(projectSystem);
				}
			}

			//?????????????????????????????????????????????????????????
			String sIds2 = projectInfo.getPeripheralSystemIds();
			if(!("").equals(sIds2)) {
				String[] peripheralSysetemIds = sIds2.split(",");
				for (String peripheralSysetemId : peripheralSysetemIds) {
					TblProjectSystem projectSystem = new TblProjectSystem();
					projectSystem.setProjectId(projectId);
					projectSystem.setSystemId(Long.valueOf(peripheralSysetemId));
					//????????????
					projectSystem.setRelationType(2);
					projectSystem.setStatus(1);
					projectSystem.setCreateBy(CommonUtil.getCurrentUserId(request));
					projectSystem.setCreateDate(new Timestamp(new Date().getTime()));
					projectSystem.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
					projectSystem.setLastUpdateDate(new Timestamp(new Date().getTime()));
					tblProjectSystemMapper.insertProjectSystem(projectSystem);
				}
			}
			
			//???????????????
			SpringContextHolder.getBean(NewProjectService.class).insertTmpNewProject(projectInfo,request);
			
		
		} catch (Exception e) {
			throw new Exception();
		}
		return projectId;
	}
	
	/**
	 * 
	* @Title: insertTmpNewProject
	* @Description: ???????????????????????????
	* @author author
	* @param projectInfo ????????????
	* @param request
	 */
	@Override
	@DataSource(name = "tmpDataSource")
	@Transactional(readOnly=false,rollbackFor=Exception.class,propagation=Propagation.REQUIRES_NEW)
	public void insertTmpNewProject(TblProjectInfo projectInfo, HttpServletRequest request) {
		// TODO Auto-generated method stub
		String projectCode = getProjectCode(projectInfo);
		projectInfo.setProjectCode(projectCode);
		//?????????????????????????????????????????????id
		Long deptId = deptInfoMapper.getDeptIdByNumber(projectInfo.getDeptNumber());
		projectInfo.setDeptId(deptId);
		Long businessDeptId = deptInfoMapper.getDeptIdByNumber(projectInfo.getBusinessDeptNumber());
		projectInfo.setBusinessDeptId(businessDeptId);
		projectInfo.setStatus(1);
		projectInfo.setProjectType(2);
		projectInfo.setProjectStatus(1);
		projectInfo.setCreateBy(CommonUtil.getCurrentUserId(request));
		projectInfo.setCreateDate(new Timestamp(new Date().getTime()));
		projectInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		projectInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));
		newProjectMapper.insertNewProject(projectInfo);
		Long projectId = projectInfo.getId();
//		String[] developSystemIds = projectInfo.getDevelopSystemIds().split(","); //????????????
		//??????????????????
		String sIds = projectInfo.getDevelopSystemIds();
		if(!sIds.equals("")) {
			String[] developSystemIds = sIds.split(",");
			for (String developSystemId : developSystemIds) {
				TblProjectSystem projectSystem = new TblProjectSystem();
				projectSystem.setProjectId(projectId);
				projectSystem.setSystemId(Long.valueOf(developSystemId));
				projectSystem.setRelationType(1);
				projectSystem.setStatus(1);
				projectSystem.setCreateBy(CommonUtil.getCurrentUserId(request));
				projectSystem.setCreateDate(new Timestamp(new Date().getTime()));
				projectSystem.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
				projectSystem.setLastUpdateDate(new Timestamp(new Date().getTime()));
				tblProjectSystemMapper.insertProjectSystem(projectSystem);
			}
		}
		
//		String[] peripheralSysetemIds = projectInfo.getPeripheralSystemIds().split(","); //????????????
		//??????????????????
		String sIds2 = projectInfo.getPeripheralSystemIds();
		if(!sIds2.equals("")) {
			String[] peripheralSysetemIds = sIds2.split(",");
			for (String peripheralSysetemId : peripheralSysetemIds) {
				TblProjectSystem projectSystem = new TblProjectSystem();
				projectSystem.setProjectId(projectId);
				projectSystem.setSystemId(Long.valueOf(peripheralSysetemId));
				projectSystem.setRelationType(2);
				projectSystem.setStatus(1);
				projectSystem.setCreateBy(CommonUtil.getCurrentUserId(request));
				projectSystem.setCreateDate(new Timestamp(new Date().getTime()));
				projectSystem.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
				projectSystem.setLastUpdateDate(new Timestamp(new Date().getTime()));
				tblProjectSystemMapper.insertProjectSystem(projectSystem);
			}
		}
	}


	/**
	 * 
	* @Title: getProjectCode
	* @Description: ???????????????????????????????????????????????????????????????????????????
	* @author author
	* @param projectInfo
	* @return String
	 */
	private String getProjectCode(TblProjectInfo projectInfo) {
		String projectCode = "";
		//???????????????????????????1???????????????2???????????????3???????????????4???????????????5????????????
		Integer projectClass = projectInfo.getProjectClass();
		switch (projectClass) {
		case 1:
			projectCode += "SD";
			break;
		case 2:
			projectCode += "HW";
			break;
		case 3:
			projectCode += "SM";
			break;
		case 4:
			projectCode += "SI";
			break;
		case 5:
			projectCode += "TC";
			break;
		}
//		String deptNum = deptInfoMapper.getDeptNumById(projectInfo.getDeptId());
		String deptNum = projectInfo.getDeptNumber();
		projectCode += deptNum.substring(deptNum.length()-2, deptNum.length());
		Calendar date = Calendar.getInstance();
		projectCode += Integer.valueOf(date.get(Calendar.YEAR)).toString();
		Long count = newProjectMapper.getCountNewProject();
		Long i = count + 1;
		if(i < 10) {
			projectCode += "0" + i.toString();
		}else {
			projectCode += i.toString();
		}
		
		return projectCode;
	}


	/**
	 * 
	* @Title: insertProgram
	* @Description: ???????????????
	* @author author
	* @param programInfo ???????????????
	* @param request
	 */
	@Override
	@Transactional(readOnly=false)
	public void insertProgram(TblProgramInfo programInfo, HttpServletRequest request) {
		String programNumber = getProgramNumber();
		programInfo.setProgramNumber(programNumber);
		programInfo.setStatus(1);
		programInfo.setCreateBy(CommonUtil.getCurrentUserId(request));
		programInfo.setCreateDate(new Timestamp(new Date().getTime()));
		programInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		programInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));
		programInfoMapper.insertProgram(programInfo);
		Long programId = programInfo.getId();
//		String[] projectIds = programInfo.getProjectIds().split(",");
		String pIds = programInfo.getProjectIds();
		if(!pIds.equals("")) {
			String[] projectIds = pIds.split(",");
			for (String projectId : projectIds) {
				TblProgramProject programProject = new TblProgramProject();
				programProject.setProgramId(programId);
				programProject.setProjectId(Long.valueOf(projectId));
				programProject.setStatus(1);
				programProject.setCreateBy(CommonUtil.getCurrentUserId(request));
				programProject.setCreateDate(new Timestamp(new Date().getTime()));
				programProject.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
				programProject.setLastUpdateDate(new Timestamp(new Date().getTime()));
				programInfoMapper.insertProgramProject(programProject);
			}
		}
	}


	//?????????????????????
	private String getProgramNumber() {
		String number = "G";
		Calendar date = Calendar.getInstance();
		number += Integer.valueOf(date.get(Calendar.YEAR)).toString();
		Long count = programInfoMapper.getCountProgram();
		Long i = count + 1;
		if(i < 10) {
			number += "0" + i.toString();
		}else {
			number += i.toString();
		}
		return number;
	}


	/**
	 * 
	* @Title: getNewProjectByPage
	* @Description: ???????????????????????????????????????
	* @author author
	* @param projectSearchInfo ????????????
	* @param pageNumber ?????????
	* @param pageSize ????????????
	* @return List<TblProjectInfo>
	 */
	@Override
	@Transactional(readOnly=true)
	public List<TblProjectInfo> getNewProjectByPage(TblProjectInfo projectSearchInfo, Integer pageNumber, Integer pageSize) {
		HashMap<String, Object> map = new HashMap<>();
		Integer start = (pageNumber-1)*pageSize;
		map.put("projectSearchInfo", projectSearchInfo);
		map.put("start", start);
		map.put("pageSize", pageSize);
		List<TblProjectInfo> list = newProjectMapper.getNewProjectByPage(map);
		for (TblProjectInfo projectInfo : list) {
			String redisStr2 = redisUtils.get("TBL_PROJECT_INFO_PROJECT_STATUS").toString();
			JSONObject jsonObj2 = JSON.parseObject(redisStr2);
			String statusName = jsonObj2.get(projectInfo.getProjectStatus()).toString();
			projectInfo.setProjectStatusName(statusName);
		}
		return list;
	}


	/**
	 * 
	* @Title: updateNewProject
	* @Description: ?????????????????????
	* @author author
	* @param projectInfo ????????????
	* @param request
	 */
	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public void updateNewProject(TblProjectInfo projectInfo, HttpServletRequest request) {
		//??????????????????
		//????????????????????????????????????????????????
//		String code = getUpdateProjectCode(projectInfo);
//		projectInfo.setProjectCode(code);
		
		Long deptId = deptInfoMapper.getDeptIdByNumber(projectInfo.getDeptNumber());
		projectInfo.setDeptId(deptId);
		Long businessDeptId = deptInfoMapper.getDeptIdByNumber(projectInfo.getBusinessDeptNumber());
		projectInfo.setBusinessDeptId(businessDeptId);
		projectInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		projectInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));
		newProjectMapper.updateNewProject(projectInfo);
		//??????????????????
		tblProjectSystemMapper.deleteProjectSystem(projectInfo.getId());
		//????????????
		String sIds = projectInfo.getDevelopSystemIds();
		if(!sIds.equals("")) {
			String[] developSystemIds = sIds.split(",");
			for (String developSystemId : developSystemIds) {
				TblProjectSystem projectSystem = new TblProjectSystem();
				projectSystem.setProjectId(projectInfo.getId());
				projectSystem.setSystemId(Long.valueOf(developSystemId));
				projectSystem.setRelationType(1);
				projectSystem.setStatus(1);
				projectSystem.setCreateBy(CommonUtil.getCurrentUserId(request));
				projectSystem.setCreateDate(new Timestamp(new Date().getTime()));
				projectSystem.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
				projectSystem.setLastUpdateDate(new Timestamp(new Date().getTime()));
				tblProjectSystemMapper.insertProjectSystem(projectSystem);
			}
		}
		 //????????????
		String sIds2 = projectInfo.getPeripheralSystemIds();
		if(!sIds2.equals("")) {
			String[] peripheralSysetemIds = sIds2.split(",");
			for (String peripheralSysetemId : peripheralSysetemIds) {
				TblProjectSystem projectSystem = new TblProjectSystem();
				projectSystem.setProjectId(projectInfo.getId());
				projectSystem.setSystemId(Long.valueOf(peripheralSysetemId));
				projectSystem.setRelationType(2);
				projectSystem.setStatus(1);
				projectSystem.setCreateBy(CommonUtil.getCurrentUserId(request));
				projectSystem.setCreateDate(new Timestamp(new Date().getTime()));
				projectSystem.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
				projectSystem.setLastUpdateDate(new Timestamp(new Date().getTime()));
				tblProjectSystemMapper.insertProjectSystem(projectSystem);
			}
		}
		
		//???????????????
		SpringContextHolder.getBean(NewProjectService.class).updateTmpNewProject(projectInfo,request);
	}
	
	
	/**
	 * 
	* @Title: updateTmpNewProject
	* @Description: ??????????????????????????????
	* @author author
	* @param projectInfo ????????????
	* @param request
	 */
	@Override
	@DataSource(name = "tmpDataSource")
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public void updateTmpNewProject(TblProjectInfo projectInfo, HttpServletRequest request) {
		// TODO Auto-generated method stub
		Long deptId = deptInfoMapper.getDeptIdByNumber(projectInfo.getDeptNumber());
		projectInfo.setDeptId(deptId);
		Long businessDeptId = deptInfoMapper.getDeptIdByNumber(projectInfo.getBusinessDeptNumber());
		projectInfo.setBusinessDeptId(businessDeptId);
		projectInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		projectInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));
		newProjectMapper.updateNewProject(projectInfo);
		//????????????????????????
		tblProjectSystemMapper.deleteProjectSystem(projectInfo.getId());
		//????????????
		String sIds = projectInfo.getDevelopSystemIds();
		if(!sIds.equals("")) {
			String[] developSystemIds = sIds.split(",");
			for (String developSystemId : developSystemIds) {
				TblProjectSystem projectSystem = new TblProjectSystem();
				projectSystem.setProjectId(projectInfo.getId());
				projectSystem.setSystemId(Long.valueOf(developSystemId));
				projectSystem.setRelationType(1);
				projectSystem.setStatus(1);
				projectSystem.setCreateBy(CommonUtil.getCurrentUserId(request));
				projectSystem.setCreateDate(new Timestamp(new Date().getTime()));
				projectSystem.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
				projectSystem.setLastUpdateDate(new Timestamp(new Date().getTime()));
				tblProjectSystemMapper.insertProjectSystem(projectSystem);
			}
		}
		 //????????????
		String sIds2 = projectInfo.getPeripheralSystemIds();
		if(!sIds2.equals("")) {
			String[] peripheralSysetemIds = sIds2.split(",");
			for (String peripheralSysetemId : peripheralSysetemIds) {
				TblProjectSystem projectSystem = new TblProjectSystem();
				projectSystem.setProjectId(projectInfo.getId());
				projectSystem.setSystemId(Long.valueOf(peripheralSysetemId));
				projectSystem.setRelationType(2);
				projectSystem.setStatus(1);
				projectSystem.setCreateBy(CommonUtil.getCurrentUserId(request));
				projectSystem.setCreateDate(new Timestamp(new Date().getTime()));
				projectSystem.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
				projectSystem.setLastUpdateDate(new Timestamp(new Date().getTime()));
				tblProjectSystemMapper.insertProjectSystem(projectSystem);
			}
		}
	}



	//???????????????????????????????????????????????????????????????
//	private String getUpdateProjectCode(TblProjectInfo projectInfo) {
//		// TODO Auto-generated method stub
//		String code = "";
//		String projectCode = newProjectMapper.getProjectCodeById(projectInfo.getId());
//		String string = projectCode.substring(4);
//		Integer projectClass = projectInfo.getProjectClass();
//		switch (projectClass) {
//		case 1:
//			code += "SD";
//			break;
//		case 2:
//			code += "HW";
//			break;
//		case 3:
//			code += "SM";
//			break;
//		case 4:
//			code += "SI";
//			break;
//		case 5:
//			code += "TC";
//			break;
//		}
//		String deptNum = projectInfo.getDeptNumber();
//		code += deptNum.substring(deptNum.length()-2, deptNum.length());
//		code += string;
//		return code;
//	}
	
	
	
	//??????????????????????????????????????????????????????????????????
	@Override
	@Transactional(readOnly=false)
	public void updateSystemDirectory(Long projectId, HttpServletRequest request, Integer projectType) throws Exception {
		try {
		//		String[] systemIds = developSystemIds.split(",");
			//?????????????????????????????????
			List<TblSystemDirectoryTemplate> systemDirTems = tblSystemDirectoryTemplateMapper.getAllSystemDirectoryTemplate(projectType);
			List<TblSystemDirectory> systemDirs = tblSystemDirectoryMapper.getSystemDirectoryBySystemId(projectId);
			//???????????????????????????????????????
			if(!systemDirs.isEmpty()&& systemDirs.size()>0){
				for (TblSystemDirectory tblSystemDirectory : systemDirs) {
					for(int i=0;i<systemDirTems.size();i++) {
						if(tblSystemDirectory.getSystemDirectoryTemplateId().longValue() ==  systemDirTems.get(i).getId().longValue()) {
							systemDirTems.remove(i);
						}
					}
				}
			}
			if(!systemDirTems.isEmpty()&& systemDirTems.size()>0){
				//??????????????????????????????
				for (TblSystemDirectoryTemplate tblSystemDirectoryTemplate : systemDirTems) {
					TblSystemDirectory systemDirectory = new TblSystemDirectory();
					systemDirectory.setProjectId(projectId);
					systemDirectory.setProjectType(tblSystemDirectoryTemplate.getProjectType());
					systemDirectory.setDocumentTypes(tblSystemDirectoryTemplate.getDocumentTypes());
					systemDirectory.setSystemDirectoryTemplateId(tblSystemDirectoryTemplate.getId());
					systemDirectory.setDirName(tblSystemDirectoryTemplate.getDirName());
					systemDirectory.setOrderNumber(tblSystemDirectoryTemplate.getOrderNumber());
					systemDirectory.setTierNumber(tblSystemDirectoryTemplate.getTierNumber());
					//????????????????????????????????????ID??????????????????????????????????????????id??????????????????ID??????????????????
	//				systemDirectory.setParentId(tblSystemDirectoryTemplate.getParentId());
	//				systemDirectory.setParentIds(tblSystemDirectoryTemplate.getParentIds());
					systemDirectory.setCreateType(1);
					systemDirectory.setStatus(1);
					systemDirectory.setCreateBy(CommonUtil.getCurrentUserId(request));
					systemDirectory.setCreateDate(new Timestamp(new Date().getTime()));
					systemDirectory.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
					systemDirectory.setLastUpdateDate(new Timestamp(new Date().getTime()));
					tblSystemDirectoryMapper.insertSystemDir(systemDirectory);
				}
				
				//???????????????????????????????????????????????????????????????
				for (TblSystemDirectoryTemplate tblSystemDirectoryTemplate : systemDirTems) {
					if(tblSystemDirectoryTemplate.getParentId()==0) {  //???????????????parntId???0?????????????????????????????????????????????parentId??????0
						Long parentId = Long.valueOf(0);
						Map<String,Object> map = new HashMap<>();
						map.put("projectId", projectId);
						map.put("templateId", tblSystemDirectoryTemplate.getId());
						map.put("parentId", parentId);
						tblSystemDirectoryMapper.updateSystemDIr(map);
					}else {
						//????????????parentId?????????id?????????????????????id????????????????????????ID????????????????????????ID
						Map<String,Object> map = new HashMap<>();
						map.put("projectId", projectId);
						map.put("templateId", tblSystemDirectoryTemplate.getParentId());
						Long id = tblSystemDirectoryMapper.getId(map);
						Map<String,Object> map2 = new HashMap<>();
						map2.put("projectId", projectId);
						map2.put("templateId", tblSystemDirectoryTemplate.getId());
						map2.put("parentId", id);
						tblSystemDirectoryMapper.updateSystemDIr(map2);
					}
					//?????????????????????????????????????????????????????????????????????
					if(tblSystemDirectoryTemplate.getParentIds().equals("0")) {
						String parentIds = "0";
						Map<String,Object> map = new HashMap<>();
						map.put("projectId", projectId);
						map.put("templateId", tblSystemDirectoryTemplate.getId());
						map.put("parentIds", parentIds);
						tblSystemDirectoryMapper.updateSystemDIr2(map);
					}else {
						String parentIds = tblSystemDirectoryTemplate.getParentIds();
						String ids = ",";
						for (String parentId : parentIds.split(",")) {
							Map<String,Object> map = new HashMap<>();
							map.put("projectId", projectId);
							map.put("templateId", parentId);
							Long id = tblSystemDirectoryMapper.getId(map);
							if(id!=null) {
								ids += id+",";
							}
						}
						Map<String,Object> map2 = new HashMap<>();
						map2.put("projectId", projectId);
						map2.put("templateId", tblSystemDirectoryTemplate.getId());
						map2.put("parentIds", ids);
						tblSystemDirectoryMapper.updateSystemDIr2(map2);
					}
				}
			}
		} catch (Exception e) {
			throw new Exception();
		}
	}
}
