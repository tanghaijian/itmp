package cn.pioneeruniverse.dev.entity;

import java.util.List;

public class TestFirstResultInfo {

    /**
     * 系统编码
     **/
    private String systemCode; //系统编码

    /**
     * 测试请求申请单号
     **/
    private String testRequestNumber; //测试请求申请单号

    /**
     * 成功数
     **/
    private String successNumber; //成功数

    /**
     * 失败数
     **/
    private String failedNumber;//失败数

    /**
     * 测试结果
     **/
    private String testResult; //测试结果

    /**
     * 测试详情url
     **/
    private String testResultDetailUrl; //测试详情url

    /**
     * 测试类型给
     **/
    private String testType; //测试类型给

    /**
     * 子系统测试结果
     **/
    private List<TestFirstResultInfo> subSystemInfo; //子系统测试结果

    /**
     * 子系统编码
     **/
    private String subSystemCode; //子系统编码

    /**
     * 子系统测试请求单号
     **/
    private String subTestRequestNumber; //子系统测试请求单号

    /**
     * 子系统测试成功数
     **/
    private String subSuccessNumber; //子系统测试成功数

    /**
     * 子系统失败数
     **/
    private String subFailedNumber; //子系统失败数

    /**
     * 子系统测试结果
     **/
    private String subTestResult; //子系统测试结果

    /**
     * 子系统测试详情Url
     **/
    private String subTestResultDetailUrl; //子系统测试详情Url

    public String getSystemCode() {
        return systemCode;
    }
    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getTestRequestNumber() {
        return testRequestNumber;
    }
    public void setTestRequestNumber(String testRequestNumber) {
        this.testRequestNumber = testRequestNumber;
    }

    public String getSuccessNumber() {
        return successNumber;
    }
    public void setSuccessNumber(String successNumber) {
        this.successNumber = successNumber;
    }

    public String getFailedNumber() {
        return failedNumber;
    }
    public void setFailedNumber(String failedNumber) {
        this.failedNumber = failedNumber;
    }

    public String getTestResult() {
        return testResult;
    }
    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public String getTestResultDetailUrl() {
        return testResultDetailUrl;
    }
    public void setTestResultDetailUrl(String testResultDetailUrl) {
        this.testResultDetailUrl = testResultDetailUrl;
    }

    public List<TestFirstResultInfo> getSubSystemInfo() {
        return subSystemInfo;
    }
    public void setSubSystemInfo(List<TestFirstResultInfo> subSystemInfo) {
        this.subSystemInfo = subSystemInfo;
    }

    public String getSubSystemCode() {
        return subSystemCode;
    }
    public void setSubSystemCode(String subSystemCode) {
        this.subSystemCode = subSystemCode;
    }

    public String getSubTestRequestNumber() {
        return subTestRequestNumber;
    }
    public void setSubTestRequestNumber(String subTestRequestNumber) {
        this.subTestRequestNumber = subTestRequestNumber;
    }

    public String getSubSuccessNumber() {
        return subSuccessNumber;
    }
    public void setSubSuccessNumber(String subSuccessNumber) {
        this.subSuccessNumber = subSuccessNumber;
    }

    public String getSubFailedNumber() {
        return subFailedNumber;
    }
    public void setSubFailedNumber(String subFailedNumber) {
        this.subFailedNumber = subFailedNumber;
    }

    public String getSubTestResult() {
        return subTestResult;
    }
    public void setSubTestResult(String subTestResult) {
        this.subTestResult = subTestResult;
    }

    public String getSubTestResultDetailUrl() {
        return subTestResultDetailUrl;
    }

    public String getTestType() {
        return testType;
    }
    public void setTestType(String testType) {
        this.testType = testType;
    }

    public void setSubTestResultDetailUrl(String subTestResultDetailUrl) {
        this.subTestResultDetailUrl = subTestResultDetailUrl;
    }
}
