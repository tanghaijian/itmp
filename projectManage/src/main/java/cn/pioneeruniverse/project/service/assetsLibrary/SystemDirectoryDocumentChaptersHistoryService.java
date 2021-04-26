package cn.pioneeruniverse.project.service.assetsLibrary;

import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersHistory;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author:liushan
 * Date: 2020/1/14 上午 10:17
 */
public interface SystemDirectoryDocumentChaptersHistoryService {
    /**
    *@author liushan
    *@Description 获取章节的历史信息
    *@Date 2020/1/14
    *@Param [chaptersHistory]
    *@return java.util.List<cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersHistory>
    **/
    List<TblSystemDirectoryDocumentChaptersHistory> getChaptersHistory(TblSystemDirectoryDocumentChaptersHistory chaptersHistory);

    /**
     *@author liushan
     *@Description 获取历史信息
     *@Date 2020/2/18
     *@Param [chaptersHistory]
     *@return java.util.Map<java.lang.String,java.lang.Object>
     **/
    Map<String, Object> getChaptersHistoryVersion(TblSystemDirectoryDocumentChaptersHistory chaptersHistory);
}
