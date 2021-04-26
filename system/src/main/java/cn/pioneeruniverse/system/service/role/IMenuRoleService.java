package cn.pioneeruniverse.system.service.role;

import java.util.List;

import cn.pioneeruniverse.system.entity.TblRoleMenuButton;

public interface IMenuRoleService {
	/**
	 *
	 * @Title: insertMenuRole
	 * @Description: 保存菜单和角色关联关系
	 * @author author
	 * @param list 菜单角色关联信息
	 */
	void insertMenuRole(List<TblRoleMenuButton> list);
	/**
	 *
	 * @Title: delMenuRole
	 * @Description: 删除菜单和角色关联关系
	 * @author author
	 * @param list 菜单角色关联信息
	 */
	void delMenuRole(List<Long> list);

	/**
	 *
	 * @Title: delMenuByRoleId
	 * @Description: 删除某个角色下的菜单关联信息
	 * @author author
	 * @param roleId 角色ID
	 */
	void delMenuByRoleId(Long roleId);
	/**
	 *
	 * @Title: delMenuByRoleIds
	 * @Description: 删除某些角色下的菜单关联关系
	 * @author author
	 * @param list 需要删除的角色ID列表
	 * @param lastUpdateBy
	 */
	void delMenuByRoleIds(List<Long> list,Long lastUpdateBy);
}
