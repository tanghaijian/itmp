package cn.pioneeruniverse.dev.service.monthlyReport;

import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlySystemData;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface TblReportMonthlySystemDataService {

    void updateMonthlySystemData(TblReportMonthlySystemData tblReportMonthlySystemData, HttpServletRequest request);

    Map<String,Object> findByMonthly(String yearMonth);

    void auditSystemData(TblReportMonthlySystemData tblReportMonthlySystemData,HttpServletRequest request);

}
