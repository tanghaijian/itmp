package cn.pioneeruniverse.dev.dao.mybatis.monthlyReport;

import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlyTaskType;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

public interface TblReportMonthlyTaskTypeMapper extends BaseMapper<TblReportMonthlyTaskType> {

    List<TblReportMonthlyTaskType> findTaskType(@Param("startDate") String startDate , @Param("endDate") String endDate);


    TblReportMonthlyTaskType findTaskTypeByYearMonthAndSystemTypeAndType(@Param("date") String date,@Param("systemType") Integer systemType,
                                                                         @Param("type") Integer type);

    void  updateByYearMonthAndSystemTypeAndLevel(TblReportMonthlyTaskType tblReportMonthlyTaskType);

    List<TblReportMonthlyTaskType> findAgileTaskRateByDate(@Param("date") String date ,@Param("type") Integer type) ;

    List<TblReportMonthlyTaskType> findDevTaskRateByDate(@Param("date") String date);

}
