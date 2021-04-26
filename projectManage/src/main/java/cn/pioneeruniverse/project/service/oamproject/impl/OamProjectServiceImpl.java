package cn.pioneeruniverse.project.service.oamproject.impl;

import java.sql.Timestamp;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.common.utils.PinYinUtil;
import cn.pioneeruniverse.common.velocity.tag.VelocityDataDict;
import cn.pioneeruniverse.project.dao.mybatis.role.RoleDao;
import cn.pioneeruniverse.project.entity.*;
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
import cn.pioneeruniverse.project.dao.mybatis.CompanyInfoMapper;
import cn.pioneeruniverse.project.dao.mybatis.DeptInfoMapper;
import cn.pioneeruniverse.project.dao.mybatis.OamProjectMapper;
import cn.pioneeruniverse.project.dao.mybatis.ProjectGroupMapper;
import cn.pioneeruniverse.project.dao.mybatis.ProjectGroupUserMapper;
import cn.pioneeruniverse.project.dao.mybatis.SystemInfoMapper;
import cn.pioneeruniverse.project.dao.mybatis.TblProjectSystemMapper;
import cn.pioneeruniverse.project.dao.mybatis.UserMapper;
import cn.pioneeruniverse.project.service.oamproject.OamProjectService;

@Service
public class OamProjectServiceImpl implements OamProjectService {

	@Autowired
	private RedisUtils redisUtils;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private ProjectGroupUserMapper projectGroupUserMapper;  

	@Autowired
	private ProjectGroupMapper projectGroupMapper;
	
	@Autowired
	private OamProjectMapper oamProjectMapper;
	
	@Autowired
	private SystemInfoMapper systemInfoMapper;
	
	@Autowired
	private DeptInfoMapper deptInfoMapper;
	
	@Autowired
	private CompanyInfoMapper companyInfoMapper;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private TblProjectSystemMapper tblProjectSystemMapper;
	
	/**
	 * 
	* @Title: selectOamProject
	* @Description: 查询运维项目
	* @author author
	* @param projectName 项目名
	* @param projectStatusName 项目状态
	* @param projectManageName 项目管理岗
	* @param developManageName 项目开发岗
	* @param uid 当前用户ID
	* @param roleCodes 角色编码
	* @param page 第几页
	* @param rows 每页数据量
	* @return List<TblProjectInfo>
	* @throws Exception
	 */
	@Override
	@Transactional(readOnly=true)
	public List<TblProjectInfo> selectOamProject(String projectName, String projectStatusName, String projectManageName,
			String developManageName,Long uid, List<String> roleCodes, Integer page, Integer rows) throws Exception {
		// TODO Auto-generated method stub
		List<Long> projectIds = new ArrayList<>(); 
		//如果管理岗人员姓名不为空，开发岗人员姓名为空，根据管理岗人员查出所在项目的id
		if(StringUtils.isNotEmpty(projectManageName) && StringUtils.isEmpty(developManageName)) {
			Integer code = getUserPost("项目管理岗");
//			 根据用户名查询出用户id
			//Long userId = userMapper.findIdByUserName(projectManageName);
            List<Long> userIds=userMapper.findIdByUserNameNew(projectManageName);
			// 根据岗位类型和用户id查询出项目组表的id
            if(userIds != null && userIds.size()>0){
                HashMap<String, Object> map = new HashMap<>();
                map.put("code", code);
                map.put("userIds", userIds);
                List<Long> ids = projectGroupUserMapper.findProjectGroupIdsByUserIdAndUserPostNew(map);
                // 根据项目组主键id查询出项目id
                List<Long> pIds = projectGroupMapper.findProjectIdsByProjectGroupIds(ids);
                projectIds = pIds;
            }else {
                return null;
            }
//			if(userId != null) {
//				HashMap<String, Object> map = new HashMap<>();
//				map.put("code", code);
//				map.put("userId", userId);
//				List<Long> ids = projectGroupUserMapper.findProjectGroupIdsByUserIdAndUserPost(map);
//				// 根据项目组主键id查询出项目id
//				List<Long> pIds = projectGroupMapper.findProjectIdsByProjectGroupIds(ids);
//				projectIds = pIds;
//			}else {
//				return null;
//			}
		}
		//如果管理岗人员为空，开发岗人员不为空，根据开发岗人员查出索爱项目的id
		if(StringUtils.isEmpty(projectManageName) && StringUtils.isNotEmpty(developManageName)) {
			Integer code = getUserPost("开发管理岗");
//			 根据用户名查询出用户id
//			Long userId = userMapper.findIdByUserName(developManageName);
            List<Long> userIds=userMapper.findIdByUserNameNew(developManageName);
            // 根据岗位类型和用户id查询出项目组表的id
            if(userIds != null && userIds.size()>0){
                HashMap<String, Object> map = new HashMap<>();
                map.put("code", code);
                map.put("userIds", userIds);
                List<Long> ids = projectGroupUserMapper.findProjectGroupIdsByUserIdAndUserPostNew(map);
                // 根据项目组主键id查询出项目id
                List<Long> pIds = projectGroupMapper.findProjectIdsByProjectGroupIds(ids);
                projectIds = pIds;
            }else {
                return null;
            }

//			if(userId != null) {
//				HashMap<String, Object> map = new HashMap<>();
//				map.put("code", code);
//				map.put("userId", userId);
//				List<Long> ids = projectGroupUserMapper.findProjectGroupIdsByUserIdAndUserPost(map);
//				// 根据项目组主键id查询出项目id
//				List<Long> pIds = projectGroupMapper.findProjectIdsByProjectGroupIds(ids);
//				projectIds = pIds;
//			}else {
//				return null;
//			}
		}
		//如果两个岗位的人员都不为空
		if(StringUtils.isNotEmpty(projectManageName) && StringUtils.isNotEmpty(developManageName)) {
			Integer code1 = getUserPost("项目管理岗");
			Integer code2 = getUserPost("开发管理岗");
            List<Long> userId1=userMapper.findIdByUserNameNew(projectManageName);
            List<Long> userId2=userMapper.findIdByUserNameNew(developManageName);
//			Long userId1 = userMapper.findIdByUserName(projectManageName);
//			Long userId2 = userMapper.findIdByUserName(developManageName);
			if(userId1.size()==0 || userId2 .size()==0) {
				return null;
			}else {
				// 根据管理岗位类型和管理人员id查询出项目组表的id
				HashMap<String, Object> map1 = new HashMap<>();
				map1.put("code", code1);
				map1.put("userIds", userId1);
				List<Long> ids1 = projectGroupUserMapper.findProjectGroupIdsByUserIdAndUserPostNew(map1);
				// 根据项目组主键id查询出项目id
				List<Long> pIds1 = projectGroupMapper.findProjectIdsByProjectGroupIds(ids1);
				
				// 根据开发岗位类型和开发人员id查询出项目组表的id
				HashMap<String, Object> map2 = new HashMap<>();
				map2.put("code", code2);
				map2.put("userIds", userId2);
				List<Long> ids2 = projectGroupUserMapper.findProjectGroupIdsByUserIdAndUserPostNew(map2);
				// 根据项目组主键id查询出项目id
				List<Long> pIds2 = projectGroupMapper.findProjectIdsByProjectGroupIds(ids2);
				//取出两个人员共同在的项目id
				for (Long pId1 : pIds1) {
					for (Long pId2 : pIds2) {
						if(pId1.longValue() == pId2.longValue()) {
							projectIds.add(pId1);
						}
					}
				}
			}
			
		}
		Integer projectStatus = null;
		// 根据接收的状态名称，从redis中解析，转换成状态码
		if (StringUtils.isNotBlank(projectStatusName)) {
			projectStatus = getStatus(projectStatusName);
		}
		//分页数据
		Integer start = (page-1)*rows;
		HashMap<String, Object> map = new HashMap<>();
		map.put("projectIds", projectIds);
		map.put("projectStatus", projectStatus);
		map.put("projectName", projectName);
		map.put("start", start);
		map.put("pageSize", rows);
		map.put("uid", uid);
		List<TblProjectInfo> list = new ArrayList<>();
		if (roleCodes!=null && roleCodes.contains("XITONGGUANLIYUAN")) {//当前登录用户的角色有系统管理员
			list = oamProjectMapper.selectOamProject(map);
		}else {
			list = oamProjectMapper.selectOamProjectCondition(map);
		}
		for (TblProjectInfo tpi : list) {
			//封装状态，把状态码转换为状态
			Integer s = tpi.getProjectStatus();
			if(s != null) {
				String status = s.toString();
				String statusName = getStatusName(status);
				tpi.setProjectStatusName(statusName);
			}
			//封装关联系统
			Long id = tpi.getId();
//			List<String> systemName = systemInfoMapper.selectSystemName(id);
			List<String> systemName = tblProjectSystemMapper.getSystemNames(id);
			tpi.setSystemName(systemName);
			List<Long> systemIds = tblProjectSystemMapper.getSystemIds(id);
			tpi.setSystemIds(systemIds);
			//查询岗位人员
			List<Long> projectGroupIds = projectGroupMapper.findProjectGroupIdsByProjectId(id);
			String redisString = redisUtils.get("TBL_PROJECT_GROUP_USER_USER_POST").toString();
			JSONObject jsonObj = JSON.parseObject(redisString);
			Integer userPost1 = null;
			for (String key : jsonObj.keySet()) {
				if(jsonObj.get(key).toString().equals("项目管理岗")) {
					userPost1 = Integer.valueOf(key);
				}
			}
			if(projectGroupIds != null && projectGroupIds.size() != 0 && userPost1 != null) {
				HashMap<String, Object> map1 = new HashMap<>();
				map1.put("projectGroupIds", projectGroupIds);
				map1.put("userPost", userPost1);
				//获取projectGroupIds项目小组和userPost1岗位的用户列表
				List<Long> userId1 = projectGroupUserMapper.findUserId(map1);
				if(userId1.size()!=0) {
				List<String> userName = userMapper.findUserNameByIds(userId1);
				tpi.setProjectManageUsers(userName);
				}
			}
			
			//获取开发管理岗对应的key值
			Integer userPost2 = null;
			for (String key : jsonObj.keySet()) {
				if(jsonObj.get(key).toString().equals("开发管理岗")) {
					userPost2 = Integer.valueOf(key);
				}
			}
			if(projectGroupIds != null && projectGroupIds.size() != 0 && userPost2 != null) {
				HashMap<String, Object> map2 = new HashMap<>();
				map2.put("projectGroupIds", projectGroupIds);
				map2.put("userPost", userPost2);
				//查询项目管理岗的人员列表
				List<Long> userId2 = projectGroupUserMapper.findUserId(map2);
				if(userId2.size()!=0) {
				List<String> userName2 = userMapper.findUserNameByIds(userId2);
				tpi.setDevelopManageUsers(userName2);
				}
			}
		}
		return list;
	}

