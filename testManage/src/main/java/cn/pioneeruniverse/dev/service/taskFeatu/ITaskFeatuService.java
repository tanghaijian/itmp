package cn.pioneeruniverse.dev.service.taskFeatu;

import cn.pioneeruniverse.dev.dto.TaskFeatuDTO;

import java.util.List;
/**
 *
 * @ClassName:ITaskFeatuService
 * @Description
 * @author author
 * @date 2020年8月26日
 *
 */
public interface ITaskFeatuService {

    /**
    * @author author
    * @Description 查询测试任务
    * @Date 2020/9/17
    * @param id
    * @return java.util.List<cn.pioneeruniverse.dev.dto.TaskFeatuDTO>
    **/
    List<TaskFeatuDTO> selectTaskFeatuByID(Long id);
}
