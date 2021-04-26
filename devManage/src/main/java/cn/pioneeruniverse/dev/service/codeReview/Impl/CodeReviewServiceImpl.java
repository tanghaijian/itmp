package cn.pioneeruniverse.dev.service.codeReview.Impl;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.common.gitlab.entity.Commit;
import cn.pioneeruniverse.common.gitlab.entity.Project;
import cn.pioneeruniverse.common.gitlab.service.GitLabWebApiService;
import cn.pioneeruniverse.common.subversion.SubversionUtils;
import cn.pioneeruniverse.common.utils.CollectionUtil;
import cn.pioneeruniverse.common.utils.DateUtil;
import cn.pioneeruniverse.dev.dao.mybatis.*;
import cn.pioneeruniverse.dev.entity.*;
import cn.pioneeruniverse.dev.feignInterface.DevManageToSystemInterface;
import cn.pioneeruniverse.dev.service.codeReview.CodeReviewService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tmatesoft.svn.core.wc.SVNInfo;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: 代码评审管理
 * @Date: Created in 17:30 2019/3/15
 * @Modified By:
 */
@Service("codeReviewService")
public class CodeReviewServiceImpl implements CodeReviewService {

    @Autowired
    private TblDevTaskScmMapper tblDevTaskScmMapper;

    @Autowired
    private TblDevTaskScmFileMapper tblDevTaskScmFileMapper;

    @Autowired
    private TblDevTaskScmFileReviewMapper tblDevTaskScmFileReviewMapper;

    @Autowired
    private TblDevTaskMapper tblDevTaskMapper;

    @Autowired
    private TblToolInfoMapper tblToolInfoMapper;

    @Autowired
    private TblDevTaskScmGitFileMapper tblDevTaskScmGitFileMapper;

    @Autowired
    private DevManageToSystemInterface devManageToSystemInterface;

    @Autowired
    private GitLabWebApiService gitLabWebApiService;

    /**
     *@author author
     *@Description 获取列表数据
     *@Date 2020/7/31
     * @param tblDevTaskScm 接收查询条件
     *@return cn.pioneeruniverse.common.entity.JqGridPage<cn.pioneeruniverse.dev.entity.TblDevTaskScm>
     **/
    @Override
    @Transactional(readOnly = true)
    public JqGridPage<TblDevTaskScm> getCodeReviewPage(JqGridPage<TblDevTaskScm> jqGridPage, TblDevTaskScm tblDevTaskScm, Long currentUserId) throws Exception {
        //获取当前登录人为工作任务的开发人或评审人的工作任务(该工作任务为:已被评审过的工作任务+需要评审但未评审的工作任务)
        jqGridPage.filtersAttrToEntityField(tblDevTaskScm);
        PageHelper.startPage(jqGridPage.getJqGridPrmNames().getPage(), jqGridPage.getJqGridPrmNames().getRows());
        List<TblDevTaskScm> list = tblDevTaskScmMapper.selectDevTaskScmPage(tblDevTaskScm, currentUserId);
        if (CollectionUtil.isNotEmpty(list)) {
            //获取开发人员中文名，获取代码评审人中文名
            for (TblDevTaskScm devTaskScm : list) {
                Map<String, String> result = devManageToSystemInterface.getDevUserNameAndReviewersNameForCodeReivew(devTaskScm.getDevUserId(), devTaskScm.getCodeReviewUserIds());
                if (result != null && !result.isEmpty()) {
                    devTaskScm.setDevUserName(result.get("devUserName"));
                    devTaskScm.setCodeReviewUserNames(result.get("codeReviewUserNames"));
                }
            }
        }
        PageInfo<TblDevTaskScm> pageInfo = new PageInfo<>(list);
        jqGridPage.processDataForResponse(pageInfo);
        return jqGridPage;
    }

