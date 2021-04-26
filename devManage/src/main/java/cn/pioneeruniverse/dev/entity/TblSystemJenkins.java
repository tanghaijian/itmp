package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
/**
 * @Author:weiji
 * @Description:系统JENKINS配置表
 */
@TableName("tbl_system_jenkins")
public class TblSystemJenkins extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long systemId;//系统ID
	private Long systemScmId;//系统源码ID
	private Integer environmentType;//环境类型
	private Long toolId;//工具
	private Integer jobType;//任务类型（数据字典：1:构建，2:部署）
	private String jobName;//任务名称
	private String jobPath;//任务相对路径
	private String cronJobName;//定时任务名称
	private String jobCron;//任务定时
	private Integer createType;//任务创建方式（1:系统自动，2:自定义）
	private String rootPom;//ROOT POM（自动生成）
	private String goalsOptions;//GOALS AND OPTIONS（自动生成）
	private Integer buildStatus;//系统构建状态（1=空闲；2=构建中）
	private Integer deployStatus;//系统部署状态（1=空闲；2=部署中）
	
	@TableField(exist = false)
	private String StringId;//id的字符串形式
	
	@TableField(exist = false)
	private Integer jobRunNumber;//此次任务在jenkins的编号

	@TableField(exist = false)
	private Boolean isRun;//是否正在运行

	@TableField(exist = false)
	private String environmentTypeName;//环境名

	public Integer getJobRunNumber() {
		return jobRunNumber;
	}

	public void setJobRunNumber(Integer jobRunNumber) {
		this.jobRunNumber = jobRunNumber;
	}

	public void setCronJobName(String cronJobName) {
		this.cronJobName = cronJobName;
	}

	public String getCronJobName() {
		return cronJobName;
	}

	public Integer getCreateType() {
		return createType;
	}

	public void setCreateType(Integer createType) {
		this.createType = createType;
	}

	public Long getSystemId() {
		return systemId;
	}

	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}

	public Long getSystemScmId() {
		return systemScmId;
	}

	public void setSystemScmId(Long systemScmId) {
		this.systemScmId = systemScmId;
	}

	public Long getToolId() {
		return toolId;
	}

	public void setToolId(Long toolId) {
		this.toolId = toolId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName == null ? null : jobName.trim();
	}

	public String getJobPath() {
		return jobPath;
	}

	public void setJobPath(String jobPath) {
		this.jobPath = jobPath;
	}

	public String getRootPom() {
		return rootPom;
	}

	public void setRootPom(String rootPom) {
		this.rootPom = rootPom == null ? null : rootPom.trim();
	}

	public String getGoalsOptions() {
		return goalsOptions;
	}

	public void setGoalsOptions(String goalsOptions) {
		this.goalsOptions = goalsOptions == null ? null : goalsOptions.trim();
	}

	public Integer getBuildStatus() {
		return buildStatus;
	}

	public void setBuildStatus(Integer buildStatus) {
		this.buildStatus = buildStatus;
	}

	public Integer getJobType() {
		return jobType;
	}

	public void setJobType(Integer jobType) {
		this.jobType = jobType;
	}

	public String getStringId() {
		return StringId;
	}

	public void setStringId(String stringId) {
		StringId = stringId;
	}

	public String getJobCron() {
		return jobCron;
	}

	public void setJobCron(String jobCron) {
		this.jobCron = jobCron;
	}

	public Integer getEnvironmentType() {
		return environmentType;
	}

	public void setEnvironmentType(Integer environmentType) {
		this.environmentType = environmentType;
	}

	public String getEnvironmentTypeName() {
		return environmentTypeName;
	}

	public void setEnvironmentTypeName(String environmentTypeName) {
		this.environmentTypeName = environmentTypeName;
	}

	public Integer getDeployStatus() {
		return deployStatus;
	}

	public void setDeployStatus(Integer deployStatus) {
		this.deployStatus = deployStatus;
	}
	public Boolean getIsRun() {
		return isRun;
	}

	public void setIsRun(Boolean run) {
		isRun = run;
	}

}