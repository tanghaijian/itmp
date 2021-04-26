package cn.pioneeruniverse.dev.vo;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;

public class RequirementVo extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long parentId;    //父ID
    private String parentIds;    //所有父ID(如：,1,2,3,)
    /*基本信息*/
    @TableField(exist = false)
    private String reqStatusName;    //需求状态名称(数据字典)
    @TableField(exist = false)
    private String reqSourceName;    //需求来源名称(数据字典)
    @TableField(exist = false)
    private String reqTypeName;        //需求类型名称(数据字典)
    @TableField(exist = false)
    private String userName;    //需求提出人名称
    @TableField(exist = false)
    private String deptName;    //归属部门名称

    private String reqPlanName;    //需求计划名称(数据字典)
    private String reqPriorityName;//需求优先级名称(数据字典)
    private String devDeptName;    //开发部门名称

    /*重点需求相关*/
    private String impReqStatusName;//是否重点需求(1=是，2=否)(数据字典)
    private String impReqDelayStatusName;//重点需求是否延误(1=是，2=否)(数据字典)

    /*其他信息*/
    private String hangupStatusName;    //需求是否挂起(1=是；2=否)(数据字典)
    private String devManageUserName;    //开发管理人员(用户表主键)
    private String reqManageUserName;    //需求管理人员(用户表主键)
    private String reqAcceptanceUserName;    //需求验收人员(用户表主键)
    private String dataMigrationStatusName;//是否数据迁移(状态 1=是；2=否)(数据字典)

    private String systemId;//查询条件(涉及系统id)
    private String systemName;//查询条件(涉及系统名称)
    private String featureName;//查询条件(开发任务)

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getReqStatusName() {
        return reqStatusName;
    }

    public void setReqStatusName(String reqStatusName) {
        this.reqStatusName = reqStatusName;
    }

    public String getReqSourceName() {
        return reqSourceName;
    }

    public void setReqSourceName(String reqSourceName) {
        this.reqSourceName = reqSourceName;
    }

    public String getReqTypeName() {
        return reqTypeName;
    }

    public void setReqTypeName(String reqTypeName) {
        this.reqTypeName = reqTypeName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getReqPlanName() {
        return reqPlanName;
    }

    public void setReqPlanName(String reqPlanName) {
        this.reqPlanName = reqPlanName;
    }

    public String getReqPriorityName() {
        return reqPriorityName;
    }

    public void setReqPriorityName(String reqPriorityName) {
        this.reqPriorityName = reqPriorityName;
    }

    public String getDevDeptName() {
        return devDeptName;
    }

    public void setDevDeptName(String devDeptName) {
        this.devDeptName = devDeptName;
    }

    public String getImpReqStatusName() {
        return impReqStatusName;
    }

    public void setImpReqStatusName(String impReqStatusName) {
        this.impReqStatusName = impReqStatusName;
    }

    public String getImpReqDelayStatusName() {
        return impReqDelayStatusName;
    }

    public void setImpReqDelayStatusName(String impReqDelayStatusName) {
        this.impReqDelayStatusName = impReqDelayStatusName;
    }

    public String getHangupStatusName() {
        return hangupStatusName;
    }

    public void setHangupStatusName(String hangupStatusName) {
        this.hangupStatusName = hangupStatusName;
    }

    public String getDevManageUserName() {
        return devManageUserName;
    }

    public void setDevManageUserName(String devManageUserName) {
        this.devManageUserName = devManageUserName;
    }

    public String getReqManageUserName() {
        return reqManageUserName;
    }

    public void setReqManageUserName(String reqManageUserName) {
        this.reqManageUserName = reqManageUserName;
    }

    public String getReqAcceptanceUserName() {
        return reqAcceptanceUserName;
    }

    public void setReqAcceptanceUserName(String reqAcceptanceUserName) {
        this.reqAcceptanceUserName = reqAcceptanceUserName;
    }

    public String getDataMigrationStatusName() {
        return dataMigrationStatusName;
    }

    public void setDataMigrationStatusName(String dataMigrationStatusName) {
        this.dataMigrationStatusName = dataMigrationStatusName;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

}