    /**
    *@author author
    *@Description 工作任务代码id 查询提交的文件数据
    *@Date 2020/8/5
     * @param devTaskScmId
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTaskScmFile>
    **/
    @Override
    @Transactional(readOnly = true)
    public List<TblDevTaskScmFile> getReviewFilesByDevTaskScmId(Long devTaskScmId) {
        if (devTaskScmId != null) {
            return tblDevTaskScmFileMapper.getReviewFilesByDevTaskScmId(devTaskScmId);
        } else {
            return null;
        }
    }
    /**
     *@author author
     *@Description 根据工作任务代码id 查询git提交的文件
     *@Date 2020/8/5
     * @param devTaskScmId
     *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTaskScmGitFile>
     **/
    @Override
    @Transactional(readOnly = true)
    public List<TblDevTaskScmGitFile> getReviewGitFilesByDevTaskScmId(Long devTaskScmId) {
        if (devTaskScmId != null) {
            return tblDevTaskScmGitFileMapper.getReviewGitFilesByDevTaskScmId(devTaskScmId);
        } else {
            return null;
        }
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
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getGitFileInfo(Long toolId, Long projectId, String branchName) throws URISyntaxException {
        Map<String, Object> result = new HashMap<>();
        TblToolInfo tblToolInfo = tblToolInfoMapper.selectByPrimaryKey(toolId);
        if (tblToolInfo != null) {
            StringBuilder baseUri = new StringBuilder();
            baseUri.append(Constants.ACCESS_PROTOCOL_MAP.get(tblToolInfo.getAccessProtocol())).
                    append(tblToolInfo.getIp()).append(":").append(tblToolInfo.getPort());
            Project project = gitLabWebApiService.getProjectById(baseUri.toString(), Integer.valueOf(projectId.intValue()), tblToolInfo.getGitApiToken());
            if (project != null) {
                StringBuilder scmUrl = new StringBuilder(project.getWebUrl()).append("/blob/").append(branchName).append("/");
                result.put("scmUrl", scmUrl);
            }
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getFileCommentsCountByFileId(Long devTaskScmFileId, Integer scmFileType) {
        return tblDevTaskScmFileReviewMapper.getScmFileCommentsCountByFileId(devTaskScmFileId, scmFileType);
    }
    /**
     *@author author
     *@Description 根据工作任务查询工作任务信息
     *@Date 2020/8/5
     * @param devTaskId
     *@return cn.pioneeruniverse.dev.entity.TblDevTaskScm
     **/
    @Override
    @Transactional(readOnly = true)
    public TblDevTaskScm getDevTaskDetailByDevTaskId(Long devTaskId) {
        return tblDevTaskScmMapper.getDevTaskDetailByDevTaskId(devTaskId);
    }

    /**
     * @param tblDevTaskScmFile
     * @return java.lang.String
     * @Description 获取文件内容
     * @MethodName getFileContent
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/4/25 11:26
     */
    @Override
    @Transactional(readOnly = true)
    public String getFileContent(TblDevTaskScmFile tblDevTaskScmFile) {
        //通过文件url获取协议，ip，端口
        URI uri = URI.create(tblDevTaskScmFile.getScmUrl());
        String scheme = uri.getScheme();
        String host = uri.getHost();
        Integer port = uri.getPort();
        if (StringUtils.isNotEmpty(scheme) && StringUtils.isNotEmpty(host) && port != null) {
            Map<String, String> svnSuperAdminMap = tblToolInfoMapper.getSvnToolSuperAdmin(scheme.toLowerCase(), host, String.valueOf(port));
            if (svnSuperAdminMap != null && !svnSuperAdminMap.isEmpty()) {
                return SubversionUtils.checkoutFileToString(tblDevTaskScmFile.getScmUrl(), "/" + tblDevTaskScmFile.getCommitFile(), Long.valueOf(tblDevTaskScmFile.getCommitNumber()), tblDevTaskScmFile.getFileContentCharset(), svnSuperAdminMap.get("superAdminAccount"), svnSuperAdminMap.get("superAdminPassword"));
            }
        }
        return null;
    }

    /**
    *@author author
    *@Description 获取git文件
    *@Date 2020/8/5
     * @param tblDevTaskScmGitFile
    *@return java.lang.String
    **/
    @Override
    @Transactional(readOnly = true)
    public String getGitFileContent(TblDevTaskScmGitFile tblDevTaskScmGitFile) throws URISyntaxException {
        TblToolInfo tblToolInfo = tblToolInfoMapper.selectByPrimaryKey(tblDevTaskScmGitFile.getToolId());
        if (tblToolInfo != null) {
            StringBuilder baseUri = new StringBuilder();
            baseUri.append(Constants.ACCESS_PROTOCOL_MAP.get(tblToolInfo.getAccessProtocol())).
                    append(tblToolInfo.getIp()).append(":").append(tblToolInfo.getPort());
            String fileContent = gitLabWebApiService.getFileContentFromRepository(baseUri.toString(), Integer.valueOf(tblDevTaskScmGitFile.getGitRepositoryId().intValue()),
                    tblDevTaskScmGitFile.getCommitFile(), tblDevTaskScmGitFile.getCommitNumber(), tblToolInfo.getGitApiToken());
            if (StringUtils.isNotEmpty(tblDevTaskScmGitFile.getFileContentCharset()) && StringUtils.isNotEmpty(fileContent)) {
                fileContent = new String(fileContent.getBytes(), Charset.forName(tblDevTaskScmGitFile.getFileContentCharset()));
            }
            return fileContent;
        }
        return null;
    }

    /**
     * @param tblDevTaskScmFile
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @Description 获取对比文件信息
     * @MethodName getCompareFileContents
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/4/25 11:55
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Map<String, Object>> getCompareFileInfo(TblDevTaskScmFile tblDevTaskScmFile) {
        URI uri = URI.create(tblDevTaskScmFile.getScmUrl());
        String scheme = uri.getScheme();
        String host = uri.getHost();
        Integer port = uri.getPort();
        // 文件版本对比 左边文件
        Map<String, Object> leftFile = new HashMap<String, Object>() {{
            put("revision", tblDevTaskScmFile.getLastCommitNumber());
        }};
        // 文件版本对比 右边边文件
        Map<String, Object> rightFile = new HashMap<String, Object>() {{
            put("revision", tblDevTaskScmFile.getCommitNumber());
        }};
        if (StringUtils.isNotEmpty(scheme) && StringUtils.isNotEmpty(host) && port != null) {
            Map<String, String> svnSuperAdminMap = tblToolInfoMapper.getSvnToolSuperAdmin(scheme.toLowerCase(), host, String.valueOf(port));
            if (svnSuperAdminMap != null && !svnSuperAdminMap.isEmpty()) {
                leftFile.put("content", SubversionUtils.checkoutFileToString(tblDevTaskScmFile.getScmUrl(),
                        "/" + tblDevTaskScmFile.getCommitFile(),
                        Long.valueOf(tblDevTaskScmFile.getLastCommitNumber()),
                        tblDevTaskScmFile.getFileContentCharset(),
                        svnSuperAdminMap.get("superAdminAccount"),
                        svnSuperAdminMap.get("superAdminPassword")));
                SVNInfo leftFileSvnInfo = SubversionUtils.getSvnInfo(tblDevTaskScmFile.getScmUrl(),
                        Long.valueOf(tblDevTaskScmFile.getLastCommitNumber()),
                        svnSuperAdminMap.get("superAdminAccount"),
                        svnSuperAdminMap.get("superAdminPassword"));
                if (leftFileSvnInfo != null) {
                    leftFile.put("author", leftFileSvnInfo.getAuthor());//TODO svn用户名未转变为真实姓名
                    leftFile.put("commitDate", DateUtil.formatDate(leftFileSvnInfo.getCommittedDate(), DateUtil.fullFormat));
                }
                //文件内容
                rightFile.put("content", SubversionUtils.checkoutFileToString(tblDevTaskScmFile.getScmUrl(),
                        "/" + tblDevTaskScmFile.getCommitFile(),
                        Long.valueOf(tblDevTaskScmFile.getCommitNumber()),
                        tblDevTaskScmFile.getFileContentCharset(),
                        svnSuperAdminMap.get("superAdminAccount"),
                        svnSuperAdminMap.get("superAdminPassword")));
                SVNInfo rightFileSvnInfo = SubversionUtils.getSvnInfo(tblDevTaskScmFile.getScmUrl(),
                        Long.valueOf(tblDevTaskScmFile.getCommitNumber()),
                        svnSuperAdminMap.get("superAdminAccount"),
                        svnSuperAdminMap.get("superAdminPassword"));
                if (rightFileSvnInfo != null) {
                    rightFile.put("author", rightFileSvnInfo.getAuthor());
                    rightFile.put("commitDate", DateUtil.formatDate(rightFileSvnInfo.getCommittedDate(), DateUtil.fullFormat));
                }
            }
        }
        return new HashMap<String, Map<String, Object>>() {{
            put("leftFile", leftFile);
            put("rightFile", rightFile);
        }};
    }
    /**
     *@author author
     *@Description 获取git文件对比数据
     *@Date 2020/8/5
     * @param tblDevTaskScmGitFile
     *@return java.util.Map<java.lang.String,java.util.Map<java.lang.String,java.lang.Object>>
     *      put("leftFile", leftFile); 左边文件数据
     *      put("rightFile", rightFile); 右边文件数据
     **/
    @Override
    @Transactional(readOnly = true)
    public Map<String, Map<String, Object>> getCompareGitFileInfo(TblDevTaskScmGitFile tblDevTaskScmGitFile) throws URISyntaxException {
        Map<String, Object> leftFile = new HashMap<String, Object>() {{
            put("revision", tblDevTaskScmGitFile.getLastCommitNumber().substring(0, 8));
        }};
        Map<String, Object> rightFile = new HashMap<String, Object>() {{
            put("revision", tblDevTaskScmGitFile.getCommitNumber().substring(0, 8));
        }};
        TblToolInfo tblToolInfo = tblToolInfoMapper.selectByPrimaryKey(tblDevTaskScmGitFile.getToolId());
        if (tblToolInfo != null) {
            StringBuilder baseUri = new StringBuilder();
            baseUri.append(Constants.ACCESS_PROTOCOL_MAP.get(tblToolInfo.getAccessProtocol())).
                    append(tblToolInfo.getIp()).append(":").append(tblToolInfo.getPort());
            String leftContent = gitLabWebApiService.getFileContentFromRepository(baseUri.toString(), Integer.valueOf(tblDevTaskScmGitFile.getGitRepositoryId().intValue()),
                    StringUtils.isNotEmpty(tblDevTaskScmGitFile.getBeforeRenameFile()) ? tblDevTaskScmGitFile.getBeforeRenameFile() : tblDevTaskScmGitFile.getCommitFile(),
                    tblDevTaskScmGitFile.getLastCommitNumber(), tblToolInfo.getGitApiToken());
            if (StringUtils.isNotEmpty(tblDevTaskScmGitFile.getFileContentCharset()) && StringUtils.isNotEmpty(leftContent)) {
                leftContent = new String(leftContent.getBytes(), Charset.forName(tblDevTaskScmGitFile.getFileContentCharset()));
            }
            leftFile.put("content", leftContent);
            String rightContent = gitLabWebApiService.getFileContentFromRepository(baseUri.toString(), Integer.valueOf(tblDevTaskScmGitFile.getGitRepositoryId().intValue()), tblDevTaskScmGitFile.getCommitFile(),
                    tblDevTaskScmGitFile.getCommitNumber(), tblToolInfo.getGitApiToken());
            if (StringUtils.isNotEmpty(tblDevTaskScmGitFile.getFileContentCharset()) && StringUtils.isNotEmpty(rightContent)) {
                rightContent = new String(rightContent.getBytes(), Charset.forName(tblDevTaskScmGitFile.getFileContentCharset()));
            }
            rightFile.put("content", rightContent);
            Commit leftCommit = gitLabWebApiService.getCommitByCommitId(baseUri.toString(), Integer.valueOf(tblDevTaskScmGitFile.getGitRepositoryId().intValue()),
                    tblDevTaskScmGitFile.getLastCommitNumber(), tblToolInfo.getGitApiToken());
            if (leftCommit != null) {
                leftFile.put("author", leftCommit.getCommitterName());
                leftFile.put("commitDate", DateUtil.formatDate(leftCommit.getCommittedDate(), DateUtil.fullFormat));
            }
            Commit rightCommit = gitLabWebApiService.getCommitByCommitId(baseUri.toString(), Integer.valueOf(tblDevTaskScmGitFile.getGitRepositoryId().intValue()),
                    tblDevTaskScmGitFile.getCommitNumber(), tblToolInfo.getGitApiToken());
            if (rightCommit != null) {
                rightFile.put("author", rightCommit.getCommitterName());
                rightFile.put("commitDate", DateUtil.formatDate(rightCommit.getCommittedDate(), DateUtil.fullFormat));
            }
        }
        return new HashMap<String, Map<String, Object>>() {{
            put("leftFile", leftFile);
            put("rightFile", rightFile);
        }};
    }
    /**
     *@author author
     *@Description 获取文件提交
     *@Date 2020/8/5
     * @param devTaskScmFileId
     * @param scmFileType
     *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTaskScmFileReview>
     **/
    @Override
    @Transactional(readOnly = true)
    public List<TblDevTaskScmFileReview> getFileComments(Long devTaskScmFileId, Integer scmFileType) {
        return tblDevTaskScmFileReviewMapper.getFileComments(devTaskScmFileId, scmFileType);
    }
    /**
     *@author author
     *@Description 修改文件评审结果
     *@Date 2020/8/5
     * @param request
     * @param tblDevTask
     *@return int
     **/
    @Override
    @Transactional(readOnly = false)
    public int updateCodeReviewResult(HttpServletRequest request, TblDevTask tblDevTask) {
        tblDevTask.preInsertOrUpdate(request);
        return tblDevTaskMapper.updateCodeReviewStatus(tblDevTask);
    }
    /**
     *@author author
     *@Description 发送代码评审提交数据
     *@Date 2020/8/5
     * @param tblDevTaskScmFileReview
     * @param request
     **/
    @Override
    @Transactional(readOnly = false)
    public void sendCodeReviewComment(TblDevTaskScmFileReview tblDevTaskScmFileReview, HttpServletRequest request) {
        tblDevTaskScmFileReview.preInsertOrUpdate(request);
        tblDevTaskScmFileReview.setReviewUserId(tblDevTaskScmFileReview.getCreateBy());
        tblDevTaskScmFileReviewMapper.insertOneDevTaskScmFileReview(tblDevTaskScmFileReview);
    }

}
