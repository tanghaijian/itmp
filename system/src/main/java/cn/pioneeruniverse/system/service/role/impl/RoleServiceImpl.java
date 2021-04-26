package cn.pioneeruniverse.system.service.role.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.PageWithBootStrap;
import cn.pioneeruniverse.common.utils.PinYinUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.system.dao.mybatis.defaultPage.UserDefaultPageDao;
import cn.pioneeruniverse.system.dao.mybatis.menu.MenuDao;
import cn.pioneeruniverse.system.dao.mybatis.role.RoleDao;
import cn.pioneeruniverse.system.dao.mybatis.user.UserDao;
import cn.pioneeruniverse.system.entity.TblMenuButtonInfo;
import cn.pioneeruniverse.system.entity.TblRoleInfo;
import cn.pioneeruniverse.system.entity.TblRoleMenuButton;
import cn.pioneeruniverse.system.entity.TblUserDefaultPage;
import cn.pioneeruniverse.system.entity.TblUserInfo;
import cn.pioneeruniverse.system.service.role.IMenuRoleService;
import cn.pioneeruniverse.system.service.role.IRoleService;
import cn.pioneeruniverse.system.service.role.IUserRoleService;


@Service("iRoleService")
@Transactional(readOnly = true)
public class RoleServiceImpl extends ServiceImpl<RoleDao, TblRoleInfo> implements IRoleService{

	public static final Integer MENU_TYPE = 1;
	public static final Integer BUTTON_TYPE = 2;

	@Autowired
	private RoleDao roleDao;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private RedisUtils redisUtils;
	@Autowired
	private IMenuRoleService iMenuRoleService;
	@Autowired
	private IUserRoleService iUserRoleService;
	@Autowired
	private UserDefaultPageDao userDefaultPageDao;

	private Long roleId = null;


	/**
	 * 
	* @Title: findRoleById
	* @Description: 通过ID获取角色信息
	* @author author
	* @param id 角色ID
	* @return TblRoleInfo 角色信息
	 */
	@Override
	public TblRoleInfo findRoleById(Long id) {
		return roleDao.findRoleById(id);
	}

	/**
	 * 
	* @Title: findUserRole
	* @Description: 获取某人所拥有角色信息，包括无效
	* @author author
	* @param userId 用户ID
	* @return List<TblRoleInfo> 角色列表
	 */
	@Override
	public List<TblRoleInfo> findUserRole(Long userId) {
		return roleDao.findUserRole(userId);
	}

	/**
	 * 
	* @Title: getUserAllRole
	* @Description: 获取某人所拥有的有效的角色信息
	* @author author
	* @param userId 用户ID
	* @return List<TblRoleInfo>角色列表
	 */
	@Transactional(readOnly = true)
	@Override
	public List<TblRoleInfo> getUserAllRole(Long userId) {
		return roleDao.getUserAllRole(userId);
	}

	/**
	 * 
	* @Title: getRoleWithMenu
	* @Description: 获取所有角色以及该角色下所具有的菜单信息
	* @author author
	* @return List<TblRoleInfo>角色信息列表
	 */
	@Override
	public List<TblRoleInfo> getRoleWithMenu() {
		return roleDao.getRoleWithMenu();
	}

