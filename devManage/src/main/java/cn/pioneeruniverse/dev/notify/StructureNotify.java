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
 * 构建部署回调处理类
 * @author weiji
 *
 */
@RestController
@RequestMapping("notify")
@EnableAspectJAutoProxy(proxyTargetClass = true,exposeProxy = true)
public class StructureNotify {
	/**
	 * 任务回调带来的参数讲解
	 * architectureType:微服务或传统服务,
	 * moduleRunJson： 里面是sonar对应模块的projectkey(如果构建时点击了sonar扫描,需要通过projectkey调用soanr获取此模块或系统的sonar扫描结果),
	 * startDate:任务开始时间,endDate:任务结束时间,
	 * scheduled:是否定时(当是ture时为定时空字符或false不是定时),moduleRunIds:tbl_system_module_jenkins_job_run 的id,
	 * jenkinsToolId:jenkins工具(tbl_tool_info的id),nowJobNumber:jenkins返回的这次任务的任务编号,
	 * jobNumber:我们系统创建任务前通过调用jenkins接口获取这次任务的任务编号(如果是定时任务的话,jenkins每次返回的jobNumber是相同的,要用nowJobNumber),
	 * systemJenkinsId:tbl_system_jenkins的id,systemId:系统id,reqFeatureqIds:开发任务id,systemScmId:tbl_system_scm的id
	 * userName:点击任务的用户名,userId:用户id,userAccount:用户账号,moduleList:此次任务勾选的子模块id,
	 * envType:此次任务环境类型key,envName:此次任务环境类型中文名称,runId:tbl_system_jenkins_job_run的id
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
	 * 构建回调处理
	 * @author weiji
	 * @param jsonMap 回调参数
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
			String jenkinsToolId = backMap.get("jenkinsToolId").toString();//jenkins 工具id
			String scheduled = null;
			//获取是否定时参数
			if (backMap.get("scheduled") != null) {//是否为定时任务
				scheduled = backMap.get("scheduled").toString();
			}
			List<Integer> moduleRunIds = new ArrayList<>();
			if (backMap.get("moduleRunIds") != null) {
				moduleRunIds = JSON.parseObject(backMap.get("moduleRunIds").toString(),List.class);
			}
			String systemScmId = backMap.get("systemScmId").toString();//tbl_system_scm 主键
			String systemId = backMap.get("systemId").toString();
			// 首先判断是否是定时任务
			TblSystemJenkinsJobRun jorRun = iStructureService.selectBuildMessageById(Long.parseLong(runId));
			String envflag = "";
			if (jorRun.getEnvironmentType() != null) {
				envflag = String.valueOf(jorRun.getEnvironmentType());
			}
			List<TblSystemModule> moduleLog=iStructureService.selectSystemModule(Long.parseLong(systemId));
			TblSystemJenkins tblSystemJenkinsLog = iStructureService.selectSystemJenkinsById(systemJenkinsId);
			TblToolInfo jenkinsTool = iStructureService.geTblToolInfo(Integer.parseInt(jenkinsToolId));
			String nowJobNumber=backMap.get("nowJobNumber").toString();//jenkins发送的job执行编号
			String jobNumberTemp=backMap.get("jobNumber").toString();//job执行编号
			if (scheduled != null && scheduled.equals("true") && !jobNumberTemp.equals(nowJobNumber)) {
            	// 定时任务（创建历史表）
				TblSystemJenkinsJobRun ttr = new TblSystemJenkinsJobRun();
				ttr.setSystemJenkinsId(jorRun.getSystemJenkinsId());
				ttr.setSystemId(jorRun.getSystemId());
				ttr.setJobName(jorRun.getJobName());
				ttr.setRootPom(".");
				ttr.setBuildStatus(1);// 正常
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
				String timeStatus = result.get("status");//任务结果
				String timeLog = result.get("log");//任务日志
				ttr.setBuildLogs(timeLog);
				ttr.setBuildStatus(Integer.parseInt(timeStatus));
				
				Map<String, Object> saveDataMap = new HashMap<String, Object>();
				iStructureService.getJenkinsStageLog(ttr, jenkinsTool, tblSystemJenkinsLog, jobName, saveDataMap);
				String moduleJson=backMap.get("moduleJson").toString();
				if (envflag.equals(Constants.PRDIN) || envflag.equals(Constants.PRDOUT)) {//生产环境
					iStructureService.getModuleRunCallBack(moduleRunIds, endTime,startTime,moduleJson,saveDataMap);
				} else {
					if(backMap.get("sonarToolId")!=null && timeStatus.equals("2")) {//sonar工具id
						String sonarToolId = backMap.get("sonarToolId").toString();
						iStructureService.sonarDetail(moduleRunJson, endTime, sonarToolId, "2",startTime,moduleJson,saveDataMap);// 2为定时任务
					}else{ //没有soanr扫描或失败时调用
						iStructureService.getModuleRunCallBack(moduleRunIds, endTime,startTime,moduleJson, saveDataMap);
					}
				}
				
				((StructureNotify)AopContext.currentProxy()).saveCallBackData(backMap, ttr, "2", saveDataMap, 1);
				callbackLog.info("定时自动构建完成");

			} else {
				TblSystemJenkinsJobRun tblSystemJenkinsJobRun = new TblSystemJenkinsJobRun();
				tblSystemJenkinsJobRun.setSystemId(Long.parseLong(systemId));
				tblSystemJenkinsJobRun.setStartDate(startTime);
				tblSystemJenkinsJobRun.setEndDate(endTime);
				tblSystemJenkinsJobRun.setSystemJenkinsId(Long.parseLong(systemJenkinsId));
				tblSystemJenkinsJobRun.setId(Long.parseLong(runId));
				tblSystemJenkinsJobRun.setLastUpdateDate(endTime);
				
				String status = "";
				// 获取jenkinsTool 和jobName
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
				if (scheduled != null && scheduled.equals("true")) {//是否为定时
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
				if ((backMap.get("sonarflag") != null && backMap.get("sonarflag").toString().equals("1")) || (scheduled != null && scheduled.equals("true")) ) {//是否需要sonar
					if(backMap.get("sonarToolId")!=null && status.equals("2")) {
						String sonarToolId = backMap.get("sonarToolId").toString();
						iStructureService.sonarDetail(moduleRunJson, endTime, sonarToolId, "1", startTime,moduleJson, saveDataMap);
					}else{
						//sonar失败情况下更新module数据
						iStructureService.getModuleRunCallBack(moduleRunIds, endTime,startTime,moduleJson, saveDataMap);
					}
				} else {
					iStructureService.getModuleRunCallBack(moduleRunIds, endTime,startTime,moduleJson, saveDataMap);
				}
				((StructureNotify)AopContext.currentProxy()).saveCallBackData(backMap, tblSystemJenkinsJobRun,"1", saveDataMap, 2);
				
				//发送消息
				sendMessage(tblSystemJenkinsJobRun,backMap,"1");
				
				callbackLog.info("自动构建完成");
			}
		} catch (Exception e) {
			this.handleException(e);
		}

	}

	/**
	 * 将插入及更新表的语句统一起来，方便事务处理。避免事务执行时间太长。
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
		callbackLog.info("callBackJenkinsLog定时回调>>>>>transaction开始");
		String timeLog = tblSystemJenkinsJobRun.getBuildLogs();
		tblSystemJenkinsJobRun.setBuildLogs("");//日志保存到s3
		if (type == 1) {//callBackJenkinsLog定时自动构建回调
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
				iStructureService.saveJenkinsStageLog(saveStageList);//保存StageView的信息
			}
			//sonarDetail()方法返回的数据
			List<TblSystemModuleJenkinsJobRun> saveJobRunList = (List<TblSystemModuleJenkinsJobRun>)saveDataMap.get("saveJobRunList");
			if (saveJobRunList != null) {
				iStructureService.saveModuleRunCallBack(jobid, flag, saveJobRunList);
			}
		} else if (type == 2) {//callBackJenkinsLog自动构建回调
			String key=	s3Util.putObjectLogs(logBucket,tblSystemJenkinsJobRun.getId()+"jobRun",timeLog);
			String bucketName=logBucket;
			tblSystemJenkinsJobRun.setBuildLogs(key+","+bucketName);
			//更新构建日志信息
			iStructureService.updateJobRun(tblSystemJenkinsJobRun);
			//构建成功，更新制品包信息
			if (tblSystemJenkinsJobRun.getBuildStatus() == 2) {
				detailPackage(backMap, tblSystemJenkinsJobRun.getId(), tblSystemJenkinsJobRun.getSystemId());
			}
			
			List<String> moduleList = (List<String>)saveDataMap.get("moduleList");
			if (moduleList != null) {
				iStructureService.updateModuleInfoFristCompile(moduleList);
			}
			TblSystemScm tblSystemScm = (TblSystemScm)saveDataMap.get("tblSystemScm");
			if (tblSystemScm != null) {// 更新scm表数据
				iStructureService.updateSystemScmBuildStatus(tblSystemScm);// 1空闲 2构建中
			}
			List<TblSystemJenkinsJobRunStage> saveStageList = (List<TblSystemJenkinsJobRunStage>)saveDataMap.get("saveStageList");
			if (saveStageList != null) {
				iStructureService.saveJenkinsStageLog(saveStageList);//保存StageView的信息
			}
			List<TblSystemModuleJenkinsJobRun> saveJobRunList = (List<TblSystemModuleJenkinsJobRun>)saveDataMap.get("saveJobRunList");
			if (saveJobRunList != null) {
				iStructureService.saveModuleRunCallBack(tblSystemJenkinsJobRun.getId(), flag, saveJobRunList);
			}
		} else if (type == 3) {//callBackManualJenkins手动构建回调
			String key=	s3Util.putObjectLogs(logBucket,tblSystemJenkinsJobRun.getId()+"jobRun",timeLog);
		    String bucketName=logBucket;
		    tblSystemJenkinsJobRun.setBuildLogs(key+","+bucketName);
			iStructureService.updateJobRun(tblSystemJenkinsJobRun);//根据id更新记录
			
			TblSystemJenkins tblSystemJenkins = (TblSystemJenkins)saveDataMap.get("tblSystemJenkins");
			if (tblSystemJenkins != null) {// 更新
				iStructureService.updateJenkins(tblSystemJenkins);
			}
			TblSystemSonar tblSystemSonar = (TblSystemSonar)saveDataMap.get("tblSystemSonar");
			if (tblSystemSonar != null) {// 更新
				tblSystemSonarMapper.updateById(tblSystemSonar);
			}
			List<TblSystemJenkinsJobRunStage> saveStageList = (List<TblSystemJenkinsJobRunStage>)saveDataMap.get("saveStageList");
			if (saveStageList != null) {
				iStructureService.saveJenkinsStageLog(saveStageList);//保存StageView的信息
			}
			List<TblSystemModuleJenkinsJobRun> saveJobRunList = (List<TblSystemModuleJenkinsJobRun>)saveDataMap.get("saveJobRunList");
			if (saveJobRunList != null) {
				iStructureService.saveModuleRunCallBack(tblSystemJenkinsJobRun.getId(), flag, saveJobRunList);
			}
		} else if (type == 4) {//callBackManualDepolyJenkins手动部署回调
			String key=	s3Util.putObjectLogs(logBucket,tblSystemJenkinsJobRun.getId()+"jobRun",timeLog);
			String bucketName=logBucket;
			tblSystemJenkinsJobRun.setBuildLogs(key+","+bucketName);
			iStructureService.updateJobRun(tblSystemJenkinsJobRun);// 根据id更新记录
			
			TblSystemJenkins tblSystemJenkins = (TblSystemJenkins)saveDataMap.get("tblSystemJenkins");
			if (tblSystemJenkins != null) {// 更新
				iStructureService.updateJenkins(tblSystemJenkins);
			}
			List<TblSystemJenkinsJobRunStage> saveStageList = (List<TblSystemJenkinsJobRunStage>)saveDataMap.get("saveStageList");
			if (saveStageList != null) {
				iStructureService.saveJenkinsStageLog(saveStageList);//保存StageView的信息
			}
			TblSystemModuleJenkinsJobRun tmjr = (TblSystemModuleJenkinsJobRun)saveDataMap.get("tblSystemModuleJenkinsJobRun");
			if (tmjr != null) {
				iStructureService.updateModuleJonRun(tmjr);
			}
			// 任务状态修改
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
			
		} else if (type == 5) {//callBackAutoDepolyJenkins定时自动部署回调
			long jobid = iStructureService.insertJenkinsJobRun(tblSystemJenkinsJobRun);
			String key=	s3Util.putObjectLogs(logBucket,jobid+"jobRun",timeLog);
			String bucketName=logBucket;
			tblSystemJenkinsJobRun.setBuildLogs(key+","+bucketName);
			tblSystemJenkinsJobRunMapper.updateById(tblSystemJenkinsJobRun);
			
			Map<String,Object> autoMap = (Map<String,Object>)saveDataMap.get("autoMap");
			if (autoMap != null) {
				autoMap.put("jobRunId",jobid);
				callbackLog.info("定时jorunid=" + jobid + "-" + tblSystemJenkinsJobRun.getId());
			}
			List<TblSystemJenkinsJobRunStage> saveStageList = (List<TblSystemJenkinsJobRunStage>)saveDataMap.get("saveStageList");
			if (saveStageList != null) {
				saveStageList.forEach(stage->{
					stage.setSystemJenkinsJobRunId(jobid);
				});
				iStructureService.saveJenkinsStageLog(saveStageList);//保存StageView的信息
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
					// 插入数据
					moduleJobrun.setSystemJenkinsJobRun(jobid);
					moduleJobrun.setId(null);
					iStructureService.insertJenkinsModuleJobRun(moduleJobrun);
				}
			}
		} else if (type == 6) {//callBackAutoDepolyJenkins自动部署回调
			// 根据id更新记录
			String key=	s3Util.putObjectLogs(logBucket,tblSystemJenkinsJobRun.getId()+"jobRun",timeLog);
			String bucketName=logBucket;
			tblSystemJenkinsJobRun.setBuildLogs(key+","+bucketName);
			iStructureService.updateJobRun(tblSystemJenkinsJobRun);
			
			List<String> moduleList = (List<String>)saveDataMap.get("moduleList");
			if (moduleList != null) {
				iStructureService.updateModuleInfoFristCompile(moduleList);
			}
			TblSystemScm tblSystemScm = (TblSystemScm)saveDataMap.get("tblSystemScm");
			if (tblSystemScm != null) {// 更新scm表数据
				iStructureService.updateSystemScmBuildStatus(tblSystemScm);// 1空闲 2构建中
			}
			List<TblSystemJenkinsJobRunStage> saveStageList = (List<TblSystemJenkinsJobRunStage>)saveDataMap.get("saveStageList");
			if (saveStageList != null) {
				iStructureService.saveJenkinsStageLog(saveStageList);//保存StageView的信息
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
			// 任务状态修改
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
			
		} else if (type == 7) {//callBackPackageDepolyJenkins自动包件部署回调
			String key=	s3Util.putObjectLogs(logBucket,tblSystemJenkinsJobRun.getId()+"jobRun",timeLog);
		    String bucketName=logBucket;
			tblSystemJenkinsJobRun.setBuildLogs(key+","+bucketName);
			iStructureService.updateJobRun(tblSystemJenkinsJobRun);
			
			TblSystemJenkins tblSystemJenkins = (TblSystemJenkins)saveDataMap.get("tblSystemJenkins");
			if (tblSystemJenkins != null) {// 更新
				iStructureService.updateJenkins(tblSystemJenkins);
			}
			List<TblSystemJenkinsJobRunStage> saveStageList = (List<TblSystemJenkinsJobRunStage>)saveDataMap.get("saveStageList");
			if (saveStageList != null) {
				iStructureService.saveJenkinsStageLog(saveStageList);//保存StageView的信息
			}
			List<TblSystemModuleJenkinsJobRun> jobRunList = (List<TblSystemModuleJenkinsJobRun>)saveDataMap.get("jobRunList");
			if (jobRunList != null) {
				for (TblSystemModuleJenkinsJobRun tmjr : jobRunList) {
					iStructureService.updateModuleJonRun(tmjr);
				}
			}
			// 任务状态修改
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
		callbackLog.info("callBackJenkinsLog定时回调>>>>>transaction结束");
	}



	private Timestamp getStartDate(TblToolInfo jenkinsTool, String jobName) throws Exception {
		return jenkinsBuildService.getJobStartDate(jenkinsTool, jobName);


	}

	/**
	 * 手工构建回调
	 * @author weiji
	 * @param jsonMap 回调参数
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
			// 更新tblsystemjenkins表数据
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
			if (backMap.get("sonarName") == null) {//sonar名称
			} else {
				sonarName = backMap.get("sonarName").toString();
				// 更新sonar相关信息
				String tblSystemSonar = backMap.get("tblSystemSonar").toString();
				iStructureService.sonarDetailManual(tblSystemJenkins.getJobName(), tblSystemSonar, timestamp,
						sonarName, moduleJobRunId, "1", saveDataMap);
			}
			((StructureNotify)AopContext.currentProxy()).saveCallBackData(backMap, tblSystemJenkinsJobRun, "1", saveDataMap, 3);

			//发送消息
			sendMessage(tblSystemJenkinsJobRun,backMap,"1");
			callbackLog.info("手动构建完成");

		} catch (Exception e) {
			this.handleException(e);
			throw e; 
		}

	}
	/**
	 * 手动部署回调
	 * @author weiji
	 * @param jsonMap 回调参数
	 * @return Map<String, Object>
	 */

