package cn.pioneeruniverse.project.service.assetsLibrary.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import cn.pioneeruniverse.common.dto.TblUserInfoDTO;
import cn.pioneeruniverse.common.entity.BootStrapTablePage;
import cn.pioneeruniverse.common.utils.CollectionUtil;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.JsonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.project.dao.mybatis.UserMapper;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.SystemDirectoryDao;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.SystemDirectoryPostAuthorityDao;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.SystemDirectoryUserAuthorityDao;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.TblSystemDirectoryRoleMapper;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.TblSystemDirectoryRoleRelationMapper;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.TblSystemDirectoryRoleUserMapper;
import cn.pioneeruniverse.project.entity.TblSystemDirectory;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryPostAuthority;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryRole;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryRoleRelation;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryRoleUser;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryUserAuthority;
import cn.pioneeruniverse.project.entity.TblUserInfo;
import cn.pioneeruniverse.project.feignInterface.ProjectToSystemInterface;
import cn.pioneeruniverse.project.service.assetsLibrary.SystemDirectoryAuthorityService;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 13:55 2019/12/5
 * @Modified By:ztt
 */
@Transactional(readOnly = true)
@Service("SystemDirectoryAuthorityService")
public class SystemDirectoryAuthorityServiceImpl implements SystemDirectoryAuthorityService {

    @Autowired
    SystemDirectoryUserAuthorityDao systemDirectoryUserAuthorityDao;

    @Autowired
    SystemDirectoryPostAuthorityDao systemDirectoryPostAuthorityDao;
    
    @Autowired
    TblSystemDirectoryRoleMapper tblSystemDirectoryRoleMapper;
    @Autowired
    TblSystemDirectoryRoleRelationMapper tblSystemDirectoryRoleRelationMapper;
    @Autowired
    TblSystemDirectoryRoleUserMapper tblSystemDirectoryRoleUserMapper;
    @Autowired
    SystemDirectoryDao systemDirectoryDao;

    @Autowired
    ProjectToSystemInterface projectToSystemInterface;
    
    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisUtils redisUtils;

    /**
     * @param bootStrapTablePage
     * @param directoryId 目录ID
     * @return cn.pioneeruniverse.common.entity.BootStrapTablePage<cn.pioneeruniverse.project.entity.TblSystemDirectoryUserAuthority>
     * @Description 查询文档库权限配置人员列表
     * @MethodName getDirectoryUserAuthority
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/12/5 15:45
     */
    @Override
    @Transactional(readOnly = true)
    public BootStrapTablePage<TblSystemDirectoryUserAuthority> getDirectoryUserAuthority(BootStrapTablePage<TblSystemDirectoryUserAuthority> bootStrapTablePage, Long directoryId) {
        List<TblSystemDirectoryUserAuthority> list = systemDirectoryUserAuthorityDao.selectList(new EntityWrapper<TblSystemDirectoryUserAuthority>().allEq(new HashMap<String, Object>() {{
            put("SYSTEM_DIRECTORY_ID", directoryId);
            put("STATUS", 1);
        }}));
        if (CollectionUtil.isNotEmpty(list)) {
            List<Long> userIds = (List<Long>) CollectionUtil.collect(list, o -> ((TblSystemDirectoryUserAuthority) o).getUserId());
            //List<Map<String, Object>> userInfo = projectToSystemInterface.getUserInfoByUserIds(userIds);
            List<TblUserInfoDTO> userInfo = userMapper.getUserInfoByUserIds(userIds);
            bootStrapTablePage.setOtherData(userInfo);
        }
        bootStrapTablePage.setRows(list);
        bootStrapTablePage.setTotal(list.size());
        return bootStrapTablePage;
    }

    /**
     * 
    * @Title: getDirectoryPostAuthority
    * @Description: 获取系统目录岗位权限
    * @author author
    * @param bootStrapTablePage
    * @param directoryId 目录ID
    * @return BootStrapTablePage<TblSystemDirectoryPostAuthority>
     */
    @Override
    @Transactional(readOnly = true)
    public BootStrapTablePage<TblSystemDirectoryPostAuthority> getDirectoryPostAuthority(BootStrapTablePage<TblSystemDirectoryPostAuthority> bootStrapTablePage, Long directoryId) {
        List<TblSystemDirectoryPostAuthority> list = systemDirectoryPostAuthorityDao.selectList(new EntityWrapper<TblSystemDirectoryPostAuthority>().allEq(new HashMap<String, Object>() {{
            put("SYSTEM_DIRECTORY_ID", directoryId);
            put("STATUS", 1);
        }}));
        bootStrapTablePage.setOtherData(JsonUtil.fromJson((String) redisUtils.get("TBL_PROJECT_GROUP_USER_USER_POST"), Map.class));
        bootStrapTablePage.setRows(list);
        bootStrapTablePage.setTotal(list.size());
        return bootStrapTablePage;
    }

