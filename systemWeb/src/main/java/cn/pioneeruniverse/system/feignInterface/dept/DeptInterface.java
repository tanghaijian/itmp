package cn.pioneeruniverse.system.feignInterface.dept;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.pioneeruniverse.system.feignFallback.dept.DeptFallback;
import cn.pioneeruniverse.system.vo.dept.TblDeptInfo;


/**
 * @deprecated
* @ClassName: DeptInterface
* @Description: 
* @author author
* @date 2020年9月4日 上午9:58:59
*
 */
@FeignClient(value="system",fallbackFactory=DeptFallback.class)
public interface DeptInterface {

    /**
    * @author author
    * @Description 查询所有部门
    * @Date 2020/9/7
    * @param dept
    * @return java.util.Map<java.lang.String,java.lang.Object>
    **/
	@RequestMapping(value="dept/getAllDept",method=RequestMethod.POST)
    Map<String,Object> getAllDept(@RequestBody TblDeptInfo dept);

	
	/**
	* @author author
	* @Description 根据id查询部门
	* @Date 2020/9/7
	* @param id
	* @return java.util.Map<java.lang.String,java.lang.Object>
	**/
	@RequestMapping(value="dept/selectDeptById",method=RequestMethod.POST)
    Map<String,Object> selectDeptById(@RequestParam("id") Long id);

	/**
	* @author author
	* @Description 根据父级查询部门
	* @Date 2020/9/7
	* @param id
	* @return java.util.Map<java.lang.String,java.lang.Object>
	**/
	@RequestMapping(value="dept/selectDeptByParentId",method=RequestMethod.POST)
    Map<String,Object> selectDeptByParentId(@RequestParam("id") Long id) ;

	/**
	* @author author
	* @Description 新增部门
	* @Date 2020/9/7
	* @param dept
	* @return java.util.Map<java.lang.String,java.lang.Object>
	**/
	@RequestMapping(value="dept/insertDept",method=RequestMethod.POST)
    Map<String,Object> insertDept(@RequestBody TblDeptInfo dept);

	/**
	* @author author
	* @Description 编辑部门
	* @Date 2020/9/7
	* @param dept
	* @return java.util.Map<java.lang.String,java.lang.Object>
	**/
	@RequestMapping(value="dept/updateDept",method=RequestMethod.POST)
    Map<String,Object> updateDept(@RequestBody TblDeptInfo dept);
	
	/**
	* @author author
	* @Description 根据公司id查询部门
	* @Date 2020/9/7
	* @param companyId
	* @return java.util.Map<java.lang.String,java.lang.Object>
	**/
	@RequestMapping(value="dept/getDeptByCompanyId",method=RequestMethod.POST)
    Map<String,Object> getDeptByCompanyId(@RequestParam("companyId") Long companyId);
}
