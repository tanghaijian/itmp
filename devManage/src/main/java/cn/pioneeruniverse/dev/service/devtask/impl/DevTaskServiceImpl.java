package cn.pioneeruniverse.dev.service.devtask.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

//import com.alibaba.fastjson.JSONObject;
import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.databus.DataBusRequestHead;
import cn.pioneeruniverse.common.databus.DataBusUtil;
import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.common.utils.BrowserUtil;
import cn.pioneeruniverse.common.utils.CollectionUtil;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.JsonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.common.utils.ReflectUtils;
import cn.pioneeruniverse.common.utils.S3Util;
import cn.pioneeruniverse.common.velocity.tag.VelocityDataDict;
import cn.pioneeruniverse.dev.dao.mybatis.TblCommissioningWindowMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDefectInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDevTaskAttentionMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDevTaskMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblProjectInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementFeatureAttachementMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementFeatureAttentionMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementFeatureDeployStatusMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementFeatureLogAttachementMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementFeatureLogMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementFeatureMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementFeatureRemarkAttachementMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementFeatureRemarkMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementFeatureTimeTraceMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSprintInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemScmMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemVersionMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblUserInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.projectPlan.ProjectPlanMapper;
import cn.pioneeruniverse.dev.entity.DevTaskVo;
import cn.pioneeruniverse.dev.entity.ExtendedField;
import cn.pioneeruniverse.dev.entity.TblCommissioningWindow;
import cn.pioneeruniverse.dev.entity.TblDataDic;
import cn.pioneeruniverse.dev.entity.TblDefectInfo;
import cn.pioneeruniverse.dev.entity.TblDefectRemark;
import cn.pioneeruniverse.dev.entity.TblDevTask;
import cn.pioneeruniverse.dev.entity.TblDevTaskLog;
import cn.pioneeruniverse.dev.entity.TblProjectInfo;
import cn.pioneeruniverse.dev.entity.TblProjectPlan;
import cn.pioneeruniverse.dev.entity.TblRequirementFeature;
import cn.pioneeruniverse.dev.entity.TblRequirementFeatureAttachement;
import cn.pioneeruniverse.dev.entity.TblRequirementFeatureAttention;
import cn.pioneeruniverse.dev.entity.TblRequirementFeatureDeployStatus;
import cn.pioneeruniverse.dev.entity.TblRequirementFeatureLog;
import cn.pioneeruniverse.dev.entity.TblRequirementFeatureLogAttachement;
import cn.pioneeruniverse.dev.entity.TblRequirementFeatureRemark;
import cn.pioneeruniverse.dev.entity.TblRequirementFeatureRemarkAttachement;
import cn.pioneeruniverse.dev.entity.TblRequirementFeatureTimeTrace;
import cn.pioneeruniverse.dev.entity.TblRequirementInfo;
import cn.pioneeruniverse.dev.entity.TblSprintInfo;
import cn.pioneeruniverse.dev.entity.TblSystemInfo;
import cn.pioneeruniverse.dev.entity.TblSystemVersion;
import cn.pioneeruniverse.dev.entity.TblUserInfo;
import cn.pioneeruniverse.dev.feignInterface.DevManageToSystemInterface;
import cn.pioneeruniverse.dev.feignInterface.DevManageToTestManageInterface;
import cn.pioneeruniverse.dev.service.defect.test.DefectService;
import cn.pioneeruniverse.dev.service.devtask.DevTaskService;
import cn.pioneeruniverse.dev.service.sprint.SprintInfoService;
import cn.pioneeruniverse.dev.service.worktask.WorkTaskService;


/**
 * 类说明
 *
 * @author:tingting
 * @version:2018年11月12日 上午11:18:44
 */
@Service("devTaskService")
@Transactional(readOnly = true)
public class DevTaskServiceImpl implements DevTaskService {

	@Autowired
	private TblRequirementFeatureMapper requirementFeatureMapper;
	@Autowired
	private TblRequirementInfoMapper requirementInfoMapper;
	@Autowired
	private TblDevTaskMapper devTaskMapper;// 工作任务mapper
	@Autowired
	private TblCommissioningWindowMapper commissioningWindowMapper;
	@Autowired
	private TblRequirementFeatureAttachementMapper requirementFeatureAttachementMapper;
	@Autowired
	private TblProjectInfoMapper projectInfoMapper;// 项目mapper
	@Autowired
	private TblRequirementFeatureRemarkMapper requirementFeatureRemarkMapper;
	@Autowired
	private TblRequirementFeatureRemarkAttachementMapper requirementFeatureRemarkAttachementMapper;
	@Autowired
	private TblRequirementFeatureLogMapper requirementFeatureLogMapper;
	@Autowired
	private TblRequirementFeatureLogAttachementMapper requirementFeatureLogAttachementMapper;
	@Autowired
	private TblSystemInfoMapper systemInfoMapper;
	@Autowired
	private TblDefectInfoMapper defectInfoMapper;
	@Autowired
	private TblSystemScmMapper systemScmMapper;
	@Autowired
	private TblSystemVersionMapper systemVersionMapper;
	@Autowired
	private RedisUtils redisUtils;
	@Autowired
	private DevManageToTestManageInterface devManageToTestManageInterface;
	@Autowired
	private TblRequirementFeatureTimeTraceMapper requirementFeatureTimeTraceMapper;
	@Autowired
	private TblSprintInfoMapper sprintInfoMapper;
	@Autowired
	private DefectService defectService;
	@Autowired
	private WorkTaskService workTaskService;
	@Autowired
	private TblProjectInfoMapper tblProjectInfoMapper;
	@Autowired
    private DevManageToSystemInterface devManageToSystemInterface;
	@Autowired
    private TblSprintInfoMapper tblSprintInfoMapper;
	@Autowired
	private TblUserInfoMapper tblUserInfoMapper;
	@Autowired
    private TblSystemVersionMapper tblSystemVersionMapper;
	@Autowired
    private TblCommissioningWindowMapper tblCommissioningWindowMapper;
    @Autowired
    private TblRequirementFeatureDeployStatusMapper deployStatusMapper;
    @Autowired
    private TblRequirementFeatureAttentionMapper tblRequirementFeatureAttentionMapper;
    @Autowired
    private TblDevTaskAttentionMapper tblDevTaskAttentionMapper;
    @Autowired
	private SprintInfoService sprintInfoService;
	@Autowired
	private ProjectPlanMapper projectPlanMapper;


	@Autowired
	private S3Util s3Util;

	//开发同步回调databus接口：itcd_manager_syn
	@Value("${databuscc.name}")
	private String databusccName;

	//数据字典部署状态中部署成功的code：key-value展示，比如{1：1，2：2}，由于部署状态分多个环境，每个环境的成功失败又分多个code，不同环境均有成功和失败，这里将部署状态重新按照成功、失败、拒绝、取消进行重新分类，下同
	@Value("${delpoy.task.success}")
	private String deployTaskSuccess;

	//数据字典部署状态中部署失败的code
	@Value("${delpoy.task.fail}")
	private String deployTaskFail;
    
	//数据字典部署状态部署驳回的code
	@Value("${delpoy.task.reject}")
	private String deployTaskReject;
    //数据字典部署状态部署取消的code
	@Value("${delpoy.task.cancel}")
	private String deployTaskCancel;


	public final static Logger logger = LoggerFactory.getLogger(DevTaskServiceImpl.class);
	private static final  String manageUserPropertyInfoName = "管理岗";
	private static final  String executeUserPropertyInfoName = "执行人";
	private static final  String deptPropertyInfoName = "所属处室";
	private static final  String systemPropertyInfoName = "涉及系统";
	private static final  String requirementPropertyInfoName = "关联需求";
	private static final  String commissioningWindowPropertyInfoName = "投产窗口";
	private static final  String sourcePropertyInfoName = "任务来源";
	private static final  String reqFeatureStatusInfoName = "任务状态";
	private static final  String reqFeaturePriorityInfoName = "优先级";
	private static final  String systemVersionInfoName = "系统版本";
	private static final  String sprintInfoName = "冲刺";

	@Override
	public List<TblRequirementInfo> getAllRequirement() {
		return requirementInfoMapper.getAllRequirement();
	}

	/**
	 * 
	* @Title: getAllReqFeature
	* @Description: 获取所有开发任务（未用）
	* @author author
	* @param requirementFeature
	* @param uid
	* @param page
	* @param rows
	* @return List<DevTaskVo>
	 */
	@Override
	public List<DevTaskVo> getAllReqFeature(TblRequirementFeature requirementFeature,Long uid, Integer page,
											Integer rows) {
		Map<String, Object> map = new HashMap<>();
		Integer start = (page - 1) * rows;
		map.put("start", start);
		map.put("pageSize", rows);
		map.put("uid", uid);
		map.put("reqFeature", requirementFeature);
		return requirementFeatureMapper.getAllReqFeature(map);
	}

	/**
	 * 
	* @Title: getAllReqFeatureCount
	* @Description: 获取开发任务数量
	* @author author
	* @param requirementFeature 封装的查询
	* @param uid 用户ID
	* @return int 开发任务数量
	 */
	@Override
	public int getAllReqFeatureCount(TblRequirementFeature requirementFeature, Long uid) {
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("reqFeature", requirementFeature);
		return requirementFeatureMapper.getAllReqFeatureCount(map);
	}


	/**
	 * 
	* @Title: selectAll
	* @Description: 页面查询开发任务
	* @author author
	* @param jqGridPage
	* @param devTaskVo 封装的查询条件
	* @param roleCodes 角色，用以区别系统管理员和其他
	* @return JqGridPage<DevTaskVo>
	 */
	@Override
	public JqGridPage<DevTaskVo> selectAll(JqGridPage<DevTaskVo> jqGridPage, DevTaskVo devTaskVo,List<String> roleCodes) {
		/*try {
			jqGridPage.filtersAttrToEntityField(devTaskVo);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		List<DevTaskVo> list = new ArrayList<>();
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
			}else{
				PageInfo<DevTaskVo> pageInfo = new PageInfo<DevTaskVo>(list);
				jqGridPage.processDataForResponse(pageInfo);
				return jqGridPage;
			}
		}

		PageHelper.startPage(jqGridPage.getJqGridPrmNames().getPage(), jqGridPage.getJqGridPrmNames().getRows());

		if (roleCodes!=null && roleCodes.contains("XITONGGUANLIYUAN")) {//当前登录用户有角色是系统管理员
			list = requirementFeatureMapper.getAll(devTaskVo);
		}else {
			list = requirementFeatureMapper.getAllCondition(devTaskVo);
		}
		/*for (DevTaskVo devTaskVo2 : list) {
			List<Map<String, Object>> brothers = requirementFeatureMapper.findBrotherDiffWindow(devTaskVo2);
			devTaskVo2.setBrothers(brothers);
		}*/
		PageInfo<DevTaskVo> pageInfo = new PageInfo<DevTaskVo>(list);
		jqGridPage.processDataForResponse(pageInfo);
		return jqGridPage;

	}

	/**
	 * 
	* @Title: findDeployByReqFeatureId
	* @Description: 获取开发任务的部署状态，页面状态字段显示
	* @author author
	* @param featureId 开发任务ID
	* @return String 部署状态名
	 */
    @Override
    public String findDeployByReqFeatureId(Long featureId){
        List<TblRequirementFeatureDeployStatus> deployStatusList=
                deployStatusMapper.findByReqFeatureId(featureId);
        String deployName = "";
        if(deployStatusList!=null&&deployStatusList.size()>0){
            for (TblRequirementFeatureDeployStatus deployStatus : deployStatusList){
                deployName = deployName + getDeployName(deployStatus.getDeployStatu())+ " | " +
                        getTime(deployStatus.getDeployTime()) + "，";
            }
        }
        return deployName;
    }

    /**
     * 
    * @Title: getDeployName
    * @Description: 部署状态对应的中文名
    * @author author
    * @param deployStatu
    * @return String 部署状态名
     */
    private String getDeployName(Integer deployStatu){
        String deployName = "";
        VelocityDataDict dict= new VelocityDataDict();
        Map<String, String> result = dict.getDictMap("TBL_REQUIREMENT_FEATURE_DEPLOY_STATUS");
        for(Entry<String, String> entry : result.entrySet()){
            if(deployStatu == Integer.valueOf(entry.getKey())){
                deployName = entry.getValue();
            }
        }
        return deployName;
    }
	@Override
	public Map<String, Object> getOneDevTask(Long id) {
		return requirementFeatureMapper.getOneDevTask(id);
	}

	@Override
	public List<Map<String, Object>> findByReqFeature(Long id) {
		return devTaskMapper.findByReqFeature(id);
	}

