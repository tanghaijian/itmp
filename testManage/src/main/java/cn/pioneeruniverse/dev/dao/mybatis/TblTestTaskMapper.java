package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblRequirementFeature;
import cn.pioneeruniverse.dev.entity.TblTestTask;

public interface TblTestTaskMapper   extends BaseMapper<TblTestTask>{
    int deleteByPrimaryKey(Long id);

    Integer insert(TblTestTask record);

    int insertSelective(TblTestTask record);

    TblTestTask selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblTestTask record);

    int updateByPrimaryKey(TblTestTask record);
    
    List<Map<String, Object>> findByReqFeature(Long id);

    List<TblTestTask> getTestTask(Map<String, Object> map);
	
	List<TblTestTask> getTestTaskAll(Map<String, Object> map);
	
	 List<Map<String, Object>> getAllFeature(Map<String, Object> map);
	
	List<TblTestTask> getAllTestUser(Long testUserId);
	
	List<TblTestTask> getAllRequirt();
	
	List<TblTestTask> getAllsystem();
	
	List<TblTestTask> getUseName();
	
	void addTestTask(TblTestTask devTask);
	
	void updateTestTask(TblTestTask devTask);
	
	List<TblTestTask>  getEditTestTask(Long id);

	void updateCommssioningWindowId(TblRequirementFeature requirementFeature);
	/**
	 * 处理中提交
	 * @param devTask
	 */
	void Handle(TblTestTask devTask);
	
	/**
	 * 待处理提交
	 * @param devTask
	 */
	void DHandle(TblTestTask devTask);
	/**
	 * 待评审提交
	 * @param devTask
	 */
	void examineHandle(TblTestTask devTask);
	//修改关联任务
	void editFeature(Long id);
	
	void assigTest(TblTestTask TestTask);
	
	List<Map<String,Object>> getTaskListByDevUserId(Long devUserId);

	String DevfindMaxCode(Integer length);
	
	Map<String,Object>  getTestUser(Long testid);

	List<TblTestTask> findByReqFeatureId(Long id);
	String getTestTaskNameById(Long id);
	String getFeatureNameById(Long id);
	String  getUserName(Long id);
	String getLogUserName(Long id);
	TblTestTask getTestOld(Long id);

	void updateReqFeatureId(Map<String, Object> map);
	
	List<TblTestTask> selectTestTaskByRequirementFeatureId(@Param("requirementFeatureId") Long requirementFeatureId,@Param("nameOrNumber")String nameOrNumber,@Param("createBy") List<Long> createBy,@Param("testTaskId") List<Long> testTaskId);

    List<TblTestTask> getAllTestTask(Map<String,Object> map);

    Long[] getTestTaskByReqId(Long requirementId);

	Long[] getTestTaskByWindowId(Long windowId);

	void updateStatus(Long id);

    int getAllTestTaskTotal(Map<String,Object> map);
    
    List<TblTestTask> selectTestTaskByCon(TblTestTask tblTestTask);

    int getTestTaskAllTotal(Map<String,Object> map);

	Long getDefectNum(Long devID);

	Long getCaseNum(Long id);

	Long getFeatureIdByTaskId(Long devID);

	List<Integer> selectTestStageById(Long id);

	List<Integer> selectTestTaskStatusById(Long id);

	List<Integer> selectTestTaskStatusById2(Long id);

	void updateReqFeaStatus(Map<Object, Object> map);

	Long getSystemIdByTaskId(Long id);

	List<TblTestTask>  getTestTaskByFeatureIds(@Param("ids")Long [] ids);

	void updateFeatureId(TblTestTask tblTestTask);

    /**
    *@author liushan
    *@Description 查询向前人所处的系统项目组的数据
    *@Date 2020/4/1
    *@Param [testTaskSet]
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblTestTask>
    **/
    List<TblTestTask> selectTestTaskByCuserAndExecution(Map<String,Object> map);

	int countTestTaskByCuserAndExecution(Map<String,Object> map);

	List<TblTestTask> getTestTaskByCurrentUser(@Param("uid")Long uid);
	
	/**
	 * 根据任务编号获取工作任务id
	 * @param testTaskCodeSql
	 * @return
	 */
	List<Long> getIdsBySql(@Param("testTaskCodeSql")String testTaskCodeSql);

	List<Long> selectIdByCaseNumSql(@Param("caseNumSql")String caseNumSql);


}