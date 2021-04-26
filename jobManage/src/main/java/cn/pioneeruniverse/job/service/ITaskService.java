package cn.pioneeruniverse.job.service;

import java.util.List;

import cn.pioneeruniverse.job.model.TaskInfo;

public interface ITaskService {
	
	public List<TaskInfo> queryJobList() throws Exception;
 
	/**
	 * 
	 * @Title: setSimpleTrigger
	 * @Description: 简单调度
	 * @param @param inputMap
	 * @param @return    参数
	 * @return Boolean    返回类型
	 * @throws
	 */
	public void setSimpleTriggerJob(TaskInfo info) throws Exception;
 
	/**
	 * 
	 * @Title: addJob
	 * @Description: 保存定时任务
	 * @param @param info    参数
	 * @return void    返回类型
	 * @throws
	 */
	public void addJob(TaskInfo info) throws Exception;
 
	/**
	 * 
	 * @Title: edit
	 * @Description: 修改定时任务
	 * @param @param info    参数
	 * @return void    返回类型
	 * @throws
	 */
	public void editJob(TaskInfo info) throws Exception;
 
	/**
	 * 
	 * @Title: delete
	 * @Description: 删除定时任务
	 * @param @param jobName
	 * @param @param jobGroup    参数
	 * @return void    返回类型
	 * @throws
	 */
	public void deleteJob(TaskInfo info) throws Exception;
 
	public boolean checkExists(TaskInfo info) throws Exception;
	
	public void startJob(TaskInfo info) throws Exception;
	
	/**
	 * 
	 * @Title: pause
	 * @Description: 暂停定时任务
	 * @param @param jobName
	 * @param @param jobGroup    参数
	 * @return void    返回类型
	 * @throws
	 */
	public void pauseJob(TaskInfo info) throws Exception;
 
	/**
	 * 
	 * @Title: resume
	 * @Description: 恢复暂停任务
	 * @param @param jobName
	 * @param @param jobGroup    参数
	 * @return void    返回类型
	 * @throws
	 */
	public void resumeJob(TaskInfo info)throws Exception;
 

}