	/**
	 * 
	* @Title: addDevTask
	* @Description: 新增开发任务
	* @author author
	* @param requirementFeature 开发任务
	* @param files 附件
	* @param defectIds 缺陷ID
	* @param dftActualWorkload  实际工作量
	* @param defectRemark 缺陷描述
	* @param dftJsonString 封装的开发缺陷信息即TblDefectInfo
	* @param request
	 */
	@Override
	@Transactional(readOnly = false)
	public void addDevTask(TblRequirementFeature requirementFeature, List<TblRequirementFeatureAttachement> files,
						   String defectIds,Double dftActualWorkload,String defectRemark, String dftJsonString,HttpServletRequest request) {
		requirementFeatureMapper.insertReqFeature(requirementFeature);

		TblRequirementFeatureTimeTrace featureTimeTrace = new TblRequirementFeatureTimeTrace();
		featureTimeTrace.setRequirementFeatureId(requirementFeature.getId());
		featureTimeTrace.setRequirementFeatureCreateTime(requirementFeature.getCreateDate());
		featureTimeTrace.setStatus(1);
		featureTimeTrace.setCreateDate(new Timestamp(new Date().getTime()));
		featureTimeTrace.setLastUpdateDate(new Timestamp(new Date().getTime()));
		requirementFeatureTimeTraceMapper.insertFeatureTimeTrace(featureTimeTrace);
		//修改冲刺任务预计工作量
		if(requirementFeature.getEstimateWorkload()!=null &&  requirementFeature.getSprintId()!=null){
			Map<String, Object> param = new HashMap<>();
			param.put("oldWorkLoad", new BigDecimal(0));
			param.put("newWorkLoad", new BigDecimal(requirementFeature.getEstimateWorkload()));
			param.put("type", 2);
			param.put("devTaskId", requirementFeature.getId());
			sprintInfoService.updateSprintWorkLoad(param);
		}

		//附件
		Long reqFeatureId = requirementFeature.getId();
		if (files != null && files.size() > 0) {
			for (TblRequirementFeatureAttachement tblRequirementFeatureAttachement : files) {
				tblRequirementFeatureAttachement.setRequirementFeatureId(reqFeatureId);
				tblRequirementFeatureAttachement.setCreateBy(CommonUtil.getCurrentUserId(request));
				tblRequirementFeatureAttachement.setCreateDate(new Timestamp(new Date().getTime()));
				requirementFeatureAttachementMapper.insertAtt(tblRequirementFeatureAttachement);
			}
		}

		//任务来源生产或缺陷问题，则关联开发任务和缺陷
		if (requirementFeature.getRequirementFeatureSource()!=null &&
				(3==requirementFeature.getRequirementFeatureSource()||2==requirementFeature.getRequirementFeatureSource())) {
			Map<String, Object> map = new HashMap<>();
			map.put("reqFid",reqFeatureId);
			if(!StringUtils.isBlank(defectIds)) {
				String[] defectIdsArr =  defectIds.split(",");
				for (String defectId : defectIdsArr) {
					map.put("defectId", defectId);
					defectInfoMapper.updateDevDefect(map);
				}
			}
		}
		TblRequirementFeatureLog log = new TblRequirementFeatureLog();
		log.setRequirementFeatureId(reqFeatureId);
		log.setLogType("新增开发任务");
		insertLog(log, request);

		//开发缺陷确认并新建任务
		if(StringUtils.isNotBlank(dftJsonString)) {//调用开发缺陷的接口
			TblDefectInfo defectInfo = JsonUtil.fromJson(dftJsonString, TblDefectInfo.class);
			TblDefectRemark defectRemark2 = new TblDefectRemark();
			defectRemark2.setDefectRemark(defectRemark);
			try {
				defectService.updateDefectwithTBC(defectInfo, defectRemark2,dftActualWorkload,  request);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("updateDefectwithTBC接口出错");
			}
		}
		//排期告警    根据该任务所在需求 查询这个需求下的开发任务 以投产窗口分组
		if(requirementFeature.getRequirementId()!=null) {
			List<Map<String, Object>> list = commissioningWindowMapper.getReqFeatureGroupbyWindow(requirementFeature.getRequirementId());
			TblRequirementInfo requirementInfo = requirementInfoMapper.findRequirementById(requirementFeature.getRequirementId());
			if (!list.isEmpty() && list.size()>1) {//说明该需求下的开发任务不全都是在同一个投产窗口  需提醒
				String messContent = "需求  “"+requirementInfo.getRequirementCode()+" | "+requirementInfo.getRequirementName()+"” 下，";
				for (Map<String, Object> map1 : list) {
					messContent+= "开发任务"+map1.get("featureCode")+( map1.get("windowName")== null ? "暂未排期，" : "排期在"+(map1.get("windowName")+"投产窗口，"));
				}
				//接收人 项目管理岗、开发管理岗、
				String userIds = "";
				userIds += requirementInfoMapper.getProManageUserIds(requirementFeature.getRequirementId())+",";//获取该需求所在系统所在项目的项目管理岗
				userIds += requirementFeature.getManageUserId()+",";//开发管理岗
				if(StringUtils.isNotBlank(userIds)) {
					Map<String,Object> emWeMap = new HashMap<String, Object>();
				    emWeMap.put("messageTitle", "【IT开发测试管理系统】- 任务排期不一致");
				    emWeMap.put("messageContent",messContent+"请留意。");
				    emWeMap.put("messageReceiver",userIds );//接收人 
				    emWeMap.put("sendMethod", 3);//发送方式 3 邮件和微信
				    devManageToSystemInterface.sendMessage(JSON.toJSONString(emWeMap));
				}
			}
		}
		//给该开发任务的开发管理岗和执行人发送邮件和微信 --ztt
		if(requirementFeature.getManageUserId()!= null && requirementFeature.getExecuteUserId()!=null) {
			try {
				Map<String,Object> emWeMap = new HashMap<String, Object>();
				emWeMap.put("messageTitle", "【IT开发测试管理系统】- 收到一个新分配的开发任务");
				emWeMap.put("messageContent","您收到一个新分配的开发任务：“"+ requirementFeature.getFeatureCode()+" | "+requirementFeature.getFeatureName()+"”，请及时处理。");
				emWeMap.put("messageReceiver",requirementFeature.getManageUserId()+","+requirementFeature.getExecuteUserId()+"," );//接收人 开发管理岗和执行人 用户表主键
				emWeMap.put("sendMethod", 3);//发送方式 3 邮件和微信
				devManageToSystemInterface.sendMessage(JSON.toJSONString(emWeMap));
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("调用发邮件微信接口出错");
			}
		}

		
	}

	/**
	 * 
	* @Title: addWorkTask
	* @Description: 添加开发工作任务
	* @author author
	* @param id
	* @param devTask 开发工作任务
	* @param request
	 */
	@Override
	@Transactional(readOnly = false)
	public void addWorkTask(Long id,TblDevTask devTask,HttpServletRequest request) {
		TblRequirementFeature requirementFeature2 = requirementFeatureMapper.selectByPrimaryKey(id);
		devTask.setDevTaskStatus(1);// 新增时状态为未实施
		devTask.setCreateBy(CommonUtil.getCurrentUserId(request));
		devTask.setCreateDate(new Timestamp(new Date().getTime()));
		devTask.setRequirementFeatureId(id);
		devTaskMapper.insertWorkTask(devTask);

		//开发工作任务记录日志
		Long testId = devTask.getId();
		TblDevTaskLog tblDevTaskLog = new TblDevTaskLog();
		tblDevTaskLog.setDevTaskId(testId);
		tblDevTaskLog.setLogType("新增工作任务");
		Long logId = workTaskService.insertDevTaskLog(tblDevTaskLog, request);
		//更新开发任务时间点追踪表  首次创建工作任务时间
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("requirementFeatureId",id);
		jsonMap.put("devTaskCreateTime",new Timestamp(new Date().getTime()));
		String json = JsonUtil.toJson(jsonMap);
		updateReqFeatureTimeTrace(json);
		
		String status = "";
		if(!"02".equals(requirementFeature2.getRequirementFeatureStatus())) {
			String beforeName = CommonUtil.getDictValueName("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS", requirementFeature2.getRequirementFeatureStatus(),"");
			String afterName = CommonUtil.getDictValueName("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS", "02","");
			status = "&nbsp;&nbsp;“<b>"+beforeName+"</b>”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“<b>"+afterName+"</b>”&nbsp;&nbsp;；";
		}
		
		//开发任务记录日志
		TblRequirementFeatureLog log = new TblRequirementFeatureLog();
		log.setRequirementFeatureId(id);
		log.setLogType("拆分开发任务");
		log.setLogDetail(status+"拆分出工作任务："+devTask.getDevTaskCode()+" | "+devTask.getDevTaskName());
		insertLog(log, request);

	}

	/**
	 * 
	* @Title: handleDevTask
	* @Description: 处理开发任务
	* @author author
	* @param requirementFeature 开发任务
	* @param request
	* @param handleRemark 备注
	 */
	@Override
	@Transactional(readOnly = false)
	public void handleDevTask(TblRequirementFeature requirementFeature, HttpServletRequest request, String handleRemark) {
		TblRequirementFeature requirementFeature2 = requirementFeatureMapper
				.selectByPrimaryKey(requirementFeature.getId());
		requirementFeatureMapper.updateDevTask(requirementFeature);

		//添加开发任务追踪点
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("requirementFeatureId", requirementFeature.getId());
		jsonMap.put("requirementFeatureDevCompleteTime",new Timestamp(new Date().getTime()));
		String json = JsonUtil.toJson(jsonMap);
		updateReqFeatureTimeTrace(json);

		//开发任务日志
		TblRequirementFeatureLog log = new TblRequirementFeatureLog();
		log.setRequirementFeatureId(requirementFeature.getId());
		log.setLogType("处理开发任务");
		Map<String, String> map = new HashMap<>();
		map.put("actualStartDate", "actualStartDate");
		map.put("actualEndDate", "actualEndDate");
		map.put("actualWorkload", "actualWorkload");
		String detail = ReflectUtils.packageModifyContent(requirementFeature, requirementFeature2, map);
		
		String status = "";
		if(!"03".equals(requirementFeature2.getRequirementFeatureStatus())) {
			String beforeName = CommonUtil.getDictValueName("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS", requirementFeature2.getRequirementFeatureStatus(),"");
			String afterName = CommonUtil.getDictValueName("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS", "03","");
			status = "&nbsp;&nbsp;“<b>"+beforeName+"</b>”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“<b>"+afterName+"</b>”&nbsp;&nbsp;；";		
		}
		String remark = "";
		if(!StringUtils.isBlank(handleRemark)) {
			remark = "备注内容： "+handleRemark;
		}
		log.setLogDetail(status + detail + remark);
		insertLog(log, request);
		TblRequirementFeature requirementFeature3= requirementFeatureMapper
				.selectByPrimaryKey(requirementFeature.getId());
		
		//同步过来的开发任务处理结果要通知databus
		if (requirementFeature3.getCreateType().equals(2)) {
			Map<String, Object> dataResult = pushData(requirementFeature3);
			//使用httpClient
			/*String resultJson = HttpUtil.doPost(devtaskServiceAddr, JsonUtil.toJson(dataResult), "UTF-8");
			Map<String, Map<String, String>> result = JsonUtil.fromJson(resultJson, HashMap.class);
		    Map<String, String> responseHead = result.get("responseHead");
		    System.out.println(responseHead.get("appMessage"));
		    if (!"0".equals(responseHead.get("status"))) {
		        logger.error("开发任务调用接口返回失败，appMessage:" + responseHead.get("appMessage"));
		    }*/

			String taskId = "";
			if(requirementFeature2.getFeatureCode()!=null) {
				taskId = requirementFeature2.getFeatureCode();
			}
			// 使用databus
			DataBusUtil.send(databusccName,taskId,JsonUtil.toJson(dataResult));
		}
		//关注提醒：开发任务状态改变或者内容变更时给关注该任务的人发送邮件和微信 --ztt
		sendEmWeMessage(requirementFeature2);
	}


