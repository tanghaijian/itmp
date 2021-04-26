package cn.pioneeruniverse.project.dao.mybatis.assetLibrary;

import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentAttachment;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblSystemDirectoryDocumentAttachmentMapper extends BaseMapper<TblSystemDirectoryDocumentAttachment> {
    int deleteByPrimaryKey(Long id);

    /**
    *@author liushan
    *@Description 添加附件
    *@Date 2020/1/15
    *@Param [record]
    *@return int
    **/
    int insertSelective(TblSystemDirectoryDocumentAttachment record);

    TblSystemDirectoryDocumentAttachment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblSystemDirectoryDocumentAttachment record);

    int updateByPrimaryKey(TblSystemDirectoryDocumentAttachment record);

    /**
    *@author liushan
    *@Description 查询此章节附件
    *@Date 2020/1/14
    *@Param
    *@return
    **/
    List<TblSystemDirectoryDocumentAttachment> selectAttachmentByChaptersId(@Param("chaptersId") Long chaptersId,@Param("limitNumber") Integer LimitNumber);

    /**
    *@author liushan
    *@Description 章节移除附件
    *@Date 2020/1/15
    *@Param [id]
    *@return void
    **/
    void removeChaptersFile(TblSystemDirectoryDocumentAttachment attachment);
}