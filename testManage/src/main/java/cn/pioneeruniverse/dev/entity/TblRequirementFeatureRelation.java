package cn.pioneeruniverse.dev.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import java.util.Date;
/**
 *
 * @ClassName:TblRequirementFeatureRelation
 * @Description:任务关联表
 * @author author
 * @date 2020年8月16日
 *
 */
@TableName("tbl_requirement_feature_relation")
public class TblRequirementFeatureRelation {
    private Long id;

    private Long devRequirementFeatureId;

    private Long testRequirementFeatureId;

    private Integer status;

    private Long createBy;

    private Date createDate;

    private Long lastUpdateBy;

    private Date lastUpdateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDevRequirementFeatureId() {
        return devRequirementFeatureId;
    }

    public void setDevRequirementFeatureId(Long devRequirementFeatureId) {
        this.devRequirementFeatureId = devRequirementFeatureId;
    }

    public Long getTestRequirementFeatureId() {
        return testRequirementFeatureId;
    }

    public void setTestRequirementFeatureId(Long testRequirementFeatureId) {
        this.testRequirementFeatureId = testRequirementFeatureId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(Long lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}