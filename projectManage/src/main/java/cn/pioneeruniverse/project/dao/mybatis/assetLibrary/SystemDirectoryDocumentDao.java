package cn.pioneeruniverse.project.dao.mybatis.assetLibrary;

import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocument;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 11:06 2019/11/15
 * @Modified By:
 */
public interface SystemDirectoryDocumentDao extends BaseMapper<TblSystemDirectoryDocument> {

    List<TblSystemDirectoryDocument> getDirectoryDocumentsByDirectoryId(@Param("directoryId") Long directoryId);

    List<TblSystemDirectoryDocument> getAllDirectoryDocuments(@Param("directoryId") Long directoryId, @Param("excludeId") Long excludeId);

    int delDirectoryDocument(TblSystemDirectoryDocument tblSystemDirectoryDocument);

    int insertDirectoryDocument(TblSystemDirectoryDocument tblSystemDirectoryDocument);

    int updateForSameDirectoryDocument(TblSystemDirectoryDocument tblSystemDirectoryDocument);

    TblSystemDirectoryDocument selectUploadedDocumentForUpdate(TblSystemDirectoryDocument tblSystemDirectoryDocument);

    List<TblSystemDirectoryDocument> getDocumentTypesBySystemIds(@Param("systemIds") String systemIds);

    List<TblSystemDirectoryDocument> getDocumentsUnderDocumentTypesDirectory(@Param("systemId") Long systemId, @Param("documentType") Long documentType);

    Boolean existRelatedDocuments(@Param("directoryIds") List<Long> directoryIds);

    int updateVersionForCoverUploadDocument(TblSystemDirectoryDocument tblSystemDirectoryDocument);
    
    void removeDocumentFile(@Param("directoryDocumentId") Long directoryDocumentId);
    
    TblSystemDirectoryDocument getDirectoryDocumentsById(@Param("id") Long id);
    
    TblSystemDirectoryDocument getMarkById(@Param("id") Long id);
    
    List<TblSystemDirectoryDocument> selectBySystemId(@Param("systemId") Long systemId,@Param("projectId") Long projectId);
}
