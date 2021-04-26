package cn.pioneeruniverse.project.vo;

import java.io.Serializable;

public class TaskForceDataDicVO implements Serializable {
    private static final long serialVersionUID = -6496067455862633155L;

    /**
     * 开发任务状态名
     **/
    private String valueName;  

  /**
   * 开发任务状态编码
   **/
    private Integer valueCode;  

  /**
   * 总数
   **/
    private Integer count;  

    public TaskForceDataDicVO() {
    }

    public TaskForceDataDicVO(String valueName, Integer valueCode, Integer count) {
        this.valueName = valueName;
        this.valueCode = valueCode;
        this.count = count;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public Integer getValueCode() {
        return valueCode;
    }

    public void setValueCode(Integer valueCode) {
        this.valueCode = valueCode;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "TaskForceDdtaDicVO{" +
                "valueName='" + valueName + '\'' +
                ", valueCode='" + valueCode + '\'' +
                ", count=" + count +
                '}';
    }
}
