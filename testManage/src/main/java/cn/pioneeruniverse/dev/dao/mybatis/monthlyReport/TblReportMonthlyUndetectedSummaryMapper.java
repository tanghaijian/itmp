package cn.pioneeruniverse.dev.dao.mybatis.monthlyReport;

import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlyUndetectedSummary;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

public interface TblReportMonthlyUndetectedSummaryMapper extends BaseMapper<TblReportMonthlyUndetectedSummary> {

    List<TblReportMonthlyUndetectedSummary> findAll();
}
