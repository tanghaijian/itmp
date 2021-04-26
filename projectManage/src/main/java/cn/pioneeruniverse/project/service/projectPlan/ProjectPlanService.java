package cn.pioneeruniverse.project.service.projectPlan;

import cn.pioneeruniverse.project.entity.TblProjectPlanApproveUser;
import cn.pioneeruniverse.project.entity.TblProjectPlanHistory;
import cn.pioneeruniverse.project.entity.TblProjectPlanInfo;
import cn.pioneeruniverse.project.entity.TblUserInfo;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public interface ProjectPlanService {
	/**
	 *
	 * @Title: getProjectPlan
	 * @Description: 获取某个项目的项目计划
	 * @author author
	 * @param projectId 项目ID
	 * @param request
	 * @return Map<String,Object>
	 */
	Map<String,Object> getProjectPlan(Long projectId,HttpServletRequest request);
	/**
	 *
	 * @Title: getProjectPlan1
	 * @Description: 获取项目的最新项目计划
	 * @author author
	 * @param projectId 项目ID
	 * @return Map<String,Object>
	 */
	Map<String,Object> getProjectPlan1(Long projectId);
	/**
	 *
	 * @Title: insertProjectPlan
	 * @Description: 批量插入项目计划
	 * @author author
	 * @param projectId 项目ID
	 * @param projectPlans 项目计划信息
	 * @param request
	 */
	void insertProjectPlan(Long projectId,String projectPlans,HttpServletRequest request);
	/**
	 *
	 * @Title: updateProjectPlan
	 * @Description: 更新项目计划，【提交变更】
	 * @author author
	 * @param projectId 项目ID
	 * @param projectPlanList 项目计划信息
	 * @param approveUsers 审批人
	 * @param updateMessage 提交信息
	 * @param request
	 */
	void updateProjectPlan(Long projectId,String projectPlanList,String approveUsers,
						   String updateMessage, HttpServletRequest request);
	/**
	 *
	 * @Title: updateProjectPlan2
	 * @Description: 更新项目计划，在已有变更时，重新申请变更
	 * @author author
	 * @param projectId 项目ID
	 * @param projectPlanList 项目计划信息
	 * @param approveUsers 审批人
	 * @param updateMessage 提交信息
	 * @param approveRequestId 变更申请id
	 * @param request
	 */
	void updateProjectPlan2(Long projectId,String projectPlanList,String approveUsers,
						   String updateMessage,Long approveRequestId, HttpServletRequest request);
	/**
	 *
	 * @Title: getAllPlanNumber
	 * @Description: 获取项目计划历史所有版本号
	 * @author author
	 * @param projectId 项目ID
	 * @return List<TblProjectPlanHistory>
	 */
	List<TblProjectPlanHistory> getAllPlanNumber(Long projectId);
	/**
	 *
	 * @Title: getPlanNumberByNumber
	 * @Description: 获取具体某个版本号下的项目计划
	 * @author author
	 * @param projectId 项目ID
	 * @param planNumber 版本号
	 * @return Map<String,Object>
	 */
	Map<String,Object> getPlanNumberByNumber(Long projectId,Integer planNumber);
	/**
	 *
	 * @Title: callOffUpdate
	 * @Description: 更新项目信息的项目计划状态
	 * @author author
	 * @param projectId 项目ID
	 * @param approveRequestId 项目计划申请id
	 * @param request
	 */
	void callOffUpdate(Long projectId, Long approveRequestId,HttpServletRequest request);
	/**
	 *
	 * @Title: getPlanApproveRequest
	 * @Description: 获取项目变更申请信息
	 * @author author
	 * @param projectId 项目ID
	 * @param approveRequestId 项目变更申请ID
	 * @param request
	 * @return Map<String,Object>
	 */
	Map<String,Object> getPlanApproveRequest(Long projectId,Long approveRequestId,HttpServletRequest request);
	/**
	 *
	 * @Title: approve
	 * @Description: 项目变更审批
	 * @author author
	 * @param approveUser 审批人
	 * @param request
	 */
	void approve(TblProjectPlanApproveUser approveUser, HttpServletRequest request);
	/**
	 *
	 * @Title: selectUserInfoVague
	 * @Description: 通过姓名和账号模糊查询人员
	 * @author author
	 * @param userInfo 查询信息，主要利用userAndAccount字段
	 * @return List<TblUserInfo>
	 */
	List<TblUserInfo> selectUserInfoVague(TblUserInfo userInfo);
	/**
	 *
	 * @Title: getProjectPlanById
	 * @Description: 获取项目计划
	 * @author author
	 * @param id  项目计划id
	 * @return
	 */
	Map<String, Object> getProjectPlanById(Long id);
	/**
	 *
	 * @Title: selectUserInfoVagueInProject
	 * @Description: 获取人员项目
	 * @author author
	 * @param userInfo 人员查询信息，封装userAndAccount：姓名或账号
	 * @param systemId 系统id
	 * @param request
	 * @return List<TblUserInfo>
	 */
    List<TblUserInfo> selectUserInfoVagueInProject(TblUserInfo userInfo,Long systemId,HttpServletRequest request);
}
