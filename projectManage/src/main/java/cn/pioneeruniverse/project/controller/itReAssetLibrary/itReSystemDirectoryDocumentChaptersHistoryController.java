package cn.pioneeruniverse.project.controller.itReAssetLibrary;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersHistory;
import cn.pioneeruniverse.project.service.itReAssetsLibrary.ItReSystemDirectoryDocumentChaptersHistoryService;

/**
 * Description: 章节历史
 */
@RestController
@RequestMapping("/assetLibrary/itReDirectoryChaptersHistory/")
public class itReSystemDirectoryDocumentChaptersHistoryController extends BaseController {

    @Autowired
    private ItReSystemDirectoryDocumentChaptersHistoryService systemDirectoryDocumentChaptersHistoryService;

    /**
    *@Description 获取章节的历史信息
    *@Date 2020/1/14
    *@Param [chaptersHistory] 封装的查询信息
    *@return java.util.Map<java.lang.String,java.lang.Object> key:data  value:List<TblSystemDirectoryDocumentChaptersHistory>章节历史信息
    **/
    @PostMapping(value="getChaptersHistory")
    public Map<String, Object> getChaptersHistory(TblSystemDirectoryDocumentChaptersHistory chaptersHistory){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            result.put("data",systemDirectoryDocumentChaptersHistoryService.getChaptersHistory(chaptersHistory));
        } catch (Exception e) {
            return this.handleException(e, "查询失败！");
        }
        return result;
    }

    /**
     *@Description 获取历史信息
     *@Date 2020/2/18
     *@Param [chaptersHistory]
     *@return java.util.Map<java.lang.String,java.lang.Object> Key:chapters value:历史章节信息
     *                                                             markdown value:markdown文件内容
     **/
    @PostMapping(value="getChaptersHistoryVersion")
    public Map<String, Object> getChaptersHistoryVersion(TblSystemDirectoryDocumentChaptersHistory chaptersHistory){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result = systemDirectoryDocumentChaptersHistoryService.getChaptersHistoryVersion(chaptersHistory);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            return this.handleException(e, "查询失败！");
        }
        return result;
    }
}
