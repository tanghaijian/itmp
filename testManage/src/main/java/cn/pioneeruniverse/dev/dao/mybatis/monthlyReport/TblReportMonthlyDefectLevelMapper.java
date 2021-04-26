package cn.pioneeruniverse.dev.dao.mybatis.monthlyReport;

import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlyDefectLevel;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblReportMonthlyDefectLevelMapper extends BaseMapper<TblReportMonthlyDefectLevel> {


    List<TblReportMonthlyDefectLevel> findDefectLevel(@Param("startDate") String startDate , @Param("endDate") String endDate);

    void insertDefectLeve(TblReportMonthlyDefectLevel tblReportMonthlyDefectLevel);

    TblReportMonthlyDefectLevel findDefectLevelByYearMonth(@Param("date") String date,@Param("systemType") Integer systemType,
                                                           @Param("level") Integer level);

    List<TblReportMonthlyDefectLevel> findDefectLevelByLevel(@Param("date") String date,@Param("level") Integer level);

    void updateByYearMonthAndSystemTypeAndLevel(TblReportMonthlyDefectLevel tblReportMonthlyDefectLevel);

    List<TblReportMonthlyDefectLevel> findDefectLevelByYearMonthAndSystemType(@Param("date") String date,@Param("systemType") Integer systemType);

    List<TblReportMonthlyDefectLevel>  findCountByByYearMonthAndSystemType(@Param("date") String date);

    List<TblReportMonthlyDefectLevel>  findCountByByYearMonthAndLevel(@Param("date") String date);
}
