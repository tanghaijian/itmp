package cn.pioneeruniverse.project.service.assetsLibrary;



import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocument;

import java.util.Map;

/**
*  数据库配置
* @author:weiji
*
*/

public interface IAssetsLibraryRqService {


    Map<String, Object> getDeptdirectory(Map<String, Object> result, long[] requireIds, long[] systemIds, long[] reqTaskIds, String type, String znodeId,String search);

    Map<String, Object> getSystemDirectory(Map<String, Object> result, long[] requireIds, long[] systemIds, long[] reqTaskIds, String type, String znodeId,String search);

    //Map<String, Object> getRelationInfo(Map<String, Object> result, TblSystemDirectoryDocument tblSystemDirectoryDocument);

   // Map<String, Object> getRequiredir(Map<String, Object> result, String requireId, String pid);

    Map<String, Object> getRelationInfo(Map<String, Object> result, String documetId);

    Map<String, Object> getDocumentHistory(Map<String, Object> result, String documetId);

    Map<String, Object> getAttachments(Map<String, Object> result, String documetId);

    Map<String, Object> getDocuments(Map<String, Object> result, long requireId, String documentType,String systemId);


    Map<String, Object> getAttachmentsByChapter(Map<String, Object> result, String chapterId);

    Map<String, Object> getAttachmentsByDocumetid(Map<String, Object> result, String documetId, String chapterId);

    Map<String, Object> getChapterIdRelation(Map<String, Object> result, String chapterId);

    Map<String, Object> getChapterRelation(String chapterId);
}
