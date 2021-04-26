package cn.pioneeruniverse.dev.controller;

import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.DateUtil;
import cn.pioneeruniverse.common.utils.ExportExcel;
import cn.pioneeruniverse.common.utils.JsonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.common.utils.S3Util;
import cn.pioneeruniverse.dev.entity.ExtendedField;
import cn.pioneeruniverse.dev.entity.TblDevTask;
import cn.pioneeruniverse.dev.entity.TblDevTaskAttachement;
import cn.pioneeruniverse.dev.entity.TblDevTaskAttention;
import cn.pioneeruniverse.dev.entity.TblDevTaskLog;
import cn.pioneeruniverse.dev.entity.TblDevTaskLogAttachement;
import cn.pioneeruniverse.dev.entity.TblDevTaskRemark;
import cn.pioneeruniverse.dev.entity.TblDevTaskRemarkAttachement;
import cn.pioneeruniverse.dev.entity.TblProjectInfo;
import cn.pioneeruniverse.dev.entity.TblRequirementFeature;
import cn.pioneeruniverse.dev.entity.TblRequirementInfo;
import cn.pioneeruniverse.dev.entity.TblSystemInfo;
import cn.pioneeruniverse.dev.feignInterface.DevManageToSystemInterface;
import cn.pioneeruniverse.dev.service.devtask.DevTaskService;
import cn.pioneeruniverse.dev.service.workRemark.workTaskRemark;
import cn.pioneeruniverse.dev.service.worktask.WorkTaskService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


/**
 * 
* @ClassName: WorkTaskController
* @Description: 开发工作任务管理Controller
* @author author
* @date 2020年8月8日 下午6:32:46
*
 */
@RestController
@RequestMapping("worktask")
public class WorkTaskController extends BaseController{
	@Autowired 
	private WorkTaskService workTaskService;
	@Autowired 
	private workTaskRemark workTaskRemark;
	@Autowired
	private DevTaskService devTaskService;
	@Autowired
	private S3Util s3Util;
	@Autowired
	private RedisUtils redisUtils;
	@Autowired
	private DevManageToSystemInterface devManageToSystemInterface;

	/**
	 * shan-根据id获取当前工作任务
	 * @param id
	 * @return
	 */
	@PostMapping("getWorkTask")
	public TblDevTask getWorkTask(Long id){
		TblDevTask devTask = workTaskService.getDevTaskById(id);
		return devTask;
	}
	
	/**
	 * 
	* @Title: toConstruction
	* @Description: 获取所有开发工作任务
	* @author author
	* @param workTask 封装的查询条件
	* @param getProjectIds 多选的项目查询跳进啊
	* @param startDate 创建开始日期
	* @param endDate 创建结束日期
	* @param rows 每页条数
	* @param page 第几页
	* @param request
	* @param sidx 按sidx字段排序，如名称，编号等
	* @param sord 正序倒序排序
	* @return Map<String, Object> key-rows:获取的开发工作任务
    *                 records:总数目
    *                 total: 总页数
    *                 page:第几页
    *                 rwos：不分页情况下返回的数据
	 */
	@PostMapping("getAllWorktask")
	public Map  toConstruction( String workTask, String getProjectIds,String startDate,String endDate,Integer rows,Integer page,HttpServletRequest request, String sidx, String sord){
		Map<String, Object> result = new HashMap<String, Object>();
		TblDevTask tblDevTask=new TblDevTask();
		try {
			 Long Userd=CommonUtil.getCurrentUserId(request);
			if (StringUtils.isNotBlank(workTask)){
				tblDevTask=JSONObject.parseObject(workTask,TblDevTask.class);
			}
			tblDevTask.setSidx(sidx);
			tblDevTask.setSord(sord);
			Long[] projectIds = null;
			if( getProjectIds != null && !getProjectIds.equals("")){
				String[] str = getProjectIds.split(",");
				projectIds = (Long[]) ConvertUtils.convert(str,Long.class);
			}
			if(startDate!=(null)&&!startDate.equals("")) {
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				Date date = sf.parse(startDate);
					 
				Timestamp ts = new Timestamp(date.getTime());
				tblDevTask.setCreateStartDate(ts);
				Date date2 = sf.parse(endDate);
				Timestamp ts2 = new Timestamp(date2.getTime());
				tblDevTask.setCreateEndDate(ts2);
				
			}
			tblDevTask.setId(Userd);
			result=workTaskService.getDevTask(tblDevTask,projectIds, page, rows,request);
		} catch (Exception e) {
			 e.printStackTrace();
		     logger.error("mes:" + e.getMessage(), e);
		}
		return result;
	}
		
