package cn.pioneeruniverse.dev.service.build;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.pioneeruniverse.dev.entity.TblSystemJenkins;
import cn.pioneeruniverse.dev.entity.TblSystemModule;
import cn.pioneeruniverse.dev.entity.TblToolInfo;

public interface IJenkinsBuildService {

	/**
	 * 构建传统的Jenkins任务
	 * @param paraMap
	 * @throws Exception
	 */
	void buildGeneralAutoJob(Map<String, Object> paraMap) throws Exception;

	/**
	 * 构建基于微服务的Jenkins任务，采用Pipeline方式
	 * @param paraMap
	 * @throws Exception
	 */
	void buildMicroAutoJob(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 手动构建Jenkins任务
	 * @param paraMap
	 * @throws Exception
	 */
	Map<String, Object> buildManualJob(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 自动部署Jenkins任务
	 * @param paraMap
	 * @throws Exception
	 */
	void buildGeneralAutoDeployJob(Map<String, Object> paraMap) throws Exception;
	
	
	/**
	 * 自动部署微服务Jenkins任务
	 * @param paraMap
	 * @throws Exception
	 */
	void buildMicroAutoDeployJob(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 包件部署Jenkins任务
	 * @param paraMap
	 * @throws Exception
	 */
	void buildPackageAutoDeployJob(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 基于非726的生产部署，包含包件部署和源码部署
	 * @param paraMap
	 * @throws Exception
	 */
	void buildPROAutoDeployJob(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 手动部署Jenkins任务
	 * @param paraMap
	 * @throws Exception
	 */
	Map<String, Object> buildManualDeployJob(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 通过任务名称获取定时表达式。
	 * @param jobName
	 * @return
	 * @throws Exception
	 */
	String getJobCron(TblToolInfo jenkinsToolInfo, TblSystemJenkins tblSystemJenkins, String jobName) throws Exception;
	
	/**
	 * 通过传入的JobName，返回Job的Parameter化配置信息
	 * @param jenkinsTool
	 * @param jobName
	 * @return
	 * @throws Exception
	 */
	String getJobParameter(TblToolInfo jenkinsTool, TblSystemJenkins tblSystemJenkins, String jobName) throws Exception;

	/**
	 * 获取下一次构建生成的Jenkins任务编号
	 * @param jenkinsTool
	 * @param tblSystemJenkins
	 * @param jobName
	 * @return
	 * @throws Exception
	 */
	int getNextBuildNumber(TblToolInfo jenkinsTool, TblSystemJenkins tblSystemJenkins, String jobName) throws Exception;
	
	Map<String, String> getBuildLogByNumber(TblToolInfo jenkinsTool, TblSystemJenkins tblSystemJenkins, String jobName, int jobNumber, List<TblSystemModule> tblSystemModuleList) throws Exception;
	
	/**
	 * 获取最后构建日志信息 手动
	 * @param jenkinsTool
	 * @param jobName
	 * @return
	 * @throws Exception
	 */
	Map<String, String> getLastBuildLog(TblToolInfo jenkinsTool, TblSystemJenkins tblSystemJenkins, String jobName, List<TblSystemModule> tblSystemModuleList) throws Exception;
	
	/**
	 * 获取正在Build的Jenkins任务日志输出。
	 * @param jobName
	 * @return
	 * @throws Exception
	 */
//	String getJenkinsBuildingLog(TblToolInfo jenkinsTool, String jobName) throws Exception;
	
	/**
	 * 获取正在Build的Jenkins任务日志输出。
	 * @param jobName
	 * @return
	 * @throws Exception
	 */
//	String getJenkinsBuildingLog(TblToolInfo jenkinsTool, TblSystemJenkins tblSystemJenkins, String jobName) throws Exception;
	
	/**
	 * 判断任务是不是正在构建
	 * @param jenkinsTool
	 * @param jobPath
	 * @param jobName
	 * @return
	 * @throws Exception
	 */
	boolean isJenkinsBuilding(TblToolInfo jenkinsTool,String jobPath, String jobName) throws Exception;
	
	/**
	 * 获取正在Build的Jenkins任务日志输出。
	 * @param jobName
	 * @return
	 * @throws Exception
	 */
	Map<String, String> getJenkinsBuildingLog(TblToolInfo jenkinsTool, TblSystemJenkins tblSystemJenkins, String jobName, String start, int jobNumber) throws Exception;

	/**
	 * 部署暂停后手动操作继续执行调用
	 * @param jenkinsTool
	 * @param subUrl
	 * @throws Exception
	 */
	void continuePipeline(TblToolInfo jenkinsTool, String subUrl) throws Exception;
	
	/**
	 * 日志中出现调用其它job的链接
	 * @param jenkinsTool
	 * @param subUrl
	 * @throws Exception
	 */
	String goOtherPageLog(TblToolInfo jenkinsTool, String subUrl) throws Exception;
	
	/**
	 * 删除手动构建时生成的回调
	 * @param jenkinsTool
	 * @param jobName
	 * @throws Exception
	 */
	void deleteJobCallback(TblToolInfo jenkinsTool,TblSystemJenkins tblSystemJenkins, String jobName) throws Exception;
	
	/**
	 * 停止构建
	 * @param jenkinsTool
	 * @param jobName
	 * @throws Exception
	 */
	void stopBuilding(TblToolInfo jenkinsTool, TblSystemJenkins tblSystemJenkins, String jobName) throws Exception;
	/**
	 * 判断任务是不是正在构建
	 * @param jenkinsTool
	 * @param jobName
	 * @return
	 * @throws Exception
	 */
//	public boolean isJenkinsBuilding(TblToolInfo jenkinsTool, String jobName) throws Exception;
	/**
	 * 判断任务是不是正在构建 手动
	 * @param jenkinsTool
	 * @param jobName
	 * @return
	 * @throws Exception
	 */
//	public boolean isJenkinsBuilding(TblToolInfo jenkinsTool, TblSystemJenkins tblSystemJenkins, String jobName) throws Exception;
	public boolean isJenkinsBuilding(TblToolInfo jenkinsTool, TblSystemJenkins tblSystemJenkins, String jobName, int jobNumber) throws Exception;
	
	/**
	 * 判断手动构建部署是否允许执行：
	 * 1.非正在构建，默认可执行，返回true
	 * 2.正在构建中，判断是否允许并发执行，允许就返回true，不允许就返回false
	 * @param jenkinsTool
	 * @param tblSystemJenkins
	 * @param jobName
	 * @return
	 * @throws Exception
	 */
	public boolean checkStartBuilding2Manual(TblToolInfo jenkinsTool, TblSystemJenkins tblSystemJenkins, String jobName) throws Exception;
	/**
	 * 判断Jenkins任务是否存在
	 * @param jenkinsTool
	 * @param jobName
	 * @return
	 * @throws Exception
	 */
	boolean existJob(TblToolInfo jenkinsTool, String jobName) throws Exception;
	
	/**
	 * 验证Jenkins的Cron表达式
	 * @param jenkinsTool
	 * @param jobName
	 * @throws Exception
	 */
	Map<String, String> validateCron(TblToolInfo jenkinsTool, String cron) throws Exception;
	
	/**
	 * 获取构建开始时间
	 * @param jenkinsTool
	 * @param jobName
	 * @return
	 * @throws Exception
	 */
	Timestamp getJobStartDate(TblToolInfo jenkinsTool, String jobName) throws Exception;
	Timestamp getJobStartDate(TblToolInfo jenkinsTool, TblSystemJenkins tblSystemJenkins, String jobName, int jobNumber) throws Exception;
	
	/**
	 * 获取构建结束时间
	 * @param jenkinsTool
	 * @param jobName
	 * @param jobNumber
	 * @return
	 * @throws Exception
	 */
	Timestamp getJobEndDate(TblToolInfo jenkinsTool, TblSystemJenkins tblSystemJenkins, String jobName, int jobNumber) throws Exception;
	
	/**
	 * 删除Jenkins Job
	 * @param jenkinsTool
	 * @param jobName
	 * @throws Exception
	 */
	void deleteJob(TblToolInfo jenkinsTool, String jobName) throws Exception;
	
	JSONObject getStageViewDescribeJson(TblToolInfo jenkinsTool, TblSystemJenkins tblSystemJenkins, String jobName, int jobNumber) throws Exception;
	JSONObject getNextPendingInputAction(TblToolInfo jenkinsTool, TblSystemJenkins tblSystemJenkins, String jobName, int jobNumber) throws Exception;
	void getStageViewNextPending(TblToolInfo jenkinsTool, TblSystemJenkins tblSystemJenkins, String jobName, int jobNumber, String interruptId, Integer flag) throws Exception;
	JSONObject getStageViewDescribeExecutionJson(TblToolInfo jenkinsTool, TblSystemJenkins tblSystemJenkins, String jobName, int jobNumber, int describeId) throws Exception;
	JSONObject getStageViewExecutionLogJson(TblToolInfo jenkinsTool, TblSystemJenkins tblSystemJenkins, String jobName, int jobNumber, int executionId) throws Exception;
}
