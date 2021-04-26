package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.pioneeruniverse.dev.entity.TblArtifactTag;
/**
 *
 * @ClassName: TblArtifactTagMapper
 * @Description: 标签mapper
 * @author author
 *
 */
public interface TblArtifactTagMapper {

    /**
     * 插入
     * @param record
     * @return
     */
    int insert(TblArtifactTag record);

    /**
     * 查询
     * @param id
     * @return
     */
    TblArtifactTag selectByPrimaryKey(Long id);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(TblArtifactTag record);
    
    List<TblArtifactTag> selectByArtifactId(@Param("artifactId") Long artifactId);

    /**
     * 删除
     * @param artifactId
     */
    void deleteByArtifactId(Long artifactId);

    List<TblArtifactTag> selectTagByArtifactId(Long id);

    List<TblArtifactTag> selectByArtifactIdAndEnv(Map<String,Object> map);
}