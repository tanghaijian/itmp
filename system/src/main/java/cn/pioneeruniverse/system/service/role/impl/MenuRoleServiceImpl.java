package cn.pioneeruniverse.system.service.role.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.pioneeruniverse.system.dao.mybatis.role.MenuRoleDao;
import cn.pioneeruniverse.system.entity.TblRoleMenuButton;
import cn.pioneeruniverse.system.service.role.IMenuRoleService;


@Service("iMenuRoleService")
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleDao, TblRoleMenuButton> implements IMenuRoleService{

	@Autowired
	private MenuRoleDao menuRoleDao;
	
	
	/**
	 * 
	* @Title: insertMenuRole
	* @Description: 保存菜单和角色关联关系
	* @author author
	* @param list 菜单角色关联信息
	 */
	@Transactional
	@Override
	public void insertMenuRole(List<TblRoleMenuButton> list) {
		if(list != null && !list.isEmpty())
			menuRoleDao.insertMenuRole(list);
	}

	/**
	 * 
	* @Title: delMenuRole
	* @Description: 删除菜单和角色关联关系
	* @author author
	* @param list 菜单角色关联信息
	 */
	@Override
	public void delMenuRole(List<Long> list) {
		if(list != null && !list.isEmpty())
			menuRoleDao.delMenuRole(list);
	}

	/**
	 * 
	* @Title: delMenuByRoleId
	* @Description: 删除某个角色下的菜单关联信息
	* @author author
	* @param roleId 角色ID
	 */
	@Override
	public void delMenuByRoleId(Long roleId) {
		menuRoleDao.delMenuByRoleId(roleId);
	}

	/**
	 * 
	* @Title: delMenuByRoleIds
	* @Description: 删除某些角色下的菜单关联关系
	* @author author
	* @param list 需要删除的角色ID列表
	* @param lastUpdateBy
	 */
	@Override
	public void delMenuByRoleIds(List<Long> list,Long lastUpdateBy) {
		menuRoleDao.delMenuByRoleIds(list,lastUpdateBy);
	}
	
	
	

}
