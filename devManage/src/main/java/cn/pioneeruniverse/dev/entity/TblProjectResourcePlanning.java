package cn.pioneeruniverse.dev.entity;

import java.util.Date;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 
* @ClassName: TblProjectResourcePlanning
* @Description: 项目资源计划（未用）
* @author author
* @date 2020年8月24日 下午3:50:26
*
 */
@TableName("tbl_project_resource_planning")
public class TblProjectResourcePlanning extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long projectId;

    private Long userId;

    private Date planStartDate;

    private Date planEndDate;

    private Integer workloadRatio;


    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

}