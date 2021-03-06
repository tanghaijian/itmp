package cn.pioneeruniverse.dev.service.version.Impl;

import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.security.auth.login.CredentialException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.StringUtil;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.dto.ResultDataDTO;
import cn.pioneeruniverse.common.dto.TblUserInfoDTO;
import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.common.gitlab.entity.Commit;
import cn.pioneeruniverse.common.gitlab.entity.Member;
import cn.pioneeruniverse.common.gitlab.entity.Project;
import cn.pioneeruniverse.common.gitlab.entity.User;
import cn.pioneeruniverse.common.gitlab.entity.UserDTO;
import cn.pioneeruniverse.common.gitlab.service.GitLabWebApiService;
import cn.pioneeruniverse.common.subversion.SubversionUtils;
import cn.pioneeruniverse.common.utils.CollectionUtil;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.JsonUtil;
import cn.pioneeruniverse.common.utils.PasswordUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.common.utils.SpringContextHolder;
import cn.pioneeruniverse.dev.dao.mybatis.TblDevTaskMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDevTaskScmFileMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDevTaskScmGitFileMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDevTaskScmHistoryMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDevTaskScmMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementFeatureMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemScmRepositoryMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemScmSubmitMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemScmSubmitRegexMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemVersionMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblToolInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblUserInfoMapper;
import cn.pioneeruniverse.dev.entity.GitLabPostReceiveHookParam;
import cn.pioneeruniverse.dev.entity.GitLabPreReceiveHookParam;
import cn.pioneeruniverse.dev.entity.SvnFileDirectoryStructure;
import cn.pioneeruniverse.dev.entity.SvnPostCommitWebHookParam;
import cn.pioneeruniverse.dev.entity.SvnPreCommitWebHookParam;
import cn.pioneeruniverse.dev.entity.TblDevTaskScm;
import cn.pioneeruniverse.dev.entity.TblDevTaskScmFile;
import cn.pioneeruniverse.dev.entity.TblDevTaskScmGitFile;
import cn.pioneeruniverse.dev.entity.TblDevTaskScmHistory;
import cn.pioneeruniverse.dev.entity.TblSystemScmRepository;
import cn.pioneeruniverse.dev.entity.TblSystemScmSubmit;
import cn.pioneeruniverse.dev.entity.TblSystemScmSubmitRegex;
import cn.pioneeruniverse.dev.entity.TblSystemVersion;
import cn.pioneeruniverse.dev.entity.TblToolInfo;
import cn.pioneeruniverse.dev.entity.TblUserInfo;
import cn.pioneeruniverse.dev.feignInterface.DevManageToSystemInterface;
import cn.pioneeruniverse.dev.service.devtask.DevTaskService;
import cn.pioneeruniverse.dev.service.version.VersionService;

/**
 * 
* @ClassName: VersionServiceImpl
* @Description: svn???git??????????????????
* @author author
* @date 2020???8???21??? ??????9:29:05
*
 */
@Service("versionService")
public class VersionServiceImpl implements VersionService {

    private final static Logger logger = LoggerFactory.getLogger(VersionServiceImpl.class);


    @Autowired
    private DevManageToSystemInterface devManageToSystemInterface;

    @Autowired
    private TblToolInfoMapper tblToolInfoMapper;

    @Autowired
    private TblRequirementFeatureMapper tblRequirementFeatureMapper;

    @Autowired
    private TblDevTaskMapper tblDevTaskMapper;

    @Autowired
    private TblDevTaskScmMapper tblDevTaskScmMapper;

    @Autowired
    private TblDevTaskScmHistoryMapper tblDevTaskScmHistoryMapper;

    @Autowired
    private TblSystemInfoMapper tblSystemInfoMapper;

    @Autowired
    private DevTaskService devTaskService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private TblDevTaskScmFileMapper tblDevTaskScmFileMapper;

    @Autowired
    private TblDevTaskScmGitFileMapper tblDevTaskScmGitFileMapper;

    @Autowired
    private TblSystemScmRepositoryMapper tblSystemScmRepositoryMapper;

    @Autowired
    private TblSystemVersionMapper systemVersionMapper;

    @Autowired
    private TblSystemScmSubmitMapper tblSystemScmSubmitMapper;

    @Autowired
    private TblSystemScmSubmitRegexMapper tblSystemScmSubmitRegexMapper;

    @Autowired
    private GitLabWebApiService gitLabWebApiService;
    
    @Autowired
    private  TblUserInfoMapper tblUserInfoMapper;

    //??????????????????
    private static final int THREAD_SIZE = 30;

    //??????SVN??????????????????
    @Value("${execute.init.svn.del.files.timeout}")
    private int exceuteInitSvnDelFilesTimeout;

    //??????SVN??????????????????
    @Value("${execute.init.svn.changed.files.timeout}")
    private int executeInitSvnChangedFilesTimeout;

    //726?????????????????????
    @Value("${db.script.file.path.for.726}")
    private String dbScriptFilePathFor726;

    /**
     * @param jqGridPage
     * @param ip
     * @param repositoryName
     * @param path
     * @return cn.pioneeruniverse.common.entity.JqGridPage<cn.pioneeruniverse.common.dto.TblUserInfoDTO>
     * @Description ??????svn??????????????????
     * @MethodName getSvnUserAuth
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/27 14:09
     */
    @Override
    public JqGridPage<TblUserInfoDTO> getSvnUserAuth(JqGridPage<TblUserInfoDTO> jqGridPage, String ip, String repositoryName, String path) throws Exception {
        Map<String, String> svnUserAuth = SubversionUtils.getUserAuth(ip, repositoryName, path);
        List<TblUserInfoDTO> userList = new LinkedList<>();
        if (svnUserAuth != null && !svnUserAuth.isEmpty()) {
        	String userScmAccounts = "";
        	for (Map.Entry<String, String> entry : svnUserAuth.entrySet()) {
        		userScmAccounts += "," + entry.getKey();
        	}
        	//???SVN??????????????????????????????????????????????????????????????????????????????
        	userScmAccounts = userScmAccounts.substring(1);
        	userList = devManageToSystemInterface.getCodeBaseUserDetailByUserScmAccountList(userScmAccounts);
        	if (CollectionUtil.isNotEmpty(userList)) {
        		for (Map.Entry<String, String> entry : svnUserAuth.entrySet()) {
        			for (TblUserInfoDTO user : userList) {
        				if (entry.getKey().equals(user.getUserScmAccount())) {
        					user.setSvnAuthority(entry.getValue());
        					break;
        				}
        			}
        		}
        	}
        }
        jqGridPage.setPage(1);
        jqGridPage.setTotal(1);
        jqGridPage.setRecords((long) userList.size());
        jqGridPage.setCells(userList);
        return jqGridPage;
    }

    /**
     * 
    * @Title: getGitLabUserAuth
    * @Description: ??????GITLAB?????????????????????
    * @author author
    * @param jqGridPage
    * @param project git??????
    * @return JqGridPage<TblUserInfoDTO>
    * @throws Exception
     */
    @Override
    public JqGridPage<TblUserInfoDTO> getGitLabUserAuth(JqGridPage<TblUserInfoDTO> jqGridPage, Project project) throws Exception {
    	//??????gitlab???????????????????????????????????????????????????????????????????????????git???????????????????????????????????????
        List<Member> members = gitLabWebApiService.getProjectMembers(project.getBaseUri(), project.getId(), project.getGitApiToken());
        List<TblUserInfoDTO> userList = new LinkedList<>();
        if (CollectionUtil.isNotEmpty(members)) {
        	String userScmAccounts = CollectionUtil.extractToString(members, "username", ",");
        	userList = devManageToSystemInterface.getCodeBaseUserDetailByUserScmAccountList(userScmAccounts);
        	if (CollectionUtil.isNotEmpty(userList)) {
        		for (Member member : members) {
        			for (TblUserInfoDTO user : userList) {
        				if (member.getUsername().equals(user.getUserScmAccount())) {
        					user.setGitLabAccessLevel(member.getAccessLevel());
        					break;
        				}
        			}
        		}
        	}
        }
        jqGridPage.setPage(1);
        jqGridPage.setTotal(1);
        jqGridPage.setRecords((long) userList.size());
        jqGridPage.setCells(userList);
        return jqGridPage;
    }

    /**
     * @param ip
     * @param repositoryName
     * @param path
     * @param modifyOperates
     * @return String
     * @Description ??????svn????????????
     * @MethodName saveSvnModify
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/27 14:12
     */
    @Override
    public String saveSvnModify(Integer accessProtocol, String ip, String repositoryName, String path, String modifyOperates) throws Exception {
        return SubversionUtils.modifyConfigFile(accessProtocol, ip, repositoryName, path, modifyOperates);
    }

    /**
     * @param systemId
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     * @Description ????????????????????????svn????????????
     * @MethodName getMySvnRepositoryLocationInfo
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/27 14:13
     */
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMySvnRepositoryLocationInfo(Long systemId) {
        return tblToolInfoMapper.getMySvnRepositoryLocationInfo(systemId);
    }

