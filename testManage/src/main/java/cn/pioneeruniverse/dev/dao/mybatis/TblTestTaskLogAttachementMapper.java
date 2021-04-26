package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;

import cn.pioneeruniverse.dev.entity.TblTestTaskLogAttachement;


public interface TblTestTaskLogAttachementMapper {
	/**
	* @author author
	* @Description 批量添加附件
	* @Date 2020/9/8
	* @param list
	* @return void
	**/
	void addLogAttachement(List<TblTestTaskLogAttachement> list);
	
	/**
	* @author author
	* @Description 批量查询日志附件
	* @Date 2020/9/8
	* @param ids
	* @return java.util.List<cn.pioneeruniverse.dev.entity.TblTestTaskLogAttachement>
	**/
	List<TblTestTaskLogAttachement> findTaskLogAttachement(List<Long> ids);
	
	/**
	* @author author
	* @Description 查询日志附件
	* @Date 2020/9/8
	* @param testId
	* @return java.util.List<cn.pioneeruniverse.dev.entity.TblTestTaskLogAttachement>
	**/
	List<TblTestTaskLogAttachement> findTestLogFile(Long testId);
}
