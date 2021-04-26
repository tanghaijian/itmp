package cn.pioneeruniverse.project.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 *@author
 *@Description 项目类
 *@Date 2020/8/7
 *@return
 **/
@TableName("tbl_project_info")
public class TblProjectInfo extends BaseEntity{

	private static final long serialVersionUID = 1L;
	
    private String projectName;  //项目名称

    private String projectCode;  //项目编号

    private Integer projectType;   //项目类型
    
    private Integer projectClass;  //项目类型
    
    private Integer developmentMode;  //开发模式

    private Integer projectStatus;  //项目状态
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date approvalDate;  //立项日期
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date closeDate;  //结项日期
    
    private Integer longTimeStatus;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date planStartDate;  //计划开始时间
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date planEndDate;  //计划结束时间
  
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date planCommissioningDate;  //计划投产日期
    
    private Integer projectPlanNumber;    //项目计划版本号
    
    private Integer projectPlanStatus;    //项目计划状态

    private Long deptId;  //牵头部门id
    
    private Long businessDeptId;  //业务部门
    
    private String budgetRemark;  //项目备注
    
    private String budgetNumber;  //预算编号

    private Long managerUserId;  //项目管理员

    private String projectOverview;  //项目简介
    
    private String projectScope;  //项目范围
    
    private String projectBackground; //项目背景
    
    @Transient
    private String deptName;//部门名称
    
    @Transient
    private String businessDeptName; //业务部门名称
    
    @Transient
    private String managerUserName;//项目管理人员姓名
    
    @Transient
    private List<Long> ids; //所有项目ID，关联查询用
    
    @Transient
    private String projectTypeName;//项目类型名称
    
    @Transient
    private String projectStatusName;//项目状态名称
    
    @Transient
    private List<String> systemName;     //涉及系统
    
    @Transient
    private List<String> projectManageUsers;   //项目管理岗人员
    
    @Transient
    private List<String> developManageUsers;    //开发管理岗人员
    
    @Transient
    private List<TblProjectGroup> list;           //项目小组集合
    
    @Transient
    private List<TblSystemInfo> systemList; //项目关联系统列表
    
    @Transient
    private List<Long> systemIds;//关联的系统的所有ID
    
    @Transient
    private String projectClassName; //项目类型名称
    
    @Transient
    private String projectUserIds; //项目成员
    
    @Transient
    private String developSystemIds; //开发系统

    @Transient
    private String peripheralSystemIds; //周边系统
    
    @Transient
    private String deptNumber;  //所属处室部门编号也就是数组字典中项目部门的键值
    
    @Transient
    private String businessDeptNumber;//业务部门编码（数据字典）
    
    @Transient
    private Long systemId; //查询条件系统
    
    public List<TblProjectGroup> getList() {
		return list;
	}

	public List<TblSystemInfo> getSystemList() {
		return systemList;
	}

	public void setSystemList(List<TblSystemInfo> systemList) {
		this.systemList = systemList;
	}

	public void setList(List<TblProjectGroup> list) {
		this.list = list;
	}

	public List<String> getProjectManageUsers() {
		return projectManageUsers;
	}

	public void setProjectManageUsers(List<String> projectManageUsers) {
		this.projectManageUsers = projectManageUsers;
	}

	public List<String> getDevelopManageUsers() {
		return developManageUsers;
	}

	public void setDevelopManageUsers(List<String> developManageUsers) {
		this.developManageUsers = developManageUsers;
	}

	public List<String> getSystemName() {
		return systemName;
	}

	public void setSystemName(List<String> systemName) {
		this.systemName = systemName;
	}

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

    public Integer getProjectType() {
        return projectType;
    }

    public void setProjectType(Integer projectType) {
        this.projectType = projectType;
    }

