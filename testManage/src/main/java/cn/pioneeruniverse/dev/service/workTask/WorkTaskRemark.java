package cn.pioneeruniverse.dev.service.workTask;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.dev.entity.TblTestTaskAttachement;
import cn.pioneeruniverse.dev.entity.TblTestTaskLog;
import cn.pioneeruniverse.dev.entity.TblTestTaskLogAttachement;
import cn.pioneeruniverse.dev.entity.TblTestTaskRemark;
import cn.pioneeruniverse.dev.entity.TblTestTaskRemarkAttachement;

public interface WorkTaskRemark {
	void addTaskRemark(String remark,Long Userid,String userName,String UserAccount,List<TblTestTaskRemarkAttachement> files);
	
	List<TblTestTaskRemark> selectRemark(String testTaskId);
	List<TblTestTaskRemarkAttachement> findTaskRemarkAttchement(List<Long> ids);
	List<TblTestTaskLog> findLogList(Long id);
	List<TblTestTaskLogAttachement> findLogAttachement(List<Long> ids);
	/**
	* @author author
	* @Description 查询所有日志
	* @Date 2020/9/4
	* @param testId
	* @return java.util.List<cn.pioneeruniverse.dev.entity.TblTestTaskLogAttachement>
	**/
	List<TblTestTaskLogAttachement> findTestLogFile(Long testId);
	
	List<TblTestTaskAttachement> findAttachement(Long testId);
	
	 void updateNo(List<TblTestTaskAttachement> list, HttpServletRequest request);
}
