package cn.pioneeruniverse.dev.notify;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.StringUtil;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.nexus.NexusAssetBO;
import cn.pioneeruniverse.common.nexus.NexusSearchVO;
import cn.pioneeruniverse.common.nexus.NexusUtil;
import cn.pioneeruniverse.common.sonar.bean.SonarQubeException;
import cn.pioneeruniverse.common.utils.JsonUtil;
import cn.pioneeruniverse.common.utils.S3Util;
import cn.pioneeruniverse.dev.controller.DeployController;
import cn.pioneeruniverse.dev.controller.DevTaskController;
import cn.pioneeruniverse.dev.dao.mybatis.TblCommissioningWindowMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDevTaskMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDevTaskScmFileMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDevTaskScmGitFileMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblProjectInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSprintInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemJenkinsJobRunMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemModuleJenkinsJobRunMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemSonarMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblUserInfoMapper;
import cn.pioneeruniverse.dev.entity.TblArtifactInfo;
import cn.pioneeruniverse.dev.entity.TblCommissioningWindow;
import cn.pioneeruniverse.dev.entity.TblSprintInfo;
import cn.pioneeruniverse.dev.entity.TblSystemInfo;
import cn.pioneeruniverse.dev.entity.TblSystemJenkins;
import cn.pioneeruniverse.dev.entity.TblSystemJenkinsJobRun;
import cn.pioneeruniverse.dev.entity.TblSystemJenkinsJobRunStage;
import cn.pioneeruniverse.dev.entity.TblSystemModule;
import cn.pioneeruniverse.dev.entity.TblSystemModuleJenkinsJobRun;
import cn.pioneeruniverse.dev.entity.TblSystemScm;
import cn.pioneeruniverse.dev.entity.TblSystemSonar;
import cn.pioneeruniverse.dev.entity.TblToolInfo;
import cn.pioneeruniverse.dev.entity.VersionInfo;
import cn.pioneeruniverse.dev.feignInterface.DevManageToSystemInterface;
import cn.pioneeruniverse.dev.service.AutomatTest.AutomatTestService;
import cn.pioneeruniverse.dev.service.build.IJenkinsBuildService;
import cn.pioneeruniverse.dev.service.deploy.IDeployService;
import cn.pioneeruniverse.dev.service.devtask.DevTaskService;
import cn.pioneeruniverse.dev.service.packages.PackageService;
import cn.pioneeruniverse.dev.service.structure.IStructureService;
/**
 * ???????????????????????????
 * @author weiji
 *
 */
@RestController
@RequestMapping("notify")
@EnableAspectJAutoProxy(proxyTargetClass = true,exposeProxy = true)
public class StructureNotify {
	/**
	 * ?????????????????????????????????
	 * architectureType:????????????????????????,
	 * moduleRunJson??? ?????????sonar???????????????projectkey(????????????????????????sonar??????,????????????projectkey??????soanr???????????????????????????sonar????????????),
	 * startDate:??????????????????,endDate:??????????????????,
	 * scheduled:????????????(??????ture????????????????????????false????????????),moduleRunIds:tbl_system_module_jenkins_job_run ???id,
	 * jenkinsToolId:jenkins??????(tbl_tool_info???id),nowJobNumber:jenkins????????????????????????????????????,
	 * jobNumber:???????????????????????????????????????jenkins???????????????????????????????????????(???????????????????????????,jenkins???????????????jobNumber????????????,??????nowJobNumber),
	 * systemJenkinsId:tbl_system_jenkins???id,systemId:??????id,reqFeatureqIds:????????????id,systemScmId:tbl_system_scm???id
	 * userName:????????????????????????,userId:??????id,userAccount:????????????,moduleList:??????????????????????????????id,
	 * envType:????????????????????????key,envName:????????????????????????????????????,runId:tbl_system_jenkins_job_run???id
	 */
	private final static Logger callbackLog = LoggerFactory.getLogger(StructureNotify.class);
	@Autowired
	private IStructureService iStructureService;
	@Autowired
	private IJenkinsBuildService jenkinsBuildService;
	@Autowired
	private DevTaskController devTaskController;
	@Autowired
	private DevTaskService devTaskService;
	@Autowired
	private DeployController deployController;
	@Autowired
	private IDeployService deployService;
	@Autowired
	private AutomatTestService automatTestService;
	@Autowired
	private PackageService packageService;
    @Autowired
	private DevManageToSystemInterface devManageToSystemInterface;
    @Autowired
	private TblProjectInfoMapper tblProjectInfoMapper;
    @Autowired
	private TblUserInfoMapper tblUserInfoMapper;
    @Autowired
	private TblSprintInfoMapper tblSprintInfoMapper;
    @Autowired
	private TblDevTaskMapper tblDevTaskMapper;
    @Autowired
	private TblDevTaskScmGitFileMapper tblDevTaskScmGitFileMapper;
    @Autowired
	private TblDevTaskScmFileMapper tblDevTaskScmFileMapper;
    @Autowired
	private TblCommissioningWindowMapper tblCommissioningWindowMapper;
    @Autowired
    private TblSystemModuleJenkinsJobRunMapper tblSystemModuleJenkinsJobRunMapper;
    @Autowired
    private TblSystemSonarMapper tblSystemSonarMapper;
    @Autowired
	private S3Util s3Util;
	@Value("${s3.logBucket}")
	private String logBucket;
	@Autowired
	private TblSystemJenkinsJobRunMapper tblSystemJenkinsJobRunMapper;


    private static ExecutorService threadPool;
    {

        threadPool = Executors.newFixedThreadPool(20);

    }
	/**
	 * ??????????????????
	 * @author weiji
	 * @param jsonMap ????????????
	 * @return Map<String, Object>
	 */

	@RequestMapping(value = "callBackJenkinsLog", method = RequestMethod.POST)
	public void callBackJenkinsLog(String jsonMap) throws IOException, InterruptedException, SonarQubeException {

		try {
			Map<String, Object> backMap = JSON.parseObject(jsonMap, Map.class);
			Timestamp startTime=formateDate(backMap);
			Timestamp endTime=formateEndDate(backMap);
			String runId = backMap.get("runId").toString();//jobrunId
			String systemJenkinsId = backMap.get("systemJenkinsId").toString();//tbl_system_jenkins id
			String moduleRunJson = backMap.get("moduleRunJson").toString();//modulerunId
			String jenkinsToolId = backMap.get("jenkinsToolId").toString();//jenkins ??????id
			String scheduled = null;
			//????????????????????????
			if (backMap.get("scheduled") != null) {//?????????????????????
				scheduled = backMap.get("scheduled").toString();
			}
			List<Integer> moduleRunIds = new ArrayList<>();
			if (backMap.get("moduleRunIds") != null) {
				moduleRunIds = JSON.parseObject(backMap.get("moduleRunIds").toString(),List.class);
			}
			String systemScmId = backMap.get("systemScmId").toString();//tbl_system_scm ??????
			String systemId = backMap.get("systemId").toString();
			// ?????????????????????????????????
			TblSystemJenkinsJobRun jorRun = iStructureService.selectBuildMessageById(Long.parseLong(runId));
			String envflag = "";
			if (jorRun.getEnvironmentType() != null) {
				envflag = String.valueOf(jorRun.getEnvironmentType());
			}
			List<TblSystemModule> moduleLog=iStructureService.selectSystemModule(Long.parseLong(systemId));
			TblSystemJenkins tblSystemJenkinsLog = iStructureService.selectSystemJenkinsById(systemJenkinsId);
			TblToolInfo jenkinsTool = iStructureService.geTblToolInfo(Integer.parseInt(jenkinsToolId));
			String nowJobNumber=backMap.get("nowJobNumber").toString();//jenkins?????????job????????????
			String jobNumberTemp=backMap.get("jobNumber").toString();//job????????????
			if (scheduled != null && scheduled.equals("true") && !jobNumberTemp.equals(nowJobNumber)) {
            	// ?????????????????????????????????
				TblSystemJenkinsJobRun ttr = new TblSystemJenkinsJobRun();
				ttr.setSystemJenkinsId(jorRun.getSystemJenkinsId());
				ttr.setSystemId(jorRun.getSystemId());
				ttr.setJobName(jorRun.getJobName());
				ttr.setRootPom(".");
				ttr.setBuildStatus(1);// ??????
				ttr.setStatus(1);
				ttr.setEndDate(endTime);
				ttr.setCreateDate(startTime);
				ttr.setStartDate(startTime);
				ttr.setLastUpdateDate(endTime);
				ttr.setEnvironmentType(jorRun.getEnvironmentType());
				ttr.setGoalsOptions("mvn clean install -Dmaven.test.skip=true");
				ttr.setCreateType(1);
				ttr.setJobRunNumber(Integer.parseInt(nowJobNumber));
				ttr.setJobType(1);
				ttr.setStartDate(startTime);
				Thread.sleep(8000);
				String jobName = jorRun.getJobName();

//				Map<String, String> result = jenkinsBuildService.getLastBuildLog(jenkinsTool, tblSystemJenkinsLog,
//						jobName);

                Map<String, String> result = jenkinsBuildService.getBuildLogByNumber(jenkinsTool,tblSystemJenkinsLog,jobName,Integer.parseInt(nowJobNumber),moduleLog);
				String timeStatus = result.get("status");//????????????
				String timeLog = result.get("log");//????????????
				ttr.setBuildLogs(timeLog);
				ttr.setBuildStatus(Integer.parseInt(timeStatus));
				
				Map<String, Object> saveDataMap = new HashMap<String, Object>();
				iStructureService.getJenkinsStageLog(ttr, jenkinsTool, tblSystemJenkinsLog, jobName, saveDataMap);
				String moduleJson=backMap.get("moduleJson").toString();
				if (envflag.equals(Constants.PRDIN) || envflag.equals(Constants.PRDOUT)) {//????????????
					iStructureService.getModuleRunCallBack(moduleRunIds, endTime,startTime,moduleJson,saveDataMap);
				} else {
					if(backMap.get("sonarToolId")!=null && timeStatus.equals("2")) {//sonar??????id
						String sonarToolId = backMap.get("sonarToolId").toString();
						iStructureService.sonarDetail(moduleRunJson, endTime, sonarToolId, "2",startTime,moduleJson,saveDataMap);// 2???????????????
					}else{ //??????soanr????????????????????????
						iStructureService.getModuleRunCallBack(moduleRunIds, endTime,startTime,moduleJson, saveDataMap);
					}
				}
				
				((StructureNotify)AopContext.currentProxy()).saveCallBackData(backMap, ttr, "2", saveDataMap, 1);
				callbackLog.info("????????????????????????");

			} else {
				TblSystemJenkinsJobRun tblSystemJenkinsJobRun = new TblSystemJenkinsJobRun();
				tblSystemJenkinsJobRun.setSystemId(Long.parseLong(systemId));
				tblSystemJenkinsJobRun.setStartDate(startTime);
				tblSystemJenkinsJobRun.setEndDate(endTime);
				tblSystemJenkinsJobRun.setSystemJenkinsId(Long.parseLong(systemJenkinsId));
				tblSystemJenkinsJobRun.setId(Long.parseLong(runId));
				tblSystemJenkinsJobRun.setLastUpdateDate(endTime);
				
				String status = "";
				// ??????jenkinsTool ???jobName
				Thread.sleep(8000);
				String jobName = jorRun.getJobName();

				Map<String, String> result=new HashMap<>();
				if(backMap.get("jobNumber")!=null){
					int jobNumber=Integer.parseInt(backMap.get("jobNumber").toString());
					result=jenkinsBuildService.getBuildLogByNumber(jenkinsTool,tblSystemJenkinsLog,jobName,jobNumber,moduleLog);
				}else{
					result = jenkinsBuildService.getLastBuildLog(jenkinsTool, tblSystemJenkinsLog, jobName,moduleLog);
				}
				status = result.get("status");
				String log = result.get("log");
				log=iStructureService.detailByteBylog(log,Constants.MAX_ALLOW_PACKET);
				tblSystemJenkinsJobRun.setBuildLogs(log);
				tblSystemJenkinsJobRun.setBuildStatus(Integer.parseInt(status));
				
				
				Map<String, Object> saveDataMap = new HashMap<String, Object>();
				if (scheduled != null && scheduled.equals("true")) {//???????????????
					tblSystemJenkinsJobRun.setCreateDate(startTime);
				} else {
					if (backMap.get("moduleList") != null && status.equals("2")) {
						List<String> moduleList = (List<String>) backMap.get("moduleList");
						saveDataMap.put("moduleList", moduleList);
					}
					TblSystemScm tblSystemScm = iStructureService.getTblsystemScmById(Long.parseLong(systemScmId));
					tblSystemScm.setBuildStatus(1);
					saveDataMap.put("tblSystemScm", tblSystemScm);
				}
				
				iStructureService.getJenkinsStageLog(jorRun, jenkinsTool, tblSystemJenkinsLog, jobName, saveDataMap);
                String moduleJson=backMap.get("moduleJson").toString();
				if ((backMap.get("sonarflag") != null && backMap.get("sonarflag").toString().equals("1")) || (scheduled != null && scheduled.equals("true")) ) {//????????????sonar
					if(backMap.get("sonarToolId")!=null && status.equals("2")) {
						String sonarToolId = backMap.get("sonarToolId").toString();
						iStructureService.sonarDetail(moduleRunJson, endTime, sonarToolId, "1", startTime,moduleJson, saveDataMap);
					}else{
						//sonar?????????????????????module??????
						iStructureService.getModuleRunCallBack(moduleRunIds, endTime,startTime,moduleJson, saveDataMap);
					}
				} else {
					iStructureService.getModuleRunCallBack(moduleRunIds, endTime,startTime,moduleJson, saveDataMap);
				}
				((StructureNotify)AopContext.currentProxy()).saveCallBackData(backMap, tblSystemJenkinsJobRun,"1", saveDataMap, 2);
				
				//????????????
				sendMessage(tblSystemJenkinsJobRun,backMap,"1");
				
				callbackLog.info("??????????????????");
			}
		} catch (Exception e) {
			this.handleException(e);
		}

	}

