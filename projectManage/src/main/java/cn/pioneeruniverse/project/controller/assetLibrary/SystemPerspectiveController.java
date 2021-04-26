package cn.pioneeruniverse.project.controller.assetLibrary;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.pioneeruniverse.common.entity.AjaxModel;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentHistory;
import cn.pioneeruniverse.project.service.assetsLibrary.SystemDirectoryService;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: 系统视角
 * @Date: Created in 10:41 2019/11/12
 * @Modified By:
 */
@RestController
@RequestMapping("systemPerspective")
public class SystemPerspectiveController {

    private static final Logger logger = LoggerFactory.getLogger(SystemPerspectiveController.class);

    @Autowired
    private SystemDirectoryService systemDirectoryService;

    /**
     * @param systemIds 系统Id
     * @param request
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     *          id :SYSTEM-*表示系统ID，DOCUMENTTYPE-*表示系统文档的类型
                Pid：页面tree的nodeId
                name:如果ID表示系统，则系统名，如果文档类型，则文档类型名
                systemId:系统ID
                documentTypeId:文档类型ID
     * @Description 获取系统视角文档树 （未用）
     * @MethodName getSystemDocumentTree
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/12/26 10:02
     */
    @RequestMapping(value = "getDirectoryTree", method = RequestMethod.POST)
    public List<Map<String, Object>> getDirectoryTree(String systemIds, HttpServletRequest request) {
        return systemDirectoryService.getDirectoryTree(systemIds, request);
    }

    /**
     * @param systemId     关联系统Id
     * @param documentType 文档类型表主键
     * @return cn.pioneeruniverse.common.entity.AjaxModel
     *         map  key type                value:markdown或者documents：文档类型
     *                  documents                 文档信息
     *                  markDownDocumentId        markdown文档对应的id
                //markdown文档ID
                result.put("markDownDocumentId", markDownList.get(0).getId());
            } else {
            	//附件类型
                result.put("type", "documents");
                //文档
                result.put("documents", documents);
     * @Description 获取系统视角文档树某一目录下的文档
     * @MethodName getFilesUnderDirectory
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/12/26 10:11
     */
    @RequestMapping(value = "getFilesUnderDirectory", method = RequestMethod.POST)
    public AjaxModel getFilesUnderDirectory(@RequestParam("systemId") Long systemId, @RequestParam("documentType") Long documentType) {
        try {
            return AjaxModel.SUCCESS(systemDirectoryService.getFilesUnderDirectory(systemId, documentType));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
     * @param documentId 文档id
     * @return cn.pioneeruniverse.common.entity.AjaxModel obj:List<TblSystemDirectoryDocumentHistory>文档历史列表
     * @Description 获取文档历史
     * @MethodName getDocumentHistory
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/12/26 17:30
     */
    @RequestMapping(value = "getDocumentHistory", method = RequestMethod.POST)
    public AjaxModel getDocumentHistory(@RequestParam Long documentId) {
        try {
            return AjaxModel.SUCCESS(systemDirectoryService.getSystemDirectoryDocumentHistory(documentId));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return AjaxModel.FAIL(e);
        }
    }
    
    /**
     * 
    * @Title: getSomeDocumentType
    * @Description: 获取系统类文档类型
    * @author author
    * @return
    * @throws
     */
    @RequestMapping(value = "getSomeDocumentType", method = RequestMethod.POST)
    public AjaxModel getSomeDocumentType() {
        try {
            return AjaxModel.SUCCESS(systemDirectoryService.getSomeDocumentType());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return AjaxModel.FAIL(e);
        }
    }
}
