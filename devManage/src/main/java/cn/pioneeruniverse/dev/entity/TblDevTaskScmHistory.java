package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("tbl_dev_task_scm_history")
public class TblDevTaskScmHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 工作任务(工作任务表主键)
     **/
    private Long devTaskId; //工作任务(工作任务表主键)

    /**
     * 开发人员(人员表主键)
     **/
    private Long devUserId; //开发人员(人员表主键)

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

}