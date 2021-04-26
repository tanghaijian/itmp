package cn.pioneeruniverse.dev.feignFallback;

import cn.pioneeruniverse.common.gitlab.entity.*;
import cn.pioneeruniverse.dev.feignInterface.DevManageToGitLabWebApiInterface;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 14:14 2019/7/3
 * @Modified By:
 */
public class DevManageToGitLabWebApiFallback implements FallbackFactory<DevManageToGitLabWebApiInterface> {

    private static final Logger logger = LoggerFactory.getLogger(DevManageToSystemFallback.class);

    @Override
    public DevManageToGitLabWebApiInterface create(Throwable throwable) {
        return new DevManageToGitLabWebApiInterface() {
            @Override
            public Project getProjectById(URI baseUri, Integer projectId, String privateToken) {
                logger.error("gitLabWebApi interface request error:" + throwable.getMessage(), throwable.getCause());
                return null;
            }

            @Override
            public List<Member> getProjectMembers(URI baseUri, Integer projectId, String privateToken) {
                logger.error("gitLabWebApi interface request error:" + throwable.getMessage(), throwable.getCause());
                return null;
            }

            @Override
            public List<Member> getProjectAllMembers(URI baseUri, Integer projectId, String privateToken) {
                logger.error("gitLabWebApi interface request error:" + throwable.getMessage(), throwable.getCause());
                return null;
            }

            @Override
            public void deleteMemberFromProject(URI baseUri, Integer projectId, Integer userId, String privateToken) {
                logger.error("gitLabWebApi interface request error:" + throwable.getMessage(), throwable.getCause());
                throw new RuntimeException(throwable.getCause());
            }

            @Override
            public void addMemberToProject(URI baseUri, Member member, Integer projectId, String privateToken) {
                logger.error("gitLabWebApi interface request error:" + throwable.getMessage(), throwable.getCause());
                throw new RuntimeException(throwable.getCause());
            }

            @Override
            public void editMemberOfProject(URI baseUri, Member member, Integer projectId, Integer userId, String privateToken) {
                logger.error("gitLabWebApi interface request error:" + throwable.getMessage(), throwable.getCause());
                throw new RuntimeException(throwable.getCause());
            }

            @Override
            public Project createProject(URI baseUri, Project project, String privateToken) {
                logger.error("gitLabWebApi interface request error:" + throwable.getMessage(), throwable.getCause());
                throw new RuntimeException(throwable.getCause());
            }

            @Override
            public User createUser(URI baseUri, User user, String privateToken) {
                logger.error("gitLabWebApi interface request error:" + throwable.getMessage(), throwable.getCause());
                throw new RuntimeException(throwable.getCause());
            }

            @Override
            public List<User> getUserByUserName(URI baseUri, String userName, String privateToken) {
                logger.error("gitLabWebApi interface request error:" + throwable.getMessage(), throwable.getCause());
                throw new RuntimeException(throwable.getCause());
            }

            @Override
            public List<User> getUserByEmail(URI baseUri, String email, String privateToken) {
                logger.error("gitLabWebApi interface request error:" + throwable.getMessage(), throwable.getCause());
                return null;
            }

            @Override
            public Commit getSingleCommitByCommitID(URI baseUri, Integer projectId, String commitId, String privateToken) {
                logger.error("gitLabWebApi interface request error:" + throwable.getMessage(), throwable.getCause());
                return null;
            }

            @Override
            public File getFileFromRepository(URI baseUri, Integer projectId, String filePath, String ref, String privateToken) {
                logger.error("gitLabWebApi interface request error:" + throwable.getMessage(), throwable.getCause());
                return null;
            }

            @Override
            public String getFileRawBlobContent(URI baseUri, Integer projectId, String blobSHA, String privateToken) {
                logger.error("gitLabWebApi interface request error:" + throwable.getMessage(), throwable.getCause());
                return null;
            }

			@Override
			public void editUserPasswordById(URI baseUri, Integer userId, String password, String privateToken) {
				 logger.error("gitLabWebApi interface request error:" + throwable.getMessage(), throwable.getCause());
			}

        };
    }
}
