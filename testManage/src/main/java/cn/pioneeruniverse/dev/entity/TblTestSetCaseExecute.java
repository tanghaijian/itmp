package cn.pioneeruniverse.dev.entity;


import org.springframework.data.annotation.Transient;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
*@author liushan
*@Description 测试案例执行实体类
*@Date 2020/8/11
*@return
**/
public class TblTestSetCaseExecute extends BaseEntity{
	//测试集表主键
	private Long testSetId;
	//系统表主键
	private Long systemId;
	//执行轮次
	private Integer excuteRound;
	//案例名称
	private String caseName;
	//案例编号
	private String caseNumber;
	//前置条件
	private String casePrecondition;
	//案例类型（数据字典：1：正面、2：负面）
	private Integer caseType;
	//执行结果 (数据字典：1=未执行；2=Pass；3=Failed；4=阻塞；5=跳过)
	private Integer caseExecuteResult;
	private Long executeUserId;//执行人
	private String ResultName ;// 执行结果名称
	
	private String excuteRemark;// 执行备注
	
	@Transient
	private String executeUserName;
	@Transient
	private String executeResult;
	
	public String getExcuteRemark() {
		return excuteRemark;
	}
	public void setExcuteRemark(String excuteRemark) {
		this.excuteRemark = excuteRemark;
	}
	public Long getTestSetId() {
		return testSetId;
	}
	public void setTestSetId(Long testSetId) {
		this.testSetId = testSetId;
	}
	public Integer getExcuteRound() {
		return excuteRound;
	}
	public void setExcuteRound(Integer excuteRound) {
		this.excuteRound = excuteRound;
	}
	public String getCaseName() {
		return caseName;
	}
	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}
	public String getCaseNumber() {
		return caseNumber;
	}
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}
	public String getCasePrecondition() {
		return casePrecondition;
	}
	public void setCasePrecondition(String casePrecondition) {
		this.casePrecondition = casePrecondition;
	}
	public Integer getCaseType() {
		return caseType;
	}
	public void setCaseType(Integer caseType) {
		this.caseType = caseType;
	}
	public Integer getCaseExecuteResult() {
		return caseExecuteResult;
	}
	public void setCaseExecuteResult(Integer caseExecuteResult) {
		this.caseExecuteResult = caseExecuteResult;
	}
	public Long getSystemId() {
		return systemId;
	}
	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}
	public String getResultName() {
		return ResultName;
	}
	public void setResultName(String resultName) {
		ResultName = resultName;
	}
	public Long getExecuteUserId() {
		return executeUserId;
	}
	public void setExecuteUserId(Long executeUserId) {
		this.executeUserId = executeUserId;
	}


	private String expectResult;	//预期结果
	private String inputData;		//输入数据
	private String testPoint;		//测试项
	private String moduleName;		//模块
	private String businessType;	//业务类型
	public String getExpectResult() {
		return expectResult;
	}
	public void setExpectResult(String expectResult) {
		this.expectResult = expectResult;
	}

	public String getInputData() {
		return inputData;
	}
	public void setInputData(String inputData) {
		this.inputData = inputData;
	}

	public String getTestPoint() {
		return testPoint;
	}
	public void setTestPoint(String testPoint) {
		this.testPoint = testPoint;
	}

	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getExecuteUserName() {
		return executeUserName;
	}
	public void setExecuteUserName(String executeUserName) {
		this.executeUserName = executeUserName;
	}
	public String getExecuteResult() {
		return executeResult;
	}
	public void setExecuteResult(String executeResult) {
		this.executeResult = executeResult;
	}
	
}
