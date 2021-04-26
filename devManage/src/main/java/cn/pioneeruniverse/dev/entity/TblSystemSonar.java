package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;
/**
 * @Author:weiji
 * @Description:系统SONAR配置实体类
 */

@TableName("tbl_system_sonar")
public class TblSystemSonar extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long systemId;//系统ID

    private Long systemScmId;//TblSystemScm的ID

    private Long systemModuleId;//系统模块ID

    private Long toolId;//工具ID

    private String sonarProjectKey;//SONAR.PROJECTKEY（自动生成）

    private String sonarProjectName;//SONAR.PROJECTNAME（自动生成）

    private String sonarSources;//SONAR.SOURCES（自动生成）

    private String sonarJavaBinaries;//SONAR.JAVA.BINARIES（自动生成）
    private Long systemJenkinsId;//TblSystemJenkins的ID

    public Long getSystemJenkinsId() {
        return systemJenkinsId;
    }

    public void setSystemJenkinsId(Long systemJenkinsId) {
        this.systemJenkinsId = systemJenkinsId;
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

    public Long getSystemModuleId() {
        return systemModuleId;
    }

    public void setSystemModuleId(Long systemModuleId) {
        this.systemModuleId = systemModuleId;
    }

    public String getSonarProjectKey() {
        return sonarProjectKey;
    }

    public void setSonarProjectKey(String sonarProjectKey) {
        this.sonarProjectKey = sonarProjectKey == null ? null : sonarProjectKey.trim();
    }

    public String getSonarProjectName() {
        return sonarProjectName;
    }

    public void setSonarProjectName(String sonarProjectName) {
        this.sonarProjectName = sonarProjectName == null ? null : sonarProjectName.trim();
    }

    public String getSonarSources() {
        return sonarSources;
    }

    public void setSonarSources(String sonarSources) {
        this.sonarSources = sonarSources == null ? null : sonarSources.trim();
    }

    public String getSonarJavaBinaries() {
        return sonarJavaBinaries;
    }

    public void setSonarJavaBinaries(String sonarJavaBinaries) {
        this.sonarJavaBinaries = sonarJavaBinaries == null ? null : sonarJavaBinaries.trim();
    }

    public Long getToolId() {
        return toolId;
    }

    public void setToolId(Long toolId) {
        this.toolId = toolId;
    }

}