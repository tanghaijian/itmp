package cn.pioneeruniverse.project.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.util.List;

/**
 *@author
 *@Description 部门类
 *@Date 2020/8/7
 *@return
 **/
@TableName("tbl_dept_info")
public class TblDeptInfo extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2537302089144628111L;
	private Long id;
	private String deptName;//部门名
	private String deptNumber;//部门编号
	private String parentDeptNumber;//父部门编号
	private Long parentDeptId;//父部门ID
	private String parentDeptIds;//该部门所有的上级部门ID
	@TableField(exist = false)
	private List<TblDeptInfo> childDept;//该部门所有的下级部门ID
	
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
	public String getParentDeptNumber() {
		return parentDeptNumber;
	}
	public void setParentDeptNumber(String parentDeptNumber) {
		this.parentDeptNumber = parentDeptNumber;
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
