package cn.pioneeruniverse.job.component;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * 
* @ClassName: Myjob
* @Description: 测试，无用
* @author author
* @date 2020年8月24日 上午9:44:09
*
 */
@Component
public class Myjob implements Job{
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("execute my job");
	}


}
