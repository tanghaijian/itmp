package cn.pioneeruniverse.project.controller.itReAssetLibrary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import cn.pioneeruniverse.project.service.itReAssetsLibrary.ItReAssetsLibraryRqService;
import cn.pioneeruniverse.project.service.itReAssetsLibrary.ItReSystemDirectoryService;

/**
 * 
* @ClassName: itReDocumentLibraryController
* @Description: IT全流程文档管理
* @author author
* @date 2020年9月9日 下午1:17:45
*
 */
@RestController
@RequestMapping("itRedocumentLibrary")
public class itReDocumentLibraryController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(itReDocumentLibraryController.class);
    @Autowired
    private ItReAssetsLibraryRqService itReAssetsLibraryRqService;
    
    @Autowired
    private ItReSystemDirectoryService systemDirectoryService;

    //存储文档的桶名
    @Value("${s3.documentBucket}")
    private String documentBucket;
   
   /**
    * 
   * @Title: getDocumentByRequirement
   * @Description: IT全流程中获取需求或开发任务下的文档信息
   * @author author
   * @param requirementCode 需求编码
   * @param featureCode 工作任务编码
   * @return Map<String,Object> status=1正常返回，status=2异常返回
   *                            data：文档信息
    */
    @RequestMapping(value = "getDocumentByRequirement")
    public Map<String, Object> getDocumentByRequirement(String requirementCode,String featureCode) {
    	 Map<String, Object> map = new HashMap<String, Object>();
    	try {
    		List<TblSystemDirectoryDocument> list=itReAssetsLibraryRqService.getDocumentByRequirement(requirementCode,featureCode);
    		map.put("status", Constants.ITMP_RETURN_SUCCESS);
    		map.put("data", list);
    	} catch (Exception e) {
              logger.error(e.getMessage(), e);
              map.put("status", 2);
          }
    	return map;
    }
    /**
     * 
    * @Title: getRequirementId
    * @Description: 获取需求ID
    * @author author
    * @param requirementCode 需求编码
    * @return Long 需求ID
     */
    @RequestMapping("getRequirementId")
    public Long getRequirementId(String requirementCode) {
		return itReAssetsLibraryRqService.getRequirementId(requirementCode);
    	
    }
    
    /**
     * 
    * @Title: getTypeAndSystem
    * @Description: 获取文档类型
    * @author author
    * @param featureCode 工作任务编码（未用）
    * @return Map<String,Object> key:data  value :map  key :documentType value :List<TblSystemDirectoryDocument>文档类型信息
     */
    @RequestMapping("getTypeAndSystem")
    public Map<String, Object> getTypeAndSystem(String featureCode){
    	 Map<String, Object> map = new HashMap<String, Object>();
    	try {
            map.put("data", itReAssetsLibraryRqService.getTypeAndSystem(featureCode));
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            return super.handleException(e, "查询失败！");
        }
    	return map;    	
    }
    /**
     * 上传附件
     * @param file
     * @param tblSystemDirectoryDocument
     * @param request
     * @return
     */
    @RequestMapping(value = "uploadNewDocument", method = RequestMethod.POST)
    public AjaxModel uploadDocument(@RequestParam("file") MultipartFile[] file, TblSystemDirectoryDocument tblSystemDirectoryDocument,String currentUserAccount,String[] fileName, HttpServletRequest request) {
        try {
        	itReAssetsLibraryRqService.upItLoadNewDocument(file, tblSystemDirectoryDocument,currentUserAccount,fileName, request);
            return AjaxModel.SUCCESS();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return AjaxModel.FAIL(e);
        }
    }
    /**
     * 上传markDown文件
     * @param tblSystemDirectoryDocument 文档信息
     * @param request
     * @return
     */
    @RequestMapping(value = "upNewMarkDocument", method = RequestMethod.POST)
    public Map<String, Object> upNewMarkDocument( TblSystemDirectoryDocument tblSystemDirectoryDocument,String currentUserAccount, HttpServletRequest request) {
    	Map<String, Object> map=new HashMap<>();
    	try {
           Long documentId=itReAssetsLibraryRqService.upItNewMarkDocument(tblSystemDirectoryDocument,currentUserAccount, request);
           map.put("documentMarkId", documentId);
           map.put("status",  Constants.ITMP_RETURN_SUCCESS);
            
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            map.put("status", 2);
        }
    	return map;
    }
    /**
     * 覆盖上传
     * @param files
     * @param tblSystemDirectoryDocument 文档信息
     * @param request
     * @return
     */
    
    @RequestMapping(value = "coverUploadOldDocument", method = RequestMethod.POST)
    public AjaxModel coverUploadOldDocument(@RequestParam("file") MultipartFile[] files, TblSystemDirectoryDocument tblSystemDirectoryDocument,String currentUserAccount,String[] fileName, HttpServletRequest request) {
        try {
        	itReAssetsLibraryRqService.coverUploadOldDocument(files, tblSystemDirectoryDocument,currentUserAccount,  fileName,request);
            return AjaxModel.SUCCESS();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return AjaxModel.FAIL(e);
        }
    }
    
    
    /**
     * 查看本章节mark基本信息
     * @param systemDirectoryDocumentChapters
     * @param request
     * @return map  markdown  md文档信息（md格式的字符串）
	                buttonState 检出按钮显示状态1可用，2不可用
	                chapters   TblSystemDirectoryDocumentChapters章节信息
     */
     @PostMapping(value = "selectChaptersMarkDown")
     public Map<String,Object> selectChaptersMarkDown(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters,String currentUserAccount,HttpServletRequest request) throws  Exception{
         Map<String, Object> map = new HashMap<String, Object>();

         try {
             map = itReAssetsLibraryRqService.selectChaptersMarkDown(systemDirectoryDocumentChapters,currentUserAccount,request);
             map.put("status", Constants.ITMP_RETURN_SUCCESS);
             map.put("currentUserId", CommonUtil.getCurrentUserId(request));
         }catch (Exception e) {
             return super.handleException(e, "查询失败！");
         }
         return map;
     }
     /**
      * 查询章节所有附件
      * @param systemDirectoryDocumentChapters 封装的查询信息
      * @param request
      * @return
      */
     @PostMapping(value = "selectChaptersFiles")
     public Map<String,Object> selectChaptersFiles(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters,HttpServletRequest request){
         Map<String, Object> map = new HashMap<String, Object>();
         try {
             map.put("files", itReAssetsLibraryRqService.selectChaptersFiles(systemDirectoryDocumentChapters,request));
             map.put("status", Constants.ITMP_RETURN_SUCCESS);
         } catch (Exception e) {
             return super.handleException(e, "查询失败！");
         }
         return map;
     }
     /**
      *  查询章节的关联所有需求和开发任务
      * @param systemDirectoryDocumentChapters 封装的查询信息
      * @param request
      * @return
      */
     @PostMapping(value = "selectChaptersRelatedData")
     public Map<String,Object> selectChaptersRelatedData(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters,HttpServletRequest request){
         Map<String, Object> map = new HashMap<String, Object>();
         try {
             map = itReAssetsLibraryRqService.selectChaptersRelatedData(systemDirectoryDocumentChapters,request);
             map.put("status", Constants.ITMP_RETURN_SUCCESS);
         } catch (Exception e) {
             return super.handleException(e, "查询失败！");
         }
         return map;
     }
     
     /**
      * 
     * @Title: addOrUpdateDocChapters
     * @Description: 新增或更新章节信息
     * @author author
     * @param systemDirectoryDocumentChapters 章节信息
     * @param currentUserAccount 当前用户账号
     * @param request
     * @return Map<String,Object> key:sysDocChapters value :更新后的章节信息
      */
     @RequestMapping("addOrUpdateDocChapters")
     public Map<String,Object> addOrUpdateDocChapters(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters,String currentUserAccount,HttpServletRequest request){
     	 Map<String, Object> map = new HashMap<String, Object>();
     	 try {
     		 TblSystemDirectoryDocumentChapters sysDocChapters =  itReAssetsLibraryRqService.addOrUpdateDocChapters(systemDirectoryDocumentChapters, currentUserAccount,request);
     		 map.put("sysDocChapters", sysDocChapters);
     		 map.put("status", Constants.ITMP_RETURN_SUCCESS);
     	 }catch (Exception e) {
  			return super.handleException(e, "编辑或新增章节失败！");
  		}
      	 return map;
     }
     
     
     /**
      * 
     * @Title: delectDocumentFile
     * @Description: 移除文档
     * @author author
     * @param directoryDocumentId 文档ID
     * @param documentType 文档类型
     * @param currentUserAccount 当前账号
     * @param requirementCode 需求编码
     * @param featureCode 开发任务编码
     * @return AjaxModel
      */
     @RequestMapping(value = "delectDocumentFile", method = RequestMethod.POST)
     public  AjaxModel   delectDocumentFile(@RequestParam("id") Long directoryDocumentId,Long documentType,String currentUserAccount,String requirementCode,String featureCode) {
     	 try {
     		itReAssetsLibraryRqService.delectDocumentFile(directoryDocumentId,documentType,currentUserAccount, requirementCode, featureCode);
              return AjaxModel.SUCCESS();
          } catch (Exception e) {
              logger.error(e.getMessage(), e);
              return AjaxModel.FAIL(e);
          }
     }
     /**
      * 章节上传附件
      * @param files
      * @param directoryDocumentAttachment
      * @param request
      * @return
      * @throws Exception
      */
     @RequestMapping(value = "uploadChaptersFiles", method = RequestMethod.POST)
     public Map<String,Object> uploadChaptersFiles(@RequestParam("files") MultipartFile[] files, TblSystemDirectoryDocumentAttachment directoryDocumentAttachment,String featureCode,String requirementCode,String currentUserAccount, String[] fileName,HttpServletRequest request) throws Exception{
    	 Map<String, Object> map = new HashMap<String, Object>();
         try {
        	 Integer number = systemDirectoryService.uploadChaptersFiles(files, directoryDocumentAttachment,requirementCode,featureCode,currentUserAccount,fileName, request);
        	
        	 map.put("status",number);
             return map;
         }catch (Exception e) {
             super.handleException(e, "操作失败！");
             map.put("status", 0);
             return map;
         }
     }
     
     /**
      * 获取文档目录（章节）
      * @param systemDirectoryDocumentId
      * @return
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
      * @param request
      * @param tblSystemDirectory
      * @return cn.pioneeruniverse.common.entity.AjaxModel
      * @Description 新增文档目录
      * @MethodName addDirectory
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
      * @param projectId
      * @return java.util.List<cn.pioneeruniverse.project.entity.TblSystemDirectory>
      * @Description 获取文档库文档树目录
      * @MethodName getDirectoryTree
      * @author xukai [xukai@pioneerservice.cn]
      */
     @RequestMapping(value = "getDirectoryTree", method = RequestMethod.POST)
     public List<TblSystemDirectory> getDirectoryTree(@RequestParam("projectId") Long projectId) {
         return systemDirectoryService.getDirectoryTreeForDocumentLibrary(projectId);
     }
     
     /**
      * 移动文档目录（章节）
      * @author tingting
      * */
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
      * 删除章节
      * @author tingting
      * */
     @RequestMapping("deleteDocChapters")
     public Map<String,Object> deleteDocChapters(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters,String currentUserAccount,HttpServletRequest request){
     	 Map<String, Object> map = new HashMap<String, Object>();
     	 try {
     		 systemDirectoryService.deleteDocChapters(systemDirectoryDocumentChapters,currentUserAccount,request);
     		 map.put("status", Constants.ITMP_RETURN_SUCCESS);
     	 }catch (Exception e) {
   			return super.handleException(e, "删除章节失败！");
   		}
       	 return map;
     }
     /**
      * 移除章节附件
      * @param directoryDocumentAttachment
      * @param request
      * @return
      */
     @PostMapping(value = "removeChaptersFile")
     public Map<String,Object> removeChaptersFile(TblSystemDirectoryDocumentAttachment directoryDocumentAttachment,String currentUserAccount, HttpServletRequest request){
         Map<String, Object> map = new HashMap<String, Object>();
         map.put("status", Constants.ITMP_RETURN_SUCCESS);
         try {
             systemDirectoryService.removeChaptersFile(directoryDocumentAttachment,currentUserAccount,request);
         }catch (Exception e) {
             return super.handleException(e, "操作失败！");
         }
         return map;
     }
     /**
      * 查看章节markDown最新版本和上次版本对比
      * @param type
      * @param systemDirectoryDocumentChapters
      * @param request
      * @return map latestVersion 最新版本   lastVersion上个版本 
      *             status 1正常，2异常
      */
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

}
