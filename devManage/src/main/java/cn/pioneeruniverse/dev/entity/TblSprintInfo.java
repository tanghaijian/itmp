package cn.pioneeruniverse.dev.entity;

import java.util.Date;

import org.springframework.data.annotation.Transient;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
*@author liushan
*@Description 冲刺实体类
*@Date 2020/8/6
*@return
**/
@TableName("tbl_sprint_info")
public class TblSprintInfo extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long systemId;//系统ID

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    private Long projectId;//项目ID
	
    private String sprintName; //冲刺名

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date sprintStartDate;//冲刺开始日期
    
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date sprintEndDate;//冲刺结束日期
    
    private Integer validStatus;//状态 1=有效；2=无效

    private Long projectPlanId;//项目计划ID
    private Double  testEstimteWorkload;//测试总预估工作量


    private Double  estimteWorkload;//总预估量
    private Double  devEstimteWorkload;//开发总预估工作量
    public Double getEstimteWorkload() {
        return estimteWorkload;
    }

    public void setEstimteWorkload(Double estimteWorkload) {
        this.estimteWorkload = estimteWorkload;
    }

    public Double getDevEstimteWorkload() {
        return devEstimteWorkload;
    }

    public void setDevEstimteWorkload(Double devEstimteWorkload) {
        this.devEstimteWorkload = devEstimteWorkload;
    }
    public Double getTestEstimteWorkload() {
        return testEstimteWorkload;
    }

    public void setTestEstimteWorkload(Double testEstimteWorkload) {
        this.testEstimteWorkload = testEstimteWorkload;
    }



    @Transient
    private String systemName;//系统名
    @Transient
    private String projectPlanName;//项目计划名
    @Transient
    private Long projectIds;    //项目id
    @Transient
    private String projectName;    //项目名称
    @Transient
    private String systemIdList;    //系统id项目集
    private String sprintIdList;    //冲刺Id结果集
    @Transient
    private String systemList; //系统id，以,隔开

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public String getSprintName() {
        return sprintName;
    }

    public void setSprintName(String sprintName) {
        this.sprintName = sprintName == null ? null : sprintName.trim();
    }

    public Date getSprintStartDate() {
        return sprintStartDate;
    }

    public void setSprintStartDate(Date sprintStartDate) {
        this.sprintStartDate = sprintStartDate;
    }

    public Date getSprintEndDate() {
        return sprintEndDate;
    }

    public void setSprintEndDate(Date sprintEndDate) {
        this.sprintEndDate = sprintEndDate;
    }
    
	public Integer getValidStatus() {
		return validStatus;
	}

	public void setValidStatus(Integer validStatus) {
		this.validStatus = validStatus;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

    public Long getProjectPlanId() {
        return projectPlanId;
    }
    public void setProjectPlanId(Long projectPlanId) {
        this.projectPlanId = projectPlanId;
    }

    public String getProjectPlanName() {
        return projectPlanName;
    }
    public void setProjectPlanName(String projectPlanName) {
        this.projectPlanName = projectPlanName;
    }

    public Long getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(Long projectIds) {
        this.projectIds = projectIds;
    }

    public String getSystemList() {
        return systemList;
    }

    public void setSystemList(String systemList) {
        this.systemList = systemList;
    }

    public String getSystemIdList() {
        return systemIdList;
    }

    public void setSystemIdList(String systemIdList) {
        this.systemIdList = systemIdList;
    }

    public String getSprintIdList() {
        return sprintIdList;
    }

    public void setSprintIdList(String sprintIdList) {
        this.sprintIdList = sprintIdList;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String toString() {
        return "TblSprintInfo{" +
                "systemId=" + systemId +
                ", sprintName='" + sprintName + '\'' +
                ", sprintStartDate=" + sprintStartDate +
                ", sprintEndDate=" + sprintEndDate +
                ", validStatus=" + validStatus +
                ", projectPlanId=" + projectPlanId +
                ", systemName='" + systemName + '\'' +
                ", projectPlanName='" + projectPlanName + '\'' +
                ", projectIds=" + projectIds +
                ", projectName='" + projectName + '\'' +
                ", systemIdList='" + systemIdList + '\'' +
                ", sprintIdList='" + sprintIdList + '\'' +
                ", systemList='" + systemList + '\'' +
                '}';
    }
}