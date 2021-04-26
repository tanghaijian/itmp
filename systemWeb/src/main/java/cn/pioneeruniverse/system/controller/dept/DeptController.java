package cn.pioneeruniverse.system.controller.dept;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.system.feignInterface.dept.DeptInterface;
import cn.pioneeruniverse.system.vo.dept.TblDeptInfo;
/**
 * @deprecated
 * @ClassName: DeptController
 * @Description: 部门Controller
 * @author author
 * @date 2020年8月4日 下午10:31:36
 *
 */
@RestController
@RequestMapping("dept")
public class DeptController {

	private final static Logger logger = LoggerFactory.getLogger(DeptController.class);
	
	
	@Autowired
	private  DeptInterface deptInterface;
	
	
	@RequestMapping("toDeptManage")
	public ModelAndView  toDeptManage(){
		ModelAndView view = new ModelAndView();
		view.setViewName("dept/deptInfoManage");
		return view;
	}
	
	@RequestMapping(value="getAllDept",method=RequestMethod.POST)
	public Map<String,Object> getAllDept(TblDeptInfo dept){
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			result  = deptInterface.getAllDept(dept);
		}catch(Exception e){
			return handleException(e,"获取部门失败");
		}
		return result;
	}

    @RequestMapping(value="getCompanyDept",method=RequestMethod.POST)
	public Map<String,Object> getCompanyDept(Long companyId){
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			result  = deptInterface.getDeptByCompanyId(companyId);
		}catch(Exception e){
			return handleException(e,"获取部门失败");
		}
		return result;
	}


    @RequestMapping(value="selectDeptById",method=RequestMethod.POST)
	public Map<String,Object> selectDeptById(Long id){
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			result = deptInterface.selectDeptById(id);
		}catch(Exception e){
			
			return handleException(e,"获取部门失败");
		}
		return result;
	}

	
	@RequestMapping(value="selectDeptByParentId",method=RequestMethod.POST)
	public Map<String,Object> selectDeptByParentId(Long id) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			result = deptInterface.selectDeptByParentId(id);
		}catch(Exception e){
			
			return handleException(e,"获取子部门失败");
		}
		return result;
	}

	@RequestMapping(value="insertDept",method=RequestMethod.POST)
	public Map<String,Object> insertDept(TblDeptInfo dept){
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			Long userId = 1L;//SecurityUserHolder.getCurrentUser().getId()
			dept.setCreateBy(userId);
			dept.setLastUpdateBy(userId);
			result = deptInterface.insertDept(dept);
		}catch(Exception e){
			
			return handleException(e,"新增部门失败");
		}
		return result;
	}

	@RequestMapping(value="updateDept",method=RequestMethod.POST)
	public Map<String,Object> updateDept(TblDeptInfo dept){
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			Long userId = 1L;//SecurityUserHolder.getCurrentUser().getId()
			dept.setLastUpdateBy(userId);
			result = deptInterface.updateDept(dept);
		}catch(Exception e){
			
			return handleException(e,"更新部门失败");
		}
		return result;
	}
	
	
	public  Map<String,Object> handleException(Exception e,String message){
		e.printStackTrace();
		logger.error(message+":"+e.getMessage(), e);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", Constants.ITMP_RETURN_FAILURE);
		map.put("errorMessage", message);
		return map;
	}
}
