package cn.pioneeruniverse.system.service.role;

import java.util.List;

import cn.pioneeruniverse.system.entity.TblUserRole;

public interface IUserRoleService {

	/**
	* @author author
	* @Description 插入
	* @Date 2020/9/14
	* @param list
	* @return void
	**/
	void insertUserRole(List<TblUserRole> list);

	/**
	* @author author
	* @Description 删除用户角色
	* @Date 2020/9/14
	* @param list
	* @return void
	**/
	void delUserRole(List<Long> list);
	/**
	 * 删除
	 * @param list
	 * @param lastUpdateBy 更新者
	 */
	void delUserRoleByRoleId(List<Long> list,Long lastUpdateBy);
	/**
	 * 删除
	 * @param list
	 * @param lastUpdateBy
	 */
    void delUserRoleByUserIds(List<Long> list,Long lastUpdateBy);
	
	void delUserRoleByUserId(Long userId);
}