	/**
	 * ???????????????????????????????????????????????????????????????????????????????????????????????????
	 * @param backMap
	 * @param tblSystemJenkinsJobRun
	 * @param flag
	 * @param saveDataMap
	 * @param type
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public void saveCallBackData(Map<String,Object> backMap, TblSystemJenkinsJobRun tblSystemJenkinsJobRun, 
			String flag, Map<String, Object> saveDataMap, Integer type) throws Exception {
		//TODO
		callbackLog.info("callBackJenkinsLog????????????>>>>>transaction??????");
		String timeLog = tblSystemJenkinsJobRun.getBuildLogs();
		tblSystemJenkinsJobRun.setBuildLogs("");//???????????????s3
		if (type == 1) {//callBackJenkinsLog????????????????????????
			long jobid = iStructureService.insertJenkinsJobRun(tblSystemJenkinsJobRun);
			String key=	s3Util.putObjectLogs(logBucket,jobid+"jobRun",timeLog);
			String bucketName=logBucket;
			tblSystemJenkinsJobRun.setBuildLogs(key+","+bucketName);
			tblSystemJenkinsJobRunMapper.updateById(tblSystemJenkinsJobRun);
			
			List<TblSystemJenkinsJobRunStage> saveStageList = (List<TblSystemJenkinsJobRunStage>)saveDataMap.get("saveStageList");
			if (saveStageList != null) {
				saveStageList.forEach(stage->{
					stage.setSystemJenkinsJobRunId(jobid);
				});
				iStructureService.saveJenkinsStageLog(saveStageList);//??????StageView?????????
			}
			//sonarDetail()?????????????????????
			List<TblSystemModuleJenkinsJobRun> saveJobRunList = (List<TblSystemModuleJenkinsJobRun>)saveDataMap.get("saveJobRunList");
			if (saveJobRunList != null) {
				iStructureService.saveModuleRunCallBack(jobid, flag, saveJobRunList);
			}
		} else if (type == 2) {//callBackJenkinsLog??????????????????
			String key=	s3Util.putObjectLogs(logBucket,tblSystemJenkinsJobRun.getId()+"jobRun",timeLog);
			String bucketName=logBucket;
			tblSystemJenkinsJobRun.setBuildLogs(key+","+bucketName);
			//????????????????????????
			iStructureService.updateJobRun(tblSystemJenkinsJobRun);
			//????????????????????????????????????
			if (tblSystemJenkinsJobRun.getBuildStatus() == 2) {
				detailPackage(backMap, tblSystemJenkinsJobRun.getId(), tblSystemJenkinsJobRun.getSystemId());
			}
			
			List<String> moduleList = (List<String>)saveDataMap.get("moduleList");
			if (moduleList != null) {
				iStructureService.updateModuleInfoFristCompile(moduleList);
			}
			TblSystemScm tblSystemScm = (TblSystemScm)saveDataMap.get("tblSystemScm");
			if (tblSystemScm != null) {// ??????scm?????????
				iStructureService.updateSystemScmBuildStatus(tblSystemScm);// 1?????? 2?????????
			}
			List<TblSystemJenkinsJobRunStage> saveStageList = (List<TblSystemJenkinsJobRunStage>)saveDataMap.get("saveStageList");
			if (saveStageList != null) {
				iStructureService.saveJenkinsStageLog(saveStageList);//??????StageView?????????
			}
			List<TblSystemModuleJenkinsJobRun> saveJobRunList = (List<TblSystemModuleJenkinsJobRun>)saveDataMap.get("saveJobRunList");
			if (saveJobRunList != null) {
				iStructureService.saveModuleRunCallBack(tblSystemJenkinsJobRun.getId(), flag, saveJobRunList);
			}
		} else if (type == 3) {//callBackManualJenkins??????????????????
			String key=	s3Util.putObjectLogs(logBucket,tblSystemJenkinsJobRun.getId()+"jobRun",timeLog);
		    String bucketName=logBucket;
		    tblSystemJenkinsJobRun.setBuildLogs(key+","+bucketName);
			iStructureService.updateJobRun(tblSystemJenkinsJobRun);//??????id????????????
			
			TblSystemJenkins tblSystemJenkins = (TblSystemJenkins)saveDataMap.get("tblSystemJenkins");
			if (tblSystemJenkins != null) {// ??????
				iStructureService.updateJenkins(tblSystemJenkins);
			}
			TblSystemSonar tblSystemSonar = (TblSystemSonar)saveDataMap.get("tblSystemSonar");
			if (tblSystemSonar != null) {// ??????
				tblSystemSonarMapper.updateById(tblSystemSonar);
			}
			List<TblSystemJenkinsJobRunStage> saveStageList = (List<TblSystemJenkinsJobRunStage>)saveDataMap.get("saveStageList");
			if (saveStageList != null) {
				iStructureService.saveJenkinsStageLog(saveStageList);//??????StageView?????????
			}
			List<TblSystemModuleJenkinsJobRun> saveJobRunList = (List<TblSystemModuleJenkinsJobRun>)saveDataMap.get("saveJobRunList");
			if (saveJobRunList != null) {
				iStructureService.saveModuleRunCallBack(tblSystemJenkinsJobRun.getId(), flag, saveJobRunList);
			}
		} else if (type == 4) {//callBackManualDepolyJenkins??????????????????
			String key=	s3Util.putObjectLogs(logBucket,tblSystemJenkinsJobRun.getId()+"jobRun",timeLog);
			String bucketName=logBucket;
			tblSystemJenkinsJobRun.setBuildLogs(key+","+bucketName);
			iStructureService.updateJobRun(tblSystemJenkinsJobRun);// ??????id????????????
			
			TblSystemJenkins tblSystemJenkins = (TblSystemJenkins)saveDataMap.get("tblSystemJenkins");
			if (tblSystemJenkins != null) {// ??????
				iStructureService.updateJenkins(tblSystemJenkins);
			}
			List<TblSystemJenkinsJobRunStage> saveStageList = (List<TblSystemJenkinsJobRunStage>)saveDataMap.get("saveStageList");
			if (saveStageList != null) {
				iStructureService.saveJenkinsStageLog(saveStageList);//??????StageView?????????
			}
			TblSystemModuleJenkinsJobRun tmjr = (TblSystemModuleJenkinsJobRun)saveDataMap.get("tblSystemModuleJenkinsJobRun");
			if (tmjr != null) {
				iStructureService.updateModuleJonRun(tmjr);
			}
			// ??????????????????
			Map<String, String> taskUpdateMap = (Map<String, String>)saveDataMap.get("taskUpdateMap");
			if (taskUpdateMap != null && taskUpdateMap.size() > 0) {
				String reqFeatureqIds = taskUpdateMap.get("reqFeatureqIds");
				String taskEnvType = taskUpdateMap.get("taskEnvType");
				String sendTask = taskUpdateMap.get("sendTask");
				String status = taskUpdateMap.get("status");
				String json = taskUpdateMap.get("json");
				devTaskController.updateDeployStatus(reqFeatureqIds, taskEnvType, sendTask, Integer.parseInt(status));
				devTaskService.updateReqFeatureTimeTraceForDeploy(json);
			}
			
		} else if (type == 5) {//callBackAutoDepolyJenkins????????????????????????
			long jobid = iStructureService.insertJenkinsJobRun(tblSystemJenkinsJobRun);
			String key=	s3Util.putObjectLogs(logBucket,jobid+"jobRun",timeLog);
			String bucketName=logBucket;
			tblSystemJenkinsJobRun.setBuildLogs(key+","+bucketName);
			tblSystemJenkinsJobRunMapper.updateById(tblSystemJenkinsJobRun);
			
			Map<String,Object> autoMap = (Map<String,Object>)saveDataMap.get("autoMap");
			if (autoMap != null) {
				autoMap.put("jobRunId",jobid);
				callbackLog.info("??????jorunid=" + jobid + "-" + tblSystemJenkinsJobRun.getId());
			}
			List<TblSystemJenkinsJobRunStage> saveStageList = (List<TblSystemJenkinsJobRunStage>)saveDataMap.get("saveStageList");
			if (saveStageList != null) {
				saveStageList.forEach(stage->{
					stage.setSystemJenkinsJobRunId(jobid);
				});
				iStructureService.saveJenkinsStageLog(saveStageList);//??????StageView?????????
			}
			List<TblSystemModuleJenkinsJobRun> updateModuleStatusList = (List<TblSystemModuleJenkinsJobRun>)saveDataMap.get("updateModuleStatusList");
			if (updateModuleStatusList != null) {
				for (TblSystemModuleJenkinsJobRun bean : updateModuleStatusList) {
					tblSystemModuleJenkinsJobRunMapper.updateByPrimaryKeySelective(bean);
				}
			}
			List<TblSystemModuleJenkinsJobRun> insertJobRunList = (List<TblSystemModuleJenkinsJobRun>)saveDataMap.get("insertJobRunList");
			if (insertJobRunList != null) {
				for (TblSystemModuleJenkinsJobRun moduleJobrun : insertJobRunList) {
					// ????????????
					moduleJobrun.setSystemJenkinsJobRun(jobid);
					moduleJobrun.setId(null);
					iStructureService.insertJenkinsModuleJobRun(moduleJobrun);
				}
			}
		} else if (type == 6) {//callBackAutoDepolyJenkins??????????????????
			// ??????id????????????
			String key=	s3Util.putObjectLogs(logBucket,tblSystemJenkinsJobRun.getId()+"jobRun",timeLog);
			String bucketName=logBucket;
			tblSystemJenkinsJobRun.setBuildLogs(key+","+bucketName);
			iStructureService.updateJobRun(tblSystemJenkinsJobRun);
			
			List<String> moduleList = (List<String>)saveDataMap.get("moduleList");
			if (moduleList != null) {
				iStructureService.updateModuleInfoFristCompile(moduleList);
			}
			TblSystemScm tblSystemScm = (TblSystemScm)saveDataMap.get("tblSystemScm");
			if (tblSystemScm != null) {// ??????scm?????????
				iStructureService.updateSystemScmBuildStatus(tblSystemScm);// 1?????? 2?????????
			}
			List<TblSystemJenkinsJobRunStage> saveStageList = (List<TblSystemJenkinsJobRunStage>)saveDataMap.get("saveStageList");
			if (saveStageList != null) {
				iStructureService.saveJenkinsStageLog(saveStageList);//??????StageView?????????
			}
			List<TblSystemModuleJenkinsJobRun> updateModuleStatusList = (List<TblSystemModuleJenkinsJobRun>)saveDataMap.get("updateModuleStatusList");
			if (updateModuleStatusList != null) {
				for (TblSystemModuleJenkinsJobRun bean : updateModuleStatusList) {
					tblSystemModuleJenkinsJobRunMapper.updateByPrimaryKeySelective(bean);
				}
			}
			List<TblSystemModuleJenkinsJobRun> saveJobRunList = (List<TblSystemModuleJenkinsJobRun>)saveDataMap.get("saveJobRunList");
			if (saveJobRunList != null) {
				iStructureService.saveModuleRunCallBack(tblSystemJenkinsJobRun.getId(), flag, saveJobRunList);
			}
			// ??????????????????
			Map<String, String> taskUpdateMap = (Map<String, String>)saveDataMap.get("taskUpdateMap");
			if (taskUpdateMap != null && taskUpdateMap.size() > 0) {
				String reqFeatureqIds = taskUpdateMap.get("reqFeatureqIds");
				String envType = taskUpdateMap.get("envType");
				String sendTask = taskUpdateMap.get("sendTask");
				String status = taskUpdateMap.get("status");
				String json = taskUpdateMap.get("json");
				devTaskController.updateDeployStatus(reqFeatureqIds, envType, sendTask, Integer.parseInt(status));
				devTaskService.updateReqFeatureTimeTraceForDeploy(json);
			}
			
		} else if (type == 7) {//callBackPackageDepolyJenkins????????????????????????
			String key=	s3Util.putObjectLogs(logBucket,tblSystemJenkinsJobRun.getId()+"jobRun",timeLog);
		    String bucketName=logBucket;
			tblSystemJenkinsJobRun.setBuildLogs(key+","+bucketName);
			iStructureService.updateJobRun(tblSystemJenkinsJobRun);
			
			TblSystemJenkins tblSystemJenkins = (TblSystemJenkins)saveDataMap.get("tblSystemJenkins");
			if (tblSystemJenkins != null) {// ??????
				iStructureService.updateJenkins(tblSystemJenkins);
			}
			List<TblSystemJenkinsJobRunStage> saveStageList = (List<TblSystemJenkinsJobRunStage>)saveDataMap.get("saveStageList");
			if (saveStageList != null) {
				iStructureService.saveJenkinsStageLog(saveStageList);//??????StageView?????????
			}
			List<TblSystemModuleJenkinsJobRun> jobRunList = (List<TblSystemModuleJenkinsJobRun>)saveDataMap.get("jobRunList");
			if (jobRunList != null) {
				for (TblSystemModuleJenkinsJobRun tmjr : jobRunList) {
					iStructureService.updateModuleJonRun(tmjr);
				}
			}
			// ??????????????????
			Map<String, String> taskUpdateMap = (Map<String, String>)saveDataMap.get("taskUpdateMap");
			if (taskUpdateMap != null && taskUpdateMap.size() > 0) {
				String reqFeatureqIds = taskUpdateMap.get("reqFeatureqIds");
				String envType = taskUpdateMap.get("envType");
				String sendTask = taskUpdateMap.get("sendTask");
				String status = taskUpdateMap.get("status");
				String json = taskUpdateMap.get("json");
				devTaskController.updateDeployStatus(reqFeatureqIds, envType, sendTask, Integer.parseInt(status));
				devTaskService.updateReqFeatureTimeTraceForDeploy(json);
			}
		}
		callbackLog.info("callBackJenkinsLog????????????>>>>>transaction??????");
	}



	private Timestamp getStartDate(TblToolInfo jenkinsTool, String jobName) throws Exception {
		return jenkinsBuildService.getJobStartDate(jenkinsTool, jobName);


	}

	/**
	 * ??????????????????
	 * @author weiji
	 * @param jsonMap ????????????
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "callBackManualJenkins", method = RequestMethod.POST)
	public void callBackManualJenkins(String jsonMap) throws Exception {

		try {
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			Map<String, Object> backMap = JSON.parseObject(jsonMap, Map.class);
			long jobRunId = Long.parseLong(backMap.get("jobRunId").toString());
			TblSystemJenkinsJobRun tJobRun = iStructureService.selectBuildMessageById(jobRunId);
			String moduleJobRunId = backMap.get("moduleJobRunId").toString();
			// ??????tblsystemjenkins?????????
			Map<String, Object> sjmap = new HashMap<>();
			sjmap.put("id", backMap.get("systemJenkinsId"));
			List<TblSystemModule> moduleLog=new ArrayList<>();
			TblSystemJenkins tblSystemJenkins = iStructureService.getSystemJenkinsByMap(sjmap).get(0);
			String sonarName = "";
			
			String scheduled = null;
			if (backMap.get("scheduled") == null) {

			} else {
				scheduled = backMap.get("scheduled").toString();
			}

			String status = "";
			Map<String, Object> saveDataMap = new HashMap<String, Object>();
			tblSystemJenkins.setLastUpdateDate(timestamp);
			if (scheduled != null && scheduled.equals("true")) {

			} else {
				tblSystemJenkins.setBuildStatus(1);
			}
			saveDataMap.put("tblSystemJenkins", tblSystemJenkins);
			
			TblToolInfo jenkinsTool = iStructureService.geTblToolInfo(tblSystemJenkins.getToolId());
			TblSystemJenkinsJobRun tblSystemJenkinsJobRun = new TblSystemJenkinsJobRun();
			tblSystemJenkinsJobRun.setSystemId(tblSystemJenkins.getSystemId());
			//tblSystemJenkinsJobRun.setBuildLogs(log);
			tblSystemJenkinsJobRun.setSystemJenkinsId(tblSystemJenkins.getId());
			tblSystemJenkinsJobRun.setId(jobRunId);
			
			String log = "";
			Timestamp startTime= null;
			Timestamp endTime= null;
			if (backMap.get("timeout") != null && backMap.get("timeout").toString().equals("true")) {
				status = backMap.get("status").toString();
				log = backMap.get("log").toString();
			} else {
				Map<String, String> result=new HashMap<>();

				if(backMap.get("jobNumber")!=null){
					int jobNumber=Integer.parseInt(backMap.get("jobNumber").toString());
					result=jenkinsBuildService.getBuildLogByNumber(jenkinsTool,tblSystemJenkins,tblSystemJenkins.getJobName(),jobNumber,moduleLog);

					startTime=jenkinsBuildService.getJobStartDate(jenkinsTool,tblSystemJenkins,tblSystemJenkins.getJobName(),jobNumber);
					endTime=jenkinsBuildService.getJobEndDate(jenkinsTool,tblSystemJenkins,tblSystemJenkins.getJobName(),jobNumber);
				}else {
					result = jenkinsBuildService.getLastBuildLog(jenkinsTool, tblSystemJenkins, tblSystemJenkins.getJobName(),moduleLog);
				}
				status = result.get("status");
				log = result.get("log");
			}
			log=iStructureService.detailByteBylog(log,Constants.MAX_ALLOW_PACKET);
			tblSystemJenkinsJobRun.setBuildStatus(Integer.parseInt(status));
			tblSystemJenkinsJobRun.setBuildLogs(log);
			tblSystemJenkinsJobRun.setEndDate(endTime);
			tblSystemJenkinsJobRun.setStartDate(startTime);
			tblSystemJenkinsJobRun.setLastUpdateDate(endTime);
			
			iStructureService.getJenkinsStageLog(tJobRun, jenkinsTool, tblSystemJenkins, tblSystemJenkins.getJobName(), saveDataMap);
			if (backMap.get("sonarName") == null) {//sonar??????
			} else {
				sonarName = backMap.get("sonarName").toString();
				// ??????sonar????????????
				String tblSystemSonar = backMap.get("tblSystemSonar").toString();
				iStructureService.sonarDetailManual(tblSystemJenkins.getJobName(), tblSystemSonar, timestamp,
						sonarName, moduleJobRunId, "1", saveDataMap);
			}
			((StructureNotify)AopContext.currentProxy()).saveCallBackData(backMap, tblSystemJenkinsJobRun, "1", saveDataMap, 3);

			//????????????
			sendMessage(tblSystemJenkinsJobRun,backMap,"1");
			callbackLog.info("??????????????????");

		} catch (Exception e) {
			this.handleException(e);
			throw e; 
		}

	}
	/**
	 * ??????????????????
	 * @author weiji
	 * @param jsonMap ????????????
	 * @return Map<String, Object>
	 */

