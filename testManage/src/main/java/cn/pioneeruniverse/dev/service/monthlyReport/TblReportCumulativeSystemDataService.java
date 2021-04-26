package cn.pioneeruniverse.dev.service.monthlyReport;

import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportCumulativeSystemData;
import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlySystemData;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface TblReportCumulativeSystemDataService {

    void updateOrInsertCumulativeSystemTypeData(String time, HttpServletRequest request);

    List<TblReportCumulativeSystemData> findByYearMonthAndSystemType(String yearMonth,Integer systemType);

    Map<String,Object>  findByYearMonth(String yearMonth);

    }
