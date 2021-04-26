package cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary;

import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocument;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;


public interface ReSystemDirectoryDocumentDao extends BaseMapper<TblSystemDirectoryDocument> {

    List<TblSystemDirectoryDocument> selectDocumentByRe(String requirementCode);
    
    Long  getRequirementId(String requirementCode);
    
    List<TblSystemDirectoryDocument> getSystemByRequirement(String requirementCode);
    
    List<TblSystemDirectoryDocument> getDocumentType(@Param("classIfy") Integer classIfy); 
    
    int insertDirectoryDocument(TblSystemDirectoryDocument tblSystemDirectoryDocument);
    
    TblSystemDirectoryDocument getDirectoryDocumentsById(@Param("id") Long id);
    
    int updateVersionForCoverUploadDocument(TblSystemDirectoryDocument tblSystemDirectoryDocument);
    
    void removeDocumentFile(@Param("directoryDocumentId") Long directoryDocumentId,@Param("userId") Long userId);
    
    Boolean existRelatedDocuments(@Param("directoryIds") List<Long> directoryIds);
    
    Long selectReFId(@Param("taskCode") String taskCode);
    
    List<TblSystemDirectoryDocument> selectDocumentByFeature(Long featureId);
    
    String  selectTypeCode(@Param("documentTypeId")Long documentTypeId);
    
    TblSystemDirectoryDocument getDocumentAndTypeCoodeById(@Param("directoryDocumentId") Long directoryDocumentId);

}