	/**
	 * 
	* @Title: insertRole
	* @Description: 保存角色
	* @author author
	* @param role 角色信息
	* @param request
	* @return Boolean  true插入成功，false插入失败
	 */
	@Override
	@Transactional(readOnly = false)
	public Boolean insertRole(TblRoleInfo role, HttpServletRequest request) {
		String empNo = PinYinUtil.getPingYin(role.getRoleName()).toUpperCase(Locale.ENGLISH);
		role = (TblRoleInfo) CommonUtil.setBaseValue(role,request);
		role.setRoleCode(empNo);
		TblRoleInfo roleInfo = roleDao.findRoleByName(role);
		//不存在才插入
		if (roleInfo == null){
			roleDao.insertRole(role);
		} else {
			return false;
		}
		
		//插入角色和菜单的关联关系
		String menuIds = role.getMenuIds();
		if(StringUtils.isNotBlank(menuIds)){
			List<TblRoleMenuButton> rms = new ArrayList<TblRoleMenuButton>();
			String[] mids = menuIds.split(",");
			Long roleId = role.getId();
			for(String menuId:mids){
				TblRoleMenuButton roleMenu = new TblRoleMenuButton();
				roleMenu.setCreateBy(role.getCreateBy());
				roleMenu.setLastUpdateBy(role.getCreateBy());
				roleMenu.setMenuButtonId(Long.valueOf(menuId));
				roleMenu.setRoleId(roleId);
				roleMenu.setStatus(role.getStatus());
				rms.add(roleMenu);
			}
			iMenuRoleService.insertMenuRole(rms);
		}
		return true;
	}

	private String selectMaxEmpNo() {
		return roleDao.selectMaxEmpNo();
	}

	/**
	 * 
	* @Title: updateRole
	* @Description: 更新角色
	* @author author
	* @param role 需要更新的角色
	* @param request
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateRole(TblRoleInfo role, HttpServletRequest request) {
		role.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		roleDao.updateRole(role);
	}

	/**
	 * 
	* @Title: getRoleByMenuId
	* @Description: 通过菜单获取与该菜单关联的角色信息
	* @author author
	* @param menuId 菜单ID
	* @return List<TblRoleInfo>角色列表
	 */
	@Override
	public List<TblRoleInfo> getRoleByMenuId(Long menuId) {
		return roleDao.getRoleByMenuId(menuId);
	}

	
	/**
	 * 
	* @Title: getAllRole
	* @Description: 获取所有角色列表，并以分页返回
	* @author author
	* @param bean 角色信息
	* @param pageIndex 第几页
	* @param pageSize 每页条数
	* @return  PageInfo<TblRoleInfo> 封装的分页信息
	 */
	@Override
	public PageInfo<TblRoleInfo> getAllRole(TblRoleInfo bean, Integer pageIndex,Integer pageSize) {
		if(pageIndex != null && pageSize != null)
			PageHelper.startPage(pageIndex, pageSize);
		List<TblRoleInfo> list =  roleDao.getAllRole(bean);
		roleId = list.get(0).getId();
		PageInfo<TblRoleInfo> pageInfo = new PageInfo<TblRoleInfo>(list);
		return pageInfo;
	}

	
	/**
	 * 
	* @Title: deleteRoles
	* @Description:删除选定的角色信息
	* @author author
	* @param roleIds 选定的角色ID
	* @param lastUpdateBy 删除人员
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteRoles(List<Long> roleIds,Long lastUpdateBy) {
		if(roleIds != null && !roleIds.isEmpty()){
			//删除角色
			roleDao.delRole(roleIds,lastUpdateBy);
			//删除角色与菜单的关联关系
			iMenuRoleService.delMenuByRoleIds(roleIds,lastUpdateBy);
			//删除角色与用户的关联关系
			iUserRoleService.delUserRoleByRoleId(roleIds,lastUpdateBy);
		}
	}

	
	/**
	 * 
	* @Title: getRoleMenu
	* @Description: 获取菜单所应的角色信息
	* @author author
	* @param id 菜单ID
	* @return
	 */
	@Override
	public List<TblRoleInfo> getRoleMenu(Long id) {
		Map<String,Object> map = new HashMap<>();
		Map<String,TblMenuButtonInfo> resultMap = new LinkedHashMap<String,TblMenuButtonInfo>();
		List<TblMenuButtonInfo> button_list = new ArrayList<>();
		TblRoleInfo roleInfo = new TblRoleInfo();
		if( id == null && roleId != null){
			id = roleId;
		}

		map.put("id",id);
		map.put("menuType",MENU_TYPE);
		//获取一级菜单
		List<TblMenuButtonInfo> menuList = menuDao.getMenusWithTYPE(map);
		//递归获取子菜单
		resultMap = menuList(menuList,resultMap);

		List<TblRoleInfo> menu_list = new ArrayList<TblRoleInfo>();
		
		//组装树形结构
		for(Map.Entry<String,TblMenuButtonInfo> entry:resultMap.entrySet()){
			roleInfo.setId(entry.getValue().getId());
			roleInfo.setLoaded(true);
			roleInfo.setExpanded(false);
			roleInfo.setMenu(entry.getValue().getMenuButtonName());
			roleInfo.setParent(entry.getValue().getParentId());

			String parentIds;
			if (entry.getValue().getParentIds() == null || entry.getValue().getParentIds().equals("")){
				parentIds =  entry.getValue().getId()+",";
				roleInfo.setLevel(0);
			} else {
				parentIds = entry.getValue().getParentIds()+entry.getValue().getId()+",";
				String[] parendIdsArr = entry.getValue().getParentIds().split(",");
				roleInfo.setLevel(parendIdsArr.length);
			}
			List<TblRoleInfo> childrenList = menuDao.findRoleMenu(parentIds);
			if (childrenList != null && childrenList.size() != 0){
				roleInfo.setLeaf(false);
			}else{
				roleInfo.setLeaf(true);
			}
			map.put("id",id);
			map.put("buttonId",entry.getValue().getId());
			String dd = entry.getValue().getId().toString();
			TblMenuButtonInfo  childrenMenu= menuDao.getButtonByRid(map);
			if (childrenMenu == null ){
				roleInfo.setIsSelect(false);
			} else {
				roleInfo.setIsSelect(true);
			}
			//按钮
			map.put("buttonType",BUTTON_TYPE);
			map.put("parentId",entry.getValue().getId());
			List<TblMenuButtonInfo> buttonList = menuDao.getButton(map);
			if (buttonList != null && !buttonList.isEmpty()) {
				for (TblMenuButtonInfo tb : buttonList) {
					// 查询是否有权限
					map.put("buttonId", tb.getId());
					TblMenuButtonInfo button = menuDao.getButtonByRid(map);
					if (button == null) {
						tb.setIsSelect(false);
					} else {
						tb.setIsSelect(true);
					}
					button_list.add(tb);
				}
				roleInfo.setButtonList(button_list);
				button_list = new ArrayList<>();
			}
			roleInfo.setLoaded(true);
			roleInfo.setExpanded(false);
			roleInfo.setId(entry.getValue().getId());
			roleInfo.setMenu(entry.getValue().getMenuButtonName());
			roleInfo.setParent(entry.getValue().getParentId());
			menu_list.add(roleInfo);
			roleInfo = new TblRoleInfo();

		}
		return menu_list;
	}