	@RequestMapping(value = "callBackManualDepolyJenkins", method = RequestMethod.POST)
	public void callBackManualDepolyJenkins(String jsonMap) throws Exception {

		try {
			Map<String, Object> backMap = JSONObject.parseObject(jsonMap, Map.class);
			String moduleJobRunId = backMap.get("moduleJobRunId").toString();//tbl_system_module_jenkins_job_run ??????
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			String status = "";
			long jobRunId = Long.parseLong(backMap.get("jobRunId").toString());//tbl_system_jenkins_job_run ??????
			TblSystemJenkinsJobRun tJobRun = iStructureService.selectBuildMessageById(jobRunId);
			Map<String, Object> sjmap = new HashMap<>();
			sjmap.put("id", backMap.get("systemJenkinsId"));//tbl_system_jenkins ??????
			List<TblSystemModule> moduleLog=new ArrayList<>();
			TblSystemJenkins tblSystemJenkins = iStructureService.getSystemJenkinsByMap(sjmap).get(0);
			
			// ??????tblsystemjenkins?????????
			String scheduled = null;
			if (backMap.get("scheduled") == null) { //????????????

			} else {
				scheduled = backMap.get("scheduled").toString();
			}

			tblSystemJenkins.setLastUpdateDate(timestamp);
			if (scheduled != null && scheduled.equals("true")) {

			} else {
				tblSystemJenkins.setDeployStatus(1);
			}
			Timestamp startTime= null;
			Timestamp endTime= null;
			Map<String, Object> saveDataMap = new HashMap<String, Object>();
			saveDataMap.put("tblSystemJenkins", tblSystemJenkins);
			
			TblToolInfo jenkinsTool = iStructureService.geTblToolInfo(tblSystemJenkins.getToolId());
			Map<String, String> result =new HashMap<>();
			//????????????????????????????????????????????????
			if(backMap.get("jobNumber")!=null){
				int jobNumber=Integer.parseInt(backMap.get("jobNumber").toString());
				result=jenkinsBuildService.getBuildLogByNumber(jenkinsTool,tblSystemJenkins,tblSystemJenkins.getJobName(),jobNumber,moduleLog);
				startTime=jenkinsBuildService.getJobStartDate(jenkinsTool,tblSystemJenkins,tblSystemJenkins.getJobName(),jobNumber);
				endTime=jenkinsBuildService.getJobEndDate(jenkinsTool,tblSystemJenkins,tblSystemJenkins.getJobName(),jobNumber);
			}else{
				result = jenkinsBuildService.getLastBuildLog(jenkinsTool, tblSystemJenkins,
						tblSystemJenkins.getJobName(),moduleLog);
			}

			TblSystemJenkinsJobRun tblSystemJenkinsJobRun = new TblSystemJenkinsJobRun();
			tblSystemJenkinsJobRun.setSystemId(tblSystemJenkins.getSystemId());
			tblSystemJenkinsJobRun.setStartDate(startTime);
			tblSystemJenkinsJobRun.setEndDate(endTime);
			tblSystemJenkinsJobRun.setSystemJenkinsId(tblSystemJenkins.getId());
			tblSystemJenkinsJobRun.setLastUpdateDate(endTime);
			tblSystemJenkinsJobRun.setId(jobRunId);
			
			status = result.get("status");//????????????
			String log = result.get("log");//????????????
			log=iStructureService.detailByteBylog(log,Constants.MAX_ALLOW_PACKET);
			tblSystemJenkinsJobRun.setBuildLogs(log);
			tblSystemJenkinsJobRun.setBuildStatus(Integer.parseInt(status));
			
			iStructureService.getJenkinsStageLog(tJobRun, jenkinsTool, tblSystemJenkins, tblSystemJenkins.getJobName(), saveDataMap);
			// ??????modulejobrun???
			TblSystemModuleJenkinsJobRun tmjr = new TblSystemModuleJenkinsJobRun();
			tmjr.setLastUpdateDate(timestamp);
			tmjr.setId(Long.parseLong(moduleJobRunId));
			tmjr.setBuildStatus(Integer.parseInt(status));
            //String moduleJson=backMap.get("moduleJson").toString();
			saveDataMap.put("tblSystemModuleJenkinsJobRun", tmjr);
			
			
			// ??????????????????
			Map<String, String> taskUpdateMap = new HashMap<String, String>();
			String reqFeatureqIds = backMap.get("reqFeatureqIds") == null ? null : backMap.get("reqFeatureqIds").toString();//????????????
			String taskEnvType = backMap.get("taskEnvType") == null ? null : backMap.get("taskEnvType").toString();
			if (reqFeatureqIds != null && !reqFeatureqIds.equals("") &&  taskEnvType != null && !taskEnvType.equals("")) {
				String userId = backMap.get("userId").toString();//??????id
				String userName = backMap.get("userName").toString();//????????????
				String userAccount = backMap.get("userAccount").toString();//??????
				Map<String, Object> sendMap = new HashMap<>();
				sendMap.put("userId", userId);
				sendMap.put("userAccount", userAccount);
				sendMap.put("userName", userName);
				String sendTask = JSON.toJSONString(sendMap);
				Map<String, Object> remap = new HashMap<>();
				remap.put("reqFeatureIds", reqFeatureqIds);//????????????id
				remap.put("requirementFeatureFirstTestDeployTime", new Timestamp(new Date().getTime()));
				String json = JsonUtil.toJson(remap);
				taskUpdateMap.put("reqFeatureqIds", reqFeatureqIds);
				taskUpdateMap.put("taskEnvType", taskEnvType);//???????????????????????????
				taskUpdateMap.put("sendTask", sendTask);
				taskUpdateMap.put("status", status);
				taskUpdateMap.put("json", json);
			}
			saveDataMap.put("taskUpdateMap", taskUpdateMap);
			
			
			
			((StructureNotify)AopContext.currentProxy()).saveCallBackData(backMap, tblSystemJenkinsJobRun, "", saveDataMap, 4);
			
			//????????????
			sendMessage(tblSystemJenkinsJobRun,backMap,"2");
			callbackLog.info("??????????????????");

		} catch (Exception e) {
			this.handleException(e);
			throw e;
		}

	}

