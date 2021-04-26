package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.gitlab.entity.Commit;
import cn.pioneeruniverse.common.gitlab.entity.Project;
import cn.pioneeruniverse.common.gitlab.entity.User;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * 
* @ClassName: GitLabPostReceiveHookParam
* @Description: gitLab PostReceive钩子参数
* @author author
* @date 2020年8月11日 上午10:18:11
*
 */
public class GitLabPostReceiveHookParam implements Serializable {
    private static final long serialVersionUID = 801757532137482895L;

    private String oldCommitId;//上次提交的版本号
    private String nowCommitId;//新提交版本号
    private String refName; //git branch
    private Integer projectId; //项目ID
    private String protocol;//协议
    private String ip;//IP
    private String port;//端口
    private List<String> commitFiles; //提交的文件
    private Project relatedProject; //关联的项目
    private TblToolInfo relatedToolInfo;//提交代码对应仓库关联的工具
    private User commitUser; //提交人
    private Commit commitInfo;//提交信息

    public String getOldCommitId() {
        return oldCommitId;
    }

    public void setOldCommitId(String oldCommitId) {
        this.oldCommitId = oldCommitId;
    }

    public String getNowCommitId() {
        return nowCommitId;
    }

    public void setNowCommitId(String nowCommitId) {
        this.nowCommitId = nowCommitId;
    }

    public String getRefName() {
        return refName;
    }

    public void setRefName(String refName) {
        this.refName = refName.split("/", 3)[2];
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public Project getRelatedProject() {
        return relatedProject;
    }

    public void setRelatedProject(Project relatedProject) {
        this.relatedProject = relatedProject;
    }

    public TblToolInfo getRelatedToolInfo() {
        return relatedToolInfo;
    }

    public void setRelatedToolInfo(TblToolInfo relatedToolInfo) {
        this.relatedToolInfo = relatedToolInfo;
    }

    public User getCommitUser() {
        return commitUser;
    }

    public void setCommitUser(User commitUser) {
        this.commitUser = commitUser;
    }

    public Commit getCommitInfo() {
        return commitInfo;
    }

    public void setCommitInfo(Commit commitInfo) {
        this.commitInfo = commitInfo;
    }

    public List<String> getCommitFiles() {
        return commitFiles;
    }

    public void setCommitFiles(String commitFiles) {
        //TODO 由于换行符无法输出，自定义以*号做分割
        this.commitFiles = Arrays.asList(commitFiles.split("\\r?\\*"));
    }
}
