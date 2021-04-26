package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 *@author liushan
 *@Description 缺陷备注实体类
 *@Date 2020/8/7
 *@return
 **/
@TableName("tbl_defect_remark")
public class TblDefectRemark extends BaseEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long defectId;// 缺陷id

    private String defectRemark;// 备注

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId; // 用户id

    private String userName; // 姓名

    private String userAccount;// 用户名

    public Long getDefectId() {
        return defectId;
    }

    public void setDefectId(Long defectId) {
        this.defectId = defectId;
    }

    public String getDefectRemark() {
        return defectRemark;
    }

    public void setDefectRemark(String defectRemark) {
        this.defectRemark = defectRemark == null ? null : defectRemark.trim();
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