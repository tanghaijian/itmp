package cn.pioneeruniverse.project.dto;

import java.io.Serializable;
import java.sql.Date;


/**
 * 项目主页基本信息
 */
public class ProjectHomeDTO implements Serializable {

    private static final long serialVersionUID = -6496067455862633155L;
    private String projectCode; //项目编码
    private String projectName; //项目名称
    private Integer projectStatus; //项目状态（数据字典 1=立项；2=实施；3=验收；4=结项）
    private Integer status;     //项目表状态 1=正常；2=删除
    private Integer developmentMode; //开发模式（数据字典，1:敏捷模型，2:瀑布模型）
    private String systemName;  // 系统名称
    private Integer projectType;  //项目类型（数据字典 1=IT运维类；2=IT新建类）
    private Date planStartDate; //计划开始日期（项目周期）
    private Date planEndDate; //计划结束日期（项目周期）
    private String budgetNumber;    //预算编号
    private String userName;  //项目管理员 ? 创建者
    private String interactedSystem;    //关联项目
    private Long systemId;     //项目id
    private Long userId;    //用户id
    private String projectBackground;   //项目背景
    private String systemCode;  //系统编码
    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(Integer projectStatus) {
        this.projectStatus = projectStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDevelopmentMode() {
        return developmentMode;
    }

    public void setDevelopmentMode(Integer developmentMode) {
        this.developmentMode = developmentMode;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public Integer getProjectType() {
        return projectType;
    }

    public void setProjectType(Integer projectType) {
        this.projectType = projectType;
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

    public String getBudgetNumber() {
        return budgetNumber;
    }

    public void setBudgetNumber(String budgetNumber) {
        this.budgetNumber = budgetNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getInteractedSystem() {
        return interactedSystem;
    }

    public void setInteractedSystem(String interactedSystem) {
        this.interactedSystem = interactedSystem;
    }

    public Long getProjectId() {
        return systemId;
    }

    public void setProjectId(Long projectId) {
        this.systemId = projectId;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getProjectBackground() {
        return projectBackground;
    }

    public void setProjectBackground(String projectBackground) {
        this.projectBackground = projectBackground;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    @Override
    public String toString() {
        return "ProjectHomeDTO{" +
                "projectCode='" + projectCode + '\'' +
                ", projectName='" + projectName + '\'' +
                ", projectStatus=" + projectStatus +
                ", status=" + status +
                ", developmentMode=" + developmentMode +
                ", systemName='" + systemName + '\'' +
                ", projectType=" + projectType +
                ", planStartDate=" + planStartDate +
                ", planEndDate=" + planEndDate +
                ", budgetNumber='" + budgetNumber + '\'' +
                ", userName='" + userName + '\'' +
                ", interactedSystem='" + interactedSystem + '\'' +
                ", systemId=" + systemId +
                ", userId=" + userId +
                ", projectBackground='" + projectBackground + '\'' +
                ", systemCode='" + systemCode + '\'' +
                '}';
    }
}
