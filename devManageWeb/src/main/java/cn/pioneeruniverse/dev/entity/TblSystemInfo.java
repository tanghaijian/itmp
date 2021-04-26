package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * 
* @ClassName: TblSystemInfo
* @Description: 系统信息
* @author author
* @date 2020年8月13日 上午9:12:25
*
 */
public class TblSystemInfo  extends BaseEntity{
	
	private static final long serialVersionUID = 1L;

    private Long projectId; //项目ID

    private String systemName; //系统名称

    private String systemCode; //系统编码

    private String groupId; //maven groupId

    private String artifactId; //maven artifactId

    private Integer architectureType; //架构类型：1=微服务架构；2=传统架构

    private Integer buildStatus; //构建状态


    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName == null ? null : systemName.trim();
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode == null ? null : systemCode.trim();
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId == null ? null : groupId.trim();
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId == null ? null : artifactId.trim();
    }

    public Integer getArchitectureType() {
        return architectureType;
    }

    public void setArchitectureType(Integer architectureType) {
        this.architectureType = architectureType;
    }

    public Integer getBuildStatus() {
        return buildStatus;
    }

    public void setBuildStatus(Integer buildStatus) {
        this.buildStatus = buildStatus;
    }

	@Override
	public String toString() {
		return "TblSystemInfo [projectId=" + projectId + ", systemName=" + systemName + ", systemCode=" + systemCode
				+ ", groupId=" + groupId + ", artifactId=" + artifactId + ", architectureType=" + architectureType
				+ ", buildStatus=" + buildStatus + "]";
	}

	
    

}
