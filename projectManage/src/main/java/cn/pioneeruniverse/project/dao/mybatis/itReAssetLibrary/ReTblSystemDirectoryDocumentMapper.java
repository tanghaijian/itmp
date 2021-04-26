package cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary;


import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocument;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersRelation;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ReTblSystemDirectoryDocumentMapper extends BaseMapper<TblSystemDirectoryDocument> {

    List<Map<String, Object>> getDeptdirectory(Map<String, Object> requirementIds);

    List<Map<String, Object>> getRequiredirectory(Map<String, Object> map);

    List<Map<String, Object>> getSystemDirectory(Map<String, Object> mapParam);

    List<Map<String, Object>> getSvnFilesByDevTaskId(Long devTaskId);

    List<Map<String, Object>> getGitFilesByDevTaskId(Long devTaskId);

    List<Map<String, Object>> getSystemDirectorySearch(Map<String, Object> mapParam);

    List<TblSystemDirectoryDocument> getDocumentsByRequire(Map<String, Object> param);

    List<TblSystemDirectoryDocument> getDocumentsByRequireSystem(Map<String, Object> param);

    List<String> getRequireBydocuId(String documetId);

    List<Map<String, Object>> getDocumentType();

    List<TblSystemDirectoryDocument> getDocmentByRequire(Map<String, Object> param);
    /**
     * 根据条件获取所有文档
     * @param tblSystemDirectoryDocument
     * @return
     */
    List<TblSystemDirectoryDocument> getAllDocument(TblSystemDirectoryDocument tblSystemDirectoryDocument);
    /**
     * 获取相关联的文档
     * @param systemDirectoryDocumentChapterId1
     * @param systemDirectoryDocumentId1
     * @return
     */
    List<TblSystemDirectoryDocument> getRelationDocument(TblSystemDirectoryDocumentChaptersRelation tblSystemDirectoryDocumentChaptersRelation);

    List<Map<String, Object>> getAllDocumentType();
    
    String selectNameById(Long id);
}