	/**
	 * 
	* @Title: updateDevTask
	* @Description: 更新开发任务
	* @author author
	* @param requirementFeature 开发任务
	* @param files 附件
	* @param defectIds 关联的开发缺陷
	* @param request
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateDevTask(TblRequirementFeature requirementFeature, List<TblRequirementFeatureAttachement> files,
							  String defectIds,HttpServletRequest request) {
		TblRequirementFeature requirementFeature2 = requirementFeatureMapper
				.selectByPrimaryKey(requirementFeature.getId());
		requirementFeatureMapper.updateByPrimaryKey(requirementFeature);
		Long userId = CommonUtil.getCurrentUserId(request);
		//同步来的任务
		if(requirementFeature.getCreateType()==2) {
			TblUserInfo userInfo = tblUserInfoMapper.getUserById(userId);
			//修改前的投产窗口
			String beforeName = commissioningWindowMapper.getWindowName(requirementFeature2.getCommissioningWindowId());
			//修改后的投产窗口
			String afterName = commissioningWindowMapper.getWindowName(requirementFeature.getCommissioningWindowId());
			//修改前的所属部门
			String deptBeforeName = requirementFeatureMapper.getDeptName(requirementFeature2.getDeptId());
			//修改后的所属部门
			String deptAfterName = requirementFeatureMapper.getDeptName(requirementFeature.getDeptId());
			//添加日志
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
				requirementFeature.setDeptId( 0);
			}
			if(requirementFeature2.getCommissioningWindowId() != requirementFeature.getCommissioningWindowId()) {
				//任务来源是生产问题的，通过问题编号去进行同步
				if(requirementFeature2.getRequirementFeatureSource()==2){
                    devManageToTestManageInterface.synReqFeaturewindow1(requirementFeature2.getQuestionNumber(),
                            requirementFeature.getCommissioningWindowId(), loginfo,beforeName,afterName);
				}else{
					//其他的通过需求ID和系统ID去同步
					devManageToTestManageInterface.synReqFeaturewindow(requirementFeature.getRequirementId(),
							requirementFeature.getSystemId(), requirementFeature.getCommissioningWindowId(), loginfo,beforeName,afterName);
				}

			}
			if(requirementFeature2.getDeptId() != requirementFeature.getDeptId()) {
				devManageToTestManageInterface.synReqFeatureDept(requirementFeature.getRequirementId(),
						requirementFeature.getSystemId(),requirementFeature.getDeptId(),loginfo,deptBeforeName,deptAfterName);
			}
		}


		//更新开发任务时间点追踪表 中 状态变为实施完成时间 字段
		if ("03".equals(requirementFeature.getRequirementFeatureStatus())) {
			Map<String, Object> jsonMap = new HashMap<>();
			jsonMap.put("requirementFeatureId", requirementFeature.getId());
			jsonMap.put("requirementFeatureDevCompleteTime",new Timestamp(new Date().getTime()));
			String json = JsonUtil.toJson(jsonMap);
			updateReqFeatureTimeTrace(json);
		}

		if("00".equals(requirementFeature.getRequirementFeatureStatus())) {
			//如果开发任务状态是取消 下属工作任务状态也变成取消
			cancelStatusReqFeature(requirementFeature.getId());
		}
		
		//把缺陷的投产窗口也修改
		//defectInfoMapper.updateCommssioningWindowId(requirementFeature);

		//
		Map<String, Object> map2 = new HashMap<>();
		map2.put("reqFid",requirementFeature.getId());
		if (requirementFeature.getRequirementFeatureSource()!=null) {
			//开发任务来自测试缺陷
			if(3 == requirementFeature.getRequirementFeatureSource()) {
				//把生产问题单号和关联需求清空
				//requirementFeatureMapper.updateReqNumberNull(requirementFeature.getId());
				//取消原有的开发任务和需求关联
				requirementFeatureMapper.updateReqNull(requirementFeature.getId());
				//取消原有的缺陷和开发任务的关联
				requirementFeatureMapper.updateDftReqFIdNull(requirementFeature.getId());
				//重新关联缺陷和开发任务关联
				String[] defectIdsArr =  defectIds.split(",");
				for (String defectId : defectIdsArr) {
					map2.put("defectId", defectId);
					defectInfoMapper.updateDevDefect(map2);
				}
			}else if(2 ==requirementFeature.getRequirementFeatureSource()) {//把关联需求和缺陷清空
				//requirementFeatureMapper.updateReqNumberNull(requirementFeature.getId());
				requirementFeatureMapper.updateReqNull(requirementFeature.getId());
				requirementFeatureMapper.updateDftReqFIdNull(requirementFeature.getId());
				String[] defectIdsArr =  defectIds.split(",");
				for (String defectId : defectIdsArr) {
					map2.put("defectId", defectId);
					defectInfoMapper.updateDevDefect(map2);
				}
			}else if(1 ==requirementFeature.getRequirementFeatureSource()) {//来源于需求：把关联缺陷和生产问题清空
				//requirementFeatureMapper.updateReqNumberNull(requirementFeature.getId());
				requirementFeatureMapper.updateDftReqFIdNull(requirementFeature.getId());
			}
		}

		//附件
		List<TblRequirementFeatureAttachement> allAtts = requirementFeatureAttachementMapper.findAttByRFId(requirementFeature.getId());
		if (requirementFeature2.getCreateType()!=null && "1".equals(requirementFeature2.getCreateType().toString())) {
			getInsertAtts(requirementFeature.getId(), files, request, allAtts);
		}
		//日志
		TblRequirementFeatureLog log = new TblRequirementFeatureLog();
		log.setRequirementFeatureId(requirementFeature.getId());
		log.setLogType("修改开发任务");

		//组装日志内容 map:需求记录的字段
		Map<String, String> map = new HashMap<>();
		map.put("featureName", "featureName");
		map.put("featureOverview", "featureOverview");
		map.put("systemId", "systemId");
		map.put("requirementFeatureSource", "requirementFeatureSource");
		map.put("requirementId", "requirementId");
		map.put("questionNumber", "questionNumber");
		map.put("manageUserId", "manageUserId");
		map.put("executeUserId", "executeUserId");
		map.put("deptId", "deptId");
		map.put("commissioningWindowId", "commissioningWindowId");
		map.put("planStartDate", "planStartDate");
		map.put("planEndDate", "planEndDate");
		map.put("estimateWorkload", "estimateWorkload");
		map.put("actualStartDate", "actualStartDate");
		map.put("actualEndDate", "actualEndDate");
		map.put("actualWorkload", "actualWorkload");
		map.put("requirementFeatureStatus", "requirementFeatureStatus");
		map.put("requirementFeaturePriority", "requirementFeaturePriority");
		map.put("storyPoint", "storyPoint");
		map.put("systemVersionId", "systemVersionId");
		map.put("sprintId", "sprintId");
		//requirementFeature修改后，requirementFeature2修改前
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
				beforeName = requirementInfoMapper.getReqName(requirementFeature2.getRequirementId());
			}
			if(requirementFeature.getRequirementId()!=null) {
				afterName = requirementInfoMapper.getReqName(requirementFeature.getRequirementId());
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
			// 如果开发任务的投产窗口改了 把该开发任务下的所有工作任务的投产窗口字段都修改
			devTaskMapper.updateCommssioningWindowId(requirementFeature);
			//关注提醒： 开发工作任务状态或者内容变更时给关注该任务的人发送邮件和微信 --ztt
			sendDevTaskEmWeMassage(requirementFeature2.getId());
			
			//排期告警    根据该任务所在需求 查询这个需求下的开发任务 以投产窗口分组
			if(requirementFeature.getRequirementId()!=null) {
				List<Map<String, Object>> list = commissioningWindowMapper.getReqFeatureGroupbyWindow(requirementFeature.getRequirementId());
				TblRequirementInfo requirementInfo = requirementInfoMapper.findRequirementById(requirementFeature.getRequirementId());
				if (!list.isEmpty() && list.size()>1) {//说明该需求下的开发任务不全都是在同一个投产窗口  需提醒
					String messContent = "需求  “"+requirementInfo.getRequirementCode()+" | "+requirementInfo.getRequirementName()+"” 下，";
					for (Map<String, Object> map1 : list) {
						messContent+= "开发任务"+map1.get("featureCode")+( map1.get("windowName")== null ? "暂未排期，" : "排期在"+(map1.get("windowName")+"投产窗口，"));
					}
					//接收人 项目管理岗、开发管理岗、
					String userIds = "";
					userIds += requirementInfoMapper.getProManageUserIds(requirementFeature.getRequirementId())+",";//获取该需求所在系统所在项目的项目管理岗
					userIds += requirementFeature.getManageUserId()+",";//开发管理岗
					if(StringUtils.isNotBlank(userIds)) {
						Map<String,Object> emWeMap = new HashMap<String, Object>();
					    emWeMap.put("messageTitle", "【IT开发测试管理系统】- 任务排期不一致");
					    emWeMap.put("messageContent",messContent+"请留意。");
					    emWeMap.put("messageReceiver",userIds );//接收人 
					    emWeMap.put("sendMethod", 3);//发送方式 3 邮件和微信
					    devManageToSystemInterface.sendMessage(JSON.toJSONString(emWeMap));
					}
				}
			}
			
		
		}
		if(detailMap.containsKey(sourcePropertyInfoName)) {
			String beforeName = "";
			String afterName = "";
			List<TblDataDic> dataDics = getDataFromRedis("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_SOURCE");
			for (TblDataDic tblDataDic : dataDics) {
				if (requirementFeature2.getRequirementFeatureSource()!=null) {
					if (tblDataDic.getValueCode().equals(requirementFeature2.getRequirementFeatureSource().toString())) {
						beforeName = tblDataDic.getValueName();
					}
				}
				if(requirementFeature.getRequirementFeatureSource()!=null) {
					if (tblDataDic.getValueCode().equals(requirementFeature.getRequirementFeatureSource().toString())) {
						afterName = tblDataDic.getValueName();
					}
				}
			}
			String title = detailMap.get(sourcePropertyInfoName).substring(0,detailMap.get(sourcePropertyInfoName).indexOf("："))+"：";
			detailMap.put(sourcePropertyInfoName,title+"&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;") ;
		}
		if(detailMap.containsKey(reqFeatureStatusInfoName)) {
			String beforeName = "";
			String afterName = "";
			List<TblDataDic> dataDics = getDataFromRedis("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS");
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
		if(detailMap.containsKey(reqFeaturePriorityInfoName)) {
			String beforeName = "";
			String afterName = "";
			List<TblDataDic> dataDics = getDataFromRedis("TBL_REQUIREMENT_FEATURE_PRIORITY");
			for (TblDataDic tblDataDic : dataDics) {
				if (requirementFeature2.getRequirementFeaturePriority()!=null) {
					if (tblDataDic.getValueCode().equals(requirementFeature2.getRequirementFeaturePriority().toString())) {
						beforeName = tblDataDic.getValueName();
					}
				}
				if (requirementFeature.getRequirementFeaturePriority()!=null) {
					if (tblDataDic.getValueCode().equals(requirementFeature.getRequirementFeaturePriority().toString())) {
						afterName = tblDataDic.getValueName();
					}
				}
			}
			String title = detailMap.get(reqFeaturePriorityInfoName).substring(0,detailMap.get(reqFeaturePriorityInfoName).indexOf("："))+"：";
			detailMap.put(reqFeaturePriorityInfoName,title+"&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;") ;
		}

		if(detailMap.containsKey(systemVersionInfoName)) {
			String beforeName = "";
			String afterName = "";
			if (requirementFeature2.getSystemVersionId()!=null) {
				beforeName = systemVersionMapper.getVersionName(requirementFeature2.getSystemVersionId());
			}
			if (requirementFeature.getSystemVersionId()!=null) {
				afterName = systemVersionMapper.getVersionName(requirementFeature.getSystemVersionId());
			}

			String title = detailMap.get(systemVersionInfoName).substring(0,detailMap.get(systemVersionInfoName).indexOf("："))+"：";
			detailMap.put(systemVersionInfoName,title+"&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;") ;
		}
		if(detailMap.containsKey(sprintInfoName)) {
			String beforeName = "";
			String afterName = "";

			if(requirementFeature2.getSprintId()!=null) {
				beforeName = sprintInfoMapper.selectByPrimaryKey(requirementFeature2.getSprintId()).getSprintName();
			}
			if (requirementFeature.getSprintId()!=null) {
				afterName =  sprintInfoMapper.selectByPrimaryKey(requirementFeature.getSprintId()).getSprintName();
			}
			String title = detailMap.get(sprintInfoName).substring(0,detailMap.get(sprintInfoName).indexOf("："))+"：";
			detailMap.put(sprintInfoName,title+"&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;") ;
			//如果开发任务的冲刺修改了 把该开发任务下的所有工作任务的冲刺都修改
			devTaskMapper.updateSprintId(requirementFeature);
			//关注提醒： 开发工作任务状态或者内容变更时给关注该任务的人发送邮件和微信 --ztt
			Long requirementFeatureId = requirementFeature.getId();
			sendDevTaskEmWeMassage(requirementFeatureId);
		}


		String detail = detailMap.toString().replace("=", "").replace(",", "；").replace("{", "").replace("}", "");
		log.setLogDetail(detail);
		insertLog(log, request);

		//日志附件
		getInsertLogAtts(files, request, allAtts, log);
		
		//关注提醒：开发任务状态改变或者内容变更时给关注该任务的人发送邮件和微信 --ztt
		sendEmWeMessage(requirementFeature2);

		if(requirementFeature.getCreateType()==2&&"03".equals(requirementFeature.getRequirementFeatureStatus())){
			requirementFeature.setFeatureCode(requirementFeature2.getFeatureCode());
			Map<String, Object> dataResult = pushData(requirementFeature);
			// 使用databus
			String taskId = "";
			if(requirementFeature2.getFeatureCode()!=null) {
				taskId = requirementFeature2.getFeatureCode();
			}
			DataBusUtil.send(databusccName,taskId,JsonUtil.toJson(dataResult));
		}
	}
	
	
	/**
	 * 
	* @Title: getAttentionList
	* @Description: 获取工作任务关注列表
	* @author author
	* @param attention
	* @return List<TblRequirementFeatureAttention>
	 */
	@Override
	public List<TblRequirementFeatureAttention> getAttentionList(TblRequirementFeatureAttention attention) {
		List<TblRequirementFeatureAttention> attentionList = tblRequirementFeatureAttentionMapper.selectList(new EntityWrapper<TblRequirementFeatureAttention>(attention));
		return attentionList;
	}
	
