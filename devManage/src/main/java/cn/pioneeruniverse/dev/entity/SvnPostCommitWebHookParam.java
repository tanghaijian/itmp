package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: svn的post-commit钩子请求参数类
 * @Date: Created in 9:52 2019/1/8
 * @Modified By:
 */
public class SvnPostCommitWebHookParam implements Serializable {

    private static final long serialVersionUID = 2231626233106300063L;

    private String author;//作者
    private String revision; //版本
    private String transaction; //事务ID
    private String reposPath; //仓库路径
    private List<String> message; //提交注释
    private String ip;
    private String port;
    private String svnRootPath;//svn仓库根路径地址
    private String protocol;
    private String reposUrl; //仓库url
    private Date commitDate; //提交日期
    private Boolean onlyDicForCommitAddDictWithFiles = Boolean.FALSE;//带有文件的文件夹新增至版本库提交日志是否只展示文件夹
    private String superAdminAccount; 
    private String superAdminPassword;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getReposPath() {
        return reposPath;
    }

    public void setReposPath(String reposPath) {
        this.reposPath = reposPath;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = Arrays.asList(message.split("\\r?\\n"));
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

    public Date getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(String commitDate) {
        this.commitDate = DateUtil.parseDate(commitDate = commitDate.substring(0, 19), DateUtil.fullFormat);
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public Boolean getOnlyDicForCommitAddDictWithFiles() {
        return onlyDicForCommitAddDictWithFiles;
    }

    public void setOnlyDicForCommitAddDictWithFiles(String onlyDicForCommitAddDictWithFiles) {
        this.onlyDicForCommitAddDictWithFiles = Boolean.valueOf(onlyDicForCommitAddDictWithFiles);
    }

    public String getReposUrl() {
        return reposUrl;
    }

    public void setReposUrl(String reposUrl) {
        this.reposUrl = reposUrl;
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
}
