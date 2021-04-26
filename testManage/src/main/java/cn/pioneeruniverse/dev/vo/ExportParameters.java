package cn.pioneeruniverse.dev.vo;

import java.io.Serializable;

/**
 * @author wucheng
 * @date 2020/7/2
 */
public class ExportParameters  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 测试工作任务编号
     */
    private String testTaskCode;

    /**
     * 测试工作任务名称
     */
    private String testTaskName;

    /**
     * 阶段
     */
    private Integer testStage;

    /**
     *系统名称
     */
    private String systemName;

    /**
     * 需求编号
     */
    private String requirementCode;


    public ExportParameters() {
    }

    public String getTestTaskCode() {
        return testTaskCode;
    }

    public void setTestTaskCode(String testTaskCode) {
        this.testTaskCode = testTaskCode;
    }

    public String getTestTaskName() {
        return testTaskName;
    }

    public void setTestTaskName(String testTaskName) {
        this.testTaskName = testTaskName;
    }

    public Integer getTestStage() {
        return testStage;
    }

    public void setTestStage(Integer testStage) {
        this.testStage = testStage;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getRequirementCode() {
        return requirementCode;
    }

    public void setRequirementCode(String requirementCode) {
        this.requirementCode = requirementCode;
    }


    @Override
    public String toString() {
        return "ExportParameters{" +
                "testTaskCode='" + testTaskCode + '\'' +
                ", testTaskName='" + testTaskName + '\'' +
                ", testStage=" + testStage +
                ", systemName='" + systemName + '\'' +
                ", requirementCode='" + requirementCode + '\'' +
                '}';
    }
}
