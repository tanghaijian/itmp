package cn.pioneeruniverse.project.service.assetsLibrary;
/**
 * 
 * @author fanwentao
 *
 */

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;

import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocument;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersRelation;
import cn.pioneeruniverse.project.vo.ZtreeVo;

public interface DocumentChaptersService {
	/**
	 * 根据文档id获取章节树
	 * 
	 * @param systemDirectoryDocumentId
	 * @return
	 */
	List<ZtreeVo> getChaptersTree(Long systemDirectoryDocumentId);

	/**
	 * 分页查询系统目录文档列表(bootstrap)
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param tblSystemDirectoryDocument
	 * @return
	 */
	List<TblSystemDirectoryDocument> selectDocumentByPage(TblSystemDirectoryDocument tblSystemDirectoryDocument);

	/**
	 * 获取关联的文档章节树和关联的文档
	 * @param tblSystemDirectoryDocumentChaptersRelation
	 * @return
	 */
	Map<String, Object> getRelationDocumentAndChapters(TblSystemDirectoryDocumentChaptersRelation tblSystemDirectoryDocumentChaptersRelation);
	
	/**
	 * 关联章节
	 * @param tblSystemDirectoryDocumentChaptersRelation
	 */
	void insertChaptersRelation(TblSystemDirectoryDocumentChaptersRelation tblSystemDirectoryDocumentChaptersRelation,String chaptersIdStr,HttpServletRequest request) throws Exception;
	
	/**
	 * 获取所有关联章节关系
	 * @param systemDirectoryDocumentId1
	 * @return
	 */
	Map<String, Object> getAllRelationChapters(Long systemDirectoryDocumentId1);
	
	/**
	 * 根据文档id导出章节正文
	 * @param systemDirectoryDocumentId
	 * @param type(导出类型)
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	void exportByDocId(Long systemDirectoryDocumentId,String type,HttpServletRequest request,HttpServletResponse response)  throws Exception ;
	
	/**
	 * 导出历史版本章节正文
	 * @param systemDirectoryDocumentId
	 * @param type(导出类型)
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	void exportByDocIdAndVersion(Long systemDirectoryDocumentId,Integer version,String type,HttpServletRequest request,HttpServletResponse response)  throws Exception ;
	
	/**
	 * 根据章节id生成章节正文
	 * @param idStr
	 * @param type(导出类型)
	 * @param request
	 * @param response
	 */
	void export(Long systemDirectoryDocumentId,String ids,String type,HttpServletRequest request,HttpServletResponse response)  throws Exception ;

	/**
	 * 文档版本对比
	 * @param requirementCode
	 * @param request
	 * @param response
	 * @return
	 * liushan
	 */
    Map<String,Object> getVersionContrast(Long systemDirectoryDocumentId,String requirementCode, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
