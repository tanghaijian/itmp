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
* @Description: svn和git代码仓库操作
* @author author
* @date 2020年8月21日 上午9:29:05
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

    //启用线程数量
    private static final int THREAD_SIZE = 30;

    //删除SVN文件超时设置
    @Value("${execute.init.svn.del.files.timeout}")
    private int exceuteInitSvnDelFilesTimeout;

    //修改SVN文件超时设置
    @Value("${execute.init.svn.changed.files.timeout}")
    private int executeInitSvnChangedFilesTimeout;

    //726数据库脚本路径
    @Value("${db.script.file.path.for.726}")
    private String dbScriptFilePathFor726;

    /**
     * @param jqGridPage
     * @param ip
     * @param repositoryName
     * @param path
     * @return cn.pioneeruniverse.common.entity.JqGridPage<cn.pioneeruniverse.common.dto.TblUserInfoDTO>
     * @Description 获取svn用户分页数据
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
        	//从SVN中获取用户信息，然后与系统用户信息对比，相符合的展示
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
    * @Description: 获取GITLAB配置的用户列表
    * @author author
    * @param jqGridPage
    * @param project git项目
    * @return JqGridPage<TblUserInfoDTO>
    * @throws Exception
     */
    @Override
    public JqGridPage<TblUserInfoDTO> getGitLabUserAuth(JqGridPage<TblUserInfoDTO> jqGridPage, Project project) throws Exception {
    	//先从gitlab中获取该项目下的所有人员的账号，然后与系统内存储的git账号进行比对，相符合则返回
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
     * @Description 保存svn配置修改
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
     * @Description 获取用户能访问的svn仓库配置
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
     * @Description 获取用户能访问的svn文件目录
     * @MethodName getSvnFileTree
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/27 14:14
     */
    @Override
    public List<SvnFileDirectoryStructure> getSvnFileTree(Long systemId) {
        List<SvnFileDirectoryStructure> list = new LinkedList<>();
        List<Map<String, Object>> reposLocationList = SpringContextHolder.getBean(VersionService.class).getMySvnRepositoryLocationInfo(systemId);
        //方便开发调试用
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
            //进行异步任务列表
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
     * @Description: 获取svn文件目录结构线程任务，回调
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
     * @Description 处理gitLab的pre-receive的钩子请求
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
                //将关联系统id放入redis(hashkey=项目id_commitId)
                if (gitLabPreReceiveHookParam.getRelatedSystemScmSubmit() != null && gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getSystemId() != null) {
                    redisUtils.setForHash("gitLabRelatedSystemId", String.valueOf(gitLabPreReceiveHookParam.getProjectId()) +
                            "_" + gitLabPreReceiveHookParam.getNowCommitId(), gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getSystemId());
                }
                //将关联工作任务id放入redis(hashkey=项目id_commitId)
                if (gitLabPreReceiveHookParam.getRelatedWorkTask() != null && gitLabPreReceiveHookParam.getRelatedWorkTask().getId() != null) {
                    redisUtils.setForHash("gitLabRelatedTaskId", String.valueOf(gitLabPreReceiveHookParam.getProjectId()) +
                            "_" + gitLabPreReceiveHookParam.getNowCommitId(), gitLabPreReceiveHookParam.getRelatedWorkTask().getId());
                }
                return "提交成功";
            } else {
                return resultDataDTO.getResDesc();
            }
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            redisUtils.delForHash("gitLabRelatedSystemId", String.valueOf(gitLabPreReceiveHookParam.getProjectId()) +
                    "_" + gitLabPreReceiveHookParam.getNowCommitId());
            redisUtils.delForHash("gitLabRelatedTaskId", String.valueOf(gitLabPreReceiveHookParam.getProjectId()) +
                    "_" + gitLabPreReceiveHookParam.getNowCommitId());
            return "提交失败，提交验证出现异常,请联系系统管理员！";
        }
    }

    /**
     * 
    * @Title: handleSvnPreCommitWebHookRequest
    * @Description: SVN提交前校验请求
    * @author author
    * @param svnPreCommitWebHookParam
    * @return String 提交提示信息
     */
    @Override
    public String handleSvnPreCommitWebHookRequest(SvnPreCommitWebHookParam svnPreCommitWebHookParam) {
        try {
            SpringContextHolder.getBean(VersionService.class).doBeforeSvnPreCommitHookCheck(svnPreCommitWebHookParam);
            String json = JSON.toJSONString(svnPreCommitWebHookParam);
        	logger.info(json);
            ResultDataDTO resultDataDTO = SpringContextHolder.getBean(VersionService.class).svnPreCommitHookCheckMainProcess(svnPreCommitWebHookParam);
            if (StringUtils.equals(resultDataDTO.getResCode(), ResultDataDTO.DEFAULT_SUCCESS_CODE)) {
                //提交代码对应的路径未在代码提交处配置则不做后续操作，提交成功
                if (svnPreCommitWebHookParam.getRelatedSystemScmSubmit() == null) {
                	logger.info("11111111111");
                    return "提交成功";
                }
                // //当前提交人为超级管理员且无法通过填写的message找到关联任务编号则不做后续操作，提交成功
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
                    return "提交成功";
                }
                svnPreCommitWebHookParam.setCheckSystemScmSubmitRegexes(true);
                //代码通配符
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
                        return "提交成功";
                    } else {
                        return resultDataDTO.getResDesc();
                    }
                } else {
                	logger.info("44444444444");
                    return "提交成功";
                }
            } else {
                return resultDataDTO.getResDesc();
            }
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            redisUtils.delForHash("svnRelatedTaskId", svnPreCommitWebHookParam.getAuthor() + "_" + svnPreCommitWebHookParam.getTransaction());
            redisUtils.delForHash("svnCommmitFile", svnPreCommitWebHookParam.getAuthor() + "_" + svnPreCommitWebHookParam.getTransaction());
            return "提交失败，提交验证出现异常,请联系系统管理员！";
        } finally {
            svnPreCommitWebHookParam.setInitDelFilesEnd(Boolean.TRUE);
            svnPreCommitWebHookParam.setInitChangedFilesEnd(Boolean.TRUE);
        }
    }

    /**
     * @param svnPreCommitWebHookParam
     * @return cn.pioneeruniverse.common.dto.ResultDataDTO
     * @Description svn pre-commit钩子验证主流程
     * @MethodName svnPreCommitHookCheckMainProcess
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/5/6 10:03
     */
    @Override
    @Transactional(readOnly = true)
    public ResultDataDTO svnPreCommitHookCheckMainProcess(SvnPreCommitWebHookParam svnPreCommitWebHookParam) {
        try {
            //TODO 之后可以启用多线程优化
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
            //1.检测所提交的代码能否对应平台中已登记的系统（未找到对应系统则不做后续拦截验证，允许提交）
            if (svnPreCommitWebHookParam.getRelatedSystemScmSubmit() != null) {
                //2.判断当前提交用户是否为超级用户（是超级用户不做后续拦截验证，允许提交）
                if (CollectionUtil.isNotEmpty(svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSubmitSuperUserNamesCollection()) && svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSubmitSuperUserNamesCollection().contains(svnPreCommitWebHookParam.getAuthor())) {
                    //判断超级用户代码提交注释中是否含有有效关联的工作任务
                    if (CollectionUtil.isNotEmpty(svnPreCommitWebHookParam.getMessage())) {
                        String svnCommitMessage = svnPreCommitWebHookParam.getMessage().get(0).trim();
                        String regex = "(\\[)\\S{1,}(\\]).*?";//commit message正则表达式
                        if (StringUtils.isNotEmpty(svnCommitMessage) && svnCommitMessage.matches(regex)) {
                            String workTaskCode = svnCommitMessage.split("\\]", 2)[0].substring(1);
                            svnPreCommitWebHookParam.setRelatedWorkTask(tblDevTaskMapper.selectByDevTaskCode(workTaskCode));
                        }
                    }
                    return ResultDataDTO.SUCCESS("提交验证成功");
                }
                logger.info("BBBBBBBBBB");
                //3.检测所提交代码对应的系统配置的代码提交开关是否开启（开启则做后续校验，未开启不允许提交）
                if (svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSubmitStatus() != null && svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSubmitStatus() == 1) {
                    //4.判断当前提交用户是否在代码提交用户清单中（代码提交用户清单为空不做该步拦截校验）
                    if (CollectionUtil.isNotEmpty(svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSubmitUserNamesCollection())) {
                        if (!svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSubmitUserNamesCollection().contains(svnPreCommitWebHookParam.getAuthor())) {
                            return ResultDataDTO.FAILURE("提交失败，您未处于代码提交用户清单中！请联系配置管理员");
                        }
                    }
                    String workTaskCode;
                    //5.校验svn代码提交备注信息是否符合格式
                    if (CollectionUtil.isNotEmpty(svnPreCommitWebHookParam.getMessage())) {
                        String svnCommitMessage = svnPreCommitWebHookParam.getMessage().get(0).trim();
                        if (StringUtils.isEmpty(svnCommitMessage)) {
                            return ResultDataDTO.FAILURE("提交失败，提交信息不符合格式(正确格式:[工作任务编号]XXX)！请点击工作任务界面复制按钮，将信息粘贴进提交信息");
                        }
                        String regex = "(\\[)\\S{1,}(\\]).*?";//commit message正则表达式
                        if (!svnCommitMessage.matches(regex)) {
                            return ResultDataDTO.FAILURE("提交失败，提交信息不符合格式(正确格式:[工作任务编号]XXX)！请点击工作任务界面复制按钮，将信息粘贴进提交信息");
                        }
                        workTaskCode = svnCommitMessage.split("\\]", 2)[0].substring(1);
                    } else {
                        return ResultDataDTO.FAILURE("提交失败，提交信息不允许为空！请点击工作任务界面复制按钮，将信息粘贴进提交信息");
                    }
                    svnPreCommitWebHookParam.setRelatedWorkTask(tblDevTaskMapper.selectByDevTaskCode(workTaskCode));
                    //6.检测svn提交备注信息中是否可以获取关联的工作任务
                    if (svnPreCommitWebHookParam.getRelatedWorkTask() == null) {
                        return ResultDataDTO.FAILURE("提交失败，未找到关联的工作任务");
                    }
                    //7.检测关联工作任务的开发人与当前代码提交人是否一致
                    if (!StringUtils.equals(svnPreCommitWebHookParam.getRelatedWorkTask().getUserScmAccount(), svnPreCommitWebHookParam.getAuthor())) {
                        return ResultDataDTO.FAILURE("提交失败，当前提交人与已关联工作任务中的开发人员不匹配");
                    }
                    //8.检测关联工作任务的任务状态是否为开发中
                    if (svnPreCommitWebHookParam.getRelatedWorkTask().getDevTaskStatus() == null || !svnPreCommitWebHookParam.getRelatedWorkTask().getDevTaskStatus().equals(2)) {
                        return ResultDataDTO.FAILURE("提交失败，当前已关联工作任务状态为" + CommonUtil.getDictValueName("TBL_DEV_TASK_DEV_TASK_STATUS", String.valueOf(svnPreCommitWebHookParam.getRelatedWorkTask().getDevTaskStatus()), null) + ",工作任务状态只有为开发中才能提交代码");
                    }
                    svnPreCommitWebHookParam.setRelatedDevTask(tblRequirementFeatureMapper.getRequirementFeatureByDevTaskCode(workTaskCode));
                    //9.检测关联工作任务对应的开发任务是否存在
                    if (svnPreCommitWebHookParam.getRelatedDevTask() == null) {
                        return ResultDataDTO.FAILURE("提交失败，无法通过已关联工作任务找到对应的开发任务");
                    }
                    //10.检测关联的系统配置的系统版本号与关联的开发任务的系统版本号是否一致(系统配置的系统版本号存在才进行该步骤检测)
                   /* if (svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSystemVersionId() != null && !CommonUtil.LongCompare(svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSystemVersionId(), svnPreCommitWebHookParam.getRelatedDevTask().getSystemVersionId())) {
                        String branchVersionName = StringUtils.isEmpty(svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSystemVersionName()) ? "" : svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSystemVersionName();
                        String devTaskVersionName = StringUtils.isEmpty(svnPreCommitWebHookParam.getRelatedDevTask().getSystemVersionName()) ? "" : svnPreCommitWebHookParam.getRelatedDevTask().getSystemVersionName();
                        if(!branchVersionName.equals(devTaskVersionName)) {
                            return ResultDataDTO.FAILURE("提交失败，分支版本号(" + branchVersionName + ")和开发任务版本号(" + devTaskVersionName + ")对应不上！注意切换！");
                        }
                    }*/
                  //10.检测关联的系统配置的系统版本号与关联的开发任务的系统版本号是否一致(系统配置的系统版本号存在才进行该步骤检测)
                     if (svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSystemVersionId() != null ) {
                         String branchVersionName = StringUtils.isEmpty(svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSystemVersionName()) ? "" : svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getSystemVersionName();
                         String devTaskVersionName = StringUtils.isEmpty(svnPreCommitWebHookParam.getRelatedDevTask().getSystemVersionName()) ? "" : svnPreCommitWebHookParam.getRelatedDevTask().getSystemVersionName();
                         if(!branchVersionName.equals(devTaskVersionName)) {
                             return ResultDataDTO.FAILURE("提交失败，分支版本号(" + branchVersionName + ")和开发任务版本号(" + devTaskVersionName + ")对应不上！注意切换！");
                         }
                     }
                    
                    //11.检测关联的系统配置的投产窗口与关联的开发任务的投产窗口是否一致()系统配置的投产窗口存在才进行该步骤检测)
                    if (svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getCommissioningWindowId() != null && !CommonUtil.LongCompare(svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getCommissioningWindowId(), svnPreCommitWebHookParam.getRelatedDevTask().getCommissioningWindowId())) {
                        String branchCommissionWindowName = StringUtils.isEmpty(svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getCommissioningWindowName()) ? "" : svnPreCommitWebHookParam.getRelatedSystemScmSubmit().getCommissioningWindowName();
                        String devTaskCommissionWindowName = StringUtils.isEmpty(svnPreCommitWebHookParam.getRelatedDevTask().getCommissioningWindowName()) ? "" : svnPreCommitWebHookParam.getRelatedDevTask().getCommissioningWindowName();
                        return ResultDataDTO.FAILURE("提交失败，分支投产窗口(" + branchCommissionWindowName + ")和开发任务投产窗口(" + devTaskCommissionWindowName + ")对应不上！注意切换！");
                    }
                } else {
                    return ResultDataDTO.FAILURE("提交失败，该分支代码提交已关闭！请联系配置管理员");
                }
            }
            logger.info("CCCCCCCCCCCCCCC");
            return ResultDataDTO.SUCCESS("提交验证成功");
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return ResultDataDTO.ABNORMAL("提交失败，提交验证出现异常,请联系系统管理员！");
        }
    }

    /**
     * 
    * @Title: gitLabPreReceiveHookCheckMainProcess
    * @Description: git提交前校验主流程
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
                //1.检测所提交的代码能否对应平台中已登记的代码提交配置（未找到则不做后续拦截验证，允许提交）
                if (gitLabPreReceiveHookParam.getRelatedSystemScmSubmit() != null) {
                    //2.判断当前提交用户是否为超级用户（是超级用户不做后续拦截验证，允许提交）
                    if (CollectionUtil.isNotEmpty(gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getSubmitSuperUserNamesCollection()) &&
                            gitLabPreReceiveHookParam.getCommitUser() != null &&
                            gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getSubmitSuperUserNamesCollection().contains(gitLabPreReceiveHookParam.getCommitUser().getUsername())) {
                        if (StringUtils.isNotEmpty(gitLabPreReceiveHookParam.getCommitMessage())) {
                            String commitTaskMessage = gitLabPreReceiveHookParam.getCommitMessage().trim();
                            String regex = "(\\[)\\S{1,}(\\]).*?";//commit message正则表达式
                            if (StringUtils.isNotEmpty(commitTaskMessage) && commitTaskMessage.matches(regex)) {
                                String workTaskCode = commitTaskMessage.split("\\]", 2)[0].substring(1);
                                gitLabPreReceiveHookParam.setRelatedWorkTask(tblDevTaskMapper.selectByDevTaskCode(workTaskCode));
                            }
                        }
                        return ResultDataDTO.SUCCESS("提交验证成功");
                    }
                    //3.检测所提交代码对应的系统配置的代码提交开关是否开启（开启则做后续校验，未开启不允许提交）
                    if (gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getSubmitStatus() != null && gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getSubmitStatus() == 1) {
                        //4.判断当前提交用户是否在代码提交用户清单中（代码提交用户清单为空不做该步拦截校验）
                        if (CollectionUtil.isNotEmpty(gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getSubmitUserNamesCollection())) {
                            if (gitLabPreReceiveHookParam.getCommitUser() == null ||
                                    !gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getSubmitUserNamesCollection().contains(gitLabPreReceiveHookParam.getCommitUser().getUsername())) {
                                return ResultDataDTO.FAILURE("提交失败，您未处于代码提交用户清单中！请联系配置管理员");
                            }
                        }
                        String workTaskCode;
                        //5.校验gitlab代码提交备注信息是否符合格式
                        if (StringUtils.isNotEmpty(gitLabPreReceiveHookParam.getCommitMessage())) {
                            String commitTaskMessage = gitLabPreReceiveHookParam.getCommitMessage().trim();
                            if (StringUtils.isEmpty(commitTaskMessage)) {
                                return ResultDataDTO.FAILURE("提交失败，提交信息不符合格式(正确格式:[工作任务编号]XXX)！请点击工作任务界面复制按钮，将信息粘贴进提交信息");
                            }
                            String regex = "(\\[)\\S{1,}(\\]).*?";//commit message正则表达式
                            if (!commitTaskMessage.matches(regex)) {
                                return ResultDataDTO.FAILURE("提交失败，提交信息不符合格式(正确格式:[工作任务编号]XXX)！请点击工作任务界面复制按钮，将信息粘贴进提交信息");
                            }
                            workTaskCode = commitTaskMessage.split("\\]", 2)[0].substring(1);
                        } else {
                            return ResultDataDTO.FAILURE("提交失败，提交信息不允许为空！请点击工作任务界面复制按钮，将信息粘贴进提交信息");
                        }
                        gitLabPreReceiveHookParam.setRelatedWorkTask(tblDevTaskMapper.selectByDevTaskCode(workTaskCode));
                        //6.检测gitlab提交备注信息中是否可以获取关联的工作任务
                        if (gitLabPreReceiveHookParam.getRelatedWorkTask() == null) {
                            return ResultDataDTO.FAILURE("提交失败，未找到关联的工作任务");
                        }
                        //7.检测关联工作任务的开发人与当前代码提交人是否一致
                        if (gitLabPreReceiveHookParam.getCommitUser() == null ||
                                !StringUtils.equals(gitLabPreReceiveHookParam.getRelatedWorkTask().getUserScmAccount(), gitLabPreReceiveHookParam.getCommitUser().getUsername())) {
                            return ResultDataDTO.FAILURE("提交失败，当前提交人与已关联工作任务中的开发人员不匹配");
                        }
                        //8.检测关联工作任务的任务状态是否为开发中
                        if (gitLabPreReceiveHookParam.getRelatedWorkTask().getDevTaskStatus() == null || !gitLabPreReceiveHookParam.getRelatedWorkTask().getDevTaskStatus().equals(2)) {
                            return ResultDataDTO.FAILURE("提交失败，当前已关联工作任务状态为" + CommonUtil.getDictValueName("TBL_DEV_TASK_DEV_TASK_STATUS", String.valueOf(gitLabPreReceiveHookParam.getRelatedWorkTask().getDevTaskStatus()), null) + ",工作任务状态只有为开发中才能提交代码");
                        }
                        gitLabPreReceiveHookParam.setRelatedDevTask(tblRequirementFeatureMapper.getRequirementFeatureByDevTaskCode(workTaskCode));
                        //9.检测关联工作任务对应的开发任务是否存在
                        if (gitLabPreReceiveHookParam.getRelatedDevTask() == null) {
                            return ResultDataDTO.FAILURE("提交失败，无法通过已关联工作任务找到对应的开发任务");
                        }
                        //10.检测关联的系统配置的系统版本号与关联的开发任务的系统版本号是否一致(系统配置的系统版本号存在才进行该步骤检测)
                        if (gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getSystemVersionId() != null && !CommonUtil.LongCompare(gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getSystemVersionId(), gitLabPreReceiveHookParam.getRelatedDevTask().getSystemVersionId())) {
                            String branchVersionName = StringUtils.isEmpty(gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getSystemVersionName()) ? "" : gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getSystemVersionName();
                            String devTaskVersionName = StringUtils.isEmpty(gitLabPreReceiveHookParam.getRelatedDevTask().getSystemVersionName()) ? "" : gitLabPreReceiveHookParam.getRelatedDevTask().getSystemVersionName();
                            return ResultDataDTO.FAILURE("提交失败，分支版本号(" + branchVersionName + ")和开发任务版本号(" + devTaskVersionName + ")对应不上！注意切换！");
                        }
                        //11.检测关联的系统配置的投产窗口与关联的开发任务的投产窗口是否一致()系统配置的投产窗口存在才进行该步骤检测)
                        if (gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getCommissioningWindowId() != null && !CommonUtil.LongCompare(gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getCommissioningWindowId(), gitLabPreReceiveHookParam.getRelatedDevTask().getCommissioningWindowId())) {
                            String branchCommissionWindowName = StringUtils.isEmpty(gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getCommissioningWindowName()) ? "" : gitLabPreReceiveHookParam.getRelatedSystemScmSubmit().getCommissioningWindowName();
                            String devTaskCommissionWindowName = StringUtils.isEmpty(gitLabPreReceiveHookParam.getRelatedDevTask().getCommissioningWindowName()) ? "" : gitLabPreReceiveHookParam.getRelatedDevTask().getCommissioningWindowName();
                            return ResultDataDTO.FAILURE("提交失败，分支投产窗口(" + branchCommissionWindowName + ")和开发任务投产窗口(" + devTaskCommissionWindowName + ")对应不上！注意切换！");
                        }
                    } else {
                        return ResultDataDTO.FAILURE("提交失败，该分支代码提交已关闭！请联系配置管理员");
                    }
                }
            }
            return ResultDataDTO.SUCCESS("提交验证成功");
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return ResultDataDTO.ABNORMAL("提交失败，提交验证出现异常,请联系系统管理员！");
        }
    }

    /**
     * 
    * @Title: svnPreCommitHookCheck
    * @Description: 提交前校验，
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
        	//校验上一次任务有无完成
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
     * @Description svn pre-commit钩子验证前准备，获取SVN的超级管理员和密码信息
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
     * @Description gitlab pre-receive钩子验证前准备
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
    * @Description: 处理post-receive前操作，获取gitlab的相管信息，如配置信息，项目信息，提交信息
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
     * @Description svn pre-commit验证成功后操作
     * @MethodName doAfterSvnPreCommitHookCheckSuccess
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/5/6 18:47
     */
    @Override
    @Transactional(readOnly = true)
    public void doAfterSvnPreCommitHookCheckSuccess(SvnPreCommitWebHookParam svnPreCommitWebHookParam, List<Map<String, String>> fileCollection) {
        //将关联工作任务id放入redis
        if (svnPreCommitWebHookParam.getRelatedWorkTask() != null && svnPreCommitWebHookParam.getRelatedWorkTask().getId() != null) {
            redisUtils.setForHash("svnRelatedTaskId", svnPreCommitWebHookParam.getAuthor() + "_" + svnPreCommitWebHookParam.getTransaction(), svnPreCommitWebHookParam.getRelatedWorkTask().getId());
        }
        //将被更改文件信息放入redis
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
     * @Description 处理svn的post-commit的钩子请求
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
            //只有开发任务id不为空才能进行相关数据入库操作
            if (taskId != null) {
                tblDevTaskScm.setDevTaskId(taskId);
                //获取关联开发任务
                Long requirementFeatureId = tblRequirementFeatureMapper.getRequirementFeatureIdByDevTaskId(tblDevTaskScm.getDevTaskId());
                if (requirementFeatureId != null) {
                    //插入时间线记录
                    devTaskService.updateReqFeatureTimeTrace(JsonUtil.toJson(new HashMap<String, Object>() {{
                        put("requirementFeatureId", requirementFeatureId);
                        put("codeFirstCommitTime", new Timestamp(svnPostCommitWebHookParam.getCommitDate().getTime()));
                    }}));
                }
                //通过用户名找id
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
                //1.插入或更新工作任务代码关联表
                tblDevTaskScm.setScmType(Constants.SCM_TYPE_SVN);
                tblDevTaskScm.setCommitNumber(svnPostCommitWebHookParam.getRevision());
                tblDevTaskScm.setCommitMassage(StringUtils.join(svnPostCommitWebHookParam.getMessage(), "\n"));
                tblDevTaskScm.setStatus(tblDevTaskScm.DEL_FLAG_NORMAL);
                tblDevTaskScm.setCreateDate(new Timestamp(new Date().getTime()));
                tblDevTaskScm.setLastUpdateDate(tblDevTaskScm.getCreateDate());
                tblDevTaskScmMapper.insertOrUpdateDevTaskScm(tblDevTaskScm);

                //2.插入代码提交历史记录表
                tblDevTaskScmHistory.setDevTaskId(taskId);
                tblDevTaskScmHistory.setScmType(Constants.SCM_TYPE_SVN);
                tblDevTaskScmHistory.setCommitNumber(svnPostCommitWebHookParam.getRevision());
                tblDevTaskScmHistory.setCommitMassage(tblDevTaskScm.getCommitMassage());
                tblDevTaskScmHistory.setStatus(tblDevTaskScmHistory.DEL_FLAG_NORMAL);
                tblDevTaskScmHistory.setCreateDate(new Timestamp(new Date().getTime()));
                tblDevTaskScmHistory.setLastUpdateDate(tblDevTaskScmHistory.getCreateDate());
                tblDevTaskScmHistoryMapper.insertDevTaskScmHistory(tblDevTaskScmHistory);

                //3.插入或更新开发任务文件提交表
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
            return "提交成功";
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "提交代码异常，请联系系统管理员！";
        } finally {
            redisUtils.delForHash("svnCommmitFile", svnPostCommitWebHookParam.getAuthor() + "_" + svnPostCommitWebHookParam.getTransaction());
            redisUtils.delForHash("svnRelatedTaskId", svnPostCommitWebHookParam.getAuthor() + "_" + svnPostCommitWebHookParam.getTransaction());
        }
    }

    /**
     * 
    * @Title: handleGitLabPostReceiveHookRequest
    * @Description: gitlab提交中校验请求
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
                //获取关联开发任务
                Long requirementFeatureId = tblRequirementFeatureMapper.getRequirementFeatureIdByDevTaskId(tblDevTaskScm.getDevTaskId());
                if (requirementFeatureId != null && gitLabPostReceiveHookParam.getCommitInfo() != null) {
                    //插入时间线记录
                    devTaskService.updateReqFeatureTimeTrace(JsonUtil.toJson(new HashMap<String, Object>() {{
                        put("requirementFeatureId", requirementFeatureId);
                        put("codeFirstCommitTime", new Timestamp(gitLabPostReceiveHookParam.getCommitInfo().getCommittedDate().getTime()));
                    }}));
                }
                //通过用户名找id
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
                //插入或更新工作任务代码关联表
                tblDevTaskScm.setScmType(Constants.SCM_TYPE_GIT);
                tblDevTaskScm.setCommitNumber(gitLabPostReceiveHookParam.getNowCommitId());
                tblDevTaskScm.setCommitMassage(gitLabPostReceiveHookParam.getCommitInfo() == null ? null : gitLabPostReceiveHookParam.getCommitInfo().getMessage());
                tblDevTaskScm.setCreateDate(new Timestamp(new Date().getTime()));
                tblDevTaskScm.setLastUpdateDate(tblDevTaskScm.getCreateDate());
                tblDevTaskScmMapper.insertOrUpdateDevTaskScm(tblDevTaskScm);
                //插入代码提交历史记录表
                tblDevTaskScmHistory.setDevTaskId(taskId);
                tblDevTaskScmHistory.setScmType(Constants.SCM_TYPE_GIT);
                tblDevTaskScmHistory.setCommitNumber(gitLabPostReceiveHookParam.getNowCommitId());
                tblDevTaskScmHistory.setCommitMassage(tblDevTaskScm.getCommitMassage());
                tblDevTaskScmHistory.setStatus(tblDevTaskScmHistory.DEL_FLAG_NORMAL);
                tblDevTaskScmHistory.setCreateDate(new Timestamp(new Date().getTime()));
                tblDevTaskScmHistory.setLastUpdateDate(tblDevTaskScmHistory.getCreateDate());
                tblDevTaskScmHistoryMapper.insertDevTaskScmHistory(tblDevTaskScmHistory);
                //判断是否需要代码评审
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
            return "提交成功";
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return "提交代码异常，请联系系统管理员！";
        } finally {
            redisUtils.delForHash("gitLabRelatedTaskId", String.valueOf(gitLabPostReceiveHookParam.getProjectId()) + "_" + gitLabPostReceiveHookParam.getNowCommitId());
            redisUtils.delForHash("gitLabRelatedSystemId", String.valueOf(gitLabPostReceiveHookParam.getProjectId()) + "_" + gitLabPostReceiveHookParam.getNowCommitId());
        }
    }

    private List<Future<ResultDataDTO>> getInitChangedFilesFutureTask(SvnPreCommitWebHookParam svnPreCommitWebHookParam) {
        //根据启用的线程数量分解更改文件的集合
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
        //根据启用的线程数量分解被删除文件的集合
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
    * @Description: 批量处理某次工作任务提交的文件信息
    * @author author
    * @param allCommitFiles 所有提交的文件
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
     * @Description 针对726项目组初始化提交文件（附带文件中包含数据库脚本文件的校验）
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
                    //过滤文件夹
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
        //检测通配符配置项
        if (svnPreCommitWebHookParam.getCheckSystemScmSubmitRegexes() &&
                CollectionUtil.isNotEmpty(svnPreCommitWebHookParam.getRelatedSystemScmSubmitRegexes())) {
            ResultDataDTO resultDataDTO = this.checkFileSubmitRegex(svnPreCommitWebHookParam, filePath);
            if (!StringUtils.equals(resultDataDTO.getResCode(), ResultDataDTO.DEFAULT_SUCCESS_CODE)) {
                return resultDataDTO;
            }
        }
        //检测726项目组DB脚本存放路径
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
        return ResultDataDTO.SUCCESS("提交验证成功");
    }

    private ResultDataDTO dealDirResult(SvnPreCommitWebHookParam svnPreCommitWebHookParam, String dirPath, String copyFromPath, String copyToPath, String type, Long revision,
                                        List<Map<String, String>> fileCollection) {
        int executeWaiteTime = 0;
        //获取文件夹下的所有文件
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
        return ResultDataDTO.SUCCESS("提交验证成功");
    }

    /**
     * @param svnPreCommitWebHookParam
     * @param filePath
     * @param type
     * @return cn.pioneeruniverse.common.dto.ResultDataDTO
     * @Description 针对726项目的db脚本是否符合规则
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
                //关联开发任务的系统版本名称不为空才开始后续校验
                if (StringUtils.isNotEmpty(svnPreCommitWebHookParam.getRelatedDevTask().getSystemVersionName())) {
                    //子目录的文件名必须与当前开发任务的系统版本一致
                    if (!StringUtils.equals(sonFilePath, svnPreCommitWebHookParam.getRelatedDevTask().getSystemVersionName())) {
                        svnPreCommitWebHookParam.setInitDelFilesEnd(true);
                        svnPreCommitWebHookParam.setInitChangedFilesEnd(true);
                        return ResultDataDTO.FAILURE("数据库脚本路径不对，请正确使用文件夹" + svnPreCommitWebHookParam.getRelatedDevTask().getSystemVersionName());
                    }
                    //相关操作不能为修改
                    if (StringUtils.equals(type, Constants.UPDATE_FILE_CONTENT) ||
                            StringUtils.equals(type, Constants.UPDATE_FILE_ATTR) ||
                            StringUtils.equals(type, Constants.UPDATE_FILE_CONTENT_ATTR)) {
                        svnPreCommitWebHookParam.setInitDelFilesEnd(true);
                        svnPreCommitWebHookParam.setInitChangedFilesEnd(true);
                        return ResultDataDTO.FAILURE("该数据库脚本可能已经发布，禁止修改，可删除并添加更新后的脚本");
                    }
                }
            }
            return ResultDataDTO.SUCCESS("提交验证成功");
        } catch (Exception e) {
            svnPreCommitWebHookParam.setInitDelFilesEnd(true);
            svnPreCommitWebHookParam.setInitChangedFilesEnd(true);
            return ResultDataDTO.ABNORMAL("提交失败，数据库脚本文件提交规则验证出现异常,请联系系统管理员！");
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
                //通配符校验
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
        //防止事务标签在asyc标签下失效
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
        //获取用户可以访问的svn工具记录
    	Integer toolType = 2;//svn
    	//查找该用户所在的系统是否创建了SVN库
        List<Map<String, Object>> svnToolList = tblToolInfoMapper.getMySvnToolInfo(currentUserId,toolType);
        //方便开发调试用
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
                    //判断svn工具协议类型
                    if (accessProtocol == 1) {
                        //svn://协议下密码为明文，只能为英文加数字
                        if (!userScmPassword.matches("^[A-Za-z0-9]+$")) {
                            throw new CredentialException("用户关联的SVN配置中存在svn://协议访问类型，在该访问类型下，密码只能由数字加英文大小写组成");
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
            throw new Exception("用户不存在关联的SVN配置");
        }
    }
    @Override
	public void modifyGitPassword(Long currentUserId, String userScmAccount, String userScmPassword,
			String entryptUserScmPassword)throws Exception {
    	 //获取用户可以访问的git工具记录
    	Integer toolType = 1;//git
    	//查找该项目是否有创建GIT仓库
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
            throw new Exception("用户不存在关联的git配置");
        }
		
	}

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMyProjectSystems(String token) {
        //系统管理员角色
        List<String> roles = (List<String>) CommonUtil.getCurrentUserByToken(token).get("roles");
        if (CollectionUtil.isNotEmpty(roles) && roles.contains("XITONGGUANLIYUAN")) {
            //如果当前用户是系统管理员就查询全部项目
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
    * @Description: 创建代码仓库
    * @author author
    * @param request
    * @param tblSystemScmRepository 仓库信息
    * @throws Exception
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void addNewCodeBase(HttpServletRequest request, TblSystemScmRepository tblSystemScmRepository) throws Exception {
        tblSystemScmRepository.preInsertOrUpdate(request);
        if (tblSystemScmRepository.getScmType().equals(2)) {
            //调用svn接口生成仓库
            SubversionUtils.createSvnRepository(tblSystemScmRepository.getIp(), tblSystemScmRepository.getRepositoryName());
            tblSystemScmRepositoryMapper.insertSystemScmRepository(tblSystemScmRepository);
        } else if (tblSystemScmRepository.getScmType().equals(1)) {
            //获取关联工具信息
            TblToolInfo tblToolInfo = tblToolInfoMapper.selectByPrimaryKey(tblSystemScmRepository.getToolId());
            StringBuilder baseUri = new StringBuilder();
            baseUri.append(Constants.ACCESS_PROTOCOL_MAP.get(tblToolInfo.getAccessProtocol())).append(tblToolInfo.getIp()).append(":").append(tblToolInfo.getPort());
            Project project = new Project();
            project.setName(tblSystemScmRepository.getRepositoryName());
            project.setVisibility(Constants.GitLab.PROJECT_VISIBILITY_PRIVATE);
            project = gitLabWebApiService.createProject(baseUri.toString(), project, tblToolInfo.getGitApiToken());
            if (project != null && project.getId() != null) {
            	//gitlab的项目ID，可以在gitlab页面上看到
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
    * @Description: 保存代码提交配置
    * @author author
    * @param request
    * @param systemScmSubmitConfigList 代码提价配置信息
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
    * @Description: 配置代码正则表达式配置，如.ignore
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
    * @Description: 获取代码提交配置
    * @author author
    * @param systemId 系统id
    * @param scmType 仓库类型1git,2svn
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
    		for (String id : userIds.split(",")) {//去重
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
	* @Description: 获取代码提交配置之通配符
	* @author author
	* @param systemId 系统ID
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
    		for (String id : userIds.split(",")) {//去重
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
    * @Description: 获取gitlab中的Project（仓库）
    * @author author
    * @param systemId 系统id
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
                //通过gitlab的api获取项目
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
    * @Description: 获取各个系统下的git仓库
    * @author author
    * @param systemIds 系统id
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
    * @Description: gitlab关联人员后保存修改
    * @author author
    * @param project gitlab项目
    * @param addUserCollection 需要添加的人员
    * @param delUserCollection 需要删除的人员
    * @param modifyUserCollection 需要修改的人员
    * @return String 修改结果提示
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
                    String username = result.get("username");//用户代码仓库账号  userScmAccount
                    String password = result.get("password");//用户代码仓库密码 userScmPassword
                    User user = gitLabWebApiService.getUserByUserName(project.getBaseUri(), addUser.getUsername(), project.getGitApiToken());
                    if (user == null || user.getId() == null) {
                        user = new User(addUser.getEmail(), username, addUser.getName(), PasswordUtil.decryptForAES(password));
                        user.setSkipConfirmation(true);
                        user = gitLabWebApiService.createUser(project.getBaseUri(), user, project.getGitApiToken());
                    }
                    if (user != null && user.getId() != null) {
                        Member member = new Member(user.getId(), addUser.getGitLabAccessLevel());
                        gitLabWebApiService.addMemberToProject(project.getBaseUri(), member, project.getId(), project.getGitApiToken());
                        msg.append(addUser.getName()).append("添加成功!").append("</br>");
                    }
                } catch (Exception e1) {
                    logger.error("add git member error,cause:" + e1.getMessage());
                    msg.append(addUser.getName()).append("添加失败!").append("</br>");
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
                        msg.append(delUser.getName()).append("删除成功!").append("\n");
                    }
                } catch (Exception e2) {
                    logger.error("del git member error,cause:" + e2.getMessage());
                    msg.append(delUser.getName()).append("删除失败!").append("\n");
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
                        msg.append(modifyUser.getName()).append("修改成功!").append("\n");
                    }
                } catch (Exception e3) {
                    logger.error("modify git member error,cause:" + e3.getMessage());
                    msg.append(modifyUser.getName()).append("修改失败!").append("\n");
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
     * @Description 文件提交通配符检测配置(检测级别:文件)
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
                        return ResultDataDTO.FAILURE("文件路径/" + filePath + "无权限提交，请联系配置管理员");
                    }
                }
            }
            return ResultDataDTO.SUCCESS("提交验证成功");
        } catch (Exception e) {
            svnPreCommitWebHookParam.setInitDelFilesEnd(true);
            svnPreCommitWebHookParam.setInitChangedFilesEnd(true);
            return ResultDataDTO.ABNORMAL("提交失败，通配符配置项验证出现异常,请联系系统管理员！");
        }
    }
    
    /**
     * 
    * @Title: getCodeFilesByDevTaskId
    * @Description: 获取某个工作任务提交的代码文件
    * @author author
    * @param devTaskId 开发工作任务Id
    * @return  Map<String, Object> key:svnFiles  提交的svn文件
    *                                  gitFiles  提交的git文件
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

