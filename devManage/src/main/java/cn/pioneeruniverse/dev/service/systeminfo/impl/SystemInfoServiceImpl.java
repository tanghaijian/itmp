package cn.pioneeruniverse.dev.service.systeminfo.impl;

import java.sql.Timestamp;
import java.util.*;
import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.dev.dao.mybatis.*;
import cn.pioneeruniverse.dev.entity.*;
import cn.pioneeruniverse.dev.vo.TaskProjectVO;

import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.pioneeruniverse.common.annotion.DataSource;
import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.common.utils.CollectionUtil;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.JsonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.common.utils.SpringContextHolder;
import cn.pioneeruniverse.dev.service.build.IJenkinsBuildService;
import cn.pioneeruniverse.dev.service.systeminfo.ISystemInfoService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 类说明
 * 
 * @author:tingting
 * @version:2018年10月30日 下午3:58:02
 */

@Service
public class SystemInfoServiceImpl
		implements ISystemInfoService {

	@Autowired
	private TblSystemInfoMapper systemInfoMapper;
	@Autowired
	private TblSystemScmMapper systemScmMapper;
	@Autowired
	private TblSystemModuleMapper systemModuleMapper;
	@Autowired
	private TblSystemModuleScmMapper systemModuleScmMapper;
	@Autowired
	private TblToolInfoMapper tblToolInfoMapper;
	@Autowired
	private TblSystemJenkinsMapper tblSystemJenkinsMapper;
	@Autowired
	private TblSystemDeployMapper systemDeployMapper;
	@Autowired
	private IJenkinsBuildService iJenkinsBuildService;
	@Autowired
	private ISystemInfoService iSystemInfoService;
	@Autowired
	private TblSystemVersionMapper tblSystemVersionMapper;
	@Autowired
	private TblServerInfoMapper tblServerInfoMapper;
	@Autowired
	private TblCommissioningWindowMapper commissioningWindowMapper;
	@Autowired
	private TblSystemDeployScriptMapper tblSystemDeployScriptMapper;
	@Autowired
	private TblSystemAutomaticTestConfigMapper tblSystemAutomaticTestConfigMapper;
	@Autowired
	private TblTopSystemInfoMapper tblTopSystemInfoMapper;
	@Autowired
	private TblUserInfoMapper tblUserInfoMapper;


	@Autowired
	private TblSystemScmRepositoryMapper scmRepositoryMapper;
	@Autowired
	private TblSystemDeployScriptTemplateMapper tblSystemDeployScriptTemplateMapper;
	@Autowired
	private TblProjectInfoMapper tblProjectInfoMapper;

	@Autowired
	RedisUtils redisUtils;

	/**
	 * 获取项目信息
	 * @param projectName
	 * @param uid
	 * @param roleCodes
	 * @return
	 */
	@Override
	public List<TaskProjectVO> getProjectListByProjectName(String projectName,String projectCode, Long uid, List<String> roleCodes,int pageNumber, int pageSize,Integer sprintType) {
		Map<String, Object> map = new HashMap<>();
		int start = (pageNumber - 1) * pageSize;
		map.put("start", start);
		map.put("pageSize", pageSize);
		map.put("projectName", projectName);
		map.put("projectCode", projectCode);
		map.put("uid", uid);
		if (roleCodes!=null && roleCodes.contains("XITONGGUANLIYUAN")) {//当前登录用户的角色包含系统管理员  查询所有
			if (sprintType != null && sprintType == 1){   //冲刺页面选择项目排除传统项目
				return   systemInfoMapper.getProjectListBySprint(map);
			}
			return systemInfoMapper.getProjectListByProjectName(map);
		}
		if (sprintType != null && sprintType == 1){   //冲刺页面选择项目排除传统项目
			return   systemInfoMapper.getProjectListBySprint(map);
		}
        List<TaskProjectVO> projectListByProjectName = systemInfoMapper.getProjectListByNoSystem(map);
		return  projectListByProjectName;
	}

	/**
	 *  根据项目id查询系统信息
	 * @param systemInfo
	 * @param uid
	 * @param roleCodes
	 * @param pageNumber
	 * @param pageSize
	 * @param projectId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getAllSystemInfo(TblSystemInfo systemInfo, Long uid, List<String> roleCodes, int pageNumber, int pageSize, Long projectId) {
		try {
			Map<String, Object> map = new HashMap<>();
			int start = (pageNumber - 1) * pageSize;
			map.put("start", start);
			map.put("pageSize", pageSize);
			map.put("systemInfo", systemInfo);
			map.put("uid", uid);
			map.put("projectId", projectId);
			if (roleCodes!=null && roleCodes.contains("XITONGGUANLIYUAN")) {//当前登录用户的角色包含系统管理员  查询所有
				return systemInfoMapper.getAllSystemInfo(map);
			}else {
				return systemInfoMapper.getAllSystemInfoCondition(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 *  查询所有系统信息
	 * @param systemInfo 查询参数
	 * @param roleCodes 角色
	 * @return Map<String, Object>
	 */

	@Override
	@Transactional(readOnly=true)
	public List<Map<String, Object>> getAllSystemInfo(TblSystemInfo systemInfo,Long uid,  List<String> roleCodes,int pageNumber, int pageSize) {
		try {
			Map<String, Object> map = new HashMap<>();
			int start = (pageNumber - 1) * pageSize;
			map.put("start", start);
			map.put("pageSize", pageSize);
			map.put("systemInfo", systemInfo);

			if (roleCodes!=null && roleCodes.contains("XITONGGUANLIYUAN")) {//当前登录用户的角色包含系统管理员  查询所有
				return systemInfoMapper.getAllSystemInfo(map);
			}else {
                map.put("uid", uid);
				return systemInfoMapper.getAllSystemInfoCondition(map);
			}
			//return systemInfoMapper.getAllSystemInfo(map);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//搜索系统信息
	@Override
	public List<Map<String, Object>> getAllSystemInfo2(TblSystemInfo systemInfo, Integer pageNumber, Integer pageSize,String flag,String notIds) {
		Map<String, Object> map = new HashMap<>();
		int start = (pageNumber - 1) * pageSize;
		map.put("start", start);
		map.put("pageSize", pageSize);
		map.put("systemInfo", systemInfo);
		if(flag!=null && flag.equals("1") && notIds!=null && !notIds.equals("")){

			map.put("notIds", notIds);
		}


		//增加查项目管理岗
		List<Map<String, Object>> list=systemInfoMapper.getAllSystemInfo(map);
		if(flag!=null && flag.equals("1"))
		if(list!=null && list.size()>0){

			String redisString = redisUtils.get("TBL_PROJECT_GROUP_USER_USER_POST").toString();
			com.alibaba.fastjson.JSONObject jsonObj = JSON.parseObject(redisString);
			Integer userPost1 = null;
			for (String key : jsonObj.keySet()) {
				if(jsonObj.get(key).toString().equals("项目管理岗")) {
					userPost1 = Integer.valueOf(key);
				}
			}
			for(Map<String, Object> param:list){

				String manangeUserNames="";
				if(param.get("projectId")!=null && !param.get("projectId").toString().equals("")){
					String projectIds=param.get("projectId").toString();
					for(String projectId:projectIds.split(",")){
						List<Long> projectGroupIds = tblProjectInfoMapper.findProjectGroupIdsByProjectId(Long.parseLong(projectId));
						if(projectGroupIds != null && projectGroupIds.size() != 0 && userPost1 != null) {
							HashMap<String, Object> map1 = new HashMap<>();
							map1.put("projectGroupIds", projectGroupIds);
							map1.put("userPost", userPost1);
							List<Long> userId1 = tblProjectInfoMapper.findUserId(map1);
							if(userId1.size()!=0) {
								List<String> userName = tblUserInfoMapper.findUserNameByIds(userId1);
                                for (String uname : userName) {
                                 manangeUserNames = manangeUserNames + uname + ",";
								 }
							}
						}
					}
				}
				if(!manangeUserNames.equals("")){
					manangeUserNames=manangeUserNames.substring(0,manangeUserNames.length()-1);
				}
				param.put("manangeUserNames",manangeUserNames);

			}
		}


		return list;
	}


	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> getAllSystemInfoByBuild(TblSystemInfo systemInfo, int pageNumber, int pageSize) {
		try {
			Map<String, Object> map = new HashMap<>();
			int start = (pageNumber - 1) * pageSize;
			map.put("start", start);
			map.put("pageSize", pageSize);
			map.put("systemInfo", systemInfo);
			return systemInfoMapper.getAllSystemInfoByBuild(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 *  查询一个系统信息
	 * @param id 系统主键
	 * @return TblSystemInfo
	 */
	@Override
	@Transactional(readOnly=true)
	public TblSystemInfo getOneSystemInfo(Long id) {
		try {
			return systemInfoMapper.getOneSystemInfo(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 *  查询系统源码
	 * @param id 系统id
	 * @return TblSystemScm
	 */
	@Override
	@Transactional(readOnly=true)
	public List<TblSystemScm> getBySystemId(Long id) {
		try {
			return systemScmMapper.getBySystemId(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}


	/**
	 *  查询子模块
	 * @param id 系统id
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public List<Map<String, Object>> getSystemModuleBySystemId(Long id) {
		return systemModuleMapper.getSystemModuleBySystemId(id);
	}

	@Override
	@Transactional(readOnly=false)
	public void updateSystemInfo(HttpServletRequest request, TblSystemInfo systemInfo, String systemScm) throws ArrayIndexOutOfBoundsException{
		// 不覆盖系统下微服务的配置 仅修改系统配置
		try {
			systemInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
			systemInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));
			systemInfoMapper.updateSystemInfo(systemInfo);
			/*List<TblSystemScm> systemInfoScms = JsonUtil.fromJson(systemScm,
					JsonUtil.createCollectionType(ArrayList.class, TblSystemScm.class));
			//找出删除的系统配置  查出当前所有的系统配置 与前台传过来的带有id的系统配置比较
			List<TblSystemScm> beforeSystemScm = systemScmMapper.getBySystemId(systemInfo.getId());
			List<Long> beforeIds = CollectionUtil.extractToList(beforeSystemScm, "id");
			List<Long> afterIds = CollectionUtil.extractToList(systemInfoScms, "id");
			List<Long> deleteIds = (List<Long>) CollectionUtil.getDiffent(beforeIds,afterIds);
			if (deleteIds.size()>0) {
				systemScmMapper.deleteBySystemIds(deleteIds);
			}
			for (TblSystemScm scm : systemInfoScms) {			
				String[] scmUrlArr = scm.getScmUrl().split("/");
				String scmRepositoryName = "";
				if (scm.getScmType() == 1) { //源码管理方式（1:GIT，2:SVN）
					scmRepositoryName = scmUrlArr[scmUrlArr.length - 1].replace(".git", "");
				} else {
					scmRepositoryName = scmUrlArr[3];
				}
				scm.setScmRepositoryName(scmRepositoryName);
				scm.setSystemId(systemInfo.getId());
			
				if (scm.getId()!=null) {
					scm.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
					scm.setLastUpdateDate(new Timestamp(new Date().getTime()));
					scm.setStatus(1);
					systemScmMapper.updateByPrimaryKeySelective(scm);
				}else {
					scm.setBuildStatus(1);// 空闲
					scm.setCreateBy(CommonUtil.getCurrentUserId(request));
					scm.setCreateDate(new Timestamp(new Date().getTime()));
					systemScmMapper.insertSystemScm(scm);
					//调用svn接口
//			        TblToolInfo tblToolInfo=tblToolInfoMapper.selectById(scm.getToolId());
//			        String ip=tblToolInfo.getIp();
//					String repositoryName=scm.getScmRepositoryName();
//					SubversionUtils.modifySvnServeConfgFileForRepository(ip, repositoryName);
				}
			}*/
			
		/*	if (systemScm != null && !"".equals(systemScm)) {
				JSONArray jsonArray = JSONArray.fromObject(systemScm);
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					String scmRepositoryName = "";
					TblSystemScm scm = new TblSystemScm();
					if (object.containsKey("environmentType")) {
						scm.setEnvironmentType(Integer.parseInt(object.getString("environmentType")));
					}
					if (object.containsKey("scmType")) {
						scm.setScmType(Integer.parseInt(object.getString("scmType")));
					}
					if (object.containsKey("scmBranch")) {
						scm.setScmBranch(object.getString("scmBranch"));
					}
					if (object.containsKey("scmUrl")) {
						scm.setScmUrl(object.getString("scmUrl"));

						scmRepositoryName = object.getString("scmUrl").toString().split("/")[3];
						scm.setScmRepositoryName(scmRepositoryName);
					}

					if (object.containsKey("toolId")) {
						scm.setToolId(Long.parseLong(object.getString("toolId")));

					}
					if(object.containsKey("submitStatus")) {
						scm.setSubmitStatus(Integer.parseInt(object.getString("submitStatus")));
					}
					if(object.containsKey("systemVersionId")) {
						scm.setSystemVersionId(Long.parseLong(object.getString("systemVersionId")));
					}
					if(object.containsKey("commissioningWindowId")) {
						scm.setCommissioningWindowId(Long.parseLong(object.getString("commissioningWindowId")));
					}

					scm.setSystemId(systemInfo.getId());
					scm.setBuildStatus(1);// 空闲

					if (object.containsKey("id") && !object.getString("id").equals("")) {
						scm.setId(Long.parseLong(object.getString("id")));
						scm.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
						scm.setLastUpdateDate(new Timestamp(new Date().getTime()));
						scm.setStatus(1);
						systemScmMapper.updateByPrimaryKeySelective(scm);
					} else {
						scm.setCreateBy(CommonUtil.getCurrentUserId(request));
						scm.setCreateDate(new Timestamp(new Date().getTime()));
						systemScmMapper.insertSystemScm(scm);
						//调用svn接口
				        TblToolInfo tblToolInfo=tblToolInfoMapper.selectById(scm.getToolId());
				        String ip=tblToolInfo.getIp();
						String repositoryName=scm.getScmRepositoryName();
						SubversionUtils.modifySvnServeConfgFileForRepository(ip, repositoryName);
					}
				}
			}*/
			/*
			 * 如果系统配置被删除 微服务与之配置的配置也应该被删除 查询出 当前系统下system_scm status = 2的系统配置
			 * 根据systemScmId和systemId把微服务与之配置的配置也应该被删除
			 **/
			/*List<TblSystemScm> systemScms = systemScmMapper.findStatus2(systemInfo.getId());
			for (TblSystemScm tblSystemScm : systemScms) {
				tblSystemScm.setSystemId(systemInfo.getId());
				systemModuleScmMapper.deleteBySystemScmId(tblSystemScm);
			}*/

			/*
			 * if ("true".equals(flag)) {// 覆盖系统下微服务的配置
			 * 
			 * // 查询该系统下所有的服务 删除原有服务的配置 添加新的配置 List<Map<String, Object>> modules =
			 * systemModuleMapper.getSystemModuleBySystemId(systemInfo.getId()); for
			 * (Map<String, Object> map2 : modules) { Long moduleId = (Long)
			 * map2.get("id");// 获取服务的id
			 * 
			 * systemModuleScmMapper.delete(moduleId);// 根据服务id删除服务的配置 system_module_scm
			 * 
			 * // 根据systeminfoId查询出systemscmId List<TblSystemScm> systemScms =
			 * systemScmMapper.getBySystemId(systemInfo.getId()); for (TblSystemScm
			 * systemScm2 : systemScms) { TblSystemModuleScm moduleScm = new
			 * TblSystemModuleScm(); moduleScm.setSystemId(systemInfo.getId());
			 * moduleScm.setSystemModuleId(moduleId);
			 * moduleScm.setSystemScmId(systemScm2.getId()); moduleScm.setCreateDate(new
			 * Timestamp(new Date().getTime())); //
			 * System.out.println(moduleScm.toString());
			 * systemModuleScmMapper.insertModuleScm(moduleScm); } }
			 * 
			 * }
			 */
		} catch (ArrayIndexOutOfBoundsException e) {
			throw e;

		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@Override
	@Transactional(readOnly=true)
	public Map<String, Object> getOneSystemModule(Long id) {
		return systemModuleMapper.getOneSystemModule(id);
	}

	//查询系统信息
	@Override
	public JqGridPage<TblSystemInfo> findSystemInfo(JqGridPage<TblSystemInfo> systemInfoJqGridPage,
			TblSystemInfo tblSystemInfo) {
		PageHelper.startPage(systemInfoJqGridPage.getJqGridPrmNames().getPage(),
				systemInfoJqGridPage.getJqGridPrmNames().getRows());
		List<TblSystemInfo> list = systemInfoMapper.selectAll();
		PageInfo<TblSystemInfo> pageInfo = new PageInfo<TblSystemInfo>(list);
		systemInfoJqGridPage.processDataForResponse(pageInfo);
		return systemInfoJqGridPage;
	}

	//查询环境根据
	@Override
	@Transactional(readOnly=true)
	public List<TblDataDic> findEnvironmentType(List<Integer> types) {
		Map<String, Object> map = new HashMap<>();
		// map.put("type", environment_type);
		map.put("typeIds", types);
		return systemInfoMapper.findEnvironmentType(map);
	}

	/**
	 *  更新子模块源码配置表
	 * @param request
	 * @param systemModule 子模块参数
	 * @param systemModuleScms 子模块源码配置信息
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateSystemModuleScm(HttpServletRequest request, TblSystemModule systemModule, String systemModuleScms) {
		Long currentUserId = CommonUtil.getCurrentUserId(request);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		systemModule.setLastUpdateBy(currentUserId);
		systemModule.setLastUpdateDate(timestamp);
		systemModuleMapper.updateByPrimaryKeySelective(systemModule);
		// 把当前服务下所有的配置置为2
//		systemModuleScmMapper.updateNo(systemModule.getId());
		if (systemModuleScms != null && !"".equals(systemModuleScms)) {
			JSONArray jsonArray = JSONArray.fromObject(systemModuleScms);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				TblSystemModuleScm moduleScm = new TblSystemModuleScm();
				moduleScm.setSystemId(systemModule.getSystemId());
				moduleScm.setSystemModuleId(systemModule.getId());
				moduleScm.setSystemScmId(Long.parseLong(object.getString("systemScmId")));
				if (object.containsKey("id")) {
					moduleScm.setId(Long.parseLong(object.getString("id")));
					moduleScm.setLastUpdateBy(currentUserId);
					moduleScm.setLastUpdateDate(timestamp);
					systemModuleScmMapper.updateYes(moduleScm);
				} else {
					moduleScm.setCreateBy(currentUserId);
					moduleScm.setCreateDate(timestamp);
					systemModuleScmMapper.insertModuleScm(moduleScm);
				}
			}

		}

	}

	//查询子模块scm
	@Override
	@Transactional(readOnly=true)
	public List<TblSystemModuleScm> finsModuleScm(Long id) {
		return systemModuleScmMapper.findModuleScm(id);
	}

	@Override
	@Transactional(readOnly=false)
	public void updateByPrimaryKeySelective(TblSystemInfo tblSystemInfo) {

		systemInfoMapper.updateByPrimaryKeySelective(tblSystemInfo);

	}

	@Override
	@Transactional(readOnly=true)
	public TblSystemInfo findById(Long id) {
		// TODO Auto-generated method stub
		return systemInfoMapper.findById(id);
	}

	//新增子模块
	@Override
	@Transactional(readOnly=false)
	public Long insertModule(HttpServletRequest request, TblSystemModule systemModule, String moduleArr) {
		// TODO Auto-generated method stub
		try {
			CommonUtil.setBaseValue(systemModule, request);
			systemModule.setFirstCompileFlag(1);
//			TblSystemInfo systemInfo = systemInfoMapper.getOneSystemInfo(systemModule.getSystemId());
//			systemModule.setGroupId(systemInfo.getGroupId());
			systemModuleMapper.insertModule(systemModule);
			if (moduleArr != null && !"".equals(moduleArr)) {
				JSONArray jsonArray = JSONArray.fromObject(moduleArr);
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					TblSystemModuleScm moduleScm = new TblSystemModuleScm();
					moduleScm.setSystemId(systemModule.getSystemId());

					moduleScm.setSystemModuleId(systemModule.getId());
					moduleScm.setSystemScmId(Long.parseLong(object.getString("systemScmId")));

					moduleScm.setCreateBy(CommonUtil.getCurrentUserId(request));
					moduleScm.setCreateDate(new Timestamp(new Date().getTime()));
					systemModuleScmMapper.insertModuleScm(moduleScm);

				}

			}
		} catch (Exception e) {
			
			e.printStackTrace();
			throw e;
		}
		return systemModule.getId();
	}

	//删除系统
	@Override
	@Transactional(readOnly=false)
	public void deleteSystem(HttpServletRequest request, TblSystemInfo tblSystemInfo, List<TblSystemModule> moduleList) {
		Long userId = CommonUtil.getCurrentUserId(request);
		Timestamp stamp = new Timestamp(new Date().getTime());
		
		tblSystemInfo.setStatus(2);
		tblSystemInfo.setLastUpdateBy(userId);
		tblSystemInfo.setLastUpdateDate(stamp);
		systemInfoMapper.updateByPrimaryKeySelective(tblSystemInfo);
	}
	
	
	@Override
	@Transactional(readOnly=false)
	public void deleteModule(HttpServletRequest request, TblSystemModule systemModule) {
		try {
			Long userId = CommonUtil.getCurrentUserId(request);
			Timestamp stamp = new Timestamp(new Date().getTime());
			
			systemModule.setStatus(2);
			systemModule.setLastUpdateBy(userId);
			systemModule.setLastUpdateDate(stamp);
			systemModuleMapper.updateByPrimaryKeySelective(systemModule);
			
			TblSystemModuleScm moduleScm = new TblSystemModuleScm();
			moduleScm.setStatus(2);
			moduleScm.setLastUpdateBy(userId);
			moduleScm.setLastUpdateDate(stamp);
			
			TblSystemModuleScm moduleScmWhere = new TblSystemModuleScm();
			moduleScmWhere.setSystemModuleId(systemModule.getId());
			systemModuleScmMapper.update(moduleScm, new EntityWrapper<TblSystemModuleScm>(moduleScmWhere));
			
			/***********如果TblSystemScm关联的TblSystemModuleScm为空，也删除TblSystemScm**********/
			Map para1 = new HashMap();
			para1.put("SYSTEM_MODULE_ID", systemModule.getId());
			List<TblSystemModuleScm> moduleScmList = systemModuleScmMapper.selectByMap(para1);
			Map para2 = new HashMap();
			for (TblSystemModuleScm tblSystemModuleScm : moduleScmList) {
				TblSystemScm scm = systemScmMapper.selectByPrimaryKey(tblSystemModuleScm.getSystemScmId());
				if (scm != null && scm.getStatus() == 1) {
					para2.put("SYSTEM_SCM_ID", scm.getId());
					para2.put("STATUS", 1);
					List<TblSystemModuleScm> moduleScmTempList = systemModuleScmMapper.selectByMap(para2);
					if (moduleScmTempList == null || moduleScmTempList.size() == 0) {
						scm.setStatus(2);
						scm.setLastUpdateBy(userId);
						scm.setLastUpdateDate(stamp);
						systemScmMapper.updateByPrimaryKeySelective(scm);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	@Transactional(readOnly=true)
	public List<TblSystemInfo> findAll() {
		// TODO Auto-generated method stub
		return systemInfoMapper.selectAll();
	}

	/**
	 * 根据systemCode 查找表中是否有存在的systemCode
	 * 
	 * @param systemCode 系统编码
	 * @return TblSystemInfo对象
	 */
	@Override
	@Transactional(readOnly=true)
	public TblSystemInfo getSystemByCode(String systemCode) {
		return systemInfoMapper.getSystemByCode(systemCode);
	}

	/**
	 * 添加对象
	 * 
	 * @param systemInfo
	 */
	@Override
	@DataSource(name = "tmpDataSource")
	@Transactional(readOnly=false,rollbackFor=Exception.class,propagation=Propagation.REQUIRES_NEW)
	public void insertSystem(TblSystemInfo systemInfo)  throws Exception {
		systemInfoMapper.insertSystem(systemInfo);
		SpringContextHolder.getBean(ISystemInfoService.class).insertTmpSystem(systemInfo);
	}

	/**
	 * 根据id修改实体类
	 * 
	 * @param systemInfo
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateSystemById(TblSystemInfo systemInfo) {
		systemInfoMapper.updateSystemById(systemInfo);
	}

	@Override
	@Transactional(readOnly=true)
	public List<TblToolInfo> getToolByType() {
		// TODO Auto-generated method stub
		return tblToolInfoMapper.selectsSvnGitBytoolType();
	}

	@Override
	@Transactional(readOnly=false)
	public void updateSystemInfoManual(HttpServletRequest request, TblSystemInfo systemInfo, String systemJenkins) {

		if (systemJenkins != null && !"".equals(systemJenkins)) {

			JSONArray jsonArray = JSONArray.fromObject(systemJenkins);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				TblSystemJenkins tblSystemJenkins = new TblSystemJenkins();
				if (object.containsKey("id")) {

				} else {

					if (object.containsKey("toolId")) {
						tblSystemJenkins.setToolId(Long.parseLong(object.getString("toolId")));

					}
					if (object.containsKey("jobName")) {
						tblSystemJenkins.setJobName(object.getString("jobName"));

					}
					if (object.containsKey("jobPath")) {
						tblSystemJenkins.setJobPath(object.getString("jobPath"));

					}
					if (object.containsKey("environmentType") && StringUtil.isNotEmpty(object.getString("environmentType"))) {
						tblSystemJenkins.setEnvironmentType(Integer.valueOf(object.getString("environmentType")));
					}
					if (object.containsKey("jobType")) {
						tblSystemJenkins.setJobType(Integer.parseInt(object.getString("jobType")));
						if(Integer.parseInt(object.getString("jobType"))==1){
							tblSystemJenkins.setBuildStatus(1);
							
						}else {
							tblSystemJenkins.setDeployStatus(1);
						}

					}else {
						tblSystemJenkins.setBuildStatus(1);
						tblSystemJenkins.setDeployStatus(1);
					}
					try {
						tblSystemJenkins.setCreateType(2);// 手动
//						tblSystemJenkins.setBuildStatus(1);
//						tblSystemJenkins.setDeployStatus(1);
						tblSystemJenkins.setStatus(1);
						tblSystemJenkins.setSystemId(systemInfo.getId());
						tblSystemJenkins.setCreateBy(CommonUtil.getCurrentUserId(request));
						tblSystemJenkins.setCreateDate(new Timestamp(new Date().getTime()));
//						TblToolInfo jenkinstool=tblToolInfoMapper.selectById(tblSystemJenkins.getToolId());
//						String jobCron=iJenkinsBuildService.getJobCron(jenkinstool, tblSystemJenkins.getJobName());
//						tblSystemJenkins.setJobCron(jobCron);
						tblSystemJenkinsMapper.insertNew(tblSystemJenkins);
					} catch (Exception e) {
						
						e.printStackTrace();
					}
				}

			}
		}
	}

	@Override
    @Transactional(readOnly=false)
	public int deleteSystemJenkinsManual(String id) {
		try {
			int flag=0; //0默认不做任何操作 1调用定时构建任务清除 2调用定时部署任务清除
			TblSystemJenkins tblSystemJenkins = tblSystemJenkinsMapper.selectById(id);
			
			
			
			if(tblSystemJenkins.getJobCron()!=null &&  !tblSystemJenkins.getJobCron().equals("") && !tblSystemJenkins.getJobCron().equals(" ")) {
				//tblSystemJenkins.setJobCron("");
				if(tblSystemJenkins.getJobType()==1) {
				flag=1;
				}else {
				flag=2;	
				}
				
			}
			tblSystemJenkins.setStatus(2);// 删除jen
			tblSystemJenkinsMapper.updateById(tblSystemJenkins);
			return flag;
		} catch (Exception e) {
			
			e.printStackTrace();
			throw e;
		}

	}

	@Override
	@Transactional(readOnly=true)
	public List<TblSystemJenkins> getsystemJenkinsManual(Map<String, Object> columnMap) {

		List<TblSystemJenkins> jenkinsList = tblSystemJenkinsMapper.selectByMap(columnMap);

		return jenkinsList;

	}

	@Override
	@Transactional(readOnly=true)
	public List<TblToolInfo> getToolinfoType(Map<String, Object> map) {
		return tblToolInfoMapper.selectByMap(map);

	}

	@Override
	public String judgeJobNames(String systemJenkins,long systemId) {
		StringBuffer existJobName=new StringBuffer("");
		if (systemJenkins != null && !"".equals(systemJenkins)) {

			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("system_id",systemId);
			paramMap.put("status",1);
			List<TblSystemJenkins> oldJenkinsList = tblSystemJenkinsMapper.selectByMap(paramMap);
			JSONArray jsonArray = JSONArray.fromObject(systemJenkins);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				String jobName = object.getString("jobName");
				String jobType = object.getString("jobType");
				String jobPath = object.getString("jobPath");
				if (object.containsKey("id")) {
				} else {
					for (TblSystemJenkins oldJenkins : oldJenkinsList) {
						if (oldJenkins.getJobName().equals(jobName) && 
								oldJenkins.getJobType().equals(jobType) && 
								oldJenkins.getJobPath().equals(jobPath)) {
							existJobName.append(jobPath + jobName+",");
							break;
						}
					}
				}

			}
		}
		if(!existJobName.toString().equals("")) {
			existJobName=existJobName.deleteCharAt(existJobName.length()-1);
			
		}
		return existJobName.toString();
	}

	/**
	 * 获取投产窗口(过去的一条和未来的所有投产)
	 */
	@Override
	@Transactional(readOnly=true)
	public List<TblCommissioningWindow> getAllTblCommissioningWindow() {
		TblCommissioningWindow beforeTime = commissioningWindowMapper.selectBeforeTime();
		List<TblCommissioningWindow> afterTime = commissioningWindowMapper.selectAfterTime();
		if(beforeTime != null) {
			afterTime.add(beforeTime);
		}
		return afterTime;
	}
	
	/**
	 * 根据条件查询部署配置
	 */
	@Override
	@Transactional(readOnly=true)
	public Map<String, Object> getDeployInfoByCon(Long systemId,Integer environmentType,Long systemModuleId){
		Map<String, Object> map = new HashMap<>();
		TblSystemDeploy systemDeploy = new TblSystemDeploy();
		systemDeploy.setSystemId(systemId);
		systemDeploy.setEnvironmentType(environmentType);
		systemDeploy.setSystemModuleId(systemModuleId);
		List<TblSystemDeploy> list = systemDeployMapper.selectByCon(systemDeploy);
		for (TblSystemDeploy tblSystemDeploy : list) {
			String serverIds = tblSystemDeploy.getServerIds();
			String[] serverIdArr = serverIds.split(",");
			List<TblServerInfo> serverInfos = tblServerInfoMapper.selectByManyId(serverIdArr); 
			tblSystemDeploy.setServerIds(serverInfos == null?"":JSON.toJSONString(serverInfos));
		}
		if(list != null && !list.isEmpty()) {
			List<TblSystemDeployScript> scripts = tblSystemDeployScriptMapper.selectByDeployId(list.get(0).getId());
			map.put("deploy", list.get(0));
			map.put("deployScriptList", scripts);
		}else {
			TblSystemDeploy systemDeploy2 = new TblSystemDeploy();
			systemDeploy2.setServerIds("");
			map.put("deploy", new TblSystemDeploy());
			map.put("deployScriptList", new ArrayList<>());
		}
		return map;
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<TblSystemDeploy> getDeployList(Long systemId, Integer architectureType) {
		TblSystemDeploy systemDeploy = new TblSystemDeploy();
		systemDeploy.setSystemId(systemId);
		
		EntityWrapper<TblSystemDeploy> wrapper = new EntityWrapper<TblSystemDeploy>();
		wrapper.eq("SYSTEM_ID", systemId);
		if (architectureType == 1) {//多模块
			wrapper.isNotNull("SYSTEM_MODULE_ID");
		} else {
			wrapper.isNull("SYSTEM_MODULE_ID");
		}
		wrapper.eq("STATUS", 1);
		
		List<TblSystemDeploy> list = systemDeployMapper.selectList(wrapper);
		return list;
	}

	/**
	 * 添加或者修改部署配置
	 * @throws Exception 
	 */
	@Override
	@Transactional(readOnly=false)
	public void addOrUpdateDeploy(String deployStr,String deployScriptStr ,HttpServletRequest request) throws Exception {
		try {
			if(deployStr != null && !deployStr.equals("")) {
				TblSystemDeploy systemDeploy = JSON.parseObject(deployStr, TblSystemDeploy.class);
				String serverIds = systemDeploy.getServerIds();
				List<Map<String, Object>> serverInfos = JSON.parseObject(serverIds, List.class);
				List<TblSystemDeployScript> systemDeployScripts = JSON.parseArray(deployScriptStr, TblSystemDeployScript.class);
				String serverStr = "";
				for (Map<String, Object> map : serverInfos) {
					serverStr += map.get("id")+",";
				}
				systemDeploy.setServerIds(serverStr.substring(0, serverStr.length()-1));
				if(systemDeploy.getId() != null) {
					//判断模板是否改变
//					TblSystemDeploy oldSystemDeploy= systemDeployMapper.selectById(systemDeploy.getId());
//					if(systemDeploy.getTemplateType()!=null && oldSystemDeploy.getTemplateType()!=systemDeploy.getTemplateType()){
//						oldSystemDeploy.setTemplateType(systemDeploy.getTemplateType());
//						oldSystemDeploy.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
//						oldSystemDeploy.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
//						systemDeployMapper.updateById(oldSystemDeploy);
//					}

					//修改部署配置
					//找出删除的部署脚本配置  查出当前所有的部署脚本配置 与前台传过来的带有id的部署脚本配置比较
					List<TblSystemDeployScript> afterScriptList = tblSystemDeployScriptMapper.selectByDeployId(systemDeploy.getId());
					List<Long> beforIds = CollectionUtil.extractToList(systemDeployScripts, "id");
					List<Long> afterIds = CollectionUtil.extractToList(afterScriptList, "id");
					List<Long> diffent = (List<Long>)CollectionUtil.getDiffent(beforIds, afterIds);
					diffent.removeAll(Collections.singleton(null));
					if(diffent.size() > 0) {
						tblSystemDeployScriptMapper.deleteByBatchIds(diffent);
					}
					for(TblSystemDeployScript systemDeployScript : systemDeployScripts) {
						if(systemDeployScript.getId() != null) {
							systemDeployScript.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
							systemDeployScript.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
							tblSystemDeployScriptMapper.updateByPrimaryKeySelective(systemDeployScript);
						}else {
							CommonUtil.setBaseValue(systemDeployScript, request);
							tblSystemDeployScriptMapper.insertScript(systemDeployScript);
						}
					}
					
					systemDeploy.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
					systemDeploy.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
					TblSystemDeploy systemDeployOld = systemDeployMapper.selectByPrimaryKey(systemDeploy.getId());
					BeanUtil.copyProperties(systemDeploy, systemDeployOld, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));//忽略空值
					systemDeployOld.setDeploySequence(systemDeploy.getDeploySequence());//为空时也能修改
					systemDeployMapper.updateAllColumnById(systemDeployOld);
				}else {   //新增部署配置
					CommonUtil.setBaseValue(systemDeploy, request);
					systemDeployMapper.insert(systemDeploy);
					for (TblSystemDeployScript tblSystemDeployScript : systemDeployScripts) {
						CommonUtil.setBaseValue(tblSystemDeployScript, request);
						tblSystemDeployScript.setSystemDeployId(systemDeploy.getId());
						tblSystemDeployScriptMapper.insertScript(tblSystemDeployScript);
					}
				}
			}
		}catch(Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly=false)
	public void addOrUpdateAutoTest(Long systemId, List<TblSystemAutomaticTestConfig> newAutoTestList,String testType, HttpServletRequest request) throws Exception {
		
		List<TblSystemAutomaticTestConfig> oldAutoTestList = this.selectAutoTestConfigBySystemId(systemId,testType);
		for (int i = 0; i < newAutoTestList.size(); i++) {
			TblSystemAutomaticTestConfig newAutoTest = newAutoTestList.get(i);
			for (int j = 0; j < oldAutoTestList.size(); j++) {
				TblSystemAutomaticTestConfig oldAutoTest = oldAutoTestList.get(j);
				if (newAutoTest.getEnvironmentType() == oldAutoTest.getEnvironmentType().intValue()) {
					if (StringUtil.isNotEmpty(newAutoTest.getTestScene()) && StringUtil.isNotEmpty(oldAutoTest.getTestScene())
							&& newAutoTest.getTestScene().equals(oldAutoTest.getTestScene())) {
						if (newAutoTest.getSystemModuleId() != null && oldAutoTest.getSystemModuleId() != null
								&& newAutoTest.getSystemModuleId() == oldAutoTest.getSystemModuleId().longValue()) {
							newAutoTestList.remove(i);
							i--;
							oldAutoTestList.remove(j);
							j--;
							break;
						}
						if (newAutoTest.getSystemModuleId() == null && oldAutoTest.getSystemModuleId() == null) {
							newAutoTestList.remove(i);
							i--;
							oldAutoTestList.remove(j);
							j--;
							break;
						}
					}
					
				}
			}
		}
		
		for (int i = 0; i < oldAutoTestList.size(); i++) {
			TblSystemAutomaticTestConfig autoTest = oldAutoTestList.get(i);
			autoTest.setStatus(2);
			autoTest.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
			autoTest.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
			tblSystemAutomaticTestConfigMapper.updateByPrimaryKeySelective(autoTest);
		}
		for (int i = 0; i < newAutoTestList.size(); i++) {
			TblSystemAutomaticTestConfig autoTest = newAutoTestList.get(i);
			CommonUtil.setBaseValue(autoTest, request);
			tblSystemAutomaticTestConfigMapper.insertAutoTest(autoTest);
		}
	}

	/**
	 * 查询数据字典返回环境类型
	 */
	@Override
	public List<Map<String, Object>> selectDictEnv() {
		List<Map<String, Object>> list = new ArrayList<>();
			Map<String, Object> dictMap = JsonUtil.fromJson((String) redisUtils.get("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE"), Map.class);
			for(Map.Entry<String, Object> entry : dictMap.entrySet()) {
				Map<String, Object> map = new HashMap<>();
				map.put("envType", entry.getKey());
				map.put("envName", entry.getValue()); 
				list.add(map);
			}
		return list;
	}

	@Override
	public List<TblSystemModuleScm> getSystemModuleByid(Map< String, Object> map) {
		
		return systemModuleScmMapper.selectByMap(map);
	}

	@Override
	public String getUserName(String userId) {
		return systemInfoMapper.getUserName(userId);
	}

	@Override
	public List<Long> findSystemIdByUserId(Long id) {
		return systemInfoMapper.findSystemIdByUserId(id);
	}

	/**
	 * 批量获取用户姓名
	 */
	@Override
	public String getBatchUserName(String ids) {
		String userName = "";
		if(ids != null && !ids.isEmpty()) {
			String[] codeReviewUserIds = ids.split(",");
			List<String> list = Arrays.asList(codeReviewUserIds);
			userName = systemInfoMapper.getBatchUserName(list);
		}
		return userName;
	}
	/**
	 *  查询系统环境
	 * @author weiji
	 * @param systemId 系统、id
	 * @return List<String>
	 */
	@Override
	public List<String> getEnvTypes(Long systemId) {
		return systemScmMapper.getEnvTypes(systemId);
	}
	@Override
	public String handleUpdateScm(String systemScm, String newSystemScm, List<String> deleteScmid) {
		if(newSystemScm!=null && !newSystemScm.equals("")) {
		List<TblSystemScm> newSystemScms = JsonUtil.fromJson(newSystemScm,
				JsonUtil.createCollectionType(ArrayList.class, TblSystemScm.class));
		List<TblSystemScm> systemScmsAdd=new ArrayList<>();
		for(TblSystemScm tblSystemScm:newSystemScms) {
			//deleteScmid.add(String.valueOf(tblSystemScm.getId()));
			tblSystemScm.setId(null);
			systemScmsAdd.add(tblSystemScm);
		}
		if(systemScmsAdd.size()>0) {
			List<TblSystemScm> systemScmOld = JsonUtil.fromJson(systemScm,
					JsonUtil.createCollectionType(ArrayList.class, TblSystemScm.class));
			systemScmOld.addAll(systemScmsAdd);
			systemScm=JsonUtil.toJson(systemScmOld);
		}
		}
		return systemScm;
	}
	
	//配置环境
	@Override
	public void configEnvironment(Long id, String envType, HttpServletRequest request) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<>();
		map.put("id", id);
		map.put("envType", envType);
		map.put("currentUser", CommonUtil.getCurrentUserId(request));
		map.put("currentTime", new Timestamp(new Date().getTime()));
		systemInfoMapper.configEnvironment(map);
	}
	
	//把系统源码配置表中环境类型不属于环境配置的置为空
//	@Override
//	public void updateSystemScm(Long id, String envType, HttpServletRequest request) {
//		// TODO Auto-generated method stub
//		List<Long> envTypes = systemScmMapper.getEnvTypesBySyetemId(id);
//		Long[] types = (Long[]) ConvertUtils.convert (envType.split(","),Long.class);
//		List<Long> list = Arrays.asList(types);
//		List<Long> envs = new ArrayList<>();
//		if(envTypes.size()!=0) {
//			for (Long type1 : envTypes) {
//				for (Long type2 : list) {
//					if(type1.longValue()==type2.longValue()) {
//						envs.add(type1);
//					}
//				}
//			}
//			envTypes.removeAll(envs);
//		Map<String,Object> map = new HashMap<>();
//		map.put("id", id);
//		map.put("envTypes", envTypes);
//		map.put("currentUser", CommonUtil.getCurrentUserId(request));
//		map.put("currentTime", new Timestamp(new Date().getTime()));
//		systemScmMapper.updateSystemScm(map);
//		}
//	}
	
	//获取该系统所配置的环境
	@Override
	public List<Map<String, Object>> selectDictEnvBySystemId(Long systemId) {
		// TODO Auto-generated method stub
		String environment = systemInfoMapper.getEnvBySystemId(systemId);
		List<Map<String, Object>> list = new ArrayList<>();
		if(environment!=null) {
		String[] arr = environment.split(",");
		Map<String, Object> dictMap = JsonUtil.fromJson((String) redisUtils.get("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE"), Map.class);
		for (String s : arr) {
//		    Object env = dictMap.get(s);
		    Map<String, Object> map = new HashMap<>();
			map.put("envType", s);
			map.put("envName", dictMap.get(s)); 
			list.add(map);
		}
		}
		return list;
	}
	
	@Override
	public List<TblSystemAutomaticTestConfig> selectAutoTestConfigBySystemId(Long systemId,String testType) {
		Map<String, Object> columnMap = new HashMap<String, Object>();
		columnMap.put("SYSTEM_ID", systemId);
		columnMap.put("STATUS", 1);
		columnMap.put("test_type",testType);
		return tblSystemAutomaticTestConfigMapper.selectByMap(columnMap);
	}
	
	@Override
	public List<TblDataDic> selectDictEnvBySystemId2(Long id) {
		// TODO Auto-generated method stub
		String environment = systemInfoMapper.getEnvBySystemId(id);
		List<TblDataDic> dics = new ArrayList<>();
		if(environment!=null) {
			String[] arr = environment.split(",");
			Map<String, Object> dictMap = JsonUtil.fromJson((String) redisUtils.get("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE"), Map.class);
			for (String s : arr) {
				TblDataDic dic = new TblDataDic();
				dic.setValueCode(s);
				dic.setValueName(dictMap.get(s).toString());
				dics.add(dic);
			}
		}
		return dics;
	}
	
	@Override
	public List<Long> findEnvIds(Long id) {
		// TODO Auto-generated method stub
		String envBySystemId = systemInfoMapper.getEnvBySystemId(id);
		String[] arr = envBySystemId.split(",");
		Long[] arr2 = (Long[])ConvertUtils.convert(arr,Long.class);
		List<Long> list=Arrays.asList(arr2);
		return list;
	}
	@Override
	public List<TblSystemInfo> findSystemByProject(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return systemInfoMapper.findSystemByProject(CommonUtil.getCurrentUserId(request));
	}

	@Override
	public List<TblSystemInfo> getSyetemByNameOrCode(String systemName, String systemCode) {
		List<TblSystemInfo> systemInfos=systemInfoMapper.getSyetemByNameOrCode(systemName,systemCode);
		return systemInfos;
	}

	@Override
	@DataSource(name = "itmpDataSource")
	@Transactional(readOnly=false,rollbackFor=Exception.class,propagation = Propagation.REQUIRED)
	public void insertItmpSystem(TblSystemInfo systemInfo,HttpServletRequest request) throws Exception {
		CommonUtil.setBaseValue(systemInfo,request);
		systemInfoMapper.insertItmpSystem(systemInfo);
		SpringContextHolder.getBean(ISystemInfoService.class).insertTmpSystem(systemInfo);
	}

	@Override
	@DataSource(name = "tmpDataSource")
	@Transactional(readOnly=false,rollbackFor=Exception.class,propagation=Propagation.REQUIRES_NEW)
	public void insertTmpSystem(TblSystemInfo systemInfo){
		systemInfoMapper.insertTmpSystem(systemInfo);
	}

	@Override
	@DataSource(name = "tmpDataSource")
	@Transactional(readOnly=false)
	public void updateTmpSystemInfo(TblSystemInfo systemInfo,int createType){
		if(createType==1){
			systemInfoMapper.updateSystemById(systemInfo);
		} else {
			systemInfoMapper.updateTmpSystem(systemInfo);
		}

	}

	@Override
	public Map<String,Object> getScmBySystemId(Long systemId,Integer architectureType) {
		Map<String,Object> map = new HashMap<>();
		if(architectureType==1){
			List<TblSystemModuleScm> moduleScms = systemModuleScmMapper.getModuleScmBySystemId(systemId);
			for (TblSystemModuleScm moduleScm:moduleScms){
				moduleScm.setIds(moduleScm.getIdsString().split(","));
				moduleScm.setModuleIds(moduleScm.getModuleIdsString().split(","));
			}
			map.put("list",moduleScms);
		}else{
			List<TblSystemScm> scm = systemScmMapper.getScmBySystemId(systemId);
			map.put("list",scm);
		}
		List<TblSystemScmRepository> scmRepositorys =
				scmRepositoryMapper.findScmRepositoryBySystemId(systemId);
		map.put("scmRepositorys",scmRepositorys);
		return map;
	}

	//删除系统源码配置
	@Override
	public void delScm(Long id,HttpServletRequest request){
		TblSystemScm scm = new TblSystemScm();
		scm.setId(id);
		scm.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		scm.setLastUpdateDate(new Timestamp(new Date().getTime()));
		systemScmMapper.deleteScmById(scm);
	}

	//删除子模块源码
	@Override
	public String delModuleScm(TblSystemModuleScm modelScm,HttpServletRequest request){

	    String delScmId="";
        String idsString = modelScm.getIdsString();
        String[] ids = idsString.split(",");
        Integer count = systemModuleScmMapper.findModuleScmByEnvironmentType(modelScm);
        if(count <= ids.length){
            TblSystemScm scm = systemScmMapper.
					findScmDoesItExist(modelScm.getSystemId(),modelScm.getEnvironmentType());
            scm.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
            scm.setLastUpdateDate(new Timestamp(new Date().getTime()));
            systemScmMapper.deleteScmById(scm);
            delScmId=scm.getId().toString();
        }
		for (String id:ids){
			TblSystemModuleScm moduleScm = new TblSystemModuleScm();
			moduleScm.setId(Long.valueOf(id));
			moduleScm.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
			moduleScm.setLastUpdateDate(new Timestamp(new Date().getTime()));
			systemModuleScmMapper.deleteModuleScmById(moduleScm);
		}
		return delScmId;
	}

	//更新源码
	@Override
	public void updateScm(List<TblSystemScm> scmList,HttpServletRequest request) {
		for (TblSystemScm sScm:scmList){
			if (StringUtil.isEmpty(sScm.getScmBranch())) {//分支为空，默认值为点.
				sScm.setScmBranch(".");
			}
			TblSystemScmRepository scmRepository=scmRepositoryMapper.findScmRepositoryById(sScm.getSystemRepositoryId());
			TblSystemScm systemScm = systemScmMapper.findScmDoesItExist(sScm.getSystemId(),sScm.getEnvironmentType());
			if(systemScm==null){
				if(sScm.getId()!=null){
					systemScmMapper.deleteScmById(sScm);
				}
				sScm.setToolId(scmRepository.getToolId());
				sScm.setScmType(scmRepository.getScmType());
				sScm.setBuildStatus(1);
				sScm.setDeployStatus(1);
				CommonUtil.setBaseValue(sScm,request);
				systemScmMapper.insertScm(sScm);
			}else{
				sScm.setId(systemScm.getId());
				sScm.setToolId(scmRepository.getToolId());
				sScm.setScmType(scmRepository.getScmType());
				sScm.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
				sScm.setLastUpdateDate(new Timestamp(new Date().getTime()));
				systemScmMapper.updateScm(sScm);
			}
		}
	}

	@Override
	public void updateModelScm(List<TblSystemModuleScm> modelScmList,HttpServletRequest request) {
		List<Integer> delScmEnv = delScmEnv(modelScmList,modelScmList.get(0).getSystemId());
		List<Long> delModule = deletModuleScmId(modelScmList,modelScmList.get(0).getSystemId());
        for (Integer scmEnv:delScmEnv){
            TblSystemScm scm = systemScmMapper.
					findScmDoesItExist(modelScmList.get(0).getSystemId(),scmEnv);
			scm.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
			scm.setLastUpdateDate(new Timestamp(new Date().getTime()));
            systemScmMapper.deleteScmById(scm);
        }
		for (Long id:delModule){
			TblSystemModuleScm moduleScm = new TblSystemModuleScm();
			moduleScm.setId(id);
			moduleScm.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
			moduleScm.setLastUpdateDate(new Timestamp(new Date().getTime()));
			systemModuleScmMapper.deleteModuleScmById(moduleScm);
		}
        
		for (TblSystemModuleScm modelScm:modelScmList){
			if (StringUtil.isEmpty(modelScm.getScmBranch())) {//分支为空，默认值为点.
				modelScm.setScmBranch(".");
			}
			TblSystemScmRepository scmRepository=scmRepositoryMapper.findScmRepositoryById(modelScm.getSystemRepositoryId());
			TblSystemModuleScm systemModuleScm = systemModuleScmMapper.findModuleScmDoesItExist(modelScm);
			if(systemModuleScm== null){
				TblSystemScm systemScm = systemScmMapper.findScmDoesItExist(modelScm.getSystemId(),modelScm.getEnvironmentType());
				if(systemScm==null){
					TblSystemScm scm = new TblSystemScm();
					scm.setSystemId(modelScm.getSystemId());
					scm.setEnvironmentType(modelScm.getEnvironmentType());
					scm.setBuildStatus(1);
					scm.setDeployStatus(1);
					CommonUtil.setBaseValue(scm,request);
					systemScmMapper.insertScm(scm);
					modelScm.setSystemScmId(scm.getId());
					modelScm.setScmType(scmRepository.getScmType());
					modelScm.setToolId(scmRepository.getToolId());
				}else {
					modelScm.setSystemScmId(systemScm.getId());
					modelScm.setScmType(scmRepository.getScmType());
					modelScm.setToolId(scmRepository.getToolId());
				}
				CommonUtil.setBaseValue(modelScm,request);
				systemModuleScmMapper.insertModuleScm1(modelScm);
			}else{
				modelScm.setId(systemModuleScm.getId());
				modelScm.setScmType(scmRepository.getScmType());
				modelScm.setToolId(scmRepository.getToolId());
				modelScm.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
				modelScm.setLastUpdateDate(new Timestamp(new Date().getTime()));
				systemModuleScmMapper.updateModuleScm1(modelScm);
			}
		}
	}

	//获取部署脚本
	@Override
	public List<TblSystemDeployScriptTemplate> getDeployScripTemplateByType(String templateType) {
		Map<String,Object> param=new HashMap<>();
		param.put("TEMPLATE_TYPE",templateType);
		param.put("STATUS",1);
		return  tblSystemDeployScriptTemplateMapper.selectByMap(param);

	}

	/**
	 *  增加主系统
	 * @param tblTopSystemInfo
	 * @param map
	 * @param request
	 * @return
	 */
	@Override
	public Map<String, Object> addTopSystem(TblTopSystemInfo tblTopSystemInfo,Map<String, Object> map, HttpServletRequest request) {
		String systemCode= tblTopSystemInfo.getSystemCode();
		Map<String, Object> param = new HashMap<>();
		param.put("status",1);
		param.put("SYSTEM_CODE",systemCode);
		List<TblTopSystemInfo> tblTopSystemInfos= tblTopSystemInfoMapper.selectByMap(param);
		List<TblSystemInfo> tblSystemInfos= systemInfoMapper.selectByMap(param);

		if((tblTopSystemInfos!=null && tblTopSystemInfos.size()>0) || (tblSystemInfos!=null && tblSystemInfos.size()>0)){
			map.put("status", Constants.ITMP_RETURN_FAILURE);
			map.put("message","已存在相同系统编码!");
		}else{
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			tblTopSystemInfo.setStatus(1);
			tblTopSystemInfo.setCreateBy(CommonUtil.getCurrentUserId(request));
			tblTopSystemInfo.setCreateDate(timestamp);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
			map.put("message","新增成功!");

		}
		return map;


	}


	/**
	 * 增加主系统
	 * @param tblTopSystemInfo
	 * @param request
	 * @return
	 */
	@Override
	public TblTopSystemInfo addTopSystem(TblTopSystemInfo tblTopSystemInfo, HttpServletRequest request) {

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			tblTopSystemInfo.setStatus(1);
			tblTopSystemInfo.setCreateBy(CommonUtil.getCurrentUserId(request));
			tblTopSystemInfo.setCreateDate(timestamp);
			tblTopSystemInfoMapper.insertNew(tblTopSystemInfo);

		return tblTopSystemInfo;


	}

	/**
	 *  根据编码获取主系统
	 * @param systemCode 主系统编码
	 * @return
	 */
	@Override
	public List<TblTopSystemInfo> getTopSysteminfosByCode(String  systemCode) {
	return 	tblTopSystemInfoMapper.getTopSysteminfosByCode(systemCode);
	}

	@Override
	public List<TblSystemModule> getModuleByMap(Map<String, Object> param) {
		return  systemModuleMapper.selectByMap(param);
	}


	/**
	 *  查询系统
	 * @param systemInfo
	 * @param uid
	 * @param roleCodes
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public List<String> getAllSystemInfoByTop(TblSystemInfo systemInfo, Long uid, List<String> roleCodes, Integer pageNumber, Integer pageSize) {

			try {
				Map<String, Object> map = new HashMap<>();
				int start = (pageNumber - 1) * pageSize;
				map.put("start", start);
				map.put("pageSize", pageSize);
				map.put("systemInfo", systemInfo);

				if (roleCodes!=null && roleCodes.contains("XITONGGUANLIYUAN")) {//当前登录用户的角色包含系统管理员  查询所有
					return systemInfoMapper.getAllSystemInfoTopPage(map);
				}else {
                    map.put("uid", uid);
					return systemInfoMapper.getAllSystemInfoConditionTopPage((map));
				}

			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}



	}

	/**
	 *  查询主系统根据主系统编码
	 * @param systemCode
	 * @param request
	 * @return
	 */
	@Override
	public List<TblTopSystemInfo> getTopSystemInfoByCode(String systemCode,HttpServletRequest request) {
		Map<String,Object> map=new HashMap<>();
		map.put("status",1);
		map.put("system_code",systemCode);
		return  tblTopSystemInfoMapper.selectByMap(map);
	}

	private List<Integer> delScmEnv(List<TblSystemModuleScm> modelScmList, long systemid) {
        List<Integer> flag = new ArrayList<>();
        List<Integer> beforFlag = new ArrayList<>();
        List<TblSystemScm> beforScmlist = systemScmMapper.getScmBySystemId(systemid);
        for (TblSystemScm scm : beforScmlist) {
            beforFlag.add(scm.getEnvironmentType());
        }
        for (Integer env : beforFlag) {
            for (TblSystemModuleScm moduleScm : modelScmList) {
                if(env ==moduleScm.getEnvironmentType()){
                    flag.add(moduleScm.getEnvironmentType());
                }
            }
        }
        HashSet h1 = new HashSet(flag);
        flag.clear();
        flag.addAll(h1);
        beforFlag.removeAll(flag);
        return beforFlag;
    }
	private List<Long> deletModuleScmId(List<TblSystemModuleScm> modelScmList, long systemid) {
		List<Long> flag = new ArrayList<>();
		List<Long> beforFlag = new ArrayList<>();
		List<TblSystemModuleScm> beforModuleScmlist = systemModuleScmMapper.findModuleScmBySystemId(systemid);
		for (TblSystemModuleScm moduleScm : beforModuleScmlist) {
			beforFlag.add(moduleScm.getId());
		}
		for (TblSystemModuleScm moduleScm : modelScmList) {
			for (TblSystemModuleScm moduleScm1 : beforModuleScmlist) {
				if(moduleScm.getSystemModuleId() ==moduleScm1.getSystemModuleId()&&
						moduleScm.getEnvironmentType()==moduleScm1.getEnvironmentType()){
					flag.add(moduleScm1.getId());
				}
			}
		}
		beforFlag.removeAll(flag);
		return beforFlag;
	}
}
