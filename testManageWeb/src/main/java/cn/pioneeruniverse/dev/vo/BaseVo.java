package cn.pioneeruniverse.dev.vo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @ClassName: BaseVo
 * @Description: 公共表
 * @author author
 * @date 2020年8月27日 15:12:25
 *
 */
public class BaseVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6496067455862633155L;

	private Long id; //ID
	
	private Integer status; //状态
	
	private String description; //描述
	
	private Long createBy; //创建人
	
	private Timestamp createDate; //创建时间
	
	private Long lastUpdateBy; //最后修改人
	
	private Timestamp lastUpdateDate; //最后一次修改时间
	
	

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
