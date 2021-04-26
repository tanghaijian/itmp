package cn.pioneeruniverse.dev.service.testtask.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.pioneeruniverse.common.annotion.DataSource;
import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.databus.DataBusRequestHead;
import cn.pioneeruniverse.common.databus.DataBusUtil;
import cn.pioneeruniverse.common.velocity.tag.VelocityDataDict;
import cn.pioneeruniverse.dev.dao.mybatis.*;
import cn.pioneeruniverse.dev.dto.TaskFeatuDTO;
import cn.pioneeruniverse.dev.entity.*;
import cn.pioneeruniverse.dev.feignInterface.TestManageToProjectManageInterface;
import cn.pioneeruniverse.dev.feignInterface.TestManageToSystemInterface;
import cn.pioneeruniverse.dev.yiranUtil.DefectExport;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ccic.databus.client.publish.DataBusPublishClient;
import com.ccic.databus.common.exception.DataBusException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.SqlUtil;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.common.utils.CollectionUtil;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.JsonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.common.utils.ReflectUtils;
import cn.pioneeruniverse.common.utils.WorderToNewWordUtils;
import cn.pioneeruniverse.dev.feignInterface.TestManageToDevManageInterface;
import cn.pioneeruniverse.dev.service.testtask.TestTaskService;
import cn.pioneeruniverse.dev.vo.TestTaskInputVo;
import cn.pioneeruniverse.dev.vo.TestTaskVo;

/**
 * 类说明
 * 
 * @author:tingting
 * @version:2018年12月5日 下午3:13:08
 */
@Service
@Transactional(readOnly = true)
public class TestTaskServiceImpl implements TestTaskService {


	@Autowired
	private TaskFeatuMapper taskFeatuMapper;
	@Autowired
	private TblRequirementFeatureMapper requirementFeatureMapper;
	@Autowired
	private TblRequirementInfoMapper requirementInfoMapper;
	@Autowired
	private TblTestTaskMapper testTaskMapper;// 工作任务mapper
	@Autowired
	private TblCommissioningWindowMapper commissioningWindowMapper;
	@Autowired
	private TblRequirementFeatureAttachementMapper requirementFeatureAttachementMapper;
	@Autowired
	private TblSystemInfoMapper systemInfoMapper;
	@Autowired
	private TblRequirementFeatureRemarkMapper requirementFeatureRemarkMapper;
	@Autowired
	private TblRequirementFeatureRemarkAttachementMapper requirementFeatureRemarkAttachementMapper;
	@Autowired
	private TblRequirementFeatureLogMapper requirementFeatureLogMapper;
	@Autowired
	private TblRequirementFeatureLogAttachementMapper requirementFeatureLogAttachementMapper;
	@Autowired
	private RedisUtils redisUtils;
	@Autowired
	private TestManageToDevManageInterface testManageToDevManageInterface;
	@Autowired
	private TblDataDicMapper tblDataDicMapper;
	@Autowired
	private TblUserInfoMapper tblUserInfoMapper;
	@Autowired
	private TblDefectInfoMapper defectInfoMapper;
	@Autowired
	private TblProjectGroupUserMapper tblProjectGroupUserMapper;
	@Autowired
	private TblRequirementFeatureDeployStatusMapper deployStatusMapper;
	@Autowired
	private TblProjectInfoMapper projectInfoMapper;
	@Autowired
	private TestManageToProjectManageInterface testManageToProjectManageInterface;

	@Value("${databuscc.name}")
	private String databusccName;

	@Autowired
	private TblRequirementFeatureRelationMapper tblRequirementFeatureRelationMapper;
	@Autowired
	private TestManageToSystemInterface testManageToSystemInterface;


	public final static Logger logger = LoggerFactory.getLogger(TestTaskServiceImpl.class);
	
	private static final  String manageUserPropertyInfoName = "管理岗";
	private static final  String executeUserPropertyInfoName = "执行人";
	private static final  String deptPropertyInfoName = "所属处室";
	private static final  String systemPropertyInfoName = "涉及系统";
	private static final  String requirementPropertyInfoName = "关联需求";
	private static final  String commissioningWindowPropertyInfoName = "投产窗口";
	private static final  String sourcePropertyInfoName = "任务类型";
	private static final  String reqFeatureStatusInfoName = "任务状态";

	@Override
	public List<TestTaskVo> getAllReqFeature(TblRequirementFeature requirementFeature,Long uid,List<Long> systemIds, Integer page,
			Integer rows) {
		Map<String, Object> map = new HashMap<>();
		Integer start = (page - 1) * rows;
		map.put("start", start);
		map.put("pageSize", rows);
		map.put("reqFeature", requirementFeature);
		map.put("systemIds", systemIds);
		map.put("uid", uid);
		return requirementFeatureMapper.getAllReqFeature(map);
	}
	
