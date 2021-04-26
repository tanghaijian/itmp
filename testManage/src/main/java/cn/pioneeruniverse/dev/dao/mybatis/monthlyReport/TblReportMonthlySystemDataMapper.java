package cn.pioneeruniverse.dev.dao.mybatis.monthlyReport;

import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlySystemData;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.apache.ibatis.annotations.Param;

import javax.annotation.security.PermitAll;
import java.util.List;
import java.util.Map;

public interface TblReportMonthlySystemDataMapper extends BaseMapper<TblReportMonthlySystemData> {

    //根据时间查找
    List<TblReportMonthlySystemData> findByYearMonth(@Param("date") String date);

    //根据系统id和时间更新信息
    void updateByYearMothAndSystemId(TblReportMonthlySystemData tblReportMonthlySystemData);

    //根据时间查询是否有历史记录
    int jugeHis(@Param("yearMonth")String yearMonth);

    int updateMonthlySystemDate(TblReportMonthlySystemData tblReportMonthlySystemData);

    List<TblReportMonthlySystemData> findByYearMonthAndUserId(@Param("date") String date, @Param("userId") Long userId);


    List<TblReportMonthlySystemData> findTaskTypeByDate(@Param("date") String date,@Param("taskType") Integer taskType);

    List<TblReportMonthlySystemData> findTotalDataByDate(@Param("systemType") Integer systemType, @Param("startYearMonth")String startYearMonth, @Param("endYearMonth") String endYearMonth);

    List<TblReportMonthlySystemData>  findAvgRepaitRound(@Param("startYearMonth")String startYearMonth, @Param("endYearMonth") String endYearMonth);

    List<TblReportMonthlySystemData> findAvgRepaitRoundByTime(@Param("date") String date);

    //根据年月和项目类型查找缺陷
    List<TblReportMonthlySystemData> findDefectPercentByDateAndType(@Param("date") String date,
                                                                    @Param("systemType") Integer systemType);

    List<TblReportMonthlySystemData> findDevDefectPercentByDate(@Param("date") String date);


    List<TblReportMonthlySystemData> findDefectRateSortByTimeAndTypeASC(@Param("date") String date,
                                                                            @Param("type") Integer type,
                                                                            @Param("number") Integer number);

    List<TblReportMonthlySystemData> findDefectRateSortByTimeAndTypeDESC(@Param("date") String date,
                                                                     @Param("type") Integer type,
                                                                     @Param("number") Integer number);


    List<TblReportMonthlySystemData> getSystemDataUserIdAndSystemName(@Param("date") String date);


    List<TblReportMonthlySystemData> findByYearMonthlyAndSystemType(@Param("date") String date,@Param("systemType") String systemType);



    TblReportMonthlySystemData findByYearMonthAndSystemId(@Param("yearMonth") String yearMonth,@Param("systemId") Long systemId);

    TblReportMonthlySystemData findByYearMonthAndTypeAndSystemId(@Param("yearMonth") String yearMonth,@Param("systemId") Long systemId,
                                                                 @Param("type") Integer type);
}
