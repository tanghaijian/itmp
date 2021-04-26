package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 14:47 2019/6/12
 * @Modified By:
 */
@TableName("tbl_system_scm_submit")
public class TblSystemScmSubmit extends BaseEntity {

    private Long systemId;//系统ID

    private String groupFlag;//分组标签

    private Long systemScmRepositoryId; //TblSystemScmRepository的ID

    private String scmRepositoryName;//仓库名称

    private String scmUrl;//源码管理地址

    private String scmBranch;//源码管理分支

    private Integer submitStatus;//代码提交开关（1:开，2:关）

    private String submitSuperUserNames;//代码提交管理(备注：此用户不受任何代码提交的约束)，用户ID，以,隔开

    private String submitUserNames;//代码提交用户清单(备注：若有设置，则按照此清单过滤；若没有设置，则所有人可提交)，用户ID，以,隔开

    private Long systemVersionId;//系统版本ID

    private Long commissioningWindowId;//投产窗口ID

    @TableField(exist = false)
    private String submitSuperUserRealNames;//submitSuperUserNames对应的姓名，以,隔开

    @TableField(exist = false)
    private String submitUserRealNames;//submitUserNames对应的姓名，以,隔开

    @TableField(exist = false)
    private Set<String> submitSuperUserNamesCollection;//submitSuperUserNames对应的姓名，以set返回

    @TableField(exist = false)
    private Set<String> submitUserNamesCollection; //submitUserNames对应的姓名，以set返回

    @TableField(exist = false)
    private String systemVersionName;//系统版本号名称

    @TableField(exist = false)
    private String commissioningWindowName;//投产窗口名称

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Long getSystemScmRepositoryId() {
        return systemScmRepositoryId;
    }

    public void setSystemScmRepositoryId(Long systemScmRepositoryId) {
        this.systemScmRepositoryId = systemScmRepositoryId;
    }

    public String getScmRepositoryName() {
        return scmRepositoryName;
    }

    public void setScmRepositoryName(String scmRepositoryName) {
        this.scmRepositoryName = scmRepositoryName;
    }

    public String getScmUrl() {
        return scmUrl;
    }

    public void setScmUrl(String scmUrl) {
        this.scmUrl = scmUrl;
    }

    public String getScmBranch() {
        return scmBranch;
    }

    public void setScmBranch(String scmBranch) {
        this.scmBranch = scmBranch;
    }

    public Integer getSubmitStatus() {
        return submitStatus;
    }

    public void setSubmitStatus(Integer submitStatus) {
        this.submitStatus = submitStatus;
    }

    public String getSubmitSuperUserNames() {
        return submitSuperUserNames;
    }

    public void setSubmitSuperUserNames(String submitSuperUserNames) {
        this.submitSuperUserNames = submitSuperUserNames;
    }

    public String getSubmitUserNames() {
        return submitUserNames;
    }

    public void setSubmitUserNames(String submitUserNames) {
        this.submitUserNames = submitUserNames;
    }

    public Long getSystemVersionId() {
        return systemVersionId;
    }

    public void setSystemVersionId(Long systemVersionId) {
        this.systemVersionId = systemVersionId;
    }

    public Long getCommissioningWindowId() {
        return commissioningWindowId;
    }

    public void setCommissioningWindowId(Long commissioningWindowId) {
        this.commissioningWindowId = commissioningWindowId;
    }

    public String getSubmitSuperUserRealNames() {
        return submitSuperUserRealNames;
    }

    public void setSubmitSuperUserRealNames(String submitSuperUserRealNames) {
        this.submitSuperUserRealNames = submitSuperUserRealNames;
    }

    public String getSubmitUserRealNames() {
        return submitUserRealNames;
    }

    public void setSubmitUserRealNames(String submitUserRealNames) {
        this.submitUserRealNames = submitUserRealNames;
    }

    public Set<String> getSubmitSuperUserNamesCollection() {
        return submitSuperUserNamesCollection;
    }

    public void setSubmitSuperUserNamesCollection(String submitSuperUserNames) {
        Set<String> submitSuperUserNamesCollection = new HashSet<>();
        submitSuperUserNamesCollection.addAll(Arrays.asList(submitSuperUserNames.split(",|，")));
        this.submitSuperUserNamesCollection = submitSuperUserNamesCollection;
    }

    public Set<String> getSubmitUserNamesCollection() {
        return submitUserNamesCollection;
    }

    public void setSubmitUserNamesCollection(String submitUserNames) {
        Set<String> submitUserNamesCollection = new HashSet<>();
        submitUserNamesCollection.addAll(Arrays.asList(submitUserNames.split(",|，")));
        this.submitUserNamesCollection = submitUserNamesCollection;
    }

    public String getSystemVersionName() {
        return systemVersionName;
    }

    public void setSystemVersionName(String systemVersionName) {
        this.systemVersionName = systemVersionName;
    }

    public String getCommissioningWindowName() {
        return commissioningWindowName;
    }

    public void setCommissioningWindowName(String commissioningWindowName) {
        this.commissioningWindowName = commissioningWindowName;
    }
    public String getGroupFlag() {
        return groupFlag;
    }

    public void setGroupFlag(String groupFlag) {
        this.groupFlag = groupFlag;
    }
}
