package cn.pioneeruniverse.project.controller;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.S3Util;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocument;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentAttachment;
import cn.pioneeruniverse.project.service.assetsLibrary.IAssetsLibraryRqService;

/**
 *  资产库（需求视角）
 * @author weiji
 *
 */
@RestController
@RequestMapping("assetsLibraryRq")
public class AssetsLibraryRequireController extends BaseController{

	@Autowired
	private IAssetsLibraryRqService iAssetsLibraryRqService;
	@Autowired
	private S3Util s3Util;





	/**
	 *  部门视角目录
	 * @param  requireId 搜索需求id
	 * @param  systemId  搜索系统id
	 * @param  reqTaskId 搜索任务id
	 * @param  type       目录层级 1部门 2需求
	 * @param  znodeId    一层目录id
	 * @param
	 * @return Map<String, Object> key:znodes  value:List<AssetsZtree>树形的部门列表信息
	 *                                 status   1正常返回，2异常返回
	 *
	 */
	@RequestMapping(value = "getDeptdirectory", method = RequestMethod.POST)
	public Map<String, Object> getDeptdirectory(HttpServletRequest request,String requireId, String systemId,String reqTaskId, String type, String znodeId,String search) {
		    Map<String, Object> result = new HashMap<>();
		      if(type==null){
                type="1";
			  }
				long[] systemIds = null;
				long[] requireIds = null;
				long[] reqTaskIds = null;
				if (!systemId.equals("")) {
					systemIds = (long[]) ConvertUtils.convert(systemId.split(","), long.class);
				}
				if (!requireId.equals("")) {
					requireIds = (long[]) ConvertUtils.convert(requireId.split(","), long.class);
				}
				if (!reqTaskId.equals("")) {
					reqTaskIds = (long[]) ConvertUtils.convert(reqTaskId.split(","), long.class);
				}

				result.put("status", Constants.ITMP_RETURN_SUCCESS);
				try {
					result = iAssetsLibraryRqService.getDeptdirectory(result, requireIds, systemIds, reqTaskIds, type, znodeId,search);
				} catch (Exception e) {
					return this.handleException(e, "获取信息失败");
				}


			return  result;

	}





