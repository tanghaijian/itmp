package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;

import cn.pioneeruniverse.dev.entity.TblDevTaskLogAttachement;


public interface TblDevTaskLogAttachementMapper {
	/**
	* @author author
	* @Description 添加开发工作任务日志附件
	* @Date 2020/9/21
	* @param list
	* @return void
	**/
	void addLogAttachement(List<TblDevTaskLogAttachement> list);
	
	/**
	* @author author
	* @Description 获取开发工作任务日志附件
	* @Date 2020/9/21
	* @param ids
	* @return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTaskLogAttachement>
	**/
	List<TblDevTaskLogAttachement> findTaskLogAttachement(List<Long> ids);
	
	/**
	* @author author
	* @Description 获取测试工作任务日志附件 ，错误sql废弃
	* @Date 2020/9/21
	* @param devId
	* @return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTaskLogAttachement>
	**/
	List<TblDevTaskLogAttachement> findTestLogFile(Long devId);
}