	/**
	 * 
	* @Title: changeAttention
	* @Description:更新关注
	* @author author
	* @param id
	* @param attentionStatus
	* @param request
	 */
	@Override
	@Transactional(readOnly = false)
	public void changeAttention(Long id, Integer attentionStatus, HttpServletRequest request) {
		Long userId = CommonUtil.getCurrentUserId(request);
		Timestamp stamp = new Timestamp(new Date().getTime());
		TblRequirementFeatureAttention attention = new TblRequirementFeatureAttention();
		attention.setRequirementFeatureId(id);
		attention.setUserId(userId);
		TblRequirementFeatureAttention attentionOld = tblRequirementFeatureAttentionMapper.selectOne(attention);
		if (attentionStatus == 1) { //关注
			if (attentionOld == null) {
				CommonUtil.setBaseValue(attention, request);
				tblRequirementFeatureAttentionMapper.insert(attention);
			} else if (attentionOld.getStatus() == 2) {
				attentionOld.setStatus(1);
				attentionOld.setLastUpdateBy(userId);
				attentionOld.setLastUpdateDate(stamp);
				tblRequirementFeatureAttentionMapper.updateByPrimaryKey(attentionOld);
			}
		} else {
			if (attentionOld != null) {
				attentionOld.setStatus(2);
				attentionOld.setLastUpdateBy(userId);
				attentionOld.setLastUpdateDate(stamp);
				tblRequirementFeatureAttentionMapper.updateByPrimaryKey(attentionOld);
			}
		}
	}

	/**
	 * 
	* @Title: getWindows
	* @Description: 获取所有投产窗口
	* @author author
	* @return List<TblCommissioningWindow>
	 */
	@Override
	public List<TblCommissioningWindow> getWindows() {
		return commissioningWindowMapper.getAll();
	}
    
	//拆分人员弹框数据
	@Override
	public List<Map<String, Object>> getSplitUser(Long systemId) {
		return requirementFeatureMapper.getSplitUser(systemId);
	}

	//通过名称获取开发任务
	@Override
	public List<TblRequirementFeature> findByName(TblRequirementFeature requirementFeature) {
		return requirementFeatureMapper.findByName(requirementFeature);
	}

	//同投产窗口的开发任务
	@Override
	public List<Map<String, Object>> findBrother(Long requirementId, Long id) {
		return requirementFeatureMapper.findBrother(requirementId, id);
	}
	
	//开发任务修改为实施中
	@Override
	@Transactional(readOnly = false)
	public void updateStatus(Long id, HttpServletRequest request) {
		TblRequirementFeature requirementFeature2 = requirementFeatureMapper.selectByPrimaryKey(id);
		requirementFeatureMapper.updateStatus(id);
		
		sendEmWeMessage(requirementFeature2);
	}

	//生成开发任务编号
	@Override
	public String findMaxCode(int length) {
		return requirementFeatureMapper.findMaxCode(length);
	}

	//获取附件
	@Override
	public List<TblRequirementFeatureAttachement> findAtt(Long id) {
		return requirementFeatureAttachementMapper.findAttByRFId(id);
	}

	/**
	 * 
	* @Title: updateTransfer
	* @Description: 转派
	* @author author
	* @param requirementFeature 开发任务
	* @param transferRemark 备注
	* @param request
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateTransfer(TblRequirementFeature requirementFeature, String transferRemark, HttpServletRequest request) {
		TblRequirementFeature requirementFeature2 = requirementFeatureMapper
				.selectByPrimaryKey(requirementFeature.getId());
		requirementFeature.setLastUpdateDate(new Timestamp(new Date().getTime()));
		requirementFeatureMapper.updateTransfer(requirementFeature);
		TblRequirementFeatureLog log = new TblRequirementFeatureLog();
		log.setRequirementFeatureId(requirementFeature.getId());
		log.setLogType("转派开发任务");
		Map<String, String> map = new HashMap<>();
		map.put("executeUserId", "executeUserId");
		//组装需要记录的日志字段：执行人
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
			String title = detail.substring(0,detail.indexOf("：")+1);
			detail = title+"&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;"+"；";

		}
		String remark = "";
		if(!StringUtils.isBlank(transferRemark)) {
			remark = "备注内容： "+transferRemark;
		}
		log.setLogDetail(detail+remark);
		insertLog(log, request);
	}

	@Override
	public List<TblRequirementFeature> findSynDevTask(TblRequirementFeature requirementFeature) {
		return requirementFeatureMapper.findSynDevTask(requirementFeature);
	}

	/**
	 * 
	* @Title: mergeDevTask
	* @Description: 合并开发任务
	* @author author
	* @param requirementFeature
	* @param synId 被合并的开发任务ID
	* @param request
	 */
	@Override
	@Transactional(readOnly = false)
	public void mergeDevTask(TblRequirementFeature requirementFeature, Long synId, HttpServletRequest request) {
		TblRequirementFeature requirementFeature2 = requirementFeatureMapper
				.selectByPrimaryKey(requirementFeature.getId());

		TblRequirementFeature requirementFeature3= requirementFeatureMapper
				.selectByPrimaryKey(synId);
		requirementFeatureMapper.updateTaskId(requirementFeature);
		
		//关注提醒：开发任务状态改变或者内容变更时给关注该任务的人发送邮件和微信 --ztt
		sendEmWeMessage(requirementFeature2);
		
		// 把原来的同步任务置为status 2
		requirementFeatureMapper.updateSynStatus(synId);
		//查询出同步任务下的工作任务移到要合并的自建任务下
		List<TblDevTask> devTasks = devTaskMapper.findByReqFeatureId(synId);
		if (devTasks!=null && devTasks.size()>0) {
			Map<String, Object> map2 = new HashMap<>();
			map2.put("devTasks",devTasks);
			map2.put("reqFeatureId", requirementFeature.getId());
			devTaskMapper.updateReqFeatureId(map2);
			//关注提醒： 开发工作任务状态或者内容变更时给关注该任务的人发送邮件和微信 --ztt
			for (TblDevTask tblDevTask : devTasks) {
				String userIds2 = tblDevTaskAttentionMapper.getUserIdsByDevTaskId(tblDevTask.getId());
				if(StringUtils.isNotBlank(userIds2)) {
					Map<String,Object> emWeMap = new HashMap<String, Object>();
					emWeMap.put("messageTitle", "【IT开发测试管理系统】- 你关注的任务有更新");
					emWeMap.put("messageContent","您关注的“"+ tblDevTask.getDevTaskCode()+" | "+tblDevTask.getDevTaskName()+"”，内容有更新，请注意。");
					emWeMap.put("messageReceiver",userIds2 );//接收人 关注该任务的人
					emWeMap.put("sendMethod", 3);//发送方式 3 邮件和微信
					devManageToSystemInterface.sendMessage(JSON.toJSONString(emWeMap));
				}
			}
		}	


		TblRequirementFeatureLog log = new TblRequirementFeatureLog();
		log.setRequirementFeatureId(requirementFeature.getId());
		log.setLogType("合并开发任务");
		Map<String, String> map = new HashMap<>();
		map.put("taskId", "taskId");
		map.put("featureCode", "featureCode");
		String detail = ReflectUtils.packageModifyContent(requirementFeature, requirementFeature2,map);
		log.setLogDetail(detail);
		insertLog(log, request);
        //合并后的状态如果为实施完成通知databus
		if("03".equals(requirementFeature2.getRequirementFeatureStatus())){
			requirementFeature2.setFeatureCode(requirementFeature3.getFeatureCode());
			requirementFeature2.setTaskId(requirementFeature3.getTaskId());
			Map<String, Object> dataResult = pushData(requirementFeature2);
			// 使用databus
			String taskId = "";
			if(requirementFeature3.getFeatureCode()!=null) {
				taskId = requirementFeature3.getFeatureCode();
			}
			DataBusUtil.send(databusccName,taskId,JsonUtil.toJson(dataResult));
		}
	}

	/**
	 * 
	* @Title: getReqFeatureAttByUrl
	* @Description: 获取附件
	* @author author
	* @param filePath
	* @return TblRequirementFeatureAttachement
	 */
	@Override
	public TblRequirementFeatureAttachement getReqFeatureAttByUrl(String filePath) {
		return requirementFeatureAttachementMapper.getReqFeatureAttByUrl(filePath);
	}

	/**
	 * 
	* @Title: addRemark
	* @Description: 添加备注附件
	* @author author
	* @param remark
	* @param files
	* @param request
	 */
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

	/**
	 * 
	* @Title: changeCancelStatus
	* @Description: 开发任务状态取消
	* @author author
	* @param requirementId 需求id
	 */
	@Override
	@Transactional(readOnly = false)
	public void changeCancelStatus(Long requirementId) {
		requirementFeatureMapper.changeCancelStatus(requirementId);
		//把开发任务下属工作任务置为取消状态  
		List<TblRequirementFeature> requirementFeatures = requirementFeatureMapper.findByrequirementId(requirementId);
		for (TblRequirementFeature tblRequirementFeature : requirementFeatures) {
			
			//关注提醒：开发任务状态改变或者内容变更时给关注该任务的人发送邮件和微信 --ztt
			sendEmWeMessage(tblRequirementFeature);
			//取消工作任务
			cancelStatusReqFeature(tblRequirementFeature.getId());
		}
	}

	/**
	 * 
	* @Title: findRemark
	* @Description: 获取开发任务备注
	* @author author
	* @param id 开发任务id
	* @return List<TblRequirementFeatureRemark>
	 */
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

	/**
	 * 
	* @Title: findLog
	* @Description: 获取日志
	* @author author
	* @param id 开发任务id
	* @return List<TblRequirementFeatureLog>
	 */
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

	/**
	 * 
	* @Title: insertLog
	* @Description: 保存开发任务日志
	* @author author
	* @param log 开发任务日志
	* @param request
	 */
	@Transactional(readOnly = false)
	public void insertLog(TblRequirementFeatureLog log, HttpServletRequest request) {
		log.setCreateBy(CommonUtil.getCurrentUserId(request));
		log.setCreateDate(new Timestamp(new Date().getTime()));
		log.setUserId(CommonUtil.getCurrentUserId(request));
		log.setUserAccount(CommonUtil.getCurrentUserAccount(request));
		log.setUserName(CommonUtil.getCurrentUserName(request));
		requirementFeatureLogMapper.insert(log);
	}

	/**
	 * 
	* @Title: insertLogAtt
	* @Description: 保存工作任务日志附件
	* @author author
	* @param tblRequirementFeatureAttachement 开发任务日志附件信息
	* @param logAttachement 日志附件
	* @param logId 日志id
	* @param request 
	 */
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
	 * 
	* @Title: getInsertAtts
	* @Description: 批量新增开发任务附件
	* @author author
	* @param reqFeatureId 开发任务id
	* @param files 开发任务附件信息
	* @param request
	* @param allAtts  原有的附件
	 */
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
		//找出现有和原有的不同的附件信息，并删除
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
	 * 
	* @Title: getInsertLogAtts
	* @Description: 批量新增开发任务日志附件
	* @author author
	* @param files 开发任务附件信息
	* @param request
	* @param allAtts 原有的附件信息
	* @param log 日志信息
	 */
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
		//找出现有和原有的不同的附件信息，并删除
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
	
	//从redis中获取数据字典信息
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


	//调整排期列表查询
	@Override
	@Transactional(readOnly=true)
	public List<TblCommissioningWindow> selectWindows(String windowName, Integer pageNumber, Integer pageSize) throws Exception {
		// TODO Auto-generated method stub
		Integer start = (pageNumber-1)*pageSize;
		HashMap<String, Object> map = new HashMap<>();
		map.put("start", start);
		map.put("pageSize", pageSize);
		map.put("windowName", windowName);
		List<TblCommissioningWindow> list = commissioningWindowMapper.selectWindows(map);
		return list;
	}

	/**
	 * 
	* @Title: findDftNoReqFeature
	* @Description: 获取没有关联开发任务的缺陷
	* @author author
	* @param defectInfo 查询条件
	* @param featureSource 任务来源
	* @param reqFeatureId 任务id
	* @param pageNumber
	* @param pageSize
	* @return List<TblDefectInfo>
	 */
	@Override
	public List<TblDefectInfo> findDftNoReqFeature(TblDefectInfo defectInfo,Integer featureSource,
												   Long reqFeatureId, Integer pageNumber, Integer pageSize) {
		Integer createType = 1;
		if(featureSource ==2){
			createType = 2;
		}

		Integer start = (pageNumber-1)*pageSize;
		HashMap<String, Object> map = new HashMap<>();
		map.put("start", start);
		map.put("pageSize", pageSize);
		map.put("createType",createType);
		map.put("defectInfo", defectInfo);
		if("".equals(defectInfo.getDefectCode())
				&&"".equals(defectInfo.getDefectSummary())
				&&defectInfo.getDefectStatus()==null){
			map.put("reqFeatureId",reqFeatureId);
		}else{
			map.put("reqFeatureId",null);
		}
		return requirementFeatureMapper.findDftNoReqFeature(map);
	}