	/**
	 * 
	* @Title: selectAll
	* @Description: 查询测试任务
	* @author author
	* @param jqGridPage
	* @param testTaskVo
	* @param roleCodes
	* @return JqGridPage<TestTaskVo>
	 */
	@Override
	public JqGridPage<TestTaskVo> selectAll(JqGridPage<TestTaskVo> jqGridPage,TestTaskVo testTaskVo,List<String> roleCodes) {
		try {
			jqGridPage.filtersAttrToEntityField(testTaskVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = filterSearch(testTaskVo);
		if(map.containsKey("status")) {
			jqGridPage.setStatus(map.get("status").toString());
			jqGridPage.setMessage(map.get("message").toString());
			return jqGridPage;
		}
		PageHelper.startPage(jqGridPage.getJqGridPrmNames().getPage(), jqGridPage.getJqGridPrmNames().getRows());
		List<TestTaskVo> list = new ArrayList<>();
		if(map.get("testTaskInputVo") != null) {
			TestTaskInputVo testTaskInputVo = JSON.parseObject(map.get("testTaskInputVo").toString(), TestTaskInputVo.class) ;
			if(testTaskInputVo.getSidx().equals("id")) {
				testTaskInputVo.setSidx("SYSTEM_CODE");
			}
			if (roleCodes!=null && roleCodes.contains("XITONGGUANLIYUAN")) {//当前登录用户有角色是系统管理员
				if("0".equals(testTaskVo.getRequirementFeatureStatus())) {
					testTaskVo.setRequirementFeatureStatus("");
				}
				long startTime = System.currentTimeMillis();
				list = requirementFeatureMapper.getAllBySql(testTaskInputVo);
				long endTime = System.currentTimeMillis();
				logger.info("测试任务查询接口耗时："+(endTime-startTime));
			}else {
				if("0".equals(testTaskVo.getRequirementFeatureStatus())) {
					testTaskVo.setRequirementFeatureStatus("");
				}
				list = requirementFeatureMapper.getAllConditionBySql(testTaskInputVo);
			}
		}
		
		for (TestTaskVo testTaskVo2 : list) {
			Long id = testTaskVo2.getId();
			String deployName = findDeployByReqFeatureId(testTaskVo2.getDeployStatusList());
			testTaskVo2.setDeployStatus(deployName);
			testTaskVo2.setChangeNumberStr(testTaskVo2.getChangeNumber() == null?"0":testTaskVo2.getChangeNumber().toString());
			testTaskVo2.settUserIds(testTaskVo2.gettUserIds() == null?new ArrayList<>():testTaskVo2.gettUserIds());
			testTaskVo2.setCreateTypeStr(CommonUtil.getDictValueName("TBL_REQUIREMENT_FEATURE_CREATE_TYPE", testTaskVo2.getCreateType() + "", ""));
			testTaskVo2.setRequirementFeatureSourceStr(CommonUtil.getDictValueName("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_SOURCE_2", testTaskVo2.getRequirementFeatureSource()+"" + "", ""));
			Map<String,Object> map2 = new HashMap<>();
			map2.put("systemId", testTaskVo2.getSystemId());
			map2.put("requirementId", testTaskVo2.getRequirementId());
			map2.put("commissioningWindowId", testTaskVo2.getCommissioningWindowId());
			int count = requirementFeatureMapper.getCountBySystemIdAndRequirementId(map2);
			if(count >= 2) {
				testTaskVo2.setFlag(true);
			}else {
				testTaskVo2.setFlag(false);
			}
		}
		PageInfo<TestTaskVo> pageInfo = new PageInfo<TestTaskVo>(list);
		jqGridPage.processDataForResponse(pageInfo);
		return jqGridPage;

	}
	/**
	 * 条件配置
	 * @param testTaskVo
	 */
	@Override
	public Map<String, Object> filterSearch(TestTaskVo testTaskVo) {
		Map<String, Object> result = new HashMap<String, Object>();
		TestTaskInputVo testTaskInputVo = new TestTaskInputVo();
		
		String featureCode = testTaskVo.getFeatureCode();
		String featureCodeSql = CommonUtil.getSearchSql("reqFeature.FEATURE_CODE", 1, featureCode);
		if(featureCodeSql.equals("error")) {
			setErrorMap(result,"任务编号");
			return result;
		}
		testTaskInputVo.setFeatureCodeSql(featureCodeSql);
		
		String featureName = testTaskVo.getFeatureName();
		String featureNameSql = CommonUtil.getSearchSql("reqFeature.FEATURE_NAME", 1, featureName);
		if(featureNameSql.equals("error")) {
			setErrorMap(result,"任务摘要");
			return result;
		}
		testTaskInputVo.setFeatureNameSql(featureNameSql);
		
		String systemName = testTaskVo.getSystemName();
		String systemNameSql = CommonUtil.getSearchSql("SYSTEM_NAME", 1, systemName);
		if(systemNameSql.equals("error")) {
			setErrorMap(result,"系统名称");
			return result;
		}
		List<Long> systemIdList = StringUtils.isEmpty(systemNameSql)?new ArrayList<Long>():systemInfoMapper.getSystemIdByInjectSql(systemNameSql);
		testTaskInputVo.setSystemIdList(systemIdList);
		
		String requirementCode = testTaskVo.getRequirementCode();
		String requirementCodeSql = CommonUtil.getSearchSql("REQUIREMENT_CODE", 1, requirementCode);
		if(requirementCodeSql.equals("error")) {
			setErrorMap(result,"需求编号");
			return result;
		}
		List<Long> reqIdList = StringUtils.isEmpty(requirementCodeSql)?new ArrayList<Long>():requirementInfoMapper.selectReqIdByReqCodeSql(requirementCodeSql);
		testTaskInputVo.setReqIdList(reqIdList);
		
		String windowName = testTaskVo.getWindowName();
		String windowNameSql = "";
		if(StringUtils.isNotEmpty(windowName)) {
			if(windowName.indexOf(">")!=-1 || windowName.indexOf("<")!=-1
					|| windowName.indexOf("=")!=-1) {     //如果投产窗口搜索内容为区间搜索，即大于小于
				if(windowName.indexOf("待定")!=-1) {		//如果内容中存在待定，分成两部分搜索，分别获取两段sql后拼接
					String endPartSql = CommonUtil.getSearchSql("WINDOW_NAME", 1, "待定");
					String startPartSql = windowName.replaceAll("待定", "");
					windowNameSql = CommonUtil.getSearchSql("WINDOW_DATE", 3, startPartSql) + " " + endPartSql;
				}else {
					windowNameSql = CommonUtil.getSearchSql("WINDOW_DATE", 3, windowName);
				}
			}else {   //如果投产窗口搜索内容为字符模糊搜索，即带星号
				windowNameSql = CommonUtil.getSearchSql("WINDOW_NAME", 1, windowName);
			}
		}
		if(windowNameSql.equals("error")) {
			setErrorMap(result,"投产窗口");
			return result;
		}
		List<Long> windowIdList = StringUtils.isEmpty(windowNameSql)?new ArrayList<Long>():commissioningWindowMapper.selectIdBySql(windowNameSql);
		testTaskInputVo.setWindowIdList(windowIdList);
		
		String requirementFeatureStatus = testTaskVo.getRequirementFeatureStatus();
		String reqFeatureStatusStr = CommonUtil.getSearchSql("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS_2", 4, requirementFeatureStatus);
		if(reqFeatureStatusStr.equals("error")) {
			setErrorMap(result,"任务状态");
			return result;
		}
		testTaskInputVo.setReqFeatureStatusStr(reqFeatureStatusStr);
		
		String manageUserName = testTaskVo.getManageUserName();
		String manageUserNameSql = CommonUtil.getSearchSql("userinfo.USER_NAME", 1, manageUserName);
		if(manageUserNameSql.equals("error")) {
			setErrorMap(result,"测试管理岗");
			return result;
		}
		testTaskInputVo.setManageUserSql(manageUserNameSql);
		
		String executeUserName = testTaskVo.getExecuteUserName();
		String executeUserNameSql = CommonUtil.getSearchSql("userinfo2.USER_NAME", 1, executeUserName);
		if(executeUserNameSql.equals("error")) {
			setErrorMap(result,"执行人");
			return result;
		}
		testTaskInputVo.setExecuteUserSql(executeUserNameSql);
		
		String deployStatus = testTaskVo.getDeployStatus();
		String deployStatusStr = "";
		boolean isNot = false;
		if(StringUtils.isNotEmpty(deployStatus) && deployStatus.indexOf("not") != -1) {
			deployStatusStr = CommonUtil.getSearchSql("TBL_REQUIREMENT_FEATURE_DEPLOY_STATUS", 4, deployStatus.replaceAll("not", ""));
			isNot = true;
		}else {
			deployStatusStr = CommonUtil.getSearchSql("TBL_REQUIREMENT_FEATURE_DEPLOY_STATUS", 4, deployStatus);
		}
		if(deployStatusStr.equals("error")) {
			setErrorMap(result,"部署状态");
			return result;
		}
		List<Long> idListByDeploy = StringUtils.isEmpty(deployStatusStr)?new ArrayList<Long>():(isNot?deployStatusMapper.getNotReqFeatureIdBySql(deployStatusStr):deployStatusMapper.getReqFeatureIdBySql(deployStatusStr));
		if(StringUtils.isNotEmpty(deployStatus)) {			//部署状态有搜索内容
			testTaskInputVo.setIdList(idListByDeploy);
		}
		
		String requirementFeatureSource = testTaskVo.getRequirementFeatureSourceStr();
		String requirementFeatureSourceStr = CommonUtil.getSearchSql("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_SOURCE_2", 4, requirementFeatureSource);
		if(requirementFeatureSourceStr.equals("error")) {
			setErrorMap(result,"任务来源");
			return result;
		}
		testTaskInputVo.setRequirementFeatureSourceStr(requirementFeatureSourceStr);
		
		String changeNumber = testTaskVo.getChangeNumberStr();
		String changeNumberStr = CommonUtil.getSearchSql("reqFeature.REQUIREMENT_CHANGE_NUMBER", 2, changeNumber);
		if(changeNumberStr.equals("error")) {
			setErrorMap(result,"需求变更次数");
			return result;
		}
		testTaskInputVo.setChangeNumberSql(changeNumberStr);
		
		String createType = testTaskVo.getCreateTypeStr();
		String createTypeStr = CommonUtil.getSearchSql("TBL_REQUIREMENT_FEATURE_CREATE_TYPE", 4, createType);
		if(createTypeStr.equals("error")) {
			setErrorMap(result,"创建方式");
			return result;
		}
		testTaskInputVo.setCreateTypeStr(createTypeStr);
		
		String defectNum = testTaskVo.getDefectNumStr();
		String defectNumStr = CommonUtil.getSearchSql("defectNum", 2, defectNum);
		if(defectNumStr.equals("error")) {
			setErrorMap(result,"缺陷数");
			return result;
		}
		
		String testCaseNum = testTaskVo.getTestCaseNumStr();
		String testCaseNumStr = CommonUtil.getSearchSql("caseNum", 2, testCaseNum);
		if(testCaseNumStr.equals("error")) {
			setErrorMap(result,"案例数");
			return result;
		}
		
		List<Long> idListByNum = requirementFeatureMapper.selectIdByDefectNumSql(defectNumStr,testCaseNumStr);
		if(StringUtils.isNotEmpty(defectNumStr) || StringUtils.isNotEmpty(testCaseNumStr)) {  //缺陷数有搜索内容
			testTaskInputVo.setIdList(idListByNum);
		}
		if(StringUtils.isNotEmpty(deployStatus) && (StringUtils.isNotEmpty(defectNumStr) || StringUtils.isNotEmpty(testCaseNumStr))) {   //缺陷数、部署状态、案例数都有搜索内容
			List<Long> idList = getSameList(idListByDeploy,idListByNum);
			testTaskInputVo.setIdList(idList);
		}
		testTaskInputVo.setUid(testTaskVo.getUid());
		testTaskInputVo.setSidx(testTaskVo.getSidx());
		testTaskInputVo.setSord(testTaskVo.getSord());
		if((StringUtils.isNotEmpty(systemName) && CollectionUtil.isEmpty(systemIdList)) 
				|| (StringUtils.isNotEmpty(requirementFeatureStatus) && StringUtils.isEmpty(reqFeatureStatusStr)) 
				|| (StringUtils.isNotEmpty(deployStatus) && CollectionUtil.isEmpty(idListByDeploy)) 
				|| (StringUtils.isNotEmpty(requirementFeatureSource) && StringUtils.isEmpty(requirementFeatureSourceStr)) 
				|| (StringUtils.isNotEmpty(createType) && StringUtils.isEmpty(createTypeStr))
				|| (StringUtils.isNotEmpty(requirementCode) && CollectionUtil.isEmpty(reqIdList)) 
				|| (StringUtils.isNotEmpty(windowName) && CollectionUtil.isEmpty(windowIdList))
				|| (StringUtils.isNotEmpty(defectNum) && CollectionUtil.isEmpty(idListByNum))
				|| (StringUtils.isNotEmpty(testCaseNum) && CollectionUtil.isEmpty(idListByNum))) {
			result.put("testTaskInputVo", null);
		}else if((StringUtils.isEmpty(featureCode))
				&&(StringUtils.isEmpty(featureName))
				&&(StringUtils.isEmpty(systemName))
				&&(StringUtils.isEmpty(requirementCode))
				&&(StringUtils.isEmpty(windowName))
				&&(StringUtils.isEmpty(requirementFeatureStatus))
				&&(StringUtils.isEmpty(manageUserName))
				&&(StringUtils.isEmpty(executeUserName))
				&&(StringUtils.isEmpty(deployStatus))
				&&(StringUtils.isEmpty(requirementFeatureSource))
				&&(StringUtils.isEmpty(changeNumber))
				&&(StringUtils.isEmpty(createType))
				&&(StringUtils.isEmpty(defectNum))
				&&(StringUtils.isEmpty(testCaseNum))
				){
			result.put("testTaskInputVo", null);
		}else {
			result.put("testTaskInputVo", JSON.toJSONString(testTaskInputVo));
		}
		return result;
	}
	
	private void setErrorMap(Map<String, Object> map,String fieldName) {
		map.put("status", Constants.ITMP_RETURN_FAILURE);
		map.put("message", fieldName+"搜索内容不能存在空格符号");
	}
	
	/**
	 * 获取相同集合
	 * @param list1
	 * @param list2
	 * @return
	 */
	private List<Long> getSameList(List<Long> list1,List<Long> list2){
		List<Long> newList = new ArrayList<Long>();
		if(CollectionUtil.isNotEmpty(list1) && CollectionUtil.isNotEmpty(list1)) {
			for(Long num : list1) {
				if(list2.contains(num)) {
					newList.add(num);
				}
			}
		}
		return newList;
	}
	
	/**
	 * 根据测试任务id获取部署状态
	 * @param deployStatusList
	 * @return
	 */
	public String findDeployByReqFeatureId(List<TblRequirementFeatureDeployStatus> deployStatusList){
		String deployName = "";
		if(deployStatusList!=null&&deployStatusList.size()>0){
			for (TblRequirementFeatureDeployStatus deployStatus : deployStatusList){
				deployName = deployName + getDeployName(deployStatus.getDeployStatu())+ " | " +
						getTime(deployStatus.getDeployTime()) + "，";
			}
		}
		return deployName;
	}

    @Override
    public String findDeployByReqFeatureId1(Long featureId){
        List<TblRequirementFeatureDeployStatus> deployStatusList =
                                deployStatusMapper.findByReqFeatureId(featureId);
        String deployArr = "";
        if(deployStatusList!=null&&deployStatusList.size()>0){
            for (TblRequirementFeatureDeployStatus deployStatus : deployStatusList){
                deployArr = deployStatus.getDeployStatu()+","+deployArr;
            }
        }
        return deployArr;
    }

	public List<Long> findDploys(String deployStatus) {
		// TODO Auto-generated method stub
		String termCode = "TBL_REQUIREMENT_FEATURE_DEPLOY_STATUS";
		Map<String,Object> map = new HashMap<>();
		map.put("termCode", termCode);
		map.put("deployStatus", deployStatus);
		return tblDataDicMapper.findDploys(map);
	}

	@Override
	public Map<String, Object> getOneTestTask(Long id) {
		Map<String, Object> oneTestTask = requirementFeatureMapper.getOneTestTask(id);
		if(oneTestTask.get("projectPlanId") != null){
			Long projectPlanId = Long.valueOf(oneTestTask.get("projectPlanId").toString());
			logger.info(projectPlanId.toString());
			Map<String, Object> projectPlan = testManageToProjectManageInterface.getProjectPlanById(projectPlanId)	;
			if(projectPlan != null && projectPlan.get("planName") != null){
				oneTestTask.put("planName",projectPlan.get("planName").toString());
			}
		}
    // 获取开发任务名称
    if (oneTestTask.get("systemId") != null) {
    	TblSystemInfo tblSystemInfo=systemInfoMapper.selectByPrimaryKey(Long.parseLong(oneTestTask.get("systemId").toString()));
		oneTestTask.put("developmentMode", tblSystemInfo.getDevelopmentMode());
      if (tblSystemInfo.getDevelopmentMode() != null ) {
		  oneTestTask.put("developmentMode", tblSystemInfo.getDevelopmentMode());
        if (oneTestTask.get("devRequirementFeatureId") != null) {

			oneTestTask.put("devRequirementFeatureId", oneTestTask.get("devRequirementFeatureId").toString());
          Map<String, Object> result =
              testManageToDevManageInterface.getRequireFeature( null, Long.parseLong( oneTestTask.get("devRequirementFeatureId").toString()));
          String devRequirementFeatureName = "";
          List<Map<String, Object>> requirementFeatures = (List<Map<String, Object>>) result.get("list");
          if (requirementFeatures != null && requirementFeatures.size() > 0) {
            devRequirementFeatureName = requirementFeatures.get(0).get("featureName").toString();
          }
          oneTestTask.put("devRequirementFeatureName", devRequirementFeatureName);
        }
      }
		}

		List<TaskFeatuDTO> taskFeatu = taskFeatuMapper.selectTaskFeatuByID(id);
		//Map<String, Object> stringObjectMap = DefectExport.taskFeatuDate(taskFeatu, oneTestTask);


		Object object = oneTestTask.get("requirementFeatureSource");
		 if(object != null) {
			 String str = object.toString();
			 String string = redisUtils.get("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_SOURCE_2").toString();
			 JSONObject jsonObject = JSON.parseObject(string);
			 String featureSource = null;
			 if(jsonObject.get(str) != null){
				 featureSource = jsonObject.get(str).toString();
			 }
			 oneTestTask.put("featureSource", featureSource);
		 }
		 //系测设计案例数
		 Long designCaseNumber = requirementFeatureMapper.getDesignCaseNumber(id);
		 oneTestTask.put("designCaseNumber", designCaseNumber);
		 //系测执行案例数
		 Long executeCaseNumber = requirementFeatureMapper.getExecuteCaseNumber(id);
		 oneTestTask.put("executeCaseNumber", executeCaseNumber);
		 //系测缺陷数
		 Long defectNum = requirementFeatureMapper.getDefectNumber(id);
		 oneTestTask.put("defectNum", defectNum);
		 //版测设计案例数
		 Long designCaseNumber2 = requirementFeatureMapper.getDesignCaseNumber2(id);
		 oneTestTask.put("designCaseNumber2", designCaseNumber2);
		 //版测执行案例数
		 Long executeCaseNumber2 = requirementFeatureMapper.getExecuteCaseNumber2(id);
		 oneTestTask.put("executeCaseNumber2", executeCaseNumber2);
		 //版测缺陷数
		 Long defectNum2 = requirementFeatureMapper.getDefectNumber2(id);
		 oneTestTask.put("defectNum2", defectNum2);
		 //系测工作量
		 Double workload = requirementFeatureMapper.getWorkload(id);
		 oneTestTask.put("workload", workload);
		 //版测工作量
		 Double workload2 = requirementFeatureMapper.getWorkload2(id);
		 oneTestTask.put("workload2", workload2);
		 return oneTestTask;
	}
	/**
	 * 导出word
	 * @throws Exception 
	 */
	@Override
	public void exportWord(Long id,HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, Object> dataMap = new HashMap<>();
		Map<String, Object> oneTestTask = requirementFeatureMapper.getOneTestTask(id);
		String featureOverview = oneTestTask.get("featureOverview") == null?"":oneTestTask.get("featureOverview").toString();
		String filename = "测试任务_"+oneTestTask.get("featureCode").toString()+".docx";
		//任务编号+任务名称
		String feature = "[" + oneTestTask.get("featureCode").toString() +" | "+ oneTestTask.get("featureName").toString() + "]\t"+featureOverview;
		oneTestTask.put("feature", feature);
		//创建日期+修改日期
		oneTestTask.put("date", "创建日期："+(oneTestTask.get("createDate") == null?"":oneTestTask.get("createDate").toString())+
				" 最后修改日期："+(oneTestTask.get("lastUpdateDate") == null?"":oneTestTask.get("lastUpdateDate").toString()));
		String requirementFeatureStatusName = CommonUtil.getDictValueName("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS_2",oneTestTask.get
				("requirementFeatureStatus") == null?"":oneTestTask.get("requirementFeatureStatus").toString() , "");
		String requirementFeatureSourceName = CommonUtil.getDictValueName("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_SOURCE_2", oneTestTask.get
				("requirementFeatureSource") == null?"":oneTestTask.get("requirementFeatureSource").toString(), "");
		String createTypeName = CommonUtil.getDictValueName("TBL_REQUIREMENT_FEATURE_CREATE_TYPE", oneTestTask.get
				("createType") == null?"":oneTestTask.get("createType").toString(), "");
		
		oneTestTask.put("requirementFeatureStatusName", requirementFeatureStatusName);
		oneTestTask.put("requirementFeatureSourceName", requirementFeatureSourceName);
		oneTestTask.put("createTypeName", createTypeName);
		 //系测设计案例数
		 Long designCaseNumber = requirementFeatureMapper.getDesignCaseNumber(id);
		 oneTestTask.put("designCaseNumber", designCaseNumber);
		 //系测执行案例数
		 Long executeCaseNumber = requirementFeatureMapper.getExecuteCaseNumber(id);
		 oneTestTask.put("executeCaseNumber", executeCaseNumber);
		 //系测缺陷数
		 Long defectNum = requirementFeatureMapper.getDefectNumber(id);
		 oneTestTask.put("defectNum", defectNum);
		 //版测设计案例数
		 Long designCaseNumber2 = requirementFeatureMapper.getDesignCaseNumber2(id);
		 oneTestTask.put("designCaseNumber2", designCaseNumber2);
		 //版测执行案例数
		 Long executeCaseNumber2 = requirementFeatureMapper.getExecuteCaseNumber2(id);
		 oneTestTask.put("executeCaseNumber2", executeCaseNumber2);
		 //版测缺陷数
		 Long defectNum2 = requirementFeatureMapper.getDefectNumber2(id);
		 oneTestTask.put("defectNum2", defectNum2);
		 //日志
		 List<TblRequirementFeatureLog> logs = findLog(id);
		 String logStr = "";
		 for (TblRequirementFeatureLog tblRequirementFeatureLog : logs) {
			String str = tblRequirementFeatureLog.getLogType() + "\t" + tblRequirementFeatureLog.getUserName() + " | " + tblRequirementFeatureLog.getUserAccount()
			+ "\t" + tblRequirementFeatureLog.getCreateDate() + "\r";
			String logDetail = tblRequirementFeatureLog.getLogDetail();
			if(StringUtils.isNotEmpty(logDetail)) {
				logDetail = logDetail.replaceAll("；", "\r").replaceAll("&nbsp;|<b>|</b>", "");
			}
			logStr += str + logDetail + "\r";
		}
		 oneTestTask.put("logs", logStr);
		 
		 for (Entry<String, Object> entry : oneTestTask.entrySet()) {
			String key = entry.getKey();
			dataMap.put("${"+key+"}$", entry.getValue() == null?"":entry.getValue().toString());
		}
		 InputStream	is = this.getClass().getResourceAsStream("/tmp/testTaskTmp.docx");
		 XWPFDocument document = WorderToNewWordUtils.changWord(is, dataMap, new HashMap<>());
			
			
			String useragent = request.getHeader("User-Agent");
	        filename = URLEncoder.encode(filename, "utf-8");
	        filename = filename.replace("+", " ");
			OutputStream outputStream = response.getOutputStream();
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename="+filename);
			document.write(outputStream);
			outputStream.flush();
			outputStream.close();
	}

	@Override
	public List<Map<String, Object>> findByReqFeature(Long id) {
		return testTaskMapper.findByReqFeature(id);
	}

	@Override
	@Transactional(readOnly = false)
	public TblRequirementFeature addTestTask(TblRequirementFeature requirementFeature, List<TblRequirementFeatureAttachement> files,
			HttpServletRequest request, String developmentDeptNumber) {
		Long deptId = requirementFeatureMapper.findDeptIdByDeptNumber(developmentDeptNumber);
		requirementFeature.setDevelopmentDeptId(deptId);
		requirementFeatureMapper.insertReqFeature(requirementFeature);


		if(requirementFeature.getDevRequirementFeatureId()!=null){
			//增加关联开发任务
			TblRequirementFeatureRelation tblRequirementFeatureRelation=new TblRequirementFeatureRelation();
			tblRequirementFeatureRelation.setDevRequirementFeatureId(requirementFeature.getDevRequirementFeatureId());
			tblRequirementFeatureRelation.setTestRequirementFeatureId(requirementFeature.getId());
			tblRequirementFeatureRelation.setStatus(1);
			tblRequirementFeatureRelation.setCreateDate(new Timestamp(new Date().getTime()));
			tblRequirementFeatureRelation.setCreateBy(CommonUtil.getCurrentUserId(request));
			tblRequirementFeatureRelationMapper.insertNew(tblRequirementFeatureRelation);
		}
		Long reqFeatureId = requirementFeature.getId();
		if (files != null && files.size() > 0) {
			for (TblRequirementFeatureAttachement tblRequirementFeatureAttachement : files) {
				tblRequirementFeatureAttachement.setRequirementFeatureId(reqFeatureId);
				tblRequirementFeatureAttachement.setCreateBy(CommonUtil.getCurrentUserId(request));
				tblRequirementFeatureAttachement.setCreateDate(new Timestamp(new Date().getTime()));
				requirementFeatureAttachementMapper.insertAtt(tblRequirementFeatureAttachement);
			}
		}





		//判断是否修改剩余时间
		if (requirementFeature.getSystemId() != null && requirementFeature.getEstimateWorkload()!=null ) {
			TblSystemInfo tblSystemInfo =
					systemInfoMapper.selectByPrimaryKey(requirementFeature.getSystemId());

      // 修改冲刺任务
      if (tblSystemInfo.getDevelopmentMode() != null && tblSystemInfo.getDevelopmentMode() == 1) {
        Map<String, Object> param = new HashMap<>();
        param.put("oldWorkLoad", new BigDecimal(0));
        param.put("newWorkLoad", new BigDecimal(requirementFeature.getEstimateWorkload()));
        param.put("type", 1);
        param.put("devTaskId", requirementFeature.getDevRequirementFeatureId());
        testManageToDevManageInterface.updateSprintWorkLoad(JSON.toJSONString(param));
			}
		}

		 //给该测试任务的测试管理岗和执行人发送邮件和微信 --ztt
		if (requirementFeature.getSystemId()!=null) {
			TblSystemInfo systemInfo = systemInfoMapper.selectByPrimaryKey(requirementFeature.getSystemId());
			if (systemInfo.getTaskMessageStatus()==1 && (requirementFeature.getManageUserId()!=null || requirementFeature.getExecuteUserId()!=null)) {
	        	Map<String,Object> emWeMap = new HashMap<String, Object>();
	    		emWeMap.put("messageTitle", "【IT开发测试管理系统】- 收到一个新分配的测试任务");
	    		emWeMap.put("messageContent","您收到一个新分配的测试任务：“"+ requirementFeature.getFeatureCode()+" | "+ requirementFeature.getFeatureName()+"”，请及时处理。");
	    		emWeMap.put("messageReceiver",requirementFeature.getManageUserId()+","+ requirementFeature.getExecuteUserId()+"," );//接收人测试管理岗和执行人 用户表主键
	    		emWeMap.put("sendMethod", 3);//发送方式 3 邮件和微信
	    		testManageToSystemInterface.sendMessage(JSON.toJSONString(emWeMap));
			}

		}
        


		//
		//		//判断是否修改剩余时间
		//		if (requirementFeature.getSystemId() != null) {
		//			TblSystemInfo tblSystemInfo =
		//					systemInfoMapper.selectByPrimaryKey(requirementFeature.getSystemId());
		//			if (tblSystemInfo.getDevelopmentMode() == 1) {
		//				Map<String, Object> param = new HashMap<>();
		//				//获取此任务关联开发任务id
		//				String DevTaskId=requirementFeatureMapper.findDevTaskId(requirementFeature.getId());
		//				param.put("devTaskId", DevTaskId);
		//				param.put("newWorkLoad", requirementFeature.getEstimateRemainWorkload());
		//				param.put("type", 1);
		//				param.put("add", "true");
		//				testManageToDevManageInterface.updateSprintWorkLoad(JSON.toJSONString(param));
		//
		//			}
		//		}
		TblRequirementFeatureLog log = new TblRequirementFeatureLog();
		log.setRequirementFeatureId(reqFeatureId);
		log.setLogType("新增测试任务");
		insertLog(log, request);
		
		return requirementFeatureMapper.selectByPrimaryKey(reqFeatureId);
	}

	@Override
	@Transactional(readOnly = false)
	public void addWorkTask(Long id,TblTestTask testTask,HttpServletRequest request) {
		TblRequirementFeature requirementFeature2 = requirementFeatureMapper.selectByPrimaryKey(id);
		testTask.setTestTaskStatus(1);// 新增时状态为未实施
		testTask.setCreateBy(CommonUtil.getCurrentUserId(request));
		testTask.setCreateDate(new Timestamp(new Date().getTime()));
		testTask.setRequirementFeatureId(id);
		testTaskMapper.addTestTask(testTask);
		String status = "";
		String afterName = "";
		List<TblDataDic> dataDics = getDataFromRedis("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS_2");
		for (TblDataDic tblDataDic : dataDics) {
			if (requirementFeature2.getRequirementFeatureStatus()!=null) {
				if (tblDataDic.getValueCode().equals(requirementFeature2.getRequirementFeatureStatus())) {
					afterName = tblDataDic.getValueName();
				}
			}
		}
		

		Long testId = testTask.getId();
		Map<Object,Object> map = new HashMap<>();
  		map.put("id", testId);
  		List<Integer> testStages = testTaskMapper.selectTestStageById(testId);//查出同一测试任务下的工作任务的测试阶段(排除撤销的工作任务)
  		if(testStages.size() == 1) { //长度为1，全部都是版测或系测
			if(testStages.get(0)==1) {//测试任务下的工作任务都为系测
				List<Integer> testTaskStatus = testTaskMapper.selectTestTaskStatusById(testId);
				if(testTaskStatus.size()==1 && testTaskStatus.get(0)==3) {//工作任务都是测试完成
					map.put("status", "03");
					testTaskMapper.updateReqFeaStatus(map);//系测完成
					status = "&nbsp;&nbsp;“<b>"+afterName+"</b>”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“<b>系测完成</b>”&nbsp;&nbsp;；";
				}else {
					map.put("status", "02");
					testTaskMapper.updateReqFeaStatus(map);//系测中
					status = "&nbsp;&nbsp;“<b>"+afterName+"</b>”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“<b>系测中</b>”&nbsp;&nbsp;；";
				}
			}else if(testStages.get(0)==2) {//测试任务下的工作任务都为版测
				List<Integer> testTaskStatus = testTaskMapper.selectTestTaskStatusById(testId);
				if(testTaskStatus.size()==1 && testTaskStatus.get(0)==3) {//工作任务都是测试完成
					map.put("status", "08");
					testTaskMapper.updateReqFeaStatus(map);//版测完成
					status = "&nbsp;&nbsp;“<b>"+afterName+"</b>”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“<b>版测完成</b>”&nbsp;&nbsp;；";
				}else {
					map.put("status", "06");
					testTaskMapper.updateReqFeaStatus(map);//版测中
					status = "&nbsp;&nbsp;“<b>"+afterName+"</b>”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“<b>版测中</b>”&nbsp;&nbsp;；";
				}
			}
		}else if(testStages.size() == 2){ //长度不为1(为2)，有版测也有系测
			List<Integer> testTaskStatus = testTaskMapper.selectTestTaskStatusById2(testId);
			if(testTaskStatus.size()==1 && testTaskStatus.get(0)==3) {//工作任务都是测试完成
				map.put("status", "08");
				testTaskMapper.updateReqFeaStatus(map);//版测完成
				status = "&nbsp;&nbsp;“<b>"+afterName+"</b>”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“<b>版测完成</b>”&nbsp;&nbsp;；";
			}else {
				map.put("status", "06");
				testTaskMapper.updateReqFeaStatus(map);//版测中
				status = "&nbsp;&nbsp;“<b>"+afterName+"</b>”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“<b>版测中</b>”&nbsp;&nbsp;；";
			}
		}
//		if(testTask.getTestStage()==1){
//			status = "&nbsp;&nbsp;“<b>"+afterName+"</b>”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“<b>系测中</b>”&nbsp;&nbsp;；";
//		}else{
//			status = "&nbsp;&nbsp;“<b>"+afterName+"</b>”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“<b>版测中</b>”&nbsp;&nbsp;；";
//		}
  		
  		//给该测试工作任务的执行人发送邮件和微信 --ztt
  		if (requirementFeature2.getSystemId()!=null) {
			TblSystemInfo systemInfo = systemInfoMapper.selectByPrimaryKey(requirementFeature2.getSystemId());
			if (systemInfo.getTaskMessageStatus()==1 && testTask.getTestUserId()!=null) {
	        	Map<String,Object> emWeMap = new HashMap<String, Object>();
	    		emWeMap.put("messageTitle", "【IT开发测试管理系统】- 收到一个新分配的工作任务");
	    		emWeMap.put("messageContent","您收到一个新分配的工作任务：“"+ testTask.getTestTaskCode()+" | "+ testTask.getTestTaskName()+"”，请及时处理。");
	    		emWeMap.put("messageReceiver",testTask.getTestUserId());//接收人测试人 用户表主键
	    		emWeMap.put("sendMethod", 3);//发送方式 3 邮件和微信
	    		testManageToSystemInterface.sendMessage(JSON.toJSONString(emWeMap));
			}
  		}
        

		TblRequirementFeatureLog log = new TblRequirementFeatureLog();
		log.setRequirementFeatureId(id);
		log.setLogType("拆分测试任务");
		
		log.setLogDetail(status+"拆分出工作任务："+testTask.getTestTaskCode()+" | "+testTask.getTestTaskName());
		insertLog(log, request);
	}

	@Override
	@Transactional(readOnly = false)
	public void handleTestTask(TblRequirementFeature requirementFeature, List<TblRequirementFeatureAttachement> files,
			HttpServletRequest request) {
		TblRequirementFeature requirementFeature2 = requirementFeatureMapper
				.selectByPrimaryKey(requirementFeature.getId());
		requirementFeatureMapper.updateTestTask(requirementFeature);
		//修改开发任务时间点追踪表  开发任务对应的测试任务首次变成实施完成时间 字段 
		Map<String, Object> jsonMap = new HashMap<>();
		TblRequirementFeature requirementFeature4 = requirementFeatureMapper.selectByPrimaryKey(requirementFeature.getId());
		if (requirementFeature4.getSystemId()!=null && requirementFeature4.getRequirementId()!=null) {
			jsonMap.put("requirementFeatureId", requirementFeature.getId());
			jsonMap.put("systemId", requirementFeature4.getSystemId());
			jsonMap.put("requirementId",requirementFeature4.getRequirementId() );
			jsonMap.put("requirementFeatureTestCompleteTime",new Timestamp(new Date().getTime()) );
			String jsonString = JsonUtil.toJson(jsonMap);
			testManageToDevManageInterface.updateReqFeatureTimeTrace(jsonString);
		}
		//为了找到这一次操作删除的附件 先查询出前当任务下所有的附件 和 返回的带id的附件比较 少了哪个 哪个就是删除了
		List<TblRequirementFeatureAttachement> allAtts = requirementFeatureAttachementMapper.findAttByRFId(requirementFeature.getId());
		
		getInsertAtts(requirementFeature.getId(),files, request, allAtts);
		
		TblRequirementFeatureLog log = new TblRequirementFeatureLog();
		log.setRequirementFeatureId(requirementFeature.getId());
		log.setLogType("处理开发任务");
		Map<String, String> map = new HashMap<>();
		map.put("actualSitStartDate", "actualSitStartDate");
		map.put("actualSitEndDate", "actualSitEndDate");
		map.put("actualSitWorkload", "actualSitWorkload");
		map.put("actualPptStartDate", "actualPptStartDate");
		map.put("actualPptEndDate", "actualPptEndDate");
		map.put("actualPptWorkload", "actualPptWorkload");
		String detail = ReflectUtils.packageModifyContent(requirementFeature, requirementFeature2, map);
		String status = "";
		if ("01".equals(requirementFeature2.getRequirementFeatureStatus())) {
			status = "&nbsp;&nbsp;“<b>系测待测试</b>”&nbsp;&nbsp;修改为&nbsp;&nbsp;“</b>系测完成</b>”&nbsp;&nbsp;；";
		} else if ("02".equals(requirementFeature2.getRequirementFeatureStatus())) {
			status = "&nbsp;&nbsp;“<b>系测中</b>”&nbsp;&nbsp;修改为&nbsp;&nbsp;“<b>系测完成</b>”&nbsp;&nbsp;；";
		} else if ("04".equals(requirementFeature2.getRequirementFeatureStatus())) {
			status = "&nbsp;&nbsp;“<b>系测待审核</b>”&nbsp;&nbsp;修改为&nbsp;&nbsp;“</b>系测完成</b>”&nbsp;&nbsp;；";
		}else if ("05".equals(requirementFeature2.getRequirementFeatureStatus())) {
			status = "&nbsp;&nbsp;“<b>版测待测试</b>”&nbsp;&nbsp;修改为&nbsp;&nbsp;“</b>版测完成</b>”&nbsp;&nbsp;；";
		}else if ("06".equals(requirementFeature2.getRequirementFeatureStatus())) {
			status = "&nbsp;&nbsp;“<b>版测中</b>”&nbsp;&nbsp;修改为&nbsp;&nbsp;“</b>版测完成</b>”&nbsp;&nbsp;；";
		}else if ("07".equals(requirementFeature2.getRequirementFeatureStatus())) {
			status = "&nbsp;&nbsp;“<b>版测待审核</b>”&nbsp;&nbsp;修改为&nbsp;&nbsp;“</b>版测完成</b>”&nbsp;&nbsp;；";
		}
		log.setLogDetail(status + detail);
		insertLog(log, request);
		
		getInsertLogAtts(files, request, allAtts, log);
		TblRequirementFeature requirementFeature3 = requirementFeatureMapper
				.selectByPrimaryKey(requirementFeature.getId());
		if (requirementFeature3.getCreateType().equals(2)) {
			Map<String, Object> dataResult = pushData(requirementFeature3);

			String taskId = "";
			if(requirementFeature3.getFeatureCode()!=null) {
				taskId = requirementFeature3.getFeatureCode();
			}

			// 使用databus
			DataBusUtil.send(databusccName,taskId,JsonUtil.toJson(dataResult));
		}
	
	}
	public Map<String, Object> pushData(TblRequirementFeature requirementFeature) {
		Map<String, Object> mapAll = new LinkedHashMap<>();

		Map<String, Object> mapBody = new HashMap<>();
		mapBody.put("tbltaskId", requirementFeature.getFeatureCode());
		mapBody.put("taskResult","测试完成");

		Double actualSitWorkload=requirementFeatureMapper.getWorkload(requirementFeature.getId());
		Double actualPptWorkload=requirementFeatureMapper.getWorkload2(requirementFeature.getId());
		if(actualSitWorkload!=null&&actualSitWorkload>0) {
			mapBody.put("taskWorkload",actualSitWorkload);
		}else if(actualPptWorkload!=null&&actualPptWorkload>0){
			mapBody.put("taskWorkload",actualPptWorkload);
		}else {
			mapBody.put("taskWorkload",0.0);
		}
		mapAll.put("requestHead",DataBusRequestHead.getRequestHead());
		mapAll.put("requestBody",mapBody);
		return mapAll;
	}
	
	/**
	 * 
	* @Title: updateTestTask
	* @Description: 更新测试任务
	* @author author
	* @param requirementFeature 测试任务信息
	* @param files
	* @param request
	* @param developmentDeptNumber 开发部门编号
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateTestTask(TblRequirementFeature requirementFeature, List<TblRequirementFeatureAttachement> files,
			HttpServletRequest request, String developmentDeptNumber) {
		Long deptId = requirementFeatureMapper.findDeptIdByDeptNumber(developmentDeptNumber);
		requirementFeature.setDevelopmentDeptId(deptId);
		TblRequirementFeature requirementFeature2 = requirementFeatureMapper
				.selectByPrimaryKey(requirementFeature.getId());
		requirementFeatureMapper.updateByPrimaryKey(requirementFeature);
		Long userId = CommonUtil.getCurrentUserId(request);
		if(requirementFeature.getCreateType()==2) {
			TblUserInfo userInfo = tblUserInfoMapper.getUserById(userId);
			String beforeName = commissioningWindowMapper.getWindowName(requirementFeature2.getCommissioningWindowId());
			String afterName = commissioningWindowMapper.getWindowName(requirementFeature.getCommissioningWindowId());
			String deptBeforeName = requirementFeatureMapper.getDeptName(requirementFeature2.getDeptId());
			String deptAfterName = requirementFeatureMapper.getDeptName(requirementFeature.getDeptId());
			TblRequirementFeatureLog log2 = new TblRequirementFeatureLog();
			log2.setCreateBy(log2.getUserId());
			log2.setCreateDate(new Timestamp(new Date().getTime()));
			log2.setUserId(userId);
			log2.setUserName(userInfo.getUserName());
			log2.setUserAccount(userInfo.getUserAccount());
			String loginfo = JsonUtil.toJson(log2);
			if(requirementFeature.getCommissioningWindowId()==null) {
				requirementFeature.setCommissioningWindowId((long) 0);
			}
			if(requirementFeature.getDeptId()==null) {
				requirementFeature.setDeptId((long) 0);
			}
			/*if(requirementFeature2.getCommissioningWindowId() != requirementFeature.getCommissioningWindowId()) {
				testManageToDevManageInterface.synReqFeaturewindow(requirementFeature.getRequirementId(),
						requirementFeature.getSystemId(), requirementFeature.getCommissioningWindowId(), loginfo,beforeName,afterName);
			}*/
			if(requirementFeature2.getDeptId() != requirementFeature.getDeptId()) {
				testManageToDevManageInterface.synReqFeatureDept(requirementFeature.getRequirementId(),
						requirementFeature.getSystemId(),requirementFeature.getDeptId(),loginfo,deptBeforeName,deptAfterName);
			}
		}
		
		if ("02".equals(requirementFeature.getRequirementFeatureStatus())) {
			//修改开发任务时间点追踪表  开发任务对应的测试任务首次变成实施中时间 字段 
			Map<String, Object> jsonMap = new HashMap<>();
			TblRequirementFeature requirementFeature3 = requirementFeatureMapper.selectByPrimaryKey(requirementFeature.getId());
			if (requirementFeature3.getSystemId()!=null && requirementFeature3.getRequirementId()!=null) {
				jsonMap.put("requirementFeatureId", requirementFeature.getId());
				jsonMap.put("systemId", requirementFeature3.getSystemId());
				jsonMap.put("requirementId",requirementFeature3.getRequirementId() );
				jsonMap.put("requirementFeatureTestingTime",new Timestamp(new Date().getTime()) );
				String jsonString = JsonUtil.toJson(jsonMap);
				testManageToDevManageInterface.updateReqFeatureTimeTrace(jsonString);
			}
		}
		
		if ("03".equals(requirementFeature.getRequirementFeatureStatus())) {
			//修改开发任务时间点追踪表  开发任务对应的测试任务首次变成实施完成时间 字段 
			Map<String, Object> jsonMap = new HashMap<>();
			TblRequirementFeature requirementFeature3 = requirementFeatureMapper.selectByPrimaryKey(requirementFeature.getId());
			if (requirementFeature3.getSystemId()!=null && requirementFeature3.getRequirementId()!=null) {
				jsonMap.put("requirementFeatureId", requirementFeature.getId());
				jsonMap.put("systemId", requirementFeature3.getSystemId());
				jsonMap.put("requirementId",requirementFeature3.getRequirementId() );
				jsonMap.put("requirementFeatureTestCompleteTime",new Timestamp(new Date().getTime()) );
				String jsonString = JsonUtil.toJson(jsonMap);
				testManageToDevManageInterface.updateReqFeatureTimeTrace(jsonString);
			}
		}
		
		if("00".equals(requirementFeature.getRequirementFeatureStatus())) {
			//如果测试任务状态是取消 下属工作任务状态也变成取消
			testTaskMapper.updateStatus(requirementFeature.getId());
		}

		if(isSyn(requirementFeature.getRequirementFeatureStatus())){
			TblRequirementFeature requirementFeature3= requirementFeatureMapper
					.selectByPrimaryKey(requirementFeature.getId());
			if (requirementFeature3.getCreateType().equals(2)) {
				Map<String, Object> dataResult = pushData(requirementFeature3);

				// 使用databus
				String taskId = "";
				if(requirementFeature3.getFeatureCode()!=null) {
					taskId = requirementFeature3.getFeatureCode();
				}

				// 使用databus
				DataBusUtil.send(databusccName,taskId,JsonUtil.toJson(dataResult));

			}
		}

		// 把该开发任务下的所有工作任务的投产窗口字段都修改
		testTaskMapper.updateCommssioningWindowId(requirementFeature);
		//把缺陷的投产窗口也修改
		List<TblTestTask> tblTestTasks = testTaskMapper.findByReqFeatureId(requirementFeature.getId());
		String testTaskIds = "";
		for (TblTestTask tblTestTask : tblTestTasks) {
			testTaskIds += tblTestTask.getId()+",";
		}
		if (StringUtils.isNotBlank(testTaskIds)) {
			defectInfoMapper.updateCommssioningWindowId(testTaskIds,requirementFeature.getCommissioningWindowId());
			if(requirementFeature.getRequirementCode() != null && !requirementFeature.getRequirementCode().equals(requirementFeature2.getRequirementCode())){//修改缺陷对应的requirementCode aviyy
				defectInfoMapper.updateRequirementCode(testTaskIds,requirementFeature.getRequirementCode());
			}
		}


		List<TblRequirementFeatureAttachement> allAtts = requirementFeatureAttachementMapper.findAttByRFId(requirementFeature.getId());
		
		getInsertAtts(requirementFeature.getId(), files, request, allAtts);
		
		TblRequirementFeatureLog log = new TblRequirementFeatureLog();
		log.setRequirementFeatureId(requirementFeature.getId());
		log.setLogType("修改测试任务");
		Map<String, String> map = new HashMap<>();
		map.put("featureName", "featureName");
		map.put("featureOverview", "featureOverview");
		map.put("systemId", "systemId");
		map.put("requirementFeatureSource", "requirementFeatureSource");
		map.put("requirementId", "requirementId");
		//map.put("questionNumber", "questionNumber");
		map.put("manageUserId", "manageUserId");
		map.put("executeUserId", "executeUserId");
		map.put("deptId", "deptId");
		map.put("commissioningWindowId", "commissioningWindowId");
		map.put("actualSitStartDate", "actualSitStartDate");
		map.put("actualSitEndDate", "actualSitEndDate");
//		map.put("actualSitWorkload", "actualSitWorkload");
		map.put("actualPptStartDate", "actualPptStartDate");
		map.put("actualPptEndDate", "actualPptEndDate");
//		map.put("actualPptWorkload", "actualPptWorkload");
		map.put("submitTestTime", "submitTestTime");
		map.put("pptDeployTime", "pptDeployTime");
		map.put("fieldTemplate", "fieldTemplate");
		map.put("importantRequirementType","importantRequirementType");
		
	//	map.put("requirementCode", "requirementCode");
		map.put("requirementFeatureStatus", "requirementFeatureStatus");
		Map<String, String> detailMap = ReflectUtils.packageModifyContentReMap(requirementFeature, requirementFeature2,map);
		if (detailMap.containsKey(systemPropertyInfoName)) {
			String beforeName = "";
			String afterName = "";
			if (requirementFeature2.getSystemId()!=null) {
				beforeName = systemInfoMapper.getSystemName(requirementFeature2.getSystemId());
			}
			if(requirementFeature.getSystemId()!=null) {
				afterName = systemInfoMapper.getSystemName(requirementFeature.getSystemId());
			}
			String title = detailMap.get(systemPropertyInfoName).substring(0,detailMap.get(systemPropertyInfoName).indexOf("："))+"：";
			detailMap.put(systemPropertyInfoName,title+"&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;") ;
		}
		if (detailMap.containsKey(requirementPropertyInfoName)) {
			String beforeName = "";
			String afterName = "";
			if (requirementFeature2.getRequirementId()!=null) {
				beforeName = requirementInfoMapper.getReqCode(requirementFeature2.getRequirementId());
			}
			if(requirementFeature.getRequirementId()!=null) {
				afterName = requirementInfoMapper.getReqCode(requirementFeature.getRequirementId());
			}
			String title = detailMap.get(requirementPropertyInfoName).substring(0,detailMap.get(requirementPropertyInfoName).indexOf("："))+"：";
			detailMap.put(requirementPropertyInfoName,title+"&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;") ;	
		}
		if (detailMap.containsKey(manageUserPropertyInfoName)) {
			String beforeName = "";
			String afterName = "";
			if (requirementFeature2.getManageUserId()!=null) {
				beforeName = requirementFeatureMapper.getUserName(requirementFeature2.getManageUserId());
			}
			if(requirementFeature.getManageUserId()!=null) {
				afterName = requirementFeatureMapper.getUserName(requirementFeature.getManageUserId());
			}
			String title = detailMap.get(manageUserPropertyInfoName).substring(0,detailMap.get(manageUserPropertyInfoName).indexOf("："))+"：";
			detailMap.put(manageUserPropertyInfoName,title+"&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;") ;
		}
		if (detailMap.containsKey(executeUserPropertyInfoName)) {
			String beforeName = "";
			String afterName = "";
			if (requirementFeature2.getExecuteUserId()!=null) {
				beforeName = requirementFeatureMapper.getUserName(requirementFeature2.getExecuteUserId());
			}
			if(requirementFeature.getExecuteUserId()!=null) {
				afterName = requirementFeatureMapper.getUserName(requirementFeature.getExecuteUserId());
			}
			String title = detailMap.get(executeUserPropertyInfoName).substring(0,detailMap.get(executeUserPropertyInfoName).indexOf("："))+"：";
			detailMap.put(executeUserPropertyInfoName,title+"&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;") ;
		}
		if (detailMap.containsKey(deptPropertyInfoName)) {
			String beforeName = "";
			String afterName = "";
			if (requirementFeature2.getDeptId()!=null) {
				beforeName = requirementFeatureMapper.getDeptName(requirementFeature2.getDeptId());
			}
			if(requirementFeature.getDeptId()!=null) {
				afterName = requirementFeatureMapper.getDeptName(requirementFeature.getDeptId());
			}
			String title = detailMap.get(deptPropertyInfoName).substring(0,detailMap.get(deptPropertyInfoName).indexOf("："))+"：";
			detailMap.put(deptPropertyInfoName,title+"&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;") ;
		}
		if(detailMap.containsKey(commissioningWindowPropertyInfoName)) {
			
			String beforeName = "";
			String afterName = "";
			if (requirementFeature2.getCommissioningWindowId()!=null) {
				beforeName = commissioningWindowMapper.getWindowName(requirementFeature2.getCommissioningWindowId());
			}
			if(requirementFeature.getCommissioningWindowId()!=null) {
				afterName = commissioningWindowMapper.getWindowName(requirementFeature.getCommissioningWindowId());
			}
			String title = detailMap.get(commissioningWindowPropertyInfoName).substring(0,detailMap.get(commissioningWindowPropertyInfoName).indexOf("："))+"：";
			detailMap.put(commissioningWindowPropertyInfoName,title+"&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;") ;
		}
		if(detailMap.containsKey(sourcePropertyInfoName)) {
			
			String beforeName = "";
			String afterName = "";
			if (requirementFeature2.getRequirementFeatureSource()!=null) {
				String string = redisUtils.get("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_SOURCE_2").toString();
				JSONObject jsonObject = JSON.parseObject(string);
				beforeName = jsonObject.get(requirementFeature2.getRequirementFeatureSource().toString()).toString();
			}
			if(requirementFeature.getRequirementFeatureSource()!=null) {
				String string = redisUtils.get("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_SOURCE_2").toString();
				JSONObject jsonObject = JSON.parseObject(string);
				afterName = jsonObject.get(requirementFeature.getRequirementFeatureSource().toString()).toString();
			}
			String title = detailMap.get(sourcePropertyInfoName).substring(0,detailMap.get(sourcePropertyInfoName).indexOf("："))+"：";
			detailMap.put(sourcePropertyInfoName,title+"&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;") ;
		}
		/*if(detailMap.containsKey(sourcePropertyInfoName)) {
			String beforeName = "";
			String afterName = "";
			List<TblDataDic> dataDics = getDataFromRedis("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_SOURCE");
			for (TblDataDic tblDataDic : dataDics) {
				if (tblDataDic.getValueCode().equals(requirementFeature2.getRequirementFeatureSource().toString())) {
					beforeName = tblDataDic.getValueName();
				}
				if (tblDataDic.getValueCode().equals(requirementFeature.getRequirementFeatureSource().toString())) {
					afterName = tblDataDic.getValueName();
				}
			}
			String title = detailMap.get(sourcePropertyInfoName).substring(0,detailMap.get(sourcePropertyInfoName).indexOf("："))+"：";
			detailMap.put(sourcePropertyInfoName,title+beforeName+"——>"+afterName) ;
		}*/
		if(detailMap.containsKey(reqFeatureStatusInfoName)) {
			String beforeName = "";
			String afterName = "";
			List<TblDataDic> dataDics = getDataFromRedis("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS_2");
			for (TblDataDic tblDataDic : dataDics) {
				if (requirementFeature2.getRequirementFeatureStatus()!=null) {
					if (tblDataDic.getValueCode().equals(requirementFeature2.getRequirementFeatureStatus().toString())) {
						beforeName = tblDataDic.getValueName();
					}
				}
				if (tblDataDic.getValueCode().equals(requirementFeature.getRequirementFeatureStatus().toString())) {
					afterName = tblDataDic.getValueName();
				}
			}
			String title = detailMap.get(reqFeatureStatusInfoName).substring(0,detailMap.get(reqFeatureStatusInfoName).indexOf("："))+"：";
			detailMap.put(reqFeatureStatusInfoName,title+"&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;") ;
		}
		if (detailMap.containsKey("重点需求类型")) {
			String beforeName = "";
			String afterName = "";
			if (requirementFeature2.getImportantRequirementType()!=null) {
				if(requirementFeature2.getImportantRequirementType().equals("1")){
					beforeName="是";
				}else{
					beforeName="否";
				}
			}
			if(requirementFeature.getImportantRequirementType()!=null) {
				if(requirementFeature.getImportantRequirementType().equals("1")){
					afterName="是";
				}else{
					afterName="否";
				}
			}
			String title = detailMap.get("重点需求类型").substring(0,detailMap.get("重点需求类型").indexOf("："))+"：";
			detailMap.put("重点需求类型",title+"&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;") ;
		}
		String detail = detailMap.toString().replace("=", "").replace(",", "；").replace("{", "").replace("}", "");
		log.setLogDetail(detail);
		insertLog(log, request);
		
		//日志附件
		getInsertLogAtts(files, request, allAtts, log);

	}

