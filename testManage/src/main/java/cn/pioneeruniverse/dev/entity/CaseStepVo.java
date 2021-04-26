package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import cn.pioneeruniverse.dev.common.ExcelField;

public class CaseStepVo extends BaseEntity{

	/**
	 * 步骤排序号
	 **/
	private Integer stepOrder;	//

	/**
	 * 步骤描述
	 **/
    private String stepDescription;  //

	/**
	 * 步骤预期结果
	 **/
    private String stepExpectedResult; //

	@ExcelField(title="步骤", type=0, align=1, sort=7)
	public Integer getStepOrder() {
		return stepOrder;
	}
	public void setStepOrder(Integer stepOrder) {
		this.stepOrder = stepOrder;
	}

	@ExcelField(title="步骤描述", type=0, align=1, sort=8)
	public String getStepDescription() {
		return stepDescription;
	}
	public void setStepDescription(String stepDescription) {
		this.stepDescription = stepDescription;
	}

	@ExcelField(title="步骤预期结果", type=0, align=1, sort=9)
	public String getStepExpectedResult() {
		return stepExpectedResult;
	}
	public void setStepExpectedResult(String stepExpectedResult) {
		this.stepExpectedResult = stepExpectedResult;
	}

	@Override
	public String toString() {
		return "CaseStepVo{" +
				"stepOrder=" + stepOrder +
				", stepDescription='" + stepDescription + '\'' +
				", stepExpectedResult='" + stepExpectedResult + '\'' +
				'}';
	}
}
