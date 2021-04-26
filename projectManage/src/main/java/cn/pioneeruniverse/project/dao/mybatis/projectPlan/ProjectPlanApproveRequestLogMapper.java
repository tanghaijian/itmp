package cn.pioneeruniverse.project.dao.mybatis.projectPlan;

import cn.pioneeruniverse.project.entity.TblProjectPlanApproveRequestLog;

import java.util.List;

public interface ProjectPlanApproveRequestLogMapper {

    /**
    * @author author
    * @Description 新增项目计划审批日志
    * @Date 2020/9/15
    * @param approveRequestLog
    * @return void
    **/
    void insertPlanApproveRequestLog(TblProjectPlanApproveRequestLog approveRequestLog);

    /**
    * @author author
    * @Description 查询项目计划审批日志
    * @Date 2020/9/15
    * @param approveRequestId
    * @return java.util.List<cn.pioneeruniverse.project.entity.TblProjectPlanApproveRequestLog>
    **/
    List<TblProjectPlanApproveRequestLog> getApproveRequestLog(Long approveRequestId);

    /**
    * @author author
    * @Description 删除项目计划审批日志
    * @Date 2020/9/15
    * @param approveRequestId
    * @return void
    **/
    void deleteApproveRequestLogByRequestId(Long approveRequestId);
}
