package cn.pioneeruniverse.dev.service.monthlyReport;

import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlyBase;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface MonthlyReportService {

    //月报数据查询
    Map<String, Object> queryReport(String time,HttpServletRequest request );

    //汇总月报
    Map<String , Object> summaryMonthlyReport(String time,HttpServletRequest request);

    //查询是否有历史记录
    Map<String, Object> queryHis(String time,HttpServletRequest request);

    //插入历史记录
    void addHis(String allDevVersionTableData, String allSystemTableData, HttpServletRequest request);

    //查询历史记录
    Map<String, Object> queryHisByTime(String time,HttpServletRequest request);

    //测试接口
    void testReport(String time,HttpServletRequest request);

    //获取月报所有数据
    Map<String,Object> getAllMonthReportData(String time, HttpServletRequest request);

    //获取缺陷遗留清单

    Map<String,Object> getRemainDefect(String time,HttpServletRequest request);


}

