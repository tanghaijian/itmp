package cn.pioneeruniverse.dev.service.displayboard;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.dev.entity.DevTaskVo;
import cn.pioneeruniverse.dev.entity.TblDataDic;
import cn.pioneeruniverse.dev.entity.TblDevTask;
import cn.pioneeruniverse.dev.entity.TblProjectInfo;
import cn.pioneeruniverse.dev.entity.TblSprintInfo;
import cn.pioneeruniverse.dev.entity.TblSystemInfo;

/**
 * 类说明
 *
 * @author:tingting
 * @version:2019年3月20日 下午1:36:51
 */
public interface DisplayBoardService {

	/**
	*@author liushan
	*@Description 根据用户获取项目
	*@Date 2020/8/4
	 * @param uid
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblProjectInfo>
	**/
	List<TblProjectInfo> getAllProjectByUid(Long uid);

	/**
	*@author liushan
	*@Description 根据项目查询系统
	*@Date 2020/8/4
	 * @param projectId
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblSystemInfo>
	**/
	List<TblSystemInfo> getSystemByPId(Long projectId);

	/**
	*@author liushan
	*@Description 根据系统获取冲刺
	*@Date 2020/8/4
	 * @param systemId
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblSprintInfo>
	**/
	List<TblSprintInfo> getSprintBySystemId(Long systemId);

    /**
	*@author liushan
	*@Description 根据冲刺获取开发任务
	*@Date 2020/8/4
	 * @param sprintId
	*@return java.util.List<cn.pioneeruniverse.dev.entity.DevTaskVo>
	**/
    List<DevTaskVo> getDevTaskBySprintId(Long sprintId, String devUserName);

	/**
	*@author liushan
	*@Description 根据开发任务获取工作任务
	*@Date 2020/8/4
	 * @param id
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTask>
	**/
    List<TblDevTask> getDevTaskByReqFeatureId(Long id);

	/**
	*@author liushan
	*@Description 编辑工作任务状态
	*@Date 2020/8/4
	 * @param reqFeatureId
 * @param status
 * @param request
	*@return void
	**/
	void updateDevTaskStatus(Long reqFeatureId, String status, HttpServletRequest request);

	/**
	*@author liushan
	*@Description 根据开发任务id统计各状态工作任务的数量
	*@Date 2020/8/4
	 * @param id
	*@return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	**/
	List<Map<String, Object>> getWorkTaskStatusCount(Long id);

	/**
	*@author liushan
	*@Description 获取开发任务数据字典状态
	*@Date 2020/8/4
	 * @param 
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblDataDic>
	**/
	List<TblDataDic> getReqFeatureStatus();

	/**
	*@author liushan
	*@Description 根据冲刺获取工作任务
	*@Date 2020/8/4
	 * @param sprintId
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTask>
	**/
    List<TblDevTask> getWorkTaskBySprintId(Long sprintId,String devUserName);

	/**
	*@author liushan
	*@Description 获取工作任务数据状态
	*@Date 2020/8/4
	 * @param 
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblDataDic>
	**/
	List<TblDataDic> getWorTaskStatus();

	/**
	*@author liushan
	*@Description 修改工作任务状态
	*@Date 2020/8/4
	 * @param devTaskId
 * @param status
 * @param request
	*@return void
	**/
	void updateWorkTaskStatus(Long devTaskId, Integer status, HttpServletRequest request);

	/**
	*@author liushan
	*@Description 根据项目获取项目小组
	*@Date 2020/8/4
	 * @param projectId
	*@return java.lang.String
	**/
	String getProjectGroupByProjectId(long projectId);

	/**
	*@author liushan
	*@Description 根据项目查询查询项目小组
	*@Date 2020/8/4
	 * @param projectId
	*@return java.lang.String
	**/
    String getProjectGroupByProjectIdNoWu(long projectId);
}