	@Override
	public List<TblCommissioningWindow> getWindows() {
		return commissioningWindowMapper.getAll();
	}

	@Override
	public List<Map<String, Object>> getSplitUser(Long systemId) {
		return requirementFeatureMapper.getSplitUser(systemId);
	}

	@Override
	public List<TblRequirementFeature> findByName(TblRequirementFeature requirementFeature) {
		return requirementFeatureMapper.findByName(requirementFeature);
	}

	@Override
	public List<Map<String, Object>> findBrother(Long requirementId, Long id) {
		return requirementFeatureMapper.findBrother(requirementId, id);
	}

	@Override
	public String findMaxCode(int length) {
		return requirementFeatureMapper.findMaxCode(length);
	}

	@Override
	public List<TblRequirementFeatureAttachement> findAtt(Long id) {
		return requirementFeatureAttachementMapper.findAttByRFId(id);
	}

	@Override
	public int getCountRequirement(TblRequirementInfo record) {
		return requirementInfoMapper.getCountRequirement(record);
	}

	@Override
	public List<Map<String, Object>> getAllRequirement(TblRequirementInfo requirement, Integer pageNumber,
			Integer pageSize) {
		Map<String, Object> map = new HashMap<>();
		int start = (pageNumber - 1) * pageSize;
		map.put("start", start);
		map.put("pageSize", pageSize);
		map.put("requirement", requirement);
		return requirementInfoMapper.getAllReq2(map);
	}

