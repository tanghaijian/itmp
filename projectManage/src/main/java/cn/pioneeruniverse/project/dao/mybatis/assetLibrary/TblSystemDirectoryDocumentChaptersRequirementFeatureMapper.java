package cn.pioneeruniverse.project.dao.mybatis.assetLibrary;

import cn.pioneeruniverse.project.entity.TblRequirementFeature;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersRequirementFeature;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblSystemDirectoryDocumentChaptersRequirementFeatureMapper extends BaseMapper<TblSystemDirectoryDocumentChaptersRequirementFeature> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(TblSystemDirectoryDocumentChaptersRequirementFeature record);

    TblSystemDirectoryDocumentChaptersRequirementFeature selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblSystemDirectoryDocumentChaptersRequirementFeature record);

    int updateByPrimaryKey(TblSystemDirectoryDocumentChaptersRequirementFeature record);

    /**
    *@author liushan
    *@Description 章节关联的所有开发任务
    *@Date 2020/1/14
    *@Param [id]
    *@return java.util.List<cn.pioneeruniverse.project.entity.TblRequirementFeature>
    **/
    List<TblRequirementFeature> selectRequirementFeatureByChaptersId(@Param("chaptersId") Long chaptersId, @Param("limitNumber") Integer LimitNumber);
}