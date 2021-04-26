package cn.pioneeruniverse.project.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.common.utils.S3Util;
import cn.pioneeruniverse.project.common.DicConstants;
import cn.pioneeruniverse.project.common.SynRequirementFeatureUtil;
import cn.pioneeruniverse.project.dao.mybatis.RequirementMapper;
import cn.pioneeruniverse.project.dao.mybatis.RequirmentSystemMapper;
import cn.pioneeruniverse.project.entity.ExtendedField;
import cn.pioneeruniverse.project.entity.TblDataDic;
import cn.pioneeruniverse.project.entity.TblRequirementAttachement;
import cn.pioneeruniverse.project.entity.TblRequirementAttention;
import cn.pioneeruniverse.project.entity.TblRequirementFeature;
import cn.pioneeruniverse.project.entity.TblRequirementInfo;
import cn.pioneeruniverse.project.entity.TblRequirementSystem;
import cn.pioneeruniverse.project.feignInterface.ProjectToSystemInterface;
import cn.pioneeruniverse.project.service.requirement.RequirementDocumentService;
import cn.pioneeruniverse.project.service.requirement.RequirementFeatureService;
import cn.pioneeruniverse.project.service.requirement.RequirementService;
import cn.pioneeruniverse.project.service.requirementsystem.RequirementSystemService;
import cn.pioneeruniverse.project.vo.SynRequirementFeature;

/**
 * 
* @ClassName: RequirementController
* @Description: 需求管理
* @author author
* @date 2020年10月13日 下午4:32:54
*
 */
@RestController
@RequestMapping("requirement")
public class RequirementController extends BaseController{

	@Autowired
	private S3Util s3Util;
	@Autowired
	private RedisUtils redisUtils;
	@Autowired
	private RequirementMapper requirementMapper;
	@Autowired
	private RequirementService requirementService;
	@Autowired
	private RequirementFeatureService requirementFeatureService;
	@Autowired
	private RequirementSystemService requirementSystemService;
	@Autowired
	private ProjectToSystemInterface projectToSystemInterface;
	@Autowired
	private RequirmentSystemMapper tblRequirementSystemMapper;
	@Autowired
	private RequirementDocumentService requirementDocumentService;

	private static Logger log = LoggerFactory.getLogger(RequirementController.class);

	/**
	 * 
	* @Title: getAllRequirement
	* @Description: 需求管理列表
	* @author author
	* @param request
	* @param findRequirment 封装的查询条件
	* @param rows  每页数量
	* @param page  第几页
	* @return Map<String,Object> status=1正常，2异常
	 */
	@RequestMapping(value="getAllRequirement",method=RequestMethod.POST)
	public String getAllRequirement(HttpServletRequest request,String findRequirment,Integer rows,Integer page){
		JSONObject jsonObj = new JSONObject();
		TblRequirementInfo requirement = new TblRequirementInfo();
		try {
			Long uid = CommonUtil.getCurrentUserId(request);
			LinkedHashMap map = (LinkedHashMap) redisUtils.get(CommonUtil.getToken(request));
			List<String> roleCodes = (List<String>) map.get("roles");//用于区分是否是系统管理员
			if (StringUtils.isNotBlank(findRequirment)) {
				requirement = JSONObject.parseObject(findRequirment, TblRequirementInfo.class);
			}
			requirement.setUserId(uid);
			int count= requirementService.getCountRequirement(requirement,roleCodes);
			List<TblRequirementInfo> list = requirementService.getAllRequirement(requirement, page, rows,roleCodes);
			jsonObj.put("page", page);//第几页
			if(rows!=null) {
				jsonObj.put("total", (count+rows-1)/rows);//总页数
			}
			jsonObj.put("records", count); //总条数
			jsonObj.put("rows", list); //当前页的数据
		} catch (Exception e) {
			e.printStackTrace();
			log.error("mes:" + e.getMessage(), e);
		}
		return jsonObj.toJSONString();
	}

	/**
	 * 
	* @Title: getFunctionCountByReqId
	* @Description: 获取需求功能点
	* @author author
	* @param request
	* @param reqId 需求ID
	* @return map key:data  value:int 需求功能点总数
	 */
	@RequestMapping(value="getFunctionCountByReqId",method=RequestMethod.POST)
	public Map<String,Object> getFunctionCountByReqId(HttpServletRequest request,Long reqId){
		Map<String, Object> result = new HashMap<>();
		try {
			int count= requirementSystemService.getFunctionCountByReqId(reqId);
			result.put("data", count);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("mes:" + e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 
	* @Title: findRequirementById
	* @Description: 获取需求详情
	* @author author
	* @param request
	* @param rIds 需求ID 
	* @param parentIds 父需求ID
	* @return map key:triFather        value:TblRequirementInfo父需求
	*                 data                   TblRequirementInfo需求信息，data.list:List<TblRequirementSystem>需求关联系统信息列表
	*                 attentionStatus        是否关注1关注，2未关注
	*                 trf                    List<TblRequirementFeature>关联需求的开发任务列表
	*                 fields                 List<ExtendedField>自定义扩展字段
	*                 triSon                 List<TblRequirementInfo>子需求信息列表
	*                 attachements           List<TblRequirementAttachement>需求附件列表
	*                 status                 1正常返回，2异常返回
	*                 
	* 
	 */
	@RequestMapping(value = "findRequirementById", method = RequestMethod.POST)
	public Map<String, Object> findRequirementById(HttpServletRequest request, Long rIds,Long parentIds) {
		Map<String, Object> result = new HashMap<>();
		try {
			//需求
			result = requirementService.findRequirementById(rIds,parentIds);
			//需求下的开发任务
			List<TblRequirementFeature> trf = requirementFeatureService.findFeatureByRequirementId(rIds);
			//查询关注功能
			Long userId = CommonUtil.getCurrentUserId(request);
			TblRequirementAttention attention = new TblRequirementAttention();
			attention.setRequirementId(rIds);
			attention.setUserId(userId);
			attention.setStatus(1);
			List<TblRequirementAttention> attentionList = requirementService.getAttentionList(attention);
			//有关注，需求详情页面红心显示
			if (attentionList != null && attentionList.size() > 0) {
				result.put("attentionStatus", 1);
			} else {
				result.put("attentionStatus", 2);//未关注
			}
			//查询扩展字段
			List<ExtendedField> extendedFields=requirementService.findRequirementField(rIds);
			result.put("fields", extendedFields);//扩展字段

			result.put("status", Constants.ITMP_RETURN_SUCCESS);
			result.put("trf",trf);
		} catch (Exception e) {
			return handleException(e, "获取需求失败");
		}
		return result;
	}

	/**
	 * 
	* @Title: changeAttention
	* @Description: 关注需求或不关注，关注之后，需求的编辑变更，会发送消息到个人工作台中
	* @author author
	* @param request
	* @param id 关注表ID
	* @param attentionStatus 关注状态
	* @return Map<String,Object> status=1正常，2异常
	 */
	@RequestMapping(value = "changeAttention", method = RequestMethod.POST)
	public Map<String, Object> changeAttention(HttpServletRequest request, Long id, Integer attentionStatus) {
		Map<String, Object> map = new HashMap<>();
		map.put("status", "success");
		try {
			if (attentionStatus != null) {
				requirementService.changeAttention(id, attentionStatus, request);
			}
		} catch (Exception e) {
			return super.handleException(e, "更新关注失败");
		}
		return map;
	}

	/**
	 * 
	* @Title: toEditRequirementById
	* @Description: 编辑需求
	* @author author
	* @param rIds 需求ID
	* @return Map<String,Object> status=1正常，2异常
	 */
	@RequestMapping(value = "toEditRequirementById", method = RequestMethod.POST)
	public Map<String, Object> toEditRequirementById(Long rIds) {
		Map<String, Object> result = new HashMap<>();
		try {
			//需求信息
			result = requirementService.toEditRequirementById(rIds);
			//需求附件
			List<TblRequirementAttachement> attachements = requirementService.getRequirementAttachement(rIds);

			//获取扩展字段
			List<ExtendedField> extendedFields=requirementService.findRequirementField(rIds);
			result.put("fields", extendedFields); //拓展字段
			result.put("attachements", attachements); //附件信息
			result.put("status", Constants.ITMP_RETURN_SUCCESS);//1成功返回，2异常返回
		} catch (Exception e) {
			return handleException(e, "获取需求失败");
		}
		return result;
	}

	/**
	 * 
	* @Title: getDataDicList
	* @Description: 获取数据字典列表
	* @author author
	* @param datadictype 字典类型，用于决定用哪个term_code
	* @return Map<String,Object> status=1正常，2异常
	 */
	@RequestMapping(value = "getDataDicList", method = RequestMethod.POST)
	public List<TblDataDic> getDataDicList(String datadictype) {
		String termCode="";
		List<TblDataDic> resultList= new ArrayList<>();
		try {
			if(datadictype!=null&&datadictype.equals("reqStatus")) {//需求状态
				termCode=DicConstants.req_status;
			}
			if(datadictype!=null&&datadictype.equals("reqSource")) {//需求来源
				termCode=DicConstants.req_source;
			}
			if(datadictype!=null&&datadictype.equals("reqType")) {//需求类型
				termCode=DicConstants.req_type;
			}
			String result =redisUtils.get(termCode).toString();
			if (!StringUtils.isBlank(result)) {
				Map<String, Object> maps = JSON.parseObject(result);
				for (Map.Entry<String, Object> entry : maps.entrySet()){
					TblDataDic tdd= new TblDataDic();
					tdd.setValueCode(entry.getKey().toString());
					tdd.setValueName(entry.getValue().toString());
					resultList.add(tdd);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("mes:" + e.getMessage(), e);
		}
		return resultList;
	}

	/**
	 * 
	* @Title: updateRequirementData
	* @Description: 同步IT全流程需求
	* @author author
	* @param requirementData 报文内容
	* @param request
	* @param response
	 */
	@RequestMapping(value = "updateRequirementData",method = RequestMethod.POST)
	public void updateRequirementData(@RequestBody String requirementData,HttpServletRequest request,HttpServletResponse response) {
		synchronized (this){
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synReq(requirementData,request,response);
		}
	}

	/**
	 * 
	* @Title: synReq
	* @Description: 同步需求
	* @author author
	* @param requirementData 需求报文内容
	* @param request
	* @param response
	 */
	public void synReq(String requirementData,HttpServletRequest request,HttpServletResponse response) {
		try {
			logger.info("开始同步需求信息数据"+requirementData);
			if (!StringUtils.isBlank(requirementData)) {
				Map<String, Object> data = jsonToMap(jsonToMap(requirementData).get("requestBody").toString());
				String requirementList = data.get("requirementList").toString();
				String reqSystemList = data.get("reqSystemList").toString();
				List<TblRequirementFeature> featureList = strToFeature(data.get("taskList").toString());
				//处理需求信息
				Map<String,Object> map = requirementService.updateRequirementDataItmp(requirementList,reqSystemList);
				if(map!=null){
					//如果报文里面没有任务信息
					if(featureList.size()==0){
						requirementFeatureService.addItmpFeatureInfo(reqSystemList,map.get("reqId").toString());
					}else{
						for(TblRequirementFeature feature:featureList) {
							//开发任务或者配置任务
							if("taskdevelop".equals(feature.getTaskType())||"taskconfigure".equals(feature.getTaskType())) {
								requirementFeatureService.updateTaskDataItmp(feature,map.get("reqId").toString());
							}else if("tasktest".equals(feature.getTaskType())){ //测试任务
								requirementFeatureService.updateTaskDataTmp(feature, map.get("reqId").toString());
							}else{//其他全部当作取消
								feature.setRequirementFeatureStatus("00");//设置为取消状态
								requirementFeatureService.updateStatusItmp(feature);
								requirementFeatureService.updateStatusByTmp(feature);
							}
						}
					}
					//发送消息
					String string = map.get("requirementInfo").toString();
					TblRequirementInfo requirementInfo = JSONObject.parseObject(string, TblRequirementInfo.class); //把字符串转换成list
					String str = map.get("synStatus").toString();

					List<TblRequirementSystem> list = tblRequirementSystemMapper.getReqSystemByReqId(requirementInfo.getId());
					requirementInfo.setList(list);
					if(str.equals("insert")) {
						requirementService.sendAddMessage(request, requirementInfo);
						//给项目管理岗发送邮件和微信 --ztt
						Map<String,Object> emWeMap = new HashMap<String, Object>();
						emWeMap.put("messageTitle", "【IT开发测试管理系统】- 收到一个新分配的需求");
						emWeMap.put("messageContent","您收到一个新的需求：“"+ requirementInfo.getRequirementCode()+" | "+requirementInfo.getRequirementName()+"，请及时处理。”");
						String userIds = requirementMapper.getProManageUserIds(requirementInfo.getId());//获取该需求所在系统所在项目的项目管理岗
						emWeMap.put("messageReceiver",userIds);//接收人 项目管理岗
						emWeMap.put("sendMethod", 3);//发送方式 3 邮件和微信
						projectToSystemInterface.sendMessage(JSON.toJSONString(emWeMap));
					} else if(str.equals("update")) {
						TblRequirementAttention attention = new TblRequirementAttention();
						attention.setRequirementId(requirementInfo.getId());
						attention.setStatus(1);
						List<TblRequirementAttention> attentionList = requirementService.getAttentionList(attention);
						if (attentionList != null && attentionList.size() > 0) {
							requirementService.sendEditMessage(request, requirementInfo, attentionList);
						}
					}
				}
			}
           //组装返回报文体
			Map<String,Object> head= new HashMap<>();
			Map<String,Object> map1= new HashMap<>();
			map1.put("consumerSeqNo","itmgr");
			map1.put("status",0);
			map1.put("seqNo","");
			map1.put("providerSeqNo","");
			map1.put("esbCode","");
			map1.put("esbMessage","");
			map1.put("appCode","0");
			map1.put("appMessage","同步需求信息成功");
			head.put("responseHead",map1);
			PrintWriter writer = response.getWriter();
			writer.write(new JSONObject(head).toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
			response.setHeader("appCode", "500");
			response.setHeader("appMessage", e.getMessage());
			log.error("同步出错" + ":" + e.getMessage(), e);
		}
	}

	/**
	 *@Description 新增需求
	 *@Date 2020/7/21
	 *@Param [requirementInfo] 需求对象
	 *@Param [reqSysList] 需求系统对象String
	 *@Param [startDate] 开始事件
	 *@Param [endDate] 结束时间
	 *@Param [workload1] 工作量
	 *@Param [time]
	 *@return Map<String,Object> status=repeat：需求重复 003:系统和模块重复 ；success:成功
	 **/
	@RequestMapping(value = "addRequirement", method = RequestMethod.POST)
	public Map<String, Object> addRequirement(HttpServletRequest request,TblRequirementInfo requirementInfo,String reqSysList,
											  String startDate, String endDate,String workload1,String time) {
//		JSONObject.parseObject(reqSysList, TblRequirementSystem.class);
		List<TblRequirementSystem> array = JSONObject.parseArray(reqSysList, TblRequirementSystem.class);
		requirementInfo.setList(array);
		Map<String, Object> map = new HashMap<>();
		List<TblRequirementInfo> list = requirementService.findRequirementByName(requirementInfo);
		if (!list.isEmpty()) {
			//需求重复
			map.put("status", "repeat");
			return map;
		}
		List<TblRequirementSystem> reqsysList = requirementInfo.getList();
		if(reqsysList != null && reqsysList.size()>1) {
			for(int i = 0; i < reqsysList.size(); i ++) {
				for(int j = i+1; j < reqsysList.size(); j ++) {
					if(reqsysList.get(i).getSystemId().equals(reqsysList.get(j).getSystemId()) &&
							reqsysList.get(i).getAssetSystemTreeId().equals(reqsysList.get(j).getAssetSystemTreeId())) {
						//系统和模块重复
						map.put("status", "003");
						return map;
					}
				}
			}
		}
		DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (startDate != null && !"".equals(startDate)) {
				Date planStartDate = dFormat.parse(startDate);
				requirementInfo.setPlanOnlineDate(planStartDate);
			}
			if (endDate != null && !"".equals(endDate)) {
				Date planEndDate = dFormat.parse(endDate);
				requirementInfo.setActualOnlineDate(planEndDate);
			}
			if (workload1 != null && !"".equals(workload1)) {
				requirementInfo.setWorkload(Double.valueOf(workload1));
			}
			if(time != null && !time.equals("")) {
				SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date date = datetimeFormatter1.parse(time);
				Timestamp closeTime = new Timestamp(date.getTime());
				requirementInfo.setCloseTime(closeTime);
			}
			map=requirementService.insertRequirementItmp(requirementInfo, request);
			//添加消息
			requirementService.sendAddMessage(request,requirementInfo);
			//成功
			map.put("status", "success");
		} catch (Exception e) {
			return super.handleException(e, "新增需求失败");

		}
		return map;
	}



	/**
	 *@Description 编辑需求
	 *@Date 2020/7/21
	 *@Param [requirementInfo] 需求对象
	 *@Param [reqSysList] 需求系统对象String
	 *@Param [startDate] 开始事件
	 *@Param [endDate] 结束时间
	 *@Param [workload1] 工作量
	 *@Param [time]
	 *@return Map<String,Object> status=003：系统和模块重复，success:成功
	 **/
	@RequestMapping(value = "editRequirement", method = RequestMethod.POST)
	public Map<String, Object> editRequirement(HttpServletRequest request,TblRequirementInfo requirementInfo,String reqSysList,
											   String startDate, String endDate,String workload1,String time) {
		List<TblRequirementSystem> array = JSONObject.parseArray(reqSysList, TblRequirementSystem.class);
		requirementInfo.setList(array);
		Map<String, Object> map = new HashMap<>();
		List<TblRequirementSystem> reqsysList = requirementInfo.getList();
		if(reqsysList != null && reqsysList.size()>1) {
			for(int i = 0; i < reqsysList.size(); i ++) {
				for(int j = i+1; j < reqsysList.size(); j ++) {
					if(reqsysList.get(i).getSystemId().equals(reqsysList.get(j).getSystemId()) &&
							reqsysList.get(i).getAssetSystemTreeId().equals(reqsysList.get(j).getAssetSystemTreeId())) {
						//系统和模块重复
						map.put("status", "003");
						return map;
					}
				}
			}
		}
		DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (startDate != null && !"".equals(startDate)) {
				Date planStartDate = dFormat.parse(startDate);
				requirementInfo.setPlanOnlineDate(planStartDate);
			}
			if (endDate != null && !"".equals(endDate)) {
				Date planEndDate = dFormat.parse(endDate);
				requirementInfo.setActualOnlineDate(planEndDate);
			}
			if (workload1 != null && !"".equals(workload1)) {
				requirementInfo.setWorkload(Double.valueOf(workload1));
			}
			if(time != null && !time.equals("")) {
				SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date date = datetimeFormatter1.parse(time);
				Timestamp closeTime = new Timestamp(date.getTime());
				requirementInfo.setCloseTime(closeTime);
			}
			map=requirementService.updateRequirementItmp(requirementInfo, request);
			map.put("status", "success");

			//关注的需求有更新发送消息
			TblRequirementAttention attention = new TblRequirementAttention();
			attention.setRequirementId(requirementInfo.getId());
			attention.setStatus(1);
			List<TblRequirementAttention> attentionList = requirementService.getAttentionList(attention);
			if (attentionList != null && attentionList.size() > 0) {
				//编辑发送消息
				requirementService.sendEditMessage(request, requirementInfo, attentionList);
			}

		} catch (Exception e) {
			return super.handleException(e, "新增需求失败");
		}
		return map;
	}

	/**
	 *@Description 上传附件
	 *@Date 2020/7/21
	 *@Param [files] 附件信息
	 *@Param [reqId] 需求id
	 *@return Map<String,Object> status=1正常，2异常
	 **/
	@RequestMapping(value = "uploadFile")
	public Map<String, Object> uploadFile(@RequestParam("files") MultipartFile[] files,
										  @RequestParam("reqId") Long reqId, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		try {
			requirementService.uploadFileItmp(files, reqId, request);
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			return handleException(e, "上传需求附件失败");
		}
		return result;
	}

	/**
	 *@Description 移除附件
	 *@Date 2020/7/21
	 *@Param [ids] 需求附件表id
	 *@return Map<String,Object> status=1正常，2异常
	 **/
	@RequestMapping(value = "removeAtt",method = RequestMethod.POST)
	public Map<String, Object> removeAtt(Long[] ids,HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		TblRequirementAttachement atta=new TblRequirementAttachement();
		try {
			if (ids == null){
				return handleException(new Exception(), "删除附件失败");
			}
			for(int i=0;i<ids.length;i++) {
				atta.setId(ids[i]);
				requirementService.removeAttItmp(atta,request);
			}
		} catch (Exception e) {
			return handleException(e, "删除附件失败");
		}
		return result;
	}

	/**
	 *@Description 下载附件
	 *@Date 2020/7/21
	 *@Param [atta] 需求附件对象
	 **/
	@RequestMapping(value= "downloadFile")
	public void downloadFile(TblRequirementAttachement atta,HttpServletResponse response) {
		try {
			s3Util.downObject(atta.getFileS3Bucket(), atta.getFileS3Key(), atta.getFileNameOld(), response);
		}catch (Exception e) {
			e.printStackTrace();
			log.error("mes:" + e.getMessage(), e);
		}
	}
	/**
	 *@Description 导出
	 *@Date 2020/7/21
	 *@Param [findRequirment] 查询条件对象字符串
	 *@Param [uid]	用户id
	 *@Param [roleCodes] 角色权限集合
	 *@return java.util.ArrayList<TblRequirementInfo> 需求信息列表
	 **/
	@RequestMapping(value="getExcelRequirement",method=RequestMethod.POST)
	public List<TblRequirementInfo> getExcelRequirement(String findRequirment,Long uid,@RequestBody List<String> roleCodes){
		TblRequirementInfo requirement = new TblRequirementInfo();
		List<TblRequirementInfo> list =new ArrayList<TblRequirementInfo>();
		try {
			if (StringUtils.isNotBlank(findRequirment)) {
				requirement = JSONObject.parseObject(findRequirment, TblRequirementInfo.class);
			}
			requirement.setUserId(uid);
			list = requirementService.getAllRequirement(requirement, null, null,roleCodes);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("mes:" + e.getMessage(), e);
		}
		return list;
	}

	/**
	 *@Description bootstrap 需求弹窗
	 *@Date 2020/7/21
	 *@Param [requirement] 查询条件对象
	 *@Param [pageSize] 每页记录数
	 *@Param [pageNumber] 第几页
	 *@return Map<String,Object> status=1正常，2异常
	 **/
	@RequestMapping(value="getAllRequirement2",method=RequestMethod.POST)
	public Map<String, Object> getAllRequirement2(HttpServletRequest request,TblRequirementInfo requirement,Integer pageSize,Integer pageNumber){
		Map<String, Object> map = new HashMap<>();
		try {
			int count= requirementService.getAllRequirementCount(requirement);
			List<Map<String, Object>> list = requirementService.getAllRequirement2(requirement, pageNumber, pageSize);
			//总数
			map.put("total", count);
			//当前页数据
			map.put("rows", list);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("mes:" + e.getMessage(), e);
		}
		return map;
	}

	/**
	 *@Description 获取拓展字段
	 *@Date 2020/7/21
	 *@return Map<String,Object> fields:扩展字段数据
	 **/
	@RequestMapping(value="getRequirementFiled")
	public  Map<String,Object> getRequirementFiled(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();

		List<ExtendedField> extendedFields=requirementService.findRequirementField(null);
		map.put("fields", extendedFields);
		return  map;

	}

	/**
	 *@Description 获取需求自定义字段
	 *@Date 2020/7/21
	 *@Param [id] 需求id
	 *@return Map<String,Object> fields:扩展字段数据
	 **/
	@RequestMapping(value="getRequirementFiled2")
	public  Map<String,Object> getRequirementFiled2(Long id){
		Map<String,Object> map = new HashMap<String,Object>();

		List<ExtendedField> extendedFields=requirementService.findRequirementField(id);
		map.put("fields", extendedFields);
		return  map;

	}

	/**
	 *@Description 获取需求编号
	 *@Date 2020/7/21
	 *@Param [reqIds] 需求ID集合
	 *@return  List<String>需求编码集合
	 **/
	@RequestMapping(value="getRequirementsByIds",method=RequestMethod.POST)
	public List<String> getRequirementsByIds(String reqIds){
		List<String> list = new ArrayList<>();
		try {
			list = requirementService.getRequirementsByIds(reqIds);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("mes:" + e.getMessage(), e);
		}
		return list;
	}

	/**
	 *@Description 获取系统
	 *@Date 2020/7/21
	 *@Param [id] 需求id
	 *@return List<String>系统名集合
	 **/
	@RequestMapping(value="getsystems",method=RequestMethod.POST)
	public List<String> getsystems(Long id){
		List<String> list = new ArrayList<>();
		try {
			list = requirementService.getsystems(id);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("mes:" + e.getMessage(), e);
		}
		return list;
	}

	/**
	 * 
	* @Title: synItcdAttr
	* @Description: 前台同步IT全流程附件
	* @author author
	* @param ids 需求id
	* @param request
	* @return Map<String,Object>  status=success:成功
	 */
	@RequestMapping(value="synItcdAtta",method=RequestMethod.POST)
	public Map<String, Object> synItcdAttr(@RequestBody Long []ids,HttpServletRequest request){
		Map<String, Object> map = requirementDocumentService.synItcdAttr(ids,request);
		return map;
	}

	/**
	 *@Description 导入需求附件信息
	 *@Date 2020/7/21
	 *@Param [file] 附件
	 *@return Map<String,Object> status=1正常，2异常
	 **/
	@RequestMapping(value="importItcdAtta",method=RequestMethod.POST)
	public Map<String, Object> importItcdAtta(MultipartFile file, HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		String fileName = file.getOriginalFilename();
		if (fileName.matches("^.+\\.(?i)(xls)$") || fileName.matches("^.+\\.(?i)(xlsx)$")) {
			map =requirementDocumentService.importExcel(file, request);
		}else if(fileName.matches("^.+\\.(?i)(csv)$")){
			map =requirementDocumentService.importCsv(file, request);
		}else{
			map.put("status", Constants.ITMP_RETURN_FAILURE);
			map.put("errorMessage", "上传文件格式出错");
			return map;
		}
		return map;
	}

	/**
	 *@Description 同步需求附件信息
	 *@Date 2020/7/21
	 *@Param [itcdAttaData] 报文
	 **/
	@RequestMapping(value="updataItcdAttr",method=RequestMethod.POST)
	public void updataItcdAttr(@RequestBody String itcdAttaData,HttpServletResponse response) throws IOException {
		if (!StringUtils.isBlank(itcdAttaData)) {
			Map<String, Object> data = jsonToMap(jsonToMap(itcdAttaData).get("requestBody").toString());
			int status = requirementDocumentService.updataItcdAttr(data);
			if(status>0) {
				Map<String,Object> head= new HashMap<>();
				Map<String,Object> map1= new HashMap<>();
				map1.put("consumerSeqNo","itmgr");
				map1.put("status",0);
				map1.put("seqNo","");
				map1.put("providerSeqNo","");
				map1.put("esbCode","");
				map1.put("esbMessage","");
				map1.put("appCode","0");
				map1.put("appMessage","同步需求附件信息成功");
				head.put("responseHead",map1);
				PrintWriter writer = response.getWriter();
				writer.write(new JSONObject(head).toJSONString());
			}
		}
	}

	//json转map
	public static Map<String, Object> jsonToMap(String str) {
		Map<String, Object> mapTypes = JSON.parseObject(str);
		return mapTypes;
	}

	//json字符串转换为开发任务实体
	public List<TblRequirementFeature> strToFeature(String str) {
		List<TblRequirementFeature> featureList=new ArrayList<>();
		List<SynRequirementFeature> resultList = JSONObject.parseArray(str, SynRequirementFeature.class);
		for (SynRequirementFeature synFeature : resultList) {
			TblRequirementFeature trf = SynRequirementFeatureUtil.SynRequirementFeatures(synFeature);
			featureList.add(trf);
		}
		return featureList;
	}
}