	/**
	 * 
	* @Title: insertOamProject
	* @Description: 开发库新增运维类项目
	* @author author
	* @param tblProjectInfo
	* @param request
	* @return Long 项目ID
	* @throws Exception
	 */
	@Override
	@DataSource(name = "itmpDataSource")
	@Transactional(readOnly=false,rollbackFor=Exception.class,propagation = Propagation.REQUIRED)
	public Long insertOamProject(TblProjectInfo tblProjectInfo,HttpServletRequest request) throws Exception {
		//转换项目类型为项目表中的状态码（虽然都是运维类型，但是为了代码的复用性，从redis中解析）
		String typeName = tblProjectInfo.getProjectTypeName();
		String redisStr = redisUtils.get("TBL_PROJECT_INFO_PROJECT_TYPE").toString();
		JSONObject jsonObj = JSON.parseObject(redisStr);
		for (String key : jsonObj.keySet()) {
			String value = jsonObj.get(key).toString();
			if(value.equals(typeName)) {
				tblProjectInfo.setProjectType(Integer.valueOf(key));
			}
		}
		//新建项目的初始化状态是进行中
		Integer status = getStatus("实施");
		tblProjectInfo.setProjectStatus(status);
		//设置创建时间
		tblProjectInfo.setCreateDate(new Timestamp(new Date().getTime()));
		tblProjectInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));
		//设置状态 1=正常；2=删除
		tblProjectInfo.setStatus(1);
		//创建者
		tblProjectInfo.setCreateBy(CommonUtil.getCurrentUserId(request));
		tblProjectInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		oamProjectMapper.insertOamProject(tblProjectInfo);
		//拿到插入的这条数据的主键id
		Long projectId = tblProjectInfo.getId();
		//获取接收页面传的系统id
