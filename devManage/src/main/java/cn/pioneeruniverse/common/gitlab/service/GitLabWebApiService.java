package cn.pioneeruniverse.common.gitlab.service;

import cn.pioneeruniverse.common.gitlab.entity.*;

import java.net.URISyntaxException;
import java.util.List;


/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 9:48 2019/7/2
 * @Modified By:
 */
public interface GitLabWebApiService {

    Project getProjectById(String baseUri, Integer projectId, String privateToken) throws URISyntaxException;

    List<Member> getProjectMembers(String baseUri, Integer projectId, String privateToken) throws URISyntaxException;

    List<Member> getProjectAllMembers(String baseUri, Integer projectId, String privateToken) throws URISyntaxException;

    void deleteMemberFromProject(String baseUri, Integer projectId, Integer userId, String privateToken) throws URISyntaxException;

    void addMemberToProject(String baseUri, Member member, Integer projectId, String privateToken) throws URISyntaxException;

    void editMemberOfProject(String baseUri, Member member, Integer projectId, Integer userId, String privateToken) throws URISyntaxException;

    Project createProject(String baseUri, Project project, String privateToken) throws URISyntaxException;

    User createUser(String baseUri, User user, String privateToken) throws URISyntaxException;

    User getUserByUserName(String baseUri, String username, String privateToken) throws URISyntaxException;

    User getUserByEmail(String baseUri, String email, String privateToken) throws URISyntaxException;

    Commit getCommitByCommitId(String baseUri, Integer projectId, String commitId, String privateToken) throws URISyntaxException;

    String getFileContentFromRepository(String baseUri, Integer projectId, String filePath, String ref, String privateToken) throws URISyntaxException;

	void editUserPasswordById(String baseUri, Integer id, String userScmPassword, String privateToken) throws URISyntaxException;
}
