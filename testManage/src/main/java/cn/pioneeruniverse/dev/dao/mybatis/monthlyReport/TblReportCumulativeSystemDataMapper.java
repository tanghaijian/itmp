package cn.pioneeruniverse.dev.dao.mybatis.monthlyReport;

import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportCumulativeSystemData;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblReportCumulativeSystemDataMapper extends BaseMapper<TblReportCumulativeSystemData> {


    List<TblReportCumulativeSystemData> findCumulativeSystemDataByYearMonth(@Param("date") String date);

    TblReportCumulativeSystemData findByYearMonthAndSystemId(@Param("month") String month,@Param("systemId") Long systemId,
                                                             @Param("systemType") Integer systemType);

    List<TblReportCumulativeSystemData> findByYearMonthAndSystemType(@Param("month") String month,  @Param("systemType") Integer systemType);

    int deleteByYearMonth(@Param("yearMonth") String yearMonth);

    int deleteByYearMonthAndSystemType(@Param("month") String month,@Param("systemId")Long systemId ,  @Param("systemType") Integer systemType);
}
