package cn.pioneeruniverse.dev.service.workTask.impl;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.common.databus.DataBusRequestHead;
import cn.pioneeruniverse.common.databus.DataBusUtil;
import cn.pioneeruniverse.common.entity.JqGridPage;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;

import cn.pioneeruniverse.common.bean.PropertyInfo;
import cn.pioneeruniverse.common.bean.ReflectFieledType;
import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.utils.CollectionUtil;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.CommonUtils;
import cn.pioneeruniverse.common.utils.JsonUtil;
import cn.pioneeruniverse.common.utils.PageWithBootStrap;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.dev.dao.mybatis.AllDevRequirementMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblCommissioningWindowMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDefectInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblProjectGroupUserMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementFeatureLogMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementFeatureMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblTestSetCaseExecuteMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblTestSetCaseMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblTestTaskAttachementMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblTestTaskLogAttachementMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblTestTaskLogMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblTestTaskMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblUserInfoMapper;
import cn.pioneeruniverse.dev.entity.TblDataDic;
import cn.pioneeruniverse.dev.entity.TblDefectInfo;
import cn.pioneeruniverse.dev.entity.TblDefectLog;
import cn.pioneeruniverse.dev.entity.TblRequirementFeature;
import cn.pioneeruniverse.dev.entity.TblRequirementFeatureLog;
import cn.pioneeruniverse.dev.entity.TblRequirementInfo;
import cn.pioneeruniverse.dev.entity.TblSystemInfo;
import cn.pioneeruniverse.dev.entity.TblTestTask;
import cn.pioneeruniverse.dev.entity.TblTestTaskAttachement;
import cn.pioneeruniverse.dev.entity.TblTestTaskLog;
import cn.pioneeruniverse.dev.entity.TblTestTaskLogAttachement;
import cn.pioneeruniverse.dev.entity.TblUserInfo;
import cn.pioneeruniverse.dev.feignInterface.TestManageToSystemInterface;
import cn.pioneeruniverse.dev.service.defect.DefectService;
import cn.pioneeruniverse.dev.service.workTask.WorkTaskService;
import cn.pioneeruniverse.dev.vo.DevDetailVo;
import cn.pioneeruniverse.dev.vo.TblTestWorkVo;
import cn.pioneeruniverse.dev.vo.TestTaskInputVo;
import cn.pioneeruniverse.dev.vo.TestWorkInputVo;

@Service("workTaskService")
public class WorkTaskServiceImpl implements WorkTaskService {
	public static final String SOURCE = "TBL_REQUIREMENT_INFO_REQUIREMENT_SOURCE";

	public static final String TYPE = "TBL_REQUIREMENT_INFO_REQUIREMENT_TYPE";

	public static final String PRIORITY = "TBL_REQUIREMENT_INFO_REQUIREMENT_PRIORITY";
	public static final String RETYPE = "TBL_REQUIREMENT_INFO_REQUIREMENT_TYPE";
	public static final String PLAN = "TBL_REQUIREMENT_INFO_REQUIREMENT_PLAN";
	public static final String STATUS = "TBL_REQUIREMENT_INFO_REQUIREMENT_STATUS";
	// ???????????????????????????????????????
	private final List<String> listType = ReflectFieledType.listType;

	@Autowired
	private TblTestTaskMapper tblTestTaskMapper;
	@Autowired
	private RedisUtils redisUtils;
	@Autowired
	private TblRequirementInfoMapper requirementInfoMapper;
	@Autowired
	private TblSystemInfoMapper SystemInfoMapper;
	@Autowired
	private AllDevRequirementMapper allDevRequirementMapper;
	@Autowired
	private TblTestTaskLogMapper tblTestTaskLogMapper;
	@Autowired
	private TblTestTaskLogAttachementMapper tblTestTaskLogAttachementMapper;
	@Autowired
	private TblTestTaskAttachementMapper tblTestTaskAttachementMapper;
	@Autowired
	private TblRequirementFeatureMapper tblRequirementFeatureMapper;
	@Autowired
	private TblUserInfoMapper tblUserInfoMapper;
	@Autowired
	private TblDefectInfoMapper tblDefectInfoMapper;
	@Autowired
	private TblTestSetCaseMapper tblTestSetCaseMapper;
	@Autowired
	private DefectService defectService;
	@Autowired
	private TblTestSetCaseExecuteMapper tblTestSetCaseExecuteMapper;
	@Autowired
	private TblProjectGroupUserMapper tblProjectGroupUserMapper;
	@Autowired
	private TblRequirementFeatureLogMapper requirementFeatureLogMapper;
	@Value("${databuscc.name}")
	private String databusccName;
	@Autowired
	private TblRequirementFeatureMapper requirementFeatureMapper;
	@Autowired
	private TblSystemInfoMapper systemInfoMapper;
	@Autowired
	private TblCommissioningWindowMapper commissioningWindowMapper;
	@Autowired
	private TestManageToSystemInterface testManageToSystemInterface;
	
