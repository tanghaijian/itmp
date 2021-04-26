package cn.pioneeruniverse.project.controller.itReAssetLibrary;

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
import cn.pioneeruniverse.project.service.itReAssetsLibrary.ItReDocumentChaptersService;
import cn.pioneeruniverse.project.vo.ZtreeVo;

/**
 * 
 * @author xukai
 *
 */
@RestController
@RequestMapping("itReDocumentChapters")
public class itReDocumentChaptersController extends BaseController{
	private static final Logger logger = LoggerFactory.getLogger(itReDocumentChaptersController.class);
	
	@Autowired
	private ItReDocumentChaptersService reDocumentChaptersService;
	@Autowired
	private DocumentChaptersService documentChaptersService;
	
	/**
	 * 根据文档id获取章节树
	 * @param systemDirectoryDocumentId
	 * @return map data:章节树，status：1正常返回，2异常返回
	 */
	@RequestMapping("/getChaptersTree")
	public Map<String, Object> getChaptersTree(Long systemDirectoryDocumentId) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<ZtreeVo> list = reDocumentChaptersService.getChaptersTree(systemDirectoryDocumentId);
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
	 * @return List 系统目录文档
	 */
	@RequestMapping("/selectDocumentByPage")
	public List<TblSystemDirectoryDocument> selectDocumentByPage(TblSystemDirectoryDocument tblSystemDirectoryDocument) {
		Map<String, Object> map = new HashMap<>();
		tblSystemDirectoryDocument.setSaveType(2);
		List<TblSystemDirectoryDocument> list = new ArrayList<TblSystemDirectoryDocument>();
		try {
			list = reDocumentChaptersService.selectDocumentByPage(tblSystemDirectoryDocument);
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
	 * @return map key :documents 文档列表
		                chapters  章节列表
		                status 1 正常返回 2异常返回
	 */
	@RequestMapping("/getRelationDocumentAndChapters")
	public Map<String, Object> getRelationDocumentAndChapters(TblSystemDirectoryDocumentChaptersRelation tblSystemDirectoryDocumentChaptersRelation) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = reDocumentChaptersService.getRelationDocumentAndChapters(tblSystemDirectoryDocumentChaptersRelation);
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
	 * @return status 1正常返回，2异常返回
	 */
	@RequestMapping("/insertChaptersRelation")
	public Map<String, Object> insertChaptersRelation(TblSystemDirectoryDocumentChaptersRelation tblSystemDirectoryDocumentChaptersRelation
			,String chaptersIdStr,HttpServletRequest request,String currentUserAccount) {
		Map<String, Object> map = new HashMap<>();
		try {
			reDocumentChaptersService.insertChaptersRelation(tblSystemDirectoryDocumentChaptersRelation, chaptersIdStr,currentUserAccount, request);			
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}
	
	/**
	 * 获取所有关联章节信息
	 * @param systemDirectoryDocumentId1
	 * @return map rows:章节信息列表
	 *             status 1正常返回，2异常返回
	 */
	@RequestMapping("/getAllRelationChapters")
	public Map<String, Object> getAllRelationChapters(Long systemDirectoryDocumentId1) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = reDocumentChaptersService.getAllRelationChapters(systemDirectoryDocumentId1);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}
	
	/**
	 * 根据文档id导出章节正文
	 * @param systemDirectoryDocumentId
	 * @param type word和pdf
	 * @param request
	 * @param response map status 1正常返回，2异常返回
	 * @return
	 */
	@RequestMapping("/exportByDocumentId")
	public Map<String, Object> exportByDocumentId(Long systemDirectoryDocumentId,String type,HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		try {
			reDocumentChaptersService.exportByDocId(systemDirectoryDocumentId,type, request, response);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}
	/**
	 * 导出历史版本章节
	 * @param systemDirectoryDocumentId
	 * @param version 版本号
	 * @param type word和pdf
	 * @param request
	 * @param response map status 1正常返回，2异常返回
	 * @return
	 */
	@RequestMapping("/exportByDocumentIdAndVersion")
	public Map<String, Object> exportByDocumentIdAndVersion(Long systemDirectoryDocumentId,String type,Integer version,HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		try {
			reDocumentChaptersService.exportByDocIdAndVersion(systemDirectoryDocumentId, version,type, request, response);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}
	
	/**
	 * 導出word
	 * @param idStr
	 * @param type word和pdf
	 * @param request
	 * @param response map status 1正常返回，2异常返回
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
     * @param requirementCode 需求编号
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getVersionContrast")
    public Map<String, Object> getVersionContrast(Long systemDirectoryDocumentId,String requirementCode,String featureCode,HttpServletRequest request,HttpServletResponse response) {
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
