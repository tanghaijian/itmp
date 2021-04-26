package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;

import cn.pioneeruniverse.dev.entity.TblRequirementFeature;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblDataDic;
import cn.pioneeruniverse.dev.entity.TblDevTask;
import org.apache.ibatis.annotations.Param;
/**
 *
 * @ClassName:TblDevTaskMapper
 * @Description:工作任务mapper
 * @author author
 * @date 2020年8月16日
 *
 */
public interface TblDevTaskMapper extends BaseMapper<TblDevTask> {

   /* int insertSelective(TblDevTask record);*/

    /**
     * 查询
     * @param id
     * @return TblDevTask
     */

    TblDevTask selectByPrimaryKey(Long id);

    TblDevTask selectByDevTaskCode(String devTaskCode);

    /**
     * 更新
     * @param record
     * @return int
     */

    int updateByPrimaryKeySelective(TblDevTask record);

    int updateByPrimaryKey(TblDevTask record);

    /**
     * 获取名称
     * @param id
     * @return String
     */

    String getFeatureNameById(Long id);

    List<Map<String, Object>> findByReqFeature(Long id);

    /**
    *@author author
    *@Description  添加
    *@Date 2020/8/25
     * @param devTask
    *@return void
    **/
    void insertWorkTask(TblDevTask devTask);

    List<TblDevTask> getDevTask(Map<String, Object> map);

    List<TblDevTask> getDevTaskAll(Map<String, Object> map);

    /**
     * @param
     * @return
     *//*
    List<Map<String, Object>> getAllFeature(Map<String, Object> map);*/
    List<TblDevTask> getAllDevUser(Long id);

    // List<Map<String, Object>> getAllDevUser2(Map<String, Object> map);
    List<TblDevTask> getAllRequirt();


    List<TblDevTask> getUseName();

    void addDevTask(TblDevTask devTask);

    void addDefectDevTask(TblDevTask devTask);

    /**
    * @author author
    * @Description 更新工作任务
    * @Date 2020/9/18
    * @param devTask
    * @return void
    **/
    void updateDevTask(TblDevTask devTask);

    List<TblDevTask> getEditDevTask(Long id);

    void updateCommssioningWindowId(TblRequirementFeature requirementFeature);

    /**
    *@author author
    *@Description 处理
    *@Date 2020/8/25
     * @param devTask
    *@return void
    **/
    void Handle(TblDevTask devTask);

    /**
    *@author author
    *@Description 评审
    *@Date 2020/8/25
     * @param devTask
    *@return void
    **/
    void CodeHandle(TblDevTask devTask);

    /**
    *@author author
    *@Description 待处理
    *@Date 2020/8/25
     * @param devTask
    *@return void
    **/
    void DHandle(TblDevTask devTask);

    /**
    *@author author
    *@Description 评审通过
    *@Date 2020/8/25
     * @param id
    *@return void
    **/
    void reviewAdopt(String id);

    /**
    *@author author
    *@Description 评审通过
    *@Date 2020/8/25
     * @param id
    *@return void
    **/
    void reviewNAdopt(String id);

    /**
    *@author author
    *@Description 修改开发任务状态为实施中
    *@Date 2020/8/25
     * @param id
    *@return void
    **/
    void editFeature(Long id);

    /**
    *@author author
    *@Description 获取 开发人
    *@Date 2020/8/25
     * @param testid
    *@return cn.pioneeruniverse.dev.entity.TblDevTask
    **/
    TblDevTask getDevUser(Long testid);

    void assigDev(TblDevTask devTask);

    List<Map<String, Object>> getTaskListByDevUserId(Long devUserId);

    Map<String,Object> getTaskDetailById(Long id);

    int finishTask(TblDevTask tblDevTask);

    String DevfindMaxCode(Integer length);

    /**
    *@author author
    *@Description 根据开发人员id查询工作任务
    *@Date 2020/8/25
     * @param userId
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTask>
    **/ 
    List<TblDevTask> findTaskByUser(Long userId);

    /**
    *@author author
    *@Description 根据开发人员id查询工作任务
    *@Date 2020/8/25
     * @param userId
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTask>
    **/ 
    List<TblDevTask> findDevTaskByDevUser(Long userId);

    List<TblDevTask> findSystemByUser(Long userId);

    List<TblDevTask> findByReqFeatureId(Long synId);

    void updateReqFeatureId(Map<String, Object> map2);

    TblDevTask getDevOld(Long id);

    //开发任务取消
    void updateStatus(Long requirementFeatureId);

    TblDevTask getDevTaskByCode(String code);

    TblDevTask getDevTaskById(Long devTaskId);

    Integer checkMySelectedTask(@Param("userAccount") String username, @Param("taskId") Long taskId);

    List<Map<String, Object>> countWorkloadBysystemId(Long systemId);

    /**
    *@author liushan
    *@Description 根据开发任务id统计各状态工作任务的数量
    *@Date 2020/8/4
     * @param id
    *@return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    **/
    List<Map<String, Object>> getStatusCount(Long id);

    int updateCodeReviewStatus(TblDevTask tblDevTask);

    /**
    *@author liushan
    *@Description 根据开发任务获取工作任务
    *@Date 2020/8/4
     * @param id
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTask>
    **/
    List<TblDevTask> findByReqFeature2(@Param("id")Long id);

    /**
    *@author liushan
    *@Description 根据冲刺获取工作任务
    *@Date 2020/8/4
     * @param sprintId
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTask>
    **/
    List<TblDevTask> getWorkTaskBySprintId(Long sprintId);
    List<TblDevTask> getWorkTaskBySprintId(@Param("sprintId")Long sprintId, @Param("devUserName")String devUserName);

    /**
    *@author liushan
    *@Description 获取工作任务数据状态
    *@Date 2020/8/4
     * @param 
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDataDic>
    **/
    List<TblDataDic> getWorTaskStatus();

    /**
    *@author liushan
    *@Description 修改工作任务状态
    *@Date 2020/8/4
     * @param id
 * @param devTaskStatus
    *@return void
    **/
    void updateWorkTaskStatus(@Param("id") Long id, @Param("devTaskStatus") Integer devTaskStatus);

    Integer selectSystemCodeReview(Long systemId);

    void updateSprintId(TblRequirementFeature requirementFeature);

    String getDevTaskFieldTemplateById(@Param("id")Long id,@Param("fieldName") String fieldName);


    void updateDevTaskNew(TblDevTask devTask);

    List<String> selectIdBySprintId(Long sprintId);

    List<String> selectIdByWindowId(Map<String, Object> param);

    /**
     *  根据id获取开发任务信息
     * @param id
     * @return
     */
    TblRequirementFeature selectRequirementFeatureById(Long id);

    /**
    *@author liushan
    *@Description 查询开发任务关联非评审通过的工作任务
    *@Date 2020/3/6
    *@Param [id]
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTask>
    **/
    List<TblDevTask> selectDevTaskCodeReviewByReqFeatureIds(Long[] ids);

    /**
     *  该任务是否关注
     * @param id
     * @return
     */
    Integer getAttentionByReqFeatureId(Long id);
    
    
    List<TblDevTask> getDevNotOverByFeaureId(@Param("featureId")Long featureId);
}