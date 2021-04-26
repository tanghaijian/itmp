package cn.pioneeruniverse.dev.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2018/10/30.
 */
public class SvnCommitLog implements Serializable {
    private static final long serialVersionUID = 8570441396192791197L;

    private Long revision; //版本号
    private String author; //作者
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date commitDate; //提交时间
    private String commitMessage; //提交注释
    private List<ChangedPath> changedPathList; //改变的文件目录列表

    public Long getRevision() {
        return revision;
    }

    public void setRevision(Long revision) {
        this.revision = revision;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(Date commitDate) {
        this.commitDate = commitDate;
    }

    public String getCommitMessage() {
        return commitMessage;
    }

    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

    public List<ChangedPath> getChangedPathList() {
        return changedPathList;
    }

    public void setChangedPathList(List<ChangedPath> changedPathList) {
        this.changedPathList = changedPathList;
    }


}
