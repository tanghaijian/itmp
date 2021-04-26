package cn.pioneeruniverse.dev.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 * @Author:weiji
 * @Description:系统源码管理配置实体类
 */
@TableName("tbl_system_scm")
public class TblSystemScm extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long systemId;

    /**
     * 环境类型（数据字典：1:DEV，2:SIT，3:UAT，4:PRD）
     */
    private Integer environmentType;
    /**
     * 源码管理方式（1:GIT，2:SVN）
     */
    private Integer scmType;

    private Long toolId;//工具ID

    private Long systemRepositoryId;//仓库ID

    private String scmUrl;//仓库url
    //仓库名称
    private String scmRepositoryName;

    private String scmBranch;//分支

    private Integer submitStatus;//代码提交开关（1:开，2:关）

    private String submitSuperUserNames;//代码提交管理(备注：此用户不受任何代码提交的约束)，id以,隔开

    private String submitUserNames;//代码提交用户清单(备注：若有设置，则按照此清单过滤；若没有设置，则所有人可提交)，id以,隔开

    private Long systemVersionId;//系统版本表主键

    private Long commissioningWindowId;//投产窗口

    private Integer buildStatus;//系统构建状态（1=空闲；2=构建中）
   
	private Integer deployStatus;//系统部署状态（1=空闲；2=部署中）
    
    @TableField(exist = false)
    private String submitSuperUserName;//代码提交管理姓名
    
    @TableField(exist = false)
    private String submitUserName;//代码提交用户清单姓名

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
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

    public String getScmUrl() {
        return scmUrl;
    }

    public void setScmUrl(String scmUrl) {
        this.scmUrl = scmUrl == null ? null : scmUrl.trim();
    }

    public String getScmBranch() {
        return scmBranch;
    }

    public void setScmBranch(String scmBranch) {
        this.scmBranch = scmBranch == null ? null : scmBranch.trim();
    }

    public Integer getBuildStatus() {
        return buildStatus;
    }

    public void setBuildStatus(Integer buildStatus) {
        this.buildStatus = buildStatus;
    }

    public Long getToolId() {
        return toolId;
    }

    public void setToolId(Long toolId) {
        this.toolId = toolId;
    }

    public String getScmRepositoryName() {
        return scmRepositoryName;
    }

    public void setScmRepositoryName(String scmRepositoryName) {
        this.scmRepositoryName = scmRepositoryName;
    }

    public Integer getSubmitStatus() {
        return submitStatus;
    }

    public void setSubmitStatus(Integer submitStatus) {
        this.submitStatus = submitStatus;
    }

    public Long getSystemVersionId() {
        return systemVersionId;
    }

    public void setSystemVersionId(Long systemVersionId) {
        this.systemVersionId = systemVersionId;
    }

    public Long getCommissioningWindowId() {
        return commissioningWindowId;
    }

    public void setCommissioningWindowId(Long commissioningWindowId) {
        this.commissioningWindowId = commissioningWindowId;
    }

    public String getSubmitSuperUserNames() {
        return submitSuperUserNames;
    }

    public void setSubmitSuperUserNames(String submitSuperUserNames) {
        this.submitSuperUserNames = submitSuperUserNames;
    }

    public String getSubmitUserNames() {
        return submitUserNames;
    }

    public void setSubmitUserNames(String submitUserNames) {
        this.submitUserNames = submitUserNames;
    }

	public String getSubmitSuperUserName() {
		return submitSuperUserName;
	}

	public void setSubmitSuperUserName(String submitSuperUserName) {
		this.submitSuperUserName = submitSuperUserName;
	}

	public String getSubmitUserName() {
		return submitUserName;
	}

	public void setSubmitUserName(String submitUserName) {
		this.submitUserName = submitUserName;
	}
	public Integer getDeployStatus() {
			return deployStatus;
	}

	public void setDeployStatus(Integer deployStatus) {
			this.deployStatus = deployStatus;
	}

    public Long getSystemRepositoryId() {
        return systemRepositoryId;
    }
    public void setSystemRepositoryId(Long systemRepositoryId) {
        this.systemRepositoryId = systemRepositoryId;
    }
}