	/**
	 * 
	* @Title: menuList
	* @Description: 
	* @author author
	* @param menuList 父菜单列表
	* @param resultMap 返回列表
	* @return Map<String,TblMenuButtonInfo>  key menuType :1菜单
	*                                            parentId ：父菜单
	*                                            menu_菜单ID： 菜单详情
	 */
	public Map<String,TblMenuButtonInfo> menuList(List<TblMenuButtonInfo> menuList,Map<String,TblMenuButtonInfo> resultMap){
		Map<String,Object> map = new HashMap<>();
		map.put("menuType",MENU_TYPE);
		if(menuList != null && menuList.size() != 0){
			for (TblMenuButtonInfo tblMenuButtonInfo:menuList){
				if (tblMenuButtonInfo.getParentIds() != null){
					map.put("parentId",tblMenuButtonInfo.getParentIds()+tblMenuButtonInfo.getId()+",");
				} else {
					map.put("parentId",tblMenuButtonInfo.getId()+",");
				}
				resultMap.put("menu_"+tblMenuButtonInfo.getId(),tblMenuButtonInfo);
				List<TblMenuButtonInfo> childrenMenus = menuDao.getChildrenMenus(map);
				menuList(childrenMenus,resultMap);
			}
		}
		return resultMap;
	}



