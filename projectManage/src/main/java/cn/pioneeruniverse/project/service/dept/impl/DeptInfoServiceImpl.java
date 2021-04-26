package cn.pioneeruniverse.project.service.dept.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.pioneeruniverse.project.dao.mybatis.DeptInfoMapper;
import cn.pioneeruniverse.project.service.dept.DeptInfoService;

@Service
public class DeptInfoServiceImpl implements DeptInfoService{

	@Autowired
	private DeptInfoMapper deptInfoMapper;
	
	/**
	* @author author
	* @Description 根据部门名称查询id
	* @Date 2020/9/7
	* @param deptName 部门名
	* @return java.lang.Long
	**/
	@Override
	public Long selectDeptId(String deptName) {
		return deptInfoMapper.selectDeptId(deptName);
		
	}

	/**
	* @author author
	* @Description 根据id查询名称
	* @Date 2020/9/7
	* @param deptId 部门id
	* @return java.lang.String
	**/
	@Override
	public String selectDeptName(Long deptId) {
		return deptInfoMapper.selectDeptName(deptId);
	}

	/**
	* @author author
	* @Description 查询所有的部门名称
	* @Date 2020/9/7
	* @return java.util.List<java.lang.String>
	**/
	@Override
	public List<String> selectAllDeptName() {
		return deptInfoMapper.selectAllDeptName();
	}

	/**
	 * 
	* @Title: findIdByDeptNumber
	* @Description: 通过部门编号获取id
	* @author author
	* @param deptNumber 部门编号
	* @return Long 部门id
	 */
	@Override
	public Long findIdByDeptNumber(String deptNumber) {
		return deptInfoMapper.findIdByDeptNumber(deptNumber);
	}

}
