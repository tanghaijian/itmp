package cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocument;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentRequirement;

public interface ReTblSystemDirectoryDocumentRequirementMapper extends BaseMapper<TblSystemDirectoryDocumentRequirement> {
	Integer insert(TblSystemDirectoryDocumentRequirement tblSystemDirectoryDocumentRequirement);
	
	int updateDocumentRequirement(TblSystemDirectoryDocument tblSystemDirectoryDocument);
	
    Long getMinVersion(@Param("systemDirectoryDocumentId") Long systemDirectoryDocumentId,@Param("requirementCode") String requirementCode);
    
    Long getMinVersionFeatrue(@Param("systemDirectoryDocumentId") Long systemDirectoryDocumentId,@Param("featureId") Long featureId);
    
    Integer insertDocumentRe(TblSystemDirectoryDocumentRequirement tblSystemDirectoryDocument);
}