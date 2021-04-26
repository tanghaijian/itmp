package cn.pioneeruniverse.dev.entity;


import cn.pioneeruniverse.common.entity.BaseEntity;

public class TblSystemVersion extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 706095827303768023L;

	private Long systemId;//系统ID
	
	private Long systemModuleId;//系统模块ID

	private String groupFlag; //分组标签
	
    private String version;//版本

	private String environmentTypes;//环境类型，数据字典的valuecode以,隔开

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public String getGroupFlag() {
		return groupFlag;
	}

	public void setGroupFlag(String groupFlag) {
		this.groupFlag = groupFlag;
	}

	public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version == null ? null : version.trim();
    }

	public Long getSystemModuleId() {
		return systemModuleId;
	}

	public void setSystemModuleId(Long systemModuleId) {
		this.systemModuleId = systemModuleId;
	}
	public String getEnvironmentTypes() {
		return environmentTypes;
	}

	public void setEnvironmentTypes(String environmentTypes) {
		this.environmentTypes = environmentTypes;
	}
    
    

}