	@RequestMapping(value = "callBackManualDepolyJenkins", method = RequestMethod.POST)
	public void callBackManualDepolyJenkins(String jsonMap) throws Exception {

		try {
			Map<String, Object> backMap = JSONObject.parseObject(jsonMap, Map.class);
			String moduleJobRunId = backMap.get("moduleJobRunId").toString();//tbl_system_module_jenkins_job_run 主键
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			String status = "";
			long jobRunId = Long.parseLong(backMap.get("jobRunId").toString());//tbl_system_jenkins_job_run 主键
			TblSystemJenkinsJobRun tJobRun = iStructureService.selectBuildMessageById(jobRunId);
			Map<String, Object> sjmap = new HashMap<>();
			sjmap.put("id", backMap.get("systemJenkinsId"));//tbl_system_jenkins 主键
			List<TblSystemModule> moduleLog=new ArrayList<>();
			TblSystemJenkins tblSystemJenkins = iStructureService.getSystemJenkinsByMap(sjmap).get(0);
			
			// 更新tblsystemjenkins表数据
			String scheduled = null;
			if (backMap.get("scheduled") == null) { //是否定时

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
			//判断是否有任务编号（后期加上的）
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
			
			status = result.get("status");//任务结果
			String log = result.get("log");//任务日志
			log=iStructureService.detailByteBylog(log,Constants.MAX_ALLOW_PACKET);
			tblSystemJenkinsJobRun.setBuildLogs(log);
			tblSystemJenkinsJobRun.setBuildStatus(Integer.parseInt(status));
			
			iStructureService.getJenkinsStageLog(tJobRun, jenkinsTool, tblSystemJenkins, tblSystemJenkins.getJobName(), saveDataMap);
			// 更新modulejobrun表
			TblSystemModuleJenkinsJobRun tmjr = new TblSystemModuleJenkinsJobRun();
			tmjr.setLastUpdateDate(timestamp);
			tmjr.setId(Long.parseLong(moduleJobRunId));
			tmjr.setBuildStatus(Integer.parseInt(status));
            //String moduleJson=backMap.get("moduleJson").toString();
			saveDataMap.put("tblSystemModuleJenkinsJobRun", tmjr);
			
			
			// 开发任务更新
			Map<String, String> taskUpdateMap = new HashMap<String, String>();
			String reqFeatureqIds = backMap.get("reqFeatureqIds") == null ? null : backMap.get("reqFeatureqIds").toString();//开发任务
			String taskEnvType = backMap.get("taskEnvType") == null ? null : backMap.get("taskEnvType").toString();
			if (reqFeatureqIds != null && !reqFeatureqIds.equals("") &&  taskEnvType != null && !taskEnvType.equals("")) {
				String userId = backMap.get("userId").toString();//用户id
				String userName = backMap.get("userName").toString();//用户名称
				String userAccount = backMap.get("userAccount").toString();//账号
				Map<String, Object> sendMap = new HashMap<>();
				sendMap.put("userId", userId);
				sendMap.put("userAccount", userAccount);
				sendMap.put("userName", userName);
				String sendTask = JSON.toJSONString(sendMap);
				Map<String, Object> remap = new HashMap<>();
				remap.put("reqFeatureIds", reqFeatureqIds);//开发任务id
				remap.put("requirementFeatureFirstTestDeployTime", new Timestamp(new Date().getTime()));
				String json = JsonUtil.toJson(remap);
				taskUpdateMap.put("reqFeatureqIds", reqFeatureqIds);
				taskUpdateMap.put("taskEnvType", taskEnvType);//手动选择的环境类型
				taskUpdateMap.put("sendTask", sendTask);
				taskUpdateMap.put("status", status);
				taskUpdateMap.put("json", json);
			}
			saveDataMap.put("taskUpdateMap", taskUpdateMap);
			
			
			
			((StructureNotify)AopContext.currentProxy()).saveCallBackData(backMap, tblSystemJenkinsJobRun, "", saveDataMap, 4);
			
			//发送消息
			sendMessage(tblSystemJenkinsJobRun,backMap,"2");
			callbackLog.info("手动部署完成");

		} catch (Exception e) {
			this.handleException(e);
			throw e;
		}

	}

	/**
	 * 自动部署回调
	 * @author weiji
	 * @param jsonMap 回调参数
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "callBackAutoDepolyJenkins", method = RequestMethod.POST)
	public void callBackAutoDepolyJenkins(String jsonMap) throws IOException, InterruptedException, SonarQubeException {

		try {
			//判断是否直接调用message
			boolean messageflag=true;
		    Map<String, Object> backMap = JSON.parseObject(jsonMap, Map.class);
			Timestamp startTime=formateDate(backMap);
			Timestamp endTime=formateEndDate(backMap);
			String jobNumber=backMap.get("jobNumber").toString();//执行编号
			String runId = backMap.get("runId").toString();//tbl_system_jenkins_job_run 主键
			String systemJenkinsId = backMap.get("systemJenkinsId").toString();//tbl_system_jenkins 主键
			String jenkinsToolId = backMap.get("jenkinsToolId").toString();//tbl_tool_info 主键
			String envType = null;
			//获取环境参数
			if (backMap.get("envType") != null) {//环境类型
				envType = backMap.get("envType").toString();
			}
			List<Integer> moduleRunIds = new ArrayList<>();
			//获取子模块ids
			if (backMap.get("moduleRunIds") != null) {//tbl_system_module_jenkins_job_run 主键
				moduleRunIds = JSON.parseObject(backMap.get("moduleRunIds").toString(),List.class);
			}
			String moduleRunJson = backMap.get("moduleRunJson").toString();
			String systemScmId = backMap.get("systemScmId").toString();//tbl_system_scm 主键
			String reqFeatureqIds = null;
			//获取开发任务ids
			if (backMap.get("reqFeatureqIds") != null) {
				reqFeatureqIds = backMap.get("reqFeatureqIds").toString();
			}
			String systemId = backMap.get("systemId").toString();
			List<TblSystemModule> moduleLog=iStructureService.selectSystemModule(Long.parseLong(systemId));
			String scheduled = null;
			if (backMap.get("scheduled") != null) {//是否定时
				scheduled = backMap.get("scheduled").toString();
			}
			// 首先判断是否是定时任务
			TblSystemJenkinsJobRun jorRun = iStructureService.selectBuildMessageById(Long.parseLong(runId));
			// 获取日志
			TblSystemJenkins tblSystemJenkinsLog = iStructureService.selectSystemJenkinsById(systemJenkinsId);
			//自动化测试数据
			List<String> moduleTestList=new ArrayList<>();
			if(backMap.get("moduleList")!=null) {
				moduleTestList = (List<String>) backMap.get("moduleList");

			}
			//定时需要modulelist和envtype也需要
             //api 自动化测试
			List<Map<String,Object>> testConfig=deployService.getTestConfig( Long.parseLong(systemId), moduleTestList,  envType,"1");
			//ui自动化测试
			List<Map<String,Object>> uiTestConfig=deployService.getTestConfig( Long.parseLong(systemId), moduleTestList,  envType,"2");
			Map<String,Object> autoMap=new HashMap<>();
			autoMap.put("systemId",systemId);
			autoMap.put("testConfig",testConfig);//自动化测试配置
			autoMap.put("uiTestConfig",uiTestConfig);//ui自动化测试
			autoMap.put("deployType","1");//源码部署
			autoMap.put("scmId",systemScmId);
			autoMap.put("jobRunId",runId);
			autoMap.put("environnmentType",envType);
			autoMap.put("jenkinsId",systemJenkinsId);
			autoMap.put("taskMapFlag",1);//默认不修改任务状态 1 不需要 2需要
			autoMap.put("autoTest","false");//是否自动化测试
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
				// 定时任务（创建历史表）
				// Timestamp time = Timestamp.valueOf(startDate);
				TblSystemJenkinsJobRun ttr = new TblSystemJenkinsJobRun();
				ttr.setSystemJenkinsId(jorRun.getSystemJenkinsId());
				ttr.setSystemId(jorRun.getSystemId());
				ttr.setJobName(jorRun.getJobName());
				ttr.setRootPom(".");
				ttr.setBuildStatus(1);// 正常
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
				//获取日志接口
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

				if (uiTestConfig.size()>0 && timeStatus.equals("2") ){//ui自动化测试
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
				callbackLog.info("定时自动部署完成");

			} else {
				Map<String, Object> saveDataMap = new HashMap<String, Object>();
 				TblSystemJenkinsJobRun tblSystemJenkinsJobRun = new TblSystemJenkinsJobRun();
 				tblSystemJenkinsJobRun.setSystemId((long) Integer.parseInt(systemId));
				tblSystemJenkinsJobRun.setSystemJenkinsId(Long.parseLong(systemJenkinsId));
				tblSystemJenkinsJobRun.setId(Long.parseLong(runId));
				tblSystemJenkinsJobRun.setStartDate(startTime);
				String status = "";
				// 获取jenkinsTool 和jobName
				Thread.sleep(8000);
				String jobName = jorRun.getJobName();
				Map<String, String> result = jenkinsBuildService.getLastBuildLog(jenkinsTool, tblSystemJenkinsLog, jobName,moduleLog);
				status = result.get("status");//任务结果
				String log = result.get("log");//任务日志
				log=iStructureService.detailByteBylog(log,Constants.MAX_ALLOW_PACKET);
				tblSystemJenkinsJobRun.setBuildLogs(log);
				// 更新scm表数据
				if (scheduled != null && scheduled.equals("true")) {

				} else {
					//修改是否第一次编译
					if(backMap.get("moduleList")!=null && status.equals("2")) {
						List<String> moduleList = (List<String>) backMap.get("moduleList");
						saveDataMap.put("moduleList", moduleList);
					}
					TblSystemScm tblSystemScm = iStructureService.getTblsystemScmById(Long.parseLong(systemScmId));
					tblSystemScm.setDeployStatus(1);
					tblSystemScm.setId(Long.parseLong(systemScmId));
					if ((testConfig.size()>0 || uiTestConfig.size()>0)&& status.equals("2")){
						//自动化测试中
						tblSystemScm.setDeployStatus(3);
						autoMap.put("autoTest","true");
					}else{
						//空闲
						tblSystemScm.setDeployStatus(1);
					}
					saveDataMap.put("tblSystemScm", tblSystemScm);
				}

				callbackLog.info("部署开始:envtype"+envType+"scheduled"+scheduled+"reqFeatureqIds"+reqFeatureqIds+"status"+status);
				iStructureService.getJenkinsStageLog(jorRun, jenkinsTool, tblSystemJenkinsLog, jobName, saveDataMap);
				
				if((testConfig.size()>0 || uiTestConfig.size()>0) && status.equals("2")){ //只有部署成功才会测试
                    String moduleJson=backMap.get("moduleJson").toString();
                    log=addTestTime(log);
					tblSystemJenkinsJobRun.setBuildStatus(4);//自动化测试中 只更新日志
                    //更新module状态
                    updateModuleStatus(moduleJson,4,startTime,endTime,saveDataMap);
				}else {
                    String moduleJson=backMap.get("moduleJson").toString();
					tblSystemJenkinsJobRun.setBuildStatus(Integer.parseInt(status));
					tblSystemJenkinsJobRun.setEndDate(endTime);
					tblSystemJenkinsJobRun.setLastUpdateDate(endTime);
					iStructureService.getModuleRunCallBack(moduleRunIds, endTime,startTime,moduleJson,saveDataMap);
				}
				
				// 任务状态修改
				Map<String, String> taskUpdateMap = new HashMap<String, String>();
				if (reqFeatureqIds != null && !reqFeatureqIds.equals("") ) {
	                autoMap.put("reqFeatureqIdsEmail",reqFeatureqIds);
					Map<String, Object> sendMap = new HashMap<>();
					String userId = backMap.get("userId").toString();
					String userAccount = backMap.get("userAccount").toString();
					String userName = backMap.get("userName").toString();
					sendMap.put("userId", userId);
					sendMap.put("status", status);
					sendMap.put("envType", envType);//环境类型
					sendMap.put("userAccount", userAccount);//用户账号
					sendMap.put("userName", userName);//用户名称
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

				// 更新状态
				if (status.equals(Constants.DEPLOY_SUCCESS)) {
					if (scheduled != null && scheduled.equals("true")) {
						// 定时任务不需要调用自动化运维平台接口 需要调用自动化测试接口
						autoMap.put("scheduled","true");

						if (testConfig.size()>0 ){//api自动化测试
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


						if (uiTestConfig.size()>0 ){//ui自动化测试
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
							// 调用自动化运维平台接口
							String versionInfo = backMap.get("versionInfo").toString();//版本
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
									autoMap.put("testType","1");//自动化测试类型
									Map<String,Object> paramMap = new HashMap<String,Object>(){{
										putAll(autoMap);
									}};
									threadPool.submit(new MyRun(paramMap));
								}
							}


							if (uiTestConfig.size()>0 ){//ui自动化测试
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
			//修改测试任务部署时间
                if(status.equals("2")) {
                    detailEnvDate(reqFeatureqIds, envType, endTime);
                }
            //发送消息
			if(messageflag) {
				sendMessage(tblSystemJenkinsJobRun, backMap, "2");
			}
			callbackLog.info("自动部署完成");
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
	 * @Description: 组装需要更新的模块信息
	 * @author author
	 * @param moduleJson 模块信息
	 * @param buildStatus
	 * @param startTime 创建时间
	 * @param endTime 更新时间
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
	 * 处理自动化运维平台数据
	 * @author weiji
	 * @param backMap 参数
	 * @param systemId 系统id
	 * @param microList 微服务
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
					//微服务
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
	* @Description: 调用自动化运维平台
	* @author author
	* @param systemId 系统id
	* @param envType 环境
	* @param reqFeatureqIds 开发任务id
	* @param versionInfo 版本信息
	* @param proDuctionDeploytype 生产部署状态
	* @param systemPackageName jar包名
	* @param microList 模块
	* @param runId
	* @param autoMap void
	 */
	private void autoPlantForm(Long systemId, String envType, String reqFeatureqIds, String versionInfo,
			String proDuctionDeploytype,String systemPackageName,List<Map<String,Object>> microList,String runId,Map<String,Object> autoMap) {

		if (envType != null && proDuctionDeploytype != null && versionInfo != null) {

			//只有两个环境PRD-IN和PRD-OUT并且部署成功后才回调
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
	 * 制品部署回调
	 * @author weiji
	 * @param jsonMap 参数
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "callBackPackageDepolyJenkins", method = RequestMethod.POST)
	public void callBackPackageDepolyJenkins(String jsonMap) throws Exception {

		try {
			//判断是否直接调用message
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
			//api 自动化测试
			List<Map<String,Object>> testConfig=deployService.getTestConfig( Long.parseLong(systemId), moduleTestList,  envType,"1");
			//ui 自动化测试
			List<Map<String,Object>> uiTestConfig=deployService.getTestConfig( Long.parseLong(systemId), moduleTestList,  envType,"2");
			Map<String,Object> autoMap=new HashMap<>();

			autoMap.put("systemId",systemId);
			autoMap.put("uiTestConfig",uiTestConfig);
			autoMap.put("testConfig",testConfig);
			autoMap.put("deployType","2");//制品部署
			autoMap.put("jenkinsId",tblSystemJenkins.getId());
			autoMap.put("jobRunId",jobRunId);
			autoMap.put("environnmentType",envType);
			autoMap.put("taskMapFlag",1);//默认不修改任务状态 1 不需要 2需要
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
					tblSystemJenkins.setDeployStatus(3);//自动化测试中
					tblSystemJenkinsJobRun.setBuildStatus(4);//自动化测试中
					autoMap.put("autoTest","true");
				}else {
					tblSystemJenkinsJobRun.setLastUpdateDate(timestamp);
					tblSystemJenkinsJobRun.setEndDate(endTime);
					tblSystemJenkins.setDeployStatus(1);
				}
				saveDataMap.put("tblSystemJenkins", tblSystemJenkins);
				
				iStructureService.getJenkinsStageLog(tJobRun, jenkinsTool, tblSystemJenkins, tblSystemJenkins.getJobName(), saveDataMap);
				
			    String moduleJson=backMap.get("moduleJson").toString();
				// 更新modulejobrun表
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
					//更新module状态
					updateModuleRunInfo(moduleJson,tmjr);
				}
				saveDataMap.put("jobRunList", jobRunList);
				
				// 任务状态修改
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
						// 调用自动化平台
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

						if (uiTestConfig.size()>0 ){//ui自动化测试
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
			//发送消息
			if(messageflag) {
				sendMessage(tblSystemJenkinsJobRun, backMap, "2");
			}
			callbackLog.info("制品部署完成");

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
	* @Description: 制品包件信息
	* @author author
	* @param backMap 回调参数
	* @param runId
	* @param systemId 系统id
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
			if (checkModuleList == null || checkModuleList.size() <= 0) {//单模块
				String artifactId = tblSystemInfo.getArtifactId();
				String groupId = tblSystemInfo.getGroupId();
				String snapShotName = tblSystemInfo.getSnapshotRepositoryName();
				String releaseName = tblSystemInfo.getReleaseRepositoryName();
				tagNexusAssetBO = getNexusAssetBO(nexusUtil, artifactId, groupId, snapShotName, releaseName, artType, version);
				addArtifactData(backMap, systemId, tblSystemJenkinsJobRun, moduleJobRunList, artType, tagNexusAssetBO, null);
			} else {
				//多模块
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
					//从仓库中获取最新的制品信息
					tagNexusAssetBO = getNexusAssetBO(nexusUtil, artifactId, groupId, snapShotName, releaseName, artType, version);
					addArtifactData(backMap, systemId, tblSystemJenkinsJobRun, moduleJobRunList, artType, tagNexusAssetBO, Long.parseLong(moduleId));
				}
			}

		}
	}

