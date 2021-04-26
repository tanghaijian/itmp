package cn.pioneeruniverse.project.dao.mybatis;

import java.util.List;

import cn.pioneeruniverse.project.entity.TblProjectSystem;

public interface TblProjectSystemMapper {

	/**
	* @author author
	* @Description 新增操作
	* @Date 2020/9/23
	* @param projectSystem
	* @return void
	**/
	void insertProjectSystem(TblProjectSystem projectSystem);

	/**
	* @author author
	* @Description 获取被开发系统
	* @Date 2020/9/23
	* @param id
	* @return java.util.List<java.lang.Long>
	**/
	List<Long> getDevelopSystemIds(Long id);

	/**
	* @author author
	* @Description 获取周边相关系统
	* @Date 2020/9/23
	* @param id
	* @return java.util.List<java.lang.Long>
	**/
	List<Long> getPeripheralSystemIds(Long id);

	/**
	* @author author
	* @Description 更新 STATUS = 2
	* @Date 2020/9/23
	* @param id
	* @return void
	**/
	void updateProjectSystem(Long id);

	/**
	* @author author
	* @Description 根据项目id获取系统名称
	* @Date 2020/9/23
	* @param id
	* @return java.util.List<java.lang.String>
	**/
	List<String> getSystemNames(Long id);

	/**
	* @author author
	* @Description 物理删除
	* @Date 2020/9/23
	* @param id
	* @return void
	**/
	void deleteProjectSystem(Long id);

	/**
	* @author author
	* @Description 根据项目获取系统id
	* @Date 2020/9/23
	* @param id
	* @return java.util.List<java.lang.Long>
	**/
    List<Long> getSystemIds(Long id);
}
