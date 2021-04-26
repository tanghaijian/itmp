package cn.pioneeruniverse.dev.service.monthlyReport;

import javax.servlet.http.HttpServletRequest;

public interface TblReportMonthlyDefectLevelService {

    void updateOrInsertDefectLevel(String time, HttpServletRequest request);
}
