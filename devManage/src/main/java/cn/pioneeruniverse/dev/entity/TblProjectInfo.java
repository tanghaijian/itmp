package cn.pioneeruniverse.dev.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Transient;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 *
 * @ClassName: TblProjectInfo
 * @Description: 项目实体类
 * @author author
 *
 */
@TableName("tbl_project_info")
public class TblProjectInfo extends BaseEntity{

	private static final long serialVersionUID = 1L;
	
    private String projectName;  //项目名称

    private String projectCode;  //项目编号

    private Integer projectType;   //项目类型

    private Integer projectStatus;  //项目状态

    private Date approvalDate;  //立项日期
    
    private Integer longTimeStatus;
    
    @JsonFormat(pattern = "yyyy/MM/dd", timezone="GMT+8")
    private Date planStartDate;  //计划开始时间
    
    @JsonFormat(pattern = "yyyy/MM/dd", timezone="GMT+8")
    private Date planEndDate;  //计划结束时间
  
    private Date planCommissioningDate;  //计划投产日期

    private Long deptId;  //牵头部门id

    private Long managerUserId;  //项目管理员

    private String projectOverview;  //项目简介


    @TableField(exist = false)
    private Long systemId;
    @TableField(exist = false)
    private String systemName1;  //系统编号
    @TableField(exist = false)
    private String systemCode;  //系统编号
    @Transient
    @TableField(exist = false)
    private String deptName;
    
    @Transient
    @TableField(exist = false)
    private List<Long> ids;
    
    @Transient
    @TableField(exist = false)
    private String projectTypeName;
    
    @Transient
    @TableField(exist = false)
    private String projectStatusName;
    
    @Transient
    @TableField(exist = false)
    private List<String> systemName;     //涉及系统
    
    @Transient
    @TableField(exist = false)
    private List<String> projectManageUsers;   //项目管理岗人员
    
    @Transient
    @TableField(exist = false)
    private List<String> developManageUsers;    //开发管理岗人员
          
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

    public Long getSystemId() {
        return systemId;
    }
    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public String getSystemName1() {
        return systemName1;
    }
    public void setSystemName1(String systemName1) {
        this.systemName1 = systemName1;
    }

    public String getSystemCode() {
        return systemCode;
    }
    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    @Override
    public String toString() {
        return "TblProjectInfo{" +
                "projectName='" + projectName + '\'' +
                ", projectCode='" + projectCode + '\'' +
                ", projectType=" + projectType +
                ", projectStatus=" + projectStatus +
                ", approvalDate=" + approvalDate +
                ", longTimeStatus=" + longTimeStatus +
                ", planStartDate=" + planStartDate +
                ", planEndDate=" + planEndDate +
                ", planCommissioningDate=" + planCommissioningDate +
                ", deptId=" + deptId +
                ", managerUserId=" + managerUserId +
                ", projectOverview='" + projectOverview + '\'' +
                ", deptName='" + deptName + '\'' +
                ", ids=" + ids +
                ", projectTypeName='" + projectTypeName + '\'' +
                ", projectStatusName='" + projectStatusName + '\'' +
                ", systemName=" + systemName +
                ", projectManageUsers=" + projectManageUsers +
                ", developManageUsers=" + developManageUsers +
                '}';
    }
}