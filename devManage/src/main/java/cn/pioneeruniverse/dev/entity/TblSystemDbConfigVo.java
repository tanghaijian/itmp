package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;


public class TblSystemDbConfigVo extends BaseEntity {

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    private Long userId;//用户ID
    private Long systemId;//系统ID

    private Long systemModuleId; //系统模块ID

    private Integer environmentType;//环境类型

    private String driverClassName; //驱动类

    private String url;//数据库url

    private String userName; //用户姓名

    private String password; //用户密码
    private String environmentTypeName; //环境类型名，对应数据字典的valueName
    private String systemName; //系统名

    public String getEnvironmentTypeName() {
        return environmentTypeName;
    }

    public void setEnvironmentTypeName(String environmentTypeName) {
        this.environmentTypeName = environmentTypeName;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }


    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    private String moduleName;




    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Long getSystemModuleId() {
        return systemModuleId;
    }

    public void setSystemModuleId(Long systemModuleId) {
        this.systemModuleId = systemModuleId;
    }

    public Integer getEnvironmentType() {
        return environmentType;
    }

    public void setEnvironmentType(Integer environmentType) {
        this.environmentType = environmentType;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName == null ? null : driverClassName.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }


}