	//通过开发任务ID获取缺陷
	@Override
	public List<TblDefectInfo> findDftByReqFId(Long id) {
		return requirementFeatureMapper.findDftByReqFId(id);
	}

	//获取所有开发任务
	@Override
	public List<Map<String, Object>> getAllFeature(TblRequirementFeature feature, int pageNumber, int pageSize) {
		Map<String, Object> map = new HashMap<>();
		int start = (pageNumber - 1) * pageSize;
		map.put("start", start);
		map.put("pageSize", pageSize);
		map.put("feature", feature);
		return requirementFeatureMapper.getAllFeature(map);

	}

	//组装databus报文
	public Map<String, Object> pushData(TblRequirementFeature requirementFeature) {
		Map<String, Object> mapAll = new LinkedHashMap<>();
		Map<String, Object> mapBody = new HashMap<>();
		mapBody.put("tbltaskId", requirementFeature.getFeatureCode());
		mapBody.put("taskResult","开发完成");
		mapBody.put("taskWorkload",requirementFeature.getActualWorkload());
		mapAll.put("requestHead",DataBusRequestHead.getRequestHead());
		mapAll.put("requestBody",mapBody);

		return mapAll;
	}

	
	//获取系统版本
	@Override
	public List<Map<String, Object>> getSystemVersionBranch(Long systemId) {
		List<Map<String, Object>> list = systemScmMapper.getSystemVersionBranch(systemId);
		for (Map<String, Object> map2 : list) {
			Long systemVersionId = (Long) map2.get("systemVersionId");
			String versionName = systemVersionMapper.getVersionName(systemVersionId);
			map2.put("systemVersionName", versionName);
		}
		return list;
	}

	@Override
	public String getVersionName(Long systemVersionId) {
		return systemVersionMapper.getVersionName(systemVersionId);
	}

	//通过需求和系统获取开发任务，合并用
	@Override
	public List<TblRequirementFeature> getReqFeatureByReqCodeAndSystemId(String requirementCode, Long systemId) {
		return  requirementFeatureMapper.getReqFeatureByReqCodeAndSystemId(requirementCode,systemId);

	}

	
	/**
	 * 
	* @Title: getDeplayReqFesture
	* @Description: 获取开发任务列表，带系统版本和投产窗口
	* @author author
	* @param feature 封装的查询跳进啊
	* @param statusArr 多选的状态
	* @param pageNumber
	* @param pageSize
	* @return List<DevTaskVo>
	 */
	@Override
	public List<DevTaskVo> getDeplayReqFesture(TblRequirementFeature feature,  String[] statusArr,int pageNumber, int pageSize) {
		Map<String, Object> map = new HashMap<>();
		int start = (pageNumber - 1) * pageSize;
		map.put("start", start);
		map.put("pageSize", pageSize);
		map.put("feature", feature);
		map.put("statusArr", statusArr);
		List<DevTaskVo> devtasks = requirementFeatureMapper.getDeplayReqFesture(map);
		for (DevTaskVo devTaskVo : devtasks) {
			List<TblDataDic> dataDics = getDataFromRedis("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS");
			for (TblDataDic dataDic : dataDics) {
				if (StringUtils.isNotBlank(dataDic.getValueCode())&&StringUtils.isNotBlank(devTaskVo.getStatusName())&&dataDic.getValueCode().equals(devTaskVo.getStatusName())) {
					devTaskVo.setStatusName(dataDic.getValueName());
				}
			}
		}
		return devtasks;
	}

