package cn.pioneeruniverse.project.dao.mybatis.projectPlan;

import cn.pioneeruniverse.project.entity.TblProjectPlanApproveRequest;
import cn.pioneeruniverse.project.entity.TblProjectPlanApproveRequestDetail;
import cn.pioneeruniverse.project.entity.TblProjectPlanInfo;
import cn.pioneeruniverse.project.vo.JqueryGanttVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectPlanApproveRequestMapper {
    //根据项目查找最新的审批申请
    TblProjectPlanApproveRequest getLastPlanApproveRequest(Long projectId);
    //根据变更审批申请id查找变更审批申请明细(主页面显示)
    List<JqueryGanttVO> getPlanApproveRequestDetail(Long approveRequestId);
    //新增变更审批申请
    void insertPlanApproveRequest(TblProjectPlanApproveRequest pro);
    //新增变更审批申请明细
    void insertPlanApproveRequestDetail(TblProjectPlanApproveRequestDetail pro);
    //修改变更审批申请
    void updatePlanApproveRequest(TblProjectPlanApproveRequest pro);
    //修改变更审批申请状态
    void updatePlanApproveStatus(TblProjectPlanApproveRequest pro);
    //根据id查找变更审批申请
    TblProjectPlanApproveRequest getPlanApproveRequestById(Long id);
    //根据变更审批申请id查找变更审批申请明细
    List<TblProjectPlanApproveRequestDetail> getRequestDetailByApproveId(Long approveRequestId);
    //删除变更审批申请（取消修改）
    void deleteApproveRequestById(Long id);
    //删除变更审批申请明细（取消修改）
    void deleteApproveRequestDetailByRequestId(Long approveRequestId);
}
