package cn.pioneeruniverse.dev.controller.defect;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.dev.service.defect.dev.DevDefectService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * Description: 开发管理系统代码
 * 缺陷管理后端controller
 * Author:liushan
 * Date: 2018/12/10 下午 1:41
 */
@RestController
@RequestMapping(value = "defect")
public class DevDefectController extends BaseController {

    @Autowired
    private DevDefectService devDefectService;

    /**
     * 同步数据
     * @param objectJson 缺陷数据 废弃
     * @return map key status:1正常返回，2异常返回
     */
    @RequestMapping(value = "syncDefect", method = RequestMethod.POST)
    public Map<String,Object> syncDefect(@RequestBody  String objectJson){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            if (StringUtils.isNotBlank(objectJson)) {
                devDefectService.syncDefect(objectJson);
            }
        } catch (Exception e) {
            return handleException(e, "同步数据信息失败");
        }
        return result;
    }

    /**
     * 同步缺陷附件
     * @param objectJson 附件数据
     * @return Map<String,Object>  key status:1正常返回，2异常返回
     */
    @RequestMapping(value = "syncDefectAtt", method = RequestMethod.POST)
    public Map<String,Object> syncDefectAtt(@RequestBody  String objectJson){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            if (StringUtils.isNotBlank(objectJson)) {
                devDefectService.syncDefectAtt(objectJson);
            }
        } catch (Exception e) {
            return handleException(e, "同步缺陷附件失败");
        }
        return result;
    }

    /**
     * 修改开发管理平台的缺陷数据，新建的工作任务或开发任务
     * @param objectJson
     * @return Map<String,Object>  key status:1正常返回，2异常返回
     */
    @RequestMapping(value = "updateDevDefect", method = RequestMethod.POST)
    public Map<String,Object> updateDevDefect(@RequestBody  String objectJson){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            if (StringUtils.isNotBlank(objectJson)) {
                devDefectService.updateDevDefect(objectJson);
            }
        } catch (Exception e) {
            return handleException(e, "修改缺陷失败");
        }
        return result;
    }

    /**
     * 
    * @Title: syncDefectWithFiles
    * @Description: 同步缺陷附件
    * @author author
    * @param objectJson  缺陷和附件map的json字符串格式
    * @return Map<String,Object>  key status:1正常返回，2异常返回
     */
    @RequestMapping(value = "syncDefectWithFiles", method = RequestMethod.POST)
    public Map<String,Object> syncDefectWithFiles(@RequestBody  String objectJson){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            if (StringUtils.isNotBlank(objectJson)) {
                devDefectService.syncDefectWithFiles(objectJson);
            }
        } catch (Exception e) {
            return handleException(e, "操作失败");
        }
        return result;
    }

}