	/**
	 * ??????????????????
	 * @author weiji
	 * @param jsonMap ????????????
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "callBackAutoDepolyJenkins", method = RequestMethod.POST)
	public void callBackAutoDepolyJenkins(String jsonMap) throws IOException, InterruptedException, SonarQubeException {

		try {
			//????????????????????????message
			boolean messageflag=true;
		    Map<String, Object> backMap = JSON.parseObject(jsonMap, Map.class);
			Timestamp startTime=formateDate(backMap);
			Timestamp endTime=formateEndDate(backMap);
			String jobNumber=backMap.get("jobNumber").toString();//????????????
			String runId = backMap.get("runId").toString();//tbl_system_jenkins_job_run ??????
			String systemJenkinsId = backMap.get("systemJenkinsId").toString();//tbl_system_jenkins ??????
			String jenkinsToolId = backMap.get("jenkinsToolId").toString();//tbl_tool_info ??????
			String envType = null;
			//??????????????????
			if (backMap.get("envType") != null) {//????????????
				envType = backMap.get("envType").toString();
			}
			List<Integer> moduleRunIds = new ArrayList<>();
			//???????????????ids
			if (backMap.get("moduleRunIds") != null) {//tbl_system_module_jenkins_job_run ??????
				moduleRunIds = JSON.parseObject(backMap.get("moduleRunIds").toString(),List.class);
			}
			String moduleRunJson = backMap.get("moduleRunJson").toString();
			String systemScmId = backMap.get("systemScmId").toString();//tbl_system_scm ??????
			String reqFeatureqIds = null;
			//??????????????????ids
			if (backMap.get("reqFeatureqIds") != null) {
				reqFeatureqIds = backMap.get("reqFeatureqIds").toString();
			}
			String systemId = backMap.get("systemId").toString();
			List<TblSystemModule> moduleLog=iStructureService.selectSystemModule(Long.parseLong(systemId));
			String scheduled = null;
			if (backMap.get("scheduled") != null) {//????????????
				scheduled = backMap.get("scheduled").toString();
			}
			// ?????????????????????????????????
			TblSystemJenkinsJobRun jorRun = iStructureService.selectBuildMessageById(Long.parseLong(runId));
			// ????????????
			TblSystemJenkins tblSystemJenkinsLog = iStructureService.selectSystemJenkinsById(systemJenkinsId);
			//?????????????????????
			List<String> moduleTestList=new ArrayList<>();
			if(backMap.get("moduleList")!=null) {
				moduleTestList = (List<String>) backMap.get("moduleList");

			}
			//????????????modulelist???envtype?????????
             //api ???????????????
			List<Map<String,Object>> testConfig=deployService.getTestConfig( Long.parseLong(systemId), moduleTestList,  envType,"1");
			//ui???????????????
			List<Map<String,Object>> uiTestConfig=deployService.getTestConfig( Long.parseLong(systemId), moduleTestList,  envType,"2");
			Map<String,Object> autoMap=new HashMap<>();
			autoMap.put("systemId",systemId);
			autoMap.put("testConfig",testConfig);//?????????????????????
			autoMap.put("uiTestConfig",uiTestConfig);//ui???????????????
			autoMap.put("deployType","1");//????????????
			autoMap.put("scmId",systemScmId);
			autoMap.put("jobRunId",runId);
			autoMap.put("environnmentType",envType);
			autoMap.put("jenkinsId",systemJenkinsId);
			autoMap.put("taskMapFlag",1);//??????????????????????????? 1 ????????? 2??????
			autoMap.put("autoTest","false");//?????????????????????
			autoMap.put("scheduled","false");
			if(backMap.get("userId")!=null) {
                autoMap.put("userId", backMap.get("userId").toString());
            }
			if(backMap.get("envName")!=null) {
                autoMap.put("envName", backMap.get("envName").toString());
            }

			TblToolInfo jenkinsTool = iStructureService.geTblToolInfo(Integer.parseInt(jenkinsToolId));
			String nowJobNumber=backMap.get("nowJobNumber").toString();
		    if (scheduled != null && scheduled.equals("true")
				&& !jobNumber.equals(nowJobNumber)) {
				// ?????????????????????????????????
				// Timestamp time = Timestamp.valueOf(startDate);
				TblSystemJenkinsJobRun ttr = new TblSystemJenkinsJobRun();
				ttr.setSystemJenkinsId(jorRun.getSystemJenkinsId());
				ttr.setSystemId(jorRun.getSystemId());
				ttr.setJobName(jorRun.getJobName());
				ttr.setRootPom(".");
				ttr.setBuildStatus(1);// ??????
				ttr.setStatus(1);
				ttr.setCreateDate(startTime);
				ttr.setEndDate(endTime);
				ttr.setLastUpdateDate(endTime);
				ttr.setEnvironmentType(jorRun.getEnvironmentType());
				ttr.setGoalsOptions("mvn clean install -Dmaven.test.skip=true");
				ttr.setCreateType(1);
				ttr.setJobType(2);
				ttr.setJobRunNumber(Integer.valueOf(nowJobNumber));
				Thread.sleep(8000);
				String jobName = jorRun.getJobName();

				ttr.setStartDate(startTime);
				//??????????????????
				Map<String, String> result = jenkinsBuildService.getLastBuildLog(jenkinsTool, tblSystemJenkinsLog, jobName,moduleLog);
				String timeStatus = result.get("status");
				String timeLog = result.get("log");
				timeLog=iStructureService.detailByteBylog(timeLog,Constants.MAX_ALLOW_PACKET);
				ttr.setBuildLogs(timeLog);
				ttr.setBuildStatus(Integer.parseInt(timeStatus));

				if((testConfig.size()>0 || uiTestConfig.size()>0) && timeStatus.equals("2")){
					String testLog=addTestTime(timeLog);
					ttr.setBuildLogs(testLog);
					autoMap.put("scheduled","true");
					autoMap.put("autoTest","true");
				}else {
					ttr.setLastUpdateDate(startTime);
					ttr.setEndDate(endTime);
				}
				
				
				
				Map<String, Object> saveDataMap = new HashMap<String, Object>();
				iStructureService.getJenkinsStageLog(ttr, jenkinsTool, tblSystemJenkinsLog, jobName, saveDataMap);
				
				String moduleJson=backMap.get("moduleJson").toString();
				List<TblSystemModuleJenkinsJobRun> insertJobRunList = new ArrayList<TblSystemModuleJenkinsJobRun>();
				for(Integer modulejobRunId:moduleRunIds) {
					if (saveDataMap.get("updateModuleStatusList") == null) {
						updateModuleStatus(moduleJson,4,startTime,endTime,saveDataMap);
					}
					TblSystemModuleJenkinsJobRun moduleJobrun=iStructureService.selectByModuleRunId(modulejobRunId);
					if((testConfig.size()>0 || uiTestConfig.size()>0)  && timeStatus.equals("2")) {

					}else{
						moduleJobrun.setLastUpdateDate(endTime);
                        moduleJobrun.setBuildStatus(Integer.parseInt(timeStatus));
					} 
					moduleJobrun.setCreateDate(startTime);
					
					updateModuleRunInfoSchedule(moduleJson,moduleJobrun,String.valueOf(modulejobRunId));
					insertJobRunList.add(moduleJobrun);
				}
				saveDataMap.put("insertJobRunList", insertJobRunList);
				
				
				saveDataMap.put("autoMap", autoMap);
				((StructureNotify)AopContext.currentProxy()).saveCallBackData(backMap, ttr, "", saveDataMap, 5);

				if(testConfig.size()>0 && timeStatus.equals("2")) {
					messageflag=false;
					for (Map<String, Object> map : testConfig) {
						autoMap.put("testConfig", map);
						autoMap.put("testType","1");
						Map<String,Object> paramMap = new HashMap<String,Object>(){{
							putAll(autoMap);
						}};
						threadPool.submit(new MyRun(paramMap));
					}
				}

				if (uiTestConfig.size()>0 && timeStatus.equals("2") ){//ui???????????????
					messageflag=false;
					for(Map<String,Object> map:uiTestConfig) {
						autoMap.put("testConfig",map);
						autoMap.put("testType","2");
						Map<String,Object> paramMap = new HashMap<String,Object>(){{
							putAll(autoMap);
						}};
						threadPool.submit(new MyRun(paramMap));
					}

				}
				callbackLog.info("????????????????????????");

			} else {
				Map<String, Object> saveDataMap = new HashMap<String, Object>();
 				TblSystemJenkinsJobRun tblSystemJenkinsJobRun = new TblSystemJenkinsJobRun();
 				tblSystemJenkinsJobRun.setSystemId((long) Integer.parseInt(systemId));
				tblSystemJenkinsJobRun.setSystemJenkinsId(Long.parseLong(systemJenkinsId));
				tblSystemJenkinsJobRun.setId(Long.parseLong(runId));
				tblSystemJenkinsJobRun.setStartDate(startTime);
				String status = "";
				// ??????jenkinsTool ???jobName
				Thread.sleep(8000);
				String jobName = jorRun.getJobName();
				Map<String, String> result = jenkinsBuildService.getLastBuildLog(jenkinsTool, tblSystemJenkinsLog, jobName,moduleLog);
				status = result.get("status");//????????????
				String log = result.get("log");//????????????
				log=iStructureService.detailByteBylog(log,Constants.MAX_ALLOW_PACKET);
				tblSystemJenkinsJobRun.setBuildLogs(log);
				// ??????scm?????????
				if (scheduled != null && scheduled.equals("true")) {

				} else {
					//???????????????????????????
					if(backMap.get("moduleList")!=null && status.equals("2")) {
						List<String> moduleList = (List<String>) backMap.get("moduleList");
						saveDataMap.put("moduleList", moduleList);
					}
					TblSystemScm tblSystemScm = iStructureService.getTblsystemScmById(Long.parseLong(systemScmId));
					tblSystemScm.setDeployStatus(1);
					tblSystemScm.setId(Long.parseLong(systemScmId));
					if ((testConfig.size()>0 || uiTestConfig.size()>0)&& status.equals("2")){
						//??????????????????
						tblSystemScm.setDeployStatus(3);
						autoMap.put("autoTest","true");
					}else{
						//??????
						tblSystemScm.setDeployStatus(1);
					}
					saveDataMap.put("tblSystemScm", tblSystemScm);
				}

				callbackLog.info("????????????:envtype"+envType+"scheduled"+scheduled+"reqFeatureqIds"+reqFeatureqIds+"status"+status);
				iStructureService.getJenkinsStageLog(jorRun, jenkinsTool, tblSystemJenkinsLog, jobName, saveDataMap);
				
				if((testConfig.size()>0 || uiTestConfig.size()>0) && status.equals("2")){ //??????????????????????????????
                    String moduleJson=backMap.get("moduleJson").toString();
                    log=addTestTime(log);
					tblSystemJenkinsJobRun.setBuildStatus(4);//?????????????????? ???????????????
                    //??????module??????
                    updateModuleStatus(moduleJson,4,startTime,endTime,saveDataMap);
				}else {
                    String moduleJson=backMap.get("moduleJson").toString();
					tblSystemJenkinsJobRun.setBuildStatus(Integer.parseInt(status));
					tblSystemJenkinsJobRun.setEndDate(endTime);
					tblSystemJenkinsJobRun.setLastUpdateDate(endTime);
					iStructureService.getModuleRunCallBack(moduleRunIds, endTime,startTime,moduleJson,saveDataMap);
				}
				
				// ??????????????????
				Map<String, String> taskUpdateMap = new HashMap<String, String>();
				if (reqFeatureqIds != null && !reqFeatureqIds.equals("") ) {
	                autoMap.put("reqFeatureqIdsEmail",reqFeatureqIds);
					Map<String, Object> sendMap = new HashMap<>();
					String userId = backMap.get("userId").toString();
					String userAccount = backMap.get("userAccount").toString();
					String userName = backMap.get("userName").toString();
					sendMap.put("userId", userId);
					sendMap.put("status", status);
					sendMap.put("envType", envType);//????????????
					sendMap.put("userAccount", userAccount);//????????????
					sendMap.put("userName", userName);//????????????
					String sendTask = JSON.toJSONString(sendMap);

					Map<String, Object> remap = new HashMap<>();
					remap.put("reqFeatureIds", reqFeatureqIds);
					remap.put("requirementFeatureFirstTestDeployTime", new Timestamp(new Date().getTime()));

					if((testConfig.size()==0 && uiTestConfig.size()==0) || status.equals("3")) {
						String json = JsonUtil.toJson(remap);
						taskUpdateMap.put("reqFeatureqIds", reqFeatureqIds);
						taskUpdateMap.put("envType", envType);
						taskUpdateMap.put("sendTask", sendTask);
						taskUpdateMap.put("status", status);
						taskUpdateMap.put("json", json);
					}else{
						sendMap.put("reqFeatureIds", reqFeatureqIds);
						sendMap.put("requirementFeatureFirstTestDeployTime", new Timestamp(new Date().getTime()));
						autoMap.put("taskMapFlag",2);
						autoMap.put("taskMap",sendMap);
					}
				}
				saveDataMap.put("taskUpdateMap", taskUpdateMap);
				
				((StructureNotify)AopContext.currentProxy()).saveCallBackData(backMap, tblSystemJenkinsJobRun, "1", saveDataMap, 6);

				// ????????????
				if (status.equals(Constants.DEPLOY_SUCCESS)) {
					if (scheduled != null && scheduled.equals("true")) {
						// ?????????????????????????????????????????????????????? ?????????????????????????????????
						autoMap.put("scheduled","true");

						if (testConfig.size()>0 ){//api???????????????
							messageflag=false;
							autoMap.put("autoTest","true");
							for(Map<String,Object> map:testConfig) {
								autoMap.put("testConfig",map);
								autoMap.put("testType","1");
								Map<String,Object> paramMap = new HashMap<String,Object>(){{
									putAll(autoMap);
								}};
								threadPool.submit(new MyRun(paramMap));
							}

						}


						if (uiTestConfig.size()>0 ){//ui???????????????
							messageflag=false;
							autoMap.put("autoTest","true");
							for(Map<String,Object> map:uiTestConfig) {
								autoMap.put("testConfig",map);
								autoMap.put("testType","2");
								Map<String,Object> paramMap = new HashMap<String,Object>(){{
									putAll(autoMap);
								}};
								threadPool.submit(new MyRun(paramMap));
							}

						}
					} else {

						if (envType.equals(Constants.PRDIN) || envType.equals(Constants.PRDOUT)) {
							List<Map<String,Object>> microList=new ArrayList<>();
							String systemPackageName = detailAutoPlant(microList,systemId,backMap);
							// ?????????????????????????????????
							String versionInfo = backMap.get("versionInfo").toString();//??????
							String proDuctionDeploytype = backMap.get("proDuctionDeploytype").toString();
							callbackLog.info("proDuctionDeploytype"+proDuctionDeploytype);
							if(proDuctionDeploytype.equals("2")) {
								messageflag = false;
							}
							autoPlantForm(Long.parseLong(systemId), envType, reqFeatureqIds, versionInfo,
										proDuctionDeploytype,systemPackageName,microList,runId,autoMap);



						}else{
							if (testConfig.size()>0){
								messageflag=false;
								for(Map<String,Object> map:testConfig) {
									autoMap.put("testConfig",map);
									autoMap.put("testType","1");//?????????????????????
									Map<String,Object> paramMap = new HashMap<String,Object>(){{
										putAll(autoMap);
									}};
									threadPool.submit(new MyRun(paramMap));
								}
							}


							if (uiTestConfig.size()>0 ){//ui???????????????
								messageflag=false;
								for(Map<String,Object> map:uiTestConfig) {
									autoMap.put("testConfig",map);
									autoMap.put("testType","2");
									Map<String,Object> paramMap = new HashMap<String,Object>(){{
										putAll(autoMap);
									}};
									threadPool.submit(new MyRun(paramMap));
								}

							}

						}
					}
				}
			//??????????????????????????????
                if(status.equals("2")) {
                    detailEnvDate(reqFeatureqIds, envType, endTime);
                }
            //????????????
			if(messageflag) {
				sendMessage(tblSystemJenkinsJobRun, backMap, "2");
			}
			callbackLog.info("??????????????????");
			}
		} catch (Exception e) {
			this.handleException(e);
		}

	}

	 class MyRun implements Runnable{
        private Map<String,Object> data ;

        public MyRun(Map<String,Object>data){
        	this.data = data;
		}
		 @Override
		 public void run() {
			 automatTestService.toAutomatTest(this.data);
		 }
	 }


	 /**
	  * 
	 * @Title: updateModuleStatus
	 * @Description: ?????????????????????????????????
	 * @author author
	 * @param moduleJson ????????????
	 * @param buildStatus
	 * @param startTime ????????????
	 * @param endTime ????????????
	 * @param saveDataMap  
	  */
    private void updateModuleStatus(String moduleJson,Integer buildStatus,Timestamp startTime,Timestamp endTime, Map<String, Object> saveDataMap){
        Map<String,Object> moduleList = (Map<String,Object>) JSONArray.parse(moduleJson);
        List<TblSystemModuleJenkinsJobRun> updateModuleStatusList = new ArrayList<TblSystemModuleJenkinsJobRun>();
        for(String key:moduleList.keySet()){
            String value = moduleList.get(key).toString();
            Map<String,Object> map = (Map<String,Object>) JSONArray.parse(value);
            SimpleDateFormat time = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
            Integer modulejobRunId=Integer.parseInt(map.get("moduleRunId").toString());
            TblSystemModuleJenkinsJobRun moduleJobrun = tblSystemModuleJenkinsJobRunMapper.selectById(modulejobRunId);

			if(map.get("endDate")==null){
				moduleJobrun.setLastUpdateDate(endTime);
			}else {
				moduleJobrun.setLastUpdateDate(Timestamp.valueOf(map.get("endDate").toString()));
			}
			if(map.get("startDate")==null){
				moduleJobrun.setCreateDate(startTime);
			}else{
				moduleJobrun.setCreateDate( Timestamp.valueOf(map.get("startDate").toString()));
			}

            moduleJobrun.setBuildStatus(buildStatus);
            updateModuleStatusList.add(moduleJobrun);
        }
        saveDataMap.put("updateModuleStatusList", updateModuleStatusList);
    }

