package cn.pioneeruniverse.project.service.itReAssetsLibrary;

import cn.pioneeruniverse.project.entity.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface ItReSystemDirectoryService {

    
    List<TblSystemDirectoryDocumentHistory> getSystemDirectoryDocumentHistory(Long documentId,String requirementCode,String featureCode);
    
    List<TblSystemDirectoryDocumentChapters> getAllDocChapters(Long systemDirectoryDocumentId);
    
    TblSystemDirectory addSystemDirectory(HttpServletRequest request, TblSystemDirectory tblSystemDirectory);
    
    void delSystemDirectory(Long directoryId) throws Exception;
    
    void updateSystemDirectoryName(HttpServletRequest request, Long directoryId, String directoryName) throws Exception;

    List<TblSystemDirectory> getDirectoryTreeForDocumentLibrary(Long projectId);
    
    void moveDocChapters(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters);
	
	void deleteDocChapters(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters,String currentUserAccount, HttpServletRequest request);
    
	void removeChaptersFile(TblSystemDirectoryDocumentAttachment directoryDocumentAttachment,String currentUserAccount, HttpServletRequest request);

	Map<String,Object> selectChaptersMarkDownContrast(Integer type, TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters, HttpServletRequest request) throws Exception;

	Map<String, Object> selectChaptersMarkDown(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters, HttpServletRequest request) throws Exception;
	 /**
     *@author liushan
     *@Description 章节上传附件
     *@Param [files, directoryDocumentAttachment, request]
     *@return void
     **/
	Integer uploadChaptersFiles(MultipartFile[] files, TblSystemDirectoryDocumentAttachment directoryDocumentAttachment, String requirementCode,String featureCode,String currentUserAccount,String[] fileName,HttpServletRequest request) throws Exception;

}
