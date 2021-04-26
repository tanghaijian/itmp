package cn.pioneeruniverse.project.service.projectPlan.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.project.dao.mybatis.UserMapper;
import cn.pioneeruniverse.project.dao.mybatis.projectPlan.ProjectPlanApproveRequestLogMapper;
import cn.pioneeruniverse.project.dao.mybatis.projectPlan.ProjectPlanApproveRequestMapper;
import cn.pioneeruniverse.project.dao.mybatis.projectPlan.ProjectPlanApproveUserMapper;
import cn.pioneeruniverse.project.dao.mybatis.projectPlan.ProjectPlanHistoryMapper;
import cn.pioneeruniverse.project.dao.mybatis.projectPlan.ProjectPlanMapper;
import cn.pioneeruniverse.project.entity.TblProjectPlanApproveRequest;
import cn.pioneeruniverse.project.entity.TblProjectPlanApproveRequestDetail;
import cn.pioneeruniverse.project.entity.TblProjectPlanApproveRequestLog;
import cn.pioneeruniverse.project.entity.TblProjectPlanApproveUser;
import cn.pioneeruniverse.project.entity.TblProjectPlanApproveUserConfig;
import cn.pioneeruniverse.project.entity.TblProjectPlanHistory;
import cn.pioneeruniverse.project.entity.TblProjectPlanInfo;
import cn.pioneeruniverse.project.entity.TblUserInfo;
import cn.pioneeruniverse.project.service.projectPlan.ProjectPlanService;
import cn.pioneeruniverse.project.vo.JqueryGanttVO;

@Service
public class ProjectPlanServiceImpl implements ProjectPlanService {
	
	@Autowired
	private ProjectPlanMapper projectPlanMapper;
	@Autowired
	private ProjectPlanHistoryMapper planHistoryMapper;
	@Autowired
	private ProjectPlanApproveRequestMapper approveRequestMapper;
	@Autowired
	private ProjectPlanApproveRequestLogMapper requestLogMapper;
	@Autowired
	private ProjectPlanApproveUserMapper approveUserMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RedisUtils redisUtils;

	private final static Logger logger = LoggerFactory.getLogger(ProjectPlanServiceImpl.class);

	/**
	 * 
	* @Title: getProjectPlan
	* @Description: 获取某个项目的项目计划
	* @author author
	* @param projectId 项目ID
	* @param request
	* @return Map<String,Object>
	 */
	@Override
	@Transactional(readOnly=true)
	public Map<String,Object> getProjectPlan(Long projectId,HttpServletRequest request) {
		Map<String,Object> map = new HashMap<>();
		List<JqueryGanttVO> list = new ArrayList<>();
		//获取项目计划状态 1:草稿，2:待审批，3:已审批
		Integer planStatus = projectPlanMapper.getProjectPlanStatus(projectId);
        Integer planNumber = 0;
		if (planStatus == null){
            planStatus = 1;
			list = projectPlanMapper.getProjectPlanTemplate();
		}else if(planStatus == 2||planStatus == 3){
			planNumber = planHistoryMapper.getMaxPlanNumber(projectId);
			TblProjectPlanApproveRequest approveRequest = approveRequestMapper.getLastPlanApproveRequest(projectId);
			list = approveRequestMapper.getPlanApproveRequestDetail(approveRequest.getId());
			map.put("approveRequestId",approveRequest.getId());
		}else if(planStatus == 4){
			planNumber = planHistoryMapper.getMaxPlanNumber(projectId);
			list = projectPlanMapper.getProjectPlan(projectId);
		}else{
            planStatus = 1;
			list = projectPlanMapper.getProjectPlanTemplate();
		}
		List<JqueryGanttVO> list1 = setList(list);
		//获取项目计划变更默认审批人
		List<TblProjectPlanApproveUserConfig> approveUserConfig =approveUserMapper.getApproveUserConfig(projectId);
        //返回数据
		map.put("data",list1);
		//项目计划状态1:草稿，2:待审批，3:审批中 ， 4正式
        map.put("planStatus",planStatus);
        //项目计划最大版本号
        map.put("planNumber",planNumber);
        //项目计划审批人
		map.put("approveUserConfig",approveUserConfig);
		//当前登录人员
		map.put("userId",CommonUtil.getCurrentUserId(request));
		return map;
	}