//		List<String> systemNames = tblProjectInfo.getSystemName();
		List<Long> systemIds = tblProjectInfo.getSystemIds();
		if(systemIds.size() != 0) {
//			HashMap<String,Object> map = new HashMap<>();
//			map.put("projectId", projectId);
//			map.put("systemIds", systemIds);
			//更新系统表，关联到项目
//			systemInfoMapper.updateSystem(map);
			for (Long systemId : systemIds) {
				TblProjectSystem projectSystem = new TblProjectSystem();
				projectSystem.setProjectId(projectId);
				projectSystem.setSystemId(systemId);
				projectSystem.setRelationType(1);
				projectSystem.setStatus(1);
				projectSystem.setCreateBy(CommonUtil.getCurrentUserId(request));
				projectSystem.setCreateDate(new Timestamp(new Date().getTime()));
				projectSystem.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
				projectSystem.setLastUpdateDate(new Timestamp(new Date().getTime()));
				tblProjectSystemMapper.insertProjectSystem(projectSystem);
			}
			
			}
		//同步到测试库
		SpringContextHolder.getBean(OamProjectService.class).insertTmpProject(tblProjectInfo,request,projectId);
		return projectId;
	}
	
	/**
	 * 
	* @Title: insertTmpProject
	* @Description: 测试库新增运维类项目
	* @author author
	* @param tblProjectInfo 项目信息
	* @param request
	* @param id 项目ID
	 */
	@Override
	@DataSource(name = "tmpDataSource")
	@Transactional(readOnly=false,rollbackFor=Exception.class,propagation=Propagation.REQUIRES_NEW)
	public void insertTmpProject(TblProjectInfo tblProjectInfo, HttpServletRequest request, Long id){
		// TODO Auto-generated method stub
		String typeName = tblProjectInfo.getProjectTypeName();
		String redisStr = redisUtils.get("TBL_PROJECT_INFO_PROJECT_TYPE").toString();
		JSONObject jsonObj = JSON.parseObject(redisStr);
		for (String key : jsonObj.keySet()) {
			String value = jsonObj.get(key).toString();
			if(value.equals(typeName)) {
				tblProjectInfo.setProjectType(Integer.valueOf(key));
			}
		}
		//新建项目的初始化状态是进行中
		Integer status = getStatus("实施");
		tblProjectInfo.setProjectStatus(status);
		//设置创建时间
		tblProjectInfo.setCreateDate(new Timestamp(new Date().getTime()));
		tblProjectInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));
		//设置状态 1=正常；2=删除
		tblProjectInfo.setStatus(1);
		//创建者
		tblProjectInfo.setCreateBy(CommonUtil.getCurrentUserId(request));
		tblProjectInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		tblProjectInfo.setId(id);
		oamProjectMapper.insertTmpOamProject(tblProjectInfo);
		//拿到插入的这条数据的主键id
		Long projectId = tblProjectInfo.getId();
		//获取接收页面传的系统id
