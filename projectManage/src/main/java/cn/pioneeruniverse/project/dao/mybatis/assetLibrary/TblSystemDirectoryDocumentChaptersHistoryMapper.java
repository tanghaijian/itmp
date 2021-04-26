package cn.pioneeruniverse.project.dao.mybatis.assetLibrary;

import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersHistory;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblSystemDirectoryDocumentChaptersHistoryMapper extends BaseMapper<TblSystemDirectoryDocumentChaptersHistory> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(TblSystemDirectoryDocumentChaptersHistory record);

    TblSystemDirectoryDocumentChaptersHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblSystemDirectoryDocumentChaptersHistory record);

    int updateByPrimaryKey(TblSystemDirectoryDocumentChaptersHistory record);

    /**
    *@author liushan
    *@Description 获取章节的历史信息
    *@Date 2020/1/14
    *@Param [chaptersHistory]
    *@return java.util.List<cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersHistory>
    **/
    List<TblSystemDirectoryDocumentChaptersHistory> getChaptersHistory(TblSystemDirectoryDocumentChaptersHistory chaptersHistory);

    /**
     *@author liushan
     *@Description
     *@Date 2020/2/17
     *@Param [chaptersHistory]
     *@return java.util.List<cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersHistory>
     **/
    List<Long> getChaptersHistoryBySystemDirectoryDocumentIdAndVersion(@Param("systemDirectoryDocumentId") Long systemDirectoryDocumentId, @Param("minVersion")  Long minVersion);

    /**
     *@author liushan
     *@Description 获取历史信息
     *@Date 2020/2/19
     *@Param [chaptersHistory]
     *@return java.util.Map<java.lang.String,java.lang.Object>
     **/
    TblSystemDirectoryDocumentChaptersHistory getChaptersHistoryVersion(TblSystemDirectoryDocumentChaptersHistory chaptersHistory);

    TblSystemDirectoryDocumentChaptersHistory getMaxVersionChaptersHistory(@Param("systemDirectoryDocumentChaptersId") Long systemDirectoryDocumentChaptersId);

    int getreqChaptersCount(@Param("systemDirectoryDocumentId") Long systemDirectoryDocumentId,@Param("requirementCode") String requirementCode);

    /**
    *@author liushan
    *@Description 需求关联的最大版本章节历史数据
    *@Date 2020/3/3
    *@Param [sId, requirementCode]
    *@return cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersHistory
    **/
    TblSystemDirectoryDocumentChaptersHistory selectReqChaptersStatus(@Param("systemDirectoryDocumentChaptersId") Long sId,@Param("requirementCode") String requirementCode);
}