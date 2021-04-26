package cn.pioneeruniverse.dev.service.monthlyReport;

import javax.servlet.http.HttpServletRequest;

public interface TblReportMonthlySystemTypeDataService {


    void updateOrInsertSystemTypeData(String time, HttpServletRequest request);
}
