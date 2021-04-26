package cn.pioneeruniverse.job.feignFallback;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.pioneeruniverse.job.feignInterface.JobManageToProjectManageInterface;
import feign.hystrix.FallbackFactory;

@Component
public class JobManageToProjectManageFallback implements FallbackFactory<JobManageToProjectManageInterface> {

	private static final Logger logger = LoggerFactory.getLogger(JobManageToProjectManageFallback.class);

	/**
	* @author author
	* @Description 降级处理
	* @Date 2020/9/14
	* @param throwable
	* @return cn.pioneeruniverse.job.feignInterface.JobManageToProjectManageInterface
	**/
	@Override
	public JobManageToProjectManageInterface create(Throwable throwable) {
		return new JobManageToProjectManageInterface() {
			@Override
			public Map<String, Object> executeFeatureToHistoryJob(String parameterJson) {
				logger.error(throwable.getMessage(), throwable.getCause());
				return null;
			}

		};
	}
}
