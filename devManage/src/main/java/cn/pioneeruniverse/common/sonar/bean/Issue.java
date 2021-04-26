package cn.pioneeruniverse.common.sonar.bean;
/**
 *
 * @ClassName:Issue
 * @deprecated
 * @author author
 * @date 2020年8月19日
 *
 */
public class Issue {
	
	private String component;//问题路径也指组件id
	private String key;
	private String line;//问题行数
	private String message;//问题描述
	private String project;//所属项目
	private String severity;//验证性
	private String type;//属于什么错误类型
	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	

	
	
	
	
	
	

}