	/**
	 *  系统视角目录
	 * @param  requireId 搜索需求id
	 * @param  systemId  搜索系统id
	 * @param  reqTaskId 搜索任务id
	 * @param  type       目录层级 1部门 2需求
	 * @param  znodeId    一层目录id
	 * @return Map<String, Object> key:znodes  value:List<AssetsZtree>树形的系统列表信息
	 *                                 status   1正常返回，2异常返回
	 */
	@RequestMapping(value = "getSystemDirectory", method = RequestMethod.POST)
	public Map<String, Object> getSystemDirectory( HttpServletRequest request,String requireId,String systemId,String reqTaskId,String type,String znodeId,String search) {
        if(type==null){
            type="1";
        }
		long[] systemIds=null;
		long[] requireIds=null;
		long[]  reqTaskIds=null;
		if(!systemId.equals("")){
			systemIds=(long[]) ConvertUtils.convert(systemId.split(","),long.class);
		}
		if(!requireId.equals("")){
			requireIds=(long[]) ConvertUtils.convert(requireId.split(","),long.class);
		}
		if(!reqTaskId.equals("")){
			reqTaskIds = (long[]) ConvertUtils.convert(reqTaskId.split(","),long.class);
		}
		Map<String, Object> result = new HashMap<>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            result=iAssetsLibraryRqService.getSystemDirectory(result,requireIds,systemIds,reqTaskIds,type,znodeId,search);
        } catch (Exception e) {
            return this.handleException(e,"获取信息失败");
        }
        return  result;

	}








	/**
	 * 
	* @Title: downObject
	* @Description: 从S3中下载文档
	* @author author
	* @param tblSystemDirectoryDocument 封装的查询条件
	* @param request
	 */
	@RequestMapping(value = "downObject")
	public void downObject(TblSystemDirectoryDocument tblSystemDirectoryDocument,HttpServletRequest request, HttpServletResponse response) {

        try {

            if(!StringUtils.isBlank(tblSystemDirectoryDocument.getDocumentS3Bucket())&&!StringUtils.isBlank(tblSystemDirectoryDocument.getDocumentS3Key())&&!StringUtils.isBlank(tblSystemDirectoryDocument.getDocumentName())) {
                String documentS3Bucket=  URLDecoder.decode(request.getParameter("documentS3Bucket"), "UTF-8");
                String documentS3Key=  URLDecoder.decode(request.getParameter("documentS3Key"), "UTF-8");
                String documentName=  URLDecoder.decode(request.getParameter("documentName"), "UTF-8");
                s3Util.downObject(documentS3Bucket, documentS3Key, documentName, response);

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("mes:" + e.getMessage(), e);
        }



	}

    /**
     * 
    * @Title: getMarkDown
    * @Description: 下载markdown转string
    * @author author
    * @param tblSystemDirectoryDocument 封装的查询条件
    * @return String 返回的文档内容
    * @throws Exception 
     */
	public String getMarkDown(TblSystemDirectoryDocument tblSystemDirectoryDocument) throws Exception {
		return s3Util.getStringByS3(tblSystemDirectoryDocument.getDocumentS3Bucket().toString(), tblSystemDirectoryDocument.getDocumentS3Key());

	}




	/**
	 * 从bucket下载markdown转string
	 * @author weiji
	 * @param tblSystemDirectoryDocument 需求id
	 *  @return Map<String, Object> key:attachments  value:普通附件
	 *                                  markDown            markdown内容
	 */

	public Map<String,Object> getStringByS3(TblSystemDirectoryDocument tblSystemDirectoryDocument) throws Exception {
		Map<String,Object> result=new HashMap<>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            String markDown=s3Util.getStringByS3(tblSystemDirectoryDocument.getDocumentS3Bucket().toString(),tblSystemDirectoryDocument.getDocumentS3Key());
            result.put("markDown",markDown);
            //获取此文档附件
            result=iAssetsLibraryRqService.getAttachments(result,String.valueOf(tblSystemDirectoryDocument.getId()));
        } catch (Exception e) {
            return this.handleException(e,"获取信息失败");
        }

        return  result;
	}


