package cn.pioneeruniverse.common.gitlab.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * 
* @ClassName: Commit
* @Description: gitlab的Commit对象，
*               详情参考：https://docs.gitlab.com/ee/api/commits.html
* @author author
* @date 2020年9月4日 下午4:43:37
*
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class Commit implements Serializable {

    private static final long serialVersionUID = 7311025227764364624L;

    //提交id
    private String id;
    //提交id的简短表示，一般是id的前几位
    private String shortId;
    //标题
    private String title;
    private String authorName;
    private String authorEmail;
    //提交人姓名
    private String committerName;
    //提交人email
    private String committerEmail;
    //创建日期
    private Date createdAt;
    //提交内容
    private String message;
    //提交日期
    private Date committedDate;
    private Date authoredDate;
    private List<String> parentIds;
    private String status;
    private Integer projectId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortId() {
        return shortId;
    }

    public void setShortId(String shortId) {
        this.shortId = shortId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getCommitterName() {
        return committerName;
    }

    public void setCommitterName(String committerName) {
        this.committerName = committerName;
    }

    public String getCommitterEmail() {
        return committerEmail;
    }

    public void setCommitterEmail(String committerEmail) {
        this.committerEmail = committerEmail;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    public Date getCreatedAt() {
        return createdAt;
    }

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", locale = "zh", timezone = "UTC")
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    public Date getCommittedDate() {
        return committedDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", locale = "zh", timezone = "UTC")
    public void setCommittedDate(Date committedDate) {
        this.committedDate = committedDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    public Date getAuthoredDate() {
        return authoredDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", locale = "zh", timezone = "UTC")
    public void setAuthoredDate(Date authoredDate) {
        this.authoredDate = authoredDate;
    }

    public List<String> getParentIds() {
        return parentIds;
    }

    public void setParentIds(List<String> parentIds) {
        this.parentIds = parentIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

}
