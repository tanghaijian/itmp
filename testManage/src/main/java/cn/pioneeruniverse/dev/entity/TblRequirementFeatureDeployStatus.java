package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.sql.Timestamp;

@TableName("tbl_requirement_feature_deploy_status")
public class TblRequirementFeatureDeployStatus extends BaseEntity {
    /**
     * 开发任务id
     **/
    private Long requirementFeatureId; //开发任务id

    /**
     * 部署状态
     **/
    private Integer deployStatu;        //部署状态

    /**
     * 部署时间
     **/
    private Timestamp deployTime;       //部署时间

    @TableField(exist = false)
    private String deploy;

    public Long getRequirementFeatureId() {
        return requirementFeatureId;
    }
    public void setRequirementFeatureId(Long requirementFeatureId) {
        this.requirementFeatureId = requirementFeatureId;
    }

    public Integer getDeployStatu() {
        return deployStatu;
    }
    public void setDeployStatu(Integer deployStatu) {
        this.deployStatu = deployStatu;
    }

    public Timestamp getDeployTime() {
        return deployTime;
    }
    public void setDeployTime(Timestamp deployTime) {
        this.deployTime = deployTime;
    }

    public String getDeploy() {
        return deploy;
    }
    public void setDeploy(String deploy) {
        this.deploy = deploy;
    }
}