//		List<String> systemNames = tblProjectInfo.getSystemName();
		List<Long> systemIds = tblProjectInfo.getSystemIds();
		if(systemIds.size() != 0) {
//			HashMap<String,Object> map = new HashMap<>();
//			map.put("projectId", projectId);
//			map.put("systemIds", systemIds);
//			//更新系统表，关联到项目
//			systemInfoMapper.updateSystem(map);
			for (Long systemId : systemIds) {
				TblProjectSystem projectSystem = new TblProjectSystem();
				projectSystem.setProjectId(projectId);
				projectSystem.setSystemId(systemId);
				projectSystem.setRelationType(1);
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
	* @Title: selectProject
	* @Description: 运维类项目详情
	* @author author
	* @param id x项目ID
	* @return TblProjectInfo 项目信息
	* @throws Exception
	 */
	@Override
	@Transactional(readOnly=true)
	public TblProjectInfo selectProject(Long id) throws Exception {
		TblProjectInfo tblProjectInfo = oamProjectMapper.selectProject(id);
		//获取项目状态
		Integer s = tblProjectInfo.getProjectStatus();
		if(s != null) {
			String projectStatus = s.toString();
			String status = getStatusName(projectStatus);
			tblProjectInfo.setProjectStatusName(status);
		}
		//获取项目关联系统
//		List<String> systemName = systemInfoMapper.selectSystemName(id);
//		tblProjectInfo.setSystemName(systemName);
//		List<TblSystemInfo> systemList = systemInfoMapper.selectSystems(id);
		List<TblSystemInfo> systemList = systemInfoMapper.getSystemsByProjectId(id);
		tblProjectInfo.setSystemList(systemList);

		Map<Long, TblProjectGroup> resultMap = new LinkedHashMap<Long, TblProjectGroup>();
		//根据项目id查出对应该项目的父级项目组集合
		List<TblProjectGroup> projectGroupList = projectGroupMapper.selectParentProjectGroups(id);
		//调用递归方法
		resultMap = getProjectGroupList(projectGroupList,resultMap);

		 List<TblProjectGroup> groupList = new ArrayList<TblProjectGroup>();
		//取到每个项目组
		for (Map.Entry<Long,TblProjectGroup> entry : resultMap.entrySet()) {
			//Long projectGroupId = tblProjectGroup.getId();
			TblProjectGroup projectGroup = new TblProjectGroup();
			Long projectGroupId = entry.getValue().getId();
			 String parentIds;
			//如果parentids是null、说明是父级 
			 if(entry.getValue().getParentIds() == null || entry.getValue().getParentIds().equals("")) {
				 parentIds = entry.getValue().getId().toString();
				 projectGroup.setLevel(0);
			 }else {
				 parentIds = entry.getValue().getParentIds() + entry.getValue().getId() + ",";
//				 projectGroup.setLevel(entry.getValue().getParentIds().replaceAll(",", "").length());
				 String[] strings = entry.getValue().getParentIds().split(",");
				 projectGroup.setLevel(strings.length);
			 }
			 //根据paraentids去查询子级
	            List<TblProjectGroup> childrenList = projectGroupMapper.getChildren(parentIds);
	            //如果有子级
	            if (childrenList != null && childrenList.size() != 0) {
	            	projectGroup.setIsLeaf(false);
	            } else {
	            	projectGroup.setIsLeaf(true);
	            }
	            projectGroup.setId(entry.getValue().getId());
	            projectGroup.setExpanded(false);
	            projectGroup.setLoaded(true);
	            projectGroup.setParent(entry.getValue().getParentId());
	            projectGroup.setProjectGroupName(entry.getValue().getProjectGroupName());
	            
	            
			//根据项目小组id查出项目小组人员对象集合
			List<TblProjectGroupUser> projectGroupUserlist = projectGroupUserMapper.selectProjectGroupUserByProjectGroupId(projectGroupId);
			List<User> list = new ArrayList<User>();
				for (TblProjectGroupUser tblProjectGroupUser : projectGroupUserlist) {
					Long userId = tblProjectGroupUser.getUserId();
					String userPost = tblProjectGroupUser.getUserPost().toString();
					//从redis中解析出该人员的岗位
					String redis = redisUtils.get("TBL_PROJECT_GROUP_USER_USER_POST").toString();
					JSONObject json = JSON.parseObject(redis);
					String valueName = json.get(userPost).toString();
					//根据用户id取用户表查出用户信息
					TblUserInfo tblUserInfo = userMapper.selectUserById(userId);
					if(tblUserInfo!=null) {
						String userName = tblUserInfo.getUserName();
					Integer userType = tblUserInfo.getUserType();
					
					Long projectGroupUserId = tblProjectGroupUser.getId();
					User user = new User();
					user.setId(userId);
					user.setValueName(valueName);
					user.setUserPost(Integer.valueOf(userPost));
					user.setUserName(userName);
					user.setUserType(userType);
					user.setPeojectGroupUserId(projectGroupUserId);
					
					//如果用户类型是1，是内部人员，根据deptId查出所属部门
					if(userType != null && Integer.valueOf(userType) == 1) {
						if(tblUserInfo.getDeptId() != null) {
							long deptId = tblUserInfo.getDeptId().longValue();
							String deptName = deptInfoMapper.selectDeptName(deptId);
							user.setDeptOrCompany(deptName);
						}
					}
					//如果用户类型是2，是外部人员，根据companyId查出所属公司。
					if(userType != null && Integer.valueOf(userType) == 2) {
						if(tblUserInfo.getCompanyId() != null) {
							Long companyId = tblUserInfo.getCompanyId();
							String companyName = companyInfoMapper.selectCompanyNameById(companyId);
							user.setDeptOrCompany(companyName);
						}
					}
					list.add(user);
					}
				}
				projectGroup.setList(list);
				
				groupList.add(projectGroup);
		}
		tblProjectInfo.setList(groupList);
		return tblProjectInfo;
	}

	/**
	 * 
	* @Title: selectUser
	* @Description: 编辑页面人员弹框
	* @author author
	* @param userName 姓名
	* @param employeeNumber 工号
	* @param deptName 部门
	* @param companyName 公司
	* @param pageNumber 第几页
	* @param pageSize 每页大小
	* @return List<TblUserInfo>
	* @throws Exception
	 */
	@Override
	@Transactional(readOnly=true)
	public List<TblUserInfo> selectUser(String userName, String employeeNumber, String deptName, String companyName,
			Integer pageNumber, Integer pageSize) throws Exception {
		// TODO Auto-generated method stub
		List<Long> deptIds = null;
		if(!deptName.isEmpty()) {
			deptIds = deptInfoMapper.selectDeptIds(deptName);
		}
		List<Long> companyIds = null;
		if(!companyName.isEmpty()) {
			companyIds = companyInfoMapper.selectCompanyId(companyName);
		}
		Integer start = (pageNumber-1)*pageSize;
		HashMap<String, Object> map = new HashMap<>();
		map.put("userName", userName);
		map.put("employeeNumber", employeeNumber);
		map.put("deptIds", deptIds);
		map.put("companyIds", companyIds);
		map.put("start", start);
		map.put("pageSize", pageSize);
		List<TblUserInfo> list = userMapper.selectUser(map);
		for (TblUserInfo tblUserInfo : list) {
			Integer deptId2 = tblUserInfo.getDeptId();
			if(deptId2 != null) {
				Long value = deptId2.longValue();
				String deptName2 = deptInfoMapper.selectDeptName(value);
				tblUserInfo.setDeptName(deptName2);
				}
			Long companyId2 = tblUserInfo.getCompanyId();
			if(companyId2 != null) {
				String companyName2 = companyInfoMapper.selectCompanyNameById(companyId2);
				tblUserInfo.setCompanyName(companyName2);
				}
			}
		return list;
	}

	/**
	 * 
	* @Title: selectDeptName
	* @Description: 人员弹框部门下拉框内容
	* @author author
	* @return List<String>部门名列表
	* @throws Exception
	 */
	@Override
	@Transactional(readOnly=true)
	public List<String> selectDeptName() throws Exception {
		// TODO Auto-generated method stub
		return deptInfoMapper.selectAllDeptName();
	}

	/**
	 * 
	* @Title: selectCompanyName
	* @Description: 人员弹框公司内容
	* @author author
	* @return List<String>公司名列表
	* @throws Exception
	 */
	@Override
	@Transactional(readOnly=true)
	public List<String> selectCompanyName() throws Exception {
		return companyInfoMapper.selectCompanyName();
	}

	/**
	 * 
	* @Title: editProject
	* @Description: 编辑项目
	* @author author
	* @param tblProjectInfo 项目信息
	* @param request
	* @throws Exception
	 */
	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class,propagation = Propagation.REQUIRED)
	public void editProject(TblProjectInfo tblProjectInfo,HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		Long id = tblProjectInfo.getId();
		//根据id把所有跟此项目关联的系统、项目小组、项目小组人员解绑
		//解绑系统
//		systemInfoMapper.untyingSystem(id);
		tblProjectSystemMapper.deleteProjectSystem(id);
		//解绑项目小组，把状态改为删除状态
		projectGroupMapper.untyingProjectGroup(id);
		//解绑人员
//		List<Long> pgIds = projectGroupMapper.findProjectGroupIdsByProjectId(id);
//		projectGroupUserMapper.untyingProjectGroupUser(pgIds);
		//根据对象中的属性，再关联
		//编辑项目
		//把项目状态转成状态码
		String statusName = tblProjectInfo.getProjectStatusName();
		Integer status = getStatus(statusName);
		tblProjectInfo.setProjectStatus(status);
		//最近修改者
		tblProjectInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		//最后修改时间
		tblProjectInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));
		oamProjectMapper.updateProject(tblProjectInfo);
		//根据系统id关联系统
//		HashMap<String,Object> map = new HashMap<>();
		List<Long> systemIds = tblProjectInfo.getSystemIds();
		if(systemIds.size() != 0) {
//			map.put("systemIds", systemIds);
//			map.put("projectId", id);
//			systemInfoMapper.updateSystemInfo(map);
			for (Long systemId : systemIds) {
				TblProjectSystem projectSystem = new TblProjectSystem();
				projectSystem.setProjectId(id);
				projectSystem.setSystemId(systemId);
				projectSystem.setRelationType(1);
				projectSystem.setStatus(1);
				projectSystem.setCreateBy(CommonUtil.getCurrentUserId(request));
				projectSystem.setCreateDate(new Timestamp(new Date().getTime()));
				projectSystem.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
				projectSystem.setLastUpdateDate(new Timestamp(new Date().getTime()));
				tblProjectSystemMapper.insertProjectSystem(projectSystem);
			}
		}
		//根据小组id关联项目小组
		List<TblProjectGroup> projectGroupList = tblProjectInfo.getList();
		//根据项目id查出projectGroupUserId
