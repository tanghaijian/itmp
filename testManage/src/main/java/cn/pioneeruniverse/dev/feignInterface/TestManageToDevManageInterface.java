package cn.pioneeruniverse.dev.feignInterface;


import cn.pioneeruniverse.dev.feignFallback.TestManageToDevManageFallback;

import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author:
 * @Description:
 * @Date: Created in 15:52 2019/1/17
 * @Modified By:
 */
@FeignClient(value = "devManage", fallbackFactory = TestManageToDevManageFallback.class)
public interface TestManageToDevManageInterface {

    /**
    *@author author
    *@Description 同步缺陷数据
    *@Date 2020/8/11
     * @param objectJson
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @RequestMapping(value = "defect/syncDefect", method = RequestMethod.POST)
    Map<String, Object> syncDefect(@RequestBody String objectJson) throws Exception;

    /**
    *@author author
    *@Description 同步缺陷附件实体类
    *@Date 2020/8/11
     * @param objectJson
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @RequestMapping(value = "defect/syncDefectAtt", method = RequestMethod.POST)
    Map<String, Object> syncDefectAtt(@RequestBody String objectJson) throws Exception;
    
   /* @RequestMapping(value="systeminfo/findSystemIdByUserId",method = RequestMethod.POST)
    List<Long> findSystemIdByUserId(@RequestParam Long id);*/

    @RequestMapping(value = "devtask/synReqFeatureDeployStatus", method = RequestMethod.POST)
    Map<String, Object> synReqFeatureDeployStatus(@RequestParam("requirementId") Long requirementId, @RequestParam("systemId") Long systemId, @RequestParam("deployStatus") String deployStatus, @RequestParam("loginfo") String loginfo);

    @RequestMapping(value = "devtask/updateReqFeatureTimeTrace", method = RequestMethod.POST)
    Map<String, Object> updateReqFeatureTimeTrace(@RequestParam("jsonString") String jsonString);

    /**
    *@author author
    *@Description 同步缺陷和附件
    *@Date 2020/8/11
     * @param json
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @RequestMapping(value = "defect/syncDefectWithFiles", method = RequestMethod.POST)
    Map<String, Object> syncDefectWithFiles(@RequestBody String json);

    @RequestMapping(value = "devtask/synReqFeaturewindow", method = RequestMethod.POST)
    Map<String, Object> synReqFeaturewindow(@RequestParam("requirementId") Long requirementId, @RequestParam("systemId") Long systemId, @RequestParam("commissioningWindowId") Long commissioningWindowId, @RequestParam("loginfo") String loginfo, @RequestParam("beforeName") String beforeName, @RequestParam("afterName") String afterName);

    @RequestMapping(value = "devtask/synReqFeatureDept", method = RequestMethod.POST)
    Map<String, Object> synReqFeatureDept(@RequestParam("requirementId") Long requirementId, @RequestParam("systemId") Long systemId, @RequestParam("deptId") Long deptId, @RequestParam("loginfo") String loginfo, @RequestParam("deptBeforeName") String deptBeforeName,
                                          @RequestParam("deptAfterName") String deptAfterName);

    @RequestMapping(value = "devtask/getDevTaskBySystemAndRequirement", method = RequestMethod.POST)
    Map<String, Object> getDevTaskBySystemAndRequirement(@RequestParam("systemId") Long systemId, @RequestParam("requirementId") Long requirementId);

    @RequestMapping(value = "/systemVersion/getSystemVersionBySystemVersionId", method = RequestMethod.POST)
    Map<String, Object> getSystemVersionBySystemVersionId(@RequestParam("systemVersionId") Long systemVersionId);
    @RequestMapping(value = "/sprintManage/updateSprintWorkLoad", method = RequestMethod.POST)
    void updateSprintWorkLoad(@RequestParam("param") String param);
    @RequestMapping(value = "/devtask/getRequireFeature", method = RequestMethod.POST)
    Map<String, Object> getRequireFeature(@RequestParam("code") String code,@RequestParam("requireFeatureId")Long requireFeatureId);

    @RequestMapping(value="/devtask/getProjectPlanTree",method=RequestMethod.POST)
    Map<String, Object> getProjectPlanTree(@RequestParam("projectId")Long projectId);
}
