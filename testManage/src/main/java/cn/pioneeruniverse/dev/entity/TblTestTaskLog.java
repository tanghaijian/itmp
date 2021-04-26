package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@TableName("tbl_test_task_log")
public class TblTestTaskLog extends BaseEntity {


    private Long testTaskId;

    /**
     * 日志类型
     **/
    private String logType; // 日志类型

    /**
     * 用户id
     **/
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId; // 用户id

    /**
     * 姓名
     **/
    private String userName; // 姓名

    /**
     * 用户名
     **/
    private String userAccount;// 用户名

    /**
     * 日志类型
     **/
    private String logDetail;// 日志类型

    public Long getTestTaskId() {
        return testTaskId;
    }

    public void setTestTaskId(Long testTaskId) {
        this.testTaskId = testTaskId;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType == null ? null : logType.trim();
    }

    public String getLogDetail() {
        return logDetail;
    }

    public void setLogDetail(String logDetail) {
        this.logDetail = logDetail == null ? null : logDetail.trim();
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


}