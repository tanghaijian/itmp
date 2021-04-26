package cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary;

import cn.pioneeruniverse.project.entity.TblRequirementInfo;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersRequirement;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReTblSystemDirectoryDocumentChaptersRequirementMapper  extends BaseMapper<TblSystemDirectoryDocumentChaptersRequirement> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(TblSystemDirectoryDocumentChaptersRequirement record);

    TblSystemDirectoryDocumentChaptersRequirement selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblSystemDirectoryDocumentChaptersRequirement record);

    int updateByPrimaryKey(TblSystemDirectoryDocumentChaptersRequirement record);

    
    TblRequirementInfo getSystemRequirementBySystemDirDocChapterIdAndVersion(@Param("systemDirectoryDocumentChaptersId") Long systemDirectoryDocumentChaptersId,@Param("chaptersVersion") Integer chaptersVersion);

   
    /**
    * @author author
    * @Description 章节关联所有需求
    * @Date 2020/9/16
    * @param chaptersId
    * @param LimitNumber
    * @return java.util.List<cn.pioneeruniverse.project.entity.TblRequirementInfo>
    **/
    List<TblRequirementInfo> selectRequirementByChaptersId(@Param("chaptersId") Long chaptersId,@Param("limitNumber") Integer LimitNumber);
  
    int insertChaptersRequirement(TblSystemDirectoryDocumentChaptersRequirement record);

}