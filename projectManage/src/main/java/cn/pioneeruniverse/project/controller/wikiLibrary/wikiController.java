package cn.pioneeruniverse.project.controller.wikiLibrary;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.ResponseMessageModel;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChapters;
import cn.pioneeruniverse.project.service.wikiLibrary.WikiService;
import cn.pioneeruniverse.project.vo.SystemDirectoryDocumentVO;

/**
 * 
* @ClassName: wikiController
* @Description: 运维类项目wiki操作，章节分两大类，一类是普通文件：以附件形式存储。二类是markdown：以纯文本和Html两种形式存储
* @author author
* @date 2020年8月28日 下午2:03:01
*
 */
@RestController
@RequestMapping("wikiLibrary")
public class wikiController extends BaseController{
	@Autowired
	private WikiService wikiService;
	
	/**
	 * 
	* @Title: getWikiTree
	* @Description: 获取该项目对应的系统目录ID，有就直接获取返回，没有就新增在返回给页面
	* @author author
	* @param projectId 项目ID
	* @param request
	* @return map systemDiretoryId 系统目录ID
	*             status 1正常返回，2异常返回
	 */
	@RequestMapping(value = "getWikiTree", method = RequestMethod.POST)
	public Map<String, Object> getWikiTree(Long projectId,HttpServletRequest request){
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			Long systemDiretoryId=wikiService.getSystemDirectory(projectId,request);
			//List<Map<String, Object>> list=wikiService.selectSystemByProjectId(projectId, systemName)
			result.put("systemDiretoryId", systemDiretoryId);
		 }catch (Exception e) {
			 return handleException(e, "操作失败！");
		}
		return result;
	}
	
	
	/**
	 * 
	* @Title: selectSystemByProjectId
	* @Description: 获取某个项目的系统 填充查询下拉框内容
	* @author author
	* @param projectId 项目ID
	* @param systemName 系统名
	* @return map id 系统ID
	*             systemName 系统名 
	*             systemDirectoryDocumentId 文档ID
	 */
	@RequestMapping(value = "selectSystemByProjectId", method = RequestMethod.POST)
	public List<Map<String, Object>> selectSystemByProjectId(Long projectId,String systemName){
		
		return wikiService.selectSystemByProjectId(projectId, systemName);
	}
	
	/**
	 * 
	 * @deprecated 废弃
	 * @param projectId
	 * @param systemId
	 * @return List<TblSystemDirectoryDocumentChapters>
	 */
	@RequestMapping(value = "selectWikiTree", method = RequestMethod.POST)
	public List<TblSystemDirectoryDocumentChapters> selectWikiTree(Long projectId,Long systemId){
		return wikiService.selectWikiTree(projectId, systemId);
	}
	
	/**
	 * 
	* @Title: richTextSubmit
	* @Description: wiki富文本内容提交
	* @author author
	* @param request
	* @param systemDirectory 文档
	* @param contentHtml 文档的html内容
	* @return ResponseMessageModel
	 */
	@RequestMapping("submit")
	public ResponseMessageModel richTextSubmit(HttpServletRequest request,String systemDirectory, String contentHtml){
		SystemDirectoryDocumentVO directory = JSONObject.parseObject(systemDirectory, SystemDirectoryDocumentVO.class);
		directory.setContentHtml(contentHtml);
		return wikiService.richTextSubmit(request, directory);
	}

	
	 /**
	  * 
	 * @Title: systemDirectoryDocumentSignOffById
	 * @Description: 文档章节签出
	 * @author author
	 * @param id  章节ID
	 * @param request
	 * @return map systemDirectory  章节信息SystemDirectoryDocumentVO
	               code 0失败，其他成功
	               content 存于S3中的文档内容
	  */
    @RequestMapping("signOff")
    public Map systemDirectoryDocumentSignOffById(Long id, HttpServletRequest request){
        Map map = wikiService.systemDirectoryDocumentSignOffById(id, request);
        return map;
    }
    /**
     * 系统目录文档暂存
     * @param request
     * @param systemDirectory 章节信息
     * @return ResponseMessageModel
     * @throws IOException
     */
    @RequestMapping("temporaryStorage")
    private ResponseMessageModel systemDirectoryDocumentTemporaryStorage(HttpServletRequest request, String systemDirectory) throws IOException {
        SystemDirectoryDocumentVO directory = JSONObject.parseObject(systemDirectory, SystemDirectoryDocumentVO.class);
        return wikiService.addTemporaryStorageById(request, directory);
    }

    /**
     * 查询章节所有附件
     * @param systemDirectoryDocumentChapters 章节信息
     * @param request
     * @return map files:章节附件信息
     *             status 1正常返回，2异常返回
     */
     @PostMapping(value = "selectChaptersFiles")
     public Map<String,Object> selectChaptersFiles(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters,HttpServletRequest request){
         Map<String, Object> map = new HashMap<String, Object>();
         try {
        	 //章节附件
             map.put("files", wikiService.selectChaptersFiles(systemDirectoryDocumentChapters,request));
             map.put("status", Constants.ITMP_RETURN_SUCCESS);
         } catch (Exception e) {
             return super.handleException(e, "查询失败！");
         }
         return map;
     }
     
     /**
      * 查看本章节wiki基本信息
      * @param systemDirectoryDocumentChapters
      * @param request
      * @return Map<String,Object>
      * @throws Exception
      */
      @PostMapping(value = "selectChaptersMarkDown")
      public Map<String,Object> selectChaptersMarkDown(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters,HttpServletRequest request) throws  Exception{
          Map<String, Object> map = new HashMap<String, Object>();

          try {
              map = wikiService.selectChaptersMarkDown(systemDirectoryDocumentChapters,request);
              map.put("status", Constants.ITMP_RETURN_SUCCESS);
              map.put("currentUserId", CommonUtil.getCurrentUserId(request));
          }catch (Exception e) {
              return super.handleException(e, "查询失败！");
          }
          return map;
      }
      
      /**
       * 根据文档ID查询所有章节信息
       * @param systemDirectoryDocumentIds
       * @return map data章节信息
       *             status 1正常返回 2异常返回
       */
      @RequestMapping("getAllDocChapters")
      public Map<String,Object> getAllDocChapters(String systemDirectoryDocumentIds ){
      	 Map<String, Object> map = new HashMap<String, Object>();
      	 try {
      		map.put("data", wikiService.getAllDocChapters(systemDirectoryDocumentIds));
      		map.put("status", Constants.ITMP_RETURN_SUCCESS);
      	 }catch (Exception e) {
   			return super.handleException(e, "获取文档目录失败！");
   		}
       	 return map;
      }
}
