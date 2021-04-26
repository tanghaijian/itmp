package cn.pioneeruniverse.dev.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.databus.DataBusUtil1;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.common.utils.S3Util;
import cn.pioneeruniverse.dev.entity.DevTaskVo;
import cn.pioneeruniverse.dev.entity.FtpS3Vo;
import cn.pioneeruniverse.dev.entity.TblArtifactInfo;
import cn.pioneeruniverse.dev.entity.TblArtifactQualityDetail;
import cn.pioneeruniverse.dev.entity.TblCommissioningWindow;
import cn.pioneeruniverse.dev.entity.TblSprintInfo;
import cn.pioneeruniverse.dev.entity.TblSystemDeployeUserConfig;
import cn.pioneeruniverse.dev.entity.TblSystemInfo;
import cn.pioneeruniverse.dev.entity.TblSystemJenkins;
import cn.pioneeruniverse.dev.entity.TblSystemModule;
import cn.pioneeruniverse.dev.entity.TblSystemScm;
import cn.pioneeruniverse.dev.entity.TblToolInfo;
import cn.pioneeruniverse.dev.entity.VersionInfo;
import cn.pioneeruniverse.dev.service.build.IJenkinsBuildService;
import cn.pioneeruniverse.dev.service.deploy.IDeployService;
import cn.pioneeruniverse.dev.service.devtask.DevTaskService;
import cn.pioneeruniverse.dev.service.packages.PackageService;
import cn.pioneeruniverse.dev.service.structure.IStructureService;
import cn.pioneeruniverse.dev.service.systeminfo.ISystemInfoService;

/**
 * @ClassName: DeployController
 * @Description: 部署管理controller
 * @author author
 * @date Sep 7, 2020 14:33:07 AM
 *
 */
@RestController
@RequestMapping("deploy")
public class DeployController {
	@Autowired
	private IJenkinsBuildService iJenkinsBuildService;
	@Autowired
	RedisUtils redisUtils;
	@Autowired
	private IDeployService iDeployService;
	@Autowired
	private IStructureService istructureService;
	@Autowired
	private PackageService packageService;
	@Autowired
	private ISystemInfoService systemInfoService;
	@Autowired
	private DevTaskService devTaskService;
	@Autowired
	private S3Util s3Util;
	@Autowired
	private DataBusUtil1 dataBusUtil;
	//s3 ftp桶value
	@Value("${s3.ftpBucket}")
	private String ftpBucket;
	//databus名称
	@Value("${databuscc.name1}")
	private String databusccName;
	//数据字典部署状态中部署成功的code：key-value展示，比如{1：1，2：2}，由于部署状态分多个环境，每个环境的成功失败又分多个code，不同环境均有成功和失败，这里将部署状态重新按照成功、失败、拒绝、取消进行重新分类，下同
	@Value("${delpoy.task.success}")
	private String delpoyTaskSuccess;
    //部署任务失败
	@Value("${delpoy.task.fail}")
	private String delpoyTaskFail;
    //部署上传文件根目录
	@Value("${ftp.root.filePath}")
	private String ftpRootFilePath;
	//databusip地址
	@Value("${dataBus.ipaddr}")
	private  String dataBusIpaddr;
     //databus操作名称 itmgr_AutoOperation，通知投产窗口的上线系统、需求
	@Value("${databuscc.operationname}")
	private String dataBusOperationname;


	private final static Logger log = LoggerFactory.getLogger(DeployController.class);

  /**
   * 查询系统部署信息
   *
   * @author weiji
   * @param systemInfo 子系统查询参数
   * @param pageSize
   * @param pageNum
   * @param scmBuildStatus 部署状态
   * @param checkIds 选择系统id
   * @param checkModuleIds 选择模块id
   * @return Map<String, Object>  key value： parent 父节点， architectureType系统架构类型， environmentType 环境类型， packEnvids ，"
   *     createType 创建方式， isLeaf 是否叶子节点，false是否， deployType 部署类型， buildStatus任务状态  ， expanded
   *     是否展开， systemName 系统名称， nowEnvironmentType 正在执行的环境， artifactId 制品id，
   *     id  ， createDate 创建日志，， systemId 系统id，
   *     lastJobName "" level 节点类型，0是最大， key_id 此节点的唯一id， lastBuildTime 最新任务跑的时间，
   *     systemFlag "" createBy 创建者， systemCode 系统编码， envids 环境ids，
   *     choiceAtrEnvids 可选部署的环境，
   *     projectName 项目名称， projectId 项目id， status 部署状态， lastUpdateBy 更新人。
   */
  @RequestMapping(value = "getAllManualSystemInfo", method = RequestMethod.POST)
  public Map<String, Object> getAllManualSystemInfo(
      HttpServletRequest request,
      String systemInfo,
      Integer pageSize,
      Integer pageNum,
      String scmBuildStatus,
      String refreshIds,
      String checkIds,
      String checkModuleIds)
      throws Exception {
		Map<String, Object> result;
		Long uid = CommonUtil.getCurrentUserId(request);
		try {
			String[] refreshIdsArray = null;
			String[] checkIdsArray = null;
			String[] checkModuleArray = null;
			if (refreshIds != null && !refreshIds.equals("")) {
				refreshIdsArray = refreshIds.split(",");
			}
			if (checkIds != null && !checkIds.equals("")) {
				checkIdsArray = checkIds.split(",");
			}

			if (checkModuleIds != null && !checkModuleIds.equals("")) {
				checkModuleArray = checkModuleIds.split(",");
			}
			result = iDeployService.getAllManualSystemInfo(systemInfo, pageSize, pageNum, scmBuildStatus, uid, refreshIdsArray, checkIdsArray, checkModuleArray,request);
		} catch (Exception e) {
			this.handleException(e);
			throw e;
		}
		return result;
	}

  /**
   * 查询系统手动部署名称
   *
   * @author weiji
   * @param systemId 系统id
   * @return Map<String, Object>
   *     key value： system_id 系统id， JOB_TYPE 任务类型， create_type 创建方式 l
   *     ztreeObjs 节点集合
   */
  @RequestMapping(value = "getDeployJobName", method = RequestMethod.POST)
  public Map<String, Object> getDeployJobName(String systemId, HttpServletRequest request) {
		Map<String, Object> result;
		try {
			result = iDeployService.getDeployJobName(systemId,request);
		} catch (Exception e) {

			this.handleException(e);
			throw e;

		}
		return result;
	}

	/**
	 *  查询系统部署参数
	 * @author weiji
	 * @param systemJenkisId 系统JENKINS配置表id
	 * @return Map<String, Object>
	 *     key value：envTypes 可选环境，paramJson 返回的参数字符串，jobName 任务名称
	 */
	@RequestMapping(value = "getJobNameParam", method = RequestMethod.POST)
	public Map<String, Object> getJobNameParam(String systemJenkisId, Long systemId) {
		Map<String, Object> result;
		try {
			result = iDeployService.getJobNameParam(systemJenkisId);
			//查询该系统下的system_scm的环境类型
			List<String> envTypes = systemInfoService.getEnvTypes(systemId);
			result.put("envTypes", envTypes);
		} catch (Exception e) {

			this.handleException(e);
			throw e;

		}
		return result;
	}