	@Override
	public List<Map<String, Object>> getAllSystemInfo(TblSystemInfo systemInfo,Long uid,Integer pageNumber, Integer pageSize) {
		Map<String, Object> map = new HashMap<>();
		int start = (pageNumber - 1) * pageSize;
		map.put("start", start);
		map.put("pageSize", pageSize);
		map.put("uid",uid);
		map.put("systemInfo", systemInfo);
		return systemInfoMapper.getAllSystemInfo(map);
	}

	@Override
	public List<Map<String, Object>> getAllSystemInfo2(TblSystemInfo systemInfo,Integer pageNumber, Integer pageSize) {
		Map<String, Object> map = new HashMap<>();
		int start = (pageNumber - 1) * pageSize;
		map.put("start", start);
		map.put("pageSize", pageSize);
		map.put("systemInfo", systemInfo);
		return systemInfoMapper.getAllSystemInfo2(map);
	}
	@Override
	public List<TblSystemInfo> findAll() {
		return systemInfoMapper.getAll();
	}

	@Override
	public List<TblRequirementInfo> getAllReq() {
		return requirementInfoMapper.getAllReq();
	}

	@Override
	@Transactional(readOnly = false)
	public void updateTransfer(TblRequirementFeature requirementFeature,String transferRemark, HttpServletRequest request) {
		TblRequirementFeature requirementFeature2 = requirementFeatureMapper
				.selectByPrimaryKey(requirementFeature.getId());
		requirementFeatureMapper.updateTransfer(requirementFeature);
		TblRequirementFeatureLog log = new TblRequirementFeatureLog();
		log.setRequirementFeatureId(requirementFeature.getId());
		log.setLogType("转派测试任务");
		Map<String, String> map = new HashMap<>();
		map.put("executeUserId", "executeUserId");
		String detail = ReflectUtils.packageModifyContent(requirementFeature, requirementFeature2, map);
		if (!StringUtils.isBlank(detail)) {
			String beforeName = "";
			String afterName = "";
			if (requirementFeature2.getExecuteUserId()!=null) {
				beforeName = requirementFeatureMapper.getUserName(requirementFeature2.getExecuteUserId());
			}
			if(requirementFeature.getExecuteUserId()!=null) {
				afterName = requirementFeatureMapper.getUserName(requirementFeature.getExecuteUserId());
			}
			//执行人：&nbsp;&nbsp;“<b>14</b>”&nbsp;&nbsp;修改为&nbsp;&nbsp;“<b>4</b>”&nbsp;&nbsp;；
			String title = detail.substring(0,detail.indexOf("：")+1);
			detail = title+"&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;"+"；";
			
		}
		String remark = "";
		if(!StringUtils.isBlank(transferRemark)) {
			remark = "备注内容： "+transferRemark;
		}
		log.setLogDetail(detail + remark);
		insertLog(log, request);
	}