//		List<Long> pgIds = projectGroupMapper.findProjectGroupIdsByProjectId(id);
//		List<Long> pguIds = projectGroupUserMapper.selectIdByProjectGroupId(pgIds);
		HashMap<String, Object> map2 = new HashMap<>();

		//将项目所有岗位对应的用户删除
		List<TblRoleInfo> roleInfoList = roleDao.
				selectUserPostRole(tblProjectInfo.getId(),null);
		Long [] roleIds = new Long[roleInfoList.size()];
		for (int i=0;i<roleInfoList.size();i++){
			roleIds[i] = roleInfoList.get(i).getId();
		}
		if(roleIds.length>0){
			roleDao.deleteProjectUserRole(roleIds);
		}

		for (TblProjectGroup tblProjectGroup : projectGroupList) {
			Long projectGroupId = tblProjectGroup.getId();
			map2.put("projectGroupId", projectGroupId);
			map2.put("id", id);
			projectGroupMapper.updateProjectGroup(map2);
//			Long projectGroupId = tblProjectGroup.getId();
			List<User> userList = tblProjectGroup.getList();
			List<Long> pguIds = projectGroupUserMapper.selectIdByProjectGroupId(projectGroupId);
			List<Long> list = new ArrayList<>();
			for (User user : userList) {
				//如果项目小组人员id为null，说明是新增的
				if(user.getPeojectGroupUserId() == null) {
					TblProjectGroupUser tblProjectGroupUser = new TblProjectGroupUser();
					tblProjectGroupUser.setProjectGroupId(projectGroupId);
					tblProjectGroupUser.setStatus(1);
					tblProjectGroupUser.setUserId(user.getId());
					tblProjectGroupUser.setUserPost(user.getUserPost());
					tblProjectGroupUser.setCreateBy(CommonUtil.getCurrentUserId(request));
					tblProjectGroupUser.setCreateDate(new Timestamp(new Date().getTime()));
					projectGroupUserMapper.insertProjectGroupUser(tblProjectGroupUser);
				}else {
					Long peojectGroupUserId = user.getPeojectGroupUserId();
					for (Long pguId : pguIds) {
						if(peojectGroupUserId.longValue() == pguId.longValue() ) {
							HashMap<String,Object> map3 = new HashMap<>();
							map3.put("projectGroupId", projectGroupId);
							map3.put("userId", user.getId());
							map3.put("userPost", user.getUserPost());
							map3.put("peojectGroupUserId", peojectGroupUserId);
							projectGroupUserMapper.updateProjectGroupUser(map3);
							list.add(peojectGroupUserId);
						}
					}
				}
				Map<String, Object> userRole = new HashMap();
				//查询角色表是否存在对应的项目组与岗位的记录
				List<TblRoleInfo> roleInfoList1 = roleDao.
						selectUserPostRole(tblProjectInfo.getId(),user.getUserPost());
				if(roleInfoList1==null || roleInfoList1.size()==0){
					//如果不存在就新增一条岗位记录
					TblRoleInfo roleInfo = new TblRoleInfo();
					roleInfo.setProjectId(tblProjectInfo.getId());
					roleInfo.setUserPost(user.getUserPost());
					VelocityDataDict dict= new VelocityDataDict();
					Map<String,String> userPostMap = dict.getDictMap("TBL_PROJECT_GROUP_USER_USER_POST");
					for(Map.Entry<String, String> entry : userPostMap.entrySet()){
						if(entry.getKey().equals(user.getUserPost().toString())) {
							roleInfo.setRoleName(entry.getValue());
							String empNo = tblProjectInfo.getId().toString() +
									PinYinUtil.getPingYin(entry.getValue()).toUpperCase(Locale.ENGLISH);
							roleInfo.setRoleCode(empNo);
						}
					}
					CommonUtil.setBaseValue(roleInfo,request);
					roleDao.insertUserPostRole(roleInfo);

					//获取新增岗位记录的id
					userRole.put("roleId", roleInfo.getId());
					userRole.put("useId", user.getId());
					userRole.put("status", 1);
					userRole.put("createBy", CommonUtil.getCurrentUserId(request));
					userRole.put("lastUpdateBy", CommonUtil.getCurrentUserId(request));
					//新增一条角色表记录
					roleDao.insertRoleUser(userRole);
				}else{
					//如果存在就查询是否有相对应的用户岗位
					Integer count = roleDao.selectUserRole(user.getId(),roleInfoList1.get(0).getId());
					if(count<1) {
						//如果没有相对应的用户岗位，新增一条角色表记录
						userRole.put("roleId", roleInfoList1.get(0).getId());
						userRole.put("useId", user.getId());
						userRole.put("status", 1);
						userRole.put("createBy", CommonUtil.getCurrentUserId(request));
						userRole.put("lastUpdateBy", CommonUtil.getCurrentUserId(request));
						roleDao.insertRoleUser(userRole);
					}
				}
			}
			List<Long> listAll = new ArrayList<>();
			List<Long> resultList= new ArrayList<>();
			listAll.addAll(pguIds);
			listAll.addAll(list);
			for (int i = 0; i < listAll.size(); i++) {
	            if(pguIds.contains(listAll.get(i)) && list.contains(listAll.get(i))){
	                continue;
	            }else{
	                resultList.add(listAll.get(i));
	            }
			}
			if(resultList.size() != 0) {
				projectGroupUserMapper.delProjectGroupUser(resultList);
			}
		}
//		editTmpProject(tblProjectInfo, request);
		SpringContextHolder.getBean(OamProjectService.class).editTmpProject(tblProjectInfo,request);
	}
	
	//编辑项目同步测管平台数据
	@Override
	@DataSource(name = "tmpDataSource")
	@Transactional(readOnly=false,rollbackFor=Exception.class,propagation=Propagation.REQUIRES_NEW)
	public void editTmpProject(TblProjectInfo tblProjectInfo, HttpServletRequest request) {
		// TODO Auto-generated method stub
		Long id = tblProjectInfo.getId();
//		systemInfoMapper.untyingSystem(id);
		tblProjectSystemMapper.deleteProjectSystem(id);
		projectGroupMapper.untyingProjectGroup(id);
		String statusName = tblProjectInfo.getProjectStatusName();
		Integer status = getStatus(statusName);
		tblProjectInfo.setProjectStatus(status);
		tblProjectInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		tblProjectInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));
		oamProjectMapper.updateProject(tblProjectInfo);
