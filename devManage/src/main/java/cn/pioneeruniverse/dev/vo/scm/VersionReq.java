package cn.pioneeruniverse.dev.vo.scm;

/**
 * @author by dyx11
 * @version 2020/8/23 22:08
 * @description
 */
public class VersionReq {

    //======================================================
    // id
    private Long id;
    //系统id
    private Long systemId;
    //用户id
    private String userIds;
    //原有的用户
    private String oldUser;
    //是否提交
    private Integer submitStatus;
    //原有是否提交
    private Integer oldSubStatus;

    //======================================================
    // GETTER & SETTER
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public String getOldUser() {
        return oldUser;
    }

    public void setOldUser(String oldUser) {
        this.oldUser = oldUser;
    }

    public Integer getSubmitStatus() {
        return submitStatus;
    }

    public void setSubmitStatus(Integer submitStatus) {
        this.submitStatus = submitStatus;
    }

    public Integer getOldSubStatus() {
        return oldSubStatus;
    }

    public void setOldSubStatus(Integer oldSubStatus) {
        this.oldSubStatus = oldSubStatus;
    }
}