	@Override
	public List<TblRequirementFeature> findSynDevTask(TblRequirementFeature requirementFeature) {
		return requirementFeatureMapper.findSynDevTask(requirementFeature);
	}

	@Override
	@Transactional(readOnly = false)
	public void mergeDevTask(TblRequirementFeature requirementFeature, Long synId, HttpServletRequest request) {
		TblRequirementFeature requirementFeature2 = requirementFeatureMapper
				.selectByPrimaryKey(requirementFeature.getId());

		requirementFeatureMapper.updateTaskId(requirementFeature);
		// 把原来的同步任务置为status 2
		requirementFeatureMapper.updateSynStatus(synId);
		//查询出同步任务下的工作任务移到要合并的自建任务下
		List<TblTestTask> testTasks = testTaskMapper.findByReqFeatureId(synId);
		Map<String, Object> map2 = new HashMap<>();
		map2.put("testTasks",testTasks);
		map2.put("reqFeatureId", requirementFeature.getId());
		testTaskMapper.updateReqFeatureId(map2);
		
		TblRequirementFeatureLog log = new TblRequirementFeatureLog();
		log.setRequirementFeatureId(requirementFeature.getId());
		log.setLogType("合并开发任务");
		Map<String, String> map = new HashMap<>();
		map.put("taskId", "taskId");
		String detail = ReflectUtils.packageModifyContent(requirementFeature, requirementFeature2,map);
		log.setLogDetail(detail);
		insertLog(log, request);
	}

