package cn.pioneeruniverse.project.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pioneeruniverse.project.entity.TblProjectGroup;
import org.apache.ibatis.annotations.Param;
/**
 *
 * @ClassName: ProjectGroupMapper
 * @Description: 项目组Mapper
 * @author author
 * @date 2020年8月12日 下午16:20
 *
 */
public interface ProjectGroupMapper {
    /**
     * 通过项目组获取项目
     * @param ids
     * @return
     */
    List<Long> findProjectIdsByProjectGroupIds(List<Long> ids);

    /**
     * 通过项目获取项目组
     * @param id
     * @return
     */
    List<Long> findProjectGroupIdsByProjectId(Long id);

    List<TblProjectGroup> selectProjectGroups(Long id);

    void untyingProjectGroup(Long ud);

    void updateProjectGroup(HashMap<String, Object> map2);

    List<TblProjectGroup> getChildrenProjectGroup(Map<String, Object> map);

    /**
     * 获取子项目
     * @param parentIds
     * @return
     */
    List<TblProjectGroup> getChildren(String parentIds);

    List<TblProjectGroup> selectParentProjectGroups(Long id);

    void deletePeojectGroup(HashMap<String, Object> map);

    /**
     * 编辑项目组
     * @param tblProjectGroup
     */

    void editProjectGroup(TblProjectGroup tblProjectGroup);

    String selectParentIds(Long parent);

    void saveProjectGroup(TblProjectGroup tblProjectGroup);

    List<Map<String, Object>> findAllProjectGroupForZTree(@Param("projectId") Long projectId);

    List<Map<String, Object>> findAllProjectGroupBySystemIdForZTree(@Param("systemId") Long systemId);

    List<TblProjectGroup> findChildProjectGroups(Long projectGroupId);

    void saveTmpProjectGroup(TblProjectGroup tblProjectGroup);

    /**
     * 查询
     * @param id
     * @return
     */

    TblProjectGroup selectById(Long id);

}
