package cn.pioneeruniverse.dev.vo;

import cn.pioneeruniverse.common.entity.BaseEntity;
import cn.pioneeruniverse.dev.common.ExcelField;
import cn.pioneeruniverse.dev.entity.TblArchivedCaseStep;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Transient;

import java.sql.Timestamp;
import java.util.List;

/**
* @author author
* @Description 归档案例
* @Date 2020/9/3
* @return
**/
@TableName("tbl_archived_case")
public class ArchivedCaseVO extends BaseEntity {

	private static final long serialVersionUID = 1L;
	//系统ID
	private Integer systemId;
	//案例类别ID
	private Integer caseCatalogId;

	/**
	 * 案例名称
	 **/
	private String caseName;

	/**
	 * 案例编号
	 **/
	private String caseNumber;

	/**
	 * 前置条件
	 **/
	private String casePrecondition;

	/**
	 * 案例类型（1.正面，2.负面）
	 **/
	private Integer caseType;

	@Transient
	private String userName;

	/**
	 * 系统名称
	 **/
	@Transient
	private String systemName;

	/**
	 * 创建时间
	 **/
	@Transient
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
	private Timestamp createTime;

	/**
	 * 最后修改者
	 **/
	@Transient
	private String lastUpdateUser;

	/**
	 * 最后修改时间
	 **/
	@Transient
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
	private Timestamp lastUpdateTime;

	/**
	 * 类型
	 **/
	private String type;

	@Transient
	private List<TblArchivedCaseStep> caseSteps;
	
	@Transient
	private Long[] uIds;
	
	@Transient
	private Long[] systemIds;

	public Integer getSystemId() {
		return systemId;
	}

	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
	}

	@ExcelField(title="案例描述", type=1, align=1, sort=4)
	public String getCaseName() {
		return caseName;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}

	@ExcelField(title="案例序号", type=1, align=1, sort=0)
	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	@ExcelField(title="前置条件", type=1, align=1, sort=5)
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@ExcelField(title="步骤", type=1, align=1, sort=7,isList = true)
	public List<TblArchivedCaseStep> getCaseSteps() {
		return caseSteps;
	}

	public void setCaseSteps(List<TblArchivedCaseStep> caseSteps) {
		this.caseSteps = caseSteps;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long[] getuIds() {
		return uIds;
	}

	public void setuIds(Long[] uIds) {
		this.uIds = uIds;
	}

	public Long[] getSystemIds() {
		return systemIds;
	}

	public void setSystemIds(Long[] systemIds) {
		this.systemIds = systemIds;
	}

	private String expectResult;	//预期结果
	private String inputData;		//输入数据
	private String testPoint;		//测试项
	private String moduleName;		//模块
	private String businessType;	//业务类型
	private String caseDescription; //案例描述

	@ExcelField(title="预期结果", type=1, align=1, sort=10)
	public String getExpectResult() {
		return expectResult;
	}
	public void setExpectResult(String expectResult) {
		this.expectResult = expectResult;
	}

	@ExcelField(title="输入数据", type=1, align=1, sort=6)
	public String getInputData() {
		return inputData;
	}

	public void setInputData(String inputData) {
		this.inputData = inputData;
	}

	@ExcelField(title="测试项", type=1, align=1, sort=3)
	public String getTestPoint() {
		return testPoint;
	}
	public void setTestPoint(String testPoint) {
		this.testPoint = testPoint;
	}

	@ExcelField(title="模块", type=1, align=1, sort=2)
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	@ExcelField(title="业务类型", type=1, align=1, sort=1)
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getCaseDescription() {
		return caseDescription;
	}

	public void setCaseDescription(String caseDescription) {
		this.caseDescription = caseDescription;
	}

	public Integer getCaseCatalogId() {
		return caseCatalogId;
	}

	public void setCaseCatalogId(Integer caseCatalogId) {
		this.caseCatalogId = caseCatalogId;
	}
	
	
}
