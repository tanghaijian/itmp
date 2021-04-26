package cn.pioneeruniverse.dev.service.worktask;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.common.dto.TblAttachementInfoDTO;
import cn.pioneeruniverse.dev.entity.*;

public interface WorkTaskService {
    Map getDevTask(TblDevTask tblDevTask, Long[] projectIds, Integer page, Integer rows,HttpServletRequest request);

    /**
     * 回去用户信息
     *
     * @param id
     * @return
     */
    List<TblDevTask> getAllDevUser(Long id);


    Map getAllsystem(TblSystemInfo systemInfo, Integer page, Integer rows);

    List<TblDevTask> getUserName();

    Map<String, Object> getSeeDetail(String id);
    
	List<TblDevTaskAttention> getAttentionList(TblDevTaskAttention attention);
	
	void changeAttention(Long id, Integer attentionStatus, HttpServletRequest request);


    /**
     * 获取编辑信息
     *
     * @param id
     * @return
     */
    Map<String, Object> getEditDevTask(String id);

    /**
     * 添加
     *
     * @param obj
     * @param Userid
     */
    void addDevTask(String obj, String attachFiles, Long Userid, HttpServletRequest request, String UserAccount) throws Exception;

    /**
     * 添加含有缺陷工作任务
     *
     * @param obj
     * @param Userid
     */
    void addDefectDevTask(String obj, String attachFiles, Long Userid, HttpServletRequest request, String UserAccount);

    /**
     * 修改
     *
     * @param obj
     * @param attachFiles
     * @param Userid
     * @param request
     */
    void updateDevTask(String obj, String attachFiles, String deleteAttaches, Long Userid, HttpServletRequest request);

    /**
     * 处理
     *  @param devTask
     * @param HattachFiles
     * @param request
     */
    void Handle(TblDevTask devTask, String HattachFiles, String deleteAttaches, HttpServletRequest request);

    void CodeHandle(String handle, String HattachFiles, String deleteAttaches, HttpServletRequest request);

    /**
     * 评审通过
     *
     * @param id
     * @param adoptFiles
     * @param request
     */
    void reviewAdopt(String id, String adoptFiles, String deleteAttaches, HttpServletRequest request);

    /**
     * 评审未通过
     *
     * @param id
     * @param adoptFiles
     * @param request
     */
    void reviewNAdopt(String id, String adoptFiles, String deleteAttaches, HttpServletRequest request);

    /**
     * 待处理
     *
     * @param handle
     * @param DHattachFiles
     * @param request
     */
    void DHandle(String handle, String DHattachFiles, String deleteAttaches, HttpServletRequest request);

    /**
     * 分派
     *
     * @param handle
     */
    void assigDev(String handle, String Remark, HttpServletRequest request);

    String DevfindMaxCode(int length);

    /**
     * @param userId
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @Description 获取分配给用户待实施、实施中的任务集合
     * @MethodName getMyTaskListForTaskPlugin
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/11/22 18:25
     */
    List<Map<String, Object>> getMyTaskListForTaskPlugin(Long userId);

    /**
     * @Description 获取开发任务详情
     * @MethodName getTaskDetailForTaskPlugin
     * @param taskId
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/10/15 14:04
     */
    Map<String,Object> getTaskDetailForTaskPlugin(Long taskId);

    /**
     * @Description 翻转工作任务状态为开发完成
     * @MethodName finishTask
     * @param tblDevTask
     * @return int
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/10/16 15:53
     */
    int finishTaskForTaskPlugin(TblDevTask tblDevTask);

    List<Map<String, Object>> getAllFeature(TblDevTask tblDevTask,Long userdId, List<String> roleCodes, Integer pageNumber, Integer pageSize);

    /* 获取文件导出信息*/
    List<TblDevTask> getExcelAllWork(TblDevTask tblDevTask, Long[] projectIds,HttpServletRequest request);

    Map devStatus();

    Map ReqStatus();

    Map ReqSystem();

    Map FeatureStatus();

    Map getAllRequirt(TblRequirementInfo RequirementInfo, Integer page, Integer rows);

    List<TblProjectInfo> findProjectByUser(HttpServletRequest request);

    TblDevTask getDevTaskByCode(String code);

    void addworkTaskAttachement(TblAttachementInfoDTO attachementInfoDTO) throws Exception;

    TblDevTask getDevTaskById(Long id);

	List<Map<String, Object>> countWorkloadBysystemId(Long systemId);
	
	 void logAll(String id, String adoptFiles, Object oldDevTask, String LogType, Map<String, Object> remarkMap, HttpServletRequest request);
	 
	 Long insertDevTaskLog(TblDevTaskLog devTaskLog, HttpServletRequest request);

    List<ExtendedField> findFieldByDevId(Long id);

    /**
     *  根据id获取开发任务信息
     * @param id
     * @return
     */
    TblRequirementFeature selectRequirementFeatureById(Long id);

    /**
     *  处理生产缺陷添加工作任务
     * @param defect
     * @param workTask
     * @param request
     * @return
     */
    void addDefectDevTask1(String defect,String workTask,String attachFiles,HttpServletRequest request) throws Exception;

    void addTestTask(TblRequirementFeature tblRequirementFeature)throws Exception;

    /**
    *@author liushan
    *@Description 同步jira附件
    *@Date 2020/5/11
    *@Param [workCodes, request]
    *@return void
    **/
    Set<String> jiraFileByCode(String workCodeJiraIds, HttpServletRequest request);
    
    /**
     * 
     * @Title: sendWorkTaskMassage
     * @Description: 开发工作任务发送消息
     * @param devTask
     * @author wangwei
     * @date 2020年8月18日
     */
    void sendWorkTaskMassage(TblDevTask devTask);
}
