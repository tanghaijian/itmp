package cn.pioneeruniverse.project.controller.assetLibrary;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersHistory;
import cn.pioneeruniverse.project.service.assetsLibrary.SystemDirectoryDocumentChaptersHistoryService;

/**
 * Description: 章节历史
 * Author:liushan
 * Date: 2020/1/14 上午 10:16
 */
@RestController
@RequestMapping("/assetLibrary/directoryChaptersHistory/")
public class SystemDirectoryDocumentChaptersHistoryController extends BaseController {

    @Autowired
    private SystemDirectoryDocumentChaptersHistoryService systemDirectoryDocumentChaptersHistoryService;

    /**
    *@author liushan
    *@Description 获取章节的历史信息
    *@Date 2020/1/14
    *@Param [chaptersHistory]
    *@return map key  status =1 正常，2异常
    *                 data 章节历史信息
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
     *@author liushan
     *@Description 获取历史信息
     *@Date 2020/2/18
     *@Param [chaptersHistory]
     *@return map key status =1 正常，2异常
     *                markdown 文件内容
     *                chapters 章节历史
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
