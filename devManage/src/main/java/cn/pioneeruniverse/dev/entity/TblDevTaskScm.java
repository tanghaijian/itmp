package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("tbl_dev_task_scm")
public class TblDevTaskScm extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 开发任务表主键
     **/
    private Long devTaskId;//开发任务表主键

    /**
     * 开发任务表主键
     **/
    private Long devUserId;//开发任务表主键

    /**
     * 仓库类型（1=SVN；2=GIT）
     **/
    private Integer scmType; //仓库类型（1=SVN；2=GIT）

    /**
     * 源码提交版本号
     **/
    private String commitNumber;//源码提交版本号

    /**
     * 源码提交信息
     **/
    private String commitMassage;//源码提交信息

    /**
     * 提交次数
     **/
    @TableField(exist = false)
    private Long countScm;    //提交次数

    /**
     * 提交人数
     **/
    @TableField(exist = false)
    private Long countScmPeople;    //提交人数

    /**
     * 开发任务编码
     **/
    @TableField(exist = false)
    private String devTaskCode;//开发任务编码

    /**
     * 开发任务名称
     **/
    @TableField(exist = false)
    private String devTaskName; //开发任务名称

    /**
     * 开发任务描述
     **/
    @TableField(exist = false)
    private String devTaskOverview; //开发任务描述

    /**
     * 评审状态
     **/
    @TableField(exist = false)
    private Integer codeReviewStatus; //评审状态

    /**
     * 系统名称
     **/
    @TableField(exist = false)
    private String systemName; //系统名称

    /**
     * 开发人员名称
     **/
    @TableField(exist = false)
    private String devUserName; //开发人员名称

    /**
     * 代码评审人名称
     **/
    @TableField(exist = false)
    private String codeReviewUserNames;//代码评审人名称

    /**
     * 代码评审人id
     **/
    @TableField(exist = false)
    private String codeReviewUserIds;//代码评审人id

    /**
     * 工作任务用户ids
     **/
    @TableField(exist = false)
    private String devUserIds;// 工作任务用户ids

    public Long getDevTaskId() {
        return devTaskId;
    }

    public void setDevTaskId(Long devTaskId) {
        this.devTaskId = devTaskId;
    }

    public Long getDevUserId() {
        return devUserId;
    }

    public void setDevUserId(Long devUserId) {
        this.devUserId = devUserId;
    }

    public Integer getScmType() {
        return scmType;
    }

    public void setScmType(Integer scmType) {
        this.scmType = scmType;
    }

    public String getCommitNumber() {
        return commitNumber;
    }

    public void setCommitNumber(String commitNumber) {
        this.commitNumber = commitNumber;
    }

    public String getCommitMassage() {
        return commitMassage;
    }

    public void setCommitMassage(String commitMassage) {
        this.commitMassage = commitMassage;
    }

    public Long getCountScm() {
        return countScm;
    }

    public void setCountScm(Long countScm) {
        this.countScm = countScm;
    }

    public Long getCountScmPeople() {
        return countScmPeople;
    }

    public void setCountScmPeople(Long countScmPeople) {
        this.countScmPeople = countScmPeople;
    }

    public String getDevTaskCode() {
        return devTaskCode;
    }

    public void setDevTaskCode(String devTaskCode) {
        this.devTaskCode = devTaskCode;
    }

    public String getDevTaskName() {
        return devTaskName;
    }

    public void setDevTaskName(String devTaskName) {
        this.devTaskName = devTaskName;
    }

    public Integer getCodeReviewStatus() {
        return codeReviewStatus;
    }

    public void setCodeReviewStatus(Integer codeReviewStatus) {
        this.codeReviewStatus = codeReviewStatus;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getDevUserName() {
        return devUserName;
    }

    public void setDevUserName(String devUserName) {
        this.devUserName = devUserName;
    }

    public String getCodeReviewUserNames() {
        return codeReviewUserNames;
    }

    public void setCodeReviewUserNames(String codeReviewUserNames) {
        this.codeReviewUserNames = codeReviewUserNames;
    }

    public String getDevTaskOverview() {
        return devTaskOverview;
    }

    public void setDevTaskOverview(String devTaskOverview) {
        this.devTaskOverview = devTaskOverview;
    }

    public String getCodeReviewUserIds() {
        return codeReviewUserIds;
    }

    public void setCodeReviewUserIds(String codeReviewUserIds) {
        this.codeReviewUserIds = codeReviewUserIds;
    }

    public String getDevUserIds() {
        return devUserIds;
    }

    public void setDevUserIds(String devUserIds) {
        this.devUserIds = devUserIds;
    }

}