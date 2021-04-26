package cn.pioneeruniverse.project.dao.mybatis.assetLibrary;

import cn.pioneeruniverse.project.entity.TblRequirementInfo;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentRequirement;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface TblSystemDirectoryDocumentRequirementMapper extends BaseMapper<TblSystemDirectoryDocumentRequirement> {
    int deleteByPrimaryKey(Long id);

    int insertDocumentRequirement(TblSystemDirectoryDocumentRequirement record);

    TblSystemDirectoryDocumentRequirement selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblSystemDirectoryDocumentRequirement record);

    int updateByPrimaryKey(TblSystemDirectoryDocumentRequirement record);

    /**
    *@author liushan
    *@Description 根据系统目录文档表ID 和 文档版本 查询需求信息
    *@Date 2020/1/9
    *@Param [systemDirectoryDocumentId, documentVersion]
    *@return cn.pioneeruniverse.project.entity.TblRequirementInfo
    **/
    TblRequirementInfo getSystemRequirementBySystemDirDocIdAndVersion(@Param("systemDirectoryDocumentId") Long systemDirectoryDocumentId,@Param("documentVersion") Integer documentVersion);

    /**
     *@author liushan
     *@Description 根据系统目录文档表ID 和 需求表ID 查询出最小的版本号
     *@Date 2020/1/9
     *@Param [systemDirectoryDocumentId, documentVersion]
     *@return cn.pioneeruniverse.project.entity.TblRequirementInfo
     **/
    Long getMinVersion(@Param("systemDirectoryDocumentId") Long systemDirectoryDocumentId,@Param("requirementCode") String requirementCode);
}