package cn.pioneeruniverse.dev.dao.mybatis.projectPlan;

import cn.pioneeruniverse.dev.entity.TblProjectPlan;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProjectPlanMapper {

    /**
    * @author author
    * @Description 计划弹窗总数
    * @Date 2020/9/16
    * @param tblProjectPlan
    * @return int
    **/
    int getAllProjectPlanCount(TblProjectPlan tblProjectPlan);
    /**
    * @author author
    * @Description 计划弹窗（bootstrap）
    * @Date 2020/9/16
    * @param map
    * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    **/
    List<Map<String, Object>> getAllProjectPlan(Map<String, Object> map);
    /**
    * @author author
    * @Description 计划树形
    * @Date 2020/9/16
    * @param projectId
    * @return java.util.List<cn.pioneeruniverse.dev.entity.TblProjectPlan>
    **/
    List<TblProjectPlan> selectPlanTree(Long projectId);

    TblProjectPlan getProjectPlanParentId(@Param("projectId")Long projectId,
                                          @Param("planLevel")Integer planLevel,
                                          @Param("planOrder")Integer planOrder);
}
