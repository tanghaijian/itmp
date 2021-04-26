package cn.pioneeruniverse.project.service.assetsLibrary;

import cn.pioneeruniverse.common.entity.AjaxModel;
import cn.pioneeruniverse.project.entity.*;
import cn.pioneeruniverse.project.entity.TblSystemDocumentType;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 11:53 2019/11/12
 * @Modified By:
 */
public interface SystemDirectoryService {

    List<TblSystemDirectory> getDirectoryTreeForDocumentLibrary(Long projectId);
    
    List<Map<String, Object>>  getDocumentTypes(Long directoryId);
    
    List<Map<String, Object>>   getSystemName(Long directoryId);
    
    List<Map<String, Object>> getDirectoryTree(String systemIds, HttpServletRequest request);

    Map<String, Object> getFilesUnderDirectory(Long systemId, Long documentType);

    List<TblSystemDirectoryDocumentAttachment> getAllAttachments(Long documentId);

    TblSystemDirectory addSystemDirectory(HttpServletRequest request, TblSystemDirectory tblSystemDirectory);

    List<TblSystemDirectoryDocumentHistory> getSystemDirectoryDocumentHistory(Long documentId);

    void delSystemDirectory(Long directoryId) throws Exception;

    void updateSystemDirectoryName(HttpServletRequest request, Long directoryId, String directoryName) throws Exception;

    TblSystemDirectoryDocument getDocumentById(Long id);

    void downLoadDocument(TblSystemDirectoryDocument tblSystemDirectoryDocument, HttpServletResponse response);

    AjaxModel upLoadNewDocument(MultipartFile[] files, TblSystemDirectoryDocument tblSystemDirectoryDocument, HttpServletRequest request) ;
    
    Long upNewMarkDocument(TblSystemDirectoryDocument tblSystemDirectoryDocument, HttpServletRequest request) ;
    
    void coverUploadOldDocument(MultipartFile[] files, TblSystemDirectoryDocument tblSystemDirectoryDocument, HttpServletRequest request) throws IOException;

    String upLoadDocument(MultipartFile file, TblSystemDirectoryDocument tblSystemDirectoryDocument, HttpServletRequest request) throws IOException;
   
    List<TblSystemDirectoryDocument> getDocumentFile(Long directoryId);

	void moveDocChapters(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters);
	
	void delectDocumentFile(Long directoryDocumentId);
	
	void signedOutDocumentFile(Long directoryDocumentId);

	List<TblSystemDirectoryDocumentChapters> getAllDocChapters(Long systemDirectoryDocumentId);
	
	TblSystemDirectoryDocument getMarkById(Long id);

	TblSystemDirectoryDocumentChapters addOrUpdateDocChapters(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters, HttpServletRequest request);

	void deleteDocChapters(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters, HttpServletRequest request);
    /**
     *@author liushan
     *@Description 章节上传附件
     *@Date 2020/1/15
     *@Param [files, directoryDocumentAttachment, request]
     *@return void
     **/
	Map<String, Object>  uploadChaptersFiles(MultipartFile[] files, TblSystemDirectoryDocumentAttachment directoryDocumentAttachment, HttpServletRequest request) throws Exception;

    /**
     *@author liushan
     *@Description 章节删除附件
     *@Date 2020/1/15
     *@Param [directoryDocumentAttachment, request]
     *@return void
     **/
    void removeChaptersFile(TblSystemDirectoryDocumentAttachment directoryDocumentAttachment, HttpServletRequest request);

	/**
	*@author liushan
	*@Description 查看本章节mark基本信息
	*@Date 2020/1/14
	*@Param [systemDirectoryDocumentChapters, request]
	*@return cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChapters
	**/
    Map<String, Object> selectChaptersMarkDown(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters, HttpServletRequest request) throws Exception;

    /**
    *@author liushan
    *@Description 查询章节所有附件
    *@Date 2020/1/15
    *@Param [systemDirectoryDocumentChapters, request]
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    List<TblSystemDirectoryDocumentAttachment> selectChaptersFiles(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters, HttpServletRequest request);

    /**
    *@author liushan
    *@Description 查询章节的关联所有需求和开发任务
    *@Date 2020/1/15
    *@Param [systemDirectoryDocumentChapters, request]
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    Map<String,Object> selectChaptersRelatedData(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters, HttpServletRequest request);

	List<TblSystemDocumentType> getSomeDocumentType();

    /**
     *@author liushan
     *@Description 查看章节markDown最新版本和上次版本对比
     *@Date 2020/2/19
     *@Param
     *@return
     **/
    Map<String,Object> selectChaptersMarkDownContrast(Integer type, TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters, HttpServletRequest request) throws Exception;

	Map<String, Object> getCurrentAuthority(Long id, Long projectId, HttpServletRequest request);
}
