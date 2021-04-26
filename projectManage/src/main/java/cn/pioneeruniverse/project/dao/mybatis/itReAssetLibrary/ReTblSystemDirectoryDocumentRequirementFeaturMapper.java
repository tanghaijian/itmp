package cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocument;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentRequirement;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentRequirementFeature;

public interface ReTblSystemDirectoryDocumentRequirementFeaturMapper extends BaseMapper<TblSystemDirectoryDocumentRequirementFeature> {
	Integer insert(TblSystemDirectoryDocumentRequirementFeature tblSystemDirectoryDocumentRequirement);
	
	int updateDocumentRequirement(TblSystemDirectoryDocument tblSystemDirectoryDocument);
	
    Long getMinVersion(@Param("systemDirectoryDocumentId") Long systemDirectoryDocumentId,@Param("requiremenFeaturetId") Long requiremenFeaturetId);
    
    Integer insertDocumentReFeature(TblSystemDirectoryDocumentRequirementFeature tblSystemDirectoryDocument);
    
    String getRequirementByFeatureCode(@Param("featureCode") String featureCode );
}