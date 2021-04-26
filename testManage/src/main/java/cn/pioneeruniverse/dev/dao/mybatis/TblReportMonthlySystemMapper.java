package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.pioneeruniverse.dev.entity.TblReportMonthlySystem;

public interface TblReportMonthlySystemMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TblReportMonthlySystem record);

    int insertSelective(TblReportMonthlySystem record);

    TblReportMonthlySystem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblReportMonthlySystem record);

    int updateByPrimaryKey(TblReportMonthlySystem record);
    
    int batchInsert(@Param("list") List<TblReportMonthlySystem> list);
    
    int deleteByYearMonth(@Param("yearMonth")String yearMonth);
    
    int jugeHis(@Param("yearMonth")String yearMonth);
    
    List<Map<String, Object>> selectDefectPro(@Param("year")Integer year,@Param("month")Integer month,@Param("systemCode")String systemCode);
    
    List<Map<String, Object>> selectReportBySystem(@Param("year")Integer year,@Param("month")Integer month);
    
    List<Map<String, Object>> selectReportBySystemYear(@Param("year")Integer year,@Param("month")Integer month);
    
    List<Map<String, Object>> selectWorseSystem(@Param("year")Integer year,@Param("systemCode")String systemCode);
    
    List<TblReportMonthlySystem> selectReportSystem(@Param("yearMonth")String yearMonth);
}