		/**
		 * 获取关联开发任务
		 * @param tblDevTask 搜搜参数
		 * @param pageNumber 当前页
		 * @param pageSize   页大小
		 * @param request
		 * @return Map<String, Object> status=1正常返回，2异常返回
		 */
		@RequestMapping(value="getAllFeature")
		public Map<String, Object> getAllFeature(TblDevTask tblDevTask,Integer pageNumber, Integer pageSize,HttpServletRequest request){
			Map<String, Object> result = new HashMap<String, Object>();
			 result.put("status", Constants.ITMP_RETURN_SUCCESS);
			 
			try {
				Long userdId =CommonUtil.getCurrentUserId(request);
				LinkedHashMap map = (LinkedHashMap) redisUtils.get(CommonUtil.getToken(request));
				List<String> roleCodes = (List<String>) map.get("roles"); 
				 List<Map<String, Object>> list = workTaskService.getAllFeature(tblDevTask,userdId,roleCodes, pageNumber, pageSize);
		         List<Map<String, Object>> list2 = workTaskService.getAllFeature(tblDevTask,userdId,roleCodes, null, null);
		         //返回的数据
		         result.put("rows",list );
		         //总条数
		         result.put("total", list2.size());
			} catch (Exception e) {
				 
				 e.printStackTrace();
			     logger.error("mes:" + e.getMessage(), e);
			     return handleException(e, "获取开发任务信息失败");
			}
			return result;
		}

	/**
	 *  获取关联开发任务
	 * @param tblDevTask 搜搜参数
	 * @param pageNumber 当前页
	 * @param pageSize   页大小
	 * @param request
	 * @return Map<String, Object>
	 */
	@RequestMapping(value="getAllFeature1")
	public Map<String, Object> getAllFeature1(TblDevTask tblDevTask,Integer pageNumber, Integer pageSize,HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);

