package cn.pioneeruniverse.dev.controller;

import cn.pioneeruniverse.common.entity.AjaxModel;
import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.dev.entity.*;
import cn.pioneeruniverse.dev.service.codeReview.CodeReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: 代码评审管理
 * @Date: Created in 16:59 2019/3/15
 * @Modified By:
 */
@RestController
@RequestMapping(value = "codeReview")
public class CodeReviewController {

    private final static Logger logger = LoggerFactory.getLogger(CodeReviewController.class);

    /**
    * @author author
    * @Description 代码库管理接口
    * @Date 2020/9/18
    **/
    @Autowired
    private CodeReviewService codeReviewService;

    /**
    *@author author
    *@Description 获取列表数据
    *@Date 2020/7/31
    * @param request
    * @param response
    * @param tblDevTaskScm 接收查询条件
    *@return cn.pioneeruniverse.common.entity.JqGridPage<cn.pioneeruniverse.dev.entity.TblDevTaskScm>
    **/
    @RequestMapping(value = "getCodeReviewPage", method = RequestMethod.POST)
    public JqGridPage<TblDevTaskScm> getCodeReviewPage(HttpServletRequest request, HttpServletResponse response, TblDevTaskScm tblDevTaskScm) {
        JqGridPage<TblDevTaskScm> jqGridPage = null;
        try {
            jqGridPage = codeReviewService.getCodeReviewPage(new JqGridPage<>(request, response), tblDevTaskScm, CommonUtil.getCurrentUserId(request));
            return jqGridPage;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
    *@author author
    *@Description 工作任务代码id 查询提交的文件数据
    *@Date 2020/8/5
    * @param devTaskScmId 工作任务和代码关联id
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTaskScmFile>
    **/
    @RequestMapping(value = "getReviewFilesByDevTaskScmId", method = RequestMethod.POST)
    public List<TblDevTaskScmFile> getReviewFilesByDevTaskScmId(Long devTaskScmId) {
        return codeReviewService.getReviewFilesByDevTaskScmId(devTaskScmId);
    }

    /**
    *@author author
    *@Description 根据工作任务代码id 查询git提交的文件
    *@Date 2020/8/5
    * @param devTaskScmId 工作任务与代码关联id
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTaskScmGitFile>
    **/
    @RequestMapping(value = "getReviewGitFilesByDevTaskScmId", method = RequestMethod.POST)
    public List<TblDevTaskScmGitFile> getReviewGitFilesByDevTaskScmId(Long devTaskScmId) {
        return codeReviewService.getReviewGitFilesByDevTaskScmId(devTaskScmId);
    }

    /**
    *@author author
    *@Description git文件信息
    *@Date 2020/8/5
    * @param toolId 工具id
    * @param projectId 项目id
    * @param branchName 分支
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @RequestMapping(value = "getGitFileInfo", method = RequestMethod.POST)
    public Map<String, Object> getGitFileInfo(Long toolId, Long projectId, String branchName) {
        try {
            return codeReviewService.getGitFileInfo(toolId, projectId, branchName);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
    *@author author
    *@Description 文件提交数量
    *@Date 2020/8/5
    * @param devTaskScmFileId 工作任务文件id
    * @param scmFileType 文件类型
    *@return cn.pioneeruniverse.common.entity.AjaxModel
    **/
    @RequestMapping(value = "getFileCommentsCountByFileId", method = RequestMethod.POST)
    public AjaxModel getFileCommentsCountByFileId(Long devTaskScmFileId, Integer scmFileType) {
        try {
            return AjaxModel.SUCCESS(codeReviewService.getFileCommentsCountByFileId(devTaskScmFileId, scmFileType));
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
    *@author author
    *@Description 根据工作任务查询工作任务信息
    *@Date 2020/8/5
    * @param devTaskId
    *@return cn.pioneeruniverse.dev.entity.TblDevTaskScm
    **/
    @RequestMapping(value = "getDevTaskDetailByDevTaskId", method = RequestMethod.POST)
    public TblDevTaskScm getDevTaskDetailByDevTaskId(Long devTaskId) {
        return codeReviewService.getDevTaskDetailByDevTaskId(devTaskId);
    }

    /**
    *@author author
    *@Description 获取文件内容
    *@Date 2020/8/5
    * @param tblDevTaskScmFile
    *@return cn.pioneeruniverse.common.entity.AjaxModel
    **/
    @RequestMapping(value = "getFileContent", method = RequestMethod.POST)
    public AjaxModel getFileContent(TblDevTaskScmFile tblDevTaskScmFile) {
        try {
            return AjaxModel.SUCCESS(codeReviewService.getFileContent(tblDevTaskScmFile));
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
    *@author author
    *@Description 获取git文件
    *@Date 2020/8/5
    * @param tblDevTaskScmGitFile
    *@return cn.pioneeruniverse.common.entity.AjaxModel
    **/
    @RequestMapping(value = "getGitFileContent", method = RequestMethod.POST)
    public AjaxModel getGitFileContent(TblDevTaskScmGitFile tblDevTaskScmGitFile) {
        try {
            return AjaxModel.SUCCESS(codeReviewService.getGitFileContent(tblDevTaskScmGitFile));
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
    *@author author
    *@Description 获取git文件提交
    *@Date 2020/8/5
    * @param devTaskScmFileId 工作任务代码关联表id
    * @param scmFileType 1svn,2git
    *@return cn.pioneeruniverse.common.entity.AjaxModel
    **/
    @RequestMapping(value = "getFileComments", method = RequestMethod.POST)
    public AjaxModel getFileComments(Long devTaskScmFileId, Integer scmFileType) {
        try {
            return AjaxModel.SUCCESS(codeReviewService.getFileComments(devTaskScmFileId,scmFileType));
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
     * 
    * @Title: getCompareFileInfo
    * @Description: 获取比对文件
    * @author author
    * @param tblDevTaskScmFile 查询bean
    * @return AjaxModel
     */
    @RequestMapping(value = "getCompareFileInfo", method = RequestMethod.POST)
    public AjaxModel getCompareFileInfo(TblDevTaskScmFile tblDevTaskScmFile) {
        try {
            return AjaxModel.SUCCESS(codeReviewService.getCompareFileInfo(tblDevTaskScmFile));
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
    *@author author
    *@Description 比较 git文件
    *@Date 2020/8/21
    * @param tblDevTaskScmGitFile
    *@return cn.pioneeruniverse.common.entity.AjaxModel
    **/
    @RequestMapping(value = "getCompareGitFileInfo", method = RequestMethod.POST)
    public AjaxModel getCompareGitFileInfo(TblDevTaskScmGitFile tblDevTaskScmGitFile) {
        try {
            return AjaxModel.SUCCESS(codeReviewService.getCompareGitFileInfo(tblDevTaskScmGitFile));
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
    *@author author
    *@Description 修改文件评审结果
    *@Date 2020/8/21
    * @param request
    * @param tblDevTask 开发工作任务
    *@return cn.pioneeruniverse.common.entity.AjaxModel
    **/
    @RequestMapping(value = "updateCodeReviewResult", method = RequestMethod.POST)
    public AjaxModel updateCodeReviewResult(HttpServletRequest request, TblDevTask tblDevTask) {
        try {
            if (codeReviewService.updateCodeReviewResult(request, tblDevTask) != 1) {
                return AjaxModel.FAIL(new Exception("代码评审已通过，无法再次评审"));
            } else {
                return AjaxModel.SUCCESS("评审状态已被更新");
            }
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
    *@author author
    *@Description 发送代码评审提交数据
    *@Date 2020/8/21
    * @param tblDevTaskScmFileReview 代码评审结果
    * @param request
    *@return cn.pioneeruniverse.common.entity.AjaxModel
    **/
    @RequestMapping(value = "sendCodeReviewComment", method = RequestMethod.POST)
    public AjaxModel sendCodeReviewComment(TblDevTaskScmFileReview tblDevTaskScmFileReview, HttpServletRequest request) {
        try {
            codeReviewService.sendCodeReviewComment(tblDevTaskScmFileReview, request);
            return AjaxModel.SUCCESS("评审消息发送成功");
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return AjaxModel.FAIL(e);
        }
    }

}