	/**
	 * 
	* @Title: updateDeployStatus
	* @Description: 更新开发任务部署状态
	* @author author
	* @param idsArr 需要更新的开发任务id
	* @param status 对应的需要更新的状态
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateDeployStatus(String idsArr[], String status) {
		Map<String, Object> map = new HashMap<>();
		map.put("idsArr", idsArr);
		map.put("status", status);
		requirementFeatureMapper.updateDeployStatus(map);
	}

	@Override
	public TblRequirementFeature getFeatureByCode(String code) {
		return requirementFeatureMapper.getFeatureByCode(code);
	}

	//保存开发任务附件
	@Override
	@Transactional(readOnly = false)
	public void insertAtt(TblRequirementFeatureAttachement featureAttachement) throws Exception{
		requirementFeatureAttachementMapper.insertAtt(featureAttachement);
	}

	//无用
	@Override
	public List<TblRequirementFeature> findByReqFeatureIds(String[] idsArr) {
		Map<String, Object> map = new HashMap<>();
		map.put("idsArr", idsArr);
		requirementFeatureMapper.findByReqFeatureIds(map);
		return null;
	}

	//获取部署状态
	@Override
	public String getDeployStatus(Long id) {
		return requirementFeatureMapper.getDeployStatus(id);
	}

	/**
	 * 
	* @Title: synReqFeatureDeployStatus
	* @Description: 开发任务部署状态新增
	* @author author
	* @param requirementId 需求ID
	* @param systemId 系统ID
	* @param deployStatus 部署状态
	* @param loginfo 日志信息
	 */
	@Override
	@Transactional(readOnly = false)
	public void synReqFeatureDeployStatus(Long requirementId, Long systemId, String deployStatus,
										  String loginfo) {

		List<TblRequirementFeatureDeployStatus> deployStatusList = JSONObject.
				parseArray(deployStatus,TblRequirementFeatureDeployStatus.class);
		//找到需要修改状态的开发任务 同一需求 同一系统下 日志需要
		List<TblRequirementFeature> requirementFeatures = requirementFeatureMapper.
				selectBySystemIdAndReqId1(systemId,requirementId);

		for (TblRequirementFeature feature : requirementFeatures) {
			List<TblRequirementFeatureDeployStatus> oldDeployStatusList =
					deployStatusMapper.findByReqFeatureId(feature.getId());

			deployStatusMapper.deleteByFeatureId(feature.getId());
			for(TblRequirementFeatureDeployStatus deployStatus1 : deployStatusList ){
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
			//部署日志处理
			insertFeatureLog(feature.getId(),oldDeployStatusList,loginfo,2);
		}
	}

	/**
	 * 
	* @Title: insertFeatureLog
	* @Description: 开发任务部署状态日志
	* @author author
	* @param reqFeatureId 开发任务ID
	* @param oldDeployStatus 原有的状态
	* @param loginfo 日志
	* @param i 是否同步测试系统，1同步，其他不同步
	 */
	@Override
	@Transactional(readOnly = false)
	public void insertFeatureLog(Long reqFeatureId, List<TblRequirementFeatureDeployStatus>
			oldDeployStatus,String loginfo,int i){
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
		if(i==1){
			TblRequirementFeature feature = requirementFeatureMapper
					.selectByPrimaryKey(reqFeatureId);
			TblRequirementFeatureLog log1 = JsonUtil.fromJson(loginfo, TblRequirementFeatureLog.class);
			log1.setCreateBy(log.getUserId());
			log1.setCreateDate(new Timestamp(new Date().getTime()));
			String loginfo1 = JsonUtil.toJson(log1);
			String newDeployStatus1 = JSON.toJSONString(newDeployStatus);
			devManageToTestManageInterface.synReqFeatureDeployStatus(feature.getRequirementId(),
					feature.getSystemId(),newDeployStatus1,loginfo1);
			if(feature.getQuestionNumber()!=null&&feature.getRequirementFeatureSource()==2){
				devManageToTestManageInterface.synReqFeatureDeployStatus1(feature.getQuestionNumber(),newDeployStatus1,loginfo1);
			}else{
				devManageToTestManageInterface.synReqFeatureDeployStatus(feature.getRequirementId(),
						feature.getSystemId(),newDeployStatus1,loginfo1);
			}
		}
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

	/**
	 * 
	* @Title: updateDeployStatusOne
	* @Description: 更新部署状态
	* @author author
	* @param ids  开发任务ID
	* @param env 环境
	* @param jsonString 日志
	* @param deplyStatus 部署状态
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateDeployStatusOne(String ids, String env, String jsonString, Integer deplyStatus) {
		try {
			String status2 = "";
			Map<String, String> deployTaskSuccessMap = JSONObject.parseObject(deployTaskSuccess,Map.class);
			Map<String, String> deployTaskFailMap = JSONObject.parseObject(deployTaskSuccess,Map.class);
			Map<String, String> deployTaskRejectMap = JSONObject.parseObject(deployTaskSuccess,Map.class);
			Map<String, String> deployTaskCancelMap = JSONObject.parseObject(deployTaskSuccess,Map.class);
			//不同部署环境的状态值不一样
			if ("2".equals(deplyStatus.toString())) {// 成功
				status2 = deployTaskSuccessMap.get(env);
			} else if ("3".equals(deplyStatus.toString())) {// 失败
				status2 = deployTaskFailMap.get(env);
			} else if ("1".equals(deplyStatus.toString())) {// 驳回
				status2 = deployTaskRejectMap.get(env);
			} else if ("4".equals(deplyStatus.toString())) {// 取消
				status2 = deployTaskCancelMap.get(env);
			}

			String idsArr[] = ids.split(",");
			// 获取每个开发任务的deploystatus 如果有不需要添加 没有才添加
			logger.info("----处理中----，开发任务id条数="+idsArr.length);
			for (String id : idsArr) {
				List<TblRequirementFeatureDeployStatus> oldStatusList =
						deployStatusMapper.findByReqFeatureId(Long.parseLong(id));

				//更新部署状态
				TblRequirementFeatureDeployStatus featureDeployStatus1 = new TblRequirementFeatureDeployStatus();
				featureDeployStatus1.setRequirementFeatureId(Long.parseLong(id));
				featureDeployStatus1.setDeployStatu(Integer.valueOf(status2));
				featureDeployStatus1.setDeployTime(new Timestamp(new Date().getTime()));
				featureDeployStatus1.setStatus(1);
				featureDeployStatus1.setLastUpdateBy(Long.valueOf(1));
				featureDeployStatus1.setLastUpdateDate(new Timestamp(new Date().getTime()));
				TblRequirementFeatureDeployStatus featureDeployStatus =
						deployStatusMapper.findByReqFeatureIdDeployStatu(Long.parseLong(id),Integer.valueOf(status2));
				if(featureDeployStatus == null){
					featureDeployStatus1.setCreateBy(Long.valueOf(1));
					featureDeployStatus1.setCreateDate(new Timestamp(new Date().getTime()));
					deployStatusMapper.insertFeatureDeployStatus(featureDeployStatus1);
				}else {
					featureDeployStatus1.setId(featureDeployStatus.getId());
					deployStatusMapper.updateFeatureDeployStatus(featureDeployStatus1);
				}
				insertFeatureLog(Long.parseLong(id),oldStatusList,jsonString,1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改部署状态失败" + ":" + e.getMessage(), e);
		}
	}
	
	/**
	 * 
	* @Title: updateReqFeatureTimeTrace
	* @Description: 更新开发任务时间追踪点
	* @author author
	* @param json 开发任务时间追踪点
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateReqFeatureTimeTrace(String json) {
		if (!StringUtils.isBlank(json)) {
			TblRequirementFeatureTimeTrace timeTrace = JsonUtil.fromJson(json, TblRequirementFeatureTimeTrace.class);
			if (timeTrace.getRequirementFeatureId()!=null) {
				TblRequirementFeatureTimeTrace timeTraceBefore = requirementFeatureTimeTraceMapper.selectByReqFeatureId(timeTrace.getRequirementFeatureId());
				if (timeTraceBefore!=null) {
					if (timeTraceBefore.getDevTaskCreateTime()==null) {//首次创建工作任务时间
						if (timeTrace.getDevTaskCreateTime()!=null) {
							requirementFeatureTimeTraceMapper.updateDevTaskCreateTime(timeTrace);
						}
					}
					if (timeTraceBefore.getCodeFirstCommitTime() == null) {//首次提交代码时间
						if (timeTrace.getCodeFirstCommitTime()!=null) {
							requirementFeatureTimeTraceMapper.updategetCodeFirstCommitTime(timeTrace);
						}
					}
					if(timeTrace.getRequirementFeatureDevCompleteTime()!=null) {// 状态变为实施完成时间
						requirementFeatureTimeTraceMapper.updateReqFeatureDevCompleteTime(timeTrace);
					}

					if (timeTraceBefore.getRequirementFeatureTestingTime()==null) {// 开发任务对应的测试任务首次变成实施中时间
						if (timeTrace.getRequirementFeatureTestingTime()!=null) {
							requirementFeatureTimeTraceMapper.updateReqFeatureTestingTime(timeTrace);
						}
					}

					if (timeTrace.getRequirementFeatureTestCompleteTime()!=null) {//开发任务对应的测试任务最后变成实施完成时间
						requirementFeatureTimeTraceMapper.updateReqFeatureTestCompleteTime(timeTrace);
					}

					if (timeTrace.getRequirementFeatureLastProdTime()!=null) {//上线部署完成时间
						requirementFeatureTimeTraceMapper.updateReqFeatureLastProdTime(timeTrace);
					}

					if (timeTrace.getRequirementFeatureProdCompleteTime()!=null) {//上线部署完成时间
						requirementFeatureTimeTraceMapper.updateReqFeatureProdCompleteTime(timeTrace);
					}
				}

			}

		}
	}

	/**
	 *    更新开发任务时间点追踪表  只针对部署
	 * */
	@Override
	@Transactional(readOnly = false)
	public void updateReqFeatureTimeTraceForDeploy(String json) {
		if (!StringUtils.isBlank(json)) {
			TblRequirementFeatureTimeTrace timeTrace = JsonUtil.fromJson(json, TblRequirementFeatureTimeTrace.class);
			if (timeTrace.getReqFeatureIds()!=null) {
				String ids[] = timeTrace.getReqFeatureIds().split(",");
				for (String id : ids) {
					TblRequirementFeatureTimeTrace timeTraceBefore = requirementFeatureTimeTraceMapper.selectByReqFeatureId(Long.parseLong(id));
					if (timeTraceBefore!=null) {
						if (timeTraceBefore.getRequirementFeatureFirstTestDeployTime()==null) {//开发任务首次测试环境部署时间
							if (timeTrace.getRequirementFeatureFirstTestDeployTime()!=null) {
								timeTrace.setRequirementFeatureId(Long.parseLong(id));
								requirementFeatureTimeTraceMapper.updateReqFeatureFirstTestDeployTime(timeTrace);
							}
						}
					}

				}
			}

		}
	}

	/**
	 * shan - 根据ID 查询实体类信息
	 * @param requirementFeatureId
	 * @return
	 */
	@Override
	public TblRequirementFeature getFeatureById(Long requirementFeatureId) {
		return requirementFeatureMapper.getFeatureById(requirementFeatureId);
	}

	@Override
	public List<TblSprintInfo> getAllSprint() {
		return sprintInfoMapper.getAllSprint();
	}

	//部署需要的开发任务列表
	@Override
	public List<DevTaskVo> getPrdDeplayReqFesture(TblRequirementFeature feature, String[] statusArr,String windowId, int pageNumber, int pageSize) {
		Map<String, Object> map = new HashMap<>();
		int start = (pageNumber - 1) * pageSize;
		map.put("start", start);
		map.put("pageSize", pageSize);
		map.put("feature", feature);
		map.put("statusArr", statusArr);

		List<DevTaskVo> devtasks = requirementFeatureMapper.getPrdDeplayReqFesture(map);
		for (DevTaskVo devTaskVo : devtasks) {
			List<TblDataDic> dataDics = getDataFromRedis("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS");
			for (TblDataDic dataDic : dataDics) {
				if (StringUtils.isNotBlank(dataDic.getValueCode())&&StringUtils.isNotBlank(devTaskVo.getStatusName())&&dataDic.getValueCode().equals(devTaskVo.getStatusName())) {
					devTaskVo.setStatusName(dataDic.getValueName());
				}
			}
		}
		return devtasks;
	}

	//投产窗口列表
	@Override
	public List<TblCommissioningWindow> getLimitWindows(Long systemId) {
		List<TblCommissioningWindow>  list=	commissioningWindowMapper.getAllWindow();
		List<TblCommissioningWindow>  beforDate=new ArrayList<>();
		List<TblCommissioningWindow>  afterDate=new ArrayList<>();
		List<TblCommissioningWindow>  resultList=new ArrayList<>();
		Date nowDate= new Date();
		for(TblCommissioningWindow tblCommissioningWindow:list) {
			Date date=tblCommissioningWindow.getWindowDate();
			if(nowDate.compareTo(date)>=0) {//过期
				beforDate.add(tblCommissioningWindow);
			}else {//没过期
				afterDate.add(tblCommissioningWindow);
			}
		}

		//没有过期
		//resultList.addAll(list);
		int aftersize=afterDate.size();
		if(aftersize!=0) {
			TblCommissioningWindow tblCommissioningWindow=new TblCommissioningWindow();
			tblCommissioningWindow=afterDate.get(aftersize-1);
			for(TblCommissioningWindow tc:list) {
				if(tc.getId()==tblCommissioningWindow.getId()) {
					tc.setFeatureStatus("defaultSelect");
				}

			}
		}
		resultList.addAll(list);
		return resultList;


	}

	@Override
	public List<TblSprintInfo> getSprintBySystemId(Long systemId) {



		return sprintInfoMapper.getSprintBySystemId(systemId);
	}
//
//	@Override
//	public List<TblSprintInfo> getSprintBySystemIdLimit(Long systemId) {
//
//		List<TblSprintInfo> befor=sprintInfoMapper.getSprintBySystemIdBefor(systemId);
//		List<TblSprintInfo> after=sprintInfoMapper.getSprintBySystemIdAfter(systemId);
//		if(befor!=null && befor.size()>0){
//			after.add(befor.get(0));
//		}
//		return after;
//	}

	@Override
	@Transactional(readOnly = false)
	public void cancelStatusReqFeature(Long reqFeatureId) {
		//把该开发任务下属工作任务置为取消状态  
		devTaskMapper.updateStatus(reqFeatureId);
		
		sendDevTaskEmWeMassage(reqFeatureId);
		
	}

	/**
	 * 
	* @Title: updateSprints
	* @Description:更新冲刺
	* @author author
	* @param ids
	* @param sprintId 冲刺ID
	* @param devTaskStatus 开发任务状态
	* @param executeUserId 执行人
	* @param request
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateSprints(String ids, Long sprintId, String devTaskStatus, Integer executeUserId, HttpServletRequest request) {
		String idArr[] = ids.split(",");
		for (String id : idArr) {

			TblRequirementFeature requirementFeature = new TblRequirementFeature();
			requirementFeature.setId(Long.parseLong(id));
			requirementFeature.setSprintId(sprintId);
			requirementFeature.setRequirementFeatureStatus(devTaskStatus);
			requirementFeature.setExecuteUserId(executeUserId);
			TblRequirementFeature requirementFeature2 = requirementFeatureMapper.selectByPrimaryKey(Long.parseLong(id));

			requirementFeatureMapper.updateSprints(Long.parseLong(id),sprintId,devTaskStatus,executeUserId);

			//开发任务日志信息
			TblRequirementFeatureLog log = new TblRequirementFeatureLog();
			log.setRequirementFeatureId(requirementFeature.getId());
			log.setLogType("修改开发任务");
			Map<String, String> map = new HashMap<>();
			if (executeUserId!=null) {
				map.put("executeUserId", "executeUserId");
			}
			if (StringUtils.isNotBlank(devTaskStatus)) {
				map.put("requirementFeatureStatus", "requirementFeatureStatus");
			}
			if(sprintId!=null) {
				map.put("sprintId", "sprintId");
			}
			
			//组装日志内容
			Map<String, String> detailMap = ReflectUtils.packageModifyContentReMap(requirementFeature, requirementFeature2,map);

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

			if(detailMap.containsKey(sprintInfoName)) {
				String beforeName = "";
				String afterName = "";

				if(requirementFeature2.getSprintId()!=null) {
					beforeName = sprintInfoMapper.selectByPrimaryKey(requirementFeature2.getSprintId()).getSprintName();
				}
				if (requirementFeature.getSprintId()!=null) {
					afterName =  sprintInfoMapper.selectByPrimaryKey(requirementFeature.getSprintId()).getSprintName();
				}
				String title = detailMap.get(sprintInfoName).substring(0,detailMap.get(sprintInfoName).indexOf("："))+"：";
				detailMap.put(sprintInfoName,title+"&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;") ;
			}
			if(detailMap.containsKey(reqFeatureStatusInfoName)) {
				String beforeName = "";
				String afterName = "";
				List<TblDataDic> dataDics = getDataFromRedis("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS");
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


			String detail = detailMap.toString().replace("=", "").replace(",", "；").replace("{", "").replace("}", "");
			log.setLogDetail(detail);
			insertLog(log, request);

			//更新开发任务时间点追踪表 中 状态变为实施完成时间 字段
			if ("03".equals(requirementFeature.getRequirementFeatureStatus())) {
				Map<String, Object> jsonMap = new HashMap<>();
				jsonMap.put("requirementFeatureId", requirementFeature.getId());
				jsonMap.put("requirementFeatureDevCompleteTime",new Timestamp(new Date().getTime()));
				String json = JsonUtil.toJson(jsonMap);
				updateReqFeatureTimeTrace(json);
			}

			if("00".equals(requirementFeature.getRequirementFeatureStatus())) {
				//如果开发任务状态是取消 下属工作任务状态也变成取消
				cancelStatusReqFeature(requirementFeature.getId());
			}

			//把下属的工作任务的冲刺都改掉
			if (sprintId!=null) {
				devTaskMapper.updateSprintId(requirementFeature);
				//关注提醒： 开发工作任务状态或者内容变更时给关注该任务的人发送邮件和微信 --ztt
				sendDevTaskEmWeMassage(requirementFeature.getId());
			}
			//关注提醒：开发任务状态改变或者内容变更时给关注该任务的人发送邮件和微信 --ztt
			sendEmWeMessage(requirementFeature2);
		}

	}
	
	/**
	 * 关注提醒： 开发工作任务状态或者内容变更时给关注该任务的人发送邮件和微信 
	 * --ztt
	 * */
	public void sendDevTaskEmWeMassage(Long requirementFeatureId) {
		List<TblDevTask> list = devTaskMapper.findByReqFeatureId(requirementFeatureId);
		if (!list.isEmpty() && list.size()>0) {
			for (TblDevTask tblDevTask : list) {
				String userIds2 = tblDevTaskAttentionMapper.getUserIdsByDevTaskId(tblDevTask.getId());
				if(StringUtils.isNotBlank(userIds2)) {
					Map<String,Object> emWeMap = new HashMap<String, Object>();
					emWeMap.put("messageTitle", "【IT开发测试管理系统】- 你关注的任务有更新");
					emWeMap.put("messageContent","您关注的“"+ tblDevTask.getDevTaskCode()+" | "+tblDevTask.getDevTaskName()+"”，内容有更新，请注意。");
					emWeMap.put("messageReceiver",userIds2 );//接收人 关注该任务的人
					emWeMap.put("sendMethod", 3);//发送方式 3 邮件和微信
					devManageToSystemInterface.sendMessage(JSON.toJSONString(emWeMap));
				}
			}
		}
	}
		
	/**
	 * 关注提醒： 开发任务状态改变或者内容变更时给关注该任务的人发送邮件和微信 
	 * --ztt
	 * */
	public void sendEmWeMessage(TblRequirementFeature requirementFeature) {
		String userIds = tblRequirementFeatureAttentionMapper.getUserIdsByReqFeatureId(requirementFeature.getId());
		if(StringUtils.isNotBlank(userIds)) {
			Map<String,Object> emWeMap = new HashMap<String, Object>();
			emWeMap.put("messageTitle", "【IT开发测试管理系统】- 你关注的任务有更新");
			emWeMap.put("messageContent","您关注的“"+ requirementFeature.getFeatureCode()+" | "+requirementFeature.getFeatureName()+"”，内容有更新，请注意。");
			emWeMap.put("messageReceiver",userIds );//接收人 关注该任务的人
			emWeMap.put("sendMethod", 3);//发送方式 3 邮件和微信
			devManageToSystemInterface.sendMessage(JSON.toJSONString(emWeMap));
		}
	}

	/**
	 * 
	* @Title: synReqFeaturewindow
	* @Description: 修改投产窗口
	* @author author
	* @param requirementId 需求id
	* @param systemId 系统id
	* @param commissioningWindowId 投产窗口id
	* @param loginfo     日志信息
	* @param beforeName  变更前名称
	* @param afterName   变更后名称
	 */
	@Override
	@Transactional(readOnly=false)
	public void synReqFeaturewindow(Long requirementId, Long systemId, Long commissioningWindowId, String loginfo,String beforeName, String afterName) {
		// TODO Auto-generated method stub
		List<TblRequirementFeature> requirementFeatures = requirementFeatureMapper.selectBySystemIdAndReqId(systemId,requirementId);
		for (TblRequirementFeature tblRequirementFeature : requirementFeatures) {
//			String beforeName = "";
//			String afterName = "";
			TblRequirementFeatureLog log = JsonUtil.fromJson(loginfo, TblRequirementFeatureLog.class);
			log.setRequirementFeatureId(tblRequirementFeature.getId());
			log.setLogType("修改投产窗口");
//			if (!StringUtils.isBlank(beforeName)) {
//				beforeName = beforeName.substring(0,beforeName.length()-1);
//			}
//			if (!StringUtils.isBlank(afterName)) {
//				afterName = afterName.substring(0,afterName.length()-1);
//			}
			log.setLogDetail("投产窗口：" + "&nbsp;&nbsp;“&nbsp;<b>"+beforeName +"</b>&nbsp;”&nbsp;&nbsp;"+ "修改为" + "&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;");
			requirementFeatureLogMapper.insert(log);
		}
		if(commissioningWindowId==0) {
			commissioningWindowId=null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("requirementId", requirementId);
		map.put("systemId", systemId);
		map.put("commissioningWindowId", commissioningWindowId);
		requirementFeatureMapper.synReqFeaturewindow(map);
	}
	
	/**
	 * 
	* @Title: synReqFeatureDept
	* @Description: 修改部门并添加日志
	* @author author
	* @param requirementId
	* @param systemId
	* @param deptId
	* @param loginfo
	* @param deptBeforeName
	* @param deptAfterName
	* @throws
	 */
	@Override
	@Transactional(readOnly=false)
	public void synReqFeatureDept(Long requirementId, Long systemId, Long deptId, String loginfo, String deptBeforeName,
			String deptAfterName) {
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

	/**
	 * 
	* @Title: getProjectGroup
	* @Description: 获取Uid这个人的项目小组信息
	* @author author
	* @param uid
	* @return
	* @throws
	 */
	@Override
	@Transactional(readOnly=false)
	public String getProjectGroup(Long uid) {
		Map<String,Object> mapParam=new HashMap<>();
		mapParam.put("uid",uid);
		List<Map<String,Object>> tblProjectInfos=tblProjectInfoMapper.getProjectGroupByUid(mapParam);
		List<String> ids=new ArrayList<>();
		for (Map<String,Object> map:tblProjectInfos){
			ids.add(map.get("id").toString());
		}
		JSONArray jsonArray=new JSONArray();
		for (Map<String,Object> map:tblProjectInfos){
			com.alibaba.fastjson.JSONObject jsonObject=new com.alibaba.fastjson.JSONObject();
			jsonObject.put("id",Long.parseLong(map.get("id").toString()));
			jsonObject.put("name",map.get("projectGroupName").toString());
			if(map.get("parentId")!=null && !map.get("parentId").toString().equals("")){
				//tblProjectInfosChildRen.add(map);
				String id=map.get("parentId").toString();
				if(ids.contains(id)){
					jsonObject.put("pId",Long.parseLong(id));
				}else{
					if(map.get("parentIds")!=null) {
						String parentIds = map.get("parentIds").toString();
						String[] parentsArray=parentIds.split(",");
						for(String ida:parentsArray){
							int i=0;
							if(ids.contains(ida)){
								if(Integer.parseInt(ida)>i) {
									jsonObject.put("pId", Long.parseLong(ida));
									i=Integer.parseInt(ida);
								}
							}

						}
					}
				}

			}else{
				//tblProjectInfosParent.add(map);

				jsonObject.put("pId",0);
			}
			jsonArray.add(jsonObject);
		}


		if(jsonArray!=null){
			return jsonArray.toString();
		}else{
			return  "";
		}


	}

	/*@Override
	public List<String> getProjectGroupBySystemId(long systemId,long uid) {
		List<String> result=new ArrayList<>();
		Map<String,Object> mapParam=new HashMap<>();
		mapParam.put("uid",uid);
		List<Map<String,Object>> tblProjectInfos=tblProjectInfoMapper.getProjectGroupByUid(mapParam);
		TblSystemInfo tblSystemInfo=  systemInfoMapper.selectById(systemId);

		if(tblSystemInfo.getProjectId()!=null){
			long projectId=tblSystemInfo.getProjectId();
			for(Map<String,Object> bean:tblProjectInfos){
				if( bean.get("projectId")!=null ){
					if(Long.parseLong(bean.get("projectId").toString())==projectId){
						result.add(bean.get("id").toString());
					}
				}
			}
		}
		return result;
	}*/

	//无用
	@Override
	@Transactional(readOnly = false)
	public void updateGroupAndVersion(String ids,Long systemVersionId, Long executeProjectGroupId) {
		String idArr[] = ids.split(",");
		for (String id : idArr) {

			TblRequirementFeature tblRequirementFeatureBatch=	requirementFeatureMapper.selectById(id);
			if(executeProjectGroupId!=null  ) {
				tblRequirementFeatureBatch.setExecuteProjectGroupId(executeProjectGroupId);
			}
			if(systemVersionId!=null) {
				tblRequirementFeatureBatch.setSystemVersionId(systemVersionId);
			}
			if(systemVersionId!=null || executeProjectGroupId!=null){
//				requirementFeatureMapper.updateGroupAndVersion(Long.parseLong(id),systemVersionId,executeProjectGroupId);
				requirementFeatureMapper.updateById(tblRequirementFeatureBatch);
			}
		}
	}
	/**
	 *  获取冲刺信息
	 * @author weiji
	 * @param systemId 系统id
	 * @return List<TblSprintInfo>
	 */

	@Override
	public List<TblSprintInfo> getSprintBySystemIdLimit(long systemId) {
		List<TblSprintInfo> befor=sprintInfoMapper.getSprintBySystemIdBefor(systemId);
		List<TblSprintInfo> after=sprintInfoMapper.getSprintBySystemIdAfter(systemId);
		if(befor!=null && befor.size()>0){
			after.add(befor.get(0));
		}
		return after;
	}

	/**
	 * 
	* @Title: getDevTaskBySystemAndRequirement
	* @Description: 通过系统和需求获取开发任务
	* @author author
	* @param systemId
	* @param requirementId
	* @return
	* @throws
	 */
	@Override
	public List<Map<String, Object>> getDevTaskBySystemAndRequirement(Long systemId, Long requirementId) {
		Map<String,Object> map = new HashMap<>();
		map.put("systemId", systemId);
		map.put("requirementId", requirementId);
		List<Map<String, Object>> result = requirementFeatureMapper.getDevTaskBySystemAndRequirement(map);
		for (Map<String, Object> map2 : result) {
			String status = map2.get("requirementFeatureStatus").toString();
			if(status != null) {
				String string = redisUtils.get("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS").toString();
				com.alibaba.fastjson.JSONObject parseObject = JSON.parseObject(string);
				String featureStatus = parseObject.get(status).toString();
				map2.put("featureStatus", featureStatus);
			}
		}
		return result;
	}


/**
 * 
* @Title: getFeatureCode
* @Description: 开发任务编号规则
* @author author
* @return
* @throws
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
			String cod = findMaxCode(length);
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
	 * 
	* @Title: importExcel
	* @Description: 导入开发任务
	* @author author
	* @param file
	* @param request
	* @return
	* @throws
	 */
    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> importExcel(MultipartFile file, HttpServletRequest request)  {
        Map<String, Object> map = new HashMap<>();
        String errorMessage="";
        try {
            String fileName = file.getOriginalFilename();
            if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
                map.put("status", Constants.ITMP_RETURN_FAILURE);
                map.put("errorMessage", "上传文件格式出错");
                return map;
            }
            boolean isExcel2003 = true;
            if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
                isExcel2003 = false;
            }
            InputStream is = null;

            try {
                is = file.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Workbook wb = null;
            if (isExcel2003) {
                wb = new HSSFWorkbook(is);
            } else {
                wb = new XSSFWorkbook(is);
            }
            Sheet sheet = wb.getSheetAt(0);
            if (sheet == null) {
                map.put("status", Constants.ITMP_RETURN_FAILURE);
                map.put("errorMessage", "标题未对应");
                return map;
            }
            Row titleRow = sheet.getRow(0);
            String[] title = {"任务名称", "任务描述","涉及系统CODE", "关联需求编号", "所属处室名称","开发管理岗姓名", "开发管理岗账号", "执行人姓名","执行人账号", "系统版本", "冲刺名称","项目组","项目小组","投产窗口名称","任务来源","优先级","预计开始时间","预计结束时间","预计工作量"};//标题验证

            for (int i = 0; i < title.length; i++) {
                if (!titleRow.getCell(i).getStringCellValue().equals(title[i])) {
                    map.put("status", Constants.ITMP_RETURN_FAILURE);
                    map.put("errorMessage", "第" + (i + 1) + "列标题未对应");
                    return map;
                }
            }
            int count = 0;
            Long caseId = null;
            int lastRowNum = sheet.getLastRowNum();
            for (int i = 1; i <= lastRowNum; i++) {   //循环表格行
                Row row=sheet.getRow(i);
                if(row==null){
                    continue;
                }
                for(int z=0;z<20;z++){
                    //转成string
                    //	if(z!=11 || z!=12 || z!=13) {
                    if (row.getCell(z) != null) {
                        row.getCell(z).setCellType(Cell.CELL_TYPE_STRING);

                    }
                    //}
                }
                String userId=CommonUtil.getCurrentUserId(request).toString();
                TblRequirementFeature tblRequirementFeature=new TblRequirementFeature();
                tblRequirementFeature.setFeatureCode(getFeatureCode());
                tblRequirementFeature.setCreateType(1);// 创建方式 自建
                tblRequirementFeature.setRequirementFeatureStatus("01");// 任务状态为 未实施
                tblRequirementFeature.setCreateBy(CommonUtil.getCurrentUserId(request));
                tblRequirementFeature.setCreateDate(new Timestamp(new Date().getTime()));
                String featureName=row.getCell(0).getStringCellValue();//任务名称
                String featureOverview=row.getCell(1).getStringCellValue();//任务描述
                String systemCode=row.getCell(2).getStringCellValue();//涉及系统Code
                String  manageUserAccount=row.getCell(6).getStringCellValue();//开发管理岗账号
                String  executeUserAccount=row.getCell(8).getStringCellValue();//执行人

                if(featureName==null || featureName.equals("") || featureOverview==null ||
                        featureOverview.equals("") ||systemCode==null || systemCode.equals("")
                        || manageUserAccount==null || manageUserAccount.equals("") || executeUserAccount==null || executeUserAccount.equals("")){

                    String error=i+"";
                    errorMessage=errorMessage+error+",";
                    continue;
                }
                tblRequirementFeature.setFeatureName(featureName);
                tblRequirementFeature.setFeatureOverview(featureOverview);
                String systemId="";
                Map<String,Object> param=new HashMap<>();
                param.put("status",1);
                param.put("system_code",systemCode);
                List<TblSystemInfo> list=systemInfoMapper.selectByMap(param);
                if(list.size()==0){
                    continue;
                }else{
                    systemId=String.valueOf(list.get(0).getId());
                    tblRequirementFeature.setSystemId(list.get(0).getId());
                }

                String  requirementFeatureSource=row.getCell(14).getStringCellValue();//任务来源
                tblRequirementFeature.setRequirementFeatureSource(Integer.parseInt(requirementFeatureSource));
                if(requirementFeatureSource.equals("1")) {

                    String requirementCode = row.getCell(3).getStringCellValue();//关联需求编号
                    if (requirementCode != null && !requirementCode.equals("")) {
                        List<Map<String,Object>> reqlist = requirementFeatureMapper.getRequirementByCode(requirementCode);
                        if (reqlist.size() == 0) {
                        } else {
                            tblRequirementFeature.setRequirementId(Long.parseLong(reqlist.get(0).get("id").toString()));
                        }
                    }
                }
                String  deptName=row.getCell(4).getStringCellValue();//所属处室
                if(!deptName.equals("")){
                    List<Map<String,Object>> deptList = requirementFeatureMapper.getDeptByName(deptName);
                    //  Map<String,Object> deptMap=devManageToSystemInterface.selectDeptByDeptName(deptName);
                    if(deptList.get(0).get("id")!=null){
                        tblRequirementFeature.setDeptId(Integer.parseInt(deptList.get(0).get("id").toString()));
                    }
                }
                //开发管理岗 执行人
                List<TblUserInfo> manusers=  tblUserInfoMapper.getUserByAccount(manageUserAccount);
                List<TblUserInfo> exeusers=  tblUserInfoMapper.getUserByAccount(executeUserAccount);
                if(manusers!=null && manusers.size()>0){

                    tblRequirementFeature.setManageUserId(Integer.parseInt(String.valueOf(manusers.get(0).getId())));
                }
                if(exeusers!=null && exeusers.size()>0){
                    tblRequirementFeature.setExecuteUserId(Integer.parseInt(String.valueOf(exeusers.get(0).getId())));
                }
                //系统版本
                String  systemVersion=row.getCell(9).getStringCellValue();//系统版本
                Map<String, Object> verparam = new HashMap<>();
                verparam.put("status", 1);
                verparam.put("SYSTEM_ID", systemId);
                verparam.put("VERSION", systemVersion);
                List<TblSystemVersion> versionInfos= tblSystemVersionMapper.selectByMap(verparam);
                if(versionInfos!=null && versionInfos.size()>0){
                    tblRequirementFeature.setSystemVersionId(versionInfos.get(0).getId());
                }
                //冲刺
                String  sprintName=row.getCell(10).getStringCellValue();

                if(!sprintName.equals("")) {
                    TblSprintInfo tblSprintInfo=new TblSprintInfo();
                    tblSprintInfo.setSystemId(Long.parseLong(systemId));
                    tblSprintInfo.setSprintName(sprintName);
                    List<TblSprintInfo> sprintInfos=tblSprintInfoMapper.getSprintBySystemIdAndName(tblSprintInfo);
                    if (sprintInfos != null && sprintInfos.size() > 0) {
                        tblRequirementFeature.setSprintId(sprintInfos.get(0).getId());
                    }
                }

                //项目小组
                String projectPart=row.getCell(11).getStringCellValue();//项目组
                String projectlite=row.getCell(12).getStringCellValue();//项目小组
                if(projectPart!=null &&! projectPart.equals("") && projectlite!=null && !projectlite.equals("")){
                    String projectId="";
                    List<TblProjectInfo> tblProjectInfos=tblProjectInfoMapper.findProjectByName(projectPart);
                    if(tblProjectInfos!=null && tblProjectInfos.size()>0){
                        projectId=tblProjectInfos.get(0).getId().toString();

                        Map<String, Object> projectLiteparam = new HashMap<>();

                        projectLiteparam.put("projectId", projectId);
                        projectLiteparam.put("projectGroupName", projectlite);
                        projectLiteparam.put("uid", userId);
                        List<Map<String,Object>> projectGroupId=tblProjectInfoMapper.getProjectGroupByName(projectLiteparam);
                        if(projectGroupId!=null && projectGroupId.size()>0){
                            tblRequirementFeature.setExecuteProjectGroupId(Long.parseLong(projectGroupId.get(0).get("id").toString()));
                        }

                    }


                }
                //投产窗口名称
                String  commissioningWindowName=row.getCell(13).getStringCellValue();//投产窗口
                if(!commissioningWindowName.equals("")) {
                    Map<String, Object> commisparam = new HashMap<>();
                    commisparam.put("status", 1);
                    commisparam.put("WINDOW_NAME", commissioningWindowName);
                    List<TblCommissioningWindow> tblCommissioningWindows = tblCommissioningWindowMapper.selectByMap(commisparam);
                    if (tblCommissioningWindows != null && tblCommissioningWindows.size() > 0) {
                        tblRequirementFeature.setCommissioningWindowId(tblCommissioningWindows.get(0).getId());
                    }
                }
                //优先级
                String  requirementFeaturePriority=row.getCell(15).getStringCellValue();//优先级
                tblRequirementFeature.setRequirementFeaturePriority(Integer.parseInt(requirementFeaturePriority));
                String  planStartDate=row.getCell(16).getStringCellValue();//预计开始时间
                String  planEndDate=row.getCell(17).getStringCellValue();//预计结束时间
                if(!planStartDate.equals("")){
                    tblRequirementFeature.setPlanStartDate(HSSFDateUtil.getJavaDate(Double.parseDouble(planStartDate)));
                }
                if(!planEndDate.equals("")){
                    tblRequirementFeature.setPlanEndDate(HSSFDateUtil.getJavaDate(Double.parseDouble(planEndDate)));
                }
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                tblRequirementFeature.setCreateDate(timestamp);
                //预计工作量
                String  estimateWorkload=row.getCell(18).getStringCellValue();//预计工作量
                if(!estimateWorkload.equals("")){
                    tblRequirementFeature.setEstimateWorkload(Double.parseDouble(estimateWorkload));
                }
                requirementFeatureMapper.insertReqFeature(tblRequirementFeature);


                TblRequirementFeatureTimeTrace featureTimeTrace = new TblRequirementFeatureTimeTrace();
                featureTimeTrace.setRequirementFeatureId(tblRequirementFeature.getId());
                featureTimeTrace.setRequirementFeatureCreateTime(tblRequirementFeature.getCreateDate());
                featureTimeTrace.setStatus(1);
                featureTimeTrace.setCreateDate(new Timestamp(new Date().getTime()));
                featureTimeTrace.setLastUpdateDate(new Timestamp(new Date().getTime()));
                requirementFeatureTimeTraceMapper.insertFeatureTimeTrace(featureTimeTrace);
                TblRequirementFeatureLog log = new TblRequirementFeatureLog();
                log.setRequirementFeatureId(tblRequirementFeature.getId());
                log.setLogType("新增开发任务");
                insertLog(log, request);



            }
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
            if(!errorMessage.equals("")){
                String notice=errorMessage.substring(0,errorMessage.length()-1);
                notice=notice+"行必填项目未填写已过滤";
                map.put("notice",notice);
            }

        } catch (IOException e) {
            map.put("status", Constants.ITMP_RETURN_FAILURE);
            map.put("errorMessage", "上传文件格式出错");
			logger.error(e.getMessage(), e);
            e.printStackTrace();
            return  map;
        }
        return map;

    }

    @Override
    public TblRequirementFeature getOneFeature(String id) {
        return requirementFeatureMapper.selectById(Long.parseLong(id));
    }


    /**
     * 
    * @Title: findFieldByReqId
    * @Description: 开发任务自定义扩展字段
    * @author author
    * @param id
    * @return
    * @throws
     */
    @Override
    public List<ExtendedField> findFieldByReqId(Long id) {
        Map<String,Object> map=devManageToSystemInterface.findFieldByTableName("tbl_requirement_feature_itmpdb");
        String listTxt = JSONArray.toJSONString(map.get("field"));
        List<ExtendedField> extendedFields = JSONArray.parseArray(listTxt, ExtendedField.class);
        if(id!=null) {
			if (extendedFields != null) {
				Iterator<ExtendedField> it = extendedFields.iterator();
				while (it.hasNext()) {
					ExtendedField extendedField = it.next();
					if (extendedField.getStatus().equals("2")) {
						it.remove();
					} else {
						String fieldName=extendedField.getFieldName();
						String valueName = requirementFeatureMapper.getFeatureFieldTemplateById(id, fieldName);
						extendedField.setValueName(valueName == null ? "" : valueName);
					}
				}
			}
		}
        if(extendedFields==null){
        	extendedFields=new ArrayList<>();
		}
        return extendedFields;
    }

	@Override
	public List<Map<String, Object>> getTreeName(Map<String, Object> map) {
		return  requirementFeatureMapper.getTreeName(map);
	}

	//根据项目ids 查询这些项目下的项目组
	@Override
	public String getProjectGroupByProjectIds(Long systemId) {
		List<Map<String, Object>> tblProjectGroups=	tblProjectInfoMapper.getProjectGroupByProjectIds(systemId);
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
				String id=map.get("parentId").toString();
				jsonObject.put("pId", Long.parseLong(id));
			}else{

				jsonObject.put("pId",0);
			}
			jsonArray.add(jsonObject);
		}

		return jsonArray.toString();
		
	}

	//根据项目ids 查询这些项目下的项目组
	@Override
	public String getProjectGroupByProjectId(Long projectId) {
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
				String id=map.get("parentId").toString();
				jsonObject.put("pId", Long.parseLong(id));
			}else{

				jsonObject.put("pId",0);
			}
			jsonArray.add(jsonObject);
		}

