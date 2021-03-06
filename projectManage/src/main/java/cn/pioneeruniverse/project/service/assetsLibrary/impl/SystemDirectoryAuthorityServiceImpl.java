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
     * @param directoryId ??????ID
     * @return cn.pioneeruniverse.common.entity.BootStrapTablePage<cn.pioneeruniverse.project.entity.TblSystemDirectoryUserAuthority>
     * @Description ???????????????????????????????????????
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
    * @Description: ??????????????????????????????
    * @author author
    * @param bootStrapTablePage
    * @param directoryId ??????ID
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
    * @Description: ???????????????????????????????????????
    * @author author
    * @param tblSystemDirectoryUserAuthorities ????????????????????????
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
    * @Description: ??????????????????????????????
    * @author author
    * @param tblSystemDirectoryUserAuthorities ????????????????????????
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
    * @Description: ???????????????????????????????????????
    * @author author
    * @param tblSystemDirectoryPostAuthorities ????????????????????????
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
    * @Description: ??????????????????????????????
    * @author author
    * @param tblSystemDirectoryPostAuthorities ????????????????????????
     */
    @Override
    @Transactional(readOnly = false)
    public void deleteDirectoryPostAuthority(List<TblSystemDirectoryPostAuthority> tblSystemDirectoryPostAuthorities) {
        for (TblSystemDirectoryPostAuthority tblSystemDirectoryPostAuthority : tblSystemDirectoryPostAuthorities) {
            systemDirectoryPostAuthorityDao.delete(new EntityWrapper<TblSystemDirectoryPostAuthority>().allEq(new HashMap<String, Object>() {{
                put("ID", tblSystemDirectoryPostAuthority.getId());//?????????????????????
                put("SYSTEM_DIRECTORY_ID", tblSystemDirectoryPostAuthority.getSystemDirectoryId());//????????????ID
                put("USER_POST", tblSystemDirectoryPostAuthority.getUserPost());//??????
            }}));
        }
    }

    /**
     * 
    * @Title: getCurrentUserDirectoryAuthority
    * @Description: ???????????????????????????????????????
    * @author author
    * @param request
    * @param projectId ??????ID
    * @param directoryId ??????ID
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
    * @Description: ???????????????????????????????????????
    * @author author
    * @param tblSystemDirectoryRole ??????????????????
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
	* @Description: ???????????????????????????????????????
	* @author author
	* @param projectId ??????ID
	* @return List<TblSystemDirectoryRole>????????????????????????
	*/
	@Override
	public List<TblSystemDirectoryRole> getAllSystemDirectoryRole(Long projectId) {
		return tblSystemDirectoryRoleMapper.getAllByProjectId(projectId);
	}

	/**
	 * 
	* @Title: getSystemDirectoryRoleUserByRoleId
	* @Description: ????????????????????????????????????????????????????????????
	* @author author
	* @param roleId ??????ID
	* @param pageNumber ?????????
	* @param pageSize ????????????
	* @return List<TblUserInfo>??????????????????
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
	* @Description: ????????????????????????
	* @author author
	* @param roleId ??????ID
	* @return
	 */
	@Override
	public Long getRoleUserCountByRoleId(Long roleId) {
		
		return tblSystemDirectoryRoleUserMapper.getRoleUserCountByRoleId(roleId);
	}

	/**
	 * 
	* @Title: getSystemDirectoryRoleRelationByRoleId
	* @Description: ???????????????????????????????????????
	* @author author
	* @param roleId ??????ID
	* @return List<TblSystemDirectoryRoleRelation>??????????????????????????????
	 */
	@Override
	public List<TblSystemDirectoryRoleRelation> getSystemDirectoryRoleRelationByRoleId(Long roleId) {
		
		return tblSystemDirectoryRoleRelationMapper.getSystemDirectoryRoleRelationByRoleId(roleId);
	}

	/**
	 * 
	* @Title: getSystemDirectoryAuth
	* @Description: ????????????????????????????????????????????????
	* @author author
	* @param projectId??????ID
	* @param roleId??????ID
	* @return List<TblSystemDirectory>??????????????????
	 */
	@Override
	public List<TblSystemDirectory> getSystemDirectoryAuth(Long projectId, Long roleId) {	
		//??????????????????????????????
		//????????????mapper???????????????????????????1???????????????????????????????????????-????????????????????????????????????????????????????????????bug
		//List<TblSystemDirectory> systemDirectorys = systemDirectoryDao.getAllDirectoryByProjectId(projectId);
		List<TblSystemDirectory> systemDirectorys = systemDirectoryDao.getAllDirectoryByNewProjectId(projectId);
		//??????????????????????????????????????????
		List<TblSystemDirectoryRoleRelation> roleRelations = tblSystemDirectoryRoleRelationMapper.getSystemDirectoryRoleRelationByRoleId(roleId);
		//????????????????????????????????????????????????????????????
		for (TblSystemDirectory tblSystemDirectory : systemDirectorys) {
			//Map<String, Object> map = new HashMap<>();
			//map.put("systemDirectory", tblSystemDirectory);
			if(tblSystemDirectory.getParentId() == null) {
				tblSystemDirectory.setLevel(1);
			}else {
				tblSystemDirectory.setLevel(tblSystemDirectory.getParentIds().split(",").length);
			}
			//???????????????????????????????????????
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
			tblSystemDirectory.setIsSelect(true);//???????????????
			//???????????????????????????
			boolean flag = false;
			for (TblSystemDirectoryRoleRelation tblSystemDirectoryRoleRelation : roleRelations) {
				if(tblSystemDirectory.getId().longValue() == tblSystemDirectoryRoleRelation.getSystemDirectoryId().longValue()) {//????????????????????????????????????????????? ??????????????????????????????
					tblSystemDirectory.setReadAuth(tblSystemDirectoryRoleRelation.getReadAuth());
					tblSystemDirectory.setWriteAuth(tblSystemDirectoryRoleRelation.getWriteAuth());
					flag = true;
				}
			}
			if(!flag) {//????????????????????????????????????????????? ????????????????????????2???
				tblSystemDirectory.setReadAuth(2);
				tblSystemDirectory.setWriteAuth(2);
				
			}
			
		}
		return systemDirectorys;
	}

	/**
	 * 
	* @Title: addSystemDirectoryRoleUser
	* @Description: ????????????????????????????????????
	* @author author
	* @param roleId ??????ID
	* @param uids  ????????????ID??????,??????
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
	* @Description: ????????????????????????
	* @author author
	* @param roleId ??????ID
	* @param uids ??????IDS
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
	* @Description: ????????????????????????
	* @author author
	* @param systemDirectoryRoleRelations
	* @param roleId
	* @param request
	 */
	@Override
	@Transactional(readOnly = false)
	public void saveSystemDirectoryAuth(List<TblSystemDirectoryRoleRelation> systemDirectoryRoleRelations,
			Long roleId, HttpServletRequest request) {
		//???????????????????????????????????????????????????
		tblSystemDirectoryRoleRelationMapper.deleteByRoleId(roleId);
		//?????????????????????????????????????????????
		for (TblSystemDirectoryRoleRelation tblSystemDirectoryRoleRelation : systemDirectoryRoleRelations) {
			CommonUtil.setBaseValue(tblSystemDirectoryRoleRelation, request);
		}
		tblSystemDirectoryRoleRelationMapper.batchInsert(systemDirectoryRoleRelations);
		
	}

	/**
	 * 
	* @Title: getCurrentUserSystemDirecoryAuth
	* @Description: ????????????????????????????????????????????????
	* @author author
	* @param projectId
	* @param systemDirectoryId
	* @param request
	* @return Map<String, Object>
	 */
	@Override
	public Map<String, Object> getCurrentUserSystemDirecoryAuth(Long projectId, Long systemDirectoryId, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		//????????????????????????
		Long uid = CommonUtil.getCurrentUserId(request);
		//??????????????????????????????????????????????????? ?????????????????????
		List<TblSystemDirectoryRoleRelation> systemDirectoryRoleRelations = tblSystemDirectoryRoleRelationMapper.getCurentUserRoleRelations(projectId,systemDirectoryId,uid);
		if (!systemDirectoryRoleRelations.isEmpty()) {
			if (systemDirectoryRoleRelations.size() == 1) {
				//?????????
				map.put("readAuth", systemDirectoryRoleRelations.get(0).getReadAuth());
				map.put("writeAuth", systemDirectoryRoleRelations.get(0).getWriteAuth());
			}else {
				map.put("message", "??????????????????????????????????????????");
			}
		}else {
			//?????????
			map.put("readAuth", 2);
			map.put("writeAuth", 2);
		}
		return map;
	}
}
