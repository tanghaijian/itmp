package cn.pioneeruniverse.dev.entity;

import java.util.Date;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 *@author liushan
 *@Description 测试案例步骤执行实体类
 *@Date 2020/8/11
 *@return
 **/
public class TblTestSetCaseStepExecute extends BaseEntity{


	private Integer testSetCaseExecuteId;// 测试集案例执行

    private Integer stepOrder;// 步骤排序

    private String stepDescription;// 描述

    private String stepExpectedResult;// 结果

    private String stepActualResult;// 实际结果
    
    private Integer stepExecuteResult;// 执行结果
	
	public Integer getTestSetCaseExecuteId() {
		return testSetCaseExecuteId;
	}

	public void setTestSetCaseExecuteId(Integer testSetCaseExecuteId) {
		this.testSetCaseExecuteId = testSetCaseExecuteId;
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

	public String getStepActualResult() {
		return stepActualResult;
	}

	public void setStepActualResult(String stepActualResult) {
		this.stepActualResult = stepActualResult;
	}

	public Integer getStepExecuteResult() {
		return stepExecuteResult;
	}

	public void setStepExecuteResult(Integer stepExecuteResult) {
		this.stepExecuteResult = stepExecuteResult;
	}
    
    

}