package cn.pioneeruniverse.job.component;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.pioneeruniverse.job.feignInterface.JobManageToSystemInterface;

/**
 * 
* @ClassName: EmailAndWeChatJob
* @Description: 邮件和微信定时任务Job
* @author author
* @date 2020年8月24日 上午9:44:27
*
 */
@Component
@DisallowConcurrentExecution
public class EmailAndWeChatJob implements Job {

	@Autowired
	private JobManageToSystemInterface systemInterface;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		systemInterface.sendMessageJob();
	}

}
