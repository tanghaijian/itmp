package cn.pioneeruniverse.dev.entity;

import java.util.Date;

import cn.pioneeruniverse.common.entity.BaseEntity;

public class TblTestSetCaseStep extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = -4746892603803783133L;

	private Long testSetCaseId;// 测试集案例

    private Integer stepOrder;// 步骤排序

    private String stepDescription;// 步骤描述

    private String stepExpectedResult;// 步骤结果

	public Long getTestSetCaseId() {
		return testSetCaseId;
	}

	public void setTestSetCaseId(Long testSetCaseId) {
		this.testSetCaseId = testSetCaseId;
	}

	public Integer getStepOrder() {
		return stepOrder;
	}

	public void setStepOrder(Integer stepOrder) {
		this.stepOrder = stepOrder;
	}

	public String getStepDescription() {
		return stepDescription;
	}

	public void setStepDescription(String stepDescription) {
		this.stepDescription = stepDescription;
	}

	public String getStepExpectedResult() {
		return stepExpectedResult;
	}

	public void setStepExpectedResult(String stepExpectedResult) {
		this.stepExpectedResult = stepExpectedResult;
	}

	@Override
	public String toString() {
		return "TblTestSetCaseStep{" +
				"testSetCaseId=" + testSetCaseId +
				", stepOrder=" + stepOrder +
				", stepDescription='" + stepDescription + '\'' +
				", stepExpectedResult='" + stepExpectedResult + '\'' +
				'}';
	}
}