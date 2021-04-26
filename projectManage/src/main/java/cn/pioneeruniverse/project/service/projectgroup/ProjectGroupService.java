package cn.pioneeruniverse.project.service.projectgroup;

import cn.pioneeruniverse.project.entity.TblProjectGroup;

import java.util.List;
import java.util.Map;

public interface ProjectGroupService {
    /**
     *
     * @Title: findProjectIdsByProjectGroupIds
     * @Description: 获取指定项目小组对应的项目id
     * @author author
     * @param ids 项目小组id
     * @return List<Long>
     */
    List<Long> findProjectIdsByProjectGroupIds(List<Long> ids);
    /**
     *
     * @Title: findProjectGroupIdsByProjectId
     * @Description: 获取指定项目下的小组id
     * @author author
     * @param id 项目id
     * @return List<Long>
     */
    List<Long> findProjectGroupIdsByProjectId(Long id);
    /**
     *
     * @Title: getProjectGroupTree
     * @Description: 获取项目小组
     * @author author
     * @param projectId 项目id
     * @return List<Map<String, Object>>
     *  key id  项目小组ID
        pId 项目小组父ID
        name 项目小组名
        projectName   项目名
     */
    List<Map<String, Object>> getProjectGroupTree(Long projectId);
    /**
     *
     * @Title: getProjectGroupTreeBySystemId
     * @Description: 获取某个系统关联的项目组
     * @author author
     * @param systemId 系统id
     * @return List<Map<String, Object>>
     * key id  项目小组ID
        pId 项目小组父ID
        name 项目小组名
        projectName   项目名
        DISPLAY_SEQ   显示顺序
     */
    List<Map<String, Object>> getProjectGroupTreeBySystemId(Long systemId);
    /**
     *
     * @Title: getProjectGroupByProjectGroupId
     * @Description: 获取某个项目小组信息
     * @author author
     * @param projectGroupId 项目小组id
     * @return TblProjectGroup
     */
    TblProjectGroup getProjectGroupByProjectGroupId(Long projectGroupId);

}
