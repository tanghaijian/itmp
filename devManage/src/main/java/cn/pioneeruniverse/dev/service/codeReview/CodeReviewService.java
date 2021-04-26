package cn.pioneeruniverse.dev.service.codeReview;

import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.dev.entity.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: 代码评审管理
 * @Date: Created in 17:30 2019/3/15
 * @Modified By:
 */
public interface CodeReviewService {
    /**
     *@author author
     *@Description 获取列表数据
     *@Date 2020/7/31
     * @param tblDevTaskScm 接收查询条件
     *@return cn.pioneeruniverse.common.entity.JqGridPage<cn.pioneeruniverse.dev.entity.TblDevTaskScm>
     **/
    JqGridPage<TblDevTaskScm> getCodeReviewPage(JqGridPage<TblDevTaskScm> jqGridPage, TblDevTaskScm tblDevTaskScm, Long currentUserId) throws Exception;

    /**
     *@author author
     *@Description 工作任务代码id 查询提交的文件数据
     *@Date 2020/8/5
     * @param devTaskScmId
     *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTaskScmFile>
     **/
    List<TblDevTaskScmFile> getReviewFilesByDevTaskScmId(Long devTaskScmId);

    /**
    *@author author
    *@Description 根据工作任务代码id 查询git提交的文件
    *@Date 2020/8/5
     * @param devTaskScmId
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTaskScmGitFile>
    **/
    List<TblDevTaskScmGitFile> getReviewGitFilesByDevTaskScmId(Long devTaskScmId);
    /**
     *@author author
     *@Description git文件信息
     *@Date 2020/8/5
     * @param toolId 工具id
     * @param projectId 项目id
     * @param branchName 分支
     *@return java.util.Map<java.lang.String,java.lang.Object>
     **/
    Map<String, Object> getGitFileInfo(Long toolId, Long projectId, String branchName) throws URISyntaxException;

    /**
     *@author author
     *@Description 文件提交数量
     *@Date 2020/8/5
     * @param devTaskScmFileId 工作任务文件id
     * @param scmFileType 文件类型
     *@return cn.pioneeruniverse.common.entity.AjaxModel
     **/
    Integer getFileCommentsCountByFileId(Long devTaskScmFileId, Integer scmFileType);

    /**
    *@author author
    *@Description 根据工作任务查询工作任务信息
    *@Date 2020/8/5
     * @param devTaskId
    *@return cn.pioneeruniverse.dev.entity.TblDevTaskScm
    **/
    TblDevTaskScm getDevTaskDetailByDevTaskId(Long devTaskId);
    /**
     * @param tblDevTaskScmFile
     * @return java.lang.String
     * @Description 获取文件内容
     * @MethodName getFileContent
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/4/25 11:26
     */
    String getFileContent(TblDevTaskScmFile tblDevTaskScmFile);

    /**
    *@author author
    *@Description 获取git文件
    *@Date 2020/8/5
     * @param tblDevTaskScmGitFile
    *@return java.lang.String
    **/
    String getGitFileContent(TblDevTaskScmGitFile tblDevTaskScmGitFile) throws URISyntaxException;

    /**
    *@author author
    *@Description 获取文件对比数据
    *@Date 2020/8/5
     * @param tblDevTaskScmFile
    *@return java.util.Map<java.lang.String,java.util.Map<java.lang.String,java.lang.Object>>
    **/
    Map<String, Map<String, Object>> getCompareFileInfo(TblDevTaskScmFile tblDevTaskScmFile);

    /**
    *@author author
    *@Description 获取git文件对比数据
    *@Date 2020/8/5
     * @param tblDevTaskScmGitFile
    *@return java.util.Map<java.lang.String,java.util.Map<java.lang.String,java.lang.Object>>
    **/
    Map<String, Map<String, Object>> getCompareGitFileInfo(TblDevTaskScmGitFile tblDevTaskScmGitFile) throws URISyntaxException;

    /**
    *@author author
    *@Description 获取文件提交
    *@Date 2020/8/5
     * @param devTaskScmFileId
 * @param scmFileType
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTaskScmFileReview>
    **/
    List<TblDevTaskScmFileReview> getFileComments(Long devTaskScmFileId,Integer scmFileType);

    /**
    *@author author
    *@Description 修改文件评审结果
    *@Date 2020/8/5
     * @param request
 * @param tblDevTask
    *@return int
    **/
    int updateCodeReviewResult(HttpServletRequest request, TblDevTask tblDevTask);

    /**
    *@author author
    *@Description 发送代码评审提交数据
    *@Date 2020/8/5
     * @param tblDevTaskScmFileReview
 * @param request
    *@return void
    **/
    void sendCodeReviewComment(TblDevTaskScmFileReview tblDevTaskScmFileReview, HttpServletRequest request);

}