	private String addTestTime(String log){
		return  log;
	}


	private void detailEnvDate(String reqFeatureqIds,String envType,Timestamp timestamp){

		deployService.detailEnvDate(reqFeatureqIds, envType, timestamp);

	}
	/**
	 * ?????????????????????????????????
	 * @author weiji
	 * @param backMap ??????
	 * @param systemId ??????id
	 * @param microList ?????????
	 * @return Map<String, Object>
	 */

	private String detailAutoPlant(List<Map<String,Object>> microList,String systemId,Map<String,Object> backMap){
		String systemPackageName = "";
		TblSystemInfo tblSystemInfo=iStructureService.geTblSystemInfo(Long.parseLong(systemId));
		if(tblSystemInfo.getArchitectureType().toString().equals(Constants.SERVER_MICRO_TYPE)){
			String subSystemPackageName=backMap.get("subSystemPackageName").toString();
			String subsystemCode=backMap.get("subSystemCode").toString();
			String[] subSystemPackageNameArray=subSystemPackageName.split(",");
			String[] subSystemCodeArray=subsystemCode.split(",");
			if(subSystemPackageNameArray.length==subSystemCodeArray.length) {
				for (int i = 0; i < subSystemPackageNameArray.length; i++) {
					//?????????
					Map<String,Object> micMap=new HashMap<>();
					micMap.put("subSystemCode",subSystemCodeArray[i]);
					micMap.put("subSystemPackageName",subSystemPackageNameArray[i]);
					microList.add(micMap);
				}
			}
		}else{
			 systemPackageName=backMap.get("systemPackageName").toString();

		}
		return systemPackageName;
	}

