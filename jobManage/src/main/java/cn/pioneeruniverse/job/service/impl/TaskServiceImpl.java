package cn.pioneeruniverse.job.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.pioneeruniverse.job.model.TaskInfo;
import cn.pioneeruniverse.job.service.ITaskService;

@Service
public class TaskServiceImpl implements ITaskService {

	@Autowired
	private Scheduler scheduler;
	private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

	/**
	 * 
	* @Title: queryJobList
	* @Description: 获取所有定时任务
	* @author author
	* @return
	* @throws Exception
	* @throws
	 */
	public List<TaskInfo> queryJobList() throws Exception {
		log.info("TaskService--data-s-->queryJobList()");
		List<TaskInfo> list = new ArrayList<>();
		for (String groupJob : scheduler.getJobGroupNames()) {
			for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.<JobKey>groupEquals(groupJob))) {
				List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
				for (Trigger trigger : triggers) {
					Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
					JobDetail jobDetail = scheduler.getJobDetail(jobKey);
					String cronExpression = "";
					String createTime = "";
					Long milliSeconds = 0l;
					Integer repeatCount = 0;
					Date startDate = null;
					Date endDate = null;
					if (trigger instanceof CronTrigger) {
						CronTrigger cronTrigger = (CronTrigger) trigger;
						cronExpression = cronTrigger.getCronExpression();
						createTime = cronTrigger.getDescription();
					} else if (trigger instanceof SimpleTrigger) {
						SimpleTrigger simpleTrigger = (SimpleTrigger) trigger;
						milliSeconds = simpleTrigger.getRepeatInterval();
						repeatCount = simpleTrigger.getRepeatCount();
						startDate = simpleTrigger.getStartTime();
						endDate = simpleTrigger.getEndTime();
					}
					TaskInfo info = new TaskInfo();
					info.setJobName(jobKey.getName());
					info.setClassName(jobDetail.getJobClass().getName());
					info.setJobGroup(jobKey.getGroup());
					info.setJobDescription(jobDetail.getDescription());
					info.setJobStatus(triggerState.name());
					info.setCronExpression(cronExpression);
					info.setCreateTime(createTime);

					info.setRepeatCount(repeatCount);
					info.setStartDate(startDate);
					info.setMilliSeconds(milliSeconds);
					info.setEndDate(endDate);
					list.add(info);
				}
			}
		}
		log.info("任务的数量为：---------------->" + list.size());

		return list;
	}

	/**
	 * 
	* @Title: setSimpleTriggerJob
	* @Description: 以simple模式设置定时任务
	* @author author
	* @param info 定时任务信息
	* @throws Exception
	* @throws
	 */
	@SuppressWarnings({ "unchecked" })
	public void setSimpleTriggerJob(TaskInfo info) throws Exception {
		log.info("TaskService--data-s-->setSimpleTriggerJob()" + info);
		String jobName = info.getJobName();
		String className = info.getClassName();
		String jobGroup = info.getJobGroup();
		String jobDescription = info.getJobDescription();
		Long milliSeconds = info.getMilliSeconds();// 多久触发一次
		Integer repeatCount = info.getRepeatCount();// 总共触发多少次
		Date startDate = info.getStartDate();
		Date endDate = info.getEndDate();

		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);// 触发器的key值
		JobKey jobKey = JobKey.jobKey(jobName, jobGroup);// job的key值
		if (checkExists(jobName, jobGroup)) {
			log.error("===> 简单调度任务失败，任务已存在, jobGroup:{}, jobName:{}", jobGroup, jobName);
			throw new Exception(String.format("Job已经存在, jobName:{%s},jobGroup:{%s}", jobName, jobGroup));
		}
		/* 简单调度 */
		SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder
				.newTrigger().withIdentity(triggerKey).startAt(startDate).withSchedule(SimpleScheduleBuilder
						.simpleSchedule().withIntervalInMilliseconds(milliSeconds).withRepeatCount(repeatCount))
				.endAt(endDate).build();
		Class<? extends Job> clazz = (Class<? extends Job>) Class.forName(className);
		JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobKey).withDescription(jobDescription).build();
		scheduler.scheduleJob(jobDetail, trigger);

	}

	/**
	 * 
	* @Title: addJob
	* @Description: 以cron形式新增定时任务
	* @author author
	* @param info 定时任务信息
	* @throws Exception
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void addJob(TaskInfo info) throws Exception {
		log.info("TaskService--data-s-->addJob()" + info);
		String jobName = info.getJobName();
		String className = info.getClassName();
		String jobGroup = info.getJobGroup();
		String cronExpression = info.getCronExpression();
		String jobDescription = info.getJobDescription();
		if (checkExists(jobName, jobGroup)) {
			log.error("===> 添加任务失败，任务已存在, jobGroup:{}, jobName:{}", jobGroup, jobName);
			throw new Exception(String.format("Job已经存在, jobName:{%s},jobGroup:{%s}", jobName, jobGroup));
		}

		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
		JobKey jobKey = JobKey.jobKey(jobName, jobGroup);

		CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule(cronExpression)
				.withMisfireHandlingInstructionDoNothing();
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withDescription(jobDescription)
				.withSchedule(schedBuilder).build();

		Class<? extends Job> clazz = (Class<? extends Job>) Class.forName(className);
		JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobKey).withDescription(jobDescription).build();
		scheduler.scheduleJob(jobDetail, trigger);
	}

	/**
	 * 
	* @Title: editJob
	* @Description: 编辑定时任务
	* @author author
	* @param info
	* @throws Exception
	* @throws
	 */
	public void editJob(TaskInfo info) throws Exception {
		log.info("TaskService--data-s-->editJob()" + info);
		String jobName = info.getJobName();
		String jobGroup = info.getJobGroup();
		String cronExpression = info.getCronExpression();
		String	jobDescription = info.getJobDescription();
		if (!checkExists(jobName, jobGroup)) {
			throw new Exception(String.format("Job不存在, jobName:{%s},jobGroup:{%s}", jobName, jobGroup));
		}
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
		JobKey jobKey = new JobKey(jobName, jobGroup);
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression)
				.withMisfireHandlingInstructionDoNothing();
		CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withDescription(jobDescription)
				.withSchedule(cronScheduleBuilder).build();

		JobDetail jobDetail = scheduler.getJobDetail(jobKey);
		jobDetail.getJobBuilder().withDescription(jobDescription);
		HashSet<Trigger> triggerSet = new HashSet<>();
		triggerSet.add(cronTrigger);
		scheduler.scheduleJob(jobDetail, triggerSet, true);
	}

	/**
	 * 删除任务
	 */
	public void deleteJob(TaskInfo info) throws Exception {
		String jobName = info.getJobName();
		String jobGroup = info.getJobGroup();
		log.info("TaskService--data-s-->deleteJob()--jobName:" + jobName + "jobGroup:" + jobGroup);
		if (checkExists(jobName, jobGroup)) {
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			scheduler.pauseTrigger(triggerKey);
			scheduler.unscheduleJob(triggerKey);
			log.info("===> delete, triggerKey:{}", triggerKey);
		}
	}

	/**
	 * 暂停任务
	 */
	public void pauseJob(TaskInfo info) throws Exception {
		String jobName = info.getJobName();
		String jobGroup = info.getJobGroup();
		log.info("TaskService--data-s-->pauseJob()--jobName:" + jobName + "jobGroup:" + jobGroup);
		if (checkExists(jobName, jobGroup)) {
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			scheduler.pauseTrigger(triggerKey);
			log.info("===> Pause success, triggerKey:{}", triggerKey);
		}

	}

	/**
	 * 恢复任务
	 */
	public void resumeJob(TaskInfo info) throws Exception {
		String jobName = info.getJobName();
		String jobGroup = info.getJobGroup();
		log.info("TaskService--data-s-->resumeJob()--jobName:" + jobName + "jobGroup:" + jobGroup);
		try {
			if (checkExists(jobName, jobGroup)) {
				TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
				scheduler.resumeTrigger(triggerKey);
				log.info("===> Resume success, triggerKey:{}", triggerKey);
			}
		} catch (SchedulerException e) {
			log.info("重新开始任务-->复杂调度" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 检查任务是否 存在
	 * @param jobName
	 * @param jobGroup
	 * @return
	 * @throws Exception
	 */
	public boolean checkExists(TaskInfo info) throws Exception {
		String jobName = info.getJobName();
		String jobGroup = info.getJobGroup();
		return checkExists(jobName,jobGroup);
	}
	private boolean checkExists(String jobName, String jobGroup) throws Exception {
		log.info("TaskService--data-s-->checkExists()--jobName:" + jobName + "jobGroup:" + jobGroup);
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
		return scheduler.checkExists(triggerKey);
	}

	/**
	 * 
	* @Title: startJob
	* @Description: 手动执行一次定时任务
	* @author author
	* @param info
	* @throws Exception
	* @throws
	 */
	@Override
	public void startJob(TaskInfo info) throws Exception {
		String jobName = info.getJobName();
		String jobGroup = info.getJobGroup();
		log.info("TaskService--data-s-->startJob()--jobName:" + jobName + "jobGroup:" + jobGroup);
		try {
			if (checkExists(jobName, jobGroup)) {
				JobKey jobKey = new JobKey(jobName, jobGroup);
				scheduler.triggerJob(jobKey);
				log.info("===> startjob success, jobkey:{}", jobKey);
			}
		} catch (SchedulerException e) {
			log.info("重新开始任务-->复杂调度" + e.getMessage());
			e.printStackTrace();
		}
	}
	

}
