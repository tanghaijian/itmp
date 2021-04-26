package cn.pioneeruniverse.project.dao.mybatis.projectPlan;

import cn.pioneeruniverse.project.entity.TblProjectPlanApproveUser;
import cn.pioneeruniverse.project.entity.TblProjectPlanApproveUserConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectPlanApproveUserMapper {
    //查找项目默认审批人
    List<TblProjectPlanApproveUserConfig> getApproveUserConfig(Long projectId);
    //添加变更审批申请的所对应的审批人
    void insertProjectPlanApproveUser(TblProjectPlanApproveUser planApproveUser);
    //删除项目默认审批人
    void deleteProjectPlanApproveUserConfig(Long projectId);
    //添加项目默认审批人
    void insertProjectPlanApproveUserConfig(TblProjectPlanApproveUserConfig approveUserConfig);
    //获取变更审批申请的审批人数量
    List<TblProjectPlanApproveUser> getAllApproveUserDesc(Long approveRequestId);
    //查询对应的审批人信息
    TblProjectPlanApproveUser getApproveUserButt(@Param("approveRequestId")Long approveRequestId,
                                                 @Param("userId")Long userId);

    //删除变更审批申请审批人信息（取消修改）
    void deletePlanApproveUserByRequestId(Long approveRequestId);

    //审批人审批操作并关闭本级审批开关
    void updateApproveUser(TblProjectPlanApproveUser approveUser);

    //打开下一级审批人审批开关
    void updateApproveUser1(Long id);
}