	/**
	 * 
	* @Title: autoPlantForm
	* @Description: ???????????????????????????
	* @author author
	* @param systemId ??????id
	* @param envType ??????
	* @param reqFeatureqIds ????????????id
	* @param versionInfo ????????????
	* @param proDuctionDeploytype ??????????????????
	* @param systemPackageName jar??????
	* @param microList ??????
	* @param runId
	* @param autoMap void
	 */
	private void autoPlantForm(Long systemId, String envType, String reqFeatureqIds, String versionInfo,
			String proDuctionDeploytype,String systemPackageName,List<Map<String,Object>> microList,String runId,Map<String,Object> autoMap) {

		if (envType != null && proDuctionDeploytype != null && versionInfo != null) {

			//??????????????????PRD-IN???PRD-OUT??????????????????????????????
			if (proDuctionDeploytype.equals("2")
					&& (envType.equals(Constants.PRDIN) || envType.equals(Constants.PRDOUT))) {
				Long[] featureIds = null;
				VersionInfo ver = JSON.parseObject(versionInfo, VersionInfo.class);
				if (reqFeatureqIds != null && !reqFeatureqIds.equals("")) {
					String[] req = reqFeatureqIds.split(",");
					featureIds = (Long[]) ConvertUtils.convert(req, Long.class);
				}

				List<Map<String,Object>> testConfig= (List<Map<String, Object>>) autoMap.get("testConfig");

				deployController.versionToOperation(systemId, featureIds, ver, systemPackageName, microList, runId,autoMap);



			}
		}
	}

