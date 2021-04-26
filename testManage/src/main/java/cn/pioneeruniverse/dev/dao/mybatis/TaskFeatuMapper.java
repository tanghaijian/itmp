package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.dev.dto.TaskFeatuDTO;

import java.util.List;
/**
 *
 * @ClassName:TaskFeatuMapper
 * @Description
 * @author author
 * @date 2020年8月26日
 *
 */
public interface TaskFeatuMapper {
    /**
     * 查询测试任务
     * @param id
     * @return List<TaskFeatuDTO>
     */

    List<TaskFeatuDTO> selectTaskFeatuByID(Long id);

}
