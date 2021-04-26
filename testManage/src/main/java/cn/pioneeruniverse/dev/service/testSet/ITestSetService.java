package cn.pioneeruniverse.dev.service.testSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.pioneeruniverse.dev.entity.*;
import org.springframework.web.multipart.MultipartFile;

public interface ITestSetService {

	/**
	*@author liushan
	*@Description 查询所有的测试工作任务及所属的测试集
	*@Date 2020/4/1
	*@Param [testTaskSet, request]
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblTestTask>
	**/
	Map<String, Object> selectTestTaskWithTestSet(Map<String,Object> testTaskSet, HttpServletRequest request);
	/**
	 *@author liushan
	 *@Description 根据测试工作任务查询测试集
	 *@Date 2020/3/18
	 *@Param [testTaskId, request]
	 *@return java.lang.Object
	 **/
	List<Map<String, Object>> getTestSetBytestTaskId(Map<String, Object> param, HttpServletRequest request);

	/**
	 *@author liushan
	 *@Description 复制添加多个案例
	 *@Date 2020/3/23
	 *@Param [testCaseIds]
	 *@return void
	 **/
	void copyInsertTestCases(Long testSetId,Long[] testCaseIds, HttpServletRequest request) throws Exception;

	/**
	 *@author liushan
	 *@Description 表格行拖动
	 *@Date 2020/4/2
	 *@Param [testCaseId, laterIndex, request]
	 *@return void
	 **/
	void moveIndexTestSetCase(Long testSetId, Long testCaseId, Integer beforeIndex, Integer laterIndex, HttpServletRequest request) throws Exception;

	/**
	*@author liushan
	*@Description 输入排序号拖动
	*@Date 2020/3/25
	*@Param [testSetCaseList, request]
	*@return void
	**/
	void moveTestSetCase(Map<String,Object> param, HttpServletRequest request) throws Exception;

	Map<String, Object> getTestSet(Integer page, Integer rows, TblTestSet testSet, List<String> roleCodes, Integer tableType);

	Map<String, Object> getAllTestSet(String nameOrNumber,String createBy,Long testTaskId);

	List<TblTestSet> getTestSetByFeatureId(Long featureId);

	void exportTemplet(HttpServletRequest request, HttpServletResponse response) throws Exception;

	Map<String, Object> importTestCase(MultipartFile file, Long testSetId, HttpServletRequest request, Integer type) throws Exception;

	Map<String, Object> insertTestSet(TblTestSet tblTestSet, HttpServletRequest request);

	Map<String, Object> selectTestSetCaseByPage(Integer page, Integer rows, TblTestSetCase testSetCase);

	Map<String, Object> getCaseByTestSetId(Integer page, Integer pageSize, Long testSetId, Integer executeRound, String executeResult);

	void updateCase(TblTestSetCase testSetCase, HttpServletRequest request);

	void updateManyCase(Long testSetId,String ids, HttpServletRequest request) throws Exception;

	void batchInsertCase(Long testSetId, String caseStr, HttpServletRequest request);
	
	public void batchInsertCaseStep(Long testSetId,String idStr, HttpServletRequest request);

	void updateTestSetStatus(Long id, Integer status, HttpServletRequest request);

	void addTestSetCase(TblTestSetCase testSetCase, List<TblTestSetCaseStep> testSetCaseStepList,
			HttpServletRequest request);

	void addExecuteUser(Long testSetId, Integer executeRound, String userIdStr, HttpServletRequest request);

	void updateExcuteUserStatus(Long id, HttpServletRequest request);

	void updateBatchStatus(String ids, HttpServletRequest request);

	Map<String, Object> getTestExcuteUser(Long testSetId, Integer excuteRound);

	Map<String, Object> getUserTable(Integer page, Integer rows, Long testSetId, Integer executeRound, String userName,
			String companyName,String deptName);

	List<Map<String, Object>> getTaskTree(Long userId,String taskName,String testSetName,Integer requirementFeatureStatus);
	
	Map<String, Object> getRelateSystem(Long testSetId);
	
	List<TblTestSetCaseStep> getTestSetCase(Long caseId);

	Map<String, Object> findOneTestSet(Long testSetId);

	void leadInTestSetCase(Long testSetId, String caseStr, String idStr, Integer flag, HttpServletRequest request);

	HashMap<String, Object> getOtherTestSetCase(Long otherTestSetId, Long testSetId, Long testTaskId, Long workTaskId,
			Integer page, Integer rows);

	Map<String, Object> leadInAllTestSetCase(Long otherTestSetId, Long testSetId, Long testTaskId, Long workTaskId, Integer flag,
			HttpServletRequest request);

	Map<String, Object> getTestSet2(Integer page, Integer rows, TblTestSet testSet, Integer tableType);
	Map<String, Object> getTestSetCaseBootStrap(Integer pageNumber, Integer pageSize, TblTestSetCase testSetCase);

	/**
	*@author liushan
	*@Description 
	*@Date 2020/5/18
	*@Param [testCaseId, request]
	*@return java.lang.Object
	**/
    List<CaseStepVo> getCaseStepByCaseId(Long testCaseId, HttpServletRequest request) throws Exception;
}
