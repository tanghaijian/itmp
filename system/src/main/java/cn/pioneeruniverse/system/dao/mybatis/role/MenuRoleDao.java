package cn.pioneeruniverse.system.dao.mybatis.role;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.system.entity.TblRoleMenuButton;

public interface MenuRoleDao extends BaseMapper<TblRoleMenuButton> {

	/**
	* @author author
	* @Description 新增菜单
	* @Date 2020/9/14
	* @param list
	* @return void
	**/
	void insertMenuRole(List<TblRoleMenuButton> list);
	
	/**
	* @author author
	* @Description 批量删除
	* @Date 2020/9/14
	* @param list
	* @return void
	**/
	void delMenuRole(List<Long> list);

	/**
	* @author author
	* @Description 获取某个角色关联的菜单ID，并合并成一行返回
	* @Date 2020/9/14
	* @param roleId
	* @return java.lang.String
	**/
	String selectMenuIdsByRoleId(Long roleId);

	/**
	* @author author
	* @Description 删除某个角色下的角色菜单关联关系
	* @Date 2020/9/14
	* @param roleId
	* @return void
	**/
	void delMenuByRoleId(Long roleId);

	/**
	* @author author
	* @Description 逻辑删除指定的角色下的角色菜单关联信息
	* @Date 2020/9/14
	* @param list
	* @param lastUpdateBy
	* @return void
	**/
	void delMenuByRoleIds(@Param("list")List<Long> list,@Param("lastUpdateBy")Long lastUpdateBy);
}
