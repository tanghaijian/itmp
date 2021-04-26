package cn.pioneeruniverse.common.gitlab.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * 
* @ClassName: Member
* @Description: gitlab项目成员封装，
* 具体请参照：https://docs.gitlab.com/ee/api/members.html
* @author author
* @date 2020年8月12日 下午2:12:18
*
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class Member implements Serializable {

    private static final long serialVersionUID = -3131213891633703953L;
    //id
    private Integer id;  
  //用户名
    private String username; 
  //姓名
    private String name; 
  //状态
    private String state; 
  //头像网址
    private String avatarUrl; 
  //主页
    private String webUrl;
  //有效期
    private Date expiresAt; 
  //权限等级
    private Integer accessLevel; 
  //用户ID
    private Integer userId; 

    public Member() {

    }

    public Member(Integer userId, Integer accessLevel) {
        this.userId = userId;
        this.accessLevel = accessLevel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    public Date getExpiresAt() {
        return expiresAt;
    }

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale = "zh", timezone = "UTC")
    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Integer getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(Integer accessLevel) {
        this.accessLevel = accessLevel;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
