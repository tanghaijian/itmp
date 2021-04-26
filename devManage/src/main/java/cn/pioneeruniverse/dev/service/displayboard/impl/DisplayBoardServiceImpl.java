package cn.pioneeruniverse.dev.service.displayboard.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.dev.entity.*;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.JsonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.dev.dao.mybatis.TblDevTaskAttentionMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDevTaskMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblProjectInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementFeatureAttentionMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementFeatureLogMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementFeatureMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSprintInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemInfoMapper;
import cn.pioneeruniverse.dev.feignInterface.DevManageToSystemInterface;
import cn.pioneeruniverse.dev.service.devtask.DevTaskService;
import cn.pioneeruniverse.dev.service.displayboard.DisplayBoardService;

/**
*  类说明 
* @author:tingting
* @version:2019年3月20日 下午1:38:34 
*/
@Service
@Transactional(readOnly = true)
public class DisplayBoardServiceImpl implements DisplayBoardService{
	@Autowired
	private TblProjectInfoMapper projectInfoMapper;
	@Autowired
	private TblSystemInfoMapper systemInfoMapper;
	@Autowired
	private TblSprintInfoMapper sprintInfoMapper;
	@Autowired
	private TblDevTaskMapper devTaskMapper;
	@Autowired
	private TblRequirementFeatureMapper requirementFeatureMapper;
	@Autowired
	private DevTaskService devTaskService;
	@Autowired
	private  TblProjectInfoMapper tblProjectInfoMapper;
	@Autowired
	private TblRequirementFeatureLogMapper requirementFeatureLogMapper;
	@Autowired
	private TblRequirementFeatureAttentionMapper tblRequirementFeatureAttentionMapper;
	@Autowired
    private DevManageToSystemInterface devManageToSystemInterface;
	@Autowired 
	private RedisUtils redisUtils;
	@Autowired
	private TblDevTaskAttentionMapper tblDevTaskAttentionMapper;

