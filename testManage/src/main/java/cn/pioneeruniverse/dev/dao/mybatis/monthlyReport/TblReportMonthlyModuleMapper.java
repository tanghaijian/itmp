package cn.pioneeruniverse.dev.dao.mybatis.monthlyReport;

import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlyModule;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblReportMonthlyModuleMapper  extends BaseMapper<TblReportMonthlyModule> {

    List<TblReportMonthlyModule> findAll();

    List<TblReportMonthlyModule> findByDate(@Param("date") String date);

    int insertMonthlyModule(@Param("lastMonthTime")String lastMonthTime,@Param("nowMonthTime") String nowMonthTime);

    int updateMonthlyModule(TblReportMonthlyModule tblReportMonthlyModule);


    //更新排序 type == 0 代表序号减小1 type == 1 代表需要自增1
    int updateModuleNum(@Param("date") String date,@Param("page") int page,@Param("area") int area ,
                        @Param("num") int num ,@Param("type") int type);
}
