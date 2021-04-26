package cn.pioneeruniverse.dev.service.monthlyReport;

import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlyUndetectedSummary;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface TblReportMonthlyUndetectedSummaryService {

    Map<String,Object> findAll(HttpServletRequest request);

    void updateUndetectedSummary(TblReportMonthlyUndetectedSummary tblReportMonthlyUndetectedSummary,HttpServletRequest request);

    void addUndetectedSummary(TblReportMonthlyUndetectedSummary tblReportMonthlyUndetectedSummary,HttpServletRequest request);

    void deleteUndetectedSummary(TblReportMonthlyUndetectedSummary tblReportMonthlyUndetectedSummary,HttpServletRequest request);

}
