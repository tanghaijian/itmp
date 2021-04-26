package cn.pioneeruniverse.project.service.userpostpower.impl;

import cn.pioneeruniverse.project.dao.mybatis.menu.MenuDao;
import cn.pioneeruniverse.project.dao.mybatis.role.RoleDao;
import cn.pioneeruniverse.project.entity.TblMenuButtonInfo;
import cn.pioneeruniverse.project.entity.TblRoleInfo;
import cn.pioneeruniverse.project.service.userpostpower.UserPostPowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserPostPowerServiceImpl implements UserPostPowerService {

	@Autowired
	private RoleDao roleDao;
	@Autowired
	private MenuDao menuDao;

	private Long roleId = null;

	public static final Integer MENU_TYPE = 1;
	public static final Integer BUTTON_TYPE = 2;

	@Override
	public List<TblRoleInfo> getUserPostRole(Long projectId, Integer userPost) {
		return roleDao.selectUserPostRole(projectId,userPost);
	}

	@Override
	public void insertUserPostRole(TblRoleInfo roleInfo) {
		roleDao.insertUserPostRole(roleInfo);
	}

	/**
	 * 单个角色的菜单按钮权限
	 * @param id 角色id
	 * @return 菜单按钮列表
	 */
	@Override
	public List<TblRoleInfo> getRoleMenu(Long id) {
		Map<String,Object> map = new HashMap<>();
		Map<String, TblMenuButtonInfo> resultMap = new LinkedHashMap<>();
		List<TblMenuButtonInfo> button_list = new ArrayList<>();
		TblRoleInfo roleInfo = new TblRoleInfo();
		if(id==null){
			id = roleId;
		}
		map.put("id",id);
		map.put("menuType",MENU_TYPE);
		List<TblMenuButtonInfo> menuList = menuDao.getMenusWithTYPE(map);
		resultMap = menuList(menuList,resultMap);

		List<TblRoleInfo> menu_list = new ArrayList<>();
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
			List<TblMenuButtonInfo> childrenList = menuDao.findRoleMenu(parentIds);
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
	* @Description: 根据父菜单递归获取其所有子菜单
	* @author author
	* @param menuList  父菜单列表
	* @param resultMap
	* @return Map<String,TblMenuButtonInfo>
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

	@Override
	public void setRoleId(Long id){
		roleId = id;
	}
	@Override
	public List<TblRoleInfo> getNoProjectRole(){
		return roleDao.selectNoProjectRole();
	}

}
