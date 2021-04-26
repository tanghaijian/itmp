package cn.pioneeruniverse.project.vo;

import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

public class MessageInfoVO implements Serializable {

    private static final long serialVersionUID = -6496067455862633155L;

    /**
     * 标题
     **/
    private String messageTitle;    //标题


    /**
     * 内容
     **/
    private String messageContent;    //内容


    /**
     * 时间
     **/
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone="GMT+8")
    private Date createDate;    //时间

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "MessageInfoVO{" +
                "messageTitle='" + messageTitle + '\'' +
                ", messageContent='" + messageContent + '\'' +
                ", createDate='" + createDate + '\'' +
                '}';
    }
}