	/**
	 *@author liushan
	 *@Description 根据用户获取项目
	 *@Date 2020/8/4
	 * @param uid
	 *@return java.util.List<cn.pioneeruniverse.dev.entity.TblProjectInfo>
	 **/
	@Override
	public List<TblProjectInfo> getAllProjectByUid(Long uid) {

		return projectInfoMapper.getAllProjectByUid(uid);
	}
	/**
	 *@author liushan
	 *@Description 根据项目查询系统
	 *@Date 2020/8/4
	 * @param projectId 项目id
	 *@return java.util.List<cn.pioneeruniverse.dev.entity.TblSystemInfo>
	 **/
	@Override
	public List<TblSystemInfo> getSystemByPId(Long projectId) {
		return systemInfoMapper.getSystemByPId(projectId);
	}
	/**
	 *@author liushan
	 *@Description 根据系统获取冲刺
	 *@Date 2020/8/4
	 * @param systemId 系统id
	 *@return java.util.List<cn.pioneeruniverse.dev.entity.TblSprintInfo>
	 **/
	@Override
	public List<TblSprintInfo> getSprintBySystemId(Long systemId) {
		return sprintInfoMapper.getSprintBySystemId(systemId);
	}
	/**
	 *@author liushan
	 *@Description 根据冲刺获取开发任务
	 *@Date 2020/8/4
	 * @param sprintId 冲刺id
	 *@return java.util.List<cn.pioneeruniverse.dev.entity.DevTaskVo>
	 **/
	@Override
	public List<DevTaskVo> getDevTaskBySprintId(Long sprintId, String devUserName) {
		return requirementFeatureMapper.getDevTaskBySprintIds(sprintId, devUserName);
	}
	/**
	 *@author liushan
	 *@Description 根据开发任务获取工作任务
	 *@Date 2020/8/4
	 * @param id 开发任务id
	 *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTask>
	 **/
	@Override
	public List<TblDevTask> getDevTaskByReqFeatureId(Long id) {
		return devTaskMapper.findByReqFeature2(id);
	}
	/**
	 *@author liushan
	 *@Description 编辑工作任务状态
	 *@Date 2020/8/4
	 * @param reqFeatureId 开发任务Id
	 * @param status
	 * @param request
	 **/
	@Override
	@Transactional(readOnly=false)
	public void updateDevTaskStatus(Long reqFeatureId, String status, HttpServletRequest request) {
		TblRequirementFeature requirementFeature2 = requirementFeatureMapper
				.selectByPrimaryKey(reqFeatureId);
		
		requirementFeatureMapper.updateReFeatureStatus(reqFeatureId,status);
		
		//更新开发任务时间点追踪表 中 状态变为实施完成时间 字段
		if ("03".equals(status)) {
			Map<String, Object> jsonMap = new HashMap<>();
			jsonMap.put("requirementFeatureId", reqFeatureId);
			jsonMap.put("requirementFeatureDevCompleteTime",new Timestamp(new Date().getTime()));
			String json = JsonUtil.toJson(jsonMap);
			devTaskService.updateReqFeatureTimeTrace(json);
		}
		
		if("00".equals(status)) {
			//如果开发任务状态是取消 下属工作任务状态也变成取消
			//devTaskMapper.updateStatus(reqFeatureId);
			devTaskService.cancelStatusReqFeature(reqFeatureId);
			
		}
		//插入日志
		TblRequirementFeatureLog log = new TblRequirementFeatureLog();
		log.setRequirementFeatureId(reqFeatureId);
		log.setLogType("修改开发任务");
		
		String beforeName = "";
		String afterName = "";
		List<TblDataDic> dataDics = getDataFromRedis("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS");
		for (TblDataDic tblDataDic : dataDics) {
			if (!StringUtils.isBlank(requirementFeature2.getRequirementFeatureStatus())) {
				if (tblDataDic.getValueCode().equals(requirementFeature2.getRequirementFeatureStatus().toString())) {
					beforeName = tblDataDic.getValueName();
				}
			}
			if(!StringUtils.isBlank(status)) {
				if (tblDataDic.getValueCode().equals(status)) {
					afterName = tblDataDic.getValueName();
				}
			}
		}
		log.setLogDetail("任务状态："+"&nbsp;&nbsp;“<b>"+beforeName+"</b>”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“<b>"+afterName+"</b>”&nbsp;&nbsp;") ;
		log.setCreateBy(CommonUtil.getCurrentUserId(request));
		log.setCreateDate(new Timestamp(new Date().getTime()));
		log.setUserId(CommonUtil.getCurrentUserId(request));
		log.setUserAccount(CommonUtil.getCurrentUserAccount(request));
		log.setUserName(CommonUtil.getCurrentUserName(request));
		requirementFeatureLogMapper.insert(log);
		
		//关注提醒：开发 任务状态或者内容变更时 给关注该任务的人发送邮件和微信 --ztt
		String userIds = tblRequirementFeatureAttentionMapper.getUserIdsByReqFeatureId(reqFeatureId);
		if(StringUtils.isNotBlank(userIds)) {
			Map<String,Object> emWeMap = new HashMap<String, Object>();
			emWeMap.put("messageTitle", "【IT开发测试管理系统】- 你关注的任务有更新");
			emWeMap.put("messageContent","您关注的“"+ requirementFeature2.getFeatureCode()+" | "+requirementFeature2.getFeatureName()+"”，内容有更新，请注意。");
			emWeMap.put("messageReceiver",userIds );//接收人 关注该任务的人
			emWeMap.put("sendMethod", 3);//发送方式 3 邮件和微信
			devManageToSystemInterface.sendMessage(JSON.toJSONString(emWeMap));
		}
		
	}
	
	public List<TblDataDic> getDataFromRedis(String termCode) {
		String result = redisUtils.get(termCode).toString();
		List<TblDataDic> dics = new ArrayList<>();
		if (!StringUtils.isBlank(result)) {
			Map<String, Object> maps = (Map<String, Object>) JSON.parse(result);
			for (Entry<String, Object> entry : maps.entrySet()) {
				TblDataDic dic = new TblDataDic();
				dic.setValueCode(entry.getKey());
				dic.setValueName(entry.getValue().toString());
				dics.add(dic);
			}
		}
		return dics;
	}
	/**
	 *@author liushan
	 *@Description 根据开发任务id统计各状态工作任务的数量
	 *@Date 2020/8/4
	 * @param id
	 *@return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 **/
	@Override
	public List<Map<String, Object>> getWorkTaskStatusCount(Long id) {
		return devTaskMapper.getStatusCount(id);
	}
	/**
	 *@author liushan
	 *@Description 获取开发任务数据字典状态
	 *@Date 2020/8/4
	 *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDataDic>
	 **/
	@Override
	public List<TblDataDic> getReqFeatureStatus() {
		return requirementFeatureMapper.getReqFeatureStatus();
	}

	/**
	 *@author liushan
	 *@Description 根据冲刺获取工作任务
	 *@Date 2020/8/4
	 * @param sprintId 冲刺id
	 *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTask>
	 **/
	@Override
	public List<TblDevTask> getWorkTaskBySprintId(Long sprintId, String devUserName) {
		return devTaskMapper.getWorkTaskBySprintId(sprintId ,devUserName);
	}

