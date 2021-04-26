package cn.pioneeruniverse.job.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.StringUtil;

public class TaskInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 增加或修改标识
	 */
	private int id;
 
	/**
	 * 任务名称classname
	 */
	private String jobName;
	
	/**
	 * 任务执行Class类名称
	 */
	private String className;
	
	/**
	 * 任务分组
	 */
	private String jobGroup;
	
	/**
	 * 任务描述
	 */
	private String jobDescription;
	
	/**
	 * 任务状态
	 * -NONE 无  
        |-NORMAL 正常状态  
        |-PAUSED 暂停状态   
        |-COMPLETE 完成  
        |-ERROR 错误
        |-BLOCKED 堵塞
	 */
	private String jobStatus;
	
	/**
	 * 任务表达式
	 */
	private String cronExpression;
	
	/**
	 * 创建时间
	 */
	private String createTime;
	
	/**
	 * 简单触发
	 * 间隔时间（毫秒）
	 */
	private Long milliSeconds;
	
	/**
	 * 简单触发
	 * 重复次数
	 * 
	 */
	private Integer repeatCount;
	
	/**
	 * 简单触发
	 * 起始时间
	 */
	private Date startDate;
	
	/**
	 * 简单触发 
	 * 终止时间
	 */
	private Date endDate;
	
	//业务逻辑需要用到的参数(手动执行时才有效)
	//http://localhost:8080/job/startJob?taskJson={"jobName":"putFeatureToHistory","parameterMap":{"startDate":"2019-12-05","endDate":"2019-12-05"}}
	private Map<String, Object> parameterMap;
	//业务逻辑需要用到的参数转换成静态好传递(手动执行时才有效)
	private static String parameterJson;
	public Map<String, Object> getParameterMap() {
		return parameterMap;
	}
	public void setParameterMap(Map<String, Object> parameterMap) {
		this.parameterMap = parameterMap;
		parameterJson = JSON.toJSONString(parameterMap);
	}
	public static String getParameterJson() {
		if (StringUtil.isEmpty(parameterJson)) {//为null或者“”会导致fallback失败
			parameterJson = "{}";
		}
		String returnJson = parameterJson;
		parameterJson = "{}";//每次使用完成置空，避免定时自动执行时仍然拿到参数。
		return returnJson;
	}
	public static void setParameterJson(String parameterJson) {
		TaskInfo.parameterJson = parameterJson;
	}

	public String getJobName() {
		return jobName;
	}
 
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
 
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getJobGroup() {
		return jobGroup;
	}
 
	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
 
	public String getJobDescription() {
		return jobDescription;
	}
 
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}
 
	public String getJobStatus() {
		return jobStatus;
	}
 
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
 
	public String getCronExpression() {
		return cronExpression;
	}
 
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
 
	public String getCreateTime() {
		return createTime;
	}
 
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
 
	public int getId() {
		return id;
	}
 
	public void setId(int id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getMilliSeconds() {
		return milliSeconds;
	}

	public void setMilliSeconds(Long milliSeconds) {
		this.milliSeconds = milliSeconds;
	}

	public Integer getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(Integer repeatCount) {
		this.repeatCount = repeatCount;
	}

}