    public Integer getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(Integer projectStatus) {
        this.projectStatus = projectStatus;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
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

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Integer getLongTimeStatus() {
		return longTimeStatus;
	}

	public void setLongTimeStatus(Integer longTimeStatus) {
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

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public String getProjectTypeName() {
		return projectTypeName;
	}

	public void setProjectTypeName(String projectTypeName) {
		this.projectTypeName = projectTypeName;
	}

	public String getProjectStatusName() {
		return projectStatusName;
	}

	public void setProjectStatusName(String projectStatusName) {
		this.projectStatusName = projectStatusName;
	}


	public List<Long> getSystemIds() {
		return systemIds;
	}

	public void setSystemIds(List<Long> systemIds) {
		this.systemIds = systemIds;
	}

	public Integer getProjectClass() {
		return projectClass;
	}

	public void setProjectClass(Integer projectClass) {
		this.projectClass = projectClass;
	}

	public Integer getDevelopmentMode() {
		return developmentMode;
	}

	public void setDevelopmentMode(Integer developmentMode) {
		this.developmentMode = developmentMode;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public Integer getProjectPlanNumber() {
		return projectPlanNumber;
	}

	public void setProjectPlanNumber(Integer projectPlanNumber) {
		this.projectPlanNumber = projectPlanNumber;
	}

	public Integer getProjectPlanStatus() {
		return projectPlanStatus;
	}

	public void setProjectPlanStatus(Integer projectPlanStatus) {
		this.projectPlanStatus = projectPlanStatus;
	}

	public Long getBusinessDeptId() {
		return businessDeptId;
	}

	public void setBusinessDeptId(Long businessDeptId) {
		this.businessDeptId = businessDeptId;
	}

	public String getBudgetRemark() {
		return budgetRemark;
	}

	public void setBudgetRemark(String budgetRemark) {
		this.budgetRemark = budgetRemark;
	}

	public String getBudgetNumber() {
		return budgetNumber;
	}

	public void setBudgetNumber(String budgetNumber) {
		this.budgetNumber = budgetNumber;
	}

	public String getProjectScope() {
		return projectScope;
	}

	public void setProjectScope(String projectScope) {
		this.projectScope = projectScope;
	}

	public String getBusinessDeptName() {
		return businessDeptName;
	}

	public void setBusinessDeptName(String businessDeptName) {
		this.businessDeptName = businessDeptName;
	}

	public String getManagerUserName() {
		return managerUserName;
	}

	public void setManagerUserName(String managerUserName) {
		this.managerUserName = managerUserName;
	}

	public String getProjectClassName() {
		return projectClassName;
	}

	public void setProjectClassName(String projectClassName) {
		this.projectClassName = projectClassName;
	}

	public String getProjectUserIds() {
		return projectUserIds;
	}

	public void setProjectUserIds(String projectUserIds) {
		this.projectUserIds = projectUserIds;
	}

	public String getDevelopSystemIds() {
		return developSystemIds;
	}

	public void setDevelopSystemIds(String developSystemIds) {
		this.developSystemIds = developSystemIds;
	}

	public String getPeripheralSystemIds() {
		return peripheralSystemIds;
	}

	public void setPeripheralSystemIds(String peripheralSystemIds) {
		this.peripheralSystemIds = peripheralSystemIds;
	}

	public String getProjectBackground() {
		return projectBackground;
	}

	public void setProjectBackground(String projectBackground) {
		this.projectBackground = projectBackground;
	}

	public String getDeptNumber() {
		return deptNumber;
	}

	public void setDeptNumber(String deptNumber) {
		this.deptNumber = deptNumber;
	}

	public String getBusinessDeptNumber() {
		return businessDeptNumber;
	}

	public void setBusinessDeptNumber(String businessDeptNumber) {
		this.businessDeptNumber = businessDeptNumber;
	}

	public Long getSystemId() {
		return systemId;
	}

	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}

	@Override
	public String toString() {
		return "TblProjectInfo [projectName=" + projectName + ", projectCode=" + projectCode + ", projectType="
				+ projectType + ", projectStatus=" + projectStatus + ", approvalDate=" + approvalDate
				+ ", longTimeStatus=" + longTimeStatus + ", planStartDate=" + planStartDate + ", planEndDate="
				+ planEndDate + ", planCommissioningDate=" + planCommissioningDate + ", deptId=" + deptId
				+ ", managerUserId=" + managerUserId + ", projectOverview=" + projectOverview + ", deptName=" + deptName
				+ ", ids=" + ids + ", projectTypeName=" + projectTypeName + ", projectStatusName=" + projectStatusName
				+ ", systemName=" + systemName + ", projectManageUsers=" + projectManageUsers + ", developManageUsers="
				+ developManageUsers + ", list=" + list + ", systemList=" + systemList + ", systemIds=" + systemIds
				+ "]";
	}

    
    

}