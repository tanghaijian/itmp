package cn.pioneeruniverse.dev.entity;

import java.util.Date;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("tbl_project_info")
public class TblProjectInfo extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String projectName;

    private String projectCode;

    private Byte projectType;

    private Byte projectStatus;

    private Date approvalDate;

    private Byte longTimeStatus;

    private Date planStartDate;

    private Date planEndDate;

    private Date planCommissioningDate;

    private Long deptId;

    private Long managerUserId;


    private String projectOverview;

    @TableField(exist = false)
    private Long systemId;  //系统id
    @TableField(exist = false)
    private String systemName;  //系统名称
    @TableField(exist = false)
    private String systemCode;  //系统编号

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode == null ? null : projectCode.trim();
    }

    public Byte getProjectType() {
        return projectType;
    }

    public void setProjectType(Byte projectType) {
        this.projectType = projectType;
    }

    public Byte getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(Byte projectStatus) {
        this.projectStatus = projectStatus;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public Byte getLongTimeStatus() {
        return longTimeStatus;
    }

    public void setLongTimeStatus(Byte longTimeStatus) {
        this.longTimeStatus = longTimeStatus;
    }

    public Date getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(Date planStartDate) {
        this.planStartDate = planStartDate;
    }

    public Date getPlanEndDate() {
        return planEndDate;
    }

    public void setPlanEndDate(Date planEndDate) {
        this.planEndDate = planEndDate;
    }

    public Date getPlanCommissioningDate() {
        return planCommissioningDate;
    }

    public void setPlanCommissioningDate(Date planCommissioningDate) {
        this.planCommissioningDate = planCommissioningDate;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getManagerUserId() {
        return managerUserId;
    }

    public void setManagerUserId(Long managerUserId) {
        this.managerUserId = managerUserId;
    }


    public String getProjectOverview() {
        return projectOverview;
    }

    public void setProjectOverview(String projectOverview) {
        this.projectOverview = projectOverview == null ? null : projectOverview.trim();
    }

    public String getSystemName() {
        return systemName;
    }
    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public Long getSystemId() {
        return systemId;
    }
    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public String getSystemCode() {
        return systemCode;
    }
    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }
}