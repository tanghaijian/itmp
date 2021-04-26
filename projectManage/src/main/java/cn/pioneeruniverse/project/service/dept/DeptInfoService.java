package cn.pioneeruniverse.project.service.dept;

import java.util.List;

public interface DeptInfoService {

	/**
	* @author author
	* @Description 根据部门名称查询id
	* @Date 2020/9/7
	* @param deptName
	* @return java.lang.Long
	**/
	Long selectDeptId(String deptName);

	/**
	* @author author
	* @Description 根据id查询名称
	* @Date 2020/9/7
	* @param deptId
	* @return java.lang.String
	**/
	String selectDeptName(Long deptId);

	/**
	* @author author
	* @Description 查询所有的部门名称
	* @Date 2020/9/7
	* @param 
	* @return java.util.List<java.lang.String>
	**/
	List<String> selectAllDeptName();

	/**
	* @author author
	* @Description 同步时查询部门信息，无视状态
	* @Date 2020/9/7
	* @param deptNumber
	* @return java.lang.Long
	**/
	Long findIdByDeptNumber(String deptNumber);

}
