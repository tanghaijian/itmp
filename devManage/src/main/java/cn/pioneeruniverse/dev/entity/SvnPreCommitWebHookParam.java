package cn.pioneeruniverse.dev.entity;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: svn的pre-commit钩子请求参数类
 * @Date: Created in 10:03 2018/12/27
 * @Modified By:
 */
public class SvnPreCommitWebHookParam implements Serializable {

    private static final long serialVersionUID = 2748731688960775678L;

    private String author;//提交者
    private String transaction;//希望检查的特定事物ID
    private String reposPath;
    private String commitDate;
    private List<String> message;
    private List<String> changedFiles; //文件
    private List<String> changedDirs; //文件目录
    private String ip;
    private String port;
    private String svnRootPath;//svn仓库根路径地址
    private String protocol;
    //仓库url
    private String reposUrl;
    private Boolean initChangedFilesEnd = Boolean.FALSE;
    private Boolean initDelFilesEnd = Boolean.FALSE;
    private Boolean checkDBScriptFileFor726 = Boolean.FALSE;//是否针对726项目组进行db脚本文件路径检测
    private Boolean checkSystemScmSubmitRegexes = Boolean.FALSE;//是否进行通配符检测
    //管理员账号
    private String superAdminAccount;
    //管理员密码
    private String superAdminPassword;
    private TblSystemScmSubmit relatedSystemScmSubmit;//提交代码所关联的配置
    private TblDevTask relatedWorkTask;//提交代码所关联的工作任务
    private TblRequirementFeature relatedDevTask;//提交代码所关联的开发任务
    private List<TblSystemScmSubmitRegex> relatedSystemScmSubmitRegexes; //提交代码关联的通配符配置

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String getReposPath() {
        return reposPath;
    }

    public void setReposPath(String reposPath) {
        this.reposPath = reposPath;
    }

    public String getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(String commitDate) {
        this.commitDate = commitDate;
    }


    public List<String> getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if (StringUtils.isNotEmpty(message)) {
            this.message = Arrays.asList(message.split("\\r?\\n"));
        }
    }

    public List<String> getChangedDirs() {
        return changedDirs;
    }

    public void setChangedDirs(String changedDirs) {
        //TODO 由于换行符无法输出，自定义以*号做分割
        this.changedDirs = Arrays.asList(changedDirs.split("\\r?\\*"));
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

    public String getSvnRootPath() {
        return svnRootPath;
    }

    public void setSvnRootPath(String svnRootPath) {
        this.svnRootPath = svnRootPath;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public List<String> getChangedFiles() {
        return changedFiles;
    }

    public void setChangedFiles(String changedFiles) {
        //TODO 由于换行符无法输出，自定义以*号做分割
        this.changedFiles = Arrays.asList(changedFiles.split("\\r?\\*"));
    }

    public String getReposUrl() {
        return reposUrl;
    }

    public void setReposUrl(String reposUrl) {
        this.reposUrl = reposUrl;
    }

    public Boolean getInitChangedFilesEnd() {
        return initChangedFilesEnd;
    }

    public void setInitChangedFilesEnd(Boolean initChangedFilesEnd) {
        this.initChangedFilesEnd = initChangedFilesEnd;
    }

    public Boolean getInitDelFilesEnd() {
        return initDelFilesEnd;
    }

    public void setInitDelFilesEnd(Boolean initDelFilesEnd) {
        this.initDelFilesEnd = initDelFilesEnd;
    }

    public void initReposUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.protocol).append("://")
                .append(this.ip).append(":")
                .append(this.port);
        if (StringUtils.isNotEmpty(this.svnRootPath)) {
            builder.append(this.svnRootPath);
        }
        builder.append("/").append(this.reposPath.substring(this.reposPath.lastIndexOf("/") + 1)).append("/");
        this.reposUrl = builder.toString();
    }

    public String getSuperAdminAccount() {
        return superAdminAccount;
    }

    public void setSuperAdminAccount(String superAdminAccount) {
        this.superAdminAccount = superAdminAccount;
    }

    public String getSuperAdminPassword() {
        return superAdminPassword;
    }

    public void setSuperAdminPassword(String superAdminPassword) {
        this.superAdminPassword = superAdminPassword;
    }

    public TblSystemScmSubmit getRelatedSystemScmSubmit() {
        return relatedSystemScmSubmit;
    }

    public void setRelatedSystemScmSubmit(TblSystemScmSubmit relatedSystemScmSubmit) {
        this.relatedSystemScmSubmit = relatedSystemScmSubmit;
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

    public List<TblSystemScmSubmitRegex> getRelatedSystemScmSubmitRegexes() {
        return relatedSystemScmSubmitRegexes;
    }

    public void setRelatedSystemScmSubmitRegexes(List<TblSystemScmSubmitRegex> relatedSystemScmSubmitRegexes) {
        this.relatedSystemScmSubmitRegexes = relatedSystemScmSubmitRegexes;
    }

    public Boolean getCheckDBScriptFileFor726() {
        return checkDBScriptFileFor726;
    }

    public void setCheckDBScriptFileFor726(Boolean checkDBScriptFileFor726) {
        this.checkDBScriptFileFor726 = checkDBScriptFileFor726;
    }

    public Boolean getCheckSystemScmSubmitRegexes() {
        return checkSystemScmSubmitRegexes;
    }

    public void setCheckSystemScmSubmitRegexes(Boolean checkSystemScmSubmitRegexes) {
        this.checkSystemScmSubmitRegexes = checkSystemScmSubmitRegexes;
    }
}