    /**
     * 
    * @Title: addOrUpdateDirectoryUserAuthority
    * @Description: 新增或修改系统目录用户权限
    * @author author
    * @param tblSystemDirectoryUserAuthorities 用户目录权限信息
    * @param request
     */
    @Override
    @Transactional(readOnly = false)
    public void addOrUpdateDirectoryUserAuthority(List<TblSystemDirectoryUserAuthority> tblSystemDirectoryUserAuthorities, HttpServletRequest request) {
        for (TblSystemDirectoryUserAuthority tblSystemDirectoryUserAuthority : tblSystemDirectoryUserAuthorities) {
            tblSystemDirectoryUserAuthority.preInsertOrUpdate(request);
            if (tblSystemDirectoryUserAuthority.getId() != null) {
            	tblSystemDirectoryUserAuthority.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
            	tblSystemDirectoryUserAuthority.setLastUpdateDate(new Timestamp(new Date().getTime()));
            	systemDirectoryUserAuthorityDao.update(tblSystemDirectoryUserAuthority , new EntityWrapper<TblSystemDirectoryUserAuthority>().allEq(new HashMap<String, Object>() {{
                    put("ID", tblSystemDirectoryUserAuthority.getId());
                  /*  put("SYSTEM_DIRECTORY_ID", tblSystemDirectoryUserAuthority.getSystemDirectoryId());
                    put("USER_ID", tblSystemDirectoryUserAuthority.getUserId());
                    put("READ_AUTH",tblSystemDirectoryUserAuthority.getReadAuth());
                    put("WRITE_AUTH",tblSystemDirectoryUserAuthority.getWriteAuth());
                    put("STATUS", 1);
                    put("LAST_UPDATE_BY",CommonUtil.getCurrentUserId(request));
                    put("LAST_UPDATE_DATE",new Timestamp(new Date().getTime()));*/
                }}));
            } else {
            	CommonUtil.setBaseValue(tblSystemDirectoryUserAuthority, request);
                systemDirectoryUserAuthorityDao.insert(tblSystemDirectoryUserAuthority);
            }
        }
    }

    /**
     * 
    * @Title: deleteDirectoryUserAuthority
    * @Description: 删除系统目录用户权限
    * @author author
    * @param tblSystemDirectoryUserAuthorities 用户目录权限信息
     */
    @Override
    @Transactional(readOnly = false)
    public void deleteDirectoryUserAuthority(List<TblSystemDirectoryUserAuthority> tblSystemDirectoryUserAuthorities) {
        for (TblSystemDirectoryUserAuthority tblSystemDirectoryUserAuthority : tblSystemDirectoryUserAuthorities) {
            systemDirectoryUserAuthorityDao.delete(new EntityWrapper<TblSystemDirectoryUserAuthority>().allEq(new HashMap<String, Object>() {{
                put("ID", tblSystemDirectoryUserAuthority.getId());
                put("SYSTEM_DIRECTORY_ID", tblSystemDirectoryUserAuthority.getSystemDirectoryId());
                put("USER_ID", tblSystemDirectoryUserAuthority.getUserId());
            }}));
        }
    }

    /**
     * 
    * @Title: addOrUpdateDirectoryPostAuthority
    * @Description: 新增或修改系统目录岗位权限
    * @author author
    * @param tblSystemDirectoryPostAuthorities 目录岗位权限信息
    * @param request
     */
    @Override
    @Transactional(readOnly = false)
    public void addOrUpdateDirectoryPostAuthority(List<TblSystemDirectoryPostAuthority> tblSystemDirectoryPostAuthorities, HttpServletRequest request) {
        for (TblSystemDirectoryPostAuthority tblSystemDirectoryPostAuthority : tblSystemDirectoryPostAuthorities) {
            tblSystemDirectoryPostAuthority.preInsertOrUpdate(request);
            if (tblSystemDirectoryPostAuthority.getId() != null) {
            	tblSystemDirectoryPostAuthority.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
            	tblSystemDirectoryPostAuthority.setLastUpdateDate(new Timestamp(new Date().getTime()));
            	systemDirectoryPostAuthorityDao.update(tblSystemDirectoryPostAuthority, new EntityWrapper<TblSystemDirectoryPostAuthority>().allEq(new HashMap<String, Object>() {{
                    put("ID", tblSystemDirectoryPostAuthority.getId());
                   /* put("SYSTEM_DIRECTORY_ID", tblSystemDirectoryPostAuthority.getSystemDirectoryId());
                    put("USER_POST", tblSystemDirectoryPostAuthority.getUserPost());
                    put("READ_AUTH",tblSystemDirectoryPostAuthority.getReadAuth());
                    put("WRITE_AUTH",tblSystemDirectoryPostAuthority.getWriteAuth());
                    put("STATUS", 1);
                    put("LAST_UPDATE_BY",CommonUtil.getCurrentUserId(request));
                    put("LAST_UPDATE_DATE",new Timestamp(new Date().getTime()));*/
                }}));
            } else {
            	CommonUtil.setBaseValue(tblSystemDirectoryPostAuthority, request);
                systemDirectoryPostAuthorityDao.insert(tblSystemDirectoryPostAuthority);
            }
        }
    }

