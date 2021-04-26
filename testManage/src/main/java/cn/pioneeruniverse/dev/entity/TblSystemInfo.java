package cn.pioneeruniverse.dev.entity;

import java.util.Date;
import java.util.List;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
@TableName("tbl_system_info")
public class TblSystemInfo extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


    private Long projectId;

    private String systemName;

    private String systemCode;

    private String systemShortName;
   
    private Integer taskMessageStatus;
    
    

    public Integer getTaskMessageStatus() {
		return taskMessageStatus;
	}

	public void setTaskMessageStatus(Integer taskMessageStatus) {
		this.taskMessageStatus = taskMessageStatus;
	}

	public Integer getDevelopmentMode() {
        return developmentMode;
    }

    public void setDevelopmentMode(Integer developmentMode) {
        this.developmentMode = developmentMode;
    }

    private Integer developmentMode;

    private String systemType;
    @TableField(exist = false)
    private List<String> systemTypeList;
    private String groupId;

    private String artifactId;

    private Byte architectureType;

    private Byte buildType;

    private Byte scmStrategy;
    
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

    public String getSystemShortName() {
        return systemShortName;
    }

    public void setSystemShortName(String systemShortName) {
        this.systemShortName = systemShortName == null ? null : systemShortName.trim();
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType == null ? null : systemType.trim();
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

    public Byte getArchitectureType() {
        return architectureType;
    }

    public void setArchitectureType(Byte architectureType) {
        this.architectureType = architectureType;
    }

    public Byte getBuildType() {
        return buildType;
    }

    public void setBuildType(Byte buildType) {
        this.buildType = buildType;
    }

    public Byte getScmStrategy() {
        return scmStrategy;
    }

    public void setScmStrategy(Byte scmStrategy) {
        this.scmStrategy = scmStrategy;
    }

	public List<String> getSystemTypeList() {
		return systemTypeList;
	}

	public void setSystemTypeList(List<String> systemTypeList) {
		this.systemTypeList = systemTypeList;
	}

    public Long getProjectId() {
        return projectId;
    }
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}