	/**
	 * 
	* @Title: getProjectPlan1
	* @Description: 获取项目的最新项目计划
	* @author author
	* @param projectId 项目ID
	* @return Map<String,Object>
	 */
	@Override
	@Transactional(readOnly=true)
	public Map<String,Object> getProjectPlan1(Long projectId) {
		Map<String,Object> map = new HashMap<>();
		Integer planNumber = planHistoryMapper.getMaxPlanNumber(projectId);
		List<JqueryGanttVO> list = projectPlanMapper.getProjectPlan(projectId);
		List<JqueryGanttVO> list1 = setList(list);
		//项目计划信息
		map.put("data",list1);
		//正式状态
        map.put("planStatus",4);
        //项目计划版本号
        map.put("planNumber",planNumber);
		return map;
	}

	/**
	 * 
	* @Title: insertProjectPlan
	* @Description: 批量插入项目计划
	* @author author
	* @param projectId 项目ID
	* @param projectPlanList 项目计划信息
	* @param request
	 */
	@Override
	@Transactional(readOnly=false)
	public void insertProjectPlan(Long projectId,String projectPlanList,HttpServletRequest request) {
		projectPlanMapper.updateProjectStatus(4,projectId);

		List<TblProjectPlanInfo> projectPlans = JSON.parseArray(projectPlanList,TblProjectPlanInfo.class);
		projectPlanMapper.deleteProjectPlanByProjectId(projectId);
		for(TblProjectPlanInfo planInfo : projectPlans){
			CommonUtil.setBaseValue(planInfo,request);
			projectPlanMapper.insertProjectPlan(planInfo);
		}
		//插入历史
		List<TblProjectPlanHistory> projectPlanHistorys = JSON.parseArray(projectPlanList,TblProjectPlanHistory.class);
		for(TblProjectPlanHistory planHistory : projectPlanHistorys){
			CommonUtil.setBaseValue(planHistory,request);
			planHistory.setProjectPlanNumber(1);
			planHistoryMapper.insertProjectPlanHistory(planHistory);
		}
	}

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
	@Override
	@Transactional(readOnly=false)
	public void updateProjectPlan(Long projectId, String projectPlanList, String approveUsers,
								  String updateMessage, HttpServletRequest request) {
		//修改项目计划状态为2（待审批）
		projectPlanMapper.updateProjectStatus(2,projectId);

		//添加审批申请记录
		TblProjectPlanApproveRequest approveRequest = new TblProjectPlanApproveRequest();
		approveRequest.setProjectId(projectId);
		approveRequest.setRequestUserId(CommonUtil.getCurrentUserId(request));
		approveRequest.setRequestReason(updateMessage);
		approveRequest.setApproveStatus(1);
		approveRequestMapper.insertPlanApproveRequest(approveRequest);

		//添加审批申请明细记录
		List<TblProjectPlanApproveRequestDetail> approveRequestDetails =
				JSON.parseArray(projectPlanList,TblProjectPlanApproveRequestDetail.class);
		for(TblProjectPlanApproveRequestDetail requestDetail : approveRequestDetails){
			CommonUtil.setBaseValue(requestDetail,request);
			requestDetail.setProjectPlanId(requestDetail.getId());
			requestDetail.setProjectPlanApproveRequestId(approveRequest.getId());
			approveRequestMapper.insertPlanApproveRequestDetail(requestDetail);
		}
		//添加审批申请日志
		TblProjectPlanApproveRequestLog requestLog = new TblProjectPlanApproveRequestLog();
		Long userId = CommonUtil.getCurrentUserId(request);
		requestLog.setProjectPlanApproveRequestId(approveRequest.getId());
		requestLog.setLogType("申请变更审批");
		requestLog.setLogDetail(updateMessage);
		requestLog.setUserId(userId);
		TblUserInfo userInfo = userMapper.selectUserById(userId);
		requestLog.setUserName(userInfo.getUserName());
		requestLog.setUserAccount(userInfo.getUserAccount());
		CommonUtil.setBaseValue(requestLog,request);
		requestLogMapper.insertPlanApproveRequestLog(requestLog);

		// 修改审批申请默认配置人员
		// 添加审批申请人
		approveUserMapper.deleteProjectPlanApproveUserConfig(projectId);
		List<TblProjectPlanApproveUser> approveUserList = JSON.parseArray(approveUsers,TblProjectPlanApproveUser.class);
		for(TblProjectPlanApproveUser user : approveUserList){
			user.setProjectPlanApproveRequestId(approveRequest.getId());
			user.setApproveStatus(1);
			if(user.getApproveLevel()==1){
				user.setApproveOnOff(1);
			}else {
				user.setApproveOnOff(0);
			}
			approveUserMapper.insertProjectPlanApproveUser(user);

			TblProjectPlanApproveUserConfig userConfig = new TblProjectPlanApproveUserConfig();
			userConfig.setProjectId(projectId);
			userConfig.setUserId(user.getUserId());
			userConfig.setApproveLevel(user.getApproveLevel());
			approveUserMapper.insertProjectPlanApproveUserConfig(userConfig);
		}
	}

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
	@Override
	@Transactional(readOnly=false)
	public void updateProjectPlan2(Long projectId, String projectPlanList, String approveUsers,
								  String updateMessage,Long approveRequestId, HttpServletRequest request) {
		
		//添加审批申请记录
		TblProjectPlanApproveRequest approveRequest = new TblProjectPlanApproveRequest();
		approveRequest.setId(approveRequestId);
		approveRequest.setRequestUserId(CommonUtil.getCurrentUserId(request));
		approveRequest.setRequestReason(updateMessage);
		approveRequestMapper.updatePlanApproveRequest(approveRequest);

		//添加审批申请明细记录
		approveRequestMapper.deleteApproveRequestDetailByRequestId(approveRequestId);
		List<TblProjectPlanApproveRequestDetail> approveRequestDetails =
				JSON.parseArray(projectPlanList,TblProjectPlanApproveRequestDetail.class);
		for(TblProjectPlanApproveRequestDetail requestDetail : approveRequestDetails){
			CommonUtil.setBaseValue(requestDetail,request);
			requestDetail.setProjectPlanId(requestDetail.getId());
			requestDetail.setProjectPlanApproveRequestId(approveRequestId);
			approveRequestMapper.insertPlanApproveRequestDetail(requestDetail);
		}

		//添加审批申请日志
		TblProjectPlanApproveRequestLog requestLog = new TblProjectPlanApproveRequestLog();
		Long userId = CommonUtil.getCurrentUserId(request);
		requestLog.setProjectPlanApproveRequestId(approveRequest.getId());
		requestLog.setLogType("重新申请变更审批");
		requestLog.setLogDetail(updateMessage);
		requestLog.setUserId(userId);
		TblUserInfo userInfo = userMapper.selectUserById(userId);
		requestLog.setUserName(userInfo.getUserName());
		requestLog.setUserAccount(userInfo.getUserAccount());
		CommonUtil.setBaseValue(requestLog,request);
		requestLogMapper.insertPlanApproveRequestLog(requestLog);

		// 修改审批申请默认配置人员,先删后插
		// 添加审批申请人
		approveUserMapper.deleteProjectPlanApproveUserConfig(projectId);
		approveUserMapper.deletePlanApproveUserByRequestId(approveRequestId);
		List<TblProjectPlanApproveUser> approveUserList = JSON.parseArray(approveUsers,TblProjectPlanApproveUser.class);
		for(TblProjectPlanApproveUser user : approveUserList){
			user.setProjectPlanApproveRequestId(approveRequestId);
			user.setApproveStatus(1);
			if(user.getApproveLevel()==1){
				user.setApproveOnOff(1);
			}else {
				user.setApproveOnOff(0);
			}
			approveUserMapper.insertProjectPlanApproveUser(user);

			TblProjectPlanApproveUserConfig userConfig = new TblProjectPlanApproveUserConfig();
			userConfig.setProjectId(projectId);
			userConfig.setUserId(user.getUserId());
			userConfig.setApproveLevel(user.getApproveLevel());
			approveUserMapper.insertProjectPlanApproveUserConfig(userConfig);
		}
	}