    /**
     * 
    * @Title: deleteDirectoryPostAuthority
    * @Description: 删除系统目录岗位权限
    * @author author
    * @param tblSystemDirectoryPostAuthorities 目录岗位权限信息
     */
    @Override
    @Transactional(readOnly = false)
    public void deleteDirectoryPostAuthority(List<TblSystemDirectoryPostAuthority> tblSystemDirectoryPostAuthorities) {
        for (TblSystemDirectoryPostAuthority tblSystemDirectoryPostAuthority : tblSystemDirectoryPostAuthorities) {
            systemDirectoryPostAuthorityDao.delete(new EntityWrapper<TblSystemDirectoryPostAuthority>().allEq(new HashMap<String, Object>() {{
                put("ID", tblSystemDirectoryPostAuthority.getId());//岗位权限表主键
                put("SYSTEM_DIRECTORY_ID", tblSystemDirectoryPostAuthority.getSystemDirectoryId());//系统目录ID
                put("USER_POST", tblSystemDirectoryPostAuthority.getUserPost());//岗位
            }}));
        }
    }

    /**
     * 
    * @Title: getCurrentUserDirectoryAuthority
    * @Description: 获取当前用户的系统目录权限
    * @author author
    * @param request
    * @param projectId 项目ID
    * @param directoryId 目录ID
    * @return null
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Boolean> getCurrentUserDirectoryAuthority(HttpServletRequest request, Long projectId, Long directoryId) {
        int readAuth = 0;
        int writeAuth = 0;
        Map<Integer, Integer> map = new HashMap<Integer, Integer>() {{
            put(1, 1);
            put(2, 0);
        }};
        Long userId = (Long) CommonUtil.getCurrentUser(request).get("id");
        TblSystemDirectoryUserAuthority tblSystemDirectoryUserAuthority = new TblSystemDirectoryUserAuthority();
        tblSystemDirectoryUserAuthority.setSystemDirectoryId(directoryId);
        tblSystemDirectoryUserAuthority.setUserId(userId);
        tblSystemDirectoryUserAuthority = systemDirectoryUserAuthorityDao.selectOne(tblSystemDirectoryUserAuthority);
        readAuth |= (tblSystemDirectoryUserAuthority.getReadAuth() == null ? 0 : (map.get(tblSystemDirectoryUserAuthority.getReadAuth()) == null ? 0 : map.get(tblSystemDirectoryUserAuthority.getReadAuth())));
        writeAuth |= (tblSystemDirectoryUserAuthority.getWriteAuth() == null ? 0 : (map.get(tblSystemDirectoryUserAuthority.getWriteAuth()) == null ? 0 : map.get(tblSystemDirectoryUserAuthority.getWriteAuth())));

        return null;
    }

    /**
     * 
    * @Title: addOrUpdateSystemDirectoryRole
    * @Description: 添加或修改系统目录用户角色
    * @author author
    * @param tblSystemDirectoryRole 系统目录角色
    * @param request
     */
	@Override
	 @Transactional(readOnly = false)
	public void addOrUpdateSystemDirectoryRole(TblSystemDirectoryRole tblSystemDirectoryRole,HttpServletRequest request) {
		if (tblSystemDirectoryRole.getId()!=null) {
			tblSystemDirectoryRole.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
			tblSystemDirectoryRole.setLastUpdateDate(new Timestamp(new Date().getTime()));
			tblSystemDirectoryRoleMapper.updateByPrimaryKeySelective(tblSystemDirectoryRole);
		}else {
			CommonUtil.setBaseValue(tblSystemDirectoryRole, request);
			tblSystemDirectoryRoleMapper.insert(tblSystemDirectoryRole);
		}
		
	}

