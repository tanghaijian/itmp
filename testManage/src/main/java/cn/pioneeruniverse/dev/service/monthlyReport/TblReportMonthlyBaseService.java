package cn.pioneeruniverse.dev.service.monthlyReport;

import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlyBase;

import javax.servlet.http.HttpServletRequest;

public interface TblReportMonthlyBaseService {

    //更新基本信息数据
    void updateMonthlyBase(TblReportMonthlyBase tblReportMonthlyBase, HttpServletRequest request);

    void updateMonthlyBaseAuditStatus(TblReportMonthlyBase tblReportMonthlyBase, HttpServletRequest request);
}
