package cn.pioneeruniverse.dev.service.monthlyReport;

import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlyModule;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface TblReportMonthlyModuleService {

    void updateModule(TblReportMonthlyModule tblReportMonthlyModule);

    Map<String,Object> addModule(String date, String module, int num, String content, HttpServletRequest request);

    void deleteModule(Long id,String date,String module,int num,HttpServletRequest request);

    void updateOrInsertModule(String time, HttpServletRequest request);
}