	/**
	 * 
	* @Title: getAllSystemDirectoryRole
	* @Description: 获取当前目录下所有的角色。
	* @author author
	* @param projectId 项目ID
	* @return List<TblSystemDirectoryRole>系统目录角色信息
	*/
	@Override
	public List<TblSystemDirectoryRole> getAllSystemDirectoryRole(Long projectId) {
		return tblSystemDirectoryRoleMapper.getAllByProjectId(projectId);
	}

	/**
	 * 
	* @Title: getSystemDirectoryRoleUserByRoleId
	* @Description: 获取该系统目录下拥有该角色权限的所有用户
	* @author author
	* @param roleId 角色ID
	* @param pageNumber 当前页
	* @param pageSize 每页数量
	* @return List<TblUserInfo>用户信息列表
	 */
	@Override
	public List<TblUserInfo> getSystemDirectoryRoleUserByRoleId(Long roleId,Integer pageNumber, Integer pageSize) {
		Map<String, Object> map = new HashMap<>();
		
		if(pageNumber!=null && pageSize!=null) {
			map.put("start", (pageNumber-1)*pageSize);
			map.put("pageSize", pageSize);
		}
		map.put("roleId", roleId);
		return tblSystemDirectoryRoleUserMapper.getSystemDirectoryRoleUserByRoleId(map);
	}

	/**
	 * 
	* @Title: getRoleUserCountByRoleId
	* @Description: 获取用户角色数量
	* @author author
	* @param roleId 角色ID
	* @return
	 */
	@Override
	public Long getRoleUserCountByRoleId(Long roleId) {
		
		return tblSystemDirectoryRoleUserMapper.getRoleUserCountByRoleId(roleId);
	}

	/**
	 * 
	* @Title: getSystemDirectoryRoleRelationByRoleId
	* @Description: 获取该角色下关联的系统目录
	* @author author
	* @param roleId 角色ID
	* @return List<TblSystemDirectoryRoleRelation>系统目录角色关联信息
	 */
	@Override
	public List<TblSystemDirectoryRoleRelation> getSystemDirectoryRoleRelationByRoleId(Long roleId) {
		
		return tblSystemDirectoryRoleRelationMapper.getSystemDirectoryRoleRelationByRoleId(roleId);
	}

	/**
	 * 
	* @Title: getSystemDirectoryAuth
	* @Description: 获取该角色，该项目下系统目录权限
	* @author author
	* @param projectId项目ID
	* @param roleId角色ID
	* @return List<TblSystemDirectory>系统目录信息
	 */
	@Override
	public List<TblSystemDirectory> getSystemDirectoryAuth(Long projectId, Long roleId) {	
		//项目下的所有系统目录
		//此方法在mapper中限制了项目类型为1：运维类。但是在新建类项目-权限设置（按目录）也用到了该方法，是一个bug
		//List<TblSystemDirectory> systemDirectorys = systemDirectoryDao.getAllDirectoryByProjectId(projectId);
		List<TblSystemDirectory> systemDirectorys = systemDirectoryDao.getAllDirectoryByNewProjectId(projectId);
		//获取角色和系统目录的关联关系
		List<TblSystemDirectoryRoleRelation> roleRelations = tblSystemDirectoryRoleRelationMapper.getSystemDirectoryRoleRelationByRoleId(roleId);
		//查询该项目下所有目录在该角色下的读写权限
		for (TblSystemDirectory tblSystemDirectory : systemDirectorys) {
			//Map<String, Object> map = new HashMap<>();
			//map.put("systemDirectory", tblSystemDirectory);
			if(tblSystemDirectory.getParentId() == null) {
				tblSystemDirectory.setLevel(1);
			}else {
				tblSystemDirectory.setLevel(tblSystemDirectory.getParentIds().split(",").length);
			}
			//获取子目录，以树的形式返回
			List<TblSystemDirectory> sonSystemDirectory = systemDirectoryDao.getSonDirectory(projectId,tblSystemDirectory.getId());
			if(!sonSystemDirectory.isEmpty()) {
				tblSystemDirectory.setLeaf(false);
				tblSystemDirectory.setIsLeaf(false);
			}else {
				tblSystemDirectory.setLeaf(true);
				tblSystemDirectory.setIsLeaf(true);
			}
			tblSystemDirectory.setLoaded(true);
			tblSystemDirectory.setExpanded(false);
			tblSystemDirectory.setIsSelect(true);//右侧有按钮
			//是否有读写读写权限
			boolean flag = false;
			for (TblSystemDirectoryRoleRelation tblSystemDirectoryRoleRelation : roleRelations) {
				if(tblSystemDirectory.getId().longValue() == tblSystemDirectoryRoleRelation.getSystemDirectoryId().longValue()) {//如果该目录在权限关系表中有数据 就直接取读写权限数据
					tblSystemDirectory.setReadAuth(tblSystemDirectoryRoleRelation.getReadAuth());
					tblSystemDirectory.setWriteAuth(tblSystemDirectoryRoleRelation.getWriteAuth());
					flag = true;
				}
			}
			if(!flag) {//如果该目在权限关系表中没有数据 读写权限直接设为2否
				tblSystemDirectory.setReadAuth(2);
				tblSystemDirectory.setWriteAuth(2);
				
			}
			
		}
		return systemDirectorys;
	}

