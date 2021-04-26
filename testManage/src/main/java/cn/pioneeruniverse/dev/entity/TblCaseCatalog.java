package cn.pioneeruniverse.dev.entity;

import org.springframework.data.annotation.Transient;

import cn.pioneeruniverse.common.entity.BaseEntity;

public class TblCaseCatalog extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3780878602160505462L;

	private Long systemId;// 系统id

    private String catalogName;// 目录

    private Long parentId;// 父级

    private String parentIds;// 所有父级
    
    @Transient
    private Long uid; //用户id(区分管理员与非管理员)

	public Long getSystemId() {
		return systemId;
	}

	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}

	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}
    
    

}