	/**
	 * 
	* @Title: getAllPlanNumber
	* @Description: 获取项目计划历史所有版本号
	* @author author
	* @param projectId 项目ID
	* @return List<TblProjectPlanHistory>
	 */
	@Override
	@Transactional(readOnly=true)
	public List<TblProjectPlanHistory> getAllPlanNumber(Long projectId) {
		return planHistoryMapper.getAllPlanNumber(projectId);
	}

	/**
	 * 
	* @Title: getPlanNumberByNumber
	* @Description: 获取具体某个版本号下的项目计划
	* @author author
	* @param projectId 项目ID
	* @param planNumber 版本号
	* @return Map<String,Object>
	 */
	@Override
	@Transactional(readOnly=true)
	public Map<String,Object> getPlanNumberByNumber(Long projectId,Integer planNumber) {
		Map<String,Object> map = new HashMap<>();
		List<JqueryGanttVO> list = planHistoryMapper.getPlanNumberByNumber(projectId,planNumber);
		//重新组装开始\结束里程碑字段，方便前台获取
		List<JqueryGanttVO> list1 = setList(list);
		//返回的项目计划
		map.put("data",list1);
		return map;
	}

	/**
	 * 
	* @Title: callOffUpdate
	* @Description: 更新项目信息的项目计划状态
	* @author author
	* @param projectId 项目ID
	* @param approveRequestId 项目计划申请id
	* @param request
	 */
	@Override
	@Transactional(readOnly=false)
	public void callOffUpdate(Long projectId, Long approveRequestId,HttpServletRequest request) {
		projectPlanMapper.updateProjectStatus(4,projectId);
		approveRequestMapper.deleteApproveRequestById(approveRequestId);
		approveRequestMapper.deleteApproveRequestDetailByRequestId(approveRequestId);
		requestLogMapper.deleteApproveRequestLogByRequestId(approveRequestId);
		approveUserMapper.deletePlanApproveUserByRequestId(approveRequestId);
	}

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
	@Override
	@Transactional(readOnly=true)
	public Map<String, Object> getPlanApproveRequest(Long projectId,Long approveRequestId,HttpServletRequest request) {
		Map<String,Object> map = new HashMap<>();
		//项目计划变更申请信息
		TblProjectPlanApproveRequest approveRequest = approveRequestMapper.getPlanApproveRequestById(approveRequestId);

		//项目计划信息
		List<TblProjectPlanInfo> projectPlan = projectPlanMapper.getProjectPlanByProjectId(projectId);
		//项目计划变更申请详情
		List<TblProjectPlanApproveRequestDetail> requestDetail
				= approveRequestMapper.getRequestDetailByApproveId(approveRequestId);
		List<TblProjectPlanApproveRequestLog> requestLog = requestLogMapper.getApproveRequestLog(approveRequestId);
		approveRequest.setCommitDate(requestDetail.get(0).getCreateDate());

		Long userId = CommonUtil.getCurrentUserId(request);

		//获取到该变更审批人员中属于userId的信息
		TblProjectPlanApproveUser approveUser = approveUserMapper.getApproveUserButt(approveRequestId,userId);
		if(approveUser != null){
			//存在一个计划变更的审批有多层，如果userId属于最高一层审批，则直接办结
			List<TblProjectPlanApproveUser> approveUsers = approveUserMapper.getAllApproveUserDesc(approveRequestId);
			if(userId.equals(approveUsers.get(0).getUserId())){
				map.put("succ",2);//办结
			}else {
				map.put("succ",1);//下一级审批
			}
			//审批申请人员表ID
			map.put("id",approveUser.getId());
		}
		//当前操作人员ID
		map.put("userId1",userId);
		//变更申请日志信息
		map.put("requestLog",requestLog);
		//项目计划
		map.put("projectPlan",projectPlan);
		//申请详情
		map.put("requestDetail",requestDetail);
		//变更申请
		map.put("approveRequest",approveRequest);
		return map;
	}

