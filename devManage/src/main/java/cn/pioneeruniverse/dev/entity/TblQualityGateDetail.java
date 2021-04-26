package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;

import java.util.Date;


/**
 *
 * @ClassName: TblQualityGateDetail
 * @Description: 质量门禁明细表
 * @author author
 *
 */

public class TblQualityGateDetail extends BaseEntity {
    private Long id;

    private Long qualityGateId;//质量门禁id

    private Long qualityMetricId;//质量指标ID

    private String operatorType;//操作符类型

    private String warningValue;//警告指标阀值（当指标值类型是字符串时，如：A;B）

    private String forbiddenValue;//禁止指标阀值（当指标值类型是字符串时，如：C;D）



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQualityGateId() {
        return qualityGateId;
    }

    public void setQualityGateId(Long qualityGateId) {
        this.qualityGateId = qualityGateId;
    }

    public Long getQualityMetricId() {
        return qualityMetricId;
    }

    public void setQualityMetricId(Long qualityMetricId) {
        this.qualityMetricId = qualityMetricId;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType == null ? null : operatorType.trim();
    }

    public String getWarningValue() {
        return warningValue;
    }

    public void setWarningValue(String warningValue) {
        this.warningValue = warningValue == null ? null : warningValue.trim();
    }

    public String getForbiddenValue() {
        return forbiddenValue;
    }

    public void setForbiddenValue(String forbiddenValue) {
        this.forbiddenValue = forbiddenValue == null ? null : forbiddenValue.trim();
    }


}