package cn.pioneeruniverse.common.Rest;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.dev.entity.TblCommissioningWindow;
import cn.pioneeruniverse.dev.entity.TblRequirementInfo;
import cn.pioneeruniverse.dev.entity.TblSystemInfo;
import cn.pioneeruniverse.dev.service.defect.test.DefectService;
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

    @RequestMapping(value="getAllComWindow",method = RequestMethod.POST)
    public Map<String, Object> getAllComWindow(TblCommissioningWindow window, Integer pageNumber, Integer pageSize){
        Map<String, Object> result = new HashMap<>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            result = defectService.getAllComWindow(window, pageNumber, pageSize);
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
    /*@RequestMapping(value="getAllReq",method = RequestMethod.POST)
    public Map<String, Object> getAllReq(TblRequirementInfo requirementInfo, Integer pageNumber, Integer pageSize){
        Map<String, Object> result = new HashMap<>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            List<TblRequirementInfo> list = defectService.getAllRequirement(requirementInfo, pageNumber, pageSize);
            List<TblRequirementInfo> list2 = defectService.getAllRequirement(requirementInfo, null, null);
            result.put("rows",list );
            result.put("total", list2.size());
        } catch (Exception e) {
            return handleException(e, "获取系统信息失败");
        }
        return result;
    }*/


    /**
     * 查询所有的系统
     * @param systemInfo
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @RequestMapping(value="selectAllSystemInfo",method = RequestMethod.POST)
    public Map<String, Object> selectAllSystemInfo(TblSystemInfo systemInfo, Integer pageNumber, Integer pageSize,HttpServletRequest request){
        Map<String, Object> result = new HashMap<>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            result = defectService.getAllSystemInfo(systemInfo, pageNumber, pageSize,request);
        } catch (Exception e) {
            return handleException(e, "获取系统信息失败");
        }
        return result;
    }
}
