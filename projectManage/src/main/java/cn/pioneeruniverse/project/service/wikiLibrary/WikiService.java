package cn.pioneeruniverse.project.service.wikiLibrary;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import cn.pioneeruniverse.common.utils.ResponseMessageModel;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocument;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentAttachment;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChapters;
import cn.pioneeruniverse.project.vo.SystemDirectoryDocumentVO;

public interface WikiService {
	Long getSystemDirectory(Long projectId,HttpServletRequest request);
	

	List<Map<String, Object>> selectSystemByProjectId(Long projectId,String systemName);
	
	ResponseMessageModel richTextSubmit(HttpServletRequest request,SystemDirectoryDocumentVO systemDirectoryDocumentVO);
	/**
     * 系统目录文档迁出
     * @param id
     * @param request
     * @return
     */
	Map systemDirectoryDocumentSignOffById(Long id, HttpServletRequest request);
	 /**
     * 暂存
     * @param request
     * @param systemDirectoryDocumentVO
     * @return
     * @throws IOException
     */
    ResponseMessageModel addTemporaryStorageById(HttpServletRequest request, SystemDirectoryDocumentVO systemDirectoryDocumentVO) throws IOException;

    /**
     * 查询章节所有附件
     * @param systemDirectoryDocumentChapters
     * @param request
     * @return
     */
    List<TblSystemDirectoryDocumentAttachment> selectChaptersFiles(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters, HttpServletRequest request);
   
    /**
     * 根据项目ID和系统ID查询章节
     * @param projectId
     * @param systemId
     * @return
     */
    List<TblSystemDirectoryDocumentChapters> selectWikiTree(Long projectId,Long systemId);
    /**
     * 查看本章节mark基本信息
     * @param systemDirectoryDocumentChapters
     * @param request
     * @return
     * @throws Exception
     */
    Map<String, Object> selectChaptersMarkDown(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters, HttpServletRequest request) throws Exception;
   /**
    * 查询章节
    * @param systemDirectoryDocumentId
    * @return
    */
    List<TblSystemDirectoryDocument> getAllDocChapters(String systemDirectoryDocumentIds);
}
