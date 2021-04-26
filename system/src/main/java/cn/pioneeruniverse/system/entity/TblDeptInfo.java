package cn.pioneeruniverse.system.entity;

import java.util.List;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("tbl_dept_info")
public class TblDeptInfo extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2537302089144628111L;
	private Long id;
	//部门名
	private String deptName;
	//部门编码
	private String deptNumber;
	//上级部门编码
	private String parentDeptNumber;
	//上级部门ID
	private Long parentDeptId;
	//所有上级部门ID
	private String parentDeptIds;
	//子部门
	@TableField(exist = false)
	private List<TblDeptInfo> childDept;
	
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
