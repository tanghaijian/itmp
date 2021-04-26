package cn.pioneeruniverse.dev.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.pioneeruniverse.common.dto.ResultDataDTO;
import cn.pioneeruniverse.common.dto.TblUserInfoDTO;
import cn.pioneeruniverse.common.entity.AjaxModel;
import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.common.gitlab.entity.Project;
import cn.pioneeruniverse.common.gitlab.entity.UserDTO;
import cn.pioneeruniverse.common.subversion.SubversionUtils;
import cn.pioneeruniverse.common.utils.JsonUtil;
import cn.pioneeruniverse.dev.entity.GitLabPostReceiveHookParam;
import cn.pioneeruniverse.dev.entity.GitLabPreReceiveHookParam;
import cn.pioneeruniverse.dev.entity.SvnFileDirectoryStructure;
import cn.pioneeruniverse.dev.entity.SvnPostCommitWebHookParam;
import cn.pioneeruniverse.dev.entity.SvnPreCommitWebHookParam;
import cn.pioneeruniverse.dev.entity.TblSystemScmRepository;
import cn.pioneeruniverse.dev.entity.TblSystemScmSubmit;
import cn.pioneeruniverse.dev.entity.TblSystemScmSubmitRegex;
import cn.pioneeruniverse.dev.entity.TblSystemScmSubmitVo;
import cn.pioneeruniverse.dev.service.systeminfo.ISystemInfoService;
import cn.pioneeruniverse.dev.service.version.VersionService;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: 版本管理restController+钩子，钩子中使用了pre-commit和post-commit
 * @Date: Created in 13:41 2018/12/13
 * @Modified By:
 */
@RestController
@RequestMapping(value = "version")
public class VersionController {
    private final static Logger logger = LoggerFactory.getLogger(VersionController.class);

    @Autowired
    private VersionService versionService;


    @Autowired
    private ISystemInfoService systemInfoService;

    /**
     * @param systemId
     * @return java.util.List<cn.pioneeruniverse.dev.entity.SvnFileDirectoryStructure>
     * @Description 获取当前用户所属项目组的svn代码库文件目录结构
     * @MethodName getSvnFileTree
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/13 16:03
     */
    @RequestMapping(value = "getSvnFileTree", method = RequestMethod.POST)
    public List<SvnFileDirectoryStructure> getSvnFileTree(Long systemId) {
        return versionService.getSvnFileTree(systemId);
    }

    /**
     * @param structure
     * @return cn.pioneeruniverse.common.entity.AjaxModel
     * @Description svn文件目录树展开获取节点数据
     * @MethodName getSvnSonFileTree
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/1/28 9:48
     */
    @RequestMapping(value = "getSvnSonFileTree", method = RequestMethod.POST)
    public AjaxModel getSvnSonFileTree(SvnFileDirectoryStructure structure) {
        try {
            List<SvnFileDirectoryStructure> sonFileStructure = SubversionUtils.getSvnDirEntry(structure.getRepositoryUrl(), structure.getUsername(), structure.getPassword(), structure.getAccessProtocol(), structure.getIp(), structure.getPort(), structure.getPath(), structure.getRepositoryName(), null).getChildren();
            return AjaxModel.SUCCESS(sonFileStructure);
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return AjaxModel.FAIL(e);
        }
    }


    /**
     * @param ip
     * @param repositoryName 仓库名
     * @param path
     * @param request
     * @param response
     * @return cn.pioneeruniverse.common.entity.JqGridPage<cn.pioneeruniverse.common.dto.TblUserInfoDTO>
     * @Description 获取svn上的用户权限
     * @MethodName getSvnUsers
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/21 14:00
     */
    @RequestMapping(value = "getSvnUsers", method = RequestMethod.POST)
    public JqGridPage<TblUserInfoDTO> getSvnUsers(String ip, String repositoryName, String path, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return versionService.getSvnUserAuth(new JqGridPage<TblUserInfoDTO>(request, response), ip, repositoryName, path);
    }

