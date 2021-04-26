package cn.pioneeruniverse.project.feignInterface;

import cn.pioneeruniverse.project.feignFallback.TestTaskFallback;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.Map;

/**
 * 
* @ClassName: TestTaskInterface
* @Description: 项目管理与测试管理关联调用
* @author author
* @date 2020年8月13日 下午8:59:15
*
 */
@FeignClient(value = "testManage", fallbackFactory = TestTaskFallback.class)
public interface TestTaskInterface {

	/**
	 * 
	* @Title: changeCancelStatus
	* @Description: 取消测试任务
	* @author author
	* @param requirementId 需求ID
	* @return map key:status =1正常，status=2异常
	* @throws
	 */
    @RequestMapping(value = "/testtask/cancelStatus", method = RequestMethod.POST)
    Map<String,Object> changeCancelStatus(@RequestParam("requirementId") Long requirementId);

    /**
     * 
    * @Title: cancelStatusReqFeature
    * @Description: 取消测试工作任务
    * @author author
    * @param reqFeatureId 测试任务ID
    * @return map key:status =1正常，status=2异常
    * @throws
     */
    @RequestMapping(value = "/testtask/cancelStatusReqFeature", method = RequestMethod.POST)
    Map<String,Object> cancelStatusReqFeature(@RequestParam("reqFeatureId") Long reqFeatureId);

    /**
     * 
    * @Title: getWorkLoadByFeIds
    * @Description: 获取所选的开发任务剩余工作量总和，燃尽图跑批
    * @author author
    * @param reqFeatureIds 开发任务IDS
    * @return map estimateRemainWorkload 剩余工作量总和  status=1正常，status=2异常
    * @throws
     */
    @RequestMapping(value = "/testtask/getWorkLoadByFeIds", method = RequestMethod.POST)
    Map<String,Object> getWorkLoadByFeIds(@RequestParam("reqFeatureIds") String reqFeatureIds);

    /**
     * 
    * @Title: getRequirementFeatureNameById
    * @Description: 获取测试测任务名，风险管理使用
    * @author author
    * @param id 测试任务ID
    * @return map featureName:测试任务名  featureManageUserId：测试管理岗人员  status =1正常，2异常
    * @throws
     */
    @RequestMapping(value = "/testtask/getRequirementFeatureNameById", method = RequestMethod.POST)
    Map<String,Object> getRequirementFeatureNameById(@RequestParam("id") Long id);

    /**
     * 
    * @Title: getNewFeatureCode
    * @Description: 获取测试任务编号
    * @author author
    * @return
    * @throws
     */
    @RequestMapping(value = "testtask/getNewFeatureCode",method=RequestMethod.POST)
    String getNewFeatureCode();
}
