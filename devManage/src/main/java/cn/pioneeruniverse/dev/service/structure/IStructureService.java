package cn.pioneeruniverse.dev.service.structure;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.common.sonar.bean.SonarQubeException;
import cn.pioneeruniverse.dev.entity.TblArtifactTag;
import cn.pioneeruniverse.dev.entity.TblProjectInfo;
import cn.pioneeruniverse.dev.entity.TblSprintInfo;
import cn.pioneeruniverse.dev.entity.TblSystemInfo;
import cn.pioneeruniverse.dev.entity.TblSystemJenkins;
import cn.pioneeruniverse.dev.entity.TblSystemJenkinsJobRun;
import cn.pioneeruniverse.dev.entity.TblSystemJenkinsJobRunStage;
import cn.pioneeruniverse.dev.entity.TblSystemModule;
import cn.pioneeruniverse.dev.entity.TblSystemModuleJenkinsJobRun;
import cn.pioneeruniverse.dev.entity.TblSystemModuleScm;
import cn.pioneeruniverse.dev.entity.TblSystemScm;
import cn.pioneeruniverse.dev.entity.TblSystemSonar;
import cn.pioneeruniverse.dev.entity.TblToolInfo;
/**
 *
 * @ClassName: IStructureService
 * @Description: 构建service
 * @author author
 *
 */
public interface IStructureService {
	/**
	 * 获取系统模块
	 * @param id
	 * @return
	 */
	Map<String, Object> getSystemModule(Integer id);
	long  creOrUpdSjenkins(TblSystemJenkins tblSystemJenkins,String env,String createType,String jobType);
	//	TblSystemSonar  creOrUpdSonar(TblSystemSonar tblSystemSonar,long sonarId);
	long  insertJenkinsJobRun(TblSystemJenkinsJobRun tblSystemJenkinsJobRun);
	long  insertJenkinsModuleJobRun(TblSystemModuleJenkinsJobRun tblSystemModuleJenkinsJobRun);
	long  insertJenkinsModuleJobRunNew(TblSystemModuleJenkinsJobRun tblSystemModuleJenkinsJobRun);
	void getJenkinsStageLog(TblSystemJenkinsJobRun jobRun, TblToolInfo tblToolInfo, 
			TblSystemJenkins tblSystemJenkins, String jobName, Map<String, Object> saveDataMap) throws Exception;
	void saveJenkinsStageLog(List<TblSystemJenkinsJobRunStage> saveStageList) throws Exception;
	Map<String, Object> getStageViewHis(Long jobRunId) throws Exception;
	Map<String, Object> getStageViewLogHis(Long jobRunId, Integer describeId) throws Exception;
	Map<String, Object> getStageViewLogDetailHis(Long jobRunId, Integer executionId) throws Exception;
	
