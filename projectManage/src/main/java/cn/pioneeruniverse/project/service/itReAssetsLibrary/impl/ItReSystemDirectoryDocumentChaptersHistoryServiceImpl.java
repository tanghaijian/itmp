package cn.pioneeruniverse.project.service.itReAssetsLibrary.impl;

import cn.pioneeruniverse.common.utils.S3Util;
import cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary.ReTblSystemDirectoryDocumentChaptersHistoryMapper;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersHistory;
import cn.pioneeruniverse.project.service.itReAssetsLibrary.ItReSystemDirectoryDocumentChaptersHistoryService;

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
@Service("ItReSystemDirectoryDocumentChaptersHistoryService")
@Transactional(readOnly = true)
public class ItReSystemDirectoryDocumentChaptersHistoryServiceImpl implements ItReSystemDirectoryDocumentChaptersHistoryService {

    @Autowired
    private ReTblSystemDirectoryDocumentChaptersHistoryMapper chaptersHistoryMapper;
    @Autowired
    private S3Util s3Util;
    /**
    *@Description 获取章节的历史信息
    *@Date 2020/1/14
    *@Param [chaptersHistory]
    *@return java.util.List<cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersHistory>
    **/
    @Override
    public List<TblSystemDirectoryDocumentChaptersHistory> getChaptersHistory(TblSystemDirectoryDocumentChaptersHistory chaptersHistory) {
        return chaptersHistoryMapper.getChaptersHistory(chaptersHistory);
    }

    /**
     *@Description 获取历史信息
     *@Date 2020/2/19
     *@Param [chaptersHistory]
     *@return java.util.Map<java.lang.String,java.lang.Object>
     **/
    @Override
    public Map<String, Object> getChaptersHistoryVersion(TblSystemDirectoryDocumentChaptersHistory chaptersHistory) {
        Map<String, Object> result = new HashMap<>();
        result.put("markdown","");
        TblSystemDirectoryDocumentChaptersHistory chaptersHistoryVersion = chaptersHistoryMapper.getChaptersHistoryVersion(chaptersHistory);
        if(StringUtils.isNotBlank(chaptersHistoryVersion.getChaptersS3Bucket()) && StringUtils.isNotBlank(chaptersHistoryVersion.getChaptersTempS3Key())) {
            //取MONGO存储KEY(存放临时数据)
            result.put("markdown",s3Util.getStringByS3(chaptersHistoryVersion.getChaptersS3Bucket(),chaptersHistoryVersion.getChaptersTempS3Key()));
        }else {
            result.put("markdown",s3Util.getStringByS3(chaptersHistoryVersion.getChaptersS3Bucket(),chaptersHistoryVersion.getChaptersS3Key()));
        }
        result.put("chapters",chaptersHistoryVersion);
        return result;
    }
}