	/**
	 *  手动部署接口
	 *
	 * @param systemJenkisId 系统JENKINS配置表id
	 * @param jsonParam 参数字符串
	 * @param reqFeatureqIds 开发任务id
	 * @param taskEnvType 环境类型
	 * @return Map<String, Object> jobRunNumber 此次任务在jenkins的编号，toolId 此次任务使用工具id，jobName 任务名称
	 */
	@Transactional
	@RequestMapping(value = "buildJobManualDeploy", method = RequestMethod.POST)
	public Map<String, Object> buildJobManualDeploy(HttpServletRequest request, String systemJenkisId, String jsonParam,
													String reqFeatureqIds, String taskEnvType) throws Exception {
		Map<String, Object> result = new HashMap<>();
		try {
			Map<String, Object> paramMap = iDeployService.buildJobManualDeploy(request, systemJenkisId,jsonParam);
			//status为2是失败直接返回
			if (paramMap.get("status") != null && paramMap.get("status").equals("2")) {
				return paramMap;
			}
			paramMap.put("jsonParam", jsonParam);
			if (reqFeatureqIds != null && !reqFeatureqIds.equals("") && !taskEnvType.equals("")) {
				paramMap.put("reqFeatureqIds", reqFeatureqIds);
				paramMap.put("taskEnvType", taskEnvType);
			}
			Long uid = CommonUtil.getCurrentUserId(request);
			String userName = CommonUtil.getCurrentUserName(request);
			String userAccount = CommonUtil.getCurrentUserAccount(request);
			paramMap.put("userName", userName);
			paramMap.put("userAccount", userAccount);
			paramMap.put("userId", String.valueOf(uid));
			paramMap.put("envName", "");
			TblToolInfo jenkinsToolInfo = (TblToolInfo) paramMap.get("jenkinsToolInfo");
			Map<String, Object> configMap = iJenkinsBuildService.buildManualDeployJob(paramMap);
			istructureService.updateConfigInfo(configMap, paramMap);
			// 轮询数据
			Boolean flag = redisUtils.exists("deployCallback");
			List<Map<String, Object>> itmpMaps = new ArrayList<>();

			if (flag == true) {
				itmpMaps = (List<Map<String, Object>>) redisUtils.get("deployCallback");
			}

			paramMap.put("jenkinsToolInfo", jenkinsToolInfo);
			itmpMaps.add(paramMap);
			redisUtils.set("deployCallback", itmpMaps);
			result.put("status", Constants.ITMP_RETURN_SUCCESS);

			// 新增日志
			result.put("toolId", jenkinsToolInfo.getId());
			result.put("jobName", paramMap.get("jobName").toString());
			result.put("jenkinsId", systemJenkisId);
			result.put("jobRunNumber", paramMap.get("jobNumber").toString());
			return result;
		} catch (Exception e) {

			this.handleException(e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("status", Constants.ITMP_RETURN_FAILURE);
			return result;
		}

	}

	/**
	 * 获取系统构建历史
	 * @author weiji
	 * @param systemId 系统id
	 * @param pageNumber
	 * @param pageSize
	 * @param jobType 任务类型
	 * @param jenkinsId 系统JENKINS配置表id
	 * @param envType 环境类型
	 * @param createType 创建方式
	 * @return Map<String, Object> moduleNames:模块名称，userName：创建者，startDate：开始时间，endDate：结束时间，buildParameter：构建的参数
	 */
	@RequestMapping(value = "getAllDeployMessage", method = RequestMethod.POST)
	public Map<String, Object> getAllDeployMessage(String systemId, Integer pageNumber, Integer pageSize,
												   String createType, Integer jobType,String jenkinsId,String envType,String flag) {
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> allList;
		List<Map<String, Object>> list;
		try {
			allList = iDeployService.selectMessageBySystemIdAndPage(Long.parseLong(systemId), 1, Integer.MAX_VALUE,
					createType, jobType,jenkinsId,envType,flag);

			list = iDeployService.selectMessageBySystemIdAndPage(Long.parseLong(systemId), pageNumber, pageSize,
					createType, jobType,jenkinsId,envType,flag);
			DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			for (Map<String, Object> maps : list) {
				String id = maps.get("id").toString();
				//获取该jobrunId下子模块
				String names=istructureService.selectModulesNamesByRunId(id);
				if(names!=null ){
					maps.put("moduleNames",names);
				}else{
					maps.put("moduleNames","");
				}
				//获取创建者
				if(maps.get("userName")==null || maps.get("userName").toString().equals("") ||
						(maps.get("jobName")!=null && maps.get("jobName").toString().contains("_deployScheduled"))
				){
					maps.put("userName","系统管理员");
				}

				maps.put("id", id);
				if (maps.get("startDate") != null) {
					maps.put("startDate", sdf.format(maps.get("startDate")));
				}

				if (maps.get("endDate") != null) {
					maps.put("endDate", sdf.format(maps.get("endDate")));
				}
				if(maps.get("buildParameter")==null){
					maps.put("buildParameter","");
				}

			}
		} catch (Exception e) {

			this.handleException(e);
			throw e;
		}

		map.put("rows", list);
		map.put("total", allList.size());
		return map;

	}

	/**
	 * 获取构建历史详细信息
	 * @author weiji
	 * @param jobRunId 系统JENKINS任务执行表id
	 * @param createType 创建方式
	 * @return Map<String, Object> content:内容，buildLogs：日志，buildStatus：部署状态，startDate：开始时间，endDate：结束时间等
	 */
	@RequestMapping(value = "getDeployMessageById", method = RequestMethod.POST)
	public Map<String, Object> getDeployMessageById(String jobRunId, String createType) {
		Map<String, Object> result;
		try {
			result = iDeployService.getDeployMessageById(jobRunId, createType);
		} catch (Exception e) {

			this.handleException(e);
			throw e;
		}
		return result;

	}

	/**
	 * 定时任务
	 * @author weiji
	 * @param systemJenkinsId 系统JENKINS配置表id
	 * @param type 部署类型
	 * @param jobCron 定时表达式
	 * @return map status：状态， message：返回信息
	 */
	@RequestMapping(value = "setCornOne", method = RequestMethod.POST)
	@Transactional
	public Map<String, Object> setCornOne(String systemJenkinsId, HttpServletRequest request, String type,
										  String jobCron) {
		Map<String, Object> map = new HashMap<>();
		// type 1 为自动部署定时 2为手动部署定时
		// 更新或获取该系统下systemjenkins表数据
		try {

			Map<String, Object> jenkinsParam = new HashMap<>();
			jenkinsParam.put("ID", systemJenkinsId);
			TblSystemJenkins tblSystemJenkins = istructureService.selectJenkinsByMap(jenkinsParam).get(0);
			if (tblSystemJenkins.getEnvironmentType() != null && (tblSystemJenkins.getEnvironmentType() == Integer.parseInt(Constants.PRDIN) || tblSystemJenkins.getEnvironmentType() == Integer.parseInt(Constants.PRDOUT))) {
				map.put("status", Constants.ITMP_RETURN_FAILURE);
				map.put("message", "暂且不支持生产环境定时任务");
				return map;
			}
			TblSystemScm scm = new TblSystemScm();
			if (tblSystemJenkins.getSystemScmId() != null) {
				long scmId = tblSystemJenkins.getSystemScmId();
				Map<String, Object> scmParam = new HashMap<>();
				scmParam.put("ID", scmId);
				scm = istructureService.selectSystemScmByMap(scmParam).get(0);
				//生产任务
				if (scm.getEnvironmentType() == Integer.parseInt(Constants.PRDIN) || scm.getEnvironmentType() == Integer.parseInt(Constants.PRDOUT)) {
					map.put("status", Constants.ITMP_RETURN_FAILURE);
					map.put("message", "暂且不支持生产环境定时任务");
					return map;
				}

			}
			if (jobCron.equals("")) {
				tblSystemJenkins.setJobCron(" ");

			} else {

				TblToolInfo jenkinsTool = istructureService.geTblToolInfo(tblSystemJenkins.getToolId());
				Map<String, String> valid = iJenkinsBuildService.validateCron(jenkinsTool, jobCron);
				String status = valid.get("status").toString();
				if (!status.equals("1")) {
					map.put("status", Constants.ITMP_RETURN_FAILURE);
					map.put("message", valid.get("message").toString());
					return map;
				}
				tblSystemJenkins.setJobCron(jobCron);
			}

			if (type.equals(Constants.CREATE_TYPE_AUTO)) {// 自动部署定时
				long systemId = scm.getSystemId();
				Object redisEnvType = redisUtils.get("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE");
				Map<String, Object> envMap = JSON.parseObject(redisEnvType.toString());
				String envName = envMap.get(scm.getEnvironmentType().toString()).toString();
				TblSystemInfo tblSystemInfo = istructureService.geTblSystemInfo(systemId);
				String serverType = tblSystemInfo.getArchitectureType() + "";
				String jobName = tblSystemInfo.getSystemCode() + "_" + scm.getEnvironmentType().toString() + "_"
						+ String.valueOf(systemId) + "_" + "deployScheduled";
				//判断是否toolid改变
				istructureService.judgeJenkins(tblSystemJenkins, jobName);
				tblSystemJenkins.setCronJobName(jobName);
				istructureService.updateJenkins(tblSystemJenkins);
				Map<String, Object> result = iDeployService.buildJobAutoDeployScheduled(request, scm.getSystemId() + "",
						serverType, scm.getEnvironmentType() + "", tblSystemInfo.getSystemName(), tblSystemJenkins);
				if (result.get("status") != null && result.get("status").toString().equals(Constants.ITMP_RETURN_FAILURE)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return result;
				}
				result.put("scheduled", "true");
				if (serverType.equals(Constants.SERVER_MICRO_TYPE)) {// 微服务
					iJenkinsBuildService.buildMicroAutoDeployJob(result);
				} else {
					iJenkinsBuildService.buildGeneralAutoDeployJob(result);
				}

			} else {
				istructureService.updateJenkins(tblSystemJenkins);
				Map<String, Object> paramMap = iDeployService.buildManualdeployScheduled(request, systemJenkinsId);
				paramMap.put("scheduled", "true");
				paramMap.put("tblSystemJenkins", tblSystemJenkins);
				Map<String, Object> configMap = iJenkinsBuildService.buildManualDeployJob(paramMap);
				istructureService.updateConfigInfo(configMap, paramMap);
			}
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
			return map;

		} catch (Exception e) {
			this.handleException(e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			map.put("status", Constants.ITMP_RETURN_FAILURE);
			map.put("message", "处理失败");
			return map;
		}

	}

	/**
	 *  获取定时任务列表
	 * @author weiji
	 * @param systemId
	 * @param type 部署类型
	 * @return map  rows：List<TblSystemJenkins>
	 */
	@RequestMapping(value = "getCorn", method = RequestMethod.POST)
	public Map<String, Object> getCorn(String type, long systemId) {
		Map<String, Object> map = new HashMap<>();
		// type 1 为自动构建部署定时 2为手动部署定时
		// 获取该系统下systemjenkins表数据
		Map<String, Object> param = new HashMap<>();
		param.put("SYSTEM_ID", systemId);
		param.put("STATUS", 1);

		if (type.equals("1")) {

			param.put("CREATE_TYPE", 1);
			param.put("JOB_TYPE", 2);

		} else {

			param.put("CREATE_TYPE", 2);
			param.put("JOB_TYPE", 2);
		}
		List<TblSystemJenkins> jenkinsList = istructureService.selectJenkinsByMap(param);

		List<TblSystemJenkins> jenkins = new ArrayList<>();
		for (TblSystemJenkins tsj : jenkinsList) {
			//自动部署
			if (type.equals(Constants.CREATE_TYPE_AUTO)) {
				if (tsj.getSystemScmId() == null || tsj.getSystemScmId().equals("")) {
					continue;

				}else{
					int statusFlag=istructureService.getTblsystemScmById(tsj.getSystemScmId()).getStatus();
					if(statusFlag==2){
						continue;
					}
				}



				if(tsj.getJobCron()==null || tsj.getJobCron().equals("") || tsj.getJobCron().equals(" ")){
					continue;
				}


				Map<String, Object> scmParam = new HashMap<>();
				scmParam.put("id", tsj.getSystemScmId());
				Integer environmentType = istructureService.selectSystemScmByMap(scmParam).get(0).getEnvironmentType();
				tsj.setEnvironmentType(environmentType);
				Object redisEnvType = redisUtils.get("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE");
				Map<String, Object> envMap = JSON.parseObject(redisEnvType.toString());
				String envName = envMap.get(environmentType + "").toString();
				tsj.setEnvironmentTypeName(envName);
			}
			tsj.setStringId(tsj.getId() + "");
			jenkins.add(tsj);

		}
		map.put("rows", jenkins);
		map.put("total", jenkins.size());
		return map;

	}

	/**
	 *  新增定时任务
	 * @author weiji
	 * @param tblSystemJenkins
	 * @return map status：状态 ，message：返回信息
	 */
	@RequestMapping(value = "insertCorn", method = RequestMethod.POST)
	@Transactional
	public Map<String, Object> insertCorn(TblSystemJenkins tblSystemJenkins, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
            String envFlag=tblSystemJenkins.getEnvironmentType().toString();
			String jobCornFlag = tblSystemJenkins.getJobCron();
			if (tblSystemJenkins.getEnvironmentType() != null && (tblSystemJenkins.getEnvironmentType() == Integer.parseInt(Constants.PRDIN) || tblSystemJenkins.getEnvironmentType() == Integer.parseInt(Constants.PRDOUT))) {
				map.put("status", Constants.ITMP_RETURN_FAILURE);
				map.put("message", "暂且不支持生产环境定时任务");
				return map;
			}
			if (jobCornFlag != null && !jobCornFlag.equals("") && !jobCornFlag.equals(" ")) {
				TblToolInfo jenkinsTool = istructureService.getEnvtool(tblSystemJenkins.getEnvironmentType() + "", "4");
				Map<String, String> valid = iJenkinsBuildService.validateCron(jenkinsTool, jobCornFlag);
				String status = valid.get("status").toString();
				if (!status.equals("1")) {
					map.put("status", Constants.ITMP_RETURN_FAILURE);
					map.put("message", valid.get("message").toString());
					return map;
				}

			}

			Map<String, Object> scmParam = new HashMap<>();
			scmParam.put("system_id", tblSystemJenkins.getSystemId());
			scmParam.put("ENVIRONMENT_TYPE", tblSystemJenkins.getEnvironmentType());
			scmParam.put("status", 1);
			if (istructureService.selectSystemScmByMap(scmParam).size() == 0) {
				map.put("status", Constants.ITMP_RETURN_FAILURE);
				map.put("message", tblSystemJenkins.getEnvironmentTypeName() + "请配置此环境下信息");
				return map;
			}
			TblSystemScm scm = istructureService.selectSystemScmByMap(scmParam).get(0);
			// 判断该环境下是否已经存在
			Map<String, Object> jenkinsParam = new HashMap<>();
			jenkinsParam.put("system_id", tblSystemJenkins.getSystemId());
			jenkinsParam.put("SYSTEM_SCM_ID", scm.getId());
			jenkinsParam.put("status", 1);
			jenkinsParam.put("create_type", 1);
			jenkinsParam.put("job_type", 2);
			List<TblSystemJenkins> jenkinsSize = istructureService.selectJenkinsByMap(jenkinsParam);
			long systemId = scm.getSystemId();
			TblSystemInfo tblSystemInfo = istructureService.geTblSystemInfo(systemId);
			//是否已经存在
			if (jenkinsSize.size() > 0) {
				if(jenkinsSize.get(0).getJobCron()==null || jenkinsSize.get(0).getJobCron().equals("") || jenkinsSize.get(0).getJobCron().equals(" ")){
					tblSystemJenkins=jenkinsSize.get(0);
					if(tblSystemJenkins.getBuildStatus()==null || tblSystemJenkins.getBuildStatus().equals("")) {
						tblSystemJenkins.setBuildStatus(1);
					}
					tblSystemJenkins.setJobCron(jobCornFlag);
//					tblSystemJenkins.setJobType(2);
//					tblSystemJenkins.setCreateType(1);
					if(tblSystemJenkins.getCronJobName()==null || tblSystemJenkins.getCronJobName().equals("")){
						String cronJobName = tblSystemInfo.getSystemCode() + "_" + envFlag + "_"
								+ tblSystemJenkins.getSystemId() + "_" + "deployScheduled";
						tblSystemJenkins.setCronJobName(cronJobName);
					}

					if(tblSystemJenkins.getToolId()==null ){
						TblToolInfo toolType = istructureService.getEnvtool(envFlag + "", "4");
						tblSystemJenkins.setToolId(toolType.getId());
					}
					istructureService.updateJenkins(tblSystemJenkins);


				}else {
				    map.put("status", Constants.ITMP_RETURN_FAILURE);
			     	map.put("message", tblSystemJenkins.getEnvironmentTypeName() + "已存在请不要重复添加");
				    return map;
				}
			}else {
				tblSystemJenkins.setSystemScmId(scm.getId());
				tblSystemJenkins.setStatus(1);
				tblSystemJenkins.setBuildStatus(1);
				tblSystemJenkins.setJobType(2);
				tblSystemJenkins.setCreateType(1);

				String cronJobName = tblSystemInfo.getSystemCode() + "_" + tblSystemJenkins.getEnvironmentType() + "_"
						+ tblSystemJenkins.getSystemId() + "_" + "deployScheduled";
				tblSystemJenkins.setCronJobName(cronJobName);
				TblToolInfo toolType = istructureService.getEnvtool(tblSystemJenkins.getEnvironmentType() + "", "4");
				tblSystemJenkins.setToolId(toolType.getId());
				tblSystemJenkins.setEnvironmentType(null);
				istructureService.insertSystemJenkins(tblSystemJenkins);

			}


			String serverType = tblSystemInfo.getArchitectureType() + "";
			Map<String, Object> result = iDeployService.buildJobAutoDeployScheduled(request, scm.getSystemId() + "",
					serverType, scm.getEnvironmentType() + "", tblSystemInfo.getSystemName(), tblSystemJenkins);

			if (result.get("status") != null && result.get("status").toString().equals(Constants.ITMP_RETURN_FAILURE)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return result;
			}
			result.put("scheduled", "true");
			if (serverType.equals(Constants.SERVER_MICRO_TYPE)) {// 微服务
				iJenkinsBuildService.buildMicroAutoDeployJob(result);
			} else {
				iJenkinsBuildService.buildGeneralAutoDeployJob(result);
			}
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
			map.put("message", "处理成功");
			return map;
		} catch (Exception e) {
			this.handleException(e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			map.put("status", Constants.ITMP_RETURN_FAILURE);
			map.put("message", "处理失败");
			return map;
		}

	}

	/**
	 * 批量部署 （废弃）
	 *
	 * @param env
	 * @param data
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "creatJenkinsDeployJobBatch", method = RequestMethod.POST)
	public Map<String, Object> creatJenkinsJobBatch(String env, String data, HttpServletRequest request) {
		Map<String, Object> result;
		try {
			result = iDeployService.buildJobAutoDeployBatch(env, data, request);

		} catch (Exception e) {

			this.handleException(e);
			throw e;
		}
		return result;
	}

	/**
	 * 获取微服务sonar信息
	 * @author weiji
	 * @param systemId 系统id
	 * @param startDate
	 * @param endDate
	 * @return Map<String, Object> bugs数目,Vulnerabilities:Vulnerabilities，Code Smells：坏味道，Coverage:重复率等
	 */
	@RequestMapping(value = "getSonarMessageMincro", method = RequestMethod.POST)
	public Map<String, Object> getSonarMessageMincro(String systemId, String startDate, String endDate) {

		Map<String, Object> result;
		try {
			result = iDeployService.getSonarMessageMincro(systemId, startDate, endDate);

		} catch (Exception e) {

			this.handleException(e);
			throw e;
		}
		return result;

	}

	/**
	 *  获取sonar扫描历史
	 * @author weiji
	 * @param systemId 系统id
	 * @param startDate
	 * @param endDate
	 * @return map  Bugs：bugs数目,Vulnerabilities:Vulnerabilities，Code Smells：坏味道，Coverage:重复率等
	 */
	@RequestMapping(value = "getSonarMessage", method = RequestMethod.POST)
	public Map<String, Object> getSonarMessage(String systemId, String startDate, String endDate) {
		Map<String, Object> result;
		try {
			result = iDeployService.getSonarMessage(systemId, startDate, endDate);
			// result = istructureService.getSonarMessage(systemId, startDate, endDate);
		} catch (Exception e) {

			this.handleException(e);
			throw e;
		}
		return result;

	}

	/**
	 * 自动部署
	 *
	 * @param sysId 系统ID
	 * @param systemName 系统名称
	 * @param modules 子模块id
	 * @param env 环境类型
	 * @param reqFeatureqIds 开发任务
	 * @param sqlType sql脚本类型 1-结构变更(ddl)、2-数据处理(dml)
	 * @param deptId 部门id
	 * @return Map<String, Object> jobName:任务名称，toolId：工具id,jenkinsId：tblsystemjenkins的id，status：状态
	 */
	@RequestMapping(value = "buildJobAutoDeploy", method = RequestMethod.POST)
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> buildJobAutoDeploy(MultipartFile[] configFile, MultipartFile[] operFile,
												  MultipartFile[] sqlFile, String sysId, String systemName, String serverType, String modules, String env,
												  String reqFeatureqIds, String recoveryTime, String deptId, String userId, String sqlType, HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<>();
		try {
			TblSystemInfo tblSystemInfo = istructureService.geTblSystemInfo(Long.parseLong(sysId));
			//部署所需参数组装，判断可以自动化部署的条件
			Map<String, Object> result = iDeployService.buildJobAutoDeploy(request, sysId, serverType, env, systemName,
					modules);
			if (result.get("status") != null && result.get("status").toString().equals(Constants.ITMP_RETURN_FAILURE)) {
				return result;
			}
			Long uid = CommonUtil.getCurrentUserId(request);
			String userName = CommonUtil.getCurrentUserName(request);
			String userAccount = CommonUtil.getCurrentUserAccount(request);
			result.put("userName", userName);
			result.put("userAccount", userAccount);
			result.put("userId", String.valueOf(uid));
			result.put("envType", env);
			if (reqFeatureqIds != null && !reqFeatureqIds.equals("")) {
				result.put("reqFeatureqIds", reqFeatureqIds);

			}

			int proDuctionDeploytype = 2;// 默认非726部署方式
			if (tblSystemInfo.getProductionDeployType() != null) {
				proDuctionDeploytype = tblSystemInfo.getProductionDeployType();
			}
			if (proDuctionDeploytype == 2 && (env.equals(Constants.PRDIN) || env.equals(Constants.PRDOUT))) {
				// 非726部署方式 自动化运维平台 prd环境 非726和726都需要推jar包 进阶部署（多一个中间流程）
				// 自动化运维平台
				VersionInfo versionInfo = new VersionInfo();
				versionInfo.setDepartmant(deptId);
				versionInfo.setVersionManager(userId);
				if (sqlType != null) {
					versionInfo.setSqlType(Integer.parseInt(sqlType));
				}
				Date date = new Date();
				date = Timestamp.valueOf(recoveryTime);
				versionInfo.setRecoveryTime(date);
				Map<String, Object> fileMap = detailAutoPlantform(tblSystemInfo, configFile, operFile, sqlFile, env,
						reqFeatureqIds, versionInfo);
				result.putAll(fileMap);
				Long[] featureIds = null;
				// 调用自动化平台
				if (reqFeatureqIds != null && !reqFeatureqIds.equals("")) {
					String[] req = reqFeatureqIds.split(",");
					featureIds = (Long[]) ConvertUtils.convert(req, Long.class);
				}
				result.put("versionInfo", versionInfo);
				result.put("proDuctionDeploytype", proDuctionDeploytype);
				result.put("envType", env);
				// 调用部署接口
				iJenkinsBuildService.buildPROAutoDeployJob(result);
			} else if (proDuctionDeploytype == 1 && (env.equals(Constants.PRDIN) || env.equals(Constants.PRDOUT))) {
				// 726部署方式 暂且不写
				if (serverType.equals(Constants.SERVER_MICRO_TYPE)) {// 微服务
					iJenkinsBuildService.buildMicroAutoDeployJob(result);
				} else {//传统
					iJenkinsBuildService.buildGeneralAutoDeployJob(result);
				}
			} else {
				if (serverType.equals(Constants.SERVER_MICRO_TYPE)) {// 微服务
					iJenkinsBuildService.buildMicroAutoDeployJob(result);
				} else {
					iJenkinsBuildService.buildGeneralAutoDeployJob(result);
				}

			}
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
			// 新增日志
			TblSystemJenkins tblSystemJenkins = (TblSystemJenkins) result.get("tblSystemJenkins");
			map.put("toolId", tblSystemJenkins.getToolId());//工具ID
			map.put("jobName", tblSystemJenkins.getJobName());//Jenkin任务名
			map.put("jenkinsId", tblSystemJenkins.getId());//jenkins 配置主键
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			map.put("status", Constants.ITMP_RETURN_FAILURE);
			map.put("message", "部署失败");
			this.handleException(e);
			return map;
		}
		return map;

	}

	/**
	 * 
	* @Title: detailAutoPlantform
	* @Description: 自动化运维平台推送文件
	* @author author
	* @param tblSystemInfo 系统信息
	* @param configFile 配置文件
	* @param operFile 操作手册
	* @param sqlFile SQL文件
	* @param env 环境
	* @param reqFeatureqIds 开发任务
	* @param versionInfo 版本信息
	* @return Map<String, Object>
	* @throws IOException
	 */
	private Map<String, Object> detailAutoPlantform(TblSystemInfo tblSystemInfo, MultipartFile[] configFile,
													MultipartFile[] operFile, MultipartFile[] sqlFile, String env, String reqFeatureqIds,
													VersionInfo versionInfo) throws IOException {
		Map<String, Object> map = new HashMap<>();
		Object envinfo = redisUtils.get("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE");
		Map<String, Object> envMap = JSON.parseObject(envinfo.toString());
		String envName = envMap.get(env).toString();
		Date currentDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String data = formatter.format(currentDate);
		data = data.replaceAll("-", "");
		// 判断今日最新
		int item = 1;
		String ftpPath = tblSystemInfo.getSystemCode() + "/" + tblSystemInfo.getSystemCode() + "_" + envName + "_"
				+ data;
		Boolean flag = redisUtils.exists(ftpPath);
		if (flag == true) {
			item = Integer.parseInt((String) redisUtils.get(ftpPath));
			item = item + 1;

		}
		NumberFormat fm = new DecimalFormat("000");
		redisUtils.set(ftpPath, fm.format(item), new Long(24 * 60 * 60));
		ftpPath = ftpRootFilePath + "/" + ftpPath + "_" + fm.format(item);
		versionInfo.setVersionFilePath(ftpPath);
		versionInfo.setVersionNumber(tblSystemInfo.getSystemCode() + "_" + envName + "_" + data + "_" + fm.format(item));
		if (envName.equals("PRD-IN")) {
			versionInfo.setVersionType(0);
		} else if (envName.equals("PRD-OUT")) {
			versionInfo.setVersionType(1);
		}
		String configPath = ftpPath + "/configuration";
		String operPath = ftpPath + "/document";
		String sqlPath = ftpPath + "/sql";
		List<FtpS3Vo> configResult = detailFile(configFile, configPath);
		List<FtpS3Vo> operFileResult = detailFile(operFile, operPath);
		List<FtpS3Vo> sqlFileResult = detailFile(sqlFile, sqlPath);
		map.put("configResult", configResult);
		map.put("operFileResult", operFileResult);
		map.put("sqlFileResult", sqlFileResult);
		map.put("ftpPath", ftpPath);
		return map;

	}
	//处理文件

	private List<FtpS3Vo> detailFile(MultipartFile[] files, String ftpPath) throws IOException {
		List<FtpS3Vo> list = new ArrayList<>();
		if (files.length > 0 && files != null) {
			for (MultipartFile file : files) {
				String OldfileName = getFileNameNoEx(file.getOriginalFilename());
				if (!file.isEmpty()) {
					InputStream inputStream = file.getInputStream();

					String extension = file.getOriginalFilename()
							.substring(file.getOriginalFilename().lastIndexOf(".") + 1);// 后缀名
					String keyname = s3Util.putObject(ftpBucket, OldfileName, inputStream);
					FtpS3Vo ftpS3Vo = new FtpS3Vo();
					ftpS3Vo.setFileName(OldfileName + "." + extension);
					ftpS3Vo.setFtpPath(ftpPath);
					ftpS3Vo.setKeyName(keyname);
					list.add(ftpS3Vo);


				} else {
					// 文件文件为空
				}
			}
		}
		return list;
	}

	public static String getFileNameNoEx(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length()))) {
				return filename.substring(0, dot);
			}
		}
		return filename;
	}

	/**
	 * 获取module
	 * @author weiji
	 * @param systemId
	 * @param env 环境类型
	 * @param architectureType 系统架构类型
	 * @return Map<String, Object> artifactList:List<TblArtifactInfo>制品包集合，modules：List<TblSystemModule>
	 */
	@RequestMapping(value = "getModuleInfo", method = RequestMethod.POST)
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> getModuleInfo(String systemId, String env, Integer architectureType,
											 HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		try {

			List<TblArtifactInfo> artifactList = new ArrayList<>();
			if (architectureType == 1) {
				List<TblSystemModule> modules = iDeployService.getModuleInfo(systemId);
				Long moduleId = null;
				if (modules != null && !modules.isEmpty()) {
					moduleId = modules.get(0).getId();
					artifactList = packageService.findNewPackage(Long.parseLong(systemId), moduleId,
							Integer.parseInt(env));

				}
				map.put("modules", modules);
			} else {
				// 普通服务
				artifactList = packageService.findNewPackage(Long.parseLong(systemId), null, Integer.parseInt(env));

			}
			map.put("artifactList", artifactList);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);

		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			map.put("status", Constants.ITMP_RETURN_FAILURE);
			this.handleException(e);
			return map;
		}
		return map;

	}

	/**
	 * 获取artifactinfo 信息
	 * @author weiji
	 * @param systemId
	 * @param moduleId
	 * @param env 环境类型
	 * @param request
	 * @return Map<String, Object> list：List<TblArtifactInfo>，status：状态
	 */
	@RequestMapping(value = "getArtifactInfo", method = RequestMethod.POST)
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> getArtifactInfo(Long systemId, Long moduleId, Integer env, HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<>();
		try {
			List<TblArtifactInfo> list = packageService.findNewPackage(systemId, moduleId, env);
			map.put("list", list);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

			map.put("status", Constants.ITMP_RETURN_FAILURE);
			// log.error(e.getMessage(), e);
			this.handleException(e);
			return map;
		}
		return map;

	}

	/**
	 *  制品部署
	 * @author weiji
	 * @param systemId 系统id
	 * @param artifactids 制品包id
	 * @param env 环境类型
	 * @param request
	 * @param configFile 配置文件
	 * @param operFile 操作文件
	 * @param sqlFile sql文件
	 * @return Map<String, Object> jobName:任务名称，toolId：工具id,jenkinsId：tblsystemjenkins的id，status：状态
	 */
	@RequestMapping(value = "buildPackageDeploy", method = RequestMethod.POST)
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> buildPackageDeploy(String systemId, String artifactids, String env,
												  HttpServletRequest request, MultipartFile[] configFile, MultipartFile[] operFile, MultipartFile[] sqlFile,
												  String recoveryTime, String deptId, String userId, String reqFeatureqIds, String sqlType) throws Exception {
		Map<String, Object> map = new HashMap<>();
		try {

			TblSystemInfo tblSystemInfo = istructureService.geTblSystemInfo(Long.parseLong(systemId));
			String[] artifactidsArray = artifactids.split(",");
			//制品部署组装参数
			Map<String, Object> paraMap = iDeployService.buildPackageDeploy(request, systemId, env,
					artifactidsArray);

			if (paraMap.get("status") != null && paraMap.get("status").equals(Constants.ITMP_RETURN_FAILURE)) {
				return paraMap;
			}

			Long uid = CommonUtil.getCurrentUserId(request);
			String userName = CommonUtil.getCurrentUserName(request);
			String userAccount = CommonUtil.getCurrentUserAccount(request);
			paraMap.put("userId", String.valueOf(uid));
			paraMap.put("userName", userName);
			paraMap.put("userAccount", userAccount);
			paraMap.put("envType", env);
			if (reqFeatureqIds != null && !reqFeatureqIds.equals("")) {
				paraMap.put("reqFeatureqIds", reqFeatureqIds);
			}
			// 获取nexustool类
			TblToolInfo nexusToolInfo = new TblToolInfo();
			Map<String, Object> param = new HashMap<>();
			param.put("status", 1);
			param.put("TOOL_TYPE", 6);
			List<TblToolInfo> toolList = istructureService.getTblToolInfo(param);
			if (toolList != null && toolList.size() > 0) {
				nexusToolInfo = toolList.get(0);
			}
			paraMap.put("nexusTool", nexusToolInfo);
			int proDuctionDeploytype = 2;// 默认非726部署方式
			if (tblSystemInfo.getProductionDeployType() != null) {
				proDuctionDeploytype = tblSystemInfo.getProductionDeployType();
			}
			if (proDuctionDeploytype == 2 && (env.equals(Constants.PRDIN) || env.equals(Constants.PRDOUT))) {// 非726部署方式
				// 非726部署方式 自动化运维平台 prd环境 非726和726都需要推jar包 进阶部署（多一个中间流程）
				VersionInfo versionInfo = new VersionInfo();
				versionInfo.setDepartmant(deptId);
				Date date = new Date();
				date = Timestamp.valueOf(recoveryTime);
				versionInfo.setRecoveryTime(date);
				versionInfo.setVersionManager(userId);
				if (sqlType != null) {
					versionInfo.setSqlType(Integer.parseInt(sqlType));
				}

				Map<String, Object> fileMap = detailAutoPlantform(tblSystemInfo, configFile, operFile, sqlFile, env, reqFeatureqIds, versionInfo);
				Long[] featureIds = null;
				paraMap.putAll(fileMap);
				if (reqFeatureqIds != null && !reqFeatureqIds.equals("")) {
					String[] req = reqFeatureqIds.split(",");

					featureIds = (Long[]) ConvertUtils.convert(req, Long.class);
				}
				paraMap.put("versionInfo", versionInfo);
				paraMap.put("proDuctionDeploytype", proDuctionDeploytype);
				paraMap.put("envType", env);
				iJenkinsBuildService.buildPROAutoDeployJob(paraMap);


			} else if (proDuctionDeploytype == 1 && (env.equals(Constants.PRDIN) || env.equals(Constants.PRDOUT))) {
				// 726部署方式 暂且不写
				iJenkinsBuildService.buildPackageAutoDeployJob(paraMap);

			} else {
				iJenkinsBuildService.buildPackageAutoDeployJob(paraMap);
			}
			// 新增日志
			TblSystemJenkins tblSystemJenkins = (TblSystemJenkins) paraMap.get("tblSystemJenkins");
			map.put("toolId", tblSystemJenkins.getToolId());
			map.put("jobName", tblSystemJenkins.getJobName());
			map.put("jenkinsId", tblSystemJenkins.getId());

		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

			map.put("status", Constants.ITMP_RETURN_FAILURE);
			map.put("message", "部署失败");

			this.handleException(e);
			return map;
		}
		return map;

	}