	/**
	 * 
	* @Title: addArtifactData
	* @Description: 从私服中获取最新包件信息然后更新系统的包件信息
	* @author author
	* @param backMap
	* @param systemId 系统id
	* @param tblSystemJenkinsJobRun 子系统的Jenkisn记录
	* @param moduleJobRunList  模块的jenkins记录
	* @param artType 1新增，2修改
	* @param tagNexusAssetBO 私服仓库中的制品
	* @param moduleId 模块id
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
	* @Description: 从制品库中获取制品信息
	* @author author
	* @param nexusUtil 
	* @param artifactId 制品id
	* @param groupId
	* @param snapShotName 闪照仓库
	* @param releaseName 正式仓库
	* @param artType 类型1快照，2正式
	* @param version 版本号
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
		if (artType.equals("1")) {//快照
			//仓库名
			nexusSearchVO.setRepository(snapShotName);
			//版本号
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
		
		//取出最新一条数据
		if(listBo.size()>0){
			tagNexusAssetBO=listBo.get(0);
		}
		return tagNexusAssetBO;
	}

	public void handleException(Exception e) {
		e.printStackTrace();
		callbackLog.error(e.getMessage(), e);

	}



	//将构建结果发送消息给对应的人员
	private void sendMessage(TblSystemJenkinsJobRun tblSystemJenkinsJobRun,Map<String,Object> backMap ,String jobType){
		//TblSystemInfo tblSystemInfo=iStructureService.geTblSystemInfo(tblSystemJenkinsJobRun.getSystemId());
		/* 改造项目和系统关系 liushan  */
		TblSystemInfo tblSystemInfo = iStructureService.getTblSystemInfoById(tblSystemJenkinsJobRun.getSystemId());
        String messageTitle="";
        String messageContent="";
		if(tblSystemInfo.getProjectIds() != null && !tblSystemInfo.getProjectIds().equals("") && backMap.get("userId")!=null && backMap.get("envName")!=null ) {
			String creatUserId = backMap.get("userId").toString();
			String envName = backMap.get("envName").toString();
			Map<String, Object> map = new HashMap<>();
			String stauts = "";

            if(jobType.equals("1")){
                messageTitle="["+tblSystemInfo.getSystemName()+"]" + "-" +  (tblSystemJenkinsJobRun.getBuildStatus() == 2 ? "构建成功" : "构建失败");
//                if(tblSystemJenkinsJobRun.getBuildStatus() == 3){
//					messageContent = tblSystemInfo.getSystemName() + "-" + envName + "-" + "构建失败"+","+detailBuildCode(tblSystemInfo,messageContent);
//
//				}else {
//					messageContent = tblSystemInfo.getSystemName() + "-" + envName + "-" + (tblSystemJenkinsJobRun.getBuildStatus() == 2 ? "构建成功" : "构建失败") + "开始时间" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tblSystemJenkinsJobRun.getStartDate()) + "-结束时间" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tblSystemJenkinsJobRun.getEndDate());
//				}
				messageContent = tblSystemInfo.getSystemName() + "-" + envName + "-" + (tblSystemJenkinsJobRun.getBuildStatus() == 2 ? "构建成功" : "构建失败") + "开始时间" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tblSystemJenkinsJobRun.getStartDate()) + "-结束时间" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tblSystemJenkinsJobRun.getEndDate());
            }else {
                messageTitle = "[" + tblSystemInfo.getSystemName() + "]" + "-" + (tblSystemJenkinsJobRun.getBuildStatus() == 2 ? "部署成功" : "部署失败");
                messageContent = tblSystemInfo.getSystemName() + "-" + envName + "-" + (tblSystemJenkinsJobRun.getBuildStatus() == 2 ? "部署成功" : "部署失败") + "开始时间" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tblSystemJenkinsJobRun.getStartDate()) + "-结束时间" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tblSystemJenkinsJobRun.getEndDate());

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


			//获取该系统下项目
			map.put("messageReceiver", userIds);
			map.put("createBy", creatUserId);
			//发送系统内消息
			devManageToSystemInterface.insertMessage(JSON.toJSONString(map));
			//发送邮件邮箱
			Map<String, Object> emWeMap = new HashMap<>();
            emWeMap.put("sendMethod","3");
            emWeMap.put("messageContent",messageContent);
            emWeMap.put("messageTitle",messageTitle);
			Map<String,Object> param=new HashMap<>();
			if(jobType.equals("1") && tblSystemJenkinsJobRun.getBuildStatus() == 3){//构建失败暂且不发送邮件


			}else {
			//查询此次 项目管理岗、开发管理岗、配置管理员
			param.put("roleName", "项目管理岗");
			param.put("userIds", Arrays.asList(userIds.split(",")));
			param.put("projectIds", Arrays.asList(tblSystemInfo.getProjectIds().split(",")));
			List<String> pList = tblUserInfoMapper.findRoleByUserIds(param);
			param.put("roleName", "开发管理岗");
			List<String> devList = tblUserInfoMapper.findRoleByUserIds(param);
			param.put("roleName", "配置管理");
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
         	if(type.equals("1")){//敏态
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
			 messageContent=messageContent+"涉及开发工作任务如下:"+workTaskCode+","+"涉及代码文件如下:"+code;
		 }
             return messageContent;
	}


	/**
	 * 
	* @Title: updateModuleRunInfo
	* @Description: 更新模块Jenkins构建记录
	* @author author
	* @param moduleJson 构建记录信息
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
	* @Description: 更新模块的jenkins定时任务记录
	* @author author
	* @param moduleJson 
	* @param tblSystemModuleJenkinsJobRun
	* @param mId 模块id
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
//				tblSystemModuleJenkinsJobRunMapper.updateById(tblSystemModuleJenkinsJobRun);//把外面insertJenkinsModuleJobRun放后面，这里update就不需要了
				break;
			}



		}

	}
}