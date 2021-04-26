package cn.pioneeruniverse.dev.common;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.dev.entity.*;
import cn.pioneeruniverse.dev.service.defect.DefectService;
import cn.pioneeruniverse.dev.service.testExecute.testExecuteService;
import cn.pioneeruniverse.dev.service.workTask.WorkTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 其他服务的弹框controller
 * Author:liushan
 * Date: 2018/12/24 下午 3:47
 */
@RestController
@RequestMapping(value = "modal")
public class ModalController extends BaseController {

    @Autowired
    private DefectService defectService;
    @Autowired
    private WorkTaskService  workTaskService;
    @Autowired
    private testExecuteService testExecuteService;

    @RequestMapping(value="getAllProject",method = RequestMethod.POST)
    public Map<String,Object> getAllProject(HttpServletRequest request){
        Map<String, Object> result = new HashMap<>();
        try {
            result = defectService.getAllProject(request);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            return handleException(e, "获取系统信息失败");
        }
        return result;
    }

    @RequestMapping(value="getAllComWindow",method = RequestMethod.POST)
    public Map<String, Object> getAllComWindow(TblCommissioningWindow window, Integer pageNumber, Integer pageSize){
        Map<String, Object> result = new HashMap<>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            result = defectService.getAllComWindow(window, pageNumber, pageSize);;
        } catch (Exception e) {
            return handleException(e, "获取系统信息失败");
        }
        return result;
    }

    /**
     * 查询所有的工作任务
     * @param testTask
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @RequestMapping(value="getAllTestTask",method = RequestMethod.POST)
    public Map<String, Object> getAllTestTask(TblTestTask testTask, Integer pageNumber, Integer pageSize, HttpServletRequest request){
        Map<String, Object> result = new HashMap<>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            result = defectService.getAllTestTask(testTask, pageNumber, pageSize,request);
        } catch (Exception e) {
            return handleException(e, "获取系统信息失败");
        }
        return result;
    }

    /**
     * 查询所有的需求
     * @param requirementInfo
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @RequestMapping(value="getAllRequirement",method = RequestMethod.POST)
    public Map<String, Object> getAllReq(TblRequirementInfo requirementInfo, Integer pageNumber, Integer pageSize){
        Map<String, Object> result = new HashMap<>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            result = defectService.getAllRequirement(requirementInfo, pageNumber, pageSize);
        } catch (Exception e) {
            return handleException(e, "获取系统信息失败");
        }
        return result;
    }



    /**
     * 查询所有的系统
     * @param systemInfo
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @RequestMapping(value="selectAllSystemInfo",method = RequestMethod.POST)
    public Map<String, Object> selectAllSystemInfo(TblSystemInfo systemInfo, Integer pageNumber, Integer pageSize){
        Map<String, Object> result = new HashMap<>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            result = defectService.getAllSystemInfo(systemInfo, pageNumber, pageSize);
        } catch (Exception e) {
            return handleException(e, "获取系统信息失败");
        }
        return result;
    }

   /* @RequestMapping(value="getAllCaseExceution",method = RequestMethod.POST)
    public Map<String,Object> getAllCaseExceution(TblTestSetCaseExecute caseExecute, Integer pageNumber, Integer pageSize,HttpServletRequest request){
        Map<String, Object> result = new HashMap<>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            result = testExecuteService.getAllCaseExceution(caseExecute,pageNumber,pageSize,request);
        } catch (Exception e) {
            return handleException(e, "获取案例信息失败");
        }
        return result;
    }*/
    
    /**
     * 获取系统下的项目
     * @param systemId
     * @return
     */
    @RequestMapping(value="getProject",method=RequestMethod.POST)
    public Map<String, Object> getProject(Long systemId){
    	Map<String,Object> map = new HashMap<>();
    	map.put("status", Constants.ITMP_RETURN_SUCCESS);
    	try {
			map.put("project", defectService.getProject(systemId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return handleException(e, "获取项目失败");
		}
		return map;
    }
}
