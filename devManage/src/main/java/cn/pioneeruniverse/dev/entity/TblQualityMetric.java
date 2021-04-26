package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;

import java.util.Date;


/**
 *
 * @ClassName: TblQualityMetric
 * @Description: 质量指标表
 * @author author
 *
 */
public class TblQualityMetric extends BaseEntity {
  private Long id;

  private String metricName;//指标名称

  private String metricCode;//指标编码

  private String metricType;//指标分类（数据字典）

  private Byte metricValueType;//指标值类型（1:数字，2:字符串）

  private String metricValues;//枚举值（当指标值类型是字符串时，如：A;B;C;D）

  private String operatorTypes;//支持操作符类型（如：>;>=;<;<=;==;!=）

  private Byte metricOrder;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getMetricName() {
    return metricName;
  }

  public void setMetricName(String metricName) {
    this.metricName = metricName == null ? null : metricName.trim();
  }

  public String getMetricCode() {
    return metricCode;
  }

  public void setMetricCode(String metricCode) {
    this.metricCode = metricCode == null ? null : metricCode.trim();
  }

  public String getMetricType() {
    return metricType;
  }

  public void setMetricType(String metricType) {
    this.metricType = metricType == null ? null : metricType.trim();
  }

  public Byte getMetricValueType() {
    return metricValueType;
  }

  public void setMetricValueType(Byte metricValueType) {
    this.metricValueType = metricValueType;
  }

  public String getMetricValues() {
    return metricValues;
  }

  public void setMetricValues(String metricValues) {
    this.metricValues = metricValues == null ? null : metricValues.trim();
  }

  public String getOperatorTypes() {
    return operatorTypes;
  }

  public void setOperatorTypes(String operatorTypes) {
    this.operatorTypes = operatorTypes == null ? null : operatorTypes.trim();
  }

  public Byte getMetricOrder() {
    return metricOrder;
  }

  public void setMetricOrder(Byte metricOrder) {
    this.metricOrder = metricOrder;
  }
}

