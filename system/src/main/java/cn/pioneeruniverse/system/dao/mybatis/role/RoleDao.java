package cn.pioneeruniverse.system.dao.mybatis.role;

import java.util.List;
import java.util.Map;

import cn.pioneeruniverse.system.entity.TblUserInfo;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.system.entity.TblRoleInfo;

public interface RoleDao extends BaseMapper<TblRoleInfo> {
	
	/**
	* @author author
	* @Description 通过角色编码和角色名获取角色信息
	* @Date 2020/9/16
	* @param bean
	* @return java.util.List<cn.pioneeruniverse.system.entity.TblRoleInfo>
	**/
	List<TblRoleInfo> getAllRole(TblRoleInfo bean);
	
	/**
	* @author author
	* @Description 通过角色ID获取角色信息
	* @Date 2020/9/16
	* @param id
	* @return cn.pioneeruniverse.system.entity.TblRoleInfo
	**/
	TblRoleInfo findRoleById(Long id);
	
	/**
	* @author author
	* @Description 获取某个人所拥有的所有角色信息
	* @Date 2020/9/16
	* @param userId
	* @return java.util.List<cn.pioneeruniverse.system.entity.TblRoleInfo>
	**/
	List<TblRoleInfo> findUserRole(Long userId);
	
	/**
	* @author author
	* @Description 获取所有角色信息，并且带出角色关联的菜单信息
	* @Date 2020/9/16
	* @param 
	* @return java.util.List<cn.pioneeruniverse.system.entity.TblRoleInfo>
	**/
	List<TblRoleInfo> getRoleWithMenu();
	
	/**
	* @author author
	* @Description 新增角色
	* @Date 2020/9/16
	* @param role
	* @return void
	**/
	void insertRole(TblRoleInfo role);
	
	/**
	* @author author
	* @Description 更新角色
	* @Date 2020/9/16
	* @param role
	* @return void
	**/
	void updateRole(TblRoleInfo role);
	
	/**
	* @author author
	* @Description 获取某个菜单关联的角色信息
	* @Date 2020/9/16
	* @param menuId
	* @return java.util.List<cn.pioneeruniverse.system.entity.TblRoleInfo>
	**/
	List<TblRoleInfo> getRoleByMenuId(Long menuId);
	
	/**
	* @author author
	* @Description 获取某个人所拥有的所有角色信息
	* @Date 2020/9/16
	* @param userId
	* @return java.util.List<cn.pioneeruniverse.system.entity.TblRoleInfo>
	**/
	List<TblRoleInfo> getRoleByUserId(Long userId);
	
	/**
	* @author author
	* @Description 获取未被项目关联的某个人的角色信息，分配岗位权限时显示角色
	* @Date 2020/9/16
	* @param userId
	* @return java.util.List<cn.pioneeruniverse.system.entity.TblRoleInfo>
	**/
	List<TblRoleInfo> getRoleByUserId1(Long userId);
	
	/**
	* @author author
	* @Description 获取拥有相同角色编码前缀的角色数量
	* @Date 2020/9/16
	* @param roleCode
	* @return java.lang.Long
	**/
	Long selectRoleCodeLen(String roleCode);
	
	/**
	* @author author
	* @Description 逻辑删除指定的角色
	* @Date 2020/9/16
	* @param ids
	* @param lastUpdateBy
	* @return void
	**/
	void delRole(@Param("ids")List<Long> ids,@Param("lastUpdateBy")Long lastUpdateBy);

	/**
	* @author author
	* @Description 新增用户和角色关联关系
	* @Date 2020/9/16
	* @param map
	* @return int
	**/
	int insertRoleUser(Map<String,Object> map);

	/**
	* @author author
	* @Description 逻辑删除指定用户下的角色关联关系
	* @Date 2020/9/16
	* @param map
	* @return void
	**/
    void updateRoleWithUser(Map<String,Object> map);

    /**
    * @author author
    * @Description 获取某个人所拥有的某个具体角色
    * @Date 2020/9/16
    * @param map
    * @return cn.pioneeruniverse.system.entity.TblRoleInfo
    **/
	TblRoleInfo getRoleUserById(Map<String,Object> map);

	/**
	* @author author
	* @Description 更新用户和角色关联关系
	* @Date 2020/9/16
	* @param map
	* @return void
	**/
	void updateRoleUser(Map<String,Object> map);

	/**
	* @author author
	* @Description 通过角色名获取角色信息
	* @Date 2020/9/16
	* @param role
	* @return cn.pioneeruniverse.system.entity.TblRoleInfo
	**/
    TblRoleInfo findRoleByName(TblRoleInfo role);

    /**
    * @author author
    * @Description 删除角色和菜单的关联关系
    * @Date 2020/9/16
    * @param map
    * @return void
    **/
	void deleteRoleMenu(Map<String,Object> map);

	/**
	* @author author
	* @Description 新增角色和菜单关联关系
	* @Date 2020/9/16
	* @param map
	* @return void
	**/
	void insertRoleMenu(Map<String,Object> map);

	/**
	* @author author
	* @Description 查询最大的编号
	* @Date 2020/9/16
	* @param 
	* @return java.lang.String
	**/
	String selectMaxEmpNo();

	/**
	* @author author
	* @Description 获取某个人所拥有的有效的角色信息
	* @Date 2020/9/16
	* @param userId
	* @return java.util.List<cn.pioneeruniverse.system.entity.TblRoleInfo>
	**/
	List<TblRoleInfo> getUserAllRole(Long userId);

	/**
	* @author author
	* @Description 获取系统管理员的角色信息
	* @Date 2020/9/16
	* @param 
	* @return cn.pioneeruniverse.system.entity.TblRoleInfo
	**/
    TblRoleInfo getAdminRole();

    /**
    * @author author
    * @Description 获取所有有效的角色信息
    * @Date 2020/9/16
    * @param 
    * @return java.util.List<cn.pioneeruniverse.system.entity.TblRoleInfo>
    **/
	List<TblRoleInfo> getRoles();
}