	/**
	 * 
	* @Title: approve
	* @Description: 项目变更审批
	* @author author
	* @param approveUser 审批人
	* @param request
	 */
	@Override
	@Transactional(readOnly=false)
	public void approve(TblProjectPlanApproveUser approveUser,HttpServletRequest request){
        //修改本级审批人审批状态为已审批（已审批）
        approveUser.setApproveStatus(2);
		approveUser.setApproveOnOff(0);
        approveUserMapper.updateApproveUser(approveUser);

        //添加审批日志信息
        TblProjectPlanApproveRequestLog requestLog = new TblProjectPlanApproveRequestLog();
        requestLog.setProjectPlanApproveRequestId(approveUser.getProjectPlanApproveRequestId());
        requestLog.setLogDetail(approveUser.getApproveSuggest());
        requestLog.setUserId(approveUser.getUserId());
        TblUserInfo userInfo = userMapper.selectUserById(approveUser.getUserId());
        requestLog.setUserName(userInfo.getUserName());
        requestLog.setUserAccount(userInfo.getUserAccount());
        CommonUtil.setBaseValue(requestLog,request);

        Integer projectPlanStatus = 0;
        //前端按钮操作：【同意并提交下一级审批人】
		if(approveUser.getOperate()==1){
			List<TblProjectPlanApproveUser> approveUsers = approveUserMapper.
					getAllApproveUserDesc(approveUser.getProjectPlanApproveRequestId());
			for(int i=0;i<approveUsers.size();i++){
				if(approveUser.getId().equals(approveUsers.get(i).getId())){
					logger.debug(approveUsers.get(i-1).getId().toString());
					approveUserMapper.updateApproveUser1(approveUsers.get(i-1).getId());
				}
			}
            projectPlanStatus = 3; //修改项目计划状态为3（审批中）
			requestLog.setLogType("同意并提交下一级审批人");
		}else if(approveUser.getOperate()==2){//【同意并办结】
            projectPlanStatus = 4; //修改项目计划状态为4（正式）
			requestLog.setLogType("同意并办结");

            //将申请变更记录的状态变为2(审批完成)
            TblProjectPlanApproveRequest approveRequest = new TblProjectPlanApproveRequest();
            approveRequest.setId(approveUser.getProjectPlanApproveRequestId());
            approveRequest.setApproveStatus(2);
            approveRequestMapper.updatePlanApproveStatus(approveRequest);

			//添加正式版本与历史版本
			insertPlan(approveUser.getProjectId(),approveUser.getProjectPlanApproveRequestId(),request);
		}else if(approveUser.getOperate()==3){//【驳回】
            projectPlanStatus = 4; //修改项目计划状态为4（正式）
			requestLog.setLogType("驳回项目计划变更");

			//将申请变更记录的状态变为3(驳回)
			TblProjectPlanApproveRequest approveRequest = new TblProjectPlanApproveRequest();
			approveRequest.setId(approveUser.getProjectPlanApproveRequestId());
			approveRequest.setApproveStatus(3);
			approveRequestMapper.updatePlanApproveStatus(approveRequest);
		}
        requestLogMapper.insertPlanApproveRequestLog(requestLog);
        projectPlanMapper.updateProjectStatus(projectPlanStatus,approveUser.getProjectId());
	}

