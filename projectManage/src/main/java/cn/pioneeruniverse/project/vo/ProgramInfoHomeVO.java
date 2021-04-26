package cn.pioneeruniverse.project.vo;

import java.io.Serializable;

public class ProgramInfoHomeVO implements Serializable {
    private static final long serialVersionUID = -6496067455862633155L;
  //项目群id
    private Long id;   
  //项目群编号
    private String programNumber;   
  //项目群名称
    private String programName;  
  //项目经理
    private String userName;    
    //项目经理id
    private Long userId;   
//    private Integer projectScheduleStatus;  //项目进度状态
    public Long getId() {
    	return id;
    }
    
    public void setId(Long id) {
    	this.id = id;
    }

    public String getProgramNumber() {
        return programNumber;
    }

    public void setProgramNumber(String programNumber) {
        this.programNumber = programNumber;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public Long getUserId() {
    	return userId;
    }
    
    public void setUserId(Long userId) {
    	this.userId = userId;
    }

    @Override
    public String toString() {
        return "ProgramInfoHomeVO{" +
        		"id='" + id + '\'' +
                "programNumber='" + programNumber + '\'' +
                ", programName='" + programName + '\'' +
                ", userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
