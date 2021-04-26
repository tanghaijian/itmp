package cn.pioneeruniverse.dev.entity.monthlyReport;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * 配置表
 * Created by aviyy on 2020/10/21.
 */
public class TblReportMonthlyConfig  extends BaseEntity {


    private Long userId;//人员ID

    private String userName;//人员姓名

    private int userType;//类型：1.月报负责人；2.系统审核人

    private Long systemId;//系统Id

    private String systemName; //系统名称

    private String remark;//备注

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
        this.userName = userName;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
