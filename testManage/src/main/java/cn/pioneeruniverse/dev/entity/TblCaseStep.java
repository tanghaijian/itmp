package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import cn.pioneeruniverse.dev.common.ExcelField;

/**
 * 案例步骤
 */
public class TblCaseStep extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	private Long caseId; //案例表主键
	
	private Integer stepOrder; //步骤排序号
	
	private String stepDescription; //步骤描述
	
	private String stepExpectedResult; //步骤预期结果

	public Long getCaseId() {
		return caseId;
	}

	public void setCaseId(Long caseId) {
		this.caseId = caseId;
	}

	@ExcelField(title="步骤", type=1, align=1, sort=7)
	public Integer getStepOrder() {
		return stepOrder;
	}

	public void setStepOrder(Integer stepOrder) {
		this.stepOrder = stepOrder;
	}

	@ExcelField(title="步骤描述", type=1, align=1, sort=8)
	public String getStepDescription() {
		return stepDescription;
	}

	public void setStepDescription(String stepDescription) {
		this.stepDescription = stepDescription;
	}

	@ExcelField(title="步骤预期结果", type=1, align=1, sort=9)
	public String getStepExpectedResult() {
		return stepExpectedResult;
	}

	public void setStepExpectedResult(String stepExpectedResult) {
		this.stepExpectedResult = stepExpectedResult;
	}

	@Override
	public String toString() {
		return "TblCaseStep{" +
				"caseId=" + caseId +
				", stepOrder=" + stepOrder +
				", stepDescription='" + stepDescription + '\'' +
				", stepExpectedResult='" + stepExpectedResult + '\'' +
				'}';
	}
}
