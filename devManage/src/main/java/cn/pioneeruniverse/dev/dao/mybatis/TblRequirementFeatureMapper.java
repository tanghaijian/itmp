package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.dev.entity.DevTaskVo;
import cn.pioneeruniverse.dev.entity.TblDataDic;
import cn.pioneeruniverse.dev.entity.TblDefectInfo;
import cn.pioneeruniverse.dev.entity.TblDevTask;
import cn.pioneeruniverse.dev.entity.TblRequirementFeature;
import cn.pioneeruniverse.dev.vo.task.DevTaskReq;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TblRequirementFeatureMapper extends BaseMapper<TblRequirementFeature> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(TblRequirementFeature record);

    TblRequirementFeature selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblRequirementFeature record);

    int updateByPrimaryKey(TblRequirementFeature record);

    List<DevTaskVo> getAllReqFeature(Map<String, Object> map);

    Map<String, Object> getOneDevTask(Long id);

    void insertReqFeature(TblRequirementFeature requirementFeature);

    void updateDevTask(TblRequirementFeature requirementFeature);

    List<DevTaskVo> getAll(DevTaskVo devTaskVo);

    List<TblDataDic> getAllReqFeatureStatus();

    int countTaskBySystemId(long id);

    /**
    *@author author
    *@Description 查询当前开发任务所属的系统（已知） 所属的项目 的项目组下的成员 
    *@Date 2020/8/17
     * @param systemId
    *@return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    **/
    List<Map<String, Object>> getSplitUser(Long systemId);

    /**
    *@author author
    *@Description 根据name值查询
    *@Date 2020/8/17
     * @param requirementFeature
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblRequirementFeature>
    **/
    List<TblRequirementFeature> findByName(TblRequirementFeature requirementFeature);

    /**
    *@author author
    *@Description 查询所有兄弟开发任务
    *@Date 2020/8/17
     * @param requirementId
 * @param id
    *@return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    **/
    List<Map<String, Object>> findBrother(Long requirementId, Long id);

    /**
    *@author author
    *@Description 查询投产周期不同的兄弟任务
    *@Date 2020/8/17
     * @param devTaskVo2
    *@return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    **/
    List<Map<String, Object>> findBrotherDiffWindow(DevTaskVo devTaskVo2);

    /**
    *@author author
    *@Description 根据id修改开发任务的状态为实施中
    *@Date 2020/8/17
     * @param id
    *@return void
    **/
    void updateStatus(Long id);

    /**
    *@author author
    *@Description 查询最大的任务编号
    *@Date 2020/8/17
     * @param length
    *@return java.lang.String
    **/    
    String findMaxCode(Integer length);

    /**
    *@author author
    *@Description 根据开发任务id 修改执行人员
    *@Date 2020/8/17
     * @param requirementFeature
    *@return void
    **/
    void updateTransfer(TblRequirementFeature requirementFeature);

    /**
    *@author author
    *@Description 查询与该自建任务同一需求下同一系统的同步任务
    *@Date 2020/8/17
     * @param requirementFeature
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblRequirementFeature>
    **/
    List<TblRequirementFeature> findSynDevTask(TblRequirementFeature requirementFeature);

    /**
    *@author author
    *@Description 根据 开发任务id 修改工作任务id, 开发任务编号，CREATE_TYPE = 2
    *@Date 2020/8/17
     * @param requirementFeature
    *@return void
    **/
    void updateTaskId(TblRequirementFeature requirementFeature);

    /**
    *@author author
    *@Description 修改 开发任务状态为2 
    *@Date 2020/8/17
     * @param id
    *@return void
    **/
    void updateSynStatus(Long id);

    /**
    *@author author
    *@Description 查询未关联信息
    *@Date 2020/8/14
     * @param requirementFeature
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblRequirementFeature>
    **/
    List<TblRequirementFeature> findFrature(TblRequirementFeature requirementFeature);

    /**
    *@author author
    *@Description 查询用户名称
    *@Date 2020/8/17
     * @param manageUserId
    *@return java.lang.String
    **/
    String getUserName(Integer manageUserId);

    /**
    *@author author
    *@Description 查询部门名称
    *@Date 2020/8/17
     * @param deptId
    *@return java.lang.String
    **/
    String getDeptName(Integer deptId);

    /**
    *@author author
    *@Description 查询投产窗口关联开发任务状态
    *@Date 2020/8/14
     * @param windowId
 * @param systemId
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblRequirementFeature>
    **/
    List<TblRequirementFeature> findFratureStatusByWindow(@Param(value = "windowId") Long windowId,
                                                          @Param(value = "systemId") Long systemId);
    /**
    *@author author
    *@Description 查询系统关联冲刺关联开发任务状态
    *@Date 2020/8/14
     * @param systemId
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblRequirementFeature>
    **/
    List<TblRequirementFeature> findFratureStatusBySystemId(Long systemId);
    /**
    *@author author
    *@Description 根据开发管理岗查询开发任务
    *@Date 2020/8/14
     * @param userId
    *@return java.util.List<cn.pioneeruniverse.dev.entity.DevTaskVo>
    **/
    List<DevTaskVo> findFeatureByManageUserId(Long userId);
    /**
    *@author author
    *@Description 根据开发工任务编号 查询开发任务所有信息
    *@Date 2020/8/17
     * @param devTaskCode
    *@return cn.pioneeruniverse.dev.entity.TblRequirementFeature
    **/
    TblRequirementFeature getRequirementFeatureByDevTaskCode(String devTaskCode);

    /**
    *@author author
    *@Description 根据 开发工作任务 查询 开发任务id 
    *@Date 2020/8/17
     * @param devTaskId
    *@return java.lang.Long
    **/
    Long getRequirementFeatureIdByDevTaskId(Long devTaskId);

    /**
    *@author author
    *@Description 根据需求 修改 开发任务状态 为 00 
    *@Date 2020/8/17
     * @param requirementId
    *@return void
    **/
    void changeCancelStatus(Long requirementId);

    /**
    *@author author
    *@Description 根据需求查询开发任务
    *@Date 2020/8/17
     * @param requirementId
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblRequirementFeature>
    **/
    List<TblRequirementFeature> findByrequirementId(Long requirementId);

    /**
    *@author author
    *@Description 缺陷弹窗未关联开发任务的所有缺陷
    *@Date 2020/8/17
     * @param map
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDefectInfo>
    **/
    List<TblDefectInfo> findDftNoReqFeature(HashMap<String, Object> map);

    /**
    *@author author
    *@Description 根据开发任务id查询缺陷
    *@Date 2020/8/17
     * @param id
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDefectInfo>
    **/
    List<TblDefectInfo> findDftByReqFId(Long id);

    /**
    *@author author
    *@Description 根据开发任务id查询缺陷
    *@Date 2020/8/17
     * @param ids
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDefectInfo>
    **/
    List<TblDefectInfo> findDftByReqFIds(Long []ids);

    /**
    *@author author
    *@Description 修改缺陷的 的 开发任务为null
    *@Date 2020/8/17
     * @param id
    *@return void
    **/
    void updateDftReqFIdNull(Long id);

    /**
    *@author author
    *@Description 根据开发任务id 修改QUESTION_NUMBER 为null
    *@Date 2020/8/17
     * @param id
    *@return void
    **/
    void updateReqNumberNull(Long id);

    /**
    *@author author
    *@Description 根据开发任务id 修改需求id 为null
    *@Date 2020/8/17
     * @param id
    *@return void
    **/
    void updateReqNull(Long id);

    /**
    *@author author
    *@Description 查询所有的开发任务
    *@Date 2020/8/17
     * @param map
    *@return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    **/
    List<Map<String, Object>> getAllFeature(Map<String, Object> map);

    /**
    *@author author
    *@Description 根据需求编号，创建方式：2 同步，来源：1 业务需求 ,查询当前的开发任务
    *@Date 2020/8/17
     * @param requirementCode
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblRequirementFeature>
    **/
    List<TblRequirementFeature> getFeatureByREQCode(String requirementCode);

    /**
     * 查询开发任务 实施中和待实施
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> getAllRequirementFeature(Map<String, Object> map);

    List<TblRequirementFeature> getReqFeatureByReqCodeAndSystemId(@Param(value = "requirementCode") String requirementCode, @Param(value = "systemId") Long systemId);

    List<DevTaskVo> getDeplayReqFesture(Map<String, Object> map);

    /**
     * 查询投产窗口开发任务
     */
    List<DevTaskVo> findDeployReqFeature(DevTaskReq req);

    void updateDeployStatus(Map<String, Object> map);

    TblRequirementFeature getFeatureByCode(String code);

    void findByReqFeatureIds(Map<String, Object> map);

    void updateDeployStatusOne(@Param(value = "id") Long id, @Param(value = "deployStatus") String status);

    String getDeployStatus(Long id);

    void synReqFeatureDeployStatus(Map<String, Object> map);

    void updateDeployStatusOneAdd(@Param(value = "id") Long id, @Param(value = "deployStatus") String status);

    String getFeatureName(Long requirementFeatureId);


    int getAllReqFeatureCount(Map<String, Object> map);
    /**
     * 查询开发任务 根据系统id 需求id 并且是同步任务
     * */
    List<TblRequirementFeature> selectBySystemIdAndReqId(@Param(value="systemId")Long systemId, @Param(value="requirementId")Long requirementId);

    TblRequirementFeature getFeatureById(Long requirementFeatureId);

    List<DevTaskVo> getPrdDeplayReqFesture(Map<String, Object> map);
    /**
    *@author liushan
    *@Description 根据冲刺id查询开发任务
    *@Date 2020/8/4
     * @param sprintId
    *@return java.util.List<cn.pioneeruniverse.dev.entity.DevTaskVo>
    **/
    List<DevTaskVo> getDevTaskBySprintIds(@Param("sprintId")Long sprintId, @Param("devUserName")String devUserName);

    /**
    *@author liushan
    *@Description 编辑工作任务状态
    *@Date 2020/8/4
     * @param id
 * @param requirementFeatureStatus
    *@return void
    **/
    void updateReFeatureStatus(@Param(value="id")Long id, @Param(value="requirementFeatureStatus")String requirementFeatureStatus);

    List<TblRequirementFeature> findFestureByWindowDate(@Param(value="windowDate") String windowDate,
                                                        @Param(value="systemId")Long systemId);

    List<DevTaskVo> getAllCondition(DevTaskVo devTaskVo);

    /**
    *@author liushan
    *@Description 获取开发任务数据字典状态
    *@Date 2020/8/4
     * @param 
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDataDic>
    **/
    List<TblDataDic> getReqFeatureStatus();

    /**
    *@author author
    *@Description 根据条件查询工作任务
    *@Date 2020/8/17
     * @param map
    *@return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    **/
    List<Map<String, Object>> getAllRequirementFeatureCondition(Map<String, Object> map);

    /**
    *@author author
    *@Description 批量修改开发任务所属冲刺
    *@Date 2020/8/17
     * @param id
 * @param sprintId
 * @param requirementFeatureStatus
 * @param executeUserId
    *@return void
    **/
    void updateSprints(@Param(value = "id")Long id, @Param(value="sprintId")Long sprintId, @Param(value="requirementFeatureStatus")String requirementFeatureStatus,@Param(value="executeUserId") Integer executeUserId);

    void synReqFeaturewindow(Map<String, Object> map);

    void updateGroupAndVersion(long parseLong, Long systemVersionId, Long executeProjectGroupId);



    List<DevTaskVo> getReqFeaByartId(Map<String, Object> map);

	void synReqFeatureDept(Map<String, Object> map);

	List<Map<String, Object>> getDevTaskBySystemAndRequirement(Map<String, Object> map);

    List<Map<String,Object>> getRequirementByCode(String requirementCode);

    List<Map<String, Object>> getDeptByName(String deptName);



    String getFeatureFieldTemplateById(@Param(value = "id")Long id, @Param(value = "fieldName")String fieldName);

    List<TblRequirementFeature> selectBySystemIdAndReqId1(@Param(value="systemId")Long systemId,
                                                          @Param(value="requirementId")Long requirementId);

    List<Map<String, Object>> getTreeName(Map<String, Object> map);
    
    Map<String, Object>selectModelName(@Param(value="systemTreeId") Long systemTreeId);

	List<Long> findWindowByReqId(Long requirementId);

    void updateCheckStatus(Long id);

    void insertTestReqFeature(TblRequirementFeature requirementFeature);

	List<DevTaskVo> findFeatureByExcuteUserId(Long userId);
	
	List<TblDevTask>  getFeatureBySystemAndRequirement(@Param(value="systemId")Long systemId, @Param(value="requirementId")Long requirementId);
}