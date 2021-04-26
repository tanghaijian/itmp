package cn.pioneeruniverse.dev.service.systemJenkinsParameter;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.dev.entity.TblSystemJenkinsParameter;
/**
 *
 * @ClassName: SystemJenkinsParameterService
 * @Description: 手动参数service
 * @author author
 *
 */
public interface SystemJenkinsParameterService {
	Map getSystemJenkinsParameterList(TblSystemJenkinsParameter systemJenkinsParameter,Integer rows,Integer page);

	/**
	 * 添加
	 * @param systemJenkinsParameter
	 * @param systemJenkinsParameterValues
	 * @param request
	 */
	void addSystemJenkinsParameter(TblSystemJenkinsParameter systemJenkinsParameter,String systemJenkinsParameterValues,HttpServletRequest request);

	/**
	 * 编辑
	 * @param systemJenkinsParameter
	 * @param systemJenkinsParameterValues
	 * @param deleteIds
	 * @param request
	 */
	void editSystemJenkinsParameter(TblSystemJenkinsParameter systemJenkinsParameter,String systemJenkinsParameterValues,String deleteIds,HttpServletRequest request);

	TblSystemJenkinsParameter selectSystemJenkinsParameterById(Long systemJenkinsParameterId,HttpServletRequest request);
	
	List<Map<String, Object>> systemSystemName(String systemName);
	
	Map<String, Object> selectJenkinsTree( Long systemId,HttpServletRequest request);
}
