package cn.pioneeruniverse.report.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;
import java.sql.Timestamp;

public class UiFavoriteDTO implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long userId;

    private String menuUrl;//菜单编号

    private String favoriteContent;//收藏内容



    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp createDate;

    /**
     * 上次修改者
     */
    private Long lastUpdateBy;

    /**
     * 上次修改时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp lastUpdateDate;



    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getFavoriteContent() {
        return favoriteContent;
    }

    public void setFavoriteContent(String favoriteContent) {
        this.favoriteContent = favoriteContent;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Long getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(Long lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public Timestamp getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Timestamp lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Override
    public String toString() {
        return "UiFavoriteDTO{" +
                "userId=" + userId +
                ", menuUrl='" + menuUrl + '\'' +
                ", favoriteContent='" + favoriteContent + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", createBy=" + createBy +
                ", createDate=" + createDate +
                ", lastUpdateBy=" + lastUpdateBy +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }
}