	/**
	 * 
	* @Title: selectUserInfoVague
	* @Description: 通过姓名和账号模糊查询人员
	* @author author
	* @param userInfo 查询信息，主要利用userAndAccount字段
	* @return List<TblUserInfo>
	 */
	@Override
	public List<TblUserInfo> selectUserInfoVague(TblUserInfo userInfo) {
		return projectPlanMapper.selectUserInfoVague(userInfo.getUserAndAccount());
	}

	/**
	 * 
	* @Title: getProjectPlanById
	* @Description: 获取项目计划
	* @author author
	* @param id  项目计划id
	* @return
	 */
	@Override
	public Map<String, Object> getProjectPlanById(Long id) {
		return projectPlanMapper.getProjectPlanById(id);
	}

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
	@Override
	public List<TblUserInfo> selectUserInfoVagueInProject(TblUserInfo userInfo,Long systemId,HttpServletRequest request) {
		Long userId=CommonUtil.getCurrentUserId(request);
		LinkedHashMap codeMap = (LinkedHashMap) redisUtils.get(CommonUtil.getToken(request));
		List<String> roleCodes = (List<String>) codeMap.get("roles");
//		if (roleCodes != null && roleCodes.contains("XITONGGUANLIYUAN")) {
//
//			return projectPlanMapper.selectUserInfoVague(userInfo.getUserAndAccount());
//		} else {
			Map<String,Object> param=new HashMap<>();
			param.put("userAndAccount",userInfo.getUserAndAccount());

			param.put("systemId",systemId);
			return projectPlanMapper.selectUserInfoVagueInProject(param);
		//}

	}

