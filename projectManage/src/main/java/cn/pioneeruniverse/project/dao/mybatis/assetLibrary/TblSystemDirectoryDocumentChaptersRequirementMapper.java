package cn.pioneeruniverse.project.dao.mybatis.assetLibrary;

import cn.pioneeruniverse.project.entity.TblRequirementInfo;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersRequirement;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblSystemDirectoryDocumentChaptersRequirementMapper  extends BaseMapper<TblSystemDirectoryDocumentChaptersRequirement> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(TblSystemDirectoryDocumentChaptersRequirement record);

    TblSystemDirectoryDocumentChaptersRequirement selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblSystemDirectoryDocumentChaptersRequirement record);

    int updateByPrimaryKey(TblSystemDirectoryDocumentChaptersRequirement record);

    /**
    *@author liushan
    *@Description 章节历史版本对应需求信息
    *@Date 2020/1/14
    *@Param [systemDirectoryDocumentChaptersId, chaptersVersion]
    *@return cn.pioneeruniverse.project.entity.TblRequirementInfo
    **/
    TblRequirementInfo getSystemRequirementBySystemDirDocChapterIdAndVersion(@Param("systemDirectoryDocumentChaptersId") Long systemDirectoryDocumentChaptersId,@Param("chaptersVersion") Integer chaptersVersion);

    /**
    *@author liushan
    *@Description 章节关联所有需求
    *@Date 2020/1/14
    *@Param [chaptersId]
    *@return java.util.List<cn.pioneeruniverse.project.entity.TblRequirementInfo>
    **/
    List<TblRequirementInfo> selectRequirementByChaptersId(@Param("chaptersId") Long chaptersId,@Param("limitNumber") Integer LimitNumber);
}