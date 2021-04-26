package cn.pioneeruniverse.project.dao.mybatis.projectPlan;

import cn.pioneeruniverse.project.entity.TblProjectPlanHistory;
import cn.pioneeruniverse.project.vo.JqueryGanttVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectPlanHistoryMapper {

    /**
    * @author author
    * @Description 新增项目计划历史
    * @Date 2020/9/15
    * @param planHistory
    * @return void
    **/
    void insertProjectPlanHistory(TblProjectPlanHistory planHistory);

    /**
    * @author author
    * @Description 获取项目计划最大数
    * @Date 2020/9/15
    * @param projectId
    * @return java.lang.Integer
    **/
    Integer getMaxPlanNumber(Long projectId);

    /**
    * @author author
    * @Description 获取所有项目计划数
    * @Date 2020/9/15
    * @param projectId
    * @return java.util.List<cn.pioneeruniverse.project.entity.TblProjectPlanHistory>
    **/
    List<TblProjectPlanHistory> getAllPlanNumber(Long projectId);

    List<JqueryGanttVO> getPlanNumberByNumber(@Param("projectId")Long projectId,
                                              @Param("planNumber")Integer planNumber);
}
