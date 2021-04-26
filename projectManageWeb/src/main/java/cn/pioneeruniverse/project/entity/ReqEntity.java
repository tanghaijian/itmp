package cn.pioneeruniverse.project.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * @Author:
 * @Description: （废弃）
 * @Date: Created in 15:06 2020/08/25
 * @Modified By:
 */
public class ReqEntity extends BaseEntity {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private	Long parentId;	//父ID
	private	String parentIds;	//所有父ID(如：,1,2,3,)
	
	private String reqStatusName;	//需求状态名称(数据字典)
	private String reqSourceName;	//需求来源名称(数据字典)
	private String reqTypeName;		//需求类型名称(数据字典)
	private	String reqPriorityName;//需求优先级名称(数据字典)
    private String userName;	//需求提出人名称
    private String deptName;	//归属部门名称	
	
	
	
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
	public String getReqStatusName() {
		return reqStatusName;
	}
	public void setReqStatusName(String reqStatusName) {
		this.reqStatusName = reqStatusName;
	}
	public String getReqSourceName() {
		return reqSourceName;
	}
	public void setReqSourceName(String reqSourceName) {
		this.reqSourceName = reqSourceName;
	}
	public String getReqTypeName() {
		return reqTypeName;
	}
	public void setReqTypeName(String reqTypeName) {
		this.reqTypeName = reqTypeName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getReqPriorityName() {
		return reqPriorityName;
	}
	public void setReqPriorityName(String reqPriorityName) {
		this.reqPriorityName = reqPriorityName;
	}
}