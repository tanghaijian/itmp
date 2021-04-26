package cn.pioneeruniverse.project.feignInterface;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.pioneeruniverse.project.feignFallback.DevTaskFallback;

import java.util.List;
import java.util.Map;

/**
 * 
* @ClassName: DevTaskInterface
* @Description: 项目和开发模块调用接口
* @author author
* @date 2020年8月13日 上午10:14:25
*
 */
@FeignClient(value = "devManage", fallbackFactory = DevTaskFallback.class)
public interface DevTaskInterface {
	/**
	 * 
	* @Title: changeCancelStatus
	* @Description: 需求取消时，取消开发任务状态
	* @author author
	* @param requirementId 需求ID
	* @return map key :status=1正常，status=2异常
	* @throws
	 */
    @RequestMapping(value = "/devtask/cancelStatus", method = RequestMethod.POST)
    Map<String, Object> changeCancelStatus(@RequestParam("requirementId") Long requirementId);

    /**
     * 
    * @Title: cancelStatusReqFeature
    * @Description: 开发任务取消，开发工作任务也取消
    * @author author
    * @param reqFeatureId 开发任务ID
    * @return map key :status=1正常，status=2异常
    * @throws
     */
    @RequestMapping(value = "/devtask/cancelStatusReqFeature", method = RequestMethod.POST)
    Map<String, Object> cancelStatusReqFeature(@RequestParam("reqFeatureId") Long reqFeatureId);

    /**
     * 
    * @Title: getCodeFilesByDevTaskId 
    * @Description: 获取某个工作任务下提交的代码文件(未用)
    * @author author
    * @param devTaskId
    * @return key svnFiles:svn中的文件，gitFiles：git中的文件
    * @throws
     */
    @RequestMapping(value = "/version/getCodeFilesByDevTaskId", method = RequestMethod.POST)
    Map<String, Object> getCodeFilesByDevTaskId(@RequestParam("devTaskId") Long devTaskId);

    /**
     * 
    * @Title: getSystemNameById
    * @Description: 根据ID获取系统名称
    * @author author
    * @param systemId 系统ID
    * @return String 系统名称
    * @throws
     */
    @RequestMapping(value = "/systeminfo/getSystemNameById", method = RequestMethod.POST)
    String getSystemNameById(@RequestParam("systemId") Long systemId);

    /**
     * 
    * @Title: getMyProjectSystems
    * @Description: 获取项目关联的系统
    * @author author
    * @param token
    * @return map key id:系统ID ，systemName：系统名称
    * @throws
     */
    @RequestMapping(value = "/version/getMyProjectSystems", method = RequestMethod.POST)
    List<Map<String, Object>> getMyProjectSystems(@RequestParam("token") String token);

    /**
     * 
    * @Title: getNewFeatureCode
    * @Description: 获取开发任务编码
    * @author author
    * @return 编码
    * @throws
     */
    @RequestMapping(value = "devtask/getNewFeatureCode",method=RequestMethod.POST)
    String getNewFeatureCode();
}