    /**
     * @param systemId
     * @return java.util.List<cn.pioneeruniverse.dev.entity.SvnFileDirectoryStructure>
     * @Description ????????????????????????svn????????????
     * @MethodName getSvnFileTree
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/27 14:14
     */
    @Override
    public List<SvnFileDirectoryStructure> getSvnFileTree(Long systemId) {
        List<SvnFileDirectoryStructure> list = new LinkedList<>();
        List<Map<String, Object>> reposLocationList = SpringContextHolder.getBean(VersionService.class).getMySvnRepositoryLocationInfo(systemId);
        //?????????????????????
       /* List<Map<String, Object>> reposLocationList = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<String, Object>() {{
            put("userName", "admin");
            put("password", "123456");
            put("repositoryName", "repos1");
            put("ip", "192.168.1.145");
            put("port", "8099");
            put("accessProtocol", 1);
        }};
        reposLocationList.add(map1);*/
        if (CollectionUtil.isNotEmpty(reposLocationList)) {
            //????????????????????????
            List<FutureTask<SvnFileDirectoryStructure>> taskList = new ArrayList<>();
            for (Map<String, Object> map : reposLocationList) {
                Callable callable = new getSvnFileDirectoryStructureCallable(map);
                FutureTask<SvnFileDirectoryStructure> task = new FutureTask<SvnFileDirectoryStructure>(callable);
                taskList.add(task);
                Thread thread = new Thread(task);
                thread.start();
            }
            for (FutureTask<SvnFileDirectoryStructure> task : taskList) {
                SvnFileDirectoryStructure structure = null;
                try {
                    structure = task.get(30, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    task.cancel(true);
                    logger.error(String.valueOf(e.getMessage()), e);
                } catch (ExecutionException e) {
                    task.cancel(true);
                    logger.error(String.valueOf(e.getMessage()), e);
                } catch (TimeoutException e) {
                    task.cancel(true);
                    logger.error(String.valueOf(e.getMessage()), e);
                } finally {
                    if (structure != null) {
                        list.add(structure);
                    }
                }
            }
        }
        return list;
    }


    /**
     * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Description: ??????svn???????????????????????????????????????
     * @Date: Created in 18:05 2018/12/13
     * @Modified By:
     */
    private class getSvnFileDirectoryStructureCallable implements Callable<SvnFileDirectoryStructure> {

        private Map<String, Object> map;

        getSvnFileDirectoryStructureCallable(Map<String, Object> map) {
            this.map = map;
        }

        @Override
        public SvnFileDirectoryStructure call() throws Exception {
            String username = (String) map.get("userName");
            String password = (String) map.get("password");
            String repositoryName = (String) map.get("repositoryName");
            String ip = (String) map.get("ip");
            String port = (String) map.get("port");
            String context = (String) map.get("context");
            Integer accessProtocol = (Integer) map.get("accessProtocol");
            StringBuilder svnUrl = new StringBuilder();
            svnUrl.append(Constants.ACCESS_PROTOCOL_MAP.get(accessProtocol)).append(ip).append(":").append(port);
            if (StringUtils.isNotEmpty(context)) {
                if (context.startsWith("/")) {
                    svnUrl.append(context);
                } else {
                    svnUrl.append("/").append(context);
                }
            }
            if (!svnUrl.toString().endsWith("/")) {
                svnUrl.append("/");
            }
            svnUrl.append(repositoryName);
            SvnFileDirectoryStructure structure = SubversionUtils.getSvnDirEntry(svnUrl.toString(), username, password, accessProtocol, ip, port, "/", repositoryName, null);
            return structure;
        }
    }

    /**
     * @param gitLabPreReceiveHookParam
     * @return java.lang.String
     * @Description ??????gitLab???pre-receive???????????????
     * @MethodName handleGitLabPreReceiveHookRequest
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/7/17 18:09
     */
    @Override
    public String handleGitLabPreReceiveHookRequest(GitLabPreReceiveHookParam gitLabPreReceiveHookParam) {
        try {
            SpringContextHolder.getBean(VersionService.class).doBeforeGitLabPreReceiveHookCheck(gitLabPreReceiveHookParam);
            ResultDataDTO resultDataDTO = SpringContextHolder.getBean(VersionService.class).gitLabPreReceiveHookCheckMainProcess(gitLabPreReceiveHookParam);
            if (StringUtils.equals(resultDataDTO.getResCode(), ResultDataDTO.DEFAULT_SUCCESS_CODE)) {
                //???????????????id??????redis(hashkey=??????id_commitId)
                if (gitLabPreReceiveHookParam.getRelatedSystemScmSubmit() != null && gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getSystemId() != null) {
                    redisUtils.setForHash("gitLabRelatedSystemId", String.valueOf(gitLabPreReceiveHookParam.getProjectId()) +
                            "_" + gitLabPreReceiveHookParam.getNowCommitId(), gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getSystemId());
                }
                //?????????????????????id??????redis(hashkey=??????id_commitId)
                if (gitLabPreReceiveHookParam.getRelatedWorkTask() != null && gitLabPreReceiveHookParam.getRelatedWorkTask().getId() != null) {
                    redisUtils.setForHash("gitLabRelatedTaskId", String.valueOf(gitLabPreReceiveHookParam.getProjectId()) +
                            "_" + gitLabPreReceiveHookParam.getNowCommitId(), gitLabPreReceiveHookParam.getRelatedWorkTask().getId());
                }
                return "????????????";
            } else {
                return resultDataDTO.getResDesc();
            }
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            redisUtils.delForHash("gitLabRelatedSystemId", String.valueOf(gitLabPreReceiveHookParam.getProjectId()) +
                    "_" + gitLabPreReceiveHookParam.getNowCommitId());
            redisUtils.delForHash("gitLabRelatedTaskId", String.valueOf(gitLabPreReceiveHookParam.getProjectId()) +
                    "_" + gitLabPreReceiveHookParam.getNowCommitId());
            return "???????????????????????????????????????,???????????????????????????";
        }
    }

    /**
     * 
    * @Title: handleSvnPreCommitWebHookRequest
    * @Description: SVN?????????????????????
    * @author author
    * @param svnPreCommitWebHookParam
    * @return String ??????????????????
     */
    @Override
    public String handleSvnPreCommitWebHookRequest(SvnPreCommitWebHookParam svnPreCommitWebHookParam) {
        try {
            SpringContextHolder.getBean(VersionService.class).doBeforeSvnPreCommitHookCheck(svnPreCommitWebHookParam);
            String json = JSON.toJSONString(svnPreCommitWebHookParam);
        	logger.info(json);
            ResultDataDTO resultDataDTO = SpringContextHolder.getBean(VersionService.class).svnPreCommitHookCheckMainProcess(svnPreCommitWebHookParam);
            if (StringUtils.equals(resultDataDTO.getResCode(), ResultDataDTO.DEFAULT_SUCCESS_CODE)) {
                //??????????????????????????????????????????????????????????????????????????????????????????
                if (svnPreCommitWebHookParam.getRelatedSystemScmSubmit() == null) {
                	logger.info("11111111111");
                    return "????????????";
                }
                // //?????????????????????????????????????????????????????????message????????????????????????????????????????????????????????????
                if (CollectionUtil.isNotEmpty(svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSubmitSuperUserNamesCollection()) &&
                        svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSubmitSuperUserNamesCollection().contains(svnPreCommitWebHookParam.getAuthor())) {
                    if (svnPreCommitWebHookParam.getRelatedWorkTask() != null) {
                        svnPreCommitWebHookParam.setCheckDBScriptFileFor726(false);
                        svnPreCommitWebHookParam.setCheckSystemScmSubmitRegexes(false);
                        resultDataDTO = SpringContextHolder.getBean(VersionService.class).svnPreCommitHookCheck(this.getInitChangedFilesFutureTask(svnPreCommitWebHookParam), svnPreCommitWebHookParam);
                        if (resultDataDTO != null) {
                            if (StringUtils.equals(resultDataDTO.getResCode(), ResultDataDTO.DEFAULT_SUCCESS_CODE)) {
                                doAfterSvnPreCommitHookCheckSuccess(svnPreCommitWebHookParam, (List<Map<String, String>>) resultDataDTO.getData());
                            } else {
                                return resultDataDTO.getResDesc();
                            }
                        }
                    }
                    logger.info("222222222");
                    return "????????????";
                }
                svnPreCommitWebHookParam.setCheckSystemScmSubmitRegexes(true);
                //???????????????
                svnPreCommitWebHookParam.setRelatedSystemScmSubmitRegexes(SpringContextHolder.getBean(VersionService.class).getSystemScmSubmitRegexesForCodeCommit(new TblSystemScmSubmitRegex(
                        svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSystemId(),
                        svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSystemScmRepositoryId(),
                        svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getScmRepositoryName()
                )));
                resultDataDTO = SpringContextHolder.getBean(VersionService.class).svnPreCommitHookCheck(this.getInitChangedFilesFutureTask(svnPreCommitWebHookParam), svnPreCommitWebHookParam);
                if (resultDataDTO != null) {
                    if (StringUtils.equals(resultDataDTO.getResCode(), ResultDataDTO.DEFAULT_SUCCESS_CODE)) {
                        doAfterSvnPreCommitHookCheckSuccess(svnPreCommitWebHookParam, (List<Map<String, String>>) resultDataDTO.getData());
                        logger.info("33333333333");
                        return "????????????";
                    } else {
                        return resultDataDTO.getResDesc();
                    }
                } else {
                	logger.info("44444444444");
                    return "????????????";
                }
            } else {
                return resultDataDTO.getResDesc();
            }
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            redisUtils.delForHash("svnRelatedTaskId", svnPreCommitWebHookParam.getAuthor() + "_" + svnPreCommitWebHookParam.getTransaction());
            redisUtils.delForHash("svnCommmitFile", svnPreCommitWebHookParam.getAuthor() + "_" + svnPreCommitWebHookParam.getTransaction());
            return "???????????????????????????????????????,???????????????????????????";
        } finally {
            svnPreCommitWebHookParam.setInitDelFilesEnd(Boolean.TRUE);
            svnPreCommitWebHookParam.setInitChangedFilesEnd(Boolean.TRUE);
        }
    }

    /**
     * @param svnPreCommitWebHookParam
     * @return cn.pioneeruniverse.common.dto.ResultDataDTO
     * @Description svn pre-commit?????????????????????
     * @MethodName svnPreCommitHookCheckMainProcess
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/5/6 10:03
     */
    @Override
    @Transactional(readOnly = true)
    public ResultDataDTO svnPreCommitHookCheckMainProcess(SvnPreCommitWebHookParam svnPreCommitWebHookParam) {
        try {
            //TODO ?????????????????????????????????
            for (String changedDir : svnPreCommitWebHookParam.getChangedDirs()) {
                String scmUrl = StringUtils.equals(changedDir, "/") ? svnPreCommitWebHookParam.getReposUrl() : (svnPreCommitWebHookParam.getReposUrl() + changedDir);
                svnPreCommitWebHookParam.setRelatedSystemScmSubmit(tblSystemScmSubmitMapper.getSystemScmSubmitByScmUrl(scmUrl));
                if (svnPreCommitWebHookParam.getRelatedSystemScmSubmit() != null) {
                	String json = JSON.toJSONString(tblSystemScmSubmitMapper.getSystemScmSubmitByScmUrl(scmUrl));
                	logger.info("changedDir" + json);
                    break;
                }
            }
            logger.info("AAAAAAAAAAAAAAA");
            //1.????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            if (svnPreCommitWebHookParam.getRelatedSystemScmSubmit() != null) {
                //2.?????????????????????????????????????????????????????????????????????????????????????????????????????????
                if (CollectionUtil.isNotEmpty(svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSubmitSuperUserNamesCollection()) && svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSubmitSuperUserNamesCollection().contains(svnPreCommitWebHookParam.getAuthor())) {
                    //??????????????????????????????????????????????????????????????????????????????
                    if (CollectionUtil.isNotEmpty(svnPreCommitWebHookParam.getMessage())) {
                        String svnCommitMessage = svnPreCommitWebHookParam.getMessage().get(0).trim();
                        String regex = "(\\[)\\S{1,}(\\]).*?";//commit message???????????????
                        if (StringUtils.isNotEmpty(svnCommitMessage) && svnCommitMessage.matches(regex)) {
                            String workTaskCode = svnCommitMessage.split("\\]", 2)[0].substring(1);
                            svnPreCommitWebHookParam.setRelatedWorkTask(tblDevTaskMapper.selectByDevTaskCode(workTaskCode));
                        }
                    }
                    return ResultDataDTO.SUCCESS("??????????????????");
                }
                logger.info("BBBBBBBBBB");
                //3.????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                if (svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSubmitStatus() != null && svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSubmitStatus() == 1) {
                    //4.????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    if (CollectionUtil.isNotEmpty(svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSubmitUserNamesCollection())) {
                        if (!svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSubmitUserNamesCollection().contains(svnPreCommitWebHookParam.getAuthor())) {
                            return ResultDataDTO.FAILURE("?????????????????????????????????????????????????????????????????????????????????");
                        }
                    }
                    String workTaskCode;
                    //5.??????svn??????????????????????????????????????????
                    if (CollectionUtil.isNotEmpty(svnPreCommitWebHookParam.getMessage())) {
                        String svnCommitMessage = svnPreCommitWebHookParam.getMessage().get(0).trim();
                        if (StringUtils.isEmpty(svnCommitMessage)) {
                            return ResultDataDTO.FAILURE("??????????????????????????????????????????(????????????:[??????????????????]XXX)???????????????????????????????????????????????????????????????????????????");
                        }
                        String regex = "(\\[)\\S{1,}(\\]).*?";//commit message???????????????
                        if (!svnCommitMessage.matches(regex)) {
                            return ResultDataDTO.FAILURE("??????????????????????????????????????????(????????????:[??????????????????]XXX)???????????????????????????????????????????????????????????????????????????");
                        }
                        workTaskCode = svnCommitMessage.split("\\]", 2)[0].substring(1);
                    } else {
                        return ResultDataDTO.FAILURE("?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
                    }
                    svnPreCommitWebHookParam.setRelatedWorkTask(tblDevTaskMapper.selectByDevTaskCode(workTaskCode));
                    //6.??????svn????????????????????????????????????????????????????????????
                    if (svnPreCommitWebHookParam.getRelatedWorkTask() == null) {
                        return ResultDataDTO.FAILURE("?????????????????????????????????????????????");
                    }
                    //7.????????????????????????????????????????????????????????????????????????
                    if (!StringUtils.equals(svnPreCommitWebHookParam.getRelatedWorkTask().getUserScmAccount(), svnPreCommitWebHookParam.getAuthor())) {
                        return ResultDataDTO.FAILURE("?????????????????????????????????????????????????????????????????????????????????");
                    }
                    //8.?????????????????????????????????????????????????????????
                    if (svnPreCommitWebHookParam.getRelatedWorkTask().getDevTaskStatus() == null || !svnPreCommitWebHookParam.getRelatedWorkTask().getDevTaskStatus().equals(2)) {
                        return ResultDataDTO.FAILURE("???????????????????????????????????????????????????" + CommonUtil.getDictValueName("TBL_DEV_TASK_DEV_TASK_STATUS", String.valueOf(svnPreCommitWebHookParam.getRelatedWorkTask().getDevTaskStatus()), null) + ",??????????????????????????????????????????????????????");
                    }
                    svnPreCommitWebHookParam.setRelatedDevTask(tblRequirementFeatureMapper.getRequirementFeatureByDevTaskCode(workTaskCode));
                    //9.?????????????????????????????????????????????????????????
                    if (svnPreCommitWebHookParam.getRelatedDevTask() == null) {
                        return ResultDataDTO.FAILURE("???????????????????????????????????????????????????????????????????????????");
                    }
                    //10.???????????????????????????????????????????????????????????????????????????????????????????????????(????????????????????????????????????????????????????????????)
                   /* if (svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSystemVersionId() != null && !CommonUtil.LongCompare(svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSystemVersionId(), svnPreCommitWebHookParam.getRelatedDevTask().getSystemVersionId())) {
                        String branchVersionName = StringUtils.isEmpty(svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSystemVersionName()) ? "" : svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSystemVersionName();
                        String devTaskVersionName = StringUtils.isEmpty(svnPreCommitWebHookParam.getRelatedDevTask().getSystemVersionName()) ? "" : svnPreCommitWebHookParam.getRelatedDevTask().getSystemVersionName();
                        if(!branchVersionName.equals(devTaskVersionName)) {
                            return ResultDataDTO.FAILURE("??????????????????????????????(" + branchVersionName + ")????????????????????????(" + devTaskVersionName + ")??????????????????????????????");
                        }
                    }*/
                  //10.???????????????????????????????????????????????????????????????????????????????????????????????????(????????????????????????????????????????????????????????????)
                     if (svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSystemVersionId() != null ) {
                         String branchVersionName = StringUtils.isEmpty(svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSystemVersionName()) ? "" : svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSystemVersionName();
                         String devTaskVersionName = StringUtils.isEmpty(svnPreCommitWebHookParam.getRelatedDevTask().getSystemVersionName()) ? "" : svnPreCommitWebHookParam.getRelatedDevTask().getSystemVersionName();
                         if(!branchVersionName.equals(devTaskVersionName)) {
                             return ResultDataDTO.FAILURE("??????????????????????????????(" + branchVersionName + ")????????????????????????(" + devTaskVersionName + ")??????????????????????????????");
                         }
                     }
                    
                    //11.?????????????????????????????????????????????????????????????????????????????????????????????()?????????????????????????????????????????????????????????)
                    if (svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getCommissioningWindowId() != null && !CommonUtil.LongCompare(svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getCommissioningWindowId(), svnPreCommitWebHookParam.getRelatedDevTask().getCommissioningWindowId())) {
                        String branchCommissionWindowName = StringUtils.isEmpty(svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getCommissioningWindowName()) ? "" : svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getCommissioningWindowName();
                        String devTaskCommissionWindowName = StringUtils.isEmpty(svnPreCommitWebHookParam.getRelatedDevTask().getCommissioningWindowName()) ? "" : svnPreCommitWebHookParam.getRelatedDevTask().getCommissioningWindowName();
                        return ResultDataDTO.FAILURE("?????????????????????????????????(" + branchCommissionWindowName + ")???????????????????????????(" + devTaskCommissionWindowName + ")??????????????????????????????");
                    }
                } else {
                    return ResultDataDTO.FAILURE("????????????????????????????????????????????????????????????????????????");
                }
            }
            logger.info("CCCCCCCCCCCCCCC");
            return ResultDataDTO.SUCCESS("??????????????????");
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return ResultDataDTO.ABNORMAL("???????????????????????????????????????,???????????????????????????");
        }
    }

    /**
     * 
    * @Title: gitLabPreReceiveHookCheckMainProcess
    * @Description: git????????????????????????
    * @author author
    * @param gitLabPreReceiveHookParam
    * @return
    * @throws
     */
    @Override
    @Transactional(readOnly = true)
    public ResultDataDTO gitLabPreReceiveHookCheckMainProcess(GitLabPreReceiveHookParam gitLabPreReceiveHookParam) {
        try {
            if (!StringUtils.equals(gitLabPreReceiveHookParam.getOldCommitId(), Constants.GitLab.NO_HOOK_INTERCEPT_COMMIT_ID) &&
                    !StringUtils.equals(gitLabPreReceiveHookParam.getNowCommitId(), Constants.GitLab.NO_HOOK_INTERCEPT_COMMIT_ID)) {
                gitLabPreReceiveHookParam.setRelatedSystemScmSubmit(tblSystemScmSubmitMapper.getGitLabSystemScmSubmit(gitLabPreReceiveHookParam.getRelatedToolInfo(),
                        Long.valueOf(gitLabPreReceiveHookParam.getProjectId()), gitLabPreReceiveHookParam.getRefName()));
                //1.????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                if (gitLabPreReceiveHookParam.getRelatedSystemScmSubmit() != null) {
                    //2.?????????????????????????????????????????????????????????????????????????????????????????????????????????
                    if (CollectionUtil.isNotEmpty(gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getSubmitSuperUserNamesCollection()) &&
                            gitLabPreReceiveHookParam.getCommitUser() != null &&
                            gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getSubmitSuperUserNamesCollection().contains(gitLabPreReceiveHookParam.getCommitUser().getUsername())) {
                        if (StringUtils.isNotEmpty(gitLabPreReceiveHookParam.getCommitMessage())) {
                            String commitTaskMessage = gitLabPreReceiveHookParam.getCommitMessage().trim();
                            String regex = "(\\[)\\S{1,}(\\]).*?";//commit message???????????????
                            if (StringUtils.isNotEmpty(commitTaskMessage) && commitTaskMessage.matches(regex)) {
                                String workTaskCode = commitTaskMessage.split("\\]", 2)[0].substring(1);
                                gitLabPreReceiveHookParam.setRelatedWorkTask(tblDevTaskMapper.selectByDevTaskCode(workTaskCode));
                            }
                        }
                        return ResultDataDTO.SUCCESS("??????????????????");
                    }
                    //3.????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    if (gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getSubmitStatus() != null && gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getSubmitStatus() == 1) {
                        //4.????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                        if (CollectionUtil.isNotEmpty(gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getSubmitUserNamesCollection())) {
                            if (gitLabPreReceiveHookParam.getCommitUser() == null ||
                                    !gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getSubmitUserNamesCollection().contains(gitLabPreReceiveHookParam.getCommitUser().getUsername())) {
                                return ResultDataDTO.FAILURE("?????????????????????????????????????????????????????????????????????????????????");
                            }
                        }
                        String workTaskCode;
                        //5.??????gitlab??????????????????????????????????????????
                        if (StringUtils.isNotEmpty(gitLabPreReceiveHookParam.getCommitMessage())) {
                            String commitTaskMessage = gitLabPreReceiveHookParam.getCommitMessage().trim();
                            if (StringUtils.isEmpty(commitTaskMessage)) {
                                return ResultDataDTO.FAILURE("??????????????????????????????????????????(????????????:[??????????????????]XXX)???????????????????????????????????????????????????????????????????????????");
                            }
                            String regex = "(\\[)\\S{1,}(\\]).*?";//commit message???????????????
                            if (!commitTaskMessage.matches(regex)) {
                                return ResultDataDTO.FAILURE("??????????????????????????????????????????(????????????:[??????????????????]XXX)???????????????????????????????????????????????????????????????????????????");
                            }
                            workTaskCode = commitTaskMessage.split("\\]", 2)[0].substring(1);
                        } else {
                            return ResultDataDTO.FAILURE("?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
                        }
                        gitLabPreReceiveHookParam.setRelatedWorkTask(tblDevTaskMapper.selectByDevTaskCode(workTaskCode));
                        //6.??????gitlab????????????????????????????????????????????????????????????
                        if (gitLabPreReceiveHookParam.getRelatedWorkTask() == null) {
                            return ResultDataDTO.FAILURE("?????????????????????????????????????????????");
                        }
                        //7.????????????????????????????????????????????????????????????????????????
                        if (gitLabPreReceiveHookParam.getCommitUser() == null ||
                                !StringUtils.equals(gitLabPreReceiveHookParam.getRelatedWorkTask().getUserScmAccount(), gitLabPreReceiveHookParam.getCommitUser().getUsername())) {
                            return ResultDataDTO.FAILURE("?????????????????????????????????????????????????????????????????????????????????");
                        }
                        //8.?????????????????????????????????????????????????????????
                        if (gitLabPreReceiveHookParam.getRelatedWorkTask().getDevTaskStatus() == null || !gitLabPreReceiveHookParam.getRelatedWorkTask().getDevTaskStatus().equals(2)) {
                            return ResultDataDTO.FAILURE("???????????????????????????????????????????????????" + CommonUtil.getDictValueName("TBL_DEV_TASK_DEV_TASK_STATUS", String.valueOf(gitLabPreReceiveHookParam.getRelatedWorkTask().getDevTaskStatus()), null) + ",??????????????????????????????????????????????????????");
                        }
                        gitLabPreReceiveHookParam.setRelatedDevTask(tblRequirementFeatureMapper.getRequirementFeatureByDevTaskCode(workTaskCode));
                        //9.?????????????????????????????????????????????????????????
                        if (gitLabPreReceiveHookParam.getRelatedDevTask() == null) {
                            return ResultDataDTO.FAILURE("???????????????????????????????????????????????????????????????????????????");
                        }
                        //10.???????????????????????????????????????????????????????????????????????????????????????????????????(????????????????????????????????????????????????????????????)
                        if (gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getSystemVersionId() != null && !CommonUtil.LongCompare(gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getSystemVersionId(), gitLabPreReceiveHookParam.getRelatedDevTask().getSystemVersionId())) {
                            String branchVersionName = StringUtils.isEmpty(gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getSystemVersionName()) ? "" : gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getSystemVersionName();
                            String devTaskVersionName = StringUtils.isEmpty(gitLabPreReceiveHookParam.getRelatedDevTask().getSystemVersionName()) ? "" : gitLabPreReceiveHookParam.getRelatedDevTask().getSystemVersionName();
                            return ResultDataDTO.FAILURE("??????????????????????????????(" + branchVersionName + ")????????????????????????(" + devTaskVersionName + ")??????????????????????????????");
                        }
                        //11.?????????????????????????????????????????????????????????????????????????????????????????????()?????????????????????????????????????????????????????????)
                        if (gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getCommissioningWindowId() != null && !CommonUtil.LongCompare(gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getCommissioningWindowId(), gitLabPreReceiveHookParam.getRelatedDevTask().getCommissioningWindowId())) {
                            String branchCommissionWindowName = StringUtils.isEmpty(gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getCommissioningWindowName()) ? "" : gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getCommissioningWindowName();
                            String devTaskCommissionWindowName = StringUtils.isEmpty(gitLabPreReceiveHookParam.getRelatedDevTask().getCommissioningWindowName()) ? "" : gitLabPreReceiveHookParam.getRelatedDevTask().getCommissioningWindowName();
                            return ResultDataDTO.FAILURE("?????????????????????????????????(" + branchCommissionWindowName + ")???????????????????????????(" + devTaskCommissionWindowName + ")??????????????????????????????");
                        }
                    } else {
                        return ResultDataDTO.FAILURE("????????????????????????????????????????????????????????????????????????");
                    }
                }
            }
            return ResultDataDTO.SUCCESS("??????????????????");
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return ResultDataDTO.ABNORMAL("???????????????????????????????????????,???????????????????????????");
        }
    }

    /**
     * 
    * @Title: svnPreCommitHookCheck
    * @Description: ??????????????????
    * @author author
    * @param futureList
    * @param svnPreCommitWebHookParam
    * @return
    * @throws
     */
    @Override
    public ResultDataDTO svnPreCommitHookCheck(List<Future<ResultDataDTO>> futureList, SvnPreCommitWebHookParam svnPreCommitWebHookParam) {
        int executeWaiteTime = 0;
        ResultDataDTO resultDataDTO = null;
        List<Map<String, String>> changedFilesCollection = new LinkedList<>();
        for (Future<ResultDataDTO> future : futureList) {
        	//?????????????????????????????????
            while (!future.isDone() && executeWaiteTime <= executeInitSvnChangedFilesTimeout) {
                try {
                    Thread.currentThread().sleep(1000);
                    executeWaiteTime++;
                } catch (InterruptedException e) {
                    logger.error(String.valueOf(e.getMessage()), e);
                }
            }
            if (executeWaiteTime > executeInitSvnChangedFilesTimeout) {
                svnPreCommitWebHookParam.setInitDelFilesEnd(Boolean.TRUE);
                svnPreCommitWebHookParam.setInitChangedFilesEnd(Boolean.TRUE);
            }
            try {
                resultDataDTO = future.get();
                if (resultDataDTO != null) {
                    if (StringUtils.equals(resultDataDTO.getResCode(), ResultDataDTO.DEFAULT_SUCCESS_CODE)) {
                        changedFilesCollection.addAll((Collection<? extends Map<String, String>>) resultDataDTO.getData());
                    } else {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                logger.error(String.valueOf(e.getMessage()), e);
            } catch (ExecutionException e) {
                logger.error(String.valueOf(e.getMessage()), e);
            }
        }
        if (resultDataDTO != null && StringUtils.equals(resultDataDTO.getResCode(), ResultDataDTO.DEFAULT_SUCCESS_CODE)) {
            resultDataDTO.setData(changedFilesCollection);
        }
        return resultDataDTO;
    }

    /**
     * @param svnPreCommitWebHookParam
     * @return void
     * @Description svn pre-commit??????????????????????????????SVN?????????????????????????????????
     * @MethodName doBeforeSvnPreCommitHookCheck
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/5/6 11:51
     */
    @Override
    @Transactional(readOnly = true)
    public void doBeforeSvnPreCommitHookCheck(SvnPreCommitWebHookParam svnPreCommitWebHookParam) {
        svnPreCommitWebHookParam.initReposUrl();
        if (StringUtils.isNotEmpty(svnPreCommitWebHookParam.getProtocol()) &&
                StringUtils.isNotEmpty(svnPreCommitWebHookParam.getIp()) &&
                StringUtils.isNotEmpty(svnPreCommitWebHookParam.getPort())) {
            Map<String, String> svnSuperAdminMap = tblToolInfoMapper.getSvnToolSuperAdmin(svnPreCommitWebHookParam.getProtocol().toLowerCase(), svnPreCommitWebHookParam.getIp(), svnPreCommitWebHookParam.getPort());
            if (svnSuperAdminMap != null && !svnSuperAdminMap.isEmpty()) {
                svnPreCommitWebHookParam.setSuperAdminAccount(svnSuperAdminMap.get("superAdminAccount"));
                svnPreCommitWebHookParam.setSuperAdminPassword(svnSuperAdminMap.get("superAdminPassword"));
            }
        }
    }

    /**
     * @param gitLabPreReceiveHookParam
     * @return void
     * @Description gitlab pre-receive?????????????????????
     * @MethodName doBeforeGitLabPreReceiveHookCheck
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/7/17 11:46
     */
    @Override
    @Transactional(readOnly = true)
    public void doBeforeGitLabPreReceiveHookCheck(GitLabPreReceiveHookParam gitLabPreReceiveHookParam) throws URISyntaxException {
        if (StringUtils.isNotEmpty(gitLabPreReceiveHookParam.getProtocol()) &&
                StringUtils.isNotEmpty(gitLabPreReceiveHookParam.getIp()) &&
                StringUtils.isNotEmpty(gitLabPreReceiveHookParam.getPort())) {
            TblToolInfo tblToolInfo = tblToolInfoMapper.getGitLabToolInfo(gitLabPreReceiveHookParam.getProtocol(), gitLabPreReceiveHookParam.getIp(), gitLabPreReceiveHookParam.getPort());
            if (tblToolInfo != null) {
                gitLabPreReceiveHookParam.setRelatedToolInfo(tblToolInfo);
                StringBuilder baseUri = new StringBuilder();
                baseUri.append(Constants.ACCESS_PROTOCOL_MAP.get(tblToolInfo.getAccessProtocol())).append(gitLabPreReceiveHookParam.getIp()).append(":").append(gitLabPreReceiveHookParam.getPort());
                Project project = gitLabWebApiService.getProjectById(baseUri.toString(), gitLabPreReceiveHookParam.getProjectId(), tblToolInfo.getGitApiToken());
                if (project != null) {
                    project.setBaseUri(baseUri.toString());
                    project.setGitApiToken(tblToolInfo.getGitApiToken());
                    gitLabPreReceiveHookParam.setRelatedProject(project);
                }
                gitLabPreReceiveHookParam.setCommitUser(gitLabWebApiService.getUserByEmail(baseUri.toString(), gitLabPreReceiveHookParam.getCommitterEmail(), tblToolInfo.getGitApiToken()));
            }
        }
    }

    /**
     * 
    * @Title: doBeforeGitLabPostReceiveHookHandle
    * @Description: ??????post-receive??????????????????gitlab???????????????????????????????????????????????????????????????
    * @author author
    * @param gitLabPostReceiveHookParam
    * @throws URISyntaxException
    * @throws
     */
    @Override
    @Transactional(readOnly = true)
    public void doBeforeGitLabPostReceiveHookHandle(GitLabPostReceiveHookParam gitLabPostReceiveHookParam) throws URISyntaxException {
        if (StringUtils.isNotEmpty(gitLabPostReceiveHookParam.getProtocol()) &&
                StringUtils.isNotEmpty(gitLabPostReceiveHookParam.getIp()) &&
                StringUtils.isNotEmpty(gitLabPostReceiveHookParam.getPort())) {
            TblToolInfo tblToolInfo = tblToolInfoMapper.getGitLabToolInfo(gitLabPostReceiveHookParam.getProtocol(), gitLabPostReceiveHookParam.getIp(), gitLabPostReceiveHookParam.getPort());
            if (tblToolInfo != null) {
                gitLabPostReceiveHookParam.setRelatedToolInfo(tblToolInfo);
                StringBuilder baseUri = new StringBuilder();
                baseUri.append(Constants.ACCESS_PROTOCOL_MAP.get(tblToolInfo.getAccessProtocol())).append(gitLabPostReceiveHookParam.getIp()).append(":").append(gitLabPostReceiveHookParam.getPort());
                Project project = gitLabWebApiService.getProjectById(baseUri.toString(), gitLabPostReceiveHookParam.getProjectId(), tblToolInfo.getGitApiToken());
                if (project != null) {
                    project.setBaseUri(baseUri.toString());
                    project.setGitApiToken(tblToolInfo.getGitApiToken());
                    gitLabPostReceiveHookParam.setRelatedProject(project);
                }
                Commit commit = gitLabWebApiService.getCommitByCommitId(baseUri.toString(), gitLabPostReceiveHookParam.getProjectId(), gitLabPostReceiveHookParam.getNowCommitId(), tblToolInfo.getGitApiToken());
                if (commit != null) {
                    gitLabPostReceiveHookParam.setCommitInfo(commit);
                    gitLabPostReceiveHookParam.setCommitUser(gitLabWebApiService.getUserByEmail(baseUri.toString(), commit.getCommitterEmail(), tblToolInfo.getGitApiToken()));
                }
            }
        }
    }

    /**
     * @param svnPreCommitWebHookParam
     * @param fileCollection
     * @return void
     * @Description svn pre-commit?????????????????????
     * @MethodName doAfterSvnPreCommitHookCheckSuccess
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/5/6 18:47
     */
    @Override
    @Transactional(readOnly = true)
    public void doAfterSvnPreCommitHookCheckSuccess(SvnPreCommitWebHookParam svnPreCommitWebHookParam, List<Map<String, String>> fileCollection) {
        //?????????????????????id??????redis
        if (svnPreCommitWebHookParam.getRelatedWorkTask() != null && svnPreCommitWebHookParam.getRelatedWorkTask().getId() != null) {
            redisUtils.setForHash("svnRelatedTaskId", svnPreCommitWebHookParam.getAuthor() + "_" + svnPreCommitWebHookParam.getTransaction(), svnPreCommitWebHookParam.getRelatedWorkTask().getId());
        }
        //??????????????????????????????redis
        if (svnPreCommitWebHookParam.getRelatedSystemScmSubmit() != null && svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSystemId() != null) {
            Boolean needReviewFlag = tblSystemInfoMapper.judgeSystemNeedCodeReview(svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSystemId());
            if (needReviewFlag != null && needReviewFlag) {
                redisUtils.setForHash("svnCommmitFile", svnPreCommitWebHookParam.getAuthor() + "_" + svnPreCommitWebHookParam.getTransaction(), fileCollection);
            }
        }
    }

    /**
     * @param svnPostCommitWebHookParam
     * @return java.lang.String
     * @Description ??????svn???post-commit???????????????
     * @MethodName handleSvnPostCommitWebHookRequest
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/1/8 10:53
     */
    @Override
    @Transactional(readOnly = false)
    public String handleSvnPostCommitWebHookRequest(SvnPostCommitWebHookParam svnPostCommitWebHookParam) {
        TblDevTaskScm tblDevTaskScm = new TblDevTaskScm();
        TblDevTaskScmHistory tblDevTaskScmHistory = new TblDevTaskScmHistory();
        TblDevTaskScmFile tblDevTaskScmFile = new TblDevTaskScmFile();
        try {
            Object svnRelatedTaskId = redisUtils.getForHash("svnRelatedTaskId", svnPostCommitWebHookParam.getAuthor() + "_" + svnPostCommitWebHookParam.getTransaction());
            Long taskId = svnRelatedTaskId == null ? null : Long.valueOf((Integer) svnRelatedTaskId);
            //??????????????????id?????????????????????????????????????????????
            if (taskId != null) {
                tblDevTaskScm.setDevTaskId(taskId);
                //????????????????????????
                Long requirementFeatureId = tblRequirementFeatureMapper.getRequirementFeatureIdByDevTaskId(tblDevTaskScm.getDevTaskId());
                if (requirementFeatureId != null) {
                    //?????????????????????
                    devTaskService.updateReqFeatureTimeTrace(JsonUtil.toJson(new HashMap<String, Object>() {{
                        put("requirementFeatureId", requirementFeatureId);
                        put("codeFirstCommitTime", new Timestamp(svnPostCommitWebHookParam.getCommitDate().getTime()));
                    }}));
                }
                //??????????????????id
                TblUserInfoDTO user = devManageToSystemInterface.getCodeBaseUserDetailByUserScmAccount(svnPostCommitWebHookParam.getAuthor());
                if (user != null) {
                    tblDevTaskScm.setDevUserId(user.getId());
                    tblDevTaskScm.setCreateBy(user.getId());
                    tblDevTaskScm.setLastUpdateBy(user.getId());
                    tblDevTaskScmHistory.setDevUserId(user.getId());
                    tblDevTaskScmHistory.setCreateBy(user.getId());
                    tblDevTaskScmHistory.setLastUpdateBy(user.getId());
                    tblDevTaskScmFile.setCreateBy(user.getId());
                    tblDevTaskScmFile.setLastUpdateBy(user.getId());
                }
                //1.??????????????????????????????????????????
                tblDevTaskScm.setScmType(Constants.SCM_TYPE_SVN);
                tblDevTaskScm.setCommitNumber(svnPostCommitWebHookParam.getRevision());
                tblDevTaskScm.setCommitMassage(StringUtils.join(svnPostCommitWebHookParam.getMessage(), "\n"));
                tblDevTaskScm.setStatus(tblDevTaskScm.DEL_FLAG_NORMAL);
                tblDevTaskScm.setCreateDate(new Timestamp(new Date().getTime()));
                tblDevTaskScm.setLastUpdateDate(tblDevTaskScm.getCreateDate());
                tblDevTaskScmMapper.insertOrUpdateDevTaskScm(tblDevTaskScm);

                //2.?????????????????????????????????
                tblDevTaskScmHistory.setDevTaskId(taskId);
                tblDevTaskScmHistory.setScmType(Constants.SCM_TYPE_SVN);
                tblDevTaskScmHistory.setCommitNumber(svnPostCommitWebHookParam.getRevision());
                tblDevTaskScmHistory.setCommitMassage(tblDevTaskScm.getCommitMassage());
                tblDevTaskScmHistory.setStatus(tblDevTaskScmHistory.DEL_FLAG_NORMAL);
                tblDevTaskScmHistory.setCreateDate(new Timestamp(new Date().getTime()));
                tblDevTaskScmHistory.setLastUpdateDate(tblDevTaskScmHistory.getCreateDate());
                tblDevTaskScmHistoryMapper.insertDevTaskScmHistory(tblDevTaskScmHistory);

                //3.??????????????????????????????????????????
                List<Map<String, String>> commitFiles = (List<Map<String, String>>) redisUtils.getForHash("svnCommmitFile", svnPostCommitWebHookParam.getAuthor() + "_" + svnPostCommitWebHookParam.getTransaction());
                if (CollectionUtil.isNotEmpty(commitFiles)) {
                    svnPostCommitWebHookParam.initReposUrl();
                    tblDevTaskScmFile.setDevTaskId(tblDevTaskScm.getDevTaskId());
                    tblDevTaskScmFile.setDevTaskScmId(tblDevTaskScm.getId());
                    tblDevTaskScmFile.setScmUrl(svnPostCommitWebHookParam.getReposUrl());
                    tblDevTaskScmFile.setLastCommitNumber(svnPostCommitWebHookParam.getRevision());
                    tblDevTaskScmFile.setCommitNumber(svnPostCommitWebHookParam.getRevision());
                    tblDevTaskScmFile.setStatus(tblDevTaskScmFile.DEL_FLAG_NORMAL);
                    tblDevTaskScmFile.setCreateDate(new Timestamp(new Date().getTime()));
                    tblDevTaskScmFile.setLastUpdateDate(tblDevTaskScmFile.getCreateDate());
                    batchInsertOrUpdateDevTaskScmFileTask(commitFiles, tblDevTaskScmFile);
                }
            }
            return "????????????";
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "????????????????????????????????????????????????";
        } finally {
            redisUtils.delForHash("svnCommmitFile", svnPostCommitWebHookParam.getAuthor() + "_" + svnPostCommitWebHookParam.getTransaction());
            redisUtils.delForHash("svnRelatedTaskId", svnPostCommitWebHookParam.getAuthor() + "_" + svnPostCommitWebHookParam.getTransaction());
        }
    }

    /**
     * 
    * @Title: handleGitLabPostReceiveHookRequest
    * @Description: gitlab?????????????????????
    * @author author
    * @param gitLabPostReceiveHookParam
    * @return
    * @throws
     */
    @Override
    @Transactional(readOnly = false)
    public String handleGitLabPostReceiveHookRequest(GitLabPostReceiveHookParam gitLabPostReceiveHookParam) {
        TblDevTaskScm tblDevTaskScm = new TblDevTaskScm();
        TblDevTaskScmHistory tblDevTaskScmHistory = new TblDevTaskScmHistory();
        TblDevTaskScmGitFile tblDevTaskScmGitFile = new TblDevTaskScmGitFile();
        try {
            Object gitLabRelatedTaskId = redisUtils.getForHash("gitLabRelatedTaskId", String.valueOf(gitLabPostReceiveHookParam.getProjectId()) + "_" + gitLabPostReceiveHookParam.getNowCommitId());
            Long taskId = gitLabRelatedTaskId == null ? null : Long.valueOf((Integer) gitLabRelatedTaskId);
            if (taskId != null) {
                SpringContextHolder.getBean(VersionService.class).doBeforeGitLabPostReceiveHookHandle(gitLabPostReceiveHookParam);
                tblDevTaskScm.setDevTaskId(taskId);
                //????????????????????????
                Long requirementFeatureId = tblRequirementFeatureMapper.getRequirementFeatureIdByDevTaskId(tblDevTaskScm.getDevTaskId());
                if (requirementFeatureId != null && gitLabPostReceiveHookParam.getCommitInfo() != null) {
                    //?????????????????????
                    devTaskService.updateReqFeatureTimeTrace(JsonUtil.toJson(new HashMap<String, Object>() {{
                        put("requirementFeatureId", requirementFeatureId);
                        put("codeFirstCommitTime", new Timestamp(gitLabPostReceiveHookParam.getCommitInfo().getCommittedDate().getTime()));
                    }}));
                }
                //??????????????????id
                if (gitLabPostReceiveHookParam.getCommitUser() != null &&
                        StringUtils.isNotEmpty(gitLabPostReceiveHookParam.getCommitUser().getUsername())) {
                    TblUserInfoDTO user = devManageToSystemInterface.getCodeBaseUserDetailByUserScmAccount(gitLabPostReceiveHookParam.getCommitUser().getUsername());
                    if (user != null) {
                        tblDevTaskScm.setDevUserId(user.getId());
                        tblDevTaskScm.setCreateBy(user.getId());
                        tblDevTaskScm.setLastUpdateBy(user.getId());
                        tblDevTaskScmHistory.setDevUserId(user.getId());
                        tblDevTaskScmHistory.setCreateBy(user.getId());
                        tblDevTaskScmHistory.setLastUpdateBy(user.getId());
                        tblDevTaskScmGitFile.setCreateBy(user.getId());
                        tblDevTaskScmGitFile.setLastUpdateBy(user.getId());
                    }
                }
                //??????????????????????????????????????????
                tblDevTaskScm.setScmType(Constants.SCM_TYPE_GIT);
                tblDevTaskScm.setCommitNumber(gitLabPostReceiveHookParam.getNowCommitId());
                tblDevTaskScm.setCommitMassage(gitLabPostReceiveHookParam.getCommitInfo() == null ? null : gitLabPostReceiveHookParam.getCommitInfo().getMessage());
                tblDevTaskScm.setCreateDate(new Timestamp(new Date().getTime()));
                tblDevTaskScm.setLastUpdateDate(tblDevTaskScm.getCreateDate());
                tblDevTaskScmMapper.insertOrUpdateDevTaskScm(tblDevTaskScm);
                //?????????????????????????????????
                tblDevTaskScmHistory.setDevTaskId(taskId);
                tblDevTaskScmHistory.setScmType(Constants.SCM_TYPE_GIT);
                tblDevTaskScmHistory.setCommitNumber(gitLabPostReceiveHookParam.getNowCommitId());
                tblDevTaskScmHistory.setCommitMassage(tblDevTaskScm.getCommitMassage());
                tblDevTaskScmHistory.setStatus(tblDevTaskScmHistory.DEL_FLAG_NORMAL);
                tblDevTaskScmHistory.setCreateDate(new Timestamp(new Date().getTime()));
                tblDevTaskScmHistory.setLastUpdateDate(tblDevTaskScmHistory.getCreateDate());
                tblDevTaskScmHistoryMapper.insertDevTaskScmHistory(tblDevTaskScmHistory);
                //??????????????????????????????
                Object gitLabRelatedSystemId = redisUtils.getForHash("gitLabRelatedSystemId", String.valueOf(gitLabPostReceiveHookParam.getProjectId()) + "_" + gitLabPostReceiveHookParam.getNowCommitId());
                if (gitLabRelatedSystemId != null) {
                    Boolean needReviewFlag = tblSystemInfoMapper.judgeSystemNeedCodeReview(Long.valueOf((Integer) gitLabRelatedSystemId));
                    if (needReviewFlag != null && needReviewFlag) {
                        if (CollectionUtil.isNotEmpty(gitLabPostReceiveHookParam.getCommitFiles())) {
                            tblDevTaskScmGitFile.setDevTaskId(tblDevTaskScm.getDevTaskId());
                            tblDevTaskScmGitFile.setDevTaskScmId(tblDevTaskScm.getId());
                            tblDevTaskScmGitFile.setToolId(gitLabPostReceiveHookParam.getRelatedToolInfo() == null ? null : gitLabPostReceiveHookParam.getRelatedToolInfo().getId());
                            tblDevTaskScmGitFile.setGitRepositoryId(Long.valueOf(gitLabPostReceiveHookParam.getProjectId()));
                            tblDevTaskScmGitFile.setGitBranch(gitLabPostReceiveHookParam.getRefName());
                            tblDevTaskScmGitFile.setCommitNumber(gitLabPostReceiveHookParam.getNowCommitId());
                            tblDevTaskScmGitFile.setLastCommitNumber(gitLabPostReceiveHookParam.getOldCommitId());
                            tblDevTaskScmGitFile.setStatus(tblDevTaskScmGitFile.DEL_FLAG_NORMAL);
                            tblDevTaskScmGitFile.setCreateDate(new Timestamp(new Date().getTime()));
                            tblDevTaskScmGitFile.setLastUpdateDate(tblDevTaskScmGitFile.getCreateDate());
                            batchInsertOrUpdateDevTaskScmGitFileTask(gitLabPostReceiveHookParam.getCommitFiles(), tblDevTaskScmGitFile);
                        }
                    }
                }
            }
            return "????????????";
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return "????????????????????????????????????????????????";
        } finally {
            redisUtils.delForHash("gitLabRelatedTaskId", String.valueOf(gitLabPostReceiveHookParam.getProjectId()) + "_" + gitLabPostReceiveHookParam.getNowCommitId());
            redisUtils.delForHash("gitLabRelatedSystemId", String.valueOf(gitLabPostReceiveHookParam.getProjectId()) + "_" + gitLabPostReceiveHookParam.getNowCommitId());
        }
    }

    private List<Future<ResultDataDTO>> getInitChangedFilesFutureTask(SvnPreCommitWebHookParam svnPreCommitWebHookParam) {
        //??????????????????????????????????????????????????????
        int changedFilesSize = svnPreCommitWebHookParam.getChangedFiles().size();
        List<Future<ResultDataDTO>> futureList = new LinkedList<>();
        for (int i = 0; i < THREAD_SIZE; i++) {
            List<String> changedFiles;
            if (i == THREAD_SIZE - 1) {
                changedFiles = svnPreCommitWebHookParam.getChangedFiles().subList(changedFilesSize / THREAD_SIZE * i, changedFilesSize);
            } else {
                changedFiles = svnPreCommitWebHookParam.getChangedFiles().subList(changedFilesSize / THREAD_SIZE * i, changedFilesSize / THREAD_SIZE * (i + 1));
            }
            if (CollectionUtil.isNotEmpty(changedFiles)) {
                futureList.add(SpringContextHolder.getBean(VersionService.class).initChangedFiles(changedFiles, svnPreCommitWebHookParam));
            }
        }
        return futureList;
    }

    private List<Future<ResultDataDTO>> getInitChangedSonFilesFutureTask(List<String> allSonFiles, String type, String copyFromPath, String copyToPath, SvnPreCommitWebHookParam svnPreCommitWebHookParam) {
        //?????????????????????????????????????????????????????????
        int allSonFilesSize = allSonFiles.size();
        List<Future<ResultDataDTO>> futureList = new LinkedList<>();
        for (int i = 0; i < THREAD_SIZE; i++) {
            List<String> sonFiles;
            if (i == THREAD_SIZE - 1) {
                sonFiles = allSonFiles.subList(allSonFilesSize / THREAD_SIZE * i, allSonFilesSize);
            } else {
                sonFiles = allSonFiles.subList(allSonFilesSize / THREAD_SIZE * i, allSonFilesSize / THREAD_SIZE * (i + 1));
            }
            if (CollectionUtil.isNotEmpty(sonFiles)) {
                futureList.add(SpringContextHolder.getBean(VersionService.class).initChangedSonFiles(sonFiles, type, copyFromPath, copyToPath, svnPreCommitWebHookParam));
            }
        }
        return futureList;
    }

    /**
     * 
    * @Title: batchInsertOrUpdateDevTaskScmFileTask
    * @Description: ???????????????????????????????????????????????????
    * @author author
    * @param allCommitFiles ?????????????????????
    * @param tblDevTaskScmFile
    * @throws
     */
    private void batchInsertOrUpdateDevTaskScmFileTask(List<Map<String, String>> allCommitFiles, TblDevTaskScmFile tblDevTaskScmFile) {
        int allCommitFilesSize = allCommitFiles.size();
        for (int i = 0; i < THREAD_SIZE; i++) {
            List<Map<String, String>> commitFiles;
            if (i == THREAD_SIZE - 1) {
                commitFiles = allCommitFiles.subList(allCommitFilesSize / THREAD_SIZE * i, allCommitFilesSize);
            } else {
                commitFiles = allCommitFiles.subList(allCommitFilesSize / THREAD_SIZE * i, allCommitFilesSize / THREAD_SIZE * (i + 1));
            }
            if (CollectionUtil.isNotEmpty(commitFiles)) {
                SpringContextHolder.getBean(VersionService.class).batchInsertOrUpdateDevTaskScmFile(commitFiles, tblDevTaskScmFile);
            }
        }
    }

    private void batchInsertOrUpdateDevTaskScmGitFileTask(List<String> allCommitFiles, TblDevTaskScmGitFile tblDevTaskScmGitFile) {
        int allCommitFilesSize = allCommitFiles.size();
        for (int i = 0; i < THREAD_SIZE; i++) {
            List<String> commitFiles;
            if (i == THREAD_SIZE - 1) {
                commitFiles = allCommitFiles.subList(allCommitFilesSize / THREAD_SIZE * i, allCommitFilesSize);
            } else {
                commitFiles = allCommitFiles.subList(allCommitFilesSize / THREAD_SIZE * i, allCommitFilesSize / THREAD_SIZE * (i + 1));
            }
            if (CollectionUtil.isNotEmpty(commitFiles)) {
                SpringContextHolder.getBean(VersionService.class).batchInsertOrUpdateDevTaskScmGitFile(commitFiles, tblDevTaskScmGitFile);
            }
        }
    }

    /**
     * @param changedFiles
     * @param svnPreCommitWebHookParam
     * @return java.util.concurrent.Future<cn.pioneeruniverse.common.dto.ResultDataDTO>
     * @Description ??????726???????????????????????????????????????????????????????????????????????????????????????
     * @MethodName initChangedFilesFor726Project
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/5/6 12:27
     */
    @Override
    @Async("initChangedFilesForSvnPreCommitHook")
    public Future<ResultDataDTO> initChangedFiles(List<String> changedFiles, SvnPreCommitWebHookParam svnPreCommitWebHookParam) {
        List<Map<String, String>> changedFilesCollection = new LinkedList<>();
        ResultDataDTO resultDataDTO = null;
        outer:
        for (int i = 0; i < changedFiles.size(); i++) {
            if (svnPreCommitWebHookParam.getInitChangedFilesEnd()) {
                break outer;
            } else {
                String changedFile = changedFiles.get(i);
                String type = changedFile.substring(0, 3).replaceFirst(" ", "");
                String filePath = changedFile.substring(3).trim();
                if (StringUtils.equals(type, Constants.ADD_FILE) || StringUtils.equals(type, Constants.UPDATE_FILE_CONTENT) ||
                        StringUtils.equals(type, Constants.UPDATE_FILE_ATTR) || StringUtils.equals(type, Constants.UPDATE_FILE_CONTENT_ATTR)) {
                    //???????????????
                    if (StringUtils.endsWith(filePath, "/")) {
                        continue;
                    }
                    resultDataDTO = dealFileResult(svnPreCommitWebHookParam, filePath, type, changedFilesCollection);
                    if (!StringUtils.equals(resultDataDTO.getResCode(), ResultDataDTO.DEFAULT_SUCCESS_CODE)) {
                        break outer;
                    }
                } else if (StringUtils.equals(type, Constants.ADD_COPY_FILE)) {
                    if (StringUtils.endsWith(filePath, "/")) {
                        String addFileCopyFrom = changedFiles.get(i + 1).trim();
                        addFileCopyFrom = addFileCopyFrom.substring(1, addFileCopyFrom.length() - 1);
                        String[] addFileCopyFromArray = addFileCopyFrom.split(":", 2);
                        Long revision = Long.valueOf(addFileCopyFromArray[1].substring(1));
                        String addFileCopyFromPath = addFileCopyFromArray[0].replaceFirst("from ", "");
                        resultDataDTO = dealDirResult(svnPreCommitWebHookParam, addFileCopyFromPath, addFileCopyFromPath, filePath, type, revision, changedFilesCollection);
                        if (!StringUtils.equals(resultDataDTO.getResCode(), ResultDataDTO.DEFAULT_SUCCESS_CODE)) {
                            break outer;
                        }
                    } else {
                        resultDataDTO = dealFileResult(svnPreCommitWebHookParam, filePath, type, changedFilesCollection);
                        if (!StringUtils.equals(resultDataDTO.getResCode(), ResultDataDTO.DEFAULT_SUCCESS_CODE)) {
                            break outer;
                        }
                    }
                } else if (StringUtils.equals(type, Constants.DEL_FILE)) {
                    if (StringUtils.endsWith(filePath, "/")) {
                        resultDataDTO = dealDirResult(svnPreCommitWebHookParam, filePath, null, null, type, -1L, changedFilesCollection);
                        if (!StringUtils.equals(resultDataDTO.getResCode(), ResultDataDTO.DEFAULT_SUCCESS_CODE)) {
                            break outer;
                        }
                    } else {
                        resultDataDTO = dealFileResult(svnPreCommitWebHookParam, filePath, type, changedFilesCollection);
                        if (!StringUtils.equals(resultDataDTO.getResCode(), ResultDataDTO.DEFAULT_SUCCESS_CODE)) {
                            break outer;
                        }
                    }
                } else {
                    continue;
                }
            }
        }
        if (resultDataDTO != null && StringUtils.equals(resultDataDTO.getResCode(), ResultDataDTO.DEFAULT_SUCCESS_CODE)) {
            resultDataDTO.setData(changedFilesCollection);
        }
        return new AsyncResult<>(resultDataDTO);
    }

    private ResultDataDTO dealFileResult(SvnPreCommitWebHookParam svnPreCommitWebHookParam, String filePath, String type,
                                         List<Map<String, String>> fileCollection) {
        //????????????????????????
        if (svnPreCommitWebHookParam.getCheckSystemScmSubmitRegexes() &&
                CollectionUtil.isNotEmpty(svnPreCommitWebHookParam.getRelatedSystemScmSubmitRegexes())) {
            ResultDataDTO resultDataDTO = this.checkFileSubmitRegex(svnPreCommitWebHookParam, filePath);
            if (!StringUtils.equals(resultDataDTO.getResCode(), ResultDataDTO.DEFAULT_SUCCESS_CODE)) {
                return resultDataDTO;
            }
        }
        //??????726?????????DB??????????????????
        if (svnPreCommitWebHookParam.getCheckDBScriptFileFor726()) {
            ResultDataDTO resultDataDTO = this.checkDBScriptFileFor726Project(svnPreCommitWebHookParam, filePath, type);
            if (!StringUtils.equals(resultDataDTO.getResCode(), ResultDataDTO.DEFAULT_SUCCESS_CODE)) {
                return resultDataDTO;
            }
        }
        if (StringUtils.equals(type, Constants.ADD_FILE) || StringUtils.equals(type, Constants.ADD_COPY_FILE)) {
            fileCollection.add(new HashMap<String, String>() {{
                put("type", type);
                put("filePath", filePath);
                put("revision", null);
            }});
        } else {
            fileCollection.add(new HashMap<String, String>() {{
                put("type", type);
                put("filePath", filePath);
                put("revision", SubversionUtils.getCurrentCommittedRevision(svnPreCommitWebHookParam.getReposUrl() + filePath,
                        svnPreCommitWebHookParam.getSuperAdminAccount(), svnPreCommitWebHookParam.getSuperAdminPassword()));
            }});
        }
        return ResultDataDTO.SUCCESS("??????????????????");
    }

    private ResultDataDTO dealDirResult(SvnPreCommitWebHookParam svnPreCommitWebHookParam, String dirPath, String copyFromPath, String copyToPath, String type, Long revision,
                                        List<Map<String, String>> fileCollection) {
        int executeWaiteTime = 0;
        //?????????????????????????????????
        List<String> filesList = SubversionUtils.getFilesUnderDir(svnPreCommitWebHookParam.getReposUrl(),
                svnPreCommitWebHookParam.getSuperAdminAccount(), svnPreCommitWebHookParam.getSuperAdminPassword(), "/" + dirPath, revision);
        if (CollectionUtil.isNotEmpty(filesList)) {
            for (Future<ResultDataDTO> future : getInitChangedSonFilesFutureTask(filesList, type, copyFromPath, copyToPath, svnPreCommitWebHookParam)) {
                while (!future.isDone() && executeWaiteTime <= exceuteInitSvnDelFilesTimeout) {
                    try {
                        Thread.currentThread().sleep(1000);
                        executeWaiteTime++;
                    } catch (InterruptedException e) {
                        logger.error(String.valueOf(e.getMessage()), e);
                    }
                    if (executeWaiteTime > exceuteInitSvnDelFilesTimeout) {
                        svnPreCommitWebHookParam.setInitDelFilesEnd(Boolean.TRUE);
                    }
                    try {
                        ResultDataDTO resultDataDTO = future.get();
                        if (resultDataDTO != null) {
                            if (StringUtils.equals(resultDataDTO.getResCode(), ResultDataDTO.DEFAULT_SUCCESS_CODE)) {
                                fileCollection.addAll((Collection<? extends Map<String, String>>) resultDataDTO.getData());
                            } else {
                                return resultDataDTO;
                            }
                        }
                    } catch (InterruptedException e) {
                        logger.error(String.valueOf(e.getMessage()), e);
                    } catch (ExecutionException e) {
                        logger.error(String.valueOf(e.getMessage()), e);
                    }
                }
            }
        }
        return ResultDataDTO.SUCCESS("??????????????????");
    }

    /**
     * @param svnPreCommitWebHookParam
     * @param filePath
     * @param type
     * @return cn.pioneeruniverse.common.dto.ResultDataDTO
     * @Description ??????726?????????db????????????????????????
     * @MethodName checkDBScriptFileFor726Project
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/5/6 14:11
     */
    private ResultDataDTO checkDBScriptFileFor726Project(SvnPreCommitWebHookParam svnPreCommitWebHookParam, String filePath, String type) {
        try {
            if (("/" + filePath).contains(dbScriptFilePathFor726)) {
                String[] filePathArray = ("/" + filePath).split(dbScriptFilePathFor726, 2);
                String sonFilePath = filePathArray[1].split("/", 2)[0];
                //String checkFilePath = filePathArray[0] + dbScriptFilePathFor726 + sonFilePath;
                //?????????????????????????????????????????????????????????????????????
                if (StringUtils.isNotEmpty(svnPreCommitWebHookParam.getRelatedDevTask().getSystemVersionName())) {
                    //?????????????????????????????????????????????????????????????????????
                    if (!StringUtils.equals(sonFilePath, svnPreCommitWebHookParam.getRelatedDevTask().getSystemVersionName())) {
                        svnPreCommitWebHookParam.setInitDelFilesEnd(true);
                        svnPreCommitWebHookParam.setInitChangedFilesEnd(true);
                        return ResultDataDTO.FAILURE("??????????????????????????????????????????????????????" + svnPreCommitWebHookParam.getRelatedDevTask().getSystemVersionName());
                    }
                    //???????????????????????????
                    if (StringUtils.equals(type, Constants.UPDATE_FILE_CONTENT) ||
                            StringUtils.equals(type, Constants.UPDATE_FILE_ATTR) ||
                            StringUtils.equals(type, Constants.UPDATE_FILE_CONTENT_ATTR)) {
                        svnPreCommitWebHookParam.setInitDelFilesEnd(true);
                        svnPreCommitWebHookParam.setInitChangedFilesEnd(true);
                        return ResultDataDTO.FAILURE("??????????????????????????????????????????????????????????????????????????????????????????");
                    }
                }
            }
            return ResultDataDTO.SUCCESS("??????????????????");
        } catch (Exception e) {
            svnPreCommitWebHookParam.setInitDelFilesEnd(true);
            svnPreCommitWebHookParam.setInitChangedFilesEnd(true);
            return ResultDataDTO.ABNORMAL("??????????????????????????????????????????????????????????????????,???????????????????????????");
        }
    }

    @Override
    @Async("initChangedSonFilesForSvnPreCommitHook")
    public Future<ResultDataDTO> initChangedSonFiles(List<String> sonFiles, String type, String copyFromPath, String copyToPath, SvnPreCommitWebHookParam svnPreCommitWebHookParam) {
        List<Map<String, String>> changedSonFilesCollection = new LinkedList<>();
        ResultDataDTO resultDataDTO = null;
        for (String sonFile : sonFiles) {
            if (svnPreCommitWebHookParam.getInitChangedFilesEnd() || svnPreCommitWebHookParam.getInitDelFilesEnd()) {
                break;
            } else {
                if (StringUtils.equals(type, Constants.ADD_COPY_FILE)) {
                    sonFile = copyToPath + sonFile.split(copyFromPath, 2)[1];
                }
                //???????????????
                if (svnPreCommitWebHookParam.getCheckSystemScmSubmitRegexes() &&
                        CollectionUtil.isNotEmpty(svnPreCommitWebHookParam.getRelatedSystemScmSubmitRegexes())) {
                    resultDataDTO = this.checkFileSubmitRegex(svnPreCommitWebHookParam, sonFile);
                    if (!StringUtils.equals(resultDataDTO.getResCode(), ResultDataDTO.DEFAULT_SUCCESS_CODE)) {
                        break;
                    }
                }
                if (svnPreCommitWebHookParam.getCheckDBScriptFileFor726()) {
                    resultDataDTO = this.checkDBScriptFileFor726Project(svnPreCommitWebHookParam, sonFile, type);
                    if (!StringUtils.equals(resultDataDTO.getResCode(), ResultDataDTO.DEFAULT_SUCCESS_CODE)) {
                        break;
                    }
                }
                Map<String, String> fileResult = new HashMap<>();
                fileResult.put("type", type);
                fileResult.put("filePath", sonFile);
                if (StringUtils.equals(type, Constants.ADD_COPY_FILE)) {
                    fileResult.put("revision", null);
                } else if (StringUtils.equals(type, Constants.DEL_FILE)) {
                    fileResult.put("revision", SubversionUtils.getCurrentCommittedRevision(svnPreCommitWebHookParam.getReposUrl() + sonFile,
                            svnPreCommitWebHookParam.getSuperAdminAccount(), svnPreCommitWebHookParam.getSuperAdminPassword()));
                }
                changedSonFilesCollection.add(fileResult);
            }
        }
        if (resultDataDTO != null && StringUtils.equals(resultDataDTO.getResCode(), ResultDataDTO.DEFAULT_SUCCESS_CODE)) {
            resultDataDTO.setData(changedSonFilesCollection);
        }
        return new AsyncResult<>(resultDataDTO);
    }

    @Override
    @Async("batchInsertOrUpdateDevTaskScmFileForPostCommitHook")
    public Future<Boolean> batchInsertOrUpdateDevTaskScmFile(List<Map<String, String>> commitFiles, TblDevTaskScmFile tblDevTaskScmFile) {
        //?????????????????????asyc???????????????
        SpringContextHolder.getBean(VersionService.class).insertOrUpdateDevTaskScmFile(commitFiles, tblDevTaskScmFile);
        return new AsyncResult<>(Boolean.TRUE);
    }

    @Override
    @Async("batchInsertOrUpdateDevTaskScmGitFileForPostReceiveHook")
    public Future<Boolean> batchInsertOrUpdateDevTaskScmGitFile(List<String> commitFiles, TblDevTaskScmGitFile tblDevTaskScmGitFile) {
        SpringContextHolder.getBean(VersionService.class).insertOrUpdateDevTaskScmGitFile(commitFiles, tblDevTaskScmGitFile);
        return new AsyncResult<>(Boolean.TRUE);
    }

    @Override
    @Transactional(readOnly = false)
    public void insertOrUpdateDevTaskScmFile(List<Map<String, String>> commitFiles, TblDevTaskScmFile tblDevTaskScmFile) {
        tblDevTaskScmFileMapper.insertOrUpdateDevTaskScmFile(commitFiles, tblDevTaskScmFile);
    }

    @Override
    @Transactional(readOnly = false)
    public void insertOrUpdateDevTaskScmGitFile(List<String> commitFiles, TblDevTaskScmGitFile tblDevTaskScmGitFile) {
        tblDevTaskScmGitFileMapper.insertOrUpdateDevTaskScmGitFile(commitFiles, tblDevTaskScmGitFile);
    }

    @Override
    @Transactional(readOnly = true)
    public void modifySvnPassword(Long currentUserId, String userScmAccount, String userScmPassword, String entryptUserScmPassword) throws Exception {
        //???????????????????????????svn????????????
    	Integer toolType = 2;//svn
    	//?????????????????????????????????????????????SVN???
        List<Map<String, Object>> svnToolList = tblToolInfoMapper.getMySvnToolInfo(currentUserId,toolType);
        //?????????????????????
      /*  List<Map<String, Object>> svnToolList = new ArrayList<Map<String, Object>>() {{
            add(new HashMap<String, Object>() {{
                put("ip", "192.168.1.116");
                put("accessProtocol", 1);
            }});
           add(new HashMap<String, Object>() {{
                put("ip", "192.168.1.116");
                put("accessProtocol", 3);
            }});
        }};*/
        if (CollectionUtil.isNotEmpty(svnToolList)) {
            for (Map<String, Object> svnTool : svnToolList) {
                if (svnTool != null && !svnTool.isEmpty()) {
                	String ip = (String) svnTool.get("ip");
                	Integer accessProtocol = (Integer) svnTool.get("accessProtocol");
                    //??????svn??????????????????
                    if (accessProtocol == 1) {
                        //svn://???????????????????????????????????????????????????
                        if (!userScmPassword.matches("^[A-Za-z0-9]+$")) {
                            throw new CredentialException("???????????????SVN???????????????svn://??????????????????????????????????????????????????????????????????????????????????????????");
                        } else {
                            SubversionUtils.modifySvnUserPassword(accessProtocol, ip, new HashMap<String, String>() {{
                                put(userScmAccount, userScmPassword);
                            }});
                        }
                    } else if (accessProtocol == 3) {
                        SubversionUtils.modifySvnUserPassword(accessProtocol, ip, new HashMap<String, String>() {{
                            put(userScmAccount, entryptUserScmPassword);
                        }});
                    }
                }
            }
        } else {
            throw new Exception("????????????????????????SVN??????");
        }
    }
    @Override
	public void modifyGitPassword(Long currentUserId, String userScmAccount, String userScmPassword,
			String entryptUserScmPassword)throws Exception {
    	 //???????????????????????????git????????????
    	Integer toolType = 1;//git
    	//??????????????????????????????GIT??????
        List<Map<String, Object>> gitToolList = tblToolInfoMapper.getMySvnToolInfo(currentUserId,toolType);  
        if (CollectionUtil.isNotEmpty(gitToolList)) {
            for (Map<String, Object> svnTool : gitToolList) {
                if (svnTool != null && !svnTool.isEmpty()) {
                	String ip = (String) svnTool.get("ip");
                	Integer accessProtocol = (Integer) svnTool.get("accessProtocol");
                	
            		StringBuilder baseUri = new StringBuilder();
                    baseUri.append(Constants.ACCESS_PROTOCOL_MAP.get(accessProtocol)).append(ip).append(":").append(svnTool.get("port"));
            		User user = gitLabWebApiService.getUserByUserName(baseUri.toString(),userScmAccount,(String)svnTool.get("gitApiToken"));
            		if(user != null) {
            			gitLabWebApiService.editUserPasswordById(baseUri.toString(),user.getId(),userScmPassword,(String)svnTool.get("gitApiToken"));
            		}
                }
            }
        } else {
            throw new Exception("????????????????????????git??????");
        }
		
	}

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMyProjectSystems(String token) {
        //?????????????????????
        List<String> roles = (List<String>) CommonUtil.getCurrentUserByToken(token).get("roles");
        if (CollectionUtil.isNotEmpty(roles) && roles.contains("XITONGGUANLIYUAN")) {
            //?????????????????????????????????????????????????????????
            return tblSystemInfoMapper.getSystems();
        } else {
            return tblSystemInfoMapper.getMySystemList(CommonUtil.getUserIdByToken(token));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCodeBaseAddresses(Integer codeBaseType) {
        return tblToolInfoMapper.getCodeBaseToolInfoByCodeBaseType(codeBaseType);
    }

    /**
     * 
    * @Title: addNewCodeBase
    * @Description: ??????????????????
    * @author author
    * @param request
    * @param tblSystemScmRepository ????????????
    * @throws Exception
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void addNewCodeBase(HttpServletRequest request, TblSystemScmRepository tblSystemScmRepository) throws Exception {
        tblSystemScmRepository.preInsertOrUpdate(request);
        if (tblSystemScmRepository.getScmType().equals(2)) {
            //??????svn??????????????????
            SubversionUtils.createSvnRepository(tblSystemScmRepository.getIp(), tblSystemScmRepository.getRepositoryName());
            tblSystemScmRepositoryMapper.insertSystemScmRepository(tblSystemScmRepository);
        } else if (tblSystemScmRepository.getScmType().equals(1)) {
            //????????????????????????
            TblToolInfo tblToolInfo = tblToolInfoMapper.selectByPrimaryKey(tblSystemScmRepository.getToolId());
            StringBuilder baseUri = new StringBuilder();
            baseUri.append(Constants.ACCESS_PROTOCOL_MAP.get(tblToolInfo.getAccessProtocol())).append(tblToolInfo.getIp()).append(":").append(tblToolInfo.getPort());
            Project project = new Project();
            project.setName(tblSystemScmRepository.getRepositoryName());
            project.setVisibility(Constants.GitLab.PROJECT_VISIBILITY_PRIVATE);
            project = gitLabWebApiService.createProject(baseUri.toString(), project, tblToolInfo.getGitApiToken());
            if (project != null && project.getId() != null) {
            	//gitlab?????????ID????????????gitlab???????????????
                tblSystemScmRepository.setGitRepositoryId(Long.valueOf(project.getId()));
                tblSystemScmRepositoryMapper.insertSystemScmRepository(tblSystemScmRepository);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCodeBases(Long systemId, Integer scmType) {
        return tblSystemScmRepositoryMapper.getSystemScmRepositories(systemId, scmType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TblSystemVersion> getSystemVersions(Long systemId) {
        return systemVersionMapper.getSystemVersionsBySystemId(systemId);
    }

    /**
     * 
    * @Title: saveCodeSubmitConfig
    * @Description: ????????????????????????
    * @author author
    * @param request
    * @param systemScmSubmitConfigList ????????????????????????
     */
    @Override
    @Transactional(readOnly = false)
    public void saveCodeSubmitConfig(HttpServletRequest request, List<TblSystemScmSubmit> systemScmSubmitConfigList) {
        if (CollectionUtil.isNotEmpty(systemScmSubmitConfigList)) {
            for (TblSystemScmSubmit tblSystemScmSubmit : systemScmSubmitConfigList) {
                tblSystemScmSubmit.preInsertOrUpdate(request);
                if (tblSystemScmSubmit.getId() != null) {
                    tblSystemScmSubmitMapper.updateSystemScmSubmit(tblSystemScmSubmit);
                } else {
                    tblSystemScmSubmitMapper.insertSystemScmSubmit(tblSystemScmSubmit);
                }
            }
        }
    }

    /**
     * 
    * @Title: saveCodeSubmitRegexConfig
    * @Description: ???????????????????????????????????????.ignore
    * @author author
    * @param request
    * @param systemScmSubmitRegexList
     */
    @Override
    @Transactional(readOnly = false)
    public void saveCodeSubmitRegexConfig(HttpServletRequest request, List<TblSystemScmSubmitRegex> systemScmSubmitRegexList) {
        if (CollectionUtil.isNotEmpty(systemScmSubmitRegexList)) {
            for (TblSystemScmSubmitRegex systemScmSubmitRegex : systemScmSubmitRegexList) {
                systemScmSubmitRegex.preInsertOrUpdate(request);
                if (systemScmSubmitRegex.getId() != null) {
                    tblSystemScmSubmitRegexMapper.updateSystemScmSubmitRegex(systemScmSubmitRegex);
                } else {
                    tblSystemScmSubmitRegexMapper.insertSystemScmSubmitRegex(systemScmSubmitRegex);
                }
            }
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void delCodeSubmitConfig(HttpServletRequest request, TblSystemScmSubmit tblSystemScmSubmit) {
        tblSystemScmSubmitMapper.deleteSystemScmSubmit(tblSystemScmSubmit);
    }

    @Override
    @Transactional(readOnly = false)
    public void delCodeSubmitRegexConfig(HttpServletRequest request, TblSystemScmSubmitRegex tblSystemScmSubmitRegex) {
        tblSystemScmSubmitRegexMapper.deleteSystemScmSubmitRegex(tblSystemScmSubmitRegex);
    }

    /**
     * 
    * @Title: getSystemScmSubmitConfigs
    * @Description: ????????????????????????
    * @author author
    * @param systemId ??????id
    * @param scmType ????????????1git,2svn
    * @return List<TblSystemScmSubmit>
     */
    @Override
    @Transactional(readOnly = true)
    public List<TblSystemScmSubmit> getSystemScmSubmitConfigs(Long systemId, Integer scmType) {
    	List<TblSystemScmSubmit> tblSystemScmSubmitList = tblSystemScmSubmitMapper.getSystemScmSubmits(systemId, scmType);
    	String userIds = "";
    	for (TblSystemScmSubmit tblSystemScmSubmit : tblSystemScmSubmitList) {
			if (StringUtil.isNotEmpty(tblSystemScmSubmit.getSubmitUserNames())) {
				userIds +=tblSystemScmSubmit.getSubmitUserNames() + ",";
			}
			if (StringUtil.isNotEmpty(tblSystemScmSubmit.getSubmitSuperUserNames())) {
				userIds +=tblSystemScmSubmit.getSubmitSuperUserNames() + ",";
			}
		}
    	if (StringUtil.isNotEmpty(userIds)) {
    		userIds = userIds.substring(0, userIds.length() - 1);
    		Set<String> set = new HashSet<String>();
    		for (String id : userIds.split(",")) {//??????
    			set.add(id);
			}
    		EntityWrapper<TblUserInfo> wrapper = new EntityWrapper<TblUserInfo>();
    		wrapper.eq("STATUS", 1);
    		wrapper.in("ID", set);
    		List<TblUserInfo> userList = tblUserInfoMapper.selectList(wrapper);
    		
    		Map<String, String> userIdsMap = new HashMap<String, String>();
    		for (TblUserInfo tblUserInfo : userList) {
    			userIdsMap.put(tblUserInfo.getId().toString(), tblUserInfo.getUserName());
			}
    		
    		String nameTemp;
    		for (TblSystemScmSubmit tblSystemScmSubmit : tblSystemScmSubmitList) {
    			nameTemp = tblSystemScmSubmit.getSubmitUserNames();
    			if (StringUtil.isNotEmpty(nameTemp)) {
    				nameTemp = assembleUserNames(userIdsMap, nameTemp);
    				tblSystemScmSubmit.setSubmitUserRealNames(nameTemp);
    			}
    			
    			nameTemp = tblSystemScmSubmit.getSubmitSuperUserNames();
    			if (StringUtil.isNotEmpty(nameTemp)) {
    				nameTemp = assembleUserNames(userIdsMap, nameTemp);
    				tblSystemScmSubmit.setSubmitSuperUserRealNames(nameTemp);
    			}
    		}
    	}
        return tblSystemScmSubmitList;
    }

	private String assembleUserNames(Map<String, String> userIdsMap, String nameTemp) {
		String[] idArr = nameTemp.split(",");
		nameTemp = "";
		for (String id : idArr) {
			if (userIdsMap.containsKey(id)) {
				nameTemp += userIdsMap.get(id) + "," ;
			}
		}
		if (StringUtil.isNotEmpty(nameTemp)) {
			nameTemp = nameTemp.substring(0, nameTemp.length() - 1);
		}
		return nameTemp;
	}

	/**
	 * 
	* @Title: getSystemScmSubmitRegexConfigs
	* @Description: ????????????????????????????????????
	* @author author
	* @param systemId ??????ID
	* @return List<TblSystemScmSubmitRegex>
	 */
    @Override
    @Transactional(readOnly = true)
    public List<TblSystemScmSubmitRegex> getSystemScmSubmitRegexConfigs(Long systemId) {
    	List<TblSystemScmSubmitRegex> submitRegexList = tblSystemScmSubmitRegexMapper.getSystemScmSubmitRegexes(systemId);
        String userIds = "";
    	for (TblSystemScmSubmitRegex submitRegex : submitRegexList) {
			if (StringUtil.isNotEmpty(submitRegex.getSubmitUserNames())) {
				userIds +=submitRegex.getSubmitUserNames() + ",";
			}
		}
    	if (StringUtil.isNotEmpty(userIds)) {
    		userIds = userIds.substring(0, userIds.length() - 1);
    		Set<String> set = new HashSet<String>();
    		for (String id : userIds.split(",")) {//??????
    			set.add(id);
			}
    		EntityWrapper<TblUserInfo> wrapper = new EntityWrapper<TblUserInfo>();
    		wrapper.eq("STATUS", 1);
    		wrapper.in("ID", set);
    		List<TblUserInfo> userList = tblUserInfoMapper.selectList(wrapper);
    		
    		Map<String, String> userIdsMap = new HashMap<String, String>();
    		for (TblUserInfo tblUserInfo : userList) {
    			userIdsMap.put(tblUserInfo.getId().toString(), tblUserInfo.getUserName());
			}
    		
    		String nameTemp;
    		for (TblSystemScmSubmitRegex submitRegex : submitRegexList) {
    			nameTemp = submitRegex.getSubmitUserNames();
    			if (StringUtil.isNotEmpty(nameTemp)) {
    				nameTemp = assembleUserNames(userIdsMap, nameTemp);
    				submitRegex.setSubmitUserRealNames(nameTemp);
    			}
    		}
    	}
    	return submitRegexList;
    }

    /**
     * 
    * @Title: getSystemGitLabProjects
    * @Description: ??????gitlab??????Project????????????
    * @author author
    * @param systemId ??????id
    * @return List<Project>
    * @throws URISyntaxException
     */
    @Override
    @Transactional(readOnly = true)
    public List<Project> getSystemGitLabProjects(Long systemId) throws URISyntaxException {
        List<Map<String, Object>> systemGitLabRepositoryLocationInfoList = tblToolInfoMapper.getSystemGitLabRepositoryLocationInfo(systemId.toString());
        if (CollectionUtil.isNotEmpty(systemGitLabRepositoryLocationInfoList)) {
            List<Project> result = new ArrayList<>();
            for (Map<String, Object> locationInfoMap : systemGitLabRepositoryLocationInfoList) {
                String accessProtocol = Constants.ACCESS_PROTOCOL_MAP.get(locationInfoMap.get("accessProtocol"));
                String ip = (String) locationInfoMap.get("ip");
                String port = (String) locationInfoMap.get("port");
                String gitApiToken = (String) locationInfoMap.get("gitApiToken");
                Long gitRepositoryId = (Long) locationInfoMap.get("gitRepositoryId");
                StringBuilder baseUri = new StringBuilder();
                baseUri.append(accessProtocol).append(ip).append(":").append(port);
                //??????gitlab???api????????????
                Project project = gitLabWebApiService.getProjectById(baseUri.toString(), Integer.valueOf(gitRepositoryId.intValue()), gitApiToken);
                if (project != null) {
                    project.setBaseUri(baseUri.toString());
                    project.setGitApiToken(gitApiToken);
                    result.add(project);
                }
            }
            return result;
        }
        return null;
    }
    
    /**
     * 
    * @Title: getSystemGitLabProjectsWithSystemIds
    * @Description: ????????????????????????git??????
    * @author author
    * @param systemIds ??????id
    * @return List<Project>
    * @throws URISyntaxException
     */
    @Override
    @Transactional(readOnly = true)
    public List<Project> getSystemGitLabProjectsWithSystemIds(String systemIds) throws URISyntaxException {
        List<Map<String, Object>> systemGitLabRepositoryLocationInfoList = tblToolInfoMapper.getSystemGitLabRepositoryLocationInfo(systemIds);
        if (CollectionUtil.isNotEmpty(systemGitLabRepositoryLocationInfoList)) {
            List<Project> result = new ArrayList<>();
            for (Map<String, Object> locationInfoMap : systemGitLabRepositoryLocationInfoList) {
            	String accessProtocol = Constants.ACCESS_PROTOCOL_MAP.get(locationInfoMap.get("accessProtocol"));
                String ip = (String) locationInfoMap.get("ip");
                String port = (String) locationInfoMap.get("port");
                String gitApiToken = (String) locationInfoMap.get("gitApiToken");
                Long gitRepositoryId = (Long) locationInfoMap.get("gitRepositoryId");
                String repositoryName = (String) locationInfoMap.get("repositoryName");
                Long systemId = (Long) locationInfoMap.get("systemId");
                StringBuilder baseUri = new StringBuilder();
                baseUri.append(accessProtocol).append(ip).append(":").append(port);
                
                Project project = new Project();
                project.setId(gitRepositoryId == null ? null : gitRepositoryId.intValue());
                project.setName(repositoryName);
                project.setBaseUri(baseUri.toString());
                project.setGitApiToken(gitApiToken);
                project.setSystemId(systemId);
                result.add(project);
            }
            return result;
        }
        return null;
    }

    /**
     * 
    * @Title: saveGitLabModify
    * @Description: gitlab???????????????????????????
    * @author author
    * @param project gitlab??????
    * @param addUserCollection ?????????????????????
    * @param delUserCollection ?????????????????????
    * @param modifyUserCollection ?????????????????????
    * @return String ??????????????????
     */
    @Override
    public String saveGitLabModify(Project project, List<UserDTO> addUserCollection, List<UserDTO> delUserCollection, List<UserDTO> modifyUserCollection) {
        StringBuilder msg = new StringBuilder();
        if (CollectionUtil.isNotEmpty(addUserCollection)) {
            for (UserDTO addUser : addUserCollection) {
                try {
                    Map<String, String> result = devManageToSystemInterface.createSvnAccountPassword(addUser.getUserId());
                    if (result == null || result.isEmpty()) {
                        throw new RuntimeException("scm account get error when add git member");
                    }
                    String username = result.get("username");//????????????????????????  userScmAccount
                    String password = result.get("password");//???????????????????????? userScmPassword
                    User user = gitLabWebApiService.getUserByUserName(project.getBaseUri(), addUser.getUsername(), project.getGitApiToken());
                    if (user == null || user.getId() == null) {
                        user = new User(addUser.getEmail(), username, addUser.getName(), PasswordUtil.decryptForAES(password));
                        user.setSkipConfirmation(true);
                        user = gitLabWebApiService.createUser(project.getBaseUri(), user, project.getGitApiToken());
                    }
                    if (user != null && user.getId() != null) {
                        Member member = new Member(user.getId(), addUser.getGitLabAccessLevel());
                        gitLabWebApiService.addMemberToProject(project.getBaseUri(), member, project.getId(), project.getGitApiToken());
                        msg.append(addUser.getName()).append("????????????!").append("</br>");
                    }
                } catch (Exception e1) {
                    logger.error("add git member error,cause:" + e1.getMessage());
                    msg.append(addUser.getName()).append("????????????!").append("</br>");
                    continue;
                }
            }
        }
        if (CollectionUtil.isNotEmpty(delUserCollection)) {
            for (UserDTO delUser : delUserCollection) {
                try {
                    User user = gitLabWebApiService.getUserByUserName(project.getBaseUri(), delUser.getUsername(), project.getGitApiToken());
                    if (user != null && user.getId() != null) {
                        gitLabWebApiService.deleteMemberFromProject(project.getBaseUri(), project.getId(), user.getId(), project.getGitApiToken());
                        msg.append(delUser.getName()).append("????????????!").append("\n");
                    }
                } catch (Exception e2) {
                    logger.error("del git member error,cause:" + e2.getMessage());
                    msg.append(delUser.getName()).append("????????????!").append("\n");
                    continue;
                }
            }
        }
        if (CollectionUtil.isNotEmpty(modifyUserCollection)) {
            for (UserDTO modifyUser : modifyUserCollection) {
                try {
                    User user = gitLabWebApiService.getUserByUserName(project.getBaseUri(), modifyUser.getUsername(), project.getGitApiToken());
                    if (user != null && user.getId() != null) {
                        Member member = new Member(user.getId(), modifyUser.getGitLabAccessLevel());
                        gitLabWebApiService.editMemberOfProject(project.getBaseUri(), member, project.getId(), user.getId(), project.getGitApiToken());
                        msg.append(modifyUser.getName()).append("????????????!").append("\n");
                    }
                } catch (Exception e3) {
                    logger.error("modify git member error,cause:" + e3.getMessage());
                    msg.append(modifyUser.getName()).append("????????????!").append("\n");
                    continue;
                }
            }
        }
        return msg.toString();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TblSystemScmSubmitRegex> getSystemScmSubmitRegexesForCodeCommit(TblSystemScmSubmitRegex tblSystemScmSubmitRegex) {
        return tblSystemScmSubmitRegexMapper.getSystemScmSubmitRegexesForCodeCommit(tblSystemScmSubmitRegex);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TblSystemScmRepository> findScmRepositoryBySystemId(Long systemId) {
        return tblSystemScmRepositoryMapper.findScmRepositoryBySystemId(systemId);
    }

    /**
     * @param svnPreCommitWebHookParam
     * @param filePath
     * @return cn.pioneeruniverse.common.dto.ResultDataDTO
     * @Description ?????????????????????????????????(????????????:??????)
     * @MethodName checkFileSubmitRegex
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/10/23 17:42
     */
    private ResultDataDTO checkFileSubmitRegex(SvnPreCommitWebHookParam svnPreCommitWebHookParam, String filePath) {
        try {
            for (TblSystemScmSubmitRegex systemScmSubmitRegex : svnPreCommitWebHookParam.getRelatedSystemScmSubmitRegexes()) {
                if (StringUtils.isNotEmpty(systemScmSubmitRegex.getRegex())) {
                    if (CommonUtil.handleExcludeURL(Arrays.asList(systemScmSubmitRegex.getRegex().split("\\r?\\n")), "/" + filePath) &&
                            !systemScmSubmitRegex.getSubmitUserNamesCollection().contains(svnPreCommitWebHookParam.getAuthor())) {
                        return ResultDataDTO.FAILURE("????????????/" + filePath + "??????????????????????????????????????????");
                    }
                }
            }
            return ResultDataDTO.SUCCESS("??????????????????");
        } catch (Exception e) {
            svnPreCommitWebHookParam.setInitDelFilesEnd(true);
            svnPreCommitWebHookParam.setInitChangedFilesEnd(true);
            return ResultDataDTO.ABNORMAL("???????????????????????????????????????????????????,???????????????????????????");
        }
    }
    
    /**
     * 
    * @Title: getCodeFilesByDevTaskId
    * @Description: ?????????????????????????????????????????????
    * @author author
    * @param devTaskId ??????????????????Id
    * @return  Map<String, Object> key:svnFiles  ?????????svn??????
    *                                  gitFiles  ?????????git??????
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getCodeFilesByDevTaskId(Long devTaskId) {
        Map<String, Object> result = new HashMap<>();
        result.put("svnFiles", tblDevTaskScmFileMapper.getSvnFilesByDevTaskId(devTaskId));
        result.put("gitFiles", tblDevTaskScmGitFileMapper.getGitFilesByDevTaskId(devTaskId));
        return result;
    }

	

}