		try {
			Long userdId =CommonUtil.getCurrentUserId(request);
			LinkedHashMap map = (LinkedHashMap) redisUtils.get(CommonUtil.getToken(request));
			List<String> roleCodes = (List<String>) map.get("roles");
			List<Map<String, Object>> list = workTaskService.getAllFeature(tblDevTask,userdId,roleCodes, pageNumber, pageSize);
			List<Map<String, Object>> list2 = workTaskService.getAllFeature(tblDevTask,userdId,roleCodes, null, null);
			result.put("rows",list );
			result.put("total", list2.size());
		} catch (Exception e) {

			e.printStackTrace();
			logger.error("mes:" + e.getMessage(), e);
			return handleException(e, "获取开发任务信息失败");
		}
		return result;
	}
	/**
	 * 添加工作任务
	 * @param objStr 添加数据
	 * @param attachFiles 附件
	 * @param request
	 * @return Map<String, Object>
	 */
	@RequestMapping(value="addDevTask",method=RequestMethod.POST)
	public Map addDevTask(String objStr,String attachFiles,HttpServletRequest request) {
		Map<String, Object>  map= new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			Long Userid=CommonUtil.getCurrentUserId(request);
			String  UserName=CommonUtil.getCurrentUserName(request);
			String UserAccount=CommonUtil.getCurrentUserAccount(request);
			workTaskService.addDevTask(objStr,attachFiles,Userid,request,UserAccount);
//			//添加消息
//			sendAddMessage(request,objStr);
		} catch (Exception e) {
			return super.handleException(e, "添加失败");
		}
		return map;
	}



	/**
	 * 
	* @Title: sendAddMessage
	* @Description: 发送消息 （未用）
	* @author author
	* @param request
	* @param objStr
	* @throws
	 */
	private void sendAddMessage(HttpServletRequest request,String objStr){
		TblDevTask devTask = JSON.parseObject(objStr, TblDevTask.class);
		TblRequirementFeature tblRequirementFeature = new TblRequirementFeature();
		if (devTask.getRequirementFeatureId() != null){
			tblRequirementFeature = workTaskService.selectRequirementFeatureById(devTask.getRequirementFeatureId());
		}
		Map<String,Object> map=new HashMap<>();
		map.put("messageTitle","收到一个新分配的开发工作任务");
		map.put("messageContent",tblRequirementFeature.getFeatureCode()+"|"+devTask.getDevTaskName());
		map.put("messageReceiverScope",2);//指定人
		map.put("messageReceiver",devTask.getDevUserId());
		map.put("projectId",tblRequirementFeature.getProjectId());
		devManageToSystemInterface.insertMessage(JSON.toJSONString(map));

	}

	/**
	 * 
	* @Title: sendTrsMessage
	* @Description: 发送消息
	* @author author
	* @param request
	* @param tblDevTask
	 */
	private void sendTrsMessage(HttpServletRequest request,TblDevTask tblDevTask){

		TblDevTask devTask=workTaskService.getDevTaskById(tblDevTask.getId());
		Map<String,Object> map=new HashMap<>();
		map.put("messageTitle","收到一个新分配的开发工作任务");
		map.put("messageContent",devTask.getDevTaskCode()+"|"+devTask.getDevTaskName());
		map.put("messageReceiverScope",2);//指定人
		map.put("messageReceiver",tblDevTask.getDevUserId());
		devManageToSystemInterface.insertMessage(JSON.toJSONString(map));

	}

	/**
	 * 添加含有缺陷的工作任务任务
	 * @param objStr
	 * @param attachFiles
	 * @param request
	 * @return Map<String, Object>
	 */
		@RequestMapping(value="addDefectDevTask",method=RequestMethod.POST)
		public Map addDefectDevTask(String objStr,String attachFiles,HttpServletRequest request) {
			Map<String, Object>  map= new HashMap<>();
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
			try {
				Long Userid=CommonUtil.getCurrentUserId(request);
				String  UserName=CommonUtil.getCurrentUserName(request);
				String UserAccount=CommonUtil.getCurrentUserAccount(request);
				workTaskService.addDefectDevTask(objStr,attachFiles,Userid,request,UserAccount);
			} catch (Exception e) {
				return super.handleException(e, "添加失败");
			}
			return map;
		}
	/**
	 * 编辑
	 * @param obj 编辑信息
	 * @param attachFiles 添加的附件
	 * @param deleteAttaches 删除得附件
	 * @param request
	 * @return Map<String, Object>
	 */
	@RequestMapping(value="updateDevTask",method=RequestMethod.POST)
	public Map updateDevTask(String obj, String attachFiles,String deleteAttaches,HttpServletRequest request) {
		//发送消息
		TblDevTask newDevTask = JSON.parseObject(obj, TblDevTask.class);
		TblDevTask oldDevTask=workTaskService.getDevTaskById(newDevTask.getId());

		Map<String, Object>  map= new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			List<TblDevTaskAttachement> tblDevTaskAttachement = JsonUtil.fromJson(deleteAttaches, JsonUtil.createCollectionType(ArrayList.class, TblDevTaskAttachement.class));
			if(tblDevTaskAttachement.size()>0) {//是否有删除得附件
				workTaskRemark.updateNo(tblDevTaskAttachement,request);
			}
			
			 
			Long Userid=CommonUtil.getCurrentUserId(request);
			workTaskService.updateDevTask(obj,attachFiles,deleteAttaches,Userid,request);


		} catch (Exception e) {
			return super.handleException(e, "编辑失败");
		}

		//关注的任务有更新发送消息
		TblDevTaskAttention attention = new TblDevTaskAttention();
		attention.setDevTaskId(newDevTask.getId());
		attention.setStatus(1);
		List<TblDevTaskAttention> attentionList = workTaskService.getAttentionList(attention);
		if (attentionList != null && attentionList.size() > 0) {
			//编辑发送消息
			sendEditMessage(request,oldDevTask,newDevTask, attentionList);
		}
		return map;
	}

    /**
     * 
    * @Title: sendEditMessage
    * @Description: 编辑开发工作任务后发送消息
    * @author author
    * @param request
    * @param oldDevTask 编辑前的工作任务
    * @param newDevTask 编辑后的哦工作任务
    * @param attentionList 关注该工作任务列表人员
     */
	private  void sendEditMessage(HttpServletRequest request,TblDevTask oldDevTask, TblDevTask newDevTask, 
			List<TblDevTaskAttention> attentionList){
		String userIds = "";
		for (TblDevTaskAttention attention : attentionList) {
			userIds += attention.getUserId() + ",";
		}
		if(StringUtil.isNotEmpty(userIds)){
			userIds = userIds.substring(0, userIds.length() - 1);
			Map<String,Object> map=new HashMap<>();
			map.put("messageTitle","你关注的开发工作任务有更新");
			map.put("messageContent",oldDevTask.getDevTaskCode()+"|"+newDevTask.getDevTaskName());
			map.put("messageReceiverScope",2);
			map.put("messageReceiver", userIds);
			//消息来源 4--关注开发工作任务
			map.put("messageSource",4);
			map.put("systemId",newDevTask.getSystemId());
			devManageToSystemInterface.insertMessage(JSON.toJSONString(map));//调用添加提示信息接口（个人工作台）
		}
	}


	/**
	 * 
	* @Title: getEditDevTask
	* @Description: 获取具体的工作任务
	* @author author
	* @param id 开发工作任务Id
	* @return Map<String, Object>
	 */
	@RequestMapping(value="getEditDevTask",method=RequestMethod.POST)
	public Map<String,Object> getEditDevTask(String id) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			List<TblDevTaskAttachement> list=workTaskRemark.findAttachement(Long.parseLong(id));
			result=workTaskService.getEditDevTask(id);
			result.put("attachements", list);

			//获取扩展字段
			List<ExtendedField> extendedFields=workTaskService.findFieldByDevId(Long.parseLong(id));
			result.put("fields", extendedFields);

		} catch (Exception e) {
			result.put("status", "2");
			return super.handleException(e, "获取工作任务信息失败");
		}
		return result;
	}
	
	/**
	 * 根据当前用户 获取项目组成员
	 * @param request
	 * @return List<TblDevTask>
	 */
	@RequestMapping(value="getAllDevUser")
	public List<TblDevTask> getAllDevUser(HttpServletRequest request){
		Long id=CommonUtil.getCurrentUserId(request);
		List<TblDevTask> list=workTaskService.getAllDevUser(id);
		return list;
	}


    /**
     * 
    * @Title: getDevTaskFiled
    * @Description: 工作任务扩展字段
    * @author author
    * @param request
    * @return Map<String,Object>
     */
	@RequestMapping(value="getDevTaskFiled")
	public  Map<String,Object> getDevTaskFiled(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();

		//扩展字段
		List<ExtendedField> extendedFields=workTaskService.findFieldByDevId(null);
		map.put("fields", extendedFields);
		return  map;

	}

	
	/**
	 * 查看工作任务详情
	 * @param request
	 * @param id 
	 * @param requirementFeatureId 开发任务ID
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="getSeeDetail",method=RequestMethod.POST)
	public Map<String,Object> getSeeDetail(HttpServletRequest request, String id,Long requirementFeatureId){
		List<TblDevTaskRemark> list= new ArrayList<>();
		List<TblDevTaskLogAttachement> listLogAttachement= new ArrayList<>();
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			result=workTaskService.getSeeDetail(id);
			//开发任务id
			list=workTaskRemark.selectRemark(id);
			List<Long> idRemark = new ArrayList<Long>(list.size());
			List<TblDevTaskRemarkAttachement> list2= new ArrayList<>();
			for(int i=0;i<list.size();i++) {
				idRemark.add(list.get(i).getId());
			}
			if(idRemark.size()>0) {//如果有备注信息查询备注附件
				list2=workTaskRemark.findTaskRemarkAttchement(idRemark);
			}
			List<TblDevTaskLog>  logsList= workTaskRemark.findLogList(Long.parseLong(id));
			List<Long> idLog = new ArrayList<Long>(list.size());
			for(int j=0;j<logsList.size();j++) {
				idLog.add(logsList.get(j).getId());
			}
			if(idLog.size()>0) {
				listLogAttachement=workTaskRemark.findLogAttachement(idLog);
			}
			List<TblDevTaskAttachement> listfile=workTaskRemark.findAttachement(Long.parseLong(id));
			Map<String, Object> devTasks = devTaskService.getOneDevTask(requirementFeatureId);

			//查询关注功能
			Long userId = CommonUtil.getCurrentUserId(request);
			TblDevTaskAttention attention = new TblDevTaskAttention();
			attention.setDevTaskId(Long.parseLong(id));
			attention.setUserId(userId);
			attention.setStatus(1);
			List<TblDevTaskAttention> attentionList = workTaskService.getAttentionList(attention);
			if (attentionList != null && attentionList.size() > 0) {
				result.put("attentionStatus", 1);
			} else {
				result.put("attentionStatus", 2);
			}

			//增加扩展字段

			List<ExtendedField> extendedFields=workTaskService.findFieldByDevId(Long.parseLong(id));
			result.put("fields", extendedFields);

			//附件
			result.put("attachements", listfile);
			//日志
			result.put("logs", logsList);
			//备注附件
			result.put("Attchement", list2);
			//备注
			result.put("rmark", list);
			//日志附件
			result.put("logAttachement",listLogAttachement);
			//工作任务
			result.put("devTasks",devTasks);

		} catch (Exception e) {
			return super.handleException(e, "查询信息失败");
		}
		
		
		return result;
		
	}
	
	//更新开发任务关注功能
	@RequestMapping(value = "changeAttention", method = RequestMethod.POST)
	public Map<String, Object> changeAttention(HttpServletRequest request, Long id, Integer attentionStatus) {
		Map<String, Object> map = new HashMap<>();
		map.put("status", "success");
		try {
			if (attentionStatus != null) {
				workTaskService.changeAttention(id, attentionStatus, request);
			}
		} catch (Exception e) {
			return super.handleException(e, "更新关注失败");
		}
		return map;
	}
	
	/**
	 * 待处理
	 * @param handle 处理数据
	 * @param DHattachFiles 附件
	 * @param deleteAttaches 删除附件
	 * @param request
	 */
	@RequestMapping(value="DHandleDev",method=RequestMethod.POST)
	public void DHandleDev(String handle,String DHattachFiles,String deleteAttaches,HttpServletRequest request){
		List<TblDevTaskAttachement> tblDevTaskAttachement = JsonUtil.fromJson(deleteAttaches, JsonUtil.createCollectionType(ArrayList.class, TblDevTaskAttachement.class));
		if(tblDevTaskAttachement.size()>0) {//附件信息
			workTaskRemark.updateNo(tblDevTaskAttachement,request);
		}
		workTaskService.DHandle(handle,DHattachFiles,deleteAttaches,request);
	}
	/**
	 * 处理中
	 * @param handle 处理数据
	 * @param HattachFiles 新增附件
	 * @param deleteAttaches 删除附件
	 * @param request
	 */
	@RequestMapping(value="HandleDev",method=RequestMethod.POST)
	public void HandleDev(String handle,String HattachFiles,String deleteAttaches,HttpServletRequest request){
		List<TblDevTaskAttachement> tblDevTaskAttachement = JsonUtil.fromJson(deleteAttaches, JsonUtil.createCollectionType(ArrayList.class, TblDevTaskAttachement.class));
		if(tblDevTaskAttachement.size()>0) {
			workTaskRemark.updateNo(tblDevTaskAttachement,request);
		}
        TblDevTask devTask = JSON.parseObject(handle, TblDevTask.class);
		devTask.setDevTaskStatus(3);
		workTaskService.Handle(devTask,HattachFiles,deleteAttaches,request);
	}
	/**
	 * 代码评审
	 * @param handle 处理数据
	 * @param HattachFiles 新增附件
	 * @param deleteAttaches 删除附件
	 * @param request
	 */
	@RequestMapping(value="CodeHandleDev",method=RequestMethod.POST)
	public void CodeHandleDev(String handle,String HattachFiles,String deleteAttaches,HttpServletRequest request){
		List<TblDevTaskAttachement> tblDevTaskAttachement = JsonUtil.fromJson(deleteAttaches, JsonUtil.createCollectionType(ArrayList.class, TblDevTaskAttachement.class));
		if(tblDevTaskAttachement.size()>0) {
			workTaskRemark.updateNo(tblDevTaskAttachement,request);
		}
		workTaskService.CodeHandle(handle,HattachFiles,deleteAttaches, request);
	}
	/**
	 * 代码评审通过
	 * @param id
	 * @param adoptFiles 新增附件
	 * @param deleteAttaches 删除附件
	 * @param request
	 */
	@RequestMapping(value="reviewAdopt",method=RequestMethod.POST)
	public void reviewAdopt(String id,String adoptFiles,String deleteAttaches,HttpServletRequest request){
		workTaskService.reviewAdopt(id,adoptFiles,deleteAttaches,request);
	}
	/**
	 * 代码评审未通过
	 * @param id
	 * @param adoptFiles 新增附件
	 * @param deleteAttaches 删除附件
	 * @param request
	 */
	@RequestMapping(value="reviewNAdopt",method=RequestMethod.POST)
	public void reviewNAdopt(String id,String adoptFiles,String deleteAttaches,HttpServletRequest request){
		List<TblDevTaskAttachement> tblDevTaskAttachement = JsonUtil.fromJson(deleteAttaches, JsonUtil.createCollectionType(ArrayList.class, TblDevTaskAttachement.class));
		if(tblDevTaskAttachement.size()>0) {
			workTaskRemark.updateNo(tblDevTaskAttachement,request);
		}
		workTaskService.reviewNAdopt(id,adoptFiles,deleteAttaches,request);
	}
	/**
	 * 分派
	 * @param assig 分派信息 
	 * @param Remark 备注
	 * @param request
	 * @return
	 */
	@RequestMapping(value="assigDev",method=RequestMethod.POST)
	public Map assigDev(String assig,String Remark,HttpServletRequest request){
		 Map<String, Object> result = new HashMap<String, Object>();
		 result.put("status", "1");
		try {
			TblDevTask devTaskSend = new TblDevTask();
			devTaskSend = JSON.parseObject(assig, TblDevTask.class);

			workTaskService.assigDev(assig,Remark,request);
			sendTrsMessage(request,devTaskSend);
			TblDevTask devTask = workTaskService.getDevTaskById(devTaskSend.getId());
			if (devTask!=null) {
				devTaskSend.setDevTaskCode(devTask.getDevTaskCode());
				devTaskSend.setDevTaskName(devTask.getDevTaskName());
				workTaskService.sendWorkTaskMassage(devTaskSend);// 发送消息
			}
		} catch (Exception e) {
			result.put("status", "2");
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 
	* @Title: getSystem
	* @Description: 分页展示关联系统弹框内容
	* @author author
	* @param workTask
	* @param rows 每页大小
	* @param page 第几页
	* @param request
	* @return Map<String,Object> 
	 */
	@RequestMapping(value="getSystem",method=RequestMethod.POST)
	public Map<String,Object> getSystem(String workTask,Integer rows,Integer page,HttpServletRequest request){
		Long userId=CommonUtil.getCurrentUserId(request);
		  Map<String, Object> result = new HashMap<String, Object>();
		TblSystemInfo systemInfo=new TblSystemInfo();
		if (StringUtils.isNotBlank(workTask)) {
			systemInfo=JSONObject.parseObject(workTask,TblSystemInfo.class);
		}
			systemInfo.setId(userId);
			 result=workTaskService.getAllsystem(systemInfo, page, rows);
		return  result;
	}
	
	/**
	 * 
	* @Title: getRequirement
	* @Description: 分页关联需求弹框内容
	* @author author
	* @param workTask 开发工作任务
	* @param rows 每页大小
	* @param page 第几页
	* @param request
	* @return Map<String,Object>
	 */
	@RequestMapping(value="Requirement",method=RequestMethod.POST)
	public Map<String,Object> getRequirement(String workTask,Integer rows,Integer page,HttpServletRequest request){
		Long userId=CommonUtil.getCurrentUserId(request);
		  Map<String, Object> result = new HashMap<String, Object>();
		TblRequirementInfo rquirement=new TblRequirementInfo();
		if (StringUtils.isNotBlank(workTask)) {
			rquirement=JSONObject.parseObject(workTask,TblRequirementInfo.class);
		} 
			
			rquirement.setId(userId);
			result=workTaskService.getAllRequirt(rquirement, page, rows);
		return  result;
	}

	/**
	 * 查询当前登陆人所属的项目
	 * @param request
	 * @return Map<String,Object>
	 */
	@PostMapping("findProjectByUser")
	public Map<String, Object>  findProjectByUser( HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			List<TblProjectInfo> list = workTaskService.findProjectByUser(request);
			result.put("data",list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("mes:" + e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 导出
	 * @param excelDate
	 * @param response
	 * @param request
	 */
	@ResponseBody
	@RequestMapping(value="getExcelAllWork")
	public void getExcelUser(String excelDate,String getProjectIds,String startDate,String endDate,HttpServletResponse response,HttpServletRequest request){
		List<TblDevTask> list = new ArrayList<TblDevTask>();
		TblDevTask tblDevTask=new TblDevTask();
		  List<Integer> Array=new ArrayList();
		try {
			 Long Userd=CommonUtil.getCurrentUserId(request);
			if (StringUtils.isNotBlank(excelDate)) {
				tblDevTask=JSONObject.parseObject(excelDate,TblDevTask.class);
			}

				tblDevTask.setId(Userd);
				
				   if(tblDevTask.getDevTaskStatus()!=null) {
		            	String devStatus=tblDevTask.getDevTaskStatus().toString();
		                if(devStatus.length()==1) {
		                	Array.add(Integer.parseInt(devStatus));
		                	tblDevTask.setDevStatus(Array);
		                }else {
		                	for(int i=0;i<devStatus.length();i++) {
		                		String status=devStatus.substring(i,i+1);
		                		Array.add(Integer.parseInt(status));
		                	}
		                	tblDevTask.setDevStatus(Array);
		                }
		            }
				   SimpleDateFormat sformat=new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
					  long now=System.currentTimeMillis();
					String day=sformat.format(now);
					String fileName = "工作任务表"+day+".xlsx";


					Long[] projectIds = null;
					if( getProjectIds != null && !getProjectIds.equals("")){
						String[] str = getProjectIds.split(",");
						projectIds = (Long[]) ConvertUtils.convert(str,Long.class);
					}



            List<ExtendedField> extendedFields=workTaskService.findFieldByDevId(null);
            List<ExtendedField> extendedFieldsNew=new ArrayList<>();
            List<String> filedNames=new ArrayList<>();

            for(ExtendedField extendedField:extendedFields){
                if(extendedField.getStatus().equals("1")){
                    extendedFieldsNew.add(extendedField);
                    filedNames.add(extendedField.getFieldName());
                }
            }

            if(startDate!=(null)&&!startDate.equals(""))  {
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				Date date = sf.parse(startDate);
					 
				Timestamp ts = new Timestamp(date.getTime());
				tblDevTask.setCreateStartDate(ts);
				Date date2 = sf.parse(endDate);
				Timestamp ts2 = new Timestamp(date2.getTime());
				tblDevTask.setCreateEndDate(ts2);
				
			}
            list=workTaskService.getExcelAllWork(tblDevTask,projectIds,request);


			//new ExportExcel("", TblDevTask.class).setDataList(list).write(response, fileName).dispose();
            new ExportExcel("", TblDevTask.class,extendedFieldsNew).setDataListNew(list,filedNames).write(response, fileName).dispose();

		} catch (Exception e) {
			e.printStackTrace();
		     logger.error("mes:" + e.getMessage(), e);
		}
	}
	
    
  //获取状态
  	@RequestMapping(value="DevStatus")
  	public Map DevStatus(){
  		Map<String, Object>  map=workTaskService.devStatus();
  		return map;
  	}
  	
  	 //获取需求
  	@RequestMapping(value="ReqStatus")
  	public Map ReqStatus(){
  		Map<String, Object>  map=workTaskService.ReqStatus();
  		return map;
  	}
  	//获取系统类型
  	@RequestMapping(value="ReqSystem")
  	public Map ReqSystem(){
  		Map<String, Object>  map=workTaskService.ReqSystem();
  		return map;
  	}
	//获取系统类型
  	@RequestMapping(value="FeatureStatus")
  	public Map FeatureStatus(){
  		Map<String, Object>  map=workTaskService.FeatureStatus();
  		return map;
  	}
  	
  	/**
  	 * 下载文件
  	 * @param request
  	 * @param response
  	 * @param fileS3Bucket
  	 * @param fileS3Key
  	 * @param fileNameOld
  	 */
  	@RequestMapping(value = "downloadFile")
  	public void downloadFile(HttpServletRequest request, HttpServletResponse response,String fileS3Bucket,String fileS3Key,String fileNameOld) {
  		try {
  			if(!StringUtils.isBlank(fileS3Bucket)&&!StringUtils.isBlank(fileS3Key)&&!StringUtils.isBlank(fileNameOld)) {
  				s3Util.downObject(fileS3Bucket, fileS3Key,fileNameOld, response);
  			}
  			} catch (Exception e) {
  				e.printStackTrace();
  				logger.error("mes:" + e.getMessage(), e);
  			}
  			
  		}
  	/**
  	 * 添加备注
  	 * @param remark
  	 * @param attachFiles
  	 * @param request
  	 */
  	@PostMapping(value="addTaskRemark")
  	public void addTaskRemark(String remark,String attachFiles,HttpServletRequest request) {
  		
  		List<TblDevTaskRemarkAttachement> files = JsonUtil.fromJson(attachFiles, JsonUtil.createCollectionType(ArrayList.class,TblDevTaskRemarkAttachement.class));
  		Long Userid=CommonUtil.getCurrentUserId(request);
  		String userName=CommonUtil.getCurrentUserName(request);
  		String UserAccount=CommonUtil.getCurrentUserAccount(request);
  		TblDevTaskRemark tblTestTaskRemark=JSON.parseObject(remark, TblDevTaskRemark.class);
  		workTaskRemark.addTaskRemark(remark,Userid,userName,UserAccount,files);
  		
  	}
  	 public static String getFileNameNoEx(String filename) { 
         if ((filename != null) && (filename.length() > 0)) { 
             int dot = filename.lastIndexOf('.'); 
             if ((dot >-1) && (dot < (filename.length()))) { 
                 return filename.substring(0, dot); 
             } 
         } 
         return filename; 
     }
  	 	/**
  	 	 * 文件上传 
  	 	 * @param files
  	 	 * @return
  	 	 * @throws Exception
  	 	 */
		@RequestMapping(value="uploadFile")
		public List<Map<String,Object>> uploadFile(@RequestParam ("files") MultipartFile[] files) throws Exception {
			List<Map<String,Object>> attinfos = new ArrayList<Map<String,Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			try {
				if (files.length > 0 && files !=null) {
					for (MultipartFile file : files) {
						String OldfileName=getFileNameNoEx(file.getOriginalFilename());
						if (!file.isEmpty()) {
							InputStream inputStream = file.getInputStream();
							map = new HashMap<String, Object>();
							String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);//后缀名
							String newFileName = UUID.randomUUID().toString().replace("-", "");
							
							Random random = new Random();
							String i = String.valueOf(random.nextInt());
							String keyname = s3Util.putObject(s3Util.getDevTaskBucket(),i,inputStream);
							map.put("fileS3Key", keyname);
							map.put("fileS3Bucket", s3Util.getDevTaskBucket());
							
							map.put("fileNameNew", newFileName);//newFileName
							//map.put("filePath", url);//url
							map.put("fileNameOld",file.getOriginalFilename());//filename
							//map.put("length", file.getSize());
							map.put("fileType",extension );//extension
							
							attinfos.add(map);
						}else {
							//文件文件为空
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return attinfos;
		}
		/**
		 * 删除附件
		 * @param attacheId
		 * @param request
		 * @return
		 */
		@RequestMapping(value="delFile",method=RequestMethod.POST)
		public Map delFile(String attacheId,HttpServletRequest request) {
			Map<String,Object> result = new HashMap<String,Object>();
			try {
				if(!attacheId.equals("")||!attacheId.equals(null)) {
					
					/*workTaskRemark.updateNo(Long.parseLong(attacheId),request);*/
					result.put("status", "success");
				}
			} catch (Exception e) {
				e.printStackTrace();
				result.put("status", "fail");
			}
			
			
			return result;
			
		}
		
	/**
	 * 根据开发任务的systemId 统计该系统，待开发、开发中的工作量统计，显示列如：姓名、工单数量、预计工作量
	 * 
	 * add by ztt
	 * */
	@RequestMapping("countWorkloadBysystemId")
	public Map<String, Object> countWorkloadBysystemId(Long systemId){
		Map<String, Object> map = new HashMap<>();
		try {
			List<Map<String, Object>> countDatas =  workTaskService.countWorkloadBysystemId(systemId);
			map.put("countDatas", countDatas);
		} catch (Exception e) {
			return super.handleException(e, "统计该系统下的待开发、开发中的工作量失败！");
		}
		return map;
	}

	/**
	 * 处理生产缺陷的工作任务
	 * @param defect
	 * @param workTask
	 * @param request
	 * @return
	 */
	@RequestMapping(value="addDevTask1",method=RequestMethod.POST)
	public Map addDevTask1(String defect,String workTask,String attachFiles,HttpServletRequest request) {
		Map<String, Object>  map= new HashMap<>();
		try {
			workTaskService.addDefectDevTask1(defect,workTask,attachFiles,request);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			return super.handleException(e, "添加失败");
		}
		return map;
	}

	/**
	*@author liushan
	*@Description 同步jira附件
	*@Date 2020/5/11
	*@return java.util.Map
	**/
	@RequestMapping(value="jiraFileByCode",method=RequestMethod.POST)
	public Map jiraFileByCode(String workCodeJiraIds,@RequestParam(required = false) String key,HttpServletRequest request) {
		Map<String, Object>  map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			Set<String> code = workTaskService.jiraFileByCode(workCodeJiraIds,request);
			map.put("code", code);

			// 结果保存到redis
			Set<String> redisCode = new HashSet<>();
			redisCode.addAll(code);
			if(StringUtils.isEmpty(key)){
				key = CommonUtil.getCurrentUserId(request) +"-"+ DateUtil.getDateString(new Timestamp(System.currentTimeMillis()),Constants.EXPORT_EXCEL_TIME_FORMAT);
			} else {
                Set<String> redis_Code = new Gson().fromJson(redisUtils.get(key).toString(),
						new TypeToken<Set<String>>(){}.getType());
                redisCode.addAll(redis_Code);
			}
			redisUtils.set(key,new Gson().toJson(redisCode),Constants.ITMP_TOKEN_TIMEOUT);
			map.put("key", key);
		} catch (Exception e) {
			return super.handleException(e, "操作失败");
		}
		return map;
	}

}