	TblSystemScm selectBysystemId(long id);
	TblToolInfo geTblToolInfo(long id);
	void updateJenkinsJobRunLog(String jboname,String log,Integer status);
	TblSystemInfo geTblSystemInfo(long id);
	List<TblSystemScm> getScmByParam(Map<String, Object> map);
	List<TblSystemJenkins> getsystemJenkinsByParam(Map<String, Object> map);
	List<TblSystemModuleScm> getModuleScmByParam(Map<String, Object> map);
	TblSystemModule getTblsystemModule(long id);
	TblSystemModuleScm getSystemModuleScm(long id);
	void updateJobRun(TblSystemJenkinsJobRun tblSystemJenkinsJobRun);
	List<TblSystemJenkinsJobRun> selectLastTimeBySystemId(long id);
	List<TblSystemJenkinsJobRun> selectNowTimeBySystemId(long id);
	List<TblSystemModule> selectSystemModule(long systemId);
	List<TblSystemModuleJenkinsJobRun>	selectLastTimeByModuleId (long moduleId);
	TblSystemJenkinsJobRun	selectBuildMessageById(long jobrunId);
	List<Map<String, Object>> selectMessageBySystemId(long id,String type);
	List<Map<String, Object>> selectMessageBySystemIdAndPage(long id,Integer pageNumber ,Integer pageSize,String type,String jenkinsId,String envType,String flag);
	List<TblProjectInfo> getAllprojectByUser(HttpServletRequest request);
	List<TblProjectInfo> getProjectListBySystemId(Long systemId);
	List<Map<String, Object>> selectModuleBuildMessage(Map<String, Object> map);
	List<Map<String, Object>> selectModuleBuildMessageNow(Map<String, Object> map);
	List<Map<String, Object>> selectModuleBuildMessagesNow(Map<String, Object> map);
	List<TblSystemModuleScm> judge(String moduleid, Long scmId, Integer systemId);
	void updateSystemScmBuildStatus(TblSystemScm t);
	List<TblSystemScm> selectBuildingBySystemid(Long id);
	List<TblSystemScm> judgeScmBuildStatus(String systemIds);
	int countScmBuildStatus(Map<String, Object> map);
	List<TblSystemModuleJenkinsJobRun> selectSonarBySystemidAndDate(Map<String, Object> map);
	List<Map<String, Object>> selectModuleJobRunByjobRunId(long jobRunId);
	int countTaskBySystemId(Long id);
	void sonarDetail(String pKeysMid,Timestamp timestamp,String sonarToolId,
			String  Flag,Timestamp createTime, String moduleJson, Map<String, Object> saveDataMap)throws SonarQubeException;
	TblSystemModuleJenkinsJobRun selectByModuleRunId(long id);
	List<TblSystemModuleJenkinsJobRun> selectSonarBySystemidAndDateMincro(Map<String, Object> map);
	public List<TblSystemSonar> selectSonarByMap(Map<String, Object> columnMap);
	public String getSonarIssue(String toolId,String dateTime,String projectKey,String type,String p) ;
	public List<TblSystemSonar> getSonarByMap(Map<String, Object> columnMap);
	public Long getRandomSonarId(String env);
	List<TblSystemJenkins> selectJenkinsByMap(Map<String, Object> map);
	List<TblSystemJenkins> selectSortJobByMap(Map<String, Object> param);
	List<TblSystemJenkinsJobRun> selectLastTimeBySystemIdManual(Long id);
	void updateJenkins(TblSystemJenkins tblSystemJenkins);
	TblSystemSonar creOrUpdSonarManual(TblSystemSonar tblSystemSonar);
	List<TblSystemJenkins> getSystemJenkinsByMap(Map<String, Object> colMap);
	List<TblSystemModuleJenkinsJobRun> selectSonarBySystemidAndDateManual(Map<String, Object> mapParam);
	void sonarDetailManual(String jobName,String systemSonarId, Timestamp timestamp, 
			String sonarName,String moduleJobRunId,String flag,Map<String, Object> saveDataMap) throws Exception;
	List<Map<String, Object>> selectModuleJobRunByjobRunIdManual(long parseLong);
	List<TblSystemJenkinsJobRun> selectLastTimeBySystemIdManualDeploy(Long id);
	void updateModuleJonRun(TblSystemModuleJenkinsJobRun tmjr);
	List<TblArtifactTag>  getArtifactTag(Map<String, Object> colMap);
	public Map<String, Object> creatJenkinsJob(String sysId, String systemName, String serverType, String modules,
											   String env,String sonarflag, HttpServletRequest request);
	Map<String, Object> creatJenkinsJobBatch(String env, String data, HttpServletRequest request);
	public Map<String, Object> getBuildMessageById(String jobRunId, String createType);
	public Map<String, Object> getSonarMessage(String systemId, String startDate, String endDate);
	public Map<String, Object> getSonarMessageMincro(String systemId, String startDate, String endDate);
	public Map<String, Object> buildJobManual(HttpServletRequest request, String systemJenkisId, String jsonParam);
	public Map<String, Object> getSonarMessageManual(String systemId, String startDate, String endDate);
	public Map<String, Object> getAllSystemInfo(String systemInfo, Integer pageSize, Integer pageNum, String scmBuildStatus,Long uid, String[] refreshIds, 
			String[] checkIds, String[] checkModuleIds,HttpServletRequest request) throws Exception;
	void detailErrorStructure(TblSystemJenkins tblSystemJenkins,String jobType,String flag);
	List<TblSystemScm> selectScmByMap(Map<String, Object> paramMap);
	void detailAutoErrorStructure(TblSystemScm scm,String jobType,String flag,String jenkinsId)  throws Exception;
	Map<String, Object> getAllSystemInfoByBuilding(String ids);
	Map<String, Object> creatJenkinsJobScheduled(String sysId, String systemName, String serverType, String env,
												 HttpServletRequest request,TblSystemJenkins tblSystemJenkins);
	List<TblSystemScm> selectSystemScmByMap(Map<String, Object> param);
	public Map<String, Object> buildJobManualScheduled(HttpServletRequest request, String systemJenkisId);
	//List<TblSystemJenkins> selectCornJenkinsByMap(Map<String, Object> systemJenkinsParam);
	void insertSystemJenkins(TblSystemJenkins tblSystemJenkins);
	public TblToolInfo getEnvtool(String env,String toolType);
	TblSystemJenkins selectSystemJenkinsById(String systemJenkinsId);
	void updateSystemScm(TblSystemScm tblSystemScm);
	void updateModuleStatusBySystemId(Map<String, Object> map);
	void updateModuleScm(Map<String, Object> moduleScmid);
	void updateConfigInfo(Map<String, Object> configMap, Map<String, Object> paramMap);
	public List<Map<String, Object>> getBreakName(String createType,long systemId,String jobType);
	List<TblToolInfo> getTblToolInfo(Map<String, Object> map);
	void detailArtAutoErrorStructure(TblSystemJenkins tblSystemJenkins, String string);
	void getModuleRunCallBack(List<Integer> moduleRunIds, Timestamp timestamp, Timestamp startTime,String moduleJson, Map<String, Object> saveDataMap);
	void saveModuleRunCallBack(long jobid, String flag, List<TblSystemModuleJenkinsJobRun> saveJobRunList);
	void judgeJenkins(TblSystemJenkins tblSystemJenkins, String jobName);
	String getJobNameByEnv(String env, long systemId, String createType, String jobType,int flag);
	TblSystemScm getTblsystemScmById(long id);
	Map<String, Object> getEnvBySystemId(long systemId);
	public String detailByteBylog(String log,int defaultSize) throws Exception;
	
	Integer getJobRunLastJobNumber(TblSystemJenkinsJobRun jobRun);

	void detailAutoLog(Map<String, Object> map);

	List<Map<String, Object>> detailSprint(List<TblSprintInfo> sprintInfoList,long systemId);

    void getDeleteEnvName(String tbl_system_scm_environment_type, Map<String, Object> envMap);

    void updateModuleInfoFristCompile(List<String> moduleList);


    void detailU(Map<String,Object> map);
    /**
    *@author liushan
    *@Description 根据系统id查询系统信息
    *@Date 2019/11/20
    *@Param [systemId]
    *@return cn.pioneeruniverse.dev.entity.TblSystemInfo
    **/
	TblSystemInfo getTblSystemInfoById(Long systemId);


    String selectModulesNamesByRunId(String id);

    String detailParam(String paramJson,String systemId,String jobName,String systemJenkisId);

    List<Map<String, Object>> getBreakNameNew(String createType, Long systemId, String jobType,Integer deployType);

}
