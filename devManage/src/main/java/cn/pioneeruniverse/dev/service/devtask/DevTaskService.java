package cn.pioneeruniverse.dev.service.devtask;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.dev.entity.DevTaskVo;
import cn.pioneeruniverse.dev.entity.ExtendedField;
import cn.pioneeruniverse.dev.entity.TblCommissioningWindow;
import cn.pioneeruniverse.dev.entity.TblDefectInfo;
import cn.pioneeruniverse.dev.entity.TblDevTask;
import cn.pioneeruniverse.dev.entity.TblProjectPlan;
import cn.pioneeruniverse.dev.entity.TblRequirementFeature;
import cn.pioneeruniverse.dev.entity.TblRequirementFeatureAttachement;
import cn.pioneeruniverse.dev.entity.TblRequirementFeatureAttention;
import cn.pioneeruniverse.dev.entity.TblRequirementFeatureDeployStatus;
import cn.pioneeruniverse.dev.entity.TblRequirementFeatureLog;
import cn.pioneeruniverse.dev.entity.TblRequirementFeatureRemark;
import cn.pioneeruniverse.dev.entity.TblRequirementFeatureRemarkAttachement;
import cn.pioneeruniverse.dev.entity.TblRequirementInfo;
import cn.pioneeruniverse.dev.entity.TblSprintInfo;

/**
*  类说明 
* @author:tingting
* @version:2018年11月12日 上午11:15:26 
*/
public interface DevTaskService {

	List<TblRequirementInfo> getAllRequirement();

	List<DevTaskVo> getAllReqFeature(TblRequirementFeature requirementFeature, Long uid, Integer page, Integer rows);

	Map<String, Object> getOneDevTask(Long id);

	List<Map<String, Object>> findByReqFeature(Long id);

	void addDevTask(TblRequirementFeature requirementFeature, List<TblRequirementFeatureAttachement> files, String defectIds,Double dftActualWorkload,String defectRemark, String dftJsonString, HttpServletRequest request);

	void addWorkTask(Long id, TblDevTask devTask, HttpServletRequest request);

	void handleDevTask(TblRequirementFeature requirementFeature, HttpServletRequest request, String handleRemark);

	void updateDevTask(TblRequirementFeature requirementFeature, List<TblRequirementFeatureAttachement> files, String defectIds, HttpServletRequest request);

	JqGridPage<DevTaskVo> selectAll(JqGridPage<DevTaskVo> jqGridPage, DevTaskVo devTaskVo, List<String> roleCodes);

	String findDeployByReqFeatureId(Long featureId);
	
	List<TblRequirementFeatureAttention> getAttentionList(TblRequirementFeatureAttention attention);
	
	void changeAttention(Long id, Integer attentionStatus, HttpServletRequest request);

	List<TblCommissioningWindow> getWindows();

	List<Map<String, Object>> getSplitUser(Long systemId);

	List<TblRequirementFeature> findByName(TblRequirementFeature requirementFeature);

	List<Map<String, Object>> findBrother(Long requirementId, Long id);

	void updateStatus(Long id, HttpServletRequest request);

	String findMaxCode(int length);

	List<TblRequirementFeatureAttachement> findAtt(Long id);

	void updateTransfer(TblRequirementFeature requirementFeature, String transferRemark, HttpServletRequest request);

	List<TblRequirementFeature> findSynDevTask(TblRequirementFeature requirementFeature);

	void mergeDevTask(TblRequirementFeature requirementFeature, Long synId, HttpServletRequest request);

	TblRequirementFeatureAttachement getReqFeatureAttByUrl(String path);

	void addRemark(TblRequirementFeatureRemark remark, List<TblRequirementFeatureRemarkAttachement> files, HttpServletRequest request);

	List<TblRequirementFeatureRemark> findRemark(Long id);

	List<TblRequirementFeatureLog> findLog(Long id);

	void changeCancelStatus(Long requirementId);

	List<TblCommissioningWindow> selectWindows(String windowName, Integer pageNumber, Integer pageSize) throws Exception;

	List<TblDefectInfo> findDftNoReqFeature(TblDefectInfo defectInfo,Integer featureSource,
											Long reqFeatureId, Integer pageNumber, Integer pageSize);

	List<TblDefectInfo> findDftByReqFId(Long id);
	
	List<Map<String, Object>> getAllFeature(TblRequirementFeature feature, int i, int maxValue);

