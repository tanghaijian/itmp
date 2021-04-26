package cn.pioneeruniverse.dev.service.systeminfo;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.dev.entity.*;
import cn.pioneeruniverse.dev.vo.TaskProjectVO;

/**
*  系统信息配置service 
* @author:tingting
* @version:2018年10月30日 下午3:58:30 
*/

public interface ISystemInfoService {
	//获取项目信息
	List<TaskProjectVO> getProjectListByProjectName(String projectName,String projectCode, Long uid, List<String> roleCodes,int pageNumber, int pageSize,Integer sprintType);
	List<Map<String, Object>> getAllSystemInfo(TblSystemInfo systemInfo, Long uid, List<String> roleCodes, int pageNumber, int pageSize,Long projectId);

	List<Map<String, Object>> getAllSystemInfo(TblSystemInfo systemInfo, Long uid, List<String> roleCodes, int pageNumber, int pageSize);

	TblSystemInfo getOneSystemInfo(Long id);

	List<TblSystemScm> getBySystemId(Long id);

	JqGridPage<TblSystemInfo> findSystemInfo(JqGridPage<TblSystemInfo> systemInfoJqGridPage, TblSystemInfo tblSystemInfo);

	List<Map<String, Object>> getSystemModuleBySystemId(Long id);
	List<Map<String, Object>> getAllSystemInfoByBuild(TblSystemInfo systemInfo, int pageNumber, int pageSize);

	void updateSystemInfo(HttpServletRequest request, TblSystemInfo systemInfo, String systemScm) throws Exception;

	Map<String, Object> getOneSystemModule(Long id);

	List<TblDataDic> findEnvironmentType(List<Integer> types);
	
	List<Map<String, Object>> selectDictEnv();

	void updateSystemModuleScm(HttpServletRequest request, TblSystemModule systemModule, String systemModuleScms);

	List<TblSystemModuleScm> finsModuleScm(Long id);
	
	List<TblCommissioningWindow> getAllTblCommissioningWindow();

	void updateByPrimaryKeySelective(TblSystemInfo tblSystemInfo);

	TblSystemInfo findById(Long id);

	Long insertModule(HttpServletRequest request, TblSystemModule systemModule, String moduleArr);
	
	void deleteSystem(HttpServletRequest request, TblSystemInfo tblSystemInfo, List<TblSystemModule> moduleList);
	
	void deleteModule(HttpServletRequest request, TblSystemModule systemModule);

	List<TblSystemInfo> findAll();

    TblSystemInfo getSystemByCode(String systemCode);

	void insertSystem(TblSystemInfo systemInfo) throws Exception ;

	void updateSystemById(TblSystemInfo systemInfo);

	List<TblToolInfo> getToolByType();

	void updateSystemInfoManual(HttpServletRequest request, TblSystemInfo systemInfo, String systemJenkins);

	int  deleteSystemJenkinsManual(String id);

	List<TblSystemJenkins>  getsystemJenkinsManual(Map<String,Object> columnMap);

	List<TblToolInfo> getToolinfoType(Map<String, Object> map);

	String judgeJobNames(String systemJenkins,long systemId);
	
	Map<String, Object> getDeployInfoByCon(Long systemId,Integer environmentType,Long systemModuleId);
	
	List<TblSystemDeploy> getDeployList(Long systemId, Integer architectureType);
	
	void addOrUpdateDeploy(String deployStr,String deployScriptStr,HttpServletRequest request) throws Exception;
	
	void addOrUpdateAutoTest(Long systemId,List<TblSystemAutomaticTestConfig> autoTestList,String testType, HttpServletRequest request) throws Exception;

	List<TblSystemModuleScm> getSystemModuleByid(Map< String, Object> map);

	String getUserName(String userId);
	
	String getBatchUserName(String ids);

	List<Long> findSystemIdByUserId(Long id);

	List<Map<String, Object>> getAllSystemInfo2(TblSystemInfo systemInfo, Integer pageNumber, Integer pageSize,String flag,String notIds);

	List<String> getEnvTypes(Long systemId);

	String handleUpdateScm(String systemScm, String newSystemScm, List<String> deleteScmid);

	void configEnvironment(Long id, String envType, HttpServletRequest request);

	List<Map<String, Object>> selectDictEnvBySystemId(Long systemId);
	
	List<TblSystemAutomaticTestConfig> selectAutoTestConfigBySystemId(Long systemId,String testType);

	List<TblDataDic> selectDictEnvBySystemId2(Long id);

	List<Long> findEnvIds(Long id);
	
	List<TblSystemInfo> findSystemByProject(HttpServletRequest request);

//	void updateSystemScm(Long id, String envType, HttpServletRequest request);

	List<TblSystemInfo> getSyetemByNameOrCode(String systemName,String systemCode);

	void insertItmpSystem(TblSystemInfo systemInfo,HttpServletRequest request) throws Exception;

	void insertTmpSystem(TblSystemInfo systemInfo) throws Exception;

	void updateTmpSystemInfo (TblSystemInfo systemInfo,int createType);




	Map<String,Object> getScmBySystemId(Long systemId,Integer architectureType );

	void delScm(Long id,HttpServletRequest request);

	String delModuleScm(TblSystemModuleScm modelScm,HttpServletRequest request);

	void updateScm(List<TblSystemScm> scmList,HttpServletRequest request);

	void updateModelScm(List<TblSystemModuleScm> modelScmList,HttpServletRequest request);

    List<TblSystemDeployScriptTemplate> getDeployScripTemplateByType(String templateType);

    Map<String, Object> addTopSystem(TblTopSystemInfo tblTopSystemInfo,Map<String, Object> map, HttpServletRequest request);

	List<String> getAllSystemInfoByTop(TblSystemInfo systemInfo, Long uid, List<String> roleCodes, Integer pages, Integer row);

    List<TblTopSystemInfo> getTopSystemInfoByCode(String systemCode,HttpServletRequest request);

	TblTopSystemInfo addTopSystem(TblTopSystemInfo tblTopSystemInfo, HttpServletRequest request);

    List<TblTopSystemInfo> getTopSysteminfosByCode(String systemCode);

    List<TblSystemModule> getModuleByMap(Map<String, Object> param);
}