    /**
     * @param project
     * @param request
     * @param response
     * @return cn.pioneeruniverse.common.entity.JqGridPage<cn.pioneeruniverse.common.dto.TblUserInfoDTO>
     * @Description 获取gitlab上的用户及权限
     * @MethodName getGitLabUsers
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/7/4 14:51
     */
    @RequestMapping(value = "getGitLabUsers", method = RequestMethod.POST)
    public JqGridPage<TblUserInfoDTO> getGitLabUsers(Project project, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return versionService.getGitLabUserAuth(new JqGridPage<TblUserInfoDTO>(request, response), project);
    }

    /**
     * @param ip
     * @param repositoryName
     * @param path
     * @param modifyOperates
     * @return java.lang.String
     * @Description 保存对svn的修改：代码库设置中，分派人员权限中的【保存修改】
     * @MethodName saveSvnModify
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/21 14:01
     */
    @RequestMapping(value = "saveSvnModify", method = RequestMethod.POST)
    public AjaxModel saveSvnModify(Integer accessProtocol, String ip, String repositoryName, String path, String modifyOperates) {
        try {
            return AjaxModel.SUCCESS(versionService.saveSvnModify(accessProtocol, ip, repositoryName, path, modifyOperates));
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
     * @param svnPreCommitWebHookParam
     * @return java.lang.String
     * @Description svn的pre-commit钩子请求
     * @MethodName svnWebHookRequest
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/27 10:07
     */
    @RequestMapping(value = "svnPreCommitWebHookRequest", method = RequestMethod.POST)
    public String svnPreCommitWebHookRequest(@RequestBody SvnPreCommitWebHookParam svnPreCommitWebHookParam) {
        return versionService.handleSvnPreCommitWebHookRequest(svnPreCommitWebHookParam);
    }

    /**
     * 
    * @Title: svnPreCommitWebHookFor726Request
    * @Description: 726项目提交校验
    * @author author
    * @param svnPreCommitWebHookParam
    * @return String
     */
    @RequestMapping(value = "svnPreCommitWebHookFor726Request", method = RequestMethod.POST)
    public String svnPreCommitWebHookFor726Request(@RequestBody SvnPreCommitWebHookParam svnPreCommitWebHookParam) {
        svnPreCommitWebHookParam.setCheckDBScriptFileFor726(true);
        return versionService.handleSvnPreCommitWebHookRequest(svnPreCommitWebHookParam);
    }

    /**
     * @param svnPostCommitWebHookParam
     * @return java.lang.String
     * @Description svn的post-commit的钩子请求
     * @MethodName svnPostCommitWebHookRequest
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/1/8 10:52
     */
    @RequestMapping(value = "svnPostCommitWebHookRequest", method = RequestMethod.POST)
    public String svnPostCommitWebHookRequest(@RequestBody SvnPostCommitWebHookParam svnPostCommitWebHookParam) {
        return versionService.handleSvnPostCommitWebHookRequest(svnPostCommitWebHookParam);
    }

    /**
     * 
    * @Title: gitLabPreReceiveHookRequest
    * @Description: pre-receive钩子
    * @author author
    * @param gitLabPreReceiveHookParam
    * @return String
     */
    @RequestMapping(value = "gitLabPreReceiveHookRequest", method = RequestMethod.POST)
    public String gitLabPreReceiveHookRequest(@RequestBody GitLabPreReceiveHookParam gitLabPreReceiveHookParam) {
        return versionService.handleGitLabPreReceiveHookRequest(gitLabPreReceiveHookParam);
    }

    /**
     * 
    * @Title: gitLabPostReceiveHookRequest
    * @Description: post-receive钩子
    * @author author
    * @param gitLabPostReceiveHookParam
    * @return
    * @throws
     */
    @RequestMapping(value = "gitLabPostReceiveHookRequest", method = RequestMethod.POST)
    public String gitLabPostReceiveHookRequest(@RequestBody GitLabPostReceiveHookParam gitLabPostReceiveHookParam) {
        return versionService.handleGitLabPostReceiveHookRequest(gitLabPostReceiveHookParam);
    }
    /**
     * @param userScmPassword 明文
     * @param entryptUserScmPassword aes加密
     * */
    @RequestMapping(value = "modifySvnPassword", method = RequestMethod.POST)
    public ResultDataDTO modifySvnPassword(Long currentUserId, String userScmAccount, String userScmPassword, String entryptUserScmPassword) {
        try {
            versionService.modifySvnPassword(currentUserId, userScmAccount, userScmPassword, entryptUserScmPassword);
            return ResultDataDTO.SUCCESS("修改SVN密码配置文件成功");
        } catch (Exception e) {
            logger.error("修改SVN密码配置文件异常，异常原因：" + e.getMessage(), e);
            return ResultDataDTO.ABNORMAL("修改SVN密码配置文件异常，异常原因：" + e.getMessage());
        }
    }
    /**
     * @param userScmPassword 明文
     * @param entryptUserScmPassword aes加密
     * */
    @RequestMapping(value = "modifyGitPassword", method = RequestMethod.POST)
    public ResultDataDTO modifyGitPassword(Long currentUserId, String userScmAccount, String userScmPassword, String entryptUserScmPassword) {
        try {
            versionService.modifyGitPassword(currentUserId, userScmAccount, userScmPassword, entryptUserScmPassword);
            return ResultDataDTO.SUCCESS("修改git密码成功");
        } catch (Exception e) {
            logger.error("修改SVN密码配置文件异常，异常原因：" + e.getMessage(), e);
            return ResultDataDTO.ABNORMAL("修改git密码异常，异常原因：" + e.getMessage());
        }
    }

    /**
     * @param token
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @Description 获取当前用户所属项目组对应的系统
     * @MethodName getMyProjectSystems
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/5/27 13:38
     */
    @RequestMapping(value = "getMyProjectSystems", method = RequestMethod.POST)
    public List<Map<String, Object>> getMyProjectSystems(String token) {
        return versionService.getMyProjectSystems(token);
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
    @RequestMapping(value = "getSystemScmSubmitConfigs", method = RequestMethod.POST)
    public List<TblSystemScmSubmit> getSystemScmSubmitConfigs(Long systemId, Integer scmType) {
        return versionService.getSystemScmSubmitConfigs(systemId, scmType);
    }

    /**
     * 
    * @Title: getSystemScmSubmitRegexConfigs
    * @Description: 正则表达式配置，一般为代码路径的正则表达式，用于ignore等
    * @author author
    * @param systemId 系统ID
    * @return List<TblSystemScmSubmitRegex>
     */
    @RequestMapping(value = "getSystemScmSubmitRegexConfigs", method = RequestMethod.POST)
    public List<TblSystemScmSubmitRegex> getSystemScmSubmitRegexConfigs(Long systemId) {
        return versionService.getSystemScmSubmitRegexConfigs(systemId);
    }

    
    /**
     * 
    * @Title: getSystemScmSubmitRegexConfigs
    * @Description: 正则表达式配置，一般为代码路径的正则表达式，用于ignore等
    * @author author
    * @param systemId 系统ID
    * @return AjaxModel
     */
    @RequestMapping(value = "getSystemScmSubmitRegexConfigList", method = RequestMethod.POST)
    public AjaxModel getSystemScmSubmitRegexConfigList(Long systemId) {
        try {
            return AjaxModel.SUCCESS(versionService.getSystemScmSubmitRegexConfigs(systemId));
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
     * 
    * @Title: getCodeBaseAddresses
    * @Description: 获取代码仓库地址
    * @author author
    * @param codeBaseType 工具：1:GIT，2:SVN，3:SONAR，4:JENKINS，5:ARTIFACTORY,6:NEXUS
    * @return List<Map<String, Object>>
     */
    @RequestMapping(value = "getCodeBaseAddresses", method = RequestMethod.POST)
    public List<Map<String, Object>> getCodeBaseAddresses(Integer codeBaseType) {
        return versionService.getCodeBaseAddresses(codeBaseType);
    }

    /**
     * 
    * @Title: addNewCodeBase
    * @Description: 添加代码库
    * @author author
    * @param request
    * @param tblSystemScmRepository 仓库信息
    * @return AjaxModel
     */
    @RequestMapping(value = "addNewCodeBase", method = RequestMethod.POST)
    public AjaxModel addNewCodeBase(HttpServletRequest request, TblSystemScmRepository tblSystemScmRepository) {
        try {
            versionService.addNewCodeBase(request, tblSystemScmRepository);
            return AjaxModel.SUCCESS(tblSystemScmRepository);
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
     * 
    * @Title: getCodeBases
    * @Description: 获取某个系统，某种工具下的代码仓库
    * @author author
    * @param systemId 系统
    * @param scmType 1git，2svn
    * @return AjaxModel
     */
    @RequestMapping(value = "getCodeBases", method = RequestMethod.POST)
    public AjaxModel getCodeBases(Long systemId, Integer scmType) {
        try {
            return AjaxModel.SUCCESS(versionService.getCodeBases(systemId, scmType));
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
     * 
    * @Title: getSystemVersions
    * @Description: 获取系统版本
    * @author author
    * @param systemId 系统ID
    * @return AjaxModel
     */
    @RequestMapping(value = "getSystemVersions", method = RequestMethod.POST)
    public AjaxModel getSystemVersions(Long systemId) {
        try {
            return AjaxModel.SUCCESS(versionService.getSystemVersions(systemId));
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
     * 
    * @Title: getCommissioningWindows
    * @Description: 获取投产窗口
    * @author author
    * @return AjaxModel
     */
    @RequestMapping(value = "getCommissioningWindows", method = RequestMethod.POST)
    public AjaxModel getCommissioningWindows() {
        try {
            return AjaxModel.SUCCESS(systemInfoService.getAllTblCommissioningWindow());
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
     * 
    * @Title: saveCodeSubmitConfig
    * @Description: 保存代码提交配置
    * @author author
    * @param request
    * @param tblSystemScmSubmitVo
    * @return AjaxModel
     */
    @RequestMapping(value = "saveCodeSubmitConfig", method = RequestMethod.POST)
    public AjaxModel saveCodeSubmitConfig(HttpServletRequest request, @RequestBody TblSystemScmSubmitVo tblSystemScmSubmitVo) {
        try {
        	//代码提交配置：配置项
            versionService.saveCodeSubmitConfig(request, tblSystemScmSubmitVo.getTblSystemScmSubmitList());
            //代码提交配置：通配符
            versionService.saveCodeSubmitRegexConfig(request, tblSystemScmSubmitVo.getTblSystemScmSubmitRegexList());
            return AjaxModel.SUCCESS();
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
     * 
    * @Title: delCodeSubmitConfig
    * @Description: 删除代码提交配置：配置项
    * @author author
    * @param request
    * @param tblSystemScmSubmit 需要删除的项
    * @return AjaxModel
     */
    @RequestMapping(value = "delCodeSubmitConfig", method = RequestMethod.POST)
    public AjaxModel delCodeSubmitConfig(HttpServletRequest request, TblSystemScmSubmit tblSystemScmSubmit) {
        try {
            versionService.delCodeSubmitConfig(request, tblSystemScmSubmit);
            return AjaxModel.SUCCESS();
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
     * 
    * @Title: delCodeSubmitRegexConfig
    * @Description: 删除代码提交配置：通配符
    * @author author
    * @param request
    * @param tblSystemScmSubmitRegex 通配符项
    * @return AjaxModel
     */
    @RequestMapping(value = "delCodeSubmitRegexConfig", method = RequestMethod.POST)
    public AjaxModel delCodeSubmitRegexConfig(HttpServletRequest request, TblSystemScmSubmitRegex tblSystemScmSubmitRegex) {
        try {
            versionService.delCodeSubmitRegexConfig(request, tblSystemScmSubmitRegex);
            return AjaxModel.SUCCESS();
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
     * 
    * @Title: getSystemGitLabProjects
    * @Description: 获取某个系统下的所有git项目，通过保存的git配置，利用gitlab 的api查找
    * @author author
    * @param systemId 系统ID
    * @return AjaxModel
     */
    @RequestMapping(value = "getSystemGitLabProjects", method = RequestMethod.POST)
    public List<Project> getSystemGitLabProjects(Long systemId) {
        try {
            return versionService.getSystemGitLabProjects(systemId);
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return null;
        }
    }
    
    /**
     * 
    * @Title: getSystemGitLabProjectsWithSystemIds
    * @Description: 通过多个项目ID获取git的项目：通过保存的git配置，利用gitlab 的api查找
    * @author author
    * @param systemIds 多个系统ID
    * @return AjaxModel
     */
    @RequestMapping(value = "getSystemGitLabProjectsWithSystemIds", method = RequestMethod.POST)
    public List<Project> getSystemGitLabProjectsWithSystemIds(String systemIds) {
        try {
            return versionService.getSystemGitLabProjectsWithSystemIds(systemIds);
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return null;
        }
    }

    /**
     * 
    * @Title: saveGitLabModify
    * @Description: gitlab人员保存修改
    * @author author
    * @param project git项目
    * @param addUsers 添加人员操作
    * @param delUsers 移出人员操作
    * @param modifyUsers 修改人员操作
    * @return AjaxModel
     */
    @RequestMapping(value = "saveGitLabModify", method = RequestMethod.POST)
    public AjaxModel saveGitLabModify(Project project, @RequestParam("addUsers") String addUsers, @RequestParam("delUsers") String delUsers, @RequestParam("modifyUsers") String modifyUsers) {
        try {
        	//添加的用户
            List<UserDTO> addUserList = JsonUtil.fromJson(addUsers, JsonUtil.createCollectionType(ArrayList.class, UserDTO.class));
            //删除的用户
            List<UserDTO> delUserList = JsonUtil.fromJson(delUsers, JsonUtil.createCollectionType(ArrayList.class, UserDTO.class));
            //修改的用户
            List<UserDTO> modifyUserList = JsonUtil.fromJson(modifyUsers, JsonUtil.createCollectionType(ArrayList.class, UserDTO.class));
            String msg = versionService.saveGitLabModify(project, addUserList, delUserList, modifyUserList);
            return AjaxModel.SUCCESS(msg);
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
     * 
    * @Title: findScmRepositoryBySystemId
    * @Description: 获取某个系统的代码仓库
    * @author author
    * @param systemId 系统id
    * @return AjaxModel
     */
    @RequestMapping(value = "findScmRepositoryBySystemId", method = RequestMethod.POST)
    public AjaxModel findScmRepositoryBySystemId(Long systemId) {
        try {
            return AjaxModel.SUCCESS(versionService.findScmRepositoryBySystemId(systemId));
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return AjaxModel.FAIL(e);
        }
    }
    
    /**
     * 
    * @Title: getCodeFilesByDevTaskId
    * @Description: 获取某次开发工作任务提交的文件
    * @author author
    * @param devTaskId 开发工作任务
    * @return Map<String,Object> key:svnFiles  提交的svn文件
    *                                  gitFiles  提交的git文件
     */
    @RequestMapping(value = "getCodeFilesByDevTaskId", method = RequestMethod.POST)
    public Map<String, Object> getCodeFilesByDevTaskId(@RequestParam Long devTaskId) {
        return versionService.getCodeFilesByDevTaskId(devTaskId);
    }
}
