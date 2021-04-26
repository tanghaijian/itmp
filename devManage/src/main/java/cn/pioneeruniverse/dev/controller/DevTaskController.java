package cn.pioneeruniverse.dev.controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.common.utils.BrowserUtil;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.ExportExcel;
import cn.pioneeruniverse.common.utils.JsonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.common.utils.S3Util;
import cn.pioneeruniverse.dev.dao.mybatis.TblCommissioningWindowMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementFeatureMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemInfoMapper;
import cn.pioneeruniverse.dev.entity.DevTaskVo;
import cn.pioneeruniverse.dev.entity.ExtendedField;
import cn.pioneeruniverse.dev.entity.TblCommissioningWindow;
import cn.pioneeruniverse.dev.entity.TblDataDic;
import cn.pioneeruniverse.dev.entity.TblDefectInfo;
import cn.pioneeruniverse.dev.entity.TblDevTask;
import cn.pioneeruniverse.dev.entity.TblProjectPlan;
import cn.pioneeruniverse.dev.entity.TblRequirementFeature;
import cn.pioneeruniverse.dev.entity.TblRequirementFeatureAttachement;
import cn.pioneeruniverse.dev.entity.TblRequirementFeatureAttention;
import cn.pioneeruniverse.dev.entity.TblRequirementFeatureLog;
import cn.pioneeruniverse.dev.entity.TblRequirementFeatureRemark;
import cn.pioneeruniverse.dev.entity.TblRequirementFeatureRemarkAttachement;
import cn.pioneeruniverse.dev.entity.TblRequirementFeatureTimeTrace;
import cn.pioneeruniverse.dev.entity.TblSprintInfo;
import cn.pioneeruniverse.dev.entity.TblSystemInfo;
import cn.pioneeruniverse.dev.feignInterface.DevManageToSystemInterface;
import cn.pioneeruniverse.dev.service.devtask.DevTaskService;
import cn.pioneeruniverse.dev.service.sprint.SprintInfoService;
import cn.pioneeruniverse.dev.service.systeminfo.ISystemInfoService;
import cn.pioneeruniverse.dev.service.worktask.impl.WorkTaskServiceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.StringUtil;


/**
 * 
* @ClassName: DevTaskController
* @Description: 开发任务管理Controller
* @author author
* @date 2020年8月7日 下午5:32:19
*
 */
@RestController
@RequestMapping("devtask")
public class DevTaskController extends BaseController {
	@Autowired
	private TblCommissioningWindowMapper commissioningWindowMapper;
	@Autowired
	private DevTaskService devTaskService;
	@Autowired
	private ISystemInfoService systemInfoService;
	@Autowired
	private RedisUtils redisUtils;
	@Autowired
	private WorkTaskServiceImpl workTaskServiceImpl;
	@Autowired
	private S3Util s3Util;
	@Autowired
	private TblRequirementFeatureMapper requirementFeatureMapper;
	@Autowired
	private DevManageToSystemInterface devManageToSystemInterface;
	@Autowired
	private TblSystemInfoMapper tblSystemInfoMapper;
	@Autowired
	private SprintInfoService sprintInfoService;


    /**
     * 
    * @Title: getAllDevTask
    * @Description: 获取所有开发任务(停用)
    * @author author
    * @param requirementFeature：封装的查询条件
    * @param page 第几页
    * @param rows 每页条数
    * @param request
    * @return Map<String, Object>
     */
	@RequestMapping(value = "getAllDevTask", method = RequestMethod.POST)
	public Map<String, Object> getAllDevTask(TblRequirementFeature requirementFeature, Integer page, Integer rows,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		Long uid = CommonUtil.getCurrentUserId(request);
		List<DevTaskVo> reqFeatures = devTaskService.getAllReqFeature(requirementFeature, uid,page, rows);
		int total = devTaskService.getAllReqFeatureCount(requirementFeature, uid);
		//返回的数据
		map.put("rows", reqFeatures);
		//总数量
		map.put("records", total);
		//总页数
		map.put("total", total%rows == 0 ? total/rows:total/rows+1 );
		//第几页
		map.put("page", page);
		return map;
	}


	//导出模板
	//@RequestMapping(value="exportTemplet")
//	public Map<String, Object> exportTemplet(HttpServletRequest request, HttpServletResponse response){
//		HashMap<String, Object> map = new HashMap<>();
//		map.put("status", Constants.ITMP_RETURN_SUCCESS);
////		try {
////			devTaskService.exportTemplet(request, response);
////		} catch (Exception e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////			logger.error("mes:" + e.getMessage(), e);
////		}
//
//		//获取文件的路径
//		String excelPath = request.getSession().getServletContext().getRealPath("/Excel/"+"xx.xls");
//		String fileName = "xx.xls".toString(); // 文件的默认保存名
//		// 读到流中
//		InputStream inStream = new FileInputStream(excelPath);//文件的存放路径
//		// 设置输出的格式
//		response.reset();
//		response.setContentType("bin");
//		response.addHeader("Content-Disposition",
//				"attachment;filename=" + URLEncoder.encode("xx.xls", "UTF-8"));
//		// 循环取出流中的数据
//		byte[] b = new byte[200];
//		int len;
//
//		while ((len = inStream.read(b)) > 0){
//			response.getOutputStream().write(b, 0, len);
//		}
//		inStream.close();
//
//	}


    /**
     * 
    * @Title: getAllDevTask2
    * @Description: 获取所有开发任务，查询列表
    * @author author
    * @param devTaskVo 封装的查询条件
    * @param startDate 创建开始日期
    * @param endDate 创建结束日期
    * @param request
    * @param response
    * @return JqGridPage<DevTaskVo>
     */
	@RequestMapping(value = "getAllDevTask2", method = RequestMethod.POST)
	public JqGridPage<DevTaskVo> getAllDevTask2(DevTaskVo devTaskVo,String startDate,String endDate, HttpServletRequest request,
			HttpServletResponse response) {
		Long uid = CommonUtil.getCurrentUserId(request);
		devTaskVo.setUserId(uid);
		LinkedHashMap map = (LinkedHashMap) redisUtils.get(CommonUtil.getToken(request));
		List<String> roleCodes = (List<String>) map.get("roles"); 
		if(startDate!=(null)&&!startDate.equals(""))  {
			
			  try {
				  SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				  Date date = sf.parse(startDate);
				 
				  Timestamp ts = new Timestamp(date.getTime());
				  devTaskVo.setCreateStartDate(ts);
				  Date date2 = sf.parse(endDate);
				  Timestamp ts2 = new Timestamp(date2.getTime());
				  devTaskVo.setCreateEndDate(ts2);
					
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		JqGridPage<DevTaskVo> jqGridPage = devTaskService.selectAll(new JqGridPage<DevTaskVo>(request, response),
				devTaskVo,roleCodes);
		return jqGridPage;
	}

	/**
	 * 
	* @Title: findDeployByReqFeatureId
	* @Description: 获取开发任务的部署状态
	* @author author
	* @param featureId 开发任务id
	* @return Map<String , Object>
	 */
	@RequestMapping(value = "findDeployByReqFeatureId", method = RequestMethod.POST)
	public Map<String , Object> findDeployByReqFeatureId(Long featureId) {
		Map<String , Object> map = new HashMap<>();
		try {
			String deployName = devTaskService.findDeployByReqFeatureId(featureId);
			map.put("deployName", deployName);
		} catch (Exception e) {
			return super.handleException(e, "获取数据失败！");
		}
		return map;
	}

	/**
	 * 获取不同投产窗口的兄弟任务，即关联了同一投产窗口的任务
	 * @param devTaskVo
	 * @return Map<String, Object>
	 */
	@RequestMapping("getBrotherDiffWindow")
	public Map<String, Object> getBrotherDiffWindow(DevTaskVo devTaskVo){
		Map<String , Object> map = new HashMap<>();
		//查询同需求下的任务是否都没有投产窗口，都没有就不查询提示
		List<Long> list = requirementFeatureMapper.findWindowByReqId(devTaskVo.getRequirementId());
		List<Long> list2 = new ArrayList<>();
		for (Long windowId : list) {
			if(windowId != null) {
				list2.add(windowId);
			}
		}
		if(list2 != null && list2.size() > 0) {
			List<Map<String, Object>> brothers = requirementFeatureMapper.findBrotherDiffWindow(devTaskVo);
			map.put("brothers", brothers);
		}
		return map;
	}
	// 获取一条开发任务信息及关联缺陷 及附件
	@RequestMapping(value = "getOneDevTask3", method = RequestMethod.POST)
	public Map<String, Object> getOneDevTask3(Long id) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = devTaskService.getOneDevTask(id);
			map.putAll(toAddData());
			if (map.containsKey("createType")&&"1".equals(map.get("createType").toString())) {//自建任务去查询附件
				// 查询附件
				List<TblRequirementFeatureAttachement> attachements = devTaskService.findAtt(id);
				map.put("attachements", attachements);
			}
			//查询关联的缺陷
			List<TblDefectInfo> defectInfos = devTaskService.findDftByReqFId(id);

			//查询关联字段(单个字段)
			List<ExtendedField> extendedFields=devTaskService.findFieldByReqId(id);
			map.put("fields", extendedFields);

			if (map.get("systemId") != null){
				String systemId=map.get("systemId").toString();
				TblSystemInfo tblSystemInfo=systemInfoService.getOneSystemInfo(Long.parseLong(systemId));
				//系统编码
				map.put("systemCode",tblSystemInfo.getSystemCode());
				//系统名称
				map.put("systemName",tblSystemInfo.getSystemName());
				//代码评审状态
				map.put("systemCodeReviewStatus",tblSystemInfo.getCodeReviewStatus());
			}

			//缺陷信息
			map.put("defectInfos", defectInfos);

		} catch (Exception e) {
			return super.handleException(e, "获取数据失败！");
		}
		
		return map;
	}

