package cn.pioneeruniverse.project.service.requirement;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.project.entity.TblRequirementFeature;

public interface RequirementFeatureService {

	List<TblRequirementFeature> findFeatureByRequirementId(Long id);
	
	List<TblRequirementFeature> getAllFeature(String featureName);
	
	//同步开发任务至itmp_db
	void updateTaskDataItmp(TblRequirementFeature feature,String reqId);
	
	//同步测试任务至tmp_db
	void updateTaskDataTmp(TblRequirementFeature feature,String reqId);

	//将开发任务变为取消
	void updateStatusItmp(TblRequirementFeature feature);

	//将测试任务变为取消
	void updateStatusByTmp(TblRequirementFeature feature);

	//每日跑批
	void executeFeatureToHistoryJob(String parameterJson);

	void addItmpFeatureInfo (String reqSystemList,String reqId);

	void addTmpFeatureInfo (String reqSystemList,String reqId);
}