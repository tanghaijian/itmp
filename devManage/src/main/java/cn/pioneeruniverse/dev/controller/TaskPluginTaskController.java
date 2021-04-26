package cn.pioneeruniverse.dev.controller;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.dto.ResultDataDTO;
import cn.pioneeruniverse.dev.entity.TblDevTask;
import cn.pioneeruniverse.dev.service.worktask.WorkTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:任务管理插件请求接口
 * @Date: Created in 18:08 2018/11/22
 * @Modified By:
 */
@RestController
@RequestMapping("/taskPlugin/task")
public class TaskPluginTaskController {

    @Autowired
    private WorkTaskService workTaskService;

    private static final Logger logger = LoggerFactory.getLogger(TaskPluginTaskController.class);


    /**
     * @param userId
     * @return cn.pioneeruniverse.common.dto.ResultDataDTO
     * @Description 获取分配给用户待实施、实施中的任务集合
     * @MethodName getMyTaskList
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/11/22 18:17
     */
    @RequestMapping(value = "/getMyTaskList", method = RequestMethod.POST)
    public ResultDataDTO getMyTaskList(Long userId) {
        try {
            List<Map<String, Object>> taskList = workTaskService.getMyTaskListForTaskPlugin(userId);
            return ResultDataDTO.SUCCESS(Constants.TaskPlugin.SUCCESS_CODE, "获取工作任务成功", taskList);
        } catch (Exception e) {
            logger.error("获取工作任务异常，服务器端异常原因：" + e.getMessage(), e);
            return ResultDataDTO.ABNORMAL(Constants.TaskPlugin.ABNORMAL_CODE, "获取工作任务异常，服务器端异常原因：" + e.getMessage());
        }
    }

    /**
     * 
    * @Title: getTaskDetail
    * @Description: 开发工作任务详情
    * @author author
    * @param taskId 开发工作任务ID
    * @return ResultDataDTO
     */
    @RequestMapping(value = "/getTaskDetail", method = RequestMethod.POST)
    public ResultDataDTO getTaskDetail(Long taskId) {
        try {
            Map<String, Object> task = workTaskService.getTaskDetailForTaskPlugin(taskId);
            return ResultDataDTO.SUCCESS(Constants.TaskPlugin.SUCCESS_CODE, "获取工作任务详情成功", task);
        } catch (Exception e) {
            logger.error("获取工作任务详情异常，服务器端异常原因：" + e.getMessage(), e);
            return ResultDataDTO.ABNORMAL(Constants.TaskPlugin.ABNORMAL_CODE, "获取工作任务详情异常，服务器端异常原因：" + e.getMessage());
        }
    }

    /**
     * 
    * @Title: finishTask
    * @Description: 开发完成
    * @author author
    * @param tblDevTask 工作任务
    * @return ResultDataDTO
     */
    @RequestMapping(value = "/finishTask", method = RequestMethod.POST)
    public ResultDataDTO finishTask(TblDevTask tblDevTask) {
        try {
            if (workTaskService.finishTaskForTaskPlugin(tblDevTask) == 1) {
                return ResultDataDTO.SUCCESS(Constants.TaskPlugin.SUCCESS_CODE, "更新工作任务成功");
            }else{
                return ResultDataDTO.FAILURE(Constants.TaskPlugin.FAILURE_CODE, "更新工作任务失败，工作任务已被修改，无法更新");
            }
        } catch (Exception e) {
            logger.error("更新工作任务异常，服务器端异常原因：" + e.getMessage(), e);
            return ResultDataDTO.ABNORMAL(Constants.TaskPlugin.ABNORMAL_CODE, "更新工作任务异常，服务器端异常原因：" + e.getMessage());
        }
    }


}
