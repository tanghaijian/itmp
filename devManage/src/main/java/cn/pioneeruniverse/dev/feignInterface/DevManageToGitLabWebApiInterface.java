package cn.pioneeruniverse.dev.feignInterface;

import cn.pioneeruniverse.common.gitlab.entity.*;
import feign.Param;
import feign.RequestLine;

import java.net.URI;
import java.util.List;


/**
 * 
* @ClassName: DevManageToGitLabWebApiInterface
* @Description: 开发模块feign调用gitlab，具体用法参考gitlab api
* @author author
* @date 2020年8月11日 下午9:29:49
*
 */
public interface DevManageToGitLabWebApiInterface {

	/**
	 * 
	* @Title: getProjectById
	* @Description: 查看gitlab 项目
	* @author author
	* @param baseUri gitlab根地址
	* @param projectId 项目ID
	* @param privateToken 个人账号token
	* @return Project
	 */
    @RequestLine("GET /projects/{projectId}?private_token={privateToken}")
    Project getProjectById(URI baseUri, @Param("projectId") Integer projectId, @Param("privateToken") String privateToken);

    /**
     * 
    * @Title: getProjectMembers
    * @Description: 列表查看gitlab项目成员
    * @author author
    * @param baseUri gitlab根地址
    * @param projectId  项目ID
    * @param privateToken 个人账号token
    * @return List<Member>
     */
    @RequestLine("GET projects/{projectId}/members?private_token={privateToken}&per_page=1000")
    List<Member> getProjectMembers(URI baseUri, @Param("projectId") Integer projectId, @Param("privateToken") String privateToken);

    /**
     * 
    * @Title: getProjectAllMembers
    * @Description:列表获取项目所有成员
    * @author author
    * @param baseUri gitlab根url
    * @param projectId 项目ID
    * @param privateToken 个人账号token
    * @return List<Member>
     */
    @RequestLine("GET projects/{projectId}/members/all?private_token={privateToken}&per_page=1000")
    List<Member> getProjectAllMembers(URI baseUri, @Param("projectId") Integer projectId, @Param("privateToken") String privateToken);

    /**
     * 
    * @Title: deleteMemberFromProject
    * @Description: 删除指定项目成员
    * @author author
    * @param baseUri gitlab根url
    * @param projectId 项目ID
    * @param userId 用户ID
    * @param privateToken  个人账号token
     */
    @RequestLine("DELETE /projects/{projectId}/members/{userId}?private_token={privateToken}")
    void deleteMemberFromProject(URI baseUri, @Param("projectId") Integer projectId, @Param("userId") Integer userId, @Param("privateToken") String privateToken);

    /**
     * 
    * @Title: addMemberToProject
    * @Description: 添加项目成员
    * @author author
    * @param baseUri
    * @param member 成员
    * @param projectId
    * @param privateToken
     */
    @RequestLine("POST /projects/{projectId}/members?private_token={privateToken}")
    void addMemberToProject(URI baseUri, Member member, @Param("projectId") Integer projectId, @Param("privateToken") String privateToken);

    /**
     * 
    * @Title: editMemberOfProject
    * @Description:编辑项目成员
    * @author author
    * @param baseUri
    * @param member 成员
    * @param projectId 项目ID
    * @param userId 用户ID
    * @param privateToken
     */
    @RequestLine("PUT /projects/{projectId}/members/{userId}?private_token={privateToken}")
    void editMemberOfProject(URI baseUri, Member member, @Param("projectId") Integer projectId, @Param("userId") Integer userId, @Param("privateToken") String privateToken);

    /**
     * 
    * @Title: createProject
    * @Description: 创建项目
    * @author author
    * @param baseUri
    * @param project 项目
    * @param privateToken
    * @return Project
     */
    @RequestLine("POST /projects?private_token={privateToken}")
    Project createProject(URI baseUri, Project project, @Param("privateToken") String privateToken);

    /**
     * 
    * @Title: createUser
    * @Description: 创建用户
    * @author author
    * @param baseUri
    * @param user
    * @param privateToken
    * @return User
     */
    @RequestLine("POST /users?private_token={privateToken}")
    User createUser(URI baseUri, User user, @Param("privateToken") String privateToken);

    /**
     * 
    * @Title: getUserByUserName
    * @Description: 通过姓名获取用户
    * @author author
    * @param baseUri
    * @param userName 用户名
    * @param privateToken
    * @return List<User>
     */
    @RequestLine("GET /users?username={userName}&private_token={privateToken}")
    List<User> getUserByUserName(URI baseUri, @Param("userName") String userName, @Param("privateToken") String privateToken);

    /**
     * 
    * @Title: getUserByEmail
    * @Description:通过email获取用户
    * @author author
    * @param baseUri
    * @param email
    * @param privateToken
    * @return List<User>
     */
    @RequestLine("GET /users?search={email}&private_token={privateToken}")
    List<User> getUserByEmail(URI baseUri, @Param("email") String email, @Param("privateToken") String privateToken);
    
    
    /**
     * 
    * @Title: editUserPasswordById
    * @Description: 修改用户密码
    * @author author
    * @param baseUri
    * @param userId 用户ID
    * @param password 密码
    * @param privateToken
     */
    @RequestLine("PUT /users/{id}?password={password}private_token={privateToken}")
    void editUserPasswordById(URI baseUri,@Param("id")Integer userId, @Param("password") String password,@Param("privateToken") String privateToken);

    /**
     * 
    * @Title: getSingleCommitByCommitID
    * @Description: 获取某次提交信息
    * @author author
    * @param baseUri
    * @param projectId
    * @param commitId 提交的hash或者branch
    * @param privateToken
    * @return Commit
     */
    @RequestLine("GET /projects/{projectId}/repository/commits/{commitId}?private_token={privateToken}")
    Commit getSingleCommitByCommitID(URI baseUri, @Param("projectId") Integer projectId, @Param("commitId") String commitId, @Param("privateToken") String privateToken);

    /**
     * 
    * @Title: getFileFromRepository
    * @Description: 获取提交的文件
    * @author author
    * @param baseUri
    * @param projectId
    * @param filePath 文件路径
    * @param ref  如master
    * @param privateToken
    * @return File
     */
    @RequestLine(value = "GET /projects/{projectId}/repository/files/{filePath}?ref={ref}&private_token={privateToken}", decodeSlash = false)
    File getFileFromRepository(URI baseUri, @Param("projectId") Integer projectId, @Param(value = "filePath", encoded = true) String filePath, @Param("ref") String ref, @Param("privateToken") String privateToken);

    /**
     * 
    * @Title: getFileRawBlobContent
    * @Description: 获取文件内容
    * @author author
    * @param baseUri
    * @param projectId
    * @param blobSHA
    * @param privateToken
    * @return String 文件内容
     */
    @RequestLine(value = "GET /projects/{projectId}/repository/blobs/{blobSHA}/raw?private_token={privateToken}")
    String getFileRawBlobContent(URI baseUri, @Param("projectId") Integer projectId, @Param("blobSHA") String blobSHA, @Param("privateToken") String privateToken);
}
