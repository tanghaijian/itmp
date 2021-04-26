package cn.pioneeruniverse.dev.service.monthlyReport;

import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlyConfig;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface TblReportMonthlyConfigService {

    void updateMonthlyConfig(TblReportMonthlyConfig tblReportMonthlyConfig, HttpServletRequest request);

    String getCurrentSystemDataChecker(Long systemId);

    Map<String,String>  getCurrentUserRole(HttpServletRequest  request);

    TblReportMonthlyConfig getReportMonthlyManager();

}
