package cn.pioneeruniverse.dev.vo;

import java.io.Serializable;
import java.sql.Timestamp;


public class BaseVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6496067455862633155L;

	private Long id;
	
	private Integer status;
	
	private String description;
	
	private Long createBy;
	
	private Timestamp createDate;
	
	private Long lastUpdateBy;
	
	private Timestamp lastUpdateDate;
	
	

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
}
