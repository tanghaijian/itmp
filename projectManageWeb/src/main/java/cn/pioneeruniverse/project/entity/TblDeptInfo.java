package cn.pioneeruniverse.project.entity;

import java.util.List;
import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * @Author:
 * @Description: 部门表实体表
 * @Date: Created in 15:06 2020/08/25
 * @Modified By:
 */
public class TblDeptInfo extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2537302089144628111L;
	private Long id;
	private String deptName; //部门名
	private String deptNumber;//部门编号
	private Long parentDeptId;//父部门ID
	private String parentDeptIds;//所有父部门ID
	private List<TblDeptInfo> childDept; //子部门
	
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDeptNumber() {
		return deptNumber;
	}
	public void setDeptNumber(String deptNumber) {
		this.deptNumber = deptNumber;
	}
	public Long getParentDeptId() {
		return parentDeptId;
	}
	public void setParentDeptId(Long parentDeptId) {
		this.parentDeptId = parentDeptId;
	}
	public String getParentDeptIds() {
		return parentDeptIds;
	}
	public void setParentDeptIds(String parentDeptIds) {
		this.parentDeptIds = parentDeptIds;
	}
	public List<TblDeptInfo> getChildDept() {
		return childDept;
	}
	public void setChildDept(List<TblDeptInfo> childDept) {
		this.childDept = childDept;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