	@Override
	public TblRequirementFeatureAttachement getReqFeatureAttByUrl(String filePath) {
		return requirementFeatureAttachementMapper.getReqFeatureAttByUrl(filePath);
	}

	@Override
	@Transactional(readOnly = false)
	public void addRemark(TblRequirementFeatureRemark remark, List<TblRequirementFeatureRemarkAttachement> files,
			HttpServletRequest request) {
		requirementFeatureRemarkMapper.addRemark(remark);
		Long remarkId = remark.getId();
		if (files != null && files.size() > 0) {
			for (TblRequirementFeatureRemarkAttachement tblRequirementFeatureRemarkAttachement : files) {
				tblRequirementFeatureRemarkAttachement.setRequirementFeatureRemarkId(remarkId);
				tblRequirementFeatureRemarkAttachement.setCreateBy(CommonUtil.getCurrentUserId(request));
				tblRequirementFeatureRemarkAttachement.setCreateDate(new Timestamp(new Date().getTime()));
				requirementFeatureRemarkAttachementMapper.insertAtt(tblRequirementFeatureRemarkAttachement);
			}
		}
	}

	@Override
	public List<TblRequirementFeatureLog> findLog(Long id) {
		List<TblRequirementFeatureLog> logs = requirementFeatureLogMapper.findByReqFeatureId(id);
		for (TblRequirementFeatureLog tblRequirementFeatureLog : logs) {
			List<TblRequirementFeatureLogAttachement> logAttachements = requirementFeatureLogAttachementMapper
					.findByLogId(tblRequirementFeatureLog.getId());
			tblRequirementFeatureLog.setLogAttachements(logAttachements);
		}
		return logs;
	}

	@Override
	public List<TblRequirementFeatureRemark> findRemark(Long id) {
		List<TblRequirementFeatureRemark> remarks = requirementFeatureRemarkMapper.findRemarkByReqFeatureId(id);
		for (TblRequirementFeatureRemark tblRequirementFeatureRemark : remarks) {
			List<TblRequirementFeatureRemarkAttachement> remarkAtts = requirementFeatureRemarkAttachementMapper
					.findByRemarkId(tblRequirementFeatureRemark.getId());
			tblRequirementFeatureRemark.setRemarkAttachements(remarkAtts);
		}
		return remarks;
	}

	@Override
	public int findDefectNum(Long id, int status) {
		return requirementFeatureMapper.findDefectNum(id,status);
	}
	
	@Transactional(readOnly = false)
	public void insertLog(TblRequirementFeatureLog log, HttpServletRequest request) {
		log.setCreateBy(CommonUtil.getCurrentUserId(request));
		log.setCreateDate(new Timestamp(new Date().getTime()));
		log.setUserId(CommonUtil.getCurrentUserId(request));
		log.setUserAccount(CommonUtil.getCurrentUserAccount(request));
		log.setUserName(CommonUtil.getCurrentUserName(request));
		requirementFeatureLogMapper.insert(log);
	}
	
	@Transactional(readOnly = false)
	public void insertLogAtt(TblRequirementFeatureAttachement tblRequirementFeatureAttachement,TblRequirementFeatureLogAttachement logAttachement,Long logId,HttpServletRequest request) {
		logAttachement.setCreateBy(CommonUtil.getCurrentUserId(request));
		logAttachement.setCreateDate(new Timestamp(new Date().getTime()));
		logAttachement.setFileNameNew(tblRequirementFeatureAttachement.getFileNameNew());
		logAttachement.setFileNameOld(tblRequirementFeatureAttachement.getFileNameOld());
		logAttachement.setFilePath(tblRequirementFeatureAttachement.getFilePath());
		logAttachement.setFileS3Bucket(tblRequirementFeatureAttachement.getFileS3Bucket());
		logAttachement.setFileS3Key(tblRequirementFeatureAttachement.getFileS3Key());
		logAttachement.setFileType(tblRequirementFeatureAttachement.getFileType());
		logAttachement.setRequirementFeatureLogId(logId);
		requirementFeatureLogAttachementMapper.insert(logAttachement);
	}
	/**
	 * 新增删除的附件
	 * @param 
	 * */
	@Transactional(readOnly = false)
	public void getInsertAtts(Long reqFeatureId,List<TblRequirementFeatureAttachement> files, HttpServletRequest request,
			List<TblRequirementFeatureAttachement> allAtts) {
		List<Long> allids = new ArrayList<>();
		if (allAtts!=null&&allAtts.size()>0) {
			for (TblRequirementFeatureAttachement tblRequirementFeatureAttachement : allAtts) {
				allids.add(tblRequirementFeatureAttachement.getId());
			}
		}
		List<Long> ids = new ArrayList<>();
		if (files!=null && files.size()>0) {
			for (TblRequirementFeatureAttachement tblRequirementFeatureAttachement : files) {
				if (tblRequirementFeatureAttachement.getId()!=null) {
					ids.add(tblRequirementFeatureAttachement.getId());
				}
			}
		}
		List<Long> diffIds = (List<Long>) CollectionUtil.getDiffent(allids, ids);
		//删除的附件
		if (diffIds!=null && diffIds.size()>0) {
			requirementFeatureAttachementMapper.deleteByIds(diffIds);
			
		}
		//新增的附件
		if (files != null && files.size() > 0) {
			for (TblRequirementFeatureAttachement tblRequirementFeatureAttachement : files) {
				if (tblRequirementFeatureAttachement.getId()==null || "".equals(tblRequirementFeatureAttachement.getId())) {
					tblRequirementFeatureAttachement.setRequirementFeatureId(reqFeatureId);
					tblRequirementFeatureAttachement.setCreateBy(CommonUtil.getCurrentUserId(request));
					tblRequirementFeatureAttachement.setCreateDate(new Timestamp(new Date().getTime()));
					requirementFeatureAttachementMapper.insertAtt(tblRequirementFeatureAttachement);
				}
			}
		}
	}
	
