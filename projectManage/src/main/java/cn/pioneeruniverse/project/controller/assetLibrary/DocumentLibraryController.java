package cn.pioneeruniverse.project.controller.assetLibrary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.entity.AjaxModel;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.project.entity.TblSystemDirectory;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocument;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentAttachment;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChapters;
import cn.pioneeruniverse.project.service.assetsLibrary.SystemDirectoryService;

/**
 * @Author: xukai [xukai@pioneerservice.cn]
 * @Description: 文档库操作，返回map key status :1正常返回，2异常返回
 *                                      其他data： 返回的数据
 * @Date: Created in 17:33 2019/12/26
 * @Modified By:
 */
@RestController
@RequestMapping("documentLibrary")
public class DocumentLibraryController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(DocumentLibraryController.class);

    @Autowired
    private SystemDirectoryService systemDirectoryService;


    @Value("${s3.documentBucket}")
    private String documentBucket;

    /**
     * @param projectId
     * @return java.util.List<cn.pioneeruniverse.project.entity.TblSystemDirectory>
     * @Description 获取文档库文档树目录
     * @MethodName getDirectoryTree
     * @author xukai [xukai@pioneerservice.cn]
     * @Date 2019/12/27 11:37
     */
    @RequestMapping(value = "getDirectoryTree", method = RequestMethod.POST)
    public List<TblSystemDirectory> getDirectoryTree(@RequestParam("projectId") Long projectId) {
        return systemDirectoryService.getDirectoryTreeForDocumentLibrary(projectId);
    }


    /**
     * 
    * @Title: addDirectory
    * @Description: 新增文档目录
    * @author author
    * @param request
    * @param tblSystemDirectory
    * @return Map key  status:1正常返回，2异常返回
    *                  data:返回的数据
    * @throws
     */
    @RequestMapping(value = "addDirectory", method = RequestMethod.POST)
    public Map<String, Object>  addDirectory(HttpServletRequest request, TblSystemDirectory tblSystemDirectory) {
    	Map<String, Object> map = new HashMap<>();
    	try {
        	TblSystemDirectory systemDirectory= systemDirectoryService.addSystemDirectory(request, tblSystemDirectory);
        	map.put("data",systemDirectory.getId());
        	map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            map = super.handleException(e, e.getMessage());
        }
    	return map;
    }

    /**
     * @param directoryId
     * @return cn.pioneeruniverse.common.entity.AjaxModel
     * @Description 删除文档目录
     * @MethodName delDirectory
     * @Date 2019/12/27 16:05
     */
    @RequestMapping(value = "delDirectory", method = RequestMethod.POST)
    public AjaxModel delDirectory(@RequestParam("id") Long id) {
        try {
            systemDirectoryService.delSystemDirectory(id);
            return AjaxModel.SUCCESS();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
     * @param request
     * @param directoryId
     * @param directoryName
     * @Description 更新文档目录名
     * @MethodName updateDirectoryName
     */
    @RequestMapping(value = "updateDirectoryName", method = RequestMethod.POST)
    public AjaxModel updateDirectoryName(HttpServletRequest request, @RequestParam("directoryId") Long directoryId, @RequestParam("directoryName") String directoryName) {
        try {
            systemDirectoryService.updateSystemDirectoryName(request, directoryId, directoryName);
            return AjaxModel.SUCCESS();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
     * @param response
     * @param tblSystemDirectoryDocument
     * @return void
     * @Description 下载文档
     * @MethodName downloadDocument
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/12/27 17:39
     */
    @RequestMapping(value = "downloadDocument", method = RequestMethod.GET)
    public void downloadDocument(HttpServletResponse response, TblSystemDirectoryDocument tblSystemDirectoryDocument) {
        try {
            systemDirectoryService.downLoadDocument(systemDirectoryService.getDocumentById(tblSystemDirectoryDocument.getId()), response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }



    /**selectChaptersMarkDown
     * @param file
     * @param tblSystemDirectoryDocument
     * @param request
     * @return  
     * @Description 上传新文档
     */
    @RequestMapping(value = "uploadNewDocument", method = RequestMethod.POST)
    public AjaxModel uploadDocument(@RequestParam("file") MultipartFile[] file, TblSystemDirectoryDocument tblSystemDirectoryDocument, HttpServletRequest request) {
        return systemDirectoryService.upLoadNewDocument(file, tblSystemDirectoryDocument, request);
    }
    /**
     * 上传markDown文件
     * @param tblSystemDirectoryDocument
     * @param request
     * @return map key documentMarkId 文档ID
     *                 status 1正常返回，2异常返回
     */
    @RequestMapping(value = "upNewMarkDocument", method = RequestMethod.POST)
    public Map<String, Object> upNewMarkDocument( TblSystemDirectoryDocument tblSystemDirectoryDocument, HttpServletRequest request) {
    	Map<String, Object> map=new HashMap<>();
    	try {
           Long documentId=systemDirectoryService.upNewMarkDocument(tblSystemDirectoryDocument, request);
           map.put("documentMarkId", documentId);
           map.put("status",  Constants.ITMP_RETURN_SUCCESS);
            
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            map.put("status", 2);
        }
    	return map;
    }
    /**
     * @param files
     * @param tblSystemDirectoryDocument
     * @param request
     * @return cn.pioneeruniverse.common.entity.AjaxModel
     * @Description 覆盖上传文档
     */
    @RequestMapping(value = "coverUploadOldDocument", method = RequestMethod.POST)
    public AjaxModel coverUploadOldDocument(@RequestParam("file") MultipartFile[] files, TblSystemDirectoryDocument tblSystemDirectoryDocument, HttpServletRequest request) {
        try {
            systemDirectoryService.coverUploadOldDocument(files, tblSystemDirectoryDocument, request);
            return AjaxModel.SUCCESS();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return AjaxModel.FAIL(e);
        }
    }
    /**
     * 获取文件类型
     * @param directoryId
     * @return
     */
    @RequestMapping(value = "getDocumentTypes", method = RequestMethod.POST)
    public  Map<String, Object>   getDocumentTypes(@RequestParam("directoryId") Long directoryId) {
        Map<String, Object> map=new HashMap<String, Object>();
        List<Map<String, Object>>  list= systemDirectoryService.getSystemName(directoryId);
        map.put("data", systemDirectoryService.getDocumentTypes(directoryId));
        map.put("system", list); 
    	return map;
    }
    /**
     * 获取目录下文件
     * @param id
     * @return
     */
    @RequestMapping(value = "getDocumentFile", method = RequestMethod.POST)
    public  Map<String, Object>   getDocumentFile(@RequestParam("id") Long id,Long projectId,HttpServletRequest request) {
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("data", systemDirectoryService.getDocumentFile(id));
        map.put("type", "documents");
       
        //查询当前登录用户对该目录的权限  ztt
       /* try {
        	Map<String, Object> authMap = systemDirectoryService.getCurrentAuthority(id,projectId,request);
        	map.put("auth", authMap);
		} catch (Exception e) {
			return super.handleException(e, "查询系统文档目录权限失败！");
		}*/
    	return map;
    }
    
    /**
     * 移除文档
     * @param directoryDocumentId
     * @return
     */
    @RequestMapping(value = "delectDocumentFile", method = RequestMethod.POST)
    public  AjaxModel   delectDocumentFile(@RequestParam("id") Long directoryDocumentId) {
    	 try {
    		 systemDirectoryService.delectDocumentFile(directoryDocumentId);
             return AjaxModel.SUCCESS();
         } catch (Exception e) {
             logger.error(e.getMessage(), e);
             return AjaxModel.FAIL(e);
         }
    }
    
    /**
     * 
    * @Title: signedOutDocumentFile
    * @Description: 签出文档
    * @author author
    * @param directoryDocumentId
    * @return
    * @throws
     */
    @RequestMapping(value = "signedOutDocumentFile", method = RequestMethod.POST)
    public  AjaxModel   signedOutDocumentFile(@RequestParam("id") Long directoryDocumentId) {
    	 try {
    		 systemDirectoryService.signedOutDocumentFile(directoryDocumentId);
             return AjaxModel.SUCCESS();
         } catch (Exception e) {
             logger.error(e.getMessage(), e);
             return AjaxModel.FAIL(e);
         }
    }
    /**
     * 查询进入mark基本信息
     * @param id
     * @return
     */
    @RequestMapping(value = "seletMarkFileId", method = RequestMethod.POST)
    public  Map<String, Object>   seletMarkFileId(@RequestParam("id") Long id) {
    	Map<String, Object> map=new HashMap<String, Object>();
    	try {
    		map.put("data", systemDirectoryService.getMarkById(id));
    		map.put("status", Constants.ITMP_RETURN_SUCCESS);
         } catch (Exception e) {
             logger.error(e.getMessage(), e);
             map.put("status",2);
         }
    	return map;
    }
    
    /**
     * 
    * @Title: temporaryStorageMark
    * @Description: 暂存markdown
    * @author author
    * @param id
    * @return
    * @throws
     */
    @RequestMapping(value = "temporaryStorageMark", method = RequestMethod.POST)
    public  Map<String, Object>   temporaryStorageMark(@RequestParam("id") Long id) {
    	Map<String, Object> map=new HashMap<String, Object>();
    	try {
         } catch (Exception e) {
             logger.error(e.getMessage(), e);
         }
    	return map;
    }
    
    /**
     * 
    * @Title: getAllDocChapters
    * @Description: 获取所有章节
    * @author author
    * @param systemDirectoryDocumentId
    * @return
    * @throws
     */
    @RequestMapping("getAllDocChapters")
    public Map<String,Object> getAllDocChapters(Long systemDirectoryDocumentId ){
    	 Map<String, Object> map = new HashMap<String, Object>();
    	 try {
    		 List<TblSystemDirectoryDocumentChapters> chapters = systemDirectoryService.getAllDocChapters(systemDirectoryDocumentId);
    		 map.put("chapters",chapters );
    		 map.put("status", Constants.ITMP_RETURN_SUCCESS);
    	 }catch (Exception e) {
 			return super.handleException(e, "获取文档目录失败！");
 		}
     	 return map;
    }

    
    
   /**
    * 
   * @Title: moveDocChapters
   * @Description: 移动章节
   * @author author
   * @param systemDirectoryDocumentChapters
   * @return
   * @throws
    */
    @RequestMapping("moveDocChapters")
    public Map<String,Object> moveDocChapters(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters){
    	 Map<String, Object> map = new HashMap<String, Object>();
    	 try {
    		 systemDirectoryService.moveDocChapters(systemDirectoryDocumentChapters);
    		 map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			return super.handleException(e, "移动文档目录失败！");
		}
    	 return map;
    }
    
    /**
     * 章节 编辑 新增
     * @author tingting
     * */
    @RequestMapping("addOrUpdateDocChapters")
    public Map<String,Object> addOrUpdateDocChapters(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters,HttpServletRequest request){
    	 Map<String, Object> map = new HashMap<String, Object>();
    	 try {
    		 TblSystemDirectoryDocumentChapters sysDocChapters =  systemDirectoryService.addOrUpdateDocChapters(systemDirectoryDocumentChapters,request);
    		 map.put("sysDocChapters", sysDocChapters);
    		 map.put("status", Constants.ITMP_RETURN_SUCCESS);
    	 }catch (Exception e) {
 			return super.handleException(e, "编辑或新增章节失败！");
 		}
     	 return map;
    }
    
    /**
     * 删除章节
     * @author tingting
     * */
    @RequestMapping("deleteDocChapters")
    public Map<String,Object> deleteDocChapters(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters,HttpServletRequest request){
    	 Map<String, Object> map = new HashMap<String, Object>();
    	 try {
    		 systemDirectoryService.deleteDocChapters(systemDirectoryDocumentChapters,request);
    		 map.put("status", Constants.ITMP_RETURN_SUCCESS);
    	 }catch (Exception e) {
  			return super.handleException(e, "删除章节失败！");
  		}
      	 return map;
    }

    /**
    *@author liushandownObject
    *@Description 章节上传附件
    *@Date 2020/1/15
    *@Param [file, request]
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @RequestMapping(value = "uploadChaptersFiles", method = RequestMethod.POST)
    public Map<String,Object> uploadChaptersFiles(@RequestParam("files") MultipartFile[] files, TblSystemDirectoryDocumentAttachment directoryDocumentAttachment, HttpServletRequest request) throws Exception{

        Map<String, Object> map = new HashMap<String, Object>();
        try {
        	map = systemDirectoryService.uploadChaptersFiles(files, directoryDocumentAttachment, request);
            map.put("status",1);
            return map;
        }catch (Exception e) {
            super.handleException(e, "操作失败！");
            map.put("status", 0);
            return map;
        }
    }

    /**
     *@author liushan
     *@Description 章节删除附件
     *@Date 2020/1/15
     *@Param [file, request]
     *@return java.util.Map<java.lang.String,java.lang.Object>
     **/
    @PostMapping(value = "removeChaptersFile")
    public Map<String,Object> removeChaptersFile(TblSystemDirectoryDocumentAttachment directoryDocumentAttachment, HttpServletRequest request){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            systemDirectoryService.removeChaptersFile(directoryDocumentAttachment,request);
        }catch (Exception e) {
            return super.handleException(e, "操作失败！");
        }
        return map;
    }

    /**
    *@author liushan
    *@Description 查看本章节mark基本信息
    *@Date 2020/1/14
    *@Param
    *@return
    **/
    @PostMapping(value = "selectChaptersMarkDown")
    public Map<String,Object> selectChaptersMarkDown(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters,HttpServletRequest request) throws  Exception{
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            map = systemDirectoryService.selectChaptersMarkDown(systemDirectoryDocumentChapters,request);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
            map.put("currentUserId", CommonUtil.getCurrentUserId(request));
        }catch (Exception e) {
            return super.handleException(e, "查询失败！");
        }
        return map;
    }



    /**
     *@author liushan
     *@Description 查看章节markDown最新版本和上次版本对比
     * type: 1是添加条目数量查看操作，2 是修改条目数量查看操作 3 是删除条目数量查看操作
     *@Date 2020/2/19
     *@Param
     *@return
     **/
    @PostMapping(value = "selectChaptersMarkDownContrast")
    public Map<String,Object> selectChaptersMarkDownContrast(Integer type,TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters,HttpServletRequest request){
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = systemDirectoryService.selectChaptersMarkDownContrast(type,systemDirectoryDocumentChapters,request);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        }catch (Exception e) {
            return super.handleException(e, "查询失败！");
        }
        return map;
    }

    /**
    *@author liushan
    *@Description 查询章节所有附件
    *@Date 2020/1/15
    *@Param [systemDirectoryDocumentChapters, request]
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @PostMapping(value = "selectChaptersFiles")
    public Map<String,Object> selectChaptersFiles(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters,HttpServletRequest request){
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map.put("files", systemDirectoryService.selectChaptersFiles(systemDirectoryDocumentChapters,request));
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            return super.handleException(e, "查询失败！");
        }
        return map;
    }

    /**
    *@author liushan
    *@Description 查询章节的关联所有需求和开发任务
    *@Date 2020/1/15
    *@Param [systemDirectoryDocumentChapters, request]
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @PostMapping(value = "selectChaptersRelatedData")
    public Map<String,Object> selectChaptersRelatedData(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters,HttpServletRequest request){
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = systemDirectoryService.selectChaptersRelatedData(systemDirectoryDocumentChapters,request);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            return super.handleException(e, "查询失败！");
        }
        return map;
    }
}
