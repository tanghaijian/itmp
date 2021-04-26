package cn.pioneeruniverse.project.dto;

import java.io.Serializable;

/**
 * 项目公告
 */
public class ProjectNoticeDTO implements Serializable {
    private static final long serialVersionUID = -6496067455862633155L;
    private String noticeContent;   //项目内容
    private String createDate;  //创建时间
    private Long id; //项目id

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
