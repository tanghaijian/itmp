package cn.pioneeruniverse.dev.entity.monthlyReport;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;

/**
 * 审核表
 * Created by aviyy on 2020/10/21.
 */
public class TblReportMonthlyAudit extends BaseEntity {

    private String yearMonth; //时间

    private Long userId;//人员ID

    private String userName;//人员姓名

    private Long systemId;//系统Id

    private String systemName; //系统名称

    private Long data1Id;//页面数据 TblReportMonthlySystemData 表 ID

    private Long data2Id;//系统统计业务数据 TblReportMonthlySystemData 表 ID

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
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
        this.userName = userName;
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

    public Long getData1Id() {
        return data1Id;
    }

    public void setData1Id(Long data1Id) {
        this.data1Id = data1Id;
    }

    public Long getData2Id() {
        return data2Id;
    }

    public void setData2Id(Long data2Id) {
        this.data2Id = data2Id;
    }
}