//		HashMap<String,Object> map = new HashMap<>();
		List<Long> systemIds = tblProjectInfo.getSystemIds();
		if(systemIds.size() != 0) {
//			map.put("systemIds", systemIds);
//			map.put("projectId", id);
//			systemInfoMapper.updateSystemInfo(map);
			for (Long systemId : systemIds) {
				TblProjectSystem projectSystem = new TblProjectSystem();
				projectSystem.setProjectId(id);
				projectSystem.setSystemId(systemId);
				projectSystem.setRelationType(1);
				projectSystem.setStatus(1);
				projectSystem.setCreateBy(CommonUtil.getCurrentUserId(request));
				projectSystem.setCreateDate(new Timestamp(new Date().getTime()));
				projectSystem.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
				projectSystem.setLastUpdateDate(new Timestamp(new Date().getTime()));
				tblProjectSystemMapper.insertProjectSystem(projectSystem);
			}
		}
		List<TblProjectGroup> projectGroupList = tblProjectInfo.getList();
		HashMap<String, Object> map2 = new HashMap<>();
		for (TblProjectGroup tblProjectGroup : projectGroupList) {
			Long projectGroupId = tblProjectGroup.getId();
			map2.put("projectGroupId", projectGroupId);
			map2.put("id", id);
			projectGroupMapper.updateProjectGroup(map2);
			List<User> userList = tblProjectGroup.getList();
			List<Long> pguIds = projectGroupUserMapper.selectIdByProjectGroupId(projectGroupId);
			List<Long> list = new ArrayList<>();
			for (User user : userList) {
				if(user.getPeojectGroupUserId() == null) {
					TblProjectGroupUser tblProjectGroupUser = new TblProjectGroupUser();
					tblProjectGroupUser.setProjectGroupId(projectGroupId);
					tblProjectGroupUser.setStatus(1);
					tblProjectGroupUser.setUserId(user.getId());
					tblProjectGroupUser.setUserPost(user.getUserPost());
					tblProjectGroupUser.setCreateBy(CommonUtil.getCurrentUserId(request));
					tblProjectGroupUser.setCreateDate(new Timestamp(new Date().getTime()));
					projectGroupUserMapper.insertProjectGroupUser(tblProjectGroupUser);
				}else {
					Long peojectGroupUserId = user.getPeojectGroupUserId();
					for (Long pguId : pguIds) {
						if(peojectGroupUserId.longValue() == pguId.longValue() ) {
							HashMap<String,Object> map3 = new HashMap<>();
							map3.put("projectGroupId", projectGroupId);
							map3.put("userId", user.getId());
							map3.put("userPost", user.getUserPost());
							map3.put("peojectGroupUserId", peojectGroupUserId);
							projectGroupUserMapper.updateProjectGroupUser(map3);
							list.add(peojectGroupUserId);
						}
					} 
					
				}
			}
			List<Long> listAll = new ArrayList<>();
			List<Long> resultList= new ArrayList<>();
			listAll.addAll(pguIds);
			listAll.addAll(list);
			for (int i = 0; i < listAll.size(); i++) {
	            if(pguIds.contains(listAll.get(i)) && list.contains(listAll.get(i))){
	                continue;
	            }else{
	                resultList.add(listAll.get(i));
	            }
			}
			if(resultList.size() != 0) {
				projectGroupUserMapper.delProjectGroupUser(resultList);
			}
		}
	}

	//获取项目下的所有项目组
	@Override
	@Transactional(readOnly=true)
	public List<TblProjectGroup> selectProjectGroup(Long id) throws Exception {
		// TODO Auto-generated method stub
		 Map<Long, TblProjectGroup> resultMap = new LinkedHashMap<Long, TblProjectGroup>();
		 //获取当前项目下的所有父级项目组
		 List<TblProjectGroup> projectGroupList = projectGroupMapper.selectParentProjectGroups(id);
		 //调用递归方法
		 resultMap = getProjectGroupList(projectGroupList,resultMap);
		 
		 List<TblProjectGroup> list = new ArrayList<TblProjectGroup>();
		 //遍历得到的所有项目组，拿到每个项目组的键值对
		 for (Map.Entry<Long,TblProjectGroup> entry : resultMap.entrySet()) {
			 TblProjectGroup projectGroup = new TblProjectGroup();
			
			 String parentIds;
			//如果parentids是null、说明是父级 
			 if(entry.getValue().getParentIds() == null || entry.getValue().getParentIds().equals("")) {
				 parentIds = entry.getValue().getId().toString();
				 projectGroup.setLevel(0);
			 }else {
				 parentIds = entry.getValue().getParentIds() + entry.getValue().getId() + ",";
//				 projectGroup.setLevel(entry.getValue().getParentIds().replaceAll(",", "").length());
				 String[] strings = entry.getValue().getParentIds().split(",");
				 projectGroup.setLevel(strings.length);
				 
			 }
			 //根据paraentids去查询子级
	            List<TblProjectGroup> childrenList = projectGroupMapper.getChildren(parentIds);
	            //如果有子级
	            if (childrenList != null && childrenList.size() != 0) {
	            	projectGroup.setIsLeaf(false);
	            } else {
	            	projectGroup.setIsLeaf(true);
	            }
	            projectGroup.setId(entry.getValue().getId());
	            projectGroup.setExpanded(false);
	            projectGroup.setLoaded(true);
	            projectGroup.setParent(entry.getValue().getParentId());
	            projectGroup.setProjectGroupName(entry.getValue().getProjectGroupName());
	            projectGroup.setOrderSeq(entry.getValue().getOrderSeq());
	            list.add(projectGroup);
		}
		 return list;
	}
	

	//递归方法
	private Map<Long, TblProjectGroup> getProjectGroupList(List<TblProjectGroup> projectGroupList,
			Map<Long, TblProjectGroup> resultMap) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		if(projectGroupList != null && projectGroupList.size() != 0) {
			//遍历当前项目下的所有父级项目组
			for (TblProjectGroup tblProjectGroup : projectGroupList) {
				//如果该项目组有parentids说明该项目组是子级
				if(tblProjectGroup.getParentIds() != null) {
					//子级的话要根据父id和自己的id当作下一级的父id去查询
					map.put("parentId", tblProjectGroup.getParentIds()+tblProjectGroup.getId()+",");
				}else {
					//父级的话根据自己的主键id去当作下一级父id去查询子级
					map.put("parentId", tblProjectGroup.getId()+",");
				}
				//把每个循环的对象放在resultMap中（当是最后一级的时候递归停止，存的是所有小组）
				resultMap.put(tblProjectGroup.getId(), tblProjectGroup);
				//去查询子级对象
				List<TblProjectGroup> childrenProjectGroups = projectGroupMapper.getChildrenProjectGroup(map);
				//调用递归方法，传入当前对象及子级
				getProjectGroupList(childrenProjectGroups,resultMap);
			}
		}
		return resultMap;
	}

	//选中人员显示在编辑页面
	@Override
	@Transactional(readOnly=true)
	public List<User> selectUsers(Long[] ids) throws Exception {
		// TODO Auto-generated method stub
		List<User> list = new ArrayList<>();
		for (int i=0;i<ids.length;i++) {
			Long id = ids[i];
			User user = new User();
			user.setId(id);
			TblUserInfo tblUserInfo = userMapper.selectUserById(id);
			String userName = tblUserInfo.getUserName();
			user.setUserName(userName);
			Integer userType = tblUserInfo.getUserType();
			user.setUserType(userType);
			//如果是1，是内部人员，查询所属部门
			if(userType!=null) {
				if(Integer.valueOf(userType) == 1) {
					if(tblUserInfo.getDeptId() != null) {
					long deptId = tblUserInfo.getDeptId().longValue();
					String deptName = deptInfoMapper.selectDeptName(deptId);
					user.setDeptOrCompany(deptName);
					}
				}
				//如果是2，是外部人员，查询所属公司
				if(Integer.valueOf(userType) == 2) {
					if(tblUserInfo.getCompanyId() != null) {
					Long companyId = tblUserInfo.getCompanyId();
					String companyName = companyInfoMapper.selectCompanyNameById(companyId);
					user.setDeptOrCompany(companyName);
					}
				}
			}
			list.add(user);
		}
		return list;
	}
	
	//结束项目
	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class,propagation = Propagation.REQUIRED)
	public void endProject(Long id, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		//最近修改者
		Long currentUserId = CommonUtil.getCurrentUserId(request);
		//最后修改时间
		Timestamp lastUpdateTime = new Timestamp(new Date().getTime());
//		Integer status = null ;
		Integer status = getStatus("结项");
		HashMap<String, Object> map = new HashMap<>();
		map.put("currentUserId", currentUserId);
		map.put("lastUpdateTime", lastUpdateTime);
		map.put("id", id);
		map.put("status", status);
		oamProjectMapper.endProject(map);
//		endTmpProject(id, request);
		SpringContextHolder.getBean(OamProjectService.class).endTmpProject(id,request);
	}

	//结束项目同步测管数据
	@Override
	@DataSource(name = "tmpDataSource")
	@Transactional(readOnly=false,rollbackFor=Exception.class,propagation=Propagation.REQUIRES_NEW)
	public void endTmpProject(Long id, HttpServletRequest request) {
		// TODO Auto-generated method stub
		//最近修改者
		Long currentUserId = CommonUtil.getCurrentUserId(request);
		//最后修改时间
		Timestamp lastUpdateTime = new Timestamp(new Date().getTime());
		Integer status = getStatus("结项");
		HashMap<String, Object> map = new HashMap<>();
		map.put("currentUserId", currentUserId);
		map.put("lastUpdateTime", lastUpdateTime);
		map.put("id", id);
		map.put("status", status);
		oamProjectMapper.endProject(map);
	}

	
	//关联系统的弹窗
	@Override
	@Transactional(readOnly=true)
	public List<TblSystemInfo> selectSystemInfo(TblSystemInfo tblSystemInfo, Integer pageNumber, Integer pageSize
			,Long[] systemIds) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<>();
		Integer start = (pageNumber-1)*pageSize;
		map.put("start", start);
		map.put("pageSize", pageSize);
		map.put("tblSystemInfo", tblSystemInfo);
		map.put("systemIds", systemIds);
		return systemInfoMapper.selectSystemInfo(map);
	}
	
	 
	 //删除项目组
	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class,propagation = Propagation.REQUIRED)
	public void deletePeojectGroup(Long projectGroupId,HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		//最近修改者
		Long currentUserId = CommonUtil.getCurrentUserId(request);
		//最后修改时间
		Timestamp lastUpdateTime = new Timestamp(new Date().getTime());
		List<Long> list = new ArrayList<>();
		//根据当前项目的id查出下一级的项目组
		List<TblProjectGroup> projectGroups = projectGroupMapper.findChildProjectGroups(projectGroupId);
		list = getProjectGroupIds(projectGroups, list);
		list.add(projectGroupId);
		HashMap<String, Object> map = new HashMap<>();
		map.put("list", list);
		map.put("currentUserId", currentUserId);
		map.put("lastUpdateTime", lastUpdateTime);
		projectGroupMapper.deletePeojectGroup(map);
		projectGroupUserMapper.deleteProjectGroupUser(map);
//		deleteTmpPeojectGroup(projectGroupId, request);
		SpringContextHolder.getBean(OamProjectService.class).deleteTmpPeojectGroup(projectGroupId, request);
	}
	
	//删除项目组同步测管数据
	@Override
	@DataSource(name = "tmpDataSource")
	@Transactional(readOnly=false,rollbackFor=Exception.class,propagation=Propagation.REQUIRES_NEW)
	public void deleteTmpPeojectGroup(Long projectGroupId, HttpServletRequest request) {
		// TODO Auto-generated method stub
		//最近修改者
		Long currentUserId = CommonUtil.getCurrentUserId(request);
		//最后修改时间
		Timestamp lastUpdateTime = new Timestamp(new Date().getTime());
		List<Long> list = new ArrayList<>();
		//根据当前项目的id查出下一级的项目组
		List<TblProjectGroup> projectGroups = projectGroupMapper.findChildProjectGroups(projectGroupId);
		list = getProjectGroupIds(projectGroups, list);
		list.add(projectGroupId);
		HashMap<String, Object> map = new HashMap<>();
		map.put("list", list);
		map.put("currentUserId", currentUserId);
		map.put("lastUpdateTime", lastUpdateTime);
		projectGroupMapper.deletePeojectGroup(map);
		projectGroupUserMapper.deleteProjectGroupUser(map);
	}

	private List<Long> getProjectGroupIds(List<TblProjectGroup> projectGroups, List<Long> list) {
		// TODO Auto-generated method stub
		List<Long> list2 = new ArrayList<>();
		List<TblProjectGroup> pgList = new ArrayList<>();
		if(projectGroups.size() != 0) {
			for (TblProjectGroup projectGroup : projectGroups) {
				Long pgId = projectGroup.getId();
				if(pgId != null) {
					list2.add(pgId);
					List<TblProjectGroup> projectGroups2 = projectGroupMapper.findChildProjectGroups(pgId);
					pgList.addAll(projectGroups2);
				}
			}
		list.addAll(list2);
		getProjectGroupIds(pgList, list);
		}
		return list;
	}

	//编辑项目组
	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class,propagation = Propagation.REQUIRED)
	public void editProjectGroup(TblProjectGroup tblProjectGroup, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		//最近修改者
		tblProjectGroup.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		//最后修改时间
		tblProjectGroup.setLastUpdateDate(new Timestamp(new Date().getTime()));
		projectGroupMapper.editProjectGroup(tblProjectGroup);
//		editTmpProjectGroup(tblProjectGroup, request);
		SpringContextHolder.getBean(OamProjectService.class).editTmpProjectGroup(tblProjectGroup, request);
	}
	
	//编辑项目组同步测管数据
	@Override
	@DataSource(name = "tmpDataSource")
	@Transactional(readOnly=false,rollbackFor=Exception.class,propagation=Propagation.REQUIRES_NEW)
	public void editTmpProjectGroup(TblProjectGroup tblProjectGroup,HttpServletRequest request) {
		// TODO Auto-generated method stub
		//最近修改者
		tblProjectGroup.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		//最后修改时间
		tblProjectGroup.setLastUpdateDate(new Timestamp(new Date().getTime()));
		projectGroupMapper.editProjectGroup(tblProjectGroup);
	}


	//新增项目组
	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class,propagation = Propagation.REQUIRED)
	public void saveProjectGroup(TblProjectGroup tblProjectGroup, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		//如果传过来的parent不为空说明有上级，这个parent就是上级的id，可以查上级的parentids加上上级的id就是新增的parentids，parent就是parentid
		Long parent = tblProjectGroup.getParent();
		if(parent != null) {
			//根据parent也就是上一级的id查出上级的ids
			String parentIds = projectGroupMapper.selectParentIds(parent);
			//上一级的ids加上上级的主键id就是新增的ids
			if(parentIds != null || parentIds != "") {
				String parentIds2 = parentIds + parent +",";
				tblProjectGroup.setParentIds(parentIds2);
			}
			if(parentIds == null || parentIds == "") {
				String parentIds3 = parent + ",";
				tblProjectGroup.setParentIds(parentIds3);
			}
			tblProjectGroup.setParentId(parent);
		}else {
//			tblProjectGroup.setParentId(parent);
			tblProjectGroup.setParentIds("");
		}
		//设置创建时间
		tblProjectGroup.setCreateDate(new Timestamp(new Date().getTime()));
		//设置状态 1=正常；2=删除
		tblProjectGroup.setStatus(1);
		//创建者
		tblProjectGroup.setCreateBy(CommonUtil.getCurrentUserId(request));
		tblProjectGroup.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		tblProjectGroup.setLastUpdateDate(new Timestamp(new Date().getTime()));
		projectGroupMapper.saveProjectGroup(tblProjectGroup);
		Long pgId = tblProjectGroup.getId();
//		saveTmpProjectGroup(tblProjectGroup, request);
		SpringContextHolder.getBean(OamProjectService.class).saveTmpProjectGroup(tblProjectGroup, request,pgId);
	}
	
	//新增项目组同步测管数据
	@Override
	@DataSource(name = "tmpDataSource")
	@Transactional(readOnly=false,rollbackFor=Exception.class,propagation=Propagation.REQUIRES_NEW)
	public void saveTmpProjectGroup(TblProjectGroup tblProjectGroup, HttpServletRequest request, Long id) {
		// TODO Auto-generated method stub
		//如果传过来的parent不为空说明有上级，这个parent就是上级的id，可以查上级的parentids加上上级的id就是新增的parentids，parent就是parentid
		Long parent = tblProjectGroup.getParent();
		if(parent != null) {
			//根据parent也就是上一级的id查出上级的ids
			String parentIds = projectGroupMapper.selectParentIds(parent);
			//上一级的ids加上上级的主键id就是新增的ids
			if(parentIds != null || parentIds != "") {
				String parentIds2 = parentIds + parent +",";
				tblProjectGroup.setParentIds(parentIds2);
			}
			if(parentIds == null || parentIds == "") {
				String parentIds3 = parent + ",";
				tblProjectGroup.setParentIds(parentIds3);
			}
			tblProjectGroup.setParentId(parent);
		}else {
//			tblProjectGroup.setParentId(parent);
			tblProjectGroup.setParentIds("");
		}
		tblProjectGroup.setId(id);
		//设置创建时间
		tblProjectGroup.setCreateDate(new Timestamp(new Date().getTime()));
		//设置状态 1=正常；2=删除
		tblProjectGroup.setStatus(1);
		//创建者
		tblProjectGroup.setCreateBy(CommonUtil.getCurrentUserId(request));
		projectGroupMapper.saveTmpProjectGroup(tblProjectGroup);
	}

	//根据状态名从redis中解析状态码
	public Integer getStatus(String statusName) {
		String redisStr = redisUtils.get("TBL_PROJECT_INFO_PROJECT_STATUS").toString();
		JSONObject jsonObj = JSON.parseObject(redisStr);
		Integer status = null;
		for (String key : jsonObj.keySet()) {
			if (jsonObj.get(key).toString().equals(statusName)) {
				status = Integer.valueOf(key);
			}
	}
		return status;
	}
	
	//根据状态码从redis中解析状态名
	public String getStatusName(String status) {
	String redisStr = redisUtils.get("TBL_PROJECT_INFO_PROJECT_STATUS").toString();
	JSONObject jsonObject = JSON.parseObject(redisStr);
	String statusName = jsonObject.get(status).toString();
	return statusName;

	}
	
	/**
	* @author author
	* @Description 根据岗位名称获取岗位码
	* @Date 2020/9/14
	* @param post
	* @return java.lang.Integer
	**/
	public Integer getUserPost(String post) {
	String redisStr = redisUtils.get("TBL_PROJECT_GROUP_USER_USER_POST").toString();
	JSONObject jsonObj = JSON.parseObject(redisStr);
	Integer code = null;
	for (String key : jsonObj.keySet()) {
		if (jsonObj.get(key).toString().equals(post)) {
			code = Integer.valueOf(key);
		}
	}
	return code;
	}

	@Override
	public List<Map<String, Object>> getProjectUserPost(Long projectId,String userPostIds) {
		List<Map<String, Object>> result = new ArrayList<>();
		List<String> list = projectGroupUserMapper.getProjectUserPost(projectId,userPostIds);
		 for (String string : list) {
			String value = CommonUtil.getDictValueName("TBL_PROJECT_GROUP_USER_USER_POST", string, "");
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("userPostId", string);
			map.put("userPostName", value);
			result.add(map);
		}
		return result;
	}

	


	


	

	
	
	
	}

