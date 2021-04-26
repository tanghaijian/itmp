package cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary;

import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentAttachment;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReTblSystemDirectoryDocumentAttachmentMapper extends BaseMapper<TblSystemDirectoryDocumentAttachment> {
    int deleteByPrimaryKey(Long id);

    
    int insertSelective(TblSystemDirectoryDocumentAttachment record);

    TblSystemDirectoryDocumentAttachment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblSystemDirectoryDocumentAttachment record);

    int updateByPrimaryKey(TblSystemDirectoryDocumentAttachment record);

    /**
    * @author author
    * @Description 查询此章节所有附件
    * @Date 2020/9/16
    * @param chaptersId
    * @param LimitNumber
    * @return java.util.List<cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentAttachment>
    **/
    List<TblSystemDirectoryDocumentAttachment> selectAttachmentByChaptersId(@Param("chaptersId") Long chaptersId,@Param("limitNumber") Integer LimitNumber);

   
    void removeChaptersFile(TblSystemDirectoryDocumentAttachment attachment);
}