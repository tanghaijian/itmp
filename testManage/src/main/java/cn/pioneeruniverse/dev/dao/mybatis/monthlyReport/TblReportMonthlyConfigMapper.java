package cn.pioneeruniverse.dev.dao.mybatis.monthlyReport;

import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlyConfig;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblReportMonthlyConfigMapper extends BaseMapper<TblReportMonthlyConfig> {


    TblReportMonthlyConfig findBySystemIdAndUserTypeAndUserId(@Param("systemId") Long systemId, @Param("userType") Integer userType);

    void updateMonthlyConfig(TblReportMonthlyConfig tblReportMonthlyConfig );

    List<TblReportMonthlyConfig> findByUserIdByUserType(@Param("userId") Long userId,@Param("userType") long userType);

    String getUserNameBySystemId(@Param("systemId") Long systemId);

    List<TblReportMonthlyConfig> findByUserId(@Param("userId") long userId);

    List<TblReportMonthlyConfig> getReportMonthlyManager();

}
