package cn.pioneeruniverse.common.gitlab.service.Impl;

import cn.pioneeruniverse.common.gitlab.entity.*;
import cn.pioneeruniverse.common.gitlab.service.GitLabWebApiService;
import cn.pioneeruniverse.common.utils.CollectionUtil;
import cn.pioneeruniverse.dev.feignFallback.DevManageToGitLabWebApiFallback;
import cn.pioneeruniverse.dev.feignInterface.DevManageToGitLabWebApiInterface;
import feign.Retryer;
import feign.Target;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.hystrix.HystrixFeign;
import feign.okhttp.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.feign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.List;


/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 9:49 2019/7/2
 * @Modified By:
 */
@Service("gitLabWebApiService")
@Import(FeignClientsConfiguration.class)
public class GitLabWebApiServiceImpl implements GitLabWebApiService {

    @Value("${gitlab.web.api.namespace}")
    private String gitlabWebApiNamespace;

    private DevManageToGitLabWebApiInterface devManageToGitLabWebApiInterface;

    @Autowired
    public GitLabWebApiServiceImpl(Encoder encoder, Decoder decoder) {
        devManageToGitLabWebApiInterface = HystrixFeign.builder()
                .decoder(decoder)
                .encoder(encoder)
                .client(new OkHttpClient())
                .retryer(Retryer.NEVER_RETRY)
                .target(Target.EmptyTarget.create(DevManageToGitLabWebApiInterface.class), new DevManageToGitLabWebApiFallback());
    }

    @Override
    public Project getProjectById(String baseUri, Integer projectId, String privateToken) throws URISyntaxException {
        return devManageToGitLabWebApiInterface.getProjectById(new URI(baseUri + gitlabWebApiNamespace), projectId, privateToken);
    }

    @Override
    public List<Member> getProjectMembers(String baseUri, Integer projectId, String privateToken) throws URISyntaxException {
        return devManageToGitLabWebApiInterface.getProjectMembers(new URI(baseUri + gitlabWebApiNamespace), projectId, privateToken);
    }

    @Override
    public List<Member> getProjectAllMembers(String baseUri, Integer projectId, String privateToken) throws URISyntaxException {
        return devManageToGitLabWebApiInterface.getProjectAllMembers(new URI(baseUri + gitlabWebApiNamespace), projectId, privateToken);
    }

    @Override
    public void deleteMemberFromProject(String baseUri, Integer projectId, Integer userId, String privateToken) throws URISyntaxException {
        devManageToGitLabWebApiInterface.deleteMemberFromProject(new URI(baseUri + gitlabWebApiNamespace), projectId, userId, privateToken);
    }

    @Override
    public void addMemberToProject(String baseUri, Member member, Integer projectId, String privateToken) throws URISyntaxException {
        devManageToGitLabWebApiInterface.addMemberToProject(new URI(baseUri + gitlabWebApiNamespace), member, projectId, privateToken);
    }

    @Override
    public void editMemberOfProject(String baseUri, Member member, Integer projectId, Integer userId, String privateToken) throws URISyntaxException {
        devManageToGitLabWebApiInterface.editMemberOfProject(new URI(baseUri + gitlabWebApiNamespace), member, projectId, userId, privateToken);
    }

    @Override
    public Project createProject(String baseUri, Project project, String privateToken) throws URISyntaxException {
        return devManageToGitLabWebApiInterface.createProject(new URI(baseUri + gitlabWebApiNamespace), project, privateToken);
    }

    @Override
    public User createUser(String baseUri, User user, String privateToken) throws URISyntaxException {
        return devManageToGitLabWebApiInterface.createUser(new URI(baseUri + gitlabWebApiNamespace), user, privateToken);
    }

    @Override
    public User getUserByUserName(String baseUri, String username, String privateToken) throws URISyntaxException {
        if (StringUtils.isNotEmpty(username)) {
            List<User> users = devManageToGitLabWebApiInterface.getUserByUserName(new URI(baseUri + gitlabWebApiNamespace), username, privateToken);
            if (CollectionUtil.isNotEmpty(users)) {
                return users.get(0);
            }
        }
        return null;
    }

    @Override
    public User getUserByEmail(String baseUri, String email, String privateToken) throws URISyntaxException {
        if (StringUtils.isNotEmpty(email)) {
            List<User> users = devManageToGitLabWebApiInterface.getUserByEmail(new URI(baseUri + gitlabWebApiNamespace), email, privateToken);
            if (CollectionUtil.isNotEmpty(users)) {
                return users.get(0);
            }
        }
        return null;
    }

    @Override
    public Commit getCommitByCommitId(String baseUri, Integer projectId, String commitId, String privateToken) throws URISyntaxException {
        return devManageToGitLabWebApiInterface.getSingleCommitByCommitID(new URI(baseUri + gitlabWebApiNamespace), projectId, commitId, privateToken);
    }

    @Override
    public String getFileContentFromRepository(String baseUri, Integer projectId, String filePath, String ref, String privateToken) throws URISyntaxException {
        File file = devManageToGitLabWebApiInterface.getFileFromRepository(new URI(baseUri + gitlabWebApiNamespace), projectId, URLEncoder.encode(filePath), ref, privateToken);
        if (file != null && file.getBlobId() != null) {
            return devManageToGitLabWebApiInterface.getFileRawBlobContent(new URI(baseUri + gitlabWebApiNamespace), projectId, file.getBlobId(), privateToken);
        }
        return null;
    }

	@Override
	public void editUserPasswordById(String baseUri,Integer userId, String userPassword,String privateToken) throws URISyntaxException {
		devManageToGitLabWebApiInterface.editUserPasswordById(new URI(baseUri + gitlabWebApiNamespace), userId, userPassword,privateToken);
		
	}

}
