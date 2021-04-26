	
package cn.pioneeruniverse.dev.dao.mybatis;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.vo.DevDetailVo;


public interface AllDevRequirementMapper extends BaseMapper<DevDetailVo> {
	/**
	 * @author author
	 * @Description 获取开发任务、需求
	 * @Date 2020/9/3
	 * @param devID
	 * @return cn.pioneeruniverse.dev.entity.DevDetailVo
	 **/
 	DevDetailVo AlldevReuirement(Long devID);

	/**
	 * 获取名称
	 * @param devuserID
	 * @return String
	 */
 	String getdevName(Long devuserID);
	/**
	 * 获取系统名称
	 * @param system
	 * @return String
	 */
 	String getSystemName(Long system);
 	/**
 	* @author author
 	* @Description 获取部门名称
 	* @Date 2020/9/3
 	* @param deptID
 	* @return java.lang.String
 	**/
 	String getdeptName(Long deptID);
}
