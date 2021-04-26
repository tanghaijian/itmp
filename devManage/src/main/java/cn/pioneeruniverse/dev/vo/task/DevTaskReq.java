package cn.pioneeruniverse.dev.vo.task;

import cn.pioneeruniverse.common.bean.PageInfo;

/**
 * @author by qingcheng
 * @version 2020/8/17 9:08
 * @description
 */
public class DevTaskReq extends PageInfo {

    //======================================================
    // 属性
    private String systemId;
    private String sprintId;
    private String commissioningWindowId;
    private String systemVersionId;
    private String featureCode;
    private String executeUserIds;

    //======================================================
    // GETTER & SETTER
    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getSprintId() {
        return sprintId;
    }

    public void setSprintId(String sprintId) {
        this.sprintId = sprintId;
    }

    public String getCommissioningWindowId() {
        return commissioningWindowId;
    }

    public void setCommissioningWindowId(String commissioningWindowId) {
        this.commissioningWindowId = commissioningWindowId;
    }

    public String getSystemVersionId() {
        return systemVersionId;
    }

    public void setSystemVersionId(String systemVersionId) {
        this.systemVersionId = systemVersionId;
    }

    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }

    public String getExecuteUserIds() {
        return executeUserIds;
    }

    public void setExecuteUserIds(String executeUserIds) {
        this.executeUserIds = executeUserIds;
    }

}
