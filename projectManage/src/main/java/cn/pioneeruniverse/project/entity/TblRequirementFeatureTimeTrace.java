package cn.pioneeruniverse.project.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotations.TableName;
import cn.pioneeruniverse.common.entity.BaseEntity;

@TableName("tbl_requirement_feature_time_trace")
public class TblRequirementFeatureTimeTrace extends BaseEntity {

    private static final long serialVersionUID = 1L;
    private Long requirementFeatureId; 			//需求特性表（开发任务）主键
    private Timestamp requirementFeatureCreateTime; //特性编号(开还任务编号)
    
	public Long getRequirementFeatureId() {
		return requirementFeatureId;
	}
	public void setRequirementFeatureId(Long requirementFeatureId) {
		this.requirementFeatureId = requirementFeatureId;
	}
	public Timestamp getRequirementFeatureCreateTime() {
		return requirementFeatureCreateTime;
	}
	public void setRequirementFeatureCreateTime(Timestamp requirementFeatureCreateTime) {
		this.requirementFeatureCreateTime = requirementFeatureCreateTime;
	}
}