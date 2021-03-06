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
* @Description: ????????????
* @author author
* @date 2020???10???13??? ??????4:32:54
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
	* @Description: ??????????????????
	* @author author
	* @param request
	* @param findRequirment ?????????????????????
	* @param rows  ????????????
	* @param page  ?????????
	* @return Map<String,Object> status=1?????????2??????
	 */
	@RequestMapping(value="getAllRequirement",method=RequestMethod.POST)
	public String getAllRequirement(HttpServletRequest request,String findRequirment,Integer rows,Integer page){
		JSONObject jsonObj = new JSONObject();
		TblRequirementInfo requirement = new TblRequirementInfo();
		try {
			Long uid = CommonUtil.getCurrentUserId(request);
			LinkedHashMap map = (LinkedHashMap) redisUtils.get(CommonUtil.getToken(request));
			List<String> roleCodes = (List<String>) map.get("roles");//????????????????????????????????????
			if (StringUtils.isNotBlank(findRequirment)) {
				requirement = JSONObject.parseObject(findRequirment, TblRequirementInfo.class);
			}
			requirement.setUserId(uid);
			int count= requirementService.getCountRequirement(requirement,roleCodes);
			List<TblRequirementInfo> list = requirementService.getAllRequirement(requirement, page, rows,roleCodes);
			jsonObj.put("page", page);//?????????
			if(rows!=null) {
				jsonObj.put("total", (count+rows-1)/rows);//?????????
			}
			jsonObj.put("records", count); //?????????
			jsonObj.put("rows", list); //??????????????????
		} catch (Exception e) {
			e.printStackTrace();
			log.error("mes:" + e.getMessage(), e);
		}
		return jsonObj.toJSONString();
	}

	/**
	 * 
	* @Title: getFunctionCountByReqId
	* @Description: ?????????????????????
	* @author author
	* @param request
	* @param reqId ??????ID
	* @return map key:data  value:int ?????????????????????
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
	* @Description: ??????????????????
	* @author author
	* @param request
	* @param rIds ??????ID 
	* @param parentIds ?????????ID
	* @return map key:triFather        value:TblRequirementInfo?????????
	*                 data                   TblRequirementInfo???????????????data.list:List<TblRequirementSystem>??????????????????????????????
	*                 attentionStatus        ????????????1?????????2?????????
	*                 trf                    List<TblRequirementFeature>?????????????????????????????????
	*                 fields                 List<ExtendedField>?????????????????????
	*                 triSon                 List<TblRequirementInfo>?????????????????????
	*                 attachements           List<TblRequirementAttachement>??????????????????
	*                 status                 1???????????????2????????????
	*                 
	* 
	 */
	@RequestMapping(value = "findRequirementById", method = RequestMethod.POST)
	public Map<String, Object> findRequirementById(HttpServletRequest request, Long rIds,Long parentIds) {
		Map<String, Object> result = new HashMap<>();
		try {
			//??????
			result = requirementService.findRequirementById(rIds,parentIds);
			//????????????????????????
			List<TblRequirementFeature> trf = requirementFeatureService.findFeatureByRequirementId(rIds);
			//??????????????????
			Long userId = CommonUtil.getCurrentUserId(request);
			TblRequirementAttention attention = new TblRequirementAttention();
			attention.setRequirementId(rIds);
			attention.setUserId(userId);
			attention.setStatus(1);
			List<TblRequirementAttention> attentionList = requirementService.getAttentionList(attention);
			//??????????????????????????????????????????
			if (attentionList != null && attentionList.size() > 0) {
				result.put("attentionStatus", 1);
			} else {
				result.put("attentionStatus", 2);//?????????
			}
			//??????????????????
			List<ExtendedField> extendedFields=requirementService.findRequirementField(rIds);
			result.put("fields", extendedFields);//????????????

			result.put("status", Constants.ITMP_RETURN_SUCCESS);
			result.put("trf",trf);
		} catch (Exception e) {
			return handleException(e, "??????????????????");
		}
		return result;
	}

	/**
	 * 
	* @Title: changeAttention
	* @Description: ??????????????????????????????????????????????????????????????????????????????????????????????????????
	* @author author
	* @param request
	* @param id ?????????ID
	* @param attentionStatus ????????????
	* @return Map<String,Object> status=1?????????2??????
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
			return super.handleException(e, "??????????????????");
		}
		return map;
	}

	/**
	 * 
	* @Title: toEditRequirementById
	* @Description: ????????????
	* @author author
	* @param rIds ??????ID
	* @return Map<String,Object> status=1?????????2??????
	 */
	@RequestMapping(value = "toEditRequirementById", method = RequestMethod.POST)
	public Map<String, Object> toEditRequirementById(Long rIds) {
		Map<String, Object> result = new HashMap<>();
		try {
			//????????????
			result = requirementService.toEditRequirementById(rIds);
			//????????????
			List<TblRequirementAttachement> attachements = requirementService.getRequirementAttachement(rIds);

			//??????????????????
			List<ExtendedField> extendedFields=requirementService.findRequirementField(rIds);
			result.put("fields", extendedFields); //????????????
			result.put("attachements", attachements); //????????????
			result.put("status", Constants.ITMP_RETURN_SUCCESS);//1???????????????2????????????
		} catch (Exception e) {
			return handleException(e, "??????????????????");
		}
		return result;
	}

	/**
	 * 
	* @Title: getDataDicList
	* @Description: ????????????????????????
	* @author author
	* @param datadictype ????????????????????????????????????term_code
	* @return Map<String,Object> status=1?????????2??????
	 */
	@RequestMapping(value = "getDataDicList", method = RequestMethod.POST)
	public List<TblDataDic> getDataDicList(String datadictype) {
		String termCode="";
		List<TblDataDic> resultList= new ArrayList<>();
		try {
			if(datadictype!=null&&datadictype.equals("reqStatus")) {//????????????
				termCode=DicConstants.req_status;
			}
			if(datadictype!=null&&datadictype.equals("reqSource")) {//????????????
				termCode=DicConstants.req_source;
			}
			if(datadictype!=null&&datadictype.equals("reqType")) {//????????????
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
	* @Description: ??????IT???????????????
	* @author author
	* @param requirementData ????????????
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
	* @Description: ????????????
	* @author author
	* @param requirementData ??????????????????
	* @param request
	* @param response
	 */
	public void synReq(String requirementData,HttpServletRequest request,HttpServletResponse response) {
		try {
			logger.info("??????????????????????????????"+requirementData);
			if (!StringUtils.isBlank(requirementData)) {
				Map<String, Object> data = jsonToMap(jsonToMap(requirementData).get("requestBody").toString());
				String requirementList = data.get("requirementList").toString();
				String reqSystemList = data.get("reqSystemList").toString();
				List<TblRequirementFeature> featureList = strToFeature(data.get("taskList").toString());
				//??????????????????
				Map<String,Object> map = requirementService.updateRequirementDataItmp(requirementList,reqSystemList);
				if(map!=null){
					//????????????????????????????????????
					if(featureList.size()==0){
						requirementFeatureService.addItmpFeatureInfo(reqSystemList,map.get("reqId").toString());
					}else{
						for(TblRequirementFeature feature:featureList) {
							//??????????????????????????????
							if("taskdevelop".equals(feature.getTaskType())||"taskconfigure".equals(feature.getTaskType())) {
								requirementFeatureService.updateTaskDataItmp(feature,map.get("reqId").toString());
							}else if("tasktest".equals(feature.getTaskType())){ //????????????
								requirementFeatureService.updateTaskDataTmp(feature, map.get("reqId").toString());
							}else{//????????????????????????
								feature.setRequirementFeatureStatus("00");//?????????????????????
								requirementFeatureService.updateStatusItmp(feature);
								requirementFeatureService.updateStatusByTmp(feature);
							}
						}
					}
					//????????????
					String string = map.get("requirementInfo").toString();
					TblRequirementInfo requirementInfo = JSONObject.parseObject(string, TblRequirementInfo.class); //?????????????????????list
					String str = map.get("synStatus").toString();

					List<TblRequirementSystem> list = tblRequirementSystemMapper.getReqSystemByReqId(requirementInfo.getId());
					requirementInfo.setList(list);
					if(str.equals("insert")) {
						requirementService.sendAddMessage(request, requirementInfo);
						//??????????????????????????????????????? --ztt
						Map<String,Object> emWeMap = new HashMap<String, Object>();
						emWeMap.put("messageTitle", "???IT???????????????????????????- ??????????????????????????????");
						emWeMap.put("messageContent","?????????????????????????????????"+ requirementInfo.getRequirementCode()+" | "+requirementInfo.getRequirementName()+"????????????????????????");
						String userIds = requirementMapper.getProManageUserIds(requirementInfo.getId());//?????????????????????????????????????????????????????????
						emWeMap.put("messageReceiver",userIds);//????????? ???????????????
						emWeMap.put("sendMethod", 3);//???????????? 3 ???????????????
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
           //?????????????????????
			Map<String,Object> head= new HashMap<>();
			Map<String,Object> map1= new HashMap<>();
			map1.put("consumerSeqNo","itmgr");
			map1.put("status",0);
			map1.put("seqNo","");
			map1.put("providerSeqNo","");
			map1.put("esbCode","");
			map1.put("esbMessage","");
			map1.put("appCode","0");
			map1.put("appMessage","????????????????????????");
			head.put("responseHead",map1);
			PrintWriter writer = response.getWriter();
			writer.write(new JSONObject(head).toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
			response.setHeader("appCode", "500");
			response.setHeader("appMessage", e.getMessage());
			log.error("????????????" + ":" + e.getMessage(), e);
		}
	}

	/**
	 *@Description ????????????
	 *@Date 2020/7/21
	 *@Param [requirementInfo] ????????????
	 *@Param [reqSysList] ??????????????????String
	 *@Param [startDate] ????????????
	 *@Param [endDate] ????????????
	 *@Param [workload1] ?????????
	 *@Param [time]
	 *@return Map<String,Object> status=repeat??????????????? 003:????????????????????? ???success:??????
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
			//????????????
			map.put("status", "repeat");
			return map;
		}
		List<TblRequirementSystem> reqsysList = requirementInfo.getList();
		if(reqsysList != null && reqsysList.size()>1) {
			for(int i = 0; i < reqsysList.size(); i ++) {
				for(int j = i+1; j < reqsysList.size(); j ++) {
					if(reqsysList.get(i).getSystemId().equals(reqsysList.get(j).getSystemId()) &&
							reqsysList.get(i).getAssetSystemTreeId().equals(reqsysList.get(j).getAssetSystemTreeId())) {
						//?????????????????????
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
			//????????????
			requirementService.sendAddMessage(request,requirementInfo);
			//??????
			map.put("status", "success");
		} catch (Exception e) {
			return super.handleException(e, "??????????????????");

		}
		return map;
	}



	/**
	 *@Description ????????????
	 *@Date 2020/7/21
	 *@Param [requirementInfo] ????????????
	 *@Param [reqSysList] ??????????????????String
	 *@Param [startDate] ????????????
	 *@Param [endDate] ????????????
	 *@Param [workload1] ?????????
	 *@Param [time]
	 *@return Map<String,Object> status=003???????????????????????????success:??????
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
						//?????????????????????
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

			//????????????????????????????????????
			TblRequirementAttention attention = new TblRequirementAttention();
			attention.setRequirementId(requirementInfo.getId());
			attention.setStatus(1);
			List<TblRequirementAttention> attentionList = requirementService.getAttentionList(attention);
			if (attentionList != null && attentionList.size() > 0) {
				//??????????????????
				requirementService.sendEditMessage(request, requirementInfo, attentionList);
			}

		} catch (Exception e) {
			return super.handleException(e, "??????????????????");
		}
		return map;
	}

	/**
	 *@Description ????????????
	 *@Date 2020/7/21
	 *@Param [files] ????????????
	 *@Param [reqId] ??????id
	 *@return Map<String,Object> status=1?????????2??????
	 **/
	@RequestMapping(value = "uploadFile")
	public Map<String, Object> uploadFile(@RequestParam("files") MultipartFile[] files,
										  @RequestParam("reqId") Long reqId, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		try {
			requirementService.uploadFileItmp(files, reqId, request);
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			return handleException(e, "????????????????????????");
		}
		return result;
	}

	/**
	 *@Description ????????????
	 *@Date 2020/7/21
	 *@Param [ids] ???????????????id
	 *@return Map<String,Object> status=1?????????2??????
	 **/
	@RequestMapping(value = "removeAtt",method = RequestMethod.POST)
	public Map<String, Object> removeAtt(Long[] ids,HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		TblRequirementAttachement atta=new TblRequirementAttachement();
		try {
			if (ids == null){
				return handleException(new Exception(), "??????????????????");
			}
			for(int i=0;i<ids.length;i++) {
				atta.setId(ids[i]);
				requirementService.removeAttItmp(atta,request);
			}
		} catch (Exception e) {
			return handleException(e, "??????????????????");
		}
		return result;
	}

	/**
	 *@Description ????????????
	 *@Date 2020/7/21
	 *@Param [atta] ??????????????????
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
	 *@Description ??????
	 *@Date 2020/7/21
	 *@Param [findRequirment] ???????????????????????????
	 *@Param [uid]	??????id
	 *@Param [roleCodes] ??????????????????
	 *@return java.util.ArrayList<TblRequirementInfo> ??????????????????
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
	 *@Description bootstrap ????????????
	 *@Date 2020/7/21
	 *@Param [requirement] ??????????????????
	 *@Param [pageSize] ???????????????
	 *@Param [pageNumber] ?????????
	 *@return Map<String,Object> status=1?????????2??????
	 **/
	@RequestMapping(value="getAllRequirement2",method=RequestMethod.POST)
	public Map<String, Object> getAllRequirement2(HttpServletRequest request,TblRequirementInfo requirement,Integer pageSize,Integer pageNumber){
		Map<String, Object> map = new HashMap<>();
		try {
			int count= requirementService.getAllRequirementCount(requirement);
			List<Map<String, Object>> list = requirementService.getAllRequirement2(requirement, pageNumber, pageSize);
			//??????
			map.put("total", count);
			//???????????????
			map.put("rows", list);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("mes:" + e.getMessage(), e);
		}
		return map;
	}

	/**
	 *@Description ??????????????????
	 *@Date 2020/7/21
	 *@return Map<String,Object> fields:??????????????????
	 **/
	@RequestMapping(value="getRequirementFiled")
	public  Map<String,Object> getRequirementFiled(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();

		List<ExtendedField> extendedFields=requirementService.findRequirementField(null);
		map.put("fields", extendedFields);
		return  map;

	}

	/**
	 *@Description ???????????????????????????
	 *@Date 2020/7/21
	 *@Param [id] ??????id
	 *@return Map<String,Object> fields:??????????????????
	 **/
	@RequestMapping(value="getRequirementFiled2")
	public  Map<String,Object> getRequirementFiled2(Long id){
		Map<String,Object> map = new HashMap<String,Object>();

		List<ExtendedField> extendedFields=requirementService.findRequirementField(id);
		map.put("fields", extendedFields);
		return  map;

	}

	/**
	 *@Description ??????????????????
	 *@Date 2020/7/21
	 *@Param [reqIds] ??????ID??????
	 *@return  List<String>??????????????????
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
	 *@Description ????????????
	 *@Date 2020/7/21
	 *@Param [id] ??????id
	 *@return List<String>???????????????
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
	* @Description: ????????????IT???????????????
	* @author author
	* @param ids ??????id
	* @param request
	* @return Map<String,Object>  status=success:??????
	 */
	@RequestMapping(value="synItcdAtta",method=RequestMethod.POST)
	public Map<String, Object> synItcdAttr(@RequestBody Long []ids,HttpServletRequest request){
		Map<String, Object> map = requirementDocumentService.synItcdAttr(ids,request);
		return map;
	}

	/**
	 *@Description ????????????????????????
	 *@Date 2020/7/21
	 *@Param [file] ??????
	 *@return Map<String,Object> status=1?????????2??????
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
			map.put("errorMessage", "????????????????????????");
			return map;
		}
		return map;
	}

	/**
	 *@Description ????????????????????????
	 *@Date 2020/7/21
	 *@Param [itcdAttaData] ??????
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
				map1.put("appMessage","??????????????????????????????");
				head.put("responseHead",map1);
				PrintWriter writer = response.getWriter();
				writer.write(new JSONObject(head).toJSONString());
			}
		}
	}

	//json???map
	public static Map<String, Object> jsonToMap(String str) {
		Map<String, Object> mapTypes = JSON.parseObject(str);
		return mapTypes;
	}

	//json????????????????????????????????????
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
