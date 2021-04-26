package cn.pioneeruniverse.dev.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 *
 * @ClassName: TblSystemAutomaticTestResult
 * @Description: 自动化测试结果实体类
 * @author author
 *
 */
@TableName("tbl_system_automatic_test_result")
public class TblSystemAutomaticTestResult extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long systemId;//系统id

	private Integer environmentType;//环境类型

	private Long systemModuleId;//模块id

	private String testScene;//测试场景

	private Long systemJenkinsJobRun;

	private String testRequestNumber;//测试号

	private Integer testResult;//测试结果


	private Integer testType;//测试类型

	private Integer successNumber;//成功数

	private Integer failedNumber;//失败数

	private String testResultDetailUrl;

	public Long getSystemId() {
		return systemId;
	}

	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}

	public Integer getEnvironmentType() {
		return environmentType;
	}

	public void setEnvironmentType(Integer environmentType) {
		this.environmentType = environmentType;
	}

	public Long getSystemModuleId() {
		return systemModuleId;
	}

	public void setSystemModuleId(Long systemModuleId) {
		this.systemModuleId = systemModuleId;
	}

	public String getTestScene() {
		return testScene;
	}

	public void setTestScene(String testScene) {
		this.testScene = testScene;
	}

	public Long getSystemJenkinsJobRun() {
		return systemJenkinsJobRun;
	}

	public void setSystemJenkinsJobRun(Long systemJenkinsJobRun) {
		this.systemJenkinsJobRun = systemJenkinsJobRun;
	}

	public String getTestRequestNumber() {
		return testRequestNumber;
	}

	public void setTestRequestNumber(String testRequestNumber) {
		this.testRequestNumber = testRequestNumber;
	}

	public Integer getTestResult() {
		return testResult;
	}

	public void setTestResult(Integer testResult) {
		this.testResult = testResult;
	}

	public Integer getSuccessNumber() {
		return successNumber;
	}

	public void setSuccessNumber(Integer successNumber) {
		this.successNumber = successNumber;
	}

	public Integer getFailedNumber() {
		return failedNumber;
	}

	public void setFailedNumber(Integer failedNumber) {
		this.failedNumber = failedNumber;
	}

	public String getTestResultDetailUrl() {
		return testResultDetailUrl;
	}

	public void setTestResultDetailUrl(String testResultDetailUrl) {
		this.testResultDetailUrl = testResultDetailUrl;
	}
	public Integer getTestType() {
		return testType;
	}

	public void setTestType(Integer testType) {
		this.testType = testType;
	}
}