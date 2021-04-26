package cn.pioneeruniverse.dev.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

@TableName("tbl_artifact_quality_detail")
public class TblArtifactQualityDetail extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 制品表主键
	 **/
	private Long artifactId;//制品表主键

	/**
	 * 质量指标表主键
	 **/
	private Long qualityMetricId;//质量指标表主键

	/**
	 * 指标值
	 **/
	private String qualityMetricValue;//指标值

	/**
	 * 指标值明细（S3key）
	 **/
	private String qualityMetricValueDetail;//指标值明细（S3key）

	/**
	 * 指标判断结果：1=SUCCESS；2=ERROR; 3=WARNING
	 **/
	private Integer qualityMetricStatus;//指标判断结果：1=SUCCESS；2=ERROR; 3=WARNING

	public Long getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(Long artifactId) {
		this.artifactId = artifactId;
	}

	public Long getQualityMetricId() {
		return qualityMetricId;
	}

	public void setQualityMetricId(Long qualityMetricId) {
		this.qualityMetricId = qualityMetricId;
	}

	public String getQualityMetricValue() {
		return qualityMetricValue;
	}

	public void setQualityMetricValue(String qualityMetricValue) {
		this.qualityMetricValue = qualityMetricValue == null ? null : qualityMetricValue.trim();
	}

	public String getQualityMetricValueDetail() {
		return qualityMetricValueDetail;
	}

	public void setQualityMetricValueDetail(String qualityMetricValueDetail) {
		this.qualityMetricValueDetail = qualityMetricValueDetail == null ? null : qualityMetricValueDetail.trim();
	}

	public Integer getQualityMetricStatus() {
		return qualityMetricStatus;
	}

	public void setQualityMetricStatus(Integer qualityMetricStatus) {
		this.qualityMetricStatus = qualityMetricStatus;
	}

}