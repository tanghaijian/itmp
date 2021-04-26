package cn.pioneeruniverse.dev.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: svn文件目录结构类
 * @Date: Created in 18:00 2018/12/12
 * @Modified By:
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SvnFileDirectoryStructure implements Serializable {

    private static final long serialVersionUID = 3702052587270165885L;

    private String name;//文件名
    private Integer type; //类型（0.文件夹，1.文件）
    private String lastAuthorName; //文件最后编辑者姓名
    private Date lastChangeDate; //文件最后更新时间
    private String repositoryName;//文件所属仓库名称
    private String path;//文件路径
    private String svnUrl;//文件url;
    private String ip;//ip地址
    private String port;//端口号
    private List<SvnFileDirectoryStructure> children;//子文件
    private String repositoryUrl;//文件所属仓库url
    private String username;//用户名
    private String password;//密码
    private Integer accessProtocol;//

    public SvnFileDirectoryStructure() {

    }

    public SvnFileDirectoryStructure(String name, Integer type, String repositoryName, String path, String svnUrl, Integer accessProtocol, String ip, String port, String repositoryUrl, String username, String password) {
        this.name = name;
        this.type = type;
        this.repositoryName = repositoryName;
        this.path = path;
        this.svnUrl = svnUrl;
        this.accessProtocol = accessProtocol;
        this.ip = ip;
        this.port = port;
        this.repositoryUrl = repositoryUrl;
        this.username = username;
        this.password = password;
    }

    public SvnFileDirectoryStructure(String name, Integer type, String lastAuthorName, Date lastChangeDate, String repositoryName, String path, String svnUrl, Integer accessProtocol, String ip, String port, String repositoryUrl, String username, String password) {
        this.name = name;
        this.type = type;
        this.lastAuthorName = lastAuthorName;
        this.lastChangeDate = lastChangeDate;
        this.repositoryName = repositoryName;
        this.path = path;
        this.svnUrl = svnUrl;
        this.accessProtocol = accessProtocol;
        this.ip = ip;
        this.port = port;
        this.repositoryUrl = repositoryUrl;
        this.username = username;
        this.password = password;
    }

    public void addChild(SvnFileDirectoryStructure child) {
        if (this.getChildren() == null) {
            this.setChildren(new LinkedList<>());
        }
        this.getChildren().add(child);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getLastAuthorName() {
        return lastAuthorName;
    }

    public void setLastAuthorName(String lastAuthorName) {
        this.lastAuthorName = lastAuthorName;
    }

    public Date getLastChangeDate() {
        return lastChangeDate;
    }

    public void setLastChangeDate(Date lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSvnUrl() {
        return svnUrl;
    }

    public void setSvnUrl(String svnUrl) {
        this.svnUrl = svnUrl;
    }

    public List<SvnFileDirectoryStructure> getChildren() {
        return children;
    }

    public void setChildren(List<SvnFileDirectoryStructure> children) {
        this.children = children;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public Integer getAccessProtocol() {
        return accessProtocol;
    }

    public void setAccessProtocol(Integer accessProtocol) {
        this.accessProtocol = accessProtocol;
    }
}
