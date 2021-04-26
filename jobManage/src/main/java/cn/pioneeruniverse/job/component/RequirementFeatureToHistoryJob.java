package cn.pioneeruniverse.job.component;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.pioneeruniverse.job.feignInterface.JobManageToProjectManageInterface;
import cn.pioneeruniverse.job.model.TaskInfo;

/**
 * 
* @ClassName: RequirementFeatureToHistoryJob
* @Description: 开发任务写入历史job，燃尽图使用
* @author author
* @date 2020年8月24日 上午9:43:38
*
 */
@Component
public class RequirementFeatureToHistoryJob implements Job {

	@Autowired
	private JobManageToProjectManageInterface projectManageInterface;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String parameterJson = TaskInfo.getParameterJson();
		projectManageInterface.executeFeatureToHistoryJob(parameterJson);
	}

}