//	/**
//	 * 调用dataBus
//	 *
//	 * @param systemId
//	 * @param featureIds
//	 * @param versionInfo
//	 * @param systemPackageName
//	 * @param subSystemList
//	 */
//	@RequestMapping(value = "itmgrToOps")
//	public void versionToOperation(Long systemId, Long[] featureIds, VersionInfo versionInfo,
//								   String systemPackageName, List<Map<String, Object>> subSystemList, String runJobId,Map<String,Object> autoMap) {
//		try {
//			if (featureIds != null && featureIds.length > 0) {
//				TblSystemInfo system = systemInfoService.findById(systemId);
//				Map<String, Object> dataResult = iDeployService.
//						pushData(system, featureIds, versionInfo, systemPackageName, subSystemList);
//				String result = JSON.toJSONString(dataResult, SerializerFeature.WriteDateUseDateFormat);
//				redisUtils.set(versionInfo.getVersionNumber(), autoMap);

//				DataBusUtil.send(databusccName,versionInfo.getVersionNumber(),result);
//			}
//		} catch (Exception e) {
//			log.error("databus调用异常，databus地址为:{}", e);
//			log.error(e.getMessage(), e);
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//	}

	/**
	 * 调用dataBus
	 *
	 * @param systemId 系统id
	 * @param featureIds 开发任务id
	 * @param versionInfo 部署信息对象
	 * @param systemPackageName jar包名称
	 * @param subSystemList 部署对象参数
	 * @param autoMap 回调参数
	 */
	@RequestMapping(value = "itmgrToOps")
	public void versionToOperation(Long systemId, Long[] featureIds, VersionInfo versionInfo,
								   String systemPackageName, List<Map<String, Object>> subSystemList, String runJobId,Map<String,Object> autoMap) {
		try {

			if (featureIds != null && featureIds.length > 0) {
				TblSystemInfo system = systemInfoService.findById(systemId);
				Map<String, Object> dataResult = iDeployService.
						pushData(system, featureIds, versionInfo, systemPackageName, subSystemList);
				String result = JSON.toJSONString(dataResult, SerializerFeature.WriteDateUseDateFormat);
				redisUtils.set(versionInfo.getVersionNumber(), autoMap);
				log.info("开始调用"+dataBusOperationname);
				HttpResponse response = dataBusUtil.postDataBus(dataBusOperationname,result);

				System.out.println("调用结束");
				log.info("已经进来");
			}
		} catch (Exception e) {
			log.error("databus调用异常，databus地址为:{}", e);
			log.error(e.getMessage(), e);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}



	/**
	 * 处理自动化运维平台发布返回结果
	 * @param publishResult
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "updateCommissioningResultData")
	public void updateCommissioningResultData(@RequestBody String publishResult,HttpServletResponse response) {
		Map<String, Object> head = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		log.info("发布系统版本返回结果" + publishResult);
		try {
			if (!StringUtils.isBlank(publishResult)) {
				Map<String, Object> data = JSON
						.parseObject(JSON.parseObject(publishResult).get("requestBody").toString());
				iDeployService.updateCommissioningResultData(data);

				map.put("consumerSeqNo", "itmgr");
				map.put("status", 0);
				map.put("seqNo", "");
				map.put("providerSeqNo", "");
				map.put("esbCode", "");
				map.put("esbMessage", "");
				map.put("appCode", "0");
				map.put("appMessage", "");
				head.put("responseHead", map);

				PrintWriter writer = response.getWriter();
				writer.write(JSON.toJSONString(head));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("返回信息处理失败" + ":" + e.getMessage(), e);
		}
	}

	/**
	 *  通过制品id获取投产窗口
	 * @author weiji
	 * @param artifactids 制品包ids
	 * @return Map<String, Object> windows：List<TblCommissioningWindow> 投产窗口集合
	 */
	@RequestMapping(value = "getWindowsByartId", method = RequestMethod.POST)
	public Map<String, Object> getWindowsByartId(String artifactids) {
		try {
			Map<String, Object> map = new HashMap<>();
			List<TblCommissioningWindow> windows = iDeployService.getWindowsByartId(artifactids);
			map.put("windows", windows);
			return map;
		} catch (Exception e) {
			this.handleException(e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 通过制品id获取关联开发任务
	 * @author weiji
	 * @param artifactids 制品包ids
	 * @return Map<String, Object> rows：List<DevTaskVo> ，total：总共数目
	 */
	@RequestMapping(value = "getReqFeaByartId", method = RequestMethod.POST)
	public Map<String, Object> getReqFeaByartId(String artifactids) {
		try {
			Map<String, Object> map = new HashMap<>();
			List<DevTaskVo> tblRequirementFeatures = iDeployService.getReqFeaByartId(artifactids);
			map.put("rows", tblRequirementFeatures);
			map.put("total", tblRequirementFeatures.size());
			return map;
		} catch (Exception e) {
			this.handleException(e);
			throw new RuntimeException(e);
		}
	}

	/**
	 *  继续获取日志
	 * @param url
	 * @param toolId 工具id
	 * @return Map<String, Object> message：日志信息
	 */
	@RequestMapping(value = "goContinueLog", method = RequestMethod.POST)
	public Map<String, Object> goContinueLog(String url,String toolId) {
		try {
			Map<String, Object> map = new HashMap<>();
			String message=iDeployService.goContinueLog(url,toolId);
			map.put("message", message);

			return map;
		} catch (Exception e) {
			this.handleException(e);
			throw new RuntimeException(e);
		}
	}


	/**
	 * 获取投产窗口
	 * @author weiji
	 * @param systemId 系统id
	 * @return Map<String, Object> windows：List<TblCommissioningWindow>
	 */

	@RequestMapping(value = "getWindowsLimit", method = RequestMethod.POST)
	public Map<String, Object> getWindowsBySystemId(long systemId) {
		try {
			Map<String, Object> map = new HashMap<>();

			List<TblCommissioningWindow> resultList = iDeployService.getWindowsLimit(systemId);

			map.put("windows", resultList);

			return map;
		} catch (Exception e) {
			this.handleException(e);
			throw new RuntimeException(e);
		}
	}
	/**
	 *  获取冲刺信息
	 * @author weiji
	 * @param systemId 系统id
	 * @return Map<String, Object> sprintInfos：List<Map<String, Object>>
	 */


	@RequestMapping(value = "getSprintBySystemId", method = RequestMethod.POST)
	public Map<String, Object> getSprintBySystemId(long systemId) {
		try {
			Map<String, Object> map = new HashMap<>();
			List<TblSprintInfo> sprintInfoList = devTaskService.getSprintBySystemIdLimit(systemId);
			List<Map<String, Object>> result = istructureService.detailSprint(sprintInfoList, systemId);
			map.put("sprintInfos",result);
			return map;
		} catch (Exception e) {
			this.handleException(e);
			throw new RuntimeException(e);
		}
	}


	/**
	 *  获取部署人员
	 * @author weiji
	 * @param systemId 系统id
	 * @return Map<String, Object> rows：List<TblSystemDeployeUserConfig> 部署人员，status：状态
	 */

	@RequestMapping(value = "getDeployUsers", method = RequestMethod.POST)
	public Map<String, Object> getDeployUsers(Long systemId) {
		Map<String, Object> map = new HashMap<>();
		try {

			List<TblSystemDeployeUserConfig> tblSystemDeployeUserConfigs=iDeployService.getDeployUsers(systemId);
            map.put("rows",tblSystemDeployeUserConfigs);
            map.put("status",Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			this.handleException(e);
			map.put("status", Constants.ITMP_RETURN_FAILURE);
			map.put("message", "查询失败");
		}
		return map;
	}

	/**
	 * 增加部署人员
	 * @author weiji
	 * @param tblSystemDeployeUserConfig 参数
	 * @return Map<String, Object> status：状态，message：返回信息
	 */

	@RequestMapping(value = "addDeployUser", method = RequestMethod.POST)
	public Map<String, Object> addDeployUser(TblSystemDeployeUserConfig tblSystemDeployeUserConfig,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			iDeployService.addDeployUser(tblSystemDeployeUserConfig,request);
			map.put("status",Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			this.handleException(e);
			map.put("status", Constants.ITMP_RETURN_FAILURE);
			map.put("message", "添加失败");
		}
		return map;
	}
	/**
	 * 更新部署人员
	 * @author weiji
	 * @param tblSystemDeployeUserConfig 参数
	 * @return Map<String, Object> status：状态
	 */
	@RequestMapping(value = "updateDeployUser", method = RequestMethod.POST)
	public Map<String, Object> updateDeployUser(TblSystemDeployeUserConfig tblSystemDeployeUserConfig,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			iDeployService.updateDeployUser(tblSystemDeployeUserConfig,request);
			map.put("status",Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			this.handleException(e);
			map.put("status", Constants.ITMP_RETURN_FAILURE);
			map.put("message", "更新失败");
		}
		return map;
	}
	/**
	 * 删除部署人员
	 * @author weiji
	 * @param id 用户id
	 * @return Map<String, Object> status：状态，message：返回信息
	 */

	@RequestMapping(value = "deleteDeployUser", method = RequestMethod.POST)
	public Map<String, Object> deleteDeployUser(long id,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			iDeployService.deleteDeployUser(id,request);
			map.put("status",Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			this.handleException(e);
			map.put("status", Constants.ITMP_RETURN_FAILURE);
			map.put("message", "删除失败");
		}
		return map;
	}
	/**
	 *  获取部署环境
	 * @author weiji
	 * @param systemId 系统id
	 * @return Map<String, Object>
	 */

    @RequestMapping(value = "getEnvsBySystemId", method = RequestMethod.POST)
    public Map<String, Object> getEnvsBySystemId(long systemId,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<Map<String,Object>> list=   iDeployService.getEnvsBySystemId(systemId);
            map.put("list",list);
        } catch (Exception e) {
            this.handleException(e);
            throw new RuntimeException(e);
        }
        return map;
    }
    
    /**
     * 质量门禁检查
     * @param artifactIds
     * @param request
     * @return
     */
	@RequestMapping(value = "checkQualityGate", method = RequestMethod.POST)
	public Map<String, Object> checkQualityGate(String artifactIds,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<TblArtifactInfo> artifactList = packageService.getPackageListById(artifactIds);
			List<TblArtifactQualityDetail> qualityDetailList = packageService.getArtifactQualityDetailList(artifactIds);
			List<TblArtifactInfo> forbiddenList = new ArrayList<TblArtifactInfo>();
			List<TblArtifactInfo> warningList = new ArrayList<TblArtifactInfo>();
			for (TblArtifactInfo tblArtifactInfo : artifactList) {
				boolean existWarning = false;
				for (TblArtifactQualityDetail tblArtifactQualityDetail : qualityDetailList) {
					if (tblArtifactInfo.getId().equals(tblArtifactQualityDetail.getArtifactId())) {
						if (tblArtifactQualityDetail.getQualityMetricStatus() == 2) {
							forbiddenList.add(tblArtifactInfo);
							break;
						}
						if (tblArtifactQualityDetail.getQualityMetricStatus() == 3 && !existWarning) {
							warningList.add(tblArtifactInfo);
							existWarning = true;
						}
					}
				}
			}
			map.put("forbiddenList", forbiddenList);
			map.put("warningList", warningList);
			map.put("status",Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			this.handleException(e);
			map.put("status", Constants.ITMP_RETURN_FAILURE);
			map.put("message", "质量门禁检查失败");
		}
		return map;
	}


	public void detailAutoLog(Map<String,Object> map) {
		try {
			istructureService.detailAutoLog(map);
		} catch (Exception e) {
			this.handleException(e);
			throw new RuntimeException(e);
		}
	}

	public void handleException(Exception e) {
		e.printStackTrace();
		log.error(e.getMessage(), e);
	}
}