	//审批完成添加正式版本与历史版本
	private void insertPlan(Long projectId,Long approveRequestId,HttpServletRequest request){
		List<TblProjectPlanApproveRequestDetail> approveRequestDetail =
				approveRequestMapper.getRequestDetailByApproveId(approveRequestId);
		String detail = JSON.toJSONString(approveRequestDetail);

		//新增
		List<TblProjectPlanInfo> projectPlans = JSON.parseArray(detail,TblProjectPlanInfo.class);
		projectPlanMapper.deleteProjectPlanByProjectId(projectId);
		for(TblProjectPlanInfo planInfo : projectPlans){
			planInfo.setProjectId(projectId);
			CommonUtil.setBaseValue(planInfo,request);
			projectPlanMapper.insertProjectPlan(planInfo);
		}
		//添加历史
		List<TblProjectPlanHistory> projectPlanHistorys = JSON.parseArray(detail,TblProjectPlanHistory.class);
		Integer number = planHistoryMapper.getMaxPlanNumber(projectId)+1;
		for(TblProjectPlanHistory planHistory : projectPlanHistorys){
			planHistory.setProjectId(projectId);
			CommonUtil.setBaseValue(planHistory,request);
			planHistory.setProjectPlanNumber(number);
			planHistoryMapper.insertProjectPlanHistory(planHistory);
		}
	}


	//甘特图重新组装开始和结束里程碑
	private List<JqueryGanttVO> setList(List<JqueryGanttVO> list){
		for(JqueryGanttVO jqueryGanttVO:list) {
			if (jqueryGanttVO.getStartIsMilestone1() != null && jqueryGanttVO.getStartIsMilestone1() == 1) {
				jqueryGanttVO.setStartIsMilestone(true);
			} else if (jqueryGanttVO.getStartIsMilestone1() != null && jqueryGanttVO.getStartIsMilestone1() == 2) {
				jqueryGanttVO.setStartIsMilestone(false);
			}
			if (jqueryGanttVO.getEndIsMilestone1() != null && jqueryGanttVO.getEndIsMilestone1() == 1) {
				jqueryGanttVO.setEndIsMilestone(true);
			} else if (jqueryGanttVO.getEndIsMilestone1() != null && jqueryGanttVO.getEndIsMilestone1() == 2) {
				jqueryGanttVO.setEndIsMilestone(false);
			}
			if(jqueryGanttVO.getStart() == null){
				jqueryGanttVO.setStart(new Timestamp(new Date().getTime()));
			}
			if(jqueryGanttVO.getEnd() == null){
				jqueryGanttVO.setEnd(new Timestamp(new Date().getTime()));
			}
		}
		return list;
	}
}
