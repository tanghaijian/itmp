package cn.pioneeruniverse.dev.entity;

import com.baomidou.mybatisplus.annotations.TableField;

import cn.pioneeruniverse.common.entity.BaseEntity;

import java.sql.Timestamp;
import java.util.List;

public class TblTestSetCase extends BaseEntity{

	private static final long serialVersionUID = 3960801397790687762L;

	private Long testSetId;//测试集
	private Long systemId;// 系统
	//案例名称	
    private String caseName;
    //案例编号
    private String caseNumber;
    //前置条件
    private String casePrecondition;
    private String caseDescription;
    //案例类型（数据字典：1：正面、2：负面）
    private Integer caseType;
    //执行结果
    private Integer caseExecuteResult;
    // 实际结果
    private String caseActualResult;
    //执行结果
    @TableField(exist = false)
    private String executeResult;

	private String expectResult;	//预期结果
	private String inputData;		//输入数据
	private String testPoint;		//测试项
	private String moduleName;		//模块
	private String businessType;	//业务类型
	
	@TableField(exist = false)
	private List<TblTestSetCaseStep> testSetCaseStep;
	
	private Integer orderCase;
	@TableField(exist = false)
	private String userName;//创建人
	@TableField(exist = false)
	private String systemName;
	@TableField(exist = false)
	private Long setCaseId;

	// 执行轮次
	private Integer excuteRound;

	public TblTestSetCase(){}

	public TblTestSetCase (Long id,Long currentUserId){
		this.setId(id);
		this.setCreateBy(currentUserId);
		this.setLastUpdateBy(currentUserId);
		this.setCreateDate(new Timestamp(System.currentTimeMillis()));
		this.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
	}

	public TblTestSetCase (Long id,Long currentUserId,String caseNumber,Integer orderCase){
		this.setId(id);
		this.setCaseNumber(caseNumber);
		this.setOrderCase(orderCase);
		this.setCreateBy(currentUserId);
		this.setLastUpdateBy(currentUserId);
		this.setCreateDate(new Timestamp(System.currentTimeMillis()));
		this.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
	}
	
	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<TblTestSetCaseStep> getTestSetCaseStep() {
		return testSetCaseStep;
	}

	public void setTestSetCaseStep(List<TblTestSetCaseStep> testSetCaseStep) {
		this.testSetCaseStep = testSetCaseStep;
	}
    
	public String getExecuteResult() {
		return executeResult;
	}

	public void setExecuteResult(String executeResult) {
		this.executeResult = executeResult;
	}

	public Long getTestSetId() {
		return testSetId;
	}
	
	public void setTestSetId(Long testSetId) {
		this.testSetId = testSetId;
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

	public Integer getCaseType() {
		return caseType;
	}

	public void setCaseType(Integer caseType) {
		this.caseType = caseType;
	}
	public String getCasePrecondition() {
		return casePrecondition;
	}

	public void setCasePrecondition(String casePrecondition) {
		this.casePrecondition = casePrecondition;
	}

	public String getCaseDescription() {
		return caseDescription;
	}

	public void setCaseDescription(String caseDescription) {
		this.caseDescription = caseDescription;
	}

	public Integer getCaseExecuteResult() {
		return caseExecuteResult;
	}

	public void setCaseExecuteResult(Integer caseExecuteResult) {
		this.caseExecuteResult = caseExecuteResult;
	}

	public String getCaseActualResult() {
		return caseActualResult;
	}

	public void setCaseActualResult(String caseActualResult) {
		this.caseActualResult = caseActualResult;
	}

	public Long getSystemId() {
		return systemId;
	}

	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}

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

	public Integer getOrderCase() {
		return orderCase;
	}

	public void setOrderCase(Integer orderCase) {
		this.orderCase = orderCase;
	}

	public Integer getExcuteRound() {
		return excuteRound;
	}

	public void setExcuteRound(Integer excuteRound) {
		this.excuteRound = excuteRound;
	}

	public Long getSetCaseId() {
		return setCaseId;
	}

	public void setSetCaseId(Long setCaseId) {
		this.setCaseId = setCaseId;
	}
}