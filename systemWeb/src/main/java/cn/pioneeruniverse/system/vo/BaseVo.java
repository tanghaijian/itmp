package cn.pioneeruniverse.system.vo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 
* @ClassName: BaseVo
* @Description: vo公共继承类
* @author author
* @date 2020年9月4日 上午10:53:34
*
 */
public class BaseVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6496067455862633155L;

	private Long id;
	//状态，1有效，2删除
	private Integer status;
	//描述
	private String description;
	//创建人
	private Long createBy;
	//创建日期
	private Timestamp createDate;
	//更新人
	private Long lastUpdateBy;
	//更新日期
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
