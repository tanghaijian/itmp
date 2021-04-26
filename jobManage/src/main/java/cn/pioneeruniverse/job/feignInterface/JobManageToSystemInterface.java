package cn.pioneeruniverse.job.feignInterface;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.pioneeruniverse.job.feignFallback.JobManageToSystemFallback;
/**
 *
 * @ClassName:JobManageToSystemInterface
 * @Description:任务模块到系统模块接口类
 * @author author
 * @date 2020年8月16日
 *
 */
@FeignClient(value = "system", fallbackFactory = JobManageToSystemFallback.class)
public interface JobManageToSystemInterface {

	/**
	 * 发送消息
	 * @return Map<String, String>
	 */
	@RequestMapping(value = "/message/sendMessageJob", method = RequestMethod.POST)
	Map<String, String> sendMessageJob();

}
