package cn.pioneeruniverse.dev.entity;


import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import cn.pioneeruniverse.common.gitlab.entity.Project;
import cn.pioneeruniverse.common.gitlab.entity.User;

/**
 * 
* @ClassName: GitLabPreReceiveHookParam
* @Description: gitLab preReceive 钩子
* @author author
* @date 2020年8月11日 上午10:25:14
*
 */
public class GitLabPreReceiveHookParam implements Serializable {
    private static final long serialVersionUID = 9047234488501032052L;

    private String oldCommitId;//旧版本号
    private String nowCommitId;//新版本号
    private String refName; //git branch
    private String commitMessage; //提交消息
    private String committerEmail;
    private Integer projectId;
    private String ip;
    private String port;
    private String protocol;
    private Project relatedProject;
    private TblSystemScmSubmit relatedSystemScmSubmit;
    private TblToolInfo relatedToolInfo;//提交代码对应仓库关联的工具
    private TblDevTask relatedWorkTask;//提交代码所关联的工作任务
    private TblRequirementFeature relatedDevTask;//提交代码所关联的开发任务
    private User commitUser;

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

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Project getRelatedProject() {
        return relatedProject;
    }

    public void setRelatedProject(Project relatedProject) {
        this.relatedProject = relatedProject;
    }

    public TblSystemScmSubmit getRelatedSystemScmSubmit() {
        return relatedSystemScmSubmit;
    }

    public void setRelatedSystemScmSubmit(TblSystemScmSubmit relatedSystemScmSubmit) {
        this.relatedSystemScmSubmit = relatedSystemScmSubmit;
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

    public TblDevTask getRelatedWorkTask() {
        return relatedWorkTask;
    }

    public void setRelatedWorkTask(TblDevTask relatedWorkTask) {
        this.relatedWorkTask = relatedWorkTask;
    }

    public TblRequirementFeature getRelatedDevTask() {
        return relatedDevTask;
    }

    public void setRelatedDevTask(TblRequirementFeature relatedDevTask) {
        this.relatedDevTask = relatedDevTask;
    }

    public String getCommitMessage() {
        return commitMessage;
    }

    //TODO 脚本传递过来的message都并行在一行
    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

    public String getCommitterEmail() {
        return committerEmail;
    }

    public void setCommitterEmail(String committerEmail) {
        if (StringUtils.isNotEmpty(committerEmail)) {
            committerEmail = committerEmail.split(" ")[2];
            this.committerEmail = committerEmail.substring(1, committerEmail.length() - 1);
        }
    }

}
