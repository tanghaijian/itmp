package cn.pioneeruniverse.job.feignInterface;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.pioneeruniverse.job.feignFallback.JobManageToProjectManageFallback;

/**
 * 
* @ClassName: JobManageToProjectManageInterface
* @Description: 定时任务与项目模块之间的接口调用
* @author author
* @date 2020年8月24日 下午2:52:27
*
 */
@FeignClient(value = "projectManage", fallbackFactory = JobManageToProjectManageFallback.class)
public interface JobManageToProjectManageInterface {

	/**
	 * 
	* @Title: executeFeatureToHistoryJob
	* @Description: 定时将开发任务写入开发任务历史当中，统计燃尽图时使用
	* @author author
	* @param parameterJson 开发任务信息
	* @return
	* @throws
	 */
	@RequestMapping(value = "/requirementFeature/executeFeatureToHistoryJob", method = RequestMethod.POST)
	Map<String, Object> executeFeatureToHistoryJob(@RequestBody String parameterJson);

}
