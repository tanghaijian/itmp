package cn.pioneeruniverse.dev.service.workRemark;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.dev.entity.TblDevTaskAttachement;
import cn.pioneeruniverse.dev.entity.TblDevTaskLog;
import cn.pioneeruniverse.dev.entity.TblDevTaskLogAttachement;
import cn.pioneeruniverse.dev.entity.TblDevTaskRemark;
import cn.pioneeruniverse.dev.entity.TblDevTaskRemarkAttachement;


public interface workTaskRemark {
	void addTaskRemark(String remark,Long userId,String userName,String UserAccount,List<TblDevTaskRemarkAttachement> files);
	
	List<TblDevTaskRemark> selectRemark(String testTaskId);
	List<TblDevTaskRemarkAttachement> findTaskRemarkAttchement(List<Long> ids);
	List<TblDevTaskLog> findLogList(Long id);
	List<TblDevTaskLogAttachement> findLogAttachement(List<Long> ids);
	/**
	 * @author author
	 * @Description 查询所有日志
	 * @Date 2020/9/4
	 * @param testId
	 * @return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTaskLogAttachement>
	 **/
	List<TblDevTaskLogAttachement> findTestLogFile(Long testId);
	
	List<TblDevTaskAttachement> findAttachement(Long testId);
	
	 void updateNo(List<TblDevTaskAttachement> list, HttpServletRequest request);

}
