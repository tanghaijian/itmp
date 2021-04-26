package cn.pioneeruniverse.dev.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 *
 * @ClassName:TblArtifactRequirementFeature
 * @Description 制品开发任务关联表
 * @author author
 * @date 2020年8月25日
 *
 */
@TableName("tbl_artifact_requirement_feature")
public class TblArtifactRequirementFeature extends BaseEntity{

	private static final long serialVersionUID = -2641141539727484292L;
	private Long artifactId;//制品ID
	private Long requirementFeatureId; //开发任务ID
	public Long getArtifactId() {
		return artifactId;
	}
	public void setArtifactId(Long artifactId) {
		this.artifactId = artifactId;
	}
	public Long getRequirementFeatureId() {
		return requirementFeatureId;
	}
	public void setRequirementFeatureId(Long requirementFeatureId) {
		this.requirementFeatureId = requirementFeatureId;
	}

	
}
