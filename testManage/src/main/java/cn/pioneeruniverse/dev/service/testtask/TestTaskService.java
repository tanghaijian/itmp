package cn.pioneeruniverse.dev.service.testtask;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;

import cn.pioneeruniverse.dev.entity.*;
import org.apache.ibatis.annotations.Param;
import org.ietf.jgss.Oid;

import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.dev.vo.RequirementVo;
import cn.pioneeruniverse.dev.vo.TestTaskVo;

/**
*  类说明 
* @author:tingting
* @version:2018年12月5日 下午3:13:08 
*/
public interface TestTaskService {

	String findDeployByReqFeatureId(List<TblRequirementFeatureDeployStatus> deployStatusList);

    String findDeployByReqFeatureId1(Long featureId);
	//List<TblRequirementInfo> getAllRequirement();

	List<TestTaskVo> getAllReqFeature(TblRequirementFeature requirementFeature, Long uid, List<Long> systemIds, Integer page, Integer rows);

	Map<String, Object> getOneTestTask(Long id);
	/**
	 * 导出word
	 * @param id
	 * @param request
	 * @param response
	 */
	void exportWord(Long id,HttpServletRequest request,HttpServletResponse response) throws Exception ;

	List<Map<String, Object>> findByReqFeature(Long id);

	TblRequirementFeature addTestTask(TblRequirementFeature requirementFeature, List<TblRequirementFeatureAttachement> files, HttpServletRequest request, String developmentDeptNumber);

	void addWorkTask(Long id, TblTestTask testTask,HttpServletRequest request);

	void handleTestTask(TblRequirementFeature requirementFeature, List<TblRequirementFeatureAttachement> files, HttpServletRequest request);

	void updateTestTask(TblRequirementFeature requirementFeature, List<TblRequirementFeatureAttachement> files, HttpServletRequest request, String developmentDeptNumber);

	JqGridPage<TestTaskVo> selectAll(JqGridPage<TestTaskVo> jqGridPage, TestTaskVo testTaskVo, List<String> roleCodes);

	List<TblCommissioningWindow> getWindows();

	List<Map<String, Object>> getSplitUser(Long systemId);

	List<TblRequirementFeature> findByName(TblRequirementFeature requirementFeature);

	List<Map<String, Object>> findBrother(Long requirementId, Long id);

	String findMaxCode(int length);

	List<TblRequirementFeatureAttachement> findAtt(Long id);

	int getCountRequirement(TblRequirementInfo requirement);

	List<Map<String, Object>> getAllRequirement(TblRequirementInfo requirement, Integer page, Integer rows);

	List<Map<String, Object>> getAllSystemInfo(TblSystemInfo systemInfo, Long uid, Integer pageNumber, Integer pageSize);
	List<Map<String, Object>> getAllSystemInfo2(TblSystemInfo systemInfo, Integer pageNumber, Integer pageSize);
	List<TblSystemInfo> findAll();

	List<TblRequirementInfo> getAllReq();
	
	List<Map<String, Object>> selectTaskByTestSetCon(String nameOrNumber, String createBy, String testTaskId,Long taskId);

	void updateTransfer(TblRequirementFeature requirementFeature,String transferRemark, HttpServletRequest request);

	void mergeDevTask(TblRequirementFeature requirementFeature, Long synId, HttpServletRequest request);

	List<TblRequirementFeature> findSynDevTask(TblRequirementFeature requirementFeature);

	TblRequirementFeatureAttachement getReqFeatureAttByUrl(String path);

	void addRemark(TblRequirementFeatureRemark remark, List<TblRequirementFeatureRemarkAttachement> files,
			HttpServletRequest request);

	List<TblRequirementFeatureLog> findLog(Long id);

	List<TblRequirementFeatureRemark> findRemark(Long id);

	int findDefectNum(Long id, int i);

	void changeCancelStatus(Long requirementId);

	void updateDeployStatus(Long reqFeatureId, String deployStatus, HttpServletRequest request);

	List<TblRequirementFeature> getTaskTree(Long userId,String taskName,String testSetName,Integer requirementFeatureStatus);

    TblRequirementFeature getFeatureByTestTaskId(Long testTaskId);

	int getAllRequirementCount(TblRequirementInfo requirement);

	void synReqFeatureDeployStatus(Long requirementId,Long systemId, String deployStatus,String loginfo);
	void synReqFeatureDeployStatus1(String questionNumber, String deployStatus,String loginfo);

	void cancelStatusReqFeature(Long reqFeatureId);

	void synReqFeaturewindow(Long requirementId, Long systemId, Long commissioningWindowId, String loginfo, String beforeName, String afterName);

	void synReqFeaturewindow1(String questionNumber, Long commissioningWindowId, String loginfo, String beforeName, String afterName);

	void synReqFeatureDept(Long requirementId, Long systemId, Integer deptId, String loginfo, String deptBeforeName,
			String deptAfterName);

	void mergeTestTask(Long systemId,Long requirementId,Long commissioningWindowId, HttpServletRequest request);

	TblDeptInfo findDeptNumber(Long id);

	void insertFeatureLog(Long reqFeatureId, List<TblRequirementFeatureDeployStatus>
			oldDeployStatus,HttpServletRequest request);

	void insertFeatureLog1(Long reqFeatureId, List<TblRequirementFeatureDeployStatus>
			oldDeployStatus,String loginfo);

    void detailEnvDate(List<Map<String, Object>> list, String env, Timestamp timestamp);

    Map<String, Object> getWorkLoadByFeIds(String requirementIds);

	List<TblRequirementFeature> getAllReqFeatureByCodeOrName(String codeOrName);

	List<TblRequirementFeature> getReqFeatureByCurrentUser(Long uid);

	Map<String,Object> getProjectInfoAll(String projectName,Long uid, List<String> roleCodes);

	TblRequirementFeature getRequirementFeatureById(Long id);

	List<TblUserInfo> getUserByNameOrACC(Long id,String userName);

	List<TblRequirementFeature> getReqFeatureByExcuteCurrentUser(Long uid);
	
	Map<String, Object> filterSearch(TestTaskVo testTaskVo);
	
	List<TblRequirementFeature> getFeatureBySystemAndRequirement(Long systemId,Long requirementId);
}