	/**
	 * 获取日志附件
	 * @param
	 * */
	@Transactional(readOnly = false)
	public void getInsertLogAtts(List<TblRequirementFeatureAttachement> files, HttpServletRequest request,
			List<TblRequirementFeatureAttachement> allAtts, TblRequirementFeatureLog log) {
		List<Long> allids = new ArrayList<>();
		if (allAtts!=null&&allAtts.size()>0) {
			for (TblRequirementFeatureAttachement tblRequirementFeatureAttachement : allAtts) {
				allids.add(tblRequirementFeatureAttachement.getId());
			}
		}
		List<Long> ids = new ArrayList<>();
		if (files!=null && files.size()>0) {
			for (TblRequirementFeatureAttachement tblRequirementFeatureAttachement : files) {
				if (tblRequirementFeatureAttachement.getId()!=null) {
					ids.add(tblRequirementFeatureAttachement.getId());
				}
			}
		}
		List<Long> diffIds = (List<Long>) CollectionUtil.getDiffent(allids, ids);
		//日志删除的附件
		if (diffIds!=null && diffIds.size()>0) {
			for (Long id : diffIds) {
				TblRequirementFeatureAttachement attachement = requirementFeatureAttachementMapper.selectByPrimaryKey(id);
				TblRequirementFeatureLogAttachement logAttachement = new TblRequirementFeatureLogAttachement();
				logAttachement.setStatus(2);
				insertLogAtt(attachement,logAttachement,log.getId(),request);
			}
		}
		//日志新增的附件
		if (files != null && files.size() > 0) {
			for (TblRequirementFeatureAttachement tblRequirementFeatureAttachement : files) {
				if (tblRequirementFeatureAttachement.getId()==null || "".equals(tblRequirementFeatureAttachement.getId())) {
					TblRequirementFeatureLogAttachement logAttachement = new TblRequirementFeatureLogAttachement();
					logAttachement.setStatus(1);
					insertLogAtt(tblRequirementFeatureAttachement,logAttachement,log.getId(),request);
				}
			}
		}
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
	
	/**
	 * 根据测试集信息查询测试任务
	 */
	@Override
	public List<Map<String, Object>> selectTaskByTestSetCon(String nameOrNumber, String createBy, String testTaskId,Long taskId) {
		List<Long> userIds = JSON.parseArray(createBy, Long.class);
		List<Long> testTaskIds = JSON.parseArray(testTaskId,Long.class);
		return requirementFeatureMapper.selectTaskByTestSetCon(nameOrNumber, userIds, testTaskIds,taskId);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void changeCancelStatus(Long requirementId) {
		requirementFeatureMapper.changeCancelStatus(requirementId);
		//把测试任务下属工作任务置为取消状态  
		List<TblRequirementFeature> requirementFeatures = requirementFeatureMapper.findByrequirementId(requirementId);
		for (TblRequirementFeature tblRequirementFeature : requirementFeatures) {
			testTaskMapper.updateStatus(tblRequirementFeature.getId());
		}	
	}

	@Override
	@Transactional(readOnly = false)
	public void synReqFeatureDeployStatus(Long requirementId, Long systemId, String deployStatus,String loginfo) {
		//找到需要修改状态的任务 同一需求 同一系统下 日志需要
		List<TblRequirementFeature> requirementFeatures = requirementFeatureMapper.selectBySystemIdAndReqId1(systemId,requirementId);
		updateDeployStatus(requirementFeatures,deployStatus,loginfo);
	}

	@Override
	@Transactional(readOnly = false)
	public void synReqFeatureDeployStatus1(String questionNumber, String deployStatus,String loginfo) {
		//找到需要修改状态的任务生产问题单号
		List<TblRequirementFeature> requirementFeatures = requirementFeatureMapper.getFeatureByQuestionNumber(questionNumber);
		updateDeployStatus(requirementFeatures,deployStatus,loginfo);
	}

	private void updateDeployStatus(List<TblRequirementFeature> requirementFeatures,String deployStatus,String loginfo){
		List<TblRequirementFeatureDeployStatus> deployStatusList = JSONObject.
				parseArray(deployStatus,TblRequirementFeatureDeployStatus.class);
		for (TblRequirementFeature feature : requirementFeatures) {
			List<TblRequirementFeatureDeployStatus> oldDeployStatusList =
					deployStatusMapper.findByReqFeatureId(feature.getId());

			deployStatusMapper.deleteByFeatureId(feature.getId());
			for (TblRequirementFeatureDeployStatus deployStatus1 : deployStatusList){
				TblRequirementFeatureDeployStatus featureDeployStatus1 =
						new TblRequirementFeatureDeployStatus();
				featureDeployStatus1.setRequirementFeatureId(feature.getId());
				featureDeployStatus1.setDeployStatu(deployStatus1.getDeployStatu());
				featureDeployStatus1.setDeployTime(deployStatus1.getDeployTime());
				featureDeployStatus1.setStatus(1);
				featureDeployStatus1.setCreateBy(deployStatus1.getCreateBy());
				featureDeployStatus1.setCreateDate(deployStatus1.getCreateDate());
				featureDeployStatus1.setLastUpdateBy(deployStatus1.getLastUpdateBy());
				featureDeployStatus1.setLastUpdateDate(deployStatus1.getLastUpdateDate());
				deployStatusMapper.insertFeatureDeployStatus(featureDeployStatus1);
			}
			insertFeatureLog1(feature.getId(),oldDeployStatusList,loginfo);
		}
	}

	private String getDeployName(Integer deployStatu){
		String deployName = "";
		VelocityDataDict dict= new VelocityDataDict();
		Map<String, String> result = dict.getDictMap("TBL_REQUIREMENT_FEATURE_DEPLOY_STATUS");
		for(Map.Entry<String, String> entry : result.entrySet()){
			if(deployStatu == Integer.valueOf(entry.getKey())){
				deployName = entry.getValue();
			}
		}
		return deployName;
	}

	@Override
	@Transactional(readOnly = false)
	public void updateDeployStatus(Long reqFeatureId, String deployStatus,HttpServletRequest request) {
		String [] strAll = deployStatus.split(",");
		List<TblRequirementFeatureDeployStatus> deployStatusList = deployStatusMapper.findByReqFeatureId(reqFeatureId);
		List<Integer> delDeployStatusList = delDeployStatus(deployStatusList,strAll);
		for(Integer delDeployStatus : delDeployStatusList){
			TblRequirementFeatureDeployStatus delDeployStatus1 =
					deployStatusMapper.findByReqFeatureIdDeployStatu(reqFeatureId,delDeployStatus);
			deployStatusMapper.deleteById(delDeployStatus1.getId());
		}
		for (String deploy : strAll){
			TblRequirementFeatureDeployStatus featureDeployStatus
					= deployStatusMapper.findByReqFeatureIdDeployStatu(reqFeatureId,Integer.valueOf(deploy));
			TblRequirementFeatureDeployStatus featureDeployStatus1= new TblRequirementFeatureDeployStatus();
			featureDeployStatus1.setRequirementFeatureId(reqFeatureId);
			featureDeployStatus1.setDeployStatu(Integer.valueOf(deploy));
			featureDeployStatus1.setStatus(1);
			featureDeployStatus1.setCreateBy(CommonUtil.getCurrentUserId(request));
			featureDeployStatus1.setCreateDate(new Timestamp(new Date().getTime()));
			featureDeployStatus1.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
			featureDeployStatus1.setLastUpdateDate(new Timestamp(new Date().getTime()));
			CommonUtil.setBaseValue(featureDeployStatus1,request);
			if(featureDeployStatus==null){
				featureDeployStatus1.setDeployTime(new Timestamp(new Date().getTime()));
				deployStatusMapper.insertFeatureDeployStatus(featureDeployStatus1);
			}
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void insertFeatureLog(Long reqFeatureId, List<TblRequirementFeatureDeployStatus>
			oldDeployStatus,HttpServletRequest request){
		// 插入日志
		String beforeName = "";
		String afterName = "";
		List<TblRequirementFeatureDeployStatus> newDeployStatus = deployStatusMapper.findByReqFeatureId(reqFeatureId);

		if(oldDeployStatus!=null && oldDeployStatus.size()>0){
			for (TblRequirementFeatureDeployStatus deployStatus : oldDeployStatus){
				beforeName = beforeName + getDeployName(deployStatus.getDeployStatu())+ " | " +
						getTime(deployStatus.getDeployTime()) + "，";
			}
		}
		if(newDeployStatus!=null && newDeployStatus.size()>0){
			for (TblRequirementFeatureDeployStatus deployStatus : newDeployStatus){
				afterName = afterName + getDeployName(deployStatus.getDeployStatu())+ " | " +
						getTime(deployStatus.getDeployTime()) + "，";
			}
		}

		TblRequirementFeatureLog log = new TblRequirementFeatureLog();
		log.setRequirementFeatureId(reqFeatureId);
		log.setLogType("修改测试任务");
		log.setLogDetail("部署状态：" + "&nbsp;&nbsp;“&nbsp;<b>" + beforeName + "</b>&nbsp;”&nbsp;&nbsp;" + "修改为"
				+ "&nbsp;&nbsp;“&nbsp;<b>" + afterName + "</b>&nbsp;”&nbsp;&nbsp;");
		insertLog(log, request);
	}

	private String getTime(Timestamp ts){
		String tsStr = "";
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		try {
			if(ts!=null){
				tsStr = sdf.format(ts);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tsStr;
	}

	@Override
	@Transactional(readOnly = false)
	public void insertFeatureLog1(Long reqFeatureId, List<TblRequirementFeatureDeployStatus>
			oldDeployStatus,String loginfo){
		// 插入日志
		String beforeName = "";
		String afterName = "";
		List<TblRequirementFeatureDeployStatus> newDeployStatus = deployStatusMapper.findByReqFeatureId(reqFeatureId);

		if(oldDeployStatus!=null && oldDeployStatus.size()>0){
			for (TblRequirementFeatureDeployStatus deployStatus : oldDeployStatus){
				beforeName = beforeName + getDeployName(deployStatus.getDeployStatu())+ " | " +
						getTime(deployStatus.getDeployTime()) + "，";
			}
		}
		if(newDeployStatus!=null && newDeployStatus.size()>0){
			for (TblRequirementFeatureDeployStatus deployStatus : newDeployStatus){
				afterName = afterName + getDeployName(deployStatus.getDeployStatu())+ " | " +
						getTime(deployStatus.getDeployTime()) + "，";
			}
		}

		TblRequirementFeatureLog log = JsonUtil.fromJson(loginfo, TblRequirementFeatureLog.class);
		log.setRequirementFeatureId(reqFeatureId);
		log.setCreateBy(log.getUserId());
		log.setCreateDate(new Timestamp(new Date().getTime()));
		log.setUserId(log.getUserId());
		log.setLogType("修改开发任务");
		log.setLogDetail("部署状态：" + "&nbsp;&nbsp;“&nbsp;<b>" + beforeName + "</b>&nbsp;”&nbsp;&nbsp;" + "修改为"
				+ "&nbsp;&nbsp;“&nbsp;<b>" + afterName + "</b>&nbsp;”&nbsp;&nbsp;");
		requirementFeatureLogMapper.insert(log);
	}

	private List<Integer> delDeployStatus(List<TblRequirementFeatureDeployStatus> deployStatusList, String [] strAll) {
		List<Integer> flag = new ArrayList<>();
		List<Integer> beforFlag = new ArrayList<>();
		for (TblRequirementFeatureDeployStatus deployStatus : deployStatusList) {
			beforFlag.add(deployStatus.getDeployStatu());
		}
		for (Integer deployStatus : beforFlag) {
			for (String str : strAll) {
				if(str .equals(deployStatus.toString())){
					flag.add(Integer.valueOf(str));
				}
			}
		}
		HashSet h1 = new HashSet(flag);
		flag.clear();
		flag.addAll(h1);
		beforFlag.removeAll(flag);
		return beforFlag;
	}

	/**
	 * 获取测试任务树
	 */
	@Override
	public List<TblRequirementFeature> getTaskTree(Long userId, String taskName,String testSetName,
			Integer requirementFeatureStatus) {
		return requirementFeatureMapper.getTaskTree(userId, taskName, testSetName, requirementFeatureStatus);
	}

	/**
	 * 根据测试工作ID 查询 测试任务信息
	 * @param testTaskId
	 * @return
	 */
	@Override
	public TblRequirementFeature getFeatureByTestTaskId(Long testTaskId) {
		return requirementFeatureMapper.getFeatureByTestTaskId(testTaskId);
	}

	@Override
	public int getAllRequirementCount(TblRequirementInfo requirement) {
		return requirementInfoMapper.getAllRequirementCount(requirement);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void cancelStatusReqFeature(Long reqFeatureId) {
		//把该开发任务下属工作任务置为取消状态  
		testTaskMapper.updateStatus(reqFeatureId);
	}

	@Override
	@Transactional(readOnly = false)
	public void synReqFeaturewindow(Long requirementId, Long systemId, Long commissioningWindowId, String loginfo, String beforeName, String afterName) {
		if(commissioningWindowId==0) {
			commissioningWindowId=null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("requirementId", requirementId);
		map.put("systemId", systemId);
		map.put("commissioningWindowId", commissioningWindowId);
		requirementFeatureMapper.synReqFeaturewindow(map);

		List<TblRequirementFeature> requirementFeatures = requirementFeatureMapper.selectBySystemIdAndReqId(systemId,requirementId);
		for (TblRequirementFeature tblRequirementFeature : requirementFeatures) {
			tblRequirementFeature.setCommissioningWindowId(commissioningWindowId);
			TblRequirementFeatureLog log = JsonUtil.fromJson(loginfo, TblRequirementFeatureLog.class);
			log.setRequirementFeatureId(tblRequirementFeature.getId());
			log.setLogType("修改投产窗口");
			log.setLogDetail("投产窗口：" + "&nbsp;&nbsp;“&nbsp;<b>"+beforeName +"</b>&nbsp;”&nbsp;&nbsp;"+ "修改为" + "&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;");
			requirementFeatureLogMapper.insert(log);

			// 把该开发任务下的所有工作任务的投产窗口字段都修改
			testTaskMapper.updateCommssioningWindowId(tblRequirementFeature);
			//把缺陷的投产窗口也修改
			List<TblTestTask> tblTestTasks = testTaskMapper.findByReqFeatureId(tblRequirementFeature.getId());
			String testTaskIds = "";
			for (TblTestTask tblTestTask : tblTestTasks) {
				testTaskIds += tblTestTask.getId()+",";
			}
			if (StringUtils.isNotBlank(testTaskIds)) {
				defectInfoMapper.updateCommssioningWindowId(testTaskIds,commissioningWindowId);
			}
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void synReqFeaturewindow1(String questionNumber, Long commissioningWindowId, String loginfo, String beforeName, String afterName) {
		if(commissioningWindowId==0) {
			commissioningWindowId=null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("questionNumber", questionNumber);
		map.put("commissioningWindowId", commissioningWindowId);
		requirementFeatureMapper.synReqFeaturewindow1(map);

		List<TblRequirementFeature> requirementFeatures = requirementFeatureMapper.getFeatureByQuestionNumber(questionNumber);
		for (TblRequirementFeature tblRequirementFeature : requirementFeatures) {
			tblRequirementFeature.setCommissioningWindowId(commissioningWindowId);
			TblRequirementFeatureLog log = JsonUtil.fromJson(loginfo, TblRequirementFeatureLog.class);
			log.setRequirementFeatureId(tblRequirementFeature.getId());
			log.setLogType("修改投产窗口");
			log.setLogDetail("投产窗口：" + "&nbsp;&nbsp;“&nbsp;<b>"+beforeName +"</b>&nbsp;”&nbsp;&nbsp;"+ "修改为" + "&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;");
			requirementFeatureLogMapper.insert(log);

			// 把该开发任务下的所有工作任务的投产窗口字段都修改
			testTaskMapper.updateCommssioningWindowId(tblRequirementFeature);
			//把缺陷的投产窗口也修改
			List<TblTestTask> tblTestTasks = testTaskMapper.findByReqFeatureId(tblRequirementFeature.getId());
			String testTaskIds = "";
			for (TblTestTask tblTestTask : tblTestTasks) {
				testTaskIds += tblTestTask.getId()+",";
			}
			if (StringUtils.isNotBlank(testTaskIds)) {
				defectInfoMapper.updateCommssioningWindowId(testTaskIds,commissioningWindowId);
			}
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void synReqFeatureDept(Long requirementId, Long systemId, Integer deptId, String loginfo,
			String deptBeforeName, String deptAfterName) {
		// TODO Auto-generated method stub
		List<TblRequirementFeature> requirementFeatures = requirementFeatureMapper.selectBySystemIdAndReqId(systemId,requirementId);
		for (TblRequirementFeature tblRequirementFeature : requirementFeatures) {
			TblRequirementFeatureLog log = JsonUtil.fromJson(loginfo, TblRequirementFeatureLog.class);
			log.setRequirementFeatureId(tblRequirementFeature.getId());
			log.setLogType("修改所属处室");
			log.setLogDetail("所属处室：" + "&nbsp;&nbsp;“&nbsp;<b>"+deptBeforeName +"</b>&nbsp;”&nbsp;&nbsp;"+ "修改为" + "&nbsp;&nbsp;“&nbsp;<b>"+deptAfterName+"</b>&nbsp;”&nbsp;&nbsp;");
			requirementFeatureLogMapper.insert(log);
		}
		if(deptId == 0) {
			deptId = null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("requirementId", requirementId);
		map.put("systemId", systemId);
		map.put("deptId", deptId);
		requirementFeatureMapper.synReqFeatureDept(map);
	}

	//合并
	@Override
	@Transactional(readOnly = false)
	public void mergeTestTask(Long systemId,Long requirementId,Long commissioningWindowId, HttpServletRequest request) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<>();
		map.put("systemId", systemId);
		map.put("requirementId", requirementId);
		map.put("commissioningWindowId",commissioningWindowId);
		//自建任务
		List<TblRequirementFeature> fesatureList = requirementFeatureMapper.findSelf(map);
		//同步任务
		TblRequirementFeature fesatureSyn = requirementFeatureMapper.findSyn(map);
		Double sitWorkload = requirementFeatureMapper.getWorkload(fesatureSyn.getId()); //系测工作量
		Double pptWorkload = requirementFeatureMapper.getWorkload2(fesatureSyn.getId());//版测工作量
		int i = 0;
		if(fesatureList != null && fesatureSyn != null) {
			Long [] ids = new Long[fesatureList.size()+1];
			int changeNumber = 0;
			//需求变更数
			if(fesatureSyn.getRequirementChangeNumber() !=null){
				changeNumber = fesatureSyn.getRequirementChangeNumber();
			}
			//实际系测工作量
			Double actualSitWorkload = 0.0;
			if(fesatureSyn.getActualSitWorkload() !=null){
				actualSitWorkload = fesatureSyn.getActualSitWorkload();
			}
			//实际版测工作量
			Double actualPptWorkload = 0.0;
			if(fesatureSyn.getActualPptWorkload() !=null){
				actualPptWorkload = fesatureSyn.getActualPptWorkload();
			}
			//系测测试案例数
			Integer sitTestCaseAmount = 0;
			if(fesatureSyn.getSitTestCaseAmount() !=null){
				sitTestCaseAmount = fesatureSyn.getSitTestCaseAmount();
			}
			//版测测试案例数
			Integer pptTestCaseAmount = 0;
			if(fesatureSyn.getPptTestCaseAmount() !=null){
				pptTestCaseAmount = fesatureSyn.getPptTestCaseAmount();
			}
			//系测测试缺陷数
			Integer sitDefectAmount = 0;
			if(fesatureSyn.getSitDefectAmount() !=null){
				sitDefectAmount = fesatureSyn.getSitDefectAmount();
			}
			//版测测试缺陷数
			Integer pptDefectAmount = 0;
			if(fesatureSyn.getPptDefectAmount() !=null){
				pptDefectAmount = fesatureSyn.getPptDefectAmount();
			}
			List<Date> submitDateList = new ArrayList<>();
			List<Date> pptDeployTimeList = new ArrayList<>();
			List<Date> actualSitStartDateList = new ArrayList<>();
			List<Date> actualPptStartDateList = new ArrayList<>();

			List<Date> actualSitEndDateList = new ArrayList<>();
			List<Date> actualPptEndDateList = new ArrayList<>();
			//循环遍历 自建任务的和同步的各个数据相加
			for(TblRequirementFeature fesature : fesatureList){
				ids [i] = fesature.getId();
				//系测工作量自建的和同步的相加
				sitWorkload = getWorkload(sitWorkload,requirementFeatureMapper.getWorkload(fesature.getId()));
				//版测工作量自建的和同步的相加 //TODO aviyy sitWorkload pptWorkload
				pptWorkload = getWorkload(pptWorkload,requirementFeatureMapper.getWorkload2(fesature.getId()));
				if(fesature.getRequirementChangeNumber()!=null){
					changeNumber = changeNumber + fesature.getRequirementChangeNumber();
				}
				if(fesature.getActualSitWorkload()!=null){
					actualSitWorkload = actualSitWorkload + fesature.getActualSitWorkload();
				}
				if(fesature.getActualPptWorkload()!=null){
					actualPptWorkload = actualPptWorkload + fesature.getActualPptWorkload();
				}
				if(fesature.getSitTestCaseAmount()!=null){
					sitTestCaseAmount = sitTestCaseAmount + fesature.getSitTestCaseAmount();
				}
				if(fesature.getPptTestCaseAmount()!=null){
					pptTestCaseAmount = pptTestCaseAmount + fesature.getPptTestCaseAmount();
				}
				if(fesature.getSitDefectAmount()!=null){
					sitDefectAmount = sitDefectAmount + fesature.getSitDefectAmount();
				}
				if(fesature.getPptDefectAmount()!=null){
					pptDefectAmount = pptDefectAmount + fesature.getPptDefectAmount();
				}
				if(fesature.getSubmitTestTime()!=null){
					submitDateList.add(fesature.getSubmitTestTime());
				}
				if(fesature.getPptDeployTime()!=null){
					pptDeployTimeList.add(fesature.getPptDeployTime());
				}
				if(fesature.getActualSitStartDate()!=null){
					actualSitStartDateList.add(fesature.getActualSitStartDate());
				}
				if(fesature.getActualPptStartDate()!=null){
					actualPptStartDateList.add(fesature.getActualPptStartDate());
				}
				if(fesature.getActualSitEndDate()!=null){
					actualSitEndDateList.add(fesature.getActualSitEndDate());
				}
				if(fesature.getActualPptEndDate()!=null){
					actualPptEndDateList.add(fesature.getActualPptEndDate());
				}

				//把被合并任务状态改为2(删除)
				requirementFeatureMapper.updateSynFeature(fesature.getId());
				i++;
			}
			//同步任务状态也修改为2(删除)
			requirementFeatureMapper.updateSynFeature(fesatureSyn.getId());
			//把同步任务的状态 名称等信息塞到第一个自建任务中 创建方式修改为2(同步)
			fesatureList.get(0).setFeatureCode(fesatureSyn.getFeatureCode());
			fesatureList.get(0).setFeatureName(fesatureSyn.getFeatureName());
			fesatureList.get(0).setTaskId(fesatureSyn.getTaskId());
			fesatureList.get(0).setCreateType(2);
			fesatureList.get(0).setRequirementChangeNumber(changeNumber);
			fesatureList.get(0).setActualSitWorkload(actualSitWorkload);
			fesatureList.get(0).setActualPptWorkload(actualPptWorkload);
			fesatureList.get(0).setSitTestCaseAmount(sitTestCaseAmount);
			fesatureList.get(0).setPptTestCaseAmount(pptTestCaseAmount);
			fesatureList.get(0).setSitDefectAmount(sitDefectAmount);
			fesatureList.get(0).setPptDefectAmount(pptDefectAmount);

			if(fesatureSyn.getSubmitTestTime()!=null){
				submitDateList.add(fesatureSyn.getSubmitTestTime());
			}
			if(fesatureSyn.getPptDeployTime()!=null){
				pptDeployTimeList.add(fesatureSyn.getPptDeployTime());
			}
			if(fesatureSyn.getActualSitStartDate()!=null){
				actualSitStartDateList.add(fesatureSyn.getActualSitStartDate());
			}
			if(fesatureSyn.getActualPptStartDate()!=null){
				actualPptStartDateList.add(fesatureSyn.getActualPptStartDate());
			}
			if(fesatureSyn.getActualSitEndDate()!=null){
				actualSitEndDateList.add(fesatureSyn.getActualSitEndDate());
			}
			if(fesatureSyn.getActualPptEndDate()!=null){
				actualPptEndDateList.add(fesatureSyn.getActualPptEndDate());
			}
			fesatureList.get(0).setSubmitTestTime(ListSort(submitDateList,0));
			fesatureList.get(0).setPptDeployTime(ListSort(pptDeployTimeList,0));
			fesatureList.get(0).setActualSitStartDate(ListSort(actualSitStartDateList,0));
			fesatureList.get(0).setActualPptStartDate(ListSort(actualPptStartDateList,0));
			fesatureList.get(0).setActualSitEndDate(ListSort(actualSitEndDateList,1));
			fesatureList.get(0).setActualPptEndDate(ListSort(actualPptEndDateList,1));
			fesatureList.get(0).setStatus(1);
			fesatureList.get(0).setLastUpdateBy(CommonUtil.getCurrentUserId(request));
			fesatureList.get(0).setLastUpdateDate(new Timestamp(new Date().getTime()));
			requirementFeatureMapper.margeFeature(fesatureList.get(0));

			ids[ids.length-1] = fesatureSyn.getId();
			List<TblRequirementFeatureAttachement>  attaList =
										requirementFeatureAttachementMapper.findAttByRFIds(ids);
			for(TblRequirementFeatureAttachement atta : attaList){
				atta.setRequirementFeatureId(fesatureList.get(0).getId());
				requirementFeatureAttachementMapper.updateFeatureId(atta);
			}

			List<TblTestTask>  testTaskList = testTaskMapper.getTestTaskByFeatureIds(ids);
			for(TblTestTask testTask : testTaskList){
				testTask.setRequirementFeatureId(fesatureList.get(0).getId());
				testTaskMapper.updateFeatureId(testTask);
			}
			TblRequirementFeatureLog log = new TblRequirementFeatureLog();
			log.setRequirementFeatureId(fesatureList.get(0).getId());
			log.setLogType("合并开发任务");
			Map<String, String> map1 = new HashMap<>();
			map1.put("taskId", "taskId");
			map1.put("featureCode", "featureCode");
			String detail = ReflectUtils.packageModifyContent(fesatureList.get(0), fesatureSyn,map1);
			log.setLogDetail(detail);
			insertLog(log, request);
			if(isSyn(fesatureList.get(0).getRequirementFeatureStatus())) {
				String taskId = fesatureSyn.getFeatureCode();
				Map<String, Object> mapAll = new LinkedHashMap<>();
				Map<String, Object> mapBody = new HashMap<>();
				mapBody.put("tbltaskId", taskId);
				mapBody.put("taskResult","测试完成");

				if(sitWorkload!=null&&sitWorkload>0) {
					mapBody.put("taskWorkload",sitWorkload);
				}else if(pptWorkload!=null&&pptWorkload>0){
					mapBody.put("taskWorkload",pptWorkload);
				}else {
					mapBody.put("taskWorkload",0.0);
				}
				mapAll.put("requestHead",DataBusRequestHead.getRequestHead());
				mapAll.put("requestBody",mapBody);
				// 使用databus
				DataBusUtil.send(databusccName,taskId,JsonUtil.toJson(mapAll));
			}
		}

	}

	//选择需求查询需求的部门number
	@Override
	@Transactional(readOnly = true)
	public TblDeptInfo findDeptNumber(Long id) {
		// TODO Auto-generated method stub
		return requirementFeatureMapper.findDeptNumberByRequirementId(id);
	}

	@Override
	public void detailEnvDate(List<Map<String, Object>> list,String env,Timestamp timestamp) {
		List<String> ids=new ArrayList<>();
		for(Map<String,Object> map:list){
			String  requirement=map.get("requirement").toString();
			String  systemId=map.get("systemId").toString();
			Map<String,Object> param=new HashMap<>();
			param.put("REQUIREMENT_ID",requirement);
			param.put("SYSTEM_ID",systemId);
			param.put("STATUS",1);
			List<TblRequirementFeature> requirementFeatures=requirementFeatureMapper.selectByMap(param);
			if(requirementFeatures!=null && requirementFeatures.size()>0){
				for(TblRequirementFeature requirementFeature:requirementFeatures){
					if(env.startsWith(Constants.XICE)){
						requirementFeature.setSubmitTestTime(timestamp);
					}else{
						requirementFeature.setPptDeployTime(timestamp);
					}
					requirementFeatureMapper.updateById(requirementFeature);
				}

			}

		}

	}

	private static Date ListSort(List<Date> list,int i) {
		if(list.size()>0){
			if(i==0){
				return Collections.min(list);
			}else{
				return Collections.max(list);
			}
		}else{
			return new Date();
		}
	}
	@Override
	public Map<String, Object> getWorkLoadByFeIds(String requirementIds) {
		Map<String,Object> map=new HashMap<>();
		Map<String,Object> param=new HashMap<>();
		List<String> list=Arrays.asList(requirementIds.split(","));
		param.put("requirementIds",list);

		Double estimateRemainWorkload=requirementFeatureMapper.getWorkLoadByFeIds(param);
		map.put("estimateRemainWorkload",estimateRemainWorkload);
		return map;
	}

	private static Boolean isSyn(String status){
		if((Integer.valueOf(status)>4 && Integer.valueOf(status)<9 )|| "03".equals(status)) {
			return true;
		}else{
			return false;
		}
	}

	private static Double getWorkload(Double workload,Double workload1){
		if(workload != null && workload1 != null) {
			return workload + workload1;
		}else if(workload != null && workload1 == null){
			return workload;
		}else if(workload == null && workload1 != null){
			return workload1;
		}else{
			return 0.0;
		}
	}

	@Override
	public List<TblRequirementFeature> getAllReqFeatureByCodeOrName(String codeOrName) {
		
		return requirementFeatureMapper.getAllReqFeatureByCodeOrName(codeOrName);
	}

	@Override
	public List<TblRequirementFeature> getReqFeatureByCurrentUser(Long uid) {
		
		return requirementFeatureMapper.getReqFeatureByCurrentUser(uid);
	}

	@Override
	public Map<String,Object> getProjectInfoAll(String projectName,Long uid, List<String> roleCodes) {
		Map<String, Object> map = new HashMap<>();
		List<TblProjectInfo> projectInfoList = new ArrayList<>();
		map.put("projectName", projectName);
		if (roleCodes!=null && roleCodes.contains("XITONGGUANLIYUAN")) {//当前登录用户的角色包含系统管理员  查询所有
			projectInfoList = projectInfoMapper.getProjectListBySystem(projectName);
		}else {
            map.put("uid", uid);
			projectInfoList = projectInfoMapper.getProjectListByNoSystem(map);
		}
		for(TblProjectInfo tblProjectInfo : projectInfoList){
			List<TblSystemInfo> systemInfoList = projectInfoMapper.getSystemByProjectId(tblProjectInfo.getId());
			if(systemInfoList !=null &&systemInfoList.size()==1){
				tblProjectInfo.setSystemId(systemInfoList.get(0).getId());
				tblProjectInfo.setSystemName(systemInfoList.get(0).getSystemName());
				tblProjectInfo.setSystemCode(systemInfoList.get(0).getSystemCode());
			}else if(systemInfoList !=null &&systemInfoList.size()>1){
				map.put("systemInfoList",systemInfoList);
			}
		}
		map.put("projectInfoList",projectInfoList);
		return map;
	}

	/**
	*@author liushan
	*@Description 查询测试任务数据
	*@Date 2020/4/29
	*@Param [id]
	*@return java.util.Map<java.lang.String,java.lang.Object>
	**/
	@Override
	public TblRequirementFeature getRequirementFeatureById(Long id) {
		return requirementFeatureMapper.getReqFeatureById(id);
	}

	@Override
	@DataSource(name = "itmpDataSource")
	public List<TblUserInfo> getUserByNameOrACC(Long id,String userName) {
		return tblUserInfoMapper.getUserByNameOrACC(id,userName);
	}

	@Override
	public List<TblRequirementFeature> getReqFeatureByExcuteCurrentUser(Long uid) {
		return requirementFeatureMapper.getReqFeatureByExcuteCurrentUser(uid);
	}

	@Override
	public List<TblRequirementFeature> getFeatureBySystemAndRequirement(Long systemId, Long requirementId) {
		return requirementFeatureMapper.getFeatureBySystemAndRequirement(systemId, requirementId);
	}
}