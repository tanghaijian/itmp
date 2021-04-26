package cn.pioneeruniverse.project.dao.mybatis;

import java.util.List;

import cn.pioneeruniverse.project.entity.TblDevTask;

public interface TblDevTaskMapper {

	/**
	* @author author
	* @Description 根据开发任务查询开发工作任务
	* @Date 2020/9/22
	* @param id
	* @return java.util.List<cn.pioneeruniverse.project.entity.TblDevTask>
	**/
	List<TblDevTask> getDevTasks(Long id);

	/**
	* @author author
	* @Description 根据系统查询系统库
	* @Date 2020/9/22
	* @param systemId
	* @return java.util.List<java.lang.Integer>
	**/
	List<Integer> getScmType(Long systemId);

}
