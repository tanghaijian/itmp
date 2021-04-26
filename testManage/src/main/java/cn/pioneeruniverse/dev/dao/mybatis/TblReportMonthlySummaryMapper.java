package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.pioneeruniverse.dev.entity.TblReportMonthlySummary;

public interface TblReportMonthlySummaryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TblReportMonthlySummary record);

    int insertSelective(TblReportMonthlySummary record);

    TblReportMonthlySummary selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblReportMonthlySummary record);

    int updateByPrimaryKey(TblReportMonthlySummary record);
    
    int batchInsert(@Param("list")List<TblReportMonthlySummary> list);
    
    List<Map<String, Object>> selectYearReport(@Param("year")Integer year,@Param("month")Integer month);
    
    int deleteByYearMonth(@Param("yearMonth")String yearMonth);
    
    int jugeHis(@Param("yearMonth")String yearMonth);
    
    List<TblReportMonthlySummary> selectReportByTime(@Param("yearMonth")String yearMonth) ;
}