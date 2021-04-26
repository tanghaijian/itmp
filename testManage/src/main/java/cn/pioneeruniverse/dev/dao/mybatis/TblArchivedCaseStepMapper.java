package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.pioneeruniverse.dev.entity.TblArchivedCase;
import cn.pioneeruniverse.dev.entity.TblArchivedCaseStep;
import cn.pioneeruniverse.dev.entity.TblCaseStep;

public interface TblArchivedCaseStepMapper {

	/**
	* @author author
	* @Description 新增归档案例步骤
	* @Date 2020/9/17
	* @param tblCaseStep
	* @return void
	**/
	void archivingCaseStep(TblCaseStep tblCaseStep);

	/**
	* @author author
	* @Description 根据案例id修改归档案例
	* @Date 2020/9/17
	* @param map
	* @return void
	**/
	void updateArchivedCaseStepByCseId(HashMap<String, Object> map);

	
	/**
	* @author author
	* @Description 新增归档案例步骤
	* @Date 2020/9/17
	* @param tblCaseStep
	* @return void
	**/
	void insertArchivedCaseStep(TblCaseStep tblCaseStep);

	/**
	* @author author
	* @Description 查询归档案例步骤
	* @Date 2020/9/17
	* @param id
	* @return java.util.List<cn.pioneeruniverse.dev.entity.TblArchivedCaseStep>
	**/
	List<TblArchivedCaseStep> findCaseStepByCaseId(Long id);

	/**
	* @author author
	* @Description 删除案例步骤
	* @Date 2020/9/17
	* @param ids
	* @return void
	**/
	void deleteArchivedCaseStep(List<Long> ids);

	/**
	* @author author
	* @Description 查询归档案例步骤id
	* @Date 2020/9/17
	* @param id
	* @return java.util.List<java.lang.Long>
	**/
	List<Long> getStepIdsByCaseId(Long id);

	/**
	* @author author
	* @Description 修改操作
	* @Date 2020/9/17
	* @param tblCaseStep
	* @return void
	**/
	void updateStep(TblCaseStep tblCaseStep);

	/**
	* @author author
	* @Description 批量修改操作 STATUS = 2 
	* @Date 2020/9/17
	* @param map
	* @return void
	**/
	void deleteCaseSteps(Map<String, Object> map);
	
}