		return jsonArray.toString();

	}

	@Override
	public int getAllProjectPlanCount(TblProjectPlan tblProjectPlan) {
		return projectPlanMapper.getAllProjectPlanCount(tblProjectPlan);
	}

	@Override
	public List<Map<String, Object>> getAllProjectPlan(TblProjectPlan tblProjectPlan, int pageNumber, int pageSize) {
		Map<String, Object> map = new HashMap<>();
		int start = (pageNumber - 1) * pageSize;
		map.put("start", start);
		map.put("pageSize", pageSize);
		map.put("tblProjectPlan", tblProjectPlan);
		return projectPlanMapper.getAllProjectPlan(map);
	}

	@Override
	public List<TblProjectPlan> getProjectPlanTree(Long projectId) {
		List<TblProjectPlan> projectPlanList = projectPlanMapper.selectPlanTree(projectId);
		if(projectPlanList!=null){
			projectPlanList.get(0).setOpen(true);
			projectPlanList.get(0).setParentId(Long.valueOf(0));
			for(TblProjectPlan projectPlan : projectPlanList){
				if(projectPlan.getPlanLevel()>0){
					TblProjectPlan ParentIdPlan = projectPlanMapper.getProjectPlanParentId(projectId,
							projectPlan.getPlanLevel()-1,projectPlan.getPlanOrder());
					if(ParentIdPlan!=null){
						projectPlan.setParentId(ParentIdPlan.getId());
					}
				}
			}
		}
		return projectPlanList;
	}
	/**
	 *  根据系统id和项目id获取冲刺信息
	 * @param systemId
	 * @param projectId
	 * @return
	 */
	@Override
	public List<TblSprintInfo> getSprintInfoListBySystemIdAndProjectId(@Param("systemId") Long systemId, @Param("projectId") Long projectId) {
		return sprintInfoMapper.getSprintInfoListBySystemIdAndProjectId(systemId,projectId);
	}

	/**
	*@author liushan
	*@Description 查询开发任务关联的系统，所属的工作任务是否有未评审数据
	*@Date 2020/3/6
	*@Param [id]
	*@return java.util.Map<java.lang.String,java.lang.Object>
	**/
	@Override
	public List<TblDevTask> selectDevTaskByReqFeatureIds(Long[] id) throws Exception {
		return devTaskMapper.selectDevTaskCodeReviewByReqFeatureIds(id);
	}

	/**
	 *  该任务是否关注
	 * @param id
	 * @return
	 */
	@Override
	public Integer getAttentionByReqFeatureId(Long id) {
		return devTaskMapper.getAttentionByReqFeatureId(id);
	}

	/**
	 * 
	* @Title: checkStatus
	* @Description: 用户验收标记
	* @author author
	* @param files 上传的附件
	* @param id 开发任务ID
	* @param request
	* @throws
	 */
	@Override
	@Transactional(readOnly = false)
	public void checkStatus(MultipartFile[] files,Long id,HttpServletRequest request){
		if (files.length > 0 && files != null) {
			for (MultipartFile file : files) {
				if (!file.isEmpty()) {
					InputStream inputStream = null;
					try{
						inputStream	= file.getInputStream();
					}catch (Exception e){
						e.getMessage();
					}
					String extension = file.getOriginalFilename()
							.substring(file.getOriginalFilename().lastIndexOf(".") + 1);// 后缀名
					String fileNameOld = file.getOriginalFilename();
					if (BrowserUtil.isMSBrowser(request)) {
						fileNameOld = fileNameOld.substring(fileNameOld.lastIndexOf("\\")+1);
					}
					Random random = new Random();
					String i = String.valueOf(random.nextInt());
					String keyname = s3Util.putObject(s3Util.getDevTaskBucket(), i, inputStream);
					TblRequirementFeatureAttachement atta = new TblRequirementFeatureAttachement();
					atta.setRequirementFeatureId(id);
					atta.setFileS3Bucket(s3Util.getDevTaskBucket());
					atta.setFileS3Key(keyname);
					atta.setFileNameOld(fileNameOld);
					atta.setFileType(extension);
					CommonUtil.setBaseValue(atta,request);
					requirementFeatureAttachementMapper.insertAtt(atta);
				}
			}
		}
		requirementFeatureMapper.updateCheckStatus(id);
	}

	@Override
	public String selectTreeName(String assetSystemTreeId)  {
		
		return selectModelName(Long.parseLong(assetSystemTreeId));
	}
	public String selectModelName(Long systemTreeId) {
		Map<String, Object> result= requirementFeatureMapper.selectModelName(systemTreeId);
		String name=result.get("systemTreeName").toString();
		if(result.containsKey("parentId")) {
			name=selectModelName(Long.parseLong(result.get("parentId").toString()))+"/"+name;
		}
		
		return name;
	}

	/**
	 * 
	* @Title: getProjectInfoAll
	* @Description: 获取所有项目信息，管理员获取所有，其他人获取自己的项目组
	* @author author
	* @param projectName
	* @param uid
	* @param roleCodes
	* @return
	* @throws
	 */
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
				tblProjectInfo.setSystemName1(systemInfoList.get(0).getSystemName());
				tblProjectInfo.setSystemCode(systemInfoList.get(0).getSystemCode());
			}else if(systemInfoList !=null &&systemInfoList.size()>1){
				map.put("systemInfoList",systemInfoList);
			}
		}
		map.put("projectInfoList",projectInfoList);
		return map;
	}

	//根据系统ID和需求ID获取开发工作任务
	@Override
	public List<TblDevTask> getFeatureBySystemAndRequirement(Long systemId, Long requirementId) {
		return requirementFeatureMapper.getFeatureBySystemAndRequirement(systemId, requirementId);
	}
	
	//根据开发任务ID查询工作任务状态为待实施和实施中的开发工作任务
	@Override
	public List<TblDevTask> getDevNotOverByFeaureId(Long featureId) {
		return devTaskMapper.getDevNotOverByFeaureId(featureId);
	}
}

