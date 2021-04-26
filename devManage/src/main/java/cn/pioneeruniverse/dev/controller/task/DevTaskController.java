package cn.pioneeruniverse.dev.controller.task;

import cn.pioneeruniverse.dev.entity.DevTaskVo;
import cn.pioneeruniverse.dev.service.task.DevTaskService;
import cn.pioneeruniverse.dev.vo.task.DevTaskReq;
import cn.pioneeruniverse.dev.vo.task.DevTaskRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author by qingcheng
 * @version  2020/8/14 14:04
 * @description 重构的开发任务管理类
 */
@RestController("rebuildDevTaskController")
@RequestMapping("devTask")
public class DevTaskController {

    //======================================================
    // 属性
    private DevTaskService devTaskService;

    //======================================================
    // 方法
    /**
     * 部署需要的开发任务列表  当前系统下实施完成状态
     * @param req 请求参数
     * @return 开发任务列表
     */
    @RequestMapping(value = "findReqFeatureByParam")
    public DevTaskRes findReqFeatureByParam(@RequestBody DevTaskReq req) {
        List<DevTaskVo> list = devTaskService.findDeployReqFeature(req);
        return new DevTaskRes(list);
    }

    //======================================================
    // GETTER & SETTER
    @Autowired
    public void setDevTaskService(DevTaskService devTaskService) {
        this.devTaskService = devTaskService;
    }

}
