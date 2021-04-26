package cn.pioneeruniverse.project.dao.mybatis;

import java.util.List;
import java.util.Map;

import cn.pioneeruniverse.project.entity.TblProgramInfo;
import cn.pioneeruniverse.project.entity.TblProgramProject;
import cn.pioneeruniverse.project.entity.TblProjectInfo;

public interface TblProgramInfoMapper {
	/**
	* @author author
	* @Description 获取项目群总数
	* @Date 2020/9/22
	* @param 
	* @return java.lang.Long
	**/
	Long getCountProgram();

	/**
	* @author author
	* @Description 新增项目群
	* @Date 2020/9/22
	* @param programInfo
	* @return void
	**/
	void insertProgram(TblProgramInfo programInfo);

	/**
	* @author author
	* @Description 新增项目群和项目关联关系
	* @Date 2020/9/22
	* @param programProject
	* @return void
	**/
	void insertProgramProject(TblProgramProject programProject);

	/**
	* @author author
	* @Description 系统管理员查询项目群
	* @Date 2020/9/22
	* @param map
	* @return java.util.List<cn.pioneeruniverse.project.entity.TblProgramInfo>
	**/
	List<TblProgramInfo> getAllPrograms(Map<String, Object> map);

	/**
	* @author author
	* @Description 普通人员查询，只能查询同项目组下的项目群信息
	* @Date 2020/9/22
	* @param map
	* @return java.util.List<cn.pioneeruniverse.project.entity.TblProgramInfo>
	**/
	List<TblProgramInfo> getProgramsByUid(Map<String, Object> map);

	/**
	* @author author
	* @Description 通过ID获取项目群
	* @Date 2020/9/22
	* @param id
	* @return cn.pioneeruniverse.project.entity.TblProgramInfo
	**/
	TblProgramInfo getProgramById(Long id);

	/**
	* @author author
	* @Description 获取项目群中的项目
	* @Date 2020/9/22
	* @param id
	* @return java.util.List<cn.pioneeruniverse.project.entity.TblProjectInfo>
	**/
	List<TblProjectInfo> getProjectsByProgramId(Long id);

	/**
	* @author author
	* @Description 更新项目群
	* @Date 2020/9/22
	* @param tblProgramInfo
	* @return void
	**/
	void updateProgram(TblProgramInfo tblProgramInfo);

	/**
	* @author author
	* @Description 逻辑删除项目群和项目关联
	* @Date 2020/9/22
	* @param id
	* @return void
	**/
	void updateProgramProject(Long id);

}
