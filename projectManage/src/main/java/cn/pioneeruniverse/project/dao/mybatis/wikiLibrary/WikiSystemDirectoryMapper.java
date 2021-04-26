package cn.pioneeruniverse.project.dao.mybatis.wikiLibrary;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.project.entity.TblSystemDirectory;
import feign.Param;

public interface WikiSystemDirectoryMapper extends BaseMapper<TblSystemDirectory>{
	/**
	* @author author
	* @Description 根据项目获取目录信息
	* @Date 2020/9/4
	* @param projectId
	* @return java.util.List<cn.pioneeruniverse.project.entity.TblSystemDirectory>
	**/
	List<TblSystemDirectory> getDirectoryByProjectId(Long projectId);
	
	/**
	* @author author
	* @Description 新增项目对应的目录
	* @Date 2020/9/4
	* @param tblSystemDirectory
	* @return int
	**/
	 int addSystemDirectory(TblSystemDirectory tblSystemDirectory);
	 
	 /**
	 * @author author
	 * @Description 项目系统信息
	 * @Date 2020/9/4
	 * @param projectId
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 **/
	 List<Map<String, Object>> systemSystemName(@Param("projectId")Long projectId);//,@Param("systemName")String systemName

	/**
	* @author author
	* @Description SAVE_TYPE为3的项目系统信息
	 * @Date 2020/9/4
	* @param projectId
	* @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	**/
	 List<Map<String, Object>> systemSystemName2(@Param("projectId")Long projectId);

}
