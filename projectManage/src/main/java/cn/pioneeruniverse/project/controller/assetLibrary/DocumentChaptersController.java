package cn.pioneeruniverse.project.controller.assetLibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocument;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersRelation;
import cn.pioneeruniverse.project.service.assetsLibrary.DocumentChaptersService;
import cn.pioneeruniverse.project.vo.ZtreeVo;

/**
 * 
* @ClassName: DocumentChaptersController
* @Description: 新建类项目文档章节信息controller
* @author author
* @date 2020年8月5日 上午10:46:47
*
 */
@RestController
@RequestMapping("documentChapters")
public class DocumentChaptersController extends BaseController{
	private static final Logger logger = LoggerFactory.getLogger(DocumentLibraryController.class);
	
	@Autowired
	private DocumentChaptersService documentChaptersService;
	
	/**
	 * 根据文档id获取章节树
	 * @param systemDirectoryDocumentId
	 * @return map key data 返回的数据
	 *                 status 1正常返回，2异常返回
	 */
	@RequestMapping("/getChaptersTree")
	public Map<String, Object> getChaptersTree(Long systemDirectoryDocumentId) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<ZtreeVo> list = documentChaptersService.getChaptersTree(systemDirectoryDocumentId);
			map.put("data", list);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}
	
	/**
	 * 分页获取文档
	 * @param systemDirectoryDocumentId
	 * @return
	 */
	@RequestMapping("/selectDocumentByPage")
	public List<TblSystemDirectoryDocument> selectDocumentByPage(TblSystemDirectoryDocument tblSystemDirectoryDocument) {
		Map<String, Object> map = new HashMap<>();
		List<TblSystemDirectoryDocument> list = new ArrayList<TblSystemDirectoryDocument>();
		try {
			list = documentChaptersService.selectDocumentByPage(tblSystemDirectoryDocument);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return list;
	}
	
	/**
	 * 获取关联的文档章节树和关联的文档
	 * @param systemDirectoryDocumentId (被选中的文档)
	 * @param systemDirectoryDocumentChapterId1 (被选中的章节)
	 * @param systemDirectoryDocumentId1 (本文档)
	 * @return
	 */
	@RequestMapping("/getRelationDocumentAndChapters")
	public Map<String, Object> getRelationDocumentAndChapters(TblSystemDirectoryDocumentChaptersRelation tblSystemDirectoryDocumentChaptersRelation) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = documentChaptersService.getRelationDocumentAndChapters(tblSystemDirectoryDocumentChaptersRelation);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}
	
	/**
	 * 关联章节
	 * @param tblSystemDirectoryDocumentChaptersRelation
	 * @param chaptersIdStr
	 * @param request
	 * @return
	 */
	@RequestMapping("/insertChaptersRelation")
	public Map<String, Object> insertChaptersRelation(TblSystemDirectoryDocumentChaptersRelation tblSystemDirectoryDocumentChaptersRelation
			,String chaptersIdStr,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			documentChaptersService.insertChaptersRelation(tblSystemDirectoryDocumentChaptersRelation, chaptersIdStr, request);			
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}
	
	/**
	 * 获取所有关联章节信息
	 * @param systemDirectoryDocumentId1
	 * @return
	 */
	@RequestMapping("/getAllRelationChapters")
	public Map<String, Object> getAllRelationChapters(Long systemDirectoryDocumentId1) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = documentChaptersService.getAllRelationChapters(systemDirectoryDocumentId1);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}
	
	/**
	 * 根据文档id导出章节正文
	 * @param systemDirectoryDocumentId
	 * @param type(导出类型)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/exportByDocumentId")
	public Map<String, Object> exportByDocumentId(Long systemDirectoryDocumentId,String type,HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		try {
			documentChaptersService.exportByDocId(systemDirectoryDocumentId,type, request, response);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}
	/**
	 * 导出历史版本章节
	 * @param systemDirectoryDocumentId
	 * @param version
	 * @param type(导出类型)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/exportByDocumentIdAndVersion")
	public Map<String, Object> exportByDocumentIdAndVersion(Long systemDirectoryDocumentId,String type,Integer version,HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		try {
			documentChaptersService.exportByDocIdAndVersion(systemDirectoryDocumentId, version,type, request, response);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}
	
	/**
	 * 導出word
	 * @param idStr
	 * @param type(导出类型)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/export")
	public Map<String, Object> export(Long systemDirectoryDocumentId,String idStr,String type,HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		try {
			documentChaptersService.export(systemDirectoryDocumentId,idStr,type, request, response);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}
	
    /**
     * 文档版本对比
     * @param requirementCode
     * @param request
     * @param response
     * @return
     * liushan
     */
    @RequestMapping("/getVersionContrast")
    public Map<String, Object> getVersionContrast(Long systemDirectoryDocumentId,String requirementCode,HttpServletRequest request,HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        try {
            map = documentChaptersService.getVersionContrast(systemDirectoryDocumentId,requirementCode, request, response);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            map = super.handleException(e, e.getMessage());
        }
        return map;
    }
}
