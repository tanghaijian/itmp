package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 * @Author:weiji
 * @Description:系统配置实体类
 */
public class TblSystemDeploy extends BaseEntity{

	private static final long serialVersionUID = 7830782747538469351L;

	private Integer environmentType;//环境类型

    private Long systemId;//系统ID

    private Long systemModuleId; //系统模块ID

    private String serverIds; //服务器ID，以,隔开

    private String systemDeployPath;//系统部署路径

    private Integer timeOut;//运行超时时间：秒

    private Integer retryNumber;//重试次数
    
    private Integer deploySequence;//部署顺序

    private Long toolId;//工具表ID

    private Integer deployStatus;//系统部署状态（1=空闲；2=构建中）
    
    private String stopSystemScript;//关闭系统脚本
    
    private String startSystemScript;//启动系统脚本
    
    private String checkScript;//检查系统脚本

	private Integer templateType;//摸板类型1,自定义，2springboot，3weblogic,4websphere,5tomcat
    
	public String getStopSystemScript() {
		return stopSystemScript;
	}

	public void setStopSystemScript(String stopSystemScript) {
		this.stopSystemScript = stopSystemScript;
	}

	public String getStartSystemScript() {
		return startSystemScript;
	}

	public void setStartSystemScript(String startSystemScript) {
		this.startSystemScript = startSystemScript;
	}

	public String getCheckScript() {
		return checkScript;
	}

	public void setCheckScript(String checkScript) {
		this.checkScript = checkScript;
	}

	public Integer getEnvironmentType() {
		return environmentType;
	}

	public void setEnvironmentType(Integer environmentType) {
		this.environmentType = environmentType;
	}

	public Long getSystemId() {
		return systemId;
	}

	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}

	public Long getSystemModuleId() {
		return systemModuleId;
	}

	public void setSystemModuleId(Long systemModuleId) {
		this.systemModuleId = systemModuleId;
	}

	public String getServerIds() {
		return serverIds;
	}

	public void setServerIds(String serverIds) {
		this.serverIds = serverIds;
	}

	public String getSystemDeployPath() {
		return systemDeployPath;
	}

	public void setSystemDeployPath(String systemDeployPath) {
		this.systemDeployPath = systemDeployPath;
	}

	public Integer getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(Integer timeOut) {
		this.timeOut = timeOut;
	}

	public Integer getRetryNumber() {
		return retryNumber;
	}

	public void setRetryNumber(Integer retryNumber) {
		this.retryNumber = retryNumber;
	}
	
	public Integer getDeploySequence() {
		return deploySequence;
	}

	public void setDeploySequence(Integer deploySequence) {
		this.deploySequence = deploySequence;
	}

	public Long getToolId() {
		return toolId;
	}

	public void setToolId(Long toolId) {
		this.toolId = toolId;
	}

	public Integer getDeployStatus() {
		return deployStatus;
	}

	public void setDeployStatus(Integer deployStatus) {
		this.deployStatus = deployStatus;
	}
	public Integer getTemplateType() {
		return templateType;
	}

	public void setTemplateType(Integer templateType) {
		this.templateType = templateType;
	}
    
    
}