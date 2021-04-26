package cn.pioneeruniverse.project.service.assetsLibrary.impl;

import cn.pioneeruniverse.common.utils.S3Util;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.TblSystemDirectoryDocumentChaptersHistoryMapper;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersHistory;
import cn.pioneeruniverse.project.service.assetsLibrary.SystemDirectoryDocumentChaptersHistoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author:liushan
 * Date: 2020/1/14 上午 10:17
 */
@Service("SystemDirectoryDocumentChaptersHistoryService")
@Transactional(readOnly = true)
public class SystemDirectoryDocumentChaptersHistoryServiceImpl implements SystemDirectoryDocumentChaptersHistoryService {

    @Autowired
    private TblSystemDirectoryDocumentChaptersHistoryMapper chaptersHistoryMapper;
    @Autowired
    private S3Util s3Util;
    /**
    *@author liushan
    *@Description 获取章节的历史信息
    *@Date 2020/1/14
    *@Param [chaptersHistory]
    *@return List<TblSystemDirectoryDocumentChaptersHistory>章节历史信息
    **/
    @Override
    public List<TblSystemDirectoryDocumentChaptersHistory> getChaptersHistory(TblSystemDirectoryDocumentChaptersHistory chaptersHistory) {
        return chaptersHistoryMapper.getChaptersHistory(chaptersHistory);
    }

    /**
     *@author liushan
     *@Description 获取历史信息
     *@Date 2020/2/19
     *@Param 
     *@return  Map<String, Object> key---markdown:markdown的文件内容
     *                   chapters:章节历史信息
     **/
    @Override
    public Map<String, Object> getChaptersHistoryVersion(TblSystemDirectoryDocumentChaptersHistory chaptersHistory) {
        Map<String, Object> result = new HashMap<>();
        result.put("markdown","");
        TblSystemDirectoryDocumentChaptersHistory chaptersHistoryVersion = chaptersHistoryMapper.getChaptersHistoryVersion(chaptersHistory);
        if(StringUtils.isNotBlank(chaptersHistoryVersion.getChaptersS3Bucket()) && StringUtils.isNotBlank(chaptersHistoryVersion.getChaptersS3Key())) {
            //获取markdown文件内容
            result.put("markdown",s3Util.getStringByS3(chaptersHistoryVersion.getChaptersS3Bucket(),chaptersHistoryVersion.getChaptersS3Key()));
        }
        result.put("chapters",chaptersHistoryVersion);
        return result;
    }
}
