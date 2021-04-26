package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblArtifactRequirementFeature;
import cn.pioneeruniverse.dev.entity.TblRequirementFeature;

public interface TblArtifactRequirementFeatureMapper extends BaseMapper<TblArtifactRequirementFeature> {

    /**
    * @author author
    * @Description 新增制品和开发任务关联关系
    * @Date 2020/9/21
    * @param record
    * @return java.lang.Integer
    **/
    Integer insert(TblArtifactRequirementFeature record);

    /**
    * @author author
    * @Description 根据id查询
    * @Date 2020/9/21
    * @param id
    * @return cn.pioneeruniverse.dev.entity.TblArtifactRequirementFeature
    **/
    TblArtifactRequirementFeature selectByPrimaryKey(Long id);

    /**
    * @author author
    * @Description 更新制品和开发任务关联关系
    * @Date 2020/9/21
    * @param record
    * @return int
    **/
    int updateByPrimaryKeySelective(TblArtifactRequirementFeature record);
    
    /**
    * @author author
    * @Description 通过某个制品ID获取到该制品与开发任务关联关系
    * @Date 2020/9/21
    * @param artifactId
    * @return java.util.List<cn.pioneeruniverse.dev.entity.TblRequirementFeature>
    **/
    List<TblRequirementFeature> selectByArtifactId(@Param("artifactId") Long artifactId);
    
} 