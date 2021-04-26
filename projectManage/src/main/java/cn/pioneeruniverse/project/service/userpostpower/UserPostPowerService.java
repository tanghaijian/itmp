package cn.pioneeruniverse.project.service.userpostpower;

import cn.pioneeruniverse.project.entity.TblRoleInfo;

import java.util.List;

public interface UserPostPowerService {

	/**
	* @author author
	* @Description
	* @Date 2020/9/4
	* @param projectId
	* @param userPost
	* @return java.util.List<cn.pioneeruniverse.project.entity.TblRoleInfo>
	**/
	List<TblRoleInfo> getUserPostRole(Long projectId, Integer userPost);

	/**
	* @author author
	* @Description
	* @Date 2020/9/4
	* @param roleInfo
	* @return void
	**/
	void insertUserPostRole(TblRoleInfo roleInfo);

	/**
	* @author author
	* @Description 单个角色的菜单按钮权限
	* @Date 2020/9/4
	* @param id
	* @return java.util.List<cn.pioneeruniverse.project.entity.TblRoleInfo>
	**/
	List<TblRoleInfo> getRoleMenu(Long id);

	/**
	* @author author
	* @Description 新增角色id
	* @Date 2020/9/4
	* @param id
	* @return void
	**/
	void setRoleId(Long id);

	/**
	* @author author
	* @Description
	* @Date 2020/9/4
	* @param
	* @return java.util.List<cn.pioneeruniverse.project.entity.TblRoleInfo>
	**/
	List<TblRoleInfo> getNoProjectRole();
}
