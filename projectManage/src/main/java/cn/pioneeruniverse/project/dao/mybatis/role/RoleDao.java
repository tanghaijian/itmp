package cn.pioneeruniverse.project.dao.mybatis.role;

import cn.pioneeruniverse.project.entity.TblRoleInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RoleDao {

	/**
	* @author author
	* @Description 查询角色权限
	* @Date 2020/9/16
	* @param projectId
	* @param userPost
	* @return java.util.List<cn.pioneeruniverse.project.entity.TblRoleInfo>
	**/
	List<TblRoleInfo> selectUserPostRole(@Param(value = "projectId") Long projectId,
                                         @Param(value = "userPost") Integer userPost);

	/**
	* @author author
	* @Description 新增角色权限
	* @Date 2020/9/16
	* @param roleInfo
	* @return void
	**/
	void insertUserPostRole(TblRoleInfo roleInfo);

	/**
	* @author author
	* @Description 查询角色权限
	* @Date 2020/9/16
	* @param 
	* @return java.util.List<cn.pioneeruniverse.project.entity.TblRoleInfo>
	**/
	List<TblRoleInfo> selectNoProjectRole();

	/**
	* @author author
	* @Description 删除项目组用户权限
	* @Date 2020/9/16
	* @param roleIds
	* @return void
	**/
	void deleteProjectUserRole(Long [] roleIds);

	/**
	* @author author
	* @Description 查询用户角色
	* @Date 2020/9/16
	* @param userId
	* @param roleId
	* @return java.lang.Integer
	**/
	Integer selectUserRole(@Param(value = "userId")Long userId,
						   @Param(value = "roleId")Long roleId);

	/**
	* @author author
	* @Description 新增用户角色
	* @Date 2020/9/16
	* @param map
	* @return void
	**/
	void insertRoleUser(Map<String,Object> map);
}
