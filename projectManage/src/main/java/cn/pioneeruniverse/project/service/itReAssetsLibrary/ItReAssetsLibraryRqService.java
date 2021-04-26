package cn.pioneeruniverse.project.service.itReAssetsLibrary;



import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocument;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentAttachment;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChapters;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;


/**
*  数据库配置
* @author:xukai
*
*/

public interface ItReAssetsLibraryRqService {
	List<TblSystemDirectoryDocument> getDocumentByRequirement(String requirementCode,String featureCode);
	
	Long getRequirementId(String requirementCode);
	
	Map<String, Object> getTypeAndSystem(String featureCode);
	
	void upItLoadNewDocument(MultipartFile[] files, TblSystemDirectoryDocument tblSystemDirectoryDocument,String currentUserAccount ,String[] fileName,HttpServletRequest request) ;
	
	Long upItNewMarkDocument(TblSystemDirectoryDocument tblSystemDirectoryDocument,String currentUserAccount, HttpServletRequest request) ;
	
	String upLoadDocument(MultipartFile file, TblSystemDirectoryDocument tblSystemDirectoryDocument, String fileName,HttpServletRequest request) throws IOException;
	
	void coverUploadOldDocument(MultipartFile[] files, TblSystemDirectoryDocument tblSystemDirectoryDocument,String currentUserAccount,String[] fileName ,HttpServletRequest request) throws IOException;

	Map<String, Object> selectChaptersMarkDown(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters,String currentUserAccount, HttpServletRequest request) throws Exception;

	List<TblSystemDirectoryDocumentAttachment> selectChaptersFiles(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters, HttpServletRequest request);
	
	Map<String,Object> selectChaptersRelatedData(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters, HttpServletRequest request);

	TblSystemDirectoryDocumentChapters addOrUpdateDocChapters(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters,String currentUserAccount, HttpServletRequest request);

	void delectDocumentFile(Long directoryDocumentId,Long documentType,String currentUserAccount ,String requirementCode,String featureCode);
	
}
