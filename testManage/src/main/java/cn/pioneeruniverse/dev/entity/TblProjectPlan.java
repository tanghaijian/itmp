package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("tbl_project_plan")
public class TblProjectPlan extends BaseEntity{

	private static final long serialVersionUID = 1L;

    private Long projectId;   //项目ID
    private String planName;  //计划名称
    private String planCode;  //计划编号
    private Long responsibleUserId;   //责任人ID

    @TableField(exist = false)
    private Long systemId;          //系统ID
    @TableField(exist = false)
    private String projectName;     //项目名称
    @TableField(exist = false)
    private String responsibleUserName;  //责任人名称

    public Long getProjectId() {
        return projectId;
    }
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getPlanName() {
        return planName;
    }
    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanCode() {
        return planCode;
    }
    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public Long getResponsibleUserId() {
        return responsibleUserId;
    }
    public void setResponsibleUserId(Long responsibleUserId) {
        this.responsibleUserId = responsibleUserId;
    }

    public Long getSystemId() {
        return systemId;
    }
    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getResponsibleUserName() {
        return responsibleUserName;
    }
    public void setResponsibleUserName(String responsibleUserName) {
        this.responsibleUserName = responsibleUserName;
    }
}