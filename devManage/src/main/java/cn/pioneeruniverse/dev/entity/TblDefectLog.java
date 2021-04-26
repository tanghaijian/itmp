package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.List;


/**
 *@author liushan
 *@Description 缺陷日志实体类
 *@Date 2020/8/6
 *@return
 **/
@TableName("tbl_defect_log")
public class TblDefectLog extends BaseEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long defectId; // 缺陷id

    private String logType; // 日志类型

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;// 用户id

    private String userName;// 用户名

    private String userAccount; // 用户编号

    private String logDetail; // 日志内容

    @TableField(exist = false)
    private List<TblDefectLogAttachement> logAttachementList;

    public Long getDefectId() {
        return defectId;
    }

    public void setDefectId(Long defectId) {
        this.defectId = defectId;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType == null ? null : logType.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount == null ? null : userAccount.trim();
    }

    public String getLogDetail() {
        return logDetail;
    }

    public void setLogDetail(String logDetail) {
        this.logDetail = logDetail == null ? null : logDetail.trim();
    }

    public List<TblDefectLogAttachement> getLogAttachementList() {
        return logAttachementList;
    }

    public void setLogAttachementList(List<TblDefectLogAttachement> logAttachementList) {
        this.logAttachementList = logAttachementList;
    }
}