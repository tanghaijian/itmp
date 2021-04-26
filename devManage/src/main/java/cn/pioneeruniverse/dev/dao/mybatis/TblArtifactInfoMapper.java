package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblArtifactInfo;
/**
 *
 * @ClassName:TblArtifactInfoMapper
 * @Description: 包件mapper
 * @author author
 * @date 2020年8月16日
 *
 */
public interface TblArtifactInfoMapper  extends BaseMapper<TblArtifactInfo>{

    /**
     * 插入
     * @param record
     * @return int
     */
    int inserts(TblArtifactInfo record);

    /**
     * 查询
     * @param id
     * @return TblArtifactInfo
     */

    TblArtifactInfo selectByPrimaryKey(Long id);

    /**
     * 更新
     * @param record
     * @return int
     */

    int updateByPrimaryKeySelective(TblArtifactInfo record);

	List<Map<String, Object>> findArtInfo(Map<String, Object> moduleParam);

    /**
     * 查找新包件
     * @param systemId
     * @param systemModuleId
     * @param env
     * @return List<TblArtifactInfo>
     */

	List<TblArtifactInfo> findNewPackage(@Param("systemId") Long systemId,@Param("systemModuleId") Long systemModuleId,@Param("env") Integer env);

	List<String> findRequidsByartId(Map<String, Object> map);
}