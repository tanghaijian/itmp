package cn.pioneeruniverse.project.dao.mybatis;

import java.util.List;

import cn.pioneeruniverse.project.entity.TblQuestionLog;

public interface TblQuestionLogMapper {

	/**
	* @author author
	* @Description 新增记录问题日志
	* @Date 2020/9/23
	* @param tblQuestionLog
	* @return void
	**/
	void insertLog(TblQuestionLog tblQuestionLog);

	/**
	* @author author
	* @Description 获取记录问题日志
	* @Date 2020/9/23
	* @param id
	* @return java.util.List<cn.pioneeruniverse.project.entity.TblQuestionLog>
	**/
	List<TblQuestionLog> getQuestionLog(Long id);

}
