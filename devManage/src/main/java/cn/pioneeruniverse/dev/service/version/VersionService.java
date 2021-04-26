package cn.pioneeruniverse.dev.service.version;

import cn.pioneeruniverse.common.dto.ResultDataDTO;
import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.common.dto.TblUserInfoDTO;
import cn.pioneeruniverse.common.gitlab.entity.Project;
import cn.pioneeruniverse.common.gitlab.entity.UserDTO;
import cn.pioneeruniverse.dev.entity.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 18:04 2018/12/13
 * @Modified By:
 */
public interface VersionService {

    JqGridPage<TblUserInfoDTO> getSvnUserAuth(JqGridPage<TblUserInfoDTO> jqGridPage, String ip, String repositoryName, String path) throws Exception;

    JqGridPage<TblUserInfoDTO> getGitLabUserAuth(JqGridPage<TblUserInfoDTO> jqGridPage, Project project) throws Exception;

    String saveSvnModify(Integer accessProtocol, String ip, String repositoryName, String path, String modifyOperates) throws Exception;

    List<Map<String, Object>> getMySvnRepositoryLocationInfo(Long systemId);

    List<SvnFileDirectoryStructure> getSvnFileTree(Long systemId);

    String handleGitLabPreReceiveHookRequest(GitLabPreReceiveHookParam gitLabPreReceiveHookParam);

    String handleSvnPreCommitWebHookRequest(SvnPreCommitWebHookParam svnPreCommitWebHookParam);

    ResultDataDTO svnPreCommitHookCheckMainProcess(SvnPreCommitWebHookParam svnPreCommitWebHookParam);

    ResultDataDTO gitLabPreReceiveHookCheckMainProcess(GitLabPreReceiveHookParam gitLabPreReceiveHookParam);

    ResultDataDTO svnPreCommitHookCheck(List<Future<ResultDataDTO>> futureList, SvnPreCommitWebHookParam svnPreCommitWebHookParam);

    void doBeforeSvnPreCommitHookCheck(SvnPreCommitWebHookParam svnPreCommitWebHookParam);

    void doBeforeGitLabPreReceiveHookCheck(GitLabPreReceiveHookParam gitLabPreReceiveHookParam) throws URISyntaxException;

    void doBeforeGitLabPostReceiveHookHandle(GitLabPostReceiveHookParam gitLabPostReceiveHookParam) throws URISyntaxException;

    void doAfterSvnPreCommitHookCheckSuccess(SvnPreCommitWebHookParam svnPreCommitWebHookParam, List<Map<String, String>> fileCollection);

    String handleSvnPostCommitWebHookRequest(SvnPostCommitWebHookParam svnPostCommitWebHookParam);

    String handleGitLabPostReceiveHookRequest(GitLabPostReceiveHookParam gitLabPostReceiveHookParam);

    Future<ResultDataDTO> initChangedFiles(List<String> changedFiles, SvnPreCommitWebHookParam svnPreCommitWebHookParam);

    Future<ResultDataDTO> initChangedSonFiles(List<String> sonFiles, String type, String copyFromPath, String copyToPath, SvnPreCommitWebHookParam svnPreCommitWebHookParam);

    Future<Boolean> batchInsertOrUpdateDevTaskScmFile(List<Map<String, String>> commitFiles, TblDevTaskScmFile tblDevTaskScmFile);

    Future<Boolean> batchInsertOrUpdateDevTaskScmGitFile(List<String> commitFiles, TblDevTaskScmGitFile tblDevTaskScmGitFile);

    void insertOrUpdateDevTaskScmFile(List<Map<String, String>> commitFiles, TblDevTaskScmFile tblDevTaskScmFile);

    void insertOrUpdateDevTaskScmGitFile(List<String> commitFiles, TblDevTaskScmGitFile tblDevTaskScmGitFile);

    void modifySvnPassword(Long currentUserId, String userScmAccount, String userScmPassword, String entryptUserScmPassword) throws Exception;

    List<Map<String, Object>> getMyProjectSystems(String token);

    List<Map<String, Object>> getCodeBaseAddresses(Integer codeBaseType);

    void addNewCodeBase(HttpServletRequest request, TblSystemScmRepository tblSystemScmRepository) throws Exception;

    List<Map<String, Object>> getCodeBases(Long systemId, Integer scmType);

    List<TblSystemVersion> getSystemVersions(Long systemId);

    void saveCodeSubmitConfig(HttpServletRequest request, List<TblSystemScmSubmit> systemScmSubmitConfigList);

    void saveCodeSubmitRegexConfig(HttpServletRequest request, List<TblSystemScmSubmitRegex> systemScmSubmitRegexList);

    void delCodeSubmitConfig(HttpServletRequest request, TblSystemScmSubmit tblSystemScmSubmit);

    void delCodeSubmitRegexConfig(HttpServletRequest request, TblSystemScmSubmitRegex tblSystemScmSubmitRegex);

    List<TblSystemScmSubmit> getSystemScmSubmitConfigs(Long systemId, Integer scmType);

    List<TblSystemScmSubmitRegex> getSystemScmSubmitRegexConfigs(Long systemId);

    List<Project> getSystemGitLabProjects(Long systemId) throws URISyntaxException;
    List<Project> getSystemGitLabProjectsWithSystemIds(String systemIds) throws URISyntaxException;

    String saveGitLabModify(Project project, List<UserDTO> addUserCollection, List<UserDTO> delUserCollection, List<UserDTO> modifyUserCollection);

    List<TblSystemScmSubmitRegex> getSystemScmSubmitRegexesForCodeCommit(TblSystemScmSubmitRegex tblSystemScmSubmitRegex);

    List<TblSystemScmRepository> findScmRepositoryBySystemId(Long systemId);

    Map<String, Object> getCodeFilesByDevTaskId(Long devTaskId);

	void modifyGitPassword(Long currentUserId, String userScmAccount, String userScmPassword,
			String entryptUserScmPassword) throws Exception;
}
