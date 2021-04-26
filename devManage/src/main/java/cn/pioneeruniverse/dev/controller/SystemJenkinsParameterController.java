package cn.pioneeruniverse.dev.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.dev.entity.TblSystemJenkinsParameter;
import cn.pioneeruniverse.dev.service.systemJenkinsParameter.SystemJenkinsParameterService;
/**
 *
 * @ClassName: SystemJenkinsParameterController
 * @Description: 手动参数控制类
 * @author author
 *
 */
@RestController
@RequestMapping("systemJenkinsParameter")
public class SystemJenkinsParameterController extends BaseController{
	@Autowired
	private SystemJenkinsParameterService systemJenkinsParameterService;
	private final static Logger log = LoggerFactory.getLogger(SystemJenkinsParameterController.class);

	/**
	 *  获取参数名称
	 * @param systemJenkinsParameter
	 * @param rows
	 * @param page
	 * @return  Map<String, Object> status=1正常返回，2异常返回
	 */
	@PostMapping("getSystemJenkinsParameterList")
	public Map<String, Object> getSystemJenkinsParameterList(TblSystemJenkinsParameter systemJenkinsParameter,Integer rows,Integer page) {
		Map<String, Object> result = new HashMap<>();
		try {
			result = systemJenkinsParameterService.getSystemJenkinsParameterList(systemJenkinsParameter, rows, page);
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result = super.handleException(e, e.getMessage());
			result.put("status", 2);
		}
		return result;
	}

	/**
	 *  新建注册任务参数化配置
	 * @param systemJenkinsParameter
	 * @param systemJenkinsParameterValues
	 * @param request
	 * @return Map<String, Object> status=1正常返回，2异常返回
	 */
	@PostMapping("addSystemJenkinsParameter")
	public Map<String, Object>  addSystemJenkinsParameter(TblSystemJenkinsParameter systemJenkinsParameter,String systemJenkinsParameterValues,HttpServletRequest request){
		Map<String, Object> result = new HashMap<>();
		try {
			systemJenkinsParameterService.addSystemJenkinsParameter(systemJenkinsParameter, systemJenkinsParameterValues, request);
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result = super.handleException(e, e.getMessage());
		}
		return result;
		
	}

	/**
	 *  编辑参数化
	 * @param systemJenkinsParameter
	 * @param systemJenkinsParameterValues
	 * @param deleteIds 删除id
	 * @param request
	 * @return Map<String, Object> status=1正常返回，2异常返回
	 */
	
	@PostMapping("editSystemJenkinsParameter")
	public Map<String, Object>  editSystemJenkinsParameter(TblSystemJenkinsParameter systemJenkinsParameter,String systemJenkinsParameterValues,String deleteIds,HttpServletRequest request){
		Map<String, Object> result = new HashMap<>();
		try {
			systemJenkinsParameterService.editSystemJenkinsParameter(systemJenkinsParameter, systemJenkinsParameterValues,deleteIds, request);
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result = super.handleException(e, e.getMessage());
		}
		return result;
		
	}

	/**
	 *  查询参数化
	 * @param systemJenkinsParameterId 主键
	 * @param request
	 * @return Map<String, Object> status=1正常返回，2异常返回
	 */
	@PostMapping("selectSystemJenkinsParameterById")
	public Map<String, Object>  selectSystemJenkinsParameterById( Long systemJenkinsParameterId ,HttpServletRequest request){
		Map<String, Object> result = new HashMap<>();
		try {
			TblSystemJenkinsParameter data=systemJenkinsParameterService.selectSystemJenkinsParameterById(systemJenkinsParameterId,request);
			//返回的数据
			result.put("data", data);
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result = super.handleException(e, e.getMessage());
		}
		return result;
		
	}
	
	/**
	 * 
	* @Title: systemSystemName
	* @Description: 通过系统名获取系统id
	* @author author
	* @param systemName
	* @return List<Map<String,Object>> key id:系统id systemName：系统名
	 */
	@PostMapping("systemSystemName")
	public List<Map<String, Object>>   systemSystemName( String systemName){
		return	systemJenkinsParameterService.systemSystemName(systemName);
		
	}

	/**
	 *  查询参数 以树的结构显示
	 * @param systemId 系统id
	 * @param request
	 * @return  Map<String, Object> status=1正常返回，2异常返回
	 */
	
	@PostMapping("selectJenkinsTree")
	public Map<String, Object>   selectJenkinsTree( Long systemId,HttpServletRequest request){
		Map<String, Object> result;
		try {
			result = systemJenkinsParameterService.selectJenkinsTree(systemId,request);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			throw e;

		}
		return	result;
		
	}
	
}