	/**
	 * 单个角色获取用户
	 * @param id 角色id
	 * @param startIndex 第几页
	 * @param pageSize 每页数量
	 * @return List<TblUserInfo> 返回查询的关联的人员列表
	 */
	@Override
	public List<TblUserInfo> getRoleUser(Long id, Integer startIndex, Integer pageSize) {
		Map<String,Object> map = new HashMap<String,Object>();
		map = PageWithBootStrap.setPageNumberSize(map,startIndex,pageSize);
		map.put("roleId",id);
		return userDao.getUserByRoleId(map);
	}

	/**
	 * 单个角色添加关联人员
	 * @param userId 人员的id
	 * @param id 角色id
	 * @param request
	 */
	@Override
	@Transactional(readOnly = false)
	public void insertRoleUser(Long[] userId, Long id, HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		for(int i = 0,len = userId.length;i < len;i++){
			map.put("useId",userId[i]);
			map.put("roleId",id);
			map.put("createBy",CommonUtil.getCurrentUserId(request));
			map.put("lastUpdateBy",CommonUtil.getCurrentUserId(request));
			TblRoleInfo role = roleDao.getRoleUserById(map);
			if (role == null){
				map.put("status",1);
				roleDao.insertRoleUser(map);
			}else{
				roleDao.updateRoleUser(map);
				role = new TblRoleInfo();
			}
		}
	}