	/**
	*@author liushan
	*@Description 查询开发任务关联的系统，所属的工作任务是否有未评审数据
	*@Date 2020/3/6
	*@Param [id]
	*@return java.util.Map<java.lang.String,java.lang.Object>
	**/
	@RequestMapping(value = "selectDevTaskByReqFeatureIds", method = RequestMethod.POST)
	public Map<String, Object> selectDevTaskByReqFeatureIds(Long[] ids) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			// 工作任务
			List<TblDevTask> list = devTaskService.selectDevTaskByReqFeatureIds(ids);
			result.put("devTasks", list);
			// 代码评审管理地址
			if(list != null && list.size() > 0 ){
				result.put("codeReViewManage", devManageToSystemInterface.getMenuByCode("devManageui:codeReviewControl:toCodeReViewManage"));
			}
		} catch (Exception e) {
			return handleException(e, "操作失败！");
		}
		return result;
	}
		
	// 获取一条开发任务信息 及 兄弟开发任务 及 下属的工作任务 及附件
	@RequestMapping(value = "getOneDevTask", method = RequestMethod.POST)
	public Map<String, Object> getOneDevTask(Long id) {
	//	Map<String, Object> map = getOneDevTask3(id);
		Map<String, Object> map = devTaskService.getOneDevTask(id);
		if (map.containsKey("createType")&&"1".equals(map.get("createType").toString())) {//自建任务去查询附件
			// 查询附件
			List<TblRequirementFeatureAttachement> attachements = devTaskService.findAtt(id);
			map.put("attachements", attachements);
		}
		//查询关联的缺陷
		List<TblDefectInfo> defectInfos = devTaskService.findDftByReqFId(id);
		map.put("defectInfos", defectInfos);
		List<Map<String, Object>> devTasks = devTaskService.findByReqFeature(id);
		if(devTasks != null && devTasks.size() > 0 ){
			// 代码评审菜单地址
			map.put("codeReViewManage", devManageToSystemInterface.getMenuByCode("devManageui:codeReviewControl:toCodeReViewManage"));
		}
		map.put("list", devTasks);
		// 查询兄弟开发任务
		if (map.containsKey("requirementId")) {
			Long requirementId = (Long) map.get("requirementId");
			List<Map<String, Object>> brotherReqFeatures = devTaskService.findBrother(requirementId, id);
			map.put("brother", brotherReqFeatures);
		}
		return map;
	}
	
	// 获取一条开发任务信息  兄弟开发任务  下属的工作任务 附件 备注 日志
	@RequestMapping(value = "getOneDevTask2", method = RequestMethod.POST)
	public Map<String, Object> getOneDevTask2(HttpServletRequest request, Long id) {
		Map<String, Object> map = getOneDevTask(id);
		//获取树名称
		if(map.get("assetSystemTreeId")!=null) {
			map.put("systemTreeName",devTaskService.selectTreeName(map.get("assetSystemTreeId").toString()));
		  //map.put("systemTreeName",getTreeName(map.get("assetSystemTreeId").toString()));
		}


		// 查询备注及备注附件
		List<TblRequirementFeatureRemark> remarks = devTaskService.findRemark(id);
		map.put("remarks", remarks);
		// 查询处理日志及附件
		List<TblRequirementFeatureLog> logs = devTaskService.findLog(id);
		map.put("logs", logs);
		//查询关联字段(单个字段)
		List<ExtendedField> extendedFields=devTaskService.findFieldByReqId(id);
		map.put("fields", extendedFields);
		
		//查询关注功能
		Long userId = CommonUtil.getCurrentUserId(request);
		TblRequirementFeatureAttention attention = new TblRequirementFeatureAttention();
		attention.setRequirementFeatureId(id);
		attention.setUserId(userId);
		attention.setStatus(1);
		List<TblRequirementFeatureAttention> attentionList = devTaskService.getAttentionList(attention);
		if (attentionList != null && attentionList.size() > 0) {
			map.put("attentionStatus", 1);
		} else {
			map.put("attentionStatus", 2);
		}

		return map;
	}
	
	//更新开发任务关注功能
	@RequestMapping(value = "changeAttention", method = RequestMethod.POST)
	public Map<String, Object> changeAttention(HttpServletRequest request, Long id, Integer attentionStatus) {
		Map<String, Object> map = new HashMap<>();
		map.put("status", "success");
		try {
			if (attentionStatus != null) {
				devTaskService.changeAttention(id, attentionStatus, request);
			}
		} catch (Exception e) {
			return super.handleException(e, "更新关注失败");
		}
		return map;
	}

	/**
	 * 
	* @Title: toAddData
	* @Description: 获取数据字典信息和开发任务自定义扩展字段
	* @author author
	* @return Map<String,Object>
	 */
	@RequestMapping(value = "toAddData", method = RequestMethod.POST)
	public Map<String, Object> toAddData() {
		Map<String, Object> map = new HashMap<>();
		try {
			//List<TblCommissioningWindow> windows = devTaskService.getWindows();
			//map.put("cmmitWindows", windows);
			String termCode = "TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_SOURCE";
			List<TblDataDic> dataDics = getDataFromRedis(termCode);
			map.put("dics", dataDics);
			List<TblDataDic> reqFeaturePriorityList = getDataFromRedis("TBL_REQUIREMENT_FEATURE_PRIORITY");
			map.put("reqFeaturePriorityList", reqFeaturePriorityList);

			List<ExtendedField> extendedFields=devTaskService.findFieldByReqId(null);
			map.put("fields", extendedFields);


		} catch (Exception e) {
			return super.handleException(e, "查询数据失败！");
		}
		return map;
	}
	/**
	 * 添加开发任务
	 * @param request
	 * @param requirementFeature 开发任务
	 * @param startDate 计划开始日期
	 * @param endDate 计划结束日期
	 * @param attachFiles 附件
	 * @param defectIds 关联的缺陷id
	 * @param dftJsonString 缺陷信息
	 * @param dftActualWorkload 实际工作量
	 * @param defectRemark 缺陷备注
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "addDevTask", method = RequestMethod.POST)
	public Map<String, Object> addDevTask(HttpServletRequest request, TblRequirementFeature requirementFeature,
			String startDate, String endDate, String attachFiles,String defectIds,String dftJsonString,Double dftActualWorkload,String defectRemark) {
		Map<String, Object> map = new HashMap<>();
		List<TblRequirementFeatureAttachement> files = JsonUtil.fromJson(attachFiles,
				JsonUtil.createCollectionType(ArrayList.class, TblRequirementFeatureAttachement.class));
		DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (startDate != null && !"".equals(startDate)) {
				Date planStartDate = dFormat.parse(startDate);
				requirementFeature.setPlanStartDate(planStartDate);
			}
			if (endDate != null && !"".equals(endDate)) {
				Date planEndDate = dFormat.parse(endDate);
				requirementFeature.setPlanEndDate(planEndDate);
			}
			requirementFeature.setFeatureCode(getFeatureCode());
			requirementFeature.setCreateType(1);// 创建方式 自建
			requirementFeature.setRequirementFeatureStatus("01");// 任务状态为 未实施
			requirementFeature.setCreateBy(CommonUtil.getCurrentUserId(request));
			requirementFeature.setCreateDate(new Timestamp(new Date().getTime()));
			devTaskService.addDevTask(requirementFeature, files,defectIds,dftActualWorkload,defectRemark,dftJsonString, request);
            sendAddMessage(request,requirementFeature);
			map.put("status", "success");
		} catch (Exception e) {
			return super.handleException(e, "新增开发任务失败");

		}
		return map;
	}

	/**
	 * 
	* @Title: sendAddMessage
	* @Description: 发送消息
	* @author author
	* @param request
	* @param tblRequirementFeature 开发任务信息
	 */
    private void sendAddMessage(HttpServletRequest request,TblRequirementFeature tblRequirementFeature){
		tblRequirementFeature.setManageUserId(1);
		Map<String,Object> map=new HashMap<>();
		map.put("messageTitle","收到一个新分配的开发任务");
		map.put("messageContent",tblRequirementFeature.getFeatureCode()+"|"+tblRequirementFeature.getFeatureName());
		map.put("messageReceiverScope",2);
		//消息来源 1-- 新建开发任务
		map.put("messageSource",1);
		map.put("messageType",1);
		map.put("systemId",tblRequirementFeature.getSystemId());
		map.put("projectId",tblRequirementFeature.getProjectId());
		if(tblRequirementFeature.getExecuteUserId()!=tblRequirementFeature.getManageUserId()) {
			map.put("messageReceiver", tblRequirementFeature.getExecuteUserId() + "," + tblRequirementFeature.getManageUserId());
		}else{
			map.put("messageReceiver", tblRequirementFeature.getExecuteUserId());
		}
		devManageToSystemInterface.insertMessage(JSON.toJSONString(map));

	}


    /**
     * 
    * @Title: sendTrsMessage
    * @Description: 转派发送消息
    * @author author
    * @param request
    * @param tr 开发任务信息
     */
	private void sendTrsMessage(HttpServletRequest request,TblRequirementFeature tr){


		TblRequirementFeature tblRequirementFeature=devTaskService.getOneFeature(String.valueOf(tr.getId()));
		Map<String,Object> map=new HashMap<>();
		map.put("messageTitle","收到一个新分配的开发任务");
		map.put("messageContent",tblRequirementFeature.getFeatureCode()+"|"+tblRequirementFeature.getFeatureName());
		map.put("messageReceiverScope",2);
		map.put("messageReceiver", tr.getExecuteUserId());

		devManageToSystemInterface.insertMessage(JSON.toJSONString(map));

	}
	/**
	 * 修改开发任务
	 * @param request
	 * @param requirementFeature 开发任务信息
	 * @param attachFiles 附件
	 * @param pstartDate 计划开始日期
	 * @param pendDate 计划结束日期
	 * @param aendDate 实际开始日期
	 * @param astartDate 实际结束日期
	 * @param defectIds 关联的缺陷id
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "updateDevTask", method = RequestMethod.POST)
	public Map<String, Object> updateDevTask(HttpServletRequest request, TblRequirementFeature requirementFeature,
			String attachFiles, String pstartDate, String pendDate, String aendDate, String astartDate,String defectIds) {
		Map<String, Object> map = new HashMap<>();
		//获取原先id
		long id=requirementFeature.getId();

		//获取旧的开发任务信息
		TblRequirementFeature oldTblRequirementFeature = devTaskService.getOneFeature(String.valueOf(id));

       // 判断是否为敏太
//		Double oldWorkLoad=oldTblRequirementFeature.getEstimateRemainWorkload();
//		Double newWorkLoad=requirementFeature.getEstimateRemainWorkload();
        Double oldWorkLoad=new Double(0);
        Double newWorkLoad=new Double(0);

        if(oldTblRequirementFeature.getEstimateWorkload()!=null){
            oldWorkLoad=oldTblRequirementFeature.getEstimateWorkload();
        }

        if(requirementFeature.getEstimateWorkload()!=null){
            newWorkLoad=requirementFeature.getEstimateWorkload();
        }



		List<TblRequirementFeatureAttachement> files = JsonUtil.fromJson(attachFiles,
				JsonUtil.createCollectionType(ArrayList.class, TblRequirementFeatureAttachement.class));


		DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (pstartDate != null && !"".equals(pstartDate)) {
				Date planStartDate = dFormat.parse(pstartDate);
				requirementFeature.setPlanStartDate(planStartDate);
			}
			if (pendDate != null && !"".equals(pendDate)) {
				Date planEndDate = dFormat.parse(pendDate);
				requirementFeature.setPlanEndDate(planEndDate);
			}
			if (astartDate != null && !"".equals(astartDate)) {
				Date actualStartDate = dFormat.parse(astartDate);
				requirementFeature.setActualStartDate(actualStartDate);
			}
			if (aendDate != null && !"".equals(aendDate)) {
				Date actualEndDate = dFormat.parse(aendDate);
				requirementFeature.setActualEndDate(actualEndDate);
			}
			requirementFeature.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
			requirementFeature.setLastUpdateDate(new Timestamp(new Date().getTime()));
			devTaskService.updateDevTask(requirementFeature, files,defectIds, request);

//			//关注的任务有更新发送消息
			TblRequirementFeatureAttention attention = new TblRequirementFeatureAttention();
			attention.setRequirementFeatureId(id);
			attention.setStatus(1);
			List<TblRequirementFeatureAttention> attentionList = devTaskService.getAttentionList(attention);
//			if (attentionList != null && attentionList.size() > 0) {
//				//编辑发送消息
//				sendEditMessage(request,oldTblRequirementFeature,requirementFeature, attentionList);
//			}
			//关注任务内容改变发送消息
            Integer status = devTaskService.getAttentionByReqFeatureId(id);
			map.put("contentStatus", status);
			map.put("old", oldTblRequirementFeature);
			map.put("new", requirementFeature);
			if(status != null && status == 1 && !requirementFeature.getRequirementFeatureStatus().equals(oldTblRequirementFeature.getRequirementFeatureStatus()) || status != null && status == 1 && !oldTblRequirementFeature.getFeatureOverview().equals(requirementFeature.getFeatureOverview())){
				devTaskMessage(request,oldTblRequirementFeature,requirementFeature, attentionList);
			}

            //更新工作量
			if (requirementFeature.getSystemId() != null) {
				TblSystemInfo tblSystemInfo =
						tblSystemInfoMapper.selectById(requirementFeature.getSystemId());
				if (tblSystemInfo.getDevelopmentMode()!=null && Math.abs(oldWorkLoad - newWorkLoad) != 0 && tblSystemInfo.getDevelopmentMode() == 1) {
					Map<String, Object> param = new HashMap<>();
					param.put("oldWorkLoad", oldWorkLoad);
					param.put("newWorkLoad", newWorkLoad);
					//1测试总预估工作量，2开发总预估工作量
					param.put("type", 2);
					param.put("devTaskId", requirementFeature.getId());
					sprintInfoService.updateSprintWorkLoad(param);

				}
			}

			map.put("status", "success");
		} catch (Exception e) {
			return super.handleException(e, "修改开发任务失败");
		}

		return map;
	}


	/**
	 * 
	* @Title: sendEditMessage
	* @Description: 更新开发任务后发送消息
	* @author author
	* @param request
	* @param oldFeature 更新前数据
	* @param newFeature 更新后数据
	* @param attentionList 关注列表
	 */
	private void sendEditMessage(HttpServletRequest request,TblRequirementFeature oldFeature,TblRequirementFeature newFeature, 
			List<TblRequirementFeatureAttention> attentionList){
		String userIds = "";
		for (TblRequirementFeatureAttention attention : attentionList) {
			userIds += attention.getUserId() + ",";
		}
		if(StringUtil.isNotEmpty(userIds)){
			userIds = userIds.substring(0, userIds.length() - 1);
			Map<String,Object> map=new HashMap<>();
			map.put("messageTitle","你关注的开发任务有更新");
			map.put("messageContent",oldFeature.getFeatureCode()+"|"+newFeature.getFeatureName());
			map.put("messageReceiverScope",2);
			map.put("messageReceiver", userIds);
			//消息来源 4--关注开发任务
			map.put("messageSource",4);
			map.put("systemId",oldFeature.getSystemId());
			devManageToSystemInterface.insertMessage(JSON.toJSONString(map));
		}

	}

	/**
	 * 
	* @Title: splitDevTasks
	* @Description: 拆分成多个开发任务(新建工作任务并关联当前开发任务)
	* @author author
	* @param request
	* @param devTasks 开发工作任务
	* @param id 开发任务id
	* @param requirementFeatureStatus
	* @param projectId 项目id
	* @return Map<String,Object>
	 */
	@RequestMapping(value = "splitDevTasks", method = RequestMethod.POST)
	public Map<String, Object> splitDevTasks(HttpServletRequest request, String devTasks, Long id,
											 String requirementFeatureStatus, Long projectId) {
		Map<String, Object> map = new HashMap<>();
		try {        
			List<TblDevTask> list = new ArrayList<TblDevTask>();
			list = JSONObject.parseArray(devTasks, TblDevTask.class);
			for (TblDevTask devTask : list) {
				devTask.setDevTaskCode(workTaskServiceImpl.getDevCode());
				devTaskService.addWorkTask(id, devTask, request);
				// 根据id修改开发任务的状态为实施中
				/*if (!"02".equals(requirementFeatureStatus)) {*/
					devTaskService.updateStatus(id, request);
				/*}*/
			}

			//发送消息

			for(TblDevTask devTask : list){
				devTask.setProjectId(projectId);
				sendAddWorkMessage(request,devTask)	;
				workTaskServiceImpl.sendWorkTaskMassage(devTask);// 发送消息
			}
			map.put("status", "success");
		} catch (Exception e) {
			return super.handleException(e, "拆分开发任务失败");
		}
		return map;




	}

	/**
	 * 
	* @Title: sendAddWorkMessage
	* @Description: 分配开发工作任务后发送消息
	* @author author
	* @param request
	* @param devTask 开发工作任务信息
	 */
	private void sendAddWorkMessage(HttpServletRequest request,TblDevTask devTask){

		Map<String,Object> map=new HashMap<>();
		map.put("messageTitle","收到一个新分配的开发工作任务");
		map.put("messageContent",devTask.getDevTaskCode()+"|"+devTask.getDevTaskName());
		map.put("messageReceiverScope",2);
		map.put("projectId",devTask.getProjectId());
		map.put("systemId",devTask.getSystemId());
		map.put("messageReceiver",devTask.getDevUserId());
		devManageToSystemInterface.insertMessage(JSON.toJSONString(map));

	}




	/**
	 * 
	* @Title: splitDevTask
	* @Description: 拆分开发任务(新建工作任务并关联当前开发任务)
	* @author author
	* @param request
	* @param devTask 开发工作任务
	* @param id 开发任务id
	* @param startDate 计划开始日期
	* @param endDate 计划结束日期
	* @param requirementFeatureStatus 开发任务状态
	* @return Map<String,Object>
	 */
	@RequestMapping(value = "splitDevTask", method = RequestMethod.POST)
	public Map<String, Object> splitDevTask(HttpServletRequest request, TblDevTask devTask, Long id, String startDate,
			String endDate, String requirementFeatureStatus) {
		Map<String, Object> map = new HashMap<>();
		DateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {

			if (startDate != null && !"".equals(startDate)) {
				Date planStartDate = dataFormat.parse(startDate);
				devTask.setPlanStartDate(planStartDate);
			}
			if (endDate != null && !"".equals(endDate)) {
				Date planEndDate = dataFormat.parse(endDate);
				devTask.setPlanEndDate(planEndDate);
			}
			devTask.setDevTaskCode(workTaskServiceImpl.getDevCode());
			devTaskService.addWorkTask(id,devTask,request);
			// 根据id修改开发任务的状态为实施中
			if (!"02".equals(requirementFeatureStatus)) {
				devTaskService.updateStatus(id, request);
			}

			map.put("status", "success");
		} catch (Exception e) {
			return super.handleException(e, "拆分开发任务失败");
		}
		return map;
	}

	/**
	 * 
	* @Title: handleDevTask
	* @Description: 处理开发任务
	* @author author
	* @param request
	* @param requirementFeature 开发任务信息
	* @param startDate 计划开始日期
	* @param endDate 计划结束日期
	* @param actualWorkload 实际工作量
	* @param handleRemark 处理备注
	* @return Map<String,Object>
	 */
	@RequestMapping(value = "handleDevTask", method = RequestMethod.POST)
	public Map<String, Object> handleDevTask(HttpServletRequest request, TblRequirementFeature requirementFeature,
			String startDate, String endDate, String actualWorkload,String handleRemark) {
		Map<String, Object> map = new HashMap<>();
		DateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (startDate != null && !"".equals(startDate)) {
				Date actualStartDate = dataFormat.parse(startDate);
				requirementFeature.setActualStartDate(actualStartDate);
			}
			if (endDate != null && !"".equals(endDate)) {
				Date actualEndDate = dataFormat.parse(endDate);
				requirementFeature.setActualEndDate(actualEndDate);
			}
			if (actualWorkload != null && !"".equals(actualWorkload)) {
				requirementFeature.setActualWorkload(Double.parseDouble(actualWorkload));
			}
			// 修改状态为实施完成
			requirementFeature.setRequirementFeatureStatus("03");
			requirementFeature.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
			requirementFeature.setLastUpdateDate(new Timestamp(new Date().getTime()));
			devTaskService.handleDevTask(requirementFeature, request,handleRemark);
			map.put("status", "success");
		} catch (Exception e) {
			return super.handleException(e, "处理开发任务失败");
		}
		return map;
	}

	/**
	 * 
	* @Title: transfer
	* @Description: 转派开发任务
	* @author author
	* @param requirementFeature 开发任务信息
	* @param transferRemark 转派备注
	* @param request
	* @return Map<String,Object>
	 */
	@RequestMapping(value = "transfer")
	public Map<String, Object> transfer(TblRequirementFeature requirementFeature,String transferRemark, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			devTaskService.updateTransfer(requirementFeature,transferRemark, request);
			map.put("status", "success");
			sendTrsMessage(request,requirementFeature);
		} catch (Exception e) {
			return super.handleException(e, "转派开发任务失败");
		}
		return map;
	}

	/**
	 * 
	* @Title: mergeSynDevTask
	* @Description: 合并任务(同步过来的任务合并到自建任务)
	* @author author
	* @param requirementFeature 开发任务信息
	* @param synId 页面或者IT全流程传过来的开发任务id
	* @param request
	* @return Map<String,Object>
	 */
	@RequestMapping("merge")
	public Map<String, Object> mergeSynDevTask(TblRequirementFeature requirementFeature, Long synId,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			// 首先被合并的任务信息中的taskId和编号更新到IT测试管理系统对应的开发任务中并把这条自建任务的创建方式改成同步 
			devTaskService.mergeDevTask(requirementFeature, synId, request);
			map.put("status", "success");
		} catch (Exception e) {
			return super.handleException(e, "合并开发任务失败");
		}
		return map;
	}

	/**
	 * 
	* @Title: addRemark
	* @Description: 添加备注
	* @author author
	* @param id 开发任务ID
	* @param remark 备注信息
	* @param attachFiles 附件
	* @param request
	* @return Map<String,Object>
	 */
	@RequestMapping("addRemark")
	public Map<String, Object> addRemark(Long id, TblRequirementFeatureRemark remark, String attachFiles,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		List<TblRequirementFeatureRemarkAttachement> files = JsonUtil.fromJson(attachFiles,
				JsonUtil.createCollectionType(ArrayList.class, TblRequirementFeatureRemarkAttachement.class));
		try {
			remark.setCreateBy(CommonUtil.getCurrentUserId(request));
			remark.setCreateDate(new Timestamp(new Date().getTime()));
			remark.setRequirementFeatureId(id);
			remark.setUserId(CommonUtil.getCurrentUserId(request));
			remark.setUserAccount(CommonUtil.getCurrentUserAccount(request));
			remark.setUserName(CommonUtil.getCurrentUserName(request));
			devTaskService.addRemark(remark, files, request);
			map.put("status", "success");
		} catch (Exception e) {
			return super.handleException(e, "新增开发任务备注失败");
		}
		return map;
	}
	
	/**
	 * 
	* @Title: changeCancelStatus
	* @Description: //取消状态  当关联的需求的状态变为取消时 该需求下开发任务及下属工作任务都变为取消状态
	* @author author
	* @param requirementId 需求id
	* @return Map<String,Object>
	 */
	@RequestMapping("cancelStatus")
	public Map<String,Object> changeCancelStatus(Long requirementId){
		Map<String, Object> map =new HashMap<>();
		try {
			devTaskService.changeCancelStatus(requirementId);
		} catch (Exception e) {
			return super.handleException(e, "修改该需求下开发任务及下属工作任务的状态为取消");
		}
		return map;
	}
	
	/**
	 * 
	* @Title: cancelStatusReqFeature
	* @Description: //取消状态 根据开发任务id下属工作任务都变为取消状态
	* @author author
	* @param reqFeatureId 开发任务id
	* @return Map<String,Object>
	 */
	@RequestMapping("cancelStatusReqFeature")
	public Map<String,Object> cancelStatusReqFeature(Long reqFeatureId){
		Map<String, Object> map =new HashMap<>();
		try {
			devTaskService.cancelStatusReqFeature(reqFeatureId);
		} catch (Exception e) {
			return super.handleException(e, "修改开发任务下属工作任务的状态为取消");
		}
		return map;
	}
	
	/**
	 * 
	* @Title: updateSprints
	* @Description: 页面【批量修改】冲刺信息
	* @author author
	* @param ids 选中的开发任务id
	* @param sprintId 冲刺id
	* @param devTaskStatus 开发任务状态
	* @param executeUserId 执行人员
	* @param systemVersionId 系统版本
	* @param executeProjectGroupId 执行小组
	* @param request
	* @return Map<String,Object>
	 */
	@RequestMapping("updateSprints")
	public Map<String, Object> updateSprints(String ids,Long sprintId,String devTaskStatus,Integer executeUserId,Long systemVersionId,
											 Long   executeProjectGroupId, HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		try {
			if (StringUtils.isNotBlank(ids)) {
				if(sprintId==null && (devTaskStatus==null || devTaskStatus.equals("")) && executeUserId==null){

				}else {

				 devTaskService.updateSprints(ids, sprintId, devTaskStatus, executeUserId, request);
				}


				//新增更新项目小组和系统版本
				devTaskService.updateGroupAndVersion(ids,systemVersionId,executeProjectGroupId);
				map.put("status", "success");
			}


			
		} catch (Exception e) {
			return super.handleException(e, "批量修改开发任务的冲刺失败！");
		}
		return map;
	}
	
	/**
	 * 
	* @Title: findSynDevTask
	* @Description: 合并任务前，查找是否有符合条件的任务：与该自建任务需求和系统都一致的同步任务
	* @author author
	* @param requirementFeature 页面选择的开发任务信息
	* @return List<TblRequirementFeature>
	 */
	@RequestMapping("findSynDevTask")
	public List<TblRequirementFeature> findSynDevTask(TblRequirementFeature requirementFeature) {
		List<TblRequirementFeature> synDevTasks = null;
		try {
			synDevTasks = devTaskService.findSynDevTask(requirementFeature);
		} catch (Exception e) {
			e.printStackTrace();
		    logger.error("mes:" + e.getMessage(), e);
		}
		return synDevTasks;
	}

	/**
	 * 
	* @Title: getSearchData
	* @Description: 查询所有搜索下拉框需要的数据：投产窗口和开发任务状态
	* @author author
	* @return Map<String,Object>
	 */
	@RequestMapping(value = "getSearchData", method = RequestMethod.POST)
	public Map<String, Object> getSearchData() {
		Map<String, Object> map = new HashMap<>();
		List<TblCommissioningWindow> windows = devTaskService.getWindows();
		map.put("windows", windows);

		String termCode = "TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS";
		List<TblDataDic> dics = getDataFromRedis(termCode);
		map.put("dataDic", dics);
		return map;
	}
	
	//导出
	@RequestMapping(value="exportExcel")
	public Map<String, Object> exportExcel(String reqFeatue,String startDate,String endDate,HttpServletResponse response,HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		String fileName = "开发任务导出数据.xlsx";
		DevTaskVo devTaskVo = JsonUtil.fromJson(reqFeatue, DevTaskVo.class);
		Long uid = CommonUtil.getCurrentUserId(request);
		devTaskVo.setUserId(uid);
		LinkedHashMap map2 = (LinkedHashMap) redisUtils.get(CommonUtil.getToken(request));
		List<String> roleCodes = (List<String>) map2.get("roles"); 
		List<DevTaskVo> list = new ArrayList<>();
		 if(startDate!=(null)&&!startDate.equals(""))  {
			  try {
				  SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				  Date date = sf.parse(startDate);
				 
				  Timestamp ts = new Timestamp(date.getTime());
				  devTaskVo.setCreateStartDate(ts);
				  Date date2 = sf.parse(endDate);
				  Timestamp ts2 = new Timestamp(date2.getTime());
				  devTaskVo.setCreateEndDate(ts2);
					
			} catch (ParseException e) {
				return super.handleException(e, "时间转换出错");
			}
			
		}


		if(devTaskVo.getWindowPdate()!=null && !devTaskVo.getWindowPdate().equals("")){
			Map<String,Object> p=new HashMap<>();

			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date = sf.parse(devTaskVo.getWindowPdate());
				p.put("WINDOW_DATE",date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			//查询此日期的windowsid
			p.put("status",1);
			List<TblCommissioningWindow> windows=commissioningWindowMapper.selectByMap(p);
			String commissioningWindowIds="";
			for(TblCommissioningWindow window:windows){
				commissioningWindowIds=commissioningWindowIds+window.getId()+",";
			}
			if(!commissioningWindowIds.equals("")){
				commissioningWindowIds=commissioningWindowIds.substring(0,commissioningWindowIds.length()-1);
				devTaskVo.setCommissioningWindowIds(commissioningWindowIds);
			}
		}

		if (roleCodes!=null && roleCodes.contains("XITONGGUANLIYUAN")) {//当前登录用户有角色是系统管理员
			list = requirementFeatureMapper.getAll(devTaskVo);
		}else {
			list = requirementFeatureMapper.getAllCondition(devTaskVo);
		}

		List<ExtendedField> extendedFields=devTaskService.findFieldByReqId(null);
		List<ExtendedField> extendedFieldsNew=new ArrayList<>();
		List<String> filedNames=new ArrayList<>();

		for(ExtendedField extendedField:extendedFields){
			if(extendedField.getStatus().equals("1")){
				extendedFieldsNew.add(extendedField);
				filedNames.add(extendedField.getFieldName());
			}
		}
		List<TblDataDic> dataDics = getDataFromRedis("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS");
		List<TblDataDic> dataDicsSource = getDataFromRedis("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_SOURCE");

		for (DevTaskVo devTaskVo2 : list) {
			if(devTaskVo2.getFieldTemplate()!=null) {
				JSONObject jsonObject=JSONObject.parseObject((devTaskVo2.getFieldTemplate()));
				String listTxt=jsonObject.getString("field");
				devTaskVo2.setExtendedFields( JSONArray.parseArray(listTxt, ExtendedField.class));
			}

			for (TblDataDic dataDic : dataDics) {
				if (StringUtils.isNotBlank(dataDic.getValueCode())&&StringUtils.isNotBlank(devTaskVo2.getStatusName())&&dataDic.getValueCode().equals(devTaskVo2.getStatusName())) {
					devTaskVo2.setStatusName(dataDic.getValueName());
				}

			}

			for (TblDataDic dataDic : dataDicsSource) {
				if (StringUtils.isNotBlank(dataDic.getValueCode())&&StringUtils.isNotBlank(devTaskVo2.getStatusName())&&dataDic.getValueCode().equals(devTaskVo2.getStatusName())) {
					devTaskVo2.setRequirementFeatureSourceName(dataDic.getValueName());
				}

			}
		}
		try {
			new ExportExcel("", DevTaskVo.class,extendedFieldsNew).setDataListNew(list,filedNames).write(response, fileName).dispose();
		} catch (IOException e) {
			return super.handleException(e, "导出开发任务失败");
		}
		return map;
	}




	// 文件上传 
	@RequestMapping(value = "uploadFile")
	public List<Map<String, Object>> uploadFile(@RequestParam("files") MultipartFile[] files,HttpServletRequest request) throws Exception {
		List<Map<String, Object>> attinfos = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (files.length > 0 && files != null) {
				for (MultipartFile file : files) {
					if (!file.isEmpty()) {
						InputStream inputStream = file.getInputStream();
						map = new HashMap<String, Object>();
						String extension = file.getOriginalFilename()
								.substring(file.getOriginalFilename().lastIndexOf(".") + 1);// 后缀名
						String fileNameOld = file.getOriginalFilename();
						if (BrowserUtil.isMSBrowser(request)) {
							fileNameOld = fileNameOld.substring(fileNameOld.lastIndexOf("\\")+1);
						}
						Random random = new Random();
						String i = String.valueOf(random.nextInt());
						String keyname = s3Util.putObject(s3Util.getDevTaskBucket(), i, inputStream);
						map.put("fileS3Key", keyname);
						map.put("fileS3Bucket", s3Util.getDevTaskBucket());
						map.put("fileNameOld", fileNameOld);
						map.put("fileType", extension);
						attinfos.add(map);
					} else {
						// 文件文件为空
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		    logger.error("mes:" + e.getMessage(), e);
		}
		return attinfos;
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
	//缺陷弹框查询未关联开发任务的缺陷列表
	@RequestMapping(value="listDftNoReqFeature")
	public Map<String, Object> listDftNoReqFeature(TblDefectInfo defectInfo,Integer featureSource,
												   Long reqFeatureId,Integer pageNumber, Integer pageSize){
		Map<String, Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			List<TblDefectInfo> list = devTaskService.findDftNoReqFeature(defectInfo,featureSource, reqFeatureId,pageNumber, pageSize);
			List<TblDefectInfo> list2 = devTaskService.findDftNoReqFeature(defectInfo,featureSource,reqFeatureId, 1, Integer.MAX_VALUE);
			map.put("total", list2.size());
			map.put("rows", list);
		} catch (Exception e) {
			return super.handleException(e, "查询缺陷列表失败");
		}
		return map;
	}
	//获取缺陷数据字典
	@RequestMapping("getDefectDic")
	public Map<String, Object> getDefectDic(){
		Map<String, Object> map = new HashMap<>();
		List<TblDataDic> defectSource = getDataFromRedis("TBL_DEFECT_INFO_DEFECT_SOURCE");//缺陷来源 系测/版测
		List<TblDataDic> defectType = getDataFromRedis("TBL_DEFECT_INFO_DEFECT_TYPE");//缺陷类型 开发/非开发
		List<TblDataDic> dftEmergencyLevel = getDataFromRedis("TBL_DEFECT_INFO_EMERGENCY_LEVEL");//紧急程度
		List<TblDataDic> dftSolveStatus = getDataFromRedis("TBL_DEFECT_INFO_SOLVE_STATUS");//解决情况
		List<TblDataDic> defectStatus = getDataFromRedis("TBL_DEFECT_INFO_DEFECT_STATUS");//缺陷状态
		List<TblDataDic> dftSeverityLevel = getDataFromRedis("TBL_DEFECT_INFO_SEVERITY_LEVEL");//严重级别
		List<TblDataDic> dftRejectReson = getDataFromRedis("TBL_DEFECT_INFO_REJECT_REASON");//严重级别
		map.put("defectSource", defectSource);
		map.put("dftSeverityLevel", dftSeverityLevel);
		map.put("dftRejectReson", dftRejectReson);
		map.put("defectType",defectType );
		map.put("dftEmergencyLevel", dftEmergencyLevel);
		map.put("dftSolveStatus", dftSolveStatus);
		map.put("defectStatus",defectStatus );
		return map;
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
	 * 获取添加开发任务编号
	 * @return
	 */
	private String getFeatureCode() {
		String featureCode = "";
		int codeInt = 0;
		Object object = redisUtils.get("TBL_REQUIREMENT_FEATURE_FEATURE_CODE");
		if (object != null && !"".equals(object)) {// redis有直接从redis中取
			String code = object.toString();
			// codeInt=Integer.parseInt(code.substring(Constants.ITMP_DEV_TASK_CODE.length()+1))+1;
			codeInt = Integer.parseInt(code) + 1;
		} else {// redis中没有查询数据库中最大的任务编号
			int length = Constants.ITMP_DEV_TASK_CODE.length() + 1;
			String cod = devTaskService.findMaxCode(length);
			if (!StringUtils.isBlank(cod)) {
				codeInt = Integer.parseInt(cod) + 1;
			} else {
				codeInt = 0;
			}
		}
		DecimalFormat df = new DecimalFormat("00000000");
		String codeString = df.format(codeInt);
		featureCode = Constants.ITMP_DEV_TASK_CODE + codeString;
		redisUtils.set("TBL_REQUIREMENT_FEATURE_FEATURE_CODE", codeString);
		return featureCode;
	}

	/**
	 * 调整排期列表分页显示
	 * @param windowName
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 * scz
	 * 2019年1月9日
	 * 下午4:11:51
	 */
	@RequestMapping(value="selectWindows",method=RequestMethod.POST)
	public Map<String, Object> selectWindows(String windowName, Integer pageNumber, Integer pageSize){
		HashMap<String, Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			List<TblCommissioningWindow> list = devTaskService.selectWindows(windowName, pageNumber, pageSize);
			List<TblCommissioningWindow> list2 = devTaskService.selectWindows(windowName, 1, Integer.MAX_VALUE);
			map.put("total", list2.size());
			map.put("rows", list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.handleException(e, "查询排期列表失败");
		}
		return map;
	}
	
	//bootstrap
	/**
	 * 获取开发任务信息
	 * @param request
	 * @param feature
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	@RequestMapping(value="getAllFeature",method=RequestMethod.POST)
	public Map<String, Object> getAllFeature(HttpServletRequest request,TblRequirementFeature feature,Integer pageSize,Integer pageNumber){
		Map<String, Object> map = new HashMap<>();
		try {
			List<Map<String, Object>> count= devTaskService.getAllFeature(feature,1,Integer.MAX_VALUE);
			List<Map<String, Object>> list = devTaskService.getAllFeature(feature, pageNumber, pageSize);
			map.put("total", count.size());
			map.put("rows", list);
		} catch (Exception e) {
			e.printStackTrace();
			this.handleException(e, "查询开发任务失败");
		}
		return map;		
	}
	
	//根据systemId查询system_scm表获取systemVersionId scmBranch
	@RequestMapping(value="getSystemVersionBranch")
	public Map<String, Object> getSystemVersionBranch(Long systemId){
		Map<String, Object> map = new HashMap<>();
		try {
			List<Map<String, Object>> list = devTaskService.getSystemVersionBranch(systemId);
			map.put("systemVersionBranchs", list);
		} catch (Exception e) {
			return super.handleException(e, "查询系统版本和系统配置分支失败");
		}
		return map;
		
	}
	
	//根据需求code和系统id查询开发任务
	@RequestMapping(value="getReqFeatureByReqCodeAndSystemId")
	public Map<String, Object> getReqFeatureByReqCodeAndSystemId (String requirementCode,Long systemId){
		Map<String, Object> map = new HashMap<>();
		try {
			if (systemId!=null) {
				List<TblRequirementFeature> list = devTaskService.getReqFeatureByReqCodeAndSystemId(requirementCode,systemId);
				map.put("data", list);
			}
		} catch (Exception e) {
			return super.handleException(e, "查询开发任务失败");
		}
		return map;
	}
	
	//部署需要的开发任务列表  当前系统下实施完成状态 
	@RequestMapping(value="deplayReqFesture")
	public Map<String, Object> deplayReqFesture(TblRequirementFeature feature,String status,Integer pageSize,Integer pageNumber){
		Map<String, Object> map = new HashMap<>();
		status = "03,";
		try {
			String statusArr[] = null;
			if (!StringUtils.isBlank(status)) {
				statusArr = status.split(",");
			}
			List<DevTaskVo> count= devTaskService.getDeplayReqFesture(feature,statusArr,1,Integer.MAX_VALUE);
			List<DevTaskVo> list = devTaskService.getDeplayReqFesture(feature,statusArr, pageNumber, pageSize);
			
			map.put("total", count.size());
			map.put("rows", list);
		} catch (Exception e) {
			e.printStackTrace();
			this.handleException(e, "查询开发任务失败");
		}
		return map;
		
	}


	//部署需要的开发任务列表  当前系统下实施完成状态
	@RequestMapping(value="deplayReqFestureByParam")
	public Map<String, Object> deplayReqFestureByParam(TblRequirementFeature feature,String status,String windowId,String sprintId, Integer pageSize,Integer pageNumber){
		Map<String, Object> map = new HashMap<>();
		status = "03,";
		try {
			String statusArr[] = null;
			if (!StringUtils.isBlank(status)) {
				statusArr = status.split(",");
			}
			List<DevTaskVo> list= devTaskService.getDeplayReqFesture(feature,statusArr,1,Integer.MAX_VALUE);
			//List<DevTaskVo> list = devTaskService.getDeplayReqFesture(feature,statusArr, pageNumber, pageSize);

			map.put("rows", list);
		} catch (Exception e) {
			e.printStackTrace();
			this.handleException(e, "查询开发任务失败");
		}
		return map;

	}

	
	/**修改开发任务的部署状态：部署回调
	 * @param
	 * ids:开发任务的id
	 * env:环境类型
	 * jsonString:日志需要的信息
	 * status 1:驳回  2：成功 3：失败 4：取消
	 * 
	 * */ 
	@RequestMapping(value="updateDeployStatus")
	public Map<String, Object> updateDeployStatus(String ids,String env,String jsonString,Integer status){
		Map<String, Object> result = new HashMap<>();
		try {
			if (!StringUtils.isBlank(ids)&& !StringUtils.isBlank(env) && status!=null) {
				devTaskService.updateDeployStatusOne(ids,env,jsonString,status);
				//devTaskService.updateDeployStatus(idsArr,status);
			}
		} catch (Exception e) {
			return super.handleException(e, "修改开发任务失败");
		}
		return result;
	}
	
	/**同步测试任务的部署状态
	 * @param 
	 * 
	 * */
	@RequestMapping(value="synReqFeatureDeployStatus")
	public Map<String, Object> synReqFeatureDeployStatus(Long requirementId,Long systemId,String deployStatus,String loginfo){
		Map<String, Object> result = new HashMap<>();
		try {
			if(requirementId!=null && systemId!=null) {
				devTaskService.synReqFeatureDeployStatus(requirementId,systemId,deployStatus,loginfo);
				result.put("status", "success");
			}
		} catch (Exception e) {
			return super.handleException(e, "同步测试任务部署状态失败");
		}
		return result;
	}
	
	@RequestMapping(value="getDeployStatus")
	public Map<String, Object> getDeployStatus(){
		Map<String, Object> map = new HashMap<>();
		List<TblDataDic> deployStatus = getDataFromRedis("TBL_REQUIREMENT_FEATURE_DEPLOY_STATUS");
		map.put("deployStatus", deployStatus);
		return map;
	}
	
	/**
	 *  更新开发任务时间点追踪表
	 * */
	@RequestMapping("updateReqFeatureTimeTrace")
	public Map<String, Object> updateReqFeatureTimeTrace(String jsonString){
		Map<String, Object> result = new HashMap<>();
		try {
			
			TblRequirementFeatureTimeTrace timeTrace = JsonUtil.fromJson(jsonString, TblRequirementFeatureTimeTrace.class);
			List<TblRequirementFeature> requirementFeatures = requirementFeatureMapper.selectBySystemIdAndReqId1(timeTrace.getSystemId(),timeTrace.getRequirementId());
			for (TblRequirementFeature tblRequirementFeature : requirementFeatures) {
				if (tblRequirementFeature.getId()!=null) {
					Map<String, Object> map = new HashMap<>();
					map.put("requirementFeatureId",tblRequirementFeature.getId());
					if (timeTrace.getRequirementFeatureTestingTime()!=null) {
						map.put("requirementFeatureTestingTime", timeTrace.getRequirementFeatureTestingTime());
					}
					if (timeTrace.getRequirementFeatureTestCompleteTime()!=null) {
						map.put("requirementFeatureTestCompleteTime", timeTrace.getRequirementFeatureTestCompleteTime());
					}
					String jsonString2 = JsonUtil.toJson(map);
					devTaskService.updateReqFeatureTimeTrace(jsonString2);
				}
			}
		} catch (Exception e) {
			return super.handleException(e, "修改开发任务时间点追踪表失败！");
		}
		return result;
	}
	
	/**
	 * 获取所有冲刺
	 * */
	@RequestMapping("getSprintBySystemId")
	public Map<String, Object> getSprintBySystemId(Long systemId,Long projectId){
		Map<String, Object> map = new HashMap<>();
		try {
//			List<TblSprintInfo> sprintInfos = devTaskService.getSprintBySystemId(systemId);
			List<TblSprintInfo> sprintInfos = devTaskService.getSprintInfoListBySystemIdAndProjectId(systemId,projectId);
			map.put("sprintInfos", sprintInfos);
		} catch (Exception e) {
			return super.handleException(e, "查看冲刺失败！");
		}
		return map;
	}
	
	
	//部署需要的开发任务列表
	@RequestMapping(value="deplayPrdReqFesture")
	public Map<String, Object> deplayPrdReqFesture(TblRequirementFeature feature,String status,String windowId,Integer pageSize,Integer pageNumber){
		Map<String, Object> map = new HashMap<>();
		if(windowId==null || windowId.equals("")) {
			return map;
		}
		status = "03,";
		try {
			String statusArr[] = null;
			if (!StringUtils.isBlank(status)) {
				statusArr = status.split(",");
			}
			List<DevTaskVo> count= devTaskService.getPrdDeplayReqFesture(feature,statusArr,windowId,1,Integer.MAX_VALUE);
			List<DevTaskVo> list = devTaskService.getPrdDeplayReqFesture(feature,statusArr,windowId, pageNumber, pageSize);
			
			map.put("total", count.size());
			map.put("rows", list);
		} catch (Exception e) {
			e.printStackTrace();
			this.handleException(e, "查询开发任务失败");
		}
		return map;
		
	}




	//部署需要的开发任务列表
	@RequestMapping(value="deplayPrdReqFestureNoPage")
	public Map<String, Object> deplayPrdReqFestureNoPage(TblRequirementFeature feature,String status,String windowId){
		Map<String, Object> map = new HashMap<>();
		//if(windowId==null || windowId.equals("")) {
			//return map;
//		}else{
//			feature.setCommissioningWindowId(Long.parseLong(windowId));
//		}
		status = "03,";

		try {
			String statusArr[] = null;
			if (!StringUtils.isBlank(status)) {
				statusArr = status.split(",");
			}
			List<DevTaskVo> list= devTaskService.getPrdDeplayReqFesture(feature,statusArr,windowId,1,Integer.MAX_VALUE);
			map.put("rows", list);
		} catch (Exception e) {
			e.printStackTrace();
			this.handleException(e, "查询开发任务失败");
		}
		return map;

	}







	// 查询窗口日期
	  @RequestMapping(value = "getSearchWindow", method = RequestMethod.POST)
	  public Map<String, Object> getSearchWindow(Long systemId) {
	    Map<String, Object> map = new HashMap<>();
	    List<TblCommissioningWindow> windows = devTaskService.getLimitWindows(systemId);
	    map.put("windows", windows);	
	    return map;
	  }
	  
	  //获取开发任务的状态
	@RequestMapping(value = "getReqFeatureStatus")
	public Map<String, Object> getReqFeatureStatus() {
		Map<String, Object> map = new HashMap<>();
		try {
			List<TblDataDic> status = requirementFeatureMapper.getReqFeatureStatus();

			Collections.sort(status, new Comparator<TblDataDic>() {
				@Override
				public int compare(TblDataDic o1, TblDataDic o2) {
					return o1.getValueSeq() - o2.getValueSeq();
				}
			});
			map.put("reqFeatureStatus", status);
		} catch (Exception e) {
			super.handleException(e, "获取开发任务状态失败！");
		}
		return map;
	}
	 
	//同步测试任务投产窗口
	@RequestMapping(value="synReqFeaturewindow")
	public Map<String, Object> synReqFeaturewindow(Long requirementId, Long systemId, Long commissioningWindowId, String loginfo ,String beforeName, String afterName ){
		Map<String, Object> result = new HashMap<>();
		try {
			if(requirementId!=null && systemId!=null && commissioningWindowId!=null) {
				devTaskService.synReqFeaturewindow(requirementId,systemId,commissioningWindowId,loginfo,beforeName,afterName);
			}
		} catch (Exception e) {
			return super.handleException(e, "同步测试任务投产窗口失败");
		}
		return result;
	}
	
	//同步测试任务所属处室
	@RequestMapping(value="synReqFeatureDept")
	public Map<String, Object> synReqFeatureDept(Long requirementId, Long systemId, Long deptId, String loginfo, String deptBeforeName,String deptAfterName){
		Map<String,Object> result = new HashMap<>();
		try {
			if(requirementId!=null && systemId!=null && deptId!=null) {
				devTaskService.synReqFeatureDept(requirementId,systemId,deptId,loginfo,deptBeforeName,deptAfterName);
			}
		} catch (Exception e) {
			return super.handleException(e, "同步测试任务所属处室失败");
		}
		return result;
	}

	//获取项目
	@RequestMapping(value="getProjectGroup")
	public Map<String, Object> getProjectGroup(HttpServletRequest request ){
		Map<String, Object> result = new HashMap<>();
		try {
			    Long uid = CommonUtil.getCurrentUserId(request);
			    String zNodes=devTaskService.getProjectGroup(uid);
               // zNodes = zNodes.replaceAll("\"(\\w+)\"(\\s*:\\s*)", "$1$2");
			    result.put("zNodes",zNodes);

		} catch (Exception e) {
			return super.handleException(e, "获取小组出错");
		}
		return result;
	}

   /* //根据系统获取项目
    @RequestMapping(value="getProjectGroupBySystemId")
    public Map<String, Object> getProjectGroupBySystemId(HttpServletRequest request ,long systemId){
        Map<String, Object> result = new HashMap<>();
        try {
            Long uid = CommonUtil.getCurrentUserId(request);
           List<String> projectGroupIds=devTaskService.getProjectGroupBySystemId(systemId,uid);
           result.put("size",projectGroupIds.size());
           if(projectGroupIds.size()==1) {
               result.put("id", projectGroupIds.get(0));
           }
        } catch (Exception e) {
            return super.handleException(e, "获取小组出错");
        }
        return result;
    }*/


	//根据项目获取项目小组   
    //需求变更 项目-系统改成多对多关系 已修改 ---ztt 
	@RequestMapping(value="getProjectGroupByProjectId")
	public Map<String, Object> getProjectGroupByProjectId( Long systemId){
		
		Map<String, Object> result = new HashMap<>();
		if( systemId!=null) {
			try {
				String projectGroup = devTaskService.getProjectGroupByProjectIds(systemId);
				result.put("zNodes", projectGroup);
			} catch (Exception e) {
				return super.handleException(e, "获取小组出错");
			}
		}else{
			   result.put("zNodes", "");
		}
		return result;
	}

	//需求变更 项目-系统改成多对多关系 已修改 ---ztt
	@RequestMapping(value="getProjectGroupByProjectId1")
	public Map<String, Object> getProjectGroupByProjectId1(Long projectId){

		Map<String, Object> result = new HashMap<>();
		if( projectId!=null) {
			try {
				String projectGroup = devTaskService.getProjectGroupByProjectId(projectId);
				result.put("zNodes", projectGroup);
			} catch (Exception e) {
				return super.handleException(e, "获取小组出错");
			}
		}else{
			result.put("zNodes", "");
		}
		return result;
	}

	//查询同系统同需求开发任务
	@RequestMapping(value="getDevTaskBySystemAndRequirement")
	public Map<String, Object> getDevTaskBySystemAndRequirement(Long systemId, Long requirementId){
		Map<String,Object> result = new HashMap<>();
		try {
			List<Map<String,Object>> list = devTaskService.getDevTaskBySystemAndRequirement(systemId,requirementId);
			result.put("list", list);
		} catch (Exception e) {
			return super.handleException(e, "查询开发任务失败");
		}
		return result;
	}
	
	@RequestMapping(value = "downloadReqExcel", method = RequestMethod.GET)
	public void downloadReqExcel(HttpServletResponse response) {
		Object redisEnvType  =  redisUtils.get("FEATURE_EXCEL_S3");
		Map<String, Object> map = JSON.parseObject(redisEnvType.toString());
		s3Util.downObject(map.get("fileS3Bucket").toString(), map.get("fileS3Key").toString(), map.get("fileNameOld").toString(), response);
	}
	/**
	 * 导入
	 * @param file
	 * @param request
	 * @return
	 */
    @RequestMapping(value = "importExcel", method = RequestMethod.POST)
    public Map<String, Object> importExcel(MultipartFile file,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        map =devTaskService.importExcel(file, request);

        return map;
    }



    /**
     * 根据ID查询开发任务
     * @param id
     * @return
     */
    @RequestMapping(value = "getOneFeature", method = RequestMethod.POST)
    public Map<String, Object> getOneFeature(String id) {
        Map<String, Object> map = new HashMap<>();
       TblRequirementFeature tblRequirementFeature= devTaskService.getOneFeature(id);
        map.put("tblRequirementFeature",tblRequirementFeature);
       return map;

    }
    
	@RequestMapping(value = "getTreeName", method = RequestMethod.POST)
    private String getTreeName(String treeId){
		String treeName="";
		Map<String,Object> map=new HashMap<>();
		map.put("id",treeId);
		List<Map<String,Object>> maps=devTaskService.getTreeName(map);
		if(maps!=null && maps.size()>0){
			treeName=maps.get(0).get("systemTreeName").toString();
		}
		return treeName;
	}

	//bootstrap
	@RequestMapping(value="getAllProjectPlan",method=RequestMethod.POST)
	public Map<String, Object> getAllProjectPlan(TblProjectPlan tblProjectPlan, Integer pageSize, Integer pageNumber){
		Map<String, Object> map = new HashMap<>();
		try {
			int count= devTaskService.getAllProjectPlanCount(tblProjectPlan);
			List<Map<String, Object>> list = devTaskService.getAllProjectPlan(tblProjectPlan, pageNumber, pageSize);
			map.put("total", count);
			map.put("rows", list);
		} catch (Exception e) {
			return super.handleException(e, "获取数据失败！");
		}
		return map;
	}



	@RequestMapping(value="getRequireFeature",method=RequestMethod.POST)
	public Map<String, Object> getRequireFeature( String code,Long requireFeatureId, Integer pageNumber){
		Map<String, Object> map = new HashMap<>();
		try {
			Map<String, Object> param = new HashMap<>();
			if(code!=null){
				param.put("FEATURE_CODE",code);
			}
			if(requireFeatureId!=null){
				param.put("ID",requireFeatureId);
			}
		   List<TblRequirementFeature> requirementFeatures= requirementFeatureMapper.selectByMap(param);
			map.put("list",requirementFeatures);
		} catch (Exception e) {
			return super.handleException(e, "获取数据失败！");
		}

		return map;
	}


	/**
	 *   关注消息发送
	 * @param request
	 * @param oldFeature
	 * @param newFeature
	 * @param attentionList
	 */
	private void devTaskMessage(HttpServletRequest request,TblRequirementFeature oldFeature,TblRequirementFeature newFeature,
								 List<TblRequirementFeatureAttention> attentionList){
		String userIds = "";
		if (!attentionList.isEmpty()){
			for (TblRequirementFeatureAttention attention : attentionList) {
				userIds += attention.getUserId() + ",";
			}
		}
		if(StringUtil.isNotEmpty(userIds)){
			userIds = userIds.substring(0, userIds.length() - 1);
		}
		Map<String,Object> map=new HashMap<>();
		map.put("messageTitle","你关注的开发任务有更新");
		map.put("messageContent",oldFeature.getFeatureCode()+"|"+newFeature.getFeatureName());
		map.put("messageReceiverScope",2);
		map.put("messageReceiver", userIds);
		//消息来源 4--关注开发任务
		map.put("messageSource",4);
		map.put("systemId",oldFeature.getSystemId());
		map.put("projectId",oldFeature.getProjectId());
		devManageToSystemInterface.insertMessage(JSON.toJSONString(map));
	}


	// 用户验收
	@RequestMapping(value = "checkStatus")
	public Map<String,Object> checkStatus(@RequestParam("files") MultipartFile[] files,
			 @RequestParam("id") Long id,HttpServletRequest request) throws Exception {
		Map<String,Object> map = new HashMap<>();
		try {
			devTaskService.checkStatus(files,id,request);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			return super.handleException(e, "用户验收失败！");
		}
		return map;
	}

	/**
	 * 获取项目信息
	 * @param projectName
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getProjectListByProjectName")
	public Map<String, Object> getProjectInfoAll(String projectName ,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			Long uid = CommonUtil.getCurrentUserId(request);
			LinkedHashMap codeMap = (LinkedHashMap) redisUtils.get(CommonUtil.getToken(request));
			List<String> roleCodes = (List<String>) codeMap.get("roles");
			if(!"".equals(projectName)){
				map = devTaskService.getProjectInfoAll(projectName,uid,roleCodes);
			}
		} catch (Exception e) {
			return super.handleException(e, "查询项目失败");
		}
		return  map;
	}

	@RequestMapping(value="getProjectPlanTree",method=RequestMethod.POST)
	public Map<String, Object> getProjectPlanTree(Long projectId){
		Map<String, Object> map = new HashMap<>();
		try {
			List<TblProjectPlan> planList = devTaskService.getProjectPlanTree(projectId);
			map.put("list",planList);
		} catch (Exception e) {
			return super.handleException(e, "获取数据失败！");
		}
		return map;
	}
	/**
	 * 获取新的开发任务编号
	 * @return
	 */
	@RequestMapping(value = "getNewFeatureCode",method=RequestMethod.POST)
	public String getNewFeatureCode(){
		String featureCode = getFeatureCode();
		return featureCode;
	}
	/**
	 * 根据系统和需求查询开发任务
	 * @param systemId
	 * @param requirementId
	 * @return
	 */
	@RequestMapping(value = "getFeatureBySystemAndRequirement",method=RequestMethod.POST)
	public Map<String, Object> getFeatureBySystemAndRequirement(Long systemId,Long requirementId){
		Map<String, Object> map = new HashMap<>();
		try {
			List<TblDevTask> list=devTaskService.getFeatureBySystemAndRequirement(systemId,requirementId);
			map.put("feature", list);
		} catch (Exception e) {
			return super.handleException(e, "获取数据失败！");
		}
		return map;
	}
	/**
	 * 查询未结束的开发任务
	 * @param featureId
	 * @return
	 */
	@RequestMapping(value = "getDevNotOverByFeaureId",method=RequestMethod.POST)
	public Map<String, Object> getDevNotOverByFeaureId(Long featureId){
		Map<String, Object> map = new HashMap<>();
		try {
			List<TblDevTask> list=devTaskService.getDevNotOverByFeaureId(featureId);
			map.put("devData", list);
		} catch (Exception e) {
			return super.handleException(e, "获取数据失败！");
		}
		return map;
	}
}
