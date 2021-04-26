package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("tbl_system_module_scm")
public class TblSystemModuleScm extends BaseEntity {

    private static final long serialVersionUID = 1L;

    //private Integer environmentType;

    private Long systemId;//系统ID

    private Long systemModuleId; //系统子模块ID
    private Long systemScmId; //系统源码表ID

    private Integer environmentType; //环境类型
    private Integer scmType; //源码管理方式（1:GIT，2:SVN）

    private Long toolId; //工具表ID
    private String scmUrl; //源码管理地址
    private String scmBranch;//源码管理分支

    private Long systemRepositoryId;//系统源码仓库表主键
    //仓库名称
    private String scmRepositoryName;

    @TableField(exist = false)
    private String [] ids; //合并后的ID，源码配置中合并行显示

    @TableField(exist = false)
    private String [] moduleIds; //合并后的子模块ID，源码配置中合并行显示

    @TableField(exist = false)
    private String idsString; //ids的字符串形式

    @TableField(exist = false)
    private String moduleIdsString; //moduleIds的字符串形式

    public Long getSystemScmId() {
        return systemScmId;
    }

    public void setSystemScmId(Long systemScmId) {
        this.systemScmId = systemScmId;
    }

    private String dependencySystemModuleIds;

    private Integer sourceCodeUpdateStatus;

	/*public Integer getEnvironmentType() {
        return environmentType;
	}

	public void setEnvironmentType(Integer environmentType) {
		this.environmentType = environmentType;
	}*/

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

    public String getDependencySystemModuleIds() {
        return dependencySystemModuleIds;
    }

    public void setDependencySystemModuleIds(String dependencySystemModuleIds) {
        this.dependencySystemModuleIds = dependencySystemModuleIds == null ? null : dependencySystemModuleIds.trim();
    }

    public Integer getSourceCodeUpdateStatus() {
        return sourceCodeUpdateStatus;
    }

    public void setSourceCodeUpdateStatus(Integer sourceCodeUpdateStatus) {
        this.sourceCodeUpdateStatus = sourceCodeUpdateStatus;
    }

    public Integer getEnvironmentType() {
        return environmentType;
    }
    public void setEnvironmentType(Integer environmentType) {
        this.environmentType = environmentType;
    }

    public Integer getScmType() {
        return scmType;
    }
    public void setScmType(Integer scmType) {
        this.scmType = scmType;
    }

    public Long getToolId() {
        return toolId;
    }
    public void setToolId(Long toolId) {
        this.toolId = toolId;
    }

    public String getScmUrl() {
        return scmUrl;
    }
    public void setScmUrl(String scmUrl) {
        this.scmUrl = scmUrl;
    }

    public String getScmBranch() {
        return scmBranch;
    }
    public void setScmBranch(String scmBranch) {
        this.scmBranch = scmBranch;
    }

    @Override
    public String toString() {
        return "TblSystemModuleScm [systemId=" + systemId + ", systemModuleId=" + systemModuleId + ", systemScmId="
                + systemScmId + ", dependencySystemModuleIds=" + dependencySystemModuleIds + ", sourceCodeUpdateStatus="
                + sourceCodeUpdateStatus + "]";
    }

    public Long getSystemRepositoryId() {
        return systemRepositoryId;
    }
    public void setSystemRepositoryId(Long systemRepositoryId) {
        this.systemRepositoryId = systemRepositoryId;
    }

    public String getScmRepositoryName() {
        return scmRepositoryName;
    }
    public void setScmRepositoryName(String scmRepositoryName) {
        this.scmRepositoryName = scmRepositoryName;
    }

    public String[] getIds() {
        return ids;
    }
    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public String[] getModuleIds() {
        return moduleIds;
    }
    public void setModuleIds(String[] moduleIds) {
        this.moduleIds = moduleIds;
    }

    public String getIdsString() {
        return idsString;
    }
    public void setIdsString(String idsString) {
        this.idsString = idsString;
    }

    public String getModuleIdsString() {
        return moduleIdsString;
    }
    public void setModuleIdsString(String moduleIdsString) {
        this.moduleIdsString = moduleIdsString;
    }
}