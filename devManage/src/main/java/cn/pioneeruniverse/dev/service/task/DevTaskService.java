package cn.pioneeruniverse.dev.service.task;

import cn.pioneeruniverse.dev.entity.DevTaskVo;
import cn.pioneeruniverse.dev.vo.task.DevTaskReq;

import java.util.List;
import java.util.Map;

/**
 * @author by qingcheng
 * @version 2020/8/17 10:28
 * @description
 */
public interface DevTaskService {

    /**
     * 查询投产窗口开发任务
     */
    List<DevTaskVo> findDeployReqFeature(DevTaskReq req);

    /**
     * 查询开发任务状态
     * @return
     */
    Map<Long, String> findDeployNameByParam(DevTaskReq req);

}