//	/**
//	 * 获取需求说明书
//	 *
//	 * @param requireId
//	 */
//
//	public Map<String,Object> getRequiredir(String requireId,String pid) throws Exception {
//		Map<String,Object> result=new HashMap<>();
//        result.put("status", Constants.ITMP_RETURN_SUCCESS);
//        try {
//            result=iAssetsLibraryRqService.getRequiredir(result,requireId,pid);
//        } catch (Exception e) {
//            return this.handleException(e,"获取信息失败");
//        }
//        return  result;
//	}


    /**
     * 获取文档
     * @author weiji
     * @param requireId 需求id
	 * @param docType 文档类型
	 * @return Map<String, Object>
	 */
    @RequestMapping(value = "getDocuments", method = RequestMethod.POST)
	public  Map<String, Object> getDocuments(long requireId,String docType,String systemId) throws Exception {
        Map<String,Object> result=new HashMap<>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            result=iAssetsLibraryRqService.getDocuments(result,requireId,docType,systemId);
        } catch (Exception e) {
            return this.handleException(e,"获取信息失败");
        }
        return  result;
    }

	/**
	 * 查询关联信息
	 * @author weiji
	 * @param documetId 文档id
	 * @return Map<String, Object> key :reqZnodes  value:List<AssetsZtree>树形的文档关联信息
	 * 树形中的type = doc关联文档信息，system关联系统信息，feature关联开发任务信息，devtask关联开发工作任务信息，scm关联代码信息
	 */
	@RequestMapping(value = "getRelationInfo", method = RequestMethod.POST)
	public Map<String,Object> getRelationInfo(String documetId) throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            result = iAssetsLibraryRqService.getRelationInfo(result, documetId);
        } catch (Exception e) {
            return this.handleException(e, "获取信息失败");

        }

        return result;
    }

    /**
     * 查询关联信息
	 * @author weiji
     * @param chapterId 章节id
	 * @return Map<String, Object>
     */
    @RequestMapping(value = "getChapterIdRelation", method = RequestMethod.POST)
    public Map<String,Object> getChapterIdRelation(String chapterId) throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            result = iAssetsLibraryRqService.getChapterIdRelation(result, chapterId);
        } catch (Exception e) {
            return this.handleException(e, "获取信息失败");

        }

        return result;
    }
    /**
     * 查询历史
     * @author weiji
     * @param documetId 文档id
	 * @return Map<String, Object>
     */
	@RequestMapping(value = "getDocumentHistory", method = RequestMethod.POST)
    public Map<String,Object> getDocumentHistory(String documetId) throws Exception {
        Map<String,Object> result=new HashMap<>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            result=iAssetsLibraryRqService.getDocumentHistory(result,documetId);
        } catch (Exception e) {
            return this.handleException(e,"获取信息失败");
        }
        return  result;
    }


    /**
     * 查询附件
     * @author weiji
     * @param documetId 文档id
	 * @param chapterId 章节id
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "getAttachmentsByDocumetid", method = RequestMethod.POST)
    public Map<String,Object> getAttachmentsByDocumetid(String documetId,String chapterId) throws Exception {
        Map<String,Object> result=new HashMap<>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            result=iAssetsLibraryRqService.getAttachmentsByDocumetid(result,documetId,chapterId);
        } catch (Exception e) {
            return this.handleException(e,"获取信息失败");
        }
        return  result;
    }

	/**
	 * 获取附加根据章节id
	 * @author weiji
	 * @param chapterId 章节id
	 * @return Map<String, Object>
	 */
	public Map<String,Object> getAttachmentsByChapter(String chapterId) throws Exception {
		Map<String,Object> result=new HashMap<>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			result=iAssetsLibraryRqService.getAttachmentsByChapter(result,chapterId);
		} catch (Exception e) {
			return this.handleException(e,"获取信息失败");
		}
		return  result;
	}



	/**
	 * 附件下载
	 * @author weiji
	 * @param tblSystemDirectoryDocumentAttachment 文档附件
	 * @return Map<String, Object>
	 */
    @RequestMapping(value = "downAttachment", method = RequestMethod.POST)
	public void downAttachment(TblSystemDirectoryDocumentAttachment tblSystemDirectoryDocumentAttachment,HttpServletResponse response) throws Exception {
		s3Util.downObject(tblSystemDirectoryDocumentAttachment.getAttachmentS3Bucket(), tblSystemDirectoryDocumentAttachment.getAttachmentS3Key(),tblSystemDirectoryDocumentAttachment.getAttachmentNameOld().toString(),response);

	}
	/**
	 * 从bucket下载附件
	 *
	 */
	@RequestMapping(value = "downAtts")
	public void downAtts(String attachmentS3Bucket,String attachmentS3Key,String attachmentNameOld,HttpServletRequest request, HttpServletResponse response) {

		try {


			attachmentS3Bucket=  URLDecoder.decode(request.getParameter("attachmentS3Bucket"), "UTF-8");
			attachmentS3Key=  URLDecoder.decode(request.getParameter("attachmentS3Key"), "UTF-8");
			attachmentNameOld=  URLDecoder.decode(request.getParameter("attachmentNameOld"), "UTF-8");
			 s3Util.downObject(attachmentS3Bucket, attachmentS3Key, attachmentNameOld, response);


		} catch (Exception e) {
			e.printStackTrace();
			logger.error("mes:" + e.getMessage(), e);
		}



	}




	/**
	 * 查询章节关联 对于markdown的文档类型的时候，在详情中会有文档章节关联信息
	 * @author weiji
	 * @param chapterId 章节id
	 * @return Map<String, Object> Key ：list  value：List<AssetsZtree>文档章节信息
	 */
    @RequestMapping(value = "getChapterRelation")
    public Map<String,Object> getChapterRelation(String chapterId,HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> result=new HashMap<>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {

            result=iAssetsLibraryRqService.getChapterRelation(chapterId);


        } catch (Exception e) {
            e.printStackTrace();
            logger.error("mes:" + e.getMessage(), e);
        }

        return  result;

    }





}