	/**
	 *@author liushan
	 *@Description 获取工作任务数据状态
	 *@Date 2020/8/4
	 *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDataDic>
	 **/
	@Override
	public List<TblDataDic> getWorTaskStatus() {
		return devTaskMapper.getWorTaskStatus();
	}

	/**
	 *@author liushan
	 *@Description 修改工作任务状态
	 *@Date 2020/8/4
	 * @param devTaskId 工作任务id
	 * @param status
	 * @param request
	 *@return void
	 **/
	@Override
	@Transactional(readOnly = false)
	public void updateWorkTaskStatus(Long devTaskId, Integer status, HttpServletRequest request) {
		TblDevTask tblDevTask = devTaskMapper.getDevOld(devTaskId);
		devTaskMapper.updateWorkTaskStatus(devTaskId,status);
		//关注提醒： 开发工作任务状态或者内容变更时给关注该任务的人发送邮件和微信 --ztt
		String userIds2 = tblDevTaskAttentionMapper.getUserIdsByDevTaskId(devTaskId);
		if(StringUtils.isNotBlank(userIds2)) {
			Map<String,Object> emWeMap = new HashMap<String, Object>();
			emWeMap.put("messageTitle", "【IT开发测试管理系统】- 你关注的任务有更新");
			emWeMap.put("messageContent","您关注的“"+ tblDevTask.getDevTaskCode()+" | "+tblDevTask.getDevTaskName()+"”，内容有更新，请注意。");
			emWeMap.put("messageReceiver",userIds2 );//接收人 关注该任务的人
			emWeMap.put("sendMethod", 3);//发送方式 3 邮件和微信
			devManageToSystemInterface.sendMessage(JSON.toJSONString(emWeMap));
		}
		
	}
	/**
	 *@author liushan
	 *@Description 根据项目获取项目小组
	 *@Date 2020/8/4
	 * @param projectId 项目id
	 *@return java.lang.String
	 **/
	@Override
	public String getProjectGroupByProjectId(long projectId) {

		Map<String,Object> mapParam=new HashMap<>();
		mapParam.put("projectId",projectId);
		List<Map<String, Object>> tblProjectGroups=	tblProjectInfoMapper.getProjectGroupByProjectId(mapParam);
		List<String> ids=new ArrayList<>();
		for (Map<String,Object> map:tblProjectGroups){
			ids.add(map.get("id").toString());
		}
		JSONArray jsonArray=new JSONArray();
		for (Map<String,Object> map:tblProjectGroups){
			com.alibaba.fastjson.JSONObject jsonObject=new com.alibaba.fastjson.JSONObject();
			jsonObject.put("id",Long.parseLong(map.get("id").toString()));
			jsonObject.put("name",map.get("projectGroupName").toString());
			if(map.get("parentId")!=null && !map.get("parentId").toString().equals("")){
				//tblProjectInfosChildRen.add(map);
				String id=map.get("parentId").toString();
				jsonObject.put("pId", Long.parseLong(id));
		    }else{

				jsonObject.put("pId",0);
			}
			jsonArray.add(jsonObject);
		}

		com.alibaba.fastjson.JSONObject noProjectObject=new com.alibaba.fastjson.JSONObject();
		noProjectObject.put("id","-1");
		noProjectObject.put("name","无项目组");
		jsonArray.add(noProjectObject);
		return jsonArray.toString();

	}

	/**
	 *@author liushan
	 *@Description 根据项目查询查询项目小组
	 *@Date 2020/8/4
	 * @param projectId
	 *@return java.lang.String
	 **/
	@Override
	public String getProjectGroupByProjectIdNoWu(long projectId) {
		Map<String,Object> mapParam=new HashMap<>();
		mapParam.put("projectId",projectId);
		List<Map<String, Object>> tblProjectGroups=	tblProjectInfoMapper.getProjectGroupByProjectId(mapParam);
		List<String> ids=new ArrayList<>();
		for (Map<String,Object> map:tblProjectGroups){
			ids.add(map.get("id").toString());
		}
		JSONArray jsonArray=new JSONArray();
		for (Map<String,Object> map:tblProjectGroups){
			com.alibaba.fastjson.JSONObject jsonObject=new com.alibaba.fastjson.JSONObject();
			jsonObject.put("id",Long.parseLong(map.get("id").toString()));
			jsonObject.put("name",map.get("projectGroupName").toString());
			if(map.get("parentId")!=null && !map.get("parentId").toString().equals("")){
				//tblProjectInfosChildRen.add(map);
				String id=map.get("parentId").toString();
				jsonObject.put("pId", Long.parseLong(id));
			}else{

				jsonObject.put("pId",0);
			}
			jsonArray.add(jsonObject);
		}

		return jsonArray.toString();

	}


}
