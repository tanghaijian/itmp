package cn.pioneeruniverse.dev.dao.mybatis.monthlyReport;

import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlyBase;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblReportMonthlyBaseMapper extends BaseMapper<TblReportMonthlyBase> {

    //根据时间查找
    List<TblReportMonthlyBase> findByYearMonth(@Param("date") String date);

    //更新
    void updateByYearMonth(TblReportMonthlyBase tblReportMonthlyBase);

    //根据时间查询历史记录
    Integer jugeHis(@Param("yearMonth")String yearMonth);

    int deleteByYearMonth(@Param("yearMonth")String yearMonth);

    int batchInsert(TblReportMonthlyBase tblReportMonthlyBase);

    List<TblReportMonthlyBase> findUndetectedNumberByYear(@Param("startYearMonth")String startYearMonth, @Param("endYearMonth") String endYearMonth);

    int updateMonthlyBase(TblReportMonthlyBase tblReportMonthlyBase);

    int updateMonthlyBaseAuditStatus(TblReportMonthlyBase tblReportMonthlyBase);

    List<TblReportMonthlyBase> findVersionsNumberByYear(@Param("startYearMonth")String startYearMonth, @Param("endYearMonth") String endYearMonth);

    List<TblReportMonthlyBase> findVersionsChangeRateByYear(@Param("startYearMonth")String startYearMonth, @Param("endYearMonth") String endYearMonth);

    //根据时间查出systemdata表总漏捡数并更新到base表中
    int updateUndefectedNumber(@Param("date") String date);

    int insertMonthBase(TblReportMonthlyBase tblReportMonthlyBase);
}