	private Timestamp getBefortime() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		Date beforDate = new Date(now.getTime() - 300000);
		return Timestamp.valueOf(sdf.format(beforDate));

	}
	/**
	 * ??????????????????
	 * @author weiji
	 * @param jsonMap ??????
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "callBackPackageDepolyJenkins", method = RequestMethod.POST)
	public void callBackPackageDepolyJenkins(String jsonMap) throws Exception {

		try {
			//????????????????????????message
			boolean messageflag=true;
			Map<String, Object> backMap = JSON.parseObject(jsonMap, Map.class);
			Timestamp startTime=formateDate(backMap);
			Timestamp endTime=formateEndDate(backMap);
			String runId = backMap.get("runId").toString();
			String systemJenkinsId = backMap.get("systemJenkinsId").toString();
			String jenkinsToolId = backMap.get("jenkinsToolId").toString();
			String moduleRunList = backMap.get("moduleRunList").toString();
			String scheduled = null;
			if (backMap.get("scheduled") != null) {
				scheduled = backMap.get("scheduled").toString();
			}
			String reqFeatureqIds = null;
			if (backMap.get("reqFeatureqIds") != null) {
				reqFeatureqIds = backMap.get("reqFeatureqIds").toString();
			}
			String envType = null;
			if (backMap.get("envType") != null) {
				envType = backMap.get("envType").toString();
			}
			String systemId = backMap.get("systemId").toString();
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			String status = "";
			long jobRunId = Long.parseLong(runId);
			TblSystemJenkinsJobRun tJobRun = iStructureService.selectBuildMessageById(jobRunId);
			Map<String, Object> symap = new HashMap<>();
			symap.put("id", systemJenkinsId);
			TblSystemJenkins tblSystemJenkins = iStructureService.getSystemJenkinsByMap(symap).get(0);
			List<String> moduleTestList=new ArrayList<>();
			if(backMap.get("moduleList")!=null) {
				moduleTestList = (List<String>) backMap.get("moduleList");

			}
			List<TblSystemModule> moduleLog=iStructureService.selectSystemModule(Long.parseLong(systemId));
			//api ???????????????
			List<Map<String,Object>> testConfig=deployService.getTestConfig( Long.parseLong(systemId), moduleTestList,  envType,"1");
			//ui ???????????????
			List<Map<String,Object>> uiTestConfig=deployService.getTestConfig( Long.parseLong(systemId), moduleTestList,  envType,"2");
			Map<String,Object> autoMap=new HashMap<>();

			autoMap.put("systemId",systemId);
			autoMap.put("uiTestConfig",uiTestConfig);
			autoMap.put("testConfig",testConfig);
			autoMap.put("deployType","2");//????????????
			autoMap.put("jenkinsId",tblSystemJenkins.getId());
			autoMap.put("jobRunId",jobRunId);
			autoMap.put("environnmentType",envType);
			autoMap.put("taskMapFlag",1);//??????????????????????????? 1 ????????? 2??????
			autoMap.put("autoTest","false");
			autoMap.put("scheduled","false");
            if(backMap.get("userId")!=null) {
                autoMap.put("userId", backMap.get("userId").toString());
            }
            if(backMap.get("envName")!=null) {
                autoMap.put("envName", backMap.get("envName").toString());
            }

				TblToolInfo jenkinsTool = iStructureService.geTblToolInfo(tblSystemJenkins.getToolId());
				Thread.sleep(8000);
				Map<String, String> result = jenkinsBuildService.getLastBuildLog(jenkinsTool, tblSystemJenkins,
						tblSystemJenkins.getJobName(),moduleLog);
				TblSystemJenkinsJobRun tblSystemJenkinsJobRun = new TblSystemJenkinsJobRun();
				tblSystemJenkinsJobRun.setSystemId(tblSystemJenkins.getSystemId());
				tblSystemJenkinsJobRun.setLastUpdateDate(endTime);
				tblSystemJenkinsJobRun.setSystemJenkinsId(tblSystemJenkins.getId());
				tblSystemJenkinsJobRun.setId(jobRunId);
				tblSystemJenkinsJobRun.setStartDate(startTime);
				
				Map<String, Object> saveDataMap = new HashMap<String, Object>();
				status = result.get("status");
				String log = result.get("log");
 			    log=iStructureService.detailByteBylog(log,Constants.MAX_ALLOW_PACKET);
 			    tblSystemJenkinsJobRun.setBuildLogs(log);
				tblSystemJenkinsJobRun.setBuildStatus(Integer.parseInt(status));
				
				tblSystemJenkins.setLastUpdateDate(timestamp);
				if((testConfig.size()>0 || uiTestConfig.size()>0) && status.equals("2")){
					tblSystemJenkins.setDeployStatus(3);//??????????????????
					tblSystemJenkinsJobRun.setBuildStatus(4);//??????????????????
					autoMap.put("autoTest","true");
				}else {
					tblSystemJenkinsJobRun.setLastUpdateDate(timestamp);
					tblSystemJenkinsJobRun.setEndDate(endTime);
					tblSystemJenkins.setDeployStatus(1);
				}
				saveDataMap.put("tblSystemJenkins", tblSystemJenkins);
				
				iStructureService.getJenkinsStageLog(tJobRun, jenkinsTool, tblSystemJenkins, tblSystemJenkins.getJobName(), saveDataMap);
				
			    String moduleJson=backMap.get("moduleJson").toString();
				// ??????modulejobrun???
				List<TblSystemModuleJenkinsJobRun> jobRunList = (List<TblSystemModuleJenkinsJobRun>) JSONArray
						.parseArray(moduleRunList, TblSystemModuleJenkinsJobRun.class);
				for (TblSystemModuleJenkinsJobRun tmjr : jobRunList) {
					if((testConfig.size()>0  || uiTestConfig.size()>0)&& status.equals("2")){
                        tmjr.setBuildStatus(4);
					}else{
                        tmjr.setBuildStatus(Integer.parseInt(status));
					}
					tmjr.setId(tmjr.getId());
					tmjr.setCreateDate(startTime);
					tmjr.setLastUpdateDate(endTime);
					//??????module??????
					updateModuleRunInfo(moduleJson,tmjr);
				}
				saveDataMap.put("jobRunList", jobRunList);
				
				// ??????????????????
				Map<String, String> taskUpdateMap = new HashMap<String, String>();
				if (reqFeatureqIds != null && !reqFeatureqIds.equals("")) {
                    autoMap.put("reqFeatureqIdsEmail",reqFeatureqIds);
					Map<String, Object> sendMap = new HashMap<>();
					String userId = backMap.get("userId").toString();
					String userAccount = backMap.get("userAccount").toString();
					String userName = backMap.get("userName").toString();
					sendMap.put("userId", userId);
					sendMap.put("userAccount", userAccount);
					sendMap.put("userName", userName);
					String sendTask = JSON.toJSONString(sendMap);

					Map<String, Object> remap = new HashMap<>();
					remap.put("reqFeatureIds", reqFeatureqIds);
					remap.put("requirementFeatureFirstTestDeployTime", new Timestamp(new Date().getTime()));

					if((testConfig.size()==0  && uiTestConfig.size()==0 )|| status.equals("3")) {
						String json = JsonUtil.toJson(remap);
						taskUpdateMap.put("reqFeatureqIds", reqFeatureqIds);
						taskUpdateMap.put("envType", envType);
						taskUpdateMap.put("sendTask", sendTask);
						taskUpdateMap.put("status", status);
						taskUpdateMap.put("json", json);
					}else{
					     sendMap .put("reqFeatureIds", reqFeatureqIds);
						 sendMap .put("envType", envType);
						 sendMap .put("status", status);
					     sendMap.put("requirementFeatureFirstTestDeployTime", new Timestamp(new Date().getTime()));
						 autoMap.put("taskMap",sendMap);
						 autoMap.put("taskMapFlag","2");
					}
				}
				saveDataMap.put("taskUpdateMap", taskUpdateMap);
				
				((StructureNotify)AopContext.currentProxy()).saveCallBackData(backMap, tblSystemJenkinsJobRun, "", saveDataMap, 7);

				if (status.equals(Constants.DEPLOY_SUCCESS)) {
					if (envType.equals(Constants.PRDIN) || envType.equals(Constants.PRDOUT)) {
						// ?????????????????????
						//Map<String,Object> trdMap=new HashMap<>();
						List<Map<String,Object>> microList=new ArrayList<>();
						String systemPackageName = detailAutoPlant(microList,systemId,backMap);
						String versionInfo = backMap.get("versionInfo").toString();
						String proDuctionDeploytype = backMap.get("proDuctionDeploytype").toString();
						if(proDuctionDeploytype.equals("2")){
							messageflag=false;
						}
						autoPlantForm(Long.parseLong(systemId), envType, reqFeatureqIds, versionInfo,
								proDuctionDeploytype,systemPackageName,microList,runId,autoMap);
					}else {
                        if (testConfig.size() > 0) {
							messageflag=false;
							for(Map<String,Object> map:testConfig) {
								autoMap.put("testConfig", map);
								autoMap.put("testType","1");
								Map<String,Object> paramMap = new HashMap<String,Object>(){{
									putAll(autoMap);
								}};
								threadPool.submit(new MyRun(paramMap));
							}
                        }

						if (uiTestConfig.size()>0 ){//ui???????????????
							messageflag=false;
							for(Map<String,Object> map:uiTestConfig) {
								autoMap.put("testConfig",map);
								autoMap.put("testType","2");
								Map<String,Object> paramMap = new HashMap<String,Object>(){{
									putAll(autoMap);
								}};
								threadPool.submit(new MyRun(paramMap));
							}

						}
                    }



				}
			//????????????
			if(messageflag) {
				sendMessage(tblSystemJenkinsJobRun, backMap, "2");
			}
			callbackLog.info("??????????????????");

		} catch (Exception e) {
			this.handleException(e);
		}

	}




	private  Timestamp formateDate(Map<String,Object> backMap){
		String startDate=backMap.get("startDate").toString();
		SimpleDateFormat time = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
		return Timestamp.valueOf(startDate);

	}

	private  Timestamp formateEndDate(Map<String,Object> backMap){
		String startDate=backMap.get("endDate").toString();
		SimpleDateFormat time = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
		return Timestamp.valueOf(startDate);

	}

	/**
	 * 
	* @Title: detailPackage
	* @Description: ??????????????????
	* @author author
	* @param backMap ????????????
	* @param runId
	* @param systemId ??????id
	* @throws Exception
	 */
	private void detailPackage(Map<String,Object> backMap, Long runId, Long systemId) throws Exception{
		TblSystemInfo tblSystemInfo = iStructureService.geTblSystemInfo(systemId);
    	TblSystemJenkinsJobRun tblSystemJenkinsJobRun=iStructureService.selectBuildMessageById(runId);
    	String artType = (String)backMap.get("artType");
    	String version = (String)backMap.get("version");
    	if(StringUtil.isNotEmpty(artType) && StringUtil.isNotEmpty(version)){
    		Map<String, Object> paraMap = new HashMap<String, Object>();
    		paraMap.put("SYSTEM_JENKINS_JOB_RUN", runId);
    		List<TblSystemModuleJenkinsJobRun> moduleJobRunList = tblSystemModuleJenkinsJobRunMapper.selectByMap(paraMap);
    		
			List<String> checkModuleList = (List<String>) backMap.get("checkModuleList");
			Map<String, Object> param = new HashMap<>();
			param.put("status", 1);
			param.put("tool_type", 6);
			List<TblToolInfo> list = iStructureService.getTblToolInfo(param);
			NexusUtil nexusUtil = new NexusUtil(list.get(0));
			NexusAssetBO tagNexusAssetBO=new NexusAssetBO();
			if (checkModuleList == null || checkModuleList.size() <= 0) {//?????????
				String artifactId = tblSystemInfo.getArtifactId();
				String groupId = tblSystemInfo.getGroupId();
				String snapShotName = tblSystemInfo.getSnapshotRepositoryName();
				String releaseName = tblSystemInfo.getReleaseRepositoryName();
				tagNexusAssetBO = getNexusAssetBO(nexusUtil, artifactId, groupId, snapShotName, releaseName, artType, version);
				addArtifactData(backMap, systemId, tblSystemJenkinsJobRun, moduleJobRunList, artType, tagNexusAssetBO, null);
			} else {
				//?????????
				for (String moduleId : checkModuleList) {
					String artifactId = tblSystemInfo.getArtifactId();
					String groupId = tblSystemInfo.getGroupId();
					String snapShotName = tblSystemInfo.getSnapshotRepositoryName();
					String releaseName = tblSystemInfo.getReleaseRepositoryName();
					TblSystemModule tblSystemModule = iStructureService.getTblsystemModule(Long.parseLong(moduleId));
					if (tblSystemModule != null) {
						if (StringUtil.isNotEmpty(tblSystemModule.getArtifactId())) {
							artifactId = tblSystemModule.getArtifactId();
						}
						if (StringUtil.isNotEmpty(tblSystemModule.getGroupId())) {
							groupId = tblSystemModule.getGroupId();
						}
						if (StringUtil.isNotEmpty(tblSystemModule.getSnapshotRepositoryName())) {
							snapShotName = tblSystemModule.getSnapshotRepositoryName();
						}
						if (StringUtil.isNotEmpty(tblSystemModule.getReleaseRepositoryName())) {
							releaseName = tblSystemModule.getReleaseRepositoryName();
						}
					}
					//???????????????????????????????????????
					tagNexusAssetBO = getNexusAssetBO(nexusUtil, artifactId, groupId, snapShotName, releaseName, artType, version);
					addArtifactData(backMap, systemId, tblSystemJenkinsJobRun, moduleJobRunList, artType, tagNexusAssetBO, Long.parseLong(moduleId));
				}
			}

		}
	}

	/**
	 * 
	* @Title: addArtifactData
	* @Description: ?????????????????????????????????????????????????????????????????????
	* @author author
	* @param backMap
	* @param systemId ??????id
	* @param tblSystemJenkinsJobRun ????????????Jenkisn??????
	* @param moduleJobRunList  ?????????jenkins??????
	* @param artType 1?????????2??????
	* @param tagNexusAssetBO ????????????????????????
	* @param moduleId ??????id
	* @throws SonarQubeException void
	 */
	private void addArtifactData(Map<String, Object> backMap, Long systemId, TblSystemJenkinsJobRun tblSystemJenkinsJobRun, 
			List<TblSystemModuleJenkinsJobRun> moduleJobRunList, String artType,
			NexusAssetBO tagNexusAssetBO, Long moduleId) throws SonarQubeException {
		if (tagNexusAssetBO != null) {
			TblArtifactInfo tblArtifactInfo=new TblArtifactInfo();
			tblArtifactInfo.setSystemId(systemId);
			tblArtifactInfo.setSystemModuleId(moduleId);
			tblArtifactInfo.setRepository(tagNexusAssetBO.getRepository());
			tblArtifactInfo.setGroupId(tagNexusAssetBO.getGroup());
			tblArtifactInfo.setArtifactId(tagNexusAssetBO.getArtifactId());
			tblArtifactInfo.setVersion(tagNexusAssetBO.getVersion());
			tblArtifactInfo.setNexusPath(tagNexusAssetBO.getPath());
			tblArtifactInfo.setRemark("");
			long userId=tblSystemJenkinsJobRun.getCreateBy();
			if(tblSystemJenkinsJobRun.getCreateBy() == null){
				userId=1;
			}
			String tagStr = String.valueOf(tblSystemJenkinsJobRun.getEnvironmentType());
			packageService.addArtifactFrature(backMap, tblArtifactInfo, moduleJobRunList, tagStr, userId, artType);
		}
	}

	/**
	 * 
	* @Title: getNexusAssetBO
	* @Description: ?????????????????????????????????
	* @author author
	* @param nexusUtil 
	* @param artifactId ??????id
	* @param groupId
	* @param snapShotName ????????????
	* @param releaseName ????????????
	* @param artType ??????1?????????2??????
	* @param version ?????????
	* @return
	* @throws Exception NexusAssetBO
	 */
	private NexusAssetBO getNexusAssetBO(NexusUtil nexusUtil, String artifactId, String groupId,
			String snapShotName, String releaseName, String artType, String version ) throws Exception {
		NexusAssetBO tagNexusAssetBO = null;
		List<NexusAssetBO> listBo;
		NexusSearchVO nexusSearchVO = new NexusSearchVO();
		nexusSearchVO.setGroupId(groupId);
		nexusSearchVO.setArtifactId(artifactId);
		if (artType.equals("1")) {//??????
			//?????????
			nexusSearchVO.setRepository(snapShotName);
			//?????????
			nexusSearchVO.setBaseVersion(version+"-SNAPSHOT");
			listBo = nexusUtil.searchAssetList(nexusSearchVO);
			Collections.sort(listBo, new Comparator<NexusAssetBO>() {
				@Override
				public int compare(NexusAssetBO o1, NexusAssetBO o2) {
					return o2.getCreateTime().compareTo(o1.getCreateTime());
				}
			});
			
		} else {//release
			nexusSearchVO.setRepository(releaseName);
			nexusSearchVO.setBaseVersion(version);
			listBo = nexusUtil.searchAssetList(nexusSearchVO);
			
		}
		
		//????????????????????????
		if(listBo.size()>0){
			tagNexusAssetBO=listBo.get(0);
		}
		return tagNexusAssetBO;
	}

	public void handleException(Exception e) {
		e.printStackTrace();
		callbackLog.error(e.getMessage(), e);

	}



	//?????????????????????????????????????????????
	private void sendMessage(TblSystemJenkinsJobRun tblSystemJenkinsJobRun,Map<String,Object> backMap ,String jobType){
		//TblSystemInfo tblSystemInfo=iStructureService.geTblSystemInfo(tblSystemJenkinsJobRun.getSystemId());
		/* ??????????????????????????? liushan  */
		TblSystemInfo tblSystemInfo = iStructureService.getTblSystemInfoById(tblSystemJenkinsJobRun.getSystemId());
        String messageTitle="";
        String messageContent="";
		if(tblSystemInfo.getProjectIds() != null && !tblSystemInfo.getProjectIds().equals("") && backMap.get("userId")!=null && backMap.get("envName")!=null ) {
			String creatUserId = backMap.get("userId").toString();
			String envName = backMap.get("envName").toString();
			Map<String, Object> map = new HashMap<>();
			String stauts = "";

            if(jobType.equals("1")){
                messageTitle="["+tblSystemInfo.getSystemName()+"]" + "-" +  (tblSystemJenkinsJobRun.getBuildStatus() == 2 ? "????????????" : "????????????");
//                if(tblSystemJenkinsJobRun.getBuildStatus() == 3){
//					messageContent = tblSystemInfo.getSystemName() + "-" + envName + "-" + "????????????"+","+detailBuildCode(tblSystemInfo,messageContent);
//
//				}else {
//					messageContent = tblSystemInfo.getSystemName() + "-" + envName + "-" + (tblSystemJenkinsJobRun.getBuildStatus() == 2 ? "????????????" : "????????????") + "????????????" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tblSystemJenkinsJobRun.getStartDate()) + "-????????????" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tblSystemJenkinsJobRun.getEndDate());
//				}
				messageContent = tblSystemInfo.getSystemName() + "-" + envName + "-" + (tblSystemJenkinsJobRun.getBuildStatus() == 2 ? "????????????" : "????????????") + "????????????" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tblSystemJenkinsJobRun.getStartDate()) + "-????????????" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tblSystemJenkinsJobRun.getEndDate());
            }else {
                messageTitle = "[" + tblSystemInfo.getSystemName() + "]" + "-" + (tblSystemJenkinsJobRun.getBuildStatus() == 2 ? "????????????" : "????????????");
                messageContent = tblSystemInfo.getSystemName() + "-" + envName + "-" + (tblSystemJenkinsJobRun.getBuildStatus() == 2 ? "????????????" : "????????????") + "????????????" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tblSystemJenkinsJobRun.getStartDate()) + "-????????????" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tblSystemJenkinsJobRun.getEndDate());

            }
