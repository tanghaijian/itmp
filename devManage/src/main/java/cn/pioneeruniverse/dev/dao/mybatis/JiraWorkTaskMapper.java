package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.common.entity.JiraDevelopmentVO;
import org.apache.ibatis.annotations.Param;

public interface JiraWorkTaskMapper {

    Long findAssetSystemTreeID(@Param("systemCode") String systemCode,
                               @Param("module") String module);

    Long findAssetSystemTreeID1(@Param("id") String id,
                                @Param("module") String module);

    Long findProjectGroupID(String projectGroupName);

    Long getSystemVersionID(@Param("systemId") Long systemId,
                            @Param("groupFlag") String groupFlag,
                            @Param("version") String version);
    /**
     * jira数据增加
     * @param jiraDevelopmentVO
     * @return
     */
    Integer insertJiraWorkTask(JiraDevelopmentVO jiraDevelopmentVO);

    Integer updateJiraWorkTask(JiraDevelopmentVO jiraDevelopmentVO);

}
