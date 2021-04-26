package cn.pioneeruniverse.project.dto;

import java.io.Serializable;

/**
 * 周边系统 && 项目成员
 */
public class PeripheralSystemDTO implements Serializable {
    private static final long serialVersionUID = -6496067455862633155L;
    private String systemName;  //周边系统
    private String userName;   //项目成员
    private Long id;    //用户id

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "PeripheralSystemDTO{" +
                "systemName='" + systemName + '\'' +
                ", userName='" + userName + '\'' +
                ", id=" + id +
                '}';
    }
}
