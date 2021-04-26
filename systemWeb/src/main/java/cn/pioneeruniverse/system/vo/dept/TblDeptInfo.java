package cn.pioneeruniverse.system.vo.dept;

import java.util.List;

import cn.pioneeruniverse.system.vo.BaseVo;

/**
 * 
 * 
* @ClassName: TblDeptInfo
* @Description: 部门信息
* @author author
* @date 2020年9月4日 上午10:44:12
*
 */
public class TblDeptInfo extends BaseVo{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3246631758181759396L;
	//部门名称
	private String deptName;
	//部门编号
	private String deptNumber;
	//父部门ID
	private Long parentDeptId;
	//所有的父部门ID
	private String parentDeptIds;
	//公司ID
	private Integer companyId;
	//公司名
	private String companyName;
	//所有子部门
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
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public List<TblDeptInfo> getChildDept() {
		return childDept;
	}
	public void setChildDept(List<TblDeptInfo> childDept) {
		this.childDept = childDept;
	}
}
