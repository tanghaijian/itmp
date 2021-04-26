package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
*@author author
*@Description 测试案例执行轮次结果
*@Date 2020/9/2
*@return
**/
public class tblTestSetExecuteRoundCaseResult extends BaseEntity{
	//测试集案例表主键
	private Long testSetCaseId;
	//执行轮次
	private Integer excuteRound;
	private Long executeUserId;
	//执行结果 (数据字典：1=未执行；2=Pass；3=Failed；4=阻塞；5=跳过)
	private Integer  caseExecuteResult;
	public Long getTestSetCaseId() {
		return testSetCaseId;
	}
	public void setTestSetCaseId(Long testSetCaseId) {
		this.testSetCaseId = testSetCaseId;
	}
	public Integer getExcuteRound() {
		return excuteRound;
	}
	public void setExcuteRound(Integer excuteRound) {
		this.excuteRound = excuteRound;
	}
	public Integer getCaseExecuteResult() {
		return caseExecuteResult;
	}
	public void setCaseExecuteResult(Integer caseExecuteResult) {
		this.caseExecuteResult = caseExecuteResult;
	}
	public Long getExecuteUserId() {
		return executeUserId;
	}
	public void setExecuteUserId(Long executeUserId) {
		this.executeUserId = executeUserId;
	}
	
}