	List<Map<String, Object>> getSystemVersionBranch(Long systemId);

	String getVersionName(Long systemVersionId);

	List<TblRequirementFeature> getReqFeatureByReqCodeAndSystemId(String requirementCode, Long systemId);

	List<DevTaskVo> getDeplayReqFesture(TblRequirementFeature feature, String[] statusArr, int i, int maxValue);

	void updateDeployStatus(String idsArr[],String status);
	
	TblRequirementFeature getFeatureByCode(String code);

	void insertAtt(TblRequirementFeatureAttachement featureAttachement) throws Exception;

	List<TblRequirementFeature> findByReqFeatureIds(String[] idsArr);

	String getDeployStatus(Long id);

	void updateDeployStatusOne(String ids, String status, String jsonString, Integer deplyStatus);

	int getAllReqFeatureCount(TblRequirementFeature requirementFeature, Long uid);

	void updateReqFeatureTimeTrace(String json);
	
	void updateReqFeatureTimeTraceForDeploy(String json);

	TblRequirementFeature getFeatureById(Long requirementFeatureId);

	List<TblSprintInfo> getAllSprint();

	List<DevTaskVo> getPrdDeplayReqFesture(TblRequirementFeature feature, String[] statusArr,String windowId, int i, int maxValue);

	List<TblCommissioningWindow> getLimitWindows(Long systemId);

	void synReqFeatureDeployStatus(Long systemId, Long requirementId, String deployStatus, String loginfo);

	List<TblSprintInfo> getSprintBySystemId(Long systemId);

	void cancelStatusReqFeature(Long reqFeatureId);

	void updateSprints(String ids, Long sprintId, String devTaskStatus, Integer executeUserId,HttpServletRequest request);

	void synReqFeaturewindow(Long requirementId, Long systemId, Long commissioningWindowId, String loginfo, String beforeName, String afterName);


	String getProjectGroup(Long uid);

    /*List<String> getProjectGroupBySystemId(long systemId,long uid);*/

    void updateGroupAndVersion(String ids,Long systemVersionId, Long executeProjectGroupId);

    List<TblSprintInfo> getSprintBySystemIdLimit(long systemId);

	void synReqFeatureDept(Long requirementId, Long systemId, Long deptId, String loginfo, String deptBeforeName,
			String deptAfterName);

	List<Map<String, Object>> getDevTaskBySystemAndRequirement(Long systemId, Long requirementId);
	Map<String, Object> importExcel(MultipartFile file, HttpServletRequest request);

	void insertFeatureLog(Long reqFeatureId, List<TblRequirementFeatureDeployStatus> oldDeployStatus,
						  String loginfo,int i);
    TblRequirementFeature getOneFeature(String id);

    List<ExtendedField> findFieldByReqId(Long id);

    List<Map<String, Object>> getTreeName(Map<String, Object> map);

	String getProjectGroupByProjectIds(Long systemId);

	String getProjectGroupByProjectId(Long projectId);

	int getAllProjectPlanCount(TblProjectPlan tblProjectPlan);

	List<Map<String, Object>> getAllProjectPlan(TblProjectPlan tblProjectPlan, int i, int maxValue);

	List<TblProjectPlan> getProjectPlanTree(Long projectId);

	//根据系统id和项目id获取冲刺信息
	List<TblSprintInfo> getSprintInfoListBySystemIdAndProjectId(@Param("systemId") Long systemId,@Param("projectId") Long projectId);

	/**
	 *@author liushan
	 *@Description 查询开发任务关联的系统，所属的工作任务是否有未评审数据
	 *@Date 2020/3/6
	 *@Param [id]
	 *@return java.util.Map<java.lang.String,java.lang.Object>
	 **/
	List<TblDevTask> selectDevTaskByReqFeatureIds(Long[] ids) throws Exception;

	/**
	 *  该任务是否关注
	 * @param id
	 * @return
	 */
	Integer getAttentionByReqFeatureId(Long id);

	void checkStatus(MultipartFile[] files,Long id,HttpServletRequest request);
	
	
	String selectTreeName(String assetSystemTreeId);

	Map<String,Object> getProjectInfoAll(String projectName,Long uid, List<String> roleCodes);
	
	List<TblDevTask> getFeatureBySystemAndRequirement(Long systemId,Long requirementId);
	
	List<TblDevTask> getDevNotOverByFeaureId(Long featureId);
}

