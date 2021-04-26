package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.pioneeruniverse.dev.entity.TblCaseStep;
import cn.pioneeruniverse.dev.entity.TblTestSetCaseStep;

public interface TblCaseStepMapper {

	void insertCaseStep(TblCaseStep tblCaseStep);

	List<TblCaseStep> findCaseStepByCaseId(Long id);

	void updateCaseStepByCaseId(HashMap<String, Object> map);

	List<TblCaseStep> getCaseStepsByCaseIds(List<Long> ids);

	void batchInsert(@Param("list") List<TblCaseStep> list);

	List<TblCaseStep> getCaseStepsByCaseId(Long caseId);

	void updateStatus(List<Long> list);

	void updateCaseStep(Map<String, Object> i);

	void insert(Map<String, Object> i);
	
	void deleteCaseStep(Long caseId);
	
	void insertStep(Map<String, Object> map);

	void updateStep(TblCaseStep tblCaseStep);

	List<Long> getStepIdsByCaseId(Long id);

	void deleteCaseSteps(Map<String, Object> map);

	/**
	*@author liushan
	*@Description 根据测试集案例id查询添加数据
	*@Date 2020/3/23
	*@Param [id, id1, currentUserId]
	*@return void
	**/
    void insertByTestSetCaseId(@Param("oldTestSetCaseId") Long oldTestSetCaseId,@Param("newTestSetCaseId") Long newTestSetCaseId,@Param("currentUserId")  Long currentUserId);
}
