package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
/**
 * @Author:
 * @Description:工具实体类
 */
@TableName("tbl_tool_info")
public class TblToolInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String toolName;       //工具名

    private Integer functionType;// 功能类别

    private Integer toolType;// 工具分类

    @TableField(exist = false)
    private String toolTypeName;// 工具名称

    private String homePath;// 地址

    private String context;

    private String ip;

    private String port;

    private String userName;

    private transient String password;

    private String accessUrl;

    private String jenkinsCredentialsId;

    private String sonarApiToken;

	private String jenkinsPluginName;

	private String environmentType;

    private String artifactRepositoryId;

    private Integer accessProtocol;//协议类型
    
    private String gitApiToken;

    public Integer getFunctionType() {
        return functionType;
    }

    public void setFunctionType(Integer functionType) {
        this.functionType = functionType;
    }

    public Integer getToolType() {
        return toolType;
    }

    public void setToolType(Integer toolType) {
        this.toolType = toolType;
    }

    public String getToolTypeName() {
        return toolTypeName;
    }

    public void setToolTypeName(String toolTypeName) {
        this.toolTypeName = toolTypeName;
    }

    public String getHomePath() {
        return homePath;
    }

    public void setHomePath(String homePath) {
        this.homePath = homePath;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccessUrl() {
        return accessUrl;
    }

    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    public String getJenkinsCredentialsId() {
        return jenkinsCredentialsId;
    }

    public void setJenkinsCredentialsId(String jenkinsCredentialsId) {
        this.jenkinsCredentialsId = jenkinsCredentialsId;
    }

    public String getSonarApiToken() {
        return sonarApiToken;
    }

    public void setSonarApiToken(String sonarApiToken) {
        this.sonarApiToken = sonarApiToken;
    }

    public String getJenkinsPluginName() {
        return jenkinsPluginName;
    }

    public void setJenkinsPluginName(String jenkinsPluginName) {
        this.jenkinsPluginName = jenkinsPluginName;
    }

	public String getArtifactRepositoryId() {
		return artifactRepositoryId;
	}

	public void setArtifactRepositoryId(String artifactRepositoryId) {
		this.artifactRepositoryId = artifactRepositoryId;
	}

    public String getEnvironmentType() {
        return environmentType;
    }

    public void setEnvironmentType(String environmentType) {
        this.environmentType = environmentType;
    }

    public Integer getAccessProtocol() {
        return accessProtocol;
    }

    public void setAccessProtocol(Integer accessProtocol) {
        this.accessProtocol = accessProtocol;
    }

    public String getToolName() {
        return toolName;
    }
    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public String getContext() {
        return context;
    }
    public void setContext(String context) {
        this.context = context;
    }

	public String getGitApiToken() {
		return gitApiToken;
	}

	public void setGitApiToken(String gitApiToken) {
		this.gitApiToken = gitApiToken;
	}
}