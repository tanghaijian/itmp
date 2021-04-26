package cn.pioneeruniverse.dev.service.monthlyReport;

import javax.servlet.http.HttpServletRequest;

public interface TblReportMonthlyTaskTypeService {

    void updateOrInsertTaskType(String time, HttpServletRequest request);
}