	/**
	 * 
	* @Title: getTestTask
	* @Description: ????????????????????????
	* @author author
	* @param sidx ????????????
	* @param sord ???????????????
	* @param tblTestTask ????????????
	* @param jqGridPage ????????????
	* @param page ?????????
	* @param rows ????????????
	* @param request
	* @return Map<String, Object>
	 */
	@Override
	@Transactional(readOnly = true)
	public Map getTestTask(String sidx, String sord,TblTestWorkVo tblTestTask,JqGridPage<TblTestWorkVo> jqGridPage, Integer page, Integer rows,
			HttpServletRequest request) {
		List<TblTestTask> list = new ArrayList<>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> parm = new HashMap<String, Object>();
		//Map<String, Object> filterMap = new HashMap<String, Object>();
		List<Integer> Array = new ArrayList();
		try {
			if (page != null && rows != null) {
				
				jqGridPage.filtersAttrToEntityField(tblTestTask);
				LinkedHashMap map = (LinkedHashMap) redisUtils.get(CommonUtil.getToken(request));
				List<String> roleCodes = (List<String>) map.get("roles");
				Map<String, Object> paraMap=filterSearch(tblTestTask);
				if(paraMap.containsKey("status")) {
					result.put("status", paraMap.get("status").toString());
					result.put("message", paraMap.get("message").toString());
					return result;
				}
				if(paraMap.get("testWorkInputVo") != null) {
					TestWorkInputVo testWorkInputVo = JSON.parseObject(paraMap.get("testWorkInputVo").toString(), TestWorkInputVo.class) ;
					testWorkInputVo.setSidx(sidx);
					testWorkInputVo.setSord(sord);
					//parm.put("filterMap", filterMap);
					Boolean flag = new CommonUtils().currentUserWithAdmin(request);
					PageHelper.startPage(page, rows);
//					if(testWorkInputVo.getSidx().equals("id")) {
//						testWorkInputVo.setSidx("SYSTEM_CODE");
//					}
					if (flag) {
						parm.put("test",testWorkInputVo);
						list = tblTestTaskMapper.getTestTaskAll(parm);
					} else {
						// ????????????????????????id
						testWorkInputVo.setCurrentUserId(CommonUtil.getCurrentUserId(request));
						parm.put("test",testWorkInputVo);
						list = tblTestTaskMapper.getTestTask(parm);
					}
					for (TblTestTask testTask : list) {
						// Long caseNum = tblTestSetCaseMapper.findCaseCount(testTask.getId());
						/*Long caseNum = tblTestTaskMapper.getCaseNum(testTask.getId());
						testTask.setCaseNum(caseNum);*/
						List<Long> uIds = tblProjectGroupUserMapper.findUserIdBySystemId(testTask.getSystemId());
						testTask.settUserIds(uIds);
					}
					//list.sort((x,y) -> (Long.valueOf(x.getCaseNum())- Double.valueOf(y.getCaseNum())) < 0d ? 1 : -1);
					
					
					PageInfo<TblTestTask> pageInfo = new PageInfo<TblTestTask>(list);
					result.put("rows", list);

					result.put("records", pageInfo.getTotal());
					result.put("total", pageInfo.getPages());
					result.put("page", page < pageInfo.getPages() ? page : pageInfo.getPages());
				}
				
			} else {
				parm.put("test", tblTestTask);
				result.put("rows", tblTestTaskMapper.getTestTask(parm));
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public Map<String, Object> getTestTask2(TblTestTask TblTestTask, Integer page, Integer rows) {
		Map<String, Object> result = new HashMap<String, Object>();
		PageHelper.startPage(page, rows);
		List<TblTestTask> list = tblTestTaskMapper.selectTestTaskByCon(TblTestTask);
		PageInfo<TblTestTask> pageInfo = new PageInfo<TblTestTask>(list);
		Map<String, Object> statusMap = JsonUtil.fromJson((String) redisUtils.get("TBL_TEST_TASK_TEST_TASK_STATUS"),
				Map.class);
		for (TblTestTask testTask : list) {
			String status = (String) statusMap.get(testTask.getTestTaskStatus().toString());
			testTask.setWorkTaskStatus(status);
		}
		result.put("rows", list);
		result.put("total", pageInfo.getTotal());
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> getAllFeature(TblTestTask TblTestTask, Long userdId, Integer pageNumber,
			Integer pageSize, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		map = PageWithBootStrap.setPageNumberSize(map, pageNumber, pageSize);
		map.put("TblTestTask", TblTestTask);
		map.put("userId", userdId);
		Boolean flag = new CommonUtils().currentUserWithAdmin(request);
		
		if (flag) {// ?????????????????????????????????????????????
			return tblRequirementFeatureMapper.getAllRequirementFeature(map);
		} else {
			return tblRequirementFeatureMapper.getRequirementFeature(map);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<TblTestTask> getAllTestUser(Long testUserId) {
		List<TblTestTask> list = tblTestTaskMapper.getAllTestUser(testUserId);
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public Map getAllRequirt(TblRequirementInfo RequirementInfo, Integer page, Integer rows) {
		List<TblRequirementInfo> list = new ArrayList<>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<String> Array = new ArrayList();
		if (page != null && rows != null) {
			PageHelper.startPage(page, rows);
			if (RequirementInfo.getRequirementType() != null) {
				String RequirementType = RequirementInfo.getRequirementType();

				Array = Arrays.asList(RequirementType.split(","));
				RequirementInfo.setRequirementTypeList(Array);
			}
			list = requirementInfoMapper.getRequirement(RequirementInfo);

			Object object = redisUtils.get(RETYPE);
			Map<String, Object> mapsource = new HashMap<String, Object>();
			if (object != null && !"".equals(object)) {// redis????????????redis??????
				mapsource = JSON.parseObject(object.toString());
			}
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					for (String key : mapsource.keySet()) {
						if (key.equals(list.get(i).getRequirementType())) {
							list.get(i).setRequirementType(mapsource.get(key).toString());
						}
					}
				}
			}

			PageInfo<TblRequirementInfo> pageInfo = new PageInfo<TblRequirementInfo>(list);
			result.put("rows", list);
			result.put("records", pageInfo.getTotal());
			result.put("total", pageInfo.getPages());
			result.put("page", page < pageInfo.getPages() ? page : pageInfo.getPages());
			return result;
		} else {
			result.put("rows", requirementInfoMapper.getRequirement(RequirementInfo));
		}
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public Map getAllsystem(TblSystemInfo systemInfo, Integer page, Integer rows) {
		List<TblSystemInfo> list = new ArrayList();
		List<String> Array = new ArrayList();
		Map<String, Object> result = new HashMap<String, Object>();
		if (page != null && rows != null) {

			PageHelper.startPage(page, rows);
			if (systemInfo.getSystemType() != null) {
				String SystemType = systemInfo.getSystemType();

				Array = Arrays.asList(SystemType.split(","));
				systemInfo.setSystemTypeList(Array);
			}
			list = SystemInfoMapper.getAllsystem(systemInfo);
			PageInfo<TblSystemInfo> pageInfo = new PageInfo<TblSystemInfo>(list);

			Object object = redisUtils.get("TBL_SYSTEM_INFO_SYSTEM_TYPE");
			Map<String, Object> mapsource = new HashMap<String, Object>();
			if (object != null && !"".equals(object)) {// redis????????????redis??????
				mapsource = JSON.parseObject(object.toString());
			}
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					for (String key : mapsource.keySet()) {
						if (key.equals(list.get(i).getSystemType())) {
							list.get(i).setSystemType(mapsource.get(key).toString());
						}
					}
				}
			}
			result.put("rows", list);
			result.put("records", pageInfo.getTotal());
			result.put("total", pageInfo.getPages());
			result.put("page", page < pageInfo.getPages() ? page : pageInfo.getPages());
			return result;
		} else {
			result.put("rows", SystemInfoMapper.getAllsystem(systemInfo));
		}

		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TblTestTask> getUserName() {
		List<TblTestTask> list = tblTestTaskMapper.getUseName();
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public Map<String, Object> getSeeDetail(String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		DevDetailVo dev = new DevDetailVo();
		Long devID = Long.parseLong(id);
		dev = allDevRequirementMapper.AlldevReuirement(devID);
		dev.setDevuserName(allDevRequirementMapper.getdevName(dev.getDevuserID()));
		dev.setCreateName(allDevRequirementMapper.getdevName(dev.getCreateBy()));
		dev.setManageUserName(allDevRequirementMapper.getdevName(dev.getManageUserId()));
		dev.setSystemName(allDevRequirementMapper.getSystemName(dev.getSystemId()));
		dev.setExecuteUserName(allDevRequirementMapper.getdevName(dev.getExecuteUserId()));
		dev.setApplyUserName(allDevRequirementMapper.getdevName(dev.getApplyUserId()));
		dev.setApplyDeptName(allDevRequirementMapper.getdeptName(dev.getApplyDeptId()));
		Object source = redisUtils.get(SOURCE);
		Object type = redisUtils.get(TYPE);
		Object priority = redisUtils.get(PRIORITY);
		Object plan = redisUtils.get(PLAN);
		Object status = redisUtils.get(STATUS);
		Object testStage = redisUtils.get("TBL_TEST_TASK_TEST_STAGE");// ????????????
		Object devStatus = redisUtils.get("TBL_TEST_TASK_TEST_TASK_STATUS");// ??????????????????
		Map<String, Object> maptestStage = JSON.parseObject(testStage.toString());// ????????????
		Map<String, Object> mapdevStatus = JSON.parseObject(devStatus.toString());
		Map<String, Object> mapsource = JSON.parseObject(source.toString());
		Map<String, Object> maptype = JSON.parseObject(type.toString());
		Map<String, Object> mappriority = JSON.parseObject(priority.toString());
		Map<String, Object> mapplan = JSON.parseObject(plan.toString());
		Map<String, Object> mapstatus = JSON.parseObject(status.toString());
		for (String key : mapsource.keySet()) {
			String reqiureSorce = dev.getRequirementSource();
			if (reqiureSorce != null) {
				if (key.equals(reqiureSorce.toUpperCase(Locale.ENGLISH))) {
					dev.setRequirementSource(mapsource.get(key).toString());
				}
			}
		}
		for (String key : maptype.keySet()) {
			if (dev.getRequirementType() != null) {
				if (key.equals(dev.getRequirementType().toUpperCase(Locale.ENGLISH))) {
					dev.setRequirementType(maptype.get(key).toString());
				}
			}

		}
		for (String key : mappriority.keySet()) {
			if (dev.getRequirementPriority() != null) {
				if (key.equals(dev.getRequirementPriority().toUpperCase(Locale.ENGLISH))) {
					dev.setRequirementPriority(mappriority.get(key).toString());
				}
			}

		}
		for (String key : mapplan.keySet()) {
			if (dev.getRequirementPanl() != null) {
				if (key.equals(dev.getRequirementPanl().toUpperCase(Locale.ENGLISH))) {
					dev.setRequirementPanl(mapplan.get(key).toString());
				}
			}

		}
		for (String key : mapstatus.keySet()) {
			if (dev.getRequirementStatus() != null) {
				if (key.equals(dev.getRequirementStatus().toUpperCase(Locale.ENGLISH))) {
					dev.setRequirementStatus(mapstatus.get(key).toString());
				}
			}

		}
		for (String key : mapdevStatus.keySet()) {
			if (key.equals(dev.getDevTaskStatus().toString().toUpperCase(Locale.ENGLISH))) {
				dev.setTestDevTaskStatus(mapdevStatus.get(key).toString());
			}
		}
		for (String key : maptestStage.keySet()) {
			if (dev.getTestStage() != null) {
				if (key.equals(dev.getTestStage().toUpperCase(Locale.ENGLISH))) {
					dev.setTestStage(maptestStage.get(key).toString());
				}
			}
		}

		// ????????????????????????
		String status2 = dev.getRequirementFeatureStatus();
		if (status2 != null) {
			String Str = redisUtils.get("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS_2").toString();
			JSONObject object = JSONObject.parseObject(Str);
			if (object.get(status2) != null) {
				dev.setRequirementFeatureStatusName(object.get(status2).toString());
			}
		}

		List<String> users = tblTestSetCaseExecuteMapper.findExecuteUser(devID);
		dev.setExecuteUser(users);
		dev.setId(Long.parseLong(id));

		TblRequirementFeature requirementFeature = tblRequirementFeatureMapper.findRequirement(devID);
		String string = null;
		if (requirementFeature.getRequirementFeatureSource() != null) {
			String str = redisUtils.get("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_SOURCE_2").toString();
			JSONObject jsonObject = JSON.parseObject(str);
			if (jsonObject.get(requirementFeature.getRequirementFeatureSource().toString()) != null) {
				string = jsonObject.get(requirementFeature.getRequirementFeatureSource().toString()).toString();
			}
		}
		dev.setRequirementFeatureSource(string);
		dev.setPptDeployTime(requirementFeature.getPptDeployTime());
		dev.setRequirementChangeNumber(requirementFeature.getRequirementChangeNumber());
		dev.setImportantRequirementType(requirementFeature.getImportantRequirementType());
		dev.setSubmitTestTime(requirementFeature.getSubmitTestTime());

		Long num = tblTestTaskMapper.getDefectNum(devID);
		dev.setDefectNum(num);

		Long featureId = tblTestTaskMapper.getFeatureIdByTaskId(devID);
		dev.setFeatureId(featureId);
		result.put("dev", dev);
		return result;
	}

	// ??????????????????
	@Override
	public Map<String, Object> getEditTestTask(String id) {
		List<TblTestTask> list = new ArrayList<>();
		Map<String, Object> result = new HashMap<String, Object>();
		Long devid = Long.parseLong(id);
		list = tblTestTaskMapper.getEditTestTask(devid);
		TblRequirementFeature tblRequirementFeature = tblRequirementFeatureMapper.getPlanDate(devid);
		result.put("tblRequirementFeature", tblRequirementFeature);
		result.put("dev", list);
		return result;
	}

	// ??????????????????
	@Transactional(readOnly = false)
	@Override
	public TblTestTask addtestTask(String obj, String attachFiles, Long Userid, HttpServletRequest request,
			String UserAccount) {
		TblTestTask devTask = JSON.parseObject(obj, TblTestTask.class);
		devTask.setCreateBy(Userid);
		devTask.setTestTaskCode(getTestCode());

		Date date = new Date();
		Timestamp time2 = new Timestamp(date.getTime());
		devTask.setCreateDate(time2);
		Integer Status = devTask.getFeatureStatus();
		Long featureId = devTask.getRequirementFeatureId();
		if (Status != null && Status == 1 || Status == 3) {
			tblTestTaskMapper.editFeature(featureId);
		}
		tblTestTaskMapper.addTestTask(devTask);
		Long testId = devTask.getId();
		TblTestTaskLog tblTestTaskLog = new TblTestTaskLog();
		tblTestTaskLog.setTestTaskId(testId);
		tblTestTaskLog.setLogType("??????????????????");
		Long logId = this.insertDefectLog(tblTestTaskLog, request);
		if (!attachFiles.equals("") && !attachFiles.equals(null)) {
			List<TblTestTaskLogAttachement> files = JsonUtil.fromJson(attachFiles,
					JsonUtil.createCollectionType(ArrayList.class, TblTestTaskLogAttachement.class));
			for (int i = 0; i < files.size(); i++) {
				files.get(i).setTestTaskLogId(logId);
				files.get(i).setCreateBy(Userid);
			}
			tblTestTaskLogAttachementMapper.addLogAttachement(files);
		}
		if (!attachFiles.equals("") && !attachFiles.equals(null) && !attachFiles.equals("[]")) {
			List<TblTestTaskAttachement> fileTask = JsonUtil.fromJson(attachFiles,
					JsonUtil.createCollectionType(ArrayList.class, TblTestTaskAttachement.class));
			for (int i = 0; i < fileTask.size(); i++) {
				fileTask.get(i).setTestTaskId(testId);
				fileTask.get(i).setCreateBy(CommonUtil.getCurrentUserId(request));
			}
			tblTestTaskAttachementMapper.addAttachement(fileTask);
		}

		Long id = devTask.getId();
		TblTestTask tblTestTask=tblTestTaskMapper.selectByPrimaryKey(id);
		updateReqFea(id, request);
		
		//???????????????????????????????????????????????????????????? --ztt
		//??????????????????????????????????????????????????????  ?????????????????????????????? task_message_status
		TblRequirementFeature requirementFeature = tblRequirementFeatureMapper.selectByPrimaryKey(featureId);
		if (requirementFeature.getSystemId()!=null) {
			TblSystemInfo systemInfo = systemInfoMapper.selectByPrimaryKey(requirementFeature.getSystemId());
	        if (systemInfo.getTaskMessageStatus()==1 && tblTestTask.getTestUserId()!=null) {
	        	Map<String,Object> emWeMap = new HashMap<String, Object>();
	    		emWeMap.put("messageTitle", "???IT???????????????????????????- ????????????????????????????????????");
	    		emWeMap.put("messageContent","?????????????????????????????????????????????"+ tblTestTask.getTestTaskCode()+" | "+ tblTestTask.getTestTaskName()+"????????????????????????");
	    		emWeMap.put("messageReceiver",tblTestTask.getTestUserId());//?????????????????? ???????????????
	    		emWeMap.put("sendMethod", 3);//???????????? 3 ???????????????
	    		testManageToSystemInterface.sendMessage(JSON.toJSONString(emWeMap));
			}
		}
		
        
		return tblTestTask;
	}

	// ??????????????????
	@Transactional(readOnly = false)
	@Override
	public void updateTestTask(String obj, String attachFiles, String deleteFiles, Long Userid,
			HttpServletRequest request) throws Exception {
		TblTestTask TestTask = JSON.parseObject(obj, TblTestTask.class);
		TblDefectInfo tblDefectInfo = new TblDefectInfo();
		tblDefectInfo.setTestTaskId(TestTask.getId());
		tblDefectInfo.setCommissioningWindowId(TestTask.getCommissioningWindowId());
		defectService.updateTmpDefectByWorkId(tblDefectInfo);
		TestTask.setLastUpdateBy(Userid);
		Date date = new Date();
		TblTestTask oldTestTask = tblTestTaskMapper.getTestOld(TestTask.getId());// ?????????
		Integer Status = TestTask.getFeatureStatus();
		if (Status != null && Status == 1 || Status == 3) {
			Long featureId = TestTask.getRequirementFeatureId();
			tblTestTaskMapper.editFeature(featureId);
		}

		Timestamp time = new Timestamp(date.getTime());
		TestTask.setLastUpdateDate(time);
		tblTestTaskMapper.updateTestTask(TestTask);

		logAlls(TestTask.getId().toString(), attachFiles, deleteFiles, oldTestTask, "??????????????????", new HashMap<>(), request);

		Long id = TestTask.getId();
		if (TestTask.getTestTaskStatus() != 0) {
			updateReqFea(id, request);
		}

	}

	@Transactional(readOnly = false)
	@Override
	public void Handle(String handle, String HattachFiles, String deleteFiles, HttpServletRequest request) {
		TblTestTask TestTask = new TblTestTask();
		TestTask = JSON.parseObject(handle, TblTestTask.class);
		TblTestTask oldTestTask = tblTestTaskMapper.getTestOld(TestTask.getId());
		TestTask.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		tblTestTaskMapper.Handle(TestTask);
		logAlls(TestTask.getId().toString(), HattachFiles, deleteFiles, oldTestTask, "??????????????????", new HashMap<>(),
				request);
		updateReqFea1(TestTask.getId(), request);
	}

	/**
	 * 
	* @Title: DHandle
	* @Description: ??????????????????
	* @author author
	* @param handle ???????????????????????????
	* @param DHattachFiles ??????
	* @param deleteFiles
	* @param request
	 */
	@Transactional(readOnly = false)
	@Override
	public void DHandle(String handle, String DHattachFiles, String deleteFiles, HttpServletRequest request) {
		TblTestTask tblTestTask = new TblTestTask();
		tblTestTask = JSON.parseObject(handle, TblTestTask.class);
		TblTestTask oldTestTask = tblTestTaskMapper.getTestOld(tblTestTask.getId());
		tblTestTask.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		tblTestTaskMapper.DHandle(tblTestTask);
		logAlls(tblTestTask.getId().toString(), DHattachFiles, deleteFiles, oldTestTask, "??????????????????", new HashMap<>(),
				request);
		updateReqFea1(tblTestTask.getId(), request);
	}

	/**
	 * ?????????
	 */
	@Override
	public void examineHandle(String handle, String DHattachFiles, String deleteFiles, HttpServletRequest request) {
		TblTestTask tblTestTask = new TblTestTask();
		tblTestTask = JSON.parseObject(handle, TblTestTask.class);
		TblTestTask oldTestTask = tblTestTaskMapper.getTestOld(tblTestTask.getId());
		tblTestTask.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		tblTestTaskMapper.examineHandle(tblTestTask);
		logAlls(tblTestTask.getId().toString(), DHattachFiles, deleteFiles, oldTestTask, "??????????????????", new HashMap<>(),
				request);
		updateReqFea(tblTestTask.getId(), request);
	}

	@Transactional(readOnly = false)
	@Override
	public void assigTest(String assig, String Remark, HttpServletRequest request) {
		TblTestTask tblTestTask = new TblTestTask();
		tblTestTask = JSON.parseObject(assig, TblTestTask.class);
		Long id = tblTestTask.getId();// ??????id
		Long DevUser = tblTestTask.getTestUserId();// ?????????ID
		TblTestTask oldTestTask = tblTestTaskMapper.getTestOld(id);// ?????????
		Map<String, Object> remarkMap = new HashMap<>();
		if (oldTestTask != null) {
			Long OleUser = oldTestTask.getTestUserId();
			if (OleUser == null) {

				if (!Remark.equals("") && Remark != null) {
					remarkMap.put("remark", Remark);
				}
				tblTestTaskMapper.assigTest(tblTestTask);
				logAll(id.toString(), "", oldTestTask, "??????????????????", remarkMap, request);

			} else if (OleUser != DevUser || !Remark.equals("")) {
				tblTestTaskMapper.assigTest(tblTestTask);
				if (!Remark.equals("") && Remark != null) {
					remarkMap.put("remark", Remark);
				}
				logAll(id.toString(), "", oldTestTask, "??????????????????", remarkMap, request);

			}

		}

	}

	@Transactional(readOnly = true)
	@Override
	public String DevfindMaxCode(int length) {
		return tblTestTaskMapper.DevfindMaxCode(length);
	}

	@Transactional(readOnly = true)
	@Override
	public List<TblTestTask> getExcelAllWork(TblTestWorkVo tblTestWorkVo, HttpServletRequest request) {
		List<TblTestTask> list = new ArrayList<>();
		Map<String, Object> paraMap=filterSearch(tblTestWorkVo);
		
		if(paraMap.get("testWorkInputVo") != null) {
			TestWorkInputVo testWorkInputVo = JSON.parseObject(paraMap.get("testWorkInputVo").toString(),TestWorkInputVo.class) ; 
			paraMap.put("test", tblTestWorkVo);
			Boolean flag = new CommonUtils().currentUserWithAdmin(request);
			if (flag) {
				paraMap.put("test",testWorkInputVo);
				list = tblTestTaskMapper.getTestTaskAll(paraMap);
			} else {
				testWorkInputVo.setCurrentUserId(CommonUtil.getCurrentUserId(request));
				paraMap.put("test",testWorkInputVo);
				list = tblTestTaskMapper.getTestTask(paraMap);
			}
		}
		
		// list = tblTestTaskMapper.getTestTask(result);
		
		Map<String, Object> testStage = JSON.parseObject(redisUtils.get("TBL_TEST_TASK_TEST_STAGE").toString(),
				Map.class);
		Object object = redisUtils.get("TBL_TEST_TASK_TEST_TASK_STATUS");
		Map<String, Object> mapsource = new HashMap<String, Object>();
		if (object != null && !"".equals(object)) {// redis????????????redis??????
			mapsource = JSON.parseObject(object.toString());
		}
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				for (String key : mapsource.keySet()) {
					if (list.get(i).getTestTaskStatus() != null
							&& key.equals(list.get(i).getTestTaskStatus().toString())) {
						list.get(i).setWorkTaskStatus(mapsource.get(key).toString());
					}
				}
				for (String key : testStage.keySet()) {
					if (list.get(i).getTestStage() != null
							&& key.equals(list.get(i).getTestStage().toString())) {
						list.get(i).setTestStageName(testStage.get(key).toString());
					}
				}
				
			}
		}
		return list;
	}

	public String getTestCode() {
		String featureCode = "";
		int codeInt = 0;
		Object object = redisUtils.get("TBL_TEST_TASK_TEST_TASK_CODE");
		if (object != null && !"".equals(object)) {// redis????????????redis??????
			String code = object.toString();
			if (!StringUtils.isBlank(code)) {
				codeInt = Integer.parseInt(code) + 1;
			}
		} else {// redis????????????????????????????????????????????????
			int length = Constants.ITMP_TESTWORK_TASK_CODE.length() + 1;
			String cod = DevfindMaxCode(length);
			if (!StringUtils.isBlank(cod)) {
				codeInt = Integer.parseInt(cod) + 1;
			} else {
				codeInt = 1;
			}

		}
		DecimalFormat mat = new DecimalFormat("00000000");
		String codeString = mat.format(codeInt);

		featureCode = Constants.ITMP_TESTWORK_TASK_CODE + codeString;
		redisUtils.set("TBL_TEST_TASK_TEST_TASK_CODE", codeString);
		return featureCode;
	}

	@Override
	public Map ReqSystem() {
		Object object = redisUtils.get("TBL_SYSTEM_INFO_SYSTEM_TYPE");
		Map<String, Object> mapsource = new HashMap<>();
		if (object != null && !"".equals(object)) {// redis????????????redis??????
			mapsource = JSON.parseObject(object.toString());
		}
		return mapsource;
	}

	@Override
	public Map ReqStatus() {
		Object object = redisUtils.get(RETYPE);
		Map<String, Object> mapsource = new HashMap<>();
		if (object != null && !"".equals(object)) {// redis????????????redis??????
			mapsource = JSON.parseObject(object.toString());
		}
		return mapsource;
	}

	public Long insertDefectLog(TblTestTaskLog defectLog, HttpServletRequest request) {
		defectLog = this.setDefectLog(defectLog, request);
		defectLog.setCreateBy(CommonUtil.getCurrentUserId(request));
		defectLog.setCreateDate(new Timestamp(new Date().getTime()));
		tblTestTaskLogMapper.insertDefectLog(defectLog);
		return defectLog.getId();
	}

	private TblTestTaskLog setDefectLog(TblTestTaskLog defectLog, HttpServletRequest request) {
		defectLog.setUserId(CommonUtil.getCurrentUserId(request));
		defectLog.setUserName(CommonUtil.getCurrentUserName(request));
		defectLog.setUserAccount(CommonUtil.getCurrentUserAccount(request));
		return defectLog;
	}

	/**
	 * ?????????????????? ???????????? ????????????
	 * 
	 * @param oldObject
	 * @param newObject
	 * @param logId
	 *
	 */

	public void updateFieledsReflect(Object oldObject, Object newObject, Long logId, Map<String, Object> remarkMap)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException,
			ClassNotFoundException {
		/*
		 * Class<?> oldDemo = null; Class<?> newDemo = null;
		 */
		List<Map<String, Object>> list = new ArrayList<>();
		if (!remarkMap.isEmpty()) {
			list.add(remarkMap);
		}
		Class<?> oldDemo = Class.forName(oldObject.getClass().getName());
		Class<?> newDemo = Class.forName(newObject.getClass().getName());

		Field[] oldFields = oldDemo.getDeclaredFields();
		// if(!oldObject.equals("Infinity")) {
		for (Field oldField : oldFields) {

			oldField.setAccessible(true);
			String oldDclareName = oldField.getName();
			Object oldDeclareValue = oldField.get(oldObject);
			PropertyInfo propertyInfo = oldField.getAnnotation(PropertyInfo.class);

			Field newField = newDemo.getDeclaredField(oldDclareName);
			newField.setAccessible(true);
			Object newDeclareValue = newField.get(newObject);

			// ????????????????????????
			if (oldDclareName.equals("requirementFeatureId") && oldDeclareValue != null) {
				oldDeclareValue = tblTestTaskMapper.getFeatureNameById((Long) oldDeclareValue);
			}
			if (oldDclareName.equals("requirementFeatureId") && newDeclareValue != null) {
				newDeclareValue = tblTestTaskMapper.getFeatureNameById((Long) newDeclareValue);
			}
			// ????????????????????????
			Object object = redisUtils.get("TBL_TEST_TASK_TEST_TASK_STATUS");
			Map<String, Object> mapsource = new HashMap<>();
			if (object != null && !"".equals(object)) {// redis????????????redis??????
				mapsource = JSON.parseObject(object.toString());
			}
			if (oldDclareName.equals("testTaskStatus") && oldDeclareValue != null) {
				for (String key : mapsource.keySet()) {
					if (key.equals(oldDeclareValue.toString())) {
						oldDeclareValue = mapsource.get(key).toString();
					}
				}
			}
			if (oldDclareName.equals("testTaskStatus") && newDeclareValue != null) {
				for (String key : mapsource.keySet()) {
					if (key.equals(newDeclareValue.toString())) {
						newDeclareValue = mapsource.get(key).toString();
					}
				}
			}

			if (oldDclareName.equals("testUserId") && newDeclareValue != null) {
				continue;
			}
			if (oldDclareName.equals("commissioningWindowId") && newDeclareValue != null) {
				continue;
			}
			if (oldDclareName.equals("featureName") && newDeclareValue != null) {
				continue;
			}
			if (oldDclareName.equals("featureStatus") && newDeclareValue != null) {
				continue;
			}
			if (oldDclareName.equals("systemInfoList") && newDeclareValue != null) {
				continue;
			}

			if (oldDclareName.equals("testStage") && oldDeclareValue != null) {
				if (newDeclareValue.toString().equals("1")) {
					oldDeclareValue = "??????";
				} else {
					oldDeclareValue = "??????";
				}

			}
			if (oldDclareName.equals("testStage") && newDeclareValue != null) {
				if (newDeclareValue.toString().equals("1")) {
					newDeclareValue = "??????";
				} else {
					newDeclareValue = "??????";
				}
			}

			String[] fieldTypeName = newField.getType().getName().split("\\.");
			if (!listType.contains(fieldTypeName[fieldTypeName.length - 1])) {
				updateFieledsReflect(oldDeclareValue, newDeclareValue, logId, remarkMap);
				continue;
			}

			if (oldDeclareValue == null || newDeclareValue == null) {
				if (oldDeclareValue == null && newDeclareValue == null) {
					continue;
				} else {
					if (oldDeclareValue == null) {
						if (oldField.getType().equals(java.util.Date.class)) {
							SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
							newDeclareValue = simpleDateFormat.format(newDeclareValue);
						}
						// ??????????????????
						Map<String, Object> newMap = new HashMap<>();
						newMap.put("fieldName", propertyInfo.name());
						newMap.put("oldValue", "");
						newMap.put("newValue", newDeclareValue.toString());
						list.add(newMap);
					} else {
						// ??????????????????
						if (oldField.getType().equals(java.sql.Timestamp.class)) {
							continue;
						} else {
							if (oldField.getType().equals(java.util.Date.class)) {
								SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
								oldDeclareValue = simpleDateFormat.format(oldDeclareValue);

							}
							/*
							 * String name= propertyInfo.name(); Map<String,Object> delMap = new
							 * HashMap<>(); delMap.put("fieldName",name);
							 * delMap.put("oldValue",oldDeclareValue.toString());
							 * delMap.put("newValue","?????????"); list.add(delMap);
							 */
						}

					}
					continue;
				}
			}
			// ??????????????????
			if (!oldDeclareValue.toString().equals(newDeclareValue.toString())) {
				if (oldField.getType().equals(java.sql.Timestamp.class)) {
					continue;
				} else {
					if (oldField.getType().equals(java.util.Date.class)) {
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
						oldDeclareValue = simpleDateFormat.format(oldDeclareValue);
						newDeclareValue = simpleDateFormat.format(newDeclareValue);
					}
					String name = propertyInfo.name();
					Map<String, Object> updateMap = new HashMap<>();
					updateMap.put("fieldName", name);
					updateMap.put("oldValue", oldDeclareValue.toString());
					updateMap.put("newValue", newDeclareValue.toString());
					list.add(updateMap);
				}
			}

			// }
		}
		Gson gson = new Gson();
		String logDetail = gson.toJson(list);
		TblDefectLog log = new TblDefectLog();
		log.setLogDetail(logDetail);
		log.setId(logId);
		// ??????????????????
		tblTestTaskLogMapper.updateLogById(log);
	}

	@Override
	@Transactional(readOnly = true)
	public TblTestTask getTestTaskById(Long id) {
		return tblTestTaskMapper.getTestOld(id);
	}

	/**
	 * ??????????????????
	 * 
	 * @param id
	 * @param adoptFiles
	 * @param oldDevTask
	 * @param request
	 */
	public void logAll(String id, String adoptFiles, Object oldDevTask, String LogType, Map<String, Object> remarkMap,
			HttpServletRequest request) {
		TblTestTaskLog tblTestTaskLog = new TblTestTaskLog();
		tblTestTaskLog.setTestTaskId(Long.parseLong(id));
		tblTestTaskLog.setLogType(LogType);
		Long logId = this.insertDefectLog(tblTestTaskLog, request);
		TblTestTask newTestTask = tblTestTaskMapper.getTestOld(Long.parseLong(id));
		if (!adoptFiles.equals("") && !adoptFiles.equals(null) && !adoptFiles.equals("[]")) {
			List<TblTestTaskLogAttachement> files = JsonUtil.fromJson(adoptFiles,
					JsonUtil.createCollectionType(ArrayList.class, TblTestTaskLogAttachement.class));
			for (int i = 0; i < files.size(); i++) {
				files.get(i).setTestTaskLogId(logId);
				files.get(i).setCreateBy(CommonUtil.getCurrentUserId(request));
				files.get(i).setStatus(1);
			}
			tblTestTaskLogAttachementMapper.addLogAttachement(files);
		}

		if (!adoptFiles.equals("") && !adoptFiles.equals(null) && !adoptFiles.equals("[]")) {
			List<TblTestTaskAttachement> fileTask = JsonUtil.fromJson(adoptFiles,
					JsonUtil.createCollectionType(ArrayList.class, TblTestTaskAttachement.class));
			for (int i = 0; i < fileTask.size(); i++) {
				fileTask.get(i).setTestTaskId(Long.parseLong(id));
				fileTask.get(i).setCreateBy(CommonUtil.getCurrentUserId(request));
			}
			tblTestTaskAttachementMapper.addAttachement(fileTask);
		}
		try {
			updateFieledsReflect(oldDevTask, newTestTask, logId, remarkMap);
		} catch (Exception e) {

		}
	}

	public void logAlls(String id, String adoptFiles, String deleteAttaches, Object oldDevTask, String LogType,
			Map<String, Object> remarkMap, HttpServletRequest request) {
		TblTestTaskLog tblTestTaskLog = new TblTestTaskLog();
		tblTestTaskLog.setTestTaskId(Long.parseLong(id));
		tblTestTaskLog.setLogType(LogType);
		Long logId = this.insertDefectLog(tblTestTaskLog, request);
		TblTestTask newTestTask = tblTestTaskMapper.getTestOld(Long.parseLong(id));
		if (!adoptFiles.equals("") && !adoptFiles.equals(null) && !adoptFiles.equals("[]")) {
			List<TblTestTaskLogAttachement> files = JsonUtil.fromJson(adoptFiles,
					JsonUtil.createCollectionType(ArrayList.class, TblTestTaskLogAttachement.class));
			for (int i = 0; i < files.size(); i++) {
				files.get(i).setTestTaskLogId(logId);
				files.get(i).setCreateBy(CommonUtil.getCurrentUserId(request));
				files.get(i).setStatus(1);
			}
			tblTestTaskLogAttachementMapper.addLogAttachement(files);
		}

		if (!adoptFiles.equals("") && !adoptFiles.equals(null) && !adoptFiles.equals("[]")) {
			List<TblTestTaskAttachement> fileTask = JsonUtil.fromJson(adoptFiles,
					JsonUtil.createCollectionType(ArrayList.class, TblTestTaskAttachement.class));
			for (int i = 0; i < fileTask.size(); i++) {
				fileTask.get(i).setTestTaskId(Long.parseLong(id));
				fileTask.get(i).setCreateBy(CommonUtil.getCurrentUserId(request));
			}
			tblTestTaskAttachementMapper.addAttachement(fileTask);
		}

		// ????????????????????????
		if (!deleteAttaches.equals("") && !deleteAttaches.equals(null) && !deleteAttaches.equals("[]")) {
			List<TblTestTaskLogAttachement> deleteFileTask = JsonUtil.fromJson(deleteAttaches,
					JsonUtil.createCollectionType(ArrayList.class, TblTestTaskLogAttachement.class));
			for (int i = 0; i < deleteFileTask.size(); i++) {
				deleteFileTask.get(i).setTestTaskLogId(logId);
				deleteFileTask.get(i).setCreateBy(CommonUtil.getCurrentUserId(request));
				deleteFileTask.get(i).setStatus(2);
			}
			tblTestTaskLogAttachementMapper.addLogAttachement(deleteFileTask);
		}
		try {
			updateFieledsReflect(oldDevTask, newTestTask, logId, remarkMap);
		} catch (Exception e) {

		}
	}

	@Override
	public List<Map<String, Object>> getAllFeatureTask(TblTestTask tblTestTask, Integer pageNumber, Integer pageSize) {
		Map<String, Object> map = new HashMap<>();
		map = PageWithBootStrap.setPageNumberSize(map, pageNumber, pageSize);
		map.put("TblTestTask", tblTestTask);
		return tblTestTaskMapper.getAllFeature(map);
	}

	@Override
	public List<Map<String, Object>> getAllTestUser(TblUserInfo tblUserInfo, Integer notWithUserID, Integer devID,
			Integer pageNumber, Integer pageSize) {
		Map<String, Object> map = new HashMap<>();
		map = PageWithBootStrap.setPageNumberSize(map, pageNumber, pageSize);
		map.put("tblUserInfo", tblUserInfo);
		map.put("notWithUserID", notWithUserID);
		map.put("devID", devID);
		return tblUserInfoMapper.getAllTestUser(map);
	}

	/**
	 * ??????????????????id??????????????????
	 */
	@Override
	public List<TblTestTask> selectTestTaskByRequirementFeatureId(Long requirementFeatureId, String nameOrNumber,
			String createBy, String testTaskId) {
		List<Long> userIds = JSON.parseArray(createBy, Long.class);
		List<Long> testTaskIds = JSON.parseArray(testTaskId, Long.class);
		return tblTestTaskMapper.selectTestTaskByRequirementFeatureId(requirementFeatureId, nameOrNumber, userIds,
				testTaskIds);
	}

	// ?????????????????????????????????????????????????????????
	public void updateReqFea(Long id, HttpServletRequest request) {
		TblRequirementFeature requirementFeature = tblRequirementFeatureMapper.getReqFeaByTaskId(id);
		String status = "";
		String afterName = "";
		List<TblDataDic> dataDics = getDataFromRedis("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS_2");
		for (TblDataDic tblDataDic : dataDics) {
			if (requirementFeature.getRequirementFeatureStatus() != null) {
				if (tblDataDic.getValueCode().equals(requirementFeature.getRequirementFeatureStatus())) {
					afterName = tblDataDic.getValueName();
				}
			}
		}
		Map<Object, Object> map = new HashMap<>();
		map.put("id", id);
		List<Integer> testStages = tblTestTaskMapper.selectTestStageById(id);// ?????????????????????????????????????????????????????????(???????????????????????????)
		if (testStages.size() == 1) { // ?????????1??????????????????????????????
			if (testStages.get(0) == 1) {// ??????????????????????????????????????????
				List<Integer> testTaskStatus = tblTestTaskMapper.selectTestTaskStatusById(id);
				if (testTaskStatus.size() == 1 && testTaskStatus.get(0) == 3) {// ??????????????????????????????
					map.put("status", "03");
					tblTestTaskMapper.updateReqFeaStatus(map);// ????????????
					if (!requirementFeature.getRequirementFeatureStatus().equals("03")) {
						status = "&nbsp;&nbsp;???<b>" + afterName + "</b>???&nbsp;&nbsp;" + "?????????"
								+ "&nbsp;&nbsp;???<b>????????????</b>???&nbsp;&nbsp;???";
						insertLog(requirementFeature, status, request);
					}
					if (requirementFeature.getCreateType().equals(2)) {
						Map<String, Object> dataResult = pushData(requirementFeature);
						String taskId = "";
						if (requirementFeature.getFeatureCode() != null) {
							taskId = requirementFeature.getFeatureCode();
						}
						// ??????databus
						DataBusUtil.send(databusccName, taskId, JsonUtil.toJson(dataResult));
					}
				} else {
					map.put("status", "02");
					tblTestTaskMapper.updateReqFeaStatus(map);// ?????????
					if (!requirementFeature.getRequirementFeatureStatus().equals("02")) {
						status = "&nbsp;&nbsp;???<b>" + afterName + "</b>???&nbsp;&nbsp;" + "?????????"
								+ "&nbsp;&nbsp;???<b>?????????</b>???&nbsp;&nbsp;???";
						insertLog(requirementFeature, status, request);
					}
				}
			} else if (testStages.get(0) == 2) {// ??????????????????????????????????????????
				List<Integer> testTaskStatus = tblTestTaskMapper.selectTestTaskStatusById(id);
				if (testTaskStatus.size() == 1 && testTaskStatus.get(0) == 3) {// ??????????????????????????????
					map.put("status", "08");
					tblTestTaskMapper.updateReqFeaStatus(map);// ????????????
					if (!requirementFeature.getRequirementFeatureStatus().equals("08")) {
						status = "&nbsp;&nbsp;???<b>" + afterName + "</b>???&nbsp;&nbsp;" + "?????????"
								+ "&nbsp;&nbsp;???<b>????????????</b>???&nbsp;&nbsp;???";
						insertLog(requirementFeature, status, request);
					}
				} else {
					map.put("status", "06");
					tblTestTaskMapper.updateReqFeaStatus(map);// ?????????
					if (!requirementFeature.getRequirementFeatureStatus().equals("06")) {
						status = "&nbsp;&nbsp;???<b>" + afterName + "</b>???&nbsp;&nbsp;" + "?????????"
								+ "&nbsp;&nbsp;???<b>?????????</b>???&nbsp;&nbsp;???";
						insertLog(requirementFeature, status, request);
					}
				}
				if (requirementFeature.getCreateType().equals(2)) {
					Map<String, Object> dataResult = pushData(requirementFeature);
					String taskId = "";
					if (requirementFeature.getFeatureCode() != null) {
						taskId = requirementFeature.getFeatureCode();
					}
					// ??????databus
					DataBusUtil.send(databusccName, taskId, JsonUtil.toJson(dataResult));
				}
			}
		} else if (testStages.size() == 2) { // ????????????1(???2)????????????????????????
			List<Integer> testTaskStatus = tblTestTaskMapper.selectTestTaskStatusById2(id);
			if (testTaskStatus.size() == 1 && testTaskStatus.get(0) == 3) {// ??????????????????????????????
				map.put("status", "08");
				tblTestTaskMapper.updateReqFeaStatus(map);// ????????????
				if (!requirementFeature.getRequirementFeatureStatus().equals("08")) {
					status = "&nbsp;&nbsp;???<b>" + afterName + "</b>???&nbsp;&nbsp;" + "?????????"
							+ "&nbsp;&nbsp;???<b>????????????</b>???&nbsp;&nbsp;???";
					insertLog(requirementFeature, status, request);
				}
			} else {
				map.put("status", "06");
				tblTestTaskMapper.updateReqFeaStatus(map);// ?????????
				if (!requirementFeature.getRequirementFeatureStatus().equals("06")) {
					status = "&nbsp;&nbsp;???<b>" + afterName + "</b>???&nbsp;&nbsp;" + "?????????"
							+ "&nbsp;&nbsp;???<b>?????????</b>???&nbsp;&nbsp;???";
					insertLog(requirementFeature, status, request);
				}
			}
			if (requirementFeature.getCreateType().equals(2)) {
				Map<String, Object> dataResult = pushData(requirementFeature);
				String taskId = "";
				if (requirementFeature.getFeatureCode() != null) {
					taskId = requirementFeature.getFeatureCode();
				}
				// ??????databus
				DataBusUtil.send(databusccName, taskId, JsonUtil.toJson(dataResult));
			}
		}
		// TblRequirementFeatureLog log = new TblRequirementFeatureLog();
		// log.setRequirementFeatureId(requirementFeature.getId());
		// log.setLogType("??????????????????");
		// log.setLogDetail(status);
		// insertLog(log, request);
	}

	// ?????????????????????????????????????????????????????????
	public void updateReqFea1(Long id, HttpServletRequest request) {
		TblRequirementFeature requirementFeature = tblRequirementFeatureMapper.getReqFeaByTaskId(id);
		String status = "";
		String afterName = "";
		List<TblDataDic> dataDics = getDataFromRedis("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS_2");
		for (TblDataDic tblDataDic : dataDics) {
			if (requirementFeature.getRequirementFeatureStatus() != null) {
				if (tblDataDic.getValueCode().equals(requirementFeature.getRequirementFeatureStatus())) {
					afterName = tblDataDic.getValueName();
				}
			}
		}
		Map<Object, Object> map = new HashMap<>();
		map.put("id", id);
		List<Integer> testStages = tblTestTaskMapper.selectTestStageById(id);// ?????????????????????????????????????????????????????????(???????????????????????????)
		if (testStages.size() == 1) { // ?????????1??????????????????????????????
			if (testStages.get(0) == 1) {// ??????????????????????????????????????????
				List<Integer> testTaskStatus = tblTestTaskMapper.selectTestTaskStatusById(id);
				if (testTaskStatus.size() == 1 && testTaskStatus.get(0) == 3) {// ??????????????????????????????
					map.put("status", "03");
					tblTestTaskMapper.updateReqFeaStatus(map);// ????????????
					if (!requirementFeature.getRequirementFeatureStatus().equals("03")) {
						status = "&nbsp;&nbsp;???<b>" + afterName + "</b>???&nbsp;&nbsp;" + "?????????"
								+ "&nbsp;&nbsp;???<b>????????????</b>???&nbsp;&nbsp;???";
						insertLog(requirementFeature, status, request);
					}
				} else {
					map.put("status", "02");
					tblTestTaskMapper.updateReqFeaStatus(map);// ?????????
					if (!requirementFeature.getRequirementFeatureStatus().equals("02")) {
						status = "&nbsp;&nbsp;???<b>" + afterName + "</b>???&nbsp;&nbsp;" + "?????????"
								+ "&nbsp;&nbsp;???<b>?????????</b>???&nbsp;&nbsp;???";
						insertLog(requirementFeature, status, request);
					}
				}
			} else if (testStages.get(0) == 2) {// ??????????????????????????????????????????
				List<Integer> testTaskStatus = tblTestTaskMapper.selectTestTaskStatusById(id);
				if (testTaskStatus.size() == 1 && testTaskStatus.get(0) == 3) {// ??????????????????????????????
					map.put("status", "08");
					tblTestTaskMapper.updateReqFeaStatus(map);// ????????????
					if (!requirementFeature.getRequirementFeatureStatus().equals("08")) {
						status = "&nbsp;&nbsp;???<b>" + afterName + "</b>???&nbsp;&nbsp;" + "?????????"
								+ "&nbsp;&nbsp;???<b>????????????</b>???&nbsp;&nbsp;???";
						insertLog(requirementFeature, status, request);
					}
				} else {
					map.put("status", "06");
					tblTestTaskMapper.updateReqFeaStatus(map);// ?????????
					if (!requirementFeature.getRequirementFeatureStatus().equals("06")) {
						status = "&nbsp;&nbsp;???<b>" + afterName + "</b>???&nbsp;&nbsp;" + "?????????"
								+ "&nbsp;&nbsp;???<b>?????????</b>???&nbsp;&nbsp;???";
						insertLog(requirementFeature, status, request);
					}
				}
			}
		} else if (testStages.size() == 2) { // ????????????1(???2)????????????????????????
			List<Integer> testTaskStatus = tblTestTaskMapper.selectTestTaskStatusById2(id);
			if (testTaskStatus.size() == 1 && testTaskStatus.get(0) == 3) {// ??????????????????????????????
				map.put("status", "08");
				tblTestTaskMapper.updateReqFeaStatus(map);// ????????????
				if (!requirementFeature.getRequirementFeatureStatus().equals("08")) {
					status = "&nbsp;&nbsp;???<b>" + afterName + "</b>???&nbsp;&nbsp;" + "?????????"
							+ "&nbsp;&nbsp;???<b>????????????</b>???&nbsp;&nbsp;???";
					insertLog(requirementFeature, status, request);
				}
			} else {
				map.put("status", "06");
				tblTestTaskMapper.updateReqFeaStatus(map);// ?????????
				if (!requirementFeature.getRequirementFeatureStatus().equals("06")) {
					status = "&nbsp;&nbsp;???<b>" + afterName + "</b>???&nbsp;&nbsp;" + "?????????"
							+ "&nbsp;&nbsp;???<b>?????????</b>???&nbsp;&nbsp;???";
					insertLog(requirementFeature, status, request);
				}
			}
		}
	}

	@Transactional(readOnly = false)
	public void insertLog(TblRequirementFeature requirementFeature, String status, HttpServletRequest request) {
		TblRequirementFeatureLog log = new TblRequirementFeatureLog();
		log.setRequirementFeatureId(requirementFeature.getId());
		log.setLogType("??????????????????");
		log.setLogDetail(status);
		log.setCreateBy(CommonUtil.getCurrentUserId(request));
		log.setCreateDate(new Timestamp(new Date().getTime()));
		log.setUserId(CommonUtil.getCurrentUserId(request));
		log.setUserAccount(CommonUtil.getCurrentUserAccount(request));
		log.setUserName(CommonUtil.getCurrentUserName(request));
		requirementFeatureLogMapper.insert(log);
	}

	private List<TblDataDic> getDataFromRedis(String termCode) {
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

	// ??????????????????id????????????????????????????????????????????????????????????????????????????????????
	@Override
	public List<Long> selectUserIdsById(Long id) {
		// TODO Auto-generated method stub
		Long systemId = tblTestTaskMapper.getSystemIdByTaskId(id);
		List<Long> uIds = tblProjectGroupUserMapper.findUserIdBySystemId(systemId);
		return uIds;
	}

	public Map<String, Object> pushData(TblRequirementFeature requirementFeature) {
		Map<String, Object> mapAll = new LinkedHashMap<>();

		Map<String, Object> mapBody = new HashMap<>();
		mapBody.put("tbltaskId", requirementFeature.getFeatureCode());
		mapBody.put("taskResult", "????????????");

		Double actualSitWorkload = requirementFeatureMapper.getWorkload(requirementFeature.getId());
		Double actualPptWorkload = requirementFeatureMapper.getWorkload2(requirementFeature.getId());
		if (actualSitWorkload != null && actualSitWorkload > 0) {
			mapBody.put("taskWorkload", actualSitWorkload);
		} else if (actualPptWorkload != null && actualPptWorkload > 0) {
			mapBody.put("taskWorkload", actualPptWorkload);
		} else {
			mapBody.put("taskWorkload", 0.0);
		}
		mapAll.put("requestHead", DataBusRequestHead.getRequestHead());
		mapAll.put("requestBody", mapBody);
		return mapAll;
	}

	@Override
	public List<TblTestTask> getTestTaskByCurrentUser(Long uid) {

		return tblTestTaskMapper.getTestTaskByCurrentUser(uid);
	}

	/**
	 * ????????????
	 * 
	 * @param testTaskVo
	 */
	private Map<String, Object> filterSearch(TblTestWorkVo tblTestTask) {
		Map<String, Object> result = new HashMap<String, Object>();
		TestWorkInputVo tblTestTaskVo = new TestWorkInputVo();
		String testTaskCode = tblTestTask.getTestTaskCode();
		String testTaskCodeSql = CommonUtil.getSearchSql("test.TEST_TASK_CODE", 1, testTaskCode);
		if (testTaskCodeSql.equals("error")) {
			setErrorMap(result, "??????????????????");
			return result;
		}
		tblTestTaskVo.setTestTaskCodeSql(testTaskCodeSql);

		String testTaskName = tblTestTask.getTestTaskName();
		String testTaskNameSql = CommonUtil.getSearchSql("test.TEST_TASK_NAME", 1, testTaskName);
		if (testTaskNameSql.equals("error")) {
			setErrorMap(result, "??????????????????");
			return result;
		}
		tblTestTaskVo.setTestTaskNameSql(testTaskNameSql);

		String workTaskStatus = tblTestTask.getTestTaskStatus();
		String testTaskStatusStr = CommonUtil.getSearchSql("TBL_TEST_TASK_TEST_TASK_STATUS", 4, workTaskStatus);
		if (testTaskStatusStr.equals("error")) {
			setErrorMap(result, "??????????????????");
			return result;
		}
		tblTestTaskVo.setWorkTaskStatus(testTaskStatusStr);

		String systemName = tblTestTask.getSystemName();
		String systemNameSql = CommonUtil.getSearchSql("SYSTEM_NAME", 1, systemName);
		if (systemNameSql.equals("error")) {
			setErrorMap(result, "????????????");
			return result;
		}
		List<Long> systemIdList = StringUtils.isEmpty(systemNameSql) ? new ArrayList<Long>()
				: systemInfoMapper.getSystemIdByInjectSql(systemNameSql);
		tblTestTaskVo.setSystemIdList(systemIdList);

		String requirementCode = tblTestTask.getRequirementCode();
		String requirementCodeSql = CommonUtil.getSearchSql("REQUIREMENT_CODE", 1, requirementCode);
		if (requirementCodeSql.equals("error")) {
			setErrorMap(result, "????????????");
			return result;
		}
		List<Long> reqIdList = StringUtils.isEmpty(requirementCodeSql) ? new ArrayList<Long>()
				: requirementInfoMapper.selectReqIdByReqCodeSql(requirementCodeSql);
		tblTestTaskVo.setReqIdList(reqIdList);

		String featureCode = tblTestTask.getFeatureCode();
		String featureCodeCodeSql = CommonUtil.getSearchSql("FEATURE_CODE", 1, featureCode);
		if (requirementCodeSql.equals("error")) {
			setErrorMap(result, "??????????????????");
			return result;
		}
		List<Long> featureIdList = StringUtils.isEmpty(featureCodeCodeSql) ? new ArrayList<Long>()
				: requirementFeatureMapper.getIdsBySql(featureCodeCodeSql);
		tblTestTaskVo.setFeatureCodeSql(featureIdList);

		String testUserName = tblTestTask.getUserName();
		String testUserNameSql = CommonUtil.getSearchSql("userinfo.USER_NAME", 1, testUserName);
		if (testUserNameSql.equals("error")) {
			setErrorMap(result, "????????????");
			return result;
		}
		/*List<Long> testUserIdList = StringUtils.isEmpty(testUserNameSql) ? new ArrayList<Long>()
				: tblUserInfoMapper.selectIdByUserNameSql(testUserNameSql);*/
		tblTestTaskVo.setTestUserIdName(testUserNameSql);

		String testStageName = tblTestTask.getTestStage();
		String testStageNameSql = CommonUtil.getSearchSql("TBL_TEST_TASK_TEST_STAGE", 4, testStageName);
		if (testStageNameSql.equals("error")) {
			setErrorMap(result, "????????????");
			return result;
		}
		tblTestTaskVo.setTestStageName(testStageNameSql);

		String windowName = tblTestTask.getWindowName();
		String windowNameSql="";
		if(StringUtils.isNotEmpty(windowName)) {
			if(windowName.indexOf(">")!=-1 || windowName.indexOf("<")!=-1
					|| windowName.indexOf("=")!=-1) {     //???????????????????????????????????????????????????????????????
				if(windowName.indexOf("??????")!=-1) {		//????????????????????????????????????????????????????????????????????????sql?????????
					String endPartSql = CommonUtil.getSearchSql("WINDOW_NAME", 1, "??????");
					String startPartSql = windowName.replaceAll("??????", "");
					windowNameSql = CommonUtil.getSearchSql("WINDOW_DATE", 3, startPartSql) + " " + endPartSql;
				}else {
					windowNameSql = CommonUtil.getSearchSql("WINDOW_DATE", 3, windowName);
				}
			}else {   //??????????????????????????????????????????????????????????????????
				windowNameSql = CommonUtil.getSearchSql("WINDOW_NAME", 1, windowName);
			}
		}
		if (requirementCodeSql.equals("error")) {
			setErrorMap(result, "????????????");
			return result;
		}
		List<Long> windowIdList = StringUtils.isEmpty(windowNameSql) ? new ArrayList<Long>()
				: commissioningWindowMapper.selectIdBySql(windowNameSql);
		tblTestTaskVo.setWindowIdList(windowIdList);
		

		String taskAssignUserName = tblTestTask.getTaskAssignUserName();
		String taskAssignUserNameSql = CommonUtil.getSearchSql("userinfo2.USER_NAME", 1, taskAssignUserName);
		if(taskAssignUserNameSql.equals("error")) {
			setErrorMap(result,"????????????");
			return result;
		}
		//List<Long> taskAssignUserIdList = StringUtils.isEmpty(taskAssignUserNameSql)?new ArrayList<Long>():tblUserInfoMapper.selectIdByUserNameSql(taskAssignUserNameSql);
		tblTestTaskVo.setTaskAssignUserId(taskAssignUserNameSql);
		
		
		String defectNum = tblTestTask.getDefectNum();
		String defectNumSql = CommonUtil.getSearchSql("da.defectNum", 2, defectNum);
		if(defectNumSql.equals("error")) {
			setErrorMap(result,"?????????");
			return result;
		}
		tblTestTaskVo.setDefectNum(defectNumSql);
		
		String testCaseNum = tblTestTask.getCaseNum();
		String testCaseNumStr = CommonUtil.getSearchSql("caseNum", 2, testCaseNum);
		if(testCaseNumStr.equals("error")) {
			setErrorMap(result,"?????????");
			return result;
		}
		List<Long> idListByNum=new ArrayList<>();
		if(!("").equals(testCaseNumStr)) {
			 idListByNum = tblTestTaskMapper.selectIdByCaseNumSql(testCaseNumStr);
		}
		
		if(StringUtils.isNotEmpty(testCaseNumStr)) {  //????????????????????????
			tblTestTaskVo.setIdList(idListByNum);
		}
		if((StringUtils.isNotEmpty(systemName) && CollectionUtil.isEmpty(systemIdList)) 
				|| (StringUtils.isNotEmpty(requirementCode) && CollectionUtil.isEmpty(reqIdList)) 
				|| (StringUtils.isNotEmpty(testUserName) && StringUtils.isEmpty(testUserNameSql)) 
				|| (StringUtils.isNotEmpty(testStageName) && StringUtils.isEmpty(testStageNameSql)) 
				|| (StringUtils.isNotEmpty(workTaskStatus) && StringUtils.isEmpty(testTaskStatusStr))
				|| (StringUtils.isNotEmpty(featureCode) && CollectionUtil.isEmpty(featureIdList)) 
				|| (StringUtils.isNotEmpty(windowName) && CollectionUtil.isEmpty(windowIdList))
				|| (StringUtils.isNotEmpty(taskAssignUserName) && StringUtils.isEmpty(taskAssignUserNameSql)) 
				|| (StringUtils.isNotEmpty(defectNum) && StringUtils.isEmpty(defectNumSql))
				|| (StringUtils.isNotEmpty(testCaseNum) && CollectionUtil.isEmpty(idListByNum))
				 ) {
			result.put("testWorkInputVo", null);
		}else if((StringUtils.isEmpty(systemName)) 
				&& (StringUtils.isEmpty(requirementCode)) 
				&& (StringUtils.isEmpty(testUserName)) 
				&& (StringUtils.isEmpty(testStageName))  
				&& (StringUtils.isEmpty(workTaskStatus)) 
				&& (StringUtils.isEmpty(featureCode)) 
				&& (StringUtils.isEmpty(windowName)) 
				&& (StringUtils.isEmpty(taskAssignUserName)) 
				&& (StringUtils.isEmpty(defectNum)) 
				&& (StringUtils.isEmpty(testCaseNum)) 
				&&(StringUtils.isEmpty(testTaskCode)) 
				&&(StringUtils.isEmpty(testTaskName)) 
				 ){
					result.put("testWorkInputVo", null);
		}else{
			result.put("testWorkInputVo", JSON.toJSONString(tblTestTaskVo));
			}
		return result;
	}

	private void setErrorMap(Map<String, Object> map, String fieldName) {
		map.put("status", Constants.ITMP_RETURN_FAILURE);
		map.put("message", fieldName + "????????????????????????????????????");
	}
}
