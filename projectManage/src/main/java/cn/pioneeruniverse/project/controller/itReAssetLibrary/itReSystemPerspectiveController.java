package cn.pioneeruniverse.project.controller.itReAssetLibrary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.pioneeruniverse.common.entity.AjaxModel;
import cn.pioneeruniverse.project.service.itReAssetsLibrary.ItReSystemDirectoryService;


@RestController
@RequestMapping("itResystemPerspective")
public class itReSystemPerspectiveController {

    private static final Logger logger = LoggerFactory.getLogger(itReSystemPerspectiveController.class);

    @Autowired
    private ItReSystemDirectoryService systemDirectoryService;

   
    /**
     * 
    * @Title: getDocumentHistory
    * @Description: 获取文档历史
    * @author author
    * @param documentId 文档ID
    * @param requirementCode 需求ID
    * @param featureCode 开发任务ID
    * @return
    * @throws
     */
    @RequestMapping(value = "getDocumentHistory", method = RequestMethod.POST)
    public AjaxModel getDocumentHistory( Long documentId,String requirementCode,String featureCode) {
        try {
            return AjaxModel.SUCCESS(systemDirectoryService.getSystemDirectoryDocumentHistory(documentId,requirementCode,featureCode));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return AjaxModel.FAIL(e);
        }
    }
    
    
}
