package cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary;

import cn.pioneeruniverse.project.entity.TblRequirementFeature;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersRequirement;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersRequirementFeature;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReTblSystemDirectoryDocumentChaptersRequirementFeatureMapper extends BaseMapper<TblSystemDirectoryDocumentChaptersRequirementFeature> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(TblSystemDirectoryDocumentChaptersRequirementFeature record);

    TblSystemDirectoryDocumentChaptersRequirementFeature selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblSystemDirectoryDocumentChaptersRequirementFeature record);

    int updateByPrimaryKey(TblSystemDirectoryDocumentChaptersRequirementFeature record);

    int insertChaptersRequirementFeature(TblSystemDirectoryDocumentChaptersRequirementFeature record);
    
    /**
    * @author author
    * @Description 章节关联的所有开发任务
    * @Date 2020/9/16
    * @param chaptersId
    * @param LimitNumber
    * @return java.util.List<cn.pioneeruniverse.project.entity.TblRequirementFeature>
    **/
    List<TblRequirementFeature> selectRequirementFeatureByChaptersId(@Param("chaptersId") Long chaptersId, @Param("limitNumber") Integer LimitNumber);
}