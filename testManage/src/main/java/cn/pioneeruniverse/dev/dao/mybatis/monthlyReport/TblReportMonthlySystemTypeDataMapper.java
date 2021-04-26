package cn.pioneeruniverse.dev.dao.mybatis.monthlyReport;

import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlySystemTypeData;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblReportMonthlySystemTypeDataMapper extends BaseMapper<TblReportMonthlySystemTypeData> {

    List<TblReportMonthlySystemTypeData> findSystemTypeData(@Param("startTime") String startTime,
                                                            @Param("endTime") String endTime);

    TblReportMonthlySystemTypeData findTaskTypeByYearMonthAndSystemType(@Param("date") String date,@Param("systemType") Integer systemType);

    void updateByYearMonthAndSystemType(TblReportMonthlySystemTypeData tblReportMonthlySystemTypeData);

    List<TblReportMonthlySystemTypeData> findByYearTime(@Param("date") String date);

    List<TblReportMonthlySystemTypeData> findByYearAndSystemType(@Param("startTime") String startTime,@Param("endTime") String endTime,
                                                                 @Param("type") int type);
}