//
            map.put("messageContent",messageContent);
            map.put("messageTitle",messageTitle);
			map.put("messageReceiverScope", 2);
			Map<String, Object> paramMap = new HashMap<>();

			String[] projects = tblSystemInfo.getProjectIds().split(",");
			String userIds = "";
			for(String project:projects){
				paramMap.put("projectId",project);

				List<Map<String, Object>> list = tblProjectInfoMapper.getUserIdByProjectId(paramMap);
				for (Map<String, Object> userMap : list) {
					userIds = userIds + userMap.get("userId").toString() + ",";
				}


			}
			if (!userIds.equals("")) {
				userIds = userIds.substring(0, userIds.length() - 1);
			}


			//????????????????????????
			map.put("messageReceiver", userIds);
			map.put("createBy", creatUserId);
			//?????????????????????
			devManageToSystemInterface.insertMessage(JSON.toJSONString(map));
			//??????????????????
			Map<String, Object> emWeMap = new HashMap<>();
            emWeMap.put("sendMethod","3");
            emWeMap.put("messageContent",messageContent);
            emWeMap.put("messageTitle",messageTitle);
			Map<String,Object> param=new HashMap<>();
			if(jobType.equals("1") && tblSystemJenkinsJobRun.getBuildStatus() == 3){//?????????????????????????????????


			}else {
			//???????????? ???????????????????????????????????????????????????
			param.put("roleName", "???????????????");
			param.put("userIds", Arrays.asList(userIds.split(",")));
			param.put("projectIds", Arrays.asList(tblSystemInfo.getProjectIds().split(",")));
			List<String> pList = tblUserInfoMapper.findRoleByUserIds(param);
			param.put("roleName", "???????????????");
			List<String> devList = tblUserInfoMapper.findRoleByUserIds(param);
			param.put("roleName", "????????????");
			List<String> configList = tblUserInfoMapper.findRoleByUserIds(param);
			pList.add(creatUserId);
			pList.addAll(devList);
			pList.addAll(configList);
			HashSet h = new HashSet(pList);
			pList.clear();
			pList.addAll(h);
			emWeMap.put("messageReceiver",String.join(",",pList));
			devManageToSystemInterface.sendMessage(JSON.toJSONString(emWeMap));
			}
		}
	}
	
	
    @RequestMapping(value = "detailModuleStatus", method = RequestMethod.POST)
    @Transactional
	private void detailModuleStatus(Map<String,Object> map){

    }

	private String detailBuildCode(TblSystemInfo tblSystemInfo,String messageContent){
    	String workTaskCode="";
    	String code="";
		List<String> devIds=new ArrayList<>();
         if( tblSystemInfo.getDevelopmentMode()!=null){
         	String type=tblSystemInfo.getDevelopmentMode().toString();
         	if(type.equals("1")){//??????
         		List<TblSprintInfo> list=tblSprintInfoMapper.findSprintBySystemIdDate(tblSystemInfo.getId());
         		if(list!=null &&list.size()>0){
					TblSprintInfo tblSprintInfo=list.get(0);
					devIds=tblDevTaskMapper.selectIdBySprintId(tblSprintInfo.getId());
				}
			}else{
				List<TblCommissioningWindow> afterTime= tblCommissioningWindowMapper.selectAfterTimeLimit();
				if(afterTime!=null && afterTime.size()>0){
					Map<String, Object> param = new HashMap<>();
					param.put("systemId", tblSystemInfo.getId());
					param.put("windowId", afterTime.get(0).getId());
					devIds=tblDevTaskMapper.selectIdByWindowId(param);

				}

			}
			 if(devIds!=null && devIds.size()>0) {
				 Map<String, Object> param = new HashMap<>();
				 param.put("devTaskIds", devIds);
				 List<Map<String, Object>> svnMaps=tblDevTaskScmFileMapper.getSvnFilesByDevTaskIds(param);
				 List<Map<String, Object>> gitMaps=tblDevTaskScmGitFileMapper.getGitFilesByDevTaskIds(param);
				 svnMaps.addAll(gitMaps);
				 for(Map<String, Object> map:svnMaps){
					 workTaskCode=workTaskCode+map.get("DEV_TASK_CODE").toString()+",";
					 code=code+map.get("COMMIT_FILE").toString()+",";
				 }
			 }

			 workTaskCode=workTaskCode.substring(0,workTaskCode.length()-1);
			 code=code.substring(0,code.length()-1);
			 messageContent=messageContent+"??????????????????????????????:"+workTaskCode+","+"????????????????????????:"+code;
		 }
             return messageContent;
	}


	/**
	 * 
	* @Title: updateModuleRunInfo
	* @Description: ????????????Jenkins????????????
	* @author author
	* @param moduleJson ??????????????????
	* @param tblSystemModuleJenkinsJobRun 
	 */
	private void updateModuleRunInfo(String moduleJson,TblSystemModuleJenkinsJobRun tblSystemModuleJenkinsJobRun){
		Map<String,Object> moduleList = (Map<String,Object>) JSONArray.parse(moduleJson);
		for(String key:moduleList.keySet()){
			String value = moduleList.get(key).toString();
			Map<String,Object> map = (Map<String,Object>) JSONArray.parse(value);
			SimpleDateFormat time = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
			Long modulejobRunId=Long.parseLong(map.get("moduleRunId").toString());

			if(String.valueOf(modulejobRunId).equals(tblSystemModuleJenkinsJobRun.getId().toString())) {
				Integer buildStatus=Integer.parseInt(map.get("buildStatus").toString());
				if(map.get("endDate")==null){

				}else {
					tblSystemModuleJenkinsJobRun.setLastUpdateDate(Timestamp.valueOf(map.get("endDate").toString()));
				}
				if(map.get("startDate")==null){

				}else{
					tblSystemModuleJenkinsJobRun.setCreateDate( Timestamp.valueOf(map.get("startDate").toString()));
				}

				tblSystemModuleJenkinsJobRun.setBuildStatus(buildStatus);
				break;
			}



		}

	}




	/**
	 * 
	* @Title: updateModuleRunInfoSchedule
	* @Description: ???????????????jenkins??????????????????
	* @author author
	* @param moduleJson 
	* @param tblSystemModuleJenkinsJobRun
	* @param mId ??????id
	 */
	private void updateModuleRunInfoSchedule(String moduleJson,TblSystemModuleJenkinsJobRun tblSystemModuleJenkinsJobRun,String mId){
		Map<String,Object> moduleList = (Map<String,Object>) JSONArray.parse(moduleJson);
		for(String key:moduleList.keySet()){
			String value = moduleList.get(key).toString();
			Map<String,Object> map = (Map<String,Object>) JSONArray.parse(value);
			SimpleDateFormat time = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
			Long modulejobRunId=Long.parseLong(map.get("moduleRunId").toString());

			if(String.valueOf(modulejobRunId).equals(mId)) {
				Integer buildStatus=Integer.parseInt(map.get("buildStatus").toString());
				if(map.get("endDate")==null){

				}else {
					tblSystemModuleJenkinsJobRun.setLastUpdateDate(Timestamp.valueOf(map.get("endDate").toString()));
				}
				if(map.get("startDate")==null){

				}else{
					tblSystemModuleJenkinsJobRun.setCreateDate( Timestamp.valueOf(map.get("startDate").toString()));
				}

				tblSystemModuleJenkinsJobRun.setBuildStatus(buildStatus);
//				tblSystemModuleJenkinsJobRunMapper.updateById(tblSystemModuleJenkinsJobRun);//?????????insertJenkinsModuleJobRun??????????????????update???????????????
				break;
			}



		}

	}
}