package cn.pioneeruniverse.dev.controller.defect;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.dev.service.defect.dev.TestDefectService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Author:liushan
 * Date: 2019/1/10 上午 10:27
 */
@RestController
@RequestMapping(value = "defect")
public class TestDefectController extends BaseController {

    @Autowired
    private TestDefectService devDefectService;

    /**
    *@author author
    *@Description 添加缺陷附件
    *@Date 2020/8/20
     * @param objectJson
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @RequestMapping(value ="insertDefectAttachement",method = RequestMethod.POST)
    public  Map<String,Object> insertDefectAttachement(@RequestBody String objectJson){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            if (StringUtils.isNotBlank(objectJson)) {
                devDefectService.insertDefectAttachement(objectJson);
            }
        } catch (Exception e) {
            return handleException(e, "上传文件失败");
        }
        return result;
    }
    /**
     * 同步缺陷数据
     * @param objectJson
     * @return Map<String,Object> status=1正常返回，2异常返回
     */
    @RequestMapping(value = "syncDefect", method = RequestMethod.POST)
    public Map<String,Object> syncDefect(@RequestBody String objectJson){
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
     * @param objectJson 缺陷附件
     * @return Map<String,Object> status=1正常返回，2异常返回
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
}