	/**
	 * 
	* @Title: addSystemDirectoryRoleUser
	* @Description: 批量增加系统目录角色用户
	* @author author
	* @param roleId 角色ID
	* @param uids  多个用户ID，以,隔开
	* @param request
	 */
	@Override
	@Transactional(readOnly = false)
	public void addSystemDirectoryRoleUser(Long roleId, String uids, HttpServletRequest request) {
		if (roleId!=null && StringUtils.isNotBlank(uids)) {
			List<TblSystemDirectoryRoleUser> list = new ArrayList<>();
			String[] arr = uids.split(",");
			for (String userId : arr) {
				TblSystemDirectoryRoleUser systemDirectoryRoleUser = new TblSystemDirectoryRoleUser();
				systemDirectoryRoleUser.setSystemDirectoryRoleId(roleId);
				systemDirectoryRoleUser.setUserId(Long.parseLong(userId));
				CommonUtil.setBaseValue(systemDirectoryRoleUser, request);
				list.add(systemDirectoryRoleUser);
			}
			tblSystemDirectoryRoleUserMapper.batchInsert(list);
		}
		
	}

	/**
	 * 
	* @Title: cancelSystemDirectoryRoleUser
	* @Description: 取消用户角色关联
	* @author author
	* @param roleId 角色ID
	* @param uids 用户IDS
	* @param request
	 */
	@Override
	@Transactional(readOnly = false)
	public void cancelSystemDirectoryRoleUser(Long roleId, String uids, HttpServletRequest request) {
		if (roleId!=null && StringUtils.isNotBlank(uids)) {
			tblSystemDirectoryRoleUserMapper.cancelSystemDirectoryRoleUser(roleId,uids);
		}
		
	}
    
	/**
	 * 
	* @Title: saveSystemDirectoryAuth
	* @Description: 保存系统目录权限
	* @author author
	* @param systemDirectoryRoleRelations
	* @param roleId
	* @param request
	 */
	@Override
	@Transactional(readOnly = false)
	public void saveSystemDirectoryAuth(List<TblSystemDirectoryRoleRelation> systemDirectoryRoleRelations,
			Long roleId, HttpServletRequest request) {
		//先把原来的该角色下的权限数据都删掉
		tblSystemDirectoryRoleRelationMapper.deleteByRoleId(roleId);
		//把前台传的有权限的新增到数据库
		for (TblSystemDirectoryRoleRelation tblSystemDirectoryRoleRelation : systemDirectoryRoleRelations) {
			CommonUtil.setBaseValue(tblSystemDirectoryRoleRelation, request);
		}
		tblSystemDirectoryRoleRelationMapper.batchInsert(systemDirectoryRoleRelations);
		
	}

	/**
	 * 
	* @Title: getCurrentUserSystemDirecoryAuth
	* @Description: 获取当前用户的系统目录权限读和写
	* @author author
	* @param projectId
	* @param systemDirectoryId
	* @param request
	* @return Map<String, Object>
	 */
	@Override
	public Map<String, Object> getCurrentUserSystemDirecoryAuth(Long projectId, Long systemDirectoryId, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		//获取当前登录用户
		Long uid = CommonUtil.getCurrentUserId(request);
		//找到在该项目下当前登录用户所属角色 查询角色的权限
		List<TblSystemDirectoryRoleRelation> systemDirectoryRoleRelations = tblSystemDirectoryRoleRelationMapper.getCurentUserRoleRelations(projectId,systemDirectoryId,uid);
		if (!systemDirectoryRoleRelations.isEmpty()) {
			if (systemDirectoryRoleRelations.size() == 1) {
				//有权限
				map.put("readAuth", systemDirectoryRoleRelations.get(0).getReadAuth());
				map.put("writeAuth", systemDirectoryRoleRelations.get(0).getWriteAuth());
			}else {
				map.put("message", "查询到数据不止一条，数据错误");
			}
		}else {
			//无权限
			map.put("readAuth", 2);
			map.put("writeAuth", 2);
		}
		return map;
	}
}