	/**
	 * 单个角色获取为关联的人员
	 * @param roleId 角色id
	 * @param userInfo 人员条件
	 * @param pageNumber 第几页
	 * @param pageSize 每页数量
	 * @return List<TblUserInfo> 返回查询的人员列表
	 */
	@Override
	public List<TblUserInfo> findUserWithNoRole(Long roleId, TblUserInfo userInfo, Integer pageNumber, Integer pageSize) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("roleId",roleId);
		map.put("user",userInfo);
		map = PageWithBootStrap.setPageNumberSize(map,pageNumber,pageSize);
		List<TblUserInfo> list = userDao.findUserWithNoRole(map);
		return list;
	}

	/**
	 * 修改关联的人员
	 * @param userId 用户id
	 * @param roleId 角色id
	 * @param request
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateRoleWithUser(Long[] userId, Long roleId, HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("roleId",roleId);
		map.put("userId",userId);
		map.put("lastUpdateBy",CommonUtil.getCurrentUserId(request));
		roleDao.updateRoleWithUser(map);
	}

	/**
	 *  单个角色修改菜单按钮关联关系
	 * @param id 角色id
	 * @param menuIds 菜单按钮id
	 * @param request
	 * @return true：成功 false：失败
	 */
	private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class) ;
	@Override
	@Transactional(readOnly = false)
	public void updateRoleMenu(Long id, Long[] menuIds, HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("roleId",id);
		map.put("menuId",menuIds);
		map.put("lastUpdateBy",CommonUtil.getCurrentUserId(request));
		//先删除，再添加
		roleDao.deleteRoleMenu(map);
		if (menuIds != null ){
			roleDao.insertRoleMenu(map);
		}
		//this.authRoleMenu();
	}

	
	/**
	 * 
	* @Title: authRoleMenu
	* @Description: 初始化鉴权url，并放于redis中，在路由过滤时使用
	*               格式{key:TBL_ROLE_INFO-角色ID，value:该角色下的菜单url以,隔开}
	* @author author
	 */
	@Override
	public void authRoleMenu() {
		List<TblRoleInfo> roleList = roleDao.getRoleWithMenu();
		if(roleList != null){
			for (TblRoleInfo tblRoleInfo : roleList) {
				String roleId = Constants.ITMP_REDIS_ROLE_MENU_PREFIX + tblRoleInfo.getId();
				StringBuilder urlSB = new StringBuilder(",");
				if (tblRoleInfo.getMenuList() != null) {
					for (TblMenuButtonInfo tblMenuButtonInfo : tblRoleInfo.getMenuList()) {
						urlSB.append(tblMenuButtonInfo.getUrl()).append(",");
					}
				}
				redisUtils.set(roleId, urlSB.toString());
			}
		}
	}
	/**
	 * 获取系统管理员角色
	 */
	@Override
	public void getAdminRole() {
		redisUtils.set("adminRole", roleDao.getAdminRole());
	}

	
	/**
	 * 
	* @Title: updateRoleName
	* @Description: 修改角色信息
	* @author author
	* @param roleInfo 角色信息
	* @param request
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateRoleName(TblRoleInfo roleInfo, HttpServletRequest request) {
		String empo = PinYinUtil.getPingYin(roleInfo.getRoleName()).toUpperCase(Locale.ENGLISH);
		roleInfo.setRoleCode(empo);
		roleInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		roleInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));
		roleDao.updateRole(roleInfo);
		if(roleInfo.getId().longValue() == 1){
			this.getAdminRole();
		}
	}

	/**
	 * 
	* @Title: getUserMenu
	* @Description: 获取某个用户所拥有的菜单，并以树形返回
	* @author author
	* @param request
	* @return List<TblMenuButtonInfo> 菜单信息
	 */
	@Override
	public List<TblMenuButtonInfo> getUserMenu(HttpServletRequest request) {
		// TODO Auto-generated method stub
		List<TblMenuButtonInfo> list = menuDao.getUserMenu2(CommonUtil.getCurrentUserId(request));
		List<Long> menuList = userDefaultPageDao.getDefaultPage(CommonUtil.getCurrentUserId(request));
		for (TblMenuButtonInfo tblMenuButtonInfo : list) {
			tblMenuButtonInfo.setLoaded(true);
			tblMenuButtonInfo.setExpanded(false);
			String parentIds = tblMenuButtonInfo.getParentIds();
			if(parentIds==null || parentIds.equals("")) {
				tblMenuButtonInfo.setLevel(1);
			}else {
				tblMenuButtonInfo.setLevel(parentIds.split(",").length+1);
			}
			List<TblMenuButtonInfo> sons = menuDao.selectSonMenu(tblMenuButtonInfo.getId());
			if(sons.size()!=0 && sons!=null) {
				tblMenuButtonInfo.setLeaf(false);
			}else {
				tblMenuButtonInfo.setLeaf(true);
			}
			if(menuList.contains(tblMenuButtonInfo.getId())) {
				tblMenuButtonInfo.setIsSelect(true);
			}else {
				tblMenuButtonInfo.setIsSelect(false);
			}
			
		}
		return list;
	}

	/**
	 * 
	* @Title: saveDefaultPage
	* @Description: 保存用户默认页面
	* @author author
	* @param list 页面列表
	* @param userId 用户ID
	 */
	@Override
	@Transactional(readOnly = false)
	public void saveDefaultPage(List<TblUserDefaultPage> list, Long userId) {
		// TODO Auto-generated method stub
		userDefaultPageDao.updateDefaultPage(userId);
		for (TblUserDefaultPage tblUserDefaultPage : list) {
			tblUserDefaultPage.setUserId(userId);
			tblUserDefaultPage.setStatus(1);
			userDefaultPageDao.saveDefaultPage(tblUserDefaultPage);
		}
	}

	/**
	 * 
	* @Title: getDefaultPage
	* @Description: 获取用户默认页面
	* @author author
	* @param userId 用户ID
	* @return List<TblUserDefaultPage>默认页面列表
	 */
	@Override
	public List<TblUserDefaultPage> getDefaultPage(Long userId) {
		// TODO Auto-generated method stub
		List<TblUserDefaultPage> list = userDefaultPageDao.getDefaultPage2(userId);
		return list;
	}

}
