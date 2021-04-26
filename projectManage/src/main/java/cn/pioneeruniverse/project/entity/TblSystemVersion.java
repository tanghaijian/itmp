package cn.pioneeruniverse.project.entity;


import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * 
* @ClassName: TblSystemVersion
* @Description: 系统版本bean
* @author author
* @date 2020年8月31日 下午1:19:43
*
 */
public class TblSystemVersion extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 706095827303768023L;

	private Long systemId;//系统ID

	private String groupFlag;//分组标签
	
    private String version;//版本

    private Long projectId;//项目ID
    private String ids;//多个ID，查找到多个TblSystemVersion时，以,隔开组装id
	private String systemIds;//多个系统ID，多选查询时用于封装选择的系统ID
	private String systemNames;//多个系统ID对应的系统名

	public String getEnvironmentTypes() {
		return environmentTypes;
	}

	public void setEnvironmentTypes(String environmentTypes) {
		this.environmentTypes = environmentTypes;
	}

	private String environmentTypes;


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

	public String getSystemIds() {
		return systemIds;
	}
	public void setSystemIds(String systemIds) {
		this.systemIds = systemIds;
	}

	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getSystemNames() {
		return systemNames;
	}
	public void setSystemNames(String systemNames) {
		this.systemNames = systemNames;
	}

	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
}