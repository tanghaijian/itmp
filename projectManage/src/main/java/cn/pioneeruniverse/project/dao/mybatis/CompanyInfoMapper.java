package cn.pioneeruniverse.project.dao.mybatis;

import java.util.List;

public interface CompanyInfoMapper {

	/**
	* @author author
	* @Description 根据id查询公司名称
	* @Date 2020/9/3
	* @param companyId
	* @return java.lang.String
	**/
	String selectCompanyNameById(Long companyId);

	/**
	* @author author
	* @Description 查询公司名称
	* @Date 2020/9/3
	* @param 
	* @return java.util.List<java.lang.String>
	**/
	List<String> selectCompanyName();

	/**
	* @author author
	* @Description 根据名称查询id
	* @Date 2020/9/3
	* @param companyName
	* @return java.util.List<java.lang.Long>
	**/
	List<Long> selectCompanyId(String